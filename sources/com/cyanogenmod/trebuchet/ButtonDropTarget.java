package com.cyanogenmod.trebuchet;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.TextView;
import com.cyanogenmod.trebuchet.DropTarget.DragObject;
import com.konka.ios7launcher.R;

public class ButtonDropTarget extends TextView implements DropTarget, DragListener {
    protected boolean mActive;
    private int mBottomDragPadding;
    protected final Paint mHoverPaint;
    protected Launcher mLauncher;
    protected SearchDropTargetBar mSearchDropTargetBar;
    protected final int mTransitionDuration;

    public ButtonDropTarget(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ButtonDropTarget(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mHoverPaint = new Paint();
        Resources r = getResources();
        this.mTransitionDuration = r.getInteger(R.integer.config_dropTargetBgTransitionDuration);
        this.mBottomDragPadding = r.getDimensionPixelSize(R.dimen.drop_target_drag_padding);
    }

    /* access modifiers changed from: 0000 */
    public void setLauncher(Launcher launcher) {
        this.mLauncher = launcher;
    }

    public boolean acceptDrop(DragObject d) {
        return false;
    }

    public void setSearchDropTargetBar(SearchDropTargetBar searchDropTargetBar) {
        this.mSearchDropTargetBar = searchDropTargetBar;
    }

    public void onDrop(DragObject d) {
    }

    public void onDragEnter(DragObject d) {
        d.dragView.setPaint(this.mHoverPaint);
    }

    public void onDragOver(DragObject d) {
    }

    public void onDragExit(DragObject d) {
        d.dragView.setPaint(null);
    }

    public void onDragStart(DragSource source, Object info, int dragAction) {
    }

    public boolean isDropEnabled() {
        return this.mActive;
    }

    public void onDragEnd() {
    }

    public void getHitRect(Rect outRect) {
        super.getHitRect(outRect);
        outRect.bottom += this.mBottomDragPadding;
    }

    public DropTarget getDropTargetDelegate(DragObject d) {
        return null;
    }

    public void getLocationInDragLayer(int[] loc) {
        this.mLauncher.getDragLayer().getLocationInDragLayer(this, loc);
    }
}
