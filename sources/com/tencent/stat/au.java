package com.tencent.stat;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import com.tencent.mid.api.MidEntity;
import com.tencent.stat.common.StatConstants;
import com.tencent.stat.common.StatLogger;
import com.tencent.stat.common.a;
import com.tencent.stat.common.e;
import com.tencent.stat.common.k;
import com.tencent.stat.common.q;
import com.tencent.tvMTA.report.ExParamKeys.cgi;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import org.json.JSONObject;

public class au {
    /* access modifiers changed from: private */
    public static StatLogger h = k.b();
    private static Context i = null;
    private static au j = null;
    volatile int a = 0;
    a b = null;
    private bc c = null;
    private bc d = null;
    private e e = null;
    private String f = StatConstants.MTA_COOPERATION_TAG;
    private String g = StatConstants.MTA_COOPERATION_TAG;
    private int k = 0;
    private ConcurrentHashMap<com.tencent.stat.a.e, String> l = null;
    private boolean m = false;
    private HashMap<String, String> n = new HashMap<>();

    private au(Context context) {
        try {
            this.e = new e();
            i = context.getApplicationContext();
            this.l = new ConcurrentHashMap<>();
            this.f = k.r(context);
            this.g = "pri_" + k.r(context);
            this.c = new bc(i, this.f);
            this.d = new bc(i, this.g);
            a(true);
            a(false);
            f();
            b(i);
            d();
            j();
        } catch (Throwable th) {
            h.e(th);
        }
    }

    public static au a(Context context) {
        if (j == null) {
            synchronized (au.class) {
                if (j == null) {
                    j = new au(context);
                }
            }
        }
        return j;
    }

    private String a(List<bd> list) {
        StringBuilder sb = new StringBuilder(list.size() * 3);
        sb.append("event_id in (");
        int i2 = 0;
        int size = list.size();
        Iterator it = list.iterator();
        while (true) {
            int i3 = i2;
            if (it.hasNext()) {
                sb.append(((bd) it.next()).a);
                if (i3 != size - 1) {
                    sb.append(",");
                }
                i2 = i3 + 1;
            } else {
                sb.append(")");
                return sb.toString();
            }
        }
    }

    private synchronized void a(int i2, boolean z) {
        try {
            if (this.a > 0 && i2 > 0 && !StatServiceImpl.a()) {
                if (StatConfig.isDebugEnable()) {
                    h.i("Load " + this.a + " unsent events");
                }
                ArrayList arrayList = new ArrayList(i2);
                b(arrayList, i2, z);
                if (arrayList.size() > 0) {
                    if (StatConfig.isDebugEnable()) {
                        h.i("Peek " + arrayList.size() + " unsent events.");
                    }
                    a((List<bd>) arrayList, 2, z);
                    g.b(i).b(arrayList, new ba(this, arrayList, z));
                }
            }
        } catch (Throwable th) {
            h.e(th);
        }
        return;
    }

