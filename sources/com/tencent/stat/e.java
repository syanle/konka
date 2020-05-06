package com.tencent.stat;

import com.tencent.stat.common.k;
import java.util.TimerTask;

class e extends TimerTask {
    final /* synthetic */ d a;

    e(d dVar) {
        this.a = dVar;
    }

    public void run() {
        if (StatConfig.isDebugEnable()) {
            k.b().i("TimerTask run");
        }
        StatServiceImpl.f(this.a.c);
        cancel();
        this.a.a();
    }
}
