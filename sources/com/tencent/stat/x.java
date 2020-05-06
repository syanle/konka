package com.tencent.stat;

import android.content.Context;
import com.tencent.stat.a.c;

final class x implements Runnable {
    final /* synthetic */ String a;
    final /* synthetic */ c b;
    final /* synthetic */ Context c;

    x(String str, c cVar, Context context) {
        this.a = str;
        this.b = cVar;
        this.c = context;
    }

    public void run() {
        try {
            if (StatServiceImpl.a(this.a)) {
                StatServiceImpl.q.error((Object) "The event_id of StatService.trackCustomBeginEvent() can not be null or empty.");
                return;
            }
            if (StatConfig.isDebugEnable()) {
                StatServiceImpl.q.i("add begin key:" + this.b);
            }
            if (StatServiceImpl.e.containsKey(this.b)) {
                StatServiceImpl.q.warn("Duplicate CustomEvent key: " + this.b.toString() + ", trackCustomBeginKVEvent() repeated?");
            } else if (StatServiceImpl.e.size() <= StatConfig.getMaxParallelTimmingEvents()) {
                StatServiceImpl.e.put(this.b, Long.valueOf(System.currentTimeMillis()));
            } else {
                StatServiceImpl.q.error((Object) "The number of timedEvent exceeds the maximum value " + Integer.toString(StatConfig.getMaxParallelTimmingEvents()));
            }
        } catch (Throwable th) {
            StatServiceImpl.q.e(th);
            StatServiceImpl.a(this.c, th);
        }
    }
}
