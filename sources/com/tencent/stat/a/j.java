package com.tencent.stat.a;

import android.content.Context;
import com.tencent.stat.StatSpecifyReportedInfo;
import com.tencent.stat.a;
import com.tencent.stat.common.k;
import com.tencent.stat.common.q;
import org.json.JSONException;
import org.json.JSONObject;

public class j extends e {
    private static String a = null;
    private String m = null;
    private String n = null;

    public j(Context context, int i, StatSpecifyReportedInfo statSpecifyReportedInfo) {
        super(context, i, statSpecifyReportedInfo);
        this.m = a.a(context).b();
        if (a == null) {
            a = k.i(context);
        }
    }

    public f a() {
        return f.NETWORK_MONITOR;
    }

    public void a(String str) {
        this.n = str;
    }

    public boolean a(JSONObject jSONObject) throws JSONException {
        q.a(jSONObject, "op", a);
        q.a(jSONObject, "cn", this.m);
        jSONObject.put("sp", this.n);
        return true;
    }
}
