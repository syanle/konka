package com.umeng.analytics;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;
import com.tencent.stat.common.StatConstants;
import com.umeng.analytics.a.d;
import com.umeng.analytics.a.h;
import com.umeng.analytics.a.l;
import com.umeng.analytics.a.m;
import com.umeng.analytics.a.o;
import com.umeng.common.Log;
import com.umeng.common.util.g;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Stack;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/* compiled from: InternalAgent */
class b extends d {
    public ExecutorService a = Executors.newSingleThreadExecutor();
    String b = StatConstants.MTA_COOPERATION_TAG;
    String c = StatConstants.MTA_COOPERATION_TAG;
    private String m;
    private String n;
    private final int o = 0;
    private final int p = 1;
    private final String q = "start_millis";
    private final String r = "end_millis";
    private final String s = "last_fetch_location_time";
    private final long t = 10000;
    private final int u = 128;
    private final int v = g.b;
    private final Stack<f> w = new Stack<>();
    private final ArrayList<f> x = new ArrayList<>();

    /* compiled from: InternalAgent */
    private final class a implements Runnable {
        private final Object b = new Object();
        private Context c;
        private int d;

        a(Context context, int i) {
            this.c = context.getApplicationContext();
            this.d = i;
        }

        public void run() {
            try {
                synchronized (this.b) {
                    switch (this.d) {
                        case 0:
                            b.this.j(this.c);
                            break;
                        case 1:
                            b.this.i(this.c);
                            break;
                    }
                }
            } catch (Exception e) {
                Log.b(g.q, "Exception occurred in invokehander.", e);
            }
        }
    }

    b() {
    }

