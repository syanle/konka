package com.umeng.common.net;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import com.umeng.common.Log;
import com.umeng.common.a.c;
import com.umeng.common.b;
import com.umeng.common.util.DeltaUpdate;
import java.io.File;

/* compiled from: DownloadingService */
class f extends Handler {
    final /* synthetic */ DownloadingService a;

    f(DownloadingService downloadingService) {
        this.a = downloadingService;
    }

    public void handleMessage(Message message) {
        switch (message.what) {
            case 5:
                C0001a aVar = (C0001a) message.obj;
                int i = message.arg2;
                try {
                    String string = message.getData().getString("filename");
                    Log.c(DownloadingService.q, "Cancel old notification....");
                    Notification notification = new Notification(17301634, this.a.x.getString(c.j(this.a.x)), System.currentTimeMillis());
                    Intent intent = new Intent("android.intent.action.VIEW");
                    intent.addFlags(268435456);
                    intent.setDataAndType(Uri.fromFile(new File(string)), "application/vnd.android.package-archive");
                    notification.setLatestEventInfo(this.a.x, aVar.b, this.a.x.getString(c.j(this.a.x)), PendingIntent.getActivity(this.a.x, 0, intent, 134217728));
                    notification.flags = 16;
                    this.a.r = (NotificationManager) this.a.getSystemService("notification");
                    this.a.r.notify(i + 1, notification);
                    Log.c(DownloadingService.q, "Show new  notification....");
                    boolean a2 = this.a.s.a(this.a.x);
                    Log.c(DownloadingService.q, String.format("isAppOnForeground = %1$B", new Object[]{Boolean.valueOf(a2)}));
                    if (a2) {
                        this.a.r.cancel(i + 1);
                        this.a.x.startActivity(intent);
                    }
                    Log.a(DownloadingService.q, String.format("%1$10s downloaded. Saved to: %2$s", new Object[]{aVar.b, string}));
                    return;
                } catch (Exception e) {
                    Log.b(DownloadingService.q, "can not install. " + e.getMessage());
                    this.a.r.cancel(i + 1);
                    return;
                }
            case 6:
                C0001a aVar2 = (C0001a) message.obj;
                int i2 = message.arg2;
                String string2 = message.getData().getString("filename");
                this.a.r.cancel(i2);
                Notification notification2 = new Notification(17301633, this.a.x.getString(c.l(this.a.x)), System.currentTimeMillis());
                notification2.setLatestEventInfo(this.a.x, b.w(this.a.x), this.a.x.getString(c.l(this.a.x)), PendingIntent.getActivity(this.a.x, 0, new Intent(), 134217728));
                this.a.r.notify(i2 + 1, notification2);
                String replace = string2.replace(".patch", ".apk");
                String a3 = DeltaUpdate.a(this.a);
                d a4 = this.a.s;
                a4.getClass();
                new b(this.a.x, i2, aVar2, replace, DownloadingService.B, DownloadingService.A).execute(new String[]{a3, replace, string2});
                return;
            default:
                return;
        }
    }
}
