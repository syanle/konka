package com.cyanogenmod.trebuchet;

import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.BlurMaskFilter.Blur;
import android.graphics.Canvas;
import android.graphics.MaskFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.TableMaskFilter;
import com.cyanogenmod.trebuchet.RocketLauncher.Board.FlyingIcon;

public class HolographicOutlineHelper {
    private static final int EXTRA_THICK = 2;
    public static final int MAX_OUTER_BLUR_RADIUS;
    private static final int MEDIUM = 1;
    public static final int MIN_OUTER_BLUR_RADIUS;
    private static final int THICK = 0;
    private static final MaskFilter sCoarseClipTable = TableMaskFilter.CreateClipTable(0, 200);
    private static final BlurMaskFilter sExtraThickInnerBlurMaskFilter;
    private static final BlurMaskFilter sExtraThickOuterBlurMaskFilter;
    private static final BlurMaskFilter sMediumInnerBlurMaskFilter;
    private static final BlurMaskFilter sMediumOuterBlurMaskFilter;
    private static final BlurMaskFilter sThickInnerBlurMaskFilter;
    private static final BlurMaskFilter sThickOuterBlurMaskFilter;
    private static final BlurMaskFilter sThinOuterBlurMaskFilter;
    private final Paint mAlphaClipPaint = new Paint();
    private final Paint mBlurPaint = new Paint();
    private final Paint mErasePaint = new Paint();
    private final Paint mHolographicPaint = new Paint();
    private int[] mTempOffset = new int[2];

    static {
        float scale = LauncherApplication.getScreenDensity();
        MIN_OUTER_BLUR_RADIUS = (int) (scale * 1.0f);
        MAX_OUTER_BLUR_RADIUS = (int) (scale * 12.0f);
        sExtraThickOuterBlurMaskFilter = new BlurMaskFilter(12.0f * scale, Blur.OUTER);
        sThickOuterBlurMaskFilter = new BlurMaskFilter(scale * 6.0f, Blur.OUTER);
        sMediumOuterBlurMaskFilter = new BlurMaskFilter(scale * 2.0f, Blur.OUTER);
        sThinOuterBlurMaskFilter = new BlurMaskFilter(scale * 1.0f, Blur.OUTER);
        sExtraThickInnerBlurMaskFilter = new BlurMaskFilter(scale * 6.0f, Blur.NORMAL);
        sThickInnerBlurMaskFilter = new BlurMaskFilter(4.0f * scale, Blur.NORMAL);
        sMediumInnerBlurMaskFilter = new BlurMaskFilter(scale * 2.0f, Blur.NORMAL);
    }

    HolographicOutlineHelper() {
        this.mHolographicPaint.setFilterBitmap(true);
        this.mHolographicPaint.setAntiAlias(true);
        this.mBlurPaint.setFilterBitmap(true);
        this.mBlurPaint.setAntiAlias(true);
        this.mErasePaint.setXfermode(new PorterDuffXfermode(Mode.DST_OUT));
        this.mErasePaint.setFilterBitmap(true);
        this.mErasePaint.setAntiAlias(true);
        this.mAlphaClipPaint.setMaskFilter(TableMaskFilter.CreateClipTable(180, 255));
    }

    public static float highlightAlphaInterpolator(float r) {
        return (float) Math.pow((double) ((1.0f - r) * 0.6f), 1.5d);
    }

    public static float viewAlphaInterpolator(float r) {
        if (r < 0.95f) {
            return (float) Math.pow((double) (r / 0.95f), 1.5d);
        }
        return 1.0f;
    }

    /* access modifiers changed from: 0000 */
    public void applyOuterBlur(Bitmap bitmap, Canvas canvas, int color) {
        this.mBlurPaint.setMaskFilter(sThickOuterBlurMaskFilter);
        Bitmap glow = bitmap.extractAlpha(this.mBlurPaint, this.mTempOffset);
        this.mHolographicPaint.setMaskFilter(sCoarseClipTable);
        this.mHolographicPaint.setAlpha(150);
        this.mHolographicPaint.setColor(color);
        canvas.drawBitmap(glow, (float) this.mTempOffset[0], (float) this.mTempOffset[1], this.mHolographicPaint);
        glow.recycle();
    }

    /* access modifiers changed from: 0000 */
    public void applyExpensiveOutlineWithBlur(Bitmap srcDst, Canvas srcDstCanvas, int color, int outlineColor, int thickness) {
        applyExpensiveOutlineWithBlur(srcDst, srcDstCanvas, color, outlineColor, this.mAlphaClipPaint, thickness);
    }

