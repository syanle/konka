package com.umeng.analytics.a;

import android.content.Context;
import android.content.SharedPreferences;
import com.umeng.analytics.g;
import com.umeng.analytics.j;
import com.umeng.common.Log;
import java.lang.reflect.Method;
import org.json.JSONObject;

/* compiled from: UTraffic */
public class p implements g {
    private static final String c = "uptr";
    private static final String d = "dntr";
    public long a = 0;
    public long b = 0;

    public void a(JSONObject jSONObject) throws Exception {
        if (jSONObject != null) {
            if (jSONObject.has(c)) {
                this.a = jSONObject.getLong(c);
            }
            if (jSONObject.has(d)) {
                this.b = jSONObject.getLong(d);
            }
        }
    }

    public void b(JSONObject jSONObject) throws Exception {
        if (this.a > 0) {
            jSONObject.put(c, this.a);
        }
        if (this.b > 0) {
            jSONObject.put(d, this.b);
        }
    }

    public boolean a() {
        if (this.a <= 0 || this.b <= 0) {
            return false;
        }
        return true;
    }

    public static p a(Context context) {
        try {
            p pVar = new p();
            long[] b2 = b(context);
            if (b2[0] <= 0 || b2[1] <= 0) {
                return null;
            }
            SharedPreferences e = j.e(context);
            long j = e.getLong(c, -1);
            long j2 = e.getLong(d, -1);
            e.edit().putLong(c, b2[1]).putLong(d, b2[0]).commit();
            if (j <= 0 || j2 <= 0) {
                return null;
            }
            b2[0] = b2[0] - j2;
            b2[1] = b2[1] - j;
            if (b2[0] <= 0 || b2[1] <= 0) {
                return null;
            }
            pVar.b = b2[0];
            pVar.a = b2[1];
            return pVar;
        } catch (Exception e2) {
            Log.e(g.q, "sdk less than 2.2 has get no traffic");
            return null;
        }
    }

    private static long[] b(Context context) throws Exception {
        Class cls = Class.forName("android.net.TrafficStats");
        Method method = cls.getMethod("getUidRxBytes", new Class[]{Integer.TYPE});
        Method method2 = cls.getMethod("getUidTxBytes", new Class[]{Integer.TYPE});
        int i = context.getApplicationInfo().uid;
        if (i == -1) {
            return null;
        }
        return new long[]{((Long) method.invoke(null, new Object[]{Integer.valueOf(i)})).longValue(), ((Long) method2.invoke(null, new Object[]{Integer.valueOf(i)})).longValue()};
    }
}
