package com.umeng.common.net;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

/* compiled from: NotificationUtils */
public class m {
    public static final String a = "pause";
    public static final String b = "continue";
    public static final String c = "cancel";
    public static final String d = "com.umeng.intent.DOWNLOAD";
    public static final String e = "com.umeng.broadcast.download.msg";

    public static String a(int i, String str) {
        if (i == 0) {
            return null;
        }
        StringBuilder sb = new StringBuilder(new StringBuilder(String.valueOf(i)).toString());
        sb.append(":");
        sb.append(str);
        return sb.toString();
    }

    public static int a(C0001a aVar) {
        int currentTimeMillis = (int) System.currentTimeMillis();
        if (currentTimeMillis < 0) {
            return -currentTimeMillis;
        }
        return currentTimeMillis;
    }

    public static PendingIntent a(Context context, String str) {
        Intent intent = new Intent(d);
        intent.addFlags(1073741824);
        intent.putExtra(e, str);
        return PendingIntent.getBroadcast(context, str.hashCode(), intent, 134217728);
    }

    public static PendingIntent b(Context context, String str) {
        Intent intent = new Intent(context, DownloadingService.class);
        intent.putExtra(e, str);
        return PendingIntent.getService(context, str.hashCode(), intent, 134217728);
    }
}
