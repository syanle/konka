package com.tencent.stat;

import android.content.Context;

final class am implements Runnable {
    final /* synthetic */ Context a;
    final /* synthetic */ StatSpecifyReportedInfo b;

    am(Context context, StatSpecifyReportedInfo statSpecifyReportedInfo) {
        this.a = context;
        this.b = statSpecifyReportedInfo;
    }

    public void run() {
        try {
            StatServiceImpl.a(this.a, false, this.b);
        } catch (Throwable th) {
            StatServiceImpl.q.e(th);
        }
    }
}
