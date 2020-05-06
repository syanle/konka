package com.cyanogenmod.trebuchet;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ActionMode;
import android.view.ActionMode.Callback;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.View.BaseSavedState;
import android.view.View.MeasureSpec;
import android.view.View.OnLongClickListener;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.animation.Interpolator;
import android.widget.Checkable;
import android.widget.Scroller;
import com.cyanogenmod.trebuchet.RocketLauncher.Board.FlyingIcon;
import com.konka.ios7launcher.R;
import com.konka.kkinterface.tv.ChannelDesk;
import java.util.ArrayList;
import java.util.Iterator;

public abstract class PagedView extends ViewGroup {
    protected static final int CHOICE_MODE_MULTIPLE = 2;
    protected static final int CHOICE_MODE_NONE = 0;
    protected static final int CHOICE_MODE_SINGLE = 1;
    private static final boolean DEBUG = false;
    protected static final int INVALID_PAGE = -1;
    protected static final int INVALID_POINTER = -1;
    private static final int MINIMUM_SNAP_VELOCITY = 2200;
    private static final int MIN_FLING_VELOCITY = 250;
    private static final int MIN_LENGTH_FOR_FLING = 25;
    private static final float OVERSCROLL_ACCELERATE_FACTOR = 2.0f;
    private static final float OVERSCROLL_DAMP_FACTOR = 0.14f;
    private static final int PAGE_SNAP_ANIMATION_DURATION = 300;
    private static final float RETURN_TO_ORIGINAL_PAGE_THRESHOLD = 0.33f;
    private static final float SIGNIFICANT_MOVE_THRESHOLD = 0.4f;
    private static final String TAG = "PagedView";
    protected static final int TOUCH_STATE_NEXT_PAGE = 3;
    protected static final int TOUCH_STATE_PREV_PAGE = 2;
    protected static final int TOUCH_STATE_REST = 0;
    protected static final int TOUCH_STATE_SCROLLING = 1;
    protected static final int sScrollIndicatorFadeInDuration = 150;
    protected static final int sScrollIndicatorFadeOutDuration = 650;
    protected static final int sScrollIndicatorFadeOutShortDuration = 150;
    protected static final int sScrollIndicatorFlashDuration = 650;
    Runnable hideScrollingIndicatorRunnable;
    private ActionMode mActionMode;
    protected int mActivePointerId;
    protected boolean mAllowLongPress;
    protected boolean mAllowOverScroll;
    protected int mCellCountX;
    protected int mCellCountY;
    protected boolean mCenterPagesVertically;
    private int[] mChildOffsets;
    private int[] mChildOffsetsWithLayoutScale;
    private int[] mChildRelativeOffsets;
    protected int mChoiceMode;
    protected boolean mClearDirtyPages;
    protected boolean mContentIsRefreshable;
    protected int mCurrentPage;
    private boolean mDeferLoadAssociatedPagesUntilScrollCompletes;
    protected boolean mDeferScrollUpdate;
    protected float mDensity;
    protected ArrayList<Boolean> mDirtyPageContent;
    private float mDownMotionX;
    protected boolean mFadeInAdjacentScreens;
    protected boolean mFirstLayout;
    protected boolean mForceScreenScrolled;
    protected boolean mHandleFadeInAdjacentScreens;
    private boolean mHasScrollIndicator;
    protected boolean mIsDataReady;
    protected boolean mIsPageMoving;
    protected float mLastMotionX;
    protected float mLastMotionXRemainder;
    protected float mLastMotionY;
    private int mLastScreenScroll;
    protected float mLayoutScale;
    protected OnLongClickListener mLongClickListener;
    protected int mMaxScrollX;
    private int mMaximumVelocity;
    private int mMinimumWidth;
    protected int mNextPage;
    protected int mOrigPaddingTop;
    protected int mOverScrollX;
    protected int mPageLayoutHeightGap;
    protected int mPageLayoutPaddingBottom;
    protected int mPageLayoutPaddingLeft;
    protected int mPageLayoutPaddingRight;
    protected int mPageLayoutPaddingTop;
    protected int mPageLayoutWidthGap;
    protected int mPageSpacing;
    private PageSwitchListener mPageSwitchListener;
    private int mPagingTouchSlop;
    private View mScrollIndicator;
    protected Scroller mScroller;
    protected int mSnapVelocity;
    protected int[] mTempVisiblePagesRange;
    protected float mTotalMotionX;
    protected int mTouchSlop;
    protected int mTouchState;
    protected float mTouchX;
    protected int mUnboundedScrollX;
    protected boolean mUsePagingTouchSlop;
    private VelocityTracker mVelocityTracker;

    public interface PageSwitchListener {
        void onPageSwitch(View view, int i);
    }

    public static class SavedState extends BaseSavedState {
        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in, null);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
        int currentPage;

        SavedState(Parcelable superState) {
            super(superState);
            this.currentPage = -1;
        }

        /* synthetic */ SavedState(Parcel parcel, SavedState savedState) {
            this(parcel);
        }

