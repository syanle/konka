package com.cyanogenmod.trebuchet;

import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.animation.DecelerateInterpolator;
import com.cyanogenmod.trebuchet.DragLayer.LayoutParams;
import com.cyanogenmod.trebuchet.RocketLauncher.Board.FlyingIcon;
import com.konka.ios7launcher.R;

public class DragView extends View {
    ValueAnimator mAnim;
    private Bitmap mBitmap;
    /* access modifiers changed from: private */
    public DragLayer mDragLayer = null;
    private Rect mDragRegion = null;
    private Point mDragVisualizeOffset = null;
    private boolean mHasDrawn = false;
    /* access modifiers changed from: private */
    public LayoutParams mLayoutParams;
    /* access modifiers changed from: private */
    public float mOffsetX = FlyingIcon.ANGULAR_VMIN;
    /* access modifiers changed from: private */
    public float mOffsetY = FlyingIcon.ANGULAR_VMIN;
    private Paint mPaint;
    private int mRegistrationX;
    private int mRegistrationY;

    public DragView(Launcher launcher, Bitmap bitmap, int registrationX, int registrationY, int left, int top, int width, int height) {
        super(launcher);
        this.mDragLayer = launcher.getDragLayer();
        Resources res = getResources();
        int dragScale = res.getInteger(R.integer.config_dragViewExtraPixels);
        Matrix scale = new Matrix();
        float scaleFactor = (float) ((width + dragScale) / width);
        if (scaleFactor != 1.0f) {
            scale.setScale(scaleFactor, scaleFactor);
        }
        final int offsetX = res.getDimensionPixelSize(R.dimen.dragViewOffsetX);
        final int offsetY = res.getDimensionPixelSize(R.dimen.dragViewOffsetY);
        this.mAnim = ValueAnimator.ofFloat(new float[]{FlyingIcon.ANGULAR_VMIN, 1.0f});
        this.mAnim.setDuration(110);
        this.mAnim.setInterpolator(new DecelerateInterpolator(2.5f));
        this.mAnim.addUpdateListener(new AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = ((Float) animation.getAnimatedValue()).floatValue();
                int deltaX = (int) ((((float) offsetX) * value) - DragView.this.mOffsetX);
                int deltaY = (int) ((((float) offsetY) * value) - DragView.this.mOffsetY);
                DragView dragView = DragView.this;
                dragView.mOffsetX = dragView.mOffsetX + ((float) deltaX);
                DragView dragView2 = DragView.this;
                dragView2.mOffsetY = dragView2.mOffsetY + ((float) deltaY);
                if (DragView.this.getParent() == null) {
                    animation.cancel();
                    return;
                }
                LayoutParams lp = DragView.this.mLayoutParams;
                lp.x += deltaX;
                lp.y += deltaY;
                DragView.this.mDragLayer.requestLayout();
            }
        });
        this.mBitmap = Bitmap.createBitmap(bitmap, left, top, width, height, scale, true);
        setDragRegion(new Rect(0, 0, width, height));
        this.mRegistrationX = registrationX;
        this.mRegistrationY = registrationY;
        int ms = MeasureSpec.makeMeasureSpec(0, 0);
        measure(ms, ms);
    }

    public float getOffsetY() {
        return this.mOffsetY;
    }

    public int getDragRegionLeft() {
        return this.mDragRegion.left;
    }

    public int getDragRegionTop() {
        return this.mDragRegion.top;
    }

    public int getDragRegionWidth() {
        return this.mDragRegion.width();
    }

    public int getDragRegionHeight() {
        return this.mDragRegion.height();
    }

    public void setDragVisualizeOffset(Point p) {
        this.mDragVisualizeOffset = p;
    }

    public Point getDragVisualizeOffset() {
        return this.mDragVisualizeOffset;
    }

    public void setDragRegion(Rect r) {
        this.mDragRegion = r;
    }

    public Rect getDragRegion() {
        return this.mDragRegion;
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(this.mBitmap.getWidth(), this.mBitmap.getHeight());
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        this.mHasDrawn = true;
        canvas.drawBitmap(this.mBitmap, FlyingIcon.ANGULAR_VMIN, FlyingIcon.ANGULAR_VMIN, this.mPaint);
    }

    public void setPaint(Paint paint) {
        this.mPaint = paint;
        invalidate();
    }

    public boolean hasDrawn() {
        return this.mHasDrawn;
    }

    public void setAlpha(float alpha) {
        super.setAlpha(alpha);
        if (this.mPaint == null) {
            this.mPaint = new Paint();
        }
        this.mPaint.setAlpha((int) (255.0f * alpha));
        invalidate();
    }

    public void show(int touchX, int touchY) {
        this.mDragLayer.addView(this);
        LayoutParams lp = new LayoutParams(0, 0);
        lp.width = this.mBitmap.getWidth();
        lp.height = this.mBitmap.getHeight();
        lp.x = touchX - this.mRegistrationX;
        lp.y = touchY - this.mRegistrationY;
        lp.customPosition = true;
        setLayoutParams(lp);
        this.mLayoutParams = lp;
        this.mAnim.start();
    }

    /* access modifiers changed from: 0000 */
    public void move(int touchX, int touchY) {
        LayoutParams lp = this.mLayoutParams;
        lp.x = (touchX - this.mRegistrationX) + ((int) this.mOffsetX);
        lp.y = (touchY - this.mRegistrationY) + ((int) this.mOffsetY);
        this.mDragLayer.requestLayout();
    }

    /* access modifiers changed from: 0000 */
    public void remove() {
        post(new Runnable() {
            public void run() {
                DragView.this.mDragLayer.removeView(DragView.this);
            }
        });
    }

    /* access modifiers changed from: 0000 */
    public int[] getPosition(int[] result) {
        LayoutParams lp = this.mLayoutParams;
        if (result == null) {
            result = new int[2];
        }
        result[0] = lp.x;
        result[1] = lp.y;
        return result;
    }

    /* access modifiers changed from: 0000 */
    public int[] getMotionPosition(int[] result) {
        LayoutParams lp = this.mLayoutParams;
        if (result == null) {
            result = new int[2];
        }
        result[0] = (lp.x + this.mRegistrationX) - ((int) this.mOffsetX);
        result[1] = (lp.y + this.mRegistrationY) - ((int) this.mOffsetY);
        return result;
    }
}
