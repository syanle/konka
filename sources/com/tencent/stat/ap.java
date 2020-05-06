package com.tencent.stat;

import android.content.Context;
import com.tencent.stat.a.j;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import org.json.JSONArray;

class ap implements Runnable {
    private Context a = null;
    private Map<String, Integer> b = null;
    private StatSpecifyReportedInfo c = null;

    public ap(Context context, Map<String, Integer> map, StatSpecifyReportedInfo statSpecifyReportedInfo) {
        this.a = context;
        this.c = statSpecifyReportedInfo;
        if (map != null) {
            this.b = map;
        }
    }

    private NetworkMonitor a(String str, int i) {
        NetworkMonitor networkMonitor = new NetworkMonitor();
        Socket socket = new Socket();
        int i2 = 0;
        try {
            networkMonitor.setDomain(str);
            networkMonitor.setPort(i);
            long currentTimeMillis = System.currentTimeMillis();
            InetSocketAddress inetSocketAddress = new InetSocketAddress(str, i);
            socket.connect(inetSocketAddress, 30000);
            networkMonitor.setMillisecondsConsume(System.currentTimeMillis() - currentTimeMillis);
            networkMonitor.setRemoteIp(inetSocketAddress.getAddress().getHostAddress());
            if (socket != null) {
                socket.close();
            }
            if (socket != null) {
                try {
                    socket.close();
                } catch (Throwable th) {
                    StatServiceImpl.q.e(th);
                }
            }
        } catch (IOException e) {
            IOException iOException = e;
            i2 = -1;
            StatServiceImpl.q.e((Throwable) iOException);
            if (socket != null) {
                socket.close();
            }
        } catch (Throwable th2) {
            StatServiceImpl.q.e(th2);
        }
        networkMonitor.setStatusCode(i2);
        return networkMonitor;
    }

    private Map<String, Integer> a() {
        HashMap hashMap = new HashMap();
        String a2 = StatConfig.a("__MTA_TEST_SPEED__", (String) null);
        if (!(a2 == null || a2.trim().length() == 0)) {
            for (String split : a2.split(";")) {
                String[] split2 = split.split(",");
                if (split2 != null && split2.length == 2) {
                    String str = split2[0];
                    if (!(str == null || str.trim().length() == 0)) {
                        try {
                            hashMap.put(str, Integer.valueOf(Integer.valueOf(split2[1]).intValue()));
                        } catch (NumberFormatException e) {
                            StatServiceImpl.q.e((Throwable) e);
                        }
                    }
                }
            }
        }
        return hashMap;
    }

    public void run() {
        try {
            if (this.b == null) {
                this.b = a();
            }
            if (this.b == null || this.b.size() == 0) {
                StatServiceImpl.q.i("empty domain list.");
                return;
            }
            JSONArray jSONArray = new JSONArray();
            for (Entry entry : this.b.entrySet()) {
                String str = (String) entry.getKey();
                if (str == null || str.length() == 0) {
                    StatServiceImpl.q.w("empty domain name.");
                } else if (((Integer) entry.getValue()) == null) {
                    StatServiceImpl.q.w("port is null for " + str);
                } else {
                    jSONArray.put(a((String) entry.getKey(), ((Integer) entry.getValue()).intValue()).toJSONObject());
                }
            }
            if (jSONArray.length() != 0) {
                j jVar = new j(this.a, StatServiceImpl.a(this.a, false, this.c), this.c);
                jVar.a(jSONArray.toString());
                new aq(jVar).a();
            }
        } catch (Throwable th) {
            StatServiceImpl.q.e(th);
        }
    }
}
