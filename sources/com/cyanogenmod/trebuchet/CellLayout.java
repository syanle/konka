package com.cyanogenmod.trebuchet;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.view.ViewDebug.ExportedProperty;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;
import android.widget.LinearLayout;
import com.cyanogenmod.trebuchet.FolderIcon.FolderRingAnimator;
import com.cyanogenmod.trebuchet.RocketLauncher.Board.FlyingIcon;
import com.konka.ios7launcher.R;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class CellLayout extends ViewGroup {
    static final String TAG = "CellLayout";
    private Drawable mActiveGlowBackground;
    private float mBackgroundAlpha;
    private float mBackgroundAlphaMultiplier;
    private Rect mBackgroundRect;
    private int mCellHeight;
    private final CellInfo mCellInfo;
    private int mCellWidth;
    private CellLayoutChildren mChildren;
    private int mCountX;
    private int mCountY;
    private InterruptibleInOutAnimator mCrosshairsAnimator;
    private Drawable mCrosshairsDrawable;
    /* access modifiers changed from: private */
    public float mCrosshairsVisibility;
    private final int[] mDragCell;
    private final Point mDragCenter;
    /* access modifiers changed from: private */
    public float[] mDragOutlineAlphas;
    private InterruptibleInOutAnimator[] mDragOutlineAnims;
    private int mDragOutlineCurrent;
    private final Paint mDragOutlinePaint;
    /* access modifiers changed from: private */
    public Point[] mDragOutlines;
    private boolean mDragging;
    private TimeInterpolator mEaseOutInterpolator;
    private int[] mFolderLeaveBehindCell;
    private ArrayList<FolderRingAnimator> mFolderOuterRings;
    private int mForegroundAlpha;
    private int mForegroundPadding;
    private Rect mForegroundRect;
    private LinearLayout mGuideLayout;
    private ViewGroup mGuideViewGroup;
    private int mHeightGap;
    private final LayoutInflater mInflater;
    private OnTouchListener mInterceptTouchListener;
    private boolean mIsDragOverlapping;
    private boolean mLastDownOnOccupiedCell;
    private int mMaxGap;
    private Drawable mNormalBackground;
    boolean[][] mOccupied;
    private int mOriginalCellHeight;
    private int mOriginalCellWidth;
    private int mOriginalHeightGap;
    private int mOriginalWidthGap;
    private Drawable mOverScrollForegroundDrawable;
    private Drawable mOverScrollLeft;
    private Drawable mOverScrollRight;
    private BubbleTextView mPressedOrFocusedIcon;
    private final Rect mRect;
    /* access modifiers changed from: private */
    public HashMap<LayoutParams, ObjectAnimator> mReorderAnimators;
    private boolean mScrollingTransformsDirty;
    private int mSpacingBottom;
    private int mSpacingLeft;
    private int mSpacingRight;
    private int mSpacingTop;
    int[] mTempLocation;
    private final int[] mTmpPoint;
    private final PointF mTmpPointF;
    private final int[] mTmpXY;
    private int mWidthGap;

    static final class CellInfo {
        View cell;
        int cellX = -1;
        int cellY = -1;
        long container;
        int screen;
        int spanX;
        int spanY;

        CellInfo() {
        }

        public String toString() {
            return "Cell[view=" + (this.cell == null ? "null" : this.cell.getClass()) + ", x=" + this.cellX + ", y=" + this.cellY + "]";
        }
    }

    public static class CellLayoutAnimationController extends LayoutAnimationController {
        public CellLayoutAnimationController(Animation animation, float delay) {
            super(animation, delay);
        }

        /* access modifiers changed from: protected */
        public long getDelayForView(View view) {
            return (long) ((int) (Math.random() * 150.0d));
        }
    }

    public static class LayoutParams extends MarginLayoutParams {
        @ExportedProperty
        public int cellHSpan;
        @ExportedProperty
        public int cellVSpan;
        @ExportedProperty
        public int cellX;
        @ExportedProperty
        public int cellY;
        boolean dropped;
        public boolean isLockedToGrid;
        @ExportedProperty
        int x;
        @ExportedProperty
        int y;

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
            this.isLockedToGrid = true;
            this.cellHSpan = 1;
            this.cellVSpan = 1;
        }

        public LayoutParams(android.view.ViewGroup.LayoutParams source) {
            super(source);
            this.isLockedToGrid = true;
            this.cellHSpan = 1;
            this.cellVSpan = 1;
        }

        public LayoutParams(LayoutParams source) {
            super(source);
            this.isLockedToGrid = true;
            this.cellX = source.cellX;
            this.cellY = source.cellY;
            this.cellHSpan = source.cellHSpan;
            this.cellVSpan = source.cellVSpan;
        }

        public LayoutParams(int cellX2, int cellY2, int cellHSpan2, int cellVSpan2) {
            super(-1, -1);
            this.isLockedToGrid = true;
            this.cellX = cellX2;
            this.cellY = cellY2;
            this.cellHSpan = cellHSpan2;
            this.cellVSpan = cellVSpan2;
        }

        public void setup(int cellWidth, int cellHeight, int widthGap, int heightGap, int spacingLeft, int spacingTop) {
            if (this.isLockedToGrid) {
                int myCellHSpan = this.cellHSpan;
                int myCellVSpan = this.cellVSpan;
                int myCellX = this.cellX;
                int myCellY = this.cellY;
                this.width = (((myCellHSpan * cellWidth) + ((myCellHSpan - 1) * widthGap)) - this.leftMargin) - this.rightMargin;
                this.height = (((myCellVSpan * cellHeight) + ((myCellVSpan - 1) * heightGap)) - this.topMargin) - this.bottomMargin;
                this.x = ((cellWidth + widthGap) * myCellX) + this.leftMargin + spacingLeft;
                this.y = ((cellHeight + heightGap) * myCellY) + this.topMargin + spacingTop;
            }
        }

        public String toString() {
            return "(" + this.cellX + ", " + this.cellY + ")";
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

    public CellLayout(Context context) {
        this(context, null, 0);
    }

    public CellLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CellLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mScrollingTransformsDirty = false;
        this.mRect = new Rect();
        this.mCellInfo = new CellInfo();
        this.mTmpXY = new int[2];
        this.mTmpPoint = new int[2];
        this.mTmpPointF = new PointF();
        this.mTempLocation = new int[2];
        this.mLastDownOnOccupiedCell = false;
        this.mFolderOuterRings = new ArrayList<>();
        this.mFolderLeaveBehindCell = new int[]{-1, -1};
        this.mForegroundAlpha = 0;
        this.mBackgroundAlphaMultiplier = 1.0f;
        this.mIsDragOverlapping = false;
        this.mDragCenter = new Point();
        this.mDragOutlines = new Point[4];
        this.mDragOutlineAlphas = new float[this.mDragOutlines.length];
        this.mDragOutlineAnims = new InterruptibleInOutAnimator[this.mDragOutlines.length];
        this.mDragOutlineCurrent = 0;
        this.mDragOutlinePaint = new Paint();
        this.mCrosshairsDrawable = null;
        this.mCrosshairsAnimator = null;
        this.mCrosshairsVisibility = FlyingIcon.ANGULAR_VMIN;
        this.mReorderAnimators = new HashMap<>();
        this.mDragCell = new int[2];
        this.mDragging = false;
        setWillNotDraw(false);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CellLayout, defStyle, 0);
        this.mInflater = (LayoutInflater) context.getSystemService("layout_inflater");
        int dimensionPixelSize = a.getDimensionPixelSize(0, 10);
        this.mCellWidth = dimensionPixelSize;
        this.mOriginalCellWidth = dimensionPixelSize;
        int dimensionPixelSize2 = a.getDimensionPixelSize(1, 10);
        this.mCellHeight = dimensionPixelSize2;
        this.mOriginalCellHeight = dimensionPixelSize2;
        int dimensionPixelSize3 = a.getDimensionPixelSize(2, 0);
        this.mOriginalWidthGap = dimensionPixelSize3;
        this.mWidthGap = dimensionPixelSize3;
        int dimensionPixelSize4 = a.getDimensionPixelSize(3, 0);
        this.mOriginalHeightGap = dimensionPixelSize4;
        this.mHeightGap = dimensionPixelSize4;
        this.mMaxGap = a.getDimensionPixelSize(4, 0);
        this.mSpacingLeft = a.getDimensionPixelSize(5, 0);
        this.mSpacingRight = a.getDimensionPixelSize(6, 0);
        this.mSpacingTop = a.getDimensionPixelSize(7, 0);
        this.mSpacingBottom = a.getDimensionPixelSize(8, 0);
        this.mCountX = LauncherModel.getCellCountX();
        this.mCountY = LauncherModel.getCellCountY();
        this.mOccupied = (boolean[][]) Array.newInstance(Boolean.TYPE, new int[]{this.mCountX, this.mCountY});
        a.recycle();
        setAlwaysDrawnWithCacheEnabled(false);
        Resources res = getResources();
        this.mNormalBackground = res.getDrawable(R.drawable.homescreen_blue_normal_holo);
        this.mActiveGlowBackground = res.getDrawable(R.drawable.homescreen_blue_strong_holo);
        this.mOverScrollLeft = res.getDrawable(R.drawable.overscroll_glow_left);
        this.mOverScrollRight = res.getDrawable(R.drawable.overscroll_glow_right);
        this.mForegroundPadding = res.getDimensionPixelSize(R.dimen.workspace_overscroll_drawable_padding);
        this.mNormalBackground.setFilterBitmap(true);
        this.mActiveGlowBackground.setFilterBitmap(true);
        this.mCrosshairsDrawable = res.getDrawable(R.drawable.gardening_crosshairs);
        this.mEaseOutInterpolator = new DecelerateInterpolator(2.5f);
        this.mCrosshairsAnimator = new InterruptibleInOutAnimator((long) res.getInteger(R.integer.config_crosshairsFadeInTime), FlyingIcon.ANGULAR_VMIN, 1.0f);
        this.mCrosshairsAnimator.getAnimator().addUpdateListener(new AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                CellLayout.this.mCrosshairsVisibility = ((Float) animation.getAnimatedValue()).floatValue();
                CellLayout.this.invalidate();
            }
        });
        this.mCrosshairsAnimator.getAnimator().setInterpolator(this.mEaseOutInterpolator);
        int[] iArr = this.mDragCell;
        this.mDragCell[1] = -1;
        iArr[0] = -1;
        for (int i = 0; i < this.mDragOutlines.length; i++) {
            this.mDragOutlines[i] = new Point(-1, -1);
        }
        int duration = res.getInteger(R.integer.config_dragOutlineFadeTime);
        float toAlphaValue = (float) res.getInteger(R.integer.config_dragOutlineMaxAlpha);
        Arrays.fill(this.mDragOutlineAlphas, FlyingIcon.ANGULAR_VMIN);
        for (int i2 = 0; i2 < this.mDragOutlineAnims.length; i2++) {
            final InterruptibleInOutAnimator anim = new InterruptibleInOutAnimator((long) duration, FlyingIcon.ANGULAR_VMIN, toAlphaValue);
            anim.getAnimator().setInterpolator(this.mEaseOutInterpolator);
            final int thisIndex = i2;
            anim.getAnimator().addUpdateListener(new AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator animation) {
                    Bitmap outline = (Bitmap) anim.getTag();
                    if (outline == null) {
                        animation.cancel();
                        return;
                    }
                    CellLayout.this.mDragOutlineAlphas[thisIndex] = ((Float) animation.getAnimatedValue()).floatValue();
                    int left = CellLayout.this.mDragOutlines[thisIndex].x;
                    int top = CellLayout.this.mDragOutlines[thisIndex].y;
                    CellLayout.this.invalidate(left, top, outline.getWidth() + left, outline.getHeight() + top);
                }
            });
            anim.getAnimator().addListener(new AnimatorListenerAdapter() {
                public void onAnimationEnd(Animator animation) {
                    if (((Float) ((ValueAnimator) animation).getAnimatedValue()).floatValue() == FlyingIcon.ANGULAR_VMIN) {
                        anim.setTag(null);
                    }
                }
            });
            this.mDragOutlineAnims[i2] = anim;
        }
        this.mBackgroundRect = new Rect();
        this.mForegroundRect = new Rect();
        this.mChildren = new CellLayoutChildren(context);
        addView(this.mChildren);
    }

    public void setupGuideView(Context context) {
        ViewGroup mGuideViewGroup2 = (ViewGroup) this.mInflater.inflate(R.layout.add_guide, null);
        this.mGuideLayout = (LinearLayout) mGuideViewGroup2.findViewById(R.id.add_guide);
        Button guideBtn = (Button) mGuideViewGroup2.findViewById(R.id.add_guide_btn);
        guideBtn.setOnKeyListener(new OnKeyListener() {
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                return FocusHelper.handleAddGuideKeyEvent(view, keyCode, keyEvent);
            }
        });
        guideBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                ((SearchDropTargetBar) ((DragLayer) CellLayout.this.getParent().getParent()).findViewById(R.id.qsb_bar)).onClickHomescreenAddButton();
            }
        });
        addView(mGuideViewGroup2);
    }

    public void refreshGuideView() {
        if (!(getParent() instanceof Folder) && this.mGuideLayout != null) {
            this.mGuideLayout.setVisibility(this.mChildren.getChildCount() > 0 ? 4 : 0);
        }
    }

    public void hideGuideView() {
        if (this.mGuideLayout != null) {
            this.mGuideLayout.setVisibility(4);
        }
    }

    static int widthInPortrait(Resources r, int numCells) {
        int cellWidth = r.getDimensionPixelSize(R.dimen.workspace_cell_width);
        return ((numCells - 1) * Math.min(r.getDimensionPixelSize(R.dimen.workspace_width_gap), r.getDimensionPixelSize(R.dimen.workspace_height_gap))) + (cellWidth * numCells);
    }

    static int heightInLandscape(Resources r, int numCells) {
        int cellHeight = r.getDimensionPixelSize(R.dimen.workspace_cell_height);
        return ((numCells - 1) * Math.min(r.getDimensionPixelSize(R.dimen.workspace_width_gap), r.getDimensionPixelSize(R.dimen.workspace_height_gap))) + (cellHeight * numCells);
    }

    public void enableHardwareLayers() {
        this.mChildren.enableHardwareLayers();
    }

    public void disableHardwareLayers() {
        this.mChildren.disableHardwareLayers();
    }

    public void setGridSize(int x, int y) {
        this.mCountX = x;
        this.mCountY = y;
        this.mOccupied = (boolean[][]) Array.newInstance(Boolean.TYPE, new int[]{this.mCountX, this.mCountY});
        requestLayout();
    }

    private void invalidateBubbleTextView(BubbleTextView icon) {
        int padding = icon.getPressedOrFocusedBackgroundPadding();
        invalidate((icon.getLeft() + getPaddingLeft()) - padding, (icon.getTop() + getPaddingTop()) - padding, icon.getRight() + getPaddingLeft() + padding, icon.getBottom() + getPaddingTop() + padding);
    }

    /* access modifiers changed from: 0000 */
    public void setOverScrollAmount(float r, boolean left) {
        if (left && this.mOverScrollForegroundDrawable != this.mOverScrollLeft) {
            this.mOverScrollForegroundDrawable = this.mOverScrollLeft;
        } else if (!left && this.mOverScrollForegroundDrawable != this.mOverScrollRight) {
            this.mOverScrollForegroundDrawable = this.mOverScrollRight;
        }
        this.mForegroundAlpha = Math.round(255.0f * r);
        this.mOverScrollForegroundDrawable.setAlpha(this.mForegroundAlpha);
        invalidate();
    }

    /* access modifiers changed from: 0000 */
    public void setPressedOrFocusedIcon(BubbleTextView icon) {
        BubbleTextView oldIcon = this.mPressedOrFocusedIcon;
        this.mPressedOrFocusedIcon = icon;
        if (oldIcon != null) {
            invalidateBubbleTextView(oldIcon);
        }
        if (this.mPressedOrFocusedIcon != null) {
            invalidateBubbleTextView(this.mPressedOrFocusedIcon);
        }
    }

    public CellLayoutChildren getChildrenLayout() {
        if (getChildCount() > 0) {
            return (CellLayoutChildren) getChildAt(0);
        }
        return null;
    }

    /* access modifiers changed from: 0000 */
    public void setIsDragOverlapping(boolean isDragOverlapping) {
        if (this.mIsDragOverlapping != isDragOverlapping) {
            this.mIsDragOverlapping = isDragOverlapping;
            invalidate();
        }
    }

    /* access modifiers changed from: 0000 */
    public boolean getIsDragOverlapping() {
        return this.mIsDragOverlapping;
    }

    /* access modifiers changed from: protected */
    public void setOverscrollTransformsDirty(boolean dirty) {
        this.mScrollingTransformsDirty = dirty;
    }

    /* access modifiers changed from: protected */
    public void resetOverscrollTransforms() {
        if (this.mScrollingTransformsDirty) {
            setOverscrollTransformsDirty(false);
            setTranslationX(FlyingIcon.ANGULAR_VMIN);
            setRotationY(FlyingIcon.ANGULAR_VMIN);
            setCameraDistance(1280.0f * LauncherApplication.getScreenDensity());
            setOverScrollAmount(FlyingIcon.ANGULAR_VMIN, false);
            setPivotX((float) (getMeasuredWidth() / 2));
            setPivotY((float) (getMeasuredHeight() / 2));
        }
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        Drawable bg;
        if (this.mBackgroundAlpha > FlyingIcon.ANGULAR_VMIN) {
            if (this.mIsDragOverlapping) {
                bg = this.mActiveGlowBackground;
            } else {
                bg = this.mNormalBackground;
            }
            bg.setAlpha((int) (this.mBackgroundAlpha * this.mBackgroundAlphaMultiplier * 255.0f));
            bg.setBounds(this.mBackgroundRect);
            bg.draw(canvas);
        }
        if (this.mCrosshairsVisibility > FlyingIcon.ANGULAR_VMIN) {
            int countX = this.mCountX;
            int countY = this.mCountY;
            Drawable d = this.mCrosshairsDrawable;
            int width = d.getIntrinsicWidth();
            int height = d.getIntrinsicHeight();
            int x = (getPaddingLeft() - (this.mWidthGap / 2)) - (width / 2);
            for (int col = 0; col <= countX; col++) {
                int y = (getPaddingTop() - (this.mHeightGap / 2)) - (height / 2);
                for (int row = 0; row <= countY; row++) {
                    this.mTmpPointF.set((float) (x - this.mDragCenter.x), (float) (y - this.mDragCenter.y));
                    float alpha = Math.min(0.4f, 0.002f * (600.0f - this.mTmpPointF.length()));
                    if (alpha > FlyingIcon.ANGULAR_VMIN) {
                        d.setBounds(x, y, x + width, y + height);
                        d.setAlpha((int) (255.0f * alpha * this.mCrosshairsVisibility));
                        d.draw(canvas);
                    }
                    y += this.mCellHeight + this.mHeightGap;
                }
                x += this.mCellWidth + this.mWidthGap;
            }
        }
        Paint paint = this.mDragOutlinePaint;
        for (int i = 0; i < this.mDragOutlines.length; i++) {
            float alpha2 = this.mDragOutlineAlphas[i];
            if (alpha2 > FlyingIcon.ANGULAR_VMIN) {
                Point p = this.mDragOutlines[i];
                Bitmap b = (Bitmap) this.mDragOutlineAnims[i].getTag();
                paint.setAlpha((int) (0.5f + alpha2));
                canvas.drawBitmap(b, (float) p.x, (float) p.y, paint);
            }
        }
        if (this.mPressedOrFocusedIcon != null) {
            int padding = this.mPressedOrFocusedIcon.getPressedOrFocusedBackgroundPadding();
            Bitmap b2 = this.mPressedOrFocusedIcon.getPressedOrFocusedBackground();
            if (b2 != null) {
                canvas.drawBitmap(b2, (float) ((this.mPressedOrFocusedIcon.getLeft() + getPaddingLeft()) - padding), (float) ((this.mPressedOrFocusedIcon.getTop() + getPaddingTop()) - padding), null);
            }
        }
        for (int i2 = 0; i2 < this.mFolderOuterRings.size(); i2++) {
            FolderRingAnimator fra = (FolderRingAnimator) this.mFolderOuterRings.get(i2);
            Drawable d2 = FolderRingAnimator.sSharedOuterRingDrawable;
            int width2 = (int) fra.getOuterRingSize();
            int height2 = width2;
            cellToPoint(fra.mCellX, fra.mCellY, this.mTempLocation);
            int centerX = this.mTempLocation[0] + (this.mCellWidth / 2);
            int centerY = this.mTempLocation[1] + (FolderRingAnimator.sPreviewSize / 2);
            canvas.save();
            canvas.translate((float) (centerX - (width2 / 2)), (float) (centerY - (height2 / 2)));
            d2.setBounds(0, 0, width2, height2);
            d2.draw(canvas);
            canvas.restore();
            Drawable d3 = FolderRingAnimator.sSharedInnerRingDrawable;
            int width3 = (int) fra.getInnerRingSize();
            int height3 = width3;
            cellToPoint(fra.mCellX, fra.mCellY, this.mTempLocation);
            int centerX2 = this.mTempLocation[0] + (this.mCellWidth / 2);
            int centerY2 = this.mTempLocation[1] + (FolderRingAnimator.sPreviewSize / 2);
            canvas.save();
            canvas.translate((float) (centerX2 - (width3 / 2)), (float) (centerY2 - (width3 / 2)));
            d3.setBounds(0, 0, width3, height3);
            d3.draw(canvas);
            canvas.restore();
        }
        if (this.mFolderLeaveBehindCell[0] >= 0 && this.mFolderLeaveBehindCell[1] >= 0) {
            Drawable d4 = FolderIcon.sSharedFolderLeaveBehind;
            int width4 = d4.getIntrinsicWidth();
            int height4 = d4.getIntrinsicHeight();
            cellToPoint(this.mFolderLeaveBehindCell[0], this.mFolderLeaveBehindCell[1], this.mTempLocation);
            int centerX3 = this.mTempLocation[0] + (this.mCellWidth / 2);
            int centerY3 = this.mTempLocation[1] + (FolderRingAnimator.sPreviewSize / 2);
            canvas.save();
            canvas.translate((float) (centerX3 - (width4 / 2)), (float) (centerY3 - (width4 / 2)));
            d4.setBounds(0, 0, width4, height4);
            d4.draw(canvas);
            canvas.restore();
        }
    }

    /* access modifiers changed from: protected */
    public void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (this.mForegroundAlpha > 0) {
            this.mOverScrollForegroundDrawable.setBounds(this.mForegroundRect);
            Paint p = ((NinePatchDrawable) this.mOverScrollForegroundDrawable).getPaint();
            p.setXfermode(new PorterDuffXfermode(Mode.ADD));
            this.mOverScrollForegroundDrawable.draw(canvas);
            p.setXfermode(null);
        }
    }

    public void showFolderAccept(FolderRingAnimator fra) {
        this.mFolderOuterRings.add(fra);
    }

    public void hideFolderAccept(FolderRingAnimator fra) {
        if (this.mFolderOuterRings.contains(fra)) {
            this.mFolderOuterRings.remove(fra);
        }
        invalidate();
    }

    public void setFolderLeaveBehindCell(int x, int y) {
        this.mFolderLeaveBehindCell[0] = x;
        this.mFolderLeaveBehindCell[1] = y;
        invalidate();
    }

    public void clearFolderLeaveBehind() {
        this.mFolderLeaveBehindCell[0] = -1;
        this.mFolderLeaveBehindCell[1] = -1;
        invalidate();
    }

    public boolean shouldDelayChildPressedState() {
        return false;
    }

    public void cancelLongPress() {
        super.cancelLongPress();
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            getChildAt(i).cancelLongPress();
        }
    }

    public void setOnInterceptTouchListener(OnTouchListener listener) {
        this.mInterceptTouchListener = listener;
    }

    /* access modifiers changed from: 0000 */
    public int getCountX() {
        return this.mCountX;
    }

    /* access modifiers changed from: 0000 */
    public int getCountY() {
        return this.mCountY;
    }

    public boolean addViewToCellLayout(View child, int index, int childId, LayoutParams params, boolean markCells) {
        LayoutParams lp = params;
        if (lp.cellX < 0 || lp.cellX > this.mCountX - 1 || lp.cellY < 0 || lp.cellY > this.mCountY - 1) {
            return false;
        }
        if (lp.cellHSpan < 0) {
            lp.cellHSpan = this.mCountX;
        }
        if (lp.cellVSpan < 0) {
            lp.cellVSpan = this.mCountY;
        }
        Log.d("czj-ios", "setid:" + childId);
        child.setId(childId);
        this.mChildren.addView(child, index, lp);
        if (markCells) {
            markCellsAsOccupiedForView(child);
        }
        refreshGuideView();
        return true;
    }

    public void removeAllViews() {
        clearOccupiedCells();
        this.mChildren.removeAllViews();
    }

    public void removeAllViewsInLayout() {
        if (this.mChildren.getChildCount() > 0) {
            clearOccupiedCells();
            this.mChildren.removeAllViewsInLayout();
        }
    }

    public void removeViewWithoutMarkingCells(View view) {
        this.mChildren.removeView(view);
        refreshGuideView();
    }

    public void removeView(View view) {
        markCellsAsUnoccupiedForView(view);
        this.mChildren.removeView(view);
        refreshGuideView();
    }

    public void removeViewAt(int index) {
        markCellsAsUnoccupiedForView(this.mChildren.getChildAt(index));
        this.mChildren.removeViewAt(index);
        refreshGuideView();
    }

    public void removeViewInLayout(View view) {
        markCellsAsUnoccupiedForView(view);
        this.mChildren.removeViewInLayout(view);
        refreshGuideView();
    }

    public void removeViews(int start, int count) {
        for (int i = start; i < start + count; i++) {
            markCellsAsUnoccupiedForView(this.mChildren.getChildAt(i));
        }
        this.mChildren.removeViews(start, count);
    }

    public void removeViewsInLayout(int start, int count) {
        for (int i = start; i < start + count; i++) {
            markCellsAsUnoccupiedForView(this.mChildren.getChildAt(i));
        }
        this.mChildren.removeViewsInLayout(start, count);
    }

    public void drawChildren(Canvas canvas) {
        this.mChildren.draw(canvas);
    }

    /* access modifiers changed from: 0000 */
    public void buildChildrenLayer() {
        this.mChildren.buildLayer();
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.mCellInfo.screen = ((ViewGroup) getParent()).indexOfChild(this);
    }

    public void setTagToCellInfoForPoint(int touchX, int touchY) {
        CellInfo cellInfo = this.mCellInfo;
        Rect frame = this.mRect;
        int x = touchX + this.mScrollX;
        int y = touchY + this.mScrollY;
        boolean found = false;
        int i = this.mChildren.getChildCount() - 1;
        while (true) {
            if (i < 0) {
                break;
            }
            View child = this.mChildren.getChildAt(i);
            LayoutParams lp = (LayoutParams) child.getLayoutParams();
            if ((child.getVisibility() == 0 || child.getAnimation() != null) && lp.isLockedToGrid) {
                child.getHitRect(frame);
                frame.offset(this.mPaddingLeft, this.mPaddingTop);
                if (frame.contains(x, y)) {
                    cellInfo.cell = child;
                    cellInfo.cellX = lp.cellX;
                    cellInfo.cellY = lp.cellY;
                    cellInfo.spanX = lp.cellHSpan;
                    cellInfo.spanY = lp.cellVSpan;
                    found = true;
                    break;
                }
            }
            i--;
        }
        this.mLastDownOnOccupiedCell = found;
        if (!found) {
            int[] cellXY = this.mTmpXY;
            pointToCellExact(x, y, cellXY);
            cellInfo.cell = null;
            cellInfo.cellX = cellXY[0];
            cellInfo.cellY = cellXY[1];
            cellInfo.spanX = 1;
            cellInfo.spanY = 1;
        }
        setTag(cellInfo);
    }

    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        if (action == 0) {
            clearTagCellInfo();
        }
        if (this.mInterceptTouchListener != null && this.mInterceptTouchListener.onTouch(this, ev)) {
            return true;
        }
        if (action == 0) {
            setTagToCellInfoForPoint((int) ev.getX(), (int) ev.getY());
        }
        return false;
    }

    private void clearTagCellInfo() {
        CellInfo cellInfo = this.mCellInfo;
        cellInfo.cell = null;
        cellInfo.cellX = -1;
        cellInfo.cellY = -1;
        cellInfo.spanX = 0;
        cellInfo.spanY = 0;
        setTag(cellInfo);
    }

    public CellInfo getTag() {
        return (CellInfo) super.getTag();
    }

    /* access modifiers changed from: 0000 */
    public void pointToCellExact(int x, int y, int[] result) {
        int hStartPadding = getPaddingLeft();
        int vStartPadding = getPaddingTop();
        result[0] = (x - hStartPadding) / (this.mCellWidth + this.mWidthGap);
        result[1] = (y - vStartPadding) / (this.mCellHeight + this.mHeightGap);
        int xAxis = this.mCountX;
        int yAxis = this.mCountY;
        if (result[0] < 0) {
            result[0] = 0;
        }
        if (result[0] >= xAxis) {
            result[0] = xAxis - 1;
        }
        if (result[1] < 0) {
            result[1] = 0;
        }
        if (result[1] >= yAxis) {
            result[1] = yAxis - 1;
        }
    }

    /* access modifiers changed from: 0000 */
    public void pointToCellRounded(int x, int y, int[] result) {
        pointToCellExact((this.mCellWidth / 2) + x, (this.mCellHeight / 2) + y, result);
    }

    /* access modifiers changed from: 0000 */
    public void cellToPoint(int cellX, int cellY, int[] result) {
        int vStartPadding = getPaddingTop() + this.mSpacingTop;
        result[0] = ((this.mCellWidth + this.mWidthGap) * cellX) + getPaddingLeft() + this.mSpacingLeft;
        result[1] = ((this.mCellHeight + this.mHeightGap) * cellY) + vStartPadding;
    }

    /* access modifiers changed from: 0000 */
    public void cellToCenterPoint(int cellX, int cellY, int[] result) {
        int hStartPadding = getPaddingLeft();
        int vStartPadding = getPaddingTop();
        result[0] = ((this.mCellWidth + this.mWidthGap) * cellX) + hStartPadding + (this.mCellWidth / 2);
        result[1] = ((this.mCellHeight + this.mHeightGap) * cellY) + vStartPadding + (this.mCellHeight / 2);
    }

    /* access modifiers changed from: 0000 */
    public int getCellWidth() {
        return this.mCellWidth;
    }

    /* access modifiers changed from: 0000 */
    public int getCellHeight() {
        return this.mCellHeight;
    }

    /* access modifiers changed from: 0000 */
    public int getWidthGap() {
        return this.mWidthGap;
    }

    /* access modifiers changed from: 0000 */
    public int getHeightGap() {
        return this.mHeightGap;
    }

    /* access modifiers changed from: 0000 */
    public Rect getContentRect(Rect r) {
        if (r == null) {
            r = new Rect();
        }
        int left = getPaddingLeft();
        int top = getPaddingTop();
        r.set(left, top, ((getWidth() + left) - this.mPaddingLeft) - this.mPaddingRight, ((getHeight() + top) - this.mPaddingTop) - this.mPaddingBottom);
        return r;
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);
        if (widthSpecMode == 0 || heightSpecMode == 0) {
            throw new RuntimeException("CellLayout cannot have UNSPECIFIED dimensions");
        }
        int numWidthGaps = this.mCountX - 1;
        int numHeightGaps = this.mCountY - 1;
        if (!LauncherApplication.isScreenLarge()) {
            int i = ((widthSpecSize - this.mPaddingLeft) - this.mPaddingRight) / this.mCountX;
            this.mOriginalCellWidth = i;
            this.mCellWidth = i;
            int i2 = ((heightSpecSize - this.mPaddingTop) - this.mPaddingBottom) / this.mCountY;
            this.mOriginalCellHeight = i2;
            this.mCellHeight = i2;
        }
        if (this.mOriginalWidthGap < 0 || this.mOriginalHeightGap < 0) {
            int vFreeSpace = ((heightSpecSize - this.mPaddingTop) - this.mPaddingBottom) - (this.mCountY * this.mOriginalCellHeight);
            this.mWidthGap = Math.min(this.mMaxGap, numWidthGaps > 0 ? (((widthSpecSize - this.mPaddingLeft) - this.mPaddingRight) - (this.mCountX * this.mOriginalCellWidth)) / numWidthGaps : 0);
            this.mHeightGap = Math.min(this.mMaxGap, numHeightGaps > 0 ? vFreeSpace / numHeightGaps : 0);
            this.mChildren.setCellDimensions(this.mCellWidth, this.mCellHeight, this.mWidthGap, this.mHeightGap, this.mSpacingLeft, this.mSpacingTop);
        } else {
            this.mWidthGap = this.mOriginalWidthGap;
            this.mHeightGap = this.mOriginalHeightGap;
        }
        this.mChildren.setCellDimensions(this.mCellWidth, this.mCellHeight, this.mWidthGap, this.mHeightGap, this.mSpacingLeft, this.mSpacingTop);
        int newWidth = widthSpecSize;
        int newHeight = heightSpecSize;
        if (widthSpecMode == Integer.MIN_VALUE) {
            newWidth = this.mPaddingLeft + this.mPaddingRight + (this.mCountX * this.mCellWidth) + ((this.mCountX - 1) * this.mWidthGap) + this.mSpacingLeft + this.mSpacingRight;
            newHeight = this.mPaddingTop + this.mPaddingBottom + (this.mCountY * this.mCellHeight) + ((this.mCountY - 1) * this.mHeightGap) + this.mSpacingTop + this.mSpacingBottom;
            setMeasuredDimension(newWidth, newHeight);
        }
        int count = getChildCount();
        for (int i3 = 0; i3 < count; i3++) {
            getChildAt(i3).measure(MeasureSpec.makeMeasureSpec((newWidth - this.mPaddingLeft) - this.mPaddingRight, 1073741824), MeasureSpec.makeMeasureSpec((newHeight - this.mPaddingTop) - this.mPaddingBottom, 1073741824));
        }
        setMeasuredDimension(newWidth, newHeight);
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean changed, int l, int t, int r, int b) {
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            getChildAt(i).layout(this.mPaddingLeft, this.mPaddingTop, (r - l) - this.mPaddingRight, (b - t) - this.mPaddingBottom);
        }
    }

    /* access modifiers changed from: protected */
    public void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.mBackgroundRect.set(0, 0, w, h);
        this.mForegroundRect.set(this.mForegroundPadding, this.mForegroundPadding, w - (this.mForegroundPadding * 2), h - (this.mForegroundPadding * 2));
    }

    /* access modifiers changed from: protected */
    public void setChildrenDrawingCacheEnabled(boolean enabled) {
        this.mChildren.setChildrenDrawingCacheEnabled(enabled);
    }

    /* access modifiers changed from: protected */
    public void setChildrenDrawnWithCacheEnabled(boolean enabled) {
        this.mChildren.setChildrenDrawnWithCacheEnabled(enabled);
    }

    public float getBackgroundAlpha() {
        return this.mBackgroundAlpha;
    }

    public void setFastBackgroundAlpha(float alpha) {
        this.mBackgroundAlpha = alpha;
    }

    public void setBackgroundAlphaMultiplier(float multiplier) {
        this.mBackgroundAlphaMultiplier = multiplier;
    }

    public float getBackgroundAlphaMultiplier() {
        return this.mBackgroundAlphaMultiplier;
    }

    public void setBackgroundAlpha(float alpha) {
        this.mBackgroundAlpha = alpha;
        invalidate();
    }

    /* access modifiers changed from: protected */
    public boolean onSetAlpha(int alpha) {
        return true;
    }

    public void setAlpha(float alpha) {
        setChildrenAlpha(alpha);
        super.setAlpha(alpha);
    }

    private void setChildrenAlpha(float alpha) {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            getChildAt(i).setAlpha(alpha);
        }
    }

    public View getChildAt(int x, int y) {
        return this.mChildren.getChildAt(x, y);
    }

    public boolean animateChildToPosition(final View child, int cellX, int cellY, int duration, int delay) {
        CellLayoutChildren clc = getChildrenLayout();
        if (clc.indexOfChild(child) == -1 || this.mOccupied[cellX][cellY]) {
            return false;
        }
        final LayoutParams lp = (LayoutParams) child.getLayoutParams();
        ItemInfo info = (ItemInfo) child.getTag();
        if (this.mReorderAnimators.containsKey(lp)) {
            ((ObjectAnimator) this.mReorderAnimators.get(lp)).cancel();
            this.mReorderAnimators.remove(lp);
        }
        int oldX = lp.x;
        int oldY = lp.y;
        this.mOccupied[lp.cellX][lp.cellY] = false;
        this.mOccupied[cellX][cellY] = true;
        lp.isLockedToGrid = true;
        info.cellX = cellX;
        lp.cellX = cellX;
        info.cellY = cellY;
        lp.cellY = cellY;
        clc.setupLp(lp);
        lp.isLockedToGrid = false;
        int newX = lp.x;
        int newY = lp.y;
        lp.x = oldX;
        lp.y = oldY;
        child.requestLayout();
        ObjectAnimator oa = ObjectAnimator.ofPropertyValuesHolder(lp, new PropertyValuesHolder[]{PropertyValuesHolder.ofInt("x", new int[]{oldX, newX}), PropertyValuesHolder.ofInt("y", new int[]{oldY, newY})});
        oa.setDuration((long) duration);
        this.mReorderAnimators.put(lp, oa);
        oa.addUpdateListener(new AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                child.requestLayout();
            }
        });
        oa.addListener(new AnimatorListenerAdapter() {
            boolean cancelled = false;

            public void onAnimationEnd(Animator animation) {
                if (!this.cancelled) {
                    lp.isLockedToGrid = true;
                }
                if (CellLayout.this.mReorderAnimators.containsKey(lp)) {
                    CellLayout.this.mReorderAnimators.remove(lp);
                }
            }

            public void onAnimationCancel(Animator animation) {
                this.cancelled = true;
            }
        });
        oa.setStartDelay((long) delay);
        oa.start();
        return true;
    }

    /* access modifiers changed from: 0000 */
    public void estimateDropCell(int originX, int originY, int spanX, int spanY, int[] result) {
        int countX = this.mCountX;
        int countY = this.mCountY;
        pointToCellRounded(originX, originY, result);
        int rightOverhang = (result[0] + spanX) - countX;
        if (rightOverhang > 0) {
            result[0] = result[0] - rightOverhang;
        }
        result[0] = Math.max(0, result[0]);
        int bottomOverhang = (result[1] + spanY) - countY;
        if (bottomOverhang > 0) {
            result[1] = result[1] - bottomOverhang;
        }
        result[1] = Math.max(0, result[1]);
    }

    /* access modifiers changed from: 0000 */
    public void visualizeDropLocation(View v, Bitmap dragOutline, int originX, int originY, int spanX, int spanY, Point dragOffset, Rect dragRegion) {
        int left;
        int top;
        int oldDragCellX = this.mDragCell[0];
        int oldDragCellY = this.mDragCell[1];
        int[] nearest = findNearestVacantArea(originX, originY, spanX, spanY, v, this.mDragCell);
        if (v == null || dragOffset != null) {
            this.mDragCenter.set(originX, originY);
        } else {
            this.mDragCenter.set((v.getWidth() / 2) + originX, (v.getHeight() / 2) + originY);
        }
        if (dragOutline != null || v != null) {
            if (!(nearest == null || (nearest[0] == oldDragCellX && nearest[1] == oldDragCellY))) {
                int[] topLeft = this.mTmpPoint;
                cellToPoint(nearest[0], nearest[1], topLeft);
                int left2 = topLeft[0];
                int top2 = topLeft[1];
                if (v != null && dragOffset == null) {
                    MarginLayoutParams lp = (MarginLayoutParams) v.getLayoutParams();
                    top = top2 + lp.topMargin + ((v.getHeight() - dragOutline.getHeight()) / 2);
                    left = left2 + lp.leftMargin + ((((this.mCellWidth * spanX) + ((spanX - 1) * this.mWidthGap)) - dragOutline.getWidth()) / 2);
                } else if (dragOffset == null || dragRegion == null) {
                    left = left2 + ((((this.mCellWidth * spanX) + ((spanX - 1) * this.mWidthGap)) - dragOutline.getWidth()) / 2);
                    top = top2 + ((((this.mCellHeight * spanY) + ((spanY - 1) * this.mHeightGap)) - dragOutline.getHeight()) / 2);
                } else {
                    left = left2 + dragOffset.x + ((((this.mCellWidth * spanX) + ((spanX - 1) * this.mWidthGap)) - dragRegion.width()) / 2);
                    top = top2 + dragOffset.y;
                }
                int oldIndex = this.mDragOutlineCurrent;
                this.mDragOutlineAnims[oldIndex].animateOut();
                this.mDragOutlineCurrent = (oldIndex + 1) % this.mDragOutlines.length;
                this.mDragOutlines[this.mDragOutlineCurrent].set(left, top);
                this.mDragOutlineAnims[this.mDragOutlineCurrent].setTag(dragOutline);
                this.mDragOutlineAnims[this.mDragOutlineCurrent].animateIn();
            }
            if (this.mCrosshairsDrawable != null) {
                invalidate();
            }
        } else if (this.mCrosshairsDrawable != null) {
            invalidate();
        }
    }

    public void clearDragOutlines() {
        this.mDragOutlineAnims[this.mDragOutlineCurrent].animateOut();
        this.mDragCell[0] = -1;
        this.mDragCell[1] = -1;
    }

    /* access modifiers changed from: 0000 */
    public int[] findNearestVacantArea(int pixelX, int pixelY, int spanX, int spanY, int[] result) {
        return findNearestVacantArea(pixelX, pixelY, spanX, spanY, null, result);
    }

    /* access modifiers changed from: 0000 */
    public int[] findNearestArea(int pixelX, int pixelY, int spanX, int spanY, View ignoreView, boolean ignoreOccupied, int[] result) {
        markCellsAsUnoccupiedForView(ignoreView);
        int pixelX2 = (int) (((float) pixelX) - (((float) ((this.mCellWidth + this.mWidthGap) * (spanX - 1))) / 2.0f));
        int pixelY2 = (int) (((float) pixelY) - (((float) ((this.mCellHeight + this.mHeightGap) * (spanY - 1))) / 2.0f));
        int[] bestXY = result != null ? result : new int[2];
        double bestDistance = Double.MAX_VALUE;
        int countX = this.mCountX;
        int countY = this.mCountY;
        boolean[][] occupied = this.mOccupied;
        for (int y = 0; y < countY - (spanY - 1); y++) {
            int x = 0;
            while (x < countX - (spanX - 1)) {
                if (ignoreOccupied) {
                    int i = 0;
                    while (true) {
                        if (i >= spanX) {
                            break;
                        }
                        for (int j = 0; j < spanY; j++) {
                            if (occupied[x + i][y + j]) {
                                x += i;
                                break;
                            }
                        }
                        i++;
                    }
                }
                int[] cellXY = this.mTmpXY;
                cellToCenterPoint(x, y, cellXY);
                double distance = Math.sqrt(Math.pow((double) (cellXY[0] - pixelX2), 2.0d) + Math.pow((double) (cellXY[1] - pixelY2), 2.0d));
                if (distance <= bestDistance) {
                    bestDistance = distance;
                    bestXY[0] = x;
                    bestXY[1] = y;
                }
                x++;
            }
        }
        markCellsAsOccupiedForView(ignoreView);
        if (bestDistance == Double.MAX_VALUE) {
            bestXY[0] = -1;
            bestXY[1] = -1;
        }
        return bestXY;
    }

    /* access modifiers changed from: 0000 */
    public int[] findNearestVacantArea(int pixelX, int pixelY, int spanX, int spanY, View ignoreView, int[] result) {
        return findNearestArea(pixelX, pixelY, spanX, spanY, ignoreView, true, result);
    }

    /* access modifiers changed from: 0000 */
    public int[] findNearestArea(int pixelX, int pixelY, int spanX, int spanY, int[] result) {
        return findNearestArea(pixelX, pixelY, spanX, spanY, null, false, result);
    }

    /* access modifiers changed from: 0000 */
    public boolean existsEmptyCell() {
        return findCellForSpan(null, 1, 1);
    }

    /* access modifiers changed from: 0000 */
    public int findCellLeftCount() {
        int count = 0;
        for (int x = 0; x < this.mCountX; x++) {
            for (int y = 0; y < this.mCountY; y++) {
                if (this.mOccupied[x][y]) {
                    count++;
                }
            }
        }
        return count;
    }

    /* access modifiers changed from: 0000 */
    public boolean findCellForSpan(int[] cellXY, int spanX, int spanY) {
        return findCellForSpanThatIntersectsIgnoring(cellXY, spanX, spanY, -1, -1, null);
    }

    /* access modifiers changed from: 0000 */
    public boolean findCellForFolderSpan(int[] cellXY, int spanX, int spanY) {
        return findCellForFolderSpanThatIntersectsIgnoring(cellXY, spanX, spanY, -1, -1, null);
    }

    /* access modifiers changed from: 0000 */
    public boolean findCellForSpanIgnoring(int[] cellXY, int spanX, int spanY, View ignoreView) {
        return findCellForSpanThatIntersectsIgnoring(cellXY, spanX, spanY, -1, -1, ignoreView);
    }

    /* access modifiers changed from: 0000 */
    public boolean findCellForSpanThatIntersects(int[] cellXY, int spanX, int spanY, int intersectX, int intersectY) {
        return findCellForSpanThatIntersectsIgnoring(cellXY, spanX, spanY, intersectX, intersectY, null);
    }

    /* access modifiers changed from: 0000 */
    public boolean findCellForFolderSpanThatIntersectsIgnoring(int[] cellXY, int spanX, int spanY, int intersectX, int intersectY, View ignoreView) {
        markCellsAsUnoccupiedForView(ignoreView);
        boolean foundCell = false;
        while (true) {
            int endX = this.mCountX - (spanX - 1);
            int endY = this.mCountY - (spanY - 1);
            System.out.println("[wjx]endX =  " + endX + "endY = " + endY + "mcountx" + this.mCountX + " " + this.mCountY);
            for (int y = 0; y < endY && !foundCell; y++) {
                int x = 0;
                while (true) {
                    if (x >= endX) {
                        break;
                    }
                    for (int i = 0; i < spanX; i++) {
                        int j = 0;
                        while (j < spanY) {
                            if (this.mOccupied[x + i][y + j]) {
                                x = x + i + 1;
                            } else {
                                j++;
                            }
                        }
                    }
                    if (cellXY != null) {
                        cellXY[0] = x;
                        cellXY[1] = y;
                    }
                    foundCell = true;
                }
            }
            if (intersectX == -1 && intersectY == -1) {
                markCellsAsOccupiedForView(ignoreView);
                return foundCell;
            }
            intersectX = -1;
            intersectY = -1;
        }
    }

    /* access modifiers changed from: 0000 */
    public boolean findCellForSpanThatIntersectsIgnoring(int[] cellXY, int spanX, int spanY, int intersectX, int intersectY, View ignoreView) {
        markCellsAsUnoccupiedForView(ignoreView);
        boolean foundCell = false;
        while (true) {
            int startX = 0;
            if (intersectX >= 0) {
                startX = Math.max(0, intersectX - (spanX - 1));
            }
            int endX = this.mCountX - (spanX - 1);
            if (intersectX >= 0) {
                endX = Math.min(endX, (spanX == 1 ? 1 : 0) + intersectX + (spanX - 1));
            }
            int startY = 0;
            if (intersectY >= 0) {
                startY = Math.max(0, intersectY - (spanY - 1));
            }
            int endY = this.mCountY - (spanY - 1);
            if (intersectY >= 0) {
                endY = Math.min(endY, (spanY == 1 ? 1 : 0) + intersectY + (spanY - 1));
            }
            for (int y = startY; y < endY && !foundCell; y++) {
                int x = startX;
                while (true) {
                    if (x >= endX) {
                        break;
                    }
                    for (int i = 0; i < spanX; i++) {
                        int j = 0;
                        while (j < spanY) {
                            if (this.mOccupied[x + i][y + j]) {
                                x = x + i + 1;
                            } else {
                                j++;
                            }
                        }
                    }
                    if (cellXY != null) {
                        cellXY[0] = x;
                        cellXY[1] = y;
                    }
                    foundCell = true;
                }
            }
            if (intersectX == -1 && intersectY == -1) {
                markCellsAsOccupiedForView(ignoreView);
                return foundCell;
            }
            intersectX = -1;
            intersectY = -1;
        }
    }

    /* access modifiers changed from: 0000 */
    public void onDragEnter() {
        if (!this.mDragging && this.mCrosshairsAnimator != null) {
            this.mCrosshairsAnimator.animateIn();
        }
        this.mDragging = true;
    }

    /* access modifiers changed from: 0000 */
    public void onDragExit() {
        if (this.mDragging) {
            this.mDragging = false;
            if (this.mCrosshairsAnimator != null) {
                this.mCrosshairsAnimator.animateOut();
            }
        }
        this.mDragCell[0] = -1;
        this.mDragCell[1] = -1;
        this.mDragOutlineAnims[this.mDragOutlineCurrent].animateOut();
        this.mDragOutlineCurrent = (this.mDragOutlineCurrent + 1) % this.mDragOutlineAnims.length;
        setIsDragOverlapping(false);
    }

    /* access modifiers changed from: 0000 */
    public void onDropChild(View child) {
        if (child != null) {
            ((LayoutParams) child.getLayoutParams()).dropped = true;
            child.requestLayout();
        }
    }

    public void cellToRect(int cellX, int cellY, int cellHSpan, int cellVSpan, RectF resultRect) {
        int cellWidth = this.mCellWidth;
        int cellHeight = this.mCellHeight;
        int widthGap = this.mWidthGap;
        int heightGap = this.mHeightGap;
        int x = getPaddingLeft() + ((cellWidth + widthGap) * cellX);
        int y = getPaddingTop() + ((cellHeight + heightGap) * cellY);
        RectF rectF = resultRect;
        rectF.set((float) x, (float) y, (float) (x + (cellHSpan * cellWidth) + ((cellHSpan - 1) * widthGap)), (float) (y + (cellVSpan * cellHeight) + ((cellVSpan - 1) * heightGap)));
    }

    public int[] rectToCell(int width, int height, int[] result) {
        return rectToCell(getResources(), width, height, result);
    }

    public static int[] rectToCell(Resources resources, int width, int height, int[] result) {
        int smallerSize = Math.min(resources.getDimensionPixelSize(R.dimen.workspace_cell_width), resources.getDimensionPixelSize(R.dimen.workspace_cell_height));
        int spanX = (int) Math.ceil((double) (((float) width) / ((float) smallerSize)));
        int spanY = (int) Math.ceil((double) (((float) height) / ((float) smallerSize)));
        if (result == null) {
            return new int[]{spanX, spanY};
        }
        result[0] = spanX;
        result[1] = spanY;
        return result;
    }

    public int[] cellSpansToSize(int hSpans, int vSpans) {
        return new int[]{(this.mCellWidth * hSpans) + ((hSpans - 1) * this.mWidthGap), (this.mCellHeight * vSpans) + ((vSpans - 1) * this.mHeightGap)};
    }

    public void calculateSpans(ItemInfo info) {
        int minWidth;
        int minHeight;
        if (info instanceof LauncherAppWidgetInfo) {
            minWidth = ((LauncherAppWidgetInfo) info).minWidth;
            minHeight = ((LauncherAppWidgetInfo) info).minHeight;
        } else if (info instanceof PendingAddWidgetInfo) {
            minWidth = ((PendingAddWidgetInfo) info).minWidth;
            minHeight = ((PendingAddWidgetInfo) info).minHeight;
        } else {
            info.spanY = 1;
            info.spanX = 1;
            return;
        }
        int[] spans = rectToCell(minWidth, minHeight, null);
        info.spanX = spans[0];
        info.spanY = spans[1];
    }

    public boolean getVacantCell(int[] vacant, int spanX, int spanY) {
        return findVacantCell(vacant, spanX, spanY, this.mCountX, this.mCountY, this.mOccupied);
    }

    static boolean findVacantCell(int[] vacant, int spanX, int spanY, int xCount, int yCount, boolean[][] occupied) {
        boolean available;
        boolean available2;
        int y = 0;
        while (y < yCount) {
            int x = 0;
            while (x < xCount) {
                if (occupied[x][y]) {
                    available = false;
                } else {
                    available = true;
                }
                for (int i = x; i < (x + spanX) - 1 && x < xCount; i++) {
                    for (int j = y; j < (y + spanY) - 1 && y < yCount; j++) {
                        if (!available2 || occupied[i][j]) {
                            available2 = false;
                        } else {
                            available2 = true;
                        }
                        if (!available2) {
                            break;
                        }
                    }
                }
                if (available2) {
                    vacant[0] = x;
                    vacant[1] = y;
                    return true;
                }
                x++;
            }
            y++;
        }
        return false;
    }

    private void clearOccupiedCells() {
        for (int x = 0; x < this.mCountX; x++) {
            for (int y = 0; y < this.mCountY; y++) {
                this.mOccupied[x][y] = false;
            }
        }
    }

    public void getExpandabilityArrayForView(View view, int[] expandability) {
        LayoutParams lp = (LayoutParams) view.getLayoutParams();
        expandability[0] = 0;
        for (int x = lp.cellX - 1; x >= 0; x--) {
            boolean flag = false;
            for (int y = lp.cellY; y < lp.cellY + lp.cellVSpan; y++) {
                if (this.mOccupied[x][y]) {
                    flag = true;
                }
            }
            if (flag) {
                break;
            }
            expandability[0] = expandability[0] + 1;
        }
        expandability[1] = 0;
        for (int y2 = lp.cellY - 1; y2 >= 0; y2--) {
            boolean flag2 = false;
            for (int x2 = lp.cellX; x2 < lp.cellX + lp.cellHSpan; x2++) {
                if (this.mOccupied[x2][y2]) {
                    flag2 = true;
                }
            }
            if (flag2) {
                break;
            }
            expandability[1] = expandability[1] + 1;
        }
        expandability[2] = 0;
        for (int x3 = lp.cellX + lp.cellHSpan; x3 < this.mCountX; x3++) {
            boolean flag3 = false;
            for (int y3 = lp.cellY; y3 < lp.cellY + lp.cellVSpan; y3++) {
                if (this.mOccupied[x3][y3]) {
                    flag3 = true;
                }
            }
            if (flag3) {
                break;
            }
            expandability[2] = expandability[2] + 1;
        }
        expandability[3] = 0;
        int y4 = lp.cellY + lp.cellVSpan;
        while (y4 < this.mCountY) {
            boolean flag4 = false;
            for (int x4 = lp.cellX; x4 < lp.cellX + lp.cellHSpan; x4++) {
                if (this.mOccupied[x4][y4]) {
                    flag4 = true;
                }
            }
            if (!flag4) {
                expandability[3] = expandability[3] + 1;
                y4++;
            } else {
                return;
            }
        }
    }

    public void onMove(View view, int newCellX, int newCellY) {
        LayoutParams lp = (LayoutParams) view.getLayoutParams();
        markCellsAsUnoccupiedForView(view);
        markCellsForView(newCellX, newCellY, lp.cellHSpan, lp.cellVSpan, true);
    }

    public void markCellsAsOccupiedForView(View view) {
        if (view != null && view.getParent() == this.mChildren) {
            LayoutParams lp = (LayoutParams) view.getLayoutParams();
            markCellsForView(lp.cellX, lp.cellY, lp.cellHSpan, lp.cellVSpan, true);
        }
    }

    public void markCellsAsUnoccupiedForView(View view) {
        if (view != null && view.getParent() == this.mChildren) {
            LayoutParams lp = (LayoutParams) view.getLayoutParams();
            markCellsForView(lp.cellX, lp.cellY, lp.cellHSpan, lp.cellVSpan, false);
        }
    }

    private void markCellsForView(int cellX, int cellY, int spanX, int spanY, boolean value) {
        int x = cellX;
        while (x < cellX + spanX && x < this.mCountX) {
            int y = cellY;
            while (y < cellY + spanY && y < this.mCountY) {
                this.mOccupied[x][y] = value;
                y++;
            }
            x++;
        }
    }

    public int getDesiredWidth() {
        return this.mPaddingLeft + this.mPaddingRight + (this.mCountX * this.mCellWidth) + (Math.max(this.mCountX - 1, 0) * this.mWidthGap);
    }

    public int getDesiredHeight() {
        return this.mPaddingTop + this.mPaddingBottom + (this.mCountY * this.mCellHeight) + (Math.max(this.mCountY - 1, 0) * this.mHeightGap);
    }

    public boolean isOccupied(int x, int y) {
        if (x < this.mCountX && y < this.mCountY) {
            return this.mOccupied[x][y];
        }
        throw new RuntimeException("Position exceeds the bound of this CellLayout");
    }

    public android.view.ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    /* access modifiers changed from: protected */
    public boolean checkLayoutParams(android.view.ViewGroup.LayoutParams p) {
        return p instanceof LayoutParams;
    }

    /* access modifiers changed from: protected */
    public android.view.ViewGroup.LayoutParams generateLayoutParams(android.view.ViewGroup.LayoutParams p) {
        return new LayoutParams(p);
    }

    public boolean lastDownOnOccupiedCell() {
        return this.mLastDownOnOccupiedCell;
    }

    public boolean requestFocus(int direction, Rect previouslyFocusedRect) {
        if (!(getParent() instanceof Workspace)) {
            return super.requestFocus(direction, previouslyFocusedRect);
        }
        if (!((Workspace) getParent()).isSearchScreen(this) || !(this.mChildren.getChildAt(0) instanceof LauncherAppWidgetHostView)) {
            return super.requestFocus(direction, previouslyFocusedRect);
        }
        return ((LauncherAppWidgetHostView) this.mChildren.getChildAt(0)).requestChildFocus();
    }
}
