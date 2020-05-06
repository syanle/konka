package com.tencent.stat.common;

import com.umeng.common.util.g;

public class f {
    static final byte[] a = "03a976511e2cbe3a7f26808fb7af3c05".getBytes();

    public static byte[] a(byte[] bArr) {
        return a(bArr, a);
    }

    /* JADX WARNING: type inference failed for: r9v0, types: [byte[]] */
    /* JADX WARNING: type inference failed for: r5v13, types: [byte] */
    /* JADX WARNING: Incorrect type for immutable var: ssa=byte, code=null, for r5v13, types: [byte] */
    /* JADX WARNING: Incorrect type for immutable var: ssa=byte[], code=null, for r9v0, types: [byte[]] */
    /* JADX WARNING: Unknown variable types count: 2 */
    static byte[] a(byte[] bArr, byte[] r9) {
        int[] iArr = new int[g.b];
        int[] iArr2 = new int[g.b];
        int length = r9.length;
        if (length < 1 || length > 256) {
            throw new IllegalArgumentException("key must be between 1 and 256 bytes");
        }
        for (int i = 0; i < 256; i++) {
            iArr[i] = i;
            iArr2[i] = r9[i % length];
        }
        int i2 = 0;
        for (int i3 = 0; i3 < 256; i3++) {
            i2 = (i2 + iArr[i3] + iArr2[i3]) & 255;
            int i4 = iArr[i3];
            iArr[i3] = iArr[i2];
            iArr[i2] = i4;
        }
        byte[] bArr2 = new byte[bArr.length];
        int i5 = 0;
        int i6 = 0;
        for (int i7 = 0; i7 < bArr.length; i7++) {
            i6 = (i6 + 1) & 255;
            i5 = (i5 + iArr[i6]) & 255;
            int i8 = iArr[i6];
            iArr[i6] = iArr[i5];
            iArr[i5] = i8;
            bArr2[i7] = (byte) (iArr[(iArr[i6] + iArr[i5]) & 255] ^ bArr[i7]);
        }
        return bArr2;
    }

    public static byte[] b(byte[] bArr) {
        return b(bArr, a);
    }

    static byte[] b(byte[] bArr, byte[] bArr2) {
        return a(bArr, bArr2);
    }
}
