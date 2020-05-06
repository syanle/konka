package com.cyanogenmod.trebuchet;

import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;

abstract class LauncherAnimatorUpdateListener implements AnimatorUpdateListener {
    /* access modifiers changed from: 0000 */
    public abstract void onAnimationUpdate(float f, float f2);

    LauncherAnimatorUpdateListener() {
    }

    public void onAnimationUpdate(ValueAnimator animation) {
        float b = ((Float) animation.getAnimatedValue()).floatValue();
        onAnimationUpdate(1.0f - b, b);
    }
}
