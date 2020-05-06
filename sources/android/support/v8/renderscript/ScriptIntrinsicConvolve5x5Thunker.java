package android.support.v8.renderscript;

import android.renderscript.ScriptIntrinsicConvolve5x5;
import android.support.v8.renderscript.Script.FieldID;
import android.support.v8.renderscript.Script.KernelID;

class ScriptIntrinsicConvolve5x5Thunker extends ScriptIntrinsicConvolve5x5 {
    ScriptIntrinsicConvolve5x5 mN;

    /* access modifiers changed from: 0000 */
    public ScriptIntrinsicConvolve5x5 getNObj() {
        return this.mN;
    }

    ScriptIntrinsicConvolve5x5Thunker(int id, RenderScript rs) {
        super(id, rs);
    }

    public static ScriptIntrinsicConvolve5x5Thunker create(RenderScript rs, Element e) {
        RenderScriptThunker rst = (RenderScriptThunker) rs;
        ElementThunker et = (ElementThunker) e;
        ScriptIntrinsicConvolve5x5Thunker si = new ScriptIntrinsicConvolve5x5Thunker(0, rs);
        si.mN = ScriptIntrinsicConvolve5x5.create(rst.mN, et.getNObj());
        return si;
    }

    public void setInput(Allocation ain) {
        this.mN.setInput(((AllocationThunker) ain).getNObj());
    }

    public void setCoefficients(float[] v) {
        this.mN.setCoefficients(v);
    }

    public void forEach(Allocation aout) {
        this.mN.forEach(((AllocationThunker) aout).getNObj());
    }

    public KernelID getKernelID() {
        KernelID k = createKernelID(0, 2, null, null);
        k.mN = this.mN.getKernelID();
        return k;
    }

    public FieldID getFieldID_Input() {
        FieldID f = createFieldID(1, null);
        f.mN = this.mN.getFieldID_Input();
        return f;
    }
}
