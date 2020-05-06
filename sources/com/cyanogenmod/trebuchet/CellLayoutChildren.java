package com.cyanogenmod.trebuchet;

import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.ViewParent;
import com.cyanogenmod.trebuchet.CellLayout.LayoutParams;

public class CellLayoutChildren extends ViewGroup {
    static final String TAG = "CellLayoutChildren";
    private int mCellHeight;
    private int mCellWidth;
    private int mHeightGap;
    private int mSpacingLeft;
    private int mSpacingTop;
    private final int[] mTmpCellXY;
    private final WallpaperManager mWallpaperManager;
    private int mWidthGap;

    public CellLayoutChildren(Context context) {
        this(context, null, 0);
    }

    public CellLayoutChildren(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CellLayoutChildren(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mTmpCellXY = new int[2];
        this.mWallpaperManager = WallpaperManager.getInstance(context);
    }

    public void enableHardwareLayers() {
        setLayerType(2, null);
    }

    public void disableHardwareLayers() {
        setLayerType(0, null);
    }

    public void setCellDimensions(int cellWidth, int cellHeight, int widthGap, int heightGap, int spacingLeft, int spacingTop) {
        this.mCellWidth = cellWidth;
        this.mCellHeight = cellHeight;
        this.mWidthGap = widthGap;
        this.mHeightGap = heightGap;
        this.mSpacingLeft = spacingLeft;
        this.mSpacingTop = spacingTop;
    }

    public View getChildAt(int x, int y) {
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            LayoutParams lp = (LayoutParams) child.getLayoutParams();
            if (lp.cellX <= x && x < lp.cellX + lp.cellHSpan && lp.cellY <= y && y < lp.cellY + lp.cellVSpan) {
                return child;
            }
        }
        return null;
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            measureChild(getChildAt(i));
        }
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getSize(heightMeasureSpec));
    }

    public void setupLp(LayoutParams lp) {
        lp.setup(this.mCellWidth, this.mCellHeight, this.mWidthGap, this.mHeightGap, this.mSpacingLeft, this.mSpacingTop);
    }

    public void measureChild(View child) {
        LayoutParams lp = (LayoutParams) child.getLayoutParams();
        lp.setup(this.mCellWidth, this.mCellHeight, this.mWidthGap, this.mHeightGap, this.mSpacingLeft, this.mSpacingTop);
        child.measure(MeasureSpec.makeMeasureSpec(lp.width, 1073741824), MeasureSpec.makeMeasureSpec(lp.height, 1073741824));
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean changed, int l, int t, int r, int b) {
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() != 8) {
                LayoutParams lp = (LayoutParams) child.getLayoutParams();
                int childLeft = lp.x;
                int childTop = lp.y;
                child.layout(childLeft, childTop, lp.width + childLeft, lp.height + childTop);
                if (lp.dropped) {
                    lp.dropped = false;
                    int[] cellXY = this.mTmpCellXY;
                    getLocationOnScreen(cellXY);
                    this.mWallpaperManager.sendWallpaperCommand(getWindowToken(), "android.home.drop", cellXY[0] + childLeft + (lp.width / 2), cellXY[1] + childTop + (lp.height / 2), 0, null);
                }
            }
        }
    }

    public boolean shouldDelayChildPressedState() {
        return false;
    }

    public void requestChildFocus(View child, View focused) {
        super.requestChildFocus(child, focused);
        if (child != null) {
            Rect r = new Rect();
            child.getDrawingRect(r);
            requestRectangleOnScreen(r);
        }
    }

    public boolean requestFocus(int direction, Rect previouslyFocusedRect) {
        ViewParent vp = getParent();
        if (!(vp instanceof CellLayout)) {
            return super.requestFocus(direction, previouslyFocusedRect);
        }
        CellLayout cl = (CellLayout) vp;
        int countX = cl.getCountX();
        int countY = cl.getCountY();
        boolean foundChild = false;
        int y = 0;
        while (!foundChild && y < countX) {
            int x = 0;
            while (!foundChild && x < countY) {
                View child = getChildAt(x, y);
                if (child != null) {
                    foundChild = child.requestFocus(direction, previouslyFocusedRect);
                }
                x++;
            }
            y++;
        }
        if (!foundChild) {
            return super.requestFocus(direction, previouslyFocusedRect);
        }
        return foundChild;
    }

    public void cancelLongPress() {
        super.cancelLongPress();
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            getChildAt(i).cancelLongPress();
        }
    }

    /* access modifiers changed from: protected */
    public void setChildrenDrawingCacheEnabled(boolean enabled) {
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View view = getChildAt(i);
            view.setDrawingCacheEnabled(enabled);
            if (!view.isHardwareAccelerated() && enabled) {
                view.buildDrawingCache(true);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void setChildrenDrawnWithCacheEnabled(boolean enabled) {
        super.setChildrenDrawnWithCacheEnabled(enabled);
    }
}
