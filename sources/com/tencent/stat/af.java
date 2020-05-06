package com.tencent.stat;

import android.content.Context;
import com.tencent.stat.a.k;

final class af implements Runnable {
    final /* synthetic */ Context a;
    final /* synthetic */ String b;
    final /* synthetic */ StatSpecifyReportedInfo c;

    af(Context context, String str, StatSpecifyReportedInfo statSpecifyReportedInfo) {
        this.a = context;
        this.b = str;
        this.c = statSpecifyReportedInfo;
    }

    public void run() {
        Long l;
        try {
            StatServiceImpl.flushDataToDB(this.a);
            synchronized (StatServiceImpl.o) {
                l = (Long) StatServiceImpl.o.remove(this.b);
            }
            if (l != null) {
                Long valueOf = Long.valueOf((System.currentTimeMillis() - l.longValue()) / 1000);
                if (valueOf.longValue() <= 0) {
                    valueOf = Long.valueOf(1);
                }
                String j = StatServiceImpl.n;
                if (j != null && j.equals(this.b)) {
                    j = "-";
                }
                k kVar = new k(this.a, j, this.b, StatServiceImpl.a(this.a, false, this.c), valueOf, this.c);
                if (!this.b.equals(StatServiceImpl.m)) {
                    StatServiceImpl.q.warn("Invalid invocation since previous onResume on diff page.");
                }
                new aq(kVar).a();
                StatServiceImpl.n = this.b;
                return;
            }
            StatServiceImpl.q.e((Object) "Starttime for PageID:" + this.b + " not found, lost onResume()?");
        } catch (Throwable th) {
            StatServiceImpl.q.e(th);
            StatServiceImpl.a(this.a, th);
        }
    }
}
