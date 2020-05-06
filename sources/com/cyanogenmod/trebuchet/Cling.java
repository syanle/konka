package com.cyanogenmod.trebuchet;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.widget.FrameLayout;
import com.cyanogenmod.trebuchet.RocketLauncher.Board.FlyingIcon;
import com.konka.ios7launcher.R;

public class Cling extends FrameLayout {
    static final String ALLAPPS_CLING_DISMISSED_KEY = "cling.allapps.dismissed";
    private static String ALLAPPS_LANDSCAPE = "all_apps_landscape";
    private static String ALLAPPS_LARGE = "all_apps_large";
    private static String ALLAPPS_PORTRAIT = "all_apps_portrait";
    static final String ALLAPPS_SORT_CLING_DISMISSED_KEY = "cling.allappssort.dismissed";
    private static String ALLAPPS_SORT_LANDSCAPE = "all_apps_sort_landscape";
    private static String ALLAPPS_SORT_LARGE = "all_apps_sort_large";
    private static String ALLAPPS_SORT_PORTRAIT = "all_apps_sort_portrait";
    static final String FOLDER_CLING_DISMISSED_KEY = "cling.folder.dismissed";
    private static String FOLDER_LANDSCAPE = "folder_landscape";
    private static String FOLDER_LARGE = "folder_large";
    private static String FOLDER_PORTRAIT = "folder_portrait";
    static final String WORKSPACE_CLING_DISMISSED_KEY = "cling.workspace.dismissed";
    private static String WORKSPACE_LANDSCAPE = "workspace_landscape";
    private static String WORKSPACE_LARGE = "workspace_large";
    private static String WORKSPACE_PORTRAIT = "workspace_portrait";
    private int mAppIconSize;
    private Drawable mBackground;
    private int mButtonBarHeight;
    private boolean mDismissed;
    private String mDrawIdentifier;
    private Paint mErasePaint;
    private Drawable mHandTouchGraphic;
    private boolean mIsInitialized;
    private Launcher mLauncher;
    private int[] mPositionData;
    private Drawable mPunchThroughGraphic;
    private int mPunchThroughGraphicCenterRadius;
    private float mRevealRadius;

    public Cling(Context context) {
        this(context, null, 0);
    }

    public Cling(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Cling(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.Cling, defStyle, 0);
        this.mDrawIdentifier = a.getString(0);
        a.recycle();
    }

    /* access modifiers changed from: 0000 */
    public void init(Launcher l, int[] positionData) {
        if (!this.mIsInitialized) {
            this.mLauncher = l;
            this.mPositionData = positionData;
            this.mDismissed = false;
            Resources r = getContext().getResources();
            this.mPunchThroughGraphic = r.getDrawable(R.drawable.cling);
            this.mPunchThroughGraphicCenterRadius = r.getDimensionPixelSize(R.dimen.clingPunchThroughGraphicCenterRadius);
            this.mAppIconSize = r.getDimensionPixelSize(R.dimen.app_icon_size);
            this.mRevealRadius = ((float) this.mAppIconSize) * 1.0f;
            this.mButtonBarHeight = r.getDimensionPixelSize(R.dimen.button_bar_height);
            this.mErasePaint = new Paint();
            this.mErasePaint.setXfermode(new PorterDuffXfermode(Mode.MULTIPLY));
            this.mErasePaint.setColor(16777215);
            this.mErasePaint.setAlpha(0);
            this.mIsInitialized = true;
        }
    }

    /* access modifiers changed from: 0000 */
    public void dismiss() {
        this.mDismissed = true;
    }

    /* access modifiers changed from: 0000 */
    public boolean isDismissed() {
        return this.mDismissed;
    }

    /* access modifiers changed from: 0000 */
    public void cleanup() {
        this.mBackground = null;
        this.mPunchThroughGraphic = null;
        this.mHandTouchGraphic = null;
        this.mIsInitialized = false;
    }

