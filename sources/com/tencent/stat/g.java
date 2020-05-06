package com.tencent.stat;

import android.content.Context;
import com.tencent.mid.api.MidEntity;
import com.tencent.mid.util.Util;
import com.tencent.stat.common.StatLogger;
import com.tencent.stat.common.e;
import com.tencent.stat.common.f;
import com.tencent.stat.common.k;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.InputStream;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.GZIPOutputStream;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

class g {
    private static StatLogger d = k.b();
    private static g e = null;
    private static Context f = null;
    DefaultHttpClient a = null;
    e b = null;
    StringBuilder c = new StringBuilder(4096);
    private long g = 0;

    private g(Context context) {
        try {
            f = context.getApplicationContext();
            this.g = System.currentTimeMillis() / 1000;
            this.b = new e();
            if (StatConfig.isDebugEnable()) {
                try {
                    Logger.getLogger("org.apache.http.wire").setLevel(Level.FINER);
                    Logger.getLogger("org.apache.http.headers").setLevel(Level.FINER);
                    System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.SimpleLog");
                    System.setProperty("org.apache.commons.logging.simplelog.showdatetime", "true");
                    System.setProperty("org.apache.commons.logging.simplelog.log.httpclient.wire", "debug");
                    System.setProperty("org.apache.commons.logging.simplelog.log.org.apache.http", "debug");
                    System.setProperty("org.apache.commons.logging.simplelog.log.org.apache.http.headers", "debug");
                } catch (Throwable th) {
                }
            }
            BasicHttpParams basicHttpParams = new BasicHttpParams();
            HttpConnectionParams.setStaleCheckingEnabled(basicHttpParams, false);
            HttpConnectionParams.setConnectionTimeout(basicHttpParams, 10000);
            HttpConnectionParams.setSoTimeout(basicHttpParams, 10000);
            this.a = new DefaultHttpClient(basicHttpParams);
            this.a.setKeepAliveStrategy(new h(this));
        } catch (Throwable th2) {
            d.e(th2);
        }
    }

    static Context a() {
        return f;
    }

    static void a(Context context) {
        f = context.getApplicationContext();
    }

    private void a(JSONObject jSONObject) {
        try {
            String optString = jSONObject.optString(MidEntity.TAG_MID);
            if (Util.isMidValid(optString)) {
                if (StatConfig.isDebugEnable()) {
                    d.i("update mid:" + optString);
                }
                Util.updateIfLocalInvalid(f, optString);
            }
            if (!jSONObject.isNull("cfg")) {
                StatConfig.a(f, jSONObject.getJSONObject("cfg"));
            }
            if (!jSONObject.isNull("ncts")) {
                int i = jSONObject.getInt("ncts");
                int currentTimeMillis = (int) (((long) i) - (System.currentTimeMillis() / 1000));
                if (StatConfig.isDebugEnable()) {
                    d.i("server time:" + i + ", diff time:" + currentTimeMillis);
                }
                k.z(f);
                k.a(f, currentTimeMillis);
            }
        } catch (Throwable th) {
            d.w(th);
        }
    }

    static g b(Context context) {
        if (e == null) {
            synchronized (g.class) {
                if (e == null) {
                    e = new g(context);
                }
            }
        }
        return e;
    }

    /* access modifiers changed from: 0000 */
    public void a(com.tencent.stat.a.e eVar, StatDispatchCallback statDispatchCallback) {
        b(Arrays.asList(new String[]{eVar.g()}), statDispatchCallback);
    }

