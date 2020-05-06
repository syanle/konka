package com.tencent.stat;

import android.content.Context;
import com.tencent.stat.common.StatConstants;
import com.tencent.stat.common.StatLogger;
import com.tencent.stat.common.k;
import com.tencent.stat.common.p;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedHashSet;

public class StatNativeCrashReport {
    public static final String PRE_TAG_TOMBSTONE_FNAME = "tombstone_";
    static StatNativeCrashReport a = new StatNativeCrashReport();
    private static StatLogger b = k.b();
    private static boolean d;
    private static boolean e = false;
    private static String f = null;
    private static boolean g;
    private volatile boolean c = false;

    static {
        d = false;
        g = false;
        try {
            System.loadLibrary("MtaNativeCrash");
            g = true;
        } catch (Throwable th) {
            d = false;
            b.w("can't find libMtaNativeCrash.so, NativeCrash report disable.");
        }
    }

    static String a(File file) {
        StringBuilder sb = new StringBuilder();
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            while (true) {
                String readLine = bufferedReader.readLine();
                if (readLine == null) {
                    break;
                }
                sb.append(readLine);
                sb.append(10);
            }
            bufferedReader.close();
        } catch (IOException e2) {
            b.e((Throwable) e2);
        }
        return sb.toString();
    }

    static LinkedHashSet<File> a(Context context) {
        LinkedHashSet<File> linkedHashSet = new LinkedHashSet<>();
        String tombstonesDir = getTombstonesDir(context);
        if (tombstonesDir != null) {
            File file = new File(tombstonesDir);
            if (file != null && file.isDirectory()) {
                File[] listFiles = file.listFiles();
                if (listFiles != null) {
                    for (File file2 : listFiles) {
                        if (file2.getName().startsWith(PRE_TAG_TOMBSTONE_FNAME) && file2.isFile()) {
                            if (StatConfig.isDebugEnable()) {
                                b.d("get tombstone file:" + file2.getAbsolutePath().toString());
                            }
                            linkedHashSet.add(file2.getAbsoluteFile());
                        }
                    }
                }
            }
        }
        return linkedHashSet;
    }

    static long b(File file) {
        long j = 0;
        try {
            return Long.valueOf(file.getName().replace(PRE_TAG_TOMBSTONE_FNAME, StatConstants.MTA_COOPERATION_TAG)).longValue();
        } catch (NumberFormatException e2) {
            b.e((Throwable) e2);
            return j;
        }
    }

    public static void doNativeCrashTest() {
        if (!g) {
            b.warn("libMtaNativeCrash.so not loaded.");
        } else {
            a.makeJniCrash();
        }
    }

    public static String getTombstonesDir(Context context) {
        if (f == null) {
            f = p.a(context, "__mta_tombstone__", StatConstants.MTA_COOPERATION_TAG);
        }
        return f;
    }

    public static void initNativeCrash(Context context, String str) {
        if (!g) {
            b.warn("libMtaNativeCrash.so not loaded.");
        } else if (!a.c) {
            if (str == null) {
                try {
                    str = context.getDir("tombstones", 0).getAbsolutePath();
                } catch (Throwable th) {
                    b.w(th);
                    return;
                }
            }
            if (str.length() > 128) {
                b.e((Object) "The length of tombstones dir: " + str + " can't exceeds 200 bytes.");
                return;
            }
            f = str;
            p.b(context, "__mta_tombstone__", str);
            setNativeCrashEnable(true);
            a.initJNICrash(str);
            a.c = true;
            if (StatConfig.isDebugEnable()) {
                b.d("initNativeCrash success.");
            }
        }
    }

    public static boolean isNativeCrashDebugEnable() {
        return e;
    }

    public static boolean isNativeCrashEnable() {
        return d;
    }

    public static String onNativeCrashHappened() {
        String str = StatConstants.MTA_COOPERATION_TAG;
        try {
            new RuntimeException("MTA has caught a native crash, java stack:\n").printStackTrace();
            return str;
        } catch (RuntimeException e2) {
            return e2.toString();
        }
    }

    public static void setNativeCrashDebugEnable(boolean z) {
        if (!g) {
            b.warn("libMtaNativeCrash.so not loaded.");
            return;
        }
        try {
            a.enableNativeCrashDebug(z);
            e = z;
        } catch (Throwable th) {
            b.w(th);
        }
    }

    public static void setNativeCrashEnable(boolean z) {
        if (!g) {
            b.warn("libMtaNativeCrash.so not loaded.");
            return;
        }
        try {
            a.enableNativeCrash(z);
            d = z;
        } catch (Throwable th) {
            b.w(th);
        }
    }

    public native void enableNativeCrash(boolean z);

    public native void enableNativeCrashDebug(boolean z);

    public native boolean initJNICrash(String str);

    public native String makeJniCrash();

    public native String stringFromJNI();
}
