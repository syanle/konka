package com.cyanogenmod.trebuchet;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewParent;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.Scroller;
import android.widget.TextView;
import com.cyanogenmod.trebuchet.RocketLauncher.Board.FlyingIcon;
import com.konka.ios7launcher.R;
import java.util.ArrayList;
import java.util.Iterator;

public class DragLayer extends FrameLayout {
    public static final long DEFAULT_HINT_DELAY_MILLIS = 1000;
    public static final long DEFAULT_HINT_DURATION_MILLIS = 1000;
    private static final long RESHOWING_GAP_MILLIS = 500;
    private TimeInterpolator mCubicEaseOutInterpolator = new DecelerateInterpolator(1.5f);
    private AppWidgetResizeFrame mCurrentResizeFrame;
    private DragController mDragController;
    private ValueAnimator mDropAnim = null;
    /* access modifiers changed from: private */
    public View mDropView = null;
    /* access modifiers changed from: private */
    public float mDropViewAlpha;
    /* access modifiers changed from: private */
    public int[] mDropViewPos = new int[2];
    /* access modifiers changed from: private */
    public float mDropViewScale;
    private ValueAnimator mFadeOutAnim = null;
    /* access modifiers changed from: private */
    public HideHintRunnable mHideHintRunnalbe = new HideHintRunnable(this, null);
    /* access modifiers changed from: private */
    public View mHintAnchor;
    /* access modifiers changed from: private */
    public TextView mHintContentView;
    /* access modifiers changed from: private */
    public long mHintDuration;
    /* access modifiers changed from: private */
    public Handler mHintHandler;
    /* access modifiers changed from: private */
    public PopupWindow mHintPopup;
    /* access modifiers changed from: private */
    public String mHintText;
    private Rect mHitRect = new Rect();
    private boolean mHoverPointClosesFolder = false;
    private long mLastShowHintMillis = 0;
    private Launcher mLauncher;
    private int mQsbIndex = -1;
    private final ArrayList<AppWidgetResizeFrame> mResizeFrames = new ArrayList<>();
    public int mScreenWidth;
    private Scroller mScroller;
    private ShowHintRunnable mShowHintRunnable = new ShowHintRunnable(this, null);
    private int[] mTmpXY = new int[2];
    private int mWorkspaceIndex = -1;
    private int mXDown;
    private int mYDown;

    private class HideHintRunnable implements Runnable {
        private HideHintRunnable() {
        }

        /* synthetic */ HideHintRunnable(DragLayer dragLayer, HideHintRunnable hideHintRunnable) {
            this();
        }

        public void run() {
            DragLayer.this.hideHint(DragLayer.this.mHintAnchor);
        }
    }

    public interface HintContent {
        String getHintText();
    }

    public static class LayoutParams extends android.widget.FrameLayout.LayoutParams {
        public boolean customPosition = false;
        public int x;
        public int y;

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public int getWidth() {
            return this.width;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public int getHeight() {
            return this.height;
        }

        public void setX(int x2) {
            this.x = x2;
        }

        public int getX() {
            return this.x;
        }

        public void setY(int y2) {
            this.y = y2;
        }

        public int getY() {
            return this.y;
        }
    }

    private class ShowHintRunnable implements Runnable {
        private ShowHintRunnable() {
        }

        /* synthetic */ ShowHintRunnable(DragLayer dragLayer, ShowHintRunnable showHintRunnable) {
            this();
        }

        public void run() {
            DragLayer.this.mHintContentView.setText(DragLayer.this.mHintText);
            int measureSpec = MeasureSpec.makeMeasureSpec(0, 0);
            DragLayer.this.mHintContentView.measure(measureSpec, measureSpec);
            int width = DragLayer.this.mHintContentView.getMeasuredWidth();
            int height = DragLayer.this.mHintContentView.getMeasuredHeight();
            if (!DragLayer.this.mHintPopup.isShowing()) {
                DragLayer.this.mHintPopup.setWidth(width);
                DragLayer.this.mHintPopup.setHeight(height);
                DragLayer.this.mHintPopup.showAsDropDown(DragLayer.this.mHintAnchor);
            } else {
                DragLayer.this.mHintPopup.update(DragLayer.this.mHintAnchor, width, height);
            }
            DragLayer.this.mHintHandler.postDelayed(DragLayer.this.mHideHintRunnalbe, DragLayer.this.mHintDuration);
        }
    }

