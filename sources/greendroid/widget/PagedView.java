package greendroid.widget;

import android.content.Context;
import android.database.DataSetObserver;
import android.os.Handler;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.FocusFinder;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.View.BaseSavedState;
import android.view.View.MeasureSpec;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.DecelerateInterpolator;
import android.widget.Scroller;
import com.cyanogenmod.trebuchet.RocketLauncher.Board.FlyingIcon;
import java.util.LinkedList;
import java.util.Queue;

public class PagedView extends ViewGroup {
    private static final int FRAME_RATE = 16;
    private static final int INVALID_PAGE = -1;
    private static final String LOG_TAG = PagedView.class.getSimpleName();
    private static final int MINIMUM_PAGE_CHANGE_VELOCITY = 500;
    private static final int VELOCITY_UNITS = 1000;
    private SparseArray<View> mActiveViews;
    /* access modifiers changed from: private */
    public PagedAdapter mAdapter;
    /* access modifiers changed from: private */
    public int mCurrentPage;
    private DataSetObserver mDataSetObserver;
    /* access modifiers changed from: private */
    public final Handler mHandler;
    private boolean mIsBeingDragged;
    private int mMaximumVelocity;
    private int mMinimumVelocity;
    private int mOffsetX;
    private OnPagedViewChangeListener mOnPageChangeListener;
    private int mPageCount;
    private int mPageSlop;
    private int mPagingTouchSlop;
    private Queue<View> mRecycler;
    /* access modifiers changed from: private */
    public Scroller mScroller;
    private Runnable mScrollerRunnable;
    private int mStartMotionX;
    private int mStartOffsetX;
    /* access modifiers changed from: private */
    public int mTargetPage;
    private VelocityTracker mVelocityTracker;

    public interface OnPagedViewChangeListener {
        void onPageChanged(PagedView pagedView, int i, int i2);

        void onStartTracking(PagedView pagedView);

        void onStopTracking(PagedView pagedView);
    }

    static class SavedState extends BaseSavedState {
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
        }

        /* synthetic */ SavedState(Parcel parcel, SavedState savedState) {
            this(parcel);
        }

