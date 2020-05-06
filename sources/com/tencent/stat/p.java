package com.tencent.stat;

import android.content.Context;
import com.tencent.stat.a.d;

final class p implements Runnable {
    final /* synthetic */ Throwable a;
    final /* synthetic */ Context b;
    final /* synthetic */ StatSpecifyReportedInfo c;

    p(Throwable th, Context context, StatSpecifyReportedInfo statSpecifyReportedInfo) {
        this.a = th;
        this.b = context;
        this.c = statSpecifyReportedInfo;
    }

    public void run() {
        if (this.a == null) {
            StatServiceImpl.q.error((Object) "The Throwable error message of StatService.reportException() can not be null!");
        } else {
            new aq(new d(this.b, StatServiceImpl.a(this.b, false, this.c), 1, this.a, this.c)).a();
        }
    }
}
