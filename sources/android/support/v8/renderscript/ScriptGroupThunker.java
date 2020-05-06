package android.support.v8.renderscript;

import android.renderscript.ScriptGroup;
import android.support.v8.renderscript.Script.FieldID;
import android.support.v8.renderscript.Script.KernelID;

class ScriptGroupThunker extends ScriptGroup {
    ScriptGroup mN;

    public static final class Builder {
        android.renderscript.ScriptGroup.Builder bN;
        RenderScript mRS;

        Builder(RenderScript rs) {
            RenderScriptThunker rst = (RenderScriptThunker) rs;
            this.mRS = rs;
            this.bN = new android.renderscript.ScriptGroup.Builder(rst.mN);
        }

        public Builder addKernel(KernelID k) {
            this.bN.addKernel(k.mN);
            return this;
        }

        public Builder addConnection(Type t, KernelID from, FieldID to) {
            this.bN.addConnection(((TypeThunker) t).getNObj(), from.mN, to.mN);
            return this;
        }

        public Builder addConnection(Type t, KernelID from, KernelID to) {
            this.bN.addConnection(((TypeThunker) t).getNObj(), from.mN, to.mN);
            return this;
        }

        public ScriptGroupThunker create() {
            ScriptGroupThunker sg = new ScriptGroupThunker(0, this.mRS);
            sg.mN = this.bN.create();
            return sg;
        }
    }

    /* access modifiers changed from: 0000 */
    public ScriptGroup getNObj() {
        return this.mN;
    }

    ScriptGroupThunker(int id, RenderScript rs) {
        super(id, rs);
    }

    public void setInput(KernelID s, Allocation a) {
        this.mN.setInput(s.mN, ((AllocationThunker) a).getNObj());
    }

    public void setOutput(KernelID s, Allocation a) {
        this.mN.setOutput(s.mN, ((AllocationThunker) a).getNObj());
    }

    public void execute() {
        this.mN.execute();
    }
}
