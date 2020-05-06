package com.umeng.analytics.onlineconfig;

import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;
import com.tencent.stat.common.StatConstants;
import com.umeng.analytics.g;
import com.umeng.analytics.j;
import com.umeng.common.Log;
import com.umeng.common.net.s;
import com.umeng.common.net.t;
import com.umeng.common.util.h;
import java.util.Iterator;
import org.json.JSONObject;

/* compiled from: OnlineConfigAgent */
public class a {
    private final String a = "last_config_time";
    private final String b = "report_policy";
    private final String c = "online_config";
    private String d = null;
    private String e = null;
    private UmengOnlineConfigureListener f = null;
    /* access modifiers changed from: private */
    public c g = null;

    /* renamed from: com.umeng.analytics.onlineconfig.a$a reason: collision with other inner class name */
    /* compiled from: OnlineConfigAgent */
    public class C0000a extends t {
        private JSONObject e;

        public C0000a(JSONObject jSONObject) {
            super(null);
            this.e = jSONObject;
        }

        public JSONObject a() {
            return this.e;
        }

        public String b() {
            return this.d;
        }
    }

    /* compiled from: OnlineConfigAgent */
    public class b extends s implements Runnable {
        Context a;

        public b(Context context) {
            this.a = context.getApplicationContext();
        }

        public void run() {
            try {
                b();
            } catch (Exception e) {
                a.this.a((JSONObject) null);
                Log.c(g.q, "reques update error", e);
            }
        }

        public boolean a() {
            return false;
        }

        private void b() {
            C0000a aVar = new C0000a(a.this.d(this.a));
            String[] strArr = g.s;
            b bVar = null;
            for (String a2 : strArr) {
                aVar.a(a2);
                bVar = (b) a((t) aVar, b.class);
                if (bVar != null) {
                    break;
                }
            }
            if (bVar == null) {
                a.this.a((JSONObject) null);
                return;
            }
            Log.a(g.q, "response : " + bVar.b);
            if (bVar.b) {
                if (a.this.g != null) {
                    a.this.g.a(bVar.c, (long) bVar.d);
                }
                a.this.a(this.a, bVar);
                a.this.b(this.a, bVar);
                a.this.a(bVar.a);
                return;
            }
            a.this.a((JSONObject) null);
        }
    }

    public void a(Context context) {
        if (context == null) {
            try {
                Log.b(g.q, "unexpected null context in updateOnlineConfig");
            } catch (Exception e2) {
                Log.b(g.q, "exception in updateOnlineConfig");
            }
        } else {
            new Thread(new b(context)).start();
        }
    }

    public void a(Context context, String str, String str2) {
        this.d = str;
        this.e = str2;
        a(context);
    }

    public void a(UmengOnlineConfigureListener umengOnlineConfigureListener) {
        this.f = umengOnlineConfigureListener;
    }

    public void a() {
        this.f = null;
    }

    public void a(c cVar) {
        this.g = cVar;
    }

    public void b() {
        this.g = null;
    }

    /* access modifiers changed from: private */
    public void a(JSONObject jSONObject) {
        if (this.f != null) {
            this.f.onDataReceived(jSONObject);
        }
    }

    private String b(Context context) throws Exception {
        String str = this.d;
        if (str == null) {
            str = com.umeng.common.b.q(context);
        }
        if (str != null) {
            return str;
        }
        throw new Exception("none appkey exception");
    }

    private String c(Context context) {
        return this.e == null ? com.umeng.common.b.u(context) : this.e;
    }

    /* access modifiers changed from: private */
    public JSONObject d(Context context) {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put(com.umeng.common.a.c, "online_config");
            jSONObject.put(com.umeng.common.a.h, b(context));
            jSONObject.put(com.umeng.common.a.g, com.umeng.common.b.d(context));
            jSONObject.put(com.umeng.common.a.d, com.umeng.common.b.v(context));
            jSONObject.put(com.umeng.common.a.i, g.c);
            jSONObject.put(com.umeng.common.a.f, h.b(com.umeng.common.b.g(context)));
            jSONObject.put(com.umeng.common.a.e, c(context));
            jSONObject.put("report_policy", j.i(context)[0]);
            jSONObject.put("last_config_time", e(context));
            return jSONObject;
        } catch (Exception e2) {
            Log.b(g.q, "exception in onlineConfigInternal");
            return null;
        }
    }

    private String e(Context context) {
        return j.b(context).getString(g.C, StatConstants.MTA_COOPERATION_TAG);
    }

    /* access modifiers changed from: private */
    public void a(Context context, b bVar) {
        Editor edit = j.b(context).edit();
        if (!TextUtils.isEmpty(bVar.e)) {
            edit.putString(g.C, bVar.e);
        }
        if (bVar.c != -1) {
            edit.putInt(g.A, bVar.c);
            edit.putLong(g.B, (long) bVar.d);
        }
        edit.commit();
    }

    /* access modifiers changed from: private */
    public void b(Context context, b bVar) {
        if (bVar.a != null && bVar.a.length() != 0) {
            Editor edit = j.b(context).edit();
            try {
                JSONObject jSONObject = bVar.a;
                Iterator keys = jSONObject.keys();
                while (keys.hasNext()) {
                    String str = (String) keys.next();
                    edit.putString(str, jSONObject.getString(str));
                }
                edit.commit();
                Log.a(g.q, "get online setting params: " + jSONObject);
            } catch (Exception e2) {
                Log.c(g.q, "save online config params", e2);
            }
        }
    }
}
