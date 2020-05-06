package android.support.v8.renderscript;

import android.renderscript.ScriptIntrinsicLUT;
import android.support.v8.renderscript.Script.KernelID;

class ScriptIntrinsicLUTThunker extends ScriptIntrinsicLUT {
    ScriptIntrinsicLUT mN;

    /* access modifiers changed from: 0000 */
    public ScriptIntrinsicLUT getNObj() {
        return this.mN;
    }

    private ScriptIntrinsicLUTThunker(int id, RenderScript rs) {
        super(id, rs);
    }

    public static ScriptIntrinsicLUTThunker create(RenderScript rs, Element e) {
        RenderScriptThunker rst = (RenderScriptThunker) rs;
        ElementThunker et = (ElementThunker) e;
        ScriptIntrinsicLUTThunker si = new ScriptIntrinsicLUTThunker(0, rs);
        si.mN = ScriptIntrinsicLUT.create(rst.mN, et.getNObj());
        return si;
    }

    public void setRed(int index, int value) {
        this.mN.setRed(index, value);
    }

    public void setGreen(int index, int value) {
        this.mN.setGreen(index, value);
    }

    public void setBlue(int index, int value) {
        this.mN.setBlue(index, value);
    }

    public void setAlpha(int index, int value) {
        this.mN.setAlpha(index, value);
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
