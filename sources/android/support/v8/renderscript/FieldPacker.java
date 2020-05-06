package android.support.v8.renderscript;

import android.renderscript.Byte2;
import android.renderscript.Byte3;
import android.renderscript.Byte4;
import android.renderscript.Double2;
import android.renderscript.Double3;
import android.renderscript.Double4;
import android.renderscript.Float2;
import android.renderscript.Float3;
import android.renderscript.Float4;
import android.renderscript.Int2;
import android.renderscript.Int3;
import android.renderscript.Int4;
import android.renderscript.Long2;
import android.renderscript.Long3;
import android.renderscript.Long4;
import android.renderscript.Matrix2f;
import android.renderscript.Matrix3f;
import android.renderscript.Matrix4f;
import android.renderscript.Short2;
import android.renderscript.Short3;
import android.renderscript.Short4;
import android.util.Log;

public class FieldPacker {
    private final byte[] mData;
    private int mLen;
    private android.renderscript.FieldPacker mN;
    private int mPos = 0;

    public FieldPacker(int len) {
        this.mLen = len;
        this.mData = new byte[len];
        if (RenderScript.shouldThunk()) {
            this.mN = new android.renderscript.FieldPacker(len);
        }
    }

    public void align(int v) {
        if (RenderScript.shouldThunk()) {
            this.mN.align(v);
        } else if (v <= 0 || ((v - 1) & v) != 0) {
            throw new RSIllegalArgumentException("argument must be a non-negative non-zero power of 2: " + v);
        } else {
            while ((this.mPos & (v - 1)) != 0) {
                byte[] bArr = this.mData;
                int i = this.mPos;
                this.mPos = i + 1;
                bArr[i] = 0;
            }
        }
    }

    public void reset() {
        if (RenderScript.shouldThunk()) {
            this.mN.reset();
        } else {
            this.mPos = 0;
        }
    }

    public void reset(int i) {
        if (RenderScript.shouldThunk()) {
            this.mN.reset(i);
        } else if (i < 0 || i >= this.mLen) {
            throw new RSIllegalArgumentException("out of range argument: " + i);
        } else {
            this.mPos = i;
        }
    }

    public void skip(int i) {
        if (RenderScript.shouldThunk()) {
            this.mN.skip(i);
            return;
        }
        int res = this.mPos + i;
        if (res < 0 || res > this.mLen) {
            throw new RSIllegalArgumentException("out of range argument: " + i);
        }
        this.mPos = res;
    }

    public void addI8(byte v) {
        if (RenderScript.shouldThunk()) {
            this.mN.addI8(v);
            return;
        }
        byte[] bArr = this.mData;
        int i = this.mPos;
        this.mPos = i + 1;
        bArr[i] = v;
    }

    public void addI16(short v) {
        if (RenderScript.shouldThunk()) {
            this.mN.addI16(v);
            return;
        }
        align(2);
        byte[] bArr = this.mData;
        int i = this.mPos;
        this.mPos = i + 1;
        bArr[i] = (byte) (v & 255);
        byte[] bArr2 = this.mData;
        int i2 = this.mPos;
        this.mPos = i2 + 1;
        bArr2[i2] = (byte) (v >> 8);
    }

    public void addI32(int v) {
        if (RenderScript.shouldThunk()) {
            this.mN.addI32(v);
            return;
        }
        align(4);
        byte[] bArr = this.mData;
        int i = this.mPos;
        this.mPos = i + 1;
        bArr[i] = (byte) (v & 255);
        byte[] bArr2 = this.mData;
        int i2 = this.mPos;
        this.mPos = i2 + 1;
        bArr2[i2] = (byte) ((v >> 8) & 255);
        byte[] bArr3 = this.mData;
        int i3 = this.mPos;
        this.mPos = i3 + 1;
        bArr3[i3] = (byte) ((v >> 16) & 255);
        byte[] bArr4 = this.mData;
        int i4 = this.mPos;
        this.mPos = i4 + 1;
        bArr4[i4] = (byte) ((v >> 24) & 255);
    }

