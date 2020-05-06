package net.londatiga.android;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;

public class PopupWindows {
    protected Drawable mBackground = null;
    protected Context mContext;
    protected LayoutInflater mInflater;
    protected View mRootView;
    protected PopupWindow mWindow;
    protected WindowManager mWindowManager;

    public PopupWindows(Context context) {
        this.mInflater = (LayoutInflater) context.getSystemService("layout_inflater");
        this.mContext = context;
        this.mWindow = new PopupWindow(context);
        this.mWindow.setTouchable(true);
        this.mWindow.setFocusable(true);
        this.mWindow.setOutsideTouchable(true);
        this.mWindow.setTouchInterceptor(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() != 4) {
                    return false;
                }
                PopupWindows.this.mWindow.dismiss();
                return true;
            }
        });
        this.mWindowManager = (WindowManager) context.getSystemService("window");
    }

    /* access modifiers changed from: protected */
    public void onShow() {
    }

    /* access modifiers changed from: protected */
    public void preShow() {
        if (this.mRootView == null) {
            throw new IllegalStateException("setContentView was not called with a view to display.");
        }
        onShow();
        if (this.mBackground == null) {
            this.mWindow.setBackgroundDrawable(new ColorDrawable(17170445));
        } else {
            this.mWindow.setBackgroundDrawable(this.mBackground);
        }
        this.mWindow.setContentView(this.mRootView);
    }

    public void showAtLocation(View parent, int gravity, int x, int y) {
        preShow();
        this.mRootView.measure(MeasureSpec.makeMeasureSpec(0, 0), MeasureSpec.makeMeasureSpec(0, 0));
        setMeasureSpecs(MeasureSpec.makeMeasureSpec(this.mRootView.getMeasuredWidth(), 1073741824), MeasureSpec.makeMeasureSpec(this.mRootView.getMeasuredHeight(), 1073741824));
        this.mWindow.showAtLocation(parent, gravity, x, y);
        this.mRootView.requestFocus();
    }

    /* access modifiers changed from: protected */
    public void setMeasureSpecs(int widthSpec, int heightSpec) {
        this.mWindow.setWidth(widthSpec);
        this.mWindow.setHeight(heightSpec);
    }

    public Context getContext() {
        return this.mContext;
    }

    public View getContentView() {
        return this.mRootView;
    }

    public void setBackgroundDrawable(Drawable background) {
        this.mBackground = background;
    }

    private void initContentView(View content, LayoutParams params) {
        this.mRootView = content;
        if (params != null) {
            this.mRootView.setLayoutParams(params);
        } else {
            this.mRootView.setLayoutParams(new LayoutParams(-2, -2));
        }
        this.mWindow.setContentView(this.mRootView);
    }

    public void setContentView(int resId) {
        initContentView(this.mInflater.inflate(resId, null), null);
    }

    public void setContentView(int resId, LayoutParams params) {
        initContentView(this.mInflater.inflate(resId, null), params);
    }

    public void setContentView(View content) {
        initContentView(content, null);
    }

    public void setContentView(View content, LayoutParams params) {
        initContentView(content, params);
    }

    public void setOnDismissListener(OnDismissListener listener) {
        this.mWindow.setOnDismissListener(listener);
    }

    public void dismiss() {
        this.mWindow.dismiss();
    }
}
