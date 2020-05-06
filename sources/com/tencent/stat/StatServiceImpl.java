package com.tencent.stat;

import android.content.Context;
import com.tencent.stat.a.a;
import com.tencent.stat.a.c;
import com.tencent.stat.a.i;
import com.tencent.stat.a.l;
import com.tencent.stat.common.StatConstants;
import com.tencent.stat.common.StatLogger;
import com.tencent.stat.common.b;
import com.tencent.stat.common.e;
import com.tencent.stat.common.k;
import com.tencent.stat.common.p;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import org.json.JSONException;
import org.json.JSONObject;

public class StatServiceImpl {
    static volatile int a = 0;
    static volatile long b = 0;
    static volatile long c = 0;
    private static e d;
    /* access modifiers changed from: private */
    public static volatile Map<c, Long> e = new ConcurrentHashMap();
    private static volatile Map<String, Properties> f = new ConcurrentHashMap();
    /* access modifiers changed from: private */
    public static volatile Map<Integer, Integer> g = new ConcurrentHashMap(10);
    /* access modifiers changed from: private */
    public static volatile long h = 0;
    private static volatile long i = 0;
    private static volatile long j = 0;
    private static String k = StatConstants.MTA_COOPERATION_TAG;
    private static volatile int l = 0;
    /* access modifiers changed from: private */
    public static volatile String m = StatConstants.MTA_COOPERATION_TAG;
    /* access modifiers changed from: private */
    public static volatile String n = StatConstants.MTA_COOPERATION_TAG;
    /* access modifiers changed from: private */
    public static Map<String, Long> o = new ConcurrentHashMap();
    private static Map<String, Long> p = new ConcurrentHashMap();
    /* access modifiers changed from: private */
    public static StatLogger q = k.b();
    /* access modifiers changed from: private */
    public static UncaughtExceptionHandler r = null;
    private static volatile boolean s = true;
    /* access modifiers changed from: private */
    public static Context t = null;

    static int a(Context context, boolean z, StatSpecifyReportedInfo statSpecifyReportedInfo) {
        boolean z2 = true;
        long currentTimeMillis = System.currentTimeMillis();
        boolean z3 = z && currentTimeMillis - i >= ((long) StatConfig.getSessionTimoutMillis());
        i = currentTimeMillis;
        if (j == 0) {
            j = k.c();
        }
        if (currentTimeMillis >= j) {
            j = k.c();
            if (au.a(context).b(context).d() != 1) {
                au.a(context).b(context).a(1);
            }
            StatConfig.b(0);
            a = 0;
            k = k.a(0);
            z3 = true;
        }
        String str = k;
        if (k.a(statSpecifyReportedInfo)) {
            str = statSpecifyReportedInfo.getAppKey() + k;
        }
        if (p.containsKey(str)) {
            z2 = z3;
        }
        if (z2) {
            if (k.a(statSpecifyReportedInfo)) {
                a(context, statSpecifyReportedInfo);
            } else if (StatConfig.c() < StatConfig.getMaxDaySessionNumbers()) {
                k.x(context);
                a(context, (StatSpecifyReportedInfo) null);
            } else {
                q.e((Object) "Exceed StatConfig.getMaxDaySessionNumbers().");
            }
            p.put(str, Long.valueOf(1));
        }
        if (s) {
            testSpeed(context);
            d(context);
            s = false;
        }
        return l;
    }

    static synchronized void a(Context context) {
        synchronized (StatServiceImpl.class) {
            if (context != null) {
                if (d == null && b(context)) {
                    Context applicationContext = context.getApplicationContext();
                    t = applicationContext;
                    d = new e();
                    k = k.a(0);
                    h = System.currentTimeMillis() + StatConfig.i;
                    d.a(new j(applicationContext));
                }
            }
        }
    }

    static void a(Context context, StatSpecifyReportedInfo statSpecifyReportedInfo) {
        if (c(context) != null) {
            if (StatConfig.isDebugEnable()) {
                q.d("start new session.");
            }
            if (statSpecifyReportedInfo == null || l == 0) {
                l = k.a();
            }
            StatConfig.a(0);
            StatConfig.b();
            new aq(new l(context, l, b(), statSpecifyReportedInfo)).a();
        }
    }

    static void a(Context context, Throwable th) {
        if (StatConfig.isEnableStatService()) {
            Context context2 = getContext(context);
            if (context2 == null) {
                q.error((Object) "The Context of StatService.reportSdkSelfException() can not be null!");
            } else if (c(context2) != null) {
                d.a(new o(context2, th));
            }
        }
    }

