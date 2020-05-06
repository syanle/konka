package com.umeng.common.net;

import android.app.Notification;
import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import com.umeng.common.Log;
import com.umeng.common.a.a;
import com.umeng.common.a.c;

/* compiled from: DownloadingService */
class g implements a {
    final /* synthetic */ DownloadingService a;

    g(DownloadingService downloadingService) {
        this.a = downloadingService;
    }

    public void a(int i) {
        int i2 = 0;
        if (DownloadingService.B.indexOfKey(i) >= 0) {
            a aVar = (a) DownloadingService.B.get(i);
            long[] jArr = aVar.f;
            if (jArr != null && jArr[1] > 0) {
                i2 = (int) ((((float) jArr[0]) / ((float) jArr[1])) * 100.0f);
                if (i2 > 100) {
                    i2 = 99;
                }
            }
            Notification a2 = this.a.s.a((Context) this.a, aVar.e, i, i2);
            aVar.b = a2;
            this.a.r.notify(i, a2);
        }
    }

    public void a(int i, int i2) {
        if (DownloadingService.B.indexOfKey(i) >= 0) {
            a aVar = (a) DownloadingService.B.get(i);
            C0001a aVar2 = aVar.e;
            Notification notification = aVar.b;
            notification.contentView.setProgressBar(a.b(this.a.x), 100, i2, false);
            notification.contentView.setTextViewText(a.a(this.a.x), String.valueOf(i2) + "%");
            this.a.r.notify(i, notification);
            Log.c(DownloadingService.q, String.format("%3$10s Notification: mNotificationId = %1$15s\t|\tprogress = %2$15s", new Object[]{Integer.valueOf(i), Integer.valueOf(i2), aVar2.b}));
        }
    }

    public void a(int i, String str) {
        if (DownloadingService.B.indexOfKey(i) >= 0) {
            a aVar = (a) DownloadingService.B.get(i);
            if (aVar != null) {
                C0001a aVar2 = aVar.e;
                aVar.b.contentView.setTextViewText(a.a(this.a.x), String.valueOf(100) + "%");
                c.a(this.a.x).a(aVar2.a, aVar2.c, 100);
                Bundle bundle = new Bundle();
                bundle.putString("filename", str);
                if (aVar2.a.equalsIgnoreCase("delta_update")) {
                    Message obtain = Message.obtain();
                    obtain.what = 6;
                    obtain.arg1 = 1;
                    obtain.obj = aVar2;
                    obtain.arg2 = i;
                    obtain.setData(bundle);
                    this.a.y.sendMessage(obtain);
                    return;
                }
                Message obtain2 = Message.obtain();
                obtain2.what = 5;
                obtain2.arg1 = 1;
                obtain2.obj = aVar2;
                obtain2.arg2 = i;
                obtain2.setData(bundle);
                this.a.y.sendMessage(obtain2);
                Message obtain3 = Message.obtain();
                obtain3.what = 5;
                obtain3.arg1 = 1;
                obtain3.arg2 = i;
                obtain3.setData(bundle);
                try {
                    if (DownloadingService.A.get(aVar2) != null) {
                        ((Messenger) DownloadingService.A.get(aVar2)).send(obtain3);
                    }
                    this.a.s.a(this.a.x, DownloadingService.B, DownloadingService.A, i);
                } catch (RemoteException e) {
                    this.a.s.a(this.a.x, DownloadingService.B, DownloadingService.A, i);
                }
            }
        }
    }

    public void a(int i, Exception exc) {
        if (DownloadingService.B.indexOfKey(i) >= 0) {
            a aVar = (a) DownloadingService.B.get(i);
            C0001a aVar2 = aVar.e;
            Notification notification = aVar.b;
            notification.contentView.setTextViewText(a.c(this.a.x), aVar2.b + this.a.x.getString(c.h(this.a.x)));
            this.a.r.notify(i, notification);
            this.a.s.a(this.a.x, DownloadingService.B, DownloadingService.A, i);
        }
    }
}
