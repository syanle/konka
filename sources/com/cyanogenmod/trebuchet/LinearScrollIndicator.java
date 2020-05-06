package com.cyanogenmod.trebuchet;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.ImageView;
import com.cyanogenmod.trebuchet.RocketLauncher.Board.FlyingIcon;
import com.konka.ios7launcher.R;

public class LinearScrollIndicator extends ImageView implements ScrollIndicator {
    private ValueAnimator mAnimator;
    private int mPaddingLeft;
    private int mPaddingRight;
    private PagedView mPagedview;

    public LinearScrollIndicator(Context context) {
        this(context, null, 0);
    }

    public LinearScrollIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LinearScrollIndicator(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.PagedView, defStyle, 0);
        this.mPaddingLeft = a.getDimensionPixelSize(7, 0);
        this.mPaddingRight = a.getDimensionPixelSize(8, 0);
        a.recycle();
    }

    public void init(PagedView pagedView) {
        this.mPagedview = pagedView;
    }

    public void show(boolean immediately, int duration) {
        updateScrollingIndicatorPosition();
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
        updateScrollingIndicatorPosition();
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
                    LinearScrollIndicator.this.setVisibility(4);
                }
            }
        });
        this.mAnimator.start();
    }

    public void update() {
        updateScrollingIndicatorPosition();
    }

    public void cancelAnimations() {
        if (this.mAnimator != null) {
            this.mAnimator.cancel();
        }
    }

    public boolean isElasticScrollIndicator() {
        return true;
    }

    private void updateScrollingIndicatorPosition() {
        int numPages = this.mPagedview.getChildCount();
        int pageWidth = this.mPagedview.getMeasuredWidth();
        int lastChildIndex = Math.max(0, this.mPagedview.getChildCount() - 1);
        int trackWidth = (pageWidth - this.mPaddingLeft) - this.mPaddingRight;
        int indicatorWidth = (getMeasuredWidth() - getPaddingLeft()) - getPaddingRight();
        int indicatorSpace = trackWidth / numPages;
        float f = (float) (trackWidth - indicatorSpace);
        int indicatorPos = ((int) (f * Math.max(FlyingIcon.ANGULAR_VMIN, Math.min(1.0f, ((float) this.mPagedview.getScrollX()) / ((float) (this.mPagedview.getChildOffset(lastChildIndex) - this.mPagedview.getRelativeChildOffset(lastChildIndex))))))) + this.mPaddingLeft;
        if (!isElasticScrollIndicator()) {
            indicatorPos += (indicatorSpace / 2) - (indicatorWidth / 2);
        } else if (getMeasuredWidth() != indicatorSpace) {
            getLayoutParams().width = indicatorSpace;
            requestLayout();
        }
        setTranslationX((float) indicatorPos);
        invalidate();
    }
}
