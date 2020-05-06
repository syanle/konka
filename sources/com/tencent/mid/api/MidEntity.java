package com.tencent.mid.api;

import android.util.Log;
import com.tencent.mid.util.Util;
import com.tencent.stat.common.StatConstants;
import org.json.JSONException;
import org.json.JSONObject;

public class MidEntity {
    public static final String TAG_IMEI = "ui";
    public static final String TAG_MAC = "mc";
    public static final String TAG_MID = "mid";
    public static final String TAG_TIMESTAMPS = "ts";
    private String a = null;
    private String b = null;
    private String c = "0";
    private long d = 0;

    public static MidEntity parse(String str) {
        MidEntity midEntity = new MidEntity();
        if (Util.isStringValid(str)) {
            try {
                JSONObject jSONObject = new JSONObject(str);
                if (!jSONObject.isNull(TAG_IMEI)) {
                    midEntity.setImei(jSONObject.getString(TAG_IMEI));
                }
                if (!jSONObject.isNull(TAG_MAC)) {
                    midEntity.setMac(jSONObject.getString(TAG_MAC));
                }
                if (!jSONObject.isNull(TAG_MID)) {
                    midEntity.setMid(jSONObject.getString(TAG_MID));
                }
                if (!jSONObject.isNull(TAG_TIMESTAMPS)) {
                    midEntity.setTimestamps(jSONObject.getLong(TAG_TIMESTAMPS));
                }
            } catch (JSONException e) {
                Log.w("MID", StatConstants.MTA_COOPERATION_TAG, e);
            }
        }
        return midEntity;
    }

    /* access modifiers changed from: 0000 */
    public JSONObject a() {
        JSONObject jSONObject = new JSONObject();
        try {
            Util.jsonPut(jSONObject, TAG_IMEI, this.a);
            Util.jsonPut(jSONObject, TAG_MAC, this.b);
            Util.jsonPut(jSONObject, TAG_MID, this.c);
            jSONObject.put(TAG_TIMESTAMPS, this.d);
        } catch (JSONException e) {
            Util.logWarn(e);
        }
        return jSONObject;
    }

    public int compairTo(MidEntity midEntity) {
        if (midEntity == null) {
            return 1;
        }
        if (!isMidValid() || !midEntity.isMidValid()) {
            return !isMidValid() ? -1 : 1;
        }
        if (this.c.equals(midEntity.c)) {
            return 0;
        }
        return this.d < midEntity.d ? -1 : 1;
    }

    public String getImei() {
        return this.a;
    }

    public String getMac() {
        return this.b;
    }

    public String getMid() {
        return this.c;
    }

    public long getTimestamps() {
        return this.d;
    }

    public boolean isMidValid() {
        return Util.isMidValid(this.c);
    }

    public void setImei(String str) {
        this.a = str;
    }

    public void setMac(String str) {
        this.b = str;
    }

    public void setMid(String str) {
        this.c = str;
    }

    public void setTimestamps(long j) {
        this.d = j;
    }

    public String toString() {
        return a().toString();
    }
}
