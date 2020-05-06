package com.umeng.common.net;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.konka.kkinterface.tv.ChannelDesk;
import com.umeng.common.Log;
import com.umeng.common.util.h;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/* compiled from: DownloadTaskList */
public class c {
    /* access modifiers changed from: private */
    public static final String a = c.class.getName();
    private static final String b = "umeng_download_task_list";
    private static final String c = "UMENG_DATA";
    private static final String d = "cp";
    private static final String e = "url";
    private static final String f = "progress";
    private static final String g = "last_modified";
    private static final String h = "extra";
    private static Context i = null;
    private static final String j = "yyyy-MM-dd HH:mm:ss";
    private a k;

    /* compiled from: DownloadTaskList */
    class a extends SQLiteOpenHelper {
        private static final int b = 2;
        private static final String c = "CREATE TABLE umeng_download_task_list (cp TEXT, url TEXT, progress INTEGER, extra TEXT, last_modified TEXT, UNIQUE (cp,url) ON CONFLICT ABORT);";

        a(Context context) {
            super(context, c.c, null, 2);
        }

        public void onCreate(SQLiteDatabase sQLiteDatabase) {
            Log.c(c.a, c);
            sQLiteDatabase.execSQL(c);
        }

        public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
        }
    }

    /* compiled from: DownloadTaskList */
    private static class b {
        public static final c a = new c(null);

        private b() {
        }
    }

    private c() {
        this.k = new a(i);
    }

    /* synthetic */ c(c cVar) {
        this();
    }

    public static c a(Context context) {
        if (i == null && context == null) {
            throw new NullPointerException();
        }
        if (i == null) {
            i = context;
        }
        return b.a;
    }

    public boolean a(String str, String str2) {
        Exception e2;
        boolean z;
        boolean z2;
        ContentValues contentValues = new ContentValues();
        contentValues.put(d, str);
        contentValues.put(e, str2);
        contentValues.put(f, Integer.valueOf(0));
        contentValues.put(g, h.a());
        String str3 = "cp=? and url=?";
        try {
            String[] strArr = {str, str2};
            Cursor query = this.k.getReadableDatabase().query(b, new String[]{f}, str3, strArr, null, null, null, "1");
            if (query.getCount() > 0) {
                Log.c(a, "insert(" + str + ", " + str2 + "): " + " already exists in the db. Insert is cancelled.");
                z = false;
            } else {
                long insert = this.k.getWritableDatabase().insert(b, null, contentValues);
                if (insert == -1) {
                    z2 = false;
                } else {
                    z2 = true;
                }
                try {
                    Log.c(a, "insert(" + str + ", " + str2 + "): " + "rowid=" + insert);
                    z = z2;
                } catch (Exception e3) {
                    Exception exc = e3;
                    z = z2;
                    e2 = exc;
                    Log.c(a, "insert(" + str + ", " + str2 + "): " + e2.getMessage(), e2);
                    return z;
                }
            }
            try {
                query.close();
            } catch (Exception e4) {
                e2 = e4;
                Log.c(a, "insert(" + str + ", " + str2 + "): " + e2.getMessage(), e2);
                return z;
            }
        } catch (Exception e5) {
            e2 = e5;
            z = false;
            Log.c(a, "insert(" + str + ", " + str2 + "): " + e2.getMessage(), e2);
            return z;
        }
        return z;
    }

    public void a(String str, String str2, int i2) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(f, Integer.valueOf(i2));
        contentValues.put(g, h.a());
        String[] strArr = {str, str2};
        this.k.getWritableDatabase().update(b, contentValues, "cp=? and url=?", strArr);
        Log.c(a, "updateProgress(" + str + ", " + str2 + ", " + i2 + ")");
    }

    public void a(String str, String str2, String str3) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(h, str3);
        contentValues.put(g, h.a());
        String[] strArr = {str, str2};
        this.k.getWritableDatabase().update(b, contentValues, "cp=? and url=?", strArr);
        Log.c(a, "updateExtra(" + str + ", " + str2 + ", " + str3 + ")");
    }

    public int b(String str, String str2) {
        int i2;
        String[] strArr = {str, str2};
        Cursor query = this.k.getReadableDatabase().query(b, new String[]{f}, "cp=? and url=?", strArr, null, null, null, "1");
        if (query.getCount() > 0) {
            query.moveToFirst();
            i2 = query.getInt(0);
        } else {
            i2 = -1;
        }
        query.close();
        return i2;
    }

    public String c(String str, String str2) {
        String str3 = null;
        String[] strArr = {str, str2};
        Cursor query = this.k.getReadableDatabase().query(b, new String[]{h}, "cp=? and url=?", strArr, null, null, null, "1");
        if (query.getCount() > 0) {
            query.moveToFirst();
            str3 = query.getString(0);
        }
        query.close();
        return str3;
    }

    /* JADX WARNING: type inference failed for: r5v0, types: [java.lang.String] */
    /* JADX WARNING: type inference failed for: r6v1, types: [java.lang.String] */
    /* JADX WARNING: type inference failed for: r7v0, types: [java.lang.String] */
    /* JADX WARNING: type inference failed for: r8v0, types: [java.lang.String] */
    /* JADX WARNING: type inference failed for: r5v1, types: [java.util.Date] */
    /* JADX WARNING: type inference failed for: r5v3, types: [java.util.Date] */
    /* JADX WARNING: type inference failed for: r5v4 */
    /* JADX WARNING: Multi-variable type inference failed. Error: jadx.core.utils.exceptions.JadxRuntimeException: No candidate types for var: r5v0, types: [java.lang.String]
  assigns: [?[int, float, boolean, short, byte, char, OBJECT, ARRAY]]
  uses: [?[OBJECT, ARRAY], java.lang.String, java.util.Date]
  mth insns count: 41
    	at jadx.core.dex.visitors.typeinference.TypeSearch.fillTypeCandidates(TypeSearch.java:237)
    	at java.util.ArrayList.forEach(Unknown Source)
    	at jadx.core.dex.visitors.typeinference.TypeSearch.run(TypeSearch.java:53)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runMultiVariableSearch(TypeInferenceVisitor.java:99)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:92)
    	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:27)
    	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$1(DepthTraversal.java:14)
    	at java.util.ArrayList.forEach(Unknown Source)
    	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
    	at jadx.core.ProcessClass.process(ProcessClass.java:30)
    	at jadx.core.ProcessClass.lambda$processDependencies$0(ProcessClass.java:49)
    	at java.util.ArrayList.forEach(Unknown Source)
    	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:49)
    	at jadx.core.ProcessClass.process(ProcessClass.java:35)
    	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:311)
    	at jadx.api.JavaClass.decompile(JavaClass.java:62)
    	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:217)
     */
    /* JADX WARNING: Unknown variable types count: 5 */
    public Date d(String str, String str2) {
        ? r5 = 0;
        String[] strArr = {str, str2};
        Cursor query = this.k.getReadableDatabase().query(b, new String[]{g}, "cp=? and url=?", strArr, r5, r5, r5, r5);
        if (query.getCount() > 0) {
            query.moveToFirst();
            String string = query.getString(0);
            Log.c(a, "getLastModified(" + str + ", " + str2 + "): " + string);
            try {
                r5 = new SimpleDateFormat(j).parse(string);
            } catch (Exception e2) {
                Log.c(a, e2.getMessage());
            }
        }
        query.close();
        return r5;
    }

    public void e(String str, String str2) {
        String[] strArr = {str, str2};
        this.k.getWritableDatabase().delete(b, "cp=? and url=?", strArr);
        Log.c(a, "delete(" + str + ", " + str2 + ")");
    }

    public List<String> a(String str) {
        String[] strArr = {str};
        Cursor query = this.k.getReadableDatabase().query(b, new String[]{e}, "cp=?", strArr, null, null, null, "1");
        ArrayList arrayList = new ArrayList();
        query.moveToFirst();
        while (!query.isAfterLast()) {
            arrayList.add(query.getString(0));
            query.moveToNext();
        }
        query.close();
        return arrayList;
    }

    public void a(int i2) {
        try {
            Date date = new Date(new Date().getTime() - ((long) (i2 * ChannelDesk.max_dtv_count)));
            this.k.getWritableDatabase().execSQL(" DELETE FROM umeng_download_task_list WHERE strftime('yyyy-MM-dd HH:mm:ss', last_modified)<=strftime('yyyy-MM-dd HH:mm:ss', '" + new SimpleDateFormat(j).format(date) + "')");
            Log.c(a, "clearOverdueTasks(" + i2 + ")" + " remove all tasks before " + new SimpleDateFormat(j).format(date));
        } catch (Exception e2) {
            Log.b(a, e2.getMessage());
        }
    }

    public void finalize() {
        this.k.close();
    }
}
