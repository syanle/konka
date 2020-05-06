package com.tencent.stat.common;

import com.tencent.mid.api.MidEntity;
import org.json.JSONException;
import org.json.JSONObject;

public class a {
    private String a = null;
    private String b = null;
    private String c = null;
    private String d = "0";
    private int e;
    private int f = 0;
    private long g = 0;

    public a() {
    }

    public a(String str, String str2, int i) {
        this.a = str;
        this.b = str2;
        this.e = i;
    }

    /* access modifiers changed from: 0000 */
    public JSONObject a() {
        JSONObject jSONObject = new JSONObject();
        try {
            q.a(jSONObject, MidEntity.TAG_IMEI, this.a);
            q.a(jSONObject, MidEntity.TAG_MAC, this.b);
            q.a(jSONObject, MidEntity.TAG_MID, this.d);
            q.a(jSONObject, "aid", this.c);
            jSONObject.put(MidEntity.TAG_TIMESTAMPS, this.g);
            jSONObject.put("ver", this.f);
        } catch (JSONException e2) {
        }
        return jSONObject;
    }

    public void a(int i) {
        this.e = i;
    }

    public String b() {
        return this.a;
    }

    public String c() {
        return this.b;
    }

    public int d() {
        return this.e;
    }

    public String toString() {
        return a().toString();
    }
}
