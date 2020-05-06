package com.tencent.mid.b;

import android.content.Context;
import android.provider.Settings.System;
import com.tencent.mid.util.Util;
import com.tencent.stat.common.StatConstants;

public class e extends f {
    public e(Context context) {
        super(context);
    }

    /* access modifiers changed from: protected */
    public void a(a aVar) {
        synchronized (this) {
            Util.logInfo("write CheckEntity to Settings.System:" + aVar.toString());
            System.putString(this.a.getContentResolver(), g(), aVar.toString());
        }
    }

    /* access modifiers changed from: protected */
    public void a(String str) {
        synchronized (this) {
            Util.logInfo("write mid to Settings.System");
            System.putString(this.a.getContentResolver(), k(), str);
        }
    }

    /* access modifiers changed from: protected */
    public boolean a() {
        return Util.checkPermission(this.a, "android.permission.WRITE_SETTINGS");
    }

    /* access modifiers changed from: protected */
    public String b() {
        String string;
        synchronized (this) {
            Util.logInfo("read mid from Settings.System");
            string = System.getString(this.a.getContentResolver(), k());
        }
        return string;
    }

    /* access modifiers changed from: protected */
    public void c() {
        synchronized (this) {
            System.putString(this.a.getContentResolver(), k(), StatConstants.MTA_COOPERATION_TAG);
            System.putString(this.a.getContentResolver(), g(), StatConstants.MTA_COOPERATION_TAG);
        }
    }

    /* access modifiers changed from: protected */
    public a d() {
        a aVar;
        synchronized (this) {
            aVar = new a(System.getString(this.a.getContentResolver(), g()));
            Util.logInfo("read readCheckEntity from Settings.System:" + aVar.toString());
        }
        return aVar;
    }
}
