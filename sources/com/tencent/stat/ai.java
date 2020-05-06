package com.tencent.stat;

import android.content.Context;
import com.tencent.stat.common.k;

final class ai implements Runnable {
    final /* synthetic */ Context a;
    final /* synthetic */ StatSpecifyReportedInfo b;

    ai(Context context, StatSpecifyReportedInfo statSpecifyReportedInfo) {
        this.a = context;
        this.b = statSpecifyReportedInfo;
    }

    public void run() {
        if (this.a == null) {
            StatServiceImpl.q.error((Object) "The Context of StatService.onResume() can not be null!");
        } else {
            StatServiceImpl.trackBeginPage(this.a, k.h(this.a), this.b);
        }
    }
}
