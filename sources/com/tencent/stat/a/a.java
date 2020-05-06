package com.tencent.stat.a;

import android.content.Context;
import com.tencent.stat.StatAccount;
import com.tencent.stat.StatSpecifyReportedInfo;
import com.tencent.stat.common.q;
import org.json.JSONException;
import org.json.JSONObject;

public class a extends e {
    private StatAccount a = null;

    public a(Context context, int i, StatAccount statAccount, StatSpecifyReportedInfo statSpecifyReportedInfo) {
        super(context, i, statSpecifyReportedInfo);
        this.a = statAccount;
    }

    public f a() {
        return f.ADDITION;
    }

    public boolean a(JSONObject jSONObject) throws JSONException {
        q.a(jSONObject, "qq", this.a.getAccount());
        jSONObject.put("acc", this.a.toJsonString());
        return true;
    }
}
