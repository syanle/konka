package com.umeng.common.util;

import java.math.BigInteger;

/* compiled from: Base64 */
public class c extends d {
    static final byte[] a = {13, 10};
    private static final int m = 6;
    private static final int n = 3;
    private static final int o = 4;
    private static final byte[] p = {65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 43, 47};
    private static final byte[] q = {65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 45, 95};
    private static final byte[] r;
    private static final int s = 63;
    private final byte[] t;
    private final byte[] u;
    private final byte[] v;
    private final int w;
    private final int x;
    private int y;

    static {
        byte[] bArr = new byte[123];
        bArr[0] = -1;
        bArr[1] = -1;
        bArr[2] = -1;
        bArr[3] = -1;
        bArr[4] = -1;
        bArr[5] = -1;
        bArr[6] = -1;
        bArr[7] = -1;
        bArr[8] = -1;
        bArr[9] = -1;
        bArr[10] = -1;
        bArr[11] = -1;
        bArr[12] = -1;
        bArr[13] = -1;
        bArr[14] = -1;
        bArr[15] = -1;
        bArr[16] = -1;
        bArr[17] = -1;
        bArr[18] = -1;
        bArr[19] = -1;
        bArr[20] = -1;
        bArr[21] = -1;
        bArr[22] = -1;
        bArr[23] = -1;
        bArr[24] = -1;
        bArr[25] = -1;
        bArr[26] = -1;
        bArr[27] = -1;
        bArr[28] = -1;
        bArr[29] = -1;
        bArr[30] = -1;
        bArr[31] = -1;
        bArr[32] = -1;
        bArr[33] = -1;
        bArr[34] = -1;
        bArr[35] = -1;
        bArr[36] = -1;
        bArr[37] = -1;
        bArr[38] = -1;
        bArr[39] = -1;
        bArr[40] = -1;
        bArr[41] = -1;
        bArr[42] = -1;
        bArr[43] = 62;
        bArr[44] = -1;
        bArr[45] = 62;
        bArr[46] = -1;
        bArr[47] = 63;
        bArr[48] = 52;
        bArr[49] = 53;
        bArr[50] = 54;
        bArr[51] = 55;
        bArr[52] = 56;
        bArr[53] = 57;
        bArr[54] = 58;
        bArr[55] = 59;
        bArr[56] = 60;
        bArr[57] = 61;
        bArr[58] = -1;
        bArr[59] = -1;
        bArr[60] = -1;
        bArr[61] = -1;
        bArr[62] = -1;
        bArr[s] = -1;
        bArr[64] = -1;
        bArr[66] = 1;
        bArr[67] = 2;
        bArr[68] = 3;
        bArr[69] = 4;
        bArr[70] = 5;
        bArr[71] = 6;
        bArr[72] = 7;
        bArr[73] = 8;
        bArr[74] = 9;
        bArr[75] = 10;
        bArr[76] = 11;
        bArr[77] = 12;
        bArr[78] = 13;
        bArr[79] = 14;
        bArr[80] = 15;
        bArr[81] = 16;
        bArr[82] = 17;
        bArr[83] = 18;
        bArr[84] = 19;
        bArr[85] = 20;
        bArr[86] = 21;
        bArr[87] = 22;
        bArr[88] = 23;
        bArr[89] = 24;
        bArr[90] = 25;
        bArr[91] = -1;
        bArr[92] = -1;
        bArr[93] = -1;
        bArr[94] = -1;
        bArr[95] = 63;
        bArr[96] = -1;
        bArr[97] = 26;
        bArr[98] = 27;
        bArr[99] = 28;
        bArr[100] = 29;
        bArr[101] = 30;
        bArr[102] = 31;
        bArr[103] = 32;
        bArr[104] = 33;
        bArr[105] = 34;
        bArr[106] = 35;
        bArr[107] = 36;
        bArr[108] = 37;
        bArr[109] = 38;
        bArr[110] = 39;
        bArr[111] = 40;
        bArr[112] = 41;
        bArr[113] = 42;
        bArr[114] = 43;
        bArr[115] = 44;
        bArr[116] = 45;
        bArr[117] = 46;
        bArr[118] = 47;
        bArr[119] = 48;
        bArr[120] = 49;
        bArr[121] = 50;
        bArr[122] = 51;
        r = bArr;
    }

    public c() {
        this(0);
    }

    public c(boolean z) {
        this(76, a, z);
    }

    public c(int i) {
        this(i, a);
    }

    public c(int i, byte[] bArr) {
        this(i, bArr, false);
    }