    public DragLayer(Context context, AttributeSet attrs) {
        super(context, attrs);
        setMotionEventSplittingEnabled(false);
        setChildrenDrawingOrderEnabled(true);
        this.mScroller = new Scroller(context, new Interpolator() {
            public float getInterpolation(float t) {
                float t2 = t - 1.0f;
                return (t2 * t2 * t2 * t2 * t2) + 1.0f;
            }
        });
        this.mScreenWidth = ((WindowManager) this.mContext.getSystemService("window")).getDefaultDisplay().getWidth();
        this.mHintHandler = new Handler();
        this.mHintContentView = (TextView) LayoutInflater.from(context).inflate(R.layout.hint_popup, null);
        this.mHintPopup = new PopupWindow(this.mHintContentView);
        this.mHintPopup.setTouchable(false);
    }

    public void setup(Launcher launcher, DragController controller) {
        this.mLauncher = launcher;
        this.mDragController = controller;
    }

    public void snapToTVPage() {
        this.mScroller.startScroll(this.mScroller.getFinalX(), this.mScroller.getFinalY(), (-this.mScreenWidth) - this.mScroller.getFinalX(), 0 - this.mScroller.getFinalY(), 500);
        if (this.mLauncher != null) {
            this.mLauncher.startTV();
        }
        invalidate();
    }

    public void snapToWorkspace() {
        if (isInTVPage()) {
            this.mScroller.startScroll(this.mScroller.getFinalX(), this.mScroller.getFinalY(), 0 - this.mScroller.getFinalX(), 0 - this.mScroller.getFinalY(), 700);
            invalidate();
        }
    }

    private boolean isInTVPage() {
        return this.mScroller.getFinalX() != 0;
    }

    public void computeScroll() {
        if (this.mScroller.computeScrollOffset()) {
            if (!(this.mScrollX == this.mScroller.getCurrX() && this.mScrollY == this.mScroller.getCurrY())) {
                scrollTo(this.mScroller.getCurrX(), this.mScroller.getCurrY());
            }
            invalidate();
        }
    }

    public void showDelayedHint(View anchor, String hintText, long delayMillis, long durationMillis) {
        this.mHintHandler.removeCallbacks(this.mHideHintRunnalbe);
        this.mHintHandler.removeCallbacks(this.mShowHintRunnable);
        long currentMillis = System.currentTimeMillis();
        if (this.mHintAnchor != anchor || currentMillis - this.mLastShowHintMillis >= RESHOWING_GAP_MILLIS) {
            this.mLastShowHintMillis = currentMillis;
            this.mHintAnchor = anchor;
            this.mHintText = hintText;
            this.mHintDuration = durationMillis;
            this.mHintHandler.postDelayed(this.mShowHintRunnable, delayMillis);
            return;
        }
        Log.d("DragLayer", "current(" + currentMillis + ") and last(" + this.mLastShowHintMillis + ") too close, not showing");
    }

    public void hideHint(View v) {
        this.mHintHandler.removeCallbacks(this.mHideHintRunnalbe);
        if (v == this.mHintAnchor) {
            this.mHintHandler.removeCallbacks(this.mShowHintRunnable);
            if (this.mHintPopup.isShowing()) {
                this.mHintPopup.dismiss();
            }
        }
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        return this.mDragController.dispatchKeyEvent(event) || super.dispatchKeyEvent(event);
    }

    private boolean isEventOverFolderTextRegion(Folder folder, MotionEvent ev) {
        getDescendantRectRelativeToSelf(folder.getEditTextRegion(), this.mHitRect);
        return this.mHitRect.contains((int) ev.getX(), (int) ev.getY());
    }

    private boolean isEventOverFolder(Folder folder, MotionEvent ev) {
        getDescendantRectRelativeToSelf(folder, this.mHitRect);
        return this.mHitRect.contains((int) ev.getX(), (int) ev.getY());
    }

