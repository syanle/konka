package com.tencent.stat;

import android.content.Context;

final class ac implements Runnable {
    final /* synthetic */ Context a;
    final /* synthetic */ int b;

    ac(Context context, int i) {
        this.a = context;
        this.b = i;
    }

    public void run() {
        try {
            StatServiceImpl.flushDataToDB(this.a);
            au.a(this.a).a(this.b);
        } catch (Throwable th) {
            StatServiceImpl.q.e(th);
            StatServiceImpl.a(this.a, th);
        }
    }
}
