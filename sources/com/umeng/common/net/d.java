package com.umeng.common.net;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.SparseArray;
import android.widget.RemoteViews;
import android.widget.Toast;
import com.cyanogenmod.trebuchet.RocketLauncher.Board.FlyingIcon;
import com.konka.kkinterface.tv.ChannelDesk;
import com.umeng.common.Log;
import com.umeng.common.a.c;
import com.umeng.common.util.DeltaUpdate;
import com.umeng.common.util.h;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/* compiled from: DownloadTool */
class d {
    static final int a = 0;
    static final int b = 1;
    /* access modifiers changed from: private */
    public static final String c = d.class.getName();

    /* compiled from: DownloadTool */
    static class a {
        b a;
        Notification b;
        int c;
        int d;
        C0001a e;
        long[] f = new long[3];

        public a(C0001a aVar, int i) {
            this.c = i;
            this.e = aVar;
        }

        public void a(SparseArray<a> sparseArray) {
            sparseArray.put(this.c, this);
        }

        public void b(SparseArray<a> sparseArray) {
            if (sparseArray.indexOfKey(this.c) >= 0) {
                sparseArray.remove(this.c);
            }
        }
    }

    /* compiled from: DownloadTool */
    class b extends AsyncTask<String, Void, Integer> {
        public int a;
        public String b;
        private C0001a d;
        private Context e;
        private NotificationManager f = ((NotificationManager) this.e.getSystemService("notification"));
        private SparseArray<a> g;
        private Map<C0001a, Messenger> h;

        public b(Context context, int i, C0001a aVar, String str, SparseArray<a> sparseArray, Map<C0001a, Messenger> map) {
            this.e = context.getApplicationContext();
            this.a = i;
            this.d = aVar;
            this.b = str;
            this.g = sparseArray;
            this.h = map;
        }

        /* access modifiers changed from: protected */
        public void onPreExecute() {
            super.onPreExecute();
        }

        /* access modifiers changed from: protected */
        /* renamed from: a */
        public Integer doInBackground(String... strArr) {
            int a2 = DeltaUpdate.a(strArr[0], strArr[1], strArr[2]) + 1;
            new File(strArr[2]).delete();
            if (a2 != 1) {
                Log.a(d.c, "file patch error");
            } else if (!h.a(new File(strArr[1])).equalsIgnoreCase(this.d.e)) {
                Log.a(d.c, "file patch error");
                return Integer.valueOf(0);
            } else {
                Log.a(d.c, "file patch success");
            }
            return Integer.valueOf(a2);
        }

        /* access modifiers changed from: protected */
        /* renamed from: a */
        public void onPostExecute(Integer num) {
            if (num.intValue() == 1) {
                Notification notification = new Notification(17301634, this.e.getString(c.k(this.e)), System.currentTimeMillis());
                Intent intent = new Intent("android.intent.action.VIEW");
                intent.addFlags(268435456);
                intent.setDataAndType(Uri.fromFile(new File(this.b)), "application/vnd.android.package-archive");
                notification.setLatestEventInfo(this.e, com.umeng.common.b.w(this.e), this.e.getString(c.k(this.e)), PendingIntent.getActivity(this.e, 0, intent, 134217728));
                notification.flags = 16;
                this.f.notify(this.a + 1, notification);
                if (d.this.a(this.e)) {
                    this.f.cancel(this.a + 1);
                    this.e.startActivity(intent);
                }
                Bundle bundle = new Bundle();
                bundle.putString("filename", this.b);
                Message obtain = Message.obtain();
                obtain.what = 5;
                obtain.arg1 = 1;
                obtain.arg2 = this.a;
                obtain.setData(bundle);
                try {
                    if (this.h.get(this.d) != null) {
                        ((Messenger) this.h.get(this.d)).send(obtain);
                    }
                    d.this.a(this.e, this.g, this.h, this.a);
                } catch (RemoteException e2) {
                    d.this.a(this.e, this.g, this.h, this.a);
                }
            } else {
                this.f.cancel(this.a + 1);
                Bundle bundle2 = new Bundle();
                bundle2.putString("filename", this.b);
                Message obtain2 = Message.obtain();
                obtain2.what = 5;
                obtain2.arg1 = 3;
                obtain2.arg2 = this.a;
                obtain2.setData(bundle2);
                try {
                    if (this.h.get(this.d) != null) {
                        ((Messenger) this.h.get(this.d)).send(obtain2);
                    }
                    d.this.a(this.e, this.g, this.h, this.a);
                } catch (RemoteException e3) {
                    d.this.a(this.e, this.g, this.h, this.a);
                }
            }
        }
    }

