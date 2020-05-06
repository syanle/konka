package com.cyanogenmod.trebuchet;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

public class PagedDialogGridView extends GridView {
    private static final String TAG = "PagedDialogGridView";
    private Runnable mLastOnLayoutListener;
    private Runnable mOnLayoutListener;

    public PagedDialogGridView(Context context) {
        this(context, null, 0);
    }

    public PagedDialogGridView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PagedDialogGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setDescendantFocusability(393216);
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.mOnLayoutListener = null;
        this.mLastOnLayoutListener = null;
    }

    public boolean isSetOnLayoutListener() {
        return this.mOnLayoutListener != null;
    }

    public void setOnLayoutListener(Runnable r) {
        this.mOnLayoutListener = r;
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (this.mOnLayoutListener != null) {
            this.mOnLayoutListener.run();
            this.mLastOnLayoutListener = this.mOnLayoutListener;
            this.mOnLayoutListener = null;
        }
    }
}
