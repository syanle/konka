package com.tencent.stat;

import android.content.Context;

final class ah implements Runnable {
    final /* synthetic */ Context a;
    final /* synthetic */ StatSpecifyReportedInfo b;

    ah(Context context, StatSpecifyReportedInfo statSpecifyReportedInfo) {
        this.a = context;
        this.b = statSpecifyReportedInfo;
    }

    public void run() {
        try {
            StatServiceImpl.stopSession();
            StatServiceImpl.a(this.a, true, this.b);
        } catch (Throwable th) {
            StatServiceImpl.q.e(th);
            StatServiceImpl.a(this.a, th);
        }
    }
}
