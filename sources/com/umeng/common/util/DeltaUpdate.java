package com.umeng.common.util;

import android.content.Context;
import com.tencent.stat.common.StatConstants;
import java.io.File;

public class DeltaUpdate {
    private static boolean a = false;
    private static final String b = "bspatch";

    public static native int bspatch(String str, String str2, String str3);

    public static boolean a() {
        return a;
    }

    public static int a(String str, String str2, String str3) {
        return bspatch(str, str2, str3);
    }

    public static String a(Context context) {
        return context.getApplicationInfo().sourceDir;
    }

    public static String b(Context context) {
        String a2 = a(context);
        if (!new File(a2).exists()) {
            return StatConstants.MTA_COOPERATION_TAG;
        }
        return h.a(new File(a2));
    }

    static {
        try {
            System.loadLibrary(b);
            a = true;
        } catch (UnsatisfiedLinkError e) {
            a = false;
        }
    }
}
