package android.support.v8.renderscript;

import android.support.v8.renderscript.Script.FieldID;
import android.support.v8.renderscript.Script.KernelID;
import com.cyanogenmod.trebuchet.RocketLauncher.Board.FlyingIcon;

public class ScriptIntrinsicConvolve3x3 extends ScriptIntrinsic {
    private Allocation mInput;
    private final float[] mValues = new float[9];

    ScriptIntrinsicConvolve3x3(int id, RenderScript rs) {
        super(id, rs);
    }

    public static ScriptIntrinsicConvolve3x3 create(RenderScript rs, Element e) {
        if (RenderScript.isNative) {
            RenderScriptThunker renderScriptThunker = (RenderScriptThunker) rs;
            return ScriptIntrinsicConvolve3x3Thunker.create(rs, e);
        }
        float[] f = {FlyingIcon.ANGULAR_VMIN, FlyingIcon.ANGULAR_VMIN, FlyingIcon.ANGULAR_VMIN, FlyingIcon.ANGULAR_VMIN, 1.0f, FlyingIcon.ANGULAR_VMIN, FlyingIcon.ANGULAR_VMIN, FlyingIcon.ANGULAR_VMIN, FlyingIcon.ANGULAR_VMIN};
        if (!e.isCompatible(Element.U8_4(rs))) {
            throw new RSIllegalArgumentException("Unsuported element type.");
        }
        ScriptIntrinsicConvolve3x3 si = new ScriptIntrinsicConvolve3x3(rs.nScriptIntrinsicCreate(1, e.getID(rs)), rs);
        si.setCoefficients(f);
        return si;
    }

    public void setInput(Allocation ain) {
        this.mInput = ain;
        setVar(1, (BaseObj) ain);
    }

    public void setCoefficients(float[] v) {
        FieldPacker fp = new FieldPacker(36);
        for (int ct = 0; ct < this.mValues.length; ct++) {
            this.mValues[ct] = v[ct];
            fp.addF32(this.mValues[ct]);
        }
        setVar(0, fp);
    }

    public void forEach(Allocation aout) {
        forEach(0, null, aout, null);
    }

    public KernelID getKernelID() {
        return createKernelID(0, 2, null, null);
    }

    public FieldID getFieldID_Input() {
        return createFieldID(1, null);
    }
}
