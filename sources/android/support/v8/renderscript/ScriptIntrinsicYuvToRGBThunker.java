package android.support.v8.renderscript;

import android.renderscript.ScriptIntrinsicYuvToRGB;
import android.support.v8.renderscript.Script.FieldID;
import android.support.v8.renderscript.Script.KernelID;

public class ScriptIntrinsicYuvToRGBThunker extends ScriptIntrinsicYuvToRGB {
    ScriptIntrinsicYuvToRGB mN;

    /* access modifiers changed from: 0000 */
    public ScriptIntrinsicYuvToRGB getNObj() {
        return this.mN;
    }

    private ScriptIntrinsicYuvToRGBThunker(int id, RenderScript rs) {
        super(id, rs);
    }

    public static ScriptIntrinsicYuvToRGBThunker create(RenderScript rs, Element e) {
        RenderScriptThunker rst = (RenderScriptThunker) rs;
        ElementThunker et = (ElementThunker) e;
        ScriptIntrinsicYuvToRGBThunker si = new ScriptIntrinsicYuvToRGBThunker(0, rs);
        si.mN = ScriptIntrinsicYuvToRGB.create(rst.mN, et.getNObj());
        return si;
    }

    public void setInput(Allocation ain) {
        this.mN.setInput(((AllocationThunker) ain).getNObj());
    }

    public void forEach(Allocation aout) {
        this.mN.setInput(((AllocationThunker) aout).getNObj());
    }

    public KernelID getKernelID() {
        KernelID k = createKernelID(0, 2, null, null);
        k.mN = this.mN.getKernelID();
        return k;
    }

    public FieldID getFieldID_Input() {
        FieldID f = createFieldID(0, null);
        f.mN = this.mN.getFieldID_Input();
        return f;
    }
}
