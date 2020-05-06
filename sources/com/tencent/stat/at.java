package com.tencent.stat;

class at implements StatDispatchCallback {
    final /* synthetic */ aq a;

    at(aq aqVar) {
        this.a = aqVar;
    }

    public void onDispatchFailure() {
        au.b().a(this.a.a, (StatDispatchCallback) null, this.a.c, true);
        StatServiceImpl.d();
    }

    public void onDispatchSuccess() {
        StatServiceImpl.c();
        if (au.b().a > 0) {
            StatServiceImpl.commitEvents(this.a.d, -1);
        }
    }
}
