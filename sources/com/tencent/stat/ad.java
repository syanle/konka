package com.tencent.stat;

import android.content.Context;

final class ad implements Runnable {
    final /* synthetic */ Context a;

    ad(Context context) {
        this.a = context;
    }

    public void run() {
        try {
            new Thread(new ap(this.a, null, null), "NetworkMonitorTask").start();
        } catch (Throwable th) {
            StatServiceImpl.q.e(th);
            StatServiceImpl.a(this.a, th);
        }
    }
}
