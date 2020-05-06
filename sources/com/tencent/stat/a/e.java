package com.tencent.stat.a;

import android.content.Context;
import com.tencent.mid.api.MidEntity;
import com.tencent.mid.util.Util;
import com.tencent.stat.StatConfig;
import com.tencent.stat.StatSpecifyReportedInfo;
import com.tencent.stat.au;
import com.tencent.stat.common.StatConstants;
import com.tencent.stat.common.a;
import com.tencent.stat.common.k;
import com.tencent.stat.common.q;
import org.json.JSONException;
import org.json.JSONObject;

public abstract class e {
    protected static String j = null;
    private StatSpecifyReportedInfo a = null;
    protected String b = null;
    protected long c;
    protected int d;
    protected a e = null;
    protected int f;
    protected String g = null;
    protected String h = null;
    protected String i = null;
    protected boolean k = false;
    protected Context l;

    e(Context context, int i2, StatSpecifyReportedInfo statSpecifyReportedInfo) {
        this.l = context;
        this.c = System.currentTimeMillis() / 1000;
        this.d = i2;
        this.h = StatConfig.getInstallChannel(context);
        this.i = k.j(context);
        this.b = StatConfig.getAppKey(context);
        if (statSpecifyReportedInfo != null) {
            this.a = statSpecifyReportedInfo;
            if (k.c(statSpecifyReportedInfo.getAppKey())) {
                this.b = statSpecifyReportedInfo.getAppKey();
            }
            if (k.c(statSpecifyReportedInfo.getInstallChannel())) {
                this.h = statSpecifyReportedInfo.getInstallChannel();
            }
            if (k.c(statSpecifyReportedInfo.getVersion())) {
                this.i = statSpecifyReportedInfo.getVersion();
            }
            this.k = statSpecifyReportedInfo.isImportant();
        }
        this.g = StatConfig.getCustomUserId(context);
        this.e = au.a(context).b(context);
        if (a() != f.NETWORK_DETECTOR) {
            this.f = k.s(context).intValue();
        } else {
            this.f = -f.NETWORK_DETECTOR.a();
        }
        if (!Util.isMidValid(j)) {
            j = StatConfig.getLocalMidOnly(context);
            if (!k.c(j)) {
                j = "0";
            }
        }
    }

    public abstract f a();

    public abstract boolean a(JSONObject jSONObject) throws JSONException;

    public boolean b(JSONObject jSONObject) {
        try {
            q.a(jSONObject, "ky", this.b);
            jSONObject.put("et", a().a());
            if (this.e != null) {
                jSONObject.put(MidEntity.TAG_IMEI, this.e.b());
                q.a(jSONObject, MidEntity.TAG_MAC, this.e.c());
                int d2 = this.e.d();
                jSONObject.put("ut", d2);
                if (d2 == 0 && k.w(this.l) == 1) {
                    jSONObject.put("ia", 1);
                }
            }
            q.a(jSONObject, "cui", this.g);
            if (a() != f.SESSION_ENV) {
                q.a(jSONObject, "av", this.i);
                q.a(jSONObject, "ch", this.h);
            }
            if (this.k) {
                jSONObject.put("impt", 1);
            }
            q.a(jSONObject, MidEntity.TAG_MID, j);
            jSONObject.put("idx", this.f);
            jSONObject.put("si", this.d);
            jSONObject.put(MidEntity.TAG_TIMESTAMPS, this.c);
            jSONObject.put("dts", k.a(this.l, false));
            return a(jSONObject);
        } catch (Throwable th) {
            return false;
        }
    }

    public long c() {
        return this.c;
    }

    public StatSpecifyReportedInfo d() {
        return this.a;
    }

    public Context e() {
        return this.l;
    }

    public boolean f() {
        return this.k;
    }

    public String g() {
        try {
            JSONObject jSONObject = new JSONObject();
            b(jSONObject);
            return jSONObject.toString();
        } catch (Throwable th) {
            return StatConstants.MTA_COOPERATION_TAG;
        }
    }
}
