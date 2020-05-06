package com.tencent.stat.common;

import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.ResolveInfo;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Proxy;
import android.os.Environment;
import android.os.Process;
import android.os.StatFs;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;
import com.konka.kkinterface.tv.ChannelDesk;
import com.tencent.stat.StatConfig;
import com.tencent.stat.StatSpecifyReportedInfo;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.zip.GZIPInputStream;
import org.apache.http.HttpHost;
import org.json.JSONObject;

public class k {
    private static String a = null;
    private static String b = null;
    private static String c = null;
    private static String d = null;
    private static Random e = null;
    private static DisplayMetrics f = null;
    private static String g = null;
    private static String h = StatConstants.MTA_COOPERATION_TAG;
    private static String i = StatConstants.MTA_COOPERATION_TAG;
    private static int j = -1;
    /* access modifiers changed from: private */
    public static StatLogger k = null;
    private static String l = null;
    private static String m = null;
    private static volatile int n = -1;
    private static String o = null;
    private static String p = null;
    private static long q = -1;
    private static String r = StatConstants.MTA_COOPERATION_TAG;
    private static n s = null;
    private static String t = "__MTA_FIRST_ACTIVATE__";
    private static int u = -1;
    private static long v = -1;
    private static int w = 0;
    private static String x = StatConstants.MTA_COOPERATION_TAG;

    public static int A(Context context) {
        return p.a(context, "mta.qq.com.difftime", 0);
    }

    public static String B(Context context) {
        if (c(x)) {
            return x;
        }
        try {
            if (q.a(context, "android.permission.CAMERA")) {
                Camera open = Camera.open();
                if (open != null) {
                    Size size = (Size) open.getParameters().getSupportedPictureSizes().get(0);
                    x = size.width + "*" + size.height;
                }
            }
        } catch (Throwable th) {
            k.w("getCameras failed, " + th.toString());
        }
        return x;
    }

    public static boolean C(Context context) {
        if (context == null) {
            return false;
        }
        ActivityManager activityManager = (ActivityManager) context.getSystemService("activity");
        if (activityManager == null) {
            return false;
        }
        String packageName = context.getPackageName();
        for (RunningAppProcessInfo runningAppProcessInfo : activityManager.getRunningAppProcesses()) {
            if (runningAppProcessInfo.processName.startsWith(packageName)) {
                return runningAppProcessInfo.importance == 400;
            }
        }
        return false;
    }

    public static String D(Context context) {
        if (context == null) {
            return null;
        }
        Intent intent = new Intent("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.HOME");
        ResolveInfo resolveActivity = context.getPackageManager().resolveActivity(intent, 0);
        if (resolveActivity.activityInfo == null || resolveActivity.activityInfo.packageName.equals("android")) {
            return null;
        }
        return resolveActivity.activityInfo.packageName;
    }

    private static long E(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService("activity");
        MemoryInfo memoryInfo = new MemoryInfo();
        activityManager.getMemoryInfo(memoryInfo);
        return memoryInfo.availMem;
    }

    public static int a() {
        return g().nextInt(Integer.MAX_VALUE);
    }

    public static int a(Context context, boolean z) {
        if (z) {
            w = A(context);
        }
        return w;
    }

    public static Long a(String str, String str2, int i2, int i3, Long l2) {
        if (str == null || str2 == null) {
            return l2;
        }
        if (str2.equalsIgnoreCase(".") || str2.equalsIgnoreCase("|")) {
            str2 = "\\" + str2;
        }
        String[] split = str.split(str2);
        if (split.length != i3) {
            return l2;
        }
        try {
            Long valueOf = Long.valueOf(0);
            int i4 = 0;
            while (i4 < split.length) {
                Long valueOf2 = Long.valueOf(((long) i2) * (valueOf.longValue() + Long.valueOf(split[i4]).longValue()));
                i4++;
                valueOf = valueOf2;
            }
            return valueOf;
        } catch (NumberFormatException e2) {
            return l2;
        }
    }

    public static String a(int i2) {
        Calendar instance = Calendar.getInstance();
        instance.roll(6, i2);
        return new SimpleDateFormat("yyyyMMdd").format(instance.getTime());
    }

    public static String a(long j2) {
        return new SimpleDateFormat("yyyyMMdd").format(new Date(j2));
    }

    public static String a(Context context, String str) {
        if (!StatConfig.isEnableConcurrentProcess()) {
            return str;
        }
        if (m == null) {
            m = q(context);
        }
        return m != null ? str + "_" + m : str;
    }

    public static String a(String str) {
        if (str == null) {
            return "0";
        }
        try {
            MessageDigest instance = MessageDigest.getInstance("MD5");
            instance.update(str.getBytes());
            byte[] digest = instance.digest();
            StringBuffer stringBuffer = new StringBuffer();
            for (byte b2 : digest) {
                byte b3 = b2 & 255;
                if (b3 < 16) {
                    stringBuffer.append("0");
                }
                stringBuffer.append(Integer.toHexString(b3));
            }
            return stringBuffer.toString();
        } catch (Throwable th) {
            return "0";
        }
    }

