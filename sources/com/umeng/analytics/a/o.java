package com.umeng.analytics.a;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Location;
import com.cyanogenmod.trebuchet.RocketLauncher.Board.FlyingIcon;
import com.umeng.analytics.g;
import com.umeng.analytics.j;
import com.umeng.common.b;
import org.json.JSONException;
import org.json.JSONObject;

/* compiled from: ULocation */
public final class o implements g {
    public static final String d = "lng";
    public static final String e = "lat";
    public static final String f = "gps_time";
    private static final String g = "last_gps_change_time";
    public double a;
    public double b;
    public long c;

    public o() {
    }

    public o(Location location) {
        if (location != null) {
            this.a = location.getLongitude();
            this.b = location.getLatitude();
            this.c = location.getTime();
        }
    }

    public static o a(Context context) {
        if (!g.i) {
            return null;
        }
        SharedPreferences e2 = j.e(context);
        Location m = b.m(context);
        if (a(m, e2)) {
            return new o(m);
        }
        return null;
    }

    public static o b(Context context) {
        if (!g.i) {
            return null;
        }
        SharedPreferences e2 = j.e(context);
        o oVar = new o();
        if (!e2.contains(f)) {
            return null;
        }
        oVar.b = (double) e2.getFloat(e, FlyingIcon.ANGULAR_VMIN);
        oVar.a = (double) e2.getFloat(d, FlyingIcon.ANGULAR_VMIN);
        oVar.c = e2.getLong(f, 0);
        e2.edit().remove(f).commit();
        return oVar;
    }

    public static Editor a(Context context, SharedPreferences sharedPreferences) {
        if (!g.i) {
            return null;
        }
        Location m = b.m(context);
        if (!a(m, sharedPreferences)) {
            return null;
        }
        Editor edit = sharedPreferences.edit();
        edit.putFloat(d, (float) m.getLongitude());
        edit.putFloat(e, (float) m.getLatitude());
        edit.putLong(f, m.getTime());
        edit.commit();
        return edit;
    }

    static boolean a(Location location, SharedPreferences sharedPreferences) {
        if (location == null || location.getTime() <= sharedPreferences.getLong(g, 0)) {
            return false;
        }
        sharedPreferences.edit().putLong(g, location.getTime()).commit();
        return true;
    }

    public void b(JSONObject jSONObject) throws Exception {
        jSONObject.put(d, this.a);
        jSONObject.put(e, this.b);
        jSONObject.put(f, this.c);
    }

    public void a(JSONObject jSONObject) throws JSONException {
        if (jSONObject != null) {
            if (jSONObject.has(d)) {
                this.a = jSONObject.getDouble(d);
            }
            if (jSONObject.has(e)) {
                this.b = jSONObject.getDouble(e);
            }
            if (jSONObject.has(f)) {
                this.c = jSONObject.getLong(f);
            }
        }
    }

    public boolean a() {
        if (this.a == 0.0d && this.b == 0.0d && this.c == 0) {
            return false;
        }
        return true;
    }
}
