package android.support.v8.renderscript;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.support.v8.renderscript.Element.DataKind;
import android.support.v8.renderscript.Element.DataType;
import android.support.v8.renderscript.Type.Builder;
import android.support.v8.renderscript.Type.CubemapFace;
import android.util.Log;
import com.cyanogenmod.trebuchet.RocketLauncher.Board.FlyingIcon;
import com.umeng.common.util.e;

public class Allocation extends BaseObj {
    public static final int USAGE_GRAPHICS_TEXTURE = 2;
    public static final int USAGE_IO_INPUT = 32;
    public static final int USAGE_IO_OUTPUT = 64;
    public static final int USAGE_SCRIPT = 1;
    public static final int USAGE_SHARED = 128;
    static Options mBitmapOptions = new Options();
    Allocation mAdaptedAllocation;
    Bitmap mBitmap;
    boolean mConstrainedFace;
    boolean mConstrainedLOD;
    boolean mConstrainedY;
    boolean mConstrainedZ;
    int mCurrentCount;
    int mCurrentDimX;
    int mCurrentDimY;
    int mCurrentDimZ;
    boolean mReadAllowed = true;
    CubemapFace mSelectedFace = CubemapFace.POSITIVE_X;
    int mSelectedLOD;
    int mSelectedY;
    int mSelectedZ;
    int mSize;
    Type mType;
    int mUsage;
    boolean mWriteAllowed = true;

    /* renamed from: android.support.v8.renderscript.Allocation$1 reason: invalid class name */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$android$graphics$Bitmap$Config = new int[Config.values().length];

