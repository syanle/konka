package android.support.v8.renderscript;

import android.renderscript.ScriptIntrinsic3DLUT;
import android.support.v8.renderscript.Script.KernelID;

class ScriptIntrinsic3DLUTThunker extends ScriptIntrinsic3DLUT {
    ScriptIntrinsic3DLUT mN;

    /* access modifiers changed from: 0000 */
    public ScriptIntrinsic3DLUT getNObj() {
        return this.mN;
    }

    private ScriptIntrinsic3DLUTThunker(int id, RenderScript rs, Element e) {
        super(id, rs, e);
    }

    public static ScriptIntrinsic3DLUTThunker create(RenderScript rs, Element e) {
        RenderScriptThunker rst = (RenderScriptThunker) rs;
        ElementThunker et = (ElementThunker) e;
        ScriptIntrinsic3DLUTThunker lut = new ScriptIntrinsic3DLUTThunker(0, rs, e);
        lut.mN = ScriptIntrinsic3DLUT.create(rst.mN, et.getNObj());
        return lut;
    }

    public void setLUT(Allocation lut) {
        this.mN.setLUT(((AllocationThunker) lut).getNObj());
    }

    public void forEach(Allocation ain, Allocation aout) {
        this.mN.forEach(((AllocationThunker) ain).getNObj(), ((AllocationThunker) aout).getNObj());
    }

    public KernelID getKernelID() {
        KernelID k = createKernelID(0, 3, null, null);
        k.mN = this.mN.getKernelID();
        return k;
    }
}