    /* access modifiers changed from: 0000 */
    public void a(List<?> list, StatDispatchCallback statDispatchCallback) {
        boolean z = false;
        if (list != null && !list.isEmpty()) {
            int size = list.size();
            list.get(0);
            try {
                this.c.delete(0, this.c.length());
                this.c.append("[");
                String str = "rc4";
                for (int i = 0; i < size; i++) {
                    this.c.append(list.get(i).toString());
                    if (i != size - 1) {
                        this.c.append(",");
                    }
                }
                this.c.append("]");
                String sb = this.c.toString();
                int length = sb.length();
                String str2 = StatConfig.getStatReportUrl() + "/?index=" + this.g;
                this.g++;
                if (StatConfig.isDebugEnable()) {
                    d.i("[" + str2 + "]Send request(" + length + "bytes), content:" + sb);
                }
                HttpPost httpPost = new HttpPost(str2);
                httpPost.addHeader("Accept-Encoding", "gzip");
                httpPost.setHeader("Connection", "Keep-Alive");
                httpPost.removeHeaders("Cache-Control");
                HttpHost a2 = a.a(f).a();
                httpPost.addHeader("Content-Encoding", str);
                if (a2 == null) {
                    this.a.getParams().removeParameter("http.route.default-proxy");
                } else {
                    if (StatConfig.isDebugEnable()) {
                        d.d("proxy:" + a2.toHostString());
                    }
                    httpPost.addHeader("X-Content-Encoding", str);
                    this.a.getParams().setParameter("http.route.default-proxy", a2);
                    httpPost.addHeader("X-Online-Host", StatConfig.k);
                    httpPost.addHeader("Accept", "*/*");
                    httpPost.addHeader("Content-Type", "json");
                }
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(length);
                byte[] bytes = sb.getBytes(com.umeng.common.util.e.f);
                int length2 = bytes.length;
                if (length > StatConfig.o) {
                    z = true;
                }
                if (z) {
                    httpPost.removeHeaders("Content-Encoding");
                    String str3 = str + ",gzip";
                    httpPost.addHeader("Content-Encoding", str3);
                    if (a2 != null) {
                        httpPost.removeHeaders("X-Content-Encoding");
                        httpPost.addHeader("X-Content-Encoding", str3);
                    }
                    byteArrayOutputStream.write(new byte[4]);
                    GZIPOutputStream gZIPOutputStream = new GZIPOutputStream(byteArrayOutputStream);
                    gZIPOutputStream.write(bytes);
                    gZIPOutputStream.close();
                    bytes = byteArrayOutputStream.toByteArray();
                    ByteBuffer.wrap(bytes, 0, 4).putInt(length2);
                    if (StatConfig.isDebugEnable()) {
                        d.d("before Gzip:" + length2 + " bytes, after Gzip:" + bytes.length + " bytes");
                    }
                }
                httpPost.setEntity(new ByteArrayEntity(f.a(bytes)));
                HttpResponse execute = this.a.execute(httpPost);
                HttpEntity entity = execute.getEntity();
                int statusCode = execute.getStatusLine().getStatusCode();
                long contentLength = entity.getContentLength();
                if (StatConfig.isDebugEnable()) {
                    d.i("http recv response status code:" + statusCode + ", content length:" + contentLength);
                }
                if (contentLength <= 0) {
                    if (statusCode != 200) {
                        d.error((Object) "Server response no error.");
                        if (statDispatchCallback != null) {
                            statDispatchCallback.onDispatchFailure();
                        }
                    } else if (statDispatchCallback != null) {
                        statDispatchCallback.onDispatchSuccess();
                    }
                    EntityUtils.toString(entity);
                    return;
                }
                if (contentLength > 0) {
                    InputStream content = entity.getContent();
                    DataInputStream dataInputStream = new DataInputStream(content);
                    byte[] bArr = new byte[((int) entity.getContentLength())];
                    dataInputStream.readFully(bArr);
                    content.close();
                    dataInputStream.close();
                    Header firstHeader = execute.getFirstHeader("Content-Encoding");
                    if (firstHeader != null) {
                        if (firstHeader.getValue().equalsIgnoreCase("gzip,rc4")) {
                            bArr = f.b(k.a(bArr));
                        } else if (firstHeader.getValue().equalsIgnoreCase("rc4,gzip")) {
                            bArr = k.a(f.b(bArr));
                        } else if (firstHeader.getValue().equalsIgnoreCase("gzip")) {
                            bArr = k.a(bArr);
                        } else if (firstHeader.getValue().equalsIgnoreCase("rc4")) {
                            bArr = f.b(bArr);
                        }
                    }
                    String str4 = new String(bArr, com.umeng.common.util.e.f);
                    if (StatConfig.isDebugEnable()) {
                        d.i("http get response data:" + str4);
                    }
                    JSONObject jSONObject = new JSONObject(str4);
                    if (statusCode == 200) {
                        a(jSONObject);
                        if (statDispatchCallback != null) {
                            if (jSONObject.optInt("ret") == 0) {
                                statDispatchCallback.onDispatchSuccess();
                            } else {
                                d.error((Object) "response error data.");
                                statDispatchCallback.onDispatchFailure();
                            }
                        }
                    } else {
                        d.error((Object) "Server response error code:" + statusCode + ", error:" + new String(bArr, com.umeng.common.util.e.f));
                        if (statDispatchCallback != null) {
                            statDispatchCallback.onDispatchFailure();
                        }
                    }
                    content.close();
                } else {
                    EntityUtils.toString(entity);
                }
                byteArrayOutputStream.close();
                th = null;
                if (th != null) {
                    d.error(th);
                    if (statDispatchCallback != null) {
                        try {
                            statDispatchCallback.onDispatchFailure();
                        } catch (Throwable th) {
                            d.e(th);
                        }
                    }
                    if (th instanceof OutOfMemoryError) {
                        System.gc();
                        this.c = null;
                        this.c = new StringBuilder(2048);
                    } else if ((th instanceof UnknownHostException) || (th instanceof SocketTimeoutException)) {
                    }
                    a.a(f).d();
                }
            } catch (Throwable th2) {
                th = th2;
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public void b(List<?> list, StatDispatchCallback statDispatchCallback) {
        if (this.b != null) {
            this.b.a(new i(this, list, statDispatchCallback));
        }
    }
}
