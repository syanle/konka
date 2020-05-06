package com.cyanogenmod.trebuchet;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import com.cyanogenmod.trebuchet.RocketLauncher.Board.FlyingIcon;
import com.cyanogenmod.trebuchet.preference.PreferencesProvider.Interface.Homescreen.Indicator;
import com.konka.ios7launcher.R;
import java.util.ArrayList;

public class DottedScrollIndicator extends LinearLayout implements ScrollIndicator, OnClickListener {
    private static boolean mUseHomescreenIconRes;
    private int mActiveDot = 0;
    private ValueAnimator mAnimator;
    private ArrayList<ImageView> mDots = new ArrayList<>();
    private LayoutInflater mInflater;
    private int mPaddingLeft;
    private int mPaddingRight;
    private PagedView mPagedview;

    public DottedScrollIndicator(Context context) {
        super(context);
        init(context);
    }

    public DottedScrollIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public DottedScrollIndicator(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.PagedView, defStyle, 0);
        this.mPaddingLeft = a.getDimensionPixelSize(7, 0);
        this.mPaddingRight = a.getDimensionPixelSize(8, 0);
        a.recycle();
    }

    private void init(Context context) {
        this.mInflater = LayoutInflater.from(context);
        mUseHomescreenIconRes = Indicator.getUseHomescreenIconRes(context);
    }

    private int getPageIconRes(int addIndex) {
        if (this.mPagedview instanceof Workspace) {
            Workspace ws = (Workspace) this.mPagedview;
            int searchScreenIndex = ws.getSearchScreenIndex();
            int homeScreenIndex = ws.getHomeScreenIndex();
            if (addIndex == searchScreenIndex) {
                return R.layout.search_indicator_item;
            }
            if (addIndex == homeScreenIndex && mUseHomescreenIconRes) {
                return R.layout.home_indicator_item;
            }
        }
        return R.layout.dotted_indicator_item;
    }

    private void addIndicator(int addIndex) {
        ImageView v = (ImageView) this.mInflater.inflate(getPageIconRes(addIndex), null);
        v.setOnClickListener(this);
        addView(v, new LayoutParams(-2, -2));
        this.mDots.add(v);
    }

    private void removeIndicator(int removeIdx) {
        removeViewAt(removeIdx);
        this.mDots.remove(removeIdx);
    }

    public void onClick(View v) {
        int index = this.mDots.indexOf(v);
        if (this.mActiveDot != index) {
            this.mPagedview.snapToPage(index);
        }
    }

    public void init(PagedView pagedView) {
        this.mPagedview = pagedView;
        int childCount = pagedView.getChildCount();
        Log.e("xshjxshj_test", "childCount = " + childCount);
        for (int i = 0; i < childCount; i++) {
            Log.e("xshjxshj_test", "i = " + i);
            addIndicator(i);
        }
    }

    public boolean isElasticScrollIndicator() {
        return false;
    }

    public void show(boolean immediately, int duration) {
        updateScrollingIndicator();
        setVisibility(0);
        cancelAnimations();
        if (immediately) {
            setAlpha(1.0f);
            return;
        }
        this.mAnimator = ObjectAnimator.ofFloat(this, "alpha", new float[]{1.0f});
        this.mAnimator.setDuration((long) duration);
        this.mAnimator.start();
    }

    public void hide(boolean immediately, int duration) {
        updateScrollingIndicator();
        cancelAnimations();
        if (immediately) {
            setVisibility(4);
            setAlpha(FlyingIcon.ANGULAR_VMIN);
            return;
        }
        this.mAnimator = ObjectAnimator.ofFloat(this, "alpha", new float[]{0.0f});
        this.mAnimator.setDuration((long) duration);
        this.mAnimator.addListener(new AnimatorListenerAdapter() {
            private boolean cancelled = false;

            public void onAnimationCancel(Animator animation) {
                this.cancelled = true;
            }

            public void onAnimationEnd(Animator animation) {
                if (!this.cancelled) {
                    DottedScrollIndicator.this.setVisibility(4);
                }
            }
        });
        this.mAnimator.start();
    }

    public void update() {
        updateScrollingIndicator();
    }

    public void cancelAnimations() {
        if (this.mAnimator != null) {
            this.mAnimator.cancel();
        }
    }

    private void updateScrollingIndicator() {
        boolean needInvalidate = false;
        int numPages = this.mPagedview.getChildCount();
        int currentPage = this.mPagedview.getPageNearestToCenterOfScreen();
        int currentSize = this.mDots.size();
        if (currentSize < numPages) {
            needInvalidate = true;
            while (currentSize < numPages) {
                addIndicator(currentSize);
                currentSize++;
            }
        } else if (currentSize > numPages) {
            needInvalidate = true;
            while (currentSize > numPages) {
                removeIndicator(currentSize - 1);
                currentSize--;
            }
        }
        this.mActiveDot = Math.min(this.mActiveDot, numPages - 1);
        if (this.mActiveDot != currentPage) {
            needInvalidate = true;
            ((ImageView) this.mDots.get(this.mActiveDot)).setSelected(false);
        }
        ((ImageView) this.mDots.get(currentPage)).setSelected(true);
        this.mActiveDot = currentPage;
        if (needInvalidate) {
            invalidate();
        }
    }
}
