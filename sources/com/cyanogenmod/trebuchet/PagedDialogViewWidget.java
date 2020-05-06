package com.cyanogenmod.trebuchet;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class PagedDialogViewWidget extends PagedViewWidget {
    public PagedDialogViewWidget(Context context) {
        this(context, null);
    }

    public PagedDialogViewWidget(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PagedDialogViewWidget(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setDescendantFocusability(393216);
    }

    public boolean onTouchEvent(MotionEvent event) {
        return false;
    }
}
