package com.tencent.tvMTA.utils;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Build.VERSION;
import android.provider.Settings.Secure;
import com.tencent.stat.common.StatConstants;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import org.apache.http.conn.util.InetAddressUtils;

public class AppUtils {
    private static final String TAG = "AppUtils";
    private static Class<?> mClassType = null;
    private static Method mGetMethod = null;

    public static boolean isNetWorkPreferennce(Context ctx) {
        if (9 == Secure.getInt(ctx.getContentResolver(), "network_preference", -1)) {
            return true;
        }
        return false;
    }

    public static boolean isWifiAvailable(Context ctx) {
        NetworkInfo wifi = ((ConnectivityManager) ctx.getSystemService("connectivity")).getNetworkInfo(1);
        if (wifi == null || !wifi.isAvailable()) {
            return false;
        }
        return true;
    }

    public static String getLocalMacAddress(Context ctx) {
        WifiInfo info = ((WifiManager) ctx.getSystemService("wifi")).getConnectionInfo();
        if (info != null) {
            return info.getMacAddress();
        }
        return StatConstants.MTA_COOPERATION_TAG;
    }

    public static String getLocalEthernetAddress(Context ctx) {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        return "0.0.0.0";
    }

    public static String getLocalIpAddress() {
        try {
            Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
            while (en.hasMoreElements()) {
                Enumeration<InetAddress> enumIpAddr = ((NetworkInterface) en.nextElement()).getInetAddresses();
                while (true) {
                    if (enumIpAddr.hasMoreElements()) {
                        InetAddress inetAddress = (InetAddress) enumIpAddr.nextElement();
                        if (!inetAddress.isLoopbackAddress() && InetAddressUtils.isIPv4Address(inetAddress.getHostAddress())) {
                            return inetAddress.getHostAddress().toString();
                        }
                    }
                }
            }
        } catch (SocketException e) {
        }
        return "0.0.0.0";
    }

    public static String getDeviceID() {
        return Build.MODEL;
    }

    public static String getBTMAC() {
        return BluetoothAdapter.getDefaultAdapter().getAddress();
    }

    private static void init() {
        try {
            if (mClassType == null) {
                mClassType = Class.forName("android.os.SystemProperties");
                mGetMethod = mClassType.getDeclaredMethod("get", new Class[]{String.class, String.class});
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getMD5() {
        init();
        String value = "1";
        try {
            return (String) mGetMethod.invoke(mClassType, new Object[]{"ro.build.version.incremental", value});
        } catch (Exception e) {
            e.printStackTrace();
            return value;
        }
    }

    public static String getDeviceCfg() {
        init();
        String value = "r2f8w6335";
        try {
            return (String) mGetMethod.invoke(mClassType, new Object[]{"ro.tencent.tvversion", value});
        } catch (Exception e) {
            e.printStackTrace();
            return value;
        }
    }

    public static String getDevicePlatform() {
        init();
        String value = "n200";
        try {
            return (String) mGetMethod.invoke(mClassType, new Object[]{"ro.build.product", value});
        } catch (Exception e) {
            e.printStackTrace();
            return value;
        }
    }

    public static int getModelName() {
        init();
        int value = 0;
        try {
            return Integer.parseInt((String) mGetMethod.invoke(mClassType, new Object[]{"ro.tencent.tvname", Integer.valueOf(value)}));
        } catch (Exception e) {
            e.printStackTrace();
            return value;
        }
    }

    public static String getGUID() {
        init();
        String value = "invalid";
        try {
            return (String) mGetMethod.invoke(mClassType, new Object[]{"persist.sys.tencent.guid", value});
        } catch (Exception e) {
            e.printStackTrace();
            return value;
        }
    }

    public static String getGUIDToken() {
        init();
        String value = "invalid";
        try {
            return (String) mGetMethod.invoke(mClassType, new Object[]{"persist.sys.tencent.securguid", value});
        } catch (Exception e) {
            e.printStackTrace();
            return value;
        }
    }

    public static String getSystemVersion() {
        init();
        String value = "1.1.1";
        try {
            return (String) mGetMethod.invoke(mClassType, new Object[]{"ro.build.version.incremental", value});
        } catch (Exception e) {
            e.printStackTrace();
            return value;
        }
    }

    public static boolean isNetworkAvailable(Context ctx) {
        ConnectivityManager manager = (ConnectivityManager) ctx.getSystemService("connectivity");
        if (manager == null) {
            return false;
        }
        NetworkInfo networkinfo = manager.getActiveNetworkInfo();
        if (networkinfo == null || !networkinfo.isAvailable() || networkinfo.getState() != State.CONNECTED) {
            return false;
        }
        return true;
    }

    public static int getSdkVersion() {
        int version = 0;
        try {
            return Integer.valueOf(VERSION.SDK).intValue();
        } catch (NumberFormatException e) {
            return version;
        }
    }

    public static long getAppInstallTime(Context ctx) {
        long j = 0;
        try {
            return new File(ctx.getPackageManager().getApplicationInfo(ctx.getPackageName(), 0).sourceDir).lastModified() / 1000;
        } catch (NameNotFoundException | Exception e) {
            return j;
        }
    }

    public static int getAppVersionCode(Context ctx) {
        char c = 10;
        try {
            return ctx.getPackageManager().getPackageInfo(ctx.getPackageName(), 0).versionCode;
        } catch (NameNotFoundException | RuntimeException e) {
            return c;
        }
    }

    public static boolean isEthernetAvailable(Context ctx) {
        NetworkInfo mobile = ((ConnectivityManager) ctx.getSystemService("connectivity")).getNetworkInfo(9);
        if (mobile == null || !mobile.isAvailable()) {
            return false;
        }
        return true;
    }

    public static String getAppVersionName(Context ctx) {
        try {
            return ctx.getPackageManager().getPackageInfo(ctx.getPackageName(), 0).versionName;
        } catch (NameNotFoundException e) {
            return Constant.VERSION_NAME;
        } catch (RuntimeException e2) {
            return Constant.VERSION_NAME;
        }
    }

    public static void setFilePermission(File path, int permission) {
        try {
            Class<?> hideClass = Class.forName("android.os.FileUtils");
            Method setPermissions = hideClass.getMethod("setPermissions", new Class[]{File.class, Integer.TYPE, Integer.TYPE, Integer.TYPE});
            try {
                setPermissions.invoke(hideClass.newInstance(), new Object[]{path, Integer.valueOf(permission), Integer.valueOf(-1), Integer.valueOf(-1)});
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
        } catch (ClassNotFoundException e2) {
            e2.printStackTrace();
        } catch (SecurityException e3) {
            e3.printStackTrace();
        } catch (IllegalArgumentException e4) {
            e4.printStackTrace();
        } catch (NoSuchMethodException e5) {
            e5.printStackTrace();
        } catch (IllegalAccessException e6) {
            e6.printStackTrace();
        } catch (InvocationTargetException e7) {
            e7.printStackTrace();
        } catch (Exception e8) {
            e8.printStackTrace();
        }
    }
}
