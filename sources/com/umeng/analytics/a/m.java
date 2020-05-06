package com.umeng.analytics.a;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;
import com.tencent.stat.common.StatConstants;
import com.umeng.analytics.f;
import com.umeng.analytics.g;
import com.umeng.analytics.j;
import com.umeng.common.Log;
import com.umeng.common.b;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import org.json.JSONArray;
import org.json.JSONObject;

/* compiled from: Terminate */
public class m extends l implements g {
    private static final String k = "duration";
    private static final String l = "activities";
    private static final String m = "terminate_time";
    public o a;
    public p b;
    public long c = 0;
    private ArrayList<k> d = new ArrayList<>();

    public m() {
    }

    public m(String str) {
        this.e = str;
    }

    public static m a(Context context) {
        m mVar = null;
        SharedPreferences e = j.e(context);
        String string = e.getString(l.f, null);
        if (string != null) {
            mVar = new m(string);
            o b2 = o.b(context);
            if (b2 != null && b2.a()) {
                mVar.a = b2;
            }
            p a2 = p.a(context);
            if (a2 != null && a2.a()) {
                mVar.b = a2;
            }
            mVar.d = c(e);
            mVar.c = d(e);
            String[] b3 = b(e);
            if (b3 != null && b3.length == 2) {
                mVar.g = b3[0];
                mVar.h = b3[1];
            }
            a(e);
        }
        return mVar;
    }

    private static void a(SharedPreferences sharedPreferences) {
        Editor edit = sharedPreferences.edit();
        edit.putLong(k, 0);
        edit.putString(l, StatConstants.MTA_COOPERATION_TAG);
        edit.commit();
    }

    private static String[] b(SharedPreferences sharedPreferences) {
        long j = sharedPreferences.getLong(m, 0);
        if (j <= 0) {
            return null;
        }
        return b.a(new Date(j)).split(" ");
    }

    private static ArrayList<k> c(SharedPreferences sharedPreferences) {
        String string = sharedPreferences.getString(l, StatConstants.MTA_COOPERATION_TAG);
        if (!StatConstants.MTA_COOPERATION_TAG.equals(string)) {
            ArrayList<k> arrayList = new ArrayList<>();
            try {
                String[] split = string.split(";");
                for (String jSONArray : split) {
                    arrayList.add(new k(new JSONArray(jSONArray)));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (arrayList.size() > 0) {
                return arrayList;
            }
        }
        return null;
    }

    private static long d(SharedPreferences sharedPreferences) {
        return sharedPreferences.getLong(k, 0) / 1000;
    }

    public static Editor a(SharedPreferences sharedPreferences, String str, long j, long j2) {
        long j3 = j2 - j;
        long j4 = sharedPreferences.getLong(k, 0);
        Editor edit = sharedPreferences.edit();
        if (g.j) {
            String string = sharedPreferences.getString(l, StatConstants.MTA_COOPERATION_TAG);
            if (!StatConstants.MTA_COOPERATION_TAG.equals(string)) {
                string = new StringBuilder(String.valueOf(string)).append(";").toString();
            }
            String sb = new StringBuilder(String.valueOf(string)).append(String.format("[\"%s\",%d]", new Object[]{str, Long.valueOf(j3 / 1000)})).toString();
            edit.remove(l);
            edit.putString(l, sb);
        }
        edit.putLong(k, j3 + j4);
        edit.putLong(m, j2);
        edit.commit();
        return edit;
    }

    public static Editor a(SharedPreferences sharedPreferences, ArrayList<f> arrayList, long j, long j2) {
        long j3 = j2 - j;
        long j4 = sharedPreferences.getLong(k, 0);
        Editor edit = sharedPreferences.edit();
        if (arrayList.size() > 0) {
            String string = sharedPreferences.getString(l, StatConstants.MTA_COOPERATION_TAG);
            StringBuilder sb = new StringBuilder();
            if (!TextUtils.isEmpty(string)) {
                sb.append(string);
                sb.append(";");
            }
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                f fVar = (f) it.next();
                sb.append(String.format("[\"%s\",%d]", new Object[]{fVar.a, Long.valueOf(fVar.b / 1000)}));
                sb.append(";");
            }
            sb.deleteCharAt(sb.length() - 1);
            edit.remove(l);
            edit.putString(l, sb.toString());
        }
        edit.putLong(k, j3 + j4);
        edit.putLong(m, j2);
        edit.commit();
        return edit;
    }

    public boolean a() {
        if (this.a == null && g.i) {
            Log.c(g.q, "missing location info in Terminate");
        }
        if (this.b == null) {
            Log.e(g.q, "missing receive and transport Traffic in Terminate ");
        }
        if (this.c <= 0) {
            Log.b(g.q, "missing Duration info in Terminate");
            return false;
        }
        if (this.d == null || this.d.size() == 0) {
            Log.e(g.q, "missing Activities info in Terminate");
        }
        return super.a();
    }

    private void c(JSONObject jSONObject) throws Exception {
        if (this.b != null) {
            this.b.b(jSONObject);
        }
    }

    private void d(JSONObject jSONObject) throws Exception {
        if (this.a != null) {
            this.a.b(jSONObject);
        }
    }

    private void e(JSONObject jSONObject) throws Exception {
        o oVar = new o();
        oVar.a(jSONObject);
        if (oVar.a()) {
            this.a = oVar;
        }
    }

    private void f(JSONObject jSONObject) throws Exception {
        p pVar = new p();
        pVar.a(jSONObject);
        if (pVar.a()) {
            this.b = pVar;
        }
    }

    private void g(JSONObject jSONObject) throws Exception {
        if (jSONObject.has(l)) {
            JSONArray jSONArray = jSONObject.getJSONArray(l);
            for (int i = 0; i < jSONArray.length(); i++) {
                this.d.add(new k(jSONArray.getJSONArray(i)));
            }
        }
    }

    private void h(JSONObject jSONObject) throws Exception {
        if (this.d.size() != 0) {
            JSONArray jSONArray = new JSONArray();
            Iterator it = this.d.iterator();
            while (it.hasNext()) {
                k kVar = (k) it.next();
                JSONArray jSONArray2 = new JSONArray();
                jSONArray2.put(kVar.a);
                jSONArray2.put(kVar.b);
                jSONArray.put(jSONArray2);
            }
            jSONObject.put(l, jSONArray);
        }
    }

    public void a(JSONObject jSONObject) {
        try {
            super.a(jSONObject);
            this.c = jSONObject.getLong(k);
            f(jSONObject);
            e(jSONObject);
            g(jSONObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void b(JSONObject jSONObject) throws Exception {
        super.b(jSONObject);
        if (this.c > 0) {
            jSONObject.put(k, this.c);
        }
        c(jSONObject);
        d(jSONObject);
        h(jSONObject);
    }
}
