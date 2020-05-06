package com.umeng.common.net;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.DeadObjectException;
import android.os.Debug;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.SparseArray;
import android.widget.Toast;
import com.tencent.stat.common.StatConstants;
import com.umeng.common.Log;
import com.umeng.common.util.h;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class DownloadingService extends Service {
    /* access modifiers changed from: private */
    public static Map<C0001a, Messenger> A = new HashMap();
    /* access modifiers changed from: private */
    public static SparseArray<a> B = new SparseArray<>();
    /* access modifiers changed from: private */
    public static Boolean D = Boolean.valueOf(false);
    static final int a = 1;
    static final int b = 2;
    static final int c = 3;
    static final int d = 4;
    static final int e = 5;
    static final int f = 6;
    public static final int g = 0;
    public static final int h = 1;
    public static final int i = 2;
    public static final int j = 3;
    public static final int k = 4;
    static final int l = 100;
    static final String m = "filename";
    public static boolean o = false;
    /* access modifiers changed from: private */
    public static final String q = DownloadingService.class.getName();
    private static final long t = 104857600;
    private static final long u = 10485760;
    private static final long v = 259200000;
    private static final int w = 3;
    private static final long z = 8000;
    private BroadcastReceiver C;
    a n;
    final Messenger p = new Messenger(new c());
    /* access modifiers changed from: private */
    public NotificationManager r;
    /* access modifiers changed from: private */
    public d s = new d();
    /* access modifiers changed from: private */
    public Context x;
    /* access modifiers changed from: private */
    public Handler y;

    interface a {
        void a(int i);

        void a(int i, int i2);

        void a(int i, Exception exc);

        void a(int i, String str);
    }

    class b extends Thread {
        /* access modifiers changed from: private */
        public Context b;
        private boolean c;
        private File d;
        private int e = 0;
        private long f = -1;
        private long g = -1;
        private int h = -1;
        private int i;
        private a j;
        private C0001a k;

        public b(Context context, C0001a aVar, int i2, int i3, a aVar2) {
            try {
                this.b = context;
                this.k = aVar;
                this.e = i3;
                if (DownloadingService.B.indexOfKey(i2) >= 0) {
                    long[] jArr = ((a) DownloadingService.B.get(i2)).f;
                    if (jArr != null && jArr.length > 1) {
                        this.f = jArr[0];
                        this.g = jArr[1];
                    }
                }
                this.j = aVar2;
                this.i = i2;
                boolean[] zArr = new boolean[1];
                this.d = q.a("/apk", context, zArr);
                this.c = zArr[0];
                q.a(this.d, this.c ? DownloadingService.t : DownloadingService.u, (long) DownloadingService.v);
                this.d = new File(this.d, a(this.k));
            } catch (Exception e2) {
                Log.c(DownloadingService.q, e2.getMessage(), e2);
                this.j.a(this.i, e2);
            }
        }

        public void run() {
            boolean z = false;
            this.e = 0;
            try {
                if (this.j != null) {
                    this.j.a(this.i);
                }
                if (this.f > 0) {
                    z = true;
                }
                a(z);
                if (DownloadingService.A.size() <= 0) {
                    DownloadingService.this.stopSelf();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }

        public void a(int i2) {
            this.h = i2;
        }

        /* JADX WARNING: Removed duplicated region for block: B:113:0x0280 A[Catch:{ RemoteException -> 0x02de, all -> 0x0317, all -> 0x0309 }] */
        /* JADX WARNING: Removed duplicated region for block: B:120:0x02ce A[SYNTHETIC, Splitter:B:120:0x02ce] */
        /* JADX WARNING: Removed duplicated region for block: B:123:0x02d3 A[SYNTHETIC, Splitter:B:123:0x02d3] */
        /* JADX WARNING: Removed duplicated region for block: B:135:0x030e A[SYNTHETIC, Splitter:B:135:0x030e] */
        /* JADX WARNING: Removed duplicated region for block: B:138:0x0313 A[SYNTHETIC, Splitter:B:138:0x0313] */
        /* JADX WARNING: Removed duplicated region for block: B:166:0x037d A[SYNTHETIC, Splitter:B:166:0x037d] */
        /* JADX WARNING: Removed duplicated region for block: B:169:0x0382 A[SYNTHETIC, Splitter:B:169:0x0382] */
        /* JADX WARNING: Removed duplicated region for block: B:261:? A[RETURN, SYNTHETIC] */
        /* JADX WARNING: Removed duplicated region for block: B:265:? A[RETURN, SYNTHETIC] */
        private void a(boolean z) {
            IOException iOException;
            InputStream inputStream;
            FileOutputStream fileOutputStream;
            InputStream inputStream2;
            int i2;
            FileOutputStream fileOutputStream2 = null;
            boolean z2 = false;
            String name = this.d.getName();
            try {
                FileOutputStream fileOutputStream3 = new FileOutputStream(this.d, true);
                try {
                    if (this.c || q.a(this.d.getAbsolutePath(), 3)) {
                        fileOutputStream = fileOutputStream3;
                    } else {
                        fileOutputStream3.close();
                        FileOutputStream openFileOutput = this.b.openFileOutput(name, 32771);
                        try {
                            this.d = this.b.getFileStreamPath(name);
                            fileOutputStream = openFileOutput;
                        } catch (IOException e2) {
                            inputStream = null;
                            fileOutputStream2 = openFileOutput;
                            iOException = e2;
                            try {
                                Log.c(DownloadingService.q, iOException.getMessage(), iOException);
                                i2 = this.e + 1;
                                this.e = i2;
                                if (i2 > 3) {
                                }
                                a();
                            } catch (RemoteException e3) {
                                e3.printStackTrace();
                                DownloadingService.this.s.a(this.b, DownloadingService.B, DownloadingService.A, this.i);
                                a((Exception) iOException);
                                DownloadingService.this.y.post(new k(this));
                                if (inputStream != null) {
                                }
                                if (fileOutputStream2 != null) {
                                }
                            } catch (Throwable th) {
                                th = th;
                                fileOutputStream = fileOutputStream2;
                                inputStream2 = inputStream;
                            }
                            if (inputStream != null) {
                            }
                            if (fileOutputStream2 != null) {
                            }
                        } catch (RemoteException e4) {
                            e = e4;
                            fileOutputStream = openFileOutput;
                            inputStream2 = null;
                            try {
                                DownloadingService.this.s.a(this.b, DownloadingService.B, DownloadingService.A, this.i);
                                e.printStackTrace();
                                if (inputStream2 != null) {
                                }
                                if (fileOutputStream != null) {
                                }
                            } catch (Throwable th2) {
                                th = th2;
                                if (inputStream2 != null) {
                                }
                                if (fileOutputStream != null) {
                                }
                                throw th;
                            }
                        } catch (Throwable th3) {
                            th = th3;
                            fileOutputStream = openFileOutput;
                            inputStream2 = null;
                            if (inputStream2 != null) {
                            }
                            if (fileOutputStream != null) {
                            }
                            throw th;
                        }
                    }
                    try {
                        Log.c(DownloadingService.q, String.format("saveAPK: url = %1$15s\t|\tfilename = %2$15s", new Object[]{this.k.c, this.d.getAbsolutePath()}));
                        HttpURLConnection a2 = a(new URL(this.k.c), this.d);
                        a2.connect();
                        inputStream2 = a2.getInputStream();
                        if (!z) {
                            try {
                                this.f = 0;
                                this.g = (long) a2.getContentLength();
                                Log.c(DownloadingService.q, String.format("getContentLength: %1$15s", new Object[]{Long.valueOf(this.g)}));
                            } catch (IOException e5) {
                                iOException = e5;
                                fileOutputStream2 = fileOutputStream;
                                inputStream = inputStream2;
                                Log.c(DownloadingService.q, iOException.getMessage(), iOException);
                                i2 = this.e + 1;
                                this.e = i2;
                                if (i2 > 3) {
                                }
                                a();
                                if (inputStream != null) {
                                }
                                if (fileOutputStream2 != null) {
                                }
                            } catch (RemoteException e6) {
                                e = e6;
                                DownloadingService.this.s.a(this.b, DownloadingService.B, DownloadingService.A, this.i);
                                e.printStackTrace();
                                if (inputStream2 != null) {
                                }
                                if (fileOutputStream != null) {
                                }
                            }
                        }
                        byte[] bArr = new byte[4096];
                        Log.c(DownloadingService.q, new StringBuilder(String.valueOf(this.k.b)).append("saveAPK getContentLength ").append(String.valueOf(this.g)).toString());
                        c.a(this.b).a(this.k.a, this.k.c);
                        int i3 = 0;
                        while (true) {
                            if (this.h >= 0) {
                                break;
                            }
                            int read = inputStream2.read(bArr);
                            if (read <= 0) {
                                break;
                            }
                            fileOutputStream.write(bArr, 0, read);
                            this.f += (long) read;
                            int i4 = i3 + 1;
                            if (i3 % 50 == 0) {
                                if (!com.umeng.common.b.n(this.b)) {
                                    break;
                                }
                                int i5 = (int) ((((float) this.f) * 100.0f) / ((float) this.g));
                                if (i5 > DownloadingService.l) {
                                    i5 = 99;
                                }
                                if (this.j != null) {
                                    this.j.a(this.i, i5);
                                }
                                b(i5);
                                c.a(this.b).a(this.k.a, this.k.c, i5);
                            }
                            i3 = i4;
                        }
                        z2 = true;
                        inputStream2.close();
                        fileOutputStream.close();
                        if (this.h == 1) {
                            a aVar = (a) DownloadingService.B.get(this.i);
                            aVar.f[0] = this.f;
                            aVar.f[1] = this.g;
                            aVar.f[2] = (long) this.e;
                            if (inputStream2 != null) {
                                try {
                                    inputStream2.close();
                                } catch (IOException e7) {
                                    e7.printStackTrace();
                                    if (fileOutputStream != null) {
                                        try {
                                            fileOutputStream.close();
                                            return;
                                        } catch (IOException e8) {
                                            e8.printStackTrace();
                                            return;
                                        }
                                    } else {
                                        return;
                                    }
                                } catch (Throwable th4) {
                                    if (fileOutputStream != null) {
                                        try {
                                            fileOutputStream.close();
                                        } catch (IOException e9) {
                                            e9.printStackTrace();
                                        }
                                    }
                                    throw th4;
                                }
                            }
                            if (fileOutputStream != null) {
                                try {
                                    fileOutputStream.close();
                                } catch (IOException e10) {
                                    e10.printStackTrace();
                                }
                            }
                        } else if (this.h == 2) {
                            DownloadingService.this.s.a(this.k, this.f, this.g, (long) this.e);
                            DownloadingService.this.r.cancel(this.i);
                            if (inputStream2 != null) {
                                try {
                                    inputStream2.close();
                                } catch (IOException e11) {
                                    e11.printStackTrace();
                                    if (fileOutputStream != null) {
                                        try {
                                            fileOutputStream.close();
                                            return;
                                        } catch (IOException e12) {
                                            e12.printStackTrace();
                                            return;
                                        }
                                    } else {
                                        return;
                                    }
                                } catch (Throwable th5) {
                                    if (fileOutputStream != null) {
                                        try {
                                            fileOutputStream.close();
                                        } catch (IOException e13) {
                                            e13.printStackTrace();
                                        }
                                    }
                                    throw th5;
                                }
                            }
                            if (fileOutputStream != null) {
                                try {
                                    fileOutputStream.close();
                                } catch (IOException e14) {
                                    e14.printStackTrace();
                                }
                            }
                        } else {
                            if (!z2) {
                                Log.b(DownloadingService.q, "Download Fail repeat count=" + this.e);
                                ((Messenger) DownloadingService.A.get(this.k)).send(Message.obtain(null, 5, 0, 0));
                                DownloadingService.this.s.a(this.b, DownloadingService.B, DownloadingService.A, this.i);
                                if (this.j != null) {
                                    this.j.a(this.i, (Exception) null);
                                }
                            } else {
                                b();
                                File file = new File(this.d.getParent(), this.d.getName().replace(".tmp", StatConstants.MTA_COOPERATION_TAG));
                                this.d.renameTo(file);
                                String absolutePath = file.getAbsolutePath();
                                a(file, absolutePath);
                                if (this.j != null) {
                                    this.j.a(this.i, absolutePath);
                                }
                            }
                            if (inputStream2 != null) {
                                try {
                                    inputStream2.close();
                                } catch (IOException e15) {
                                    e15.printStackTrace();
                                    if (fileOutputStream != null) {
                                        try {
                                            fileOutputStream.close();
                                            return;
                                        } catch (IOException e16) {
                                            e16.printStackTrace();
                                            return;
                                        }
                                    } else {
                                        return;
                                    }
                                } catch (Throwable th6) {
                                    if (fileOutputStream != null) {
                                        try {
                                            fileOutputStream.close();
                                        } catch (IOException e17) {
                                            e17.printStackTrace();
                                        }
                                    }
                                    throw th6;
                                }
                            }
                            if (fileOutputStream != null) {
                                try {
                                    fileOutputStream.close();
                                } catch (IOException e18) {
                                    e18.printStackTrace();
                                }
                            }
                        }
                    } catch (IOException e19) {
                        iOException = e19;
                        inputStream = null;
                        fileOutputStream2 = fileOutputStream;
                    } catch (RemoteException e20) {
                        e = e20;
                        inputStream2 = null;
                        DownloadingService.this.s.a(this.b, DownloadingService.B, DownloadingService.A, this.i);
                        e.printStackTrace();
                        if (inputStream2 != null) {
                            try {
                                inputStream2.close();
                            } catch (IOException e21) {
                                e21.printStackTrace();
                                if (fileOutputStream != null) {
                                    try {
                                        fileOutputStream.close();
                                        return;
                                    } catch (IOException e22) {
                                        e22.printStackTrace();
                                        return;
                                    }
                                } else {
                                    return;
                                }
                            } catch (Throwable th7) {
                                if (fileOutputStream != null) {
                                    try {
                                        fileOutputStream.close();
                                    } catch (IOException e23) {
                                        e23.printStackTrace();
                                    }
                                }
                                throw th7;
                            }
                        }
                        if (fileOutputStream != null) {
                            try {
                                fileOutputStream.close();
                            } catch (IOException e24) {
                                e24.printStackTrace();
                            }
                        }
                    } catch (Throwable th8) {
                        th = th8;
                        inputStream2 = null;
                        if (inputStream2 != null) {
                            try {
                                inputStream2.close();
                            } catch (IOException e25) {
                                e25.printStackTrace();
                                if (fileOutputStream != null) {
                                    try {
                                        fileOutputStream.close();
                                    } catch (IOException e26) {
                                        e26.printStackTrace();
                                    }
                                }
                            } catch (Throwable th9) {
                                if (fileOutputStream != null) {
                                    try {
                                        fileOutputStream.close();
                                    } catch (IOException e27) {
                                        e27.printStackTrace();
                                    }
                                }
                                throw th9;
                            }
                        }
                        if (fileOutputStream != null) {
                            try {
                                fileOutputStream.close();
                            } catch (IOException e28) {
                                e28.printStackTrace();
                            }
                        }
                        throw th;
                    }
                } catch (IOException e29) {
                    inputStream = null;
                    fileOutputStream2 = fileOutputStream3;
                    iOException = e29;
                    Log.c(DownloadingService.q, iOException.getMessage(), iOException);
                    i2 = this.e + 1;
                    this.e = i2;
                    if (i2 > 3) {
                    }
                    a();
                    if (inputStream != null) {
                    }
                    if (fileOutputStream2 != null) {
                    }
                } catch (RemoteException e30) {
                    e = e30;
                    fileOutputStream = fileOutputStream3;
                    inputStream2 = null;
                    DownloadingService.this.s.a(this.b, DownloadingService.B, DownloadingService.A, this.i);
                    e.printStackTrace();
                    if (inputStream2 != null) {
                    }
                    if (fileOutputStream != null) {
                    }
                } catch (Throwable th10) {
                    th = th10;
                    fileOutputStream = fileOutputStream3;
                    inputStream2 = null;
                    if (inputStream2 != null) {
                    }
                    if (fileOutputStream != null) {
                    }
                    throw th;
                }
            } catch (IOException e31) {
                iOException = e31;
                inputStream = null;
                Log.c(DownloadingService.q, iOException.getMessage(), iOException);
                i2 = this.e + 1;
                this.e = i2;
                if (i2 > 3 || this.k.g) {
                    a();
                } else {
                    Log.b(DownloadingService.q, "Download Fail out of max repeat count");
                    ((Messenger) DownloadingService.A.get(this.k)).send(Message.obtain(null, 5, 0, 0));
                    DownloadingService.this.s.a(this.b, DownloadingService.B, DownloadingService.A, this.i);
                    a((Exception) iOException);
                    DownloadingService.this.y.post(new k(this));
                }
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e32) {
                        e32.printStackTrace();
                        if (fileOutputStream2 != null) {
                            try {
                                fileOutputStream2.close();
                                return;
                            } catch (IOException e33) {
                                e33.printStackTrace();
                                return;
                            }
                        } else {
                            return;
                        }
                    } catch (Throwable th11) {
                        if (fileOutputStream2 != null) {
                            try {
                                fileOutputStream2.close();
                            } catch (IOException e34) {
                                e34.printStackTrace();
                            }
                        }
                        throw th11;
                    }
                }
                if (fileOutputStream2 != null) {
                    try {
                        fileOutputStream2.close();
                    } catch (IOException e35) {
                        e35.printStackTrace();
                    }
                }
            } catch (RemoteException e36) {
                e = e36;
                fileOutputStream = null;
                inputStream2 = null;
                DownloadingService.this.s.a(this.b, DownloadingService.B, DownloadingService.A, this.i);
                e.printStackTrace();
                if (inputStream2 != null) {
                }
                if (fileOutputStream != null) {
                }
            } catch (Throwable th12) {
                th = th12;
                fileOutputStream = null;
                inputStream2 = null;
                if (inputStream2 != null) {
                }
                if (fileOutputStream != null) {
                }
                throw th;
            }
        }

        private void a() {
            Log.c(DownloadingService.q, "wait for repeating Test network repeat count=" + this.e);
            try {
                if (!this.k.g) {
                    Thread.sleep(DownloadingService.z);
                    if (this.g < 1) {
                        a(false);
                    } else {
                        a(true);
                    }
                } else {
                    a aVar = (a) DownloadingService.B.get(this.i);
                    aVar.f[0] = this.f;
                    aVar.f[1] = this.g;
                    aVar.f[2] = (long) this.e;
                    String a2 = m.a(this.i, m.b);
                    Intent intent = new Intent(this.b, DownloadingService.class);
                    intent.putExtra(m.e, a2);
                    DownloadingService.this.s.a(DownloadingService.this, DownloadingService.B, DownloadingService.A, intent);
                    DownloadingService.this.a(this.b.getString(com.umeng.common.a.c.c(this.b)));
                    Log.c(DownloadingService.q, "changed play state button on op-notification.");
                }
            } catch (InterruptedException e2) {
                a((Exception) e2);
                DownloadingService.this.s.a(this.b, DownloadingService.B, DownloadingService.A, this.i);
            }
        }

        private void b(int i2) throws RemoteException {
            try {
                if (DownloadingService.A.get(this.k) != null) {
                    ((Messenger) DownloadingService.A.get(this.k)).send(Message.obtain(null, 3, i2, 0));
                }
            } catch (DeadObjectException e2) {
                Log.b(DownloadingService.q, String.format("Service Client for downloading %1$15s is dead. Removing messenger from the service", new Object[]{this.k.b}));
                DownloadingService.A.put(this.k, null);
            }
        }

        private HttpURLConnection a(URL url, File file) throws IOException {
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setRequestProperty("Accept-Encoding", "identity");
            httpURLConnection.addRequestProperty("Connection", "keep-alive");
            httpURLConnection.setConnectTimeout(5000);
            httpURLConnection.setReadTimeout(10000);
            if (file.exists() && file.length() > 0) {
                httpURLConnection.setRequestProperty("Range", "bytes=" + file.length() + "-");
            }
            return httpURLConnection;
        }

        private String a(C0001a aVar) {
            String sb;
            if (this.k.e != null) {
                sb = new StringBuilder(String.valueOf(this.k.e)).append(".apk.tmp").toString();
            } else {
                sb = new StringBuilder(String.valueOf(h.a(this.k.c))).append(".apk.tmp").toString();
            }
            if (this.k.a.equalsIgnoreCase("delta_update")) {
                return sb.replace(".apk", ".patch");
            }
            return sb;
        }

        private void b() {
            if (this.k.f != null) {
                HashMap hashMap = new HashMap();
                hashMap.put("dsize", String.valueOf(this.g));
                hashMap.put("dtime", h.a().split(" ")[1]);
                hashMap.put("ptimes", String.valueOf(this.e));
                DownloadingService.this.s.a((Map<String, String>) hashMap, true, this.k.f);
            }
        }

        private void a(File file, String str) throws RemoteException {
            if (this.k.d != null && !this.k.d.equalsIgnoreCase(h.a(file))) {
                if (this.k.a.equalsIgnoreCase("delta_update")) {
                    DownloadingService.this.r.cancel(this.i);
                    Bundle bundle = new Bundle();
                    bundle.putString(DownloadingService.m, str);
                    Message obtain = Message.obtain();
                    obtain.what = 5;
                    obtain.arg1 = 3;
                    obtain.arg2 = this.i;
                    obtain.setData(bundle);
                    try {
                        if (DownloadingService.A.get(this.k) != null) {
                            ((Messenger) DownloadingService.A.get(this.k)).send(obtain);
                        }
                        DownloadingService.this.s.a(this.b, DownloadingService.B, DownloadingService.A, this.i);
                    } catch (RemoteException e2) {
                        DownloadingService.this.s.a(this.b, DownloadingService.B, DownloadingService.A, this.i);
                    }
                } else {
                    ((Messenger) DownloadingService.A.get(this.k)).send(Message.obtain(null, 5, 0, 0));
                    DownloadingService.this.s.a(this.b, DownloadingService.B, DownloadingService.A, this.i);
                    Notification notification = new Notification(17301634, this.b.getString(com.umeng.common.a.c.h(this.b)), System.currentTimeMillis());
                    notification.setLatestEventInfo(this.b, com.umeng.common.b.w(this.b), new StringBuilder(String.valueOf(this.k.b)).append(this.b.getString(com.umeng.common.a.c.h(this.b))).toString(), PendingIntent.getActivity(this.b, 0, new Intent(), 0));
                    notification.flags |= 16;
                    DownloadingService.this.r.notify(this.i, notification);
                }
            }
        }

        private void a(Exception exc) {
            Log.b(DownloadingService.q, "can not install. " + exc.getMessage());
            if (this.j != null) {
                this.j.a(this.i, exc);
            }
            DownloadingService.this.s.a(this.k, this.f, this.g, (long) this.e);
        }
    }

    class c extends Handler {
        c() {
        }

        public void handleMessage(Message message) {
            Log.c(DownloadingService.q, "IncomingHandler(msg.what:" + message.what + " msg.arg1:" + message.arg1 + " msg.arg2:" + message.arg2 + " msg.replyTo:" + message.replyTo);
            switch (message.what) {
                case 4:
                    Bundle data = message.getData();
                    Log.c(DownloadingService.q, "IncomingHandler(msg.getData():" + data);
                    C0001a a2 = C0001a.a(data);
                    if (DownloadingService.this.s.a(a2, DownloadingService.o, DownloadingService.A, message.replyTo)) {
                        Log.a(DownloadingService.q, a2.b + " is already in downloading list. ");
                        Toast.makeText(DownloadingService.this.x, com.umeng.common.a.c.b(DownloadingService.this.x), 0).show();
                        Message obtain = Message.obtain();
                        obtain.what = 2;
                        obtain.arg1 = 2;
                        obtain.arg2 = 0;
                        try {
                            message.replyTo.send(obtain);
                            return;
                        } catch (RemoteException e) {
                            e.printStackTrace();
                            return;
                        }
                    } else if (com.umeng.common.b.n(DownloadingService.this.getApplicationContext())) {
                        DownloadingService.A.put(a2, message.replyTo);
                        Message obtain2 = Message.obtain();
                        obtain2.what = 1;
                        obtain2.arg1 = 1;
                        obtain2.arg2 = 0;
                        try {
                            message.replyTo.send(obtain2);
                        } catch (RemoteException e2) {
                            e2.printStackTrace();
                        }
                        DownloadingService.this.a(a2);
                        return;
                    } else {
                        Toast.makeText(DownloadingService.this.x, com.umeng.common.a.c.a(DownloadingService.this.x), 0).show();
                        Message obtain3 = Message.obtain();
                        obtain3.what = 2;
                        obtain3.arg1 = 4;
                        obtain3.arg2 = 0;
                        try {
                            message.replyTo.send(obtain3);
                            return;
                        } catch (RemoteException e3) {
                            e3.printStackTrace();
                            return;
                        }
                    }
                default:
                    super.handleMessage(message);
                    return;
            }
        }
    }

    public IBinder onBind(Intent intent) {
        Log.c(q, "onBind ");
        return this.p.getBinder();
    }

    public void onStart(Intent intent, int i2) {
        Log.c(q, "onStart ");
        this.s.a(this, B, A, intent);
        super.onStart(intent, i2);
    }

    public void onCreate() {
        super.onCreate();
        if (o) {
            Log.LOG = true;
            Debug.waitForDebugger();
        }
        Log.c(q, "onCreate ");
        this.r = (NotificationManager) getSystemService("notification");
        this.x = this;
        this.y = new f(this);
        this.n = new g(this);
    }

    private void d() {
        IntentFilter intentFilter = new IntentFilter(m.d);
        this.C = new h(this);
        registerReceiver(this.C, intentFilter);
    }

    /* access modifiers changed from: private */
    public void a(C0001a aVar) {
        Log.c(q, "startDownload([mComponentName:" + aVar.a + " mTitle:" + aVar.b + " mUrl:" + aVar.c + "])");
        int a2 = this.s.a(aVar);
        b bVar = new b(getApplicationContext(), aVar, a2, 0, this.n);
        a aVar2 = new a(aVar, a2);
        aVar2.a(B);
        aVar2.a = bVar;
        bVar.start();
        e();
        if (o) {
            for (int i2 = 0; i2 < B.size(); i2++) {
                Log.c(q, "Running task " + ((a) B.valueAt(i2)).e.b);
            }
        }
    }

    /* access modifiers changed from: private */
    public void a(String str) {
        synchronized (D) {
            if (!D.booleanValue()) {
                Log.c(q, "show single toast.[" + str + "]");
                D = Boolean.valueOf(true);
                this.y.post(new i(this, str));
                this.y.postDelayed(new j(this), 1200);
            }
        }
    }

    public void onDestroy() {
        try {
            c.a(getApplicationContext()).a(259200);
            c.a(getApplicationContext()).finalize();
            if (this.C != null) {
                unregisterReceiver(this.C);
            }
        } catch (Exception e2) {
            Log.b(q, e2.getMessage());
        }
        super.onDestroy();
    }

    private void e() {
        if (o) {
            int size = A.size();
            int size2 = B.size();
            Log.a(q, "Client size =" + size + "   cacheSize = " + size2);
            if (size != size2) {
                throw new RuntimeException("Client size =" + size + "   cacheSize = " + size2);
            }
        }
    }
}