    public void addI64(long v) {
        if (RenderScript.shouldThunk()) {
            this.mN.addI64(v);
            return;
        }
        align(8);
        byte[] bArr = this.mData;
        int i = this.mPos;
        this.mPos = i + 1;
        bArr[i] = (byte) ((int) (v & 255));
        byte[] bArr2 = this.mData;
        int i2 = this.mPos;
        this.mPos = i2 + 1;
        bArr2[i2] = (byte) ((int) ((v >> 8) & 255));
        byte[] bArr3 = this.mData;
        int i3 = this.mPos;
        this.mPos = i3 + 1;
        bArr3[i3] = (byte) ((int) ((v >> 16) & 255));
        byte[] bArr4 = this.mData;
        int i4 = this.mPos;
        this.mPos = i4 + 1;
        bArr4[i4] = (byte) ((int) ((v >> 24) & 255));
        byte[] bArr5 = this.mData;
        int i5 = this.mPos;
        this.mPos = i5 + 1;
        bArr5[i5] = (byte) ((int) ((v >> 32) & 255));
        byte[] bArr6 = this.mData;
        int i6 = this.mPos;
        this.mPos = i6 + 1;
        bArr6[i6] = (byte) ((int) ((v >> 40) & 255));
        byte[] bArr7 = this.mData;
        int i7 = this.mPos;
        this.mPos = i7 + 1;
        bArr7[i7] = (byte) ((int) ((v >> 48) & 255));
        byte[] bArr8 = this.mData;
        int i8 = this.mPos;
        this.mPos = i8 + 1;
        bArr8[i8] = (byte) ((int) ((v >> 56) & 255));
    }

    public void addU8(short v) {
        if (RenderScript.shouldThunk()) {
            this.mN.addU8(v);
        } else if (v < 0 || v > 255) {
            throw new IllegalArgumentException("Saving value out of range for type");
        } else {
            byte[] bArr = this.mData;
            int i = this.mPos;
            this.mPos = i + 1;
            bArr[i] = (byte) v;
        }
    }

    public void addU16(int v) {
        if (RenderScript.shouldThunk()) {
            this.mN.addU16(v);
        } else if (v < 0 || v > 65535) {
            Log.e("rs", "FieldPacker.addU16( " + v + " )");
            throw new IllegalArgumentException("Saving value out of range for type");
        } else {
            align(2);
            byte[] bArr = this.mData;
            int i = this.mPos;
            this.mPos = i + 1;
            bArr[i] = (byte) (v & 255);
            byte[] bArr2 = this.mData;
            int i2 = this.mPos;
            this.mPos = i2 + 1;
            bArr2[i2] = (byte) (v >> 8);
        }
    }

    public void addU32(long v) {
        if (RenderScript.shouldThunk()) {
            this.mN.addU32(v);
        } else if (v < 0 || v > 4294967295L) {
            Log.e("rs", "FieldPacker.addU32( " + v + " )");
            throw new IllegalArgumentException("Saving value out of range for type");
        } else {
            align(4);
            byte[] bArr = this.mData;
            int i = this.mPos;
            this.mPos = i + 1;
            bArr[i] = (byte) ((int) (v & 255));
            byte[] bArr2 = this.mData;
            int i2 = this.mPos;
            this.mPos = i2 + 1;
            bArr2[i2] = (byte) ((int) ((v >> 8) & 255));
            byte[] bArr3 = this.mData;
            int i3 = this.mPos;
            this.mPos = i3 + 1;
            bArr3[i3] = (byte) ((int) ((v >> 16) & 255));
            byte[] bArr4 = this.mData;
            int i4 = this.mPos;
            this.mPos = i4 + 1;
            bArr4[i4] = (byte) ((int) ((v >> 24) & 255));
        }
    }

    public void addU64(long v) {
        if (RenderScript.shouldThunk()) {
            this.mN.addU64(v);
        } else if (v < 0) {
            Log.e("rs", "FieldPacker.addU64( " + v + " )");
            throw new IllegalArgumentException("Saving value out of range for type");
        } else {
            align(8);
            byte[] bArr = this.mData;
            int i = this.mPos;
            this.mPos = i + 1;
            bArr[i] = (byte) ((int) (v & 255));
            byte[] bArr2 = this.mData;
            int i2 = this.mPos;
            this.mPos = i2 + 1;
            bArr2[i2] = (byte) ((int) ((v >> 8) & 255));
            byte[] bArr3 = this.mData;
            int i3 = this.mPos;
            this.mPos = i3 + 1;
            bArr3[i3] = (byte) ((int) ((v >> 16) & 255));
            byte[] bArr4 = this.mData;
            int i4 = this.mPos;
            this.mPos = i4 + 1;
            bArr4[i4] = (byte) ((int) ((v >> 24) & 255));
            byte[] bArr5 = this.mData;
            int i5 = this.mPos;
            this.mPos = i5 + 1;
            bArr5[i5] = (byte) ((int) ((v >> 32) & 255));
            byte[] bArr6 = this.mData;
            int i6 = this.mPos;
            this.mPos = i6 + 1;
            bArr6[i6] = (byte) ((int) ((v >> 40) & 255));
            byte[] bArr7 = this.mData;
            int i7 = this.mPos;
            this.mPos = i7 + 1;
            bArr7[i7] = (byte) ((int) ((v >> 48) & 255));
            byte[] bArr8 = this.mData;
            int i8 = this.mPos;
            this.mPos = i8 + 1;
            bArr8[i8] = (byte) ((int) ((v >> 56) & 255));
        }
    }

