package com.tencent.stat.a;

import android.content.Context;
import com.tencent.stat.StatSpecifyReportedInfo;
import com.tencent.stat.common.b;
import com.tencent.stat.common.k;
import org.json.JSONException;
import org.json.JSONObject;

public class l extends e {
    private b a;
    private JSONObject m = null;

    public l(Context context, int i, JSONObject jSONObject, StatSpecifyReportedInfo statSpecifyReportedInfo) {
        super(context, i, statSpecifyReportedInfo);
        this.a = new b(context);
        this.m = jSONObject;
    }

    public f a() {
        return f.SESSION_ENV;
    }

    public boolean a(JSONObject jSONObject) throws JSONException {
        if (this.e != null) {
            jSONObject.put("ut", this.e.d());
        }
        if (this.m != null) {
            jSONObject.put("cfg", this.m);
        }
        if (k.y(this.l)) {
            jSONObject.put("ncts", 1);
        }
        this.a.a(jSONObject, (Thread) null);
        return true;
    }
}
