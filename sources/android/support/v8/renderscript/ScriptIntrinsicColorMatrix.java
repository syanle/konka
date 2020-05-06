package android.support.v8.renderscript;

import android.support.v8.renderscript.Script.KernelID;
import com.cyanogenmod.trebuchet.RocketLauncher.Board.FlyingIcon;

public class ScriptIntrinsicColorMatrix extends ScriptIntrinsic {
    private Allocation mInput;
    private final Matrix4f mMatrix = new Matrix4f();

    protected ScriptIntrinsicColorMatrix(int id, RenderScript rs) {
        super(id, rs);
    }

    public static ScriptIntrinsicColorMatrix create(RenderScript rs, Element e) {
        if (RenderScript.isNative) {
            RenderScriptThunker renderScriptThunker = (RenderScriptThunker) rs;
            return ScriptIntrinsicColorMatrixThunker.create(rs, e);
        } else if (e.isCompatible(Element.U8_4(rs))) {
            return new ScriptIntrinsicColorMatrix(rs.nScriptIntrinsicCreate(2, e.getID(rs)), rs);
        } else {
            throw new RSIllegalArgumentException("Unsuported element type.");
        }
    }

    private void setMatrix() {
        FieldPacker fp = new FieldPacker(64);
        fp.addMatrix(this.mMatrix);
        setVar(0, fp);
    }

    public void setColorMatrix(Matrix4f m) {
        this.mMatrix.load(m);
        setMatrix();
    }

    public void setColorMatrix(Matrix3f m) {
        this.mMatrix.load(m);
        setMatrix();
    }

    public void setGreyscale() {
        this.mMatrix.loadIdentity();
        this.mMatrix.set(0, 0, 0.299f);
        this.mMatrix.set(1, 0, 0.587f);
        this.mMatrix.set(2, 0, 0.114f);
        this.mMatrix.set(0, 1, 0.299f);
        this.mMatrix.set(1, 1, 0.587f);
        this.mMatrix.set(2, 1, 0.114f);
        this.mMatrix.set(0, 2, 0.299f);
        this.mMatrix.set(1, 2, 0.587f);
        this.mMatrix.set(2, 2, 0.114f);
        setMatrix();
    }

    public void setYUVtoRGB() {
        this.mMatrix.loadIdentity();
        this.mMatrix.set(0, 0, 1.0f);
        this.mMatrix.set(1, 0, FlyingIcon.ANGULAR_VMIN);
        this.mMatrix.set(2, 0, 1.13983f);
        this.mMatrix.set(0, 1, 1.0f);
        this.mMatrix.set(1, 1, -0.39465f);
        this.mMatrix.set(2, 1, -0.5806f);
        this.mMatrix.set(0, 2, 1.0f);
        this.mMatrix.set(1, 2, 2.03211f);
        this.mMatrix.set(2, 2, FlyingIcon.ANGULAR_VMIN);
        setMatrix();
    }

    public void setRGBtoYUV() {
        this.mMatrix.loadIdentity();
        this.mMatrix.set(0, 0, 0.299f);
        this.mMatrix.set(1, 0, 0.587f);
        this.mMatrix.set(2, 0, 0.114f);
        this.mMatrix.set(0, 1, -0.14713f);
        this.mMatrix.set(1, 1, -0.28886f);
        this.mMatrix.set(2, 1, 0.436f);
        this.mMatrix.set(0, 2, 0.615f);
        this.mMatrix.set(1, 2, -0.51499f);
        this.mMatrix.set(2, 2, -0.10001f);
        setMatrix();
    }

    public void forEach(Allocation ain, Allocation aout) {
        forEach(0, ain, aout, null);
    }

    public KernelID getKernelID() {
        return createKernelID(0, 3, null, null);
    }
}
