package com.umeng.analytics.a;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Build.VERSION;
import android.util.Log;
import com.tencent.mid.api.MidEntity;
import com.umeng.analytics.g;
import com.umeng.analytics.j;
import com.umeng.common.a;
import com.umeng.common.b;
import org.json.JSONObject;

/* compiled from: Header */
public class f implements g {
    public String A;
    public String B;
    public String C;
    public String D;
    public String E;
    public String F;
    private final String G = a.h;
    private final String H = a.e;
    private final String I = "device_id";
    private final String J = a.f;
    private final String K = MidEntity.TAG_MAC;
    private final String L = g.H;
    private final String M = "device_model";
    private final String N = "os";
    private final String O = "os_version";
    private final String P = "resolution";
    private final String Q = "cpu";
    private final String R = "gpu_vender";
    private final String S = "gpu_renderer";
    private final String T = "device_board";
    private final String U = "device_brand";
    private final String V = "device_manutime";
    private final String W = "device_manufacturer";
    private final String X = "device_manuid";
    private final String Y = "device_name";
    private final String Z = "app_version";
    public String a;
    private final String aa = a.g;
    private final String ab = "package_name";
    private final String ac = "sdk_type";
    private final String ad = a.i;
    private final String ae = "timezone";
    private final String af = "country";
    private final String ag = "language";
    private final String ah = "access";
    private final String ai = "access_subtype";
    private final String aj = "carrier";
    private final String ak = "wrapper_type";
    private final String al = "wrapper_version";
    public String b;
    public String c;
    public String d;
    public String e;
    public long f;
    public String g;
    public String h;
    public String i;
    public String j;
    public String k;
    public String l;
    public String m;
    public String n;
    public String o;
    public long p;
    public String q;
    public String r;
    public String s;
    public String t;
    public String u;
    public String v;
    public String w;
    public String x;
    public int y;
    public String z;

    public f() {
    }

    public f(String str, String str2) {
        this.a = str;
        this.b = str2;
    }

    private void c(JSONObject jSONObject) throws Exception {
        this.a = jSONObject.getString(a.h);
        this.c = jSONObject.getString("device_id");
        this.d = jSONObject.getString(a.f);
        if (jSONObject.has(MidEntity.TAG_MAC)) {
            this.e = jSONObject.getString(MidEntity.TAG_MAC);
        }
        if (jSONObject.has(a.e)) {
            this.b = jSONObject.getString(a.e);
        }
        if (jSONObject.has(g.H)) {
            this.f = jSONObject.getLong(g.H);
        }
    }

    private void d(JSONObject jSONObject) throws Exception {
        String str;
        String str2;
        String str3;
        String str4;
        String str5;
        String str6;
        String str7;
        String str8;
        String str9;
        String str10;
        String str11 = null;
        this.g = jSONObject.has("device_model") ? jSONObject.getString("device_model") : null;
        if (jSONObject.has("os")) {
            str = jSONObject.getString("os");
        } else {
            str = null;
        }
        this.h = str;
        if (jSONObject.has("os_version")) {
            str2 = jSONObject.getString("os_version");
        } else {
            str2 = null;
        }
        this.i = str2;
        if (jSONObject.has("resolution")) {
            str3 = jSONObject.getString("resolution");
        } else {
            str3 = null;
        }
        this.j = str3;
        if (jSONObject.has("cpu")) {
            str4 = jSONObject.getString("cpu");
        } else {
            str4 = null;
        }
        this.k = str4;
        if (jSONObject.has("gpu_vender")) {
            str5 = jSONObject.getString("gpu_vender");
        } else {
            str5 = null;
        }
        this.l = str5;
        if (jSONObject.has("gpu_renderer")) {
            str6 = jSONObject.getString("gpu_renderer");
        } else {
            str6 = null;
        }
        this.m = str6;
        if (jSONObject.has("device_board")) {
            str7 = jSONObject.getString("device_board");
        } else {
            str7 = null;
        }
        this.n = str7;
        if (jSONObject.has("device_brand")) {
            str8 = jSONObject.getString("device_brand");
        } else {
            str8 = null;
        }
        this.o = str8;
        this.p = jSONObject.has("device_manutime") ? jSONObject.getLong("device_manutime") : 0;
        if (jSONObject.has("device_manufacturer")) {
            str9 = jSONObject.getString("device_manufacturer");
        } else {
            str9 = null;
        }
        this.q = str9;
        if (jSONObject.has("device_manuid")) {
            str10 = jSONObject.getString("device_manuid");
        } else {
            str10 = null;
        }
        this.r = str10;
        if (jSONObject.has("device_name")) {
            str11 = jSONObject.getString("device_name");
        }
        this.s = str11;
    }

