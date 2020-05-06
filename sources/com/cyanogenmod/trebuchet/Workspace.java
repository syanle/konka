package com.cyanogenmod.trebuchet;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.app.AlertDialog.Builder;
import android.app.WallpaperManager;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProviderInfo;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff.Mode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region.Op;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.IBinder;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Pair;
import android.view.Display;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.cyanogenmod.trebuchet.CellLayout.LayoutParams;
import com.cyanogenmod.trebuchet.DropTarget.DragObject;
import com.cyanogenmod.trebuchet.FolderIcon.FolderRingAnimator;
import com.cyanogenmod.trebuchet.InstallWidgetReceiver.WidgetListAdapter;
import com.cyanogenmod.trebuchet.InstallWidgetReceiver.WidgetMimeTypeHandlerData;
import com.cyanogenmod.trebuchet.RocketLauncher.Board.FlyingIcon;
import com.cyanogenmod.trebuchet.preference.PreferencesProvider.Interface.Homescreen;
import com.cyanogenmod.trebuchet.preference.PreferencesProvider.Interface.Homescreen.Indicator;
import com.cyanogenmod.trebuchet.preference.PreferencesProvider.Interface.Homescreen.Scrolling;
import com.konka.ios7launcher.R;
import com.konka.kkinterface.tv.CommonDesk;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.londatiga.android.ActionItem;
import net.londatiga.android.QuickAction;
import net.londatiga.android.QuickAction.OnActionItemClickListener;

public class Workspace extends PagedView implements DropTarget, DragSource, DragScroller, OnTouchListener, DragListener, OnActionItemClickListener {
    private static /* synthetic */ int[] $SWITCH_TABLE$com$cyanogenmod$trebuchet$Workspace$TransitionEffect = null;
    private static final int ADJACENT_SCREEN_DROP_DURATION = 300;
    private static final int BACKGROUND_FADE_OUT_DURATION = 350;
    private static float CAMERA_DISTANCE = 6500.0f;
    private static final int CHILDREN_OUTLINE_FADE_IN_DURATION = 100;
    private static final int CHILDREN_OUTLINE_FADE_OUT_DELAY = 0;
    private static final int CHILDREN_OUTLINE_FADE_OUT_DURATION = 375;
    private static final boolean DEBUG = false;
    private static final int DEFAULT_CELL_COUNT_X = 8;
    private static final int DEFAULT_CELL_COUNT_Y = 2;
    private static final int FOLDER_CREATION_TIMEOUT = 250;
    private static final int ID_QUICKACTION_APP_DELETE = 2;
    private static final int ID_QUICKACTION_APP_MOVE = 1;
    private static final int ID_QUICKACTION_APP_RUN = 0;
    private static final int ID_QUICKACTION_FOLDER_DELETE = 3;
    private static final int ID_QUICKACTION_FOLDER_MOVE = 2;
    private static final int ID_QUICKACTION_FOLDER_RENAME = 1;
    private static final int ID_QUICKACTION_FOLDER_RUN = 0;
    private static final int ID_QUICKACTION_WIDGET_DELETE = 1;
    private static final int ID_QUICKACTION_WIDGET_MOVE = 0;
    static final float MAX_SWIPE_ANGLE = 1.0471976f;
    static final float START_DAMPING_TOUCH_SLOP_ANGLE = 0.5235988f;
    private static final String TAG = "Launcher.Workspace";
    static final float TOUCH_SLOP_DAMPING_FACTOR = 4.0f;
    private static final float WALLPAPER_SCREENS_SPAN = 2.0f;
    private static final float WORKSPACE_OVERSCROLL_ROTATION = 24.0f;
    private static final float WORKSPACE_ROTATION = 12.5f;
    private static final float WORKSPACE_ROTATION_ANGLE = 12.5f;
    private AccelerateInterpolator mAlphaInterpolator;
    boolean mAnimatingViewIntoPlace;
    /* access modifiers changed from: private */
    public AnimatorSet mAnimator;
    private QuickAction mAppQuickAction;
    private Drawable mBackground;
    private float mBackgroundAlpha;
    private ValueAnimator mBackgroundFadeInAnimation;
    private ValueAnimator mBackgroundFadeOutAnimation;
    private final Camera mCamera;
    private AnimatorListener mChangeStateAnimationListener;
    boolean mChildrenLayersEnabled;
    private float mChildrenOutlineAlpha;
    private ObjectAnimator mChildrenOutlineFadeInAnimation;
    private ObjectAnimator mChildrenOutlineFadeOutAnimation;
    /* access modifiers changed from: private */
    public boolean mCreateUserFolderOnDrop;
    private CellInfo mCurSelectCell;
    private float mCurrentRotationY;
    private float mCurrentScaleX;
    private float mCurrentScaleY;
    private float mCurrentTranslationX;
    private float mCurrentTranslationY;
    private int mDefaultHomescreen;
    /* access modifiers changed from: private */
    public Runnable mDelayedResizeRunnable;
    /* access modifiers changed from: private */
    public int mDisplayHeight;
    /* access modifiers changed from: private */
    public int mDisplayWidth;
    /* access modifiers changed from: private */
    public DragController mDragController;
    /* access modifiers changed from: private */
    public FolderRingAnimator mDragFolderRingAnimator;
    private CellInfo mDragInfo;
    private Bitmap mDragOutline;
    private CellLayout mDragTargetLayout;
    private int mDragViewMultiplyColor;
    private float[] mDragViewVisualCenter;
    boolean mDrawBackground;
    private final Paint mExternalDragOutlinePaint;
    private boolean mFadeScrollingIndicator;
    private OnFocusChangeListener mFocusChangeListener;
    private final Alarm mFolderCreationAlarm;
    private QuickAction mFolderQuickAction;
    private boolean mHideIconLabels;
    private boolean mHomescreenAllowAdd;
    private IconCache mIconCache;
    private boolean mInScrollArea;
    private LayoutInflater mInflater;
    boolean mIsDragOccuring;
    /* access modifiers changed from: private */
    public boolean mIsSwitchingState;
    private View mLastDragOverView;
    /* access modifiers changed from: private */
    public Launcher mLauncher;
    private DecelerateInterpolator mLeftScreenAlphaInterpolator;
    private final Matrix mMatrix;
    /* access modifiers changed from: private */
    public float[] mNewAlphas;
    /* access modifiers changed from: private */
    public float[] mNewBackgroundAlphaMultipliers;
    /* access modifiers changed from: private */
    public float[] mNewBackgroundAlphas;
    /* access modifiers changed from: private */
    public float[] mNewRotationYs;
    /* access modifiers changed from: private */
    public float[] mNewRotations;
    /* access modifiers changed from: private */
    public float[] mNewScaleXs;
    /* access modifiers changed from: private */
    public float[] mNewScaleYs;
    /* access modifiers changed from: private */
    public float[] mNewTranslationXs;
    /* access modifiers changed from: private */
    public float[] mNewTranslationYs;
    private int mNumberHomescreens;
    /* access modifiers changed from: private */
    public float[] mOldAlphas;
    /* access modifiers changed from: private */
    public float[] mOldBackgroundAlphaMultipliers;
    /* access modifiers changed from: private */
    public float[] mOldBackgroundAlphas;
    /* access modifiers changed from: private */
    public float[] mOldRotationYs;
    /* access modifiers changed from: private */
    public float[] mOldRotations;
    /* access modifiers changed from: private */
    public float[] mOldScaleXs;
    /* access modifiers changed from: private */
    public float[] mOldScaleYs;
    /* access modifiers changed from: private */
    public float[] mOldTranslationXs;
    /* access modifiers changed from: private */
    public float[] mOldTranslationYs;
    private final HolographicOutlineHelper mOutlineHelper;
    private float mOverScrollMaxBackgroundAlpha;
    private int mOverScrollPageIndex;
    private float mOverscrollFade;
    private boolean mResizeAnyWidget;
    private float mSavedRotationY;
    private int mSavedScrollX;
    private float mSavedTranslationX;
    /* access modifiers changed from: private */
    public boolean mScaleOnItemSelect;
    private int mScreenPaddingHorizontal;
    private int mScreenPaddingVertical;
    /* access modifiers changed from: private */
    public boolean mScrollWallpaper;
    private final int mSearchScreen;
    private final boolean mSearchScreenEnable;
    private boolean mShowDockDivider;
    private boolean mShowScrollingIndicator;
    private boolean mShowSearchBar;
    private SpringLoadedDragController mSpringLoadedDragController;
    private float mSpringLoadedShrinkFactor;
    private State mState;
    /* access modifiers changed from: private */
    public State mStateAfterFirstLayout;
    private boolean mSwitchStateAfterFirstLayout;
    /* access modifiers changed from: private */
    public int[] mTargetCell;
    private int[] mTempCell;
    private float[] mTempCellLayoutCenterCoordinates;
    private float[] mTempDragBottomRightCoordinates;
    private float[] mTempDragCoordinates;
    private int[] mTempEstimate;
    private final float[] mTempFloat2;
    private Matrix mTempInverseMatrix;
    private final Rect mTempRect;
    private final int[] mTempXY;
    private TransitionEffect mTransitionEffect;
    /* access modifiers changed from: private */
    public float mTransitionProgress;
    boolean mUpdateWallpaperOffsetImmediately;
    int mWallpaperHeight;
    /* access modifiers changed from: private */
    public final WallpaperManager mWallpaperManager;
    WallpaperOffsetInterpolator mWallpaperOffset;
    private float mWallpaperScrollRatio;
    private int mWallpaperTravelWidth;
    int mWallpaperWidth;
    private QuickAction mWidgetQuickAction;
    private IBinder mWindowToken;
    private float mXDown;
    private float mYDown;
    private final ZInterpolator mZInterpolator;
    private final ZoomInInterpolator mZoomInInterpolator;

    class FolderCreationAlarmListener implements OnAlarmListener {
        int cellX;
        int cellY;
        CellLayout layout;

        public FolderCreationAlarmListener(CellLayout layout2, int cellX2, int cellY2) {
            this.layout = layout2;
            this.cellX = cellX2;
            this.cellY = cellY2;
        }

        public void onAlarm(Alarm alarm) {
            if (Workspace.this.mDragFolderRingAnimator == null) {
                Workspace.this.mDragFolderRingAnimator = new FolderRingAnimator(Workspace.this.mLauncher, null);
            }
            Workspace.this.mDragFolderRingAnimator.setCell(this.cellX, this.cellY);
            Workspace.this.mDragFolderRingAnimator.setCellLayout(this.layout);
            Workspace.this.mDragFolderRingAnimator.animateToAcceptState();
            this.layout.showFolderAccept(Workspace.this.mDragFolderRingAnimator);
            this.layout.clearDragOutlines();
            Workspace.this.mCreateUserFolderOnDrop = true;
        }
    }

    static class InverseZInterpolator implements TimeInterpolator {
        private ZInterpolator zInterpolator;

        public InverseZInterpolator(float foc) {
            this.zInterpolator = new ZInterpolator(foc);
        }

        public float getInterpolation(float input) {
            return 1.0f - this.zInterpolator.getInterpolation(1.0f - input);
        }
    }

    enum State {
        NORMAL,
        SPRING_LOADED,
        SMALL
    }

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

    class WallpaperOffsetInterpolator {
        float mFinalHorizontalWallpaperOffset = FlyingIcon.ANGULAR_VMIN;
        float mFinalVerticalWallpaperOffset = 0.5f;
        float mHorizontalCatchupConstant = 0.35f;
        float mHorizontalWallpaperOffset = FlyingIcon.ANGULAR_VMIN;
        boolean mIsMovingFast;
        long mLastWallpaperOffsetUpdateTime;
        boolean mOverrideHorizontalCatchupConstant;
        float mVerticalCatchupConstant = 0.35f;
        float mVerticalWallpaperOffset = 0.5f;

        public WallpaperOffsetInterpolator() {
        }

        public void setOverrideHorizontalCatchupConstant(boolean override) {
            this.mOverrideHorizontalCatchupConstant = override;
        }

        public void setHorizontalCatchupConstant(float f) {
            this.mHorizontalCatchupConstant = f;
        }

        public void setVerticalCatchupConstant(float f) {
            this.mVerticalCatchupConstant = f;
        }

        public boolean computeScrollOffset() {
            if (Float.compare(this.mHorizontalWallpaperOffset, this.mFinalHorizontalWallpaperOffset) == 0 && Float.compare(this.mVerticalWallpaperOffset, this.mFinalVerticalWallpaperOffset) == 0) {
                this.mIsMovingFast = false;
                return false;
            }
            boolean isLandscape = Workspace.this.mDisplayWidth > Workspace.this.mDisplayHeight;
            long timeSinceLastUpdate = Math.max(1, Math.min(33, System.currentTimeMillis() - this.mLastWallpaperOffsetUpdateTime));
            float xdiff = Math.abs(this.mFinalHorizontalWallpaperOffset - this.mHorizontalWallpaperOffset);
            if (!this.mIsMovingFast && ((double) xdiff) > 0.07d) {
                this.mIsMovingFast = true;
            }
            float fractionToCatchUpIn1MsHorizontal = this.mOverrideHorizontalCatchupConstant ? this.mHorizontalCatchupConstant : this.mIsMovingFast ? isLandscape ? 0.5f : 0.75f : isLandscape ? 0.27f : 0.5f;
            float fractionToCatchUpIn1MsHorizontal2 = fractionToCatchUpIn1MsHorizontal / 33.0f;
            float fractionToCatchUpIn1MsVertical = this.mVerticalCatchupConstant / 33.0f;
            float hOffsetDelta = this.mFinalHorizontalWallpaperOffset - this.mHorizontalWallpaperOffset;
            float vOffsetDelta = this.mFinalVerticalWallpaperOffset - this.mVerticalWallpaperOffset;
            boolean jumpToFinalValue = Math.abs(hOffsetDelta) < 1.0E-5f && Math.abs(vOffsetDelta) < 1.0E-5f;
            if (!LauncherApplication.isScreenLarge() || jumpToFinalValue) {
                this.mHorizontalWallpaperOffset = this.mFinalHorizontalWallpaperOffset;
                this.mVerticalWallpaperOffset = this.mFinalVerticalWallpaperOffset;
            } else {
                float percentToCatchUpVertical = Math.min(1.0f, ((float) timeSinceLastUpdate) * fractionToCatchUpIn1MsVertical);
                this.mHorizontalWallpaperOffset += Math.min(1.0f, ((float) timeSinceLastUpdate) * fractionToCatchUpIn1MsHorizontal2) * hOffsetDelta;
                this.mVerticalWallpaperOffset += percentToCatchUpVertical * vOffsetDelta;
            }
            this.mLastWallpaperOffsetUpdateTime = System.currentTimeMillis();
            return true;
        }

        public float getCurrX() {
            return this.mHorizontalWallpaperOffset;
        }

        public float getFinalX() {
            return this.mFinalHorizontalWallpaperOffset;
        }

        public float getCurrY() {
            return this.mVerticalWallpaperOffset;
        }

        public float getFinalY() {
            return this.mFinalVerticalWallpaperOffset;
        }

        public void setFinalX(float x) {
            this.mFinalHorizontalWallpaperOffset = Math.max(FlyingIcon.ANGULAR_VMIN, Math.min(x, 1.0f));
        }

        public void setFinalY(float y) {
            this.mFinalVerticalWallpaperOffset = Math.max(FlyingIcon.ANGULAR_VMIN, Math.min(y, 1.0f));
        }

        public void jumpToFinal() {
            this.mHorizontalWallpaperOffset = this.mFinalHorizontalWallpaperOffset;
            this.mVerticalWallpaperOffset = this.mFinalVerticalWallpaperOffset;
        }
    }

    enum WallpaperVerticalOffset {
        TOP,
        MIDDLE,
        BOTTOM
    }

    static class ZInterpolator implements TimeInterpolator {
        private float focalLength;

        public ZInterpolator(float foc) {
            this.focalLength = foc;
        }

        public float getInterpolation(float input) {
            return (1.0f - (this.focalLength / (this.focalLength + input))) / (1.0f - (this.focalLength / (this.focalLength + 1.0f)));
        }
    }

    static class ZoomInInterpolator implements TimeInterpolator {
        private final DecelerateInterpolator decelerate = new DecelerateInterpolator(3.0f);
        private final InverseZInterpolator inverseZInterpolator = new InverseZInterpolator(0.35f);

        ZoomInInterpolator() {
        }

        public float getInterpolation(float input) {
            return this.decelerate.getInterpolation(this.inverseZInterpolator.getInterpolation(input));
        }
    }

    static class ZoomOutInterpolator implements TimeInterpolator {
        private final DecelerateInterpolator decelerate = new DecelerateInterpolator(0.75f);
        private final ZInterpolator zInterpolator = new ZInterpolator(0.13f);

        ZoomOutInterpolator() {
        }

        public float getInterpolation(float input) {
            return this.decelerate.getInterpolation(this.zInterpolator.getInterpolation(input));
        }
    }

