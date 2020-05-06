package com.umeng.analytics.a;

import com.konka.appupgrade.constant.Provider.ToBeUpgraded;
import com.umeng.analytics.g;
import com.umeng.common.Log;
import java.io.PrintWriter;
import java.io.StringWriter;
import org.json.JSONObject;

/* compiled from: Error */
public class d extends n implements g {
    public String a;
    private final String b = "context";

    public d() {
    }

    public d(String str) {
        this.a = str;
    }

    public d(Throwable th) {
        this.a = a(th);
    }

    private String a(Throwable th) {
        if (th == null) {
            return null;
        }
        try {
            StringWriter stringWriter = new StringWriter();
            PrintWriter printWriter = new PrintWriter(stringWriter);
            th.printStackTrace(printWriter);
            for (Throwable cause = th.getCause(); cause != null; cause = cause.getCause()) {
                cause.printStackTrace(printWriter);
            }
            String obj = stringWriter.toString();
            printWriter.close();
            stringWriter.close();
            return obj;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean a() {
        if (this.a != null) {
            return super.a();
        }
        Log.b(g.q, "mContent is not initialized");
        return false;
    }

    public void a(JSONObject jSONObject) {
        if (jSONObject != null) {
            try {
                this.g = jSONObject.getString(ToBeUpgraded.DATE);
                this.h = jSONObject.getString("time");
                this.a = jSONObject.getString("context");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void b(JSONObject jSONObject) throws Exception {
        jSONObject.put(ToBeUpgraded.DATE, this.g);
        jSONObject.put("time", this.h);
        jSONObject.put("context", this.a);
    }
}
