package com.tencent.stat;

class ar implements StatDispatchCallback {
    final /* synthetic */ aq a;

    ar(aq aqVar) {
        this.a = aqVar;
    }

    public void onDispatchFailure() {
        StatServiceImpl.d();
    }

    public void onDispatchSuccess() {
        StatServiceImpl.c();
        if (au.b().a() >= StatConfig.getMaxBatchReportCount()) {
            au.b().a(StatConfig.getMaxBatchReportCount());
        }
    }
}
