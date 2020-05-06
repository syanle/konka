package com.tencent.stat;

import android.content.Context;

final class ak implements Runnable {
    final /* synthetic */ StatAccount a;
    final /* synthetic */ Context b;
    final /* synthetic */ StatSpecifyReportedInfo c;

    ak(StatAccount statAccount, Context context, StatSpecifyReportedInfo statSpecifyReportedInfo) {
        this.a = statAccount;
        this.b = context;
        this.c = statSpecifyReportedInfo;
    }

    public void run() {
        if (this.a == null || this.a.getAccount().trim().length() == 0) {
            StatServiceImpl.q.w("account is null or empty.");
            return;
        }
        StatConfig.setQQ(this.b, this.a.getAccount());
        StatServiceImpl.b(this.b, this.a, this.c);
    }
}
