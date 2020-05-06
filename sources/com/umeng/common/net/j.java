package com.umeng.common.net;

/* compiled from: DownloadingService */
class j implements Runnable {
    final /* synthetic */ DownloadingService a;

    j(DownloadingService downloadingService) {
        this.a = downloadingService;
    }

    public void run() {
        DownloadingService.D = Boolean.valueOf(false);
    }
}
