package com.tencent.stat;

import com.tencent.stat.a.d;
import com.tencent.stat.a.e;
import java.lang.Thread.UncaughtExceptionHandler;

class an implements UncaughtExceptionHandler {
    an() {
    }

    public void uncaughtException(Thread thread, Throwable th) {
        if (StatConfig.isEnableStatService() && StatServiceImpl.t != null) {
            if (StatConfig.isAutoExceptionCaught()) {
                au.a(StatServiceImpl.t).a((e) new d(StatServiceImpl.t, StatServiceImpl.a(StatServiceImpl.t, false, (StatSpecifyReportedInfo) null), 2, th, thread, null), (StatDispatchCallback) null, false, true);
                StatServiceImpl.q.debug("MTA has caught the following uncaught exception:");
                StatServiceImpl.q.error(th);
            }
            StatServiceImpl.flushDataToDB(StatServiceImpl.t);
            if (StatServiceImpl.r != null) {
                StatServiceImpl.q.d("Call the original uncaught exception handler.");
                if (!(StatServiceImpl.r instanceof an)) {
                    StatServiceImpl.r.uncaughtException(thread, th);
                }
            }
        }
    }
}
