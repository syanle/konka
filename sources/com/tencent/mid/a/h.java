package com.tencent.mid.a;

import android.content.Context;
import com.tencent.mid.api.MidCallback;
import com.tencent.mid.api.MidConstants;
import com.tencent.mid.api.MidEntity;
import com.tencent.mid.b.a;
import com.tencent.mid.b.g;
import com.tencent.mid.util.Util;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class h implements Runnable {
    private Context a = null;
    private MidCallback b = null;
    private int c = 0;

    public h(Context context, int i, MidCallback midCallback) {
        this.a = context;
        this.c = i;
        this.b = midCallback;
    }

    private void a() {
        MidEntity a2 = g.a(this.a).a((List<Integer>) new ArrayList<Integer>(Arrays.asList(new Integer[]{Integer.valueOf(1)})));
        MidEntity a3 = g.a(this.a).a((List<Integer>) new ArrayList<Integer>(Arrays.asList(new Integer[]{Integer.valueOf(2)})));
        MidEntity a4 = g.a(this.a).a((List<Integer>) new ArrayList<Integer>(Arrays.asList(new Integer[]{Integer.valueOf(4)})));
        if (!Util.equal(a2, a3) || !Util.equal(a2, a4)) {
            MidEntity newerMidEntity = Util.getNewerMidEntity(Util.getNewerMidEntity(a2, a3), Util.getNewerMidEntity(a2, a4));
            Util.logInfo("local mid check failed, redress with mid:" + newerMidEntity.toString());
            g.a(this.a).a(newerMidEntity);
            return;
        }
        Util.logInfo("local mid check passed.");
    }

    private void b() {
        d.a(this.a).a(new g(this.a), (MidCallback) new i(this));
    }

    private void c() {
        a b2 = g.a(this.a).b();
        if (b2 == null) {
            Util.logInfo("CheckEntity is null");
            return;
        }
        int c2 = b2.c() + 1;
        long abs = Math.abs(System.currentTimeMillis() - b2.b());
        Util.logInfo("check entity: " + b2.toString() + ",duration:" + abs);
        if ((c2 <= b2.d() || abs <= a.a) && abs <= ((long) b2.a()) * a.a) {
            b2.b(c2);
            g.a(this.a).a(b2);
            return;
        }
        a();
        b();
    }

    public void run() {
        Util.logInfo("request type:" + this.c);
        switch (this.c) {
            case 1:
                if (Util.isNetworkAvailable(this.a)) {
                    d.a(this.a).a(new g(this.a), this.b);
                    return;
                } else {
                    this.b.onFail(MidConstants.ERROR_NETWORK, "network not available.");
                    return;
                }
            case 2:
                if (Util.isNetworkAvailable(this.a)) {
                    c();
                    return;
                }
                return;
            default:
                Util.logInfo("wrong type:" + this.c);
                return;
        }
    }
}
