package com.tencent.mid.util;

import android.content.Context;
import android.os.Build;
import android.os.Build.VERSION;
import android.util.DisplayMetrics;
import java.util.Locale;
import java.util.TimeZone;
import org.json.JSONArray;
import org.json.JSONObject;

class g {
    String a;
    String b;
    DisplayMetrics c;
    int d;
    String e;
    String f;
    String g;
    String h;
    String i;
    String j;
    String k;
    int l;
    String m;
    Context n;
    private String o;
    private String p;
    private String q;
    private String r;

    private g(Context context) {
        this.b = String.valueOf(2.1f);
        this.d = VERSION.SDK_INT;
        this.e = Build.MODEL;
        this.f = Build.MANUFACTURER;
        this.g = Locale.getDefault().getLanguage();
        this.l = 0;
        this.m = null;
        this.n = null;
        this.o = null;
        this.p = null;
        this.q = null;
        this.r = null;
        this.n = context;
        this.c = i.a(context);
        this.a = i.c(context);
        this.i = i.b(context);
        this.j = TimeZone.getDefault().getID();
        this.k = i.f(context);
        this.m = context.getPackageName();
        this.r = i.a();
    }

    /* access modifiers changed from: 0000 */
    public void a(JSONObject jSONObject) {
        jSONObject.put("sr", this.c.widthPixels + "*" + this.c.heightPixels);
        Util.jsonPut(jSONObject, "av", this.a);
        Util.jsonPut(jSONObject, "ch", this.h);
        Util.jsonPut(jSONObject, "mf", this.f);
        Util.jsonPut(jSONObject, "sv", this.b);
        Util.jsonPut(jSONObject, "ov", Integer.toString(this.d));
        jSONObject.put("os", 1);
        Util.jsonPut(jSONObject, "op", this.i);
        Util.jsonPut(jSONObject, "lg", this.g);
        Util.jsonPut(jSONObject, "md", this.e);
        Util.jsonPut(jSONObject, "tz", this.j);
        if (this.l != 0) {
            jSONObject.put("jb", this.l);
        }
        Util.jsonPut(jSONObject, "sd", this.k);
        Util.jsonPut(jSONObject, "apn", this.m);
        if (Util.isNetworkAvailable(this.n) && Util.isWifiNet(this.n)) {
            JSONObject jSONObject2 = new JSONObject();
            Util.jsonPut(jSONObject2, "bs", Util.getWiFiBBSID(this.n));
            Util.jsonPut(jSONObject2, "ss", Util.getWiFiSSID(this.n));
            if (jSONObject2.length() > 0) {
                Util.jsonPut(jSONObject, "wf", jSONObject2.toString());
            }
        }
        JSONArray wifiTopN = Util.getWifiTopN(this.n, 10);
        if (wifiTopN != null && wifiTopN.length() > 0) {
            Util.jsonPut(jSONObject, "wflist", wifiTopN.toString());
        }
        Util.jsonPut(jSONObject, "sen", this.o);
        Util.jsonPut(jSONObject, "cpu", this.p);
        Util.jsonPut(jSONObject, "ram", this.q);
        Util.jsonPut(jSONObject, "rom", this.r);
    }
}