    /* access modifiers changed from: 0000 */
    public void a(String str) {
        try {
            this.w.push(new f(str, System.currentTimeMillis()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* access modifiers changed from: 0000 */
    public void b(String str) {
        try {
            if (this.w.isEmpty() || !((f) this.w.peek()).a.equals(str)) {
                Log.e(g.q, "onPageEnd called without 'PageName' from corresponding onPageStart");
                return;
            }
            f fVar = (f) this.w.pop();
            fVar.b = System.currentTimeMillis() - fVar.b;
            this.x.add(fVar);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* access modifiers changed from: 0000 */
    public void a(String str, String str2) {
        this.e.E = str;
        this.e.F = str2;
    }

    /* access modifiers changed from: 0000 */
    public void a(Context context) {
        if (context == null) {
            try {
                Log.b(g.q, "unexpected null context in onPause");
            } catch (Exception e) {
                Log.b(g.q, "Exception occurred in Mobclick.onRause(). ", e);
            }
        } else if (!context.getClass().getName().equals(this.m)) {
            Log.b(g.q, "onPause() called without context from corresponding onResume()");
        } else {
            this.a.execute(new a(context, 0));
        }
    }

    /* access modifiers changed from: 0000 */
    public void a(Context context, String str) {
        if (!TextUtils.isEmpty(str)) {
            if (context == null) {
                Log.b(g.q, "unexpected null context in reportError");
                return;
            }
            this.d.a(new d(str));
            e(context);
        }
    }

    /* access modifiers changed from: 0000 */
    public void a(Context context, Throwable th) {
        if (context != null && th != null) {
            this.d.a(new d(th));
            e(context);
        }
    }

    private void h(Context context) {
        if (context == null) {
            Log.b(g.q, "unexpected null context in onResume");
        } else {
            this.m = context.getClass().getName();
        }
    }

    /* access modifiers changed from: 0000 */
    public void b(Context context) {
        try {
            h(context);
            this.a.execute(new a(context, 1));
        } catch (Exception e) {
            Log.b(g.q, "Exception occurred in Mobclick.onResume(). ", e);
        }
    }

    /* access modifiers changed from: 0000 */
    public void c(Context context) {
        try {
            a(context, 2);
        } catch (Exception e) {
            Log.b(g.q, "Exception occurred in Mobclick.flush(). ", e);
        }
    }

    /* access modifiers changed from: 0000 */
    public void a(Context context, String str, String str2, long j, int i) {
        if (context != null) {
            try {
                if (a(str, 128) && i > 0) {
                    if (this.n == null) {
                        Log.e(g.q, "can't call onEvent before session is initialized");
                        return;
                    }
                    if (str2 != null) {
                        if (!a(str2, (int) g.b)) {
                            Log.b(g.q, "invalid label in onEvent");
                            return;
                        }
                    }
                    this.d.a(this.n, str, str2, j, i);
                    e(context);
                    return;
                }
            } catch (Exception e) {
                Log.b(g.q, "Exception occurred in Mobclick.onEvent(). ", e);
                return;
            }
        }
        Log.b(g.q, "invalid params in onEvent");
    }

    /* access modifiers changed from: 0000 */
    public void a(Context context, String str, HashMap<String, String> hashMap, long j) {
        if (context != null) {
            try {
                if (!TextUtils.isEmpty(str)) {
                    if (!a((Map<String, String>) hashMap)) {
                        return;
                    }
                    if (this.n == null) {
                        Log.e(g.q, "can't call onEvent before session is initialized");
                        return;
                    }
                    this.d.a(this.n, str, hashMap, j);
                    e(context);
                    return;
                }
            } catch (Exception e) {
                Log.b(g.q, "Exception occurred in Mobclick.onEvent(). ", e);
                return;
            }
        }
        Log.b(g.q, "invalid params in onKVEventEnd");
    }

    /* access modifiers changed from: private */
    public synchronized void i(Context context) {
        SharedPreferences e = j.e(context);
        if (e != null) {
            if (a(e)) {
                this.n = b(context, e);
                Log.a(g.q, "Start new session: " + this.n);
            } else {
                this.n = c(context, e);
                Log.a(g.q, "Extend current session: " + this.n);
            }
        }
    }

    /* access modifiers changed from: private */
    public synchronized void j(Context context) {
        Editor a2;
        SharedPreferences e = j.e(context);
        if (e != null) {
            long j = e.getLong("start_millis", -1);
            if (j == -1) {
                Log.b(g.q, "onEndSession called before onStartSession");
            } else {
                long currentTimeMillis = System.currentTimeMillis();
                if (g.j) {
                    a2 = m.a(e, this.m, j, currentTimeMillis);
                } else {
                    a2 = m.a(e, this.x, j, currentTimeMillis);
                    this.x.clear();
                }
                a2.putLong("start_millis", -1);
                a2.putLong("end_millis", currentTimeMillis);
                a2.commit();
            }
            a(context, e);
            a(context, 5);
        }
    }

    private void a(Context context, SharedPreferences sharedPreferences) {
        long currentTimeMillis = System.currentTimeMillis();
        if (g.i && currentTimeMillis - sharedPreferences.getLong("last_fetch_location_time", 0) >= 10000) {
            Editor a2 = o.a(context, sharedPreferences);
            if (a2 != null) {
                a2.putLong("last_fetch_location_time", currentTimeMillis);
                a2.commit();
            }
        }
    }

    private boolean a(SharedPreferences sharedPreferences) {
        if (System.currentTimeMillis() - sharedPreferences.getLong("end_millis", -1) > g.d) {
            return true;
        }
        return false;
    }

    private String b(Context context, SharedPreferences sharedPreferences) {
        long currentTimeMillis = System.currentTimeMillis();
        String a2 = a(context, currentTimeMillis);
        h hVar = new h(context, a2);
        m a3 = m.a(context);
        this.d.a(hVar);
        this.d.a(a3);
        Editor edit = sharedPreferences.edit();
        edit.putString(l.f, a2);
        edit.putLong("start_millis", currentTimeMillis);
        edit.putLong("end_millis", -1);
        edit.commit();
        a(context, 4);
        return a2;
    }

    private String a(Context context, long j) {
        String str = this.l == null ? com.umeng.common.b.q(context) : this.l;
        StringBuilder sb = new StringBuilder();
        sb.append(j).append(str).append(com.umeng.common.util.h.b(com.umeng.common.b.g(context)));
        return com.umeng.common.util.h.a(sb.toString());
    }

    private String c(Context context, SharedPreferences sharedPreferences) {
        Long valueOf = Long.valueOf(System.currentTimeMillis());
        Editor edit = sharedPreferences.edit();
        edit.putLong("start_millis", valueOf.longValue());
        edit.putLong("end_millis", -1);
        edit.commit();
        return sharedPreferences.getString(l.f, null);
    }

    private void d(Context context, String str) {
        try {
            if (g.m) {
                this.d.a(str);
                return;
            }
            k a2 = k.a(context, str);
            a2.a(Long.valueOf(System.currentTimeMillis()));
            a2.a(context);
        } catch (Exception e) {
            Log.a(g.q, "exception in save event begin info");
        }
    }

    private int e(Context context, String str) {
        long longValue;
        try {
            if (g.m) {
                longValue = this.d.b(str);
            } else {
                longValue = k.a(context, str).a().longValue();
            }
            if (longValue > 0) {
                return (int) (System.currentTimeMillis() - longValue);
            }
            return -1;
        } catch (Exception e) {
            Log.a(g.q, "exception in get event duration", e);
            return -1;
        }
    }

    /* access modifiers changed from: 0000 */
    public void b(Context context, String str) {
        if (context == null || !a(str, 128)) {
            Log.b(g.q, "invalid params in onEventBegin");
        } else {
            d(context, "_t" + str);
        }
    }

    /* access modifiers changed from: 0000 */
    public void c(Context context, String str) {
        if (context == null || TextUtils.isEmpty(str)) {
            Log.a(g.q, "input Context is null or event_id is empty");
            return;
        }
        int e = e(context, "_t" + str);
        if (e < 0) {
            Log.b(g.q, "event duration less than 0 in onEventEnd");
            return;
        }
        a(context, str, null, (long) e, 1);
    }

    /* access modifiers changed from: 0000 */
    public void a(Context context, String str, String str2) {
        if (context == null || !a(str, 128) || !a(str2, (int) g.b)) {
            Log.b(g.q, "invalid params in onEventBegin");
        } else {
            d(context, "_tl" + str + str2);
        }
    }

    /* access modifiers changed from: 0000 */
    public void b(Context context, String str, String str2) {
        if (context == null || TextUtils.isEmpty(str2)) {
            Log.b(g.q, "invalid params in onEventEnd");
            return;
        }
        int e = e(context, "_tl" + str + str2);
        if (e < 0) {
            Log.b(g.q, "event duration less than 0 in onEvnetEnd");
            return;
        }
        a(context, str, str2, (long) e, 1);
    }

    /* access modifiers changed from: 0000 */
    public void a(Context context, String str, HashMap<String, String> hashMap, String str2) {
        if (context == null || TextUtils.isEmpty(str) || TextUtils.isEmpty(str2)) {
            Log.b(g.q, "invalid params in onKVEventBegin");
        } else if (a((Map<String, String>) hashMap)) {
            try {
                String sb = new StringBuilder(String.valueOf(str)).append(str2).toString();
                this.d.a(sb, hashMap);
                this.d.a(sb);
            } catch (Exception e) {
                Log.e(g.q, "exception in save k-v event begin inof", e);
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public void c(Context context, String str, String str2) {
        if (context == null || TextUtils.isEmpty(str) || TextUtils.isEmpty(str2)) {
            Log.b(g.q, "invalid params in onKVEventEnd");
            return;
        }
        String sb = new StringBuilder(String.valueOf(str)).append(str2).toString();
        int e = e(context, sb);
        if (e < 0) {
            Log.b(g.q, "event duration less than 0 in onEvnetEnd");
            return;
        }
        a(context, str, this.d.c(sb), (long) e);
    }

    /* access modifiers changed from: 0000 */
    public boolean a(String str, int i) {
        if (str == null) {
            return false;
        }
        int length = str.getBytes().length;
        if (length == 0 || length > i) {
            return false;
        }
        return true;
    }

    /* access modifiers changed from: 0000 */
    public boolean a(Map<String, String> map) {
        if (map == null || map.isEmpty()) {
            Log.b(g.q, "map is null or empty in onEvent");
            return false;
        }
        for (Entry entry : map.entrySet()) {
            if (a((String) entry.getKey(), 128)) {
                if (!a((String) entry.getValue(), (int) g.b)) {
                }
            }
            Log.b(g.q, String.format("invalid key-<%s> or value-<%s> ", new Object[]{entry.getKey(), entry.getValue()}));
            return false;
        }
        return true;
    }

    /* access modifiers changed from: 0000 */
    public void d(Context context) {
        try {
            if (!this.w.isEmpty()) {
                b(((f) this.w.peek()).a);
            }
            j(context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void b(Context context, Throwable th) {
        try {
            this.d.a(new d(th));
            if (!this.w.isEmpty()) {
                b(((f) this.w.peek()).a);
            }
            j(context);
        } catch (Exception e) {
            Log.a(g.q, "Exception in onAppCrash", e);
        }
    }
}
