package com.tencent.stat.a;

import android.content.Context;
import com.tencent.stat.StatConfig;
import com.tencent.stat.StatSpecifyReportedInfo;
import com.tencent.stat.common.q;
import org.json.JSONException;
import org.json.JSONObject;

public class i extends e {
    public static final StatSpecifyReportedInfo a = new StatSpecifyReportedInfo();

    static {
        a.setAppKey("A9VH9B8L4GX4");
    }

    public i(Context context) {
        super(context, 0, a);
    }

    public f a() {
        return f.NETWORK_DETECTOR;
    }

    public boolean a(JSONObject jSONObject) throws JSONException {
        q.a(jSONObject, "actky", StatConfig.getAppKey(this.l));
        return true;
    }
}
