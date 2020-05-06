package com.cyanogenmod.trebuchet;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.StateListDrawable;
import android.widget.ImageView;
import com.konka.ios7launcher.R;

public class HolographicViewHelper {
    private int mHighlightColor;
    private boolean mStatesUpdated;
    private final Canvas mTempCanvas = new Canvas();

    public HolographicViewHelper(Context context) {
        this.mHighlightColor = context.getResources().getColor(R.color.konka_orange);
    }

    /* access modifiers changed from: 0000 */
    public void generatePressedFocusedStates(ImageView v) {
        if (!this.mStatesUpdated && v != null) {
            this.mStatesUpdated = true;
            FastBitmapDrawable d = new FastBitmapDrawable(createPressImage(v, this.mTempCanvas));
            StateListDrawable states = new StateListDrawable();
            states.addState(new int[]{16842919}, d);
            states.addState(new int[]{16842908}, d);
            states.addState(new int[0], v.getDrawable());
            v.setImageDrawable(states);
        }
    }

    /* access modifiers changed from: 0000 */
    public void invalidatePressedFocusedStates(ImageView v) {
        this.mStatesUpdated = false;
        if (v != null) {
            v.invalidate();
        }
    }

    private Bitmap createPressImage(ImageView v, Canvas canvas) {
        int padding = HolographicOutlineHelper.MAX_OUTER_BLUR_RADIUS;
        Bitmap b = Bitmap.createBitmap(v.getWidth() + padding, v.getHeight() + padding, Config.ARGB_8888);
        canvas.setBitmap(b);
        canvas.save();
        v.getDrawable().draw(canvas);
        canvas.restore();
        canvas.drawColor(this.mHighlightColor, Mode.SRC_IN);
        canvas.setBitmap(null);
        return b;
    }
}
