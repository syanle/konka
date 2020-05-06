package com.cyanogenmod.trebuchet;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.SystemProperties;
import android.support.v8.renderscript.Allocation;
import android.support.v8.renderscript.Element;
import android.support.v8.renderscript.RenderScript;
import android.support.v8.renderscript.ScriptIntrinsicBlur;
import android.text.Selection;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ActionMode;
import android.view.ActionMode.Callback;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.view.View.OnLongClickListener;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import com.cyanogenmod.trebuchet.DragLayer.LayoutParams;
import com.cyanogenmod.trebuchet.DropTarget.DragObject;
import com.cyanogenmod.trebuchet.RocketLauncher.Board;
import com.cyanogenmod.trebuchet.RocketLauncher.Board.FlyingIcon;
import com.cyanogenmod.trebuchet.preference.PreferencesProvider.Interface.Homescreen;
import com.enrique.stackblur.StackBlurManager;
import com.konka.ios7launcher.R;
import com.konka.kkinterface.tv.CommonDesk;
import com.tencent.stat.common.StatConstants;
import com.tencent.tvMTA.report.ReportHelper;
import java.util.ArrayList;
import java.util.Iterator;
import net.londatiga.android.ActionItem;
import net.londatiga.android.QuickAction;
import net.londatiga.android.QuickAction.OnActionItemClickListener;

