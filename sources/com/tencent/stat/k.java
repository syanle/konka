package com.tencent.stat;

import android.content.Context;

final class k implements Runnable {
    final /* synthetic */ Context a;
    final /* synthetic */ StatSpecifyReportedInfo b;

    k(Context context, StatSpecifyReportedInfo statSpecifyReportedInfo) {
        this.a = context;
        this.b = statSpecifyReportedInfo;
    }

    public void run() {
        if (this.a == null) {
            StatServiceImpl.q.error((Object) "The Context of StatService.onPause() can not be null!");
        } else {
            StatServiceImpl.trackEndPage(this.a, com.tencent.stat.common.k.h(this.a), this.b);
        }
    }
}