        static {
            try {
                $SwitchMap$android$graphics$Bitmap$Config[Config.ALPHA_8.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$android$graphics$Bitmap$Config[Config.ARGB_8888.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$android$graphics$Bitmap$Config[Config.RGB_565.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$android$graphics$Bitmap$Config[Config.ARGB_4444.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
        }
    }

    public enum MipmapControl {
        MIPMAP_NONE(0),
        MIPMAP_FULL(1),
        MIPMAP_ON_SYNC_TO_TEXTURE(2);
        
        int mID;

        private MipmapControl(int id) {
            this.mID = id;
        }
    }

    private int getIDSafe() {
        if (this.mAdaptedAllocation != null) {
            return this.mAdaptedAllocation.getID(this.mRS);
        }
        return getID(this.mRS);
    }

    public Element getElement() {
        return this.mType.getElement();
    }

    public int getUsage() {
        return this.mUsage;
    }

    public int getBytesSize() {
        return this.mType.getCount() * this.mType.getElement().getBytesSize();
    }

    private void updateCacheInfo(Type t) {
        this.mCurrentDimX = t.getX();
        this.mCurrentDimY = t.getY();
        this.mCurrentDimZ = t.getZ();
        this.mCurrentCount = this.mCurrentDimX;
        if (this.mCurrentDimY > 1) {
            this.mCurrentCount *= this.mCurrentDimY;
        }
        if (this.mCurrentDimZ > 1) {
            this.mCurrentCount *= this.mCurrentDimZ;
        }
    }

    private void setBitmap(Bitmap b) {
        this.mBitmap = b;
    }

    Allocation(int id, RenderScript rs, Type t, int usage) {
        super(id, rs);
        if ((usage & -228) != 0) {
            throw new RSIllegalArgumentException("Unknown usage specified.");
        }
        if ((usage & 32) != 0) {
            this.mWriteAllowed = false;
            if ((usage & -36) != 0) {
                throw new RSIllegalArgumentException("Invalid usage combination.");
            }
        }
        this.mType = t;
        this.mUsage = usage;
        this.mSize = this.mType.getCount() * this.mType.getElement().getBytesSize();
        if (t != null) {
            updateCacheInfo(t);
        }
        if (RenderScript.sUseGCHooks) {
            try {
                RenderScript.registerNativeAllocation.invoke(RenderScript.sRuntime, new Object[]{Integer.valueOf(this.mSize)});
            } catch (Exception e) {
                Log.e("RenderScript_jni", "Couldn't invoke registerNativeAllocation:" + e);
                throw new RSRuntimeException("Couldn't invoke registerNativeAllocation:" + e);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void finalize() throws Throwable {
        if (RenderScript.sUseGCHooks) {
            RenderScript.registerNativeFree.invoke(RenderScript.sRuntime, new Object[]{Integer.valueOf(this.mSize)});
        }
        super.finalize();
    }

    private void validateIsInt32() {
        if (this.mType.mElement.mType != DataType.SIGNED_32 && this.mType.mElement.mType != DataType.UNSIGNED_32) {
            throw new RSIllegalArgumentException("32 bit integer source does not match allocation type " + this.mType.mElement.mType);
        }
    }

    private void validateIsInt16() {
        if (this.mType.mElement.mType != DataType.SIGNED_16 && this.mType.mElement.mType != DataType.UNSIGNED_16) {
            throw new RSIllegalArgumentException("16 bit integer source does not match allocation type " + this.mType.mElement.mType);
        }
    }

    private void validateIsInt8() {
        if (this.mType.mElement.mType != DataType.SIGNED_8 && this.mType.mElement.mType != DataType.UNSIGNED_8) {
            throw new RSIllegalArgumentException("8 bit integer source does not match allocation type " + this.mType.mElement.mType);
        }
    }

    private void validateIsFloat32() {
        if (this.mType.mElement.mType != DataType.FLOAT_32) {
            throw new RSIllegalArgumentException("32 bit float source does not match allocation type " + this.mType.mElement.mType);
        }
    }

    private void validateIsObject() {
        if (this.mType.mElement.mType != DataType.RS_ELEMENT && this.mType.mElement.mType != DataType.RS_TYPE && this.mType.mElement.mType != DataType.RS_ALLOCATION && this.mType.mElement.mType != DataType.RS_SAMPLER && this.mType.mElement.mType != DataType.RS_SCRIPT) {
            throw new RSIllegalArgumentException("Object source does not match allocation type " + this.mType.mElement.mType);
        }
    }

    public Type getType() {
        return this.mType;
    }

    public void syncAll(int srcLocation) {
        switch (srcLocation) {
            case 1:
            case 2:
                this.mRS.validate();
                this.mRS.nAllocationSyncAll(getIDSafe(), srcLocation);
                return;
            default:
                throw new RSIllegalArgumentException("Source must be exactly one usage type.");
        }
    }

    public void ioSend() {
        if ((this.mUsage & 64) == 0) {
            throw new RSIllegalArgumentException("Can only send buffer if IO_OUTPUT usage specified.");
        }
        this.mRS.validate();
        this.mRS.nAllocationIoSend(getID(this.mRS));
    }

    public void ioSendOutput() {
        ioSend();
    }

    public void ioReceive() {
        if ((this.mUsage & 32) == 0) {
            throw new RSIllegalArgumentException("Can only receive if IO_INPUT usage specified.");
        }
        this.mRS.validate();
        this.mRS.nAllocationIoReceive(getID(this.mRS));
    }

    public void copyFrom(BaseObj[] d) {
        this.mRS.validate();
        validateIsObject();
        if (d.length != this.mCurrentCount) {
            throw new RSIllegalArgumentException("Array size mismatch, allocation sizeX = " + this.mCurrentCount + ", array length = " + d.length);
        }
        int[] i = new int[d.length];
        for (int ct = 0; ct < d.length; ct++) {
            i[ct] = d[ct].getID(this.mRS);
        }
        copy1DRangeFromUnchecked(0, this.mCurrentCount, i);
    }

    private void validateBitmapFormat(Bitmap b) {
        Config bc = b.getConfig();
        if (bc == null) {
            throw new RSIllegalArgumentException("Bitmap has an unsupported format for this operation");
        }
        switch (AnonymousClass1.$SwitchMap$android$graphics$Bitmap$Config[bc.ordinal()]) {
            case 1:
                if (this.mType.getElement().mKind != DataKind.PIXEL_A) {
                    throw new RSIllegalArgumentException("Allocation kind is " + this.mType.getElement().mKind + ", type " + this.mType.getElement().mType + " of " + this.mType.getElement().getBytesSize() + " bytes, passed bitmap was " + bc);
                }
                return;
            case 2:
                if (this.mType.getElement().mKind != DataKind.PIXEL_RGBA || this.mType.getElement().getBytesSize() != 4) {
                    throw new RSIllegalArgumentException("Allocation kind is " + this.mType.getElement().mKind + ", type " + this.mType.getElement().mType + " of " + this.mType.getElement().getBytesSize() + " bytes, passed bitmap was " + bc);
                }
                return;
            case 3:
                if (this.mType.getElement().mKind != DataKind.PIXEL_RGB || this.mType.getElement().getBytesSize() != 2) {
                    throw new RSIllegalArgumentException("Allocation kind is " + this.mType.getElement().mKind + ", type " + this.mType.getElement().mType + " of " + this.mType.getElement().getBytesSize() + " bytes, passed bitmap was " + bc);
                }
                return;
            case 4:
                if (this.mType.getElement().mKind != DataKind.PIXEL_RGBA || this.mType.getElement().getBytesSize() != 2) {
                    throw new RSIllegalArgumentException("Allocation kind is " + this.mType.getElement().mKind + ", type " + this.mType.getElement().mType + " of " + this.mType.getElement().getBytesSize() + " bytes, passed bitmap was " + bc);
                }
                return;
            default:
                return;
        }
    }

    private void validateBitmapSize(Bitmap b) {
        if (this.mCurrentDimX != b.getWidth() || this.mCurrentDimY != b.getHeight()) {
            throw new RSIllegalArgumentException("Cannot update allocation from bitmap, sizes mismatch");
        }
    }

    public void copyFromUnchecked(int[] d) {
        this.mRS.validate();
        if (this.mCurrentDimZ > 0) {
            copy3DRangeFromUnchecked(0, 0, 0, this.mCurrentDimX, this.mCurrentDimY, this.mCurrentDimZ, d);
        } else if (this.mCurrentDimY > 0) {
            copy2DRangeFromUnchecked(0, 0, this.mCurrentDimX, this.mCurrentDimY, d);
        } else {
            copy1DRangeFromUnchecked(0, this.mCurrentCount, d);
        }
    }

    public void copyFromUnchecked(short[] d) {
        this.mRS.validate();
        if (this.mCurrentDimZ > 0) {
            copy3DRangeFromUnchecked(0, 0, 0, this.mCurrentDimX, this.mCurrentDimY, this.mCurrentDimZ, d);
        } else if (this.mCurrentDimY > 0) {
            copy2DRangeFromUnchecked(0, 0, this.mCurrentDimX, this.mCurrentDimY, d);
        } else {
            copy1DRangeFromUnchecked(0, this.mCurrentCount, d);
        }
    }

    public void copyFromUnchecked(byte[] d) {
        this.mRS.validate();
        if (this.mCurrentDimZ > 0) {
            copy3DRangeFromUnchecked(0, 0, 0, this.mCurrentDimX, this.mCurrentDimY, this.mCurrentDimZ, d);
        } else if (this.mCurrentDimY > 0) {
            copy2DRangeFromUnchecked(0, 0, this.mCurrentDimX, this.mCurrentDimY, d);
        } else {
            copy1DRangeFromUnchecked(0, this.mCurrentCount, d);
        }
    }

    public void copyFromUnchecked(float[] d) {
        this.mRS.validate();
        if (this.mCurrentDimZ > 0) {
            copy3DRangeFromUnchecked(0, 0, 0, this.mCurrentDimX, this.mCurrentDimY, this.mCurrentDimZ, d);
        } else if (this.mCurrentDimY > 0) {
            copy2DRangeFromUnchecked(0, 0, this.mCurrentDimX, this.mCurrentDimY, d);
        } else {
            copy1DRangeFromUnchecked(0, this.mCurrentCount, d);
        }
    }

    public void copyFrom(int[] d) {
        this.mRS.validate();
        if (this.mCurrentDimZ > 0) {
            copy3DRangeFrom(0, 0, 0, this.mCurrentDimX, this.mCurrentDimY, this.mCurrentDimZ, d);
        } else if (this.mCurrentDimY > 0) {
            copy2DRangeFrom(0, 0, this.mCurrentDimX, this.mCurrentDimY, d);
        } else {
            copy1DRangeFrom(0, this.mCurrentCount, d);
        }
    }

    public void copyFrom(short[] d) {
        this.mRS.validate();
        if (this.mCurrentDimZ > 0) {
            copy3DRangeFrom(0, 0, 0, this.mCurrentDimX, this.mCurrentDimY, this.mCurrentDimZ, d);
        } else if (this.mCurrentDimY > 0) {
            copy2DRangeFrom(0, 0, this.mCurrentDimX, this.mCurrentDimY, d);
        } else {
            copy1DRangeFrom(0, this.mCurrentCount, d);
        }
    }

    public void copyFrom(byte[] d) {
        this.mRS.validate();
        if (this.mCurrentDimZ > 0) {
            copy3DRangeFrom(0, 0, 0, this.mCurrentDimX, this.mCurrentDimY, this.mCurrentDimZ, d);
        } else if (this.mCurrentDimY > 0) {
            copy2DRangeFrom(0, 0, this.mCurrentDimX, this.mCurrentDimY, d);
        } else {
            copy1DRangeFrom(0, this.mCurrentCount, d);
        }
    }

    public void copyFrom(float[] d) {
        this.mRS.validate();
        if (this.mCurrentDimZ > 0) {
            copy3DRangeFrom(0, 0, 0, this.mCurrentDimX, this.mCurrentDimY, this.mCurrentDimZ, d);
        } else if (this.mCurrentDimY > 0) {
            copy2DRangeFrom(0, 0, this.mCurrentDimX, this.mCurrentDimY, d);
        } else {
            copy1DRangeFrom(0, this.mCurrentCount, d);
        }
    }

    public void copyFrom(Bitmap b) {
        this.mRS.validate();
        if (b.getConfig() == null) {
            Bitmap newBitmap = Bitmap.createBitmap(b.getWidth(), b.getHeight(), Config.ARGB_8888);
            new Canvas(newBitmap).drawBitmap(b, FlyingIcon.ANGULAR_VMIN, FlyingIcon.ANGULAR_VMIN, null);
            copyFrom(newBitmap);
            return;
        }
        validateBitmapSize(b);
        validateBitmapFormat(b);
        this.mRS.nAllocationCopyFromBitmap(getID(this.mRS), b);
    }

    public void copyFrom(Allocation a) {
        this.mRS.validate();
        if (!this.mType.equals(a.getType())) {
            throw new RSIllegalArgumentException("Types of allocations must match.");
        }
        copy2DRangeFrom(0, 0, this.mCurrentDimX, this.mCurrentDimY, a, 0, 0);
    }

    public void setFromFieldPacker(int xoff, FieldPacker fp) {
        this.mRS.validate();
        int eSize = this.mType.mElement.getBytesSize();
        byte[] data = fp.getData();
        int count = data.length / eSize;
        if (eSize * count != data.length) {
            throw new RSIllegalArgumentException("Field packer length " + data.length + " not divisible by element size " + eSize + ".");
        }
        copy1DRangeFromUnchecked(xoff, count, data);
    }

    public void setFromFieldPacker(int xoff, int component_number, FieldPacker fp) {
        this.mRS.validate();
        if (component_number >= this.mType.mElement.mElements.length) {
            throw new RSIllegalArgumentException("Component_number " + component_number + " out of range.");
        } else if (xoff < 0) {
            throw new RSIllegalArgumentException("Offset must be >= 0.");
        } else {
            byte[] data = fp.getData();
            int eSize = this.mType.mElement.mElements[component_number].getBytesSize() * this.mType.mElement.mArraySizes[component_number];
            if (data.length != eSize) {
                throw new RSIllegalArgumentException("Field packer sizelength " + data.length + " does not match component size " + eSize + ".");
            }
            this.mRS.nAllocationElementData1D(getIDSafe(), xoff, this.mSelectedLOD, component_number, data, data.length);
        }
    }

    private void data1DChecks(int off, int count, int len, int dataSize) {
        this.mRS.validate();
        if (off < 0) {
            throw new RSIllegalArgumentException("Offset must be >= 0.");
        } else if (count < 1) {
            throw new RSIllegalArgumentException("Count must be >= 1.");
        } else if (off + count > this.mCurrentCount) {
            throw new RSIllegalArgumentException("Overflow, Available count " + this.mCurrentCount + ", got " + count + " at offset " + off + ".");
        } else if (len < dataSize) {
            throw new RSIllegalArgumentException("Array too small for allocation type.");
        }
    }

    public void generateMipmaps() {
        this.mRS.nAllocationGenerateMipmaps(getID(this.mRS));
    }

    public void copy1DRangeFromUnchecked(int off, int count, int[] d) {
        int dataSize = this.mType.mElement.getBytesSize() * count;
        data1DChecks(off, count, d.length * 4, dataSize);
        this.mRS.nAllocationData1D(getIDSafe(), off, this.mSelectedLOD, count, d, dataSize);
    }

    public void copy1DRangeFromUnchecked(int off, int count, short[] d) {
        int dataSize = this.mType.mElement.getBytesSize() * count;
        data1DChecks(off, count, d.length * 2, dataSize);
        this.mRS.nAllocationData1D(getIDSafe(), off, this.mSelectedLOD, count, d, dataSize);
    }

    public void copy1DRangeFromUnchecked(int off, int count, byte[] d) {
        int dataSize = this.mType.mElement.getBytesSize() * count;
        data1DChecks(off, count, d.length, dataSize);
        this.mRS.nAllocationData1D(getIDSafe(), off, this.mSelectedLOD, count, d, dataSize);
    }

    public void copy1DRangeFromUnchecked(int off, int count, float[] d) {
        int dataSize = this.mType.mElement.getBytesSize() * count;
        data1DChecks(off, count, d.length * 4, dataSize);
        this.mRS.nAllocationData1D(getIDSafe(), off, this.mSelectedLOD, count, d, dataSize);
    }

    public void copy1DRangeFrom(int off, int count, int[] d) {
        validateIsInt32();
        copy1DRangeFromUnchecked(off, count, d);
    }

    public void copy1DRangeFrom(int off, int count, short[] d) {
        validateIsInt16();
        copy1DRangeFromUnchecked(off, count, d);
    }

    public void copy1DRangeFrom(int off, int count, byte[] d) {
        validateIsInt8();
        copy1DRangeFromUnchecked(off, count, d);
    }

    public void copy1DRangeFrom(int off, int count, float[] d) {
        validateIsFloat32();
        copy1DRangeFromUnchecked(off, count, d);
    }

    public void copy1DRangeFrom(int off, int count, Allocation data, int dataOff) {
        this.mRS.nAllocationData2D(getIDSafe(), off, 0, this.mSelectedLOD, this.mSelectedFace.mID, count, 1, data.getID(this.mRS), dataOff, 0, data.mSelectedLOD, data.mSelectedFace.mID);
    }

    private void validate2DRange(int xoff, int yoff, int w, int h) {
        if (this.mAdaptedAllocation == null) {
            if (xoff < 0 || yoff < 0) {
                throw new RSIllegalArgumentException("Offset cannot be negative.");
            } else if (h < 0 || w < 0) {
                throw new RSIllegalArgumentException("Height or width cannot be negative.");
            } else if (xoff + w > this.mCurrentDimX || yoff + h > this.mCurrentDimY) {
                throw new RSIllegalArgumentException("Updated region larger than allocation.");
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public void copy2DRangeFromUnchecked(int xoff, int yoff, int w, int h, byte[] data) {
        this.mRS.validate();
        validate2DRange(xoff, yoff, w, h);
        this.mRS.nAllocationData2D(getIDSafe(), xoff, yoff, this.mSelectedLOD, this.mSelectedFace.mID, w, h, data, data.length);
    }

    /* access modifiers changed from: 0000 */
    public void copy2DRangeFromUnchecked(int xoff, int yoff, int w, int h, short[] data) {
        this.mRS.validate();
        validate2DRange(xoff, yoff, w, h);
        this.mRS.nAllocationData2D(getIDSafe(), xoff, yoff, this.mSelectedLOD, this.mSelectedFace.mID, w, h, data, data.length * 2);
    }

    /* access modifiers changed from: 0000 */
    public void copy2DRangeFromUnchecked(int xoff, int yoff, int w, int h, int[] data) {
        this.mRS.validate();
        validate2DRange(xoff, yoff, w, h);
        this.mRS.nAllocationData2D(getIDSafe(), xoff, yoff, this.mSelectedLOD, this.mSelectedFace.mID, w, h, data, data.length * 4);
    }

    /* access modifiers changed from: 0000 */
    public void copy2DRangeFromUnchecked(int xoff, int yoff, int w, int h, float[] data) {
        this.mRS.validate();
        validate2DRange(xoff, yoff, w, h);
        this.mRS.nAllocationData2D(getIDSafe(), xoff, yoff, this.mSelectedLOD, this.mSelectedFace.mID, w, h, data, data.length * 4);
    }

    public void copy2DRangeFrom(int xoff, int yoff, int w, int h, byte[] data) {
        validateIsInt8();
        copy2DRangeFromUnchecked(xoff, yoff, w, h, data);
    }

    public void copy2DRangeFrom(int xoff, int yoff, int w, int h, short[] data) {
        validateIsInt16();
        copy2DRangeFromUnchecked(xoff, yoff, w, h, data);
    }

    public void copy2DRangeFrom(int xoff, int yoff, int w, int h, int[] data) {
        validateIsInt32();
        copy2DRangeFromUnchecked(xoff, yoff, w, h, data);
    }

    public void copy2DRangeFrom(int xoff, int yoff, int w, int h, float[] data) {
        validateIsFloat32();
        copy2DRangeFromUnchecked(xoff, yoff, w, h, data);
    }

    public void copy2DRangeFrom(int xoff, int yoff, int w, int h, Allocation data, int dataXoff, int dataYoff) {
        this.mRS.validate();
        validate2DRange(xoff, yoff, w, h);
        this.mRS.nAllocationData2D(getIDSafe(), xoff, yoff, this.mSelectedLOD, this.mSelectedFace.mID, w, h, data.getID(this.mRS), dataXoff, dataYoff, data.mSelectedLOD, data.mSelectedFace.mID);
    }

    public void copy2DRangeFrom(int xoff, int yoff, Bitmap data) {
        this.mRS.validate();
        if (data.getConfig() == null) {
            Bitmap newBitmap = Bitmap.createBitmap(data.getWidth(), data.getHeight(), Config.ARGB_8888);
            new Canvas(newBitmap).drawBitmap(data, FlyingIcon.ANGULAR_VMIN, FlyingIcon.ANGULAR_VMIN, null);
            copy2DRangeFrom(xoff, yoff, newBitmap);
            return;
        }
        validateBitmapFormat(data);
        validate2DRange(xoff, yoff, data.getWidth(), data.getHeight());
        this.mRS.nAllocationData2D(getIDSafe(), xoff, yoff, this.mSelectedLOD, this.mSelectedFace.mID, data);
    }

    private void validate3DRange(int xoff, int yoff, int zoff, int w, int h, int d) {
        if (this.mAdaptedAllocation == null) {
            if (xoff < 0 || yoff < 0 || zoff < 0) {
                throw new RSIllegalArgumentException("Offset cannot be negative.");
            } else if (h < 0 || w < 0 || d < 0) {
                throw new RSIllegalArgumentException("Height or width cannot be negative.");
            } else if (xoff + w > this.mCurrentDimX || yoff + h > this.mCurrentDimY || zoff + d > this.mCurrentDimZ) {
                throw new RSIllegalArgumentException("Updated region larger than allocation.");
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public void copy3DRangeFromUnchecked(int xoff, int yoff, int zoff, int w, int h, int d, byte[] data) {
        this.mRS.validate();
        validate3DRange(xoff, yoff, zoff, w, h, d);
        this.mRS.nAllocationData3D(getIDSafe(), xoff, yoff, zoff, this.mSelectedLOD, w, h, d, data, data.length);
    }

    /* access modifiers changed from: 0000 */
    public void copy3DRangeFromUnchecked(int xoff, int yoff, int zoff, int w, int h, int d, short[] data) {
        this.mRS.validate();
        validate3DRange(xoff, yoff, zoff, w, h, d);
        this.mRS.nAllocationData3D(getIDSafe(), xoff, yoff, zoff, this.mSelectedLOD, w, h, d, data, data.length * 2);
    }

    /* access modifiers changed from: 0000 */
    public void copy3DRangeFromUnchecked(int xoff, int yoff, int zoff, int w, int h, int d, int[] data) {
        this.mRS.validate();
        validate3DRange(xoff, yoff, zoff, w, h, d);
        this.mRS.nAllocationData3D(getIDSafe(), xoff, yoff, zoff, this.mSelectedLOD, w, h, d, data, data.length * 4);
    }

    /* access modifiers changed from: 0000 */
    public void copy3DRangeFromUnchecked(int xoff, int yoff, int zoff, int w, int h, int d, float[] data) {
        this.mRS.validate();
        validate3DRange(xoff, yoff, zoff, w, h, d);
        this.mRS.nAllocationData3D(getIDSafe(), xoff, yoff, zoff, this.mSelectedLOD, w, h, d, data, data.length * 4);
    }

    public void copy3DRangeFrom(int xoff, int yoff, int zoff, int w, int h, int d, byte[] data) {
        validateIsInt8();
        copy3DRangeFromUnchecked(xoff, yoff, zoff, w, h, d, data);
    }

    public void copy3DRangeFrom(int xoff, int yoff, int zoff, int w, int h, int d, short[] data) {
        validateIsInt16();
        copy3DRangeFromUnchecked(xoff, yoff, zoff, w, h, d, data);
    }

    public void copy3DRangeFrom(int xoff, int yoff, int zoff, int w, int h, int d, int[] data) {
        validateIsInt32();
        copy3DRangeFromUnchecked(xoff, yoff, zoff, w, h, d, data);
    }

    public void copy3DRangeFrom(int xoff, int yoff, int zoff, int w, int h, int d, float[] data) {
        validateIsFloat32();
        copy3DRangeFromUnchecked(xoff, yoff, zoff, w, h, d, data);
    }

    public void copy3DRangeFrom(int xoff, int yoff, int zoff, int w, int h, int d, Allocation data, int dataXoff, int dataYoff, int dataZoff) {
        this.mRS.validate();
        validate3DRange(xoff, yoff, zoff, w, h, d);
        int i = xoff;
        int i2 = yoff;
        int i3 = zoff;
        int i4 = w;
        int i5 = h;
        int i6 = d;
        int i7 = dataXoff;
        int i8 = dataYoff;
        int i9 = dataZoff;
        this.mRS.nAllocationData3D(getIDSafe(), i, i2, i3, this.mSelectedLOD, i4, i5, i6, data.getID(this.mRS), i7, i8, i9, data.mSelectedLOD);
    }

    public void copyTo(Bitmap b) {
        this.mRS.validate();
        validateBitmapFormat(b);
        validateBitmapSize(b);
        this.mRS.nAllocationCopyToBitmap(getID(this.mRS), b);
    }

    public void copyTo(byte[] d) {
        validateIsInt8();
        this.mRS.validate();
        this.mRS.nAllocationRead(getID(this.mRS), d);
    }

    public void copyTo(short[] d) {
        validateIsInt16();
        this.mRS.validate();
        this.mRS.nAllocationRead(getID(this.mRS), d);
    }

    public void copyTo(int[] d) {
        validateIsInt32();
        this.mRS.validate();
        this.mRS.nAllocationRead(getID(this.mRS), d);
    }

    public void copyTo(float[] d) {
        validateIsFloat32();
        this.mRS.validate();
        this.mRS.nAllocationRead(getID(this.mRS), d);
    }

    static {
        mBitmapOptions.inScaled = false;
    }

    public static Allocation createTyped(RenderScript rs, Type type, MipmapControl mips, int usage) {
        if (RenderScript.isNative) {
            return AllocationThunker.createTyped((RenderScriptThunker) rs, type, mips, usage);
        }
        rs.validate();
        if (type.getID(rs) == 0) {
            throw new RSInvalidStateException("Bad Type");
        }
        int id = rs.nAllocationCreateTyped(type.getID(rs), mips.mID, usage, 0);
        if (id != 0) {
            return new Allocation(id, rs, type, usage);
        }
        throw new RSRuntimeException("Allocation creation failed.");
    }

    public static Allocation createTyped(RenderScript rs, Type type, int usage) {
        return createTyped(rs, type, MipmapControl.MIPMAP_NONE, usage);
    }

    public static Allocation createTyped(RenderScript rs, Type type) {
        return createTyped(rs, type, MipmapControl.MIPMAP_NONE, 1);
    }

    public static Allocation createSized(RenderScript rs, Element e, int count, int usage) {
        if (RenderScript.isNative) {
            RenderScriptThunker renderScriptThunker = (RenderScriptThunker) rs;
            return AllocationThunker.createSized(rs, e, count, usage);
        }
        rs.validate();
        Builder b = new Builder(rs, e);
        b.setX(count);
        Type t = b.create();
        int id = rs.nAllocationCreateTyped(t.getID(rs), MipmapControl.MIPMAP_NONE.mID, usage, 0);
        if (id != 0) {
            return new Allocation(id, rs, t, usage);
        }
        throw new RSRuntimeException("Allocation creation failed.");
    }

    public static Allocation createSized(RenderScript rs, Element e, int count) {
        return createSized(rs, e, count, 1);
    }

    static Element elementFromBitmap(RenderScript rs, Bitmap b) {
        Config bc = b.getConfig();
        if (bc == Config.ALPHA_8) {
            return Element.A_8(rs);
        }
        if (bc == Config.ARGB_4444) {
            return Element.RGBA_4444(rs);
        }
        if (bc == Config.ARGB_8888) {
            return Element.RGBA_8888(rs);
        }
        if (bc == Config.RGB_565) {
            return Element.RGB_565(rs);
        }
        throw new RSInvalidStateException("Bad bitmap type: " + bc);
    }

    static Type typeFromBitmap(RenderScript rs, Bitmap b, MipmapControl mip) {
        Builder tb = new Builder(rs, elementFromBitmap(rs, b));
        tb.setX(b.getWidth());
        tb.setY(b.getHeight());
        tb.setMipmaps(mip == MipmapControl.MIPMAP_FULL);
        return tb.create();
    }

    public static Allocation createFromBitmap(RenderScript rs, Bitmap b, MipmapControl mips, int usage) {
        if (RenderScript.isNative) {
            return AllocationThunker.createFromBitmap((RenderScriptThunker) rs, b, mips, usage);
        }
        rs.validate();
        if (b.getConfig() != null) {
            Type t = typeFromBitmap(rs, b, mips);
            if (mips == MipmapControl.MIPMAP_NONE && t.getElement().isCompatible(Element.RGBA_8888(rs)) && usage == 131) {
                int id = rs.nAllocationCreateBitmapBackedAllocation(t.getID(rs), mips.mID, b, usage);
                if (id == 0) {
                    throw new RSRuntimeException("Load failed.");
                }
                Allocation alloc = new Allocation(id, rs, t, usage);
                alloc.setBitmap(b);
                return alloc;
            }
            int id2 = rs.nAllocationCreateFromBitmap(t.getID(rs), mips.mID, b, usage);
            if (id2 != 0) {
                return new Allocation(id2, rs, t, usage);
            }
            throw new RSRuntimeException("Load failed.");
        } else if ((usage & 128) != 0) {
            throw new RSIllegalArgumentException("USAGE_SHARED cannot be used with a Bitmap that has a null config.");
        } else {
            Bitmap newBitmap = Bitmap.createBitmap(b.getWidth(), b.getHeight(), Config.ARGB_8888);
            new Canvas(newBitmap).drawBitmap(b, FlyingIcon.ANGULAR_VMIN, FlyingIcon.ANGULAR_VMIN, null);
            return createFromBitmap(rs, newBitmap, mips, usage);
        }
    }

    public static Allocation createFromBitmap(RenderScript rs, Bitmap b) {
        return createFromBitmap(rs, b, MipmapControl.MIPMAP_NONE, 131);
    }

    public static Allocation createCubemapFromBitmap(RenderScript rs, Bitmap b, MipmapControl mips, int usage) {
        boolean isPow2;
        boolean z = true;
        rs.validate();
        int height = b.getHeight();
        int width = b.getWidth();
        if (width % 6 != 0) {
            throw new RSIllegalArgumentException("Cubemap height must be multiple of 6");
        } else if (width / 6 != height) {
            throw new RSIllegalArgumentException("Only square cube map faces supported");
        } else {
            if (((height - 1) & height) == 0) {
                isPow2 = true;
            } else {
                isPow2 = false;
            }
            if (!isPow2) {
                throw new RSIllegalArgumentException("Only power of 2 cube faces supported");
            }
            Element e = elementFromBitmap(rs, b);
            Builder tb = new Builder(rs, e);
            tb.setX(height);
            tb.setY(height);
            tb.setFaces(true);
            if (mips != MipmapControl.MIPMAP_FULL) {
                z = false;
            }
            tb.setMipmaps(z);
            Type t = tb.create();
            int id = rs.nAllocationCubeCreateFromBitmap(t.getID(rs), mips.mID, b, usage);
            if (id != 0) {
                return new Allocation(id, rs, t, usage);
            }
            throw new RSRuntimeException("Load failed for bitmap " + b + " element " + e);
        }
    }

    public static Allocation createCubemapFromBitmap(RenderScript rs, Bitmap b) {
        return createCubemapFromBitmap(rs, b, MipmapControl.MIPMAP_NONE, 2);
    }

    public static Allocation createCubemapFromCubeFaces(RenderScript rs, Bitmap xpos, Bitmap xneg, Bitmap ypos, Bitmap yneg, Bitmap zpos, Bitmap zneg, MipmapControl mips, int usage) {
        return null;
    }

    public static Allocation createCubemapFromCubeFaces(RenderScript rs, Bitmap xpos, Bitmap xneg, Bitmap ypos, Bitmap yneg, Bitmap zpos, Bitmap zneg) {
        return createCubemapFromCubeFaces(rs, xpos, xneg, ypos, yneg, zpos, zneg, MipmapControl.MIPMAP_NONE, 2);
    }

    public static Allocation createFromBitmapResource(RenderScript rs, Resources res, int id, MipmapControl mips, int usage) {
        rs.validate();
        if ((usage & 224) != 0) {
            throw new RSIllegalArgumentException("Unsupported usage specified.");
        }
        Bitmap b = BitmapFactory.decodeResource(res, id);
        Allocation alloc = createFromBitmap(rs, b, mips, usage);
        b.recycle();
        return alloc;
    }

    public static Allocation createFromBitmapResource(RenderScript rs, Resources res, int id) {
        return createFromBitmapResource(rs, res, id, MipmapControl.MIPMAP_NONE, 3);
    }

    public static Allocation createFromString(RenderScript rs, String str, int usage) {
        rs.validate();
        try {
            byte[] allocArray = str.getBytes(e.f);
            Allocation alloc = createSized(rs, Element.U8(rs), allocArray.length, usage);
            alloc.copyFrom(allocArray);
            return alloc;
        } catch (Exception e) {
            throw new RSRuntimeException("Could not convert string to utf-8.");
        }
    }
}
