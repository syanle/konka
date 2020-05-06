package com.tencent.stat;

import java.util.List;

class i implements Runnable {
    final /* synthetic */ List a;
    final /* synthetic */ StatDispatchCallback b;
    final /* synthetic */ g c;

    i(g gVar, List list, StatDispatchCallback statDispatchCallback) {
        this.c = gVar;
        this.a = list;
        this.b = statDispatchCallback;
    }

    public void run() {
        this.c.a(this.a, this.b);
    }
}