    public static HttpHost a(Context context) {
        if (context == null) {
            return null;
        }
        try {
            if (context.getPackageManager().checkPermission("android.permission.ACCESS_NETWORK_STATE", context.getPackageName()) != 0) {
                return null;
            }
            NetworkInfo activeNetworkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
            if (activeNetworkInfo == null) {
                return null;
            }
            if (activeNetworkInfo.getTypeName() != null && activeNetworkInfo.getTypeName().equalsIgnoreCase("WIFI")) {
                return null;
            }
            String extraInfo = activeNetworkInfo.getExtraInfo();
            if (extraInfo == null) {
                return null;
            }
            if (extraInfo.equals("cmwap") || extraInfo.equals("3gwap") || extraInfo.equals("uniwap")) {
                return new HttpHost("10.0.0.172", 80);
            }
            if (extraInfo.equals("ctwap")) {
                return new HttpHost("10.0.0.200", 80);
            }
            String defaultHost = Proxy.getDefaultHost();
            if (defaultHost != null && defaultHost.trim().length() > 0) {
                return new HttpHost(defaultHost, Proxy.getDefaultPort());
            }
            return null;
        } catch (Throwable th) {
            k.e(th);
        }
    }

    public static void a(Context context, int i2) {
        w = i2;
        p.b(context, "mta.qq.com.difftime", i2);
    }

    public static boolean a(StatSpecifyReportedInfo statSpecifyReportedInfo) {
        if (statSpecifyReportedInfo == null) {
            return false;
        }
        return c(statSpecifyReportedInfo.getAppKey());
    }

