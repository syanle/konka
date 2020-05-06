package com.umeng.common.net;

import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import com.umeng.common.Log;

/* compiled from: DownloadAgent */
public class a {
    /* access modifiers changed from: private */
    public static final String b = a.class.getName();
    final Messenger a = new Messenger(new b());
    /* access modifiers changed from: private */
    public Context c;
    /* access modifiers changed from: private */
    public l d;
    /* access modifiers changed from: private */
    public Messenger e;
    /* access modifiers changed from: private */
    public String f;
    /* access modifiers changed from: private */
    public String g;
    /* access modifiers changed from: private */
    public String h;
    /* access modifiers changed from: private */
    public String i;
    /* access modifiers changed from: private */
    public String j;
    /* access modifiers changed from: private */
    public String[] k;
    /* access modifiers changed from: private */
    public boolean l = false;
    /* access modifiers changed from: private */
    public ServiceConnection m = new b(this);

    /* renamed from: com.umeng.common.net.a$a reason: collision with other inner class name */
    /* compiled from: DownloadAgent */
    static class C0001a {
        public String a;
        public String b;
        public String c;
        public String d;
        public String e;
        public String[] f = null;
        public boolean g = false;

        public C0001a(String str, String str2, String str3) {
            this.a = str;
            this.b = str2;
            this.c = str3;
        }

        public Bundle a() {
            Bundle bundle = new Bundle();
            bundle.putString("mComponentName", this.a);
            bundle.putString("mTitle", this.b);
            bundle.putString("mUrl", this.c);
            bundle.putString("mMd5", this.d);
            bundle.putString("mTargetMd5", this.e);
            bundle.putStringArray("reporturls", this.f);
            bundle.putBoolean("rich_notification", this.g);
            return bundle;
        }

        public static C0001a a(Bundle bundle) {
            C0001a aVar = new C0001a(bundle.getString("mComponentName"), bundle.getString("mTitle"), bundle.getString("mUrl"));
            aVar.d = bundle.getString("mMd5");
            aVar.e = bundle.getString("mTargetMd5");
            aVar.f = bundle.getStringArray("reporturls");
            aVar.g = bundle.getBoolean("rich_notification");
            return aVar;
        }
    }

    /* compiled from: DownloadAgent */
    class b extends Handler {
        b() {
        }

        public void handleMessage(Message message) {
            try {
                Log.c(a.b, "DownloadAgent.handleMessage(" + message.what + "): ");
                switch (message.what) {
                    case 1:
                        if (a.this.d != null) {
                            a.this.d.a();
                            return;
                        }
                        return;
                    case 2:
                        a.this.d.b(message.arg1);
                        return;
                    case 3:
                        if (a.this.d != null) {
                            a.this.d.a(message.arg1);
                            return;
                        }
                        return;
                    case 5:
                        a.this.c.unbindService(a.this.m);
                        if (a.this.d == null) {
                            return;
                        }
                        if (message.arg1 == 1 || message.arg1 == 3) {
                            a.this.d.a(message.arg1, message.arg2, message.getData().getString("filename"));
                            return;
                        }
                        a.this.d.a(0, 0, null);
                        Log.c(a.b, "DownloadAgent.handleMessage(DownloadingService.DOWNLOAD_COMPLETE_FAIL): ");
                        return;
                    default:
                        super.handleMessage(message);
                        return;
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.c(a.b, "DownloadAgent.handleMessage(" + message.what + "): " + e.getMessage());
            }
            e.printStackTrace();
            Log.c(a.b, "DownloadAgent.handleMessage(" + message.what + "): " + e.getMessage());
        }
    }

    public void a(String str) {
        this.i = str;
    }

    public void b(String str) {
        this.j = str;
    }

    public void a(String[] strArr) {
        this.k = strArr;
    }

    public void a(boolean z) {
        this.l = z;
    }

    public a(Context context, String str, String str2, String str3, l lVar) {
        this.c = context.getApplicationContext();
        this.f = str;
        this.g = str2;
        this.h = str3;
        this.d = lVar;
    }

    public void a() {
        this.c.bindService(new Intent(this.c, DownloadingService.class), this.m, 1);
    }
}
