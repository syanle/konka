package com.tencent.mid.b;

import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import com.tencent.mid.util.Util;
import com.tencent.stat.common.StatConstants;

public class d extends f {
    public d(Context context) {
        super(context);
    }

    /* access modifiers changed from: protected */
    public void a(a aVar) {
        synchronized (this) {
            Util.logInfo("write CheckEntity to sharedPreferences:" + aVar.toString());
            Editor edit = PreferenceManager.getDefaultSharedPreferences(this.a).edit();
            edit.putString(g(), aVar.toString());
            edit.commit();
        }
    }

    /* access modifiers changed from: protected */
    public void a(String str) {
        synchronized (this) {
            Util.logInfo("write mid to sharedPreferences");
            Editor edit = PreferenceManager.getDefaultSharedPreferences(this.a).edit();
            edit.putString(k(), str);
            edit.commit();
        }
    }

    /* access modifiers changed from: protected */
    public boolean a() {
        return true;
    }

    /* access modifiers changed from: protected */
    public String b() {
        String string;
        synchronized (this) {
            Util.logInfo("read mid from sharedPreferences");
            string = PreferenceManager.getDefaultSharedPreferences(this.a).getString(k(), null);
        }
        return string;
    }

    /* access modifiers changed from: protected */
    public void c() {
        synchronized (this) {
            Editor edit = PreferenceManager.getDefaultSharedPreferences(this.a).edit();
            edit.putString(k(), StatConstants.MTA_COOPERATION_TAG);
            edit.putString(g(), StatConstants.MTA_COOPERATION_TAG);
            edit.commit();
        }
    }

    /* access modifiers changed from: protected */
    public a d() {
        a aVar;
        synchronized (this) {
            aVar = new a(PreferenceManager.getDefaultSharedPreferences(this.a).getString(g(), null));
            Util.logInfo("read CheckEntity from sharedPreferences:" + aVar.toString());
        }
        return aVar;
    }
}