    public static byte[] a(byte[] bArr) throws IOException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bArr);
        GZIPInputStream gZIPInputStream = new GZIPInputStream(byteArrayInputStream);
        byte[] bArr2 = new byte[4096];
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(bArr.length * 2);
        while (true) {
            int read = gZIPInputStream.read(bArr2);
            if (read != -1) {
                byteArrayOutputStream.write(bArr2, 0, read);
            } else {
                byte[] byteArray = byteArrayOutputStream.toByteArray();
                byteArrayInputStream.close();
                gZIPInputStream.close();
                byteArrayOutputStream.close();
                return byteArray;
            }
        }
    }

    public static long b(String str) {
        return a(str, ".", 100, 3, Long.valueOf(0)).longValue();
    }

    public static synchronized StatLogger b() {
        StatLogger statLogger;
        synchronized (k.class) {
            if (k == null) {
                k = new StatLogger(StatConstants.LOG_TAG);
                k.setDebugEnable(false);
            }
            statLogger = k;
        }
        return statLogger;
    }

    public static synchronized String b(Context context) {
        String str;
        synchronized (k.class) {
            if (a == null || a.trim().length() == 0) {
                a = q.a(context);
                if (a == null || a.trim().length() == 0) {
                    a = Integer.toString(g().nextInt(Integer.MAX_VALUE));
                }
                str = a;
            } else {
                str = a;
            }
        }
        return str;
    }

    public static long c() {
        try {
            Calendar instance = Calendar.getInstance();
            instance.set(11, 0);
            instance.set(12, 0);
            instance.set(13, 0);
            instance.set(14, 0);
            return instance.getTimeInMillis() + 86400000;
        } catch (Throwable th) {
            k.e(th);
            return System.currentTimeMillis() + 86400000;
        }
    }

    public static synchronized String c(Context context) {
        String str;
        synchronized (k.class) {
            if (c == null || c.trim().length() == 0) {
                c = q.b(context);
            }
            str = c;
        }
        return str;
    }

    public static boolean c(String str) {
        return (str == null || str.trim().length() == 0) ? false : true;
    }

    public static DisplayMetrics d(Context context) {
        if (f == null) {
            f = new DisplayMetrics();
            ((WindowManager) context.getApplicationContext().getSystemService("window")).getDefaultDisplay().getMetrics(f);
        }
        return f;
    }

    public static String d() {
        if (c(p)) {
            return p;
        }
        long e2 = e() / 1000000;
        StatFs statFs = new StatFs(Environment.getDataDirectory().getPath());
        p = String.valueOf((((long) statFs.getAvailableBlocks()) * ((long) statFs.getBlockSize())) / 1000000) + "/" + String.valueOf(e2);
        return p;
    }

    public static long e() {
        StatFs statFs = new StatFs(Environment.getDataDirectory().getPath());
        return ((long) statFs.getBlockCount()) * ((long) statFs.getBlockSize());
    }

    public static boolean e(Context context) {
        try {
            if (q.a(context, "android.permission.ACCESS_WIFI_STATE")) {
                ConnectivityManager connectivityManager = (ConnectivityManager) context.getApplicationContext().getSystemService("connectivity");
                if (connectivityManager != null) {
                    NetworkInfo[] allNetworkInfo = connectivityManager.getAllNetworkInfo();
                    if (allNetworkInfo != null) {
                        for (int i2 = 0; i2 < allNetworkInfo.length; i2++) {
                            if (allNetworkInfo[i2].getTypeName().equalsIgnoreCase("WIFI") && allNetworkInfo[i2].isConnected()) {
                                return true;
                            }
                        }
                    }
                }
                return false;
            }
            k.warn("can not get the permission of android.permission.ACCESS_WIFI_STATE");
            return false;
        } catch (Throwable th) {
            k.e(th);
        }
    }

    public static String f(Context context) {
        if (b != null) {
            return b;
        }
        try {
            ApplicationInfo applicationInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), 128);
            if (applicationInfo != null) {
                String string = applicationInfo.metaData.getString("TA_APPKEY");
                if (string != null) {
                    b = string;
                    return string;
                }
                k.i("Could not read APPKEY meta-data from AndroidManifest.xml");
            }
        } catch (Throwable th) {
            k.i("Could not read APPKEY meta-data from AndroidManifest.xml");
        }
        return null;
    }

    public static String g(Context context) {
        try {
            ApplicationInfo applicationInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), 128);
            if (applicationInfo != null) {
                Object obj = applicationInfo.metaData.get("InstallChannel");
                if (obj != null) {
                    return obj.toString();
                }
                k.w("Could not read InstallChannel meta-data from AndroidManifest.xml");
            }
        } catch (Throwable th) {
            k.e((Object) "Could not read InstallChannel meta-data from AndroidManifest.xml");
        }
        return null;
    }

    private static synchronized Random g() {
        Random random;
        synchronized (k.class) {
            if (e == null) {
                e = new Random();
            }
            random = e;
        }
        return random;
    }

    private static long h() {
        if (q > 0) {
            return q;
        }
        long j2 = 1;
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader("/proc/meminfo"), 8192);
            j2 = (long) (Integer.valueOf(bufferedReader.readLine().split("\\s+")[1]).intValue() * 1024);
            bufferedReader.close();
        } catch (Exception e2) {
        }
        q = j2;
        return q;
    }

    public static String h(Context context) {
        if (context == null) {
            return null;
        }
        return context.getClass().getName();
    }

    public static String i(Context context) {
        if (g != null) {
            return g;
        }
        try {
            if (!q.a(context, "android.permission.READ_PHONE_STATE")) {
                k.e((Object) "Could not get permission of android.permission.READ_PHONE_STATE");
            } else if (k(context)) {
                TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService("phone");
                if (telephonyManager != null) {
                    g = telephonyManager.getSimOperator();
                }
            }
        } catch (Throwable th) {
            k.e(th);
        }
        return g;
    }

    public static String j(Context context) {
        if (c(h)) {
            return h;
        }
        try {
            h = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
            if (h == null) {
                return StatConstants.MTA_COOPERATION_TAG;
            }
        } catch (Throwable th) {
            k.e(th);
        }
        return h;
    }

    public static boolean k(Context context) {
        return context.getPackageManager().checkPermission("android.permission.READ_PHONE_STATE", context.getPackageName()) == 0;
    }

    public static String l(Context context) {
        String str = StatConstants.MTA_COOPERATION_TAG;
        try {
            if (!q.a(context, "android.permission.INTERNET") || !q.a(context, "android.permission.ACCESS_NETWORK_STATE")) {
                k.e((Object) "can not get the permission of android.permission.ACCESS_WIFI_STATE");
                return str;
            }
            NetworkInfo activeNetworkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
            if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
                String typeName = activeNetworkInfo.getTypeName();
                String extraInfo = activeNetworkInfo.getExtraInfo();
                if (typeName != null) {
                    return typeName.equalsIgnoreCase("WIFI") ? "WIFI" : typeName.equalsIgnoreCase("MOBILE") ? extraInfo == null ? "MOBILE" : extraInfo : extraInfo == null ? typeName : extraInfo;
                }
            }
            return str;
        } catch (Throwable th) {
            k.e(th);
        }
    }

    public static Integer m(Context context) {
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService("phone");
            if (telephonyManager != null) {
                return Integer.valueOf(telephonyManager.getNetworkType());
            }
        } catch (Throwable th) {
        }
        return null;
    }

    public static String n(Context context) {
        if (c(i)) {
            return i;
        }
        try {
            i = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
            if (i == null || i.length() == 0) {
                return "unknown";
            }
        } catch (Throwable th) {
            k.e(th);
        }
        return i;
    }

    public static int o(Context context) {
        if (j != -1) {
            return j;
        }
        try {
            if (o.a()) {
                j = 1;
            }
        } catch (Throwable th) {
            k.e(th);
        }
        j = 0;
        return j;
    }

    public static String p(Context context) {
        if (c(l)) {
            return l;
        }
        try {
            if (q.a(context, "android.permission.WRITE_EXTERNAL_STORAGE")) {
                String externalStorageState = Environment.getExternalStorageState();
                if (externalStorageState == null || !externalStorageState.equals("mounted")) {
                    return null;
                }
                String path = Environment.getExternalStorageDirectory().getPath();
                if (path == null) {
                    return null;
                }
                StatFs statFs = new StatFs(path);
                long blockSize = (((long) statFs.getBlockSize()) * ((long) statFs.getAvailableBlocks())) / 1000000;
                l = String.valueOf(blockSize) + "/" + String.valueOf((((long) statFs.getBlockCount()) * ((long) statFs.getBlockSize())) / 1000000);
                return l;
            }
            k.warn("can not get the permission of android.permission.WRITE_EXTERNAL_STORAGE");
            return null;
        } catch (Throwable th) {
            k.e(th);
            return null;
        }
    }

    static String q(Context context) {
        try {
            if (m != null) {
                return m;
            }
            int myPid = Process.myPid();
            Iterator it = ((ActivityManager) context.getSystemService("activity")).getRunningAppProcesses().iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                RunningAppProcessInfo runningAppProcessInfo = (RunningAppProcessInfo) it.next();
                if (runningAppProcessInfo.pid == myPid) {
                    m = runningAppProcessInfo.processName;
                    break;
                }
            }
            return m;
        } catch (Throwable th) {
        }
    }

    public static String r(Context context) {
        return a(context, StatConstants.DATABASE_NAME);
    }

    public static synchronized Integer s(Context context) {
        Integer valueOf;
        int i2 = 0;
        synchronized (k.class) {
            if (n <= 0) {
                n = p.a(context, "MTA_EVENT_INDEX", 0);
                p.b(context, "MTA_EVENT_INDEX", n + ChannelDesk.max_dtv_count);
            } else if (n % ChannelDesk.max_dtv_count == 0) {
                try {
                    int i3 = n + ChannelDesk.max_dtv_count;
                    if (n < 2147383647) {
                        i2 = i3;
                    }
                    p.b(context, "MTA_EVENT_INDEX", i2);
                } catch (Throwable th) {
                    k.w(th);
                }
            }
            n++;
            valueOf = Integer.valueOf(n);
        }
        return valueOf;
    }

    public static String t(Context context) {
        try {
            return String.valueOf(E(context) / 1000000) + "/" + String.valueOf(h() / 1000000);
        } catch (Throwable th) {
            th.printStackTrace();
            return null;
        }
    }

    public static JSONObject u(Context context) {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("n", l.a());
            String d2 = l.d();
            if (d2 != null && d2.length() > 0) {
                jSONObject.put("na", d2);
            }
            int b2 = l.b();
            if (b2 > 0) {
                jSONObject.put("fx", b2 / 1000000);
            }
            int c2 = l.c();
            if (c2 > 0) {
                jSONObject.put("fn", c2 / 1000000);
            }
        } catch (Throwable th) {
            Log.w(StatConstants.LOG_TAG, "get cpu error", th);
        }
        return jSONObject;
    }

    public static String v(Context context) {
        if (c(r)) {
            return r;
        }
        try {
            SensorManager sensorManager = (SensorManager) context.getSystemService("sensor");
            if (sensorManager != null) {
                List sensorList = sensorManager.getSensorList(-1);
                if (sensorList != null) {
                    StringBuilder sb = new StringBuilder(sensorList.size() * 10);
                    for (int i2 = 0; i2 < sensorList.size(); i2++) {
                        sb.append(((Sensor) sensorList.get(i2)).getType());
                        if (i2 != sensorList.size() - 1) {
                            sb.append(",");
                        }
                    }
                    r = sb.toString();
                }
            }
        } catch (Throwable th) {
            k.e(th);
        }
        return r;
    }

    public static synchronized int w(Context context) {
        int i2;
        synchronized (k.class) {
            if (u != -1) {
                i2 = u;
            } else {
                x(context);
                i2 = u;
            }
        }
        return i2;
    }

    public static void x(Context context) {
        u = p.a(context, t, 1);
        if (u == 1) {
            p.b(context, t, 0);
        }
    }

    public static boolean y(Context context) {
        if (v < 0) {
            v = p.a(context, "mta.qq.com.checktime", 0);
        }
        return Math.abs(System.currentTimeMillis() - v) > 86400000;
    }

    public static void z(Context context) {
        v = System.currentTimeMillis();
        p.b(context, "mta.qq.com.checktime", v);
    }
}
