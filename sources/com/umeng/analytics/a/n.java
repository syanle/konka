package com.umeng.analytics.a;

import com.konka.appupgrade.constant.Provider.ToBeUpgraded;
import com.umeng.analytics.g;
import com.umeng.common.Log;
import com.umeng.common.util.h;
import org.json.JSONObject;

/* compiled from: Time */
public class n {
    public String g;
    public String h;
    protected final String i = ToBeUpgraded.DATE;
    protected final String j = "time";

    public n() {
        String a = h.a();
        this.g = a.split(" ")[0];
        this.h = a.split(" ")[1];
    }

    public boolean a() {
        if (this.g != null && this.h != null) {
            return true;
        }
        Log.b(g.q, "Date or Time is not initialized");
        return false;
    }

    public void b(JSONObject jSONObject) throws Exception {
        jSONObject.put(ToBeUpgraded.DATE, this.g);
        jSONObject.put("time", this.h);
    }

    public void a(JSONObject jSONObject) throws Exception {
        this.g = jSONObject.getString(ToBeUpgraded.DATE);
        this.h = jSONObject.getString("time");
    }
}
