package com.umeng.common;

import android.content.Context;

/* compiled from: Res */
public class c {
    private static final String a = c.class.getName();
    private static c b = null;
    private static String c = null;
    private static Class d = null;
    private static Class e = null;
    private static Class f = null;
    private static Class g = null;
    private static Class h = null;
    private static Class i = null;
    private static Class j = null;

    private c(String str) {
        try {
            e = Class.forName(new StringBuilder(String.valueOf(str)).append(".R$drawable").toString());
        } catch (ClassNotFoundException e2) {
            Log.b(a, e2.getMessage());
        }
        try {
            f = Class.forName(new StringBuilder(String.valueOf(str)).append(".R$layout").toString());
        } catch (ClassNotFoundException e3) {
            Log.b(a, e3.getMessage());
        }
        try {
            d = Class.forName(new StringBuilder(String.valueOf(str)).append(".R$id").toString());
        } catch (ClassNotFoundException e4) {
            Log.b(a, e4.getMessage());
        }
        try {
            g = Class.forName(new StringBuilder(String.valueOf(str)).append(".R$anim").toString());
        } catch (ClassNotFoundException e5) {
            Log.b(a, e5.getMessage());
        }
        try {
            h = Class.forName(new StringBuilder(String.valueOf(str)).append(".R$style").toString());
        } catch (ClassNotFoundException e6) {
            Log.b(a, e6.getMessage());
        }
        try {
            i = Class.forName(new StringBuilder(String.valueOf(str)).append(".R$string").toString());
        } catch (ClassNotFoundException e7) {
            Log.b(a, e7.getMessage());
        }
        try {
            j = Class.forName(new StringBuilder(String.valueOf(str)).append(".R$array").toString());
        } catch (ClassNotFoundException e8) {
            Log.b(a, e8.getMessage());
        }
    }

    public static synchronized c a(Context context) {
        c cVar;
        String packageName;
        synchronized (c.class) {
            if (b == null) {
                if (c != null) {
                    packageName = c;
                } else {
                    packageName = context.getPackageName();
                }
                c = packageName;
                b = new c(c);
            }
            cVar = b;
        }
        return cVar;
    }

    public static void a(String str) {
        c = str;
    }

    public int b(String str) {
        return a(g, str);
    }

    public int c(String str) {
        return a(d, str);
    }

    public int d(String str) {
        return a(e, str);
    }

    public int e(String str) {
        return a(f, str);
    }

    public int f(String str) {
        return a(h, str);
    }

    public int g(String str) {
        return a(i, str);
    }

    public int h(String str) {
        return a(j, str);
    }

    private int a(Class<?> cls, String str) {
        if (cls == null) {
            Log.b(a, "getRes(null," + str + ")");
            throw new IllegalArgumentException("ResClass is not initialized. Please make sure you have added neccessary resources. Also make sure you have " + c + ".R$* configured in obfuscation. field=" + str);
        }
        try {
            return cls.getField(str).getInt(str);
        } catch (Exception e2) {
            Log.b(a, "getRes(" + cls.getName() + ", " + str + ")");
            Log.b(a, "Error getting resource. Make sure you have copied all resources (res/) from SDK to your project. ");
            Log.b(a, e2.getMessage());
            return -1;
        }
    }
}