    static boolean a() {
        if (a < 2) {
            return false;
        }
        b = System.currentTimeMillis();
        return true;
    }

    static boolean a(String str) {
        return str == null || str.length() == 0;
    }

    static JSONObject b() {
        JSONObject jSONObject = new JSONObject();
        try {
            JSONObject jSONObject2 = new JSONObject();
            if (StatConfig.b.d != 0) {
                jSONObject2.put("v", StatConfig.b.d);
            }
            jSONObject.put(Integer.toString(StatConfig.b.a), jSONObject2);
            JSONObject jSONObject3 = new JSONObject();
            if (StatConfig.a.d != 0) {
                jSONObject3.put("v", StatConfig.a.d);
            }
            jSONObject.put(Integer.toString(StatConfig.a.a), jSONObject3);
        } catch (JSONException e2) {
            q.e((Throwable) e2);
        }
        return jSONObject;
    }

    /* access modifiers changed from: private */
    public static void b(Context context, StatAccount statAccount, StatSpecifyReportedInfo statSpecifyReportedInfo) {
        try {
            new aq(new a(context, a(context, false, statSpecifyReportedInfo), statAccount, statSpecifyReportedInfo)).a();
        } catch (Throwable th) {
            q.e(th);
            a(context, th);
        }
    }

    static boolean b(Context context) {
        boolean z = false;
        long a2 = p.a(context, StatConfig.c, 0);
        long b2 = k.b(StatConstants.VERSION);
        boolean z2 = true;
        if (b2 <= a2) {
            q.error((Object) "MTA is disable for current version:" + b2 + ",wakeup version:" + a2);
            z2 = false;
        }
        long a3 = p.a(context, StatConfig.d, 0);
        if (a3 > System.currentTimeMillis()) {
            q.error((Object) "MTA is disable for current time:" + System.currentTimeMillis() + ",wakeup time:" + a3);
        } else {
            z = z2;
        }
        StatConfig.setEnableStatService(z);
        return z;
    }

    static e c(Context context) {
        if (d == null) {
            synchronized (StatServiceImpl.class) {
                if (d == null) {
                    try {
                        a(context);
                    } catch (Throwable th) {
                        q.error(th);
                        StatConfig.setEnableStatService(false);
                    }
                }
            }
        }
        return d;
    }

    static void c() {
        a = 0;
        b = 0;
    }

    public static void commitEvents(Context context, int i2) {
        if (StatConfig.isEnableStatService()) {
            if (StatConfig.isDebugEnable()) {
                q.i("commitEvents, maxNumber=" + i2);
            }
            Context context2 = getContext(context);
            if (context2 == null) {
                q.error((Object) "The Context of StatService.commitEvents() can not be null!");
            } else if (i2 < -1 || i2 == 0) {
                q.error((Object) "The maxNumber of StatService.commitEvents() should be -1 or bigger than 0.");
            } else if (a.a(t).f() && c(context2) != null) {
                d.a(new ac(context2, i2));
            }
        }
    }

    static void d() {
        a++;
        b = System.currentTimeMillis();
        flushDataToDB(t);
    }

    static void d(Context context) {
        if (StatConfig.isEnableStatService() && c(context) != null) {
            d.a(new q(context));
        }
    }

    static void e(Context context) {
        if (StatConfig.isEnableStatService()) {
            Context context2 = getContext(context);
            if (context2 == null) {
                q.error((Object) "The Context of StatService.sendNetworkDetector() can not be null!");
                return;
            }
            try {
                g.b(context2).a((com.tencent.stat.a.e) new i(context2), (StatDispatchCallback) new s());
            } catch (Throwable th) {
                q.e(th);
            }
        }
    }

    static void f(Context context) {
        c = System.currentTimeMillis() + ((long) (60000 * StatConfig.getSendPeriodMinutes()));
        p.b(context, "last_period_ts", c);
        commitEvents(context, -1);
    }

    public static void flushDataToDB(Context context) {
        if (StatConfig.isEnableStatService() && StatConfig.m > 0) {
            Context context2 = getContext(context);
            if (context2 == null) {
                q.error((Object) "The Context of StatService.testSpeed() can not be null!");
            } else {
                au.a(context2).c();
            }
        }
    }

    public static Properties getCommonKeyValueForKVEvent(String str) {
        return (Properties) f.get(str);
    }

    public static Context getContext(Context context) {
        return context != null ? context : t;
    }

