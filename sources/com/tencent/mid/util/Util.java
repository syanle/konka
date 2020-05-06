package com.tencent.mid.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Proxy;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Environment;
import android.os.StatFs;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;
import com.tencent.mid.api.MidEntity;
import com.tencent.mid.api.MidService;
import com.tencent.mid.b.g;
import com.tencent.stat.common.StatConstants;
import com.umeng.common.util.e;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.List;
import java.util.zip.GZIPInputStream;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.apache.http.HttpHost;
import org.json.JSONArray;
import org.json.JSONObject;

public class Util {
    public static byte[] StringToBytes(String str) {
        if (str == null) {
            return null;
        }
        try {
            return str.getBytes(e.f);
        } catch (UnsupportedEncodingException e) {
            return str.getBytes();
        }
    }

    public static String bytesToString(byte[] bArr) {
        if (bArr == null) {
            return null;
        }
        try {
            return new String(bArr, e.f);
        } catch (UnsupportedEncodingException e) {
            return new String(bArr);
        }
    }

    public static boolean checkPermission(Context context, String str) {
        try {
            return context.getPackageManager().checkPermission(str, context.getPackageName()) == 0;
        } catch (Throwable th) {
            Log.e("MID", "checkPermission error", th);
            return false;
        }
    }

    public static void clear(Context context) {
        if (context != null) {
            g.a(context).c();
        }
    }

    public static String decode(String str) {
        if (str == null) {
            return null;
        }
        if (VERSION.SDK_INT < 8) {
            return str;
        }
        try {
            return new String(h.b(a.a(str.getBytes(e.f), 0)), e.f).trim().replace("\t", StatConstants.MTA_COOPERATION_TAG).replace("\n", StatConstants.MTA_COOPERATION_TAG).replace("\r", StatConstants.MTA_COOPERATION_TAG);
        } catch (Throwable th) {
            Log.e("MID", "decode error", th);
            return str;
        }
    }

    public static byte[] deocdeGZipContent(byte[] bArr) {
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

    public static String encode(String str) {
        if (str == null) {
            return null;
        }
        if (VERSION.SDK_INT < 8) {
            return str;
        }
        try {
            return new String(a.b(h.a(str.getBytes(e.f)), 0), e.f).trim().replace("\t", StatConstants.MTA_COOPERATION_TAG).replace("\n", StatConstants.MTA_COOPERATION_TAG).replace("\r", StatConstants.MTA_COOPERATION_TAG);
        } catch (Throwable th) {
            Log.w("MID", "encode error", th);
            return str;
        }
    }

    public static boolean equal(MidEntity midEntity, MidEntity midEntity2) {
        return (midEntity == null || midEntity2 == null) ? midEntity == null && midEntity2 == null : midEntity.compairTo(midEntity2) == 0;
    }

    public static String getDeviceModel() {
        return Build.MODEL;
    }

    public static DisplayMetrics getDisplayMetrics(Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((WindowManager) context.getApplicationContext().getSystemService("window")).getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics;
    }

    public static String getExternalStorageInfo(Context context) {
        try {
            if (checkPermission(context, "android.permission.WRITE_EXTERNAL_STORAGE")) {
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
            Log.e("MID", "can not get the permission of android.permission.WRITE_EXTERNAL_STORAGE");
            return null;
        } catch (Throwable th) {
            Log.e("MID", StatConstants.MTA_COOPERATION_TAG, th);
            return null;
        }
    }

    public static byte[] getHMAC(String str, String str2) {
        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec(str.getBytes(), "hmacmd5");
            Mac instance = Mac.getInstance("HmacSHA1");
            instance.init(secretKeySpec);
            instance.update(str2.getBytes());
            return instance.doFinal();
        } catch (Exception e) {
            logWarn(e);
            return null;
        }
    }

    public static HttpHost getHttpProxy() {
        if (Proxy.getDefaultHost() != null) {
            return new HttpHost(Proxy.getDefaultHost(), Proxy.getDefaultPort());
        }
        return null;
    }

    public static HttpHost getHttpProxy(Context context) {
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
            logInfo("network type:" + extraInfo);
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
            logWarn(th);
        }
    }

