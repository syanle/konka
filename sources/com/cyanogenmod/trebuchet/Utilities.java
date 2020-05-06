package com.cyanogenmod.trebuchet;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BlurMaskFilter;
import android.graphics.BlurMaskFilter.Blur;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Rect;
import android.graphics.TableMaskFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PaintDrawable;
import com.cyanogenmod.trebuchet.RocketLauncher.Board.FlyingIcon;
import com.konka.ios7launcher.R;

final class Utilities {
    private static final String TAG = "Launcher.Utilities";
    private static final Paint sBlurPaint = new Paint();
    private static final Canvas sCanvas = new Canvas();
    static int sColorIndex = 0;
    static int[] sColors = {-65536, -16711936, -16776961};
    private static final Paint sDisabledPaint = new Paint();
    private static final Paint sGlowColorFocusedPaint = new Paint();
    private static final Paint sGlowColorPressedPaint = new Paint();
    private static int sIconHeight = -1;
    private static int sIconTextureHeight = -1;
    private static int sIconTextureWidth = -1;
    private static int sIconWidth = -1;
    private static final Rect sOldBounds = new Rect();

    Utilities() {
    }

    static {
        sCanvas.setDrawFilter(new PaintFlagsDrawFilter(4, 2));
    }

    static Bitmap ComposeTwoBitmap(Bitmap background, Bitmap icon) {
        int left;
        int top;
        if (background == null) {
            return icon;
        }
        Bitmap newBitmap = Bitmap.createBitmap(background);
        Canvas canvas = new Canvas(newBitmap);
        Paint paint = new Paint();
        int w_a = background.getWidth();
        int h_a = background.getHeight();
        int w_b = icon.getWidth();
        int h_b = icon.getHeight();
        if (w_a - w_b >= 0) {
            left = (w_a - w_b) / 2;
        } else {
            left = 0;
        }
        if (h_a - h_b >= 0) {
            top = (h_a - h_b) / 2;
        } else {
            top = 0;
        }
        canvas.drawBitmap(icon, new Rect(0, 0, w_b, h_b), new Rect(left, top, left + w_b, top + h_b), paint);
        canvas.save(31);
        canvas.restore();
        return newBitmap;
    }

    static Bitmap createIconBitmap(Bitmap icon, Context context) {
        int textureWidth = sIconTextureWidth;
        int textureHeight = sIconTextureHeight;
        int sourceWidth = icon.getWidth();
        int sourceHeight = icon.getHeight();
        if (sourceWidth <= textureWidth || sourceHeight <= textureHeight) {
            return (sourceWidth == textureWidth && sourceHeight == textureHeight) ? icon : createIconBitmap((Drawable) new BitmapDrawable(icon), context);
        }
        return Bitmap.createBitmap(icon, (sourceWidth - textureWidth) / 2, (sourceHeight - textureHeight) / 2, textureWidth, textureHeight);
    }

    static Bitmap createIconBitmap(Drawable icon, Context context) {
        Bitmap bitmap;
        synchronized (sCanvas) {
            if (sIconWidth == -1) {
                initStatics(context);
            }
            int width = sIconWidth;
            int height = sIconHeight;
            if (icon instanceof PaintDrawable) {
                PaintDrawable painter = (PaintDrawable) icon;
                painter.setIntrinsicWidth(width);
                painter.setIntrinsicHeight(height);
            } else if (icon instanceof BitmapDrawable) {
                BitmapDrawable bitmapDrawable = (BitmapDrawable) icon;
                if (bitmapDrawable.getBitmap().getDensity() == 0) {
                    bitmapDrawable.setTargetDensity(context.getResources().getDisplayMetrics());
                }
            }
            int sourceWidth = icon.getIntrinsicWidth();
            int sourceHeight = icon.getIntrinsicHeight();
            if (sourceWidth > 0 && sourceHeight > 0) {
                if (width < sourceWidth || height < sourceHeight) {
                    float ratio = ((float) sourceWidth) / ((float) sourceHeight);
                    if (sourceWidth > sourceHeight) {
                        height = (int) (((float) width) / ratio);
                    } else if (sourceHeight > sourceWidth) {
                        width = (int) (((float) height) * ratio);
                    }
                } else if (sourceWidth < width && sourceHeight < height) {
                    width = sourceWidth;
                    height = sourceHeight;
                }
            }
            int textureWidth = sIconTextureWidth;
            int textureHeight = sIconTextureHeight;
            bitmap = Bitmap.createBitmap(textureWidth, textureHeight, Config.ARGB_8888);
            Canvas canvas = sCanvas;
            canvas.setBitmap(bitmap);
            int left = (textureWidth - width) / 2;
            int top = (textureHeight - height) / 2;
            sOldBounds.set(icon.getBounds());
            icon.setBounds(left, top, left + width, top + height);
            icon.draw(canvas);
            icon.setBounds(sOldBounds);
            canvas.setBitmap(null);
        }
        return bitmap;
    }

    static Bitmap resampleIconBitmap(Bitmap bitmap, Context context) {
        synchronized (sCanvas) {
            if (sIconWidth == -1) {
                initStatics(context);
            }
            if (bitmap.getWidth() != sIconWidth || bitmap.getHeight() != sIconHeight) {
                bitmap = createIconBitmap((Drawable) new BitmapDrawable(bitmap), context);
            }
        }
        return bitmap;
    }

    static Bitmap drawDisabledBitmap(Bitmap bitmap, Context context) {
        Bitmap disabled;
        synchronized (sCanvas) {
            if (sIconWidth == -1) {
                initStatics(context);
            }
            disabled = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
            Canvas canvas = sCanvas;
            canvas.setBitmap(disabled);
            canvas.drawBitmap(bitmap, FlyingIcon.ANGULAR_VMIN, FlyingIcon.ANGULAR_VMIN, sDisabledPaint);
            canvas.setBitmap(null);
        }
        return disabled;
    }

    private static void initStatics(Context context) {
        Resources resources = context.getResources();
        float density = resources.getDisplayMetrics().density;
        int dimension = (int) resources.getDimension(R.dimen.app_icon_size);
        sIconHeight = dimension;
        sIconWidth = dimension;
        int i = sIconWidth;
        sIconTextureHeight = i;
        sIconTextureWidth = i;
        sBlurPaint.setMaskFilter(new BlurMaskFilter(5.0f * density, Blur.NORMAL));
        sGlowColorPressedPaint.setColor(-15616);
        sGlowColorPressedPaint.setMaskFilter(TableMaskFilter.CreateClipTable(0, 30));
        sGlowColorFocusedPaint.setColor(-29184);
        sGlowColorFocusedPaint.setMaskFilter(TableMaskFilter.CreateClipTable(0, 30));
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0.2f);
        sDisabledPaint.setColorFilter(new ColorMatrixColorFilter(cm));
        sDisabledPaint.setAlpha(136);
    }
}