    private int[] getPunchThroughPosition() {
        if (this.mDrawIdentifier.equals(WORKSPACE_PORTRAIT)) {
            return new int[]{getMeasuredWidth() / 2, getMeasuredHeight() - (this.mButtonBarHeight / 2)};
        } else if (this.mDrawIdentifier.equals(WORKSPACE_LANDSCAPE)) {
            return new int[]{getMeasuredWidth() - (this.mButtonBarHeight / 2), getMeasuredHeight() / 2};
        } else if (this.mDrawIdentifier.equals(ALLAPPS_SORT_PORTRAIT) || this.mDrawIdentifier.equals(ALLAPPS_SORT_LANDSCAPE) || this.mDrawIdentifier.equals(ALLAPPS_SORT_LARGE)) {
            return new int[]{this.mButtonBarHeight / 2, this.mButtonBarHeight / 2};
        } else if (this.mDrawIdentifier.equals(WORKSPACE_LARGE)) {
            float scale = LauncherApplication.getScreenDensity();
            int cornerYOffset = (int) (10.0f * scale);
            return new int[]{getMeasuredWidth() - ((int) (15.0f * scale)), cornerYOffset};
        } else if (this.mDrawIdentifier.equals(ALLAPPS_PORTRAIT) || this.mDrawIdentifier.equals(ALLAPPS_LANDSCAPE) || this.mDrawIdentifier.equals(ALLAPPS_LARGE)) {
            return this.mPositionData;
        } else {
            return new int[]{-1, -1};
        }
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (this.mDrawIdentifier.equals(WORKSPACE_PORTRAIT) || this.mDrawIdentifier.equals(WORKSPACE_LANDSCAPE) || this.mDrawIdentifier.equals(WORKSPACE_LARGE) || this.mDrawIdentifier.equals(ALLAPPS_PORTRAIT) || this.mDrawIdentifier.equals(ALLAPPS_LANDSCAPE) || this.mDrawIdentifier.equals(ALLAPPS_LARGE) || this.mDrawIdentifier.equals(ALLAPPS_SORT_PORTRAIT) || this.mDrawIdentifier.equals(ALLAPPS_SORT_LANDSCAPE) || this.mDrawIdentifier.equals(ALLAPPS_SORT_LARGE)) {
            int[] pos = getPunchThroughPosition();
            if (Math.sqrt(Math.pow((double) (event.getX() - ((float) pos[0])), 2.0d) + Math.pow((double) (event.getY() - ((float) pos[1])), 2.0d)) < ((double) this.mRevealRadius)) {
                return false;
            }
        } else if (this.mDrawIdentifier.equals(FOLDER_PORTRAIT) || this.mDrawIdentifier.equals(FOLDER_LANDSCAPE) || this.mDrawIdentifier.equals(FOLDER_LARGE)) {
            Folder f = this.mLauncher.getWorkspace().getOpenFolder();
            if (f != null) {
                Rect r = new Rect();
                f.getHitRect(r);
                if (r.contains((int) event.getX(), (int) event.getY())) {
                    return false;
                }
            }
        }
        return true;
    }

    /* access modifiers changed from: protected */
    public void dispatchDraw(Canvas canvas) {
        if (this.mIsInitialized) {
            this.mLauncher.getWindowManager().getDefaultDisplay().getMetrics(new DisplayMetrics());
            Bitmap b = Bitmap.createBitmap(getMeasuredWidth(), getMeasuredHeight(), Config.ARGB_8888);
            Canvas c = new Canvas(b);
            if (this.mBackground == null) {
                if (this.mDrawIdentifier.equals(WORKSPACE_PORTRAIT) || this.mDrawIdentifier.equals(WORKSPACE_LANDSCAPE) || this.mDrawIdentifier.equals(WORKSPACE_LARGE)) {
                    this.mBackground = getResources().getDrawable(R.drawable.bg_cling1);
                } else if (this.mDrawIdentifier.equals(ALLAPPS_PORTRAIT) || this.mDrawIdentifier.equals(ALLAPPS_LANDSCAPE) || this.mDrawIdentifier.equals(ALLAPPS_LARGE)) {
                    this.mBackground = getResources().getDrawable(R.drawable.bg_cling2);
                } else if (this.mDrawIdentifier.equals(FOLDER_PORTRAIT) || this.mDrawIdentifier.equals(FOLDER_LANDSCAPE)) {
                    this.mBackground = getResources().getDrawable(R.drawable.bg_cling3);
                } else if (this.mDrawIdentifier.equals(FOLDER_LARGE)) {
                    this.mBackground = getResources().getDrawable(R.drawable.bg_cling4);
                }
            }
            if (this.mBackground != null) {
                this.mBackground.setBounds(0, 0, getMeasuredWidth(), getMeasuredHeight());
                this.mBackground.draw(c);
            } else {
                c.drawColor(-1728053248);
            }
            float scale = this.mRevealRadius / ((float) this.mPunchThroughGraphicCenterRadius);
            int dw = (int) (((float) this.mPunchThroughGraphic.getIntrinsicWidth()) * scale);
            int dh = (int) (((float) this.mPunchThroughGraphic.getIntrinsicHeight()) * scale);
            int[] pos = getPunchThroughPosition();
            int cx = pos[0];
            int cy = pos[1];
            if (cx > -1 && cy > -1) {
                c.drawCircle((float) cx, (float) cy, this.mRevealRadius, this.mErasePaint);
                this.mPunchThroughGraphic.setBounds(cx - (dw / 2), cy - (dh / 2), (dw / 2) + cx, (dh / 2) + cy);
                this.mPunchThroughGraphic.draw(c);
            }
            if (this.mDrawIdentifier.equals(ALLAPPS_PORTRAIT) || this.mDrawIdentifier.equals(ALLAPPS_LANDSCAPE) || this.mDrawIdentifier.equals(ALLAPPS_LARGE)) {
                if (this.mHandTouchGraphic == null) {
                    this.mHandTouchGraphic = getResources().getDrawable(R.drawable.hand);
                }
                int offset = this.mAppIconSize / 4;
                this.mHandTouchGraphic.setBounds(cx + offset, cy + offset, this.mHandTouchGraphic.getIntrinsicWidth() + cx + offset, this.mHandTouchGraphic.getIntrinsicHeight() + cy + offset);
                this.mHandTouchGraphic.draw(c);
            }
            canvas.drawBitmap(b, FlyingIcon.ANGULAR_VMIN, FlyingIcon.ANGULAR_VMIN, null);
            c.setBitmap(null);
        }
        super.dispatchDraw(canvas);
    }
}
