package com.umeng.analytics.a;

import com.umeng.analytics.g;
import com.umeng.common.Log;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import org.json.JSONArray;
import org.json.JSONObject;

/* compiled from: LogBody */
public class i implements g {
    public ArrayList<g> a = new ArrayList<>();
    public ArrayList<g> b = new ArrayList<>();
    public ArrayList<g> c = new ArrayList<>();
    public ArrayList<g> d = new ArrayList<>();
    public ArrayList<g> e = new ArrayList<>();
    private final String f = "launch";
    private final String g = "terminate";
    private final String h = "error";
    private final String i = "event";
    private final String j = "ekv";

    public void a(h hVar) {
        if (hVar != null && hVar.a()) {
            this.a.add(hVar);
        }
    }

    public void a(m mVar) {
        if (mVar != null && mVar.a()) {
            this.b.add(mVar);
        }
    }

    public void a(d dVar) {
        if (dVar != null && dVar.a()) {
            this.c.add(dVar);
        }
    }

    public void a(e eVar) {
        if (eVar != null && eVar.a()) {
            this.d.add(eVar);
        }
    }

    public void a(String str, a aVar) {
        if (aVar != null && aVar.a()) {
            Iterator it = this.e.iterator();
            while (it.hasNext()) {
                b bVar = (b) ((g) it.next());
                if (bVar.a.equals(str)) {
                    bVar.b.add(aVar);
                    return;
                }
            }
            this.e.add(new b(str, aVar));
        }
    }

    public void a(b bVar) {
        if (bVar != null && bVar.a()) {
            this.e.add(bVar);
        }
    }

    private void c(JSONObject jSONObject) throws Exception {
        if (jSONObject.has("launch")) {
            JSONArray jSONArray = jSONObject.getJSONArray("launch");
            for (int i2 = 0; i2 < jSONArray.length(); i2++) {
                h hVar = new h();
                hVar.a(jSONArray.getJSONObject(i2));
                a(hVar);
            }
        }
    }

    private void d(JSONObject jSONObject) throws Exception {
        if (jSONObject.has("terminate")) {
            JSONArray jSONArray = jSONObject.getJSONArray("terminate");
            for (int i2 = 0; i2 < jSONArray.length(); i2++) {
                m mVar = new m();
                mVar.a(jSONArray.getJSONObject(i2));
                a(mVar);
            }
        }
    }

    private void e(JSONObject jSONObject) throws Exception {
        if (jSONObject.has("event")) {
            JSONArray jSONArray = jSONObject.getJSONArray("event");
            for (int i2 = 0; i2 < jSONArray.length(); i2++) {
                e eVar = new e();
                eVar.a(jSONArray.getJSONObject(i2));
                a(eVar);
            }
        }
    }

    private void f(JSONObject jSONObject) throws Exception {
        if (jSONObject.has("ekv")) {
            JSONArray jSONArray = jSONObject.getJSONArray("ekv");
            for (int i2 = 0; i2 < jSONArray.length(); i2++) {
                b bVar = new b();
                bVar.a(jSONArray.getJSONObject(i2));
                a(bVar);
            }
        }
    }

    private void g(JSONObject jSONObject) throws Exception {
        if (jSONObject.has("error")) {
            JSONArray jSONArray = jSONObject.getJSONArray("error");
            for (int i2 = 0; i2 < jSONArray.length(); i2++) {
                d dVar = new d();
                dVar.a(jSONArray.getJSONObject(i2));
                a(dVar);
            }
        }
    }

    public void a(JSONObject jSONObject) {
        if (jSONObject != null) {
            try {
                c(jSONObject);
                d(jSONObject);
                e(jSONObject);
                f(jSONObject);
                g(jSONObject);
            } catch (Exception e2) {
                Log.b(g.q, "merge log body eror", e2);
            }
        }
    }

