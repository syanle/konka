package com.umeng.common.net;

import android.widget.Toast;
import com.umeng.common.a.c;

/* compiled from: DownloadingService */
class k implements Runnable {
    final /* synthetic */ b a;

    k(b bVar) {
        this.a = bVar;
    }

    public void run() {
        Toast.makeText(DownloadingService.this, c.h(this.a.b), 0).show();
    }
}
