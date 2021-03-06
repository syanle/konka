package com.tencent.stat.a;

import android.content.Context;
import com.tencent.stat.StatGameUser;
import com.tencent.stat.StatSpecifyReportedInfo;
import com.tencent.stat.common.q;
import org.json.JSONException;
import org.json.JSONObject;

public class g extends e {
    private StatGameUser a = null;

    public g(Context context, int i, StatGameUser statGameUser, StatSpecifyReportedInfo statSpecifyReportedInfo) {
        super(context, i, statSpecifyReportedInfo);
        this.a = statGameUser.clone();
    }

    public f a() {
        return f.MTA_GAME_USER;
    }

    public boolean a(JSONObject jSONObject) throws JSONException {
        if (this.a == null) {
            return false;
        }
        q.a(jSONObject, "wod", this.a.getWorldName());
        q.a(jSONObject, "gid", this.a.getAccount());
        q.a(jSONObject, "lev", this.a.getLevel());
        return true;
    }
}