    private boolean handleTouchDown(MotionEvent ev, boolean intercept) {
        Rect hitRect = new Rect();
        int x = (int) ev.getX();
        int y = (int) ev.getY();
        Iterator it = this.mResizeFrames.iterator();
        while (it.hasNext()) {
            AppWidgetResizeFrame child = (AppWidgetResizeFrame) it.next();
            child.getHitRect(hitRect);
            if (hitRect.contains(x, y) && child.beginResizeIfPointInRegion(x - child.getLeft(), y - child.getTop())) {
                this.mCurrentResizeFrame = child;
                this.mXDown = x;
                this.mYDown = y;
                requestDisallowInterceptTouchEvent(true);
                return true;
            }
        }
        Folder currentFolder = this.mLauncher.getWorkspace().getOpenFolder();
        if (currentFolder != null && !this.mLauncher.isFolderClingVisible() && intercept) {
            if (!currentFolder.isEditingName() || isEventOverFolderTextRegion(currentFolder, ev)) {
                getDescendantRectRelativeToSelf(currentFolder, hitRect);
                if (!isEventOverFolder(currentFolder, ev)) {
                    this.mLauncher.closeFolder();
                    return true;
                }
            } else {
                currentFolder.dismissEditingName();
                return true;
            }
        }
        return false;
    }

    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (ev.getAction() == 0 && handleTouchDown(ev, true)) {
            return true;
        }
        clearAllResizeFrames();
        return this.mDragController.onInterceptTouchEvent(ev);
    }

    public boolean onInterceptHoverEvent(MotionEvent ev) {
        Folder currentFolder = null;
        if (this.mLauncher.getWorkspace() != null) {
            currentFolder = this.mLauncher.getWorkspace().getOpenFolder();
        }
        if (currentFolder == null || !AccessibilityManager.getInstance(this.mContext).isTouchExplorationEnabled()) {
            return false;
        }
        switch (ev.getAction()) {
            case 7:
                break;
            case 9:
                if (isEventOverFolder(currentFolder, ev)) {
                    this.mHoverPointClosesFolder = false;
                    break;
                } else {
                    sendTapOutsideFolderAccessibilityEvent(currentFolder.isEditingName());
                    this.mHoverPointClosesFolder = true;
                    return true;
                }
            default:
                return false;
        }
        boolean isOverFolder = isEventOverFolder(currentFolder, ev);
        if (!isOverFolder && !this.mHoverPointClosesFolder) {
            sendTapOutsideFolderAccessibilityEvent(currentFolder.isEditingName());
            this.mHoverPointClosesFolder = true;
            return true;
        } else if (!isOverFolder) {
            return true;
        } else {
            this.mHoverPointClosesFolder = false;
            return false;
        }
    }

    private void sendTapOutsideFolderAccessibilityEvent(boolean isEditingName) {
        if (AccessibilityManager.getInstance(this.mContext).isEnabled()) {
            int stringId = isEditingName ? R.string.folder_tap_to_rename : R.string.folder_tap_to_close;
            AccessibilityEvent event = AccessibilityEvent.obtain(8);
            onInitializeAccessibilityEvent(event);
            event.getText().add(this.mContext.getString(stringId));
            AccessibilityManager.getInstance(this.mContext).sendAccessibilityEvent(event);
        }
    }

    public boolean onHoverEvent(MotionEvent ev) {
        return false;
    }

    public boolean onTouchEvent(MotionEvent ev) {
        boolean handled = false;
        int action = ev.getAction();
        int x = (int) ev.getX();
        int y = (int) ev.getY();
        if (ev.getAction() == 0 && ev.getAction() == 0 && handleTouchDown(ev, false)) {
            return true;
        }
        if (this.mCurrentResizeFrame != null) {
            handled = true;
            switch (action) {
                case 1:
                case 3:
                    this.mCurrentResizeFrame.commitResizeForDelta(x - this.mXDown, y - this.mYDown);
                    this.mCurrentResizeFrame = null;
                    break;
                case 2:
                    this.mCurrentResizeFrame.visualizeResizeForDelta(x - this.mXDown, y - this.mYDown);
                    break;
            }
        }
        if (!handled) {
            return this.mDragController.onTouchEvent(ev);
        }
        return true;
    }

    public float getDescendantRectRelativeToSelf(View descendant, Rect r) {
        this.mTmpXY[0] = 0;
        this.mTmpXY[1] = 0;
        float scale = getDescendantCoordRelativeToSelf(descendant, this.mTmpXY);
        r.set(this.mTmpXY[0], this.mTmpXY[1], this.mTmpXY[0] + descendant.getWidth(), this.mTmpXY[1] + descendant.getHeight());
        return scale;
    }

    public void getLocationInDragLayer(View child, int[] loc) {
        loc[0] = 0;
        loc[1] = 0;
        getDescendantCoordRelativeToSelf(child, loc);
    }

