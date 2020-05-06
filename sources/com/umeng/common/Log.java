package com.umeng.common;

public class Log {
    public static boolean LOG = false;

    public static void a(String str, String str2) {
        if (LOG) {
            android.util.Log.i(str, str2);
        }
    }

    public static void a(String str, String str2, Exception exc) {
        if (LOG) {
            android.util.Log.i(str, exc.toString() + ":  [" + str2 + "]");
        }
    }

    public static void b(String str, String str2) {
        if (LOG) {
            android.util.Log.e(str, str2);
        }
    }

    public static void b(String str, String str2, Exception exc) {
        if (LOG) {
            android.util.Log.e(str, exc.toString() + ":  [" + str2 + "]");
            for (StackTraceElement stackTraceElement : exc.getStackTrace()) {
                android.util.Log.e(str, "        at\t " + stackTraceElement.toString());
            }
        }
    }

    public static void c(String str, String str2) {
        if (LOG) {
            android.util.Log.d(str, str2);
        }
    }

    public static void c(String str, String str2, Exception exc) {
        if (LOG) {
            android.util.Log.d(str, exc.toString() + ":  [" + str2 + "]");
        }
    }

    public static void d(String str, String str2) {
        if (LOG) {
            android.util.Log.v(str, str2);
        }
    }

    public static void d(String str, String str2, Exception exc) {
        if (LOG) {
            android.util.Log.v(str, exc.toString() + ":  [" + str2 + "]");
        }
    }

    public static void e(String str, String str2) {
        if (LOG) {
            android.util.Log.w(str, str2);
        }
    }

    public static void e(String str, String str2, Exception exc) {
        if (LOG) {
            android.util.Log.w(str, exc.toString() + ":  [" + str2 + "]");
            for (StackTraceElement stackTraceElement : exc.getStackTrace()) {
                android.util.Log.w(str, "        at\t " + stackTraceElement.toString());
            }
        }
    }
}
