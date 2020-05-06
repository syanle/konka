package greendroid.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.BaseSavedState;
import com.cyanogenmod.trebuchet.RocketLauncher.Board.FlyingIcon;
import com.konka.ios7launcher.R;

public class PageIndicator extends View {
    private static final int MIN_DOT_COUNT = 1;
    public static final int NO_ACTIVE_DOT = -1;
    private static Rect sInRect = new Rect();
    private static Rect sOutRect = new Rect();
    private int mActiveDot;
    private int mDotCount;
    private Drawable mDotDrawable;
    private int mDotSpacing;
    private int mDotType;
    private int[] mExtraState;
    private int mGravity;
    private boolean mInitializing;

    public interface DotType {
        public static final int MULTIPLE = 1;
        public static final int SINGLE = 0;
    }

    static class SavedState extends BaseSavedState {
        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in, null);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
        int activeDot;

        SavedState(Parcelable superState) {
            super(superState);
        }

        /* synthetic */ SavedState(Parcel parcel, SavedState savedState) {
            this(parcel);
        }

        private SavedState(Parcel in) {
            super(in);
            this.activeDot = in.readInt();
        }

        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(this.activeDot);
        }
    }

    public PageIndicator(Context context) {
        this(context, null);
    }

    public PageIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PageIndicator(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initPageIndicator();
        this.mInitializing = true;
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.PageIndicator, defStyle, 0);
        setDotCount(a.getInt(0, this.mDotCount));
        setActiveDot(a.getInt(1, this.mActiveDot));
        setDotDrawable(a.getDrawable(2));
        setDotSpacing(a.getDimensionPixelSize(3, this.mDotSpacing));
        setGravity(a.getInt(4, this.mGravity));
        setDotType(a.getInt(5, this.mDotType));
        a.recycle();
        this.mInitializing = false;
    }

    private void initPageIndicator() {
        this.mDotCount = 1;
        this.mGravity = 17;
        this.mActiveDot = 0;
        this.mDotSpacing = 0;
        this.mDotType = 0;
        this.mExtraState = onCreateDrawableState(1);
        mergeDrawableStates(this.mExtraState, SELECTED_STATE_SET);
    }

    public int getDotCount() {
        return this.mDotCount;
    }

    public void setDotCount(int dotCount) {
        if (dotCount < 1) {
            dotCount = 1;
        }
        if (this.mDotCount != dotCount) {
            this.mDotCount = dotCount;
            requestLayout();
            invalidate();
        }
    }

    public int getActiveDot() {
        return this.mActiveDot;
    }

    public void setActiveDot(int activeDot) {
        if (activeDot < 0) {
            activeDot = -1;
        }
        switch (this.mDotType) {
            case 0:
                if (activeDot > this.mDotCount - 1) {
                    activeDot = -1;
                    break;
                }
                break;
            case 1:
                if (activeDot > this.mDotCount) {
                    activeDot = -1;
                    break;
                }
                break;
        }
        this.mActiveDot = activeDot;
        invalidate();
    }

    public Drawable getDotDrawable() {
        return this.mDotDrawable;
    }

    public void setDotDrawable(Drawable dotDrawable) {
        if (dotDrawable != this.mDotDrawable) {
            if (this.mDotDrawable != null) {
                this.mDotDrawable.setCallback(null);
            }
            this.mDotDrawable = dotDrawable;
            if (dotDrawable != null) {
                if (dotDrawable.getIntrinsicHeight() != -1 && dotDrawable.getIntrinsicWidth() != -1) {
                    dotDrawable.setBounds(0, 0, dotDrawable.getIntrinsicWidth(), dotDrawable.getIntrinsicHeight());
                    dotDrawable.setCallback(this);
                    if (dotDrawable.isStateful()) {
                        dotDrawable.setState(getDrawableState());
                    }
                } else {
                    return;
                }
            }
            requestLayout();
            invalidate();
        }
    }

    public int getDotSpacing() {
        return this.mDotSpacing;
    }

    public void setDotSpacing(int dotSpacing) {
        Log.d("PageIndicator", "setDotSpacing to " + dotSpacing);
        if (dotSpacing != this.mDotSpacing) {
            this.mDotSpacing = dotSpacing;
            requestLayout();
            invalidate();
        }
    }

    public int getGravity() {
        return this.mGravity;
    }

    public void setGravity(int gravity) {
        if (this.mGravity != gravity) {
            this.mGravity = gravity;
            invalidate();
        }
    }

    public int getDotType() {
        return this.mDotType;
    }

    public void setDotType(int dotType) {
        if ((dotType == 0 || dotType == 1) && this.mDotType != dotType) {
            this.mDotType = dotType;
            invalidate();
        }
    }

    public void requestLayout() {
        if (!this.mInitializing) {
            super.requestLayout();
        }
    }

    public void invalidate() {
        if (!this.mInitializing) {
            super.invalidate();
        }
    }

    /* access modifiers changed from: protected */
    public boolean verifyDrawable(Drawable who) {
        return super.verifyDrawable(who) || who == this.mDotDrawable;
    }

    /* access modifiers changed from: protected */
    public void drawableStateChanged() {
        super.drawableStateChanged();
        this.mExtraState = onCreateDrawableState(1);
        mergeDrawableStates(this.mExtraState, SELECTED_STATE_SET);
        invalidate();
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Drawable d = this.mDotDrawable;
        int width = 0;
        int height = 0;
        if (d != null) {
            width = (this.mDotCount * (d.getIntrinsicWidth() + this.mDotSpacing)) - this.mDotSpacing;
            height = d.getIntrinsicHeight();
        }
        setMeasuredDimension(resolveSize(width + getPaddingRight() + getPaddingLeft(), widthMeasureSpec), resolveSize(height + getPaddingBottom() + getPaddingTop(), heightMeasureSpec));
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        Drawable d = this.mDotDrawable;
        if (d != null) {
            int count = this.mDotType == 0 ? this.mDotCount : this.mActiveDot;
            if (count > 0) {
                int h = d.getIntrinsicHeight();
                int w = Math.max(0, ((d.getIntrinsicWidth() + this.mDotSpacing) * count) - this.mDotSpacing);
                sInRect.set(getPaddingLeft(), getPaddingTop(), getWidth() - getPaddingRight(), getHeight() - getPaddingBottom());
                Gravity.apply(this.mGravity, w, h, sInRect, sOutRect);
                canvas.save();
                canvas.translate((float) sOutRect.left, (float) sOutRect.top);
                for (int i = 0; i < count; i++) {
                    if (d.isStateful()) {
                        int[] state = getDrawableState();
                        if (this.mDotType == 1 || i == this.mActiveDot) {
                            state = this.mExtraState;
                        }
                        d.setCallback(null);
                        d.setState(state);
                        d.setCallback(this);
                    }
                    d.draw(canvas);
                    canvas.translate((float) (this.mDotSpacing + d.getIntrinsicWidth()), FlyingIcon.ANGULAR_VMIN);
                }
                canvas.restore();
            }
        }
    }

    public Parcelable onSaveInstanceState() {
        SavedState ss = new SavedState(super.onSaveInstanceState());
        ss.activeDot = this.mActiveDot;
        return ss;
    }

    public void onRestoreInstanceState(Parcelable state) {
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        this.mActiveDot = ss.activeDot;
    }
}
