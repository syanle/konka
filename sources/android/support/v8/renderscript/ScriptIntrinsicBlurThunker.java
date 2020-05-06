package android.support.v8.renderscript;

import android.renderscript.ScriptIntrinsicBlur;
import android.support.v8.renderscript.Script.FieldID;
import android.support.v8.renderscript.Script.KernelID;

class ScriptIntrinsicBlurThunker extends ScriptIntrinsicBlur {
    ScriptIntrinsicBlur mN;

    /* access modifiers changed from: 0000 */
    public ScriptIntrinsicBlur getNObj() {
        return this.mN;
    }

    protected ScriptIntrinsicBlurThunker(int id, RenderScript rs) {
        super(id, rs);
    }

    public static ScriptIntrinsicBlurThunker create(RenderScript rs, Element e) {
        RenderScriptThunker rst = (RenderScriptThunker) rs;
        ElementThunker et = (ElementThunker) e;
        ScriptIntrinsicBlurThunker blur = new ScriptIntrinsicBlurThunker(0, rs);
        blur.mN = ScriptIntrinsicBlur.create(rst.mN, et.getNObj());
        return blur;
    }

    public void setInput(Allocation ain) {
        this.mN.setInput(((AllocationThunker) ain).getNObj());
    }

    public void setRadius(float radius) {
        this.mN.setRadius(radius);
    }

    public void forEach(Allocation aout) {
        AllocationThunker aoutt = (AllocationThunker) aout;
        if (aoutt != null) {
            this.mN.forEach(aoutt.getNObj());
        }
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
