package com.tencent.mid.a;

import com.tencent.mid.api.MidCallback;

class e implements Runnable {
    final /* synthetic */ g a;
    final /* synthetic */ MidCallback b;
    final /* synthetic */ d c;

    e(d dVar, g gVar, MidCallback midCallback) {
        this.c = dVar;
        this.a = gVar;
        this.b = midCallback;
    }

    public void run() {
        this.c.b(this.a, this.b);
    }
}