    private void e(JSONObject jSONObject) throws Exception {
        String str;
        String str2;
        String str3 = null;
        if (jSONObject.has("app_version")) {
            str = jSONObject.getString("app_version");
        } else {
            str = null;
        }
        this.t = str;
        if (jSONObject.has(a.g)) {
            str2 = jSONObject.getString(a.g);
        } else {
            str2 = null;
        }
        this.u = str2;
        if (jSONObject.has("package_name")) {
            str3 = jSONObject.getString("package_name");
        }
        this.v = str3;
    }

    private void f(JSONObject jSONObject) throws Exception {
        this.w = jSONObject.getString("sdk_type");
        this.x = jSONObject.getString(a.i);
    }

    private void g(JSONObject jSONObject) throws Exception {
        String str;
        String str2 = null;
        this.y = jSONObject.has("timezone") ? jSONObject.getInt("timezone") : 8;
        if (jSONObject.has("country")) {
            str = jSONObject.getString("country");
        } else {
            str = null;
        }
        this.z = str;
        if (jSONObject.has("language")) {
            str2 = jSONObject.getString("language");
        }
        this.A = str2;
    }

    private void h(JSONObject jSONObject) throws Exception {
        String str;
        String str2;
        String str3 = null;
        if (jSONObject.has("access")) {
            str = jSONObject.getString("access");
        } else {
            str = null;
        }
        this.B = str;
        if (jSONObject.has("access_subtype")) {
            str2 = jSONObject.getString("access_subtype");
        } else {
            str2 = null;
        }
        this.C = str2;
        if (jSONObject.has("carrier")) {
            str3 = jSONObject.getString("carrier");
        }
        this.D = str3;
    }

    private void i(JSONObject jSONObject) throws Exception {
        String str;
        String str2 = null;
        if (jSONObject.has("wrapper_type")) {
            str = jSONObject.getString("wrapper_type");
        } else {
            str = null;
        }
        this.E = str;
        if (jSONObject.has("wrapper_version")) {
            str2 = jSONObject.getString("wrapper_version");
        }
        this.F = str2;
    }

    public void a(JSONObject jSONObject) throws Exception {
        if (jSONObject != null) {
            c(jSONObject);
            d(jSONObject);
            e(jSONObject);
            f(jSONObject);
            g(jSONObject);
            h(jSONObject);
            i(jSONObject);
        }
    }

    private void j(JSONObject jSONObject) throws Exception {
        jSONObject.put(a.h, this.a);
        jSONObject.put("device_id", this.c);
        jSONObject.put(a.f, this.d);
        if (this.b != null) {
            jSONObject.put(a.e, this.b);
        }
        if (this.e != null) {
            jSONObject.put(MidEntity.TAG_MAC, this.e);
        }
        if (this.f > 0) {
            jSONObject.put(g.H, this.f);
        }
    }

    private void k(JSONObject jSONObject) throws Exception {
        if (this.g != null) {
            jSONObject.put("device_model", this.g);
        }
        if (this.h != null) {
            jSONObject.put("os", this.h);
        }
        if (this.i != null) {
            jSONObject.put("os_version", this.i);
        }
        if (this.j != null) {
            jSONObject.put("resolution", this.j);
        }
        if (this.k != null) {
            jSONObject.put("cpu", this.k);
        }
        if (this.l != null) {
            jSONObject.put("gpu_vender", this.l);
        }
        if (this.m != null) {
            jSONObject.put("gpu_vender", this.m);
        }
        if (this.n != null) {
            jSONObject.put("device_board", this.n);
        }
        if (this.o != null) {
            jSONObject.put("device_brand", this.o);
        }
        if (this.p > 0) {
            jSONObject.put("device_manutime", this.p);
        }
        if (this.q != null) {
            jSONObject.put("device_manufacturer", this.q);
        }
        if (this.r != null) {
            jSONObject.put("device_manuid", this.r);
        }
        if (this.s != null) {
            jSONObject.put("device_name", this.s);
        }
    }

