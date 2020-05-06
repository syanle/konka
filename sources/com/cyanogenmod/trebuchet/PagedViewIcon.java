package com.cyanogenmod.trebuchet;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.Checkable;
import android.widget.TextView;
import com.konka.ios7launcher.R;

public class PagedViewIcon extends TextView implements Checkable {
    private static final String TAG = "PagedViewIcon";
    private int mAlpha;
    private float mCheckedAlpha;
    private ObjectAnimator mCheckedAlphaAnimator;
    private int mCheckedFadeInDuration;
    private int mCheckedFadeOutDuration;
    private Bitmap mCheckedOutline;
    private int mHolographicAlpha;
    private Bitmap mHolographicOutline;
    private HolographicOutlineHelper mHolographicOutlineHelper;
    HolographicPagedViewIcon mHolographicOutlineView;
    private Bitmap mIcon;
    private boolean mIsChecked;
    private final Paint mPaint;

    public PagedViewIcon(Context context) {
        this(context, null);
    }

    public PagedViewIcon(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PagedViewIcon(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mPaint = new Paint();
        this.mAlpha = 255;
        this.mCheckedAlpha = 1.0f;
        Resources r = context.getResources();
        if (r.getInteger(R.integer.config_dragAppsCustomizeIconFadeAlpha) > 0) {
            this.mCheckedAlpha = ((float) r.getInteger(R.integer.config_dragAppsCustomizeIconFadeAlpha)) / 256.0f;
            this.mCheckedFadeInDuration = r.getInteger(R.integer.config_dragAppsCustomizeIconFadeInDuration);
            this.mCheckedFadeOutDuration = r.getInteger(R.integer.config_dragAppsCustomizeIconFadeOutDuration);
        }
        this.mHolographicOutlineView = new HolographicPagedViewIcon(context, this);
    }

    /* access modifiers changed from: protected */
    public HolographicPagedViewIcon getHolographicOutlineView() {
        return this.mHolographicOutlineView;
    }

    /* access modifiers changed from: protected */
    public Bitmap getHolographicOutline() {
        return this.mHolographicOutline;
    }

    public void applyFromApplicationInfo(ApplicationInfo info, HolographicOutlineHelper holoOutlineHelper) {
        this.mHolographicOutlineHelper = holoOutlineHelper;
        this.mIcon = info.iconBitmap;
        setCompoundDrawablesWithIntrinsicBounds(null, new FastBitmapDrawable(this.mIcon), null, null);
        setText(info.title);
        setTag(info);
    }

    public void setHolographicOutline(Bitmap holoOutline) {
        this.mHolographicOutline = holoOutline;
        getHolographicOutlineView().invalidate();
    }

    public void setAlpha(float alpha) {
        float viewAlpha = HolographicOutlineHelper.viewAlphaInterpolator(alpha);
        int newViewAlpha = (int) (viewAlpha * 255.0f);
        int newHolographicAlpha = (int) (HolographicOutlineHelper.highlightAlphaInterpolator(alpha) * 255.0f);
        if (this.mAlpha != newViewAlpha || this.mHolographicAlpha != newHolographicAlpha) {
            this.mAlpha = newViewAlpha;
            this.mHolographicAlpha = newHolographicAlpha;
            super.setAlpha(viewAlpha);
        }
    }

    public void invalidateCheckedImage() {
        if (this.mCheckedOutline != null) {
            this.mCheckedOutline.recycle();
            this.mCheckedOutline = null;
        }
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        if (this.mAlpha > 0) {
            super.onDraw(canvas);
        }
        Bitmap overlay = null;
        if (this.mCheckedOutline != null) {
            this.mPaint.setAlpha(255);
            overlay = this.mCheckedOutline;
        }
        if (overlay != null) {
            int offset = getScrollX();
            int compoundPaddingLeft = getCompoundPaddingLeft();
            canvas.drawBitmap(overlay, (float) (offset + compoundPaddingLeft + ((((getWidth() - getCompoundPaddingRight()) - compoundPaddingLeft) - overlay.getWidth()) / 2)), (float) this.mPaddingTop, this.mPaint);
        }
    }

    public boolean isChecked() {
        return this.mIsChecked;
    }

    /* access modifiers changed from: 0000 */
    public void setChecked(boolean checked, boolean animate) {
        float alpha;
        int duration;
        if (this.mIsChecked != checked) {
            this.mIsChecked = checked;
            if (this.mIsChecked) {
                alpha = this.mCheckedAlpha;
                duration = this.mCheckedFadeInDuration;
            } else {
                alpha = 1.0f;
                duration = this.mCheckedFadeOutDuration;
            }
            if (this.mCheckedAlphaAnimator != null) {
                this.mCheckedAlphaAnimator.cancel();
            }
            if (animate) {
                this.mCheckedAlphaAnimator = ObjectAnimator.ofFloat(this, "alpha", new float[]{getAlpha(), alpha});
                this.mCheckedAlphaAnimator.setDuration((long) duration);
                this.mCheckedAlphaAnimator.start();
            } else {
                setAlpha(alpha);
            }
            invalidate();
        }
    }

    public void setChecked(boolean checked) {
        setChecked(checked, true);
    }

    public void toggle() {
        setChecked(!this.mIsChecked);
    }
}
