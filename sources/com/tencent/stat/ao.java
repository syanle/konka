package com.tencent.stat;

import android.content.Context;
import com.tencent.stat.a.d;
import java.io.File;
import java.util.Iterator;

class ao implements Runnable {
    private Context a = null;

    public ao(Context context) {
        this.a = context;
    }

    public void run() {
        Iterator it = StatNativeCrashReport.a(this.a).iterator();
        while (it.hasNext()) {
            File file = (File) it.next();
            d dVar = new d(this.a, StatServiceImpl.a(this.a, false, (StatSpecifyReportedInfo) null), StatNativeCrashReport.a(file), 3, 10240, new Thread(), null);
            dVar.a(StatNativeCrashReport.b(file));
            new aq(dVar).a();
            file.delete();
            if (StatConfig.isDebugEnable()) {
                StatServiceImpl.q.d("delete tombstone file:" + file.getAbsolutePath().toString());
            }
        }
    }
}
