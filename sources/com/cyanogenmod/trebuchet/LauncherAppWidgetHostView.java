package com.cyanogenmod.trebuchet;

import android.appwidget.AppWidgetHostView;
import android.appwidget.AppWidgetProviderInfo;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.StateListDrawable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import com.konka.ios7launcher.R;

public class LauncherAppWidgetHostView extends AppWidgetHostView {
    public static final int CHILDS_FOCUSABLE = 1;
    public static final int PARENT_FOCUSABLE = 0;
    private int mFocusableState = 0;
    /* access modifiers changed from: private */
    public boolean mHasPerformedLongPress;
    private LayoutInflater mInflater;
    private CheckForLongPress mPendingCheckForLongPress;

    class CheckForLongPress implements Runnable {
        private int mOriginalWindowAttachCount;

        CheckForLongPress() {
        }

        public void run() {
            if (LauncherAppWidgetHostView.this.mParent != null && LauncherAppWidgetHostView.this.hasWindowFocus() && this.mOriginalWindowAttachCount == LauncherAppWidgetHostView.this.getWindowAttachCount() && !LauncherAppWidgetHostView.this.mHasPerformedLongPress && LauncherAppWidgetHostView.this.performLongClick()) {
                LauncherAppWidgetHostView.this.mHasPerformedLongPress = true;
            }
        }

        public void rememberWindowAttachCount() {
            this.mOriginalWindowAttachCount = LauncherAppWidgetHostView.this.getWindowAttachCount();
        }
    }

    public LauncherAppWidgetHostView(Context context, AppWidgetProviderInfo appWidget) {
        super(context);
        this.mInflater = (LayoutInflater) context.getSystemService("layout_inflater");
        setFocusable(true);
        StateListDrawable sld = new StateListDrawable();
        sld.addState(new int[]{16842919}, getResources().getDrawable(R.drawable.home_selectbox_s));
        sld.addState(new int[]{16842908}, getResources().getDrawable(R.drawable.home_selectbox_s));
        sld.addState(new int[]{16843623}, getResources().getDrawable(R.drawable.app_hover));
        setBackgroundDrawable(sld);
    }

    /* access modifiers changed from: protected */
    public View getErrorView() {
        return this.mInflater.inflate(R.layout.appwidget_error, this, false);
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == 0) {
            switch (event.getKeyCode()) {
                case 4:
                    if (this.mFocusableState == 1 && requestParentFocus()) {
                        return true;
                    }
            }
        }
        return super.dispatchKeyEvent(event);
    }

    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (this.mHasPerformedLongPress) {
            this.mHasPerformedLongPress = false;
            return true;
        }
        switch (ev.getAction()) {
            case 0:
                postCheckForLongClick();
                return false;
            case 1:
            case 3:
                this.mHasPerformedLongPress = false;
                if (this.mPendingCheckForLongPress == null) {
                    return false;
                }
                removeCallbacks(this.mPendingCheckForLongPress);
                return false;
            default:
                return false;
        }
    }

    private void postCheckForLongClick() {
        this.mHasPerformedLongPress = false;
        if (this.mPendingCheckForLongPress == null) {
            this.mPendingCheckForLongPress = new CheckForLongPress();
        }
        this.mPendingCheckForLongPress.rememberWindowAttachCount();
        postDelayed(this.mPendingCheckForLongPress, (long) ViewConfiguration.getLongPressTimeout());
    }

    public void cancelLongPress() {
        super.cancelLongPress();
        this.mHasPerformedLongPress = false;
        if (this.mPendingCheckForLongPress != null) {
            removeCallbacks(this.mPendingCheckForLongPress);
        }
    }

    public int getDescendantFocusability() {
        switch (this.mFocusableState) {
            case 1:
                return 131072;
            default:
                return 393216;
        }
    }

    public boolean requestChildFocus() {
        boolean tookFocus = false;
        this.mFocusableState = 1;
        int count = getChildCount();
        for (int i = 0; i < count && !tookFocus; i++) {
            tookFocus = getChildAt(i).requestFocus();
        }
        return tookFocus;
    }

    public boolean requestParentFocus() {
        this.mFocusableState = 0;
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            child.clearFocus();
            child.setPressed(false);
        }
        return super.requestFocus();
    }

    public boolean requestFocus(int arg0, Rect arg1) {
        CellLayout cellLayout = (CellLayout) getParent().getParent();
        if (((Workspace) cellLayout.getParent()).isSearchScreen(cellLayout)) {
            return requestChildFocus();
        }
        this.mFocusableState = 0;
        return super.requestFocus(arg0, arg1);
    }
}
