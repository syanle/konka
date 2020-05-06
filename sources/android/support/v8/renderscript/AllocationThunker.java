package android.support.v8.renderscript;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory.Options;
import android.renderscript.Allocation;
import android.renderscript.Allocation.MipmapControl;
import android.renderscript.BaseObj;
import android.renderscript.Element;
import android.renderscript.FieldPacker;

class AllocationThunker extends Allocation {
    static Options mBitmapOptions = new Options();
    Allocation mN;

    /* access modifiers changed from: 0000 */
    public Allocation getNObj() {
        return this.mN;
    }

    static MipmapControl convertMipmapControl(Allocation.MipmapControl mc) {
        switch (mc) {
            case MIPMAP_NONE:
                return MipmapControl.MIPMAP_NONE;
            case MIPMAP_FULL:
                return MipmapControl.MIPMAP_FULL;
            case MIPMAP_ON_SYNC_TO_TEXTURE:
                return MipmapControl.MIPMAP_ON_SYNC_TO_TEXTURE;
            default:
                return null;
        }
    }

    public Type getType() {
        return TypeThunker.find(this.mN.getType());
    }

    public Element getElement() {
        return getType().getElement();
    }

    public int getUsage() {
        return this.mN.getUsage();
    }

    public int getBytesSize() {
        return this.mN.getBytesSize();
    }

    AllocationThunker(RenderScript rs, Type t, int usage, Allocation na) {
        super(0, rs, t, usage);
        this.mType = t;
        this.mUsage = usage;
        this.mN = na;
    }

    public void syncAll(int srcLocation) {
        this.mN.syncAll(srcLocation);
    }

    public void ioSend() {
        this.mN.ioSend();
    }

    public void ioReceive() {
        this.mN.ioReceive();
    }

    public void copyFrom(BaseObj[] d) {
        if (d != null) {
            BaseObj[] dN = new BaseObj[d.length];
            for (int i = 0; i < d.length; i++) {
                dN[i] = d[i].getNObj();
            }
            this.mN.copyFrom(dN);
        }
    }

    public void copyFromUnchecked(int[] d) {
        this.mN.copyFromUnchecked(d);
    }

    public void copyFromUnchecked(short[] d) {
        this.mN.copyFromUnchecked(d);
    }

    public void copyFromUnchecked(byte[] d) {
        this.mN.copyFromUnchecked(d);
    }

    public void copyFromUnchecked(float[] d) {
        this.mN.copyFromUnchecked(d);
    }

    public void copyFrom(int[] d) {
        this.mN.copyFrom(d);
    }

    public void copyFrom(short[] d) {
        this.mN.copyFrom(d);
    }

    public void copyFrom(byte[] d) {
        this.mN.copyFrom(d);
    }

    public void copyFrom(float[] d) {
        this.mN.copyFrom(d);
    }

    public void copyFrom(Bitmap b) {
        this.mN.copyFrom(b);
    }

    public void copyFrom(Allocation a) {
        this.mN.copyFrom(((AllocationThunker) a).mN);
    }

    public void setFromFieldPacker(int xoff, FieldPacker fp) {
        this.mN.setFromFieldPacker(xoff, new FieldPacker(fp.getData()));
    }

    public void setFromFieldPacker(int xoff, int component_number, FieldPacker fp) {
        this.mN.setFromFieldPacker(xoff, component_number, new FieldPacker(fp.getData()));
    }

    public void generateMipmaps() {
        this.mN.generateMipmaps();
    }

    public void copy1DRangeFromUnchecked(int off, int count, int[] d) {
        this.mN.copy1DRangeFromUnchecked(off, count, d);
    }

    public void copy1DRangeFromUnchecked(int off, int count, short[] d) {
        this.mN.copy1DRangeFromUnchecked(off, count, d);
    }

    public void copy1DRangeFromUnchecked(int off, int count, byte[] d) {
        this.mN.copy1DRangeFromUnchecked(off, count, d);
    }

    public void copy1DRangeFromUnchecked(int off, int count, float[] d) {
        this.mN.copy1DRangeFromUnchecked(off, count, d);
    }

    public void copy1DRangeFrom(int off, int count, int[] d) {
        this.mN.copy1DRangeFrom(off, count, d);
    }

    public void copy1DRangeFrom(int off, int count, short[] d) {
        this.mN.copy1DRangeFrom(off, count, d);
    }

    public void copy1DRangeFrom(int off, int count, byte[] d) {
        this.mN.copy1DRangeFrom(off, count, d);
    }

    public void copy1DRangeFrom(int off, int count, float[] d) {
        this.mN.copy1DRangeFrom(off, count, d);
    }