    d() {
    }

    /* access modifiers changed from: 0000 */
    public int a(C0001a aVar) {
        return Math.abs((int) (((long) ((aVar.b.hashCode() >> 2) + (aVar.c.hashCode() >> 3))) + System.currentTimeMillis()));
    }

    /* access modifiers changed from: 0000 */
    public Notification a(Context context, C0001a aVar, int i, int i2) {
        Context applicationContext = context.getApplicationContext();
        Notification notification = new Notification(17301633, applicationContext.getString(c.i(applicationContext)), 1);
        RemoteViews remoteViews = new RemoteViews(applicationContext.getPackageName(), com.umeng.common.a.b.a(applicationContext));
        remoteViews.setProgressBar(com.umeng.common.a.a.b(applicationContext), 100, i2, false);
        remoteViews.setTextViewText(com.umeng.common.a.a.a(applicationContext), new StringBuilder(String.valueOf(i2)).append("%").toString());
        remoteViews.setTextViewText(com.umeng.common.a.a.c(applicationContext), new StringBuilder(String.valueOf(applicationContext.getResources().getString(c.g(applicationContext)))).append(aVar.b).toString());
        notification.contentView = remoteViews;
        notification.when = System.currentTimeMillis();
        notification.contentIntent = PendingIntent.getActivity(applicationContext, 0, new Intent(), 134217728);
        if (aVar.g) {
            notification.flags = 2;
            remoteViews.setOnClickPendingIntent(com.umeng.common.a.a.d(applicationContext), m.b(applicationContext, m.a(i, m.b)));
            remoteViews.setViewVisibility(com.umeng.common.a.a.d(applicationContext), 0);
            b(context, notification, i);
            PendingIntent b2 = m.b(applicationContext, m.a(i, m.c));
            remoteViews.setViewVisibility(com.umeng.common.a.a.f(applicationContext), 0);
            remoteViews.setOnClickPendingIntent(com.umeng.common.a.a.f(applicationContext), b2);
        } else {
            notification.flags = 16;
            remoteViews.setViewVisibility(com.umeng.common.a.a.d(applicationContext), 8);
            remoteViews.setViewVisibility(com.umeng.common.a.a.f(applicationContext), 8);
        }
        return notification;
    }

    /* access modifiers changed from: 0000 */
    public boolean a(Context context) {
        List<RunningAppProcessInfo> runningAppProcesses = ((ActivityManager) context.getSystemService("activity")).getRunningAppProcesses();
        if (runningAppProcesses == null) {
            return false;
        }
        String packageName = context.getPackageName();
        for (RunningAppProcessInfo runningAppProcessInfo : runningAppProcesses) {
            if (runningAppProcessInfo.importance == 100 && runningAppProcessInfo.processName.equals(packageName)) {
                return true;
            }
        }
        return false;
    }

    /* access modifiers changed from: 0000 */
    public boolean a(C0001a aVar, boolean z, Map<C0001a, Messenger> map, Messenger messenger) {
        if (z) {
            int nextInt = new Random().nextInt(ChannelDesk.max_dtv_count);
            if (map != null) {
                for (C0001a aVar2 : map.keySet()) {
                    Log.c(c, "_" + nextInt + " downling  " + aVar2.b + "   " + aVar2.c);
                }
            } else {
                Log.c(c, "_" + nextInt + "downling  null");
            }
        }
        if (map == null) {
            return false;
        }
        for (C0001a aVar3 : map.keySet()) {
            if (aVar.e != null && aVar.e.equals(aVar3.e)) {
                map.put(aVar3, messenger);
                return true;
            } else if (aVar3.c.equals(aVar.c)) {
                map.put(aVar3, messenger);
                return true;
            }
        }
        return false;
    }

    /* access modifiers changed from: 0000 */
    public void a(Context context, Notification notification, int i) {
        Context applicationContext = context.getApplicationContext();
        NotificationManager notificationManager = (NotificationManager) applicationContext.getSystemService("notification");
        int d = com.umeng.common.a.a.d(applicationContext);
        notification.contentView.setTextViewText(d, applicationContext.getResources().getString(c.e(applicationContext.getApplicationContext())));
        notification.contentView.setInt(d, "setBackgroundResource", com.umeng.common.c.a(applicationContext).d("umeng_common_gradient_green"));
        notificationManager.notify(i, notification);
    }

