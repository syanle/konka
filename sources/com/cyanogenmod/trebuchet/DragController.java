package com.cyanogenmod.trebuchet;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Handler;
import android.os.IBinder;
import android.os.Vibrator;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.inputmethod.InputMethodManager;
import com.cyanogenmod.trebuchet.DropTarget.DragObject;
import com.cyanogenmod.trebuchet.RocketLauncher.Board;
import com.konka.ios7launcher.R;
import com.konka.kkinterface.tv.CommonDesk;
import java.util.ArrayList;
import java.util.Iterator;

public class DragController {
    private static final boolean DEBUG = false;
    public static int DRAG_ACTION_COPY = 1;
    public static int DRAG_ACTION_MOVE = 0;
    private static final boolean PROFILE_DRAWING_DURING_DRAG = false;
    private static final int SCROLL_DELAY = 600;
    static final int SCROLL_LEFT = 0;
    static final int SCROLL_NONE = -1;
    private static final int SCROLL_OUTSIDE_ZONE = 0;
    static final int SCROLL_RIGHT = 1;
    private static final int SCROLL_WAITING_IN_ZONE = 1;
    private static final String TAG = "Launcher.DragController";
    private static final int VIBRATE_DURATION = 35;
    static final int VIRTUAL_MOTION_MOVE = 1;
    static final int VIRTUAL_MOTION_MOVE_CANCEL = 3;
    static final int VIRTUAL_MOTION_MOVE_CONFIRM = 2;
    static final int VIRTUAL_MOTION_PRESS_DOWN = 0;
    private final int[] mCoordinatesTemp = new int[2];
    /* access modifiers changed from: private */
    public int mDistanceSinceScroll = 0;
    private Rect mDragLayerRect = new Rect();
    private DragObject mDragObject;
    /* access modifiers changed from: private */
    public DragScroller mDragScroller;
    private boolean mDragging;
    private ArrayList<DropTarget> mDropTargets = new ArrayList<>();
    private boolean mForceScroll = false;
    private Handler mHandler;
    private InputMethodManager mInputMethodManager;
    private DropTarget mLastDropTarget;
    private int[] mLastTouch = new int[2];
    private Launcher mLauncher;
    private ArrayList<DragListener> mListeners = new ArrayList<>();
    private int mMotionDownX;
    private int mMotionDownY;
    private View mMoveTarget;
    private Rect mRectTemp = new Rect();
    private int mScreenHeight;
    private int mScreenWidth;
    private ScrollRunnable mScrollRunnable = new ScrollRunnable();
    /* access modifiers changed from: private */
    public int mScrollState = 0;
    private View mScrollView;
    private int mScrollZone;
    private int[] mTmpPoint = new int[2];
    private final Vibrator mVibrator;
    private IBinder mWindowToken;

    interface DragListener {
        void onDragEnd();

        void onDragStart(DragSource dragSource, Object obj, int i);
    }

    private class ScrollRunnable implements Runnable {
        private int mDirection;

        ScrollRunnable() {
        }

        public void run() {
            if (DragController.this.mDragScroller != null) {
                if (this.mDirection == 0) {
                    DragController.this.mDragScroller.scrollLeft();
                } else {
                    DragController.this.mDragScroller.scrollRight();
                }
                DragController.this.mScrollState = 0;
                DragController.this.mDistanceSinceScroll = 0;
                DragController.this.mDragScroller.onExitScrollArea();
            }
        }

        /* access modifiers changed from: 0000 */
        public void setDirection(int direction) {
            this.mDirection = direction;
        }
    }

    public DragController(Launcher launcher) {
        this.mVibrator = (Vibrator) launcher.getSystemService("vibrator");
        this.mLauncher = launcher;
        this.mHandler = new Handler();
        this.mScrollZone = launcher.getResources().getDimensionPixelSize(R.dimen.scroll_zone);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        launcher.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        this.mScreenWidth = displayMetrics.widthPixels;
        this.mScreenHeight = displayMetrics.heightPixels;
    }

