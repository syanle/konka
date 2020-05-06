package com.tencent.mid.b;

import android.content.Context;
import com.tencent.mid.api.MidEntity;
import com.tencent.mid.util.Util;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class g {
    private static g b = null;
    private Map<Integer, f> a;

    private g(Context context) {
        this.a = null;
        this.a = new HashMap(3);
        this.a.put(Integer.valueOf(1), new e(context));
        this.a.put(Integer.valueOf(2), new c(context));
        this.a.put(Integer.valueOf(4), new d(context));
    }

    public static synchronized g a(Context context) {
        g gVar;
        synchronized (g.class) {
            if (b == null) {
                b = new g(context);
            }
            gVar = b;
        }
        return gVar;
    }

    public MidEntity a() {
        return a((List<Integer>) new ArrayList<Integer>(Arrays.asList(new Integer[]{Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(4)})));
    }

    public MidEntity a(List<Integer> list) {
        if (list == null || list.size() == 0) {
            return null;
        }
        for (Integer num : list) {
            f fVar = (f) this.a.get(num);
            if (fVar != null) {
                MidEntity h = fVar.h();
                if (h != null && h.isMidValid()) {
                    return h;
                }
            }
        }
        return null;
    }

    public void a(int i, int i2) {
        a b2 = b();
        if (i > 0) {
            b2.c(i);
        }
        if (i2 > 0) {
            b2.a(i2);
        }
        b2.a(System.currentTimeMillis());
        b2.b(0);
        a(b2);
    }

    public void a(MidEntity midEntity) {
        for (Entry value : this.a.entrySet()) {
            ((f) value.getValue()).a(midEntity);
        }
    }

    public void a(a aVar) {
        for (Entry value : this.a.entrySet()) {
            ((f) value.getValue()).b(aVar);
        }
    }

    public a b() {
        return b(new ArrayList(Arrays.asList(new Integer[]{Integer.valueOf(1), Integer.valueOf(4)})));
    }

    public a b(List<Integer> list) {
        if (list == null || list.size() == 0) {
            return null;
        }
        for (Integer num : list) {
            f fVar = (f) this.a.get(num);
            if (fVar != null) {
                a j = fVar.j();
                if (j != null) {
                    return j;
                }
            }
        }
        return null;
    }

    public void c() {
        Util.logInfo("clear mid cache");
        for (Entry value : this.a.entrySet()) {
            ((f) value.getValue()).i();
        }
    }
}
