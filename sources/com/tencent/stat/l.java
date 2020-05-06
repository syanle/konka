package com.tencent.stat;

import android.content.Context;
import com.tencent.stat.common.k;

final class l implements Runnable {
    final /* synthetic */ Context a;

    l(Context context) {
        this.a = context;
    }

    public void run() {
        if (this.a == null) {
            StatServiceImpl.q.error((Object) "The Context of StatService.onStop() can not be null!");
            return;
        }
        StatServiceImpl.flushDataToDB(this.a);
        if (!StatServiceImpl.a()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (k.C(this.a)) {
                if (StatConfig.isDebugEnable()) {
                    StatServiceImpl.q.i("onStop isBackgroundRunning flushDataToDB");
                }
                StatServiceImpl.commitEvents(this.a, -1);
            }
        }
    }
}
