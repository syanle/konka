package com.tencent.mid.b;

import android.content.Context;
import com.tencent.mid.api.MidEntity;
import com.tencent.mid.util.Util;

public abstract class f {
    protected Context a = null;

    protected f(Context context) {
        this.a = context;
    }

    private void d(String str) {
        if (a()) {
            a(b(str));
        }
    }

    public static String e() {
        return Util.decode("6X8Y4XdM2Vhvn0I=");
    }

    public static String f() {
        return Util.decode("6X8Y4XdM2Vhvn0KfzcEatGnWaNU=");
    }

    public static String g() {
        return Util.decode("4kU71lN96TJUomD1vOU9lgj9U+kKmxDPLVM+zzjst5U=");
    }

    private String l() {
        if (a()) {
            return c(b());
        }
        return null;
    }

    public void a(MidEntity midEntity) {
        if (midEntity != null) {
            d(midEntity.toString());
        }
    }

    /* access modifiers changed from: protected */
    public abstract void a(a aVar);

    /* access modifiers changed from: protected */
    public abstract void a(String str);

    /* access modifiers changed from: protected */
    public abstract boolean a();

    /* access modifiers changed from: protected */
    public abstract String b();

    /* access modifiers changed from: protected */
    public String b(String str) {
        return Util.encode(str);
    }

    public void b(a aVar) {
        if (aVar != null && a()) {
            a(aVar);
        }
    }

    /* access modifiers changed from: protected */
    public String c(String str) {
        return Util.decode(str);
    }

    /* access modifiers changed from: protected */
    public abstract void c();

    /* access modifiers changed from: protected */
    public abstract a d();

    public MidEntity h() {
        String l = l();
        if (l != null) {
            return MidEntity.parse(l);
        }
        return null;
    }

    /* access modifiers changed from: 0000 */
    public void i() {
        if (a()) {
            c();
        }
    }

    public a j() {
        if (a()) {
            return d();
        }
        return null;
    }

    /* access modifiers changed from: protected */
    public String k() {
        return Util.decode("4kU71lN96TJUomD1vOU9lgj9Tw==");
    }
}
