package com.umeng.analytics;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import com.tencent.stat.common.StatConstants;
import com.umeng.analytics.a.f;
import com.umeng.analytics.onlineconfig.c;
import com.umeng.common.Log;
import com.umeng.common.util.e;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.json.JSONObject;

/* compiled from: PolicyManager */
public abstract class d implements i, c {
    private final a a = new a();
    /* access modifiers changed from: private */
    public final Object b = new Object();
    private final Handler c;
    protected final c d = new c();
    protected final f e = new f();
    protected final int f = 1;
    protected final int g = 2;
    protected final int h = 3;
    protected final int i = 4;
    protected final int j = 5;
    String k = null;
    String l = null;
    private final String m = "body";
    private final String n = "header";
    private int o = -1;
    private long p = -1;
    private long q = -1;
    private long r = -1;
    private boolean s = false;

    /* compiled from: PolicyManager */
    private final class a implements Runnable {
        private Context b;

        a(Context context) {
            this.b = context.getApplicationContext();
        }

        public void run() {
            try {
                synchronized (d.this.b) {
                    d.this.d.a(this.b);
                }
            } catch (Exception e) {
                Log.b(g.q, "Exception occurred in ReportMessageHandler", e);
            } catch (Error e2) {
                Log.b(g.q, "Error : " + e2.getMessage());
                try {
                    j.j(this.b);
                } catch (Exception e3) {
                    e3.printStackTrace();
                }
            }
        }
    }

    /* compiled from: PolicyManager */
    private final class b implements Runnable {
        private Context b;

        b(Context context) {
            this.b = context.getApplicationContext();
        }

        public void run() {
            try {
                synchronized (d.this.b) {
                    d.this.d(this.b);
                }
            } catch (Exception e) {
                Log.b(g.q, "Exception occurred in ReportMessageHandler", e);
            } catch (Error e2) {
                Log.b(g.q, "Error : " + e2.getMessage());
                try {
                    j.j(this.b);
                } catch (Exception e3) {
                    e3.printStackTrace();
                }
            }
        }
    }

    public d() {
        HandlerThread handlerThread = new HandlerThread(g.q);
        handlerThread.start();
        this.c = new Handler(handlerThread.getLooper());
    }

    public void e(Context context) {
        a(context, 1);
    }

    public synchronized void a(Context context, int i2) {
        if (!this.s && i2 == 4) {
            a(context);
            f(context);
            this.s = true;
        }
        if (i2 == 5) {
            this.d.a(context);
        } else {
            if (b(context, i2)) {
                this.c.post(new b(context));
            }
            if (this.d.b()) {
                this.c.post(new a(context));
            }
        }
    }

    private void a(Context context) {
        if (this.o == -1) {
            int[] i2 = j.i(context);
            this.o = i2[0];
            this.p = (long) i2[1];
            if (this.o == 4 || this.o == 6) {
                this.q = j.e(context).getLong(g.F, -1);
            }
        }
    }

    public void f(Context context) {
        this.a.a(context);
        this.a.a((i) this);
    }

    private void b(Context context) {
        if (this.o == 6 || this.o == 4) {
            j.e(context).edit().putLong(g.F, this.q).commit();
        }
        if (this.r != -1) {
            this.e.f = this.r;
            j.c(context).edit().putLong(g.H, this.r).commit();
        }
    }

    /* access modifiers changed from: 0000 */
    public boolean b(Context context, int i2) {
        if (!com.umeng.common.b.n(context)) {
            return false;
        }
        switch (i2) {
            case 1:
                break;
            case 2:
                return true;
            case 4:
                if (this.o == 1) {
                    return true;
                }
                break;
            default:
                return false;
        }
        if (this.o == 0) {
            return true;
        }
        if (this.o == 6 && System.currentTimeMillis() - this.q > this.p) {
            this.q = System.currentTimeMillis();
            return true;
        } else if (this.o == 4 && System.currentTimeMillis() - this.q > g.g) {
            this.q = System.currentTimeMillis();
            return true;
        } else if (this.o != 5 || !com.umeng.common.b.l(context)) {
            return false;
        } else {
            return true;
        }
    }

