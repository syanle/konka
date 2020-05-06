package com.cyanogenmod.trebuchet;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Region.Op;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.TextView;
import com.cyanogenmod.trebuchet.RocketLauncher.Board.FlyingIcon;
import com.konka.ios7launcher.R;
import com.tencent.stat.common.StatConstants;

public class BubbleTextView extends TextView {
    static final float PADDING_V = 3.0f;
    static final int SHADOW_LARGE_COLOUR = -587202560;
    static final float SHADOW_LARGE_RADIUS = 4.0f;
    static final int SHADOW_SMALL_COLOUR = -872415232;
    static final float SHADOW_SMALL_RADIUS = 1.75f;
    static final float SHADOW_Y_OFFSET = 2.0f;
    private Drawable mBackground;
    private boolean mBackgroundSizeChanged;
    private float mBubbleColorAlpha;
    private boolean mDidInvalidateForPressedState;
    private int mFocusedGlowColor;
    private int mFocusedOutlineColor;
    private final HolographicOutlineHelper mOutlineHelper = new HolographicOutlineHelper();
    private Paint mPaint;
    private int mPressedGlowColor;
    private Bitmap mPressedOrFocusedBackground;
    private int mPressedOutlineColor;
    private int mPrevAlpha = -1;
    private boolean mStayPressed;
    private final Canvas mTempCanvas = new Canvas();
    private final Rect mTempRect = new Rect();
    private boolean mTextVisible = true;
    private CharSequence mVisibleText;

    public BubbleTextView(Context context) {
        super(context);
        init();
    }

