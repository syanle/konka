package com.umeng.common.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import com.tencent.stat.common.StatConstants;
import com.umeng.common.Log;
import java.io.File;
import java.io.FileInputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/* compiled from: Helper */
public class h {
    public static final String a = System.getProperty("line.separator");
    private static final String b = "helper";

    public static String a(String str) {
        if (str == null) {
            return null;
        }
        try {
            byte[] bytes = str.getBytes();
            MessageDigest instance = MessageDigest.getInstance("MD5");
            instance.reset();
            instance.update(bytes);
            byte[] digest = instance.digest();
            StringBuffer stringBuffer = new StringBuffer();
            for (byte valueOf : digest) {
                stringBuffer.append(String.format("%02X", new Object[]{Byte.valueOf(valueOf)}));
            }
            return stringBuffer.toString();
        } catch (Exception e) {
            return str.replaceAll("[^[a-z][A-Z][0-9][.][_]]", StatConstants.MTA_COOPERATION_TAG);
        }
    }

    public static String b(String str) {
        try {
            MessageDigest instance = MessageDigest.getInstance("MD5");
            instance.update(str.getBytes());
            byte[] digest = instance.digest();
            StringBuffer stringBuffer = new StringBuffer();
            for (byte b2 : digest) {
                stringBuffer.append(Integer.toHexString(b2 & 255));
            }
            return stringBuffer.toString();
        } catch (NoSuchAlgorithmException e) {
            Log.a(b, "getMD5 error", e);
            return StatConstants.MTA_COOPERATION_TAG;
        }
    }

    public static String a(File file) {
        byte[] bArr = new byte[1024];
        try {
            if (!file.isFile()) {
                return StatConstants.MTA_COOPERATION_TAG;
            }
            MessageDigest instance = MessageDigest.getInstance("MD5");
            FileInputStream fileInputStream = new FileInputStream(file);
            while (true) {
                int read = fileInputStream.read(bArr, 0, 1024);
                if (read == -1) {
                    fileInputStream.close();
                    return String.format("%1$032x", new Object[]{new BigInteger(1, instance.digest())});
                }
                instance.update(bArr, 0, read);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String a(Context context, long j) {
        String str = StatConstants.MTA_COOPERATION_TAG;
        if (j < 1000) {
            return ((int) j) + "B";
        }
        if (j < 1000000) {
            return new StringBuilder(String.valueOf(Math.round(((double) ((float) j)) / 1000.0d))).append("K").toString();
        }
        if (j < 1000000000) {
            return new StringBuilder(String.valueOf(new DecimalFormat("#0.0").format(((double) ((float) j)) / 1000000.0d))).append("M").toString();
        }
        return new StringBuilder(String.valueOf(new DecimalFormat("#0.00").format(((double) ((float) j)) / 1.0E9d))).append("G").toString();
    }

    public static String c(String str) {
        String str2 = StatConstants.MTA_COOPERATION_TAG;
        try {
            long longValue = Long.valueOf(str).longValue();
            if (longValue < 1024) {
                return ((int) longValue) + "B";
            }
            if (longValue < 1048576) {
                return new StringBuilder(String.valueOf(new DecimalFormat("#0.00").format(((double) ((float) longValue)) / 1024.0d))).append("K").toString();
            }
            if (longValue < 1073741824) {
                return new StringBuilder(String.valueOf(new DecimalFormat("#0.00").format(((double) ((float) longValue)) / 1048576.0d))).append("M").toString();
            }
            return new StringBuilder(String.valueOf(new DecimalFormat("#0.00").format(((double) ((float) longValue)) / 1.073741824E9d))).append("G").toString();
        } catch (NumberFormatException e) {
            return str;
        }
    }

    public static void a(Context context, String str) {
        context.startActivity(context.getPackageManager().getLaunchIntentForPackage(str));
    }

    public static boolean b(Context context, String str) {
        try {
            context.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(str)));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean d(String str) {
        return str == null || str.length() == 0;
    }

    public static boolean e(String str) {
        if (d(str)) {
            return false;
        }
        String lowerCase = str.trim().toLowerCase();
        if (lowerCase.startsWith("http://") || lowerCase.startsWith("https://")) {
            return true;
        }
        return false;
    }

    public static String a() {
        return a(new Date());
    }

    public static String a(Date date) {
        if (date == null) {
            return StatConstants.MTA_COOPERATION_TAG;
        }
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).format(date);
    }
}