    private String c(Context context) {
        if (!this.e.b()) {
            this.e.b(context, this.l, this.k);
        }
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(this.e.w);
        stringBuffer.append("/");
        stringBuffer.append(this.e.x);
        stringBuffer.append(" ");
        try {
            StringBuffer stringBuffer2 = new StringBuffer();
            stringBuffer2.append(context.getPackageManager().getApplicationLabel(context.getApplicationInfo()).toString());
            stringBuffer2.append("/");
            stringBuffer2.append(this.e.t);
            stringBuffer2.append(" ");
            stringBuffer2.append(this.e.g);
            stringBuffer2.append("/");
            stringBuffer2.append(this.e.i);
            stringBuffer2.append(" ");
            stringBuffer2.append(this.e.d);
            stringBuffer.append(URLEncoder.encode(stringBuffer2.toString(), e.f));
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        return stringBuffer.toString();
    }

    /* access modifiers changed from: private */
    public void d(Context context) {
        JSONObject g2 = g(context);
        if (g2 != null && !g2.isNull("body")) {
            String str = null;
            for (String a2 : g.r) {
                str = a(context, g2, a2);
                if (str != null) {
                    break;
                }
            }
            if (str != null) {
                j.j(context);
                Log.a(g.q, "send applog succeed :" + str);
            } else {
                this.q = -1;
                j.b(context, g2, com.umeng.common.b.d(context));
                Log.a(g.q, "send applog failed");
            }
            b(context);
        }
    }

    /* access modifiers changed from: 0000 */
    public JSONObject g(Context context) {
        JSONObject jSONObject = new JSONObject();
        try {
            if (!this.e.b()) {
                this.e.b(context, this.l, this.k);
            }
            if (!this.e.a()) {
                Log.b(g.q, "protocol Header need Appkey or Device ID ,Please check AndroidManifest.xml ");
                return null;
            }
            this.d.b(context);
            if (this.d.a() <= 0) {
                Log.c(g.q, "no message to send");
                return null;
            } else if (!this.d.c()) {
                throw new Exception("protocol Body has invalid field: " + this.d.d().toString());
            } else {
                jSONObject.put("header", new e(this));
                jSONObject.put("body", this.d.d());
                this.d.e();
                return jSONObject;
            }
        } catch (Exception e2) {
            Log.b(g.q, StatConstants.MTA_COOPERATION_TAG, e2);
            j.j(context);
            return null;
        } catch (Error e3) {
            Log.b(g.q, "Error:" + e3.getMessage());
            j.j(context);
            return null;
        }
    }

    private String a(InputStream inputStream) {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream), 64);
        StringBuilder sb = new StringBuilder();
        while (true) {
            try {
                String readLine = bufferedReader.readLine();
                if (readLine == null) {
                    try {
                        return sb.toString();
                    } catch (IOException e2) {
                        Log.b(g.q, "Caught IOException in convertStreamToString()", e2);
                        return null;
                    }
                } else {
                    sb.append(new StringBuilder(String.valueOf(readLine)).append("\n").toString());
                }
            } catch (IOException e3) {
                Log.b(g.q, "Caught IOException in convertStreamToString()", e3);
                try {
                    return null;
                } catch (IOException e4) {
                    Log.b(g.q, "Caught IOException in convertStreamToString()", e4);
                    return null;
                }
            } finally {
                try {
                    inputStream.close();
                } catch (IOException e5) {
                    Log.b(g.q, "Caught IOException in convertStreamToString()", e5);
                    return null;
                }
            }
        }
    }

    private String a(Context context, JSONObject jSONObject, String str) {
        HttpPost httpPost = new HttpPost(str);
        BasicHttpParams basicHttpParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(basicHttpParams, 10000);
        HttpConnectionParams.setSoTimeout(basicHttpParams, 30000);
        DefaultHttpClient defaultHttpClient = new DefaultHttpClient(basicHttpParams);
        httpPost.addHeader("X-Umeng-Sdk", c(context));
        try {
            String a2 = h.a(context);
            if (a2 != null) {
                defaultHttpClient.getParams().setParameter("http.route.default-proxy", new HttpHost(a2, 80));
            }
            String jSONObject2 = jSONObject.toString();
            Log.a(g.q, jSONObject2);
            if (g.t) {
                byte[] a3 = com.umeng.common.util.f.a("content=" + jSONObject2, "utf-8");
                httpPost.addHeader("Content-Encoding", "deflate");
                httpPost.setEntity(new InputStreamEntity(new ByteArrayInputStream(a3), (long) com.umeng.common.util.f.a));
            } else {
                ArrayList arrayList = new ArrayList(1);
                arrayList.add(new BasicNameValuePair("content", jSONObject2));
                httpPost.setEntity(new UrlEncodedFormEntity(arrayList, e.f));
            }
            Date date = new Date();
            HttpResponse execute = defaultHttpClient.execute(httpPost);
            this.r = new Date().getTime() - date.getTime();
            if (execute.getStatusLine().getStatusCode() != 200) {
                return null;
            }
            Log.a(g.q, "Sent message to " + str);
            HttpEntity entity = execute.getEntity();
            if (entity != null) {
                return a(entity.getContent());
            }
            return null;
        } catch (ClientProtocolException e2) {
            Log.b(g.q, "ClientProtocolException,Failed to send message.", e2);
            return null;
        } catch (IOException e3) {
            Log.b(g.q, "IOException,Failed to send message.", e3);
            return null;
        }
    }

    public void a(int i2, long j2) {
        this.o = i2;
        this.p = j2;
    }
}