    public void addF32(float v) {
        if (RenderScript.shouldThunk()) {
            this.mN.addF32(v);
        } else {
            addI32(Float.floatToRawIntBits(v));
        }
    }

    public void addF64(double v) {
        if (RenderScript.shouldThunk()) {
            this.mN.addF64(v);
        } else {
            addI64(Double.doubleToRawLongBits(v));
        }
    }

    public void addObj(BaseObj obj) {
        if (RenderScript.shouldThunk()) {
            if (obj != null) {
                this.mN.addObj(obj.getNObj());
            } else {
                this.mN.addObj(null);
            }
        } else if (obj != null) {
            addI32(obj.getID(null));
        } else {
            addI32(0);
        }
    }

    public void addF32(Float2 v) {
        if (RenderScript.shouldThunk()) {
            this.mN.addF32(new Float2(v.x, v.y));
            return;
        }
        addF32(v.x);
        addF32(v.y);
    }

    public void addF32(Float3 v) {
        if (RenderScript.shouldThunk()) {
            this.mN.addF32(new Float3(v.x, v.y, v.z));
            return;
        }
        addF32(v.x);
        addF32(v.y);
        addF32(v.z);
    }

    public void addF32(Float4 v) {
        if (RenderScript.shouldThunk()) {
            this.mN.addF32(new Float4(v.x, v.y, v.z, v.w));
            return;
        }
        addF32(v.x);
        addF32(v.y);
        addF32(v.z);
        addF32(v.w);
    }

    public void addF64(Double2 v) {
        if (RenderScript.shouldThunk()) {
            this.mN.addF64(new Double2(v.x, v.y));
            return;
        }
        addF64(v.x);
        addF64(v.y);
    }

    public void addF64(Double3 v) {
        if (RenderScript.shouldThunk()) {
            this.mN.addF64(new Double3(v.x, v.y, v.z));
            return;
        }
        addF64(v.x);
        addF64(v.y);
        addF64(v.z);
    }

    public void addF64(Double4 v) {
        if (RenderScript.shouldThunk()) {
            this.mN.addF64(new Double4(v.x, v.y, v.z, v.w));
            return;
        }
        addF64(v.x);
        addF64(v.y);
        addF64(v.z);
        addF64(v.w);
    }

    public void addI8(Byte2 v) {
        if (RenderScript.shouldThunk()) {
            this.mN.addI8(new Byte2(v.x, v.y));
            return;
        }
        addI8(v.x);
        addI8(v.y);
    }

    public void addI8(Byte3 v) {
        if (RenderScript.shouldThunk()) {
            this.mN.addI8(new Byte3(v.x, v.y, v.z));
            return;
        }
        addI8(v.x);
        addI8(v.y);
        addI8(v.z);
    }

    public void addI8(Byte4 v) {
        if (RenderScript.shouldThunk()) {
            this.mN.addI8(new Byte4(v.x, v.y, v.z, v.w));
            return;
        }
        addI8(v.x);
        addI8(v.y);
        addI8(v.z);
        addI8(v.w);
    }

    public void addU8(Short2 v) {
        if (RenderScript.shouldThunk()) {
            this.mN.addU8(new Short2(v.x, v.y));
            return;
        }
        addU8(v.x);
        addU8(v.y);
    }

    public void addU8(Short3 v) {
        if (RenderScript.shouldThunk()) {
            this.mN.addU8(new Short3(v.x, v.y, v.z));
            return;
        }
        addU8(v.x);
        addU8(v.y);
        addU8(v.z);
    }

    public void addU8(Short4 v) {
        if (RenderScript.shouldThunk()) {
            this.mN.addU8(new Short4(v.x, v.y, v.z, v.w));
            return;
        }
        addU8(v.x);
        addU8(v.y);
        addU8(v.z);
        addU8(v.w);
    }

    public void addI16(Short2 v) {
        if (RenderScript.shouldThunk()) {
            this.mN.addI16(new Short2(v.x, v.y));
            return;
        }
        addI16(v.x);
        addI16(v.y);
    }

    public void addI16(Short3 v) {
        if (RenderScript.shouldThunk()) {
            this.mN.addI16(new Short3(v.x, v.y, v.z));
            return;
        }
        addI16(v.x);
        addI16(v.y);
        addI16(v.z);
    }

    public void addI16(Short4 v) {
        if (RenderScript.shouldThunk()) {
            this.mN.addI16(new Short4(v.x, v.y, v.z, v.w));
            return;
        }
        addI16(v.x);
        addI16(v.y);
        addI16(v.z);
        addI16(v.w);
    }

    public void addU16(Int2 v) {
        if (RenderScript.shouldThunk()) {
            this.mN.addU16(new Int2(v.x, v.y));
            return;
        }
        addU16(v.x);
        addU16(v.y);
    }

