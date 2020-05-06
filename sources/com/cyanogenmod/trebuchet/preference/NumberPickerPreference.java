package com.cyanogenmod.trebuchet.preference;

import android.content.Context;
import android.content.res.TypedArray;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;
import com.android.internal.R;

public class NumberPickerPreference extends DialogPreference {
    private int mDefault;
    private int mMax;
    private String mMaxExternalKey;
    private int mMin;
    private String mMinExternalKey;
    private NumberPicker mNumberPicker;

    public NumberPickerPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray dialogType = context.obtainStyledAttributes(attrs, R.styleable.DialogPreference, 0, 0);
        TypedArray numberPickerType = context.obtainStyledAttributes(attrs, com.konka.ios7launcher.R.styleable.NumberPickerPreference, 0, 0);
        this.mMaxExternalKey = numberPickerType.getString(2);
        this.mMinExternalKey = numberPickerType.getString(3);
        this.mMax = numberPickerType.getInt(0, 5);
        this.mMin = numberPickerType.getInt(1, 0);
        this.mDefault = dialogType.getInt(11, this.mMin);
        dialogType.recycle();
        numberPickerType.recycle();
    }

    /* access modifiers changed from: protected */
    public View onCreateDialogView() {
        int max = this.mMax;
        int min = this.mMin;
        if (this.mMaxExternalKey != null) {
            max = getSharedPreferences().getInt(this.mMaxExternalKey, this.mMax);
        }
        if (this.mMinExternalKey != null) {
            min = getSharedPreferences().getInt(this.mMinExternalKey, this.mMin);
        }
        View view = ((LayoutInflater) getContext().getSystemService("layout_inflater")).inflate(com.konka.ios7launcher.R.layout.number_picker_dialog, null);
        this.mNumberPicker = (NumberPicker) view.findViewById(com.konka.ios7launcher.R.id.number_picker);
        if (this.mNumberPicker == null) {
            throw new RuntimeException("mNumberPicker is null!");
        }
        this.mNumberPicker.setWrapSelectorWheel(false);
        this.mNumberPicker.setMaxValue(max);
        this.mNumberPicker.setMinValue(min);
        this.mNumberPicker.setValue(getPersistedInt(this.mDefault));
        EditText textInput = (EditText) this.mNumberPicker.findViewById(16909330);
        if (textInput != null) {
            textInput.setCursorVisible(false);
            textInput.setFocusable(false);
            textInput.setFocusableInTouchMode(false);
        }
        return view;
    }

    /* access modifiers changed from: protected */
    public void onDialogClosed(boolean positiveResult) {
        if (positiveResult) {
            persistInt(this.mNumberPicker.getValue());
        }
    }
}