    public boolean dragging() {
        return this.mDragging;
    }

    public void startDrag(View v, DragSource source, Object dragInfo, int dragAction, boolean isInTouchMode) {
        startDrag(v, source, dragInfo, dragAction, null, isInTouchMode);
    }

    public void startDrag(View v, DragSource source, Object dragInfo, int dragAction, Rect dragRegion, boolean isInTouchMode) {
        Bitmap b = getViewBitmap(v);
        if (b != null) {
            int[] loc = this.mCoordinatesTemp;
            this.mLauncher.getDragLayer().getLocationInDragLayer(v, loc);
            startDrag(b, loc[0], loc[1], source, dragInfo, dragAction, null, dragRegion, isInTouchMode);
            b.recycle();
            if (dragAction == DRAG_ACTION_MOVE) {
                v.setVisibility(8);
            }
        }
    }

    public void startDrag(View v, Bitmap bmp, DragSource source, Object dragInfo, int dragAction, Rect dragRegion, boolean isInTouchMode) {
        int[] loc = this.mCoordinatesTemp;
        this.mLauncher.getDragLayer().getLocationInDragLayer(v, loc);
        startDrag(bmp, loc[0], loc[1], source, dragInfo, dragAction, null, dragRegion, isInTouchMode);
        if (dragAction == DRAG_ACTION_MOVE) {
            v.setVisibility(8);
        }
    }

    public void startDrag(Bitmap b, int dragLayerX, int dragLayerY, DragSource source, Object dragInfo, int dragAction, boolean isIntouchMode) {
        startDrag(b, dragLayerX, dragLayerY, source, dragInfo, dragAction, null, null, isIntouchMode);
    }

    public void startDrag(Bitmap b, int dragLayerX, int dragLayerY, DragSource source, Object dragInfo, int dragAction, Point dragOffset, Rect dragRegion, boolean isInTouchMode) {
        int dragRegionTop;
        if (this.mInputMethodManager == null) {
            this.mInputMethodManager = (InputMethodManager) this.mLauncher.getSystemService("input_method");
        }
        this.mInputMethodManager.hideSoftInputFromWindow(this.mWindowToken, 0);
        if (!isInTouchMode) {
            int[] dragLayerPos = getClampedDragLayerPos((float) ((b.getWidth() / 2) + dragLayerX), (float) ((b.getHeight() / 2) + dragLayerY));
            onKeyEvent(0, dragLayerPos[0], dragLayerPos[1]);
        }
        Iterator it = this.mListeners.iterator();
        while (it.hasNext()) {
            ((DragListener) it.next()).onDragStart(source, dragInfo, dragAction);
        }
        int registrationX = this.mMotionDownX - dragLayerX;
        int registrationY = this.mMotionDownY - dragLayerY;
        int dragRegionLeft = dragRegion == null ? 0 : dragRegion.left;
        if (dragRegion == null) {
            dragRegionTop = 0;
        } else {
            dragRegionTop = dragRegion.top;
        }
        this.mDragging = true;
        this.mDragObject = new DragObject();
        this.mDragObject.dragComplete = false;
        this.mDragObject.xOffset = this.mMotionDownX - (dragLayerX + dragRegionLeft);
        this.mDragObject.yOffset = this.mMotionDownY - (dragLayerY + dragRegionTop);
        this.mDragObject.dragSource = source;
        this.mDragObject.dragInfo = dragInfo;
        this.mDragObject.isInTouchMode = isInTouchMode;
        this.mVibrator.vibrate(35);
        DragObject dragObject = this.mDragObject;
        DragView dragView = new DragView(this.mLauncher, b, registrationX, registrationY, 0, 0, b.getWidth(), b.getHeight());
        dragObject.dragView = dragView;
        if (dragOffset != null) {
            dragView.setDragVisualizeOffset(new Point(dragOffset));
        }
        if (dragRegion != null) {
            dragView.setDragRegion(new Rect(dragRegion));
        }
        dragView.show(this.mMotionDownX, this.mMotionDownY);
        if (!isInTouchMode) {
            Log.d(TAG, "It is not in touch mode!!!");
            this.mDragObject.dragView.setFocusable(true);
            this.mDragObject.dragView.setFocusableInTouchMode(true);
            this.mDragObject.dragView.requestFocus();
        } else {
            Log.d(TAG, "It is in touch mode!!!");
        }
        handleMoveEvent(this.mMotionDownX, this.mMotionDownY);
        this.mLauncher.hideAddGuide();
    }

