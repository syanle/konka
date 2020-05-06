package com.tencent.mid.util;

class d extends b {
    static final /* synthetic */ boolean g = (!a.class.desiredAssertionStatus());
    private static final byte[] h = {65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 43, 47};
    private static final byte[] i = {65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 45, 95};
    int c;
    public final boolean d;
    public final boolean e;
    public final boolean f;
    private final byte[] j;
    private int k;
    private final byte[] l;

    public d(int i2, byte[] bArr) {
        boolean z = true;
        this.a = bArr;
        this.d = (i2 & 1) == 0;
        this.e = (i2 & 2) == 0;
        if ((i2 & 4) == 0) {
            z = false;
        }
        this.f = z;
        this.l = (i2 & 8) == 0 ? h : i;
        this.j = new byte[2];
        this.c = 0;
        this.k = this.e ? 19 : -1;
    }

    public boolean a(byte[] bArr, int i2, int i3, boolean z) {
        int i4;
        int i5;
        int i6;
        int i7;
        byte b;
        int i8;
        byte b2;
        int i9;
        byte b3;
        int i10;
        int i11;
        int i12;
        int i13;
        byte[] bArr2 = this.l;
        byte[] bArr3 = this.a;
        int i14 = 0;
        int i15 = this.k;
        int i16 = i3 + i2;
        byte b4 = -1;
        switch (this.c) {
            case 0:
                i4 = i2;
                break;
            case 1:
                if (i2 + 2 <= i16) {
                    int i17 = i2 + 1;
                    int i18 = i17 + 1;
                    b4 = ((this.j[0] & 255) << 16) | ((bArr[i2] & 255) << 8) | (bArr[i17] & 255);
                    this.c = 0;
                    i4 = i18;
                    break;
                }
            case 2:
                if (i2 + 1 <= i16) {
                    i4 = i2 + 1;
                    b4 = ((this.j[0] & 255) << 16) | ((this.j[1] & 255) << 8) | (bArr[i2] & 255);
                    this.c = 0;
                    break;
                }
            default:
                i4 = i2;
                break;
        }
        if (b4 != -1) {
            bArr3[0] = bArr2[(b4 >> 18) & 63];
            bArr3[1] = bArr2[(b4 >> 12) & 63];
            bArr3[2] = bArr2[(b4 >> 6) & 63];
            i14 = 4;
            bArr3[3] = bArr2[b4 & 63];
            i15--;
            if (i15 == 0) {
                if (this.f) {
                    i13 = 5;
                    bArr3[4] = 13;
                } else {
                    i13 = 4;
                }
                i14 = i13 + 1;
                bArr3[i13] = 10;
                i15 = 19;
            }
        }
        while (true) {
            int i19 = i6;
            int i20 = i5;
            if (i4 + 3 <= i16) {
                byte b5 = ((bArr[i4] & 255) << 16) | ((bArr[i4 + 1] & 255) << 8) | (bArr[i4 + 2] & 255);
                bArr3[i20] = bArr2[(b5 >> 18) & 63];
                bArr3[i20 + 1] = bArr2[(b5 >> 12) & 63];
                bArr3[i20 + 2] = bArr2[(b5 >> 6) & 63];
                bArr3[i20 + 3] = bArr2[b5 & 63];
                i4 += 3;
                i5 = i20 + 4;
                i6 = i19 - 1;
                if (i6 == 0) {
                    if (this.f) {
                        i12 = i5 + 1;
                        bArr3[i5] = 13;
                    } else {
                        i12 = i5;
                    }
                    i5 = i12 + 1;
                    bArr3[i12] = 10;
                    i6 = 19;
                }
            } else {
                if (z) {
                    if (i4 - this.c == i16 - 1) {
                        if (this.c > 0) {
                            i11 = 1;
                            b3 = this.j[0];
                            i10 = i4;
                        } else {
                            int i21 = i4 + 1;
                            b3 = bArr[i4];
                            i10 = i21;
                            i11 = 0;
                        }
                        int i22 = (b3 & 255) << 4;
                        this.c -= i11;
                        int i23 = i20 + 1;
                        bArr3[i20] = bArr2[(i22 >> 6) & 63];
                        int i24 = i23 + 1;
                        bArr3[i23] = bArr2[i22 & 63];
                        if (this.d) {
                            int i25 = i24 + 1;
                            bArr3[i24] = 61;
                            i24 = i25 + 1;
                            bArr3[i25] = 61;
                        }
                        if (this.e) {
                            if (this.f) {
                                int i26 = i24 + 1;
                                bArr3[i24] = 13;
                                i24 = i26;
                            }
                            int i27 = i24 + 1;
                            bArr3[i24] = 10;
                            i24 = i27;
                        }
                        i4 = i10;
                        i20 = i24;
                    } else if (i4 - this.c == i16 - 2) {
                        if (this.c > 1) {
                            i8 = 1;
                            b = this.j[0];
                        } else {
                            int i28 = i4 + 1;
                            b = bArr[i4];
                            i4 = i28;
                            i8 = 0;
                        }
                        int i29 = (b & 255) << 10;
                        if (this.c > 0) {
                            int i30 = i8 + 1;
                            b2 = this.j[i8];
                            i8 = i30;
                        } else {
                            int i31 = i4 + 1;
                            b2 = bArr[i4];
                            i4 = i31;
                        }
                        int i32 = ((b2 & 255) << 2) | i29;
                        this.c -= i8;
                        int i33 = i20 + 1;
                        bArr3[i20] = bArr2[(i32 >> 12) & 63];
                        int i34 = i33 + 1;
                        bArr3[i33] = bArr2[(i32 >> 6) & 63];
                        int i35 = i34 + 1;
                        bArr3[i34] = bArr2[i32 & 63];
                        if (this.d) {
                            i9 = i35 + 1;
                            bArr3[i35] = 61;
                        } else {
                            i9 = i35;
                        }
                        if (this.e) {
                            if (this.f) {
                                int i36 = i9 + 1;
                                bArr3[i9] = 13;
                                i9 = i36;
                            }
                            int i37 = i9 + 1;
                            bArr3[i9] = 10;
                            i9 = i37;
                        }
                        i20 = i9;
                    } else if (this.e && i20 > 0 && i19 != 19) {
                        if (this.f) {
                            i7 = i20 + 1;
                            bArr3[i20] = 13;
                        } else {
                            i7 = i20;
                        }
                        i20 = i7 + 1;
                        bArr3[i7] = 10;
                    }
                    if (!g && this.c != 0) {
                        throw new AssertionError();
                    } else if (!g && i4 != i16) {
                        throw new AssertionError();
                    }
                } else if (i4 == i16 - 1) {
                    byte[] bArr4 = this.j;
                    int i38 = this.c;
                    this.c = i38 + 1;
                    bArr4[i38] = bArr[i4];
                } else if (i4 == i16 - 2) {
                    byte[] bArr5 = this.j;
                    int i39 = this.c;
                    this.c = i39 + 1;
                    bArr5[i39] = bArr[i4];
                    byte[] bArr6 = this.j;
                    int i40 = this.c;
                    this.c = i40 + 1;
                    bArr6[i40] = bArr[i4 + 1];
                }
                this.b = i20;
                this.k = i19;
                return true;
            }
        }
    }
}
