package com.umeng.common.net;

import android.widget.Toast;

/* compiled from: DownloadingService */
class i implements Runnable {
    final /* synthetic */ DownloadingService a;
    private final /* synthetic */ String b;

    i(DownloadingService downloadingService, String str) {
        this.a = downloadingService;
        this.b = str;
    }

    public void run() {
        Toast.makeText(this.a.x, this.b, 0).show();
    }
}