    public BubbleTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BubbleTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        this.mBackground = getBackground();
        Resources res = getContext().getResources();
        int bubbleColor = res.getColor(R.color.bubble_dark_background);
        this.mPaint = new Paint(3);
        this.mPaint.setColor(bubbleColor);
        this.mBubbleColorAlpha = ((float) Color.alpha(bubbleColor)) / 255.0f;
        int color = res.getColor(R.color.konka_orange);
        this.mPressedGlowColor = color;
        this.mPressedOutlineColor = color;
        this.mFocusedGlowColor = color;
        this.mFocusedOutlineColor = color;
        setShadowLayer(4.0f, FlyingIcon.ANGULAR_VMIN, SHADOW_Y_OFFSET, SHADOW_LARGE_COLOUR);
    }

    public void applyFromShortcutInfo(ShortcutInfo info, IconCache iconCache) {
        FastBitmapDrawable fbd = new FastBitmapDrawable(info.getIcon(iconCache));
        fbd.setFilterBitmap(true);
        setCompoundDrawablesWithIntrinsicBounds(null, fbd, null, null);
        setText(info.title);
        setTag(info);
    }

    /* access modifiers changed from: protected */
    public boolean setFrame(int left, int top, int right, int bottom) {
        if (!(this.mLeft == left && this.mRight == right && this.mTop == top && this.mBottom == bottom)) {
            this.mBackgroundSizeChanged = true;
        }
        return super.setFrame(left, top, right, bottom);
    }

    /* access modifiers changed from: protected */
    public boolean verifyDrawable(Drawable who) {
        return who == this.mBackground || super.verifyDrawable(who);
    }

    /* access modifiers changed from: protected */
    public void drawableStateChanged() {
        boolean backgroundEmptyBefore;
        if (!isPressed()) {
            if (this.mPressedOrFocusedBackground == null) {
                backgroundEmptyBefore = true;
            } else {
                backgroundEmptyBefore = false;
            }
            if (!this.mStayPressed) {
                this.mPressedOrFocusedBackground = null;
            }
            if (isFocused()) {
                if (getLayout() == null) {
                    this.mPressedOrFocusedBackground = null;
                } else {
                    this.mPressedOrFocusedBackground = null;
                }
                this.mStayPressed = false;
                setCellLayoutPressedOrFocusedIcon();
            }
            boolean backgroundEmptyNow = this.mPressedOrFocusedBackground == null;
            if (!backgroundEmptyBefore && backgroundEmptyNow) {
                setCellLayoutPressedOrFocusedIcon();
            }
        } else if (!this.mDidInvalidateForPressedState) {
            setCellLayoutPressedOrFocusedIcon();
        }
        Drawable d = this.mBackground;
        if (d != null && d.isStateful()) {
            d.setState(getDrawableState());
        }
        super.drawableStateChanged();
    }

    private void drawWithPadding(Canvas destCanvas, int padding) {
        Rect clipRect = this.mTempRect;
        getDrawingRect(clipRect);
        clipRect.bottom = (getExtendedPaddingTop() - 3) + getLayout().getLineTop(0);
        destCanvas.save();
        destCanvas.translate((float) ((-getScrollX()) + (padding / 2)), (float) ((-getScrollY()) + (padding / 2)));
        destCanvas.clipRect(clipRect, Op.REPLACE);
        draw(destCanvas);
        destCanvas.restore();
    }

    private Bitmap createGlowingOutline(Canvas canvas, int outlineColor, int glowColor) {
        int padding = HolographicOutlineHelper.MAX_OUTER_BLUR_RADIUS;
        Bitmap b = Bitmap.createBitmap(Launcher.sDisplayMetrics, getWidth() + padding, getHeight() + padding, Config.ARGB_8888);
        canvas.setBitmap(b);
        drawWithPadding(canvas, padding);
        this.mOutlineHelper.applyExtraThickExpensiveOutlineWithBlur(b, canvas, glowColor, outlineColor);
        canvas.setBitmap(null);
        return b;
    }

    public boolean onTouchEvent(MotionEvent event) {
        boolean result = super.onTouchEvent(event);
        switch (event.getAction()) {
            case 0:
                if (this.mPressedOrFocusedBackground == null) {
                    this.mPressedOrFocusedBackground = createGlowingOutline(this.mTempCanvas, this.mPressedGlowColor, this.mPressedOutlineColor);
                }
                if (!isPressed()) {
                    this.mDidInvalidateForPressedState = false;
                    break;
                } else {
                    this.mDidInvalidateForPressedState = true;
                    setCellLayoutPressedOrFocusedIcon();
                    break;
                }
            case 1:
            case 3:
                if (!isPressed()) {
                    this.mPressedOrFocusedBackground = null;
                    break;
                }
                break;
        }
        return result;
    }

    /* access modifiers changed from: 0000 */
    public void setStayPressed(boolean stayPressed) {
        this.mStayPressed = stayPressed;
        if (!stayPressed) {
            this.mPressedOrFocusedBackground = null;
        }
        setCellLayoutPressedOrFocusedIcon();
    }

    /* Debug info: failed to restart local var, previous not found, register: 3 */
    /* access modifiers changed from: 0000 */
    public void setCellLayoutPressedOrFocusedIcon() {
        if (getParent() instanceof CellLayoutChildren) {
            CellLayoutChildren parent = (CellLayoutChildren) getParent();
            if (parent != null) {
                CellLayout layout = (CellLayout) parent.getParent();
                if (this.mPressedOrFocusedBackground == null) {
                    this = null;
                }
                layout.setPressedOrFocusedIcon(this);
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public void clearPressedOrFocusedBackground() {
        this.mPressedOrFocusedBackground = null;
        setCellLayoutPressedOrFocusedIcon();
    }

    /* access modifiers changed from: 0000 */
    public Bitmap getPressedOrFocusedBackground() {
        return this.mPressedOrFocusedBackground;
    }

    /* access modifiers changed from: 0000 */
    public int getPressedOrFocusedBackgroundPadding() {
        return HolographicOutlineHelper.MAX_OUTER_BLUR_RADIUS / 2;
    }

    public void draw(Canvas canvas) {
        Drawable background = this.mBackground;
        if (background != null) {
            int scrollX = this.mScrollX;
            int scrollY = this.mScrollY;
            if (this.mBackgroundSizeChanged) {
                background.setBounds(0, 0, this.mRight - this.mLeft, this.mBottom - this.mTop);
                this.mBackgroundSizeChanged = false;
            }
            if ((scrollX | scrollY) == 0) {
                background.draw(canvas);
            } else {
                canvas.translate((float) scrollX, (float) scrollY);
                background.draw(canvas);
                canvas.translate((float) (-scrollX), (float) (-scrollY));
            }
        }
        getPaint().setShadowLayer(4.0f, FlyingIcon.ANGULAR_VMIN, SHADOW_Y_OFFSET, SHADOW_LARGE_COLOUR);
        super.draw(canvas);
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (this.mBackground != null) {
            this.mBackground.setCallback(this);
        }
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (this.mBackground != null) {
            this.mBackground.setCallback(null);
        }
    }

    /* access modifiers changed from: protected */
    public boolean onSetAlpha(int alpha) {
        if (this.mPrevAlpha != alpha) {
            this.mPrevAlpha = alpha;
            this.mPaint.setAlpha((int) (((float) alpha) * this.mBubbleColorAlpha));
            super.onSetAlpha(alpha);
        }
        return true;
    }

    public void setTextVisible(boolean visible) {
        if (this.mTextVisible != visible) {
            this.mTextVisible = visible;
            if (visible) {
                setText(this.mVisibleText);
                return;
            }
            this.mVisibleText = getText();
            setText(StatConstants.MTA_COOPERATION_TAG);
        }
    }
}
