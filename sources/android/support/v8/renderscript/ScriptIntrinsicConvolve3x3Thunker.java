package android.support.v8.renderscript;

import android.renderscript.ScriptIntrinsicConvolve3x3;
import android.support.v8.renderscript.Script.FieldID;
import android.support.v8.renderscript.Script.KernelID;

class ScriptIntrinsicConvolve3x3Thunker extends ScriptIntrinsicConvolve3x3 {
    ScriptIntrinsicConvolve3x3 mN;

    /* access modifiers changed from: 0000 */
    public ScriptIntrinsicConvolve3x3 getNObj() {
        return this.mN;
    }

    ScriptIntrinsicConvolve3x3Thunker(int id, RenderScript rs) {
        super(id, rs);
    }

    public static ScriptIntrinsicConvolve3x3Thunker create(RenderScript rs, Element e) {
        RenderScriptThunker rst = (RenderScriptThunker) rs;
        ElementThunker et = (ElementThunker) e;
        ScriptIntrinsicConvolve3x3Thunker si = new ScriptIntrinsicConvolve3x3Thunker(0, rs);
        si.mN = ScriptIntrinsicConvolve3x3.create(rst.mN, et.getNObj());
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