    /* access modifiers changed from: 0000 */
    public Bitmap getViewBitmap(View v) {
        v.clearFocus();
        v.setPressed(false);
        boolean willNotCache = v.willNotCacheDrawing();
        v.setWillNotCacheDrawing(false);
        int color = v.getDrawingCacheBackgroundColor();
        v.setDrawingCacheBackgroundColor(0);
        float alpha = v.getAlpha();
        v.setAlpha(1.0f);
        if (color != 0) {
            v.destroyDrawingCache();
        }
        v.buildDrawingCache();
        Bitmap cacheBitmap = v.getDrawingCache();
        if (cacheBitmap == null) {
            return null;
        }
        Bitmap createBitmap = Bitmap.createBitmap(cacheBitmap);
        v.destroyDrawingCache();
        v.setAlpha(alpha);
        v.setWillNotCacheDrawing(willNotCache);
        v.setDrawingCacheBackgroundColor(color);
        return createBitmap;
    }

    private boolean handleDragViewKeyEvent(KeyEvent event) {
        int x;
        int x2;
        if (event.getAction() != 1) {
            return false;
        }
        int[] result = new int[2];
        int iCellWidth = this.mLauncher.getResources().getDimensionPixelSize(R.dimen.workspace_cell_width) + this.mLauncher.getResources().getDimensionPixelSize(R.dimen.workspace_width_gap);
        int iCellHeight = this.mLauncher.getResources().getDimensionPixelSize(R.dimen.workspace_cell_height) + this.mLauncher.getResources().getDimensionPixelSize(R.dimen.workspace_height_gap);
        int iconSize = this.mLauncher.getResources().getDimensionPixelSize(R.dimen.app_icon_size);
        int width = this.mDragObject.dragView.getWidth();
        int iViewHeight = this.mDragObject.dragView.getHeight();
        switch (event.getKeyCode()) {
            case 4:
                this.mDragObject.dragView.getMotionPosition(result);
                onKeyEvent(3, result[0], result[1]);
                break;
            case 19:
                this.mDragObject.dragView.getMotionPosition(result);
                int x3 = result[0];
                int y = result[1] - iCellHeight;
                if (y >= 0) {
                    onKeyEvent(1, x3, y);
                    break;
                }
                break;
            case Board.NUM_ICONS /*20*/:
                this.mDragObject.dragView.getMotionPosition(result);
                int x4 = result[0];
                int y2 = result[1] + iCellHeight;
                if (!(this.mDragObject.dragSource instanceof Folder) ? (iViewHeight / 2) + y2 <= this.mScreenHeight - iCellHeight : (iViewHeight / 2) + y2 <= this.mScreenHeight) {
                    onKeyEvent(1, x4, y2);
                    break;
                }
            case 21:
                this.mDragObject.dragView.getMotionPosition(result);
                if (result[0] + (iconSize / 2) > this.mScreenWidth) {
                    x2 = (result[0] - iCellWidth) + (iconSize / 2);
                } else if (result[0] - iCellWidth <= 0) {
                    x2 = result[0];
                    if ((this.mDragObject.dragSource instanceof Workspace) && ((Workspace) this.mDragObject.dragSource).getCurrentPage() > 0) {
                        x2 = result[0] + ((((Workspace) this.mDragObject.dragSource).mCellCountX - 1) * iCellWidth);
                    }
                    this.mDragScroller.scrollLeft();
                } else {
                    x2 = result[0] - iCellWidth;
                    if (x2 < 0) {
                        x2 += iconSize / 2;
                    }
                }
                onKeyEvent(1, x2, result[1]);
                break;
            case 22:
                this.mDragObject.dragView.getMotionPosition(result);
                if (result[0] + (iCellWidth * 2) > this.mScreenWidth) {
                    x = result[0];
                    if ((this.mDragObject.dragSource instanceof Workspace) && ((Workspace) this.mDragObject.dragSource).getCurrentPage() < ((Workspace) this.mDragObject.dragSource).getPageCount() - 1) {
                        x = result[0] - ((((Workspace) this.mDragObject.dragSource).mCellCountX - 1) * iCellWidth);
                    }
                    this.mDragScroller.scrollRight();
                } else if (result[0] - (iconSize / 2) < 0) {
                    x = (result[0] + iCellWidth) - (iconSize / 2);
                } else {
                    x = result[0] + iCellWidth;
                    if (x > this.mScreenWidth) {
                        x -= iconSize / 2;
                    }
                }
                onKeyEvent(1, x, result[1]);
                break;
            case 23:
            case CommonDesk.Cmd_SignalLock /*66*/:
                this.mDragObject.dragView.getMotionPosition(result);
                onKeyEvent(2, result[0], result[1]);
                break;
            default:
                return false;
        }
        return true;
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        if (this.mDragging && this.mDragObject != null && !this.mDragObject.isInTouchMode) {
            handleDragViewKeyEvent(event);
        }
        return this.mDragging;
    }

