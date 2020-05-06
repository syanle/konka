package com.tencent.mid.a;

import android.content.Context;
import android.util.Log;
import com.tencent.mid.api.MidEntity;
import com.tencent.mid.util.Util;
import com.tencent.mid.util.e;
import org.json.JSONObject;

public class g {
    protected Context a = null;
    private int b = 0;

    public g(Context context) {
        this.a = context;
        this.b = (int) (System.currentTimeMillis() / 1000);
    }

    public int a() {
        return 2;
    }

    public JSONObject a(JSONObject jSONObject) {
        if (jSONObject == null) {
            jSONObject = new JSONObject();
        }
        jSONObject.put("et", a());
        b(jSONObject);
        return jSONObject;
    }

    /* access modifiers changed from: protected */
    public void b(JSONObject jSONObject) {
        try {
            jSONObject.put(MidEntity.TAG_MID, "0");
            jSONObject.put(MidEntity.TAG_TIMESTAMPS, this.b);
            jSONObject.put("si", this.b);
            Util.jsonPut(jSONObject, MidEntity.TAG_IMEI, Util.getImei(this.a));
            Util.jsonPut(jSONObject, MidEntity.TAG_MAC, Util.getWifiMacAddress(this.a));
            MidEntity a2 = com.tencent.mid.b.g.a(this.a).a();
            if (a2 != null && Util.isMidValid(a2.getMid())) {
                jSONObject.put(MidEntity.TAG_MID, a2.getMid());
            }
            new e(this.a).a(jSONObject);
        } catch (Throwable th) {
            Log.e("MID", "encode error.", th);
        }
    }
}
