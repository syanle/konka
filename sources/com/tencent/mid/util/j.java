package com.tencent.mid.util;

import com.tencent.stat.common.StatConstants;

public class j {
    public static String a(byte[] bArr) {
        StringBuilder sb = new StringBuilder(StatConstants.MTA_COOPERATION_TAG);
        if (bArr == null || bArr.length <= 0) {
            return null;
        }
        for (byte b : bArr) {
            String hexString = Integer.toHexString(b & 255);
            if (hexString.length() < 2) {
                sb.append(0);
            }
            sb.append(hexString);
        }
        return sb.toString().toUpperCase();
    }
}
