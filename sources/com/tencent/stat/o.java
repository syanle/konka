package com.tencent.stat;

import android.content.Context;
import com.tencent.stat.a.d;
import com.tencent.stat.a.i;

final class o implements Runnable {
    final /* synthetic */ Context a;
    final /* synthetic */ Throwable b;

    o(Context context, Throwable th) {
        this.a = context;
        this.b = th;
    }

    public void run() {
        try {
            if (StatConfig.isEnableStatService()) {
                new aq(new d(this.a, StatServiceImpl.a(this.a, false, (StatSpecifyReportedInfo) null), 99, this.b, i.a)).a();
            }
        } catch (Throwable th) {
            StatServiceImpl.q.e((Object) "reportSdkSelfException error: " + th);
        }
    }
}
