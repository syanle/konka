package com.tencent.tvMTA.core;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.os.Build;
import android.os.Build.VERSION;
import com.tencent.stat.common.StatConstants;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URLEncoder;
import java.util.Enumeration;
import org.apache.http.conn.util.InetAddressUtils;

public class GlobalInfo {
    public static final int CHANNEL_ID_DEFAULT = 10009;
    private static long mAppInstallTime;
    private static String mAppVer = StatConstants.MTA_COOPERATION_TAG;
    private static String mDeviceID;
    private static String mGUID;
    private static String mIPinfo;
    private static String mLicence_Id;
    private static String mMACAdress;
    private static String mMD5;
    private static String mOSVersion;
    private static String mOpenid;
    private static String mOpenid_Type;
    private static String mPackage;
    private static int mPlatform = 42;
    private static String mQQ;
    private static String mQua;
    private static String mResource;
    private static int mSdkVersion;
    private static String mUserAgent;
    private static int mVerCode = 0;

    public static void setAppVersion(String ver) {
        mAppVer = ver;
    }

    public static String getAppVersion() {
        return mAppVer;
    }

    public static void setVersionCode(int code) {
        mVerCode = code;
    }

    public static String getResource() {
        return mResource;
    }

    public static void setResource(Context ctx) {
        int iWidth = ctx.getResources().getDisplayMetrics().widthPixels;
        mResource = new StringBuilder(String.valueOf(iWidth)).append("x").append(ctx.getResources().getDisplayMetrics().heightPixels).toString();
    }

    public static String getPackageName() {
        return mPackage;
    }

    public static void setPackageName(Context ctx) {
        mPackage = ctx.getPackageName();
    }

    public static String getQua() {
        return mQua;
    }

    public static void setQua() {
        StringBuilder sb = new StringBuilder();
        sb.append("QV=1&");
        sb.append("PR=LAUNCHER&");
        sb.append("VN=" + mAppVer + "&");
        sb.append("PT=KK&");
        sb.append("RL=" + mResource + "&");
        sb.append("IT=" + mAppInstallTime + "&");
        sb.append("OS=" + mOSVersion + "&");
        sb.append("SV=" + VERSION.RELEASE + "&");
        sb.append("CHID=10009&");
        sb.append("DV=" + mDeviceID);
        try {
            mQua = URLEncoder.encode(sb.toString(), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            mQua = sb.toString();
        }
    }

    public static int getVersionCode() {
        return mVerCode;
    }

    public static String getSysVesion() {
        return mOSVersion;
    }

    public static void setSysVesion(String version) {
        mOSVersion = version;
    }

    public static String getQQ() {
        return mQQ;
    }

    public static void setQQ(String qq) {
        mQQ = qq;
    }

    public static String getOpenID() {
        return mOpenid;
    }

    public static void setOpenID(String openid) {
        mOpenid = openid;
    }

    public static String getOpenIDType() {
        return mOpenid_Type;
    }

    public static void setOpenIDType(String openidType) {
        mOpenid_Type = openidType;
    }

    public static String getLicenseID() {
        return mLicence_Id;
    }

    public static void setLicenseID(String licenseID) {
        mLicence_Id = licenseID;
    }

    public static String getMD5() {
        return mMD5;
    }

    public static void setMD5(String md5) {
        mMD5 = md5;
    }

    public static long getAppInstallTime() {
        return mAppInstallTime;
    }

    public static void setAppInstallTime(long time) {
        mAppInstallTime = time;
    }

    public static int getSdkNum() {
        return mSdkVersion;
    }

    public static void setSdkNum(int sdkNum) {
        mSdkVersion = sdkNum;
    }

    public static int getPlatform() {
        return mPlatform;
    }

    public static String getGUID() {
        return mGUID;
    }

    public static void setGUID(String GUID) {
        mGUID = GUID;
    }

    public static String getMACAdress() {
        return mMACAdress;
    }

    public static void setMACAdress(String mac) {
        mMACAdress = mac;
    }

    public static String getIPInfo() {
        return mIPinfo;
    }

    public static void setIPInfo(String ip) {
        mIPinfo = ip;
    }

    public static String getDeviceID() {
        return mDeviceID;
    }

    public static void setDeviceID(String deviceID) {
        mDeviceID = deviceID;
    }

    public static String getUserAgent() {
        return mUserAgent;
    }

    public static void setUserAgent(String userAgent) {
        mUserAgent = userAgent;
    }

    public static int getFLowForMTA() {
        return (int) ((System.currentTimeMillis() / 1000) + ((long) ((int) (Math.random() * 10.0d))));
    }

    public static String getDeviceName() {
        return urlToFileName(Build.MODEL);
    }

    public static String getLocalIPAdresss() {
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
        return StatConstants.MTA_COOPERATION_TAG;
    }

    public static String urlToFileName(String fileName) {
        return fileName.replace("\\", StatConstants.MTA_COOPERATION_TAG).replace("/", StatConstants.MTA_COOPERATION_TAG).replace(":", StatConstants.MTA_COOPERATION_TAG).replace("*", StatConstants.MTA_COOPERATION_TAG).replace("?", StatConstants.MTA_COOPERATION_TAG).replace("\"", StatConstants.MTA_COOPERATION_TAG).replace("<", StatConstants.MTA_COOPERATION_TAG).replace(">", StatConstants.MTA_COOPERATION_TAG).replace("|", StatConstants.MTA_COOPERATION_TAG).replace("&", "_").replace(" ", "_");
    }

    public static String getNetworkTypeName(Context ctx) {
        ConnectivityManager manager = (ConnectivityManager) ctx.getSystemService("connectivity");
        if (manager == null) {
            return StatConstants.MTA_COOPERATION_TAG;
        }
        NetworkInfo networkinfo = manager.getActiveNetworkInfo();
        if (networkinfo == null || !networkinfo.isAvailable()) {
            return StatConstants.MTA_COOPERATION_TAG;
        }
        if (networkinfo.getState() == State.CONNECTED) {
            return networkinfo.getTypeName();
        }
        return StatConstants.MTA_COOPERATION_TAG;
    }
}