    public float getDescendantCoordRelativeToSelf(View descendant, int[] coord) {
        float[] pt = {(float) coord[0], (float) coord[1]};
        descendant.getMatrix().mapPoints(pt);
        float scale = 1.0f * descendant.getScaleX();
        pt[0] = pt[0] + ((float) descendant.getLeft());
        pt[1] = pt[1] + ((float) descendant.getTop());
        ViewParent viewParent = descendant.getParent();
        while ((viewParent instanceof View) && viewParent != this) {
            View view = (View) viewParent;
            view.getMatrix().mapPoints(pt);
            scale *= view.getScaleX();
            pt[0] = pt[0] + ((float) (view.getLeft() - view.getScrollX()));
            pt[1] = pt[1] + ((float) (view.getTop() - view.getScrollY()));
            viewParent = view.getParent();
        }
        coord[0] = Math.round(pt[0]);
        coord[1] = Math.round(pt[1]);
        return scale;
    }

    public void getViewRectRelativeToSelf(View v, Rect r) {
        int[] loc = new int[2];
        getLocationInWindow(loc);
        int x = loc[0];
        int y = loc[1];
        v.getLocationInWindow(loc);
        int left = loc[0] - x;
        int top = loc[1] - y;
        r.set(left, top, v.getMeasuredWidth() + left, v.getMeasuredHeight() + top);
    }