    public static String getHttpUrl() {
        return "http://pingmid.qq.com:80/";
    }

    public static String getImei(Context context) {
        try {
            if (checkPermission(context, "android.permission.READ_PHONE_STATE")) {
                String str = StatConstants.MTA_COOPERATION_TAG;
                String deviceId = ((TelephonyManager) context.getSystemService("phone")).getDeviceId();
                if (deviceId != null) {
                    return deviceId;
                }
            } else {
                logInfo("Could not get permission of android.permission.READ_PHONE_STATE");
            }
        } catch (Throwable th) {
            logWarn(th);
        }
        return StatConstants.MTA_COOPERATION_TAG;
    }

    public static String getLinkedWay(Context context) {
        try {
            if (!checkPermission(context, "android.permission.INTERNET") || !checkPermission(context, "android.permission.ACCESS_NETWORK_STATE")) {
                Log.e("MID", "can not get the permission of android.permission.ACCESS_WIFI_STATE");
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
            Log.e("MID", StatConstants.MTA_COOPERATION_TAG, th);
        }
    }

    public static MidEntity getNewerMidEntity(MidEntity midEntity, MidEntity midEntity2) {
        if (midEntity != null && midEntity2 != null) {
            return midEntity.compairTo(midEntity2) >= 0 ? midEntity : midEntity2;
        }
        if (midEntity != null) {
            return midEntity;
        }
        if (midEntity2 != null) {
            return midEntity2;
        }
        return null;
    }

    public static String getRemoteUrlIp(String str) {
        try {
            URL url = new URL(str);
            if (url != null) {
                InetAddress byName = InetAddress.getByName(url.getHost());
                if (byName != null) {
                    return byName.getHostAddress();
                }
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
        return null;
    }

    public static String getSimOperator(Context context) {
        try {
            if (checkPermission(context, "android.permission.READ_PHONE_STATE")) {
                TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService("phone");
                if (telephonyManager != null) {
                    return telephonyManager.getSimOperator();
                }
            } else {
                Log.e("MID", "Could not get permission of android.permission.READ_PHONE_STATE");
            }
        } catch (Throwable th) {
            Log.e("MID", StatConstants.MTA_COOPERATION_TAG, th);
        }
        return null;
    }

    public static Integer getTelephonyNetworkType(Context context) {
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService("phone");
            if (telephonyManager != null) {
                return Integer.valueOf(telephonyManager.getNetworkType());
            }
        } catch (Throwable th) {
        }
        return null;
    }

    public static String getWiFiBBSID(Context context) {
        try {
            WifiInfo wifiInfo = getWifiInfo(context);
            if (wifiInfo != null) {
                return wifiInfo.getBSSID();
            }
        } catch (Throwable th) {
            logWarn(th);
        }
        return null;
    }

    public static String getWiFiSSID(Context context) {
        try {
            WifiInfo wifiInfo = getWifiInfo(context);
            if (wifiInfo != null) {
                return wifiInfo.getSSID();
            }
        } catch (Throwable th) {
            logWarn(th);
        }
        return null;
    }

    public static WifiInfo getWifiInfo(Context context) {
        if (checkPermission(context, "android.permission.ACCESS_WIFI_STATE")) {
            WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService("wifi");
            if (wifiManager != null) {
                return wifiManager.getConnectionInfo();
            }
        }
        return null;
    }

    public static String getWifiMacAddress(Context context) {
        if (checkPermission(context, "android.permission.ACCESS_WIFI_STATE")) {
            try {
                WifiManager wifiManager = (WifiManager) context.getSystemService("wifi");
                return wifiManager == null ? StatConstants.MTA_COOPERATION_TAG : wifiManager.getConnectionInfo().getMacAddress();
            } catch (Exception e) {
                logInfo("get wifi address error" + e);
                return StatConstants.MTA_COOPERATION_TAG;
            }
        } else {
            logInfo("Could not get permission of android.permission.ACCESS_WIFI_STATE");
            return StatConstants.MTA_COOPERATION_TAG;
        }
    }

    public static JSONArray getWifiTopN(Context context, int i) {
        try {
            if (!checkPermission(context, "android.permission.INTERNET") || !checkPermission(context, "android.permission.ACCESS_NETWORK_STATE")) {
                logInfo("can not get the permisson of android.permission.INTERNET");
                return null;
            }
            WifiManager wifiManager = (WifiManager) context.getSystemService("wifi");
            if (wifiManager != null) {
                List scanResults = wifiManager.getScanResults();
                if (scanResults != null && scanResults.size() > 0) {
                    Collections.sort(scanResults, new k());
                    JSONArray jSONArray = new JSONArray();
                    int i2 = 0;
                    while (i2 < scanResults.size() && i2 < i) {
                        ScanResult scanResult = (ScanResult) scanResults.get(i2);
                        JSONObject jSONObject = new JSONObject();
                        jSONObject.put("bs", scanResult.BSSID);
                        jSONObject.put("ss", scanResult.SSID);
                        jSONArray.put(jSONObject);
                        i2++;
                    }
                    return jSONArray;
                }
            }
            return null;
        } catch (Throwable th) {
            logWarn(th);
        }
    }

    public static boolean isMidValid(String str) {
        return str != null && str.trim().length() >= 40;
    }

    public static boolean isNetworkAvailable(Context context) {
        try {
            if (checkPermission(context, "android.permission.INTERNET") && checkPermission(context, "android.permission.ACCESS_NETWORK_STATE")) {
                ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
                if (connectivityManager != null) {
                    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                    if (activeNetworkInfo != null && activeNetworkInfo.isAvailable()) {
                        return true;
                    }
                    Log.w("MID", "Network error");
                    return false;
                }
            }
        } catch (Throwable th) {
            Log.e("MID", "isNetworkAvailable error", th);
        }
        return false;
    }

    public static boolean isStringValid(String str) {
        return (str == null || str.trim().length() == 0) ? false : true;
    }

    public static boolean isWifiNet(Context context) {
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
        return (activeNetworkInfo == null || activeNetworkInfo.getTypeName() == null || !activeNetworkInfo.getTypeName().equalsIgnoreCase("WIFI")) ? false : true;
    }

    public static void jsonPut(JSONObject jSONObject, String str, String str2) {
        if (isStringValid(str2)) {
            jSONObject.put(str, str2);
        }
    }

    public static void logInfo(String str) {
        if (MidService.isEnableDebug()) {
            Log.i("MID", str);
        }
    }

    public static void logWarn(Throwable th) {
        if (MidService.isEnableDebug()) {
            Log.w("MID", th);
        }
    }

    public static String md5(String str) {
        try {
            MessageDigest instance = MessageDigest.getInstance("MD5");
            instance.update(str.getBytes(e.f));
            byte[] digest = instance.digest();
            StringBuffer stringBuffer = new StringBuffer();
            for (byte append : digest) {
                stringBuffer.append(append);
            }
            return stringBuffer.toString();
        } catch (NoSuchAlgorithmException e) {
            logWarn(e);
            return str;
        } catch (UnsupportedEncodingException e2) {
            logWarn(e2);
            return str;
        }
    }

    public static void updateIfLocalInvalid(Context context, String str) {
        if (isMidValid(str) && !isStringValid(MidService.getLocalMidOnly(context)) && isStringValid(str)) {
            MidEntity midEntity = new MidEntity();
            midEntity.setImei(getImei(context));
            midEntity.setMac(getWifiMacAddress(context));
            midEntity.setMid(str);
            g.a(context).a(midEntity);
        }
    }
}
