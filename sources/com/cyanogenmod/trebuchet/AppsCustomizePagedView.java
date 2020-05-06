package com.cyanogenmod.trebuchet;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProviderInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.Rect;
import android.graphics.TableMaskFilter;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.Toast;
import com.cyanogenmod.trebuchet.AppsCustomizeView.ContentType;
import com.cyanogenmod.trebuchet.AppsCustomizeView.SortMode;
import com.cyanogenmod.trebuchet.DropTarget.DragObject;
import com.cyanogenmod.trebuchet.LauncherModel.WidgetAndShortcutNameComparator;
import com.cyanogenmod.trebuchet.RocketLauncher.Board.FlyingIcon;
import com.cyanogenmod.trebuchet.preference.PreferencesProvider.Interface.Drawer;
import com.cyanogenmod.trebuchet.preference.PreferencesProvider.Interface.Drawer.Indicator;
import com.cyanogenmod.trebuchet.preference.PreferencesProvider.Interface.Drawer.Scrolling;
import com.konka.android.tv.KKCommonManager;
import com.konka.android.tv.KKCommonManager.EN_KK_LAUNCHER_CONFIG_FILE_TYPE;
import com.konka.ios7launcher.R;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import net.londatiga.android.ActionItem;
import net.londatiga.android.QuickAction;
import net.londatiga.android.QuickAction.OnActionItemClickListener;

public class AppsCustomizePagedView extends PagedViewWithDraggableItems implements AppsCustomizeView, OnClickListener, OnKeyListener, DragSource, OnActionItemClickListener {
    private static /* synthetic */ int[] $SWITCH_TABLE$com$cyanogenmod$trebuchet$AppsCustomizePagedView$TransitionEffect = null;
    private static /* synthetic */ int[] $SWITCH_TABLE$com$cyanogenmod$trebuchet$AppsCustomizeView$ContentType = null;
    private static final float CAMERA_DISTANCE = 6500.0f;
    private static final int ID_QUICKACTION_ADD_TO_HOMEPAGE = 1;
    private static final int ID_QUICKACTION_RUN = 0;
    private static final int ID_QUICKACTION_UNINSTALL = 2;
    static final String LOG_TAG = "AppsCustomizePagedView";
    private static final boolean PERFORM_OVERSCROLL_ROTATION = true;
    private static final float TRANSITION_MAX_ROTATION = 22.0f;
    private static final float TRANSITION_PIVOT = 0.65f;
    private static final float TRANSITION_SCALE_FACTOR = 0.74f;
    private static final float TRANSITION_SCREEN_ROTATION = 12.5f;
    static final int sLookAheadPageCount = 2;
    static final int sLookBehindPageCount = 2;
    private static final int sPageSleepDelay = 200;
    private AccelerateInterpolator mAlphaInterpolator = new AccelerateInterpolator(0.9f);
    private int mAppIconSize;
    private String mAppListFileName = "applist";
    private ArrayList<ApplicationInfo> mApps;
    private Canvas mCanvas;
    private int mClingFocusedX;
    private int mClingFocusedY;
    /* access modifiers changed from: private */
    public ContentType mContentType;
    private int mContentWidth;
    private View mCurSelectView;
    private Drawable mDefaultWidgetBackground;
    private DragController mDragController;
    private int mDragViewMultiplyColor;
    private boolean mFadeScrollingIndicator;
    private boolean mHasShownAllAppsCling;
    private boolean mHasShownAllAppsSortCling;
    private HolographicOutlineHelper mHolographicOutlineHelper;
    private IconCache mIconCache;
    /* access modifiers changed from: private */
    public boolean mJoinWidgetsApps;
    /* access modifiers changed from: private */
    public Launcher mLauncher;
    private final LayoutInflater mLayoutInflater;
    private DecelerateInterpolator mLeftScreenAlphaInterpolator = new DecelerateInterpolator(4.0f);
    private int mMaxAppCellCountX;
    private int mMaxAppCellCountY;
    private int mNumAppsPages = 0;
    private int mNumWidgetPages = 0;
    private boolean mOverscrollTransformsDirty = false;
    private final PackageManager mPackageManager;
    private final QuickAction mQuickAction;
    ArrayList<AppsCustomizeAsyncTask> mRunningTasks;
    private int mSaveInstanceStateItemIndex = -1;
    private boolean mShowScrollingIndicator;
    private SortMode mSortMode = SortMode.KKDefault;
    private TransitionEffect mTransitionEffect;
    private int mWidgetCountX;
    private int mWidgetCountY;
    private int mWidgetHeightGap;
    private final int mWidgetPreviewIconPaddedDimension;
    private PagedViewCellLayout mWidgetSpacingLayout;
    private int mWidgetWidthGap;
    private ArrayList<Object> mWidgets;
    ZInterpolator mZInterpolator = new ZInterpolator(0.5f);
    private final float sWidgetPreviewIconPaddingPercentage = 0.25f;

    public enum TransitionEffect {
        Standard,
        Tablet,
        ZoomIn,
        ZoomOut,
        RotateUp,
        RotateDown,
        CubeIn,
        CubeOut,
        Stack
    }

    static /* synthetic */ int[] $SWITCH_TABLE$com$cyanogenmod$trebuchet$AppsCustomizePagedView$TransitionEffect() {
        int[] iArr = $SWITCH_TABLE$com$cyanogenmod$trebuchet$AppsCustomizePagedView$TransitionEffect;
        if (iArr == null) {
            iArr = new int[TransitionEffect.values().length];
            try {
                iArr[TransitionEffect.CubeIn.ordinal()] = 7;
            } catch (NoSuchFieldError e) {
            }
            try {
                iArr[TransitionEffect.CubeOut.ordinal()] = 8;
            } catch (NoSuchFieldError e2) {
            }
            try {
                iArr[TransitionEffect.RotateDown.ordinal()] = 6;
            } catch (NoSuchFieldError e3) {
            }
            try {
                iArr[TransitionEffect.RotateUp.ordinal()] = 5;
            } catch (NoSuchFieldError e4) {
            }
            try {
                iArr[TransitionEffect.Stack.ordinal()] = 9;
            } catch (NoSuchFieldError e5) {
            }
            try {
                iArr[TransitionEffect.Standard.ordinal()] = 1;
            } catch (NoSuchFieldError e6) {
            }
            try {
                iArr[TransitionEffect.Tablet.ordinal()] = 2;
            } catch (NoSuchFieldError e7) {
            }
            try {
                iArr[TransitionEffect.ZoomIn.ordinal()] = 3;
            } catch (NoSuchFieldError e8) {
            }
            try {
                iArr[TransitionEffect.ZoomOut.ordinal()] = 4;
            } catch (NoSuchFieldError e9) {
            }
            $SWITCH_TABLE$com$cyanogenmod$trebuchet$AppsCustomizePagedView$TransitionEffect = iArr;
        }
        return iArr;
    }

