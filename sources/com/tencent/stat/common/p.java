package com.tencent.stat.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

public class p {
    private static SharedPreferences a = null;

    public static int a(Context context, String str, int i) {
        return a(context).getInt(k.a(context, StatConstants.MTA_COOPERATION_TAG + str), i);
    }

    public static long a(Context context, String str, long j) {
        return a(context).getLong(k.a(context, StatConstants.MTA_COOPERATION_TAG + str), j);
    }

    static synchronized SharedPreferences a(Context context) {
        SharedPreferences sharedPreferences;
        synchronized (p.class) {
            if (a == null) {
                a = PreferenceManager.getDefaultSharedPreferences(context);
            }
            sharedPreferences = a;
        }
        return sharedPreferences;
    }

    public static String a(Context context, String str, String str2) {
        return a(context).getString(k.a(context, StatConstants.MTA_COOPERATION_TAG + str), str2);
    }

    public static void b(Context context, String str, int i) {
        String a2 = k.a(context, StatConstants.MTA_COOPERATION_TAG + str);
        Editor edit = a(context).edit();
        edit.putInt(a2, i);
        edit.commit();
    }

    public static void b(Context context, String str, long j) {
        String a2 = k.a(context, StatConstants.MTA_COOPERATION_TAG + str);
        Editor edit = a(context).edit();
        edit.putLong(a2, j);
        edit.commit();
    }

    public static void b(Context context, String str, String str2) {
        String a2 = k.a(context, StatConstants.MTA_COOPERATION_TAG + str);
        Editor edit = a(context).edit();
        edit.putString(a2, str2);
        edit.commit();
    }
}
