package com.tencent.stat;

import android.content.Context;
import com.tencent.stat.a.d;

final class n implements Runnable {
    final /* synthetic */ String a;
    final /* synthetic */ Context b;
    final /* synthetic */ StatSpecifyReportedInfo c;

    n(String str, Context context, StatSpecifyReportedInfo statSpecifyReportedInfo) {
        this.a = str;
        this.b = context;
        this.c = statSpecifyReportedInfo;
    }

    public void run() {
        try {
            if (StatServiceImpl.a(this.a)) {
                StatServiceImpl.q.error((Object) "Error message in StatService.reportError() is empty.");
            } else {
                new aq(new d(this.b, StatServiceImpl.a(this.b, false, this.c), this.a, 0, StatConfig.getMaxReportEventLength(), null, this.c)).a();
            }
        } catch (Throwable th) {
            StatServiceImpl.q.e(th);
            StatServiceImpl.a(this.b, th);
        }
    }
}
