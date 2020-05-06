package com.tencent.stat;

import android.content.Context;

final class m implements Runnable {
    final /* synthetic */ Context a;

    m(Context context) {
        this.a = context;
    }

    public void run() {
        StatServiceImpl.flushDataToDB(this.a);
    }
}
