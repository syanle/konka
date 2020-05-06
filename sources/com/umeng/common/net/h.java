package com.umeng.common.net;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/* compiled from: DownloadingService */
class h extends BroadcastReceiver {
    final /* synthetic */ DownloadingService a;

    h(DownloadingService downloadingService) {
        this.a = downloadingService;
    }

    public void onReceive(Context context, Intent intent) {
        this.a.s.a(this.a, DownloadingService.B, DownloadingService.A, intent);
    }
}
