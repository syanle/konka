package com.tencent.mid.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.os.StatFs;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import com.tencent.stat.common.StatConstants;

public class i {
    public static DisplayMetrics a(Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((WindowManager) context.getApplicationContext().getSystemService("window")).getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics;
    }

    public static String a() {
        try {
            long b = b() / 1000000;
            StatFs statFs = new StatFs(Environment.getDataDirectory().getPath());
            return String.valueOf((((long) statFs.getAvailableBlocks()) * ((long) statFs.getBlockSize())) / 1000000) + "/" + String.valueOf(b);
        } catch (Throwable th) {
            Util.logWarn(th);
            return StatConstants.MTA_COOPERATION_TAG;
        }
    }

    public static long b() {
        StatFs statFs = new StatFs(Environment.getDataDirectory().getPath());
        return ((long) statFs.getBlockCount()) * ((long) statFs.getBlockSize());
    }

    public static String b(Context context) {
        try {
            if (Util.checkPermission(context, "android.permission.READ_PHONE_STATE")) {
                TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService("phone");
                if (telephonyManager != null) {
                    return telephonyManager.getSimOperator();
                }
            } else {
                Util.logInfo("Could not get permission of android.permission.READ_PHONE_STATE");
            }
        } catch (Throwable th) {
            Util.logWarn(th);
        }
        return null;
    }

    public static String c(Context context) {
        String str = StatConstants.MTA_COOPERATION_TAG;
        try {
            String str2 = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
            return str2 == null ? StatConstants.MTA_COOPERATION_TAG : str2;
        } catch (Throwable th) {
            Throwable th2 = th;
            String str3 = str;
            Util.logWarn(th2);
            return str3;
        }
    }

    public static String d(Context context) {
        try {
            if (!Util.checkPermission(context, "android.permission.INTERNET") || !Util.checkPermission(context, "android.permission.ACCESS_NETWORK_STATE")) {
                Util.logInfo("can not get the permission of android.permission.ACCESS_WIFI_STATE");
                return null;
            }
            NetworkInfo activeNetworkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
            if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
                String typeName = activeNetworkInfo.getTypeName();
                String extraInfo = activeNetworkInfo.getExtraInfo();
                if (typeName != null) {
                    return typeName.equalsIgnoreCase("WIFI") ? "WIFI" : typeName.equalsIgnoreCase("MOBILE") ? extraInfo == null ? "MOBILE" : extraInfo : extraInfo == null ? typeName : extraInfo;
                }
            }
            return null;
        } catch (Throwable th) {
            Util.logWarn(th);
        }
    }

    public static Integer e(Context context) {
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService("phone");
            if (telephonyManager != null) {
                return Integer.valueOf(telephonyManager.getNetworkType());
            }
        } catch (Throwable th) {
        }
        return null;
    }

    public static String f(Context context) {
        try {
            if (Util.checkPermission(context, "android.permission.WRITE_EXTERNAL_STORAGE")) {
                String externalStorageState = Environment.getExternalStorageState();
                if (externalStorageState == null || !externalStorageState.equals("mounted")) {
                    return null;
                }
                String path = Environment.getExternalStorageDirectory().getPath();
                if (path == null) {
                    return null;
                }
                StatFs statFs = new StatFs(path);
                return String.valueOf((((long) statFs.getBlockSize()) * ((long) statFs.getAvailableBlocks())) / 1000000) + "/" + String.valueOf((((long) statFs.getBlockCount()) * ((long) statFs.getBlockSize())) / 1000000);
            }
            Util.logInfo("can not get the permission of android.permission.WRITE_EXTERNAL_STORAGE");
            return null;
        } catch (Throwable th) {
            Util.logWarn(th);
            return null;
        }
    }
}
