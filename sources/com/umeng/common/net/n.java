package com.umeng.common.net;

import android.os.AsyncTask;
import com.tencent.stat.common.StatConstants;
import com.umeng.common.Log;

/* compiled from: ReportClient */
public class n extends s {
    private static final String a = n.class.getName();

    /* compiled from: ReportClient */
    public interface a {
        void a();

        void a(com.umeng.common.net.p.a aVar);
    }

    /* compiled from: ReportClient */
    private class b extends AsyncTask<Integer, Integer, com.umeng.common.net.p.a> {
        private o b;
        private a c;

        public b(o oVar, a aVar) {
            this.b = oVar;
            this.c = aVar;
        }

        /* access modifiers changed from: protected */
        public void onPreExecute() {
            if (this.c != null) {
                this.c.a();
            }
        }

        /* access modifiers changed from: protected */
        /* renamed from: a */
        public void onPostExecute(com.umeng.common.net.p.a aVar) {
            if (this.c != null) {
                this.c.a(aVar);
            }
        }

        /* access modifiers changed from: protected */
        /* renamed from: a */
        public com.umeng.common.net.p.a doInBackground(Integer... numArr) {
            return n.this.a(this.b);
        }
    }

    public com.umeng.common.net.p.a a(o oVar) {
        p pVar = (p) a((t) oVar, p.class);
        return pVar == null ? com.umeng.common.net.p.a.FAIL : pVar.a;
    }

    public void a(o oVar, a aVar) {
        try {
            new b(oVar, aVar).execute(new Integer[0]);
        } catch (Exception e) {
            Log.b(a, StatConstants.MTA_COOPERATION_TAG, e);
            if (aVar != null) {
                aVar.a(com.umeng.common.net.p.a.FAIL);
            }
        }
    }
}
