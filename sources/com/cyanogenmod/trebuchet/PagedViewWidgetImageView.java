package com.cyanogenmod.trebuchet;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

class PagedViewWidgetImageView extends ImageView {
    public boolean mAllowRequestLayout = true;

    public PagedViewWidgetImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void requestLayout() {
        if (this.mAllowRequestLayout) {
            super.requestLayout();
        }
    }
}
