package android.support.v8.renderscript;

import android.support.v8.renderscript.Script.KernelID;
import com.umeng.common.util.g;

public class ScriptIntrinsicLUT extends ScriptIntrinsic {
    private final byte[] mCache = new byte[1024];
    private boolean mDirty = true;
    private final Matrix4f mMatrix = new Matrix4f();
    private Allocation mTables;

    protected ScriptIntrinsicLUT(int id, RenderScript rs) {
        super(id, rs);
    }

    public static ScriptIntrinsicLUT create(RenderScript rs, Element e) {
        if (RenderScript.isNative) {
            RenderScriptThunker renderScriptThunker = (RenderScriptThunker) rs;
            return ScriptIntrinsicLUTThunker.create(rs, e);
        }
        ScriptIntrinsicLUT si = new ScriptIntrinsicLUT(rs.nScriptIntrinsicCreate(3, e.getID(rs)), rs);
        si.mTables = Allocation.createSized(rs, Element.U8(rs), 1024);
        for (int ct = 0; ct < 256; ct++) {
            si.mCache[ct] = (byte) ct;
            si.mCache[ct + g.b] = (byte) ct;
            si.mCache[ct + 512] = (byte) ct;
            si.mCache[ct + 768] = (byte) ct;
        }
        si.setVar(0, (BaseObj) si.mTables);
        return si;
    }

    private void validate(int index, int value) {
        if (index < 0 || index > 255) {
            throw new RSIllegalArgumentException("Index out of range (0-255).");
        } else if (value < 0 || value > 255) {
            throw new RSIllegalArgumentException("Value out of range (0-255).");
        }
    }

    public void setRed(int index, int value) {
        validate(index, value);
        this.mCache[index] = (byte) value;
        this.mDirty = true;
    }

    public void setGreen(int index, int value) {
        validate(index, value);
        this.mCache[index + g.b] = (byte) value;
        this.mDirty = true;
    }

    public void setBlue(int index, int value) {
        validate(index, value);
        this.mCache[index + 512] = (byte) value;
        this.mDirty = true;
    }

    public void setAlpha(int index, int value) {
        validate(index, value);
        this.mCache[index + 768] = (byte) value;
        this.mDirty = true;
    }

    public void forEach(Allocation ain, Allocation aout) {
        if (this.mDirty) {
            this.mDirty = false;
            this.mTables.copyFromUnchecked(this.mCache);
        }
        forEach(0, ain, aout, null);
    }

    public KernelID getKernelID() {
        return createKernelID(0, 3, null, null);
    }
}
