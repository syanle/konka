package com.umeng.common.net;

import com.konka.kkinterface.tv.ChannelDesk;
import com.umeng.common.Log;
import com.umeng.common.util.h;
import java.io.IOException;
import java.util.Map;
import java.util.Random;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;

/* compiled from: DownloadTool */
class e implements Runnable {
    final /* synthetic */ d a;
    private final /* synthetic */ String[] b;
    private final /* synthetic */ boolean c;
    private final /* synthetic */ Map d;

    e(d dVar, String[] strArr, boolean z, Map map) {
        this.a = dVar;
        this.b = strArr;
        this.c = z;
        this.d = map;
    }

    public void run() {
        int nextInt = new Random().nextInt(ChannelDesk.max_dtv_count);
        if (this.b == null) {
            Log.a(d.c, new StringBuilder(String.valueOf(nextInt)).append("service report: urls is null").toString());
            return;
        }
        String[] strArr = this.b;
        int length = strArr.length;
        int i = 0;
        while (i < length) {
            String str = strArr[i];
            String a2 = h.a();
            String str2 = a2.split(" ")[0];
            String str3 = a2.split(" ")[1];
            long currentTimeMillis = System.currentTimeMillis();
            StringBuilder sb = new StringBuilder(str);
            sb.append("&data=").append(str2);
            sb.append("&time=").append(str3);
            sb.append("&ts=").append(currentTimeMillis);
            if (this.c) {
                sb.append("&action_type=").append(1);
            } else {
                sb.append("&action_type=").append(-2);
            }
            if (this.d != null) {
                for (String str4 : this.d.keySet()) {
                    sb.append("&").append(str4).append("=").append((String) this.d.get(str4));
                }
            }
            try {
                Log.a(d.c, new StringBuilder(String.valueOf(nextInt)).append(": service report:\tget: ").append(sb.toString()).toString());
                HttpGet httpGet = new HttpGet(sb.toString());
                BasicHttpParams basicHttpParams = new BasicHttpParams();
                HttpConnectionParams.setConnectionTimeout(basicHttpParams, 10000);
                HttpConnectionParams.setSoTimeout(basicHttpParams, 20000);
                HttpResponse execute = new DefaultHttpClient(basicHttpParams).execute(httpGet);
                Log.a(d.c, new StringBuilder(String.valueOf(nextInt)).append(": service report:status code:  ").append(execute.getStatusLine().getStatusCode()).toString());
                if (execute.getStatusLine().getStatusCode() != 200) {
                    i++;
                } else {
                    return;
                }
            } catch (ClientProtocolException e) {
                Log.c(d.c, new StringBuilder(String.valueOf(nextInt)).append(": service report:\tClientProtocolException,Failed to send message.").append(str).toString(), e);
            } catch (IOException e2) {
                Log.c(d.c, new StringBuilder(String.valueOf(nextInt)).append(": service report:\tIOException,Failed to send message.").append(str).toString(), e2);
            }
        }
    }
}
