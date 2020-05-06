package com.tencent.stat;

class as implements StatDispatchCallback {
    final /* synthetic */ aq a;

    as(aq aqVar) {
        this.a = aqVar;
    }

    public void onDispatchFailure() {
        StatServiceImpl.d();
    }

    public void onDispatchSuccess() {
        StatServiceImpl.c();
    }
}
