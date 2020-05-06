package com.tencent.mid.util;

public class a {
    static final /* synthetic */ boolean a = (!a.class.desiredAssertionStatus());

    private a() {
    }

    public static byte[] a(byte[] bArr, int i) {
        return a(bArr, 0, bArr.length, i);
    }

    public static byte[] a(byte[] bArr, int i, int i2, int i3) {
        c cVar = new c(i3, new byte[((i2 * 3) / 4)]);
        if (!cVar.a(bArr, i, i2, true)) {
            throw new IllegalArgumentException("bad base-64");
        } else if (cVar.b == cVar.a.length) {
            return cVar.a;
        } else {
            byte[] bArr2 = new byte[cVar.b];
            System.arraycopy(cVar.a, 0, bArr2, 0, cVar.b);
            return bArr2;
        }
    }

    public static byte[] b(byte[] bArr, int i) {
        return b(bArr, 0, bArr.length, i);
    }

    public static byte[] b(byte[] bArr, int i, int i2, int i3) {
        d dVar = new d(i3, null);
        int i4 = (i2 / 3) * 4;
        if (!dVar.d) {
            switch (i2 % 3) {
                case 1:
                    i4 += 2;
                    break;
                case 2:
                    i4 += 3;
                    break;
            }
        } else if (i2 % 3 > 0) {
            i4 += 4;
        }
        if (dVar.e && i2 > 0) {
            i4 += (dVar.f ? 2 : 1) * (((i2 - 1) / 57) + 1);
        }
        dVar.a = new byte[i4];
        dVar.a(bArr, i, i2, true);
        if (a || dVar.b == i4) {
            return dVar.a;
        }
        throw new AssertionError();
    }
}
