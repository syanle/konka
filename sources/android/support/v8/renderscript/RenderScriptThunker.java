package android.support.v8.renderscript;

import android.content.Context;
import android.renderscript.RenderScript;
import android.support.v8.renderscript.RenderScript.Priority;

class RenderScriptThunker extends RenderScript {
    RenderScript mN;

    /* access modifiers changed from: 0000 */
    public void validate() {
        if (this.mN == null) {
            throw new RSInvalidStateException("Calling RS with no Context active.");
        }
    }

    public void setPriority(Priority p) {
        if (p == Priority.LOW) {
            this.mN.setPriority(RenderScript.Priority.LOW);
        }
        if (p == Priority.NORMAL) {
            this.mN.setPriority(RenderScript.Priority.NORMAL);
        }
    }

    RenderScriptThunker(Context ctx) {
        super(ctx);
        isNative = true;
    }

    public static RenderScript create(Context ctx, int sdkVersion) {
        RenderScriptThunker rs = new RenderScriptThunker(ctx);
        rs.mN = RenderScript.create(ctx, sdkVersion);
        return rs;
    }

    public void contextDump() {
        this.mN.contextDump();
    }

    public void finish() {
        this.mN.finish();
    }

    public void destroy() {
        this.mN.destroy();
        this.mN = null;
    }
}