        private SavedState(Parcel in) {
            super(in);
            this.currentPage = -1;
            this.currentPage = in.readInt();
        }

        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(this.currentPage);
        }
    }

    private static class ScrollInterpolator implements Interpolator {
        public float getInterpolation(float t) {
            float t2 = t - 1.0f;
            return (t2 * t2 * t2 * t2 * t2) + 1.0f;
        }
    }

    public abstract void syncPageItems(int i, boolean z);

    public abstract void syncPages();

    public PagedView(Context context) {
        this(context, null, 0);
    }

    public PagedView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PagedView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mSnapVelocity = 500;
        this.mFirstLayout = true;
        this.mNextPage = -1;
        this.mLastScreenScroll = -1;
        this.mTouchState = 0;
        this.mForceScreenScrolled = false;
        this.mAllowLongPress = true;
        this.mCellCountX = 0;
        this.mCellCountY = 0;
        this.mAllowOverScroll = true;
        this.mTempVisiblePagesRange = new int[2];
        this.mLayoutScale = 1.0f;
        this.mActivePointerId = -1;
        this.mContentIsRefreshable = true;
        this.mFadeInAdjacentScreens = true;
        this.mHandleFadeInAdjacentScreens = false;
        this.mUsePagingTouchSlop = true;
        this.mDeferScrollUpdate = false;
        this.mIsPageMoving = false;
        this.mIsDataReady = false;
        this.mClearDirtyPages = false;
        this.mHasScrollIndicator = true;
        this.hideScrollingIndicatorRunnable = new Runnable() {
            public void run() {
                PagedView.this.hideScrollingIndicator(false);
            }
        };
        this.mOrigPaddingTop = this.mPaddingTop;
        this.mChoiceMode = 0;
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.PagedView, defStyle, 0);
        setPageSpacing(a.getDimensionPixelSize(6, 0));
        this.mPageLayoutPaddingTop = a.getDimensionPixelSize(2, 0);
        this.mPageLayoutPaddingBottom = a.getDimensionPixelSize(3, 0);
        this.mPageLayoutPaddingLeft = a.getDimensionPixelSize(4, 0);
        this.mPageLayoutPaddingRight = a.getDimensionPixelSize(5, 0);
        this.mPageLayoutWidthGap = a.getDimensionPixelSize(0, 0);
        this.mPageLayoutHeightGap = a.getDimensionPixelSize(1, 0);
        a.recycle();
        setHapticFeedbackEnabled(false);
        init();
    }

    /* access modifiers changed from: protected */
    public void init() {
        this.mDirtyPageContent = new ArrayList<>();
        this.mDirtyPageContent.ensureCapacity(32);
        this.mScroller = new Scroller(getContext(), new ScrollInterpolator());
        this.mCurrentPage = 0;
        this.mCenterPagesVertically = false;
        ViewConfiguration configuration = ViewConfiguration.get(getContext());
        this.mTouchSlop = configuration.getScaledTouchSlop();
        this.mPagingTouchSlop = configuration.getScaledPagingTouchSlop();
        this.mMaximumVelocity = configuration.getScaledMaximumFlingVelocity();
        this.mDensity = getResources().getDisplayMetrics().density;
    }

    public void setPageSwitchListener(PageSwitchListener pageSwitchListener) {
        this.mPageSwitchListener = pageSwitchListener;
        if (this.mPageSwitchListener != null) {
            this.mPageSwitchListener.onPageSwitch(getPageAt(this.mCurrentPage), this.mCurrentPage);
        }
    }

    /* access modifiers changed from: protected */
    public void setDataIsReady() {
        this.mIsDataReady = true;
    }

    /* access modifiers changed from: protected */
    public boolean isDataReady() {
        return this.mIsDataReady;
    }

    /* access modifiers changed from: 0000 */
    public int getCurrentPage() {
        return this.mCurrentPage;
    }

    /* access modifiers changed from: 0000 */
    public int getPageCount() {
        return getChildCount();
    }

    /* access modifiers changed from: 0000 */
    public View getPageAt(int index) {
        return getChildAt(index);
    }

    /* access modifiers changed from: protected */
    public int indexToPage(int index) {
        return index;
    }

    /* access modifiers changed from: protected */
    public void updateCurrentPageScroll() {
        int newX = getChildOffset(this.mCurrentPage) - getRelativeChildOffset(this.mCurrentPage);
        scrollTo(newX, 0);
        this.mScroller.setFinalX(newX);
    }

    /* access modifiers changed from: 0000 */
    public void setCurrentPage(int currentPage) {
        if (!this.mScroller.isFinished()) {
            this.mScroller.abortAnimation();
        }
        if (getChildCount() != 0) {
            this.mCurrentPage = Math.max(0, Math.min(currentPage, getPageCount() - 1));
            updateCurrentPageScroll();
            updateScrollingIndicator();
            notifyPageSwitchListener();
            invalidate();
        }
    }

    /* access modifiers changed from: protected */
    public void notifyPageSwitchListener() {
        if (this.mPageSwitchListener != null) {
            this.mPageSwitchListener.onPageSwitch(getPageAt(this.mCurrentPage), this.mCurrentPage);
        }
    }

    /* access modifiers changed from: protected */
    public void pageBeginMoving() {
        if (!this.mIsPageMoving) {
            this.mIsPageMoving = true;
            onPageBeginMoving();
        }
    }

    /* access modifiers changed from: protected */
    public void pageEndMoving() {
        if (this.mIsPageMoving) {
            this.mIsPageMoving = false;
            onPageEndMoving();
        }
    }

    /* access modifiers changed from: protected */
    public boolean isPageMoving() {
        return this.mIsPageMoving;
    }

    /* access modifiers changed from: protected */
    public void onPageBeginMoving() {
        showScrollingIndicator(false);
    }

    /* access modifiers changed from: protected */
    public void onPageEndMoving() {
        hideScrollingIndicator(false);
    }

    public void setOnLongClickListener(OnLongClickListener l) {
        this.mLongClickListener = l;
        int count = getPageCount();
        for (int i = 0; i < count; i++) {
            getPageAt(i).setOnLongClickListener(l);
        }
    }

    public void scrollBy(int x, int y) {
        scrollTo(this.mUnboundedScrollX + x, this.mScrollY + y);
    }

    public void scrollTo(int x, int y) {
        this.mUnboundedScrollX = x;
        if (x < 0) {
            super.scrollTo(0, y);
            if (this.mAllowOverScroll) {
                overScroll((float) x);
            }
        } else if (x > this.mMaxScrollX) {
            super.scrollTo(this.mMaxScrollX, y);
            if (this.mAllowOverScroll) {
                overScroll((float) (x - this.mMaxScrollX));
            }
        } else {
            this.mOverScrollX = x;
            super.scrollTo(x, y);
        }
        this.mTouchX = (float) x;
    }

    public void computeScroll() {
        if (this.mScroller.computeScrollOffset()) {
            if (!(this.mScrollX == this.mScroller.getCurrX() && this.mScrollY == this.mScroller.getCurrY())) {
                scrollTo(this.mScroller.getCurrX(), this.mScroller.getCurrY());
            }
            invalidate();
        } else if (this.mNextPage != -1) {
            this.mCurrentPage = Math.max(0, Math.min(this.mNextPage, getPageCount() - 1));
            this.mNextPage = -1;
            notifyPageSwitchListener();
            if (this.mDeferLoadAssociatedPagesUntilScrollCompletes) {
                loadAssociatedPages(this.mCurrentPage);
                this.mDeferLoadAssociatedPagesUntilScrollCompletes = false;
            }
            if (this.mTouchState == 0) {
                pageEndMoving();
            }
            if (AccessibilityManager.getInstance(getContext()).isEnabled()) {
                AccessibilityEvent ev = AccessibilityEvent.obtain(4096);
                ev.getText().add(getCurrentPageDescription());
                sendAccessibilityEventUnchecked(ev);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int childWidthMode;
        int childHeightMode;
        if (!this.mIsDataReady) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            return;
        }
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        if (widthMode != 1073741824) {
            throw new IllegalStateException("Workspace can only be used in EXACTLY mode.");
        }
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int maxChildHeight = 0;
        int verticalPadding = this.mOrigPaddingTop + this.mPaddingBottom;
        int horizontalPadding = this.mPaddingLeft + this.mPaddingRight;
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getPageAt(i);
            LayoutParams lp = child.getLayoutParams();
            if (lp.width == -2) {
                childWidthMode = Integer.MIN_VALUE;
            } else {
                childWidthMode = 1073741824;
            }
            if (lp.height == -2) {
                childHeightMode = Integer.MIN_VALUE;
            } else {
                childHeightMode = 1073741824;
            }
            child.measure(MeasureSpec.makeMeasureSpec(widthSize - horizontalPadding, childWidthMode), MeasureSpec.makeMeasureSpec(heightSize - verticalPadding, childHeightMode));
            maxChildHeight = Math.max(maxChildHeight, child.getMeasuredHeight());
        }
        if (heightMode == Integer.MIN_VALUE) {
            heightSize = maxChildHeight + verticalPadding;
        }
        setMeasuredDimension(widthSize, heightSize);
        invalidateCachedOffsets();
        if (isScrollingIndicatorEnabled() && this.mScrollIndicator != null) {
            try {
                ((ScrollIndicator) this.mScrollIndicator).update();
            } catch (ClassCastException e) {
                Log.e(TAG, "scroll indicator should implement ScrollIndicator interface!", e);
            }
        }
        if (childCount > 0) {
            this.mMaxScrollX = getChildOffset(childCount - 1) - getRelativeChildOffset(childCount - 1);
        } else {
            this.mMaxScrollX = 0;
        }
    }

    /* access modifiers changed from: protected */
    public void scrollToNewPageWithoutMovingPages(int newCurrentPage) {
        int delta = (getChildOffset(newCurrentPage) - getRelativeChildOffset(newCurrentPage)) - this.mScrollX;
        int pageCount = getChildCount();
        for (int i = 0; i < pageCount; i++) {
            View page = getPageAt(i);
            page.setX(page.getX() + ((float) delta));
        }
        setCurrentPage(newCurrentPage);
    }

    public void setLayoutScale(float childrenScale) {
        this.mLayoutScale = childrenScale;
        invalidateCachedOffsets();
        int childCount = getChildCount();
        float[] childrenX = new float[childCount];
        float[] childrenY = new float[childCount];
        for (int i = 0; i < childCount; i++) {
            View child = getPageAt(i);
            childrenX[i] = child.getX();
            childrenY[i] = child.getY();
        }
        int widthSpec = MeasureSpec.makeMeasureSpec(getMeasuredWidth(), 1073741824);
        int heightSpec = MeasureSpec.makeMeasureSpec(getMeasuredHeight(), 1073741824);
        requestLayout();
        measure(widthSpec, heightSpec);
        layout(this.mLeft, this.mTop, this.mRight, this.mBottom);
        for (int i2 = 0; i2 < childCount; i2++) {
            View child2 = getPageAt(i2);
            child2.setX(childrenX[i2]);
            child2.setY(childrenY[i2]);
        }
        scrollToNewPageWithoutMovingPages(this.mCurrentPage);
    }

    public void setPageSpacing(int pageSpacing) {
        this.mPageSpacing = pageSpacing;
        invalidateCachedOffsets();
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean changed, int left, int top, int right, int bottom) {
        if (this.mIsDataReady) {
            int verticalPadding = this.mOrigPaddingTop + this.mPaddingBottom;
            int childCount = getChildCount();
            int childLeft = 0;
            if (childCount > 0) {
                childLeft = getRelativeChildOffset(0);
                if (this.mPageSpacing < 0) {
                    setPageSpacing(((right - left) - getChildAt(0).getMeasuredWidth()) / 2);
                }
            }
            for (int i = 0; i < childCount; i++) {
                View child = getPageAt(i);
                if (child.getVisibility() != 8) {
                    int childWidth = getScaledMeasuredWidth(child);
                    int childHeight = child.getMeasuredHeight();
                    int childTop = this.mOrigPaddingTop;
                    if (this.mCenterPagesVertically) {
                        childTop += ((getMeasuredHeight() - verticalPadding) - childHeight) / 2;
                    }
                    child.layout(childLeft, childTop, child.getMeasuredWidth() + childLeft, childTop + childHeight);
                    childLeft += this.mPageSpacing + childWidth;
                }
            }
            if (this.mFirstLayout && this.mCurrentPage >= 0 && this.mCurrentPage < getChildCount()) {
                setHorizontalScrollBarEnabled(false);
                int newX = getChildOffset(this.mCurrentPage) - getRelativeChildOffset(this.mCurrentPage);
                scrollTo(newX, 0);
                this.mScroller.setFinalX(newX);
                setHorizontalScrollBarEnabled(true);
                this.mFirstLayout = false;
            }
            if (this.mFirstLayout && this.mCurrentPage >= 0 && this.mCurrentPage < getChildCount()) {
                this.mFirstLayout = false;
            }
        }
    }

    /* access modifiers changed from: protected */
    public void screenScrolled(int screenScroll) {
        if (isScrollingIndicatorEnabled()) {
            updateScrollingIndicator();
        }
        if (this.mFadeInAdjacentScreens && !this.mHandleFadeInAdjacentScreens) {
            for (int i = 0; i < getChildCount(); i++) {
                View child = getChildAt(i);
                if (child != null) {
                    child.setAlpha(1.0f - Math.abs(getScrollProgress(screenScroll, child, i)));
                    child.invalidate();
                }
            }
            invalidate();
        }
    }

    /* access modifiers changed from: protected */
    public void onViewAdded(View child) {
        super.onViewAdded(child);
        this.mForceScreenScrolled = true;
        updateScrollingIndicator();
        invalidate();
        invalidateCachedOffsets();
    }

    public void removeViewAt(int index) {
        boolean removeCurrentPage;
        boolean needToScroll = false;
        if (getCurrentPage() == index) {
            removeCurrentPage = true;
        } else {
            removeCurrentPage = false;
        }
        if (getCurrentPage() >= index && index >= 0) {
            needToScroll = true;
        }
        updateScrollingIndicator();
        super.removeViewAt(index);
        invalidateCachedOffsets();
        if (needToScroll) {
            Log.d(TAG, "needToScroll");
            this.mForceScreenScrolled = true;
            this.mCurrentPage--;
            int destPage = this.mCurrentPage;
            if (!this.mScroller.isFinished()) {
                this.mScroller.abortAnimation();
            }
            this.mScroller.startScroll(this.mUnboundedScrollX, 0, (getChildOffset(this.mCurrentPage) - getRelativeChildOffset(this.mCurrentPage)) - this.mUnboundedScrollX, 0, 0);
            if (removeCurrentPage && !isInTouchMode()) {
                getPageAt(destPage).requestFocus();
            }
        }
    }

    /* access modifiers changed from: protected */
    public void invalidateCachedOffsets() {
        int count = getChildCount();
        if (count == 0) {
            this.mChildOffsets = null;
            this.mChildRelativeOffsets = null;
            this.mChildOffsetsWithLayoutScale = null;
            return;
        }
        this.mChildOffsets = new int[count];
        this.mChildRelativeOffsets = new int[count];
        this.mChildOffsetsWithLayoutScale = new int[count];
        for (int i = 0; i < count; i++) {
            this.mChildOffsets[i] = -1;
            this.mChildRelativeOffsets[i] = -1;
            this.mChildOffsetsWithLayoutScale[i] = -1;
        }
    }

    /* access modifiers changed from: protected */
    public int getChildOffset(int index) {
        int[] childOffsets = Float.compare(this.mLayoutScale, 1.0f) == 0 ? this.mChildOffsets : this.mChildOffsetsWithLayoutScale;
        if (childOffsets != null && index < childOffsets.length && childOffsets[index] != -1) {
            return childOffsets[index];
        }
        if (getChildCount() == 0 || childOffsets == null || index >= childOffsets.length) {
            return 0;
        }
        int offset = getRelativeChildOffset(0);
        for (int i = 0; i < index; i++) {
            offset += getScaledMeasuredWidth(getPageAt(i)) + this.mPageSpacing;
        }
        if (childOffsets == null) {
            return offset;
        }
        childOffsets[index] = offset;
        return offset;
    }

    /* access modifiers changed from: protected */
    public int getRelativeChildOffset(int index) {
        if (this.mChildRelativeOffsets != null && index < this.mChildRelativeOffsets.length && this.mChildRelativeOffsets[index] != -1) {
            return this.mChildRelativeOffsets[index];
        }
        int offset = this.mPaddingLeft + (((getMeasuredWidth() - (this.mPaddingLeft + this.mPaddingRight)) - getChildWidth(index)) / 2);
        if (this.mChildRelativeOffsets == null || index >= this.mChildRelativeOffsets.length) {
            return offset;
        }
        this.mChildRelativeOffsets[index] = offset;
        return offset;
    }

    /* access modifiers changed from: protected */
    public int getScaledRelativeChildOffset(int index) {
        return this.mPaddingLeft + (((getMeasuredWidth() - (this.mPaddingLeft + this.mPaddingRight)) - getScaledMeasuredWidth(getPageAt(index))) / 2);
    }

    /* access modifiers changed from: protected */
    public int getScaledMeasuredWidth(View child) {
        int maxWidth;
        if (child == null) {
            return 0;
        }
        int measuredWidth = child.getMeasuredWidth();
        int minWidth = this.mMinimumWidth;
        if (minWidth > measuredWidth) {
            maxWidth = minWidth;
        } else {
            maxWidth = measuredWidth;
        }
        return (int) ((((float) maxWidth) * this.mLayoutScale) + 0.5f);
    }

    /* access modifiers changed from: protected */
    public void getVisiblePages(int[] range) {
        int pageCount = getChildCount();
        if (pageCount > 0) {
            int pageWidth = getScaledMeasuredWidth(getPageAt(0));
            int screenWidth = getMeasuredWidth();
            int x = getScaledRelativeChildOffset(0) + pageWidth;
            int leftScreen = 0;
            while (x <= this.mScrollX && leftScreen < pageCount - 1) {
                leftScreen++;
                x += getScaledMeasuredWidth(getPageAt(leftScreen)) + this.mPageSpacing;
            }
            int rightScreen = leftScreen;
            while (x < this.mScrollX + screenWidth && rightScreen < pageCount - 1) {
                rightScreen++;
                x += getScaledMeasuredWidth(getPageAt(rightScreen)) + this.mPageSpacing;
            }
            range[0] = leftScreen;
            range[1] = rightScreen;
            return;
        }
        range[0] = -1;
        range[1] = -1;
    }

    /* access modifiers changed from: protected */
    public void dispatchDraw(Canvas canvas) {
        if (this.mOverScrollX != this.mLastScreenScroll || this.mForceScreenScrolled) {
            screenScrolled(this.mOverScrollX);
            this.mLastScreenScroll = this.mOverScrollX;
            this.mForceScreenScrolled = false;
        }
        if (getChildCount() > 0) {
            getVisiblePages(this.mTempVisiblePagesRange);
            int leftScreen = this.mTempVisiblePagesRange[0];
            int rightScreen = this.mTempVisiblePagesRange[1];
            if (leftScreen != -1 && rightScreen != -1) {
                long drawingTime = getDrawingTime();
                canvas.save();
                canvas.clipRect(this.mScrollX, this.mScrollY, (this.mScrollX + this.mRight) - this.mLeft, (this.mScrollY + this.mBottom) - this.mTop);
                for (int i = rightScreen; i >= leftScreen; i--) {
                    drawChild(canvas, getPageAt(i), drawingTime);
                }
                canvas.restore();
            }
        }
    }

    public boolean requestChildRectangleOnScreen(View child, Rect rectangle, boolean immediate) {
        int page = indexToPage(indexOfChild(child));
        if (page == this.mCurrentPage && this.mScroller.isFinished()) {
            return false;
        }
        snapToPage(page);
        return true;
    }

    /* access modifiers changed from: protected */
    public boolean onRequestFocusInDescendants(int direction, Rect previouslyFocusedRect) {
        int focusablePage;
        if (this.mNextPage != -1) {
            focusablePage = this.mNextPage;
        } else {
            focusablePage = this.mCurrentPage;
        }
        View v = getPageAt(focusablePage);
        if (v != null) {
            return v.requestFocus(direction, previouslyFocusedRect);
        }
        return false;
    }

    public boolean dispatchUnhandledMove(View focused, int direction) {
        if (direction == 17) {
            if (getCurrentPage() > 0) {
                snapToPage(getCurrentPage() - 1);
                return true;
            }
        } else if (direction == 66 && getCurrentPage() < getPageCount() - 1) {
            snapToPage(getCurrentPage() + 1);
            return true;
        }
        return super.dispatchUnhandledMove(focused, direction);
    }

    public void addFocusables(ArrayList<View> views, int direction, int focusableMode) {
        if (this.mCurrentPage >= 0 && this.mCurrentPage < getPageCount()) {
            getPageAt(this.mCurrentPage).addFocusables(views, direction);
        }
        if (direction == 17) {
            if (this.mCurrentPage > 0) {
                getPageAt(this.mCurrentPage - 1).addFocusables(views, direction);
            }
        } else if (direction == 66 && this.mCurrentPage < getPageCount() - 1) {
            getPageAt(this.mCurrentPage + 1).addFocusables(views, direction);
        }
    }

    public void focusableViewAvailable(View focused) {
        View current = getPageAt(this.mCurrentPage);
        View v = focused;
        while (v != current) {
            if (v != this && (v.getParent() instanceof View)) {
                v = (View) v.getParent();
            } else {
                return;
            }
        }
        super.focusableViewAvailable(focused);
    }

    public void requestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        if (disallowIntercept) {
            getPageAt(this.mCurrentPage).cancelLongPress();
        }
        super.requestDisallowInterceptTouchEvent(disallowIntercept);
    }

    /* access modifiers changed from: protected */
    public boolean hitsPreviousPage(float x, float y) {
        return x < ((float) (getRelativeChildOffset(this.mCurrentPage) - this.mPageSpacing));
    }

    /* access modifiers changed from: protected */
    public boolean hitsNextPage(float x, float y) {
        return x > ((float) ((getMeasuredWidth() - getRelativeChildOffset(this.mCurrentPage)) + this.mPageSpacing));
    }

    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean finishedScrolling;
        acquireVelocityTrackerAndAddMovement(ev);
        if (getChildCount() <= 0) {
            return super.onInterceptTouchEvent(ev);
        }
        int action = ev.getAction();
        if (action == 2 && this.mTouchState == 1) {
            return true;
        }
        switch (action & 255) {
            case 0:
                float x = ev.getX();
                float y = ev.getY();
                this.mDownMotionX = x;
                this.mLastMotionX = x;
                this.mLastMotionY = y;
                this.mLastMotionXRemainder = FlyingIcon.ANGULAR_VMIN;
                this.mTotalMotionX = FlyingIcon.ANGULAR_VMIN;
                this.mActivePointerId = ev.getPointerId(0);
                this.mAllowLongPress = true;
                int xDist = Math.abs(this.mScroller.getFinalX() - this.mScroller.getCurrX());
                if (this.mScroller.isFinished() || xDist < this.mTouchSlop) {
                    finishedScrolling = true;
                } else {
                    finishedScrolling = false;
                }
                if (finishedScrolling) {
                    this.mTouchState = 0;
                    this.mScroller.abortAnimation();
                } else {
                    this.mTouchState = 1;
                }
                if (!(this.mTouchState == 2 || this.mTouchState == 3 || getChildCount() <= 0)) {
                    if (!hitsPreviousPage(x, y)) {
                        if (hitsNextPage(x, y)) {
                            this.mTouchState = 3;
                            break;
                        }
                    } else {
                        this.mTouchState = 2;
                        break;
                    }
                }
                break;
            case 1:
            case 3:
                this.mTouchState = 0;
                this.mAllowLongPress = false;
                this.mActivePointerId = -1;
                releaseVelocityTracker();
                break;
            case 2:
                if (this.mActivePointerId != -1) {
                    determineScrollingStart(ev);
                    break;
                }
            case 6:
                onSecondaryPointerUp(ev);
                releaseVelocityTracker();
                break;
        }
        if (this.mTouchState == 0) {
            return false;
        }
        return true;
    }

    /* access modifiers changed from: protected */
    public void animateClickFeedback(View v, final Runnable r) {
        ObjectAnimator anim = (ObjectAnimator) AnimatorInflater.loadAnimator(this.mContext, R.anim.paged_view_click_feedback);
        anim.setTarget(v);
        anim.addListener(new AnimatorListenerAdapter() {
            public void onAnimationRepeat(Animator animation) {
                r.run();
            }
        });
        anim.start();
    }

    /* access modifiers changed from: protected */
    public void determineScrollingStart(MotionEvent ev) {
        determineScrollingStart(ev, 1.0f);
    }

    /* access modifiers changed from: protected */
    public void determineScrollingStart(MotionEvent ev, float touchSlopScale) {
        boolean xPaged;
        boolean xMoved;
        boolean yMoved = false;
        int pointerIndex = ev.findPointerIndex(this.mActivePointerId);
        if (pointerIndex != -1) {
            float x = ev.getX(pointerIndex);
            int xDiff = (int) Math.abs(x - this.mLastMotionX);
            int yDiff = (int) Math.abs(ev.getY(pointerIndex) - this.mLastMotionY);
            int touchSlop = Math.round(((float) this.mTouchSlop) * touchSlopScale);
            if (xDiff > this.mPagingTouchSlop) {
                xPaged = true;
            } else {
                xPaged = false;
            }
            if (xDiff > touchSlop) {
                xMoved = true;
            } else {
                xMoved = false;
            }
            if (yDiff > touchSlop) {
                yMoved = true;
            }
            if (xMoved || xPaged || yMoved) {
                if (!this.mUsePagingTouchSlop ? xMoved : xPaged) {
                    this.mTouchState = 1;
                    this.mTotalMotionX += Math.abs(this.mLastMotionX - x);
                    this.mLastMotionX = x;
                    this.mLastMotionXRemainder = FlyingIcon.ANGULAR_VMIN;
                    this.mTouchX = (float) this.mScrollX;
                    pageBeginMoving();
                }
                cancelCurrentPageLongPress();
            }
        }
    }

    /* access modifiers changed from: protected */
    public void cancelCurrentPageLongPress() {
        if (this.mAllowLongPress) {
            this.mAllowLongPress = false;
            View currentPage = getPageAt(this.mCurrentPage);
            if (currentPage != null) {
                currentPage.cancelLongPress();
            }
        }
    }

    /* access modifiers changed from: protected */
    public float getScrollProgress(int screenScroll, View v, int page) {
        return Math.max(Math.min(((float) ((screenScroll + (getMeasuredWidth() / 2)) - ((getChildOffset(page) - getRelativeChildOffset(page)) + (getMeasuredWidth() / 2)))) / (((float) (getScaledMeasuredWidth(v) + this.mPageSpacing)) * 1.0f), 1.0f), -1.0f);
    }

    private float overScrollInfluenceCurve(float f) {
        float f2 = f - 1.0f;
        return (f2 * f2 * f2) + 1.0f;
    }

    /* access modifiers changed from: protected */
    public void acceleratedOverScroll(float amount) {
        int screenSize = getMeasuredWidth();
        float f = OVERSCROLL_ACCELERATE_FACTOR * (amount / ((float) screenSize));
        if (f != FlyingIcon.ANGULAR_VMIN) {
            if (Math.abs(f) >= 1.0f) {
                f /= Math.abs(f);
            }
            int overScrollAmount = Math.round(((float) screenSize) * f);
            if (amount < FlyingIcon.ANGULAR_VMIN) {
                this.mOverScrollX = overScrollAmount;
                this.mScrollX = 0;
            } else {
                this.mOverScrollX = this.mMaxScrollX + overScrollAmount;
                this.mScrollX = this.mMaxScrollX;
            }
            invalidate();
        }
    }

    /* access modifiers changed from: protected */
    public void dampedOverScroll(float amount) {
        int screenSize = getMeasuredWidth();
        float f = amount / ((float) screenSize);
        if (f != FlyingIcon.ANGULAR_VMIN) {
            float f2 = (f / Math.abs(f)) * overScrollInfluenceCurve(Math.abs(f));
            if (Math.abs(f2) >= 1.0f) {
                f2 /= Math.abs(f2);
            }
            int overScrollAmount = Math.round(OVERSCROLL_DAMP_FACTOR * f2 * ((float) screenSize));
            if (amount < FlyingIcon.ANGULAR_VMIN) {
                this.mOverScrollX = overScrollAmount;
                this.mScrollX = 0;
            } else {
                this.mOverScrollX = this.mMaxScrollX + overScrollAmount;
                this.mScrollX = this.mMaxScrollX;
            }
            invalidate();
        }
    }

    /* access modifiers changed from: protected */
    public void overScroll(float amount) {
        dampedOverScroll(amount);
    }

    public boolean onTouchEvent(MotionEvent ev) {
        if (getChildCount() <= 0) {
            return super.onTouchEvent(ev);
        }
        acquireVelocityTrackerAndAddMovement(ev);
        switch (ev.getAction() & 255) {
            case 0:
                if (!this.mScroller.isFinished()) {
                    this.mScroller.abortAnimation();
                }
                float x = ev.getX();
                this.mLastMotionX = x;
                this.mDownMotionX = x;
                this.mLastMotionXRemainder = FlyingIcon.ANGULAR_VMIN;
                this.mTotalMotionX = FlyingIcon.ANGULAR_VMIN;
                this.mActivePointerId = ev.getPointerId(0);
                if (this.mTouchState == 1) {
                    pageBeginMoving();
                    break;
                }
                break;
            case 1:
                if (this.mTouchState == 1) {
                    int activePointerId = this.mActivePointerId;
                    float x2 = ev.getX(ev.findPointerIndex(activePointerId));
                    VelocityTracker velocityTracker = this.mVelocityTracker;
                    velocityTracker.computeCurrentVelocity(ChannelDesk.max_dtv_count, (float) this.mMaximumVelocity);
                    int velocityX = (int) velocityTracker.getXVelocity(activePointerId);
                    int deltaX = (int) (x2 - this.mDownMotionX);
                    int pageWidth = getScaledMeasuredWidth(getPageAt(this.mCurrentPage));
                    boolean isSignificantMove = ((float) Math.abs(deltaX)) > ((float) pageWidth) * SIGNIFICANT_MOVE_THRESHOLD;
                    int snapVelocity = this.mSnapVelocity;
                    this.mTotalMotionX += Math.abs((this.mLastMotionX + this.mLastMotionXRemainder) - x2);
                    boolean isFling = this.mTotalMotionX > 25.0f && Math.abs(velocityX) > snapVelocity;
                    boolean returnToOriginalPage = false;
                    if (((float) Math.abs(deltaX)) > ((float) pageWidth) * RETURN_TO_ORIGINAL_PAGE_THRESHOLD && Math.signum((float) velocityX) != Math.signum((float) deltaX) && isFling) {
                        returnToOriginalPage = true;
                    }
                    if (((isSignificantMove && deltaX > 0 && !isFling) || (isFling && velocityX > 0)) && this.mCurrentPage > 0) {
                        snapToPageWithVelocity(returnToOriginalPage ? this.mCurrentPage : this.mCurrentPage - 1, velocityX);
                    } else if (((!isSignificantMove || deltaX >= 0 || isFling) && (!isFling || velocityX >= 0)) || this.mCurrentPage >= getChildCount() - 1) {
                        snapToDestination();
                    } else {
                        snapToPageWithVelocity(returnToOriginalPage ? this.mCurrentPage : this.mCurrentPage + 1, velocityX);
                    }
                } else if (this.mTouchState == 2) {
                    int nextPage = Math.max(0, this.mCurrentPage - 1);
                    if (nextPage != this.mCurrentPage) {
                        snapToPage(nextPage);
                    } else {
                        snapToDestination();
                    }
                } else if (this.mTouchState == 3) {
                    int nextPage2 = Math.min(getChildCount() - 1, this.mCurrentPage + 1);
                    if (nextPage2 != this.mCurrentPage) {
                        snapToPage(nextPage2);
                    } else {
                        snapToDestination();
                    }
                } else {
                    onUnhandledTap(ev);
                }
                this.mTouchState = 0;
                this.mActivePointerId = -1;
                releaseVelocityTracker();
                break;
            case 2:
                if (this.mTouchState != 1) {
                    determineScrollingStart(ev);
                    break;
                } else {
                    float x3 = ev.getX(ev.findPointerIndex(this.mActivePointerId));
                    float deltaX2 = (this.mLastMotionX + this.mLastMotionXRemainder) - x3;
                    this.mTotalMotionX += Math.abs(deltaX2);
                    if (Math.abs(deltaX2) < 1.0f) {
                        awakenScrollBars();
                        break;
                    } else {
                        this.mTouchX += deltaX2;
                        if (!this.mDeferScrollUpdate) {
                            scrollBy((int) deltaX2, 0);
                        } else {
                            invalidate();
                        }
                        this.mLastMotionX = x3;
                        this.mLastMotionXRemainder = deltaX2 - ((float) ((int) deltaX2));
                        break;
                    }
                }
            case 3:
                if (this.mTouchState == 1) {
                    snapToDestination();
                }
                this.mTouchState = 0;
                this.mActivePointerId = -1;
                releaseVelocityTracker();
                break;
            case 6:
                onSecondaryPointerUp(ev);
                break;
        }
        return true;
    }

    public boolean onGenericMotionEvent(MotionEvent event) {
        float vscroll;
        float hscroll;
        if ((event.getSource() & 2) != 0) {
            switch (event.getAction()) {
                case 8:
                    if ((event.getMetaState() & 1) != 0) {
                        vscroll = FlyingIcon.ANGULAR_VMIN;
                        hscroll = event.getAxisValue(9);
                    } else {
                        vscroll = -event.getAxisValue(9);
                        hscroll = event.getAxisValue(10);
                    }
                    if (!(hscroll == FlyingIcon.ANGULAR_VMIN && vscroll == FlyingIcon.ANGULAR_VMIN)) {
                        if (hscroll > FlyingIcon.ANGULAR_VMIN || vscroll > FlyingIcon.ANGULAR_VMIN) {
                            scrollRight();
                        } else {
                            scrollLeft();
                        }
                        return true;
                    }
                    break;
            }
        }
        return super.onGenericMotionEvent(event);
    }

    private void acquireVelocityTrackerAndAddMovement(MotionEvent ev) {
        if (this.mVelocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
        }
        this.mVelocityTracker.addMovement(ev);
    }

    private void releaseVelocityTracker() {
        if (this.mVelocityTracker != null) {
            this.mVelocityTracker.recycle();
            this.mVelocityTracker = null;
        }
    }

    private void onSecondaryPointerUp(MotionEvent ev) {
        int pointerIndex = (ev.getAction() & 65280) >> 8;
        if (ev.getPointerId(pointerIndex) == this.mActivePointerId) {
            int newPointerIndex = pointerIndex == 0 ? 1 : 0;
            float x = ev.getX(newPointerIndex);
            this.mDownMotionX = x;
            this.mLastMotionX = x;
            this.mLastMotionY = ev.getY(newPointerIndex);
            this.mLastMotionXRemainder = FlyingIcon.ANGULAR_VMIN;
            this.mActivePointerId = ev.getPointerId(newPointerIndex);
            if (this.mVelocityTracker != null) {
                this.mVelocityTracker.clear();
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onUnhandledTap(MotionEvent ev) {
    }

    public void requestChildFocus(View child, View focused) {
        super.requestChildFocus(child, focused);
        int page = indexToPage(indexOfChild(child));
        if (page >= 0 && page != getCurrentPage() && !isInTouchMode()) {
            snapToPage(page);
        }
    }

    /* access modifiers changed from: protected */
    public int getChildIndexForRelativeOffset(int relativeOffset) {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            int left = getRelativeChildOffset(i);
            int right = left + getScaledMeasuredWidth(getPageAt(i));
            if (left <= relativeOffset && relativeOffset <= right) {
                return i;
            }
        }
        return -1;
    }

    /* access modifiers changed from: protected */
    public int getChildWidth(int index) {
        int measuredWidth = getPageAt(index).getMeasuredWidth();
        int minWidth = this.mMinimumWidth;
        return minWidth > measuredWidth ? minWidth : measuredWidth;
    }

    /* access modifiers changed from: 0000 */
    public int getPageNearestToCenterOfScreen() {
        int minDistanceFromScreenCenter = Integer.MAX_VALUE;
        int minDistanceFromScreenCenterIndex = -1;
        int screenCenter = this.mScrollX + (getMeasuredWidth() / 2);
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            int distanceFromScreenCenter = Math.abs((getChildOffset(i) + (getScaledMeasuredWidth(getPageAt(i)) / 2)) - screenCenter);
            if (distanceFromScreenCenter < minDistanceFromScreenCenter) {
                minDistanceFromScreenCenter = distanceFromScreenCenter;
                minDistanceFromScreenCenterIndex = i;
            }
        }
        return minDistanceFromScreenCenterIndex;
    }

    /* access modifiers changed from: protected */
    public void snapToDestination() {
        snapToPage(getPageNearestToCenterOfScreen(), 300);
    }

    /* access modifiers changed from: 0000 */
    public float distanceInfluenceForSnapDuration(float f) {
        return (float) Math.sin((double) ((float) (((double) (f - 0.5f)) * 0.4712389167638204d)));
    }

    /* access modifiers changed from: protected */
    public void snapToPageWithVelocity(int whichPage, int velocity) {
        int whichPage2 = Math.max(0, Math.min(whichPage, getChildCount() - 1));
        int halfScreenSize = getMeasuredWidth() / 2;
        int delta = (getChildOffset(whichPage2) - getRelativeChildOffset(whichPage2)) - this.mUnboundedScrollX;
        if (Math.abs(velocity) < MIN_FLING_VELOCITY) {
            snapToPage(whichPage2, 300);
            return;
        }
        snapToPage(whichPage2, delta, Math.round(1000.0f * Math.abs((((float) halfScreenSize) + (((float) halfScreenSize) * distanceInfluenceForSnapDuration(Math.min(1.0f, (((float) Math.abs(delta)) * 1.0f) / ((float) (halfScreenSize * 2)))))) / ((float) Math.max(MINIMUM_SNAP_VELOCITY, Math.abs(velocity))))) * 4);
    }

    /* access modifiers changed from: protected */
    public void snapToPage(int whichPage) {
        snapToPage(whichPage, 300);
    }

    /* access modifiers changed from: protected */
    public void snapToPage(int whichPage, int duration) {
        int whichPage2 = Math.max(0, Math.min(whichPage, getPageCount() - 1));
        snapToPage(whichPage2, (getChildOffset(whichPage2) - getRelativeChildOffset(whichPage2)) - this.mUnboundedScrollX, duration);
    }

    /* access modifiers changed from: protected */
    public void snapToPage(int whichPage, int delta, int duration) {
        this.mNextPage = whichPage;
        View focusedChild = getFocusedChild();
        if (!(focusedChild == null || whichPage == this.mCurrentPage || focusedChild != getPageAt(this.mCurrentPage))) {
            focusedChild.clearFocus();
        }
        pageBeginMoving();
        awakenScrollBars(duration);
        if (duration == 0) {
            duration = Math.abs(delta);
        }
        if (!this.mScroller.isFinished()) {
            this.mScroller.abortAnimation();
        }
        this.mScroller.startScroll(this.mUnboundedScrollX, 0, delta, 0, duration);
        if (this.mDeferScrollUpdate) {
            loadAssociatedPages(this.mNextPage);
        } else {
            this.mDeferLoadAssociatedPagesUntilScrollCompletes = true;
        }
        notifyPageSwitchListener();
        invalidate();
    }

    public void scrollLeft() {
        if (this.mScroller.isFinished()) {
            if (this.mCurrentPage > 0) {
                snapToPage(this.mCurrentPage - 1);
            }
        } else if (this.mNextPage > 0) {
            snapToPage(this.mNextPage - 1);
        }
    }

    public void scrollRight() {
        if (this.mScroller.isFinished()) {
            if (this.mCurrentPage < getChildCount() - 1) {
                snapToPage(this.mCurrentPage + 1);
            }
        } else if (this.mNextPage < getChildCount() - 1) {
            snapToPage(this.mNextPage + 1);
        }
    }

    public int getPageForView(View v) {
        if (v != null) {
            ViewParent vp = v.getParent();
            int count = getChildCount();
            for (int i = 0; i < count; i++) {
                if (vp == getPageAt(i)) {
                    return i;
                }
            }
        }
        return -1;
    }

    public boolean allowLongPress() {
        return this.mAllowLongPress;
    }

    public void setAllowLongPress(boolean allowLongPress) {
        this.mAllowLongPress = allowLongPress;
    }

    /* access modifiers changed from: protected */
    public void loadAssociatedPages(int page) {
        loadAssociatedPages(page, false);
    }

    /* access modifiers changed from: protected */
    public void loadAssociatedPages(int page, boolean immediateAndOnly) {
        boolean z;
        if (this.mContentIsRefreshable) {
            int count = getChildCount();
            if (page < count) {
                int lowerPageBound = getAssociatedLowerPageBound(page);
                int upperPageBound = getAssociatedUpperPageBound(page);
                for (int i = 0; i < count; i++) {
                    if (i == page || !immediateAndOnly) {
                        Page layout = (Page) getPageAt(i);
                        int childCount = layout.getPageChildCount();
                        if (lowerPageBound > i || i > upperPageBound) {
                            if (this.mClearDirtyPages) {
                                if (childCount > 0) {
                                    layout.removeAllViewsOnPage();
                                }
                                this.mDirtyPageContent.set(i, Boolean.valueOf(true));
                            }
                        } else if (((Boolean) this.mDirtyPageContent.get(i)).booleanValue()) {
                            if (i != page || !immediateAndOnly) {
                                z = false;
                            } else {
                                z = true;
                            }
                            syncPageItems(i, z);
                            this.mDirtyPageContent.set(i, Boolean.valueOf(false));
                        }
                    }
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public int getAssociatedLowerPageBound(int page) {
        return Math.max(0, page - 1);
    }

    /* access modifiers changed from: protected */
    public int getAssociatedUpperPageBound(int page) {
        return Math.min(page + 1, getChildCount() - 1);
    }

    /* access modifiers changed from: protected */
    public void startChoiceMode(int mode, Callback callback) {
        if (isChoiceMode(0)) {
            this.mChoiceMode = mode;
            this.mActionMode = startActionMode(callback);
        }
    }

    public void endChoiceMode() {
        if (!isChoiceMode(0)) {
            this.mChoiceMode = 0;
            resetCheckedGrandchildren();
            if (this.mActionMode != null) {
                this.mActionMode.finish();
            }
            this.mActionMode = null;
        }
    }

    /* access modifiers changed from: protected */
    public boolean isChoiceMode(int mode) {
        return this.mChoiceMode == mode;
    }

    /* access modifiers changed from: protected */
    public ArrayList<Checkable> getCheckedGrandchildren() {
        ArrayList<Checkable> checked = new ArrayList<>();
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            Page layout = (Page) getPageAt(i);
            int grandChildCount = layout.getPageChildCount();
            for (int j = 0; j < grandChildCount; j++) {
                View v = layout.getChildOnPageAt(j);
                if ((v instanceof Checkable) && ((Checkable) v).isChecked()) {
                    checked.add((Checkable) v);
                }
            }
        }
        return checked;
    }

    /* access modifiers changed from: protected */
    public Checkable getSingleCheckedGrandchild() {
        if (this.mChoiceMode != 2) {
            int childCount = getChildCount();
            for (int i = 0; i < childCount; i++) {
                Page layout = (Page) getPageAt(i);
                int grandChildCount = layout.getPageChildCount();
                for (int j = 0; j < grandChildCount; j++) {
                    View v = layout.getChildOnPageAt(j);
                    if ((v instanceof Checkable) && ((Checkable) v).isChecked()) {
                        return (Checkable) v;
                    }
                }
            }
        }
        return null;
    }

    /* access modifiers changed from: protected */
    public void resetCheckedGrandchildren() {
        Iterator it = getCheckedGrandchildren().iterator();
        while (it.hasNext()) {
            ((Checkable) it.next()).setChecked(false);
        }
    }

    /* access modifiers changed from: protected */
    public void invalidatePageData() {
        invalidatePageData(-1, false);
    }

    /* access modifiers changed from: protected */
    public void invalidatePageData(int currentPage) {
        invalidatePageData(currentPage, false);
    }

    /* access modifiers changed from: protected */
    public void invalidatePageData(int currentPage, boolean immediateAndOnly) {
        if (this.mIsDataReady && this.mContentIsRefreshable) {
            this.mScroller.forceFinished(true);
            this.mNextPage = -1;
            syncPages();
            measure(MeasureSpec.makeMeasureSpec(getMeasuredWidth(), 1073741824), MeasureSpec.makeMeasureSpec(getMeasuredHeight(), 1073741824));
            if (currentPage > -1) {
                setCurrentPage(Math.min(getPageCount() - 1, currentPage));
            }
            int count = getChildCount();
            this.mDirtyPageContent.clear();
            for (int i = 0; i < count; i++) {
                this.mDirtyPageContent.add(Boolean.valueOf(true));
            }
            loadAssociatedPages(this.mCurrentPage, immediateAndOnly);
            requestLayout();
        }
    }

    /* access modifiers changed from: protected */
    public View getScrollingIndicator() {
        boolean z;
        if (this.mHasScrollIndicator && this.mScrollIndicator == null) {
            ViewGroup parent = (ViewGroup) getParent();
            if (parent == null) {
                return null;
            }
            this.mScrollIndicator = parent.findViewById(R.id.paged_view_indicator);
            if (this.mScrollIndicator != null) {
                z = true;
            } else {
                z = false;
            }
            this.mHasScrollIndicator = z;
            if (this.mHasScrollIndicator) {
                try {
                    ((ScrollIndicator) this.mScrollIndicator).init(this);
                } catch (ClassCastException e) {
                    Log.e(TAG, "scroll indicator should implement ScrollIndicator interface!", e);
                }
                this.mScrollIndicator.setVisibility(0);
            }
        }
        return this.mScrollIndicator;
    }

    /* access modifiers changed from: protected */
    public boolean isScrollingIndicatorEnabled() {
        return true;
    }

    /* access modifiers changed from: protected */
    public void flashScrollingIndicator(boolean animated) {
        removeCallbacks(this.hideScrollingIndicatorRunnable);
        showScrollingIndicator(!animated);
        postDelayed(this.hideScrollingIndicatorRunnable, 650);
    }

    /* access modifiers changed from: protected */
    public void showScrollingIndicator(boolean immediately) {
        showScrollingIndicator(immediately, 150);
    }

    /* access modifiers changed from: protected */
    public void showScrollingIndicator(boolean immediately, int duration) {
        if (getChildCount() >= 1 && isScrollingIndicatorEnabled()) {
            getScrollingIndicator();
            if (this.mScrollIndicator != null) {
                try {
                    ((ScrollIndicator) this.mScrollIndicator).show(immediately, duration);
                } catch (ClassCastException e) {
                    Log.e(TAG, "scroll indicator should implement ScrollIndicator interface!", e);
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public void cancelScrollingIndicatorAnimations() {
        if (this.mScrollIndicator != null) {
            try {
                ((ScrollIndicator) this.mScrollIndicator).cancelAnimations();
            } catch (ClassCastException e) {
                Log.e(TAG, "scroll indicator should implement ScrollIndicator interface!", e);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void hideScrollingIndicator(boolean immediately) {
        hideScrollingIndicator(immediately, 650);
    }

    /* access modifiers changed from: protected */
    public void hideScrollingIndicator(boolean immediately, int duration) {
        if (getChildCount() > 1 && isScrollingIndicatorEnabled()) {
            getScrollingIndicator();
            if (this.mScrollIndicator != null) {
                try {
                    ((ScrollIndicator) this.mScrollIndicator).hide(immediately, duration);
                } catch (ClassCastException e) {
                    Log.e(TAG, "scroll indicator should implement ScrollIndicator interface!", e);
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public void enableScrollingIndicator() {
        this.mHasScrollIndicator = true;
        getScrollingIndicator();
        if (this.mScrollIndicator != null) {
            this.mScrollIndicator.setVisibility(0);
        }
    }

    /* access modifiers changed from: protected */
    public void disableScrollingIndicator() {
        if (this.mScrollIndicator != null) {
            this.mScrollIndicator.setVisibility(8);
        }
        this.mHasScrollIndicator = false;
        this.mScrollIndicator = null;
    }

    /* access modifiers changed from: protected */
    public boolean hasElasticScrollIndicator() {
        boolean z = false;
        if (this.mScrollIndicator == null) {
            return z;
        }
        try {
            return ((ScrollIndicator) this.mScrollIndicator).isElasticScrollIndicator();
        } catch (ClassCastException e) {
            Log.e(TAG, "scroll indicator should implement ScrollIndicator interface!", e);
            return z;
        }
    }

    public void updateScrollingIndicator() {
        if (getChildCount() >= 1 && isScrollingIndicatorEnabled()) {
            getScrollingIndicator();
            if (this.mScrollIndicator != null) {
                try {
                    ((ScrollIndicator) this.mScrollIndicator).update();
                } catch (ClassCastException e) {
                    Log.e(TAG, "scroll indicator should implement ScrollIndicator interface!", e);
                }
            }
        }
    }

    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
        super.onInitializeAccessibilityNodeInfo(info);
        info.setScrollable(true);
    }

    public void onInitializeAccessibilityEvent(AccessibilityEvent event) {
        super.onInitializeAccessibilityEvent(event);
        event.setScrollable(true);
        if (event.getEventType() == 4096) {
            event.setFromIndex(this.mCurrentPage);
            event.setToIndex(this.mCurrentPage);
            event.setItemCount(getChildCount());
        }
    }

    /* access modifiers changed from: protected */
    public String getCurrentPageDescription() {
        return String.format(this.mContext.getString(R.string.default_scroll_format), new Object[]{Integer.valueOf((this.mNextPage != -1 ? this.mNextPage : this.mCurrentPage) + 1), Integer.valueOf(getChildCount())});
    }

    public boolean onHoverEvent(MotionEvent event) {
        return true;
    }
}
