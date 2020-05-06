package com.cyanogenmod.trebuchet;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TimeAnimator;
import android.animation.TimeAnimator.TimeListener;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AccelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.konka.ios7launcher.R;
import java.util.HashMap;
import java.util.Random;

public class RocketLauncher extends Activity {
    public static final boolean ROCKET_LAUNCHER = true;

    public static class Board extends FrameLayout {
        public static final boolean FIXED_STARS = true;
        public static final boolean FLYING_STARS = true;
        public static final int LAUNCH_ZOOM_TIME = 400;
        public static final float MANEUVERING_THRUST_SCALE = 0.1f;
        public static final int NUM_ICONS = 20;
        static Random sRNG = new Random();
        TimeAnimator mAnim;
        ComponentName[] mComponentNames;
        final Runnable mEngageWarp = new Runnable() {
            public void run() {
                Board.this.mManeuveringThrusters = false;
            }
        };
        HashMap<ComponentName, Bitmap> mIcons;
        /* access modifiers changed from: private */
        public boolean mManeuveringThrusters = false;
        /* access modifiers changed from: private */
        public float mSpeedScale = 1.0f;

        public class FlyingIcon extends ImageView {
            public static final float ANGULAR_VMAX = 45.0f;
            public static final float ANGULAR_VMIN = 0.0f;
            public static final float SCALE_MAX = 4.0f;
            public static final float SCALE_MIN = 0.5f;
            public static final float VMAX = 1000.0f;
            public static final float VMIN = 100.0f;
            public float angle;
            public float anglex;
            public float angley;
            public float boardCenterX;
            public float boardCenterY;
            public ComponentName component;
            public float dist;
            public float endscale;
            public float fuse;
            public final float[] hsv = new float[3];
            public float v;
            public float vr;

            public FlyingIcon(Context context, AttributeSet as) {
                super(context, as);
                setLayerType(2, null);
                setBackgroundResource(R.drawable.flying_icon_bg);
                this.hsv[1] = 1.0f;
                this.hsv[2] = 1.0f;
            }

            public boolean onTouchEvent(MotionEvent event) {
                if (!Board.this.mManeuveringThrusters || this.component == null) {
                    return false;
                }
                if (getAlpha() < 0.5f) {
                    setPressed(false);
                    return false;
                }
                switch (event.getAction()) {
                    case 0:
                        setPressed(true);
                        Board.this.resetWarpTimer();
                        break;
                    case 1:
                        if (isPressed()) {
                            setPressed(false);
                            postDelayed(new Runnable() {
                                public void run() {
                                    try {
                                        FlyingIcon.this.getContext().startActivity(new Intent("android.intent.action.MAIN").addFlags(268435456).setComponent(FlyingIcon.this.component));
                                    } catch (ActivityNotFoundException | SecurityException e) {
                                    }
                                }
                            }, 400);
                            this.endscale = ANGULAR_VMIN;
                            AnimatorSet s = new AnimatorSet();
                            s.playTogether(new Animator[]{ObjectAnimator.ofFloat(this, "scaleX", new float[]{15.0f}), ObjectAnimator.ofFloat(this, "scaleY", new float[]{15.0f}), ObjectAnimator.ofFloat(this, "alpha", new float[]{0.0f})});
                            s.setDuration(500);
                            s.setInterpolator(new AccelerateInterpolator(3.0f));
                            s.start();
                            break;
                        }
                        break;
                    case 2:
                        Rect hit = new Rect();
                        Point offset = new Point();
                        getGlobalVisibleRect(hit, offset);
                        setPressed(hit.contains(((int) event.getX()) + offset.x, ((int) event.getY()) + offset.y));
                        Board.this.resetWarpTimer();
                        break;
                }
                return true;
            }

            public String toString() {
                return String.format("<'%s' @ (%.1f, %.1f) v=%.1f a=%.1f dist/fuse=%.1f/%.1f>", new Object[]{BaseLauncherColumns.ICON, Float.valueOf(getX()), Float.valueOf(getY()), Float.valueOf(this.v), Float.valueOf(this.angle), Float.valueOf(this.dist), Float.valueOf(this.fuse)});
            }

            public void randomizeIcon() {
                this.component = (ComponentName) Board.pick(Board.this.mComponentNames);
                setImageBitmap((Bitmap) Board.this.mIcons.get(this.component));
            }

