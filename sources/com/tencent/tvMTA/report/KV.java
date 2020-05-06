package com.tencent.tvMTA.report;

import android.text.TextUtils;

public class KV {
    private String key;
    private Object value;

    public KV(String key2, Object value2) {
        this.key = key2;
        this.value = value2;
    }

    public String getKey() {
        return this.key;
    }

    public void setKey(String key2) {
        this.key = key2;
    }

    public Object getValue() {
        return this.value;
    }

    public void setValue(Object value2) {
        this.value = value2;
    }

    public boolean isValid() {
        return !TextUtils.isEmpty(this.key) && this.value != null && !TextUtils.isEmpty(this.value.toString());
    }
}
