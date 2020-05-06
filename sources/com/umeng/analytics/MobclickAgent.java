package com.umeng.analytics;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import com.tencent.stat.common.StatConstants;
import com.umeng.analytics.onlineconfig.UmengOnlineConfigureListener;
import com.umeng.analytics.onlineconfig.a;
import com.umeng.analytics.onlineconfig.c;
import com.umeng.common.Log;
import com.umeng.common.b;
import java.util.HashMap;
import javax.microedition.khronos.opengles.GL10;

public class MobclickAgent {
    private static final b a = new b();
    private static final a b = new a();
    private static /* synthetic */ int[] c;

    static /* synthetic */ int[] b() {
        int[] iArr = c;
        if (iArr == null) {
            iArr = new int[Gender.values().length];
            try {
                iArr[Gender.Female.ordinal()] = 2;
            } catch (NoSuchFieldError e) {
            }
            try {
                iArr[Gender.Male.ordinal()] = 1;
            } catch (NoSuchFieldError e2) {
            }
            try {
                iArr[Gender.Unknown.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            c = iArr;
        }
        return iArr;
    }

    static {
        b.a((c) a);
    }

    public static void setAutoLocation(boolean z) {
        g.i = z;
    }

    public static void setWrapper(String str, String str2) {
        a.a(str, str2);
    }

    public static void setSessionContinueMillis(long j) {
        g.d = j;
    }

    public static void setEnableEventBuffer(boolean z) {
        g.m = z;
    }

    public static void setOnlineConfigureListener(UmengOnlineConfigureListener umengOnlineConfigureListener) {
        b.a(umengOnlineConfigureListener);
    }

    static b a() {
        return a;
    }

    public static void setOpenGLContext(GL10 gl10) {
        if (gl10 != null) {
            String[] a2 = b.a(gl10);
            if (a2.length == 2) {
                a.b = a2[0];
                a.c = a2[1];
            }
        }
    }

    public static void openActivityDurationTrack(boolean z) {
        g.j = z;
    }

    public static void onPageStart(String str) {
        if (!TextUtils.isEmpty(str)) {
            a.a(str);
        } else {
            Log.b(g.q, "pageName is null or empty");
        }
    }

    public static void onPageEnd(String str) {
        if (!TextUtils.isEmpty(str)) {
            a.b(str);
        } else {
            Log.b(g.q, "pageName is null or empty");
        }
    }

    public static void setDebugMode(boolean z) {
        Log.LOG = z;
    }

    public static void setDefaultReportPolicy(Context context, int i) {
        Log.e(g.q, "此方法不再使用，请使用在线参数配置，发送策略");
    }

    public static void onPause(Context context) {
        a.a(context);
    }

    public static void onResume(Context context) {
        if (context == null) {
            Log.b(g.q, "unexpected null context in onResume");
        } else {
            a.b(context);
        }
    }

    public static void onResume(Context context, String str, String str2) {
        if (context == null) {
            Log.b(g.q, "unexpected null context in onResume");
        } else if (str == null || str.length() == 0) {
            Log.b(g.q, "unexpected empty appkey in onResume");
        } else {
            a.l = str;
            a.k = str2;
            a.b(context);
        }
    }

    public static void onError(Context context) {
    }

    public static void onError(Context context, String str) {
        if (str == null || str.length() == 0) {
            Log.b(g.q, "unexpected empty appkey in onError");
            return;
        }
        a.l = str;
        onError(context);
    }

    public static void reportError(Context context, String str) {
        a.a(context, str);
    }

    public static void reportError(Context context, Throwable th) {
        a.a(context, th);
    }

    public static void flush(Context context) {
        a.c(context);
    }

    public static void onEvent(Context context, String str) {
        a.a(context, str, null, -1, 1);
    }

    public static void onEvent(Context context, String str, int i) {
        a.a(context, str, null, -1, i);
    }

    public static void onEvent(Context context, String str, String str2, int i) {
        if (TextUtils.isEmpty(str2)) {
            Log.a(g.q, "label is null or empty");
        } else {
            a.a(context, str, str2, -1, i);
        }
    }

    public static void onEvent(Context context, String str, String str2) {
        if (TextUtils.isEmpty(str2)) {
            Log.a(g.q, "label is null or empty");
        } else {
            a.a(context, str, str2, -1, 1);
        }
    }

    public static void onEvent(Context context, String str, HashMap<String, String> hashMap) {
        a.a(context, str, hashMap, -1);
    }

    public static void onEventDuration(Context context, String str, long j) {
        if (j <= 0) {
            Log.a(g.q, "duration is not valid in onEventDuration");
        } else {
            a.a(context, str, null, j, 1);
        }
    }

    public static void onEventDuration(Context context, String str, String str2, long j) {
        if (TextUtils.isEmpty(str2)) {
            Log.a(g.q, "label is null or empty");
        } else if (j <= 0) {
            Log.a(g.q, "duration is not valid in onEventDuration");
        } else {
            a.a(context, str, str2, j, 1);
        }
    }

    public static void onEventDuration(Context context, String str, HashMap<String, String> hashMap, long j) {
        if (j <= 0) {
            Log.a(g.q, "duration is not valid in onEventDuration");
        } else {
            a.a(context, str, hashMap, j);
        }
    }

    public static void onEventBegin(Context context, String str) {
        a.b(context, str);
    }

    public static void onEventEnd(Context context, String str) {
        a.c(context, str);
    }

    public static void onEventBegin(Context context, String str, String str2) {
        a.a(context, str, str2);
    }

    public static void onEventEnd(Context context, String str, String str2) {
        a.b(context, str, str2);
    }

    public static void onKVEventBegin(Context context, String str, HashMap<String, String> hashMap, String str2) {
        a.a(context, str, hashMap, str2);
    }

    public static void onKVEventEnd(Context context, String str, String str2) {
        a.c(context, str, str2);
    }

    public static String getConfigParams(Context context, String str) {
        return j.b(context).getString(str, StatConstants.MTA_COOPERATION_TAG);
    }

    public static void updateOnlineConfig(Context context, String str, String str2) {
        if (str == null || str.length() == 0) {
            Log.b(g.q, "unexpected empty appkey in onResume");
        } else {
            b.a(context, str, str2);
        }
    }

    public static void updateOnlineConfig(Context context) {
        b.a(context);
    }

    public void setGender(Context context, Gender gender) {
        int i = 0;
        SharedPreferences a2 = j.a(context);
        switch (b()[gender.ordinal()]) {
            case 1:
                i = 1;
                break;
            case 2:
                i = 2;
                break;
        }
        a2.edit().putInt("gender", i).commit();
    }

    public void setAge(Context context, int i) {
        SharedPreferences a2 = j.a(context);
        if (i < 0 || i > 200) {
            Log.a(g.q, "not a valid age!");
        } else {
            a2.edit().putInt("age", i).commit();
        }
    }

    public void setUserID(Context context, String str, String str2) {
        SharedPreferences a2 = j.a(context);
        if (TextUtils.isEmpty(str)) {
            Log.a(g.q, "userID is null or empty");
            return;
        }
        a2.edit().putString("user_id", str).commit();
        if (TextUtils.isEmpty(str2)) {
            Log.a(g.q, "id source is null or empty");
        } else {
            a2.edit().putString("id_source", str2).commit();
        }
    }

    public static void onKillProcess(Context context) {
        a.d(context);
    }
}