    public static void onLowMemory(Context context) {
        if (StatConfig.isEnableStatService() && c(getContext(context)) != null) {
            d.a(new m(context));
        }
    }

    public static void onPause(Context context, StatSpecifyReportedInfo statSpecifyReportedInfo) {
        if (StatConfig.isEnableStatService() && c(context) != null) {
            d.a(new k(context, statSpecifyReportedInfo));
        }
    }

    public static void onResume(Context context, StatSpecifyReportedInfo statSpecifyReportedInfo) {
        if (StatConfig.isEnableStatService() && c(context) != null) {
            d.a(new ai(context, statSpecifyReportedInfo));
        }
    }

    public static void onStop(Context context, StatSpecifyReportedInfo statSpecifyReportedInfo) {
        if (StatConfig.isEnableStatService()) {
            Context context2 = getContext(context);
            if (c(context2) != null) {
                d.a(new l(context2));
            }
        }
    }

    public static void reportAccount(Context context, StatAccount statAccount, StatSpecifyReportedInfo statSpecifyReportedInfo) {
        if (StatConfig.isEnableStatService()) {
            Context context2 = getContext(context);
            if (context2 == null) {
                q.e((Object) "context is null in reportAccount.");
            } else if (c(context2) != null) {
                d.a(new ak(statAccount, context2, statSpecifyReportedInfo));
            }
        }
    }

    public static void reportAppMonitorStat(Context context, StatAppMonitor statAppMonitor, StatSpecifyReportedInfo statSpecifyReportedInfo) {
        if (StatConfig.isEnableStatService()) {
            Context context2 = getContext(context);
            if (context2 == null) {
                q.error((Object) "The Context of StatService.reportAppMonitorStat() can not be null!");
            } else if (statAppMonitor == null) {
                q.error((Object) "The StatAppMonitor of StatService.reportAppMonitorStat() can not be null!");
            } else if (statAppMonitor.getInterfaceName() == null) {
                q.error((Object) "The interfaceName of StatAppMonitor on StatService.reportAppMonitorStat() can not be null!");
            } else {
                StatAppMonitor clone = statAppMonitor.clone();
                if (c(context2) != null) {
                    d.a(new z(context2, statSpecifyReportedInfo, clone));
                }
            }
        }
    }

    public static void reportError(Context context, String str, StatSpecifyReportedInfo statSpecifyReportedInfo) {
        if (StatConfig.isEnableStatService()) {
            Context context2 = getContext(context);
            if (context2 == null) {
                q.error((Object) "The Context of StatService.reportError() can not be null!");
            } else if (c(context2) != null) {
                d.a(new n(str, context2, statSpecifyReportedInfo));
            }
        }
    }

    public static void reportException(Context context, Throwable th, StatSpecifyReportedInfo statSpecifyReportedInfo) {
        if (StatConfig.isEnableStatService()) {
            Context context2 = getContext(context);
            if (context2 == null) {
                q.error((Object) "The Context of StatService.reportException() can not be null!");
            } else if (c(context2) != null) {
                d.a(new p(th, context2, statSpecifyReportedInfo));
            }
        }
    }

    public static void reportGameUser(Context context, StatGameUser statGameUser, StatSpecifyReportedInfo statSpecifyReportedInfo) {
        if (StatConfig.isEnableStatService()) {
            Context context2 = getContext(context);
            if (context2 == null) {
                q.error((Object) "The Context of StatService.reportGameUser() can not be null!");
            } else if (c(context2) != null) {
                d.a(new al(statGameUser, context2, statSpecifyReportedInfo));
            }
        }
    }

    public static void reportQQ(Context context, String str, StatSpecifyReportedInfo statSpecifyReportedInfo) {
        if (StatConfig.isEnableStatService()) {
            Context context2 = getContext(context);
            if (context2 == null) {
                q.error((Object) "context is null in reportQQ()");
            } else if (c(context2) != null) {
                d.a(new aj(str, context2, statSpecifyReportedInfo));
            }
        }
    }

    public static void setCommonKeyValueForKVEvent(String str, Properties properties) {
        if (!k.c(str)) {
            q.e((Object) "event_id or commonProp for setCommonKeyValueForKVEvent is invalid.");
        } else if (properties == null || properties.size() <= 0) {
            f.remove(str);
        } else {
            f.put(str, (Properties) properties.clone());
        }
    }

    public static void setContext(Context context) {
        if (context != null) {
            t = context.getApplicationContext();
        }
    }

