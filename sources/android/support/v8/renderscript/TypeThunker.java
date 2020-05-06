package android.support.v8.renderscript;

import android.renderscript.Type;
import android.renderscript.Type.Builder;
import java.util.HashMap;

class TypeThunker extends Type {
    static HashMap<Type, Type> mMap = new HashMap<>();
    Type mN;

    /* access modifiers changed from: 0000 */
    public Type getNObj() {
        return this.mN;
    }

    /* access modifiers changed from: 0000 */
    public void internalCalc() {
        this.mDimX = this.mN.getX();
        this.mDimY = this.mN.getY();
        this.mDimZ = this.mN.getZ();
        this.mDimFaces = this.mN.hasFaces();
        this.mDimMipmaps = this.mN.hasMipmaps();
        this.mDimYuv = this.mN.getYuv();
        calcElementCount();
    }

    TypeThunker(RenderScript rs, Type t) {
        super(0, rs);
        this.mN = t;
        internalCalc();
        this.mElement = new ElementThunker(rs, t.getElement());
        synchronized (mMap) {
            mMap.put(this.mN, this);
        }
    }

    static Type find(Type nt) {
        return (Type) mMap.get(nt);
    }

    static Type create(RenderScript rs, Element e, int dx, int dy, int dz, boolean dmip, boolean dfaces, int yuv) {
        Builder tb = new Builder(((RenderScriptThunker) rs).mN, ((ElementThunker) e).mN);
        if (dx > 0) {
            tb.setX(dx);
        }
        if (dy > 0) {
            tb.setY(dy);
        }
        if (dz > 0) {
            tb.setZ(dz);
        }
        if (dmip) {
            tb.setMipmaps(dmip);
        }
        if (dfaces) {
            tb.setFaces(dfaces);
        }
        if (yuv > 0) {
            tb.setYuvFormat(yuv);
        }
        TypeThunker tt = new TypeThunker(rs, tb.create());
        tt.internalCalc();
        return tt;
    }
}
