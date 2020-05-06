package com.cyanogenmod.trebuchet;

import android.content.ComponentName;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.TransitionDrawable;
import android.util.AttributeSet;
import android.view.ViewGroup;
import com.cyanogenmod.trebuchet.DropTarget.DragObject;
import com.konka.ios7launcher.R;
import com.tencent.stat.common.StatConstants;

public class InfoDropTarget extends ButtonDropTarget {
    private TransitionDrawable mDrawable;
    private int mHoverColor;
    private ColorStateList mOriginalTextColor;

    public InfoDropTarget(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public InfoDropTarget(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mHoverColor = -16776961;
    }

    /* access modifiers changed from: protected */
    public void onFinishInflate() {
        super.onFinishInflate();
        this.mOriginalTextColor = getTextColors();
        this.mHoverColor = getResources().getColor(R.color.info_target_hover_tint);
        this.mHoverPaint.setColorFilter(new PorterDuffColorFilter(this.mHoverColor, Mode.SRC_ATOP));
        this.mDrawable = (TransitionDrawable) getCompoundDrawables()[0];
        this.mDrawable.setCrossFadeEnabled(true);
        if (getResources().getConfiguration().orientation == 2 && !LauncherApplication.isScreenLarge()) {
            setText(StatConstants.MTA_COOPERATION_TAG);
        }
    }

    private boolean isAllAppsApplication(DragSource source, Object info) {
        return (source instanceof AppsCustomizeView) && (info instanceof ApplicationInfo);
    }

    public boolean acceptDrop(DragObject d) {
        ComponentName componentName = null;
        if (d.dragInfo instanceof ApplicationInfo) {
            componentName = ((ApplicationInfo) d.dragInfo).componentName;
        } else if (d.dragInfo instanceof ShortcutInfo) {
            componentName = ((ShortcutInfo) d.dragInfo).intent.getComponent();
        }
        if (componentName != null) {
            this.mLauncher.startApplicationDetailsActivity(componentName);
        }
        return false;
    }

    public void onDragStart(DragSource source, Object info, int dragAction) {
        boolean isVisible = true;
        if (!isAllAppsApplication(source, info)) {
            isVisible = false;
        }
        this.mActive = isVisible;
        this.mDrawable.resetTransition();
        setTextColor(this.mOriginalTextColor);
        ((ViewGroup) getParent()).setVisibility(isVisible ? 0 : 8);
    }

    public void onDragEnd() {
        super.onDragEnd();
        this.mActive = false;
    }

    public void onDragEnter(DragObject d) {
        super.onDragEnter(d);
        this.mDrawable.startTransition(this.mTransitionDuration);
        setTextColor(this.mHoverColor);
    }

    public void onDragExit(DragObject d) {
        super.onDragExit(d);
        if (!d.dragComplete) {
            this.mDrawable.resetTransition();
            setTextColor(this.mOriginalTextColor);
        }
    }
}