            public void randomize() {
                this.v = Board.randfrange(100.0f, 1000.0f);
                this.angle = Board.randfrange(ANGULAR_VMIN, 360.0f);
                this.anglex = (float) Math.sin((((double) this.angle) / 180.0d) * 3.141592653589793d);
                this.angley = (float) Math.cos((((double) this.angle) / 180.0d) * 3.141592653589793d);
                this.vr = Board.randfrange(ANGULAR_VMIN, 45.0f) * ((float) Board.randsign());
                this.endscale = Board.randfrange(0.5f, 4.0f);
                randomizeIcon();
            }

            public void reset() {
                randomize();
                this.boardCenterX = (float) ((Board.this.getWidth() - getWidth()) / 2);
                this.boardCenterY = (float) ((Board.this.getHeight() - getHeight()) / 2);
                setX(this.boardCenterX);
                setY(this.boardCenterY);
                this.fuse = Math.max(this.boardCenterX, this.boardCenterY);
                setRotation(180.0f - this.angle);
                setScaleX(ANGULAR_VMIN);
                setScaleY(ANGULAR_VMIN);
                this.dist = ANGULAR_VMIN;
                setAlpha(ANGULAR_VMIN);
            }

            public void update(float dt) {
                float f = ANGULAR_VMIN;
                this.dist += this.v * dt;
                setX(getX() + (this.anglex * this.v * dt));
                setY(getY() + (this.angley * this.v * dt));
                if (this.endscale > ANGULAR_VMIN) {
                    float scale = Board.lerp(ANGULAR_VMIN, this.endscale, (float) Math.sqrt((double) (this.dist / this.fuse)));
                    setScaleX(Board.lerp(1.0f, 0.75f, (float) Math.pow((double) ((this.v - 100.0f) / 900.0f), 3.0d)) * scale);
                    setScaleY(Board.lerp(1.0f, 1.5f, (float) Math.pow((double) ((this.v - 100.0f) / 900.0f), 3.0d)) * scale);
                    float q1 = this.fuse * 0.15f;
                    float q4 = this.fuse * 0.75f;
                    if (this.dist < q1) {
                        setAlpha((float) Math.sqrt((double) (this.dist / q1)));
                    } else if (this.dist > q4) {
                        if (this.dist < this.fuse) {
                            f = 1.0f - ((float) Math.pow((double) ((this.dist - q4) / (this.fuse - q4)), 2.0d));
                        }
                        setAlpha(f);
                    } else {
                        setAlpha(1.0f);
                    }
                }
            }
        }

        public class FlyingStar extends FlyingIcon {
            public FlyingStar(Context context, AttributeSet as) {
                super(context, as);
            }

            public void randomizeIcon() {
                setImageResource(R.drawable.widget_resize_handle_bottom);
            }

            public void randomize() {
                super.randomize();
                this.v = Board.randfrange(750.0f, 2000.0f);
                this.endscale = Board.randfrange(1.0f, 2.0f);
            }
        }

        static float lerp(float a, float b, float f) {
            return ((b - a) * f) + a;
        }

        static float randfrange(float a, float b) {
            return lerp(a, b, sRNG.nextFloat());
        }

        static int randsign() {
            return sRNG.nextBoolean() ? 1 : -1;
        }

        static <E> E pick(E[] array) {
            if (array.length == 0) {
                return null;
            }
            return array[sRNG.nextInt(array.length)];
        }

        public Board(Context context, AttributeSet as) {
            super(context, as);
            setBackgroundColor(-16777216);
            this.mIcons = ((LauncherApplication) context.getApplicationContext()).getIconCache().getAllIcons();
            this.mComponentNames = new ComponentName[this.mIcons.size()];
            this.mComponentNames = (ComponentName[]) this.mIcons.keySet().toArray(this.mComponentNames);
        }