    public void copy1DRangeFrom(int off, int count, Allocation data, int dataOff) {
        this.mN.copy1DRangeFrom(off, count, ((AllocationThunker) data).mN, dataOff);
    }

    public void copy2DRangeFrom(int xoff, int yoff, int w, int h, byte[] data) {
        this.mN.copy2DRangeFrom(xoff, yoff, w, h, data);
    }

    public void copy2DRangeFrom(int xoff, int yoff, int w, int h, short[] data) {
        this.mN.copy2DRangeFrom(xoff, yoff, w, h, data);
    }

    public void copy2DRangeFrom(int xoff, int yoff, int w, int h, int[] data) {
        this.mN.copy2DRangeFrom(xoff, yoff, w, h, data);
    }

    public void copy2DRangeFrom(int xoff, int yoff, int w, int h, float[] data) {
        this.mN.copy2DRangeFrom(xoff, yoff, w, h, data);
    }

    public void copy2DRangeFrom(int xoff, int yoff, int w, int h, Allocation data, int dataXoff, int dataYoff) {
        int i = xoff;
        int i2 = yoff;
        int i3 = w;
        int i4 = h;
        this.mN.copy2DRangeFrom(i, i2, i3, i4, ((AllocationThunker) data).mN, dataXoff, dataYoff);
    }

    public void copy2DRangeFrom(int xoff, int yoff, Bitmap data) {
        this.mN.copy2DRangeFrom(xoff, yoff, data);
    }

    public void copyTo(Bitmap b) {
        this.mN.copyTo(b);
    }

    public void copyTo(byte[] d) {
        this.mN.copyTo(d);
    }

    public void copyTo(short[] d) {
        this.mN.copyTo(d);
    }

    public void copyTo(int[] d) {
        this.mN.copyTo(d);
    }

    public void copyTo(float[] d) {
        this.mN.copyTo(d);
    }

    static {
        mBitmapOptions.inScaled = false;
    }

    public static Allocation createTyped(RenderScript rs, Type type, Allocation.MipmapControl mips, int usage) {
        return new AllocationThunker(rs, type, usage, Allocation.createTyped(((RenderScriptThunker) rs).mN, ((TypeThunker) type).mN, convertMipmapControl(mips), usage));
    }

    public static Allocation createFromBitmap(RenderScript rs, Bitmap b, Allocation.MipmapControl mips, int usage) {
        Allocation a = Allocation.createFromBitmap(((RenderScriptThunker) rs).mN, b, convertMipmapControl(mips), usage);
        return new AllocationThunker(rs, new TypeThunker(rs, a.getType()), usage, a);
    }

    public static Allocation createCubemapFromBitmap(RenderScript rs, Bitmap b, Allocation.MipmapControl mips, int usage) {
        Allocation a = Allocation.createCubemapFromBitmap(((RenderScriptThunker) rs).mN, b, convertMipmapControl(mips), usage);
        return new AllocationThunker(rs, new TypeThunker(rs, a.getType()), usage, a);
    }

    public static Allocation createCubemapFromCubeFaces(RenderScript rs, Bitmap xpos, Bitmap xneg, Bitmap ypos, Bitmap yneg, Bitmap zpos, Bitmap zneg, Allocation.MipmapControl mips, int usage) {
        Allocation a = Allocation.createCubemapFromCubeFaces(((RenderScriptThunker) rs).mN, xpos, xneg, ypos, yneg, zpos, zneg, convertMipmapControl(mips), usage);
        return new AllocationThunker(rs, new TypeThunker(rs, a.getType()), usage, a);
    }

    public static Allocation createFromBitmapResource(RenderScript rs, Resources res, int id, Allocation.MipmapControl mips, int usage) {
        Allocation a = Allocation.createFromBitmapResource(((RenderScriptThunker) rs).mN, res, id, convertMipmapControl(mips), usage);
        return new AllocationThunker(rs, new TypeThunker(rs, a.getType()), usage, a);
    }

    public static Allocation createFromString(RenderScript rs, String str, int usage) {
        Allocation a = Allocation.createFromString(((RenderScriptThunker) rs).mN, str, usage);
        return new AllocationThunker(rs, new TypeThunker(rs, a.getType()), usage, a);
    }

    public static Allocation createSized(RenderScript rs, Element e, int count, int usage) {
        ElementThunker elementThunker = (ElementThunker) e;
        Allocation a = Allocation.createSized(((RenderScriptThunker) rs).mN, (Element) e.getNObj(), count, usage);
        return new AllocationThunker(rs, new TypeThunker(rs, a.getType()), usage, a);
    }
}