    /* access modifiers changed from: 0000 */
    public void b(Context context, Notification notification, int i) {
        Context applicationContext = context.getApplicationContext();
        NotificationManager notificationManager = (NotificationManager) applicationContext.getSystemService("notification");
        int d = com.umeng.common.a.a.d(applicationContext);
        notification.contentView.setTextViewText(d, applicationContext.getResources().getString(c.d(applicationContext.getApplicationContext())));
        notification.contentView.setInt(d, "setBackgroundResource", com.umeng.common.c.a(applicationContext).d("umeng_common_gradient_orange"));
        notificationManager.notify(i, notification);
    }

    /* access modifiers changed from: 0000 */
    public void a(Context context, SparseArray<a> sparseArray, Map<C0001a, Messenger> map, int i) {
        NotificationManager notificationManager = (NotificationManager) context.getApplicationContext().getSystemService("notification");
        a aVar = (a) sparseArray.get(i);
        if (aVar != null) {
            Log.c(c, "download service clear cache " + aVar.e.b);
            if (aVar.a != null) {
                aVar.a.a(2);
            }
            notificationManager.cancel(aVar.c);
            if (map.containsKey(aVar.e)) {
                map.remove(aVar.e);
            }
            aVar.b(sparseArray);
        }
    }

    /* access modifiers changed from: 0000 */
    public void a(C0001a aVar, long j, long j2, long j3) {
        if (aVar.f != null) {
            HashMap hashMap = new HashMap();
            hashMap.put("dsize", String.valueOf(j));
            hashMap.put("dtime", h.a().split(" ")[1]);
            float f = FlyingIcon.ANGULAR_VMIN;
            if (j2 > 0) {
                f = ((float) j) / ((float) j2);
            }
            hashMap.put("dpcent", String.valueOf((int) (f * 100.0f)));
            hashMap.put("ptimes", String.valueOf(j3));
            a((Map<String, String>) hashMap, false, aVar.f);
        }
    }

    /* access modifiers changed from: 0000 */
    public final void a(Map<String, String> map, boolean z, String[] strArr) {
        new Thread(new e(this, strArr, z, map)).start();
    }

    /* access modifiers changed from: 0000 */
    /* JADX WARNING: No exception handlers in catch block: Catch:{  } */
    public boolean a(DownloadingService downloadingService, SparseArray<a> sparseArray, Map<C0001a, Messenger> map, Intent intent) {
        Context applicationContext;
        int parseInt;
        try {
            applicationContext = downloadingService.getApplicationContext();
            String[] split = intent.getExtras().getString(m.e).split(":");
            parseInt = Integer.parseInt(split[0]);
            String trim = split[1].trim();
            if (parseInt != 0 && !TextUtils.isEmpty(trim) && sparseArray.indexOfKey(parseInt) >= 0) {
                a aVar = (a) sparseArray.get(parseInt);
                b bVar = aVar.a;
                if (m.b.equals(trim)) {
                    if (bVar == null) {
                        Log.c(c, "Receive action do play click.");
                        if (!com.umeng.common.b.a(applicationContext, "android.permission.ACCESS_NETWORK_STATE") || com.umeng.common.b.n(applicationContext)) {
                            downloadingService.getClass();
                            b bVar2 = new b(applicationContext, aVar.e, parseInt, aVar.d, downloadingService.n);
                            aVar.a = bVar2;
                            bVar2.start();
                            b(applicationContext, aVar.b, parseInt);
                            return true;
                        }
                        Toast.makeText(applicationContext, applicationContext.getResources().getString(c.a(applicationContext.getApplicationContext())), 1).show();
                        return false;
                    }
                    Log.c(c, "Receive action do play click.");
                    bVar.a(1);
                    aVar.a = null;
                    a(applicationContext, aVar.b, parseInt);
                    return true;
                } else if (m.c.equals(trim)) {
                    Log.c(c, "Receive action do stop click.");
                    if (bVar != null) {
                        bVar.a(2);
                    } else {
                        a(aVar.e, aVar.f[0], aVar.f[1], aVar.f[2]);
                    }
                    a(applicationContext, sparseArray, map, parseInt);
                    return true;
                }
            }
        } catch (Exception e) {
            a(applicationContext, sparseArray, map, parseInt);
        } catch (Exception e2) {
            e2.printStackTrace();
        } catch (Throwable th) {
            a(applicationContext, sparseArray, map, parseInt);
            throw th;
        }
        return false;
    }
}