    static /* synthetic */ int[] $SWITCH_TABLE$com$cyanogenmod$trebuchet$Workspace$TransitionEffect() {
        int[] iArr = $SWITCH_TABLE$com$cyanogenmod$trebuchet$Workspace$TransitionEffect;
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
            $SWITCH_TABLE$com$cyanogenmod$trebuchet$Workspace$TransitionEffect = iArr;
        }
        return iArr;
    }

    public Workspace(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Workspace(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mChildrenOutlineAlpha = FlyingIcon.ANGULAR_VMIN;
        this.mDrawBackground = true;
        this.mBackgroundAlpha = FlyingIcon.ANGULAR_VMIN;
        this.mOverScrollMaxBackgroundAlpha = FlyingIcon.ANGULAR_VMIN;
        this.mOverScrollPageIndex = -1;
        this.mWallpaperScrollRatio = 1.0f;
        this.mTargetCell = new int[2];
        this.mDragTargetLayout = null;
        this.mTempCell = new int[2];
        this.mTempEstimate = new int[2];
        this.mDragViewVisualCenter = new float[2];
        this.mTempDragCoordinates = new float[2];
        this.mTempCellLayoutCenterCoordinates = new float[2];
        this.mTempDragBottomRightCoordinates = new float[2];
        this.mTempInverseMatrix = new Matrix();
        this.mState = State.NORMAL;
        this.mIsSwitchingState = false;
        this.mSwitchStateAfterFirstLayout = false;
        this.mAnimatingViewIntoPlace = false;
        this.mIsDragOccuring = false;
        this.mChildrenLayersEnabled = true;
        this.mInScrollArea = false;
        this.mOutlineHelper = new HolographicOutlineHelper();
        this.mDragOutline = null;
        this.mTempRect = new Rect();
        this.mTempXY = new int[2];
        this.mOverscrollFade = FlyingIcon.ANGULAR_VMIN;
        this.mExternalDragOutlinePaint = new Paint();
        this.mMatrix = new Matrix();
        this.mCamera = new Camera();
        this.mTempFloat2 = new float[2];
        this.mUpdateWallpaperOffsetImmediately = false;
        this.mFolderCreationAlarm = new Alarm();
        this.mDragFolderRingAnimator = null;
        this.mLastDragOverView = null;
        this.mCreateUserFolderOnDrop = false;
        this.mFocusChangeListener = new OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (v != null) {
                    if (Workspace.this.mScaleOnItemSelect) {
                        float currentScale = v.getScaleX();
                        float toScale = hasFocus ? 1.1f : 1.0f;
                        ObjectAnimator anim = ObjectAnimator.ofPropertyValuesHolder(v, new PropertyValuesHolder[]{PropertyValuesHolder.ofFloat("scaleX", new float[]{currentScale, toScale}), PropertyValuesHolder.ofFloat("scaleY", new float[]{currentScale, toScale})});
                        anim.setDuration(100);
                        v.bringToFront();
                        anim.start();
                    }
                    if (hasFocus) {
                        try {
                            if (v.getBackground() instanceof AnimationDrawable) {
                                ((AnimationDrawable) v.getBackground()).start();
                            }
                        } catch (ClassCastException e) {
                        }
                    }
                }
            }
        };
        this.mZoomInInterpolator = new ZoomInInterpolator();
        this.mZInterpolator = new ZInterpolator(0.5f);
        this.mAlphaInterpolator = new AccelerateInterpolator(0.9f);
        this.mLeftScreenAlphaInterpolator = new DecelerateInterpolator(4.0f);
        this.mContentIsRefreshable = false;
        setDataIsReady();
        this.mHandleFadeInAdjacentScreens = true;
        this.mWallpaperManager = WallpaperManager.getInstance(context);
        int cellCountX = 8;
        int cellCountY = 2;
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.Workspace, defStyle, 0);
        Resources res = context.getResources();
        if (LauncherApplication.isScreenLarge()) {
            int[] cellCount = getCellCountsForLarge(context);
            cellCountX = cellCount[0];
            cellCountY = cellCount[1];
        }
        this.mSpringLoadedShrinkFactor = ((float) res.getInteger(R.integer.config_workspaceSpringLoadShrinkPercentage)) / 100.0f;
        this.mDragViewMultiplyColor = res.getColor(R.color.drag_view_multiply_color);
        int cellCountX2 = a.getInt(0, cellCountX);
        int cellCountY2 = a.getInt(1, cellCountY);
        a.recycle();
        if (!LauncherApplication.isScreenLarge()) {
            cellCountX2 = Homescreen.getCellCountX(context, cellCountX2);
            cellCountY2 = Homescreen.getCellCountY(context, cellCountY2);
        }
        this.mCellCountX = cellCountX2;
        this.mCellCountY = cellCountY2;
        LauncherModel.updateWorkspaceLayoutCells(cellCountX2, cellCountY2);
        setHapticFeedbackEnabled(false);
        this.mNumberHomescreens = Homescreen.getNumberHomescreens(context);
        LauncherModel.updateWorkspaceNumberHomescreens(context, this.mNumberHomescreens);
        this.mDefaultHomescreen = Homescreen.getDefaultHomescreen(context);
        if (this.mDefaultHomescreen >= this.mNumberHomescreens) {
            this.mDefaultHomescreen = this.mNumberHomescreens / 2;
        }
        this.mScreenPaddingVertical = Homescreen.getScreenPaddingVertical(context);
        this.mScreenPaddingHorizontal = Homescreen.getScreenPaddingHorizontal(context);
        this.mShowSearchBar = Homescreen.getShowSearchBar(context);
        this.mResizeAnyWidget = Homescreen.getResizeAnyWidget(context);
        this.mHideIconLabels = Homescreen.getHideIconLabels(context);
        this.mScrollWallpaper = Scrolling.getScrollWallpaper(context);
        this.mTransitionEffect = Scrolling.getTransitionEffect(context, res.getString(R.string.config_workspaceDefaultTransitionEffect));
        this.mFadeInAdjacentScreens = Scrolling.getFadeInAdjacentScreens(context, res.getBoolean(R.bool.config_workspaceDefualtFadeInAdjacentScreens));
        this.mShowScrollingIndicator = Indicator.getShowScrollingIndicator(context);
        this.mFadeScrollingIndicator = Indicator.getFadeScrollingIndicator(context);
        this.mShowDockDivider = Indicator.getShowDockDivider(context);
        this.mHomescreenAllowAdd = Homescreen.getHomescreenAllowAdd(context);
        this.mSearchScreenEnable = Homescreen.getSearchScreenEnable(context);
        this.mSearchScreen = res.getInteger(R.integer.config_searchScreen);
        this.mScaleOnItemSelect = Homescreen.getScaleOnItemSelect(context);
        this.mLauncher = (Launcher) context;
        initWorkspace();
        setMotionEventSplittingEnabled(true);
    }

    public static int[] getCellCountsForLarge(Context context) {
        int[] cellCount = new int[2];
        Resources res = context.getResources();
        float actionBarHeight = context.obtainStyledAttributes(new int[]{16843499}).getDimension(0, FlyingIcon.ANGULAR_VMIN);
        float systemBarHeight = res.getDimension(R.dimen.status_bar_height);
        float smallestScreenDimH = (float) res.getDisplayMetrics().heightPixels;
        float smallestScreenDimW = (float) res.getDisplayMetrics().widthPixels;
        cellCount[0] = 1;
        while (((float) CellLayout.widthInPortrait(res, cellCount[0] + 1)) <= smallestScreenDimW) {
            cellCount[0] = cellCount[0] + 1;
        }
        cellCount[1] = 1;
        while (((float) CellLayout.heightInLandscape(res, cellCount[1] + 1)) + actionBarHeight <= smallestScreenDimH - systemBarHeight) {
            cellCount[1] = cellCount[1] + 1;
        }
        return cellCount;
    }

    public int[] estimateItemSize(int hSpan, int vSpan, PendingAddItemInfo pendingItemInfo, boolean springLoaded) {
        int[] size = new int[2];
        if (getChildCount() > 0) {
            RectF r = estimateItemPosition((CellLayout) this.mLauncher.getWorkspace().getPageAt(0), pendingItemInfo, 0, 0, hSpan, vSpan);
            size[0] = (int) r.width();
            size[1] = (int) r.height();
            if (springLoaded) {
                size[0] = (int) (((float) size[0]) * this.mSpringLoadedShrinkFactor);
                size[1] = (int) (((float) size[1]) * this.mSpringLoadedShrinkFactor);
            }
        } else {
            size[0] = Integer.MAX_VALUE;
            size[1] = Integer.MAX_VALUE;
        }
        return size;
    }

    public RectF estimateItemPosition(CellLayout cl, ItemInfo pendingInfo, int hCell, int vCell, int hSpan, int vSpan) {
        RectF r = new RectF();
        cl.cellToRect(hCell, vCell, hSpan, vSpan, r);
        if (pendingInfo instanceof PendingAddWidgetInfo) {
            Rect p = this.mLauncher.getDefaultPaddingForWidget(this.mContext, ((PendingAddWidgetInfo) pendingInfo).componentName, null);
            r.top += (float) p.top;
            r.left += (float) p.left;
            r.right -= (float) p.right;
            r.bottom -= (float) p.bottom;
        }
        return r;
    }

    public void buildPageHardwareLayers() {
        updateChildrenLayersEnabled(true);
        if (getWindowToken() != null) {
            int childCount = getChildCount();
            for (int i = 0; i < childCount; i++) {
                ((CellLayout) getPageAt(i)).buildChildrenLayer();
            }
        }
        updateChildrenLayersEnabled(false);
    }

    public TransitionEffect getTransitionEffect() {
        return this.mTransitionEffect;
    }

    public State getState() {
        return this.mState;
    }

    public void onDragStart(DragSource source, Object info, int dragAction) {
        addView(getCellLayoutFromXml());
        this.mIsDragOccuring = true;
        updateChildrenLayersEnabled(false);
        this.mLauncher.lockScreenOrientationOnLargeUI();
    }

    public void onDragEnd() {
        this.mIsDragOccuring = false;
        updateChildrenLayersEnabled(false);
        removeEmptyHomescreens();
        refreshAddGuide();
        this.mLauncher.unlockScreenOrientationOnLargeUI();
    }

    private CellLayout getCellLayoutFromXml() {
        CellLayout screen = (CellLayout) this.mInflater.inflate(R.layout.workspace_screen, null);
        screen.setPadding(screen.getPaddingLeft() + this.mScreenPaddingHorizontal, screen.getPaddingTop() + this.mScreenPaddingVertical, screen.getPaddingRight() + this.mScreenPaddingHorizontal, screen.getPaddingBottom() + this.mScreenPaddingVertical);
        return screen;
    }

    /* access modifiers changed from: protected */
    public void initWorkspace() {
        Context context = getContext();
        this.mCurrentPage = this.mDefaultHomescreen;
        Launcher.setScreen(this.mCurrentPage);
        this.mIconCache = ((LauncherApplication) context.getApplicationContext()).getIconCache();
        this.mExternalDragOutlinePaint.setAntiAlias(true);
        setWillNotDraw(false);
        setChildrenDrawnWithCacheEnabled(true);
        Resources res = getResources();
        this.mInflater = (LayoutInflater) getContext().getSystemService("layout_inflater");
        int homeCount = this.mNumberHomescreens;
        for (int i = 0; i < homeCount; i++) {
            addView(getCellLayoutFromXml());
        }
        try {
            this.mBackground = res.getDrawable(R.drawable.apps_customize_bg);
        } catch (NotFoundException e) {
        }
        if (!this.mShowSearchBar) {
            int paddingTop = 0;
            if (this.mLauncher.getCurrentOrientation() == 1) {
                paddingTop = (int) res.getDimension(R.dimen.qsb_bar_hidden_inset);
            }
            setPadding(0, paddingTop, getPaddingRight(), getPaddingBottom());
        }
        if (!this.mShowScrollingIndicator) {
            disableScrollingIndicator();
        }
        this.mChangeStateAnimationListener = new AnimatorListenerAdapter() {
            public void onAnimationStart(Animator animation) {
                Workspace.this.mIsSwitchingState = true;
            }

            public void onAnimationEnd(Animator animation) {
                Workspace.this.mIsSwitchingState = false;
                if (Workspace.this.mScrollWallpaper) {
                    Workspace.this.mWallpaperOffset.setOverrideHorizontalCatchupConstant(false);
                }
                Workspace.this.mAnimator = null;
                Workspace.this.updateChildrenLayersEnabled(false);
            }
        };
        this.mSnapVelocity = CHILDREN_OUTLINE_FADE_IN_DURATION;
        Display display = this.mLauncher.getWindowManager().getDefaultDisplay();
        this.mDisplayWidth = display.getWidth();
        this.mDisplayHeight = display.getHeight();
        if (this.mScrollWallpaper) {
            this.mWallpaperOffset = new WallpaperOffsetInterpolator();
            this.mWallpaperTravelWidth = (int) (((float) this.mDisplayWidth) * wallpaperTravelToScreenWidthRatio(this.mDisplayWidth, this.mDisplayHeight));
        }
        this.mAppQuickAction = new QuickAction(context, 0, 3);
        this.mWidgetQuickAction = new QuickAction(context, 0, 3);
        this.mFolderQuickAction = new QuickAction(context, 0, 3);
        initQuickActionMenu();
    }

    public void removeViews(int start, int count) {
        super.removeViews(start, count);
        this.mNumberHomescreens = getChildCount();
        LauncherModel.updateWorkspaceNumberHomescreens(this.mLauncher, this.mNumberHomescreens);
    }

    public void removeViewAt(int index) {
        super.removeViewAt(index);
        this.mNumberHomescreens--;
        LauncherModel.updateWorkspaceNumberHomescreens(this.mLauncher, this.mNumberHomescreens);
    }

    /* access modifiers changed from: protected */
    public void onViewAdded(View child) {
        super.onViewAdded(child);
        if (!(child instanceof CellLayout)) {
            throw new IllegalArgumentException("A Workspace can only have CellLayout children.");
        }
        CellLayout cl = (CellLayout) child;
        cl.setOnInterceptTouchListener(this);
        cl.setClickable(true);
        cl.enableHardwareLayers();
        this.mNumberHomescreens++;
        LauncherModel.updateWorkspaceNumberHomescreens(this.mLauncher, this.mNumberHomescreens);
    }

    public void refreshAddGuide() {
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            ((CellLayout) getChildAt(i)).refreshGuideView();
        }
    }

    /* access modifiers changed from: 0000 */
    public Folder getOpenFolder() {
        DragLayer dragLayer = this.mLauncher.getDragLayer();
        int count = dragLayer.getChildCount();
        for (int i = 0; i < count; i++) {
            View child = dragLayer.getChildAt(i);
            if (child instanceof Folder) {
                Folder folder = (Folder) child;
                if (folder.getInfo().opened) {
                    return folder;
                }
            }
        }
        return null;
    }

    /* access modifiers changed from: 0000 */
    public boolean isTouchActive() {
        return this.mTouchState != 0;
    }

    /* access modifiers changed from: 0000 */
    public void addInScreen(View child, long container, int screen, int x, int y, int spanX, int spanY) {
        addInScreen(child, container, screen, x, y, spanX, spanY, false);
    }

    /* access modifiers changed from: 0000 */
    public void addInScreen(View child, long container, int screen, int x, int y, int spanX, int spanY, boolean insert) {
        CellLayout layout;
        if (container != -100 || (screen >= 0 && screen < getChildCount())) {
            if (container == -101) {
                layout = this.mLauncher.getHotseat().getLayout();
                child.setOnKeyListener(new HotseatIconKeyEventListener(this.mLauncher));
                if (screen < 0) {
                    screen = this.mLauncher.getHotseat().getOrderInHotseat(x, y);
                }
                Log.d(TAG, "x = " + x + " , y = " + y);
            } else {
                if (!this.mHideIconLabels) {
                    if (child instanceof FolderIcon) {
                        ((FolderIcon) child).setTextVisible(true);
                    } else if (child instanceof BubbleTextView) {
                        ((BubbleTextView) child).setTextVisible(true);
                    }
                }
                layout = (CellLayout) getPageAt(screen);
                child.setOnKeyListener(new IconKeyEventListener(this.mLauncher));
            }
            LayoutParams lp = (LayoutParams) child.getLayoutParams();
            if (lp == null) {
                lp = new LayoutParams(x, y, spanX, spanY);
            } else {
                lp.cellX = x;
                lp.cellY = y;
                lp.cellHSpan = spanX;
                lp.cellVSpan = spanY;
            }
            if (spanX < 0 && spanY < 0) {
                lp.isLockedToGrid = false;
            }
            int childId = LauncherModel.getCellLayoutChildId(container, screen, x, y, spanX, spanY);
            boolean markCellsAsOccupied = !(child instanceof Folder);
            Log.d("czj-ios", "Workspace.addInScreen -> CellLayout.addViewToCellLayout");
            if (!layout.addViewToCellLayout(child, insert ? 0 : -1, childId, lp, markCellsAsOccupied)) {
                Log.w(TAG, "Failed to add to item at (" + lp.cellX + "," + lp.cellY + ") to CellLayout");
            }
            if (!(child instanceof Folder) && container != -101) {
                child.setHapticFeedbackEnabled(false);
                child.setOnLongClickListener(this.mLongClickListener);
                child.setOnFocusChangeListener(this.mFocusChangeListener);
            }
            if (child instanceof DropTarget) {
                this.mDragController.addDropTarget((DropTarget) child);
                return;
            }
            return;
        }
        Log.e(TAG, "The screen must be >= 0 and < " + getChildCount() + " (was " + screen + "); skipping child");
    }

    private boolean hitsPage(int index, float x, float y) {
        View page = getPageAt(index);
        if (page == null) {
            return false;
        }
        float[] localXY = {x, y};
        mapPointFromSelfToChild(page, localXY);
        if (localXY[0] < FlyingIcon.ANGULAR_VMIN || localXY[0] >= ((float) page.getWidth()) || localXY[1] < FlyingIcon.ANGULAR_VMIN || localXY[1] >= ((float) page.getHeight())) {
            return false;
        }
        return true;
    }

    /* access modifiers changed from: protected */
    public boolean hitsPreviousPage(float x, float y) {
        return LauncherApplication.isScreenLarge() && hitsPage((this.mNextPage == -1 ? this.mCurrentPage : this.mNextPage) + -1, x, y);
    }

    /* access modifiers changed from: protected */
    public boolean hitsNextPage(float x, float y) {
        return LauncherApplication.isScreenLarge() && hitsPage((this.mNextPage == -1 ? this.mCurrentPage : this.mNextPage) + 1, x, y);
    }

    public boolean onTouch(View v, MotionEvent event) {
        return isSmall() || this.mIsSwitchingState;
    }

    public boolean isSwitchingState() {
        return this.mIsSwitchingState;
    }

    /* access modifiers changed from: protected */
    public void onWindowVisibilityChanged(int visibility) {
        this.mLauncher.onWindowVisibilityChanged(visibility);
    }

    public boolean dispatchUnhandledMove(View focused, int direction) {
        if (isSmall() || this.mIsSwitchingState || !isSearchScreen(getCurrentPage())) {
            return false;
        }
        return super.dispatchUnhandledMove(focused, direction);
    }

    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction() & 255) {
            case 0:
                this.mXDown = ev.getX();
                this.mYDown = ev.getY();
                break;
            case 1:
            case 6:
                if (this.mTouchState == 0 && !((CellLayout) getPageAt(this.mCurrentPage)).lastDownOnOccupiedCell()) {
                    onWallpaperTap(ev);
                    break;
                }
        }
        return super.onInterceptTouchEvent(ev);
    }

    /* access modifiers changed from: protected */
    public void determineScrollingStart(MotionEvent ev) {
        if (!isSmall() && !this.mIsSwitchingState) {
            float deltaX = Math.abs(ev.getX() - this.mXDown);
            float deltaY = Math.abs(ev.getY() - this.mYDown);
            if (Float.compare(deltaX, FlyingIcon.ANGULAR_VMIN) != 0) {
                float theta = (float) Math.atan((double) (deltaY / deltaX));
                if (deltaX > ((float) this.mTouchSlop) || deltaY > ((float) this.mTouchSlop)) {
                    cancelCurrentPageLongPress();
                }
                if (theta > MAX_SWIPE_ANGLE) {
                    return;
                }
                if (theta > START_DAMPING_TOUCH_SLOP_ANGLE) {
                    super.determineScrollingStart(ev, 1.0f + (4.0f * ((float) Math.sqrt((double) ((theta - START_DAMPING_TOUCH_SLOP_ANGLE) / START_DAMPING_TOUCH_SLOP_ANGLE)))));
                } else {
                    super.determineScrollingStart(ev);
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public boolean isScrollingIndicatorEnabled() {
        return this.mState != State.SPRING_LOADED;
    }

    /* access modifiers changed from: protected */
    public void onPageBeginMoving() {
        if (isHardwareAccelerated()) {
            updateChildrenLayersEnabled(false);
        } else if (this.mNextPage != -1) {
            enableChildrenCache(this.mCurrentPage, this.mNextPage);
        } else {
            enableChildrenCache(this.mCurrentPage - 1, this.mCurrentPage + 1);
        }
        if (LauncherApplication.isScreenLarge()) {
            showOutlines();
        }
    }

    /* access modifiers changed from: protected */
    public void onPageEndMoving() {
        if (this.mFadeScrollingIndicator) {
            hideScrollingIndicator(false);
        } else {
            updateScrollingIndicator();
        }
        if (isHardwareAccelerated()) {
            updateChildrenLayersEnabled(false);
        } else {
            clearChildrenCache();
        }
        if (!this.mDragController.dragging() && LauncherApplication.isScreenLarge()) {
            hideOutlines();
        }
        this.mOverScrollMaxBackgroundAlpha = FlyingIcon.ANGULAR_VMIN;
        this.mOverScrollPageIndex = -1;
        if (this.mDelayedResizeRunnable != null) {
            this.mDelayedResizeRunnable.run();
            this.mDelayedResizeRunnable = null;
        }
    }

    /* access modifiers changed from: protected */
    public void notifyPageSwitchListener() {
        super.notifyPageSwitchListener();
        Launcher.setScreen(this.mCurrentPage);
        this.mLauncher.refreshPageBtn();
    }

    /* access modifiers changed from: protected */
    public void flashScrollingIndicator(boolean animated) {
        if (this.mFadeScrollingIndicator) {
            super.flashScrollingIndicator(animated);
        } else {
            showScrollingIndicator(true);
        }
    }

    private float wallpaperTravelToScreenWidthRatio(int width, int height) {
        return (0.30769226f * (((float) width) / ((float) height))) + 1.0076923f;
    }

    private int getScrollRange() {
        return getChildOffset(getChildCount() - 1) - getChildOffset(0);
    }

    /* access modifiers changed from: protected */
    public void setWallpaperDimension() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        this.mLauncher.getWindowManager().getDefaultDisplay().getRealMetrics(displayMetrics);
        int maxDim = Math.max(displayMetrics.widthPixels, displayMetrics.heightPixels);
        int minDim = Math.min(displayMetrics.widthPixels, displayMetrics.heightPixels);
        if (LauncherApplication.isScreenLarge()) {
            this.mWallpaperWidth = (int) (((float) maxDim) * wallpaperTravelToScreenWidthRatio(maxDim, minDim));
            this.mWallpaperHeight = maxDim;
        } else {
            this.mWallpaperWidth = Math.max((int) (((float) minDim) * WALLPAPER_SCREENS_SPAN), maxDim);
            this.mWallpaperHeight = maxDim;
        }
        new Thread("setWallpaperDimension") {
            public void run() {
                Workspace.this.mWallpaperManager.suggestDesiredDimensions(Workspace.this.mWallpaperWidth, Workspace.this.mWallpaperHeight);
            }
        }.start();
    }

    public void setVerticalWallpaperOffset(float offset) {
        this.mWallpaperOffset.setFinalY(offset);
    }

    public float getVerticalWallpaperOffset() {
        return this.mWallpaperOffset.getCurrY();
    }

    public void setHorizontalWallpaperOffset(float offset) {
        this.mWallpaperOffset.setFinalX(offset);
    }

    public float getHorizontalWallpaperOffset() {
        return this.mWallpaperOffset.getCurrX();
    }

    private float wallpaperOffsetForCurrentScroll() {
        int wallpaperTravelWidth = this.mWallpaperWidth;
        if (LauncherApplication.isScreenLarge()) {
            wallpaperTravelWidth = this.mWallpaperTravelWidth;
        }
        this.mWallpaperManager.setWallpaperOffsetSteps(1.0f / ((float) (getChildCount() - 1)), 1.0f);
        float layoutScale = this.mLayoutScale;
        this.mLayoutScale = 1.0f;
        int scrollRange = getScrollRange();
        float adjustedScrollX = ((float) Math.max(0, Math.min(this.mScrollX, this.mMaxScrollX))) * this.mWallpaperScrollRatio;
        this.mLayoutScale = layoutScale;
        return ((((float) wallpaperTravelWidth) * (adjustedScrollX / ((float) scrollRange))) + ((float) ((this.mWallpaperWidth - wallpaperTravelWidth) / 2))) / ((float) this.mWallpaperWidth);
    }

    private void syncWallpaperOffsetWithScroll() {
        if (this.mScrollWallpaper) {
            this.mWallpaperOffset.setFinalX(wallpaperOffsetForCurrentScroll());
        }
    }

    private void centerWallpaperOffset() {
        if (this.mWindowToken != null) {
            this.mWallpaperManager.setWallpaperOffsetSteps(0.5f, FlyingIcon.ANGULAR_VMIN);
            this.mWallpaperManager.setWallpaperOffsets(getWindowToken(), 0.5f, FlyingIcon.ANGULAR_VMIN);
        }
    }

    public void updateWallpaperOffsetImmediately() {
        this.mUpdateWallpaperOffsetImmediately = true;
    }

    private void updateWallpaperOffsets() {
        boolean keepUpdating;
        boolean updateNow;
        if (this.mUpdateWallpaperOffsetImmediately) {
            updateNow = true;
            keepUpdating = false;
            this.mWallpaperOffset.jumpToFinal();
            this.mUpdateWallpaperOffsetImmediately = false;
        } else {
            keepUpdating = this.mWallpaperOffset.computeScrollOffset();
            updateNow = keepUpdating;
        }
        if (updateNow && this.mWindowToken != null) {
            this.mWallpaperManager.setWallpaperOffsets(this.mWindowToken, this.mWallpaperOffset.getCurrX(), this.mWallpaperOffset.getCurrY());
        }
        if (keepUpdating) {
            invalidate();
        }
    }

    /* access modifiers changed from: protected */
    public void updateCurrentPageScroll() {
        super.updateCurrentPageScroll();
        if (this.mScrollWallpaper) {
            computeWallpaperScrollRatio(this.mCurrentPage);
        }
    }

    /* access modifiers changed from: protected */
    public void snapToPage(int whichPage) {
        super.snapToPage(whichPage);
        if (this.mScrollWallpaper) {
            computeWallpaperScrollRatio(whichPage);
        }
    }

    private void computeWallpaperScrollRatio(int page) {
        float layoutScale = this.mLayoutScale;
        int scaled = getChildOffset(page) - getRelativeChildOffset(page);
        this.mLayoutScale = 1.0f;
        float unscaled = (float) (getChildOffset(page) - getRelativeChildOffset(page));
        this.mLayoutScale = layoutScale;
        if (scaled > 0) {
            this.mWallpaperScrollRatio = (1.0f * unscaled) / ((float) scaled);
        } else {
            this.mWallpaperScrollRatio = 1.0f;
        }
    }

    public void computeScroll() {
        super.computeScroll();
        if (this.mScrollWallpaper) {
            syncWallpaperOffsetWithScroll();
        }
    }

    /* access modifiers changed from: 0000 */
    public void showOutlines() {
    }

    /* access modifiers changed from: 0000 */
    public void hideOutlines() {
        if (!isSmall() && !this.mIsSwitchingState) {
            if (this.mChildrenOutlineFadeInAnimation != null) {
                this.mChildrenOutlineFadeInAnimation.cancel();
            }
            if (this.mChildrenOutlineFadeOutAnimation != null) {
                this.mChildrenOutlineFadeOutAnimation.cancel();
            }
            this.mChildrenOutlineFadeOutAnimation = ObjectAnimator.ofFloat(this, "childrenOutlineAlpha", new float[]{0.0f});
            this.mChildrenOutlineFadeOutAnimation.setDuration(375);
            this.mChildrenOutlineFadeOutAnimation.setStartDelay(0);
            this.mChildrenOutlineFadeOutAnimation.start();
        }
    }

    public void showOutlinesTemporarily() {
        if (!this.mIsPageMoving && !isTouchActive()) {
            snapToPage(this.mCurrentPage);
        }
    }

    public void setChildrenOutlineAlpha(float alpha) {
        this.mChildrenOutlineAlpha = alpha;
        for (int i = 0; i < getChildCount(); i++) {
            ((CellLayout) getPageAt(i)).setBackgroundAlpha(alpha);
        }
    }

    public float getChildrenOutlineAlpha() {
        return this.mChildrenOutlineAlpha;
    }

    /* access modifiers changed from: 0000 */
    public void disableBackground() {
        this.mDrawBackground = false;
    }

    /* access modifiers changed from: 0000 */
    public void enableBackground() {
        this.mDrawBackground = true;
    }

    private void animateBackgroundGradient(float finalAlpha, boolean animated) {
        if (this.mBackground != null) {
            if (this.mBackgroundFadeInAnimation != null) {
                this.mBackgroundFadeInAnimation.cancel();
                this.mBackgroundFadeInAnimation = null;
            }
            if (this.mBackgroundFadeOutAnimation != null) {
                this.mBackgroundFadeOutAnimation.cancel();
                this.mBackgroundFadeOutAnimation = null;
            }
            float startAlpha = getBackgroundAlpha();
            if (finalAlpha == startAlpha) {
                return;
            }
            if (animated) {
                this.mBackgroundFadeOutAnimation = ValueAnimator.ofFloat(new float[]{startAlpha, finalAlpha});
                this.mBackgroundFadeOutAnimation.addUpdateListener(new AnimatorUpdateListener() {
                    public void onAnimationUpdate(ValueAnimator animation) {
                        Workspace.this.setBackgroundAlpha(((Float) animation.getAnimatedValue()).floatValue());
                    }
                });
                this.mBackgroundFadeOutAnimation.setInterpolator(new DecelerateInterpolator(1.5f));
                this.mBackgroundFadeOutAnimation.setDuration(350);
                this.mBackgroundFadeOutAnimation.start();
                return;
            }
            setBackgroundAlpha(finalAlpha);
        }
    }

    public void setBackgroundAlpha(float alpha) {
        if (alpha != this.mBackgroundAlpha) {
            this.mBackgroundAlpha = alpha;
            invalidate();
        }
    }

    public float getBackgroundAlpha() {
        return this.mBackgroundAlpha;
    }

    /* access modifiers changed from: protected */
    public float getOffsetXForRotation(float degrees, int width, int height) {
        this.mMatrix.reset();
        this.mCamera.save();
        this.mCamera.rotateY(Math.abs(degrees));
        this.mCamera.getMatrix(this.mMatrix);
        this.mCamera.restore();
        this.mMatrix.preTranslate(((float) (-width)) * 0.5f, ((float) (-height)) * 0.5f);
        this.mMatrix.postTranslate(((float) width) * 0.5f, ((float) height) * 0.5f);
        this.mTempFloat2[0] = (float) width;
        this.mTempFloat2[1] = (float) height;
        this.mMatrix.mapPoints(this.mTempFloat2);
        return (degrees > FlyingIcon.ANGULAR_VMIN ? 1.0f : -1.0f) * (((float) width) - this.mTempFloat2[0]);
    }

    /* access modifiers changed from: 0000 */
    public float backgroundAlphaInterpolator(float r) {
        if (r < 0.1f) {
            return FlyingIcon.ANGULAR_VMIN;
        }
        if (r > 0.4f) {
            return 1.0f;
        }
        return (r - 0.1f) / (0.4f - 0.1f);
    }

    /* access modifiers changed from: 0000 */
    public float overScrollBackgroundAlphaInterpolator(float r) {
        if (r > this.mOverScrollMaxBackgroundAlpha) {
            this.mOverScrollMaxBackgroundAlpha = r;
        } else if (r < this.mOverScrollMaxBackgroundAlpha) {
            r = this.mOverScrollMaxBackgroundAlpha;
        }
        return Math.min(r / 0.08f, 1.0f);
    }

    private void screenScrolledStandard(int screenScroll) {
        for (int i = 0; i < getChildCount(); i++) {
            CellLayout cl = (CellLayout) getPageAt(i);
            if (cl != null) {
                float scrollProgress = getScrollProgress(screenScroll, cl, i);
                if (this.mFadeInAdjacentScreens && !isSmall()) {
                    cl.setAlpha(1.0f - Math.abs(scrollProgress));
                    cl.invalidate();
                }
            }
        }
    }

    private void screenScrolledTablet(int screenScroll) {
        for (int i = 0; i < getChildCount(); i++) {
            CellLayout cl = (CellLayout) getPageAt(i);
            if (cl != null) {
                float scrollProgress = getScrollProgress(screenScroll, cl, i);
                float rotation = 12.5f * scrollProgress;
                cl.setTranslationX(getOffsetXForRotation(rotation, cl.getWidth(), cl.getHeight()));
                cl.setRotationY(rotation);
                if (this.mFadeInAdjacentScreens && !isSmall()) {
                    cl.setAlpha(1.0f - Math.abs(scrollProgress));
                }
                cl.invalidate();
            }
        }
        invalidate();
    }

    private void screenScrolledZoom(int screenScroll, boolean in) {
        float f;
        for (int i = 0; i < getChildCount(); i++) {
            CellLayout cl = (CellLayout) getPageAt(i);
            if (cl != null) {
                float scrollProgress = getScrollProgress(screenScroll, cl, i);
                if (in) {
                    f = -0.2f;
                } else {
                    f = 0.1f;
                }
                float scale = 1.0f + (f * Math.abs(scrollProgress));
                if (!in) {
                    cl.setTranslationX(((float) cl.getMeasuredWidth()) * 0.1f * (-scrollProgress));
                }
                cl.setScaleX(scale);
                cl.setScaleY(scale);
                if (this.mFadeInAdjacentScreens && !isSmall()) {
                    cl.setAlpha(1.0f - Math.abs(scrollProgress));
                }
                cl.invalidate();
            }
        }
    }

    private void screenScrolledRotate(int screenScroll, boolean up) {
        for (int i = 0; i < getChildCount(); i++) {
            CellLayout cl = (CellLayout) getPageAt(i);
            if (cl != null) {
                float scrollProgress = getScrollProgress(screenScroll, cl, i);
                float rotation = (up ? 12.5f : -12.5f) * scrollProgress;
                float translationX = ((float) cl.getMeasuredWidth()) * scrollProgress;
                float rotatePoint = (((float) cl.getMeasuredWidth()) * 0.5f) / ((float) Math.tan(Math.toRadians(6.25d)));
                cl.setPivotX(((float) cl.getMeasuredWidth()) * 0.5f);
                if (up) {
                    cl.setPivotY(-rotatePoint);
                } else {
                    cl.setPivotY(((float) cl.getMeasuredHeight()) + rotatePoint);
                }
                cl.setRotation(rotation);
                cl.setTranslationX(translationX);
                if (this.mFadeInAdjacentScreens && !isSmall()) {
                    cl.setAlpha(1.0f - Math.abs(scrollProgress));
                }
                cl.invalidate();
            }
        }
    }

    private void screenScrolledCube(int screenScroll, boolean in) {
        for (int i = 0; i < getChildCount(); i++) {
            CellLayout cl = (CellLayout) getPageAt(i);
            if (cl != null) {
                float scrollProgress = getScrollProgress(screenScroll, cl, i);
                float rotation = (in ? 90.0f : -90.0f) * scrollProgress;
                float alpha = 1.0f - Math.abs(scrollProgress);
                if (in) {
                    cl.setCameraDistance(this.mDensity * CAMERA_DISTANCE);
                }
                cl.setPivotX((float) (scrollProgress < FlyingIcon.ANGULAR_VMIN ? 0 : cl.getMeasuredWidth()));
                cl.setPivotY(((float) cl.getMeasuredHeight()) * 0.5f);
                cl.setRotationY(rotation);
                cl.setAlpha(alpha);
                cl.invalidate();
            }
        }
    }

    private void screenScrolledStack(int screenScroll) {
        float alpha;
        for (int i = 0; i < getChildCount(); i++) {
            CellLayout cl = (CellLayout) getPageAt(i);
            if (cl != null) {
                float scrollProgress = getScrollProgress(screenScroll, cl, i);
                float interpolatedProgress = this.mZInterpolator.getInterpolation(Math.abs(Math.min(scrollProgress, FlyingIcon.ANGULAR_VMIN)));
                float scale = (1.0f - interpolatedProgress) + (0.76f * interpolatedProgress);
                float translationX = Math.min(FlyingIcon.ANGULAR_VMIN, scrollProgress) * ((float) cl.getMeasuredWidth());
                if (LauncherApplication.isScreenLarge() && scrollProgress >= FlyingIcon.ANGULAR_VMIN) {
                    alpha = this.mLeftScreenAlphaInterpolator.getInterpolation(1.0f - scrollProgress);
                } else if (scrollProgress < FlyingIcon.ANGULAR_VMIN) {
                    alpha = this.mAlphaInterpolator.getInterpolation(1.0f - Math.abs(scrollProgress));
                } else {
                    alpha = 1.0f;
                }
                cl.setTranslationX(translationX);
                cl.setScaleX(scale);
                cl.setScaleY(scale);
                cl.setAlpha(alpha);
                if (alpha <= FlyingIcon.ANGULAR_VMIN) {
                    cl.setVisibility(4);
                } else if (cl.getVisibility() != 0) {
                    cl.setVisibility(0);
                }
                cl.invalidate();
            }
        }
        invalidate();
    }

    /* access modifiers changed from: protected */
    public void screenScrolled(int screenScroll) {
        int index;
        float f = 0.25f;
        boolean z = false;
        super.screenScrolled(screenScroll);
        if (!isSwitchingState()) {
            if (isSmall()) {
                for (int i = 0; i < getChildCount(); i++) {
                    CellLayout cl = (CellLayout) getPageAt(i);
                    if (cl != null) {
                        float rotation = 12.5f * getScrollProgress(screenScroll, cl, i);
                        cl.setTranslationX(FlyingIcon.ANGULAR_VMIN);
                        cl.setRotationY(rotation);
                        cl.invalidate();
                    }
                }
            } else if (this.mOverScrollX < 0 || this.mOverScrollX > this.mMaxScrollX) {
                if (this.mOverScrollX < 0) {
                    index = 0;
                } else {
                    index = getChildCount() - 1;
                }
                CellLayout cl2 = (CellLayout) getPageAt(index);
                if (!LauncherApplication.isScreenLarge()) {
                    if (cl2 != null) {
                        float scrollProgress = getScrollProgress(screenScroll, cl2, index);
                        float abs = Math.abs(scrollProgress);
                        if (index == 0) {
                            z = true;
                        }
                        cl2.setOverScrollAmount(abs, z);
                        float rotation2 = -24.0f * scrollProgress;
                        cl2.setCameraDistance(this.mDensity * CAMERA_DISTANCE);
                        float measuredWidth = (float) cl2.getMeasuredWidth();
                        if (index == 0) {
                            f = 0.75f;
                        }
                        cl2.setPivotX(f * measuredWidth);
                        cl2.setPivotY(((float) cl2.getMeasuredHeight()) * 0.5f);
                        cl2.setRotationY(rotation2);
                        cl2.setTranslationX(FlyingIcon.ANGULAR_VMIN);
                        cl2.setOverscrollTransformsDirty(true);
                        setFadeForOverScroll(Math.abs(scrollProgress));
                    }
                } else if (cl2 != null) {
                    float scrollProgress2 = getScrollProgress(screenScroll, cl2, index);
                    float abs2 = Math.abs(scrollProgress2);
                    if (index == 0) {
                        z = true;
                    }
                    cl2.setOverScrollAmount(abs2, z);
                    float f2 = -12.5f * scrollProgress2;
                    cl2.setBackgroundAlphaMultiplier(overScrollBackgroundAlphaInterpolator(Math.abs(scrollProgress2)));
                    this.mOverScrollPageIndex = index;
                    float measuredWidth2 = (float) cl2.getMeasuredWidth();
                    if (index == 0) {
                        f = 0.75f;
                    }
                    cl2.setPivotX(f * measuredWidth2);
                    cl2.setPivotY(((float) cl2.getMeasuredHeight()) * 0.5f);
                    cl2.setOverscrollTransformsDirty(true);
                }
            } else {
                if (LauncherApplication.isScreenLarge()) {
                    for (int i2 = 0; i2 < getChildCount(); i2++) {
                        if (this.mOverScrollPageIndex != i2) {
                            CellLayout cl3 = (CellLayout) getPageAt(i2);
                            if (cl3 != null) {
                                cl3.setBackgroundAlphaMultiplier(backgroundAlphaInterpolator(Math.abs(getScrollProgress(screenScroll, cl3, i2))));
                            }
                        }
                    }
                }
                if (this.mOverscrollFade != FlyingIcon.ANGULAR_VMIN) {
                    setFadeForOverScroll(FlyingIcon.ANGULAR_VMIN);
                }
                if (!isSwitchingState()) {
                    ((CellLayout) getPageAt(0)).resetOverscrollTransforms();
                    ((CellLayout) getPageAt(getChildCount() - 1)).resetOverscrollTransforms();
                }
                switch ($SWITCH_TABLE$com$cyanogenmod$trebuchet$Workspace$TransitionEffect()[this.mTransitionEffect.ordinal()]) {
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
        }
    }

    /* access modifiers changed from: protected */
    public void overScroll(float amount) {
        if (LauncherApplication.isScreenLarge()) {
            dampedOverScroll(amount);
        } else {
            acceleratedOverScroll(amount);
        }
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.mWindowToken = getWindowToken();
        computeScroll();
        this.mDragController.setWindowToken(this.mWindowToken);
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        this.mWindowToken = null;
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean changed, int left, int top, int right, int bottom) {
        if (this.mFirstLayout && this.mCurrentPage >= 0 && this.mCurrentPage < getChildCount()) {
            this.mUpdateWallpaperOffsetImmediately = true;
        }
        super.onLayout(changed, left, top, right, bottom);
        if (this.mSwitchStateAfterFirstLayout) {
            this.mSwitchStateAfterFirstLayout = false;
            post(new Runnable() {
                public void run() {
                    Workspace.this.changeState(Workspace.this.mStateAfterFirstLayout, false);
                }
            });
        }
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        if (this.mScrollWallpaper) {
            updateWallpaperOffsets();
        }
        if (this.mBackground != null && this.mBackgroundAlpha > FlyingIcon.ANGULAR_VMIN && this.mDrawBackground) {
            this.mBackground.setAlpha((int) (this.mBackgroundAlpha * 255.0f));
            this.mBackground.setBounds(this.mScrollX, 0, this.mScrollX + getMeasuredWidth(), getMeasuredHeight());
            this.mBackground.draw(canvas);
        }
        super.onDraw(canvas);
    }

    /* access modifiers changed from: 0000 */
    public boolean isDrawingBackgroundGradient() {
        return this.mBackground != null && this.mBackgroundAlpha > FlyingIcon.ANGULAR_VMIN && this.mDrawBackground;
    }

    public void scrollTo(int x, int y) {
        super.scrollTo(x, y);
        syncChildrenLayersEnabledOnVisiblePages();
    }

    /* access modifiers changed from: private */
    public void syncChildrenLayersEnabledOnVisiblePages() {
        if (this.mChildrenLayersEnabled) {
            getVisiblePages(this.mTempVisiblePagesRange);
            int leftScreen = this.mTempVisiblePagesRange[0];
            int rightScreen = this.mTempVisiblePagesRange[1];
            if (leftScreen != -1 && rightScreen != -1) {
                for (int i = leftScreen; i <= rightScreen; i++) {
                    ViewGroup page = (ViewGroup) getPageAt(i);
                    if (page.getVisibility() == 0 && page.getAlpha() > 0.020833334f) {
                        ((CellLayout) getPageAt(i)).enableHardwareLayers();
                    }
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (this.mInScrollArea && !LauncherApplication.isScreenLarge()) {
            int width = getWidth();
            int height = getHeight();
            int offset = (((height - getPageAt(0).getHeight()) - this.mPaddingTop) - this.mPaddingBottom) / 2;
            int paddingTop = this.mPaddingTop + offset;
            int paddingBottom = this.mPaddingBottom + offset;
            CellLayout leftPage = (CellLayout) getPageAt(this.mCurrentPage - 1);
            CellLayout rightPage = (CellLayout) getPageAt(this.mCurrentPage + 1);
            if (leftPage != null && leftPage.getIsDragOverlapping()) {
                Drawable d = getResources().getDrawable(R.drawable.page_hover_left_holo);
                d.setBounds(this.mScrollX, paddingTop, this.mScrollX + d.getIntrinsicWidth(), height - paddingBottom);
                d.draw(canvas);
            } else if (rightPage != null && rightPage.getIsDragOverlapping()) {
                Drawable d2 = getResources().getDrawable(R.drawable.page_hover_right_holo);
                d2.setBounds((this.mScrollX + width) - d2.getIntrinsicWidth(), paddingTop, this.mScrollX + width, height - paddingBottom);
                d2.draw(canvas);
            }
        }
    }

    /* access modifiers changed from: protected */
    public boolean onRequestFocusInDescendants(int direction, Rect previouslyFocusedRect) {
        if (this.mLauncher.isAllAppsVisible()) {
            return false;
        }
        Folder openFolder = getOpenFolder();
        if (openFolder != null) {
            return openFolder.requestFocus(direction, previouslyFocusedRect);
        }
        return super.onRequestFocusInDescendants(direction, previouslyFocusedRect);
    }

    public int getDescendantFocusability() {
        if (isSmall()) {
            return 393216;
        }
        return super.getDescendantFocusability();
    }

    public void addFocusables(ArrayList<View> views, int direction, int focusableMode) {
        if (!this.mLauncher.isAllAppsVisible()) {
            Folder openFolder = getOpenFolder();
            if (openFolder != null) {
                openFolder.addFocusables(views, direction);
            } else {
                super.addFocusables(views, direction, focusableMode);
            }
        }
    }

    public boolean isSmall() {
        return this.mState == State.SMALL || this.mState == State.SPRING_LOADED;
    }

    /* access modifiers changed from: 0000 */
    public void enableChildrenCache(int fromPage, int toPage) {
        if (fromPage > toPage) {
            int temp = fromPage;
            fromPage = toPage;
            toPage = temp;
        }
        int screenCount = getChildCount();
        int fromPage2 = Math.max(fromPage, 0);
        int toPage2 = Math.min(toPage, screenCount - 1);
        for (int i = fromPage2; i <= toPage2; i++) {
            CellLayout layout = (CellLayout) getPageAt(i);
            layout.setChildrenDrawnWithCacheEnabled(true);
            layout.setChildrenDrawingCacheEnabled(true);
        }
    }

    /* access modifiers changed from: 0000 */
    public void clearChildrenCache() {
        int screenCount = getChildCount();
        for (int i = 0; i < screenCount; i++) {
            CellLayout layout = (CellLayout) getPageAt(i);
            layout.setChildrenDrawnWithCacheEnabled(false);
            if (!isHardwareAccelerated()) {
                layout.setChildrenDrawingCacheEnabled(false);
            }
        }
    }

    /* access modifiers changed from: private */
    public void updateChildrenLayersEnabled(boolean force) {
        boolean small;
        boolean dragging;
        boolean enableChildrenLayers;
        if (isSmall() || this.mIsSwitchingState) {
            small = true;
        } else {
            small = false;
        }
        if (this.mAnimatingViewIntoPlace || this.mIsDragOccuring) {
            dragging = true;
        } else {
            dragging = false;
        }
        if (small || dragging || isPageMoving()) {
            enableChildrenLayers = true;
        } else {
            enableChildrenLayers = false;
        }
        if (enableChildrenLayers != this.mChildrenLayersEnabled) {
            this.mChildrenLayersEnabled = enableChildrenLayers;
            if (!enableChildrenLayers) {
                for (int i = 0; i < getPageCount(); i++) {
                    ((CellLayout) getPageAt(i)).disableHardwareLayers();
                }
            }
        }
    }

    private void enableHwLayersOnVisiblePages() {
        if (this.mChildrenLayersEnabled) {
            int screenCount = getChildCount();
            getVisiblePages(this.mTempVisiblePagesRange);
            int leftScreen = this.mTempVisiblePagesRange[0];
            int rightScreen = this.mTempVisiblePagesRange[1];
            if (leftScreen == rightScreen) {
                if (rightScreen < screenCount - 1) {
                    rightScreen++;
                } else if (leftScreen > 0) {
                    leftScreen--;
                }
            }
            for (int i = 0; i < screenCount; i++) {
                CellLayout layout = (CellLayout) getChildAt(i);
                if (leftScreen > i || i > rightScreen || layout.getVisibility() != 0 || layout.getAlpha() <= 0.020833334f) {
                    layout.disableHardwareLayers();
                }
            }
            for (int i2 = 0; i2 < screenCount; i2++) {
                CellLayout layout2 = (CellLayout) getChildAt(i2);
                if (leftScreen <= i2 && i2 <= rightScreen && layout2.getVisibility() == 0 && layout2.getAlpha() > 0.020833334f) {
                    layout2.enableHardwareLayers();
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onWallpaperTap(MotionEvent ev) {
        int[] position = this.mTempCell;
        getLocationOnScreen(position);
        int pointerIndex = ev.getActionIndex();
        position[0] = position[0] + ((int) ev.getX(pointerIndex));
        position[1] = position[1] + ((int) ev.getY(pointerIndex));
        this.mWallpaperManager.sendWallpaperCommand(getWindowToken(), ev.getAction() == 1 ? "android.wallpaper.tap" : "android.wallpaper.secondaryTap", position[0], position[1], 0, null);
    }

    public void onDragStartedWithItem(View v) {
        this.mDragOutline = createDragOutline(v, new Canvas(), HolographicOutlineHelper.MAX_OUTER_BLUR_RADIUS);
    }

    public void onDragStartedWithItem(PendingAddItemInfo info, Bitmap b, Paint alphaClipPaint) {
        Canvas canvas = new Canvas();
        int bitmapPadding = HolographicOutlineHelper.MAX_OUTER_BLUR_RADIUS;
        int[] size = estimateItemSize(info.spanX, info.spanY, info, false);
        this.mDragOutline = createDragOutline(b, canvas, bitmapPadding, size[0], size[1], alphaClipPaint);
    }

    public void onDragStopped(boolean success) {
        if (!success) {
            doDragExit(null);
        }
    }

    public void exitWidgetResizeMode() {
        this.mLauncher.getDragLayer().clearAllResizeFrames();
    }

    private void initAnimationArrays() {
        int childCount = getChildCount();
        if (this.mOldTranslationXs == null) {
            this.mOldTranslationXs = new float[childCount];
            this.mOldTranslationYs = new float[childCount];
            this.mOldScaleXs = new float[childCount];
            this.mOldScaleYs = new float[childCount];
            this.mOldBackgroundAlphas = new float[childCount];
            this.mOldBackgroundAlphaMultipliers = new float[childCount];
            this.mOldAlphas = new float[childCount];
            this.mOldRotations = new float[childCount];
            this.mOldRotationYs = new float[childCount];
            this.mNewTranslationXs = new float[childCount];
            this.mNewTranslationYs = new float[childCount];
            this.mNewScaleXs = new float[childCount];
            this.mNewScaleYs = new float[childCount];
            this.mNewBackgroundAlphas = new float[childCount];
            this.mNewBackgroundAlphaMultipliers = new float[childCount];
            this.mNewAlphas = new float[childCount];
            this.mNewRotations = new float[childCount];
            this.mNewRotationYs = new float[childCount];
        }
    }

    public void changeState(State shrinkState) {
        changeState(shrinkState, true);
    }

    /* access modifiers changed from: 0000 */
    public void changeState(State state, boolean animated) {
        changeState(state, animated, 0);
    }

    /* access modifiers changed from: 0000 */
    public void changeState(State state, boolean animated, int delay) {
        int duration;
        if (this.mState != state) {
            if (this.mFirstLayout) {
                this.mSwitchStateAfterFirstLayout = false;
                this.mStateAfterFirstLayout = state;
                return;
            }
            initAnimationArrays();
            if (this.mAnimator != null) {
                this.mAnimator.cancel();
            }
            this.mAnimator = new AnimatorSet();
            setCurrentPage(this.mNextPage != -1 ? this.mNextPage : this.mCurrentPage);
            State oldState = this.mState;
            boolean oldStateIsNormal = oldState == State.NORMAL;
            final boolean oldStateIsSmall = oldState == State.SMALL;
            this.mState = state;
            boolean stateIsNormal = state == State.NORMAL;
            boolean stateIsSpringLoaded = state == State.SPRING_LOADED;
            boolean stateIsSmall = state == State.SMALL;
            float finalScaleFactor = 1.0f;
            float finalBackgroundAlpha = stateIsSpringLoaded ? 1.0f : FlyingIcon.ANGULAR_VMIN;
            float translationX = FlyingIcon.ANGULAR_VMIN;
            boolean zoomIn = true;
            if (state != State.NORMAL) {
                finalScaleFactor = this.mSpringLoadedShrinkFactor - (stateIsSmall ? 0.1f : FlyingIcon.ANGULAR_VMIN);
                if (!oldStateIsNormal || !stateIsSmall) {
                    finalBackgroundAlpha = 1.0f;
                    setLayoutScale(finalScaleFactor);
                } else {
                    zoomIn = false;
                    if (animated) {
                        hideScrollingIndicator(false, 150);
                    }
                    setLayoutScale(finalScaleFactor);
                    updateChildrenLayersEnabled(false);
                }
            } else {
                setLayoutScale(1.0f);
            }
            if (zoomIn) {
                duration = getResources().getInteger(R.integer.config_workspaceUnshrinkTime);
            } else {
                duration = getResources().getInteger(R.integer.config_appsCustomizeWorkspaceShrinkTime);
            }
            int i = 0;
            while (i < getChildCount()) {
                CellLayout cl = (CellLayout) getPageAt(i);
                float rotation = FlyingIcon.ANGULAR_VMIN;
                float rotationY = FlyingIcon.ANGULAR_VMIN;
                float initialAlpha = cl.getAlpha();
                float finalAlphaMultiplierValue = 1.0f;
                float finalAlpha = (!this.mFadeInAdjacentScreens || stateIsSpringLoaded || i == this.mCurrentPage) ? 1.0f : FlyingIcon.ANGULAR_VMIN;
                if ((oldStateIsSmall && stateIsNormal) || (oldStateIsNormal && stateIsSmall)) {
                    if (i == this.mCurrentPage || !animated) {
                        finalAlpha = 1.0f;
                        finalAlphaMultiplierValue = FlyingIcon.ANGULAR_VMIN;
                    } else {
                        initialAlpha = FlyingIcon.ANGULAR_VMIN;
                        finalAlpha = FlyingIcon.ANGULAR_VMIN;
                    }
                }
                if (this.mTransitionEffect == TransitionEffect.Tablet || stateIsSmall || stateIsSpringLoaded) {
                    if (i < this.mCurrentPage) {
                        rotationY = 12.5f;
                    } else if (i > this.mCurrentPage) {
                        rotationY = -12.5f;
                    }
                }
                if (this.mTransitionEffect == TransitionEffect.Stack) {
                    if (stateIsSmall || stateIsSpringLoaded) {
                        cl.setVisibility(0);
                    } else if (stateIsNormal) {
                        if (i <= this.mCurrentPage) {
                            cl.setVisibility(0);
                        } else {
                            cl.setVisibility(8);
                        }
                    }
                }
                if ((this.mTransitionEffect == TransitionEffect.Tablet && stateIsNormal) || (LauncherApplication.isScreenLarge() && (stateIsSmall || stateIsSpringLoaded))) {
                    translationX = getOffsetXForRotation(rotationY, cl.getWidth(), cl.getHeight());
                }
                if (stateIsNormal && (this.mTransitionEffect == TransitionEffect.RotateUp || this.mTransitionEffect == TransitionEffect.RotateDown)) {
                    rotation = (this.mTransitionEffect == TransitionEffect.RotateUp ? 12.5f : -12.5f) * ((float) Math.abs(this.mCurrentPage - i));
                }
                if (stateIsSmall || stateIsSpringLoaded) {
                    cl.setCameraDistance(1280.0f * this.mDensity);
                    if (this.mTransitionEffect == TransitionEffect.RotateUp || this.mTransitionEffect == TransitionEffect.RotateDown) {
                        cl.setTranslationX(FlyingIcon.ANGULAR_VMIN);
                    }
                    cl.setPivotX(((float) cl.getMeasuredWidth()) * 0.5f);
                    cl.setPivotY(((float) cl.getMeasuredHeight()) * 0.5f);
                }
                this.mOldAlphas[i] = initialAlpha;
                this.mNewAlphas[i] = finalAlpha;
                if (animated) {
                    this.mOldTranslationXs[i] = cl.getTranslationX();
                    this.mOldTranslationYs[i] = cl.getTranslationY();
                    this.mOldScaleXs[i] = cl.getScaleX();
                    this.mOldScaleYs[i] = cl.getScaleY();
                    this.mOldBackgroundAlphas[i] = cl.getBackgroundAlpha();
                    this.mOldBackgroundAlphaMultipliers[i] = cl.getBackgroundAlphaMultiplier();
                    this.mOldRotations[i] = cl.getRotation();
                    this.mOldRotationYs[i] = cl.getRotationY();
                    this.mNewTranslationXs[i] = translationX;
                    this.mNewTranslationYs[i] = 0.0f;
                    this.mNewScaleXs[i] = finalScaleFactor;
                    this.mNewScaleYs[i] = finalScaleFactor;
                    this.mNewBackgroundAlphas[i] = finalBackgroundAlpha;
                    this.mNewBackgroundAlphaMultipliers[i] = finalAlphaMultiplierValue;
                    this.mNewRotations[i] = rotation;
                    this.mNewRotationYs[i] = rotationY;
                } else {
                    cl.setTranslationX(translationX);
                    cl.setTranslationY(FlyingIcon.ANGULAR_VMIN);
                    cl.setScaleX(finalScaleFactor);
                    cl.setScaleY(finalScaleFactor);
                    cl.setBackgroundAlpha(finalBackgroundAlpha);
                    cl.setBackgroundAlphaMultiplier(finalAlphaMultiplierValue);
                    cl.setAlpha(finalAlpha);
                    cl.setRotation(rotation);
                    cl.setRotationY(rotationY);
                    this.mChangeStateAnimationListener.onAnimationEnd(null);
                }
                i++;
            }
            if (animated) {
                ValueAnimator animWithInterpolator = ValueAnimator.ofFloat(new float[]{FlyingIcon.ANGULAR_VMIN, 1.0f}).setDuration((long) duration);
                if (zoomIn) {
                    animWithInterpolator.setInterpolator(this.mZoomInInterpolator);
                }
                final boolean z = stateIsNormal;
                AnonymousClass6 r0 = new AnimatorListenerAdapter() {
                    public void onAnimationEnd(Animator animation) {
                        if (!Workspace.this.mFadeInAdjacentScreens && z && oldStateIsSmall) {
                            for (int i = 0; i < Workspace.this.getChildCount(); i++) {
                                ((CellLayout) Workspace.this.getPageAt(i)).setAlpha(1.0f);
                            }
                        }
                    }
                };
                animWithInterpolator.addListener(r0);
                AnonymousClass7 r02 = new LauncherAnimatorUpdateListener() {
                    public void onAnimationUpdate(float a, float b) {
                        Workspace.this.mTransitionProgress = b;
                        if (b != FlyingIcon.ANGULAR_VMIN) {
                            Workspace.this.invalidate();
                            for (int i = 0; i < Workspace.this.getChildCount(); i++) {
                                CellLayout cl = (CellLayout) Workspace.this.getPageAt(i);
                                cl.invalidate();
                                cl.setTranslationX((Workspace.this.mOldTranslationXs[i] * a) + (Workspace.this.mNewTranslationXs[i] * b));
                                cl.setTranslationY((Workspace.this.mOldTranslationYs[i] * a) + (Workspace.this.mNewTranslationYs[i] * b));
                                cl.setScaleX((Workspace.this.mOldScaleXs[i] * a) + (Workspace.this.mNewScaleXs[i] * b));
                                cl.setScaleY((Workspace.this.mOldScaleYs[i] * a) + (Workspace.this.mNewScaleYs[i] * b));
                                cl.setFastBackgroundAlpha((Workspace.this.mOldBackgroundAlphas[i] * a) + (Workspace.this.mNewBackgroundAlphas[i] * b));
                                cl.setBackgroundAlphaMultiplier((Workspace.this.mOldBackgroundAlphaMultipliers[i] * a) + (Workspace.this.mNewBackgroundAlphaMultipliers[i] * b));
                                cl.setAlpha((Workspace.this.mOldAlphas[i] * a) + (Workspace.this.mNewAlphas[i] * b));
                                cl.invalidate();
                            }
                            Workspace.this.syncChildrenLayersEnabledOnVisiblePages();
                        }
                    }
                };
                animWithInterpolator.addUpdateListener(r02);
                ValueAnimator rotationAnim = ValueAnimator.ofFloat(new float[]{FlyingIcon.ANGULAR_VMIN, 1.0f}).setDuration((long) duration);
                rotationAnim.setInterpolator(new DecelerateInterpolator(WALLPAPER_SCREENS_SPAN));
                AnonymousClass8 r03 = new LauncherAnimatorUpdateListener() {
                    public void onAnimationUpdate(float a, float b) {
                        if (b != FlyingIcon.ANGULAR_VMIN) {
                            for (int i = 0; i < Workspace.this.getChildCount(); i++) {
                                CellLayout cl = (CellLayout) Workspace.this.getPageAt(i);
                                if (Workspace.this.mOldRotations[i] != Workspace.this.mNewRotations[i]) {
                                    cl.setRotation((Workspace.this.mOldRotations[i] * a) + (Workspace.this.mNewRotations[i] * b));
                                }
                                cl.setRotationY((Workspace.this.mOldRotationYs[i] * a) + (Workspace.this.mNewRotationYs[i] * b));
                            }
                        }
                    }
                };
                rotationAnim.addUpdateListener(r03);
                this.mAnimator.playTogether(new Animator[]{animWithInterpolator, rotationAnim});
                this.mAnimator.setStartDelay((long) delay);
                this.mAnimator.addListener(this.mChangeStateAnimationListener);
                this.mAnimator.start();
            }
            if (stateIsSpringLoaded) {
                animateBackgroundGradient(((float) getResources().getInteger(R.integer.config_appsCustomizeSpringLoadedBgAlpha)) / 100.0f, false);
            } else {
                animateBackgroundGradient(FlyingIcon.ANGULAR_VMIN, true);
            }
            syncChildrenLayersEnabledOnVisiblePages();
        }
    }

    private void drawDragView(View v, Canvas destCanvas, int padding, boolean pruneToDrawable) {
        Rect clipRect = this.mTempRect;
        v.getDrawingRect(clipRect);
        boolean textVisible = false;
        destCanvas.save();
        if (!(v instanceof TextView) || !pruneToDrawable) {
            if (v instanceof FolderIcon) {
                if (!this.mHideIconLabels && ((FolderIcon) v).getTextVisible()) {
                    ((FolderIcon) v).setTextVisible(false);
                    textVisible = true;
                }
            } else if (v instanceof BubbleTextView) {
                BubbleTextView tv = (BubbleTextView) v;
                clipRect.bottom = (tv.getExtendedPaddingTop() - 3) + tv.getLayout().getLineTop(0);
            } else if (v instanceof TextView) {
                TextView tv2 = (TextView) v;
                clipRect.bottom = (tv2.getExtendedPaddingTop() - tv2.getCompoundDrawablePadding()) + tv2.getLayout().getLineTop(0);
            }
            destCanvas.translate((float) ((-v.getScrollX()) + (padding / 2)), (float) ((-v.getScrollY()) + (padding / 2)));
            destCanvas.clipRect(clipRect, Op.REPLACE);
            v.draw(destCanvas);
            if (!this.mHideIconLabels && textVisible) {
                ((FolderIcon) v).setTextVisible(true);
            }
        } else {
            Drawable d = ((TextView) v).getCompoundDrawables()[1];
            clipRect.set(0, 0, d.getIntrinsicWidth() + padding, d.getIntrinsicHeight() + padding);
            destCanvas.translate((float) (padding / 2), (float) (padding / 2));
            d.draw(destCanvas);
        }
        destCanvas.restore();
    }

    public Bitmap createDragBitmap(View v, Canvas canvas, int padding) {
        Bitmap b;
        int outlineColor = getResources().getColor(R.color.konka_orange);
        if (v instanceof TextView) {
            Drawable d = ((TextView) v).getCompoundDrawables()[1];
            b = Bitmap.createBitmap(Launcher.sDisplayMetrics, d.getIntrinsicWidth() + padding, d.getIntrinsicHeight() + padding, Config.ARGB_8888);
        } else {
            b = Bitmap.createBitmap(Launcher.sDisplayMetrics, v.getWidth() + padding, v.getHeight() + padding, Config.ARGB_8888);
        }
        canvas.setBitmap(b);
        drawDragView(v, canvas, padding, true);
        this.mOutlineHelper.applyOuterBlur(b, canvas, outlineColor);
        canvas.drawColor(this.mDragViewMultiplyColor, Mode.MULTIPLY);
        canvas.setBitmap(null);
        return b;
    }

    private Bitmap createDragOutline(View v, Canvas canvas, int padding) {
        int outlineColor = getResources().getColor(R.color.konka_orange);
        Bitmap b = Bitmap.createBitmap(Launcher.sDisplayMetrics, v.getWidth() + padding, v.getHeight() + padding, Config.ARGB_8888);
        canvas.setBitmap(b);
        drawDragView(v, canvas, padding, true);
        this.mOutlineHelper.applyMediumExpensiveOutlineWithBlur(b, canvas, outlineColor, outlineColor);
        canvas.setBitmap(null);
        return b;
    }

    private Bitmap createDragOutline(Bitmap orig, Canvas canvas, int padding, int w, int h, Paint alphaClipPaint) {
        int outlineColor = getResources().getColor(R.color.konka_orange);
        Bitmap b = Bitmap.createBitmap(w, h, Config.ARGB_8888);
        canvas.setBitmap(b);
        Rect src = new Rect(0, 0, orig.getWidth(), orig.getHeight());
        float scaleFactor = Math.min(((float) (w - padding)) / ((float) orig.getWidth()), ((float) (h - padding)) / ((float) orig.getHeight()));
        int scaledWidth = (int) (((float) orig.getWidth()) * scaleFactor);
        int scaledHeight = (int) (((float) orig.getHeight()) * scaleFactor);
        Rect dst = new Rect(0, 0, scaledWidth, scaledHeight);
        dst.offset((w - scaledWidth) / 2, (h - scaledHeight) / 2);
        canvas.drawBitmap(orig, src, dst, null);
        this.mOutlineHelper.applyMediumExpensiveOutlineWithBlur(b, canvas, outlineColor, outlineColor, alphaClipPaint);
        canvas.setBitmap(null);
        return b;
    }

    private Bitmap createExternalDragOutline(Canvas canvas, int padding) {
        Resources r = getResources();
        int outlineColor = r.getColor(R.color.konka_orange);
        int iconWidth = r.getDimensionPixelSize(R.dimen.workspace_cell_width);
        int iconHeight = r.getDimensionPixelSize(R.dimen.workspace_cell_height);
        int rectRadius = r.getDimensionPixelSize(R.dimen.external_drop_icon_rect_radius);
        int inset = (int) (((float) Math.min(iconWidth, iconHeight)) * 0.2f);
        Bitmap b = Bitmap.createBitmap(iconWidth + padding, iconHeight + padding, Config.ARGB_8888);
        canvas.setBitmap(b);
        canvas.drawRoundRect(new RectF((float) inset, (float) inset, (float) (iconWidth - inset), (float) (iconHeight - inset)), (float) rectRadius, (float) rectRadius, this.mExternalDragOutlinePaint);
        this.mOutlineHelper.applyMediumExpensiveOutlineWithBlur(b, canvas, outlineColor, outlineColor);
        canvas.setBitmap(null);
        return b;
    }

    /* access modifiers changed from: 0000 */
    public void startDrag(CellInfo cellInfo) {
        View child = cellInfo.cell;
        this.mDragInfo = cellInfo;
        child.clearFocus();
        child.setPressed(false);
        child.setVisibility(8);
        this.mDragOutline = createDragOutline(child, new Canvas(), HolographicOutlineHelper.MAX_OUTER_BLUR_RADIUS);
        beginDragShared(child, this);
    }

    public void beginDragShared(View child, DragSource source) {
        Resources r = getResources();
        int bitmapPadding = HolographicOutlineHelper.MAX_OUTER_BLUR_RADIUS;
        Bitmap b = createDragBitmap(child, new Canvas(), bitmapPadding);
        int bmpWidth = b.getWidth();
        this.mLauncher.getDragLayer().getLocationInDragLayer(child, this.mTempXY);
        int dragLayerX = this.mTempXY[0] + ((child.getWidth() - bmpWidth) / 2);
        int dragLayerY = this.mTempXY[1] - (bitmapPadding / 2);
        Point dragVisualizeOffset = null;
        Rect dragRect = null;
        if ((child instanceof BubbleTextView) || (child instanceof PagedViewIcon)) {
            int iconSize = r.getDimensionPixelSize(R.dimen.app_icon_size);
            int iconPaddingTop = r.getDimensionPixelSize(R.dimen.app_icon_padding_top);
            int top = child.getPaddingTop();
            int left = (bmpWidth - iconSize) / 2;
            int right = left + iconSize;
            int bottom = top + iconSize;
            dragLayerY += top;
            dragVisualizeOffset = new Point((-bitmapPadding) / 2, iconPaddingTop - (bitmapPadding / 2));
            dragRect = new Rect(left, top, right, bottom);
        } else if (child instanceof FolderIcon) {
            dragRect = new Rect(0, 0, child.getWidth(), r.getDimensionPixelSize(R.dimen.folder_preview_size));
        }
        if (child instanceof BubbleTextView) {
            ((BubbleTextView) child).clearPressedOrFocusedBackground();
        }
        this.mDragController.startDrag(b, dragLayerX, dragLayerY, source, child.getTag(), DragController.DRAG_ACTION_MOVE, dragVisualizeOffset, dragRect, child.isInTouchMode());
        b.recycle();
    }

    /* access modifiers changed from: 0000 */
    public void addApplicationShortcut(ShortcutInfo info, CellLayout target, long container, int screen, int cellX, int cellY, boolean insertAtFirst, int intersectX, int intersectY) {
        View view = this.mLauncher.createShortcut(R.layout.application, target, info);
        AppInfoManager.getInstance().setIOS7Icon(info, this.mIconCache);
        int[] cellXY = new int[2];
        target.findCellForSpanThatIntersects(cellXY, 1, 1, intersectX, intersectY);
        addInScreen(view, container, screen, cellXY[0], cellXY[1], 1, 1, insertAtFirst);
        LauncherModel.addOrMoveItemInDatabase(this.mLauncher, info, container, screen, cellXY[0], cellXY[1]);
    }

    public boolean transitionStateShouldAllowDrop() {
        return !isSwitchingState() || this.mTransitionProgress > 0.5f;
    }

    public boolean acceptDrop(DragObject d) {
        int spanX;
        int spanY;
        if (d.dragSource != this) {
            if (this.mDragTargetLayout == null) {
                return false;
            }
            if (!transitionStateShouldAllowDrop()) {
                return false;
            }
            ItemInfo info = (ItemInfo) d.dragInfo;
            if (((info instanceof ApplicationInfo) || (info instanceof ShortcutInfo)) && ((info.itemType == 0 || info.itemType == 1) && !(d.dragSource instanceof Folder))) {
                Intent intent = info instanceof ShortcutInfo ? ((ShortcutInfo) info).intent : ((ApplicationInfo) info).intent;
                if (LauncherModel.shortcutExistsByIntent(this.mContext, intent)) {
                    Log.d(TAG, "already on workspace: " + intent.toString());
                    return false;
                }
            }
            this.mDragViewVisualCenter = getDragViewVisualCenter(d.x, d.y, d.xOffset, d.yOffset, d.dragView, this.mDragViewVisualCenter);
            if (this.mLauncher.isHotseatLayout(this.mDragTargetLayout)) {
                mapPointFromSelfToSibling(this.mLauncher.getHotseat(), this.mDragViewVisualCenter);
            } else {
                mapPointFromSelfToChild(this.mDragTargetLayout, this.mDragViewVisualCenter, null);
            }
            View ignoreView = null;
            if (this.mDragInfo != null) {
                CellInfo dragCellInfo = this.mDragInfo;
                spanX = dragCellInfo.spanX;
                spanY = dragCellInfo.spanY;
                ignoreView = dragCellInfo.cell;
            } else {
                ItemInfo dragInfo = (ItemInfo) d.dragInfo;
                spanX = dragInfo.spanX;
                spanY = dragInfo.spanY;
            }
            this.mTargetCell = findNearestArea((int) this.mDragViewVisualCenter[0], (int) this.mDragViewVisualCenter[1], spanX, spanY, this.mDragTargetLayout, this.mTargetCell);
            if (!checkDropPermission()) {
                return false;
            }
            if (willCreateUserFolder((ItemInfo) d.dragInfo, this.mDragTargetLayout, this.mTargetCell, true)) {
                return true;
            }
            if (willAddToExistingUserFolder(d.dragInfo, this.mDragTargetLayout, this.mTargetCell)) {
                return true;
            }
            if (!this.mDragTargetLayout.findCellForSpanIgnoring(null, spanX, spanY, ignoreView)) {
                if (this.mTargetCell != null && this.mLauncher.isHotseatLayout(this.mDragTargetLayout) && Hotseat.isAllAppsButtonRank(this.mLauncher.getHotseat().getOrderInHotseat(this.mTargetCell[0], this.mTargetCell[1]))) {
                    return false;
                }
                this.mLauncher.showOutOfSpaceMessage();
                return false;
            }
        } else if (!checkDropPermission()) {
            return false;
        }
        return true;
    }

    /* access modifiers changed from: 0000 */
    public boolean checkDropPermission() {
        if (this.mTargetCell[0] < 0) {
            int i = this.mDragInfo.screen;
        } else {
            int indexOfChild = indexOfChild(this.mDragTargetLayout);
        }
        if (this.mLauncher.isHotseatLayout(this.mDragTargetLayout)) {
            return false;
        }
        if (!isAllowAddItem(this.mDragTargetLayout)) {
            this.mLauncher.showNotAllowedAddMessage();
            return false;
        } else if (!isTargetCellLocked(this.mDragTargetLayout, this.mTargetCell)) {
            return true;
        } else {
            this.mLauncher.warnTargetisLocked();
            return false;
        }
    }

    /* access modifiers changed from: 0000 */
    public boolean isTargetCellLocked(CellLayout target, int[] targetCell) {
        View dropOverView = target.getChildAt(targetCell[0], targetCell[1]);
        if (dropOverView == null) {
            return false;
        }
        return this.mLauncher.isItemLocked((ItemInfo) dropOverView.getTag());
    }

    /* access modifiers changed from: 0000 */
    public boolean willCreateUserFolder(ItemInfo info, CellLayout target, int[] targetCell, boolean considerTimeout) {
        boolean willBecomeShortcut;
        View dropOverView = target.getChildAt(targetCell[0], targetCell[1]);
        boolean hasntMoved = false;
        if (this.mDragInfo != null) {
            CellLayout cellParent = getParentCellLayoutForView(this.mDragInfo.cell);
            if (this.mDragInfo.cellX == targetCell[0] && this.mDragInfo.cellY == targetCell[1] && cellParent == target) {
                hasntMoved = true;
            } else {
                hasntMoved = false;
            }
        }
        if (dropOverView == null || hasntMoved) {
            return false;
        }
        if (considerTimeout && !this.mCreateUserFolderOnDrop) {
            return false;
        }
        boolean aboveShortcut = dropOverView.getTag() instanceof ShortcutInfo;
        if (info.itemType == 0 || info.itemType == 1) {
            willBecomeShortcut = true;
        } else {
            willBecomeShortcut = false;
        }
        if (!aboveShortcut || !willBecomeShortcut) {
            return false;
        }
        return true;
    }

    /* access modifiers changed from: 0000 */
    public boolean willAddToExistingUserFolder(Object dragInfo, CellLayout target, int[] targetCell) {
        View dropOverView = target.getChildAt(targetCell[0], targetCell[1]);
        if (!(dropOverView instanceof FolderIcon) || !((FolderIcon) dropOverView).acceptDrop(dragInfo)) {
            return false;
        }
        return true;
    }

    /* access modifiers changed from: 0000 */
    public boolean createUserFolderIfNecessary(View newView, long container, CellLayout target, int[] targetCell, boolean external, DragView dragView, Runnable postAnimationRunnable) {
        int screen;
        View v = target.getChildAt(targetCell[0], targetCell[1]);
        boolean hasntMoved = false;
        if (this.mDragInfo != null) {
            hasntMoved = this.mDragInfo.cellX == targetCell[0] && this.mDragInfo.cellY == targetCell[1] && getParentCellLayoutForView(this.mDragInfo.cell) == target;
        }
        if (v == null || hasntMoved || !this.mCreateUserFolderOnDrop) {
            return false;
        }
        this.mCreateUserFolderOnDrop = false;
        if (targetCell == null) {
            screen = this.mDragInfo.screen;
        } else {
            screen = indexOfChild(target);
        }
        boolean willBecomeShortcut = newView.getTag() instanceof ShortcutInfo;
        if (!(v.getTag() instanceof ShortcutInfo) || !willBecomeShortcut) {
            return false;
        }
        ShortcutInfo sourceInfo = (ShortcutInfo) newView.getTag();
        ShortcutInfo destInfo = (ShortcutInfo) v.getTag();
        if (!external) {
            getParentCellLayoutForView(this.mDragInfo.cell).removeView(this.mDragInfo.cell);
        }
        Rect folderLocation = new Rect();
        float scale = this.mLauncher.getDragLayer().getDescendantRectRelativeToSelf(v, folderLocation);
        target.removeView(v);
        FolderIcon fi = this.mLauncher.addFolder(target, container, screen, targetCell[0], targetCell[1]);
        destInfo.cellX = -1;
        destInfo.cellY = -1;
        sourceInfo.cellX = -1;
        sourceInfo.cellY = -1;
        if (dragView != null) {
            fi.performCreateAnimation(destInfo, v, sourceInfo, dragView, folderLocation, scale, postAnimationRunnable);
        } else {
            fi.addItem(destInfo);
            fi.addItem(sourceInfo);
        }
        fi.requestFocus();
        return true;
    }

    /* access modifiers changed from: 0000 */
    public boolean addToExistingFolderIfNecessary(View newView, CellLayout target, int[] targetCell, DragObject d, boolean external) {
        View dropOverView = target.getChildAt(targetCell[0], targetCell[1]);
        if (dropOverView instanceof FolderIcon) {
            FolderIcon fi = (FolderIcon) dropOverView;
            if (fi.acceptDrop(d.dragInfo)) {
                fi.onDrop(d);
                if (!external) {
                    getParentCellLayoutForView(this.mDragInfo.cell).removeView(this.mDragInfo.cell);
                }
                fi.requestFocus();
                return true;
            }
        }
        return false;
    }

    public void onDrop(DragObject d) {
        int i;
        this.mDragViewVisualCenter = getDragViewVisualCenter(d.x, d.y, d.xOffset, d.yOffset, d.dragView, this.mDragViewVisualCenter);
        if (this.mDragTargetLayout != null) {
            if (this.mLauncher.isHotseatLayout(this.mDragTargetLayout)) {
                mapPointFromSelfToSibling(this.mLauncher.getHotseat(), this.mDragViewVisualCenter);
            } else {
                mapPointFromSelfToChild(this.mDragTargetLayout, this.mDragViewVisualCenter, null);
            }
        }
        CellLayout dropTargetLayout = this.mDragTargetLayout;
        int snapScreen = -1;
        if (d.dragSource != this) {
            onDropExternal(new int[]{(int) this.mDragViewVisualCenter[0], (int) this.mDragViewVisualCenter[1]}, d.dragInfo, dropTargetLayout, false, d);
        } else if (this.mDragInfo != null) {
            View cell = this.mDragInfo.cell;
            if (dropTargetLayout != null) {
                boolean hasMovedLayouts = getParentCellLayoutForView(cell) != dropTargetLayout;
                boolean hasMovedIntoHotseat = this.mLauncher.isHotseatLayout(dropTargetLayout);
                if (hasMovedIntoHotseat) {
                    i = CommonDesk.SETIS_END_COMPLETE;
                } else {
                    i = -100;
                }
                long container = (long) i;
                int screen = this.mTargetCell[0] < 0 ? this.mDragInfo.screen : indexOfChild(dropTargetLayout);
                this.mTargetCell = findNearestArea((int) this.mDragViewVisualCenter[0], (int) this.mDragViewVisualCenter[1], this.mDragInfo != null ? this.mDragInfo.spanX : 1, this.mDragInfo != null ? this.mDragInfo.spanY : 1, dropTargetLayout, this.mTargetCell);
                if (!this.mInScrollArea) {
                    if (createUserFolderIfNecessary(cell, container, dropTargetLayout, this.mTargetCell, false, d.dragView, null)) {
                        return;
                    }
                }
                if (!addToExistingFolderIfNecessary(cell, dropTargetLayout, this.mTargetCell, d, false)) {
                    this.mTargetCell = findNearestVacantArea((int) this.mDragViewVisualCenter[0], (int) this.mDragViewVisualCenter[1], this.mDragInfo.spanX, this.mDragInfo.spanY, cell, dropTargetLayout, this.mTargetCell);
                    if (this.mCurrentPage != screen && !hasMovedIntoHotseat) {
                        snapScreen = screen;
                        snapToPage(screen);
                    }
                    if (this.mTargetCell[0] >= 0 && this.mTargetCell[1] >= 0) {
                        if (hasMovedLayouts) {
                            getParentCellLayoutForView(cell).removeView(cell);
                            addInScreen(cell, container, screen, this.mTargetCell[0], this.mTargetCell[1], this.mDragInfo.spanX, this.mDragInfo.spanY);
                        }
                        final ItemInfo info = (ItemInfo) cell.getTag();
                        LayoutParams lp = (LayoutParams) cell.getLayoutParams();
                        dropTargetLayout.onMove(cell, this.mTargetCell[0], this.mTargetCell[1]);
                        lp.cellX = this.mTargetCell[0];
                        lp.cellY = this.mTargetCell[1];
                        cell.setId(LauncherModel.getCellLayoutChildId(container, this.mDragInfo.screen, this.mTargetCell[0], this.mTargetCell[1], this.mDragInfo.spanX, this.mDragInfo.spanY));
                        if (container != -101 && (cell instanceof LauncherAppWidgetHostView)) {
                            CellLayout cellLayout = dropTargetLayout;
                            LauncherAppWidgetHostView hostView = (LauncherAppWidgetHostView) cell;
                            AppWidgetProviderInfo pinfo = hostView.getAppWidgetInfo();
                            boolean hasMovedCells = (this.mTargetCell[0] == this.mDragInfo.cellX && this.mTargetCell[1] == this.mDragInfo.cellY) ? false : true;
                            if ((pinfo.resizeMode != 0 || this.mResizeAnyWidget) && !hasMovedCells && !hasMovedLayouts) {
                                final LauncherAppWidgetHostView launcherAppWidgetHostView = hostView;
                                final CellLayout cellLayout2 = cellLayout;
                                AnonymousClass9 r0 = new Runnable() {
                                    public void run() {
                                        Workspace.this.mLauncher.getDragLayer().addResizeFrame(info, launcherAppWidgetHostView, cellLayout2);
                                    }
                                };
                                final AnonymousClass9 r1 = r0;
                                post(new Runnable() {
                                    public void run() {
                                        if (!Workspace.this.isPageMoving()) {
                                            r1.run();
                                        } else {
                                            Workspace.this.mDelayedResizeRunnable = r1;
                                        }
                                    }
                                });
                            }
                        }
                        LauncherModel.moveItemInDatabase(this.mLauncher, info, container, screen, lp.cellX, lp.cellY);
                    }
                } else {
                    return;
                }
            }
            CellLayout parent = (CellLayout) cell.getParent().getParent();
            AnonymousClass11 r02 = new Runnable() {
                public void run() {
                    Workspace.this.mAnimatingViewIntoPlace = false;
                    Workspace.this.updateChildrenLayersEnabled(false);
                }
            };
            this.mAnimatingViewIntoPlace = true;
            if (d.dragView.hasDrawn()) {
                int duration = snapScreen < 0 ? -1 : 300;
                setFinalScrollForPageChange(snapScreen);
                this.mLauncher.getDragLayer().animateViewIntoPosition(d.dragView, cell, duration, (Runnable) r02);
                resetFinalScrollForPageChange(snapScreen);
            } else {
                cell.setVisibility(0);
            }
            parent.onDropChild(cell);
            if (!cell.isInTouchMode()) {
                cell.setVisibility(0);
                cell.requestFocus();
            }
        }
    }

    public void setFinalScrollForPageChange(int screen) {
        if (screen >= 0) {
            this.mSavedScrollX = getScrollX();
            CellLayout cl = (CellLayout) getPageAt(screen);
            this.mSavedTranslationX = cl.getTranslationX();
            this.mSavedRotationY = cl.getRotationY();
            setScrollX(getChildOffset(screen) - getRelativeChildOffset(screen));
            cl.setTranslationX(FlyingIcon.ANGULAR_VMIN);
            cl.setRotationY(FlyingIcon.ANGULAR_VMIN);
        }
    }

    public void resetFinalScrollForPageChange(int screen) {
        if (screen >= 0) {
            CellLayout cl = (CellLayout) getPageAt(screen);
            setScrollX(this.mSavedScrollX);
            cl.setTranslationX(this.mSavedTranslationX);
            cl.setRotationY(this.mSavedRotationY);
        }
    }

    public void getViewLocationRelativeToSelf(View v, int[] location) {
        getLocationInWindow(location);
        int x = location[0];
        int y = location[1];
        v.getLocationInWindow(location);
        int vX = location[0];
        int vY = location[1];
        location[0] = vX - x;
        location[1] = vY - y;
    }

    public void onDragEnter(DragObject d) {
        if (this.mDragTargetLayout != null) {
            this.mDragTargetLayout.setIsDragOverlapping(false);
            this.mDragTargetLayout.onDragExit();
        }
        this.mDragTargetLayout = getCurrentDropLayout();
        this.mDragTargetLayout.setIsDragOverlapping(true);
        this.mDragTargetLayout.onDragEnter();
        if (LauncherApplication.isScreenLarge()) {
            showOutlines();
        }
    }

    private void doDragExit(DragObject d) {
        cleanupFolderCreation(d);
        onResetScrollArea();
        if (this.mDragTargetLayout != null) {
            this.mDragTargetLayout.setIsDragOverlapping(false);
            this.mDragTargetLayout.onDragExit();
        }
        this.mLastDragOverView = null;
        this.mSpringLoadedDragController.cancel();
        if (!this.mIsPageMoving) {
            hideOutlines();
        }
    }

    public void onDragExit(DragObject d) {
        doDragExit(d);
    }

    public DropTarget getDropTargetDelegate(DragObject d) {
        return null;
    }

    private Pair<Integer, List<WidgetMimeTypeHandlerData>> validateDrag(DragEvent event) {
        LauncherModel model = this.mLauncher.getModel();
        ClipDescription desc = event.getClipDescription();
        int mimeTypeCount = desc.getMimeTypeCount();
        for (int i = 0; i < mimeTypeCount; i++) {
            String mimeType = desc.getMimeType(i);
            if (mimeType.equals(InstallShortcutReceiver.SHORTCUT_MIMETYPE)) {
                return new Pair<>(Integer.valueOf(i), null);
            }
            List<WidgetMimeTypeHandlerData> widgets = model.resolveWidgetsForMimeType(this.mContext, mimeType);
            if (widgets.size() > 0) {
                return new Pair<>(Integer.valueOf(i), widgets);
            }
        }
        return null;
    }

    public boolean onDragEvent(DragEvent event) {
        ClipDescription desc = event.getClipDescription();
        CellLayout layout = (CellLayout) getPageAt(this.mCurrentPage);
        int[] pos = new int[2];
        layout.getLocationOnScreen(pos);
        int x = ((int) event.getX()) - pos[0];
        int y = ((int) event.getY()) - pos[1];
        switch (event.getAction()) {
            case 1:
                Pair<Integer, List<WidgetMimeTypeHandlerData>> test = validateDrag(event);
                if (test != null) {
                    if (!(test.second == null) || layout.findCellForSpan(pos, 1, 1)) {
                        this.mDragOutline = createExternalDragOutline(new Canvas(), HolographicOutlineHelper.MAX_OUTER_BLUR_RADIUS);
                        showOutlines();
                        layout.onDragEnter();
                        layout.visualizeDropLocation(null, this.mDragOutline, x, y, 1, 1, null, null);
                        return true;
                    }
                    this.mLauncher.showOutOfSpaceMessage();
                    return false;
                }
                Toast.makeText(this.mContext, this.mContext.getString(R.string.external_drop_widget_error), 0).show();
                return false;
            case 2:
                layout.visualizeDropLocation(null, this.mDragOutline, x, y, 1, 1, null, null);
                return true;
            case 3:
                LauncherModel model = this.mLauncher.getModel();
                ClipData data = event.getClipData();
                pos[0] = x;
                pos[1] = y;
                Pair<Integer, List<WidgetMimeTypeHandlerData>> test2 = validateDrag(event);
                if (test2 != null) {
                    int index = ((Integer) test2.first).intValue();
                    List<WidgetMimeTypeHandlerData> widgets = (List) test2.second;
                    boolean isShortcut = widgets == null;
                    String mimeType = desc.getMimeType(index);
                    if (isShortcut) {
                        ShortcutInfo info = model.infoFromShortcutIntent(this.mContext, data.getItemAt(index).getIntent(), data.getIcon());
                        if (info != null) {
                            onDropExternal(new int[]{x, y}, info, layout, false);
                        }
                    } else if (widgets.size() == 1) {
                        this.mLauncher.addAppWidgetFromDrop(new PendingAddWidgetInfo(((WidgetMimeTypeHandlerData) widgets.get(0)).widgetInfo, mimeType, data), -100, this.mCurrentPage, null, pos);
                    } else {
                        WidgetListAdapter adapter = new WidgetListAdapter(this.mLauncher, mimeType, data, widgets, layout, this.mCurrentPage, pos);
                        Builder builder = new Builder(this.mContext);
                        builder.setAdapter(adapter, adapter);
                        builder.setCancelable(true);
                        builder.setTitle(this.mContext.getString(R.string.external_drop_widget_pick_title));
                        builder.setIcon(R.drawable.ic_no_applications);
                        builder.show();
                    }
                }
                return true;
            case 4:
                layout.onDragExit();
                hideOutlines();
                return true;
            default:
                return super.onDragEvent(event);
        }
    }

    /* access modifiers changed from: 0000 */
    public void mapPointFromSelfToChild(View v, float[] xy) {
        mapPointFromSelfToChild(v, xy, null);
    }

    /* access modifiers changed from: 0000 */
    public void mapPointFromSelfToChild(View v, float[] xy, Matrix cachedInverseMatrix) {
        if (cachedInverseMatrix == null) {
            v.getMatrix().invert(this.mTempInverseMatrix);
            cachedInverseMatrix = this.mTempInverseMatrix;
        }
        xy[0] = (xy[0] + ((float) this.mScrollX)) - ((float) v.getLeft());
        xy[1] = (xy[1] + ((float) this.mScrollY)) - ((float) v.getTop());
        cachedInverseMatrix.mapPoints(xy);
    }

    /* access modifiers changed from: 0000 */
    public void mapPointFromSelfToSibling(View v, float[] xy) {
        xy[0] = xy[0] - ((float) v.getLeft());
        xy[1] = xy[1] - ((float) v.getTop());
    }

    /* access modifiers changed from: 0000 */
    public void mapPointFromChildToSelf(View v, float[] xy) {
        v.getMatrix().mapPoints(xy);
        xy[0] = xy[0] - ((float) (this.mScrollX - v.getLeft()));
        xy[1] = xy[1] - ((float) (this.mScrollY - v.getTop()));
    }

    private static float squaredDistance(float[] point1, float[] point2) {
        float distanceX = point1[0] - point2[0];
        float distanceY = point2[1] - point2[1];
        return (distanceX * distanceX) + (distanceY * distanceY);
    }

    /* access modifiers changed from: 0000 */
    public boolean overlaps(CellLayout cl, DragView dragView, int dragViewX, int dragViewY, Matrix cachedInverseMatrix) {
        float[] draggedItemTopLeft = this.mTempDragCoordinates;
        draggedItemTopLeft[0] = (float) dragViewX;
        draggedItemTopLeft[1] = (float) dragViewY;
        float[] draggedItemBottomRight = this.mTempDragBottomRightCoordinates;
        draggedItemBottomRight[0] = draggedItemTopLeft[0] + ((float) dragView.getDragRegionWidth());
        draggedItemBottomRight[1] = draggedItemTopLeft[1] + ((float) dragView.getDragRegionHeight());
        mapPointFromSelfToChild(cl, draggedItemTopLeft, cachedInverseMatrix);
        float overlapRegionLeft = Math.max(FlyingIcon.ANGULAR_VMIN, draggedItemTopLeft[0]);
        float overlapRegionTop = Math.max(FlyingIcon.ANGULAR_VMIN, draggedItemTopLeft[1]);
        if (overlapRegionLeft <= ((float) cl.getWidth()) && overlapRegionTop >= FlyingIcon.ANGULAR_VMIN) {
            mapPointFromSelfToChild(cl, draggedItemBottomRight, cachedInverseMatrix);
            float overlapRegionRight = Math.min((float) cl.getWidth(), draggedItemBottomRight[0]);
            float overlapRegionBottom = Math.min((float) cl.getHeight(), draggedItemBottomRight[1]);
            if (overlapRegionRight >= FlyingIcon.ANGULAR_VMIN && overlapRegionBottom <= ((float) cl.getHeight()) && (overlapRegionRight - overlapRegionLeft) * (overlapRegionBottom - overlapRegionTop) > FlyingIcon.ANGULAR_VMIN) {
                return true;
            }
        }
        return false;
    }

    private CellLayout findMatchingPageForDragOver(DragView dragView, float originX, float originY, boolean exact) {
        int screenCount = getChildCount();
        CellLayout bestMatchingScreen = null;
        float smallestDistSoFar = Float.MAX_VALUE;
        for (int i = 0; i < screenCount; i++) {
            CellLayout cl = (CellLayout) getPageAt(i);
            float[] touchXy = {originX, originY};
            cl.getMatrix().invert(this.mTempInverseMatrix);
            mapPointFromSelfToChild(cl, touchXy, this.mTempInverseMatrix);
            if (touchXy[0] >= FlyingIcon.ANGULAR_VMIN && touchXy[0] <= ((float) cl.getWidth()) && touchXy[1] >= FlyingIcon.ANGULAR_VMIN && touchXy[1] <= ((float) cl.getHeight())) {
                return cl;
            }
            if (!exact) {
                float[] cellLayoutCenter = this.mTempCellLayoutCenterCoordinates;
                cellLayoutCenter[0] = (float) (cl.getWidth() / 2);
                cellLayoutCenter[1] = (float) (cl.getHeight() / 2);
                mapPointFromChildToSelf(cl, cellLayoutCenter);
                touchXy[0] = originX;
                touchXy[1] = originY;
                float dist = squaredDistance(touchXy, cellLayoutCenter);
                if (dist < smallestDistSoFar) {
                    smallestDistSoFar = dist;
                    bestMatchingScreen = cl;
                }
            }
        }
        return bestMatchingScreen;
    }

    private float[] getDragViewVisualCenter(int x, int y, int xOffset, int yOffset, DragView dragView, float[] recycle) {
        float[] res;
        if (recycle == null) {
            res = new float[2];
        } else {
            res = recycle;
        }
        int top = (y + getResources().getDimensionPixelSize(R.dimen.dragViewOffsetY)) - yOffset;
        res[0] = (float) ((dragView.getDragRegion().width() / 2) + ((x + getResources().getDimensionPixelSize(R.dimen.dragViewOffsetX)) - xOffset));
        res[1] = (float) ((dragView.getDragRegion().height() / 2) + top);
        return res;
    }

    private boolean isDragWidget(DragObject d) {
        return (d.dragInfo instanceof LauncherAppWidgetInfo) || (d.dragInfo instanceof PendingAddWidgetInfo);
    }

    private boolean isExternalDragWidget(DragObject d) {
        return d.dragSource != this && isDragWidget(d);
    }

    public void onDragOver(DragObject d) {
        if (!this.mInScrollArea && !this.mIsSwitchingState) {
            Rect r = new Rect();
            CellLayout layout = null;
            ItemInfo item = (ItemInfo) d.dragInfo;
            if (item.spanX < 0 || item.spanY < 0) {
                throw new RuntimeException("Improper spans found");
            }
            this.mDragViewVisualCenter = getDragViewVisualCenter(d.x, d.y, d.xOffset, d.yOffset, d.dragView, this.mDragViewVisualCenter);
            if (isSmall()) {
                if (this.mLauncher.getHotseat() != null && !isExternalDragWidget(d)) {
                    this.mLauncher.getHotseat().getHitRect(r);
                    if (r.contains(d.x, d.y)) {
                        layout = this.mLauncher.getHotseat().getLayout();
                    }
                }
                if (layout == null) {
                    layout = findMatchingPageForDragOver(d.dragView, (float) d.x, (float) d.y, false);
                }
                if (layout != this.mDragTargetLayout) {
                    cleanupFolderCreation(d);
                    if (this.mDragTargetLayout != null) {
                        this.mDragTargetLayout.setIsDragOverlapping(false);
                        this.mDragTargetLayout.onDragExit();
                    }
                    this.mDragTargetLayout = layout;
                    if (this.mDragTargetLayout != null) {
                        this.mDragTargetLayout.setIsDragOverlapping(true);
                        this.mDragTargetLayout.onDragEnter();
                    } else {
                        this.mLastDragOverView = null;
                    }
                    if (this.mState == State.SPRING_LOADED) {
                        if (this.mLauncher.isHotseatLayout(layout)) {
                            this.mSpringLoadedDragController.cancel();
                        } else {
                            this.mSpringLoadedDragController.setAlarm(this.mDragTargetLayout);
                        }
                    }
                }
            } else {
                if (this.mLauncher.getHotseat() != null && !isDragWidget(d)) {
                    this.mLauncher.getHotseat().getHitRect(r);
                    if (r.contains(d.x, d.y)) {
                        layout = this.mLauncher.getHotseat().getLayout();
                    }
                }
                if (layout == null) {
                    layout = getCurrentDropLayout();
                }
                if (layout != this.mDragTargetLayout) {
                    if (this.mDragTargetLayout != null) {
                        this.mDragTargetLayout.setIsDragOverlapping(false);
                        this.mDragTargetLayout.onDragExit();
                    }
                    this.mDragTargetLayout = layout;
                    this.mDragTargetLayout.setIsDragOverlapping(true);
                    this.mDragTargetLayout.onDragEnter();
                }
            }
            if (this.mDragTargetLayout != null) {
                View child = this.mDragInfo == null ? null : this.mDragInfo.cell;
                if (this.mLauncher.isHotseatLayout(this.mDragTargetLayout)) {
                    mapPointFromSelfToSibling(this.mLauncher.getHotseat(), this.mDragViewVisualCenter);
                } else {
                    mapPointFromSelfToChild(this.mDragTargetLayout, this.mDragViewVisualCenter, null);
                }
                ItemInfo info = (ItemInfo) d.dragInfo;
                this.mTargetCell = findNearestArea((int) this.mDragViewVisualCenter[0], (int) this.mDragViewVisualCenter[1], 1, 1, this.mDragTargetLayout, this.mTargetCell);
                View dragOverView = this.mDragTargetLayout.getChildAt(this.mTargetCell[0], this.mTargetCell[1]);
                boolean userFolderPending = willCreateUserFolder(info, this.mDragTargetLayout, this.mTargetCell, false);
                boolean isOverFolder = dragOverView instanceof FolderIcon;
                if (dragOverView != this.mLastDragOverView) {
                    cancelFolderCreation();
                    if (this.mLastDragOverView != null && (this.mLastDragOverView instanceof FolderIcon)) {
                        ((FolderIcon) this.mLastDragOverView).onDragExit(d.dragInfo);
                    }
                }
                if (userFolderPending && dragOverView != this.mLastDragOverView) {
                    this.mFolderCreationAlarm.setOnAlarmListener(new FolderCreationAlarmListener(this.mDragTargetLayout, this.mTargetCell[0], this.mTargetCell[1]));
                    this.mFolderCreationAlarm.setAlarm(250);
                }
                if (dragOverView != this.mLastDragOverView && isOverFolder) {
                    ((FolderIcon) dragOverView).onDragEnter(d.dragInfo);
                    if (this.mDragTargetLayout != null) {
                        this.mDragTargetLayout.clearDragOutlines();
                    }
                }
                this.mLastDragOverView = dragOverView;
                if (!this.mCreateUserFolderOnDrop && !isOverFolder) {
                    this.mDragTargetLayout.visualizeDropLocation(child, this.mDragOutline, (int) this.mDragViewVisualCenter[0], (int) this.mDragViewVisualCenter[1], item.spanX, item.spanY, d.dragView.getDragVisualizeOffset(), d.dragView.getDragRegion());
                }
            }
        }
    }

    private void cleanupFolderCreation(DragObject d) {
        if (this.mDragFolderRingAnimator != null && this.mCreateUserFolderOnDrop) {
            this.mDragFolderRingAnimator.animateToNaturalState();
        }
        if (!(this.mLastDragOverView == null || !(this.mLastDragOverView instanceof FolderIcon) || d == null)) {
            ((FolderIcon) this.mLastDragOverView).onDragExit(d.dragInfo);
        }
        this.mFolderCreationAlarm.cancelAlarm();
    }

    private void cancelFolderCreation() {
        if (this.mDragFolderRingAnimator != null && this.mCreateUserFolderOnDrop) {
            this.mDragFolderRingAnimator.animateToNaturalState();
        }
        this.mCreateUserFolderOnDrop = false;
        this.mFolderCreationAlarm.cancelAlarm();
    }

    public void getHitRect(Rect outRect) {
        outRect.set(0, 0, this.mDisplayWidth, this.mDisplayHeight);
    }

    private void addExternalItemInner(ItemInfo info, CellLayout cellLayout, boolean willSnapToPageWhenDone) {
        int i;
        View view;
        if (this.mLauncher.isHotseatLayout(cellLayout)) {
            i = CommonDesk.SETIS_END_COMPLETE;
        } else {
            i = -100;
        }
        long container = (long) i;
        int screen = indexOfChild(cellLayout);
        switch (info.itemType) {
            case 0:
            case 1:
                if (info.container == -1 && (info instanceof ApplicationInfo)) {
                    ItemInfo info2 = new ShortcutInfo((ApplicationInfo) info);
                    AppInfoManager.getInstance().setIOS7Icon((ShortcutInfo) info2, this.mIconCache);
                    info = info2;
                }
                view = this.mLauncher.createShortcut(R.layout.application, cellLayout, (ShortcutInfo) info);
                break;
            case 2:
                view = FolderIcon.fromXml(R.layout.folder_icon, this.mLauncher, cellLayout, (FolderInfo) info, this.mIconCache);
                if (this.mHideIconLabels) {
                    ((FolderIcon) view).setTextVisible(false);
                    break;
                }
                break;
            default:
                throw new IllegalStateException("Unknown item type: " + info.itemType);
        }
        addInScreen(view, container, screen, this.mTargetCell[0], this.mTargetCell[1], info.spanX, info.spanY, false);
        cellLayout.onDropChild(view);
        LayoutParams lp = (LayoutParams) view.getLayoutParams();
        cellLayout.getChildrenLayout().measureChild(view);
        LauncherModel.addOrMoveItemInDatabase(this.mLauncher, info, container, screen, lp.cellX, lp.cellY);
        if (willSnapToPageWhenDone) {
            snapToPage(screen);
        }
    }

    public boolean addExternalItem(ItemInfo item, CellLayout cellLayout, boolean willSnapToPageWhenDone) {
        if (!cellLayout.findCellForSpan(this.mTargetCell, item.spanX, item.spanY)) {
            return false;
        }
        addExternalItemInner(item, cellLayout, willSnapToPageWhenDone);
        return true;
    }

    public void addExternalItem(ItemInfo item, boolean willSnapToPageWhenDone) {
        boolean added = false;
        int i = 0;
        while (!added && i < this.mNumberHomescreens) {
            CellLayout cellLayout = (CellLayout) getChildAt(i);
            if (cellLayout.findCellForSpan(this.mTargetCell, item.spanX, item.spanY)) {
                addExternalItemInner(item, cellLayout, willSnapToPageWhenDone);
                added = true;
            }
            i++;
        }
        if (!added) {
            CellLayout newScreen = getCellLayoutFromXml();
            addView(newScreen);
            newScreen.findCellForSpan(this.mTargetCell, item.spanX, item.spanY);
            addExternalItemInner(item, newScreen, willSnapToPageWhenDone);
        }
    }

    public void addExternalItemsStartingFromPage(ArrayList<? extends ItemInfo> items, int startPage, boolean willSnapToPageWhenDone) {
        int firstItemScreen = -1;
        int current = Math.min(startPage, this.mNumberHomescreens - 1);
        do {
            CellLayout cellLayout = (CellLayout) getChildAt(current);
            while (!items.isEmpty()) {
                ItemInfo info = (ItemInfo) items.get(0);
                if (!cellLayout.findCellForSpan(this.mTargetCell, info.spanX, info.spanY)) {
                    break;
                }
                addExternalItemInner(info, cellLayout, willSnapToPageWhenDone);
                items.remove(0);
                if (firstItemScreen < 0) {
                    firstItemScreen = current;
                }
            }
            if (items.isEmpty()) {
                break;
            }
            current++;
            if (current >= this.mNumberHomescreens) {
                current = 0;
                continue;
            }
        } while (current != startPage);
        if (!items.isEmpty()) {
            if (firstItemScreen < 0) {
                firstItemScreen = this.mNumberHomescreens;
            }
            int N = LauncherModel.getCellCountX() * LauncherModel.getCellCountY();
            int newScreenCount = ((items.size() + N) - 1) / N;
            for (int i = 0; i < newScreenCount; i++) {
                CellLayout newScreen = getCellLayoutFromXml();
                addView(newScreen);
                for (int j = Math.min(items.size(), N); j > 0; j--) {
                    ItemInfo info2 = (ItemInfo) items.get(0);
                    newScreen.findCellForSpan(this.mTargetCell, info2.spanX, info2.spanY);
                    addExternalItemInner(info2, newScreen, false);
                    items.remove(0);
                }
            }
        }
        if (willSnapToPageWhenDone && firstItemScreen > 0) {
            snapToPage(firstItemScreen);
        }
    }

    public void addExternalItems(ArrayList<? extends ItemInfo> items, boolean willSnapToPageWhenDone) {
        addExternalItemsStartingFromPage(items, 0, willSnapToPageWhenDone);
    }

    public boolean isAllowAddItem(CellLayout layout) {
        return !isDefaultHomeScreen(layout) || this.mHomescreenAllowAdd;
    }

    public boolean addExternalItemToScreen(ItemInfo dragInfo, CellLayout layout) {
        if (!isAllowAddItem(layout)) {
            this.mLauncher.showNotAllowedAddMessage();
            return false;
        } else if (layout.findCellForSpan(this.mTempEstimate, dragInfo.spanX, dragInfo.spanY)) {
            onDropExternal(dragInfo.dropPos, dragInfo, layout, false);
            return true;
        } else {
            this.mLauncher.showOutOfSpaceMessage();
            return false;
        }
    }

    private void onDropExternal(int[] touchXY, Object dragInfo, CellLayout cellLayout, boolean insertAtFirst) {
        onDropExternal(touchXY, dragInfo, cellLayout, insertAtFirst, null);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:26:0x00b3, code lost:
        if (willAddToExistingUserFolder(r40.dragInfo, r35.mDragTargetLayout, r35.mTargetCell) != false) goto L_0x00b5;
     */
    private void onDropExternal(int[] touchXY, Object dragInfo, CellLayout cellLayout, boolean insertAtFirst, DragObject d) {
        int i;
        View view;
        ItemInfo info;
        AnonymousClass12 r0 = new Runnable() {
            public void run() {
                Workspace.this.mLauncher.exitSpringLoadedDragModeDelayed(true, false);
            }
        };
        ItemInfo info2 = (ItemInfo) dragInfo;
        int spanX = info2.spanX;
        int spanY = info2.spanY;
        if (this.mDragInfo != null) {
            spanX = this.mDragInfo.spanX;
            spanY = this.mDragInfo.spanY;
        }
        if (this.mLauncher.isHotseatLayout(cellLayout)) {
            i = CommonDesk.SETIS_END_COMPLETE;
        } else {
            i = -100;
        }
        final long container = (long) i;
        final int screen = indexOfChild(cellLayout);
        if (!(this.mLauncher.isHotseatLayout(cellLayout) || screen == this.mCurrentPage || this.mState == State.SPRING_LOADED)) {
            snapToPage(screen);
        }
        if (info2 instanceof PendingAddItemInfo) {
            PendingAddItemInfo pendingInfo = (PendingAddItemInfo) dragInfo;
            boolean findNearestVacantCell = touchXY != null;
            if (findNearestVacantCell) {
                if (pendingInfo.itemType == 1) {
                    this.mTargetCell = findNearestArea(touchXY[0], touchXY[1], spanX, spanY, cellLayout, this.mTargetCell);
                    if (d != null) {
                        if (!willCreateUserFolder((ItemInfo) d.dragInfo, this.mDragTargetLayout, this.mTargetCell, true)) {
                        }
                        findNearestVacantCell = false;
                    }
                }
                if (findNearestVacantCell) {
                    this.mTargetCell = findNearestVacantArea(touchXY[0], touchXY[1], spanX, spanY, null, cellLayout, this.mTargetCell);
                }
            } else {
                cellLayout.findCellForSpan(this.mTargetCell, spanX, spanY);
            }
            final PendingAddItemInfo pendingAddItemInfo = pendingInfo;
            final CellLayout cellLayout2 = cellLayout;
            Runnable onAnimationCompleteRunnable = new Runnable() {
                public void run() {
                    switch (pendingAddItemInfo.itemType) {
                        case 1:
                            Workspace.this.mLauncher.processShortcutFromDrop(pendingAddItemInfo.componentName, container, screen, Workspace.this.mTargetCell, null);
                            break;
                        case 4:
                            Workspace.this.mLauncher.addAppWidgetFromDrop((PendingAddWidgetInfo) pendingAddItemInfo, container, screen, Workspace.this.mTargetCell, null);
                            break;
                        default:
                            throw new IllegalStateException("Unknown item type: " + pendingAddItemInfo.itemType);
                    }
                    cellLayout2.onDragExit();
                }
            };
            if (d != null) {
                RectF r = estimateItemPosition(cellLayout, pendingInfo, this.mTargetCell[0], this.mTargetCell[1], spanX, spanY);
                int[] loc = {(int) r.left, (int) r.top};
                setFinalTransitionTransform(cellLayout);
                float cellLayoutScale = this.mLauncher.getDragLayer().getDescendantCoordRelativeToSelf(cellLayout, loc);
                resetTransitionTransform(cellLayout);
                float dragViewScale = Math.min(r.width() / ((float) d.dragView.getMeasuredWidth()), r.height() / ((float) d.dragView.getMeasuredHeight()));
                loc[0] = (int) (((float) loc[0]) - ((((float) d.dragView.getMeasuredWidth()) - (r.width() * cellLayoutScale)) / WALLPAPER_SCREENS_SPAN));
                loc[1] = (int) (((float) loc[1]) - ((((float) d.dragView.getMeasuredHeight()) - (r.height() * cellLayoutScale)) / WALLPAPER_SCREENS_SPAN));
                this.mLauncher.getDragLayer().animateViewIntoPosition(d.dragView, loc, dragViewScale * cellLayoutScale, onAnimationCompleteRunnable);
                ItemInfo itemInfo = info2;
                return;
            }
            Log.d(TAG, "directly call onAnimationCompleteRunnable");
            cellLayout.post(onAnimationCompleteRunnable);
            ItemInfo itemInfo2 = info2;
            return;
        }
        switch (info2.itemType) {
            case 0:
            case 1:
                if (info2.container != -1 || !(info2 instanceof ApplicationInfo)) {
                    info = info2;
                } else {
                    info = new ShortcutInfo((ApplicationInfo) info2);
                }
                view = this.mLauncher.createShortcut(R.layout.application, cellLayout, (ShortcutInfo) info);
                break;
            case 2:
                view = FolderIcon.fromXml(R.layout.folder_icon, this.mLauncher, cellLayout, (FolderInfo) info2, this.mIconCache);
                if (!this.mHideIconLabels) {
                    info = info2;
                    break;
                } else {
                    ((FolderIcon) view).setTextVisible(false);
                    info = info2;
                    break;
                }
            default:
                throw new IllegalStateException("Unknown item type: " + info2.itemType);
        }
        if (touchXY != null) {
            this.mTargetCell = findNearestArea(touchXY[0], touchXY[1], spanX, spanY, cellLayout, this.mTargetCell);
            d.postAnimationRunnable = r0;
            if (!createUserFolderIfNecessary(view, container, cellLayout, this.mTargetCell, true, d.dragView, d.postAnimationRunnable)) {
                if (addToExistingFolderIfNecessary(view, cellLayout, this.mTargetCell, d, true)) {
                    return;
                }
            } else {
                return;
            }
        }
        if (touchXY != null) {
            this.mTargetCell = findNearestVacantArea(touchXY[0], touchXY[1], 1, 1, null, cellLayout, this.mTargetCell);
        } else {
            cellLayout.findCellForSpan(this.mTargetCell, 1, 1);
        }
        addInScreen(view, container, screen, this.mTargetCell[0], this.mTargetCell[1], info.spanX, info.spanY, insertAtFirst);
        cellLayout.onDropChild(view);
        LayoutParams lp = (LayoutParams) view.getLayoutParams();
        cellLayout.getChildrenLayout().measureChild(view);
        LauncherModel.addOrMoveItemInDatabase(this.mLauncher, info, container, screen, lp.cellX, lp.cellY);
        if (d != null && d.dragView != null) {
            setFinalTransitionTransform(cellLayout);
            this.mLauncher.getDragLayer().animateViewIntoPosition(d.dragView, view, r0);
            resetTransitionTransform(cellLayout);
        }
    }

    public void setFinalTransitionTransform(CellLayout layout) {
        if (isSwitchingState()) {
            int index = indexOfChild(layout);
            this.mCurrentScaleX = layout.getScaleX();
            this.mCurrentScaleY = layout.getScaleY();
            this.mCurrentTranslationX = layout.getTranslationX();
            this.mCurrentTranslationY = layout.getTranslationY();
            this.mCurrentRotationY = layout.getRotationY();
            layout.setScaleX(this.mNewScaleXs[index]);
            layout.setScaleY(this.mNewScaleYs[index]);
            layout.setTranslationX(this.mNewTranslationXs[index]);
            layout.setTranslationY(this.mNewTranslationYs[index]);
            layout.setRotationY(this.mNewRotationYs[index]);
        }
    }

    public void resetTransitionTransform(CellLayout layout) {
        if (isSwitchingState()) {
            this.mCurrentScaleX = layout.getScaleX();
            this.mCurrentScaleY = layout.getScaleY();
            this.mCurrentTranslationX = layout.getTranslationX();
            this.mCurrentTranslationY = layout.getTranslationY();
            this.mCurrentRotationY = layout.getRotationY();
            layout.setScaleX(this.mCurrentScaleX);
            layout.setScaleY(this.mCurrentScaleY);
            layout.setTranslationX(this.mCurrentTranslationX);
            layout.setTranslationY(this.mCurrentTranslationY);
            layout.setRotationY(this.mCurrentRotationY);
        }
    }

    public CellLayout getCurrentDropLayout() {
        return (CellLayout) getPageAt(this.mNextPage == -1 ? this.mCurrentPage : this.mNextPage);
    }

    public CellLayout getCurrentPageLayout() {
        return (CellLayout) getPageAt(this.mCurrentPage);
    }

    public CellInfo getDragInfo() {
        return this.mDragInfo;
    }

    private int[] findNearestVacantArea(int pixelX, int pixelY, int spanX, int spanY, View ignoreView, CellLayout layout, int[] recycle) {
        return layout.findNearestVacantArea(pixelX, pixelY, spanX, spanY, ignoreView, recycle);
    }

    private int[] findNearestArea(int pixelX, int pixelY, int spanX, int spanY, CellLayout layout, int[] recycle) {
        return layout.findNearestArea(pixelX, pixelY, spanX, spanY, recycle);
    }

    /* access modifiers changed from: 0000 */
    public void setup(DragController dragController) {
        this.mSpringLoadedDragController = new SpringLoadedDragController(this.mLauncher);
        this.mDragController = dragController;
        updateChildrenLayersEnabled(false);
        if (!this.mScrollWallpaper) {
            centerWallpaperOffset();
        }
    }

    /* access modifiers changed from: private */
    public void removeEmptyHomescreens() {
        int lastScreenIndex = getChildCount() - 1;
        for (int index = lastScreenIndex; index >= 0; index--) {
            View child = getChildAt(index);
            if (!(child instanceof CellLayout)) {
                Log.d(TAG, "WTF: strange thing in workspace - " + child.getClass().getSimpleName());
            } else if (((CellLayout) child).getChildrenLayout().getChildCount() <= 0) {
                removeViewAt(index);
                if (index != lastScreenIndex) {
                    LauncherModel.updateItemsInDatabaseAfterScreen(this.mLauncher, index);
                }
                lastScreenIndex--;
            }
        }
    }

    public void onDropCompleted(View target, DragObject d, boolean success) {
        CellLayout cellLayout;
        if (success) {
            if (!(target == this || this.mDragInfo == null)) {
                getParentCellLayoutForView(this.mDragInfo.cell).removeView(this.mDragInfo.cell);
                if (this.mDragInfo.cell instanceof DropTarget) {
                    this.mDragController.removeDropTarget((DropTarget) this.mDragInfo.cell);
                }
            }
            removeEmptyHomescreens();
        } else if (this.mDragInfo != null) {
            doDragExit(null);
            if (this.mLauncher.isHotseatLayout(target)) {
                cellLayout = this.mLauncher.getHotseat().getLayout();
            } else {
                cellLayout = (CellLayout) getPageAt(this.mDragInfo.screen);
            }
            if (cellLayout != null) {
                cellLayout.onDropChild(this.mDragInfo.cell);
            }
        }
        if (d.cancelled && this.mDragInfo.cell != null) {
            this.mDragInfo.cell.setVisibility(0);
            this.mDragInfo.cell.requestFocus();
        }
        this.mDragOutline = null;
        this.mDragInfo = null;
    }

    public boolean isDropEnabled() {
        return true;
    }

    /* access modifiers changed from: protected */
    public void onRestoreInstanceState(Parcelable state) {
        super.onRestoreInstanceState(state);
        Launcher.setScreen(this.mCurrentPage);
    }

    public void scrollLeft() {
        if (!isSmall() && !this.mIsSwitchingState) {
            super.scrollLeft();
        }
        Folder openFolder = getOpenFolder();
        if (openFolder != null) {
            openFolder.completeDragExit();
        }
    }

    public void scrollRight() {
        if (!isSmall() && !this.mIsSwitchingState) {
            super.scrollRight();
        }
        Folder openFolder = getOpenFolder();
        if (openFolder != null) {
            openFolder.completeDragExit();
        }
    }

    public boolean onEnterScrollArea(int x, int y, int direction) {
        int i;
        if (this.mLauncher.getHotseat() != null) {
            Rect r = new Rect();
            this.mLauncher.getHotseat().getHitRect(r);
            if (r.contains(x, y)) {
                return false;
            }
        }
        if (isSmall() || this.mIsSwitchingState) {
            return false;
        }
        this.mInScrollArea = true;
        int i2 = this.mCurrentPage;
        if (direction == 0) {
            i = -1;
        } else {
            i = 1;
        }
        CellLayout layout = (CellLayout) getPageAt(i2 + i);
        cancelFolderCreation();
        if (layout == null) {
            return false;
        }
        if (this.mDragTargetLayout != null) {
            this.mDragTargetLayout.setIsDragOverlapping(false);
            this.mDragTargetLayout.onDragExit();
        }
        this.mDragTargetLayout = layout;
        this.mDragTargetLayout.setIsDragOverlapping(true);
        invalidate();
        return true;
    }

    public boolean onExitScrollArea() {
        boolean result = false;
        if (this.mInScrollArea) {
            if (this.mDragTargetLayout != null) {
                this.mDragTargetLayout.setIsDragOverlapping(false);
                this.mDragTargetLayout = getCurrentDropLayout();
                this.mDragTargetLayout.onDragEnter();
                invalidate();
                result = true;
            }
            this.mInScrollArea = false;
        }
        return result;
    }

    private void onResetScrollArea() {
        if (this.mDragTargetLayout != null) {
            this.mDragTargetLayout.setIsDragOverlapping(false);
            invalidate();
        }
        this.mInScrollArea = false;
    }

    /* access modifiers changed from: 0000 */
    public CellLayout getParentCellLayoutForView(View v) {
        Iterator it = getWorkspaceAndHotseatCellLayouts().iterator();
        while (it.hasNext()) {
            CellLayout layout = (CellLayout) it.next();
            if (layout.getChildrenLayout().indexOfChild(v) > -1) {
                return layout;
            }
        }
        return null;
    }

    /* access modifiers changed from: 0000 */
    public ArrayList<CellLayout> getWorkspaceAndHotseatCellLayouts() {
        ArrayList<CellLayout> layouts = new ArrayList<>();
        int screenCount = getChildCount();
        for (int screen = 0; screen < screenCount; screen++) {
            layouts.add((CellLayout) getPageAt(screen));
        }
        if (this.mLauncher.getHotseat() != null) {
            layouts.add(this.mLauncher.getHotseat().getLayout());
        }
        return layouts;
    }

    /* access modifiers changed from: 0000 */
    public ArrayList<CellLayoutChildren> getWorkspaceAndHotseatCellLayoutChildren() {
        ArrayList<CellLayoutChildren> childrenLayouts = new ArrayList<>();
        int screenCount = getChildCount();
        for (int screen = 0; screen < screenCount; screen++) {
            childrenLayouts.add(((CellLayout) getPageAt(screen)).getChildrenLayout());
        }
        if (this.mLauncher.getHotseat() != null) {
            childrenLayouts.add(this.mLauncher.getHotseat().getLayout().getChildrenLayout());
        }
        return childrenLayouts;
    }

    public Folder getFolderForTag(Object tag) {
        Iterator it = getWorkspaceAndHotseatCellLayoutChildren().iterator();
        while (it.hasNext()) {
            CellLayoutChildren layout = (CellLayoutChildren) it.next();
            int count = layout.getChildCount();
            int i = 0;
            while (true) {
                if (i < count) {
                    View child = layout.getChildAt(i);
                    if (child instanceof Folder) {
                        Folder f = (Folder) child;
                        if (f.getInfo() == tag && f.getInfo().opened) {
                            return f;
                        }
                    }
                    i++;
                }
            }
        }
        return null;
    }

    public View getViewForTag(Object tag) {
        Iterator it = getWorkspaceAndHotseatCellLayoutChildren().iterator();
        while (it.hasNext()) {
            CellLayoutChildren layout = (CellLayoutChildren) it.next();
            int count = layout.getChildCount();
            int i = 0;
            while (true) {
                if (i < count) {
                    View child = layout.getChildAt(i);
                    if (child.getTag() == tag) {
                        return child;
                    }
                    i++;
                }
            }
        }
        return null;
    }

    /* access modifiers changed from: 0000 */
    public void clearDropTargets() {
        Iterator it = getWorkspaceAndHotseatCellLayoutChildren().iterator();
        while (it.hasNext()) {
            CellLayoutChildren layout = (CellLayoutChildren) it.next();
            int childCount = layout.getChildCount();
            for (int j = 0; j < childCount; j++) {
                View v = layout.getChildAt(j);
                if (v instanceof DropTarget) {
                    this.mDragController.removeDropTarget((DropTarget) v);
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public boolean isNeedToRemoveFinally(ComponentName component) {
        if (component == null) {
            return false;
        }
        boolean getApp = false;
        try {
            this.mLauncher.getPackageManager().getApplicationInfo(component.getPackageName(), 0);
            getApp = true;
            Log.d("czj-ios", "package [" + component.getPackageName() + " ] exist now");
        } catch (NameNotFoundException e) {
        }
        if (getApp) {
            LauncherModel model = this.mLauncher.getModel();
            if (model != null) {
                AllAppsList applist = model.getAllAppsList();
                if (applist != null && !applist.findBlackedPackage(component)) {
                    Log.d("czj-ios", "package [" + component.getPackageName() + " ] do not need to remove finally");
                    return false;
                }
            }
        }
        return true;
    }

    /* access modifiers changed from: 0000 */
    public void removeItems(ArrayList<? extends ItemInfo> apps) {
        ComponentName componentName;
        final AppWidgetManager widgets = AppWidgetManager.getInstance(getContext());
        final ArrayList<ComponentName> componentNames = new ArrayList<>();
        int appCount = apps.size();
        if (appCount > 0) {
            int itemType = -1;
            for (int i = 0; i < appCount; i++) {
                if (itemType < 0) {
                    itemType = apps.get(i) instanceof ApplicationInfo ? 0 : 1;
                }
                if (itemType == 0) {
                    componentName = ((ApplicationInfo) apps.get(i)).componentName;
                } else {
                    componentName = ((ShortcutInfo) apps.get(i)).intent.getComponent();
                }
                componentNames.add(componentName);
            }
            Iterator it = getWorkspaceAndHotseatCellLayouts().iterator();
            while (it.hasNext()) {
                final CellLayout layoutParent = (CellLayout) it.next();
                final ViewGroup layout = layoutParent.getChildrenLayout();
                post(new Runnable() {
                    public void run() {
                        ArrayList<View> childrenToRemove = new ArrayList<>();
                        childrenToRemove.clear();
                        for (int i = componentNames.size() - 1; i >= 0; i--) {
                            if (!Workspace.this.isNeedToRemoveFinally((ComponentName) componentNames.get(i))) {
                                componentNames.remove(i);
                            }
                        }
                        int childCount = layout.getChildCount();
                        for (int j = 0; j < childCount; j++) {
                            View view = layout.getChildAt(j);
                            Object tag = view.getTag();
                            if (tag instanceof ShortcutInfo) {
                                ShortcutInfo info = (ShortcutInfo) tag;
                                Intent intent = info.intent;
                                ComponentName name = intent.getComponent();
                                if ("android.intent.action.MAIN".equals(intent.getAction()) && name != null) {
                                    Iterator it = componentNames.iterator();
                                    while (it.hasNext()) {
                                        ComponentName removeComponent = (ComponentName) it.next();
                                        if (removeComponent.getPackageName().equals(name.getPackageName()) && removeComponent.getClassName().equals(name.getClassName())) {
                                            LauncherModel.deleteItemFromDatabase(Workspace.this.mLauncher, info);
                                            childrenToRemove.add(view);
                                        }
                                    }
                                }
                            } else if (tag instanceof FolderInfo) {
                                FolderInfo info2 = (FolderInfo) tag;
                                ArrayList<ShortcutInfo> contents = info2.contents;
                                int size = contents.size();
                                ArrayList<ShortcutInfo> appsToRemoveFromFolder = new ArrayList<>();
                                Iterator it2 = contents.iterator();
                                while (it2.hasNext()) {
                                    ShortcutInfo appInfo = (ShortcutInfo) it2.next();
                                    Intent intent2 = appInfo.intent;
                                    ComponentName name2 = intent2.getComponent();
                                    if ("android.intent.action.MAIN".equals(intent2.getAction()) && name2 != null) {
                                        Iterator it3 = componentNames.iterator();
                                        while (it3.hasNext()) {
                                            ComponentName removeComponent2 = (ComponentName) it3.next();
                                            if (removeComponent2.getPackageName().equals(name2.getPackageName()) && removeComponent2.getClassName().equals(name2.getClassName())) {
                                                appsToRemoveFromFolder.add(appInfo);
                                            }
                                        }
                                    }
                                }
                                Iterator it4 = appsToRemoveFromFolder.iterator();
                                while (it4.hasNext()) {
                                    ShortcutInfo item = (ShortcutInfo) it4.next();
                                    info2.remove(item);
                                    LauncherModel.deleteItemFromDatabase(Workspace.this.mLauncher, item);
                                }
                                if (contents.isEmpty()) {
                                    Workspace.this.mLauncher.removeFolder(info2);
                                    LauncherModel.deleteFolderContentsFromDatabase(Workspace.this.mLauncher, info2);
                                    childrenToRemove.add(view);
                                }
                            } else if (tag instanceof LauncherAppWidgetInfo) {
                                LauncherAppWidgetInfo info3 = (LauncherAppWidgetInfo) tag;
                                AppWidgetProviderInfo provider = widgets.getAppWidgetInfo(info3.appWidgetId);
                                if (provider != null) {
                                    Iterator it5 = componentNames.iterator();
                                    while (it5.hasNext()) {
                                        ComponentName removeComponent3 = (ComponentName) it5.next();
                                        if (removeComponent3.getPackageName().equals(provider.provider.getPackageName()) && removeComponent3.getClassName().equals(provider.provider.getClassName())) {
                                            LauncherModel.deleteItemFromDatabase(Workspace.this.mLauncher, info3);
                                            childrenToRemove.add(view);
                                        }
                                    }
                                }
                            }
                        }
                        int childCount2 = childrenToRemove.size();
                        for (int j2 = 0; j2 < childCount2; j2++) {
                            View child = (View) childrenToRemove.get(j2);
                            layoutParent.removeViewInLayout(child);
                            if (child instanceof DropTarget) {
                                Workspace.this.mDragController.removeDropTarget((DropTarget) child);
                            }
                        }
                        if (childCount2 > 0) {
                            layout.requestLayout();
                            layout.invalidate();
                        }
                    }
                });
            }
            post(new Runnable() {
                public void run() {
                    Workspace.this.removeEmptyHomescreens();
                }
            });
        }
    }

    /* access modifiers changed from: 0000 */
    public void updateShortcuts(ArrayList<ApplicationInfo> apps) {
        Iterator it = getWorkspaceAndHotseatCellLayoutChildren().iterator();
        while (it.hasNext()) {
            CellLayoutChildren layout = (CellLayoutChildren) it.next();
            int childCount = layout.getChildCount();
            for (int j = 0; j < childCount; j++) {
                View view = layout.getChildAt(j);
                Object tag = view.getTag();
                if (tag instanceof ShortcutInfo) {
                    ShortcutInfo info = (ShortcutInfo) tag;
                    Intent intent = info.intent;
                    ComponentName name = intent.getComponent();
                    if (info.itemType == 0 && "android.intent.action.MAIN".equals(intent.getAction()) && name != null) {
                        Iterator it2 = apps.iterator();
                        while (it2.hasNext()) {
                            if (((ApplicationInfo) it2.next()).componentName.equals(name)) {
                                info.setIcon(this.mIconCache.getIcon(info.intent));
                                AppInfoManager.getInstance().setIOS7Icon(info, this.mIconCache);
                                ((TextView) view).setCompoundDrawablesWithIntrinsicBounds(null, new FastBitmapDrawable(info.getIcon(this.mIconCache)), null, null);
                            }
                        }
                    }
                }
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public void moveToDefaultScreen(boolean animate) {
        if (!isSmall()) {
            if (animate) {
                snapToPage(this.mDefaultHomescreen);
            } else {
                setCurrentPage(this.mDefaultHomescreen);
            }
        }
        getPageAt(this.mDefaultHomescreen).requestFocus();
    }

    public void syncPages() {
    }

    public void syncPageItems(int page, boolean immediate) {
    }

    /* access modifiers changed from: protected */
    public String getCurrentPageDescription() {
        return String.format(this.mContext.getString(R.string.workspace_scroll_format), new Object[]{Integer.valueOf((this.mNextPage != -1 ? this.mNextPage : this.mCurrentPage) + 1), Integer.valueOf(getChildCount())});
    }

    public void getLocationInDragLayer(int[] loc) {
        this.mLauncher.getDragLayer().getLocationInDragLayer(this, loc);
    }

    /* access modifiers changed from: 0000 */
    public void setFadeForOverScroll(float fade) {
        if (isScrollingIndicatorEnabled()) {
            this.mOverscrollFade = fade;
            float reducedFade = 0.5f + ((1.0f - fade) * 0.5f);
            ViewGroup parent = (ViewGroup) getParent();
            ImageView qsbDivider = (ImageView) parent.findViewById(R.id.qsb_divider);
            ImageView dockDivider = (ImageView) parent.findViewById(R.id.dock_divider);
            View scrollIndicator = getScrollingIndicator();
            cancelScrollingIndicatorAnimations();
            if (qsbDivider != null && this.mShowSearchBar) {
                qsbDivider.setAlpha(reducedFade);
            }
            if (dockDivider != null && this.mShowDockDivider) {
                dockDivider.setAlpha(reducedFade);
            }
            if (scrollIndicator != null && this.mShowScrollingIndicator) {
                scrollIndicator.setAlpha(1.0f - fade);
            }
        }
    }

    public boolean isDefaultHomeScreen(int screen) {
        return this.mDefaultHomescreen == screen + 1;
    }

    public boolean isDefaultHomeScreen(CellLayout layout) {
        return this.mDefaultHomescreen == indexOfChild(layout);
    }

    public boolean allowLongPress() {
        return super.allowLongPress();
    }

    private void initQuickActionMenu() {
        if (this.mAppQuickAction != null) {
            Resources r = getContext().getResources();
            new ActionItem(0, r.getString(R.string.quick_action_run));
            ActionItem moveItem = new ActionItem(1, r.getString(R.string.quick_action_move));
            ActionItem uninstallItem = new ActionItem(2, r.getString(R.string.quick_action_uninstall));
            this.mAppQuickAction.addActionItem(moveItem);
            this.mAppQuickAction.addActionItem(uninstallItem);
            this.mAppQuickAction.setOnActionItemClickListener(this);
        } else {
            Log.d(TAG, "the mIconQuickAction has not been initialized!");
        }
        if (this.mWidgetQuickAction != null) {
            Resources r2 = getContext().getResources();
            ActionItem moveItem2 = new ActionItem(0, r2.getString(R.string.quick_action_move));
            ActionItem uninstallItem2 = new ActionItem(1, r2.getString(R.string.quick_action_delete));
            this.mWidgetQuickAction.addActionItem(moveItem2);
            this.mWidgetQuickAction.addActionItem(uninstallItem2);
            this.mWidgetQuickAction.setOnActionItemClickListener(this);
        } else {
            Log.d(TAG, "the mWidgetQuickAction has not been initialized!");
        }
        if (this.mFolderQuickAction != null) {
            Resources r3 = getContext().getResources();
            new ActionItem(0, r3.getString(R.string.quick_action_run));
            ActionItem renameItem = new ActionItem(1, r3.getString(R.string.quick_action_rename));
            ActionItem moveItem3 = new ActionItem(2, r3.getString(R.string.quick_action_move));
            ActionItem uninstallItem3 = new ActionItem(3, r3.getString(R.string.quick_action_delete));
            this.mFolderQuickAction.addActionItem(moveItem3);
            this.mFolderQuickAction.addActionItem(renameItem);
            this.mFolderQuickAction.addActionItem(uninstallItem3);
            this.mFolderQuickAction.setOnActionItemClickListener(this);
            return;
        }
        Log.d(TAG, "the mFolderQuickAction has not been initialized!");
    }

    public void popQuickActionMenu(CellInfo cellInfo) {
        boolean z;
        boolean z2;
        boolean z3 = false;
        View child = cellInfo.cell;
        ItemInfo itemInfo = (ItemInfo) child.getTag();
        if (child instanceof BubbleTextView) {
            if (this.mAppQuickAction != null) {
                this.mCurSelectCell = cellInfo;
                this.mAppQuickAction.setActionItemAccessible(1, !this.mLauncher.isItemLocked(itemInfo));
                QuickAction quickAction = this.mAppQuickAction;
                if (!this.mLauncher.isItemLocked(itemInfo)) {
                    z3 = true;
                }
                quickAction.setActionItemAccessible(2, z3);
                this.mAppQuickAction.show(child);
                return;
            }
            Log.d(TAG, "the mIconQuickAction is not initialized");
        } else if (child instanceof FolderIcon) {
            if (this.mFolderQuickAction != null) {
                this.mCurSelectCell = cellInfo;
                QuickAction quickAction2 = this.mFolderQuickAction;
                if (this.mLauncher.isItemLocked(itemInfo)) {
                    z2 = false;
                } else {
                    z2 = true;
                }
                quickAction2.setActionItemAccessible(2, z2);
                QuickAction quickAction3 = this.mFolderQuickAction;
                if (!this.mLauncher.isItemLocked(itemInfo)) {
                    z3 = true;
                }
                quickAction3.setActionItemAccessible(3, z3);
                this.mFolderQuickAction.show(child);
                return;
            }
            Log.d(TAG, "the mFolderQuickAction is not initialized");
        } else if (!(child instanceof LauncherAppWidgetHostView)) {
        } else {
            if (this.mWidgetQuickAction != null) {
                this.mCurSelectCell = cellInfo;
                QuickAction quickAction4 = this.mWidgetQuickAction;
                if (this.mLauncher.isItemLocked(itemInfo)) {
                    z = false;
                } else {
                    z = true;
                }
                quickAction4.setActionItemAccessible(0, z);
                QuickAction quickAction5 = this.mWidgetQuickAction;
                if (!this.mLauncher.isItemLocked(itemInfo)) {
                    z3 = true;
                }
                quickAction5.setActionItemAccessible(1, z3);
                this.mWidgetQuickAction.show(child);
                return;
            }
            Log.d(TAG, "the mWidgetQuickAction is not initialized");
        }
    }

    private void deleteAppWidget(ItemInfo item) {
        this.mLauncher.removeAppWidget((LauncherAppWidgetInfo) item);
        LauncherModel.deleteItemFromDatabase(this.mLauncher, item);
        final LauncherAppWidgetInfo launcherAppWidgetInfo = (LauncherAppWidgetInfo) item;
        final LauncherAppWidgetHost appWidgetHost = this.mLauncher.getAppWidgetHost();
        if (appWidgetHost != null) {
            new Thread("deleteAppWidgetId") {
                public void run() {
                    appWidgetHost.deleteAppWidgetId(launcherAppWidgetInfo.appWidgetId);
                }
            }.start();
        }
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
                    } else {
                        startDrag(this.mCurSelectCell);
                        return;
                    }
                case 2:
                    ShortcutInfo info = (ShortcutInfo) this.mCurSelectCell.cell.getTag();
                    if (this.mLauncher.getPackageManager().resolveActivity(info.intent, 0) != null) {
                        this.mLauncher.startShortcutUninstallActivity(info);
                        return;
                    }
                    LauncherModel.deleteItemFromDatabase(this.mLauncher, info);
                    ((CellLayout) this.mCurSelectCell.cell.getParent().getParent()).removeView(this.mCurSelectCell.cell);
                    return;
                default:
                    return;
            }
        } else if (source == this.mWidgetQuickAction) {
            switch (actionId) {
                case 0:
                    if (this.mLauncher.isItemLocked((ItemInfo) this.mCurSelectCell.cell.getTag())) {
                        this.mLauncher.warnTargetisLocked();
                        return;
                    } else {
                        startDrag(this.mCurSelectCell);
                        return;
                    }
                case 1:
                    if (this.mLauncher.isItemLocked((ItemInfo) this.mCurSelectCell.cell.getTag())) {
                        this.mLauncher.warnTargetisLocked();
                        return;
                    }
                    deleteAppWidget((ItemInfo) this.mCurSelectCell.cell.getTag());
                    ((CellLayout) this.mCurSelectCell.cell.getParent().getParent()).removeView(this.mCurSelectCell.cell);
                    return;
                default:
                    return;
            }
        } else if (source == this.mFolderQuickAction) {
            switch (actionId) {
                case 0:
                    this.mLauncher.setFolderState(Folder.STATE_TO_RUN);
                    this.mLauncher.onClick(this.mCurSelectCell.cell);
                    return;
                case 1:
                    if (this.mLauncher.isItemLocked((ItemInfo) this.mCurSelectCell.cell.getTag())) {
                        this.mLauncher.warnTargetisLocked();
                        return;
                    }
                    this.mLauncher.setFolderState(Folder.STATE_TO_EDIT);
                    this.mLauncher.onClick(this.mCurSelectCell.cell);
                    return;
                case 2:
                    if (this.mLauncher.isItemLocked((ItemInfo) this.mCurSelectCell.cell.getTag())) {
                        this.mLauncher.warnTargetisLocked();
                        return;
                    } else {
                        startDrag(this.mCurSelectCell);
                        return;
                    }
                case 3:
                    if (this.mLauncher.isItemLocked((ItemInfo) this.mCurSelectCell.cell.getTag())) {
                        this.mLauncher.warnTargetisLocked();
                        return;
                    }
                    FolderInfo folderInfo = (FolderInfo) this.mCurSelectCell.cell.getTag();
                    if (!folderInfo.contents.isEmpty()) {
                        this.mLauncher.removeFolder(folderInfo);
                        LauncherModel.deleteFolderContentsFromDatabase(this.mLauncher, folderInfo);
                        ((CellLayout) this.mCurSelectCell.cell.getParent().getParent()).removeView(this.mCurSelectCell.cell);
                        return;
                    }
                    this.mLauncher.removeFolder(folderInfo);
                    LauncherModel.deleteFolderContentsFromDatabase(this.mLauncher, folderInfo);
                    ((CellLayout) this.mCurSelectCell.cell.getParent().getParent()).removeView(this.mCurSelectCell.cell);
                    return;
                default:
                    return;
            }
        }
    }

    public boolean isInSearchScreen() {
        return isSearchScreen(this.mCurrentPage);
    }

    public boolean isSearchScreen(int screen) {
        return this.mSearchScreenEnable && screen == this.mSearchScreen;
    }

    public boolean isSearchScreen(CellLayout cellLayout) {
        return this.mSearchScreenEnable && indexOfChild(cellLayout) == this.mSearchScreen;
    }

    public CellLayout getSearchScreen() {
        if (!this.mSearchScreenEnable) {
            return null;
        }
        return (CellLayout) getChildAt(this.mSearchScreen);
    }

    public int getSearchScreenIndex() {
        if (this.mSearchScreenEnable) {
            return this.mSearchScreen;
        }
        return -1;
    }

    public int getHomeScreenIndex() {
        return this.mDefaultHomescreen;
    }

    public void closeAllQuickActions() {
        if (this.mAppQuickAction != null && this.mAppQuickAction.isShowing()) {
            this.mAppQuickAction.dismiss();
        }
        if (this.mWidgetQuickAction != null && this.mWidgetQuickAction.isShowing()) {
            this.mWidgetQuickAction.dismiss();
        }
        if (this.mFolderQuickAction != null && this.mFolderQuickAction.isShowing()) {
            this.mFolderQuickAction.dismiss();
        }
    }
}