    private JSONArray a(ArrayList<g> arrayList) {
        JSONArray jSONArray;
        Object obj;
        if (arrayList.size() > 0) {
            JSONArray jSONArray2 = new JSONArray();
            Iterator it = arrayList.iterator();
            Object obj2 = null;
            while (it.hasNext()) {
                try {
                    obj = new j(this, (g) it.next());
                } catch (Exception e2) {
                    Log.a(g.q, "Fail to write json ...", e2);
                    obj = obj2;
                }
                if (obj == null) {
                    obj2 = obj;
                } else {
                    jSONArray2.put(obj);
                    obj2 = obj;
                }
            }
            jSONArray = jSONArray2;
        } else {
            jSONArray = null;
        }
        if (jSONArray == null || jSONArray.length() == 0) {
            return null;
        }
        return jSONArray;
    }

    public void b(JSONObject jSONObject) throws Exception {
        JSONArray a2 = a(this.a);
        JSONArray a3 = a(this.b);
        JSONArray a4 = a(this.d);
        JSONArray a5 = a(this.c);
        JSONArray a6 = a(this.e);
        if (a2 != null) {
            jSONObject.put("launch", a2);
        }
        if (a3 != null) {
            jSONObject.put("terminate", a3);
        }
        if (a4 != null) {
            jSONObject.put("event", a4);
        }
        if (a5 != null) {
            jSONObject.put("error", a5);
        }
        if (a6 != null) {
            jSONObject.put("ekv", a6);
        }
    }

    public boolean a() {
        Iterator it = this.a.iterator();
        while (it.hasNext()) {
            if (!((g) it.next()).a()) {
                return false;
            }
        }
        Iterator it2 = this.b.iterator();
        while (it2.hasNext()) {
            if (!((g) it2.next()).a()) {
                return false;
            }
        }
        Iterator it3 = this.d.iterator();
        while (it3.hasNext()) {
            if (!((g) it3.next()).a()) {
                return false;
            }
        }
        Iterator it4 = this.e.iterator();
        while (it4.hasNext()) {
            if (!((g) it4.next()).a()) {
                return false;
            }
        }
        Iterator it5 = this.c.iterator();
        while (it5.hasNext()) {
            if (!((g) it5.next()).a()) {
                return false;
            }
        }
        if (this.a.size() == 0 && this.b.size() == 0 && this.d.size() == 0 && this.e.size() == 0 && this.c.size() == 0) {
            return false;
        }
        return true;
    }

    public void a(i iVar) {
        this.a.addAll(iVar.a);
        this.b.addAll(iVar.b);
        this.d.addAll(iVar.d);
        this.c.addAll(iVar.c);
        b(iVar.e);
    }

    private void b(ArrayList<g> arrayList) {
        if (!arrayList.isEmpty()) {
            HashMap hashMap = new HashMap();
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                b bVar = (b) ((g) it.next());
                if (hashMap.containsKey(bVar.a)) {
                    ((b) hashMap.get(bVar.a)).a(bVar);
                } else {
                    hashMap.put(bVar.a, bVar);
                }
            }
            Iterator it2 = this.e.iterator();
            while (it2.hasNext()) {
                b bVar2 = (b) ((g) it2.next());
                if (hashMap.containsKey(bVar2.a)) {
                    ((b) hashMap.get(bVar2.a)).a(bVar2);
                } else {
                    hashMap.put(bVar2.a, bVar2);
                }
            }
            this.e.clear();
            for (b add : hashMap.values()) {
                this.e.add(add);
            }
        }
    }

    public int b() {
        int i2 = 0;
        Iterator it = this.e.iterator();
        while (true) {
            int i3 = i2;
            if (!it.hasNext()) {
                return this.a.size() + i3 + this.b.size() + this.d.size() + this.c.size();
            }
            i2 = ((b) ((g) it.next())).b.size() + i3;
        }
    }

    public void c() {
        this.a.clear();
        this.b.clear();
        this.d.clear();
        this.e.clear();
        this.c.clear();
    }
}
