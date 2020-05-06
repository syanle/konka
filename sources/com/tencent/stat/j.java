package com.tencent.stat;

import android.content.Context;
import com.tencent.mid.api.MidService;
import com.tencent.mid.util.Util;
import com.tencent.stat.common.k;

final class j implements Runnable {
    final /* synthetic */ Context a;

    j(Context context) {
        this.a = context;
    }

    public void run() {
        a.a(StatServiceImpl.t).h();
        if (Util.isMidValid(MidService.getLocalMidOnly(this.a))) {
            MidService.getMid(this.a);
        }
        k.a(this.a, true);
        au.a(this.a);
        g.b(this.a);
        StatServiceImpl.r = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(new an());
        if (StatConfig.getStatSendStrategy() == StatReportStrategy.APP_LAUNCH) {
            StatServiceImpl.commitEvents(this.a, -1);
        }
        if (StatConfig.isDebugEnable()) {
            StatServiceImpl.q.d("Init MTA StatService success.");
        }
    }
}
