package com.umeng.analytics;

import android.content.Context;
import android.util.Log;
import com.tencent.stat.common.StatConstants;
import com.umeng.analytics.a.a;
import com.umeng.analytics.a.d;
import com.umeng.analytics.a.e;
import com.umeng.analytics.a.g;
import com.umeng.analytics.a.h;
import com.umeng.analytics.a.i;
import com.umeng.analytics.a.m;
import com.umeng.common.b;
import java.util.HashMap;
import org.json.JSONObject;

/* compiled from: MessageBuffer */
public class c {
    private i a = new i();
    private HashMap<String, k> b = new HashMap<>();
    private HashMap<String, HashMap<String, String>> c = new HashMap<>();
    private int d = 10;

    public void a(int i) {
        this.d = i;
    }

    public int a() {
        return this.a.b();
    }

    public boolean b() {
        return this.a.b() > this.d;
    }

    public void a(String str) {
        if (this.b.containsKey(str)) {
            ((k) this.b.get(str)).a(Long.valueOf(System.currentTimeMillis()));
            return;
        }
        k kVar = new k(str);
        kVar.a(Long.valueOf(System.currentTimeMillis()));
        this.b.put(str, kVar);
    }

    public long b(String str) {
        if (this.b.containsKey(str)) {
            return ((k) this.b.get(str)).a().longValue();
        }
        return -1;
    }

    public void a(String str, HashMap<String, String> hashMap) {
        if (!this.c.containsKey(str)) {
            this.c.put(str, hashMap);
        }
    }

    public HashMap<String, String> c(String str) {
        if (!this.b.containsKey(str) || ((k) this.b.get(str)).b() <= 0) {
            return (HashMap) this.c.remove(str);
        }
        return (HashMap) this.c.get(str);
    }

    public synchronized void a(String str, String str2, String str3, long j, int i) {
        this.a.a(new e(str, str2, str3, i, j));
    }

    public synchronized void a(String str, String str2, HashMap<String, String> hashMap, long j) {
        this.a.a(str, new a(str2, hashMap, j));
    }

    public synchronized void a(d dVar) {
        this.a.a(dVar);
    }

    public synchronized void a(h hVar) {
        this.a.a(hVar);
    }

    public synchronized void a(m mVar) {
        this.a.a(mVar);
    }

    public boolean c() {
        return this.a.a();
    }

    public void a(Context context) {
        if (a() > 0) {
            String d2 = b.d(context);
            JSONObject a2 = j.a(context, d2);
            i iVar = new i();
            if (a2 != null) {
                iVar.a(a2);
            }
            synchronized (this) {
                iVar.a(this.a);
                this.a.c();
            }
            j.a(context, (g) iVar, d2);
        }
    }

    public void b(Context context) {
        JSONObject a2 = j.a(context, b.d(context));
        if (a2 != null && a2.length() != 0) {
            i iVar = new i();
            iVar.a(a2);
            synchronized (this) {
                this.a.a(iVar);
            }
        }
    }

    public synchronized JSONObject d() {
        JSONObject jSONObject;
        try {
            jSONObject = new JSONObject();
            this.a.b(jSONObject);
        } catch (Exception e) {
            Log.d(g.q, StatConstants.MTA_COOPERATION_TAG, e);
            jSONObject = null;
        }
        return jSONObject;
    }

    public synchronized void e() {
        this.a.c();
    }
}
