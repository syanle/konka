package com.umeng.common.net;

import com.konka.kkinterface.tv.ChannelDesk;
import com.umeng.common.Log;
import com.umeng.common.util.e;
import com.umeng.common.util.f;
import com.umeng.common.util.h;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;
import java.util.zip.GZIPInputStream;
import java.util.zip.InflaterInputStream;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.json.JSONException;
import org.json.JSONObject;

/* compiled from: UClient */
public class s {
    private static final String a = s.class.getName();
    private Map<String, String> b;

    public <T extends u> T a(t tVar, Class<T> cls) {
        JSONObject jSONObject;
        String trim = tVar.c().trim();
        b(trim);
        if (t.c.equals(trim)) {
            jSONObject = a(tVar.b());
        } else if (t.b.equals(trim)) {
            jSONObject = a(tVar.d, tVar.a());
        } else {
            jSONObject = null;
        }
        if (jSONObject == null) {
            return null;
        }
        try {
            return (u) cls.getConstructor(new Class[]{JSONObject.class}).newInstance(new Object[]{jSONObject});
        } catch (SecurityException e) {
            Log.b(a, "SecurityException", e);
        } catch (NoSuchMethodException e2) {
            Log.b(a, "NoSuchMethodException", e2);
        } catch (IllegalArgumentException e3) {
            Log.b(a, "IllegalArgumentException", e3);
        } catch (InstantiationException e4) {
            Log.b(a, "InstantiationException", e4);
        } catch (IllegalAccessException e5) {
            Log.b(a, "IllegalAccessException", e5);
        } catch (InvocationTargetException e6) {
            Log.b(a, "InvocationTargetException", e6);
        }
        return null;
    }

    private JSONObject a(String str, JSONObject jSONObject) {
        InputStream inputStream;
        String jSONObject2 = jSONObject.toString();
        int nextInt = new Random().nextInt(ChannelDesk.max_dtv_count);
        Log.c(a, new StringBuilder(String.valueOf(nextInt)).append(":\trequest: ").append(str).append(h.a).append(jSONObject2).toString());
        HttpPost httpPost = new HttpPost(str);
        DefaultHttpClient defaultHttpClient = new DefaultHttpClient(b());
        try {
            if (a()) {
                byte[] a2 = f.a("content=" + jSONObject2, Charset.defaultCharset().toString());
                httpPost.addHeader("Content-Encoding", "deflate");
                httpPost.setEntity(new InputStreamEntity(new ByteArrayInputStream(a2), (long) a2.length));
            } else {
                ArrayList arrayList = new ArrayList(1);
                arrayList.add(new BasicNameValuePair("content", jSONObject2));
                httpPost.setEntity(new UrlEncodedFormEntity(arrayList, e.f));
            }
            HttpResponse execute = defaultHttpClient.execute(httpPost);
            if (execute.getStatusLine().getStatusCode() == 200) {
                HttpEntity entity = execute.getEntity();
                if (entity == null) {
                    return null;
                }
                InputStream content = entity.getContent();
                Header firstHeader = execute.getFirstHeader("Content-Encoding");
                if (firstHeader == null || !firstHeader.getValue().equalsIgnoreCase("deflate")) {
                    inputStream = content;
                } else {
                    inputStream = new InflaterInputStream(content);
                }
                String a3 = a(inputStream);
                Log.a(a, new StringBuilder(String.valueOf(nextInt)).append(":\tresponse: ").append(h.a).append(a3).toString());
                if (a3 == null) {
                    return null;
                }
                return new JSONObject(a3);
            }
            Log.c(a, new StringBuilder(String.valueOf(nextInt)).append(":\tFailed to send message. StatusCode = ").append(execute.getStatusLine().getStatusCode()).append(h.a).append(str).toString());
            return null;
        } catch (ClientProtocolException e) {
            Log.c(a, new StringBuilder(String.valueOf(nextInt)).append(":\tClientProtocolException,Failed to send message.").append(str).toString(), e);
            return null;
        } catch (IOException e2) {
            Log.c(a, new StringBuilder(String.valueOf(nextInt)).append(":\tIOException,Failed to send message.").append(str).toString(), e2);
            return null;
        } catch (JSONException e3) {
            Log.c(a, new StringBuilder(String.valueOf(nextInt)).append(":\tIOException,Failed to send message.").append(str).toString(), e3);
            return null;
        }
    }

