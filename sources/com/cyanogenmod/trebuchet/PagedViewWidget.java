package com.cyanogenmod.trebuchet;

import android.animation.ObjectAnimator;
import android.appwidget.AppWidgetProviderInfo;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.cyanogenmod.trebuchet.RocketLauncher.Board.FlyingIcon;
import com.konka.ios7launcher.R;

public class PagedViewWidget extends LinearLayout implements Checkable {
    static final String TAG = "PagedViewWidgetLayout";
    private static boolean sDeletePreviewsWhenDetachedFromWindow = true;
    private int mAlpha;
    private float mCheckedAlpha;
    private ObjectAnimator mCheckedAlphaAnimator;
    private int mCheckedFadeInDuration;
    private int mCheckedFadeOutDuration;
    private String mDimensionsFormatString;
    private int mHolographicAlpha;
    private Bitmap mHolographicOutline;
    private HolographicOutlineHelper mHolographicOutlineHelper;
    private boolean mIsChecked;
    private final Paint mPaint;
    private ImageView mPreviewImageView;
    private final RectF mTmpScaleRect;

    public PagedViewWidget(Context context) {
        this(context, null);
    }

    public PagedViewWidget(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PagedViewWidget(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mPaint = new Paint();
        this.mTmpScaleRect = new RectF();
        this.mAlpha = 255;
        this.mCheckedAlpha = 1.0f;
        Resources r = context.getResources();
        if (r.getInteger(R.integer.config_dragAppsCustomizeIconFadeAlpha) > 0) {
            this.mCheckedAlpha = ((float) r.getInteger(R.integer.config_dragAppsCustomizeIconFadeAlpha)) / 256.0f;
            this.mCheckedFadeInDuration = r.getInteger(R.integer.config_dragAppsCustomizeIconFadeInDuration);
            this.mCheckedFadeOutDuration = r.getInteger(R.integer.config_dragAppsCustomizeIconFadeOutDuration);
        }
        this.mDimensionsFormatString = r.getString(R.string.widget_dims_format);
        setWillNotDraw(false);
        setClipToPadding(false);
    }

    public static void setDeletePreviewsWhenDetachedFromWindow(boolean value) {
        sDeletePreviewsWhenDetachedFromWindow = value;
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (sDeletePreviewsWhenDetachedFromWindow) {
            ImageView image = (ImageView) findViewById(R.id.widget_preview);
            if (image != null) {
                FastBitmapDrawable preview = (FastBitmapDrawable) image.getDrawable();
                if (!(preview == null || preview.getBitmap() == null)) {
                    preview.getBitmap().recycle();
                }
                image.setImageDrawable(null);
            }
        }
    }

    public void applyFromAppWidgetProviderInfo(AppWidgetProviderInfo info, int maxWidth, int[] cellSpan, HolographicOutlineHelper holoOutlineHelper) {
        this.mHolographicOutlineHelper = holoOutlineHelper;
        ImageView image = (ImageView) findViewById(R.id.widget_preview);
        if (maxWidth > -1) {
            image.setMaxWidth(maxWidth);
        }
        image.setContentDescription(info.label);
        this.mPreviewImageView = image;
        ((TextView) findViewById(R.id.widget_name)).setText(info.label);
        TextView dims = (TextView) findViewById(R.id.widget_dims);
        if (dims != null) {
            dims.setText(String.format(this.mDimensionsFormatString, new Object[]{Integer.valueOf(cellSpan[0]), Integer.valueOf(cellSpan[1])}));
        }
    }

    public void applyFromResolveInfo(PackageManager pm, ResolveInfo info, HolographicOutlineHelper holoOutlineHelper) {
        this.mHolographicOutlineHelper = holoOutlineHelper;
        CharSequence label = info.loadLabel(pm);
        ImageView image = (ImageView) findViewById(R.id.widget_preview);
        image.setContentDescription(label);
        this.mPreviewImageView = image;
        ((TextView) findViewById(R.id.widget_name)).setText(label);
        TextView dims = (TextView) findViewById(R.id.widget_dims);
        if (dims != null) {
            dims.setText(String.format(this.mDimensionsFormatString, new Object[]{Integer.valueOf(1), Integer.valueOf(1)}));
        }
    }

    public int[] getPreviewSize() {
        ImageView i = (ImageView) findViewById(R.id.widget_preview);
        return new int[]{(i.getWidth() - i.getPaddingLeft()) - i.getPaddingRight(), (i.getHeight() - i.getPaddingBottom()) - i.getPaddingTop()};
    }

    /* access modifiers changed from: 0000 */
    public void applyPreview(FastBitmapDrawable preview) {
        PagedViewWidgetImageView image = (PagedViewWidgetImageView) findViewById(R.id.widget_preview);
        if (preview != null) {
            image.mAllowRequestLayout = false;
            image.setImageDrawable(preview);
            image.setAlpha(1.0f);
            image.mAllowRequestLayout = true;
        }
    }

    public void setHolographicOutline(Bitmap holoOutline) {
        this.mHolographicOutline = holoOutline;
        invalidate();
    }

    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        return true;
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        if (this.mAlpha > 0) {
            super.onDraw(canvas);
        }
        if (this.mHolographicOutline != null && this.mHolographicAlpha > 0) {
            this.mTmpScaleRect.set(FlyingIcon.ANGULAR_VMIN, FlyingIcon.ANGULAR_VMIN, 1.0f, 1.0f);
            this.mPreviewImageView.getImageMatrix().mapRect(this.mTmpScaleRect);
            this.mPaint.setAlpha(this.mHolographicAlpha);
            canvas.save();
            canvas.scale(this.mTmpScaleRect.right, this.mTmpScaleRect.bottom);
            canvas.drawBitmap(this.mHolographicOutline, (float) this.mPreviewImageView.getLeft(), (float) this.mPreviewImageView.getTop(), this.mPaint);
            canvas.restore();
        }
    }

    /* access modifiers changed from: protected */
    public boolean onSetAlpha(int alpha) {
        return true;
    }

    private void setChildrenAlpha(float alpha) {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            getChildAt(i).setAlpha(alpha);
        }
    }

    public void setAlpha(float alpha) {
        float viewAlpha = HolographicOutlineHelper.viewAlphaInterpolator(alpha);
        int newViewAlpha = (int) (viewAlpha * 255.0f);
        int newHolographicAlpha = (int) (HolographicOutlineHelper.highlightAlphaInterpolator(alpha) * 255.0f);
        if (this.mAlpha != newViewAlpha || this.mHolographicAlpha != newHolographicAlpha) {
            this.mAlpha = newViewAlpha;
            this.mHolographicAlpha = newHolographicAlpha;
            setChildrenAlpha(viewAlpha);
            super.setAlpha(viewAlpha);
        }
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

    public boolean isChecked() {
        return this.mIsChecked;
    }

    public void toggle() {
        setChecked(!this.mIsChecked);
    }
}
