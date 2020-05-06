package com.cyanogenmod.trebuchet.preference;

import android.content.Context;
import android.content.res.TypedArray;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import com.android.internal.R;

public class DoubleNumberPickerPreference extends DialogPreference {
    private int mDefault1;
    private int mDefault2;
    private int mMax1;
    private int mMax2;
    private String mMaxExternalKey1;
    private String mMaxExternalKey2;
    private int mMin1;
    private int mMin2;
    private String mMinExternalKey1;
    private String mMinExternalKey2;
    private NumberPicker mNumberPicker1;
    private NumberPicker mNumberPicker2;
    private String mPickerTitle1;
    private String mPickerTitle2;

    public DoubleNumberPickerPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray dialogType = context.obtainStyledAttributes(attrs, R.styleable.DialogPreference, 0, 0);
        TypedArray doubleNumberPickerType = context.obtainStyledAttributes(attrs, com.konka.ios7launcher.R.styleable.DoubleNumberPickerPreference, 0, 0);
        this.mMaxExternalKey1 = doubleNumberPickerType.getString(6);
        this.mMinExternalKey1 = doubleNumberPickerType.getString(7);
        this.mMaxExternalKey2 = doubleNumberPickerType.getString(8);
        this.mMinExternalKey2 = doubleNumberPickerType.getString(9);
        this.mPickerTitle1 = doubleNumberPickerType.getString(10);
        this.mPickerTitle2 = doubleNumberPickerType.getString(11);
        this.mMax1 = doubleNumberPickerType.getInt(2, 5);
        this.mMin1 = doubleNumberPickerType.getInt(3, 0);
        this.mMax2 = doubleNumberPickerType.getInt(4, 5);
        this.mMin2 = doubleNumberPickerType.getInt(5, 0);
        this.mDefault1 = doubleNumberPickerType.getInt(0, this.mMin1);
        this.mDefault2 = doubleNumberPickerType.getInt(1, this.mMin2);
        dialogType.recycle();
        doubleNumberPickerType.recycle();
    }

    /* access modifiers changed from: protected */
    public View onCreateDialogView() {
        int max1 = this.mMax1;
        int min1 = this.mMin1;
        int max2 = this.mMax2;
        int min2 = this.mMin2;
        if (this.mMaxExternalKey1 != null) {
            max1 = getSharedPreferences().getInt(this.mMaxExternalKey1, this.mMax1);
        }
        if (this.mMinExternalKey1 != null) {
            min1 = getSharedPreferences().getInt(this.mMinExternalKey1, this.mMin1);
        }
        if (this.mMaxExternalKey2 != null) {
            max2 = getSharedPreferences().getInt(this.mMaxExternalKey2, this.mMax2);
        }
        if (this.mMinExternalKey2 != null) {
            min2 = getSharedPreferences().getInt(this.mMinExternalKey2, this.mMin2);
        }
        View view = ((LayoutInflater) getContext().getSystemService("layout_inflater")).inflate(com.konka.ios7launcher.R.layout.double_number_picker_dialog, null);
        this.mNumberPicker1 = (NumberPicker) view.findViewById(com.konka.ios7launcher.R.id.number_picker_1);
        this.mNumberPicker2 = (NumberPicker) view.findViewById(com.konka.ios7launcher.R.id.number_picker_2);
        if (this.mNumberPicker1 == null || this.mNumberPicker2 == null) {
            throw new RuntimeException("mNumberPicker1 or mNumberPicker2 is null!");
        }
        this.mNumberPicker1.setWrapSelectorWheel(false);
        this.mNumberPicker1.setMaxValue(max1);
        this.mNumberPicker1.setMinValue(min1);
        this.mNumberPicker1.setValue(getPersistedValue(1));
        this.mNumberPicker2.setWrapSelectorWheel(false);
        this.mNumberPicker2.setMaxValue(max2);
        this.mNumberPicker2.setMinValue(min2);
        this.mNumberPicker2.setValue(getPersistedValue(2));
        TextView pickerTitle1 = (TextView) view.findViewById(com.konka.ios7launcher.R.id.picker_title_1);
        TextView pickerTitle2 = (TextView) view.findViewById(com.konka.ios7launcher.R.id.picker_title_2);
        if (!(pickerTitle1 == null || pickerTitle2 == null)) {
            pickerTitle1.setText(this.mPickerTitle1);
            pickerTitle2.setText(this.mPickerTitle2);
        }
        EditText textInput1 = (EditText) this.mNumberPicker1.findViewById(16909330);
        EditText textInput2 = (EditText) this.mNumberPicker2.findViewById(16909330);
        if (!(textInput1 == null || textInput2 == null)) {
            textInput1.setCursorVisible(false);
            textInput1.setFocusable(false);
            textInput1.setFocusableInTouchMode(false);
            textInput2.setCursorVisible(false);
            textInput2.setFocusable(false);
            textInput2.setFocusableInTouchMode(false);
        }
        return view;
    }

    private int getPersistedValue(int value) {
        String[] values = getPersistedString(this.mDefault1 + "|" + this.mDefault2).split("\\|");
        if (value == 1) {
            try {
                return Integer.parseInt(values[0]);
            } catch (NumberFormatException e) {
                return this.mDefault1;
            }
        } else {
            try {
                return Integer.parseInt(values[1]);
            } catch (NumberFormatException e2) {
                return this.mDefault2;
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onDialogClosed(boolean positiveResult) {
        if (positiveResult) {
            persistString(new StringBuilder(String.valueOf(this.mNumberPicker1.getValue())).append("|").append(this.mNumberPicker2.getValue()).toString());
        }
    }

    public void setMin1(int min) {
        this.mMin1 = min;
    }

    public void setMax1(int max) {
        this.mMax1 = max;
    }

    public void setMin2(int min) {
        this.mMin2 = min;
    }

    public void setMax2(int max) {
        this.mMax2 = max;
    }

    public void setDefault1(int def) {
        this.mDefault1 = def;
    }

    public void setDefault2(int def) {
        this.mDefault2 = def;
    }
}
