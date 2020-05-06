package com.cyanogenmod.trebuchet;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.konka.ios7launcher.R;

public class UpgradeItem extends RelativeLayout {
    private Button mDetailButton;
    private TextView mIconView;
    private TextView mPrimaryText;
    private TextView mSecondaryText;

    public UpgradeItem(Context context) {
        this(context, null, 0);
    }

    public UpgradeItem(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public UpgradeItem(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public static UpgradeItem fromXml(Context context) {
        return (UpgradeItem) LayoutInflater.from(context).inflate(R.layout.qsb_upgrade_item, null);
    }

    /* access modifiers changed from: protected */
    public void onFinishInflate() {
        super.onFinishInflate();
        this.mIconView = (TextView) findViewById(R.id.icon);
        this.mPrimaryText = (TextView) findViewById(R.id.primary_text);
        this.mSecondaryText = (TextView) findViewById(R.id.secondary_text);
        this.mDetailButton = (Button) findViewById(R.id.detailButton);
    }

    public UpgradeItem setIcon(int textId, int iconId) {
        this.mIconView.setText(textId);
        this.mIconView.setCompoundDrawablesWithIntrinsicBounds(0, iconId, 0, 0);
        return this;
    }

    public UpgradeItem setPrimaryText(int resId) {
        this.mPrimaryText.setText(resId);
        return this;
    }

    public UpgradeItem setPrimaryText(CharSequence text) {
        this.mPrimaryText.setText(text);
        return this;
    }

    public UpgradeItem setSecondaryText(int resId) {
        this.mSecondaryText.setText(resId);
        return this;
    }

    public UpgradeItem setSecondaryText(CharSequence text) {
        this.mSecondaryText.setText(text);
        return this;
    }

    public UpgradeItem setButton(int textId, OnClickListener listener) {
        this.mDetailButton.setText(textId);
        this.mDetailButton.setOnClickListener(listener);
        return this;
    }
}
