package com.tencent.mid.a;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import com.tencent.mid.api.MidCallback;
import com.tencent.mid.api.MidConstants;
import com.tencent.mid.api.MidEntity;
import com.tencent.mid.b.a;
import com.tencent.mid.b.g;
import com.tencent.mid.util.Util;
import com.tencent.mid.util.j;
import com.tencent.stat.common.StatConstants;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONObject;

public class d {
    private static String b = "iikVs3FGzEQ23RaD1JlHsSWSI5Z26m2hX3gO51mH3ag=";
    private static d c = null;
    private static Context d = null;
    Handler a = null;

    private d(Context context) {
        try {
            HandlerThread handlerThread = new HandlerThread("HttpManager");
            handlerThread.start();
            this.a = new Handler(handlerThread.getLooper());
            d = context.getApplicationContext();
        } catch (Throwable th) {
            Util.logWarn(th);
        }
    }

    static Context a() {
        return d;
    }

    public static synchronized d a(Context context) {
        d dVar;
        synchronized (d.class) {
            if (c == null) {
                c = new d(context);
            }
            dVar = c;
        }
        return dVar;
    }

    private String a(f fVar, MidCallback midCallback) {
        int i;
        int i2 = -1;
        int a2 = fVar.a();
        String b2 = fVar.b();
        String str = "0";
        if (a2 == 200) {
            boolean z = false;
            if (Util.isStringValid(b2)) {
                JSONObject jSONObject = new JSONObject(b2);
                if (!jSONObject.isNull(MidEntity.TAG_MID)) {
                    str = jSONObject.optString(MidEntity.TAG_MID);
                    if (Util.isMidValid(str)) {
                        MidEntity midEntity = new MidEntity();
                        midEntity.setMid(str);
                        midEntity.setMac(Util.getWifiMacAddress(d));
                        midEntity.setImei(Util.getImei(d));
                        if (!jSONObject.isNull(MidEntity.TAG_TIMESTAMPS)) {
                            long optLong = jSONObject.optLong(MidEntity.TAG_TIMESTAMPS);
                            if (optLong > 0) {
                                midEntity.setTimestamps(optLong);
                            }
                        } else {
                            midEntity.setTimestamps(System.currentTimeMillis());
                        }
                        Util.logInfo("new mid midEntity:" + midEntity.toString());
                        midCallback.onSuccess(midEntity.toString());
                        g.a(d).a(midEntity);
                        z = true;
                    }
                }
                i = !jSONObject.isNull(a.c) ? jSONObject.getInt(a.c) : -1;
                if (!jSONObject.isNull(a.d)) {
                    i2 = jSONObject.getInt(a.d);
                }
            } else {
                i = -1;
            }
            g.a(d).a(i, i2);
            if (!z) {
                midCallback.onSuccess(g.a(d).a());
            }
        } else {
            String str2 = "Server response error code:" + a2 + ", error:" + b2;
            Util.logInfo(str2);
            midCallback.onFail(a2, str2);
        }
        return str;
    }

    private String b() {
        return Util.decode(b);
    }

    /* access modifiers changed from: private */
    public void b(g gVar, MidCallback midCallback) {
        String str = StatConstants.MTA_COOPERATION_TAG;
        try {
            String httpUrl = Util.getHttpUrl();
            f a2 = b.a(httpUrl);
            if (a2.a() != 200) {
                String str2 = "response code invalid:" + a2.a();
                Util.logInfo(str2);
                midCallback.onFail(a2.a(), str2);
                b.b();
                return;
            }
            int i = 0;
            String str3 = null;
            JSONObject jSONObject = new JSONObject(a2.b());
            if (!jSONObject.isNull("rand")) {
                i = jSONObject.getInt("rand");
                str3 = j.a(Util.getHMAC(b(), String.valueOf(i)));
            }
            if (str3 == null || i == 0) {
                Util.logInfo("hmac == null");
                b.b();
                return;
            }
            HashMap hashMap = new HashMap();
            hashMap.put("k", str3);
            hashMap.put("s", String.valueOf(i));
            String str4 = httpUrl + b.a((Map<String, String>) hashMap);
            f a3 = b.a(str4);
            if (a3.a() != 200) {
                String str5 = "hmac invalid.";
                Util.logInfo(str5);
                midCallback.onFail(a3.a(), str5);
                b.b();
                return;
            }
            JSONObject jSONObject2 = new JSONObject();
            gVar.a(jSONObject2);
            Util.jsonPut(jSONObject2, "rip", Util.getRemoteUrlIp(Util.getHttpUrl()));
            String a4 = a(b.a(str4, "[" + jSONObject2.toString() + "]"), midCallback);
            if (Util.isMidValid(jSONObject2.optString(MidEntity.TAG_MID)) || Util.isMidValid(a4)) {
                b.b();
                return;
            }
            throw new Exception("get Mid failed, something wrong");
        } catch (Throwable th) {
            midCallback.onFail(MidConstants.ERROR_SDK_LOGIC, th.toString());
            Log.e("MID", "request MID  failed", th);
        }
        b.b();
    }

    /* access modifiers changed from: 0000 */
    public void a(g gVar, MidCallback midCallback) {
        if (gVar == null || this.a == null || midCallback == null) {
            if (midCallback != null) {
                midCallback.onFail(MidConstants.ERROR_ARGUMENT, "packet == null || handler == null");
            }
        } else if (Thread.currentThread().getId() == this.a.getLooper().getThread().getId()) {
            b(gVar, midCallback);
        } else {
            this.a.post(new e(this, gVar, midCallback));
        }
    }
}
