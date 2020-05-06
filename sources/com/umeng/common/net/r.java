package com.umeng.common.net;

import java.io.File;

/* compiled from: ResUtil */
class r implements Runnable {
    private final /* synthetic */ File a;
    private final /* synthetic */ long b;

    r(File file, long j) {
        this.a = file;
        this.b = j;
    }

    public void run() {
        q.b(this.a, this.b);
        q.g = null;
    }
}