    public c(int i, byte[] bArr, boolean z) {
        int length;
        if (bArr == null) {
            length = 0;
        } else {
            length = bArr.length;
        }
        super(3, 4, i, length);
        this.u = r;
        if (bArr == null) {
            this.x = 4;
            this.v = null;
        } else if (n(bArr)) {
            throw new IllegalArgumentException("lineSeparator must not contain base64 characters: [" + a.f(bArr) + "]");
        } else if (i > 0) {
            this.x = bArr.length + 4;
            this.v = new byte[bArr.length];
            System.arraycopy(bArr, 0, this.v, 0, bArr.length);
        } else {
            this.x = 4;
            this.v = null;
        }
        this.w = this.x - 1;
        this.t = z ? q : p;
    }

    public boolean a() {
        return this.t == q;
    }

    /* JADX WARNING: type inference failed for: r0v34 */
    /* access modifiers changed from: 0000 */
    /* JADX WARNING: Incorrect type for immutable var: ssa=byte, code=int, for r0v5, types: [int, byte] */
    /* JADX WARNING: Multi-variable type inference failed */
    public void a(byte[] bArr, int i, int i2) {
        if (!this.j) {
            if (i2 < 0) {
                this.j = true;
                if (this.l != 0 || this.g != 0) {
                    a(this.x);
                    int i3 = this.i;
                    switch (this.l) {
                        case 1:
                            byte[] bArr2 = this.h;
                            int i4 = this.i;
                            this.i = i4 + 1;
                            bArr2[i4] = this.t[(this.y >> 2) & s];
                            byte[] bArr3 = this.h;
                            int i5 = this.i;
                            this.i = i5 + 1;
                            bArr3[i5] = this.t[(this.y << 4) & s];
                            if (this.t == p) {
                                byte[] bArr4 = this.h;
                                int i6 = this.i;
                                this.i = i6 + 1;
                                bArr4[i6] = 61;
                                byte[] bArr5 = this.h;
                                int i7 = this.i;
                                this.i = i7 + 1;
                                bArr5[i7] = 61;
                                break;
                            }
                            break;
                        case 2:
                            byte[] bArr6 = this.h;
                            int i8 = this.i;
                            this.i = i8 + 1;
                            bArr6[i8] = this.t[(this.y >> 10) & s];
                            byte[] bArr7 = this.h;
                            int i9 = this.i;
                            this.i = i9 + 1;
                            bArr7[i9] = this.t[(this.y >> 4) & s];
                            byte[] bArr8 = this.h;
                            int i10 = this.i;
                            this.i = i10 + 1;
                            bArr8[i10] = this.t[(this.y << 2) & s];
                            if (this.t == p) {
                                byte[] bArr9 = this.h;
                                int i11 = this.i;
                                this.i = i11 + 1;
                                bArr9[i11] = 61;
                                break;
                            }
                            break;
                    }
                    this.k = (this.i - i3) + this.k;
                    if (this.g > 0 && this.k > 0) {
                        System.arraycopy(this.v, 0, this.h, this.i, this.v.length);
                        this.i += this.v.length;
                        return;
                    }
                    return;
                }
                return;
            }
            int i12 = 0;
            while (i12 < i2) {
                a(this.x);
                this.l = (this.l + 1) % 3;
                int i13 = i + 1;
                int i14 = bArr[i];
                if (i14 < 0) {
                    i14 += g.b;
                }
                this.y = i14 + (this.y << 8);
                if (this.l == 0) {
                    byte[] bArr10 = this.h;
                    int i15 = this.i;
                    this.i = i15 + 1;
                    bArr10[i15] = this.t[(this.y >> 18) & s];
                    byte[] bArr11 = this.h;
                    int i16 = this.i;
                    this.i = i16 + 1;
                    bArr11[i16] = this.t[(this.y >> 12) & s];
                    byte[] bArr12 = this.h;
                    int i17 = this.i;
                    this.i = i17 + 1;
                    bArr12[i17] = this.t[(this.y >> 6) & s];
                    byte[] bArr13 = this.h;
                    int i18 = this.i;
                    this.i = i18 + 1;
                    bArr13[i18] = this.t[this.y & s];
                    this.k += 4;
                    if (this.g > 0 && this.g <= this.k) {
                        System.arraycopy(this.v, 0, this.h, this.i, this.v.length);
                        this.i += this.v.length;
                        this.k = 0;
                    }
                }
                i12++;
                i = i13;
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public void b(byte[] bArr, int i, int i2) {
        if (!this.j) {
            if (i2 < 0) {
                this.j = true;
            }
            int i3 = 0;
            while (true) {
                if (i3 >= i2) {
                    break;
                }
                a(this.w);
                int i4 = i + 1;
                byte b = bArr[i];
                if (b == 61) {
                    this.j = true;
                    break;
                }
                if (b >= 0 && b < r.length) {
                    byte b2 = r[b];
                    if (b2 >= 0) {
                        this.l = (this.l + 1) % 4;
                        this.y = b2 + (this.y << 6);
                        if (this.l == 0) {
                            byte[] bArr2 = this.h;
                            int i5 = this.i;
                            this.i = i5 + 1;
                            bArr2[i5] = (byte) ((this.y >> 16) & 255);
                            byte[] bArr3 = this.h;
                            int i6 = this.i;
                            this.i = i6 + 1;
                            bArr3[i6] = (byte) ((this.y >> 8) & 255);
                            byte[] bArr4 = this.h;
                            int i7 = this.i;
                            this.i = i7 + 1;
                            bArr4[i7] = (byte) (this.y & 255);
                        }
                    }
                }
                i3++;
                i = i4;
            }
            if (this.j && this.l != 0) {
                a(this.w);
                switch (this.l) {
                    case 2:
                        this.y >>= 4;
                        byte[] bArr5 = this.h;
                        int i8 = this.i;
                        this.i = i8 + 1;
                        bArr5[i8] = (byte) (this.y & 255);
                        return;
                    case 3:
                        this.y >>= 2;
                        byte[] bArr6 = this.h;
                        int i9 = this.i;
                        this.i = i9 + 1;
                        bArr6[i9] = (byte) ((this.y >> 8) & 255);
                        byte[] bArr7 = this.h;
                        int i10 = this.i;
                        this.i = i10 + 1;
                        bArr7[i10] = (byte) (this.y & 255);
                        return;
                    default:
                        return;
                }
            }
        }
    }

    public static boolean a(byte b) {
        return b == 61 || (b >= 0 && b < r.length && r[b] != -1);
    }

    public static boolean a(String str) {
        return b(a.f(str));
    }

    public static boolean a(byte[] bArr) {
        return b(bArr);
    }

    public static boolean b(byte[] bArr) {
        for (int i = 0; i < bArr.length; i++) {
            if (!a(bArr[i]) && !c(bArr[i])) {
                return false;
            }
        }
        return true;
    }

    public static byte[] c(byte[] bArr) {
        return a(bArr, false);
    }

    public static String d(byte[] bArr) {
        return a.f(a(bArr, false));
    }

    public static byte[] e(byte[] bArr) {
        return a(bArr, false, true);
    }

    public static String f(byte[] bArr) {
        return a.f(a(bArr, false, true));
    }

    public static byte[] g(byte[] bArr) {
        return a(bArr, true);
    }

    public static byte[] a(byte[] bArr, boolean z) {
        return a(bArr, z, false);
    }

    public static byte[] a(byte[] bArr, boolean z, boolean z2) {
        return a(bArr, z, z2, Integer.MAX_VALUE);
    }

    public static byte[] a(byte[] bArr, boolean z, boolean z2, int i) {
        if (bArr == null || bArr.length == 0) {
            return bArr;
        }
        c cVar = z ? new c(z2) : new c(0, a, z2);
        long o2 = cVar.o(bArr);
        if (o2 <= ((long) i)) {
            return cVar.l(bArr);
        }
        throw new IllegalArgumentException("Input array too big, the output array would be bigger (" + o2 + ") than the specified maximum size of " + i);
    }

    public static byte[] b(String str) {
        return new c().c(str);
    }

    public static byte[] h(byte[] bArr) {
        return new c().k(bArr);
    }

    public static BigInteger i(byte[] bArr) {
        return new BigInteger(1, h(bArr));
    }

    public static byte[] a(BigInteger bigInteger) {
        if (bigInteger != null) {
            return a(b(bigInteger), false);
        }
        throw new NullPointerException("encodeInteger called with null parameter");
    }

    static byte[] b(BigInteger bigInteger) {
        int bitLength = ((bigInteger.bitLength() + 7) >> 3) << 3;
        byte[] byteArray = bigInteger.toByteArray();
        if (bigInteger.bitLength() % 8 != 0 && (bigInteger.bitLength() / 8) + 1 == bitLength / 8) {
            return byteArray;
        }
        int i = 0;
        int length = byteArray.length;
        if (bigInteger.bitLength() % 8 == 0) {
            i = 1;
            length--;
        }
        byte[] bArr = new byte[(bitLength / 8)];
        System.arraycopy(byteArray, i, bArr, (bitLength / 8) - length, length);
        return bArr;
    }

    /* access modifiers changed from: protected */
    public boolean b(byte b) {
        return b >= 0 && b < this.u.length && this.u[b] != -1;
    }
}