    public boolean dispatchUnhandledMove(View focused, int direction) {
        return this.mDragController.dispatchUnhandledMove(focused, direction);
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            android.widget.FrameLayout.LayoutParams flp = (android.widget.FrameLayout.LayoutParams) child.getLayoutParams();
            if (flp instanceof LayoutParams) {
                LayoutParams lp = (LayoutParams) flp;
                if (lp.customPosition) {
                    child.layout(lp.x, lp.y, lp.x + lp.width, lp.y + lp.height);
                }
            }
        }
    }

    public void clearAllResizeFrames() {
        if (this.mResizeFrames.size() > 0) {
            Iterator it = this.mResizeFrames.iterator();
            while (it.hasNext()) {
                removeView((AppWidgetResizeFrame) it.next());
            }
            this.mResizeFrames.clear();
        }
    }

    public boolean hasResizeFrames() {
        return this.mResizeFrames.size() > 0;
    }

    public boolean isWidgetBeingResized() {
        return this.mCurrentResizeFrame != null;
    }

    public void addResizeFrame(ItemInfo itemInfo, LauncherAppWidgetHostView widget, CellLayout cellLayout) {
        AppWidgetResizeFrame resizeFrame = new AppWidgetResizeFrame(getContext(), itemInfo, widget, cellLayout, this);
        LayoutParams lp = new LayoutParams(-1, -1);
        lp.customPosition = true;
        addView(resizeFrame, lp);
        this.mResizeFrames.add(resizeFrame);
        resizeFrame.snapToWidget(false);
    }

    public void animateViewIntoPosition(DragView dragView, View child) {
        animateViewIntoPosition(dragView, child, null);
    }

    public void animateViewIntoPosition(DragView dragView, int[] pos, float scale, Runnable onFinishRunnable) {
        Rect r = new Rect();
        getViewRectRelativeToSelf(dragView, r);
        animateViewIntoPosition(dragView, r.left, r.top, pos[0], pos[1], scale, onFinishRunnable, -1);
    }

    public void animateViewIntoPosition(DragView dragView, View child, Runnable onFinishAnimationRunnable) {
        animateViewIntoPosition(dragView, child, -1, onFinishAnimationRunnable);
    }

    public void animateViewIntoPosition(DragView dragView, View child, int duration, Runnable onFinishAnimationRunnable) {
        int toY;
        int toX;
        ((CellLayoutChildren) child.getParent()).measureChild(child);
        com.cyanogenmod.trebuchet.CellLayout.LayoutParams lp = (com.cyanogenmod.trebuchet.CellLayout.LayoutParams) child.getLayoutParams();
        Rect r = new Rect();
        getViewRectRelativeToSelf(dragView, r);
        int[] coord = {lp.x, lp.y};
        float scale = getDescendantCoordRelativeToSelf((View) child.getParent(), coord);
        int toX2 = coord[0];
        int toY2 = coord[1];
        if (child instanceof TextView) {
            TextView tv = (TextView) child;
            toY = (toY2 + Math.round(((float) tv.getPaddingTop()) * scale)) - ((dragView.getHeight() - Math.round(((float) tv.getCompoundDrawables()[1].getIntrinsicHeight()) * scale)) / 2);
            toX = toX2 - ((dragView.getMeasuredWidth() - Math.round(((float) child.getMeasuredWidth()) * scale)) / 2);
        } else if (child instanceof FolderIcon) {
            toY = toY2 - (HolographicOutlineHelper.MAX_OUTER_BLUR_RADIUS / 2);
            toX = toX2 - ((dragView.getMeasuredWidth() - Math.round(((float) child.getMeasuredWidth()) * scale)) / 2);
        } else {
            toY = toY2 - (Math.round(((float) (dragView.getHeight() - child.getMeasuredHeight())) * scale) / 2);
            toX = toX2 - (Math.round(((float) (dragView.getMeasuredWidth() - child.getMeasuredWidth())) * scale) / 2);
        }
        int fromX = r.left;
        int fromY = r.top;
        child.setVisibility(4);
        child.setAlpha(FlyingIcon.ANGULAR_VMIN);
        final View view = child;
        final Runnable runnable = onFinishAnimationRunnable;
        animateViewIntoPosition(dragView, fromX, fromY, toX, toY, scale, new Runnable() {
            public void run() {
                view.setVisibility(0);
                ObjectAnimator oa = ObjectAnimator.ofFloat(view, "alpha", new float[]{FlyingIcon.ANGULAR_VMIN, 1.0f});
                oa.setDuration(60);
                final Runnable runnable = runnable;
                oa.addListener(new AnimatorListenerAdapter() {
                    public void onAnimationEnd(Animator animation) {
                        if (runnable != null) {
                            runnable.run();
                        }
                    }
                });
                oa.start();
            }
        }, duration);
    }

    private void animateViewIntoPosition(View view, int fromX, int fromY, int toX, int toY, float finalScale, Runnable onCompleteRunnable, int duration) {
        animateView(view, new Rect(fromX, fromY, view.getMeasuredWidth() + fromX, view.getMeasuredHeight() + fromY), new Rect(toX, toY, view.getMeasuredWidth() + toX, view.getMeasuredHeight() + toY), 1.0f, finalScale, duration, null, null, onCompleteRunnable, true);
    }

    public void animateView(View view, Rect from, Rect to, float finalAlpha, float finalScale, int duration, Interpolator motionInterpolator, Interpolator alphaInterpolator, Runnable onCompleteRunnable, boolean fadeOut) {
        float dist = (float) Math.sqrt(Math.pow((double) (to.left - from.left), 2.0d) + Math.pow((double) (to.top - from.top), 2.0d));
        Resources res = getResources();
        float maxDist = (float) res.getInteger(R.integer.config_dropAnimMaxDist);
        if (duration < 0) {
            duration = res.getInteger(R.integer.config_dropAnimMaxDuration);
            if (dist < maxDist) {
                duration = (int) (((float) duration) * this.mCubicEaseOutInterpolator.getInterpolation(dist / maxDist));
            }
        }
        if (this.mDropAnim != null) {
            this.mDropAnim.cancel();
        }
        if (this.mFadeOutAnim != null) {
            this.mFadeOutAnim.cancel();
        }
        this.mDropView = view;
        final float initialAlpha = view.getAlpha();
        this.mDropAnim = new ValueAnimator();
        if (alphaInterpolator == null || motionInterpolator == null) {
            this.mDropAnim.setInterpolator(this.mCubicEaseOutInterpolator);
        }
        this.mDropAnim.setDuration((long) duration);
        this.mDropAnim.setFloatValues(new float[]{FlyingIcon.ANGULAR_VMIN, 1.0f});
        this.mDropAnim.removeAllUpdateListeners();
        final View view2 = view;
        final Interpolator interpolator = alphaInterpolator;
        final Interpolator interpolator2 = motionInterpolator;
        final Rect rect = from;
        final Rect rect2 = to;
        final float f = finalScale;
        final float f2 = finalAlpha;
        this.mDropAnim.addUpdateListener(new AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                float percent = ((Float) animation.getAnimatedValue()).floatValue();
                int width = view2.getMeasuredWidth();
                int height = view2.getMeasuredHeight();
                DragLayer.this.invalidate(DragLayer.this.mDropViewPos[0], DragLayer.this.mDropViewPos[1], DragLayer.this.mDropViewPos[0] + width, DragLayer.this.mDropViewPos[1] + height);
                float alphaPercent = interpolator == null ? percent : interpolator.getInterpolation(percent);
                float motionPercent = interpolator2 == null ? percent : interpolator2.getInterpolation(percent);
                DragLayer.this.mDropViewPos[0] = rect.left + Math.round(((float) (rect2.left - rect.left)) * motionPercent);
                DragLayer.this.mDropViewPos[1] = rect.top + Math.round(((float) (rect2.top - rect.top)) * motionPercent);
                DragLayer.this.mDropViewScale = (f * percent) + (1.0f - percent);
                DragLayer.this.mDropViewAlpha = (f2 * alphaPercent) + ((1.0f - alphaPercent) * initialAlpha);
                DragLayer.this.invalidate(DragLayer.this.mDropViewPos[0], DragLayer.this.mDropViewPos[1], DragLayer.this.mDropViewPos[0] + width, DragLayer.this.mDropViewPos[1] + height);
            }
        });
        final Runnable runnable = onCompleteRunnable;
        final boolean z = fadeOut;
        this.mDropAnim.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animation) {
                if (runnable != null) {
                    runnable.run();
                }
                if (z) {
                    DragLayer.this.fadeOutDragView();
                } else {
                    DragLayer.this.mDropView = null;
                }
            }
        });
        this.mDropAnim.start();
    }

    /* access modifiers changed from: private */
    public void fadeOutDragView() {
        this.mFadeOutAnim = new ValueAnimator();
        this.mFadeOutAnim.setDuration(150);
        this.mFadeOutAnim.setFloatValues(new float[]{FlyingIcon.ANGULAR_VMIN, 1.0f});
        this.mFadeOutAnim.removeAllUpdateListeners();
        this.mFadeOutAnim.addUpdateListener(new AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                DragLayer.this.mDropViewAlpha = 1.0f - ((Float) animation.getAnimatedValue()).floatValue();
                DragLayer.this.invalidate(DragLayer.this.mDropViewPos[0], DragLayer.this.mDropViewPos[1], DragLayer.this.mDropViewPos[0] + DragLayer.this.mDropView.getMeasuredWidth(), DragLayer.this.mDropViewPos[1] + DragLayer.this.mDropView.getMeasuredHeight());
            }
        });
        this.mFadeOutAnim.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animation) {
                DragLayer.this.mDropView = null;
            }
        });
        this.mFadeOutAnim.start();
    }

    /* access modifiers changed from: protected */
    public void onViewAdded(View child) {
        super.onViewAdded(child);
        updateChildIndices();
    }

    /* access modifiers changed from: protected */
    public void onViewRemoved(View child) {
        super.onViewRemoved(child);
        updateChildIndices();
    }

    private void updateChildIndices() {
        if (this.mLauncher != null) {
            this.mWorkspaceIndex = indexOfChild(this.mLauncher.getWorkspace());
            this.mQsbIndex = indexOfChild(this.mLauncher.getSearchBar());
        }
    }

    /* access modifiers changed from: protected */
    public int getChildDrawingOrder(int childCount, int i) {
        if (LauncherApplication.isScreenLandscape(getContext())) {
            return super.getChildDrawingOrder(childCount, i);
        }
        if (this.mWorkspaceIndex == -1 || this.mQsbIndex == -1 || this.mLauncher.getWorkspace().isDrawingBackgroundGradient()) {
            return i;
        }
        if (i == this.mQsbIndex) {
            return this.mWorkspaceIndex;
        }
        if (i == this.mWorkspaceIndex) {
            return this.mQsbIndex;
        }
        return i;
    }

    /* access modifiers changed from: protected */
    public void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (this.mDropView != null) {
            canvas.save(1);
            int xPos = this.mDropViewPos[0] - this.mDropView.getScrollX();
            int yPos = this.mDropViewPos[1] - this.mDropView.getScrollY();
            int width = this.mDropView.getMeasuredWidth();
            int height = this.mDropView.getMeasuredHeight();
            canvas.translate((float) xPos, (float) yPos);
            canvas.translate(((1.0f - this.mDropViewScale) * ((float) width)) / 2.0f, ((1.0f - this.mDropViewScale) * ((float) height)) / 2.0f);
            canvas.scale(this.mDropViewScale, this.mDropViewScale);
            this.mDropView.setAlpha(this.mDropViewAlpha);
            this.mDropView.draw(canvas);
            canvas.restore();
        }
    }
}