        private SavedState(Parcel in) {
            super(in);
            this.currentPage = in.readInt();
        }

        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(this.currentPage);
        }
    }

    public PagedView(Context context) {
        this(context, null);
    }

    public PagedView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PagedView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mHandler = new Handler();
        this.mTargetPage = -1;
        this.mActiveViews = new SparseArray<>();
        this.mRecycler = new LinkedList();
        this.mDataSetObserver = new DataSetObserver() {
            public void onInvalidated() {
            }

            public void onChanged() {
                int currentPage = PagedView.this.mCurrentPage;
                PagedView.this.setAdapter(PagedView.this.mAdapter);
                PagedView.this.mCurrentPage = currentPage;
                PagedView.this.setOffsetX(PagedView.this.getOffsetForPage(currentPage));
            }
        };
        this.mScrollerRunnable = new Runnable() {
            public void run() {
                Scroller scroller = PagedView.this.mScroller;
                if (!scroller.isFinished()) {
                    scroller.computeScrollOffset();
                    PagedView.this.setOffsetX(scroller.getCurrX());
                    PagedView.this.mHandler.postDelayed(this, 16);
                    return;
                }
                PagedView.this.performPageChange(PagedView.this.mTargetPage);
            }
        };
        initPagedView();
    }

    private void initPagedView() {
        Context context = getContext();
        this.mScroller = new Scroller(context, new DecelerateInterpolator());
        ViewConfiguration conf = ViewConfiguration.get(context);
        this.mPagingTouchSlop = conf.getScaledTouchSlop() * 2;
        this.mMaximumVelocity = conf.getScaledMaximumFlingVelocity();
        this.mMinimumVelocity = (int) ((context.getResources().getDisplayMetrics().density * 500.0f) + 0.5f);
    }

    private boolean handleKeyEvent(int keyCode, KeyEvent event) {
        View currentView = (View) this.mActiveViews.get(getCurrentPage());
        if (event.getAction() != 0 || currentView == null) {
            return false;
        }
        switch (event.getKeyCode()) {
            case 21:
            case 22:
                View currentFocused = currentView.findFocus();
                if (currentFocused == this) {
                    currentFocused = null;
                }
                View nextFocused = null;
                int direction = event.getKeyCode() == 21 ? 17 : 66;
                if (currentView instanceof ViewGroup) {
                    nextFocused = FocusFinder.getInstance().findNextFocus((ViewGroup) currentView, currentFocused, direction);
                }
                if (nextFocused != null && nextFocused != currentView && nextFocused.requestFocus(direction)) {
                    return true;
                }
                if (event.getKeyCode() == 21) {
                    smoothScrollToPrevious();
                    return true;
                } else if (event.getKeyCode() == 22) {
                    smoothScrollToNext();
                    return true;
                }
                break;
            case 92:
                smoothScrollToPrevious();
                return true;
            case 93:
                smoothScrollToNext();
                return true;
        }
        return false;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (handleKeyEvent(keyCode, event)) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (handleKeyEvent(keyCode, event)) {
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int childWidth = 0;
        int childHeight = 0;
        if ((this.mAdapter == null ? 0 : this.mAdapter.getCount()) > 0) {
            if (widthMode == 0 || heightMode == 0) {
                View child = obtainView(this.mCurrentPage);
                measureChild(child, widthMeasureSpec, heightMeasureSpec);
                childWidth = child.getMeasuredWidth();
                childHeight = child.getMeasuredHeight();
            }
            if (widthMode == 0) {
                widthSize = childWidth;
            }
            if (heightMode == 0) {
                heightSize = childHeight;
            }
        }
        setMeasuredDimension(widthSize, heightSize);
    }

    /* access modifiers changed from: protected */
    public void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.mPageSlop = (int) (((double) w) * 0.5d);
        this.mOffsetX = getOffsetForPage(this.mCurrentPage);
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean changed, int l, int t, int r, int b) {
        if (this.mPageCount > 0) {
            int startPage = getPageForOffset(this.mOffsetX);
            int endPage = getPageForOffset((this.mOffsetX - getWidth()) + 1);
            recycleViews(startPage, endPage);
            for (int i = startPage; i <= endPage; i++) {
                View child = (View) this.mActiveViews.get(i);
                if (child == null) {
                    child = obtainView(i);
                }
                setupView(child, i);
            }
        }
    }

    public boolean onGenericMotionEvent(MotionEvent event) {
        if ((event.getSource() & 2) != 0) {
            switch (event.getAction()) {
                case 8:
                    if (!this.mIsBeingDragged) {
                        float vscroll = event.getAxisValue(9);
                        if (vscroll != FlyingIcon.ANGULAR_VMIN) {
                            smoothScrollToPage((vscroll < FlyingIcon.ANGULAR_VMIN ? 1 : -1) + getCurrentPage());
                            return true;
                        }
                    }
                    break;
            }
        }
        return super.onGenericMotionEvent(event);
    }

    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean z = false;
        int action = ev.getAction();
        if (action == 2 && this.mIsBeingDragged) {
            return true;
        }
        int x = (int) ev.getX();
        switch (action) {
            case 0:
                this.mStartMotionX = x;
                if (!this.mScroller.isFinished()) {
                    z = true;
                }
                this.mIsBeingDragged = z;
                if (this.mIsBeingDragged) {
                    this.mScroller.forceFinished(true);
                    this.mHandler.removeCallbacks(this.mScrollerRunnable);
                    break;
                }
                break;
            case 1:
            case 3:
                this.mIsBeingDragged = false;
                break;
            case 2:
                if (Math.abs(x - this.mStartMotionX) > this.mPagingTouchSlop) {
                    this.mIsBeingDragged = true;
                    performStartTracking(x);
                    break;
                }
                break;
        }
        return this.mIsBeingDragged;
    }

    public boolean onTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        int x = (int) ev.getX();
        if (this.mVelocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
        }
        this.mVelocityTracker.addMovement(ev);
        switch (action) {
            case 0:
                if (!this.mScroller.isFinished()) {
                    this.mScroller.forceFinished(true);
                    this.mHandler.removeCallbacks(this.mScrollerRunnable);
                }
                performStartTracking(x);
                break;
            case 1:
            case 3:
                setOffsetX(this.mStartOffsetX - (this.mStartMotionX - x));
                int direction = 0;
                int slop = this.mStartMotionX - x;
                if (Math.abs(slop) > this.mPageSlop) {
                    direction = slop > 0 ? 1 : -1;
                } else {
                    this.mVelocityTracker.computeCurrentVelocity(1000, (float) this.mMaximumVelocity);
                    int initialVelocity = (int) this.mVelocityTracker.getXVelocity();
                    if (Math.abs(initialVelocity) > this.mMinimumVelocity) {
                        direction = initialVelocity > 0 ? -1 : 1;
                    }
                }
                if (this.mOnPageChangeListener != null) {
                    this.mOnPageChangeListener.onStopTracking(this);
                }
                smoothScrollToPage(getActualCurrentPage() + direction);
                if (this.mVelocityTracker != null) {
                    this.mVelocityTracker.recycle();
                    this.mVelocityTracker = null;
                    break;
                }
                break;
            case 2:
                int newOffset = this.mStartOffsetX - (this.mStartMotionX - x);
                if (newOffset <= 0 && newOffset >= getOffsetForPage(this.mPageCount - 1)) {
                    setOffsetX(newOffset);
                    break;
                } else {
                    this.mStartOffsetX = this.mOffsetX;
                    this.mStartMotionX = x;
                    break;
                }
        }
        return true;
    }

    public void setOnPageChangeListener(OnPagedViewChangeListener listener) {
        this.mOnPageChangeListener = listener;
    }

    public void setAdapter(PagedAdapter adapter) {
        if (this.mAdapter != null) {
            this.mAdapter.unregisterDataSetObserver(this.mDataSetObserver);
        }
        this.mRecycler.clear();
        this.mActiveViews.clear();
        removeAllViews();
        this.mAdapter = adapter;
        this.mTargetPage = -1;
        this.mCurrentPage = 0;
        this.mOffsetX = 0;
        if (this.mAdapter != null) {
            this.mAdapter.registerDataSetObserver(this.mDataSetObserver);
            this.mPageCount = this.mAdapter.getCount();
        }
        requestLayout();
        invalidate();
    }

    public int getCurrentPage() {
        return this.mCurrentPage;
    }

    private int getActualCurrentPage() {
        return this.mTargetPage != -1 ? this.mTargetPage : this.mCurrentPage;
    }

    public void smoothScrollToPage(int page) {
        scrollToPage(page, true);
    }

    public void smoothScrollToNext() {
        smoothScrollToPage(getActualCurrentPage() + 1);
    }

    public void smoothScrollToPrevious() {
        smoothScrollToPage(getActualCurrentPage() - 1);
    }

    public void scrollToPage(int page) {
        scrollToPage(page, false);
    }

    public void scrollToNext() {
        scrollToPage(getActualCurrentPage() + 1);
    }

    public void scrollToPrevious() {
        scrollToPage(getActualCurrentPage() - 1);
    }

    private void scrollToPage(int page, boolean animated) {
        int page2 = Math.max(0, Math.min(page, this.mPageCount - 1));
        int targetOffset = getOffsetForPage(page2);
        int dx = targetOffset - this.mOffsetX;
        if (dx == 0) {
            performPageChange(page2);
        } else if (animated) {
            this.mTargetPage = page2;
            this.mScroller.startScroll(this.mOffsetX, 0, dx, 0);
            this.mHandler.post(this.mScrollerRunnable);
        } else {
            setOffsetX(targetOffset);
            performPageChange(page2);
        }
    }

    /* access modifiers changed from: private */
    public void setOffsetX(int offsetX) {
        if (offsetX != this.mOffsetX) {
            int startPage = getPageForOffset(offsetX);
            int endPage = getPageForOffset((offsetX - getWidth()) + 1);
            recycleViews(startPage, endPage);
            int leftAndRightOffset = offsetX - this.mOffsetX;
            for (int i = startPage; i <= endPage; i++) {
                View child = (View) this.mActiveViews.get(i);
                if (child == null) {
                    child = obtainView(i);
                    setupView(child, i);
                }
                child.offsetLeftAndRight(leftAndRightOffset);
            }
            this.mOffsetX = offsetX;
            invalidate();
        }
    }

    /* access modifiers changed from: private */
    public int getOffsetForPage(int page) {
        return -(getWidth() * page);
    }

    private int getPageForOffset(int offset) {
        return (-offset) / getWidth();
    }

    private void recycleViews(int start, int end) {
        SparseArray<View> activeViews = this.mActiveViews;
        int count = activeViews.size();
        for (int i = 0; i < count; i++) {
            int key = activeViews.keyAt(i);
            if (key < start || key > end) {
                View recycled = (View) activeViews.valueAt(i);
                removeView(recycled);
                this.mRecycler.add(recycled);
                activeViews.delete(key);
            }
        }
    }

    private View obtainView(int position) {
        View recycled = (View) this.mRecycler.poll();
        View child = this.mAdapter.getView(position, recycled, this);
        if (child == null) {
            throw new NullPointerException("PagedAdapter.getView must return a non-null View");
        }
        if (recycled == null || child != recycled) {
        }
        addView(child);
        this.mActiveViews.put(position, child);
        return child;
    }

    private void setupView(View child, int position) {
        if (child != null && child.getVisibility() != 8) {
            LayoutParams lp = child.getLayoutParams();
            if (lp == null) {
                lp = new LayoutParams(-1, -1);
            }
            child.measure(getChildMeasureSpec(MeasureSpec.makeMeasureSpec(getWidth(), 1073741824), 0, lp.width), getChildMeasureSpec(MeasureSpec.makeMeasureSpec(getHeight(), 1073741824), 0, lp.height));
            int childLeft = this.mOffsetX - getOffsetForPage(position);
            child.layout(childLeft, 0, child.getMeasuredWidth() + childLeft, child.getMeasuredHeight());
        }
    }

    private void performStartTracking(int startMotionX) {
        if (this.mOnPageChangeListener != null) {
            this.mOnPageChangeListener.onStartTracking(this);
        }
        this.mStartMotionX = startMotionX;
        this.mStartOffsetX = this.mOffsetX;
    }

    /* access modifiers changed from: private */
    public void performPageChange(int newPage) {
        if (this.mCurrentPage != newPage) {
            if (this.mOnPageChangeListener != null) {
                this.mOnPageChangeListener.onPageChanged(this, this.mCurrentPage, newPage);
            }
            this.mCurrentPage = newPage;
        }
    }

    public Parcelable onSaveInstanceState() {
        SavedState ss = new SavedState(super.onSaveInstanceState());
        ss.currentPage = this.mCurrentPage;
        return ss;
    }

    public void onRestoreInstanceState(Parcelable state) {
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        this.mCurrentPage = ss.currentPage;
    }
}
