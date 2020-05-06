package com.tencent.stat;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.tencent.stat.common.StatConstants;
import com.tencent.stat.common.q;
import java.util.ArrayList;

class bc extends SQLiteOpenHelper {
    private String a = StatConstants.MTA_COOPERATION_TAG;
    private Context b = null;

    public bc(Context context, String str) {
        super(context, str, null, 3);
        this.a = str;
        this.b = context.getApplicationContext();
        if (StatConfig.isDebugEnable()) {
            au.h.i("SQLiteOpenHelper " + this.a);
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:20:0x005b  */
    private void a(SQLiteDatabase sQLiteDatabase) {
        Cursor cursor;
        String str = null;
        try {
            cursor = sQLiteDatabase.query("user", null, null, null, null, null, null);
            try {
                ContentValues contentValues = new ContentValues();
                if (cursor.moveToNext()) {
                    str = cursor.getString(0);
                    cursor.getInt(1);
                    cursor.getString(2);
                    cursor.getLong(3);
                    contentValues.put("uid", q.b(str));
                }
                if (str != null) {
                    sQLiteDatabase.update("user", contentValues, "uid=?", new String[]{str});
                }
                if (cursor != null) {
                    cursor.close();
                }
            } catch (Throwable th) {
                th = th;
                try {
                    au.h.e(th);
                    if (cursor != null) {
                        cursor.close();
                    }
                } catch (Throwable th2) {
                    th = th2;
                    if (cursor != null) {
                    }
                    throw th;
                }
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

    /* JADX WARNING: Removed duplicated region for block: B:21:0x007c  */
    private void b(SQLiteDatabase sQLiteDatabase) {
        Cursor cursor;
        Cursor cursor2;
        try {
            cursor2 = sQLiteDatabase.query("events", null, null, null, null, null, null);
            try {
                ArrayList<bd> arrayList = new ArrayList<>();
                while (cursor2.moveToNext()) {
                    arrayList.add(new bd(cursor2.getLong(0), cursor2.getString(1), cursor2.getInt(2), cursor2.getInt(3)));
                }
                ContentValues contentValues = new ContentValues();
                for (bd bdVar : arrayList) {
                    contentValues.put("content", q.b(bdVar.b));
                    sQLiteDatabase.update("events", contentValues, "event_id=?", new String[]{Long.toString(bdVar.a)});
                }
                if (cursor2 != null) {
                    cursor2.close();
                }
            } catch (Throwable th) {
                th = th;
                if (cursor2 != null) {
                }
                throw th;
            }
        } catch (Throwable th2) {
            th = th2;
            cursor2 = null;
            if (cursor2 != null) {
            }
            throw th;
        }
    }

    public synchronized void close() {
        super.close();
    }

    public void onCreate(SQLiteDatabase sQLiteDatabase) {
        sQLiteDatabase.execSQL("create table if not exists events(event_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, content TEXT, status INTEGER, send_count INTEGER, timestamp LONG)");
        sQLiteDatabase.execSQL("create table if not exists user(uid TEXT PRIMARY KEY, user_type INTEGER, app_ver TEXT, ts INTEGER)");
        sQLiteDatabase.execSQL("create table if not exists config(type INTEGER PRIMARY KEY NOT NULL, content TEXT, md5sum TEXT, version INTEGER)");
        sQLiteDatabase.execSQL("create table if not exists keyvalues(key TEXT PRIMARY KEY NOT NULL, value TEXT)");
        sQLiteDatabase.execSQL("CREATE INDEX if not exists status_idx ON events(status)");
    }

    public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
        au.h.debug("upgrade DB from oldVersion " + i + " to newVersion " + i2);
        if (i == 1) {
            sQLiteDatabase.execSQL("create table if not exists keyvalues(key TEXT PRIMARY KEY NOT NULL, value TEXT)");
            a(sQLiteDatabase);
            b(sQLiteDatabase);
        }
        if (i == 2) {
            a(sQLiteDatabase);
            b(sQLiteDatabase);
        }
    }
}