    private void l(JSONObject jSONObject) throws Exception {
        if (this.t != null) {
            jSONObject.put("app_version", this.t);
        }
        if (this.u != null) {
            jSONObject.put(a.g, this.u);
        }
        if (this.v != null) {
            jSONObject.put("package_name", this.v);
        }
    }

    private void m(JSONObject jSONObject) throws Exception {
        jSONObject.put("sdk_type", this.w);
        jSONObject.put(a.i, this.x);
    }

    private void n(JSONObject jSONObject) throws Exception {
        jSONObject.put("timezone", this.y);
        if (this.z != null) {
            jSONObject.put("country", this.z);
        }
        if (this.A != null) {
            jSONObject.put("language", this.A);
        }
    }

    private void o(JSONObject jSONObject) throws Exception {
        if (this.B != null) {
            jSONObject.put("access", this.B);
        }
        if (this.C != null) {
            jSONObject.put("access_subtype", this.C);
        }
        if (this.D != null) {
            jSONObject.put("carrier", this.D);
        }
    }

    private void p(JSONObject jSONObject) throws Exception {
        if (this.E != null) {
            jSONObject.put("wrapper_type", this.E);
        }
        if (this.F != null) {
            jSONObject.put("wrapper_version", this.F);
        }
    }

    public void b(JSONObject jSONObject) throws Exception {
        j(jSONObject);
        k(jSONObject);
        l(jSONObject);
        m(jSONObject);
        n(jSONObject);
        o(jSONObject);
        p(jSONObject);
    }

    public boolean a() {
        if (this.a == null) {
            Log.e(g.q, "missing appkey ");
            return false;
        } else if (this.c != null && this.d != null) {
            return true;
        } else {
            Log.e(g.q, "missing device id");
            return false;
        }
    }

    public void a(Context context, String... strArr) {
        if (strArr != null && strArr.length == 2) {
            this.a = strArr[0];
            this.b = strArr[1];
        }
        if (this.a == null) {
            this.a = b.q(context);
        }
        if (this.b == null) {
            this.b = b.u(context);
        }
        this.c = b.g(context);
        this.d = b.h(context);
        this.e = b.r(context);
        SharedPreferences c2 = j.c(context);
        if (c2 != null) {
            this.f = c2.getLong(g.H, 0);
        }
    }

    public void a(Context context) {
        this.g = Build.MODEL;
        this.h = "Android";
        this.i = VERSION.RELEASE;
        this.j = b.s(context);
        this.k = b.a();
        this.n = Build.BOARD;
        this.o = Build.BRAND;
        this.p = Build.TIME;
        this.q = Build.MANUFACTURER;
        this.r = Build.ID;
        this.s = Build.DEVICE;
    }

    public void b(Context context) {
        this.t = b.e(context);
        this.u = b.d(context);
        this.v = b.v(context);
    }

    public void c(Context context) {
        this.w = "Android";
        this.x = g.c;
    }

    public void d(Context context) {
        this.y = b.o(context);
        String[] p2 = b.p(context);
        this.z = p2[0];
        this.A = p2[1];
    }

    public void e(Context context) {
        String[] k2 = b.k(context);
        this.B = k2[0];
        this.C = k2[1];
        this.D = b.t(context);
    }

    public void b(Context context, String... strArr) {
        a(context, strArr);
        a(context);
        b(context);
        c(context);
        d(context);
        e(context);
    }

    public boolean b() {
        if (this.a == null || this.c == null) {
            return false;
        }
        return true;
    }
}
