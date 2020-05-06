package android.support.v8.renderscript;

import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class ScriptC extends Script {
    private static final String TAG = "ScriptC";

    protected ScriptC(int id, RenderScript rs) {
        super(id, rs);
    }

    protected ScriptC(RenderScript rs, Resources resources, int resourceID) {
        super(0, rs);
        if (RenderScript.isNative) {
            this.mT = new ScriptCThunker((RenderScriptThunker) rs, resources, resourceID);
            return;
        }
        int id = internalCreate(rs, resources, resourceID);
        if (id == 0) {
            throw new RSRuntimeException("Loading of ScriptC script failed.");
        }
        setID(id);
    }

    private static synchronized int internalCreate(RenderScript rs, Resources resources, int resourceID) {
        int nScriptCCreate;
        synchronized (ScriptC.class) {
            InputStream is = resources.openRawResource(resourceID);
            try {
                byte[] pgm = new byte[1024];
                int pgmLength = 0;
                while (true) {
                    int bytesLeft = pgm.length - pgmLength;
                    if (bytesLeft == 0) {
                        byte[] buf2 = new byte[(pgm.length * 2)];
                        System.arraycopy(pgm, 0, buf2, 0, pgm.length);
                        pgm = buf2;
                        bytesLeft = pgm.length - pgmLength;
                    }
                    int bytesRead = is.read(pgm, pgmLength, bytesLeft);
                    if (bytesRead <= 0) {
                        is.close();
                        nScriptCCreate = rs.nScriptCCreate(resources.getResourceEntryName(resourceID), rs.getApplicationContext().getCacheDir().toString(), pgm, pgmLength);
                    } else {
                        pgmLength += bytesRead;
                    }
                }
            } catch (IOException e) {
                throw new NotFoundException();
            } catch (Throwable th) {
                is.close();
                throw th;
            }
        }
        return nScriptCCreate;
    }
}
