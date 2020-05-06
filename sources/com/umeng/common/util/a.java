package com.umeng.common.util;

import java.io.UnsupportedEncodingException;

/* compiled from: AESStringUtils */
public class a {
    public static byte[] a(String str) {
        return a(str, e.a);
    }

    public static byte[] b(String str) {
        return a(str, e.b);
    }

    public static byte[] c(String str) {
        return a(str, e.c);
    }

    public static byte[] d(String str) {
        return a(str, e.d);
    }

    public static byte[] e(String str) {
        return a(str, e.e);
    }

    public static byte[] f(String str) {
        return a(str, e.f);
    }

    public static byte[] a(String str, String str2) {
        if (str == null) {
            return null;
        }
        try {
            return str.getBytes(str2);
        } catch (UnsupportedEncodingException e) {
            throw a(str2, e);
        }
    }

    private static IllegalStateException a(String str, UnsupportedEncodingException unsupportedEncodingException) {
        return new IllegalStateException(new StringBuilder(String.valueOf(str)).append(": ").append(unsupportedEncodingException).toString());
    }

    public static String a(byte[] bArr, String str) {
        if (bArr == null) {
            return null;
        }
        try {
            return new String(bArr, str);
        } catch (UnsupportedEncodingException e) {
            throw a(str, e);
        }
    }

    public static String a(byte[] bArr) {
        return a(bArr, e.a);
    }

    public static String b(byte[] bArr) {
        return a(bArr, e.b);
    }

    public static String c(byte[] bArr) {
        return a(bArr, e.c);
    }

    public static String d(byte[] bArr) {
        return a(bArr, e.d);
    }

    public static String e(byte[] bArr) {
        return a(bArr, e.e);
    }

    public static String f(byte[] bArr) {
        return a(bArr, e.f);
    }
}