    public static void setEnvAttributes(Context context, Map<String, String> map) {
        if (map == null || map.size() > 512) {
            q.error((Object) "The map in setEnvAttributes can't be null or its size can't exceed 512.");
            return;
        }
        try {
            b.a(context, map);
        } catch (JSONException e2) {
            q.e((Throwable) e2);
        }
    }

    public static void startNewSession(Context context, StatSpecifyReportedInfo statSpecifyReportedInfo) {
        if (StatConfig.isEnableStatService()) {
            Context context2 = getContext(context);
            if (context2 == null) {
                q.error((Object) "The Context of StatService.startNewSession() can not be null!");
            } else if (c(context2) != null) {
                d.a(new ah(context2, statSpecifyReportedInfo));
            }
        }
    }

    public static boolean startStatService(Context context, String str, String str2, StatSpecifyReportedInfo statSpecifyReportedInfo) throws MtaSDkException {
        try {
            if (!StatConfig.isEnableStatService()) {
                q.error((Object) "MTA StatService is disable.");
                return false;
            }
            String str3 = StatConstants.VERSION;
            if (StatConfig.isDebugEnable()) {
                q.d("MTA SDK version, current: " + str3 + " ,required: " + str2);
            }
            String str4 = StatConstants.MTA_COOPERATION_TAG;
            if (context == null || str2 == null) {
                q.error((Object) "Context or mtaSdkVersion in StatService.startStatService() is null, please check it!");
                StatConfig.setEnableStatService(false);
                return false;
            } else if (k.b(str3) < k.b(str2)) {
                q.error((Object) ("MTA SDK version conflicted, current: " + str3 + ",required: " + str2) + ". please delete the current SDK and download the latest one. official website: http://mta.qq.com/ or http://mta.oa.com/");
                StatConfig.setEnableStatService(false);
                return false;
            } else {
                String installChannel = StatConfig.getInstallChannel(context);
                if (installChannel == null || installChannel.length() == 0) {
                    StatConfig.setInstallChannel("-");
                }
                if (str != null) {
                    StatConfig.setAppKey(context, str);
                }
                if (c(context) != null) {
                    d.a(new am(context, statSpecifyReportedInfo));
                }
                return true;
            }
        } catch (Throwable th) {
            q.e(th);
            return false;
        }
    }

    public static void stopSession() {
        i = 0;
    }

    public static void testSpeed(Context context) {
        if (StatConfig.isEnableStatService()) {
            Context context2 = getContext(context);
            if (context2 == null) {
                q.error((Object) "The Context of StatService.testSpeed() can not be null!");
            } else if (c(context2) != null) {
                d.a(new ad(context2));
            }
        }
    }

    public static void testSpeed(Context context, Map<String, Integer> map, StatSpecifyReportedInfo statSpecifyReportedInfo) {
        if (StatConfig.isEnableStatService()) {
            Context context2 = getContext(context);
            if (context2 == null) {
                q.error((Object) "The Context of StatService.testSpeed() can not be null!");
            } else if (map == null || map.size() == 0) {
                q.error((Object) "The domainMap of StatService.testSpeed() can not be null or empty!");
            } else {
                HashMap hashMap = new HashMap(map);
                if (c(context2) != null) {
                    d.a(new ae(context2, hashMap, statSpecifyReportedInfo));
                }
            }
        }
    }

    public static void trackBeginPage(Context context, String str, StatSpecifyReportedInfo statSpecifyReportedInfo) {
        if (StatConfig.isEnableStatService()) {
            Context context2 = getContext(context);
            if (context2 == null || str == null || str.length() == 0) {
                q.error((Object) "The Context or pageName of StatService.trackBeginPage() can not be null or empty!");
                return;
            }
            String str2 = new String(str);
            if (c(context2) != null) {
                d.a(new u(str2, context2, statSpecifyReportedInfo));
            }
        }
    }

    public static void trackCustomBeginEvent(Context context, String str, StatSpecifyReportedInfo statSpecifyReportedInfo, String... strArr) {
        if (StatConfig.isEnableStatService()) {
            Context context2 = getContext(context);
            if (context2 == null) {
                q.error((Object) "The Context of StatService.trackCustomBeginEvent() can not be null!");
                return;
            }
            c cVar = new c(str, strArr, null);
            if (c(context2) != null) {
                d.a(new v(str, cVar, context2));
            }
        }
    }

