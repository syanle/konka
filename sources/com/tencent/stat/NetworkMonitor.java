package com.tencent.stat;

import com.tencent.mid.api.MidEntity;
import com.tencent.stat.common.StatConstants;
import org.json.JSONException;
import org.json.JSONObject;

public class NetworkMonitor {
    private long a = 0;
    private int b = 0;
    private String c = StatConstants.MTA_COOPERATION_TAG;
    private int d = 0;
    private String e = StatConstants.MTA_COOPERATION_TAG;

    public String getDomain() {
        return this.c;
    }

    public long getMillisecondsConsume() {
        return this.a;
    }

    public int getPort() {
        return this.d;
    }

    public String getRemoteIp() {
        return this.e;
    }

    public int getStatusCode() {
        return this.b;
    }

    public void setDomain(String str) {
        this.c = str;
    }

    public void setMillisecondsConsume(long j) {
        this.a = j;
    }

    public void setPort(int i) {
        this.d = i;
    }

    public void setRemoteIp(String str) {
        this.e = str;
    }

    public void setStatusCode(int i) {
        this.b = i;
    }

    public JSONObject toJSONObject() {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("tm", this.a);
            jSONObject.put("st", this.b);
            if (this.c != null) {
                jSONObject.put("dm", this.c);
            }
            jSONObject.put("pt", this.d);
            if (this.e != null) {
                jSONObject.put("rip", this.e);
            }
            jSONObject.put(MidEntity.TAG_TIMESTAMPS, System.currentTimeMillis() / 1000);
        } catch (JSONException e2) {
        }
        return jSONObject;
    }
}
