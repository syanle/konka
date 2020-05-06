package com.cyanogenmod.trebuchet;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import com.konka.ios7launcher.R;

public class PagedDialogViewIcon extends FrameLayout implements Checkable {
    private static final int[] CHECKED_STATE_SET = {16842912};
    private static final String TAG = "PagedDialogViewIcon";
    private boolean mBroadcasting;
    private CheckBox mCheckbox;
    private OnCheckedChangeListener mOnCheckedChangeListener;
    private PagedViewIcon mPagedViewIcon;

    public interface OnCheckedChangeListener {
        void onCheckedChanged(PagedDialogViewIcon pagedDialogViewIcon, boolean z);
    }

    public PagedDialogViewIcon(Context context) {
        this(context, null);
    }

    public PagedDialogViewIcon(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PagedDialogViewIcon(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setDescendantFocusability(393216);
        LayoutInflater.from(context).inflate(R.layout.paged_dialog_apps_icon, this, true);
        this.mPagedViewIcon = (PagedViewIcon) findViewById(R.id.paged_view_icon);
        this.mPagedViewIcon.setLayoutParams(new LayoutParams(-2, -2, 17));
        this.mPagedViewIcon.setPadding(0, 0, 0, 0);
        this.mPagedViewIcon.setBackgroundDrawable(null);
        this.mPagedViewIcon.setClickable(false);
        this.mCheckbox = (CheckBox) findViewById(R.id.paged_view_checkbox);
    }

    public void applyFromApplicationInfo(ApplicationInfo info, HolographicOutlineHelper holoOutlineHelper) {
        this.mPagedViewIcon.applyFromApplicationInfo(info, holoOutlineHelper);
    }

    public ApplicationInfo getApplicationInfo() {
        return (ApplicationInfo) this.mPagedViewIcon.getTag();
    }

    public void setChecked(boolean checked) {
        if (this.mCheckbox.isChecked() != checked) {
            this.mCheckbox.setChecked(checked);
            if (!this.mBroadcasting) {
                this.mBroadcasting = true;
                if (this.mOnCheckedChangeListener != null) {
                    this.mOnCheckedChangeListener.onCheckedChanged(this, checked);
                }
                this.mBroadcasting = false;
            }
        }
    }

    public void setOnCheckedChangeListener(OnCheckedChangeListener listener) {
        this.mOnCheckedChangeListener = listener;
    }

    public void onInitializeAccessibilityEvent(AccessibilityEvent event) {
        super.onInitializeAccessibilityEvent(event);
        event.setChecked(this.mCheckbox.isChecked());
    }

    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
        super.onInitializeAccessibilityNodeInfo(info);
        info.setCheckable(true);
        info.setChecked(this.mCheckbox.isChecked());
    }

    /* access modifiers changed from: protected */
    public int[] onCreateDrawableState(int extraSpace) {
        int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
        if (isChecked()) {
            mergeDrawableStates(drawableState, CHECKED_STATE_SET);
        }
        return drawableState;
    }

    public boolean isChecked() {
        if (this.mCheckbox == null) {
            return false;
        }
        return this.mCheckbox.isChecked();
    }

    public void toggle() {
        this.mCheckbox.toggle();
    }
}
