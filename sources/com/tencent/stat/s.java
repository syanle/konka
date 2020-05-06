package com.tencent.stat;

final class s implements StatDispatchCallback {
    s() {
    }

    public void onDispatchFailure() {
        StatServiceImpl.d();
    }

    public void onDispatchSuccess() {
        StatServiceImpl.c();
    }
}