    /* JADX WARNING: Removed duplicated region for block: B:17:0x0091  */
    /* JADX WARNING: Removed duplicated region for block: B:41:0x00ec  */
    private void a(com.tencent.stat.a.e eVar, StatDispatchCallback statDispatchCallback, boolean z) {
        long j2;
        long j3;
        SQLiteDatabase sQLiteDatabase = null;
        try {
            SQLiteDatabase c2 = c(z);
            c2.beginTransaction();
            if (!z && this.a > StatConfig.getMaxStoreEventCount()) {
                h.warn("Too many events stored in db.");
                this.a -= this.c.getWritableDatabase().delete("events", "event_id in (select event_id from events where timestamp in (select min(timestamp) from events) limit 1)", null);
            }
            ContentValues contentValues = new ContentValues();
            String g2 = eVar.g();
            if (StatConfig.isDebugEnable()) {
                h.i("insert 1 event, content:" + g2);
            }
            contentValues.put("content", q.b(g2));
            contentValues.put("send_count", "0");
            contentValues.put("status", Integer.toString(1));
            contentValues.put("timestamp", Long.valueOf(eVar.c()));
            j2 = c2.insert("events", null, contentValues);
            c2.setTransactionSuccessful();
            if (c2 != null) {
                try {
                    c2.endTransaction();
                    j3 = j2;
                } catch (Throwable th) {
                    h.e(th);
                    j3 = j2;
                }
                if (j3 <= 0) {
                    this.a++;
                    if (StatConfig.isDebugEnable()) {
                        h.d("directStoreEvent insert event to db, event:" + eVar.g());
                    }
                    if (statDispatchCallback != null) {
                        statDispatchCallback.onDispatchSuccess();
                        return;
                    }
                    return;
                }
                h.error((Object) "Failed to store event:" + eVar.g());
                return;
            }
        } catch (Throwable th2) {
            h.e(th2);
            j3 = -1;
        }
        j3 = j2;
        if (j3 <= 0) {
        }
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Removed duplicated region for block: B:40:0x00d8 A[SYNTHETIC, Splitter:B:40:0x00d8] */
    /* JADX WARNING: Removed duplicated region for block: B:48:0x00e9 A[SYNTHETIC, Splitter:B:48:0x00e9] */
    public synchronized void a(List<bd> list, int i2, boolean z) {
        SQLiteDatabase sQLiteDatabase;
        String str;
        String str2 = null;
        synchronized (this) {
            if (list.size() != 0) {
                int b2 = b(z);
                try {
                    sQLiteDatabase = c(z);
                    if (i2 == 2) {
                        try {
                            str = "update events set status=" + i2 + ", send_count=send_count+1  where " + a(list);
                        } catch (Throwable th) {
                            th = th;
                            try {
                                h.e(th);
                                if (sQLiteDatabase != null) {
                                    try {
                                        sQLiteDatabase.endTransaction();
                                    } catch (Throwable th2) {
                                        h.e(th2);
                                    }
                                }
                                return;
                            } catch (Throwable th3) {
                                th = th3;
                                if (sQLiteDatabase != null) {
                                    try {
                                        sQLiteDatabase.endTransaction();
                                    } catch (Throwable th4) {
                                        h.e(th4);
                                    }
                                }
                                throw th;
                            }
                        }
                    } else {
                        str = "update events set status=" + i2 + " where " + a(list);
                        if (this.k % 3 == 0) {
                            str2 = "delete from events where send_count>" + b2;
                        }
                        this.k++;
                    }
                    if (StatConfig.isDebugEnable()) {
                        h.i("update sql:" + str);
                    }
                    sQLiteDatabase.beginTransaction();
                    sQLiteDatabase.execSQL(str);
                    if (str2 != null) {
                        h.i("update for delete sql:" + str2);
                        sQLiteDatabase.execSQL(str2);
                        f();
                    }
                    sQLiteDatabase.setTransactionSuccessful();
                    if (sQLiteDatabase != null) {
                        try {
                            sQLiteDatabase.endTransaction();
                        } catch (Throwable th5) {
                            h.e(th5);
                        }
                    }
                } catch (Throwable th6) {
                    th = th6;
                    sQLiteDatabase = null;
                    if (sQLiteDatabase != null) {
                    }
                    throw th;
                }
            }
        }
        return;
    }

    /* JADX INFO: used method not loaded: com.tencent.stat.common.StatLogger.e(java.lang.Throwable):null, types can be incorrect */
    /* access modifiers changed from: private */
    /* JADX WARNING: Code restructure failed: missing block: B:27:0x00ce, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:?, code lost:
        h.e(r0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:48:0x00f5, code lost:
        r1 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:49:0x00f6, code lost:
        h.e(r1);
     */
    /* JADX WARNING: Exception block dominator not found, dom blocks: [B:25:0x00c9, B:44:0x00f1] */
    public synchronized void a(List<bd> list, boolean z) {
        SQLiteDatabase sQLiteDatabase = null;
        synchronized (this) {
            if (list.size() != 0) {
                if (StatConfig.isDebugEnable()) {
                    h.i("Delete " + list.size() + " events, important:" + z);
                }
                StringBuilder sb = new StringBuilder(list.size() * 3);
                sb.append("event_id in (");
                int size = list.size();
                int i2 = 0;
                for (bd bdVar : list) {
                    sb.append(bdVar.a);
                    if (i2 != size - 1) {
                        sb.append(",");
                    }
                    i2++;
                }
                sb.append(")");
                try {
                    SQLiteDatabase c2 = c(z);
                    c2.beginTransaction();
                    int delete = c2.delete("events", sb.toString(), null);
                    if (StatConfig.isDebugEnable()) {
                        h.i("delete " + size + " event " + sb.toString() + ", success delete:" + delete);
                    }
                    this.a -= delete;
                    c2.setTransactionSuccessful();
                    f();
                    if (c2 != null) {
                        c2.endTransaction();
                    }
                } catch (Throwable th) {
                    h.e(th);
                }
            }
        }
        return;
    }

    private void a(boolean z) {
        SQLiteDatabase sQLiteDatabase = null;
        try {
            SQLiteDatabase c2 = c(z);
            c2.beginTransaction();
            ContentValues contentValues = new ContentValues();
            contentValues.put("status", Integer.valueOf(1));
            int update = c2.update("events", contentValues, "status=?", new String[]{Long.toString(2)});
            if (StatConfig.isDebugEnable()) {
                h.i("update " + update + " unsent events.");
            }
            c2.setTransactionSuccessful();
            if (c2 != null) {
                try {
                    c2.endTransaction();
                } catch (Throwable th) {
                    h.e(th);
                }
            }
        } catch (Throwable th2) {
            h.e(th2);
        }
    }

    private int b(boolean z) {
        return !z ? StatConfig.getMaxSendRetryCount() : StatConfig.getMaxImportantDataSendRetryCount();
    }

    public static au b() {
        return j;
    }

    /* access modifiers changed from: private */
    public void b(int i2, boolean z) {
        int i3 = i2 == -1 ? !z ? g() : h() : i2;
        if (i3 > 0) {
            int sendPeriodMinutes = StatConfig.getSendPeriodMinutes() * 60 * StatConfig.getNumEventsCommitPerSec();
            if (i3 > sendPeriodMinutes && sendPeriodMinutes > 0) {
                i3 = sendPeriodMinutes;
            }
            int a2 = StatConfig.a();
            int i4 = i3 / a2;
            int i5 = i3 % a2;
            if (StatConfig.isDebugEnable()) {
                h.i("sentStoreEventsByDb sendNumbers=" + i3 + ",important=" + z + ",maxSendNumPerFor1Period=" + sendPeriodMinutes + ",maxCount=" + i4 + ",restNumbers=" + i5);
            }
            for (int i6 = 0; i6 < i4; i6++) {
                a(a2, z);
            }
            if (i5 > 0) {
                a(i5, z);
            }
        }
    }

    /* access modifiers changed from: private */
    public synchronized void b(com.tencent.stat.a.e eVar, StatDispatchCallback statDispatchCallback, boolean z, boolean z2) {
        if (StatConfig.getMaxStoreEventCount() > 0) {
            if (StatConfig.m <= 0 || z || z2) {
                a(eVar, statDispatchCallback, z);
            } else if (StatConfig.m > 0) {
                if (StatConfig.isDebugEnable()) {
                    h.i("cacheEventsInMemory.size():" + this.l.size() + ",numEventsCachedInMemory:" + StatConfig.m + ",numStoredEvents:" + this.a);
                    h.i("cache event:" + eVar.g());
                }
                this.l.put(eVar, StatConstants.MTA_COOPERATION_TAG);
                if (this.l.size() >= StatConfig.m) {
                    i();
                }
                if (statDispatchCallback != null) {
                    if (this.l.size() > 0) {
                        i();
                    }
                    statDispatchCallback.onDispatchSuccess();
                }
            }
        }
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Removed duplicated region for block: B:40:0x00fa A[SYNTHETIC, Splitter:B:40:0x00fa] */
    public synchronized void b(f fVar) {
        Cursor cursor;
        boolean z;
        long insert;
        try {
            String a2 = fVar.a();
            String a3 = k.a(a2);
            ContentValues contentValues = new ContentValues();
            contentValues.put("content", fVar.b.toString());
            contentValues.put("md5sum", a3);
            fVar.c = a3;
            contentValues.put(cgi.CGI_COMMON_VERSION, Integer.valueOf(fVar.d));
            cursor = this.c.getReadableDatabase().query("config", null, null, null, null, null, null);
            while (true) {
                try {
                    if (cursor.moveToNext()) {
                        if (cursor.getInt(0) == fVar.a) {
                            z = true;
                            break;
                        }
                    } else {
                        z = false;
                        break;
                    }
                } catch (Throwable th) {
                    th = th;
                    try {
                        h.e(th);
                        if (cursor != null) {
                            cursor.close();
                        }
                        try {
                            this.c.getWritableDatabase().endTransaction();
                        } catch (Exception e2) {
                        }
                        return;
                    } catch (Throwable th2) {
                        th = th2;
                        if (cursor != null) {
                            cursor.close();
                        }
                        try {
                            this.c.getWritableDatabase().endTransaction();
                        } catch (Exception e3) {
                        }
                        throw th;
                    }
                }
            }
            this.c.getWritableDatabase().beginTransaction();
            if (true == z) {
                insert = (long) this.c.getWritableDatabase().update("config", contentValues, "type=?", new String[]{Integer.toString(fVar.a)});
            } else {
                contentValues.put(com.umeng.common.a.c, Integer.valueOf(fVar.a));
                insert = this.c.getWritableDatabase().insert("config", null, contentValues);
            }
            if (insert == -1) {
                h.e((Object) "Failed to store cfg:" + a2);
            } else {
                h.d("Sucessed to store cfg:" + a2);
            }
            this.c.getWritableDatabase().setTransactionSuccessful();
            if (cursor != null) {
                cursor.close();
            }
            try {
                this.c.getWritableDatabase().endTransaction();
            } catch (Exception e4) {
            }
        } catch (Throwable th3) {
            th = th3;
            cursor = null;
            if (cursor != null) {
            }
            this.c.getWritableDatabase().endTransaction();
            throw th;
        }
        return;
    }

    /* JADX WARNING: Removed duplicated region for block: B:24:0x0099  */
    private void b(List<bd> list, int i2, boolean z) {
        Cursor cursor;
        Cursor cursor2;
        try {
            cursor2 = d(z).query("events", null, "status=?", new String[]{Integer.toString(1)}, null, null, null, Integer.toString(i2));
            while (cursor2.moveToNext()) {
                try {
                    long j2 = cursor2.getLong(0);
                    String string = cursor2.getString(1);
                    if (!StatConfig.g) {
                        string = q.a(string);
                    }
                    int i3 = cursor2.getInt(2);
                    int i4 = cursor2.getInt(3);
                    bd bdVar = new bd(j2, string, i3, i4);
                    if (StatConfig.isDebugEnable()) {
                        h.i("peek event, id=" + j2 + ",send_count=" + i4 + ",timestamp=" + cursor2.getLong(4));
                    }
                    list.add(bdVar);
                } catch (Throwable th) {
                    th = th;
                    if (cursor2 != null) {
                        cursor2.close();
                    }
                    throw th;
                }
            }
            if (cursor2 != null) {
                cursor2.close();
            }
        } catch (Throwable th2) {
            th = th2;
            cursor2 = null;
            if (cursor2 != null) {
            }
            throw th;
        }
    }

    private SQLiteDatabase c(boolean z) {
        return !z ? this.c.getWritableDatabase() : this.d.getWritableDatabase();
    }

    private SQLiteDatabase d(boolean z) {
        return !z ? this.c.getReadableDatabase() : this.d.getReadableDatabase();
    }

    private void f() {
        this.a = g() + h();
    }

    private int g() {
        return (int) DatabaseUtils.queryNumEntries(this.c.getReadableDatabase(), "events");
    }

    private int h() {
        return (int) DatabaseUtils.queryNumEntries(this.d.getReadableDatabase(), "events");
    }

    /* JADX INFO: used method not loaded: com.tencent.stat.common.StatLogger.e(java.lang.Throwable):null, types can be incorrect */
    /* access modifiers changed from: private */
    /* JADX WARNING: Code restructure failed: missing block: B:42:0x012e, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:44:?, code lost:
        h.e(r0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:53:0x0146, code lost:
        r1 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:54:0x0147, code lost:
        h.e(r1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:59:?, code lost:
        return;
     */
    /* JADX WARNING: Exception block dominator not found, dom blocks: [B:40:0x0127, B:49:0x013f] */
    public void i() {
        SQLiteDatabase sQLiteDatabase = null;
        if (!this.m) {
            synchronized (this.l) {
                if (this.l.size() != 0) {
                    this.m = true;
                    if (StatConfig.isDebugEnable()) {
                        h.i("insert " + this.l.size() + " events ,numEventsCachedInMemory:" + StatConfig.m + ",numStoredEvents:" + this.a);
                    }
                    try {
                        sQLiteDatabase = this.c.getWritableDatabase();
                        sQLiteDatabase.beginTransaction();
                        Iterator it = this.l.entrySet().iterator();
                        while (it.hasNext()) {
                            com.tencent.stat.a.e eVar = (com.tencent.stat.a.e) ((Entry) it.next()).getKey();
                            ContentValues contentValues = new ContentValues();
                            String g2 = eVar.g();
                            if (StatConfig.isDebugEnable()) {
                                h.i("insert content:" + g2);
                            }
                            contentValues.put("content", q.b(g2));
                            contentValues.put("send_count", "0");
                            contentValues.put("status", Integer.toString(1));
                            contentValues.put("timestamp", Long.valueOf(eVar.c()));
                            sQLiteDatabase.insert("events", null, contentValues);
                            it.remove();
                        }
                        sQLiteDatabase.setTransactionSuccessful();
                        if (sQLiteDatabase != null) {
                            sQLiteDatabase.endTransaction();
                            f();
                        }
                    } catch (Throwable th) {
                        h.e(th);
                    }
                    this.m = false;
                    if (StatConfig.isDebugEnable()) {
                        h.i("after insert, cacheEventsInMemory.size():" + this.l.size() + ",numEventsCachedInMemory:" + StatConfig.m + ",numStoredEvents:" + this.a);
                    }
                }
            }
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:17:0x003f  */
    private void j() {
        Cursor cursor;
        try {
            cursor = this.c.getReadableDatabase().query("keyvalues", null, null, null, null, null, null);
            while (cursor.moveToNext()) {
                try {
                    this.n.put(cursor.getString(0), cursor.getString(1));
                } catch (Throwable th) {
                    th = th;
                    try {
                        h.e(th);
                        if (cursor != null) {
                            cursor.close();
                            return;
                        }
                        return;
                    } catch (Throwable th2) {
                        th = th2;
                        if (cursor != null) {
                            cursor.close();
                        }
                        throw th;
                    }
                }
            }
            if (cursor != null) {
                cursor.close();
            }
        } catch (Throwable th3) {
            th = th3;
            cursor = null;
            if (cursor != null) {
            }
            throw th;
        }
    }

    public int a() {
        return this.a;
    }

    /* access modifiers changed from: 0000 */
    public void a(int i2) {
        this.e.a(new bb(this, i2));
    }

    /* access modifiers changed from: 0000 */
    public void a(com.tencent.stat.a.e eVar, StatDispatchCallback statDispatchCallback, boolean z, boolean z2) {
        if (this.e != null) {
            this.e.a(new ay(this, eVar, statDispatchCallback, z, z2));
        }
    }

    /* access modifiers changed from: 0000 */
    public void a(f fVar) {
        if (fVar != null) {
            this.e.a(new az(this, fVar));
        }
    }

    /* access modifiers changed from: 0000 */
    public void a(List<bd> list, int i2, boolean z, boolean z2) {
        if (this.e != null) {
            this.e.a(new av(this, list, i2, z, z2));
        }
    }

    /* access modifiers changed from: 0000 */
    public void a(List<bd> list, boolean z, boolean z2) {
        if (this.e != null) {
            this.e.a(new aw(this, list, z, z2));
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:83:0x0205 A[SYNTHETIC, Splitter:B:83:0x0205] */
    /* JADX WARNING: Removed duplicated region for block: B:92:0x021f A[SYNTHETIC, Splitter:B:92:0x021f] */
    public synchronized a b(Context context) {
        Cursor cursor;
        Cursor cursor2;
        a aVar;
        String str;
        String str2;
        boolean z;
        String c2;
        if (this.b != null) {
            aVar = this.b;
        } else {
            try {
                this.c.getWritableDatabase().beginTransaction();
                if (StatConfig.isDebugEnable()) {
                    h.i("try to load user info from db.");
                }
                cursor = this.c.getReadableDatabase().query("user", null, null, null, null, null, null, null);
                boolean z2 = false;
                String str3 = StatConstants.MTA_COOPERATION_TAG;
                try {
                    if (cursor.moveToNext()) {
                        String string = cursor.getString(0);
                        String a2 = q.a(string);
                        int i2 = cursor.getInt(1);
                        String string2 = cursor.getString(2);
                        long currentTimeMillis = System.currentTimeMillis() / 1000;
                        int i3 = (i2 == 1 || k.a(cursor.getLong(3) * 1000).equals(k.a(1000 * currentTimeMillis))) ? i2 : 1;
                        int i4 = !string2.equals(k.n(context)) ? i3 | 2 : i3;
                        String[] split = a2.split(",");
                        if (split == null || split.length <= 0) {
                            String b2 = k.b(context);
                            str = b2;
                            str2 = b2;
                            z = true;
                        } else {
                            String str4 = split[0];
                            if (str4 == null || str4.length() < 11) {
                                String a3 = q.a(context);
                                if (a3 == null || a3.length() <= 10) {
                                    a3 = str4;
                                    z = false;
                                } else {
                                    z = true;
                                }
                                str2 = a2;
                                str = a3;
                            } else {
                                String str5 = str4;
                                z = false;
                                str2 = a2;
                                str = str5;
                            }
                        }
                        if (split == null || split.length < 2) {
                            c2 = k.c(context);
                            if (c2 != null && c2.length() > 0) {
                                str2 = str + "," + c2;
                                z = true;
                            }
                        } else {
                            c2 = split[1];
                            str2 = str + "," + c2;
                        }
                        this.b = new a(str, c2, i4);
                        ContentValues contentValues = new ContentValues();
                        contentValues.put("uid", q.b(str2));
                        contentValues.put("user_type", Integer.valueOf(i4));
                        contentValues.put("app_ver", k.n(context));
                        contentValues.put(MidEntity.TAG_TIMESTAMPS, Long.valueOf(currentTimeMillis));
                        if (z) {
                            this.c.getWritableDatabase().update("user", contentValues, "uid=?", new String[]{string});
                        }
                        if (i4 != i2) {
                            this.c.getWritableDatabase().replace("user", null, contentValues);
                            z2 = true;
                        } else {
                            z2 = true;
                        }
                    }
                    if (!z2) {
                        String b3 = k.b(context);
                        String c3 = k.c(context);
                        String str6 = (c3 == null || c3.length() <= 0) ? b3 : b3 + "," + c3;
                        long currentTimeMillis2 = System.currentTimeMillis() / 1000;
                        String n2 = k.n(context);
                        ContentValues contentValues2 = new ContentValues();
                        contentValues2.put("uid", q.b(str6));
                        contentValues2.put("user_type", Integer.valueOf(0));
                        contentValues2.put("app_ver", n2);
                        contentValues2.put(MidEntity.TAG_TIMESTAMPS, Long.valueOf(currentTimeMillis2));
                        this.c.getWritableDatabase().insert("user", null, contentValues2);
                        this.b = new a(b3, c3, 0);
                    }
                    this.c.getWritableDatabase().setTransactionSuccessful();
                    if (cursor != null) {
                        try {
                            cursor.close();
                        } catch (Throwable th) {
                            h.e(th);
                        }
                    }
                    this.c.getWritableDatabase().endTransaction();
                } catch (Throwable th2) {
                    th = th2;
                    if (cursor != null) {
                        try {
                            cursor.close();
                        } catch (Throwable th3) {
                            h.e(th3);
                            throw th;
                        }
                    }
                    this.c.getWritableDatabase().endTransaction();
                    throw th;
                }
            } catch (Throwable th4) {
                th = th4;
                cursor = null;
                if (cursor != null) {
                }
                this.c.getWritableDatabase().endTransaction();
                throw th;
            }
            aVar = this.b;
        }
        return aVar;
    }

    /* access modifiers changed from: 0000 */
    public void c() {
        if (StatConfig.isEnableStatService()) {
            try {
                this.e.a(new ax(this));
            } catch (Throwable th) {
                h.e(th);
            }
        }
    }

    /* access modifiers changed from: 0000 */
    /* JADX WARNING: Removed duplicated region for block: B:17:0x005b  */
    public void d() {
        Cursor cursor;
        try {
            cursor = this.c.getReadableDatabase().query("config", null, null, null, null, null, null);
            while (cursor.moveToNext()) {
                try {
                    int i2 = cursor.getInt(0);
                    String string = cursor.getString(1);
                    String string2 = cursor.getString(2);
                    int i3 = cursor.getInt(3);
                    f fVar = new f(i2);
                    fVar.a = i2;
                    fVar.b = new JSONObject(string);
                    fVar.c = string2;
                    fVar.d = i3;
                    StatConfig.a(i, fVar);
                } catch (Throwable th) {
                    th = th;
                    try {
                        h.e(th);
                        if (cursor != null) {
                            cursor.close();
                            return;
                        }
                        return;
                    } catch (Throwable th2) {
                        th = th2;
                        if (cursor != null) {
                        }
                        throw th;
                    }
                }
            }
            if (cursor != null) {
                cursor.close();
            }
        } catch (Throwable th3) {
            th = th3;
            cursor = null;
            if (cursor != null) {
                cursor.close();
            }
            throw th;
        }
    }
}
