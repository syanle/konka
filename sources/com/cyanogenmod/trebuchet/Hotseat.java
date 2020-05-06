package com.cyanogenmod.trebuchet;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.FrameLayout;
import com.cyanogenmod.trebuchet.CellLayout.LayoutParams;
import com.konka.ios7launcher.R;

public class Hotseat extends FrameLayout {
    private static final int DEFAULT_CELL_COUNT_X = 5;
    private static final int DEFAULT_CELL_COUNT_Y = 1;
    private static final int sAllAppsButtonRank = 2;
    private int mCellCountX;
    private int mCellCountY;
    private CellLayout mContent;
    private boolean mIsLandscape;
    /* access modifiers changed from: private */
    public Launcher mLauncher;

    public Hotseat(Context context) {
        this(context, null);
    }

    public Hotseat(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Hotseat(Context context, AttributeSet attrs, int defStyle) {
        boolean z = true;
        super(context, attrs, defStyle);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.Hotseat, defStyle, 0);
        this.mCellCountX = a.getInt(0, -1);
        this.mCellCountY = a.getInt(1, -1);
        if (context.getResources().getConfiguration().orientation != 2) {
            z = false;
        }
        this.mIsLandscape = z;
    }

    public void setup(Launcher launcher) {
        this.mLauncher = launcher;
        setOnKeyListener(new HotseatIconKeyEventListener(this.mLauncher));
    }

    /* access modifiers changed from: 0000 */
    public CellLayout getLayout() {
        return this.mContent;
    }

    /* access modifiers changed from: 0000 */
    public int getOrderInHotseat(int x, int y) {
        return this.mIsLandscape ? (this.mContent.getCountY() - y) - 1 : x;
    }

    /* access modifiers changed from: 0000 */
    public int getCellXFromOrder(int rank) {
        if (this.mIsLandscape) {
            return 0;
        }
        return rank;
    }

    /* access modifiers changed from: 0000 */
    public int getCellYFromOrder(int rank) {
        if (this.mIsLandscape) {
            return this.mContent.getCountY() - (rank + 1);
        }
        return 0;
    }

    public static boolean isAllAppsButtonRank(int rank) {
        return rank == 2;
    }

    /* access modifiers changed from: 0000 */
    public int getCellLayoutX() {
        return this.mCellCountX;
    }

    /* access modifiers changed from: 0000 */
    public int getCellLayoutY() {
        return this.mCellCountY;
    }

    /* access modifiers changed from: protected */
    public void onFinishInflate() {
        super.onFinishInflate();
        if (this.mCellCountX < 0) {
            this.mCellCountX = 5;
        }
        if (this.mCellCountY < 0) {
            this.mCellCountY = 1;
        }
        this.mContent = (CellLayout) findViewById(R.id.layout);
        this.mContent.setGridSize(this.mCellCountX, this.mCellCountY);
        clearLayout();
    }

    /* access modifiers changed from: 0000 */
    public void clearLayout() {
        this.mContent.removeAllViewsInLayout();
    }

    /* access modifiers changed from: 0000 */
    public void resetLayout() {
        this.mContent.removeAllViewsInLayout();
        Context context = getContext();
        BubbleTextView allAppsButton = (BubbleTextView) LayoutInflater.from(context).inflate(R.layout.application, this.mContent, false);
        allAppsButton.setCompoundDrawablesWithIntrinsicBounds(null, context.getResources().getDrawable(R.drawable.all_apps_button_icon), null, null);
        allAppsButton.setContentDescription(context.getString(R.string.all_apps_button_label));
        allAppsButton.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (Hotseat.this.mLauncher != null && (event.getAction() & 255) == 0) {
                    Hotseat.this.mLauncher.onTouchDownAllAppsButton(v);
                }
                return false;
            }
        });
        allAppsButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (Hotseat.this.mLauncher != null) {
                    Hotseat.this.mLauncher.onClickAllAppsButton(v);
                }
            }
        });
        int x = getCellXFromOrder(2);
        int y = getCellYFromOrder(2);
        Log.d("czj-ios", "Hotset.resetLayout -> CellLayout.addViewToCellLayout");
        this.mContent.addViewToCellLayout(allAppsButton, -1, 0, new LayoutParams(x, y, 1, 1), true);
    }
}
