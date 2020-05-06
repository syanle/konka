package android.support.v8.renderscript;

import android.support.v8.renderscript.Script.FieldID;
import android.support.v8.renderscript.Script.KernelID;
import com.cyanogenmod.trebuchet.RocketLauncher.Board.FlyingIcon;

public class ScriptIntrinsicBlur extends ScriptIntrinsic {
    private Allocation mInput;
    private final float[] mValues = new float[9];

    protected ScriptIntrinsicBlur(int id, RenderScript rs) {
        super(id, rs);
    }

    public static ScriptIntrinsicBlur create(RenderScript rs, Element e) {
        if (RenderScript.isNative) {
            RenderScriptThunker renderScriptThunker = (RenderScriptThunker) rs;
            return ScriptIntrinsicBlurThunker.create(rs, e);
        } else if (e.isCompatible(Element.U8_4(rs)) || e.isCompatible(Element.U8(rs))) {
            ScriptIntrinsicBlur sib = new ScriptIntrinsicBlur(rs.nScriptIntrinsicCreate(5, e.getID(rs)), rs);
            sib.setRadius(5.0f);
            return sib;
        } else {
            throw new RSIllegalArgumentException("Unsuported element type.");
        }
    }

    public void setInput(Allocation ain) {
        this.mInput = ain;
        setVar(1, (BaseObj) ain);
    }

    public void setRadius(float radius) {
        if (radius <= FlyingIcon.ANGULAR_VMIN || radius > 25.0f) {
            throw new RSIllegalArgumentException("Radius out of range (0 < r <= 25).");
        }
        setVar(0, radius);
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
