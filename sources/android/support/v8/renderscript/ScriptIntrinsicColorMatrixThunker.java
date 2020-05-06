package android.support.v8.renderscript;

import android.renderscript.Matrix3f;
import android.renderscript.Matrix4f;
import android.renderscript.ScriptIntrinsicColorMatrix;
import android.support.v8.renderscript.Script.KernelID;

class ScriptIntrinsicColorMatrixThunker extends ScriptIntrinsicColorMatrix {
    ScriptIntrinsicColorMatrix mN;

    /* access modifiers changed from: 0000 */
    public ScriptIntrinsicColorMatrix getNObj() {
        return this.mN;
    }

    private ScriptIntrinsicColorMatrixThunker(int id, RenderScript rs) {
        super(id, rs);
    }

    public static ScriptIntrinsicColorMatrixThunker create(RenderScript rs, Element e) {
        RenderScriptThunker rst = (RenderScriptThunker) rs;
        ElementThunker et = (ElementThunker) e;
        ScriptIntrinsicColorMatrixThunker cm = new ScriptIntrinsicColorMatrixThunker(0, rs);
        cm.mN = ScriptIntrinsicColorMatrix.create(rst.mN, et.getNObj());
        return cm;
    }

    public void setColorMatrix(Matrix4f m) {
        this.mN.setColorMatrix(new Matrix4f(m.getArray()));
    }

    public void setColorMatrix(Matrix3f m) {
        this.mN.setColorMatrix(new Matrix3f(m.getArray()));
    }

    public void setGreyscale() {
        this.mN.setGreyscale();
    }

    public void setYUVtoRGB() {
        this.mN.setYUVtoRGB();
    }

    public void setRGBtoYUV() {
        this.mN.setRGBtoYUV();
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