    /* access modifiers changed from: 0000 */
    public void applyExpensiveOutlineWithBlur(Bitmap srcDst, Canvas srcDstCanvas, int color, int outlineColor, Paint alphaClipPaint, int thickness) {
        BlurMaskFilter outerBlurMaskFilter;
        BlurMaskFilter innerBlurMaskFilter;
        if (alphaClipPaint == null) {
            alphaClipPaint = this.mAlphaClipPaint;
        }
        Bitmap glowShape = srcDst.extractAlpha(alphaClipPaint, this.mTempOffset);
        switch (thickness) {
            case 0:
                outerBlurMaskFilter = sThickOuterBlurMaskFilter;
                break;
            case 1:
                outerBlurMaskFilter = sMediumOuterBlurMaskFilter;
                break;
            case 2:
                outerBlurMaskFilter = sExtraThickOuterBlurMaskFilter;
                break;
            default:
                throw new RuntimeException("Invalid blur thickness");
        }
        this.mBlurPaint.setMaskFilter(outerBlurMaskFilter);
        int[] outerBlurOffset = new int[2];
        Bitmap thickOuterBlur = glowShape.extractAlpha(this.mBlurPaint, outerBlurOffset);
        if (thickness == 2) {
            this.mBlurPaint.setMaskFilter(sMediumOuterBlurMaskFilter);
        } else {
            this.mBlurPaint.setMaskFilter(sThinOuterBlurMaskFilter);
        }
        int[] brightOutlineOffset = new int[2];
        Bitmap brightOutline = glowShape.extractAlpha(this.mBlurPaint, brightOutlineOffset);
        srcDstCanvas.setBitmap(glowShape);
        srcDstCanvas.drawColor(-16777216, Mode.SRC_OUT);
        switch (thickness) {
            case 0:
                innerBlurMaskFilter = sThickInnerBlurMaskFilter;
                break;
            case 1:
                innerBlurMaskFilter = sMediumInnerBlurMaskFilter;
                break;
            case 2:
                innerBlurMaskFilter = sExtraThickInnerBlurMaskFilter;
                break;
            default:
                throw new RuntimeException("Invalid blur thickness");
        }
        this.mBlurPaint.setMaskFilter(innerBlurMaskFilter);
        int[] thickInnerBlurOffset = new int[2];
        Bitmap thickInnerBlur = glowShape.extractAlpha(this.mBlurPaint, thickInnerBlurOffset);
        srcDstCanvas.setBitmap(thickInnerBlur);
        srcDstCanvas.drawBitmap(glowShape, (float) (-thickInnerBlurOffset[0]), (float) (-thickInnerBlurOffset[1]), this.mErasePaint);
        srcDstCanvas.drawRect(FlyingIcon.ANGULAR_VMIN, FlyingIcon.ANGULAR_VMIN, (float) (-thickInnerBlurOffset[0]), (float) thickInnerBlur.getHeight(), this.mErasePaint);
        srcDstCanvas.drawRect(FlyingIcon.ANGULAR_VMIN, FlyingIcon.ANGULAR_VMIN, (float) thickInnerBlur.getWidth(), (float) (-thickInnerBlurOffset[1]), this.mErasePaint);
        srcDstCanvas.setBitmap(srcDst);
        srcDstCanvas.drawColor(0, Mode.CLEAR);
        this.mHolographicPaint.setColor(color);
        srcDstCanvas.drawBitmap(thickInnerBlur, (float) thickInnerBlurOffset[0], (float) thickInnerBlurOffset[1], this.mHolographicPaint);
        srcDstCanvas.drawBitmap(thickOuterBlur, (float) outerBlurOffset[0], (float) outerBlurOffset[1], this.mHolographicPaint);
        this.mHolographicPaint.setColor(outlineColor);
        srcDstCanvas.drawBitmap(brightOutline, (float) brightOutlineOffset[0], (float) brightOutlineOffset[1], this.mHolographicPaint);
        srcDstCanvas.setBitmap(null);
        brightOutline.recycle();
        thickOuterBlur.recycle();
        thickInnerBlur.recycle();
        glowShape.recycle();
    }

    /* access modifiers changed from: 0000 */
    public void applyExtraThickExpensiveOutlineWithBlur(Bitmap srcDst, Canvas srcDstCanvas, int color, int outlineColor) {
        applyExpensiveOutlineWithBlur(srcDst, srcDstCanvas, color, outlineColor, 2);
    }

    /* access modifiers changed from: 0000 */
    public void applyThickExpensiveOutlineWithBlur(Bitmap srcDst, Canvas srcDstCanvas, int color, int outlineColor) {
        applyExpensiveOutlineWithBlur(srcDst, srcDstCanvas, color, outlineColor, 0);
    }

    /* access modifiers changed from: 0000 */
    public void applyMediumExpensiveOutlineWithBlur(Bitmap srcDst, Canvas srcDstCanvas, int color, int outlineColor, Paint alphaClipPaint) {
        applyExpensiveOutlineWithBlur(srcDst, srcDstCanvas, color, outlineColor, alphaClipPaint, 1);
    }

    /* access modifiers changed from: 0000 */
    public void applyMediumExpensiveOutlineWithBlur(Bitmap srcDst, Canvas srcDstCanvas, int color, int outlineColor) {
        applyExpensiveOutlineWithBlur(srcDst, srcDstCanvas, color, outlineColor, 1);
    }
}