    public boolean a() {
        return false;
    }

    private static String a(InputStream inputStream) {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream), 8192);
        StringBuilder sb = new StringBuilder();
        while (true) {
            try {
                String readLine = bufferedReader.readLine();
                if (readLine == null) {
                    try {
                        return sb.toString();
                    } catch (IOException e) {
                        Log.b(a, "Caught IOException in convertStreamToString()", e);
                        return null;
                    }
                } else {
                    sb.append(new StringBuilder(String.valueOf(readLine)).append("\n").toString());
                }
            } catch (IOException e2) {
                Log.b(a, "Caught IOException in convertStreamToString()", e2);
                try {
                    return null;
                } catch (IOException e3) {
                    Log.b(a, "Caught IOException in convertStreamToString()", e3);
                    return null;
                }
            } finally {
                try {
                    inputStream.close();
                } catch (IOException e4) {
                    Log.b(a, "Caught IOException in convertStreamToString()", e4);
                    return null;
                }
            }
        }
    }

    private JSONObject a(String str) {
        InputStream inputStream;
        int nextInt = new Random().nextInt(ChannelDesk.max_dtv_count);
        try {
            String property = System.getProperty("line.separator");
            if (str.length() <= 1) {
                Log.b(a, new StringBuilder(String.valueOf(nextInt)).append(":\tInvalid baseUrl.").toString());
                return null;
            }
            Log.a(a, new StringBuilder(String.valueOf(nextInt)).append(":\tget: ").append(str).toString());
            HttpGet httpGet = new HttpGet(str);
            if (this.b != null && this.b.size() > 0) {
                for (String str2 : this.b.keySet()) {
                    httpGet.addHeader(str2, (String) this.b.get(str2));
                }
            }
            HttpResponse execute = new DefaultHttpClient(b()).execute(httpGet);
            if (execute.getStatusLine().getStatusCode() == 200) {
                HttpEntity entity = execute.getEntity();
                if (entity != null) {
                    InputStream content = entity.getContent();
                    Header firstHeader = execute.getFirstHeader("Content-Encoding");
                    if (firstHeader == null || !firstHeader.getValue().equalsIgnoreCase("gzip")) {
                        if (firstHeader != null) {
                            if (firstHeader.getValue().equalsIgnoreCase("deflate")) {
                                Log.a(a, new StringBuilder(String.valueOf(nextInt)).append("  Use InflaterInputStream get data....").toString());
                                inputStream = new InflaterInputStream(content);
                            }
                        }
                        inputStream = content;
                    } else {
                        Log.a(a, new StringBuilder(String.valueOf(nextInt)).append("  Use GZIPInputStream get data....").toString());
                        inputStream = new GZIPInputStream(content);
                    }
                    String a2 = a(inputStream);
                    Log.a(a, new StringBuilder(String.valueOf(nextInt)).append(":\tresponse: ").append(property).append(a2).toString());
                    if (a2 == null) {
                        return null;
                    }
                    return new JSONObject(a2);
                }
            } else {
                Log.c(a, new StringBuilder(String.valueOf(nextInt)).append(":\tFailed to send message. StatusCode = ").append(execute.getStatusLine().getStatusCode()).append(h.a).append(str).toString());
            }
            return null;
        } catch (ClientProtocolException e) {
            Log.c(a, new StringBuilder(String.valueOf(nextInt)).append(":\tClientProtocolException,Failed to send message.").append(str).toString(), e);
            return null;
        } catch (Exception e2) {
            Log.c(a, new StringBuilder(String.valueOf(nextInt)).append(":\tIOException,Failed to send message.").append(str).toString(), e2);
            return null;
        }
    }

    private HttpParams b() {
        BasicHttpParams basicHttpParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(basicHttpParams, 10000);
        HttpConnectionParams.setSoTimeout(basicHttpParams, 20000);
        HttpProtocolParams.setUserAgent(basicHttpParams, System.getProperty("http.agent"));
        return basicHttpParams;
    }

    public s a(Map<String, String> map) {
        this.b = map;
        return this;
    }

    private void b(String str) {
        if (h.d(str) || !(t.c.equals(str.trim()) ^ t.b.equals(str.trim()))) {
            throw new RuntimeException("验证请求方式失败[" + str + "]");
        }
    }
}
