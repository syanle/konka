package com.tencent.mid.a;

import com.tencent.mid.api.MidService;
import com.tencent.mid.util.Util;
import com.tencent.mid.util.h;
import com.umeng.common.util.e;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.GZIPOutputStream;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.util.EntityUtils;

public class b {
    private static DefaultHttpClient a = null;
    private static int b = 50000;

    public static f a(String str) {
        a = a();
        Util.logInfo("http get:" + str);
        HttpGet httpGet = new HttpGet(str);
        HttpHost httpProxy = Util.getHttpProxy(d.a());
        Util.logInfo("proxy==" + (httpProxy == null ? "null" : httpProxy.getHostName()));
        if (httpProxy != null) {
            httpGet.addHeader("X-Online-Host", "pingmid.qq.com");
            httpGet.addHeader("Accept", "*/*");
            httpGet.removeHeaders("X-Content-Encoding");
        } else {
            a.getParams().removeParameter("http.route.default-proxy");
        }
        httpGet.addHeader("Accept-Encoding", "gzip");
        HttpResponse execute = a.execute(httpGet);
        int statusCode = execute.getStatusLine().getStatusCode();
        String a2 = a(execute);
        Util.logInfo("http get response code:" + statusCode + " ,data:" + a2);
        return new f(statusCode, a2);
    }

    public static f a(String str, String str2) {
        a = a();
        Util.logInfo("[" + str + "]Send request(" + str2.length() + "bytes):" + str2);
        HttpPost httpPost = new HttpPost(str);
        httpPost.addHeader("Accept-Encoding", "gzip");
        httpPost.setHeader("Connection", "Keep-Alive");
        httpPost.removeHeaders("Cache-Control");
        HttpHost httpProxy = Util.getHttpProxy(d.a());
        Util.logInfo("proxy==" + (httpProxy == null ? "null" : httpProxy.getHostName()));
        if (httpProxy != null) {
            httpPost.addHeader("X-Online-Host", "pingmid.qq.com");
            httpPost.addHeader("Accept", "*/*");
            httpPost.addHeader("Content-Type", "json");
        } else {
            a.getParams().removeParameter("http.route.default-proxy");
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] bytes = str2.getBytes(e.f);
        int length = bytes.length;
        if (str2.length() >= 256) {
            if (httpProxy == null) {
                httpPost.addHeader("Content-Encoding", "rc4,gzip");
            } else {
                httpPost.addHeader("X-Content-Encoding", "rc4,gzip");
            }
            byteArrayOutputStream.write(new byte[4]);
            GZIPOutputStream gZIPOutputStream = new GZIPOutputStream(byteArrayOutputStream);
            gZIPOutputStream.write(bytes);
            gZIPOutputStream.close();
            bytes = byteArrayOutputStream.toByteArray();
            ByteBuffer.wrap(bytes, 0, 4).putInt(length);
            Util.logInfo("before Gzip:" + length + " bytes, after Gzip:" + bytes.length + " bytes");
        } else if (httpProxy == null) {
            httpPost.addHeader("Content-Encoding", "rc4");
        } else {
            httpPost.addHeader("X-Content-Encoding", "rc4");
        }
        byteArrayOutputStream.close();
        httpPost.setEntity(new ByteArrayEntity(h.a(bytes)));
        HttpResponse execute = a.execute(httpPost);
        HttpEntity entity = execute.getEntity();
        int statusCode = execute.getStatusLine().getStatusCode();
        long contentLength = entity.getContentLength();
        Util.logInfo("recv response status code:" + statusCode + ", content length:" + contentLength);
        String str3 = null;
        if (contentLength <= 0) {
            EntityUtils.toString(entity);
        } else {
            str3 = a(execute);
        }
        Util.logInfo("recv response status code:" + statusCode + ", content :" + str3);
        return new f(statusCode, str3);
    }

    public static String a(Map<String, String> map) {
        StringBuilder sb = new StringBuilder();
        if (!(map == null || map.size() == 0)) {
            int i = 0;
            for (Entry entry : map.entrySet()) {
                int i2 = i + 1;
                sb.append(i == 0 ? "?" : "&");
                sb.append((String) entry.getKey());
                sb.append("=");
                sb.append((String) entry.getValue());
                i = i2;
            }
        }
        return sb.toString();
    }

    private static String a(HttpResponse httpResponse) {
        InputStream content = httpResponse.getEntity().getContent();
        DataInputStream dataInputStream = new DataInputStream(content);
        byte[] bArr = new byte[((int) httpResponse.getEntity().getContentLength())];
        dataInputStream.readFully(bArr);
        content.close();
        dataInputStream.close();
        Header firstHeader = httpResponse.getFirstHeader("Content-Encoding");
        if (firstHeader != null) {
            if (firstHeader.getValue().equalsIgnoreCase("gzip,rc4")) {
                bArr = h.b(Util.deocdeGZipContent(bArr));
            } else if (firstHeader.getValue().equalsIgnoreCase("rc4,gzip")) {
                bArr = Util.deocdeGZipContent(h.b(bArr));
            } else if (firstHeader.getValue().equalsIgnoreCase("gzip")) {
                bArr = Util.deocdeGZipContent(bArr);
            } else if (firstHeader.getValue().equalsIgnoreCase("rc4")) {
                bArr = h.b(bArr);
            }
        }
        return new String(bArr, e.f);
    }

    public static synchronized DefaultHttpClient a() {
        DefaultHttpClient defaultHttpClient;
        synchronized (b.class) {
            if (a == null) {
                if (MidService.isEnableDebug()) {
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
                ConnManagerParams.setTimeout(basicHttpParams, (long) b);
                ConnManagerParams.setMaxConnectionsPerRoute(basicHttpParams, new ConnPerRouteBean(20));
                ConnManagerParams.setMaxTotalConnections(basicHttpParams, 100);
                HttpProtocolParams.setVersion(basicHttpParams, HttpVersion.HTTP_1_1);
                HttpProtocolParams.setContentCharset(basicHttpParams, e.f);
                HttpConnectionParams.setStaleCheckingEnabled(basicHttpParams, false);
                HttpClientParams.setRedirecting(basicHttpParams, true);
                HttpConnectionParams.setSoTimeout(basicHttpParams, b);
                HttpConnectionParams.setConnectionTimeout(basicHttpParams, b);
                HttpConnectionParams.setTcpNoDelay(basicHttpParams, true);
                SchemeRegistry schemeRegistry = new SchemeRegistry();
                schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
                a = new DefaultHttpClient(new ThreadSafeClientConnManager(basicHttpParams, schemeRegistry), basicHttpParams);
                a.getParams().setParameter("http.route.default-proxy", Util.getHttpProxy(d.a()));
                try {
                    a.setKeepAliveStrategy(new c());
                } catch (Throwable th2) {
                    th2.printStackTrace();
                }
            }
            a.getParams().setParameter("http.route.default-proxy", Util.getHttpProxy(d.a()));
            defaultHttpClient = a;
        }
        return defaultHttpClient;
    }

    public static void b() {
        try {
            if (a != null) {
                a.getConnectionManager().shutdown();
                a = null;
                Util.logInfo("close http client.");
            }
        } catch (Throwable th) {
            Util.logWarn(th);
        }
    }
}
