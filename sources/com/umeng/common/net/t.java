package com.umeng.common.net;

import org.json.JSONObject;

/* compiled from: URequest */
public abstract class t {
    protected static String b = "POST";
    protected static String c = "GET";
    protected String d;

    public abstract JSONObject a();

    public abstract String b();

    /* access modifiers changed from: protected */
    public String c() {
        return b;
    }

    public t(String str) {
        this.d = str;
    }

    public void a(String str) {
        this.d = str;
    }

    public String d() {
        return this.d;
    }
}