        private void reset() {
            FlyingIcon nv;
            removeAllViews();
            LayoutParams wrap = new LayoutParams(-2, -2);
            for (int i = 0; i < 20; i++) {
                ImageView fixedStar = new ImageView(getContext(), null);
                fixedStar.setImageResource(R.drawable.widget_resize_handle_bottom);
                float s = randfrange(0.25f, 0.75f);
                fixedStar.setScaleX(s);
                fixedStar.setScaleY(s);
                fixedStar.setAlpha(0.75f);
                addView(fixedStar, wrap);
                fixedStar.setX(randfrange(FlyingIcon.ANGULAR_VMIN, (float) getWidth()));
                fixedStar.setY(randfrange(FlyingIcon.ANGULAR_VMIN, (float) getHeight()));
            }
            for (int i2 = 0; i2 < 40; i2++) {
                if (i2 < 20) {
                    nv = new FlyingStar(getContext(), null);
                } else {
                    nv = new FlyingIcon(getContext(), null);
                }
                addView(nv, wrap);
                nv.reset();
            }
            this.mAnim = new TimeAnimator();
            this.mAnim.setTimeListener(new TimeListener() {
                public void onTimeUpdate(TimeAnimator animation, long totalTime, long deltaTime) {
                    if (totalTime < 3000) {
                        float s = 1.0f - ((float) Math.pow((double) ((((float) totalTime) / 3000.0f) - 1.0f), 4.0d));
                        Board.this.setScaleX(s);
                        Board.this.setScaleY(s);
                    } else {
                        Board.this.setScaleX(1.0f);
                        Board.this.setScaleY(1.0f);
                    }
                    if (Board.this.mManeuveringThrusters) {
                        if (Board.this.mSpeedScale > 0.1f) {
                            Board board = Board.this;
                            board.mSpeedScale = board.mSpeedScale - (((float) (2 * deltaTime)) / 1000.0f);
                        }
                        if (Board.this.mSpeedScale < 0.1f) {
                            Board.this.mSpeedScale = 0.1f;
                        }
                    } else {
                        if (Board.this.mSpeedScale < 1.0f) {
                            Board board2 = Board.this;
                            board2.mSpeedScale = board2.mSpeedScale + (((float) deltaTime) / 1000.0f);
                        }
                        if (Board.this.mSpeedScale > 1.0f) {
                            Board.this.mSpeedScale = 1.0f;
                        }
                    }
                    for (int i = 0; i < Board.this.getChildCount(); i++) {
                        View v = Board.this.getChildAt(i);
                        if (v instanceof FlyingIcon) {
                            FlyingIcon nv = (FlyingIcon) v;
                            nv.update((((float) deltaTime) / 1000.0f) * Board.this.mSpeedScale);
                            float scaledWidth = ((float) nv.getWidth()) * nv.getScaleX();
                            float scaledHeight = ((float) nv.getHeight()) * nv.getScaleY();
                            if (nv.getX() + scaledWidth < FlyingIcon.ANGULAR_VMIN || nv.getX() - scaledWidth > ((float) Board.this.getWidth()) || nv.getY() + scaledHeight < FlyingIcon.ANGULAR_VMIN || nv.getY() - scaledHeight > ((float) Board.this.getHeight())) {
                                nv.reset();
                            }
                        }
                    }
                }
            });
        }

        /* access modifiers changed from: protected */
        public void onAttachedToWindow() {
            super.onAttachedToWindow();
            setLayerType(2, null);
            reset();
            this.mAnim.start();
        }

        /* access modifiers changed from: protected */
        public void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);
            this.mAnim.cancel();
            reset();
            this.mAnim.start();
        }

        /* access modifiers changed from: protected */
        public void onDetachedFromWindow() {
            super.onDetachedFromWindow();
            this.mAnim.cancel();
        }

        public boolean isOpaque() {
            return true;
        }

        public boolean onInterceptTouchEvent(MotionEvent e) {
            return !this.mManeuveringThrusters;
        }

        public void resetWarpTimer() {
            Handler h = getHandler();
            h.removeCallbacks(this.mEngageWarp);
            h.postDelayed(this.mEngageWarp, 5000);
        }

        public boolean onTouchEvent(MotionEvent event) {
            if (event.getAction() != 0 || this.mManeuveringThrusters) {
                return false;
            }
            this.mManeuveringThrusters = true;
            resetWarpTimer();
            return true;
        }
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        getWindow().addFlags(1024);
    }

    public void onStart() {
        super.onStart();
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int longside = metrics.widthPixels > metrics.heightPixels ? metrics.widthPixels : metrics.heightPixels;
        Board b = new Board(this, null);
        setContentView(b, new LayoutParams(longside, longside));
        b.setX((float) ((metrics.widthPixels - longside) / 2));
        b.setY((float) ((metrics.heightPixels - longside) / 2));
    }

    public void onUserInteraction() {
    }
}