public class Folder extends LinearLayout implements DragSource, OnClickListener, OnLongClickListener, DropTarget, FolderListener, OnEditorActionListener, OnFocusChangeListener, OnActionItemClickListener, OnKeyListener {
    private static final boolean DEBUG = true;
    private static final int FULL_GROW = 0;
    private static final int ID_QUICKACTION_APP_DELETE = 2;
    private static final int ID_QUICKACTION_APP_MOVE = 1;
    private static final int ID_QUICKACTION_APP_RUN = 0;
    public static final String ONLINE_GAME_PACKAGE = "com.ktcp.game";
    public static final String ONLINE_MUSIC_PACKAGE = "com.ktcp.music";
    public static final String ONLINE_PHOTO_PACKAGE = "com.ktcp.photo";
    public static final String ONLINE_VIDEO_PACKAGE = "com.ktcp.video";
    private static final int ON_EXIT_CLOSE_DELAY = 800;
    private static final int PARTIAL_GROW = 1;
    private static final int REORDER_ANIMATION_DURATION = 230;
    static final int STATE_ANIMATING = 1;
    static final int STATE_NONE = -1;
    static final int STATE_OPEN = 2;
    static final int STATE_SMALL = 0;
    public static int STATE_TO_EDIT = 0;
    public static int STATE_TO_RUN = 1;
    private static final String TAG = "Launcher.Folder";
    private static String sDefaultFolderName;
    private static final boolean sGenBluredRootView = SystemProperties.getBoolean("debug.ios7.folder.blur", false);
    private static String sHintText;
    private static final boolean sUseRenderScript = SystemProperties.getBoolean("debug.ios7.folder.blur.rs", false);
    private LinearLayout folderSurface;
    private Callback mActionModeCallback = new Callback() {
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            return false;
        }

        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        public void onDestroyActionMode(ActionMode mode) {
        }

        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }
    };
    private QuickAction mAppQuickAction;
    private ImageView mArrowDown;
    private ImageView mArrowUp;
    protected CellLayout mContent;
    private CellInfo mCurSelectCell;
    private ObjectAnimator mCurrentAnim;
    private ShortcutInfo mCurrentDragInfo;
    private View mCurrentDragView;
    private boolean mDeleteFolderOnDropCompleted = false;
    protected DragController mDragController;
    private boolean mDragInProgress = false;
    /* access modifiers changed from: private */
    public int[] mEmptyCell = new int[2];
    private int mExpandDuration;
    private FolderIcon mFolderIcon;
    private int mFolderLocationX;
    private int mFolderLocationY;
    FolderEditText mFolderName;
    private int mFolderNameHeight;
    /* access modifiers changed from: private */
    public int mFolderState = STATE_TO_RUN;
    private final IconCache mIconCache;
    private Drawable mIconDrawable;
    private Rect mIconRect = new Rect();
    private final LayoutInflater mInflater;
    protected FolderInfo mInfo;
    private InputMethodManager mInputMethodManager;
    private boolean mIsEditingName = false;
    private boolean mItemAddedBackToSelfViaIcon = false;
    private ArrayList<View> mItemsInReadingOrder = new ArrayList<>();
    boolean mItemsInvalidated = false;
    protected Launcher mLauncher;
    private int mMaxCountX;
    private int mMaxCountY;
    private int mMaxNumItems;
    private int mMode = 1;
    private Rect mNewSize = new Rect();
    private Alarm mOnExitAlarm = new Alarm();
    OnAlarmListener mOnExitAlarmListener = new OnAlarmListener() {
        public void onAlarm(Alarm alarm) {
            Folder.this.completeDragExit();
        }
    };
    private int[] mPreviousTargetCell = new int[2];
    private boolean mRearrangeOnClose = false;
    private RenderScript mRenderScript;
    private Alarm mReorderAlarm = new Alarm();
    OnAlarmListener mReorderAlarmListener = new OnAlarmListener() {
        public void onAlarm(Alarm alarm) {
            Folder.this.realTimeReorder(Folder.this.mEmptyCell, Folder.this.mTargetCell);
        }
    };
    /* access modifiers changed from: private */
    public int mState = -1;
    private boolean mSuppressFolderDeletion = false;
    boolean mSuppressOnAdd = false;
    /* access modifiers changed from: private */
    public int[] mTargetCell = new int[2];
    private Rect mTempRect = new Rect();
    /* access modifiers changed from: private */
    public ImageView mTransBg;
    Workspace mWorkspace;

    public Folder(Context context, AttributeSet attrs) {
        super(context, attrs);
        setAlwaysDrawnWithCacheEnabled(false);
        this.mInflater = LayoutInflater.from(context);
        this.mIconCache = ((LauncherApplication) context.getApplicationContext()).getIconCache();
        Resources res = getResources();
        this.mMaxCountX = res.getInteger(R.integer.folder_max_count_x);
        this.mMaxCountY = res.getInteger(R.integer.folder_max_count_y);
        this.mMaxNumItems = res.getInteger(R.integer.folder_max_num_items);
        if (this.mMaxCountX <= 0 || this.mMaxCountY <= 0 || this.mMaxNumItems <= 0) {
            this.mMaxCountX = LauncherModel.getCellCountX();
            this.mMaxCountY = LauncherModel.getCellCountY();
            this.mMaxNumItems = this.mMaxCountX * this.mMaxCountY;
        }
        this.mInputMethodManager = (InputMethodManager) this.mContext.getSystemService("input_method");
        this.mExpandDuration = res.getInteger(R.integer.config_folderAnimDuration);
        if (sDefaultFolderName == null) {
            sDefaultFolderName = res.getString(R.string.folder_name);
        }
        if (sHintText == null) {
            sHintText = res.getString(R.string.folder_hint_text);
        }
        this.mLauncher = (Launcher) context;
        setFocusableInTouchMode(true);
        this.mAppQuickAction = new QuickAction(context, 0, 3);
        initQuickActionMenu();
    }

    /* access modifiers changed from: protected */
    public void onFinishInflate() {
        super.onFinishInflate();
        this.mArrowUp = (ImageView) findViewById(R.id.arrow_up);
        this.mArrowDown = (ImageView) findViewById(R.id.arrow_down);
        this.mTransBg = new ImageView(getContext());
        this.mTransBg.setImageResource(R.color.folder_background);
        this.mContent = (CellLayout) findViewById(R.id.folder_content);
        this.mContent.setGridSize(this.mMaxCountX, this.mMaxCountY);
        this.mContent.getChildrenLayout().setMotionEventSplittingEnabled(false);
        this.mFolderName = (FolderEditText) findViewById(R.id.folder_name);
        this.mFolderName.setFolder(this);
        this.mFolderName.setOnFocusChangeListener(this);
        this.mFolderName.measure(0, 0);
        this.mFolderNameHeight = this.mFolderName.getMeasuredHeight();
        this.folderSurface = (LinearLayout) findViewById(R.id.folder_surface);
        this.mFolderName.setCustomSelectionActionModeCallback(this.mActionModeCallback);
        this.mFolderName.setOnEditorActionListener(this);
        this.mFolderName.setSelectAllOnFocus(true);
        this.mFolderName.setInputType(this.mFolderName.getInputType() | 524288 | 8192);
        if (Homescreen.getHideIconLabels(this.mLauncher)) {
            this.mFolderName.setVisibility(8);
            this.mFolderNameHeight = getPaddingBottom();
        }
        this.mFolderName.setOnKeyListener(this);
    }

    public void onClick(View v) {
        Object tag = v.getTag();
        if (tag instanceof ShortcutInfo) {
            ShortcutInfo item = (ShortcutInfo) tag;
            int[] pos = new int[2];
            v.getLocationOnScreen(pos);
            item.intent.setSourceBounds(new Rect(pos[0], pos[1], pos[0] + v.getWidth(), pos[1] + v.getHeight()));
            this.mLauncher.startActivitySafely(v, item.intent, item);
            if (((ShortcutInfo) tag).checkShortcutPackageName("com.ktcp.video")) {
                Log.v("Launcher..", "/////////");
                ReportHelper.reportApkClick(getContext(), "com.ktcp.video", "com.ktcp.video", System.currentTimeMillis());
            } else if (((ShortcutInfo) tag).checkShortcutPackageName("com.ktcp.music")) {
                Log.v("Launcher..", "/////////");
                ReportHelper.reportApkClick(getContext(), "com.ktcp.music", "com.ktcp.music", System.currentTimeMillis());
            } else if (((ShortcutInfo) tag).checkShortcutPackageName("com.ktcp.game")) {
                Log.v("Launcher..", "/////////");
                ReportHelper.reportApkClick(getContext(), "com.ktcp.game", "com.ktcp.game", System.currentTimeMillis());
            } else if (((ShortcutInfo) tag).checkShortcutPackageName("com.ktcp.photo")) {
                Log.v("Launcher..", "/////////");
                ReportHelper.reportApkClick(getContext(), "com.ktcp.photo", "com.ktcp.photo", System.currentTimeMillis());
            }
        }
    }

    private void initQuickActionMenu() {
        if (this.mAppQuickAction != null) {
            Resources r = getContext().getResources();
            new ActionItem(0, r.getString(R.string.quick_action_run));
            ActionItem moveItem = new ActionItem(1, r.getString(R.string.quick_action_move));
            ActionItem uninstallItem = new ActionItem(2, r.getString(R.string.quick_action_delete));
            this.mAppQuickAction.addActionItem(moveItem);
            this.mAppQuickAction.addActionItem(uninstallItem);
            this.mAppQuickAction.setOnActionItemClickListener(this);
            return;
        }
        Log.d(TAG, "the mIconQuickAction has not been initialized!");
    }

    public void popQuickActionMenu(CellInfo cellInfo) {
        View child = cellInfo.cell;
        if (!(child instanceof BubbleTextView)) {
            return;
        }
        if (this.mAppQuickAction != null) {
            this.mCurSelectCell = cellInfo;
            this.mAppQuickAction.show(child);
            return;
        }
        Log.d(TAG, "the mIconQuickAction is not initialized");
    }

    public void onItemClick(QuickAction source, int pos, int actionId) {
        if (source == this.mAppQuickAction) {
            switch (actionId) {
                case 0:
                    this.mLauncher.onClick(this.mCurSelectCell.cell);
                    return;
                case 1:
                    if (this.mLauncher.isItemLocked((ItemInfo) this.mCurSelectCell.cell.getTag())) {
                        this.mLauncher.warnTargetisLocked();
                        return;
                    }
                    View v = this.mCurSelectCell.cell;
                    Object tag = v.getTag();
                    if (tag instanceof ShortcutInfo) {
                        ShortcutInfo item = (ShortcutInfo) tag;
                        this.mLauncher.dismissFolderCling(null);
                        this.mLauncher.getWorkspace().onDragStartedWithItem(v);
                        this.mLauncher.getWorkspace().beginDragShared(v, this);
                        this.mIconDrawable = ((TextView) v).getCompoundDrawables()[1];
                        this.mCurrentDragInfo = item;
                        this.mEmptyCell[0] = item.cellX;
                        this.mEmptyCell[1] = item.cellY;
                        this.mCurrentDragView = v;
                        this.mContent.removeView(this.mCurrentDragView);
                        this.mInfo.remove(this.mCurrentDragInfo);
                        this.mDragInProgress = true;
                        this.mItemAddedBackToSelfViaIcon = false;
                    }
                    setFocusOnFirstChild();
                    return;
                case 2:
                    if (this.mLauncher.isItemLocked((ItemInfo) this.mCurSelectCell.cell.getTag())) {
                        this.mLauncher.warnTargetisLocked();
                        return;
                    }
                    Object tag2 = this.mCurSelectCell.cell.getTag();
                    if (tag2 instanceof ShortcutInfo) {
                        ShortcutInfo item2 = (ShortcutInfo) tag2;
                        this.mInfo.remove(item2);
                        this.mContent.removeView(this.mCurSelectCell.cell);
                        if (this.mInfo.contents.size() == 0) {
                            this.mLauncher.closeFolder();
                        } else {
                            setFocusOnFirstChild();
                        }
                        if (!this.mWorkspace.addExternalItem(item2, this.mLauncher.getWorkspace().getCurrentPageLayout(), false)) {
                            this.mWorkspace.addExternalItem(item2, false);
                            return;
                        }
                        return;
                    }
                    return;
                default:
                    return;
            }
        }
    }

    public boolean onLongClick(View v) {
        Object tag = v.getTag();
        if (tag instanceof ShortcutInfo) {
            ShortcutInfo item = (ShortcutInfo) tag;
            if (!v.isInTouchMode()) {
                return false;
            }
            this.mLauncher.dismissFolderCling(null);
            this.mLauncher.getWorkspace().onDragStartedWithItem(v);
            this.mLauncher.getWorkspace().beginDragShared(v, this);
            this.mIconDrawable = ((TextView) v).getCompoundDrawables()[1];
            this.mCurrentDragInfo = item;
            this.mEmptyCell[0] = item.cellX;
            this.mEmptyCell[1] = item.cellY;
            this.mCurrentDragView = v;
            this.mContent.removeView(this.mCurrentDragView);
            this.mInfo.remove(this.mCurrentDragInfo);
            this.mDragInProgress = true;
            this.mItemAddedBackToSelfViaIcon = false;
        }
        return true;
    }

    public boolean isEditingName() {
        return this.mIsEditingName;
    }

    public void startEditingFolderName() {
        this.mFolderName.setHint(StatConstants.MTA_COOPERATION_TAG);
        this.mIsEditingName = true;
    }

    public void dismissEditingName() {
        if (this.mFolderName.equals(StatConstants.MTA_COOPERATION_TAG)) {
            this.mFolderName.setText(getResources().getString(R.string.folder_name));
        }
        this.mInputMethodManager.hideSoftInputFromWindow(getWindowToken(), 0);
        doneEditingFolderName(true);
    }

    public void doneEditingFolderName(boolean commit) {
        this.mFolderName.setHint(sHintText);
        String newTitle = this.mFolderName.getText().toString();
        if (!Homescreen.getHideIconLabels(this.mLauncher)) {
            if (newTitle.equals(StatConstants.MTA_COOPERATION_TAG)) {
                newTitle = getResources().getString(R.string.folder_name);
            }
            this.mInfo.setTitle(newTitle);
        }
        LauncherModel.updateItemInDatabase(this.mLauncher, this.mInfo);
        if (commit) {
            sendCustomAccessibilityEvent(32, String.format(this.mContext.getString(R.string.folder_renamed), new Object[]{newTitle}));
        }
        requestFocus();
        Selection.setSelection(this.mFolderName.getText(), 0, 0);
        setFocusOnFirstChild();
        this.mIsEditingName = false;
        if (this.mInfo.contents.size() == 0) {
            this.mLauncher.closeFolder();
        }
    }

    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId != 6 && actionId != 5) {
            return false;
        }
        dismissEditingName();
        return true;
    }

    public View getEditTextRegion() {
        return this.mFolderName;
    }

    public Drawable getDragDrawable() {
        return this.mIconDrawable;
    }

    public boolean onTouchEvent(MotionEvent ev) {
        return true;
    }

    public void setDragController(DragController dragController) {
        this.mDragController = dragController;
    }

    /* access modifiers changed from: 0000 */
    public void setFolderIcon(FolderIcon icon) {
        this.mFolderIcon = icon;
    }

    public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent event) {
        return true;
    }

    /* access modifiers changed from: 0000 */
    public FolderInfo getInfo() {
        return this.mInfo;
    }

    /* access modifiers changed from: 0000 */
    public void bind(FolderInfo info) {
        this.mInfo = info;
        ArrayList<ShortcutInfo> children = info.contents;
        ArrayList<ShortcutInfo> overflow = new ArrayList<>();
        setupContentForNumItems(children.size());
        int count = 0;
        Iterator it = children.iterator();
        while (it.hasNext()) {
            ShortcutInfo child = (ShortcutInfo) it.next();
            if (!createAndAddShortcut(child)) {
                overflow.add(child);
            } else {
                count++;
            }
        }
        setupContentForNumItems(count);
        Iterator it2 = overflow.iterator();
        while (it2.hasNext()) {
            ShortcutInfo item = (ShortcutInfo) it2.next();
            this.mInfo.remove(item);
            LauncherModel.deleteItemFromDatabase(this.mLauncher, item);
        }
        this.mItemsInvalidated = true;
        updateTextViewFocus();
        this.mInfo.addListener(this);
        if (!sDefaultFolderName.contentEquals(this.mInfo.title)) {
            this.mFolderName.setText(this.mInfo.title);
        } else {
            this.mFolderName.setText(getResources().getString(R.string.folder_name));
        }
    }

    static Folder fromXml(Context context) {
        return (Folder) LayoutInflater.from(context).inflate(R.layout.user_folder, null);
    }

    private void positionAndSizeAsIcon() {
        if (getParent() instanceof DragLayer) {
            LayoutParams lp = (LayoutParams) getLayoutParams();
            if (this.mMode == 1) {
                setScaleX(0.8f);
                setScaleY(0.8f);
                setAlpha(FlyingIcon.ANGULAR_VMIN);
            } else {
                this.mLauncher.getDragLayer().getDescendantRectRelativeToSelf(this.mFolderIcon, this.mIconRect);
                lp.width = this.mIconRect.width();
                lp.height = this.mIconRect.height();
                lp.x = this.mIconRect.left;
                lp.y = this.mIconRect.top;
                this.mContent.setAlpha(FlyingIcon.ANGULAR_VMIN);
            }
            this.mState = 0;
        }
    }

    /* access modifiers changed from: private */
    public void addTransBg() {
        if (getParent() instanceof DragLayer) {
            DragLayer dragLayer = (DragLayer) getParent();
            LayoutParams param = new LayoutParams(-1, -1);
            if (this.mTransBg.getParent() == null) {
                dragLayer.addView(this.mTransBg, dragLayer.getChildCount() - 1, param);
            }
        }
    }

    /* access modifiers changed from: private */
    public void removeTransBg() {
        if (getParent() instanceof DragLayer) {
            ((DragLayer) getParent()).removeView(this.mTransBg);
        }
    }

    /* access modifiers changed from: private */
    public BitmapDrawable getBluredRootView() {
        BitmapDrawable ret;
        if (!sGenBluredRootView) {
            return null;
        }
        View rootView = getRootView();
        rootView.buildDrawingCache();
        Bitmap bmp = rootView.getDrawingCache();
        if (this.mRenderScript == null) {
            this.mRenderScript = RenderScript.create(this.mLauncher);
        }
        long startTime = System.nanoTime() / 1000000;
        if (sUseRenderScript) {
            Allocation input = Allocation.createFromBitmap(this.mRenderScript, bmp);
            Allocation output = Allocation.createTyped(this.mRenderScript, input.getType());
            ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(this.mRenderScript, Element.U8_4(this.mRenderScript));
            script.setRadius(12.0f);
            script.setInput(input);
            script.forEach(output);
            output.copyTo(bmp);
            ret = new BitmapDrawable(getResources(), bmp);
        } else {
            StackBlurManager sbm = new StackBlurManager(bmp);
            sbm.processNatively(5);
            ret = new BitmapDrawable(getResources(), sbm.returnBlurredImage());
        }
        Log.d(TAG, "wzd blur time=" + ((System.nanoTime() / 1000000) - startTime));
        return ret;
    }

    public void animateOpen(Folder folder, int[] pos) {
        ObjectAnimator oa;
        positionAndSizeAsIcon();
        if (getParent() instanceof DragLayer) {
            if (this.mCurrentAnim != null) {
                this.mCurrentAnim.cancel();
                this.mState = 0;
            }
            LayoutParams lp = (LayoutParams) getLayoutParams();
            this.mFolderLocationX = pos[0];
            this.mFolderLocationY = pos[1];
            centerAboutScreen();
            if (this.mMode == 1) {
                oa = ObjectAnimator.ofPropertyValuesHolder(this, new PropertyValuesHolder[]{PropertyValuesHolder.ofFloat("alpha", new float[]{1.0f}), PropertyValuesHolder.ofFloat("scaleX", new float[]{1.0f}), PropertyValuesHolder.ofFloat("scaleY", new float[]{1.0f})});
            } else {
                oa = ObjectAnimator.ofPropertyValuesHolder(lp, new PropertyValuesHolder[]{PropertyValuesHolder.ofInt("width", new int[]{this.mNewSize.width()}), PropertyValuesHolder.ofInt("height", new int[]{this.mNewSize.height()}), PropertyValuesHolder.ofInt("x", new int[]{this.mNewSize.left}), PropertyValuesHolder.ofInt("y", new int[]{this.mNewSize.top})});
                oa.addUpdateListener(new AnimatorUpdateListener() {
                    public void onAnimationUpdate(ValueAnimator animation) {
                        Folder.this.requestLayout();
                    }
                });
                PropertyValuesHolder alpha = PropertyValuesHolder.ofFloat("alpha", new float[]{1.0f});
                ObjectAnimator alphaOa = ObjectAnimator.ofPropertyValuesHolder(this.mContent, new PropertyValuesHolder[]{alpha});
                alphaOa.setDuration((long) this.mExpandDuration);
                alphaOa.setInterpolator(new AccelerateInterpolator(2.0f));
                alphaOa.start();
            }
            getViewTreeObserver().addOnPreDrawListener(new OnPreDrawListener() {
                public boolean onPreDraw() {
                    Folder.this.getViewTreeObserver().removeOnPreDrawListener(this);
                    BitmapDrawable bluredRootView = Folder.this.getBluredRootView();
                    if (bluredRootView != null) {
                        Folder.this.mTransBg.setBackground(bluredRootView);
                    }
                    Folder.this.addTransBg();
                    return true;
                }
            });
            oa.addListener(new AnimatorListenerAdapter() {
                public void onAnimationStart(Animator animation) {
                    Folder.this.sendCustomAccessibilityEvent(32, String.format(Folder.this.mContext.getString(R.string.folder_opened), new Object[]{Integer.valueOf(Folder.this.mContent.getCountX()), Integer.valueOf(Folder.this.mContent.getCountY())}));
                    Folder.this.mState = 1;
                }

                public void onAnimationEnd(Animator animation) {
                    Folder.this.mState = 2;
                    Cling cling = Folder.this.mLauncher.showFirstRunFoldersCling();
                    if (cling != null) {
                        cling.bringToFront();
                    }
                    if (Folder.this.mFolderName.getText().toString().equals(StatConstants.MTA_COOPERATION_TAG)) {
                        Folder.this.mFolderName.setText(Folder.this.getResources().getString(R.string.folder_name));
                    }
                    if (Folder.this.mFolderState == Folder.STATE_TO_EDIT || Folder.this.mInfo.contents.size() == 0) {
                        Folder.this.setFocusOnEditText();
                        Folder.this.mFolderState = Folder.STATE_TO_RUN;
                        return;
                    }
                    Folder.this.setFocusOnFirstChild();
                }
            });
            oa.setDuration((long) this.mExpandDuration);
            this.mCurrentAnim = oa;
            oa.start();
        }
    }

    /* access modifiers changed from: private */
    public void sendCustomAccessibilityEvent(int type, String text) {
        if (AccessibilityManager.getInstance(this.mContext).isEnabled()) {
            AccessibilityEvent event = AccessibilityEvent.obtain(type);
            onInitializeAccessibilityEvent(event);
            event.getText().add(text);
            AccessibilityManager.getInstance(this.mContext).sendAccessibilityEvent(event);
        }
    }

    public void setFocusOnFirstChild() {
        View firstChild = this.mContent.getChildAt(0, 0);
        if (firstChild != null) {
            firstChild.requestFocus();
        }
    }

    /* access modifiers changed from: private */
    public void setFocusOnEditText() {
        this.mFolderName.requestFocus();
    }

    public void animateClosed() {
        ObjectAnimator oa;
        if (getParent() instanceof DragLayer) {
            if (this.mAppQuickAction != null && this.mAppQuickAction.isShowing()) {
                this.mAppQuickAction.dismiss();
            }
            if (this.mCurrentAnim != null) {
                this.mCurrentAnim.cancel();
                this.mState = 2;
            }
            if (this.mMode == 1) {
                oa = ObjectAnimator.ofPropertyValuesHolder(this, new PropertyValuesHolder[]{PropertyValuesHolder.ofFloat("alpha", new float[]{0.0f}), PropertyValuesHolder.ofFloat("scaleX", new float[]{0.9f}), PropertyValuesHolder.ofFloat("scaleY", new float[]{0.9f})});
            } else {
                oa = ObjectAnimator.ofPropertyValuesHolder((LayoutParams) getLayoutParams(), new PropertyValuesHolder[]{PropertyValuesHolder.ofInt("width", new int[]{this.mIconRect.width()}), PropertyValuesHolder.ofInt("height", new int[]{this.mIconRect.height()}), PropertyValuesHolder.ofInt("x", new int[]{this.mIconRect.left}), PropertyValuesHolder.ofInt("y", new int[]{this.mIconRect.top})});
                oa.addUpdateListener(new AnimatorUpdateListener() {
                    public void onAnimationUpdate(ValueAnimator animation) {
                        Folder.this.requestLayout();
                    }
                });
                PropertyValuesHolder alpha = PropertyValuesHolder.ofFloat("alpha", new float[]{0.0f});
                ObjectAnimator alphaOa = ObjectAnimator.ofPropertyValuesHolder(this.mContent, new PropertyValuesHolder[]{alpha});
                alphaOa.setDuration((long) this.mExpandDuration);
                alphaOa.setInterpolator(new DecelerateInterpolator(2.0f));
                alphaOa.start();
            }
            oa.addListener(new AnimatorListenerAdapter() {
                public void onAnimationEnd(Animator animation) {
                    Folder.this.onCloseComplete();
                    Folder.this.mState = 0;
                }

                public void onAnimationStart(Animator animation) {
                    Folder.this.sendCustomAccessibilityEvent(32, Folder.this.mContext.getString(R.string.folder_closed));
                    Folder.this.mState = 1;
                    Folder.this.removeTransBg();
                }
            });
            oa.setDuration((long) this.mExpandDuration);
            this.mCurrentAnim = oa;
            oa.start();
        }
    }

    /* access modifiers changed from: 0000 */
    public void notifyDataSetChanged() {
        this.mContent.removeAllViewsInLayout();
        bind(this.mInfo);
    }

    public boolean acceptDrop(DragObject d) {
        int itemType = ((ItemInfo) d.dragInfo).itemType;
        if ((itemType == 0 || itemType == 1) && checkFolderAdd(1)) {
            return true;
        }
        return false;
    }

    /* access modifiers changed from: protected */
    public boolean findAndSetEmptyCells(ShortcutInfo item) {
        int[] emptyCell = new int[2];
        if (!this.mContent.findCellForFolderSpan(emptyCell, item.spanX, item.spanY)) {
            return false;
        }
        item.cellX = emptyCell[0];
        item.cellY = emptyCell[1];
        return true;
    }

    /* access modifiers changed from: protected */
    public boolean createAndAddShortcut(ShortcutInfo item) {
        TextView textView = (TextView) this.mInflater.inflate(R.layout.application, this, false);
        textView.setCompoundDrawablesWithIntrinsicBounds(null, new FastBitmapDrawable(item.getIcon(this.mIconCache)), null, null);
        textView.setText(item.title);
        textView.setTag(item);
        textView.setOnClickListener(this);
        textView.setOnLongClickListener(this);
        if ((this.mContent.getChildAt(item.cellX, item.cellY) != null || item.cellX < 0 || item.cellY < 0 || item.cellX >= this.mContent.getCountX() || item.cellY >= this.mContent.getCountY()) && !findAndSetEmptyCells(item)) {
            return false;
        }
        if (item.checkShortcutPackageName(MessageManager.MESSAGE_PACKAGE_NAME)) {
            Log.d(TAG, "create message folder shortcut");
            MessageManager messageManager = MessageManager.getInstance();
            messageManager.setMessageView((BubbleTextView) textView);
            messageManager.setFolderInfo(this.mInfo);
        }
        if (item.checkShortcutPackageName("com.konka.market.main")) {
            Log.d(TAG, "create market folder shortcut");
            MarketManager marketManager = MarketManager.getInstance();
            marketManager.setMarketView((BubbleTextView) textView);
            marketManager.setMarketFolderInfo(this.mInfo);
        }
        CellLayout.LayoutParams lp = new CellLayout.LayoutParams(item.cellX, item.cellY, item.spanX, item.spanY);
        textView.setOnKeyListener(new FolderKeyEventListener(this.mLauncher));
        Log.d("czj-ios", "Folder.createAndAddShortcut -> CellLayout.addViewToCellLayout");
        this.mContent.addViewToCellLayout(textView, -1, (int) item.id, lp, true);
        return true;
    }

    public void onDragEnter(DragObject d) {
        this.mPreviousTargetCell[0] = -1;
        this.mPreviousTargetCell[1] = -1;
        this.mOnExitAlarm.cancelAlarm();
    }

    /* access modifiers changed from: 0000 */
    public boolean readingOrderGreaterThan(int[] v1, int[] v2) {
        return v1[1] > v2[1] || (v1[1] == v2[1] && v1[0] > v2[0]);
    }

    /* access modifiers changed from: private */
    public void realTimeReorder(int[] empty, int[] target) {
        int delay = 0;
        float delayAmount = 30.0f;
        if (readingOrderGreaterThan(target, empty)) {
            int y = empty[0] >= this.mContent.getCountX() + -1 ? empty[1] + 1 : empty[1];
            while (y <= target[1]) {
                int startX = y == empty[1] ? empty[0] + 1 : 0;
                int endX = y < target[1] ? this.mContent.getCountX() - 1 : target[0];
                for (int x = startX; x <= endX; x++) {
                    if (this.mContent.animateChildToPosition(this.mContent.getChildAt(x, y), empty[0], empty[1], REORDER_ANIMATION_DURATION, delay)) {
                        empty[0] = x;
                        empty[1] = y;
                        delay = (int) (((float) delay) + delayAmount);
                        delayAmount = (float) (((double) delayAmount) * 0.9d);
                    }
                }
                y++;
            }
            return;
        }
        int y2 = empty[0] == 0 ? empty[1] - 1 : empty[1];
        while (y2 >= target[1]) {
            int startX2 = y2 == empty[1] ? empty[0] - 1 : this.mContent.getCountX() - 1;
            int endX2 = y2 > target[1] ? 0 : target[0];
            for (int x2 = startX2; x2 >= endX2; x2--) {
                if (this.mContent.animateChildToPosition(this.mContent.getChildAt(x2, y2), empty[0], empty[1], REORDER_ANIMATION_DURATION, delay)) {
                    empty[0] = x2;
                    empty[1] = y2;
                    delay = (int) (((float) delay) + delayAmount);
                    delayAmount = (float) (((double) delayAmount) * 0.9d);
                }
            }
            y2--;
        }
    }

    public void onDragOver(DragObject d) {
        float[] r = getDragViewVisualCenter(d.x, d.y, d.xOffset, d.yOffset, d.dragView, null);
        this.mTargetCell = this.mContent.findNearestArea((int) r[0], (int) r[1], 1, 1, this.mTargetCell);
        if (this.mTargetCell[0] != this.mPreviousTargetCell[0] || this.mTargetCell[1] != this.mPreviousTargetCell[1]) {
            this.mReorderAlarm.cancelAlarm();
            this.mReorderAlarm.setOnAlarmListener(this.mReorderAlarmListener);
            this.mReorderAlarm.setAlarm(150);
            this.mPreviousTargetCell[0] = this.mTargetCell[0];
            this.mPreviousTargetCell[1] = this.mTargetCell[1];
        }
    }

    private float[] getDragViewVisualCenter(int x, int y, int xOffset, int yOffset, DragView dragView, float[] recycle) {
        float[] res;
        if (recycle == null) {
            res = new float[2];
        } else {
            res = recycle;
        }
        int top = y - yOffset;
        res[0] = (float) ((dragView.getDragRegion().width() / 2) + (x - xOffset));
        res[1] = (float) ((dragView.getDragRegion().height() / 2) + top);
        return res;
    }

    public void completeDragExit() {
        this.mLauncher.closeFolder();
        this.mCurrentDragInfo = null;
        this.mCurrentDragView = null;
        this.mSuppressOnAdd = false;
        this.mRearrangeOnClose = true;
    }

    public void onDragExit(DragObject d) {
        if (!d.dragComplete) {
            this.mOnExitAlarm.setOnAlarmListener(this.mOnExitAlarmListener);
            this.mOnExitAlarm.setAlarm(800);
        }
        this.mReorderAlarm.cancelAlarm();
    }

    public void onDropCompleted(View target, DragObject d, boolean success) {
        if (!success) {
            this.mFolderIcon.onDrop(d);
            if (this.mOnExitAlarm.alarmPending()) {
                this.mSuppressFolderDeletion = true;
            }
        } else if (this.mDeleteFolderOnDropCompleted && !this.mItemAddedBackToSelfViaIcon) {
            replaceFolderWithFinalItem();
        }
        if (target != this && this.mOnExitAlarm.alarmPending()) {
            this.mOnExitAlarm.cancelAlarm();
            completeDragExit();
        }
        this.mDeleteFolderOnDropCompleted = false;
        this.mDragInProgress = false;
        this.mItemAddedBackToSelfViaIcon = false;
        this.mCurrentDragInfo = null;
        this.mCurrentDragView = null;
        this.mSuppressOnAdd = false;
        updateItemLocationsInDatabase();
    }

    private void updateItemLocationsInDatabase() {
        Iterator it = getItemsInReadingOrder().iterator();
        while (it.hasNext()) {
            ItemInfo info = (ItemInfo) ((View) it.next()).getTag();
            LauncherModel.moveItemInDatabase(this.mLauncher, info, this.mInfo.id, 0, info.cellX, info.cellY);
        }
    }

    public void notifyDrop() {
        if (this.mDragInProgress) {
            this.mItemAddedBackToSelfViaIcon = true;
        }
    }

    public boolean isDropEnabled() {
        return true;
    }

    public DropTarget getDropTargetDelegate(DragObject d) {
        return null;
    }

    private void setupContentDimensions(int count) {
        arrangeChildren(getItemsInReadingOrder());
    }

    public boolean checkFolderAdd(int itemNumber) {
        return getItemCount() + itemNumber <= this.mMaxNumItems;
    }

    private void centerAboutIcon() {
        LayoutParams lp = (LayoutParams) getLayoutParams();
        int width = getPaddingLeft() + getPaddingRight() + this.mContent.getDesiredWidth();
        int height = getPaddingTop() + getPaddingBottom() + this.mContent.getDesiredHeight() + this.mFolderNameHeight;
        DragLayer parent = (DragLayer) this.mLauncher.findViewById(R.id.drag_layer);
        parent.getDescendantRectRelativeToSelf(this.mFolderIcon, this.mTempRect);
        int centerX = this.mTempRect.centerX();
        int centeredLeft = centerX - (width / 2);
        int centeredTop = this.mTempRect.centerY() - (height / 2);
        CellLayoutChildren boundingLayout = this.mLauncher.getWorkspace().getCurrentDropLayout().getChildrenLayout();
        Rect bounds = new Rect();
        parent.getDescendantRectRelativeToSelf(boundingLayout, bounds);
        int left = Math.min(Math.max(bounds.left, centeredLeft), (bounds.left + bounds.width()) - width);
        int top = Math.min(Math.max(bounds.top, centeredTop), (bounds.top + bounds.height()) - height);
        if (width >= bounds.width()) {
            left = bounds.left + ((bounds.width() - width) / 2);
        }
        if (height >= bounds.height()) {
            top = bounds.top + ((bounds.height() - height) / 2);
        }
        int folderPivotX = (width / 2) + (centeredLeft - left);
        int folderPivotY = (height / 2) + (centeredTop - top);
        setPivotX((float) folderPivotX);
        setPivotY((float) folderPivotY);
        int folderIconPivotY = (int) (((float) this.mFolderIcon.getMeasuredHeight()) * ((1.0f * ((float) folderPivotY)) / ((float) height)));
        this.mFolderIcon.setPivotX((float) ((int) (((float) this.mFolderIcon.getMeasuredWidth()) * ((1.0f * ((float) folderPivotX)) / ((float) width)))));
        this.mFolderIcon.setPivotY((float) folderIconPivotY);
        if (this.mMode == 1) {
            lp.width = width;
            lp.height = height;
            lp.x = left;
            lp.y = top;
            return;
        }
        this.mNewSize.set(left, top, left + width, top + height);
    }

    private void centerAboutScreen() {
        LayoutParams lp = (LayoutParams) getLayoutParams();
        int width = getPaddingLeft() + getPaddingRight() + this.mContent.getDesiredWidth();
        int height = getPaddingTop() + getPaddingBottom() + this.mContent.getDesiredHeight() + this.mFolderNameHeight;
        int left = (Launcher.getWidthPixels() - width) / 2;
        int top = (Launcher.getHeightPixels() - height) / 2;
        if (this.mMode == 1) {
            lp.width = width;
            lp.height = height;
            lp.x = left;
            lp.y = top;
            return;
        }
        this.mNewSize.set(left, top, left + width, top + height);
    }

    private void setupContentForNumItems(int count) {
        setupContentDimensions(count);
        if (((LayoutParams) getLayoutParams()) == null) {
            LayoutParams lp = new LayoutParams(0, 0);
            lp.customPosition = true;
            setLayoutParams(lp);
        }
        centerAboutScreen();
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = getPaddingLeft() + getPaddingRight() + this.mContent.getDesiredWidth();
        int height = getPaddingTop() + getPaddingBottom() + this.mContent.getDesiredHeight() + this.mFolderNameHeight;
        Log.d(TAG, "[wzd] folder width,height = " + width + ", " + height);
        int contentWidthSpec = MeasureSpec.makeMeasureSpec(this.mContent.getDesiredWidth(), 1073741824);
        this.mContent.measure(contentWidthSpec, MeasureSpec.makeMeasureSpec(this.mContent.getDesiredHeight(), 1073741824));
        this.mFolderName.measure(contentWidthSpec, MeasureSpec.makeMeasureSpec(this.mFolderNameHeight, 1073741824));
        setMeasuredDimension(width, height);
    }

    private void arrangeChildren(ArrayList<View> list) {
        int[] vacant = new int[2];
        if (list == null) {
            list = getItemsInReadingOrder();
        }
        this.mContent.removeAllViews();
        Iterator it = list.iterator();
        while (it.hasNext()) {
            View v = (View) it.next();
            this.mContent.getVacantCell(vacant, 1, 1);
            CellLayout.LayoutParams lp = (CellLayout.LayoutParams) v.getLayoutParams();
            lp.cellX = vacant[0];
            lp.cellY = vacant[1];
            ItemInfo info = (ItemInfo) v.getTag();
            if (info.cellX != vacant[0] || info.cellY != vacant[1]) {
                info.cellX = vacant[0];
                info.cellY = vacant[1];
                LauncherModel.addOrMoveItemInDatabase(this.mLauncher, info, this.mInfo.id, 0, info.cellX, info.cellY);
            }
            Log.d("czj-ios", "Folder.arrangeChildren -> CellLayout.addViewToCellLayout");
            this.mContent.addViewToCellLayout(v, -1, (int) info.id, lp, true);
        }
        this.mItemsInvalidated = true;
    }

    public int getItemCount() {
        return this.mContent.getChildrenLayout().getChildCount();
    }

    public View getItemAt(int index) {
        return this.mContent.getChildrenLayout().getChildAt(index);
    }

    /* access modifiers changed from: private */
    public void onCloseComplete() {
        DragLayer parent = (DragLayer) getParent();
        if (parent != null) {
            parent.removeView(this);
        }
        this.mDragController.removeDropTarget(this);
        clearFocus();
        this.mFolderIcon.requestFocus();
        if (this.mRearrangeOnClose) {
            setupContentForNumItems(getItemCount());
            this.mRearrangeOnClose = false;
        }
        if (getItemCount() <= 0) {
            if (!this.mDragInProgress && !this.mSuppressFolderDeletion) {
                replaceFolderWithFinalItem();
            } else if (this.mDragInProgress) {
                this.mDeleteFolderOnDropCompleted = true;
            }
        }
        this.mSuppressFolderDeletion = false;
    }

    private void replaceFolderWithFinalItem() {
        ItemInfo finalItem = null;
        if (getItemCount() == 1) {
            finalItem = (ItemInfo) this.mInfo.contents.get(0);
        }
        CellLayout cellLayout = this.mLauncher.getCellLayout(this.mInfo.container, this.mInfo.screen);
        cellLayout.removeView(this.mFolderIcon);
        if (this.mFolderIcon instanceof DropTarget) {
            this.mDragController.removeDropTarget((DropTarget) this.mFolderIcon);
        }
        this.mLauncher.removeFolder(this.mInfo);
        if (finalItem != null) {
            LauncherModel.addOrMoveItemInDatabase(this.mLauncher, finalItem, this.mInfo.container, this.mInfo.screen, this.mInfo.cellX, this.mInfo.cellY);
        }
        LauncherModel.deleteItemFromDatabase(this.mLauncher, this.mInfo);
        if (finalItem != null) {
            this.mLauncher.getWorkspace().addInScreen(this.mLauncher.createShortcut(R.layout.application, cellLayout, (ShortcutInfo) finalItem), this.mInfo.container, this.mInfo.screen, this.mInfo.cellX, this.mInfo.cellY, this.mInfo.spanX, this.mInfo.spanY);
        }
    }

    private void updateTextViewFocus() {
        View lastChild = getItemAt(getItemCount() - 1);
        getItemAt(getItemCount() - 1);
        if (lastChild != null) {
            this.mFolderName.setNextFocusDownId(lastChild.getId());
            this.mFolderName.setNextFocusRightId(lastChild.getId());
            this.mFolderName.setNextFocusLeftId(lastChild.getId());
            this.mFolderName.setNextFocusUpId(lastChild.getId());
        }
    }

    public void onDrop(DragObject d) {
        ShortcutInfo item;
        if (d.dragInfo instanceof ApplicationInfo) {
            item = ((ApplicationInfo) d.dragInfo).makeShortcut();
            item.spanX = 1;
            item.spanY = 1;
        } else {
            item = (ShortcutInfo) d.dragInfo;
        }
        if (item == this.mCurrentDragInfo) {
            ShortcutInfo si = (ShortcutInfo) this.mCurrentDragView.getTag();
            CellLayout.LayoutParams lp = (CellLayout.LayoutParams) this.mCurrentDragView.getLayoutParams();
            int i = this.mEmptyCell[0];
            lp.cellX = i;
            si.cellX = i;
            int i2 = this.mEmptyCell[1];
            lp.cellY = i2;
            si.cellX = i2;
            Log.d("czj-ios", "Folder.onDrop -> CellLayout.addViewToCellLayout");
            this.mContent.addViewToCellLayout(this.mCurrentDragView, -1, (int) item.id, lp, true);
            if (d.dragView.hasDrawn()) {
                this.mLauncher.getDragLayer().animateViewIntoPosition(d.dragView, this.mCurrentDragView);
            } else {
                this.mCurrentDragView.setVisibility(0);
            }
            this.mItemsInvalidated = true;
            setupContentDimensions(getItemCount());
            this.mSuppressOnAdd = true;
        }
        this.mInfo.add(item);
        if (!this.mCurrentDragView.isInTouchMode()) {
            this.mCurrentDragView.setVisibility(0);
            this.mCurrentDragView.requestFocus();
        }
    }

    public void onAdd(ShortcutInfo item) {
        this.mItemsInvalidated = true;
        if (!this.mSuppressOnAdd) {
            if (!findAndSetEmptyCells(item)) {
                setupContentForNumItems(getItemCount() + 1);
                findAndSetEmptyCells(item);
            }
            createAndAddShortcut(item);
            LauncherModel.addOrMoveItemInDatabase(this.mLauncher, item, this.mInfo.id, 0, item.cellX, item.cellY);
        }
    }

    public void onRemove(ShortcutInfo item) {
        this.mItemsInvalidated = true;
        if (item != this.mCurrentDragInfo) {
            this.mContent.removeView(getViewForInfo(item));
            if (this.mState == 1) {
                this.mRearrangeOnClose = true;
            } else {
                setupContentForNumItems(getItemCount());
            }
            if (getItemCount() <= 0) {
                replaceFolderWithFinalItem();
            }
        }
    }

    private View getViewForInfo(ShortcutInfo item) {
        for (int j = 0; j < this.mContent.getCountY(); j++) {
            for (int i = 0; i < this.mContent.getCountX(); i++) {
                View v = this.mContent.getChildAt(i, j);
                if (v.getTag() == item) {
                    return v;
                }
            }
        }
        return null;
    }

    public void onItemsChanged() {
        updateTextViewFocus();
    }

    public void onTitleChanged(CharSequence title) {
    }

    public ArrayList<View> getItemsInReadingOrder() {
        return getItemsInReadingOrder(true);
    }

    public ArrayList<View> getItemsInReadingOrder(boolean includeCurrentDragItem) {
        if (this.mItemsInvalidated) {
            this.mItemsInReadingOrder.clear();
            for (int j = 0; j < this.mContent.getCountY(); j++) {
                for (int i = 0; i < this.mContent.getCountX(); i++) {
                    View v = this.mContent.getChildAt(i, j);
                    if (v != null && (((ShortcutInfo) v.getTag()) != this.mCurrentDragInfo || includeCurrentDragItem)) {
                        this.mItemsInReadingOrder.add(v);
                    }
                }
            }
            this.mItemsInvalidated = false;
        }
        return this.mItemsInReadingOrder;
    }

    public void getLocationInDragLayer(int[] loc) {
        this.mLauncher.getDragLayer().getLocationInDragLayer(this, loc);
    }

    public void onFocusChange(View v, boolean hasFocus) {
        if (v == this.mFolderName && hasFocus) {
            startEditingFolderName();
        }
    }

    public int getFolderState() {
        return this.mFolderState;
    }

    public void setFolderState(int state) {
        this.mFolderState = state;
    }

    public boolean onKey(View v, int keyCode, KeyEvent keyEvent) {
        if (keyEvent.getAction() == 0) {
            switch (keyCode) {
                case 19:
                case Board.NUM_ICONS /*20*/:
                    if (isEditingName()) {
                        if (this.mInfo.contents.size() == 0) {
                            return true;
                        }
                        dismissEditingName();
                        return true;
                    }
                    break;
                case 21:
                    if (isEditingName() && ((this.mInfo.contents.size() == 0 && this.mFolderName.getText().toString().length() == 0) || this.mFolderName.getSelectionStart() == 0)) {
                        if (this.mInfo.contents.size() == 0) {
                            return true;
                        }
                        dismissEditingName();
                        return true;
                    }
                case 22:
                    if (isEditingName() && ((this.mInfo.contents.size() == 0 && this.mFolderName.getText().toString().length() == 0) || this.mFolderName.getSelectionStart() == this.mFolderName.getText().toString().length())) {
                        if (this.mInfo.contents.size() == 0) {
                            return true;
                        }
                        dismissEditingName();
                        return true;
                    }
                case CommonDesk.Cmd_SignalLock /*66*/:
                    if (this.mInfo.contents.size() == 0) {
                        dismissEditingName();
                        return true;
                    }
                    break;
            }
        }
        return false;
    }
}
