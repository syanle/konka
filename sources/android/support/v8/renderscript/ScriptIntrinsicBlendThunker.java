package android.support.v8.renderscript;

import android.renderscript.ScriptIntrinsicBlend;
import android.support.v8.renderscript.Script.KernelID;

class ScriptIntrinsicBlendThunker extends ScriptIntrinsicBlend {
    ScriptIntrinsicBlend mN;

    /* access modifiers changed from: 0000 */
    public ScriptIntrinsicBlend getNObj() {
        return this.mN;
    }

    ScriptIntrinsicBlendThunker(int id, RenderScript rs) {
        super(id, rs);
    }

    public static ScriptIntrinsicBlendThunker create(RenderScript rs, Element e) {
        RenderScriptThunker rst = (RenderScriptThunker) rs;
        ElementThunker et = (ElementThunker) e;
        ScriptIntrinsicBlendThunker blend = new ScriptIntrinsicBlendThunker(0, rs);
        blend.mN = ScriptIntrinsicBlend.create(rst.mN, et.getNObj());
        return blend;
    }

    public void forEachClear(Allocation ain, Allocation aout) {
        this.mN.forEachClear(((AllocationThunker) ain).getNObj(), ((AllocationThunker) aout).getNObj());
    }

    public KernelID getKernelIDClear() {
        KernelID k = createKernelID(0, 3, null, null);
        k.mN = this.mN.getKernelIDClear();
        return k;
    }

    public void forEachSrc(Allocation ain, Allocation aout) {
        this.mN.forEachSrc(((AllocationThunker) ain).getNObj(), ((AllocationThunker) aout).getNObj());
    }

    public KernelID getKernelIDSrc() {
        KernelID k = createKernelID(1, 3, null, null);
        k.mN = this.mN.getKernelIDSrc();
        return k;
    }

    public void forEachDst(Allocation ain, Allocation aout) {
        this.mN.forEachDst(((AllocationThunker) ain).getNObj(), ((AllocationThunker) aout).getNObj());
    }

    public KernelID getKernelIDDst() {
        KernelID k = createKernelID(2, 3, null, null);
        k.mN = this.mN.getKernelIDDst();
        return k;
    }

    public void forEachSrcOver(Allocation ain, Allocation aout) {
        this.mN.forEachSrcOver(((AllocationThunker) ain).getNObj(), ((AllocationThunker) aout).getNObj());
    }

    public KernelID getKernelIDSrcOver() {
        KernelID k = createKernelID(3, 3, null, null);
        k.mN = this.mN.getKernelIDSrcOver();
        return k;
    }

    public void forEachDstOver(Allocation ain, Allocation aout) {
        this.mN.forEachDstOver(((AllocationThunker) ain).getNObj(), ((AllocationThunker) aout).getNObj());
    }

    public KernelID getKernelIDDstOver() {
        KernelID k = createKernelID(4, 3, null, null);
        k.mN = this.mN.getKernelIDDstOver();
        return k;
    }

    public void forEachSrcIn(Allocation ain, Allocation aout) {
        this.mN.forEachSrcIn(((AllocationThunker) ain).getNObj(), ((AllocationThunker) aout).getNObj());
    }

    public KernelID getKernelIDSrcIn() {
        KernelID k = createKernelID(5, 3, null, null);
        k.mN = this.mN.getKernelIDSrcIn();
        return k;
    }

    public void forEachDstIn(Allocation ain, Allocation aout) {
        this.mN.forEachDstIn(((AllocationThunker) ain).getNObj(), ((AllocationThunker) aout).getNObj());
    }

    public KernelID getKernelIDDstIn() {
        KernelID k = createKernelID(6, 3, null, null);
        k.mN = this.mN.getKernelIDDstIn();
        return k;
    }

    public void forEachSrcOut(Allocation ain, Allocation aout) {
        this.mN.forEachSrcOut(((AllocationThunker) ain).getNObj(), ((AllocationThunker) aout).getNObj());
    }

    public KernelID getKernelIDSrcOut() {
        KernelID k = createKernelID(7, 3, null, null);
        k.mN = this.mN.getKernelIDSrcOut();
        return k;
    }

    public void forEachDstOut(Allocation ain, Allocation aout) {
        this.mN.forEachDstOut(((AllocationThunker) ain).getNObj(), ((AllocationThunker) aout).getNObj());
    }

    public KernelID getKernelIDDstOut() {
        KernelID k = createKernelID(8, 3, null, null);
        k.mN = this.mN.getKernelIDDstOut();
        return k;
    }

    public void forEachSrcAtop(Allocation ain, Allocation aout) {
        this.mN.forEachSrcAtop(((AllocationThunker) ain).getNObj(), ((AllocationThunker) aout).getNObj());
    }

    public KernelID getKernelIDSrcAtop() {
        KernelID k = createKernelID(9, 3, null, null);
        k.mN = this.mN.getKernelIDSrcAtop();
        return k;
    }

    public void forEachDstAtop(Allocation ain, Allocation aout) {
        this.mN.forEachDstAtop(((AllocationThunker) ain).getNObj(), ((AllocationThunker) aout).getNObj());
    }

    public KernelID getKernelIDDstAtop() {
        KernelID k = createKernelID(10, 3, null, null);
        k.mN = this.mN.getKernelIDDstAtop();
        return k;
    }

    public void forEachXor(Allocation ain, Allocation aout) {
        this.mN.forEachXor(((AllocationThunker) ain).getNObj(), ((AllocationThunker) aout).getNObj());
    }

    public KernelID getKernelIDXor() {
        KernelID k = createKernelID(11, 3, null, null);
        k.mN = this.mN.getKernelIDXor();
        return k;
    }

    public void forEachMultiply(Allocation ain, Allocation aout) {
        this.mN.forEachMultiply(((AllocationThunker) ain).getNObj(), ((AllocationThunker) aout).getNObj());
    }

    public KernelID getKernelIDMultiply() {
        KernelID k = createKernelID(14, 3, null, null);
        k.mN = this.mN.getKernelIDMultiply();
        return k;
    }

    public void forEachAdd(Allocation ain, Allocation aout) {
        this.mN.forEachAdd(((AllocationThunker) ain).getNObj(), ((AllocationThunker) aout).getNObj());
    }

    public KernelID getKernelIDAdd() {
        KernelID k = createKernelID(34, 3, null, null);
        k.mN = this.mN.getKernelIDAdd();
        return k;
    }

    public void forEachSubtract(Allocation ain, Allocation aout) {
        this.mN.forEachSubtract(((AllocationThunker) ain).getNObj(), ((AllocationThunker) aout).getNObj());
    }

    public KernelID getKernelIDSubtract() {
        KernelID k = createKernelID(35, 3, null, null);
        k.mN = this.mN.getKernelIDSubtract();
        return k;
    }
}
