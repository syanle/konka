package com.umeng.analytics.onlineconfig;

import com.konka.kkinterface.tv.ChannelDesk;
import com.umeng.analytics.g;
import com.umeng.common.Log;
import com.umeng.common.net.u;
import org.json.JSONObject;

/* compiled from: OnlineConfigResponse */
public class b extends u {
    public JSONObject a = null;
    boolean b = false;
    int c = -1;
    int d = -1;
    String e;
    private final String f = "config_update";
    private final String g = "report_policy";
    private final String h = "online_params";
    private final String i = "last_config_time";
    private final String j = g.G;

    public b(JSONObject jSONObject) {
        super(jSONObject);
        if (jSONObject != null) {
            a(jSONObject);
            a();
        }
    }

    private void a(JSONObject jSONObject) {
        try {
            if (jSONObject.has("config_update") && !jSONObject.getString("config_update").toLowerCase().equals("no")) {
                if (jSONObject.has("report_policy")) {
                    this.c = jSONObject.getInt("report_policy");
                    this.d = jSONObject.optInt(g.G) * ChannelDesk.max_dtv_count;
                    this.e = jSONObject.optString("last_config_time");
                } else {
                    Log.e(g.q, " online config fetch no report policy");
                }
                this.a = jSONObject.optJSONObject("online_params");
                this.b = true;
            }
        } catch (Exception e2) {
            Log.e(g.q, "fail to parce online config response", e2);
        }
    }

    private void a() {
        if (this.c < 0 || this.c > 6) {
            this.c = 1;
        }
        if (this.c == 6 && this.d <= 0) {
            this.d = g.h;
        }
    }
}