    static /* synthetic */ int[] $SWITCH_TABLE$com$cyanogenmod$trebuchet$AppsCustomizeView$ContentType() {
        int[] iArr = $SWITCH_TABLE$com$cyanogenmod$trebuchet$AppsCustomizeView$ContentType;
        if (iArr == null) {
            iArr = new int[ContentType.values().length];
            try {
                iArr[ContentType.Apps.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                iArr[ContentType.Widgets.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            $SWITCH_TABLE$com$cyanogenmod$trebuchet$AppsCustomizeView$ContentType = iArr;
        }
        return iArr;
    }

    public AppsCustomizePagedView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mLayoutInflater = LayoutInflater.from(context);
        this.mPackageManager = context.getPackageManager();
        this.mContentType = ContentType.Apps;
        this.mApps = new ArrayList<>();
        this.mWidgets = new ArrayList<>();
        this.mIconCache = ((LauncherApplication) context.getApplicationContext()).getIconCache();
        this.mHolographicOutlineHelper = new HolographicOutlineHelper();
        this.mCanvas = new Canvas();
        this.mRunningTasks = new ArrayList<>();
        Resources resources = context.getResources();
        this.mDefaultWidgetBackground = resources.getDrawable(R.drawable.default_widget_preview_holo);
        this.mAppIconSize = resources.getDimensionPixelSize(R.dimen.app_icon_size);
        this.mDragViewMultiplyColor = resources.getColor(R.color.drag_view_multiply_color);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.AppsCustomizePagedView, 0, 0);
        this.mMaxAppCellCountX = a.getInt(0, -1);
        this.mMaxAppCellCountY = a.getInt(1, -1);
        this.mWidgetWidthGap = a.getDimensionPixelSize(2, 0);
        this.mWidgetHeightGap = a.getDimensionPixelSize(3, 0);
        this.mWidgetCountX = a.getInt(4, 2);
        this.mWidgetCountY = a.getInt(5, 2);
        this.mClingFocusedX = a.getInt(6, 0);
        this.mClingFocusedY = a.getInt(7, 0);
        a.recycle();
        this.mWidgetSpacingLayout = new PagedViewCellLayout(getContext());
        this.mWidgetPreviewIconPaddedDimension = (int) (((float) this.mAppIconSize) * 1.5f);
        this.mHandleFadeInAdjacentScreens = true;
        this.mJoinWidgetsApps = Drawer.getJoinWidgetsApps(context);
        this.mTransitionEffect = Scrolling.getTransitionEffect(context, resources.getString(R.string.config_drawerDefaultTransitionEffect));
        this.mFadeInAdjacentScreens = Scrolling.getFadeInAdjacentScreens(context);
        this.mShowScrollingIndicator = Indicator.getShowScrollingIndicator(context);
        this.mFadeScrollingIndicator = Indicator.getFadeScrollingIndicator(context);
        if (!this.mShowScrollingIndicator) {
            disableScrollingIndicator();
        }
        this.mQuickAction = new QuickAction(context, 0, 3);
        initQuickActionMenu();
        loadAppListFile();
    }

    /* access modifiers changed from: protected */
    public void init() {
        super.init();
        this.mCenterPagesVertically = false;
        setDragSlopeThreshold(((float) getContext().getResources().getInteger(R.integer.config_appsCustomizeDragSlopeThreshold)) / 100.0f);
    }

    /* access modifiers changed from: protected */
    public void onUnhandledTap(MotionEvent ev) {
        LauncherApplication.isScreenLarge();
    }

    private void initQuickActionMenu() {
        if (this.mQuickAction != null) {
            Resources r = getContext().getResources();
            ActionItem runItem = new ActionItem(0, r.getString(R.string.quick_action_run));
            ActionItem addItem = new ActionItem(1, r.getString(R.string.quick_action_add_to_homepage));
            ActionItem uninstallItem = new ActionItem(2, r.getString(R.string.quick_action_uninstall));
            this.mQuickAction.addActionItem(runItem);
            this.mQuickAction.addActionItem(addItem);
            this.mQuickAction.addActionItem(uninstallItem);
            this.mQuickAction.setOnActionItemClickListener(this);
            return;
        }
        Log.d(LOG_TAG, "the quickAciton has been initialized!");
    }

    private void popQuickActionMenu(View v) {
        if (this.mQuickAction != null) {
            this.mCurSelectView = v;
            this.mQuickAction.show(v);
        }
    }

    private int getMiddleComponentIndexOnCurrentPage() {
        if (getPageCount() <= 0) {
            return -1;
        }
        int currentPage = getCurrentPage();
        if (!this.mJoinWidgetsApps) {
            switch ($SWITCH_TABLE$com$cyanogenmod$trebuchet$AppsCustomizeView$ContentType()[this.mContentType.ordinal()]) {
                case 1:
                    int numItemsPerPage = this.mCellCountX * this.mCellCountY;
                    int childCount = ((PagedViewCellLayout) getPageAt(currentPage)).getChildrenLayout().getChildCount();
                    if (childCount > 0) {
                        return (currentPage * numItemsPerPage) + (childCount / 2);
                    }
                    return -1;
                case 2:
                    int numItemsPerPage2 = this.mWidgetCountX * this.mWidgetCountY;
                    int childCount2 = ((PagedViewGridLayout) getPageAt(currentPage)).getChildCount();
                    if (childCount2 > 0) {
                        return (currentPage * numItemsPerPage2) + (childCount2 / 2);
                    }
                    return -1;
                default:
                    return -1;
            }
        } else if (currentPage < this.mNumAppsPages) {
            int numItemsPerPage3 = this.mCellCountX * this.mCellCountY;
            int childCount3 = ((PagedViewCellLayout) getPageAt(currentPage)).getChildrenLayout().getChildCount();
            if (childCount3 > 0) {
                return (currentPage * numItemsPerPage3) + (childCount3 / 2);
            }
            return -1;
        } else {
            int numApps = this.mApps.size();
            int numItemsPerPage4 = this.mWidgetCountX * this.mWidgetCountY;
            int childCount4 = ((PagedViewGridLayout) getPageAt(currentPage)).getChildCount();
            if (childCount4 > 0) {
                return ((currentPage - this.mNumAppsPages) * numItemsPerPage4) + numApps + (childCount4 / 2);
            }
            return -1;
        }
    }

    public int getSaveInstanceStateIndex() {
        if (this.mSaveInstanceStateItemIndex == -1) {
            this.mSaveInstanceStateItemIndex = getMiddleComponentIndexOnCurrentPage();
        }
        return this.mSaveInstanceStateItemIndex;
    }

    /* access modifiers changed from: 0000 */
    public int getPageForComponent(int index) {
        if (!this.mJoinWidgetsApps) {
            switch ($SWITCH_TABLE$com$cyanogenmod$trebuchet$AppsCustomizeView$ContentType()[this.mContentType.ordinal()]) {
                case 1:
                    return index / (this.mCellCountX * this.mCellCountY);
                case 2:
                    return index / (this.mWidgetCountX * this.mWidgetCountY);
                default:
                    return -1;
            }
        } else if (index < 0) {
            return 0;
        } else {
            if (index < this.mApps.size()) {
                return index / (this.mCellCountX * this.mCellCountY);
            }
            return this.mNumAppsPages + ((index - this.mApps.size()) / (this.mWidgetCountX * this.mWidgetCountY));
        }
    }

    private boolean testDataReady() {
        if (this.mContentType == ContentType.Widgets || this.mJoinWidgetsApps) {
            if (this.mApps.isEmpty() || this.mWidgets.isEmpty()) {
                return false;
            }
            return true;
        } else if (!this.mApps.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    public void restore(int index) {
        loadAssociatedPages(this.mCurrentPage);
        if (index >= 0) {
            this.mSaveInstanceStateItemIndex = index;
        }
    }

    private void updatePageCounts() {
        if (this.mJoinWidgetsApps) {
            this.mNumWidgetPages = (int) Math.ceil((double) (((float) this.mWidgets.size()) / ((float) (this.mWidgetCountX * this.mWidgetCountY))));
            this.mNumAppsPages = (int) Math.ceil((double) (((float) this.mApps.size()) / ((float) (this.mCellCountX * this.mCellCountY))));
        }
    }

    /* access modifiers changed from: protected */
    public void onDataReady(int width, int height) {
        boolean isLandscape = getResources().getConfiguration().orientation == 2;
        int maxCellCountX = Integer.MAX_VALUE;
        int maxCellCountY = Integer.MAX_VALUE;
        if (LauncherApplication.isScreenLarge()) {
            if (isLandscape) {
                maxCellCountX = LauncherModel.getCellCountX();
            } else {
                maxCellCountX = LauncherModel.getCellCountY();
            }
            if (isLandscape) {
                maxCellCountY = LauncherModel.getCellCountY();
            } else {
                maxCellCountY = LauncherModel.getCellCountX();
            }
        }
        if (this.mMaxAppCellCountX > -1) {
            maxCellCountX = Math.min(maxCellCountX, this.mMaxAppCellCountX);
        }
        if (this.mMaxAppCellCountY > -1) {
            maxCellCountY = Math.min(maxCellCountY, this.mMaxAppCellCountY);
        }
        this.mWidgetSpacingLayout.setGap(this.mPageLayoutWidthGap, this.mPageLayoutHeightGap);
        this.mWidgetSpacingLayout.setPadding(this.mPageLayoutPaddingLeft, this.mPageLayoutPaddingTop, this.mPageLayoutPaddingRight, this.mPageLayoutPaddingBottom);
        this.mWidgetSpacingLayout.calculateCellCount(width, height, maxCellCountX, maxCellCountY);
        this.mCellCountX = this.mWidgetSpacingLayout.getCellCountX();
        this.mCellCountY = this.mWidgetSpacingLayout.getCellCountY();
        updatePageCounts();
        this.mWidgetSpacingLayout.measure(MeasureSpec.makeMeasureSpec(getMeasuredWidth(), Integer.MIN_VALUE), MeasureSpec.makeMeasureSpec(getMeasuredHeight(), Integer.MIN_VALUE));
        this.mContentWidth = this.mWidgetSpacingLayout.getContentWidth();
        boolean hostIsTransitioning = getTabHost().isTransitioning();
        invalidatePageData(Math.max(0, getPageForComponent(this.mSaveInstanceStateItemIndex)), hostIsTransitioning);
        if (!hostIsTransitioning) {
            post(new Runnable() {
                public void run() {
                    AppsCustomizePagedView.this.showAllAppsCling();
                }
            });
        }
    }

    public void showAllAppsCling() {
        AppsCustomizeTabHost tabHost = getTabHost();
        if (tabHost != null) {
            Cling allAppsCling = (Cling) tabHost.findViewById(R.id.all_apps_cling);
            if (!this.mHasShownAllAppsCling && isDataReady() && testDataReady()) {
                this.mHasShownAllAppsCling = true;
                int[] offset = new int[2];
                int[] pos = this.mWidgetSpacingLayout.estimateCellPosition(this.mClingFocusedX, this.mClingFocusedY);
                this.mLauncher.getDragLayer().getLocationInDragLayer(this, offset);
                pos[0] = pos[0] + ((getMeasuredWidth() - this.mWidgetSpacingLayout.getMeasuredWidth()) / 2) + offset[0];
                pos[1] = pos[1] + offset[1];
                this.mLauncher.showFirstRunAllAppsCling(pos);
            } else if (!this.mHasShownAllAppsSortCling && isDataReady() && testDataReady() && allAppsCling != null && allAppsCling.isDismissed()) {
                this.mHasShownAllAppsSortCling = true;
                this.mLauncher.showFirstRunAllAppsSortCling();
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        if (!isDataReady() && testDataReady()) {
            setDataIsReady();
            setMeasuredDimension(width, height);
            onDataReady(width, height);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void onPackagesUpdated() {
        postDelayed(new Runnable() {
            public void run() {
                AppsCustomizePagedView.this.updatePackages();
            }
        }, 500);
    }

    public void updatePackages() {
        boolean wasEmpty = this.mWidgets.isEmpty();
        this.mWidgets.clear();
        List<AppWidgetProviderInfo> widgets = AppWidgetManager.getInstance(this.mLauncher).getInstalledProviders();
        List<ResolveInfo> shortcuts = this.mPackageManager.queryIntentActivities(new Intent("android.intent.action.CREATE_SHORTCUT"), 0);
        for (AppWidgetProviderInfo widget : widgets) {
            if (widget.minWidth <= 0 || widget.minHeight <= 0) {
                Log.e(LOG_TAG, "Widget " + widget.provider + " has invalid dimensions (" + widget.minWidth + ", " + widget.minHeight + ")");
            } else {
                this.mWidgets.add(widget);
            }
        }
        this.mWidgets.addAll(shortcuts);
        Collections.sort(this.mWidgets, new WidgetAndShortcutNameComparator(this.mPackageManager));
        updatePageCounts();
        if (!wasEmpty) {
            cancelAllTasks();
            invalidatePageData();
        } else if (testDataReady()) {
            requestLayout();
        }
    }

    public void onClick(View v) {
        if (this.mLauncher.isAllAppsCustomizeOpen() && !this.mLauncher.getWorkspace().isSwitchingState()) {
            this.mCurSelectView = v;
            if (v instanceof PagedViewIcon) {
                final View fv = v;
                final ApplicationInfo appInfo = (ApplicationInfo) v.getTag();
                animateClickFeedback(v, new Runnable() {
                    public void run() {
                        AppsCustomizePagedView.this.mLauncher.startActivitySafely(fv, appInfo.intent, appInfo);
                    }
                });
            } else if (v instanceof PagedViewWidget) {
                Toast.makeText(getContext(), R.string.long_press_widget_to_add, 0).show();
                float offsetY = (float) getResources().getDimensionPixelSize(R.dimen.dragViewOffsetY);
                ImageView p = (ImageView) v.findViewById(R.id.widget_preview);
                AnimatorSet bounce = new AnimatorSet();
                ValueAnimator tyuAnim = ObjectAnimator.ofFloat(p, "translationY", new float[]{offsetY});
                tyuAnim.setDuration(125);
                ValueAnimator tydAnim = ObjectAnimator.ofFloat(p, "translationY", new float[]{0.0f});
                tydAnim.setDuration(100);
                bounce.play(tyuAnim).before(tydAnim);
                bounce.setInterpolator(new AccelerateInterpolator());
                bounce.start();
            }
        }
    }

    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (keyCode != 82 || !(v instanceof PagedViewIcon)) {
            if (keyCode == 66 || keyCode == 23) {
                this.mCurSelectView = v;
            }
            return FocusHelper.handleAppsCustomizeKeyEvent(v, keyCode, event);
        }
        popQuickActionMenu(v);
        return true;
    }

    public View getCurSelectView() {
        return this.mCurSelectView;
    }

    /* access modifiers changed from: protected */
    public void determineDraggingStart(MotionEvent ev) {
    }

    private void beginDraggingApplication(View v) {
        this.mLauncher.getWorkspace().onDragStartedWithItem(v);
        this.mLauncher.getWorkspace().beginDragShared(v, this);
    }

    private void beginDraggingWidget(View v) {
        Bitmap preview;
        ImageView image = (ImageView) v.findViewById(R.id.widget_preview);
        PendingAddItemInfo createItemInfo = (PendingAddItemInfo) v.getTag();
        if (createItemInfo instanceof PendingAddWidgetInfo) {
            PendingAddWidgetInfo createWidgetInfo = (PendingAddWidgetInfo) createItemInfo;
            int[] spanXY = this.mLauncher.getSpanForWidget(createWidgetInfo, (int[]) null);
            createItemInfo.spanX = spanXY[0];
            createItemInfo.spanY = spanXY[1];
            int[] maxSize = this.mLauncher.getWorkspace().estimateItemSize(spanXY[0], spanXY[1], createWidgetInfo, true);
            preview = getWidgetPreview(createWidgetInfo.componentName, createWidgetInfo.previewImage, createWidgetInfo.icon, spanXY[0], spanXY[1], maxSize[0], maxSize[1]);
        } else {
            preview = Bitmap.createBitmap(this.mWidgetPreviewIconPaddedDimension, this.mWidgetPreviewIconPaddedDimension, Config.ARGB_8888);
            Drawable d = image.getDrawable();
            this.mCanvas.setBitmap(preview);
            d.draw(this.mCanvas);
            this.mCanvas.setBitmap(null);
            createItemInfo.spanY = 1;
            createItemInfo.spanX = 1;
        }
        if ((createItemInfo instanceof PendingAddWidgetInfo) && ((PendingAddWidgetInfo) createItemInfo).previewImage != 0) {
            new Paint().setMaskFilter(TableMaskFilter.CreateClipTable(0, 255));
        }
        Bitmap outline = Bitmap.createScaledBitmap(preview, preview.getWidth(), preview.getHeight(), false);
        this.mCanvas.setBitmap(preview);
        this.mCanvas.drawColor(this.mDragViewMultiplyColor, Mode.MULTIPLY);
        this.mCanvas.setBitmap(null);
        this.mLauncher.lockScreenOrientationOnLargeUI();
        this.mLauncher.getWorkspace().onDragStartedWithItem(createItemInfo, outline, null);
        this.mDragController.startDrag((View) image, preview, (DragSource) this, (Object) createItemInfo, DragController.DRAG_ACTION_COPY, (Rect) null, v.isInTouchMode());
        outline.recycle();
        preview.recycle();
    }

    /* access modifiers changed from: protected */
    public boolean beginDragging(View v) {
        this.mLauncher.dismissAllAppsCling(null);
        if (!super.beginDragging(v)) {
            return false;
        }
        this.mLauncher.enterSpringLoadedDragMode();
        if (v instanceof PagedViewIcon) {
            beginDraggingApplication(v);
        } else if (v instanceof PagedViewWidget) {
            beginDraggingWidget(v);
        }
        return true;
    }

    private void endDragging(View target, boolean success) {
        this.mLauncher.getWorkspace().onDragStopped(success);
        if (!success || (target != this.mLauncher.getWorkspace() && !(target instanceof DeleteDropTarget))) {
            this.mLauncher.exitSpringLoadedDragMode();
        }
        this.mLauncher.unlockScreenOrientationOnLargeUI();
    }

    public void onDropCompleted(View target, DragObject d, boolean success) {
        endDragging(target, success);
        if (!success) {
            boolean showOutOfSpaceMessage = false;
            if (target instanceof Workspace) {
                CellLayout layout = (CellLayout) ((Workspace) target).getChildAt(this.mLauncher.getCurrentWorkspaceScreen());
                ItemInfo itemInfo = (ItemInfo) d.dragInfo;
                if (layout != null) {
                    layout.calculateSpans(itemInfo);
                    showOutOfSpaceMessage = !layout.findCellForSpan(null, itemInfo.spanX, itemInfo.spanY);
                }
            }
            if (showOutOfSpaceMessage) {
                this.mLauncher.showOutOfSpaceMessage();
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        cancelAllTasks();
    }

    public void clearAllWidgetPreviews() {
        cancelAllTasks();
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View v = getPageAt(i);
            if (v instanceof PagedViewGridLayout) {
                ((PagedViewGridLayout) v).removeAllViewsOnPage();
                this.mDirtyPageContent.set(i, Boolean.valueOf(true));
            }
        }
    }

    private void cancelAllTasks() {
        Iterator<AppsCustomizeAsyncTask> iter = this.mRunningTasks.iterator();
        while (iter.hasNext()) {
            ((AppsCustomizeAsyncTask) iter.next()).cancel(false);
            iter.remove();
        }
    }

    public ContentType getContentType() {
        return this.mContentType;
    }

    public void setContentType(ContentType type) {
        boolean z = true;
        this.mContentType = type;
        if (!this.mJoinWidgetsApps) {
            if (type == ContentType.Apps) {
                z = false;
            }
            invalidatePageData(0, z);
        } else if (type == ContentType.Widgets) {
            invalidatePageData(this.mNumAppsPages, true);
        } else if (type == ContentType.Apps) {
            invalidatePageData(0, true);
        }
    }

    /* access modifiers changed from: protected */
    public void snapToPage(int whichPage, int delta, int duration) {
        super.snapToPage(whichPage, delta, duration);
        if (this.mJoinWidgetsApps) {
            updateCurrentTab(whichPage);
        }
        Iterator it = this.mRunningTasks.iterator();
        while (it.hasNext()) {
            AppsCustomizeAsyncTask task = (AppsCustomizeAsyncTask) it.next();
            int pageIndex = task.page + this.mNumAppsPages;
            if ((this.mNextPage <= this.mCurrentPage || pageIndex < this.mCurrentPage) && (this.mNextPage >= this.mCurrentPage || pageIndex > this.mCurrentPage)) {
                task.setThreadPriority(19);
            } else {
                task.setThreadPriority(getThreadPriorityForPage(pageIndex));
            }
        }
    }

    private void updateCurrentTab(int currentPage) {
        AppsCustomizeTabHost tabHost = getTabHost();
        if (tabHost != null) {
            String tag = tabHost.getCurrentTabTag();
            if (tag == null) {
                return;
            }
            if (currentPage >= this.mNumAppsPages && !tag.equals(tabHost.getTabTagForContentType(ContentType.Widgets))) {
                tabHost.setCurrentTabFromContent(ContentType.Widgets);
            } else if (currentPage < this.mNumAppsPages && !tag.equals(tabHost.getTabTagForContentType(ContentType.Apps))) {
                tabHost.setCurrentTabFromContent(ContentType.Apps);
            }
        }
    }

    public boolean isContentType(ContentType type) {
        return this.mContentType == type;
    }

    public void setCurrentToApps() {
    }

    public void setCurrentToWidgets() {
        invalidatePageData(0);
    }

    public void reloadCurrentPage() {
        if (!LauncherApplication.isScreenLarge()) {
            flashScrollingIndicator(true);
        }
        loadAssociatedPages(this.mCurrentPage);
        requestFocus();
    }

    public void loadContent() {
        loadAssociatedPages(this.mCurrentPage);
    }

    public void loadContent(boolean immediately) {
        loadAssociatedPages(this.mCurrentPage, immediately);
    }

    public void onTabChanged(final ContentType type) {
        if (!isContentType(type) || this.mJoinWidgetsApps) {
            final int duration = getResources().getInteger(R.integer.config_tabTransitionDuration);
            post(new Runnable() {
                public void run() {
                    if (AppsCustomizePagedView.this.getMeasuredWidth() <= 0 || AppsCustomizePagedView.this.getMeasuredHeight() <= 0) {
                        AppsCustomizePagedView.this.reloadCurrentPage();
                        return;
                    }
                    int[] visiblePageRange = new int[2];
                    AppsCustomizePagedView.this.getVisiblePages(visiblePageRange);
                    if (visiblePageRange[0] == -1 && visiblePageRange[1] == -1) {
                        AppsCustomizePagedView.this.reloadCurrentPage();
                        return;
                    }
                    ArrayList<View> visiblePages = new ArrayList<>();
                    for (int i = visiblePageRange[0]; i <= visiblePageRange[1]; i++) {
                        visiblePages.add(AppsCustomizePagedView.this.getPageAt(i));
                    }
                    final FrameLayout animationBuffer = (FrameLayout) AppsCustomizePagedView.this.getTabHost().findViewById(R.id.animation_buffer);
                    AppsCustomizePagedView self = (AppsCustomizePagedView) AppsCustomizePagedView.this.getTabHost().findViewById(R.id.apps_customize_pane_content);
                    animationBuffer.scrollTo(AppsCustomizePagedView.this.getScrollX(), 0);
                    for (int i2 = visiblePages.size() - 1; i2 >= 0; i2--) {
                        View child = (View) visiblePages.get(i2);
                        if (child instanceof PagedViewCellLayout) {
                            ((PagedViewCellLayout) child).resetChildrenOnKeyListeners();
                        } else if (child instanceof PagedViewGridLayout) {
                            ((PagedViewGridLayout) child).resetChildrenOnKeyListeners();
                        }
                        PagedViewWidget.setDeletePreviewsWhenDetachedFromWindow(false);
                        AppsCustomizePagedView.this.removeView(child);
                        PagedViewWidget.setDeletePreviewsWhenDetachedFromWindow(true);
                        animationBuffer.setAlpha(1.0f);
                        animationBuffer.setVisibility(0);
                        LayoutParams p = new LayoutParams(child.getWidth(), child.getHeight());
                        p.setMargins(child.getLeft(), child.getTop(), 0, 0);
                        animationBuffer.addView(child, p);
                    }
                    AppsCustomizePagedView.this.hideScrollingIndicator(false);
                    AppsCustomizePagedView.this.setContentType(type);
                    ObjectAnimator outAnim = ObjectAnimator.ofFloat(animationBuffer, "alpha", new float[]{0.0f});
                    outAnim.addListener(new AnimatorListenerAdapter() {
                        public void onAnimationEnd(Animator animation) {
                            animationBuffer.setVisibility(8);
                            animationBuffer.removeAllViews();
                        }

                        public void onAnimationCancel(Animator animation) {
                            animationBuffer.setVisibility(8);
                            animationBuffer.removeAllViews();
                        }
                    });
                    ObjectAnimator inAnim = ObjectAnimator.ofFloat(self, "alpha", new float[]{1.0f});
                    inAnim.addListener(new AnimatorListenerAdapter() {
                        public void onAnimationEnd(Animator animation) {
                            AppsCustomizePagedView.this.reloadCurrentPage();
                        }
                    });
                    AnimatorSet animSet = new AnimatorSet();
                    animSet.playTogether(new Animator[]{outAnim, inAnim});
                    animSet.setDuration((long) duration);
                    animSet.start();
                }
            });
        }
    }

    private void setVisibilityOnChildren(ViewGroup layout, int visibility) {
        int childCount = layout.getChildCount();
        for (int i = 0; i < childCount; i++) {
            layout.getChildAt(i).setVisibility(visibility);
        }
    }

    private void setupPage(PagedViewCellLayout layout) {
        layout.setCellCount(this.mCellCountX, this.mCellCountY);
        layout.setGap(this.mPageLayoutWidthGap, this.mPageLayoutHeightGap);
        layout.setPadding(this.mPageLayoutPaddingLeft, this.mPageLayoutPaddingTop, this.mPageLayoutPaddingRight, this.mPageLayoutPaddingBottom);
        setVisibilityOnChildren(layout, 8);
        int widthSpec = MeasureSpec.makeMeasureSpec(getMeasuredWidth(), Integer.MIN_VALUE);
        int heightSpec = MeasureSpec.makeMeasureSpec(getMeasuredHeight(), Integer.MIN_VALUE);
        layout.setMinimumWidth(getPageContentWidth());
        layout.measure(widthSpec, heightSpec);
        setVisibilityOnChildren(layout, 0);
    }

    public void syncAppsPages() {
        Context context = getContext();
        int numPages = (int) Math.ceil((double) (((float) this.mApps.size()) / ((float) (this.mCellCountX * this.mCellCountY))));
        for (int i = 0; i < numPages; i++) {
            PagedViewCellLayout layout = new PagedViewCellLayout(context);
            setupPage(layout);
            addView(layout);
        }
    }

    public void syncAppsPageItems(int page, boolean immediate) {
        int numCells = this.mCellCountX * this.mCellCountY;
        int startIndex = page * numCells;
        int endIndex = Math.min(startIndex + numCells, this.mApps.size());
        PagedViewCellLayout layout = (PagedViewCellLayout) getPageAt(page);
        layout.removeAllViewsOnPage();
        for (int i = startIndex; i < endIndex; i++) {
            PagedViewIcon icon = (PagedViewIcon) this.mLayoutInflater.inflate(R.layout.apps_customize_application, layout, false);
            icon.applyFromApplicationInfo((ApplicationInfo) this.mApps.get(i), this.mHolographicOutlineHelper);
            icon.setOnClickListener(this);
            icon.setOnLongClickListener(this);
            icon.setOnTouchListener(this);
            icon.setOnKeyListener(this);
            int index = i - startIndex;
            layout.addViewToCellLayout(icon, -1, i, new PagedViewCellLayout.LayoutParams(index % this.mCellCountX, index / this.mCellCountX, 1, 1));
        }
        layout.createHardwareLayers();
    }

    private int getWidgetPageLoadPriority(int page) {
        int toPage = this.mCurrentPage;
        if (this.mNextPage > -1) {
            toPage = this.mNextPage;
        }
        Iterator<AppsCustomizeAsyncTask> iter = this.mRunningTasks.iterator();
        int minPageDiff = Integer.MAX_VALUE;
        while (iter.hasNext()) {
            minPageDiff = Math.abs((((AppsCustomizeAsyncTask) iter.next()).page + this.mNumAppsPages) - toPage);
        }
        int rawPageDiff = Math.abs(page - toPage);
        return rawPageDiff - Math.min(rawPageDiff, minPageDiff);
    }

    private int getThreadPriorityForPage(int page) {
        int pageDiff = Math.abs(page - this.mCurrentPage);
        if (pageDiff <= 0) {
            return -2;
        }
        if (pageDiff <= 1) {
            return -1;
        }
        return 0;
    }

    private int getSleepForPage(int page) {
        return Math.max(0, getWidgetPageLoadPriority(page) * sPageSleepDelay);
    }

    /* access modifiers changed from: private */
    public void prepareLoadWidgetPreviewsTask(int page, ArrayList<Object> widgets, int cellWidth, int cellHeight) {
        Iterator<AppsCustomizeAsyncTask> iter = this.mRunningTasks.iterator();
        while (iter.hasNext()) {
            AppsCustomizeAsyncTask task = (AppsCustomizeAsyncTask) iter.next();
            int taskPage = task.page + this.mNumAppsPages;
            if (taskPage < getAssociatedLowerPageBound(this.mCurrentPage) || taskPage > getAssociatedUpperPageBound(this.mCurrentPage)) {
                task.cancel(false);
                iter.remove();
            } else {
                task.setThreadPriority(getThreadPriorityForPage(taskPage));
            }
        }
        final int sleepMs = getSleepForPage(this.mNumAppsPages + page);
        AsyncTaskPageData pageData = new AsyncTaskPageData(page, widgets, cellWidth, cellHeight, new AsyncTaskCallback() {
            public void run(AppsCustomizeAsyncTask task, AsyncTaskPageData data) {
                try {
                    Thread.sleep((long) sleepMs);
                } catch (Exception e) {
                }
                try {
                    AppsCustomizePagedView.this.loadWidgetPreviewsInBackground(task, data);
                } finally {
                    if (task.isCancelled()) {
                        data.cleanup(true);
                    }
                }
            }
        }, new AsyncTaskCallback() {
            public void run(AppsCustomizeAsyncTask task, AsyncTaskPageData data) {
                try {
                    AppsCustomizePagedView.this.mRunningTasks.remove(task);
                    if (!task.isCancelled()) {
                        if (AppsCustomizePagedView.this.mJoinWidgetsApps || (task.page <= AppsCustomizePagedView.this.getPageCount() && task.pageContentType == AppsCustomizePagedView.this.mContentType)) {
                            AppsCustomizePagedView.this.onSyncWidgetPageItems(data);
                            data.cleanup(task.isCancelled());
                        }
                    }
                } finally {
                    data.cleanup(task.isCancelled());
                }
            }
        });
        AppsCustomizeAsyncTask t = new AppsCustomizeAsyncTask(page, this.mContentType, Type.LoadWidgetPreviewData);
        t.setThreadPriority(getThreadPriorityForPage(this.mNumAppsPages + page));
        t.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new AsyncTaskPageData[]{pageData});
        this.mRunningTasks.add(t);
    }

    private void prepareGenerateHoloOutlinesTask(int page, ArrayList<Object> items, ArrayList<Bitmap> images) {
        Iterator<AppsCustomizeAsyncTask> iter = this.mRunningTasks.iterator();
        while (iter.hasNext()) {
            AppsCustomizeAsyncTask task = (AppsCustomizeAsyncTask) iter.next();
            if (task.page == page && task.dataType == Type.LoadHolographicIconsData) {
                task.cancel(false);
                iter.remove();
            }
        }
        AsyncTaskPageData pageData = new AsyncTaskPageData(page, items, images, new AsyncTaskCallback() {
            /* JADX INFO: finally extract failed */
            public void run(AppsCustomizeAsyncTask task, AsyncTaskPageData data) {
                try {
                    task.syncThreadPriority();
                    ArrayList<Bitmap> images = data.generatedImages;
                    ArrayList<Bitmap> srcImages = data.sourceImages;
                    int count = srcImages.size();
                    Canvas c = new Canvas();
                    for (int i = 0; i < count && !task.isCancelled(); i++) {
                        task.syncThreadPriority();
                        Bitmap b = (Bitmap) srcImages.get(i);
                        Bitmap outline = Bitmap.createBitmap(b.getWidth(), b.getHeight(), Config.ARGB_8888);
                        c.setBitmap(outline);
                        c.save();
                        c.drawBitmap(b, FlyingIcon.ANGULAR_VMIN, FlyingIcon.ANGULAR_VMIN, null);
                        c.restore();
                        c.setBitmap(null);
                        images.add(outline);
                    }
                    if (task.isCancelled()) {
                        data.cleanup(true);
                    }
                } catch (Throwable th) {
                    if (task.isCancelled()) {
                        data.cleanup(true);
                    }
                    throw th;
                }
            }
        }, new AsyncTaskCallback() {
            public void run(AppsCustomizeAsyncTask task, AsyncTaskPageData data) {
                try {
                    AppsCustomizePagedView.this.mRunningTasks.remove(task);
                    if (!task.isCancelled()) {
                        if (AppsCustomizePagedView.this.mJoinWidgetsApps || (task.page <= AppsCustomizePagedView.this.getPageCount() && task.pageContentType == AppsCustomizePagedView.this.mContentType)) {
                            AppsCustomizePagedView.this.onHolographicPageItemsLoaded(data);
                            data.cleanup(task.isCancelled());
                        }
                    }
                } finally {
                    data.cleanup(task.isCancelled());
                }
            }
        });
        AppsCustomizeAsyncTask t = new AppsCustomizeAsyncTask(page, this.mContentType, Type.LoadHolographicIconsData);
        t.setThreadPriority(10);
        t.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, new AsyncTaskPageData[]{pageData});
        this.mRunningTasks.add(t);
    }

    private void setupPage(PagedViewGridLayout layout) {
        layout.setPadding(this.mPageLayoutPaddingLeft, this.mPageLayoutPaddingTop, this.mPageLayoutPaddingRight, this.mPageLayoutPaddingBottom);
        int widthSpec = MeasureSpec.makeMeasureSpec(getMeasuredWidth(), Integer.MIN_VALUE);
        int heightSpec = MeasureSpec.makeMeasureSpec(getMeasuredHeight(), Integer.MIN_VALUE);
        layout.setMinimumWidth(getPageContentWidth());
        layout.measure(widthSpec, heightSpec);
    }

    private void renderDrawableToBitmap(Drawable d, Bitmap bitmap, int x, int y, int w, int h) {
        renderDrawableToBitmap(d, bitmap, x, y, w, h, 1.0f, -1);
    }

    private void renderDrawableToBitmap(Drawable d, Bitmap bitmap, int x, int y, int w, int h, float scale, int multiplyColor) {
        if (bitmap != null) {
            Canvas c = new Canvas(bitmap);
            c.scale(scale, scale);
            Rect oldBounds = d.copyBounds();
            d.setBounds(x, y, x + w, y + h);
            d.draw(c);
            d.setBounds(oldBounds);
            if (multiplyColor != -1) {
                c.drawColor(this.mDragViewMultiplyColor, Mode.MULTIPLY);
            }
            c.setBitmap(null);
        }
    }

    private Bitmap getShortcutPreview(ResolveInfo info) {
        int bitmapSize = this.mAppIconSize;
        Bitmap preview = Bitmap.createBitmap(bitmapSize, bitmapSize, Config.ARGB_8888);
        renderDrawableToBitmap(this.mIconCache.getFullResIcon(info), preview, 0, 0, this.mAppIconSize, this.mAppIconSize);
        return preview;
    }

    private Bitmap getWidgetPreview(ComponentName provider, int previewImage, int iconId, int cellHSpan, int cellVSpan, int maxWidth, int maxHeight) {
        int bitmapWidth;
        int bitmapHeight;
        String packageName = provider.getPackageName();
        if (maxWidth < 0) {
            maxWidth = Integer.MAX_VALUE;
        }
        if (maxHeight < 0) {
            maxHeight = Integer.MAX_VALUE;
        }
        Drawable drawable = null;
        if (previewImage != 0) {
            drawable = this.mPackageManager.getDrawable(packageName, previewImage, null);
            if (drawable == null) {
                Log.w(LOG_TAG, "Can't load widget preview drawable 0x" + Integer.toHexString(previewImage) + " for provider: " + provider);
            }
        }
        boolean widgetPreviewExists = drawable != null;
        if (widgetPreviewExists) {
            bitmapWidth = drawable.getIntrinsicWidth();
            bitmapHeight = drawable.getIntrinsicHeight();
            maxWidth = Math.min(maxWidth, this.mWidgetSpacingLayout.estimateCellWidth(cellHSpan));
            maxHeight = Math.min(maxHeight, this.mWidgetSpacingLayout.estimateCellHeight(cellVSpan));
        } else {
            bitmapWidth = this.mWidgetSpacingLayout.estimateCellWidth(cellHSpan);
            bitmapHeight = this.mWidgetSpacingLayout.estimateCellHeight(cellVSpan);
            if (cellHSpan == cellVSpan) {
                int minOffset = (int) (((float) this.mAppIconSize) * 0.25f);
                if (cellHSpan <= 1) {
                    bitmapHeight = this.mAppIconSize + (minOffset * 2);
                    bitmapWidth = bitmapHeight;
                } else {
                    bitmapHeight = this.mAppIconSize + (minOffset * 4);
                    bitmapWidth = bitmapHeight;
                }
            }
        }
        float scale = 1.0f;
        if (bitmapWidth > maxWidth) {
            scale = ((float) maxWidth) / ((float) bitmapWidth);
        }
        if (((float) bitmapHeight) * scale > ((float) maxHeight)) {
            scale = ((float) maxHeight) / ((float) bitmapHeight);
        }
        if (scale != 1.0f) {
            bitmapWidth = (int) (((float) bitmapWidth) * scale);
            bitmapHeight = (int) (((float) bitmapHeight) * scale);
        }
        Bitmap preview = Bitmap.createBitmap(bitmapWidth, bitmapHeight, Config.ARGB_8888);
        if (widgetPreviewExists) {
            renderDrawableToBitmap(drawable, preview, 0, 0, bitmapWidth, bitmapHeight);
        } else {
            float iconScale = Math.min(((float) Math.min(bitmapWidth, bitmapHeight)) / ((float) (this.mAppIconSize + (((int) (((float) this.mAppIconSize) * 0.25f)) * 2))), 1.0f);
            if (!(cellHSpan == 1 && cellVSpan == 1)) {
                renderDrawableToBitmap(this.mDefaultWidgetBackground, preview, 0, 0, bitmapWidth, bitmapHeight);
            }
            Drawable icon = null;
            try {
                int hoffset = (int) (((float) (bitmapWidth / 2)) - ((((float) this.mAppIconSize) * iconScale) / 2.0f));
                int yoffset = (int) (((float) (bitmapHeight / 2)) - ((((float) this.mAppIconSize) * iconScale) / 2.0f));
                if (iconId > 0) {
                    icon = this.mIconCache.getFullResIcon(packageName, iconId);
                }
                Resources resources = this.mLauncher.getResources();
                if (icon == null) {
                    icon = resources.getDrawable(R.drawable.ic_launcher_application);
                }
                renderDrawableToBitmap(icon, preview, hoffset, yoffset, (int) (((float) this.mAppIconSize) * iconScale), (int) (((float) this.mAppIconSize) * iconScale));
            } catch (NotFoundException e) {
            }
        }
        return preview;
    }

    public void syncWidgetPages() {
        Context context = getContext();
        int numPages = (int) Math.ceil((double) (((float) this.mWidgets.size()) / ((float) (this.mWidgetCountX * this.mWidgetCountY))));
        for (int j = 0; j < numPages; j++) {
            PagedViewGridLayout layout = new PagedViewGridLayout(context, this.mWidgetCountX, this.mWidgetCountY);
            setupPage(layout);
            addView(layout, new PagedViewGridLayout.LayoutParams(-1, -1));
        }
    }

    public void syncWidgetPageItems(int page, boolean immediate) {
        int numItemsPerPage = this.mWidgetCountX * this.mWidgetCountY;
        final ArrayList<Object> items = new ArrayList<>();
        final int cellWidth = (((this.mWidgetSpacingLayout.getContentWidth() - this.mPageLayoutPaddingLeft) - this.mPageLayoutPaddingRight) - ((this.mWidgetCountX - 1) * this.mWidgetWidthGap)) / this.mWidgetCountX;
        final int cellHeight = (((this.mWidgetSpacingLayout.getContentHeight() - this.mPageLayoutPaddingTop) - this.mPageLayoutPaddingBottom) - ((this.mWidgetCountY - 1) * this.mWidgetHeightGap)) / this.mWidgetCountY;
        int offset = page * numItemsPerPage;
        for (int i = offset; i < Math.min(offset + numItemsPerPage, this.mWidgets.size()); i++) {
            items.add(this.mWidgets.get(i));
        }
        final PagedViewGridLayout layout = (PagedViewGridLayout) getPageAt(this.mNumAppsPages + page);
        layout.setColumnCount(layout.getCellCountX());
        for (int i2 = 0; i2 < items.size(); i2++) {
            Object rawInfo = items.get(i2);
            PagedViewWidget widget = (PagedViewWidget) this.mLayoutInflater.inflate(R.layout.apps_customize_widget, layout, false);
            if (rawInfo instanceof AppWidgetProviderInfo) {
                AppWidgetProviderInfo info = (AppWidgetProviderInfo) rawInfo;
                PendingAddItemInfo createItemInfo = new PendingAddWidgetInfo(info, null, null);
                widget.applyFromAppWidgetProviderInfo(info, -1, this.mLauncher.getSpanForWidget(info, (int[]) null), this.mHolographicOutlineHelper);
                widget.setTag(createItemInfo);
            } else if (rawInfo instanceof ResolveInfo) {
                ResolveInfo info2 = (ResolveInfo) rawInfo;
                PendingAddItemInfo createItemInfo2 = new PendingAddItemInfo();
                createItemInfo2.itemType = 1;
                createItemInfo2.componentName = new ComponentName(info2.activityInfo.packageName, info2.activityInfo.name);
                widget.applyFromResolveInfo(this.mPackageManager, info2, this.mHolographicOutlineHelper);
                widget.setTag(createItemInfo2);
            }
            widget.setOnClickListener(this);
            widget.setOnLongClickListener(this);
            widget.setOnTouchListener(this);
            widget.setOnKeyListener(this);
            int ix = i2 % this.mWidgetCountX;
            int iy = i2 / this.mWidgetCountX;
            GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams(GridLayout.spec(iy, GridLayout.LEFT), GridLayout.spec(ix, GridLayout.TOP));
            layoutParams.width = cellWidth;
            layoutParams.height = cellHeight;
            layoutParams.setGravity(51);
            if (ix > 0) {
                layoutParams.leftMargin = this.mWidgetWidthGap;
            }
            if (iy > 0) {
                layoutParams.topMargin = this.mWidgetHeightGap;
            }
            layout.addView(widget, layoutParams);
        }
        final boolean z = immediate;
        final int i3 = page;
        layout.setOnLayoutListener(new Runnable() {
            public void run() {
                int maxPreviewWidth = cellWidth;
                int maxPreviewHeight = cellHeight;
                if (layout.getChildCount() > 0) {
                    int[] maxSize = ((PagedViewWidget) layout.getChildAt(0)).getPreviewSize();
                    maxPreviewWidth = maxSize[0];
                    maxPreviewHeight = maxSize[1];
                }
                if (z) {
                    AsyncTaskPageData data = new AsyncTaskPageData(i3, items, maxPreviewWidth, maxPreviewHeight, null, null);
                    AppsCustomizePagedView.this.loadWidgetPreviewsInBackground(null, data);
                    AppsCustomizePagedView.this.onSyncWidgetPageItems(data);
                    return;
                }
                AppsCustomizePagedView.this.prepareLoadWidgetPreviewsTask(i3, items, maxPreviewWidth, maxPreviewHeight);
            }
        });
    }

    /* access modifiers changed from: private */
    public void loadWidgetPreviewsInBackground(AppsCustomizeAsyncTask task, AsyncTaskPageData data) {
        if (task != null) {
            task.syncThreadPriority();
        }
        ArrayList<Object> items = data.items;
        ArrayList<Bitmap> images = data.generatedImages;
        Iterator it = items.iterator();
        while (it.hasNext()) {
            Object item = it.next();
            if (task != null) {
                if (!task.isCancelled()) {
                    task.syncThreadPriority();
                } else {
                    return;
                }
            }
            if (item instanceof AppWidgetProviderInfo) {
                AppWidgetProviderInfo info = (AppWidgetProviderInfo) item;
                int[] cellSpans = this.mLauncher.getSpanForWidget(info, (int[]) null);
                images.add(getWidgetPreview(info.provider, info.previewImage, info.icon, cellSpans[0], cellSpans[1], data.maxImageWidth, data.maxImageHeight));
            } else if (item instanceof ResolveInfo) {
                images.add(getShortcutPreview((ResolveInfo) item));
            }
        }
    }

    /* access modifiers changed from: private */
    public void onSyncWidgetPageItems(AsyncTaskPageData data) {
        PagedViewGridLayout layout = (PagedViewGridLayout) getPageAt(this.mNumAppsPages + data.page);
        int count = data.items.size();
        for (int i = 0; i < count; i++) {
            PagedViewWidget widget = (PagedViewWidget) layout.getChildAt(i);
            if (widget != null) {
                widget.applyPreview(new FastBitmapDrawable((Bitmap) data.generatedImages.get(i)));
            }
        }
        layout.createHardwareLayer();
        invalidate();
        Iterator it = this.mRunningTasks.iterator();
        while (it.hasNext()) {
            AppsCustomizeAsyncTask task = (AppsCustomizeAsyncTask) it.next();
            task.setThreadPriority(getThreadPriorityForPage(task.page + this.mNumAppsPages));
        }
    }

    /* access modifiers changed from: private */
    public void onHolographicPageItemsLoaded(AsyncTaskPageData data) {
        invalidate();
        ViewGroup layout = (ViewGroup) getPageAt(data.page);
        if (layout instanceof PagedViewCellLayout) {
            PagedViewCellLayout cl = (PagedViewCellLayout) layout;
            int count = cl.getPageChildCount();
            if (count == data.generatedImages.size()) {
                for (int i = 0; i < count; i++) {
                    ((PagedViewIcon) cl.getChildOnPageAt(i)).setHolographicOutline((Bitmap) data.generatedImages.get(i));
                }
                return;
            }
            return;
        }
        int count2 = layout.getChildCount();
        if (count2 == data.generatedImages.size()) {
            for (int i2 = 0; i2 < count2; i2++) {
                ((PagedViewWidget) layout.getChildAt(i2)).setHolographicOutline((Bitmap) data.generatedImages.get(i2));
            }
        }
    }

    public void syncPages() {
        removeAllViews();
        cancelAllTasks();
        if (this.mJoinWidgetsApps) {
            Context context = getContext();
            for (int j = 0; j < this.mNumWidgetPages; j++) {
                PagedViewGridLayout layout = new PagedViewGridLayout(context, this.mWidgetCountX, this.mWidgetCountY);
                setupPage(layout);
                addView(layout, new PagedViewGridLayout.LayoutParams(-1, -1));
            }
            for (int i = 0; i < this.mNumAppsPages; i++) {
                PagedViewCellLayout layout2 = new PagedViewCellLayout(context);
                setupPage(layout2);
                addView(layout2);
            }
            return;
        }
        switch ($SWITCH_TABLE$com$cyanogenmod$trebuchet$AppsCustomizeView$ContentType()[this.mContentType.ordinal()]) {
            case 1:
                syncAppsPages();
                return;
            case 2:
                syncWidgetPages();
                return;
            default:
                return;
        }
    }

    public void syncPageItems(int page, boolean immediate) {
        if (!this.mJoinWidgetsApps) {
            switch ($SWITCH_TABLE$com$cyanogenmod$trebuchet$AppsCustomizeView$ContentType()[this.mContentType.ordinal()]) {
                case 1:
                    syncAppsPageItems(page, immediate);
                    return;
                case 2:
                    syncWidgetPageItems(page, immediate);
                    return;
                default:
                    return;
            }
        } else if (page < this.mNumAppsPages) {
            syncAppsPageItems(page, immediate);
        } else {
            syncWidgetPageItems(page - this.mNumAppsPages, immediate);
        }
    }

    /* access modifiers changed from: 0000 */
    public View getPageAt(int index) {
        return getChildAt((getChildCount() - index) - 1);
    }

    /* access modifiers changed from: protected */
    public int indexToPage(int index) {
        return (getChildCount() - index) - 1;
    }

    private void screenScrolledStandard(int screenScroll) {
        for (int i = 0; i < getChildCount(); i++) {
            View v = getPageAt(i);
            if (v != null) {
                float scrollProgress = getScrollProgress(screenScroll, v, i);
                if (this.mFadeInAdjacentScreens) {
                    v.setAlpha(1.0f - Math.abs(scrollProgress));
                }
            }
        }
    }

    private void screenScrolledTablet(int screenScroll) {
        for (int i = 0; i < getChildCount(); i++) {
            View v = getPageAt(i);
            if (v != null) {
                float scrollProgress = getScrollProgress(screenScroll, v, i);
                float rotation = TRANSITION_SCREEN_ROTATION * scrollProgress;
                v.setTranslationX(this.mLauncher.getWorkspace().getOffsetXForRotation(rotation, v.getWidth(), v.getHeight()));
                v.setRotationY(rotation);
                if (this.mFadeInAdjacentScreens) {
                    v.setAlpha(1.0f - Math.abs(scrollProgress));
                }
            }
        }
    }

    private void screenScrolledZoom(int screenScroll, boolean in) {
        float f;
        for (int i = 0; i < getChildCount(); i++) {
            View v = getPageAt(i);
            if (v != null) {
                float scrollProgress = getScrollProgress(screenScroll, v, i);
                if (in) {
                    f = -0.2f;
                } else {
                    f = 0.1f;
                }
                float scale = 1.0f + (f * Math.abs(scrollProgress));
                if (!in) {
                    v.setTranslationX(((float) v.getMeasuredWidth()) * 0.1f * (-scrollProgress));
                }
                v.setScaleX(scale);
                v.setScaleY(scale);
                if (this.mFadeInAdjacentScreens) {
                    v.setAlpha(1.0f - Math.abs(scrollProgress));
                }
            }
        }
    }

    private void screenScrolledRotate(int screenScroll, boolean up) {
        for (int i = 0; i < getChildCount(); i++) {
            View v = getPageAt(i);
            if (v != null) {
                float scrollProgress = getScrollProgress(screenScroll, v, i);
                float rotation = (up ? TRANSITION_SCREEN_ROTATION : -12.5f) * scrollProgress;
                float translationX = ((float) v.getMeasuredWidth()) * scrollProgress;
                float rotatePoint = (((float) v.getMeasuredWidth()) * 0.5f) / ((float) Math.tan(Math.toRadians(6.25d)));
                v.setPivotX(((float) v.getMeasuredWidth()) * 0.5f);
                if (up) {
                    v.setPivotY(-rotatePoint);
                } else {
                    v.setPivotY(((float) v.getMeasuredHeight()) + rotatePoint);
                }
                v.setRotation(rotation);
                v.setTranslationX(translationX);
                if (this.mFadeInAdjacentScreens) {
                    v.setAlpha(1.0f - Math.abs(scrollProgress));
                }
            }
        }
    }

    private void screenScrolledCube(int screenScroll, boolean in) {
        for (int i = 0; i < getChildCount(); i++) {
            View v = getPageAt(i);
            if (v != null) {
                float scrollProgress = getScrollProgress(screenScroll, v, i);
                float rotation = (in ? 90.0f : -90.0f) * scrollProgress;
                float alpha = 1.0f - Math.abs(scrollProgress);
                if (in) {
                    v.setCameraDistance(this.mDensity * CAMERA_DISTANCE);
                }
                v.setPivotX((float) (scrollProgress < FlyingIcon.ANGULAR_VMIN ? 0 : v.getMeasuredWidth()));
                v.setPivotY(((float) v.getMeasuredHeight()) * 0.5f);
                v.setRotationY(rotation);
                v.setAlpha(alpha);
            }
        }
    }

    private void screenScrolledStack(int screenScroll) {
        float alpha;
        for (int i = 0; i < getChildCount(); i++) {
            View v = getPageAt(i);
            if (v != null) {
                float scrollProgress = getScrollProgress(screenScroll, v, i);
                float interpolatedProgress = this.mZInterpolator.getInterpolation(Math.abs(Math.min(scrollProgress, FlyingIcon.ANGULAR_VMIN)));
                float scale = (1.0f - interpolatedProgress) + (0.76f * interpolatedProgress);
                float translationX = Math.min(FlyingIcon.ANGULAR_VMIN, scrollProgress) * ((float) v.getMeasuredWidth());
                if (LauncherApplication.isScreenLarge() && scrollProgress >= FlyingIcon.ANGULAR_VMIN) {
                    alpha = this.mLeftScreenAlphaInterpolator.getInterpolation(1.0f - scrollProgress);
                } else if (scrollProgress < FlyingIcon.ANGULAR_VMIN) {
                    alpha = this.mAlphaInterpolator.getInterpolation(1.0f - Math.abs(scrollProgress));
                } else {
                    alpha = 1.0f;
                }
                v.setTranslationX(translationX);
                v.setScaleX(scale);
                v.setScaleY(scale);
                v.setAlpha(alpha);
                if (alpha <= 0.020833334f) {
                    v.setVisibility(4);
                } else if (v.getVisibility() != 0) {
                    v.setVisibility(0);
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public void screenScrolled(int screenScroll) {
        int index = 0;
        super.screenScrolled(screenScroll);
        if (this.mOverScrollX < 0 || this.mOverScrollX > this.mMaxScrollX) {
            if (this.mOverScrollX >= 0) {
                index = getChildCount() - 1;
            }
            View v = getPageAt(index);
            if (v != null) {
                float rotation = -22.0f * getScrollProgress(screenScroll, v, index);
                v.setCameraDistance(this.mDensity * CAMERA_DISTANCE);
                v.setPivotX((index == 0 ? TRANSITION_PIVOT : 0.35000002f) * ((float) v.getMeasuredWidth()));
                v.setPivotY(((float) v.getMeasuredHeight()) * 0.5f);
                v.setRotationY(rotation);
                v.setTranslationX(FlyingIcon.ANGULAR_VMIN);
                this.mOverscrollTransformsDirty = true;
                return;
            }
            return;
        }
        if (this.mOverscrollTransformsDirty) {
            this.mOverscrollTransformsDirty = false;
            View v0 = getPageAt(0);
            View v1 = getPageAt(getChildCount() - 1);
            v0.setTranslationX(FlyingIcon.ANGULAR_VMIN);
            v1.setTranslationX(FlyingIcon.ANGULAR_VMIN);
            v0.setRotationY(FlyingIcon.ANGULAR_VMIN);
            v1.setRotationY(FlyingIcon.ANGULAR_VMIN);
            v0.setCameraDistance(this.mDensity * 1280.0f);
            v1.setCameraDistance(this.mDensity * 1280.0f);
            v0.setPivotX((float) (v0.getMeasuredWidth() / 2));
            v1.setPivotX((float) (v1.getMeasuredWidth() / 2));
            v0.setPivotY((float) (v0.getMeasuredHeight() / 2));
            v1.setPivotY((float) (v1.getMeasuredHeight() / 2));
        }
        switch ($SWITCH_TABLE$com$cyanogenmod$trebuchet$AppsCustomizePagedView$TransitionEffect()[this.mTransitionEffect.ordinal()]) {
            case 1:
                screenScrolledStandard(screenScroll);
                return;
            case 2:
                screenScrolledTablet(screenScroll);
                return;
            case 3:
                screenScrolledZoom(screenScroll, true);
                return;
            case 4:
                screenScrolledZoom(screenScroll, false);
                return;
            case 5:
                screenScrolledRotate(screenScroll, true);
                return;
            case 6:
                screenScrolledRotate(screenScroll, false);
                return;
            case 7:
                screenScrolledCube(screenScroll, true);
                return;
            case 8:
                screenScrolledCube(screenScroll, false);
                return;
            case 9:
                screenScrolledStack(screenScroll);
                return;
            default:
                return;
        }
    }

    /* access modifiers changed from: protected */
    public void overScroll(float amount) {
        acceleratedOverScroll(amount);
    }

    public int getPageContentWidth() {
        return this.mContentWidth;
    }

    /* access modifiers changed from: protected */
    public void onPageEndMoving() {
        if (this.mFadeScrollingIndicator) {
            hideScrollingIndicator(false);
        }
        this.mSaveInstanceStateItemIndex = -1;
    }

    /* access modifiers changed from: protected */
    public void flashScrollingIndicator(boolean animated) {
        if (this.mFadeScrollingIndicator) {
            super.flashScrollingIndicator(animated);
        } else {
            showScrollingIndicator(false);
        }
    }

    public void setup(Launcher launcher, DragController dragController) {
        this.mLauncher = launcher;
        this.mDragController = dragController;
    }

    public SortMode getSortMode() {
        return this.mSortMode;
    }

    public void setSortMode(SortMode sortMode) {
        if (this.mSortMode != sortMode) {
            this.mSortMode = sortMode;
            if (this.mSortMode == SortMode.Title) {
                Collections.sort(this.mApps, LauncherModel.APP_NAME_COMPARATOR);
            } else if (this.mSortMode == SortMode.InstallDate) {
                Collections.sort(this.mApps, LauncherModel.APP_INSTALL_TIME_COMPARATOR);
            } else if (this.mSortMode == SortMode.KKDefault) {
                sortByKonka(this.mApps);
            }
            if (this.mJoinWidgetsApps) {
                for (int i = 0; i < this.mNumAppsPages; i++) {
                    syncAppsPageItems(i, true);
                }
            } else if (this.mContentType == ContentType.Apps) {
                for (int i2 = 0; i2 < getChildCount(); i2++) {
                    syncAppsPageItems(i2, true);
                }
            }
        }
    }

    private void loadAppListFile() {
        try {
            InputStream in = getResources().openRawResource(R.raw.defaultapplist);
            byte[] buffer = new byte[in.available()];
            in.read(buffer);
            FileOutputStream outputStream = getContext().openFileOutput(this.mAppListFileName, 0);
            outputStream.write(buffer);
            outputStream.flush();
            outputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
    }

    private List<String> getAppNameList(String fileName) {
        Log.d("Launcher", "the file name====" + fileName);
        List<String> appNameList = new ArrayList<>();
        try {
            FileReader fr = new FileReader(fileName);
            BufferedReader buf = new BufferedReader(fr);
            try {
                String line = buf.readLine();
                String s = getRightNameString(line);
                int i = 0;
                while (line != null) {
                    if (s != null) {
                        appNameList.add(i, s);
                        i++;
                    }
                    line = buf.readLine();
                    s = getRightNameString(line);
                }
            } catch (IOException ex1) {
                ex1.printStackTrace();
            }
            try {
                buf.close();
                fr.close();
            } catch (IOException ex2) {
                ex2.printStackTrace();
            }
            FileReader fileReader = fr;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return appNameList;
    }

    private String getRightNameString(String strLine) {
        if (strLine == null) {
            return null;
        }
        int iEnd = strLine.indexOf(35);
        if (iEnd > 0) {
            return strLine.substring(0, iEnd);
        }
        if (iEnd < 0) {
            return strLine;
        }
        return null;
    }

    private void sortByKonka(ArrayList<ApplicationInfo> list) {
        List<String> appNameList;
        Context context = getContext();
        String fileName = null;
        try {
            fileName = KKCommonManager.getInstance(context).getLauncherConfigurationFileName(EN_KK_LAUNCHER_CONFIG_FILE_TYPE.APP_LIST, context.getPackageName());
        } catch (NoClassDefFoundError | NoSuchMethodError e) {
        }
        if (fileName == null || !new File(fileName).exists()) {
            Log.d(LOG_TAG, "the applist is not found, so use the default list");
            appNameList = getAppNameList(context.getFilesDir() + "/" + this.mAppListFileName);
        } else {
            appNameList = getAppNameList(fileName);
        }
        Iterator<ApplicationInfo> iterator = list.iterator();
        while (iterator.hasNext()) {
            ApplicationInfo applicationInfo = (ApplicationInfo) iterator.next();
            int iIndex = appNameList.indexOf(applicationInfo.componentName.getClassName());
            if (iIndex != -1) {
                applicationInfo.sortOrderID = iIndex;
            }
        }
        Collections.sort(list, LauncherModel.APP_KONKA_DEFAULT_COMPARATOR);
    }

    public void showIndicator(boolean immediately) {
        showScrollingIndicator(immediately);
    }

    public void hideIndicator(boolean immediately) {
        hideScrollingIndicator(immediately);
    }

    public void flashIndicator(boolean immediately) {
        flashScrollingIndicator(!immediately);
    }

    public void setApps(ArrayList<ApplicationInfo> list) {
        this.mApps = list;
        if (this.mSortMode == SortMode.Title) {
            Collections.sort(this.mApps, LauncherModel.APP_NAME_COMPARATOR);
        } else if (this.mSortMode == SortMode.InstallDate) {
            Collections.sort(this.mApps, LauncherModel.APP_INSTALL_TIME_COMPARATOR);
        } else if (this.mSortMode == SortMode.KKDefault) {
            sortByKonka(this.mApps);
        }
        updatePageCounts();
        if (testDataReady()) {
            requestLayout();
        }
    }

    private void addAppsWithoutInvalidate(ArrayList<ApplicationInfo> list) {
        Iterator it = list.iterator();
        while (it.hasNext()) {
            ApplicationInfo info = (ApplicationInfo) it.next();
            int index = 0;
            if (this.mSortMode == SortMode.Title) {
                index = Collections.binarySearch(this.mApps, info, LauncherModel.APP_NAME_COMPARATOR);
            } else if (this.mSortMode == SortMode.InstallDate) {
                index = Collections.binarySearch(this.mApps, info, LauncherModel.APP_INSTALL_TIME_COMPARATOR);
            } else if (this.mSortMode == SortMode.KKDefault) {
                index = Collections.binarySearch(this.mApps, info, LauncherModel.APP_KONKA_DEFAULT_COMPARATOR);
            }
            if (index < 0) {
                this.mApps.add(-(index + 1), info);
            }
        }
    }

    public void addApps(ArrayList<ApplicationInfo> list) {
        addAppsWithoutInvalidate(list);
        updatePageCounts();
        invalidatePageData();
    }

    private int findAppByComponent(List<ApplicationInfo> list, ApplicationInfo item) {
        ComponentName removeComponent = item.intent.getComponent();
        int length = list.size();
        for (int i = 0; i < length; i++) {
            if (((ApplicationInfo) list.get(i)).intent.getComponent().equals(removeComponent)) {
                return i;
            }
        }
        return -1;
    }

    private void removeAppsWithoutInvalidate(ArrayList<ApplicationInfo> list) {
        Iterator it = list.iterator();
        while (it.hasNext()) {
            int removeIndex = findAppByComponent(this.mApps, (ApplicationInfo) it.next());
            if (removeIndex > -1) {
                this.mApps.remove(removeIndex);
            }
        }
    }

    public void removeApps(ArrayList<ApplicationInfo> list) {
        removeAppsWithoutInvalidate(list);
        updatePageCounts();
        invalidatePageData();
    }

    public void updateApps(ArrayList<ApplicationInfo> list) {
        removeAppsWithoutInvalidate(list);
        addAppsWithoutInvalidate(list);
        updatePageCounts();
        invalidatePageData();
    }

    public void reset() {
        if (this.mJoinWidgetsApps) {
            AppsCustomizeTabHost tabHost = getTabHost();
            String tag = tabHost.getCurrentTabTag();
            if (tag != null && !tag.equals(tabHost.getTabTagForContentType(ContentType.Apps))) {
                tabHost.setCurrentTabFromContent(ContentType.Apps);
            }
        } else if (this.mContentType != ContentType.Apps) {
            ((AppsCustomizeTabHost) this.mLauncher.findViewById(R.id.apps_customize_pane)).selectAppsTab();
            return;
        }
        if (this.mCurrentPage != 0) {
            invalidatePageData(0);
        }
    }

    /* access modifiers changed from: private */
    public AppsCustomizeTabHost getTabHost() {
        return (AppsCustomizeTabHost) this.mLauncher.findViewById(R.id.apps_customize_pane);
    }

    public void dumpState() {
        ApplicationInfo.dumpApplicationInfoList(LOG_TAG, "mApps", this.mApps);
        dumpAppWidgetProviderInfoList(LOG_TAG, "mWidgets", this.mWidgets);
    }

    private void dumpAppWidgetProviderInfoList(String tag, String label, ArrayList<Object> list) {
        Log.d(tag, new StringBuilder(String.valueOf(label)).append(" size=").append(list.size()).toString());
        Iterator it = list.iterator();
        while (it.hasNext()) {
            Object i = it.next();
            if (i instanceof AppWidgetProviderInfo) {
                AppWidgetProviderInfo info = (AppWidgetProviderInfo) i;
                Log.d(tag, "   label=\"" + info.label + "\" previewImage=" + info.previewImage + " resizeMode=" + info.resizeMode + " configure=" + info.configure + " initialLayout=" + info.initialLayout + " minWidth=" + info.minWidth + " minHeight=" + info.minHeight);
            } else if (i instanceof ResolveInfo) {
                ResolveInfo info2 = (ResolveInfo) i;
                Log.d(tag, "   label=\"" + info2.loadLabel(this.mPackageManager) + "\" icon=" + info2.icon);
            }
        }
    }

    public void surrender() {
        cancelAllTasks();
    }

    /* access modifiers changed from: protected */
    public int getAssociatedLowerPageBound(int page) {
        int count = getChildCount();
        return Math.max(Math.min(page - 2, count - Math.min(count, 5)), 0);
    }

    /* access modifiers changed from: protected */
    public int getAssociatedUpperPageBound(int page) {
        int count = getChildCount();
        return Math.min(Math.max(page + 2, Math.min(count, 5) - 1), count - 1);
    }

    /* access modifiers changed from: protected */
    public String getCurrentPageDescription() {
        int stringId;
        int count;
        int page = this.mNextPage != -1 ? this.mNextPage : this.mCurrentPage;
        int stringId2 = R.string.default_scroll_format;
        if (this.mJoinWidgetsApps) {
            if (page < this.mNumAppsPages) {
                stringId = R.string.apps_customize_apps_scroll_format;
                count = this.mNumAppsPages;
            } else {
                page -= this.mNumAppsPages;
                stringId = R.string.apps_customize_widgets_scroll_format;
                count = this.mNumWidgetPages;
            }
            return String.format(this.mContext.getString(stringId), new Object[]{Integer.valueOf(page + 1), Integer.valueOf(count)});
        }
        switch ($SWITCH_TABLE$com$cyanogenmod$trebuchet$AppsCustomizeView$ContentType()[this.mContentType.ordinal()]) {
            case 1:
                stringId2 = R.string.apps_customize_apps_scroll_format;
                break;
            case 2:
                stringId2 = R.string.apps_customize_widgets_scroll_format;
                break;
        }
        return String.format(this.mContext.getString(stringId2), new Object[]{Integer.valueOf(page + 1), Integer.valueOf(getChildCount())});
    }

    private void startApplication(View v) {
        final View fv = v;
        final ApplicationInfo appInfo = (ApplicationInfo) v.getTag();
        animateClickFeedback(v, new Runnable() {
            public void run() {
                AppsCustomizePagedView.this.mLauncher.startActivitySafely(fv, appInfo.intent, appInfo);
            }
        });
    }

    private void addToHomePage(View v) {
        ApplicationInfo appInfo = (ApplicationInfo) v.getTag();
        Intent shortcut = new Intent(InstallShortcutReceiver.ACTION_INSTALL_SHORTCUT);
        shortcut.putExtra("duplicate", true);
        shortcut.putExtra("android.intent.extra.shortcut.NAME", appInfo.title);
        shortcut.putExtra("android.intent.extra.shortcut.ICON", appInfo.iconBitmap);
        shortcut.putExtra("android.intent.extra.shortcut.INTENT", appInfo.intent);
        getContext().sendBroadcast(shortcut);
    }

    private void uninstallApp(View v) {
        boolean isUninstall = false;
        ApplicationInfo appInfo = (ApplicationInfo) v.getTag();
        if ((appInfo.flags & 1) != 0) {
            isUninstall = true;
        }
        if (!isUninstall) {
            Context context = getContext();
            Toast.makeText(context, context.getString(R.string.app_can_not_uninstall, new Object[]{appInfo.title}), 0).show();
            return;
        }
        this.mLauncher.startApplicationUninstallActivity(appInfo);
    }

    public void onItemClick(QuickAction source, int pos, int actionId) {
        if (source == this.mQuickAction) {
            switch (actionId) {
                case 0:
                    startApplication(this.mCurSelectView);
                    return;
                case 1:
                    addToHomePage(this.mCurSelectView);
                    return;
                case 2:
                    uninstallApp(this.mCurSelectView);
                    return;
                default:
                    return;
            }
        }
    }
}
