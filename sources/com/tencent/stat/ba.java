package com.tencent.stat;

import java.util.List;

class ba implements StatDispatchCallback {
    final /* synthetic */ List a;
    final /* synthetic */ boolean b;
    final /* synthetic */ au c;

    ba(au auVar, List list, boolean z) {
        this.c = auVar;
        this.a = list;
        this.b = z;
    }

    public void onDispatchFailure() {
        StatServiceImpl.d();
        this.c.a(this.a, 1, this.b, true);
    }

    public void onDispatchSuccess() {
        StatServiceImpl.c();
        this.c.a(this.a, this.b, true);
    }
}