    public boolean isDragging() {
        return this.mDragging;
    }

    public void cancelDrag() {
        if (this.mDragging) {
            if (this.mLastDropTarget != null) {
                this.mLastDropTarget.onDragExit(this.mDragObject);
            }
            this.mDragObject.cancelled = true;
            this.mDragObject.dragComplete = true;
            this.mDragObject.dragSource.onDropCompleted(null, this.mDragObject, false);
        }
        endDrag();
    }

    public void onAppsRemoved(ArrayList<ApplicationInfo> apps, Context context) {
        if (this.mDragObject != null) {
            Object rawDragInfo = this.mDragObject.dragInfo;
            if (rawDragInfo instanceof ShortcutInfo) {
                ShortcutInfo dragInfo = (ShortcutInfo) rawDragInfo;
                Iterator it = apps.iterator();
                while (it.hasNext()) {
                    if (((ApplicationInfo) it.next()).intent.getComponent().equals(dragInfo.intent.getComponent())) {
                        cancelDrag();
                        return;
                    }
                }
            }
        }
    }

    private void endDrag() {
        if (this.mDragging) {
            this.mDragging = false;
            Iterator it = this.mListeners.iterator();
            while (it.hasNext()) {
                ((DragListener) it.next()).onDragEnd();
            }
            if (this.mDragObject.dragView != null) {
                this.mDragObject.dragView.remove();
                this.mDragObject.dragView = null;
            }
        }
    }

