package android.support.v8.renderscript;

import android.support.v8.renderscript.Script.KernelID;

public class ScriptIntrinsic3DLUT extends ScriptIntrinsic {
    private Element mElement;
    private Allocation mLUT;

    protected ScriptIntrinsic3DLUT(int id, RenderScript rs, Element e) {
        super(id, rs);
        this.mElement = e;
    }

    public static ScriptIntrinsic3DLUT create(RenderScript rs, Element e) {
        if (RenderScript.isNative) {
            RenderScriptThunker renderScriptThunker = (RenderScriptThunker) rs;
            return ScriptIntrinsic3DLUTThunker.create(rs, e);
        }
        int id = rs.nScriptIntrinsicCreate(8, e.getID(rs));
        if (e.isCompatible(Element.U8_4(rs))) {
            return new ScriptIntrinsic3DLUT(id, rs, e);
        }
        throw new RSIllegalArgumentException("Element must be compatible with uchar4.");
    }

    public void setLUT(Allocation lut) {
        Type t = lut.getType();
        if (t.getZ() == 0) {
            throw new RSIllegalArgumentException("LUT must be 3d.");
        } else if (!t.getElement().isCompatible(this.mElement)) {
            throw new RSIllegalArgumentException("LUT element type must match.");
        } else {
            this.mLUT = lut;
            setVar(0, (BaseObj) this.mLUT);
        }
    }

    public void forEach(Allocation ain, Allocation aout) {
        forEach(0, ain, aout, null);
    }

    public KernelID getKernelID() {
        return createKernelID(0, 3, null, null);
    }
}
