package com.umeng.analytics;

import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;
import java.util.Vector;

/* compiled from: UmengTimeStack */
public class k {
    private static final int b = 4;
    private Vector<Long> a;
    private String c;

    public k(String str) {
        this.a = new Vector<>(4);
        this.c = str;
    }

    public k(String str, int i) {
        this.c = str;
        if (i < 0) {
            this.a = new Vector<>(4);
        } else {
            this.a = new Vector<>(i);
        }
    }

    public void a(Long l) {
        while (this.a.size() >= 4) {
            this.a.remove(0);
        }
        this.a.add(l);
    }

    public Long a() {
        int size = this.a.size();
        if (size <= 0) {
            return Long.valueOf(-1);
        }
        return (Long) this.a.remove(size - 1);
    }

    public int b() {
        return this.a.size();
    }

    public String toString() {
        int size = this.a.size();
        if (size <= 0) {
            return null;
        }
        StringBuffer stringBuffer = new StringBuffer(4);
        for (int i = 0; i < size; i++) {
            stringBuffer.append(this.a.get(i));
            if (i != size - 1) {
                stringBuffer.append(",");
            }
        }
        this.a.clear();
        return stringBuffer.toString();
    }

    public static k a(Context context, String str) {
        return a(str, j.e(context).getString(str, null));
    }

    public static k a(String str, String str2) {
        k kVar = new k(str);
        if (!TextUtils.isEmpty(str2)) {
            for (String trim : str2.split(",")) {
                String trim2 = trim.trim();
                if (!TextUtils.isEmpty(trim2)) {
                    Long.valueOf(-1);
                    try {
                        kVar.a(Long.valueOf(Long.parseLong(trim2)));
                    } catch (Exception e) {
                    }
                }
            }
        }
        return kVar;
    }

    public void a(Context context) {
        String kVar = toString();
        Editor edit = j.e(context).edit();
        if (TextUtils.isEmpty(kVar)) {
            edit.remove(this.c).commit();
        } else {
            edit.putString(this.c, kVar).commit();
        }
    }
}
