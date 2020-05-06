package android.support.v8.renderscript;

import android.content.res.Resources;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.FieldPacker;
import android.renderscript.Script;
import android.renderscript.Script.FieldID;
import android.renderscript.Script.KernelID;
import android.renderscript.ScriptC;
import android.support.v8.renderscript.Script.LaunchOptions;

class ScriptCThunker extends ScriptC {
    private static final String TAG = "ScriptC";

    protected ScriptCThunker(RenderScriptThunker rs, Resources resources, int resourceID) {
        super(rs.mN, resources, resourceID);
    }

    /* access modifiers changed from: 0000 */
    public KernelID thunkCreateKernelID(int slot, int sig, Element ein, Element eout) {
        Element nein = null;
        Element neout = null;
        if (ein != null) {
            nein = ((ElementThunker) ein).mN;
        }
        if (eout != null) {
            neout = ((ElementThunker) eout).mN;
        }
        return createKernelID(slot, sig, nein, neout);
    }

    /* access modifiers changed from: 0000 */
    public void thunkInvoke(int slot) {
        invoke(slot);
    }

    /* access modifiers changed from: 0000 */
    public void thunkBindAllocation(Allocation va, int slot) {
        Allocation nva = null;
        if (va != null) {
            nva = ((AllocationThunker) va).mN;
        }
        bindAllocation(nva, slot);
    }

    /* access modifiers changed from: 0000 */
    public void thunkSetTimeZone(String timeZone) {
        setTimeZone(timeZone);
    }

    /* access modifiers changed from: 0000 */
    public void thunkInvoke(int slot, FieldPacker v) {
        invoke(slot, new FieldPacker(v.getData()));
    }

    /* access modifiers changed from: 0000 */
    public void thunkForEach(int slot, Allocation ain, Allocation aout, FieldPacker v) {
        Allocation nin = null;
        Allocation nout = null;
        FieldPacker nfp = null;
        if (ain != null) {
            nin = ((AllocationThunker) ain).mN;
        }
        if (aout != null) {
            nout = ((AllocationThunker) aout).mN;
        }
        if (v != null) {
            nfp = new FieldPacker(v.getData());
        }
        forEach(slot, nin, nout, nfp);
    }

    /* access modifiers changed from: 0000 */
    public void thunkForEach(int slot, Allocation ain, Allocation aout, FieldPacker v, LaunchOptions sc) {
        Script.LaunchOptions lo = null;
        if (sc != null) {
            lo = new Script.LaunchOptions();
            if (sc.getXEnd() > 0) {
                lo.setX(sc.getXStart(), sc.getXEnd());
            }
            if (sc.getYEnd() > 0) {
                lo.setY(sc.getYStart(), sc.getYEnd());
            }
            if (sc.getZEnd() > 0) {
                lo.setZ(sc.getZStart(), sc.getZEnd());
            }
        }
        Allocation nin = null;
        Allocation nout = null;
        FieldPacker nfp = null;
        if (ain != null) {
            nin = ((AllocationThunker) ain).mN;
        }
        if (aout != null) {
            nout = ((AllocationThunker) aout).mN;
        }
        if (v != null) {
            nfp = new FieldPacker(v.getData());
        }
        forEach(slot, nin, nout, nfp, lo);
    }

    /* access modifiers changed from: 0000 */
    public void thunkSetVar(int index, float v) {
        setVar(index, v);
    }

    /* access modifiers changed from: 0000 */
    public void thunkSetVar(int index, double v) {
        setVar(index, v);
    }

    /* access modifiers changed from: 0000 */
    public void thunkSetVar(int index, int v) {
        setVar(index, v);
    }

    /* access modifiers changed from: 0000 */
    public void thunkSetVar(int index, long v) {
        setVar(index, v);
    }

    /* access modifiers changed from: 0000 */
    public void thunkSetVar(int index, boolean v) {
        setVar(index, v);
    }

    /* access modifiers changed from: 0000 */
    public void thunkSetVar(int index, BaseObj o) {
        if (o == null) {
            setVar(index, 0);
        } else {
            setVar(index, o.getNObj());
        }
    }

    /* access modifiers changed from: 0000 */
    public void thunkSetVar(int index, FieldPacker v) {
        setVar(index, new FieldPacker(v.getData()));
    }

    /* access modifiers changed from: 0000 */
    public void thunkSetVar(int index, FieldPacker v, Element e, int[] dims) {
        setVar(index, new FieldPacker(v.getData()), ((ElementThunker) e).mN, dims);
    }

    /* access modifiers changed from: 0000 */
    public FieldID thunkCreateFieldID(int slot, Element e) {
        return createFieldID(slot, ((ElementThunker) e).getNObj());
    }
}