    public void addU16(Int3 v) {
        if (RenderScript.shouldThunk()) {
            this.mN.addU16(new Int3(v.x, v.y, v.z));
            return;
        }
        addU16(v.x);
        addU16(v.y);
        addU16(v.z);
    }

    public void addU16(Int4 v) {
        if (RenderScript.shouldThunk()) {
            this.mN.addU16(new Int4(v.x, v.y, v.z, v.w));
            return;
        }
        addU16(v.x);
        addU16(v.y);
        addU16(v.z);
        addU16(v.w);
    }

    public void addI32(Int2 v) {
        if (RenderScript.shouldThunk()) {
            this.mN.addI32(new Int2(v.x, v.y));
            return;
        }
        addI32(v.x);
        addI32(v.y);
    }

    public void addI32(Int3 v) {
        if (RenderScript.shouldThunk()) {
            this.mN.addI32(new Int3(v.x, v.y, v.z));
            return;
        }
        addI32(v.x);
        addI32(v.y);
        addI32(v.z);
    }

    public void addI32(Int4 v) {
        if (RenderScript.shouldThunk()) {
            this.mN.addI32(new Int4(v.x, v.y, v.z, v.w));
            return;
        }
        addI32(v.x);
        addI32(v.y);
        addI32(v.z);
        addI32(v.w);
    }

    public void addU32(Long2 v) {
        if (RenderScript.shouldThunk()) {
            this.mN.addU32(new Long2(v.x, v.y));
            return;
        }
        addU32(v.x);
        addU32(v.y);
    }

    public void addU32(Long3 v) {
        if (RenderScript.shouldThunk()) {
            this.mN.addU32(new Long3(v.x, v.y, v.z));
            return;
        }
        addU32(v.x);
        addU32(v.y);
        addU32(v.z);
    }

    public void addU32(Long4 v) {
        if (RenderScript.shouldThunk()) {
            this.mN.addU32(new Long4(v.x, v.y, v.z, v.w));
            return;
        }
        addU32(v.x);
        addU32(v.y);
        addU32(v.z);
        addU32(v.w);
    }

    public void addI64(Long2 v) {
        if (RenderScript.shouldThunk()) {
            this.mN.addI64(new Long2(v.x, v.y));
            return;
        }
        addI64(v.x);
        addI64(v.y);
    }

    public void addI64(Long3 v) {
        if (RenderScript.shouldThunk()) {
            this.mN.addI64(new Long3(v.x, v.y, v.z));
            return;
        }
        addI64(v.x);
        addI64(v.y);
        addI64(v.z);
    }

    public void addI64(Long4 v) {
        if (RenderScript.shouldThunk()) {
            this.mN.addI64(new Long4(v.x, v.y, v.z, v.w));
            return;
        }
        addI64(v.x);
        addI64(v.y);
        addI64(v.z);
        addI64(v.w);
    }

    public void addU64(Long2 v) {
        if (RenderScript.shouldThunk()) {
            this.mN.addU64(new Long2(v.x, v.y));
            return;
        }
        addU64(v.x);
        addU64(v.y);
    }

    public void addU64(Long3 v) {
        if (RenderScript.shouldThunk()) {
            this.mN.addU64(new Long3(v.x, v.y, v.z));
            return;
        }
        addU64(v.x);
        addU64(v.y);
        addU64(v.z);
    }

    public void addU64(Long4 v) {
        if (RenderScript.shouldThunk()) {
            this.mN.addU64(new Long4(v.x, v.y, v.z, v.w));
            return;
        }
        addU64(v.x);
        addU64(v.y);
        addU64(v.z);
        addU64(v.w);
    }

    public void addMatrix(Matrix4f v) {
        if (RenderScript.shouldThunk()) {
            this.mN.addMatrix(new Matrix4f(v.getArray()));
            return;
        }
        for (float addF32 : v.mMat) {
            addF32(addF32);
        }
    }

    public void addMatrix(Matrix3f v) {
        if (RenderScript.shouldThunk()) {
            this.mN.addMatrix(new Matrix3f(v.getArray()));
            return;
        }
        for (float addF32 : v.mMat) {
            addF32(addF32);
        }
    }

    public void addMatrix(Matrix2f v) {
        if (RenderScript.shouldThunk()) {
            this.mN.addMatrix(new Matrix2f(v.getArray()));
            return;
        }
        for (float addF32 : v.mMat) {
            addF32(addF32);
        }
    }

    public void addBoolean(boolean v) {
        if (RenderScript.shouldThunk()) {
            this.mN.addBoolean(v);
        } else {
            addI8((byte) (v ? 1 : 0));
        }
    }

    public final byte[] getData() {
        if (RenderScript.shouldThunk()) {
            return this.mN.getData();
        }
        return this.mData;
    }
}
