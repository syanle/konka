package com.tencent.stat;

import com.tencent.stat.a.e;

class ay implements Runnable {
    final /* synthetic */ e a;
    final /* synthetic */ StatDispatchCallback b;
    final /* synthetic */ boolean c;
    final /* synthetic */ boolean d;
    final /* synthetic */ au e;

    ay(au auVar, e eVar, StatDispatchCallback statDispatchCallback, boolean z, boolean z2) {
        this.e = auVar;
        this.a = eVar;
        this.b = statDispatchCallback;
        this.c = z;
        this.d = z2;
    }

    public void run() {
        this.e.b(this.a, this.b, this.c, this.d);
    }
}