    public static void trackCustomBeginKVEvent(Context context, String str, Properties properties, StatSpecifyReportedInfo statSpecifyReportedInfo) {
        if (StatConfig.isEnableStatService()) {
            Context context2 = getContext(context);
            if (context2 == null) {
                q.error((Object) "The Context of StatService.trackCustomBeginEvent() can not be null!");
                return;
            }
            c cVar = new c(str, null, properties);
            if (c(context2) != null) {
                d.a(new x(str, cVar, context2));
            }
        }
    }

    public static void trackCustomEndEvent(Context context, String str, StatSpecifyReportedInfo statSpecifyReportedInfo, String... strArr) {
        if (StatConfig.isEnableStatService()) {
            Context context2 = getContext(context);
            if (context2 == null) {
                q.error((Object) "The Context of StatService.trackCustomEndEvent() can not be null!");
                return;
            }
            c cVar = new c(str, strArr, null);
            if (c(context2) != null) {
                d.a(new w(str, cVar, context2, statSpecifyReportedInfo));
            }
        }
    }

    public static void trackCustomEndKVEvent(Context context, String str, Properties properties, StatSpecifyReportedInfo statSpecifyReportedInfo) {
        if (StatConfig.isEnableStatService()) {
            Context context2 = getContext(context);
            if (context2 == null) {
                q.error((Object) "The Context of StatService.trackCustomEndEvent() can not be null!");
                return;
            }
            c cVar = new c(str, null, properties);
            if (c(context2) != null) {
                d.a(new y(str, cVar, context2, statSpecifyReportedInfo));
            }
        }
    }

    public static void trackCustomEvent(Context context, String str, StatSpecifyReportedInfo statSpecifyReportedInfo, String... strArr) {
        if (StatConfig.isEnableStatService()) {
            Context context2 = getContext(context);
            if (context2 == null) {
                q.error((Object) "The Context of StatService.trackCustomEvent() can not be null!");
            } else if (a(str)) {
                q.error((Object) "The event_id of StatService.trackCustomEvent() can not be null or empty.");
            } else {
                c cVar = new c(str, strArr, null);
                if (c(context2) != null) {
                    d.a(new r(context2, statSpecifyReportedInfo, cVar));
                }
            }
        }
    }

    public static void trackCustomKVEvent(Context context, String str, Properties properties, StatSpecifyReportedInfo statSpecifyReportedInfo) {
        if (StatConfig.isEnableStatService()) {
            Context context2 = getContext(context);
            if (context2 == null) {
                q.error((Object) "The Context of StatService.trackCustomEvent() can not be null!");
            } else if (a(str)) {
                q.error((Object) "The event_id of StatService.trackCustomEvent() can not be null or empty.");
            } else {
                c cVar = new c(str, null, properties);
                if (c(context2) != null) {
                    d.a(new t(context2, statSpecifyReportedInfo, cVar));
                }
            }
        }
    }

    public static void trackCustomKVTimeIntervalEvent(Context context, String str, Properties properties, int i2, StatSpecifyReportedInfo statSpecifyReportedInfo) {
        if (StatConfig.isEnableStatService()) {
            Context context2 = getContext(context);
            if (context2 == null) {
                q.error((Object) "The Context of StatService.trackCustomEndEvent() can not be null!");
            } else if (a(str)) {
                q.error((Object) "The event_id of StatService.trackCustomEndEvent() can not be null or empty.");
            } else {
                c cVar = new c(str, null, properties);
                if (c(context2) != null) {
                    d.a(new ab(context2, statSpecifyReportedInfo, cVar, i2));
                }
            }
        }
    }

    public static void trackCustomTimeIntervalEvent(Context context, int i2, String str, String... strArr) {
        if (StatConfig.isEnableStatService()) {
            if (i2 <= 0) {
                q.error((Object) "The intervalSecond of StatService.trackCustomTimeIntervalEvent() can must bigger than 0!");
                return;
            }
            Context context2 = getContext(context);
            if (context2 == null) {
                q.error((Object) "The Context of StatService.trackCustomTimeIntervalEvent() can not be null!");
            } else if (c(context2) != null) {
                d.a(new aa());
            }
        }
    }

    public static void trackEndPage(Context context, String str, StatSpecifyReportedInfo statSpecifyReportedInfo) {
        if (StatConfig.isEnableStatService()) {
            Context context2 = getContext(context);
            if (context2 == null || str == null || str.length() == 0) {
                q.error((Object) "The Context or pageName of StatService.trackEndPage() can not be null or empty!");
                return;
            }
            String str2 = new String(str);
            if (c(context2) != null) {
                d.a(new af(context2, str2, statSpecifyReportedInfo));
            }
        }
    }
}
