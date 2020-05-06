package android.support.v8.renderscript;

import android.support.v8.renderscript.Script.FieldID;
import android.support.v8.renderscript.Script.KernelID;

public class ScriptIntrinsicYuvToRGB extends ScriptIntrinsic {
    private Allocation mInput;

    ScriptIntrinsicYuvToRGB(int id, RenderScript rs) {
        super(id, rs);
    }

    public static ScriptIntrinsicYuvToRGB create(RenderScript rs, Element e) {
        if (!RenderScript.isNative) {
            return new ScriptIntrinsicYuvToRGB(rs.nScriptIntrinsicCreate(6, e.getID(rs)), rs);
        }
        RenderScriptThunker renderScriptThunker = (RenderScriptThunker) rs;
        return ScriptIntrinsicYuvToRGBThunker.create(rs, e);
    }

    public void setInput(Allocation ain) {
        this.mInput = ain;
        setVar(0, (BaseObj) ain);
    }

    public void forEach(Allocation aout) {
        forEach(0, null, aout, null);
    }

    public KernelID getKernelID() {
        return createKernelID(0, 2, null, null);
    }

    public FieldID getFieldID_Input() {
        return createFieldID(0, null);
    }
}
