package android.support.v8.renderscript;

public class BaseObj {
    private boolean mDestroyed = false;
    private int mID;
    RenderScript mRS;

    BaseObj(int id, RenderScript rs) {
        rs.validate();
        this.mRS = rs;
        this.mID = id;
    }

    /* access modifiers changed from: 0000 */
    public void setID(int id) {
        if (this.mID != 0) {
            throw new RSRuntimeException("Internal Error, reset of object ID.");
        }
        this.mID = id;
    }

    /* access modifiers changed from: 0000 */
    public int getID(RenderScript rs) {
        this.mRS.validate();
        if (RenderScript.isNative) {
            RenderScriptThunker renderScriptThunker = (RenderScriptThunker) rs;
            if (getNObj() != null) {
                return getNObj().hashCode();
            }
        }
        if (this.mDestroyed) {
            throw new RSInvalidStateException("using a destroyed object.");
        } else if (this.mID == 0) {
            throw new RSRuntimeException("Internal error: Object id 0.");
        } else if (rs == null || rs == this.mRS) {
            return this.mID;
        } else {
            throw new RSInvalidStateException("using object with mismatched context.");
        }
    }

    /* access modifiers changed from: 0000 */
    public android.renderscript.BaseObj getNObj() {
        return null;
    }

    /* access modifiers changed from: 0000 */
    public void checkValid() {
        if (this.mID == 0 && getNObj() == null) {
            throw new RSIllegalArgumentException("Invalid object.");
        }
    }

    /* access modifiers changed from: protected */
    public void finalize() throws Throwable {
        if (!this.mDestroyed) {
            if (this.mID != 0 && this.mRS.isAlive()) {
                this.mRS.nObjDestroy(this.mID);
            }
            this.mRS = null;
            this.mID = 0;
            this.mDestroyed = true;
        }
        super.finalize();
    }

    public synchronized void destroy() {
        if (this.mDestroyed) {
            throw new RSInvalidStateException("Object already destroyed.");
        }
        this.mDestroyed = true;
        this.mRS.nObjDestroy(this.mID);
    }

    public int hashCode() {
        return this.mID;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        if (this.mID != ((BaseObj) obj).mID) {
            return false;
        }
        return true;
    }
}
