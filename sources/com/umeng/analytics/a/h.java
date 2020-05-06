package com.umeng.analytics.a;

import android.content.Context;
import com.umeng.analytics.g;
import com.umeng.common.Log;
import org.json.JSONObject;

/* compiled from: Launch */
public class h extends l implements g {
    o a;

    public h() {
    }

    public h(Context context, String str) {
        this.e = str;
        this.a = o.a(context);
    }

    public void a(o oVar) {
        this.a = oVar;
    }

    public void c(JSONObject jSONObject) throws Exception {
        if (this.a != null) {
            this.a.b(jSONObject);
        }
    }

    public void d(JSONObject jSONObject) throws Exception {
        o oVar = new o();
        oVar.a(jSONObject);
        if (oVar.a()) {
            this.a = oVar;
        }
    }

    public boolean a() {
        if (this.a == null && g.i) {
            Log.c(g.q, "missing location info in Launch");
        }
        return super.a();
    }

    public void a(JSONObject jSONObject) {
        if (jSONObject != null) {
            try {
                super.a(jSONObject);
                d(jSONObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void b(JSONObject jSONObject) throws Exception {
        c(jSONObject);
        super.b(jSONObject);
    }

    public JSONObject b() {
        Exception e;
        JSONObject jSONObject;
        try {
            jSONObject = new JSONObject();
            try {
                b(jSONObject);
            } catch (Exception e2) {
                e = e2;
                e.printStackTrace();
                return jSONObject;
            }
        } catch (Exception e3) {
            Exception exc = e3;
            jSONObject = null;
            e = exc;
            e.printStackTrace();
            return jSONObject;
        }
        return jSONObject;
    }
}
