package com.tencent.stat;

import android.content.Context;
import java.util.Map;

final class ae implements Runnable {
    final /* synthetic */ Context a;
    final /* synthetic */ Map b;
    final /* synthetic */ StatSpecifyReportedInfo c;

    ae(Context context, Map map, StatSpecifyReportedInfo statSpecifyReportedInfo) {
        this.a = context;
        this.b = map;
        this.c = statSpecifyReportedInfo;
    }

    public void run() {
        try {
            new Thread(new ap(this.a, this.b, this.c), "NetworkMonitorTask").start();
        } catch (Throwable th) {
            StatServiceImpl.q.e(th);
            StatServiceImpl.a(this.a, th);
        }
    }
}