    private int[] getClampedDragLayerPos(float x, float y) {
        this.mLauncher.getDragLayer().getLocalVisibleRect(this.mDragLayerRect);
        this.mTmpPoint[0] = (int) Math.max((float) this.mDragLayerRect.left, Math.min(x, (float) (this.mDragLayerRect.right - 1)));
        this.mTmpPoint[1] = (int) Math.max((float) this.mDragLayerRect.top, Math.min(y, (float) (this.mDragLayerRect.bottom - 1)));
        return this.mTmpPoint;
    }

    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        int[] dragLayerPos = getClampedDragLayerPos(ev.getX(), ev.getY());
        int dragLayerX = dragLayerPos[0];
        int dragLayerY = dragLayerPos[1];
        switch (action) {
            case 0:
                this.mMotionDownX = dragLayerX;
                this.mMotionDownY = dragLayerY;
                this.mLastDropTarget = null;
                break;
            case 1:
                if (this.mDragging) {
                    drop((float) dragLayerX, (float) dragLayerY);
                }
                endDrag();
                break;
            case 3:
                cancelDrag();
                break;
        }
        return this.mDragging;
    }

    /* access modifiers changed from: 0000 */
    public void setMoveTarget(View view) {
        this.mMoveTarget = view;
    }

    public boolean dispatchUnhandledMove(View focused, int direction) {
        return this.mMoveTarget != null && this.mMoveTarget.dispatchUnhandledMove(focused, direction);
    }

    private void handleMoveEvent(int x, int y) {
        this.mDragObject.dragView.move(x, y);
        int[] coordinates = this.mCoordinatesTemp;
        DropTarget dropTarget = findDropTarget(x, y, coordinates);
        this.mDragObject.x = coordinates[0];
        this.mDragObject.y = coordinates[1];
        if (dropTarget != null) {
            DropTarget delegate = dropTarget.getDropTargetDelegate(this.mDragObject);
            if (delegate != null) {
                dropTarget = delegate;
            }
            if (this.mLastDropTarget != dropTarget) {
                if (this.mLastDropTarget != null) {
                    this.mLastDropTarget.onDragExit(this.mDragObject);
                }
                dropTarget.onDragEnter(this.mDragObject);
            }
            dropTarget.onDragOver(this.mDragObject);
        } else if (this.mLastDropTarget != null) {
            this.mLastDropTarget.onDragExit(this.mDragObject);
        }
        this.mLastDropTarget = dropTarget;
        int slop = ViewConfiguration.get(this.mLauncher).getScaledWindowTouchSlop();
        this.mDistanceSinceScroll = (int) (((double) this.mDistanceSinceScroll) + Math.sqrt(Math.pow((double) (this.mLastTouch[0] - x), 2.0d) + Math.pow((double) (this.mLastTouch[1] - y), 2.0d)));
        this.mLastTouch[0] = x;
        this.mLastTouch[1] = y;
        if (x < this.mScrollZone) {
            if (this.mScrollState != 0) {
                return;
            }
            if (this.mDistanceSinceScroll > slop || this.mForceScroll) {
                this.mScrollState = 1;
                this.mForceScroll = false;
                if (this.mDragScroller.onEnterScrollArea(x, y, 0)) {
                    this.mScrollRunnable.setDirection(0);
                    this.mHandler.postDelayed(this.mScrollRunnable, 600);
                }
            }
        } else if (x > this.mScrollView.getWidth() - this.mScrollZone) {
            if (this.mScrollState != 0) {
                return;
            }
            if (this.mDistanceSinceScroll > slop || this.mForceScroll) {
                this.mScrollState = 1;
                this.mForceScroll = false;
                if (this.mDragScroller.onEnterScrollArea(x, y, 1)) {
                    this.mScrollRunnable.setDirection(1);
                    this.mHandler.postDelayed(this.mScrollRunnable, 600);
                }
            }
        } else if (this.mScrollState == 1) {
            this.mScrollState = 0;
            this.mScrollRunnable.setDirection(1);
            this.mHandler.removeCallbacks(this.mScrollRunnable);
            this.mDragScroller.onExitScrollArea();
        }
    }

    public boolean onTouchEvent(MotionEvent ev) {
        if (!this.mDragging) {
            return false;
        }
        int action = ev.getAction();
        int[] dragLayerPos = getClampedDragLayerPos(ev.getX(), ev.getY());
        int dragLayerX = dragLayerPos[0];
        int dragLayerY = dragLayerPos[1];
        switch (action) {
            case 0:
                this.mMotionDownX = dragLayerX;
                this.mMotionDownY = dragLayerY;
                if (dragLayerX >= this.mScrollZone && dragLayerX <= this.mScrollView.getWidth() - this.mScrollZone) {
                    this.mScrollState = 0;
                    break;
                } else {
                    this.mScrollState = 1;
                    this.mHandler.postDelayed(this.mScrollRunnable, 600);
                    break;
                }
                break;
            case 1:
                handleMoveEvent(dragLayerX, dragLayerY);
                this.mHandler.removeCallbacks(this.mScrollRunnable);
                if (this.mDragging) {
                    if (!canDrop((float) dragLayerX, (float) dragLayerY)) {
                        cancelDrag();
                        break;
                    } else {
                        drop((float) dragLayerX, (float) dragLayerY);
                        endDrag();
                        break;
                    }
                }
                break;
            case 2:
                handleMoveEvent(dragLayerX, dragLayerY);
                break;
            case 3:
                cancelDrag();
                break;
        }
        return true;
    }

    public boolean onKeyEvent(int iKeyEvent, int x, int y) {
        if (!this.mDragging && iKeyEvent != 0) {
            return false;
        }
        switch (iKeyEvent) {
            case 0:
                this.mMotionDownX = x;
                this.mMotionDownY = y;
                this.mLastDropTarget = null;
                break;
            case 1:
                handleMoveEvent(x, y);
                break;
            case 2:
                this.mHandler.removeCallbacks(this.mScrollRunnable);
                if (this.mDragging) {
                    if (!canDrop((float) x, (float) y)) {
                        cancelDrag();
                        break;
                    } else {
                        drop((float) x, (float) y);
                        endDrag();
                        break;
                    }
                }
                break;
            case 3:
                cancelDrag();
                break;
        }
        return true;
    }

    private boolean canDrop(float x, float y) {
        return findDropTarget((int) x, (int) y, this.mCoordinatesTemp).acceptDrop(this.mDragObject);
    }

    private void drop(float x, float y) {
        int[] coordinates = this.mCoordinatesTemp;
        DropTarget dropTarget = findDropTarget((int) x, (int) y, coordinates);
        this.mDragObject.x = coordinates[0];
        this.mDragObject.y = coordinates[1];
        boolean accepted = false;
        if (dropTarget != null) {
            this.mDragObject.dragComplete = true;
            dropTarget.onDragExit(this.mDragObject);
            if (dropTarget.acceptDrop(this.mDragObject)) {
                dropTarget.onDrop(this.mDragObject);
                accepted = true;
            }
        }
        this.mDragObject.dragSource.onDropCompleted((View) dropTarget, this.mDragObject, accepted);
    }

    private DropTarget findDropTarget(int x, int y, int[] dropCoordinates) {
        Rect r = this.mRectTemp;
        ArrayList<DropTarget> dropTargets = this.mDropTargets;
        for (int i = dropTargets.size() - 1; i >= 0; i--) {
            DropTarget target = (DropTarget) dropTargets.get(i);
            if (target.isDropEnabled()) {
                target.getHitRect(r);
                target.getLocationInDragLayer(dropCoordinates);
                r.offset(dropCoordinates[0] - target.getLeft(), dropCoordinates[1] - target.getTop());
                this.mDragObject.x = x;
                this.mDragObject.y = y;
                if (r.contains(x, y)) {
                    DropTarget delegate = target.getDropTargetDelegate(this.mDragObject);
                    if (delegate != null) {
                        target = delegate;
                        target.getLocationInDragLayer(dropCoordinates);
                    }
                    dropCoordinates[0] = x - dropCoordinates[0];
                    dropCoordinates[1] = y - dropCoordinates[1];
                    return target;
                }
            }
        }
        return null;
    }

    public void setDragScoller(DragScroller scroller) {
        this.mDragScroller = scroller;
    }

    public void setWindowToken(IBinder token) {
        this.mWindowToken = token;
    }

    public void addDragListener(DragListener l) {
        this.mListeners.add(l);
    }

    public void removeDragListener(DragListener l) {
        this.mListeners.remove(l);
    }

    public void addDropTarget(DropTarget target) {
        this.mDropTargets.add(target);
    }

    public void removeDropTarget(DropTarget target) {
        this.mDropTargets.remove(target);
    }

    public void setScrollView(View v) {
        this.mScrollView = v;
    }

    /* access modifiers changed from: 0000 */
    public DragView getDragView() {
        return this.mDragObject.dragView;
    }
}
