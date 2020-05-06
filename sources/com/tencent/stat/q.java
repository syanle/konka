package com.tencent.stat;

import android.content.Context;

final class q implements Runnable {
    final /* synthetic */ Context a;

    q(Context context) {
        this.a = context;
    }

    public void run() {
        if (this.a == null) {
            StatServiceImpl.q.error((Object) "The Context of StatService.reportNativeCrash() can not be null!");
            return;
        }
        try {
            new Thread(new ao(this.a), "NativeCrashRepoter").start();
        } catch (Throwable th) {
            StatServiceImpl.q.e(th);
            StatServiceImpl.a(this.a, th);
        }
    }
}
