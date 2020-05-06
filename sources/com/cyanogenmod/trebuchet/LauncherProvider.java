package com.cyanogenmod.trebuchet;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.appwidget.AppWidgetHost;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProviderInfo;
import android.content.ComponentName;
import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Xml;
import com.android.internal.util.XmlUtils;
import com.konka.android.tv.KKCommonManager;
import com.konka.android.tv.KKCommonManager.EN_KK_LAUNCHER_CONFIG_FILE_TYPE;
import com.konka.ios7launcher.R;
import com.konka.kkimplements.common.IniReader;
import com.konka.kkinterface.tv.ChannelDesk;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

public class LauncherProvider extends ContentProvider {
    static final String AUTHORITY = "com.konka.ios7launcher.settings";
    static final Uri CONTENT_APPWIDGET_RESET_URI = Uri.parse("content://com.konka.ios7launcher.settings/appWidgetReset");
    private static final String DATABASE_NAME = "launcher.db";
    private static final int DATABASE_VERSION = 9;
    private static final boolean LOGD = true;
    static final String PARAMETER_NOTIFY = "notify";
    static final String TABLE_FAVORITES = "favorites";
    private static final String TAG = "Launcher.LauncherProvider";
    /* access modifiers changed from: private */
    public static final ArrayList<ComponentName[]> sCompatComponents = new ArrayList<>();
    private DatabaseHelper mOpenHelper;

    private static class DatabaseHelper extends SQLiteOpenHelper {
        private static final String TAG_APPWIDGET = "appwidget";
        private static final String TAG_CLOCK = "clock";
        private static final String TAG_FAVORITE = "favorite";
        private static final String TAG_FAVORITES = "favorites";
        private static final String TAG_FOLDER = "folder";
        private static final String TAG_SEARCH = "search";
        private static final String TAG_SHORTCUT = "shortcut";
        private final AppWidgetHost mAppWidgetHost;
        private final Context mContext;
        private long mMaxId = -1;

        DatabaseHelper(Context context) {
            super(context, LauncherProvider.DATABASE_NAME, null, 9);
            this.mContext = context;
            this.mAppWidgetHost = new AppWidgetHost(context, 2048);
            if (this.mMaxId == -1) {
                this.mMaxId = initializeMaxId(getWritableDatabase());
            }
        }

        private void sendAppWidgetResetNotify() {
            this.mContext.getContentResolver().notifyChange(LauncherProvider.CONTENT_APPWIDGET_RESET_URI, null);
        }

        public void onCreate(SQLiteDatabase db) {
            Log.d(LauncherProvider.TAG, "creating new launcher database");
            this.mMaxId = 1;
            db.execSQL("CREATE TABLE favorites (_id INTEGER PRIMARY KEY,title TEXT,intent TEXT,container INTEGER,screen INTEGER,cellX INTEGER,cellY INTEGER,spanX INTEGER,spanY INTEGER,itemType INTEGER,appWidgetId INTEGER NOT NULL DEFAULT -1,isShortcut INTEGER,iconType INTEGER,iconPackage TEXT,iconResource TEXT,icon BLOB,uri TEXT,displayMode INTEGER,lock INTEGER);");
            if (this.mAppWidgetHost != null) {
                this.mAppWidgetHost.deleteHost();
                sendAppWidgetResetNotify();
            }
            if (!convertDatabase(db)) {
                String fileName = null;
                try {
                    fileName = KKCommonManager.getInstance(this.mContext).getLauncherConfigurationFileName(EN_KK_LAUNCHER_CONFIG_FILE_TYPE.WORKSPACE, this.mContext.getPackageName());
                } catch (NoClassDefFoundError | NoSuchMethodError e) {
                }
                if (fileName == null || !new File(fileName).exists()) {
                    Log.d(LauncherProvider.TAG, "the workspace.ini is not found, so load the default workspace");
                    loadFavorites(db, (int) R.xml.default_workspace);
                    return;
                }
                loadFavorites(db, fileName);
            }
        }

        private boolean convertDatabase(SQLiteDatabase db) {
            Log.d(LauncherProvider.TAG, "converting database from an older format, but not onUpgrade");
            boolean converted = false;
            Uri uri = Uri.parse("content://settings/old_favorites?notify=true");
            ContentResolver resolver = this.mContext.getContentResolver();
            Cursor cursor = null;
            try {
                cursor = resolver.query(uri, null, null, null, null);
            } catch (Exception e) {
            }
            if (cursor != null && cursor.getCount() > 0) {
                try {
                    converted = copyFromCursor(db, cursor) > 0;
                    if (converted) {
                        resolver.delete(uri, null, null);
                    }
                } finally {
                    cursor.close();
                }
            }
            if (converted) {
                Log.d(LauncherProvider.TAG, "converted and now triggering widget upgrade");
                convertWidgets(db);
            }
            return converted;
        }

        /* JADX INFO: finally extract failed */
        private int copyFromCursor(SQLiteDatabase db, Cursor c) {
            int idIndex = c.getColumnIndexOrThrow("_id");
            int intentIndex = c.getColumnIndexOrThrow(BaseLauncherColumns.INTENT);
            int titleIndex = c.getColumnIndexOrThrow(BaseLauncherColumns.TITLE);
            int iconTypeIndex = c.getColumnIndexOrThrow(BaseLauncherColumns.ICON_TYPE);
            int iconIndex = c.getColumnIndexOrThrow(BaseLauncherColumns.ICON);
            int iconPackageIndex = c.getColumnIndexOrThrow(BaseLauncherColumns.ICON_PACKAGE);
            int iconResourceIndex = c.getColumnIndexOrThrow(BaseLauncherColumns.ICON_RESOURCE);
            int containerIndex = c.getColumnIndexOrThrow("container");
            int itemTypeIndex = c.getColumnIndexOrThrow(BaseLauncherColumns.ITEM_TYPE);
            int screenIndex = c.getColumnIndexOrThrow("screen");
            int cellXIndex = c.getColumnIndexOrThrow("cellX");
            int cellYIndex = c.getColumnIndexOrThrow("cellY");
            int uriIndex = c.getColumnIndexOrThrow("uri");
            int displayModeIndex = c.getColumnIndexOrThrow("displayMode");
            int lockIndex = c.getColumnIndexOrThrow("lock");
            ContentValues[] rows = new ContentValues[c.getCount()];
            int i = 0;
            while (c.moveToNext()) {
                ContentValues values = new ContentValues(c.getColumnCount());
                values.put("_id", Long.valueOf(c.getLong(idIndex)));
                values.put(BaseLauncherColumns.INTENT, c.getString(intentIndex));
                values.put(BaseLauncherColumns.TITLE, c.getString(titleIndex));
                values.put(BaseLauncherColumns.ICON_TYPE, Integer.valueOf(c.getInt(iconTypeIndex)));
                values.put(BaseLauncherColumns.ICON, c.getBlob(iconIndex));
                values.put(BaseLauncherColumns.ICON_PACKAGE, c.getString(iconPackageIndex));
                values.put(BaseLauncherColumns.ICON_RESOURCE, c.getString(iconResourceIndex));
                values.put("container", Integer.valueOf(c.getInt(containerIndex)));
                values.put(BaseLauncherColumns.ITEM_TYPE, Integer.valueOf(c.getInt(itemTypeIndex)));
                values.put("appWidgetId", Integer.valueOf(-1));
                values.put("screen", Integer.valueOf(c.getInt(screenIndex)));
                values.put("cellX", Integer.valueOf(c.getInt(cellXIndex)));
                values.put("cellY", Integer.valueOf(c.getInt(cellYIndex)));
                values.put("uri", c.getString(uriIndex));
                values.put("displayMode", Integer.valueOf(c.getInt(displayModeIndex)));
                values.put("lock", Integer.valueOf(c.getInt(lockIndex)));
                int i2 = i + 1;
                rows[i] = values;
                i = i2;
            }
            db.beginTransaction();
            int total = 0;
            try {
                int numValues = rows.length;
                for (int i3 = 0; i3 < numValues; i3++) {
                    if (LauncherProvider.dbInsertAndCheck(db, TAG_FAVORITES, null, rows[i3]) < 0) {
                        db.endTransaction();
                        return 0;
                    }
                    total++;
                }
                db.setTransactionSuccessful();
                db.endTransaction();
                return total;
            } catch (Throwable th) {
                db.endTransaction();
                throw th;
            }
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.d(LauncherProvider.TAG, "onUpgrade triggered");
            int version = oldVersion;
            if (version < 3) {
                db.beginTransaction();
                try {
                    db.execSQL("ALTER TABLE favorites ADD COLUMN appWidgetId INTEGER NOT NULL DEFAULT -1;");
                    db.setTransactionSuccessful();
                    version = 3;
                } catch (SQLException ex) {
                    Log.e(LauncherProvider.TAG, ex.getMessage(), ex);
                } finally {
                    db.endTransaction();
                }
                if (version == 3) {
                    convertWidgets(db);
                }
            }
            if (version < 4) {
                version = 4;
            }
            if (version < 6) {
                db.beginTransaction();
                try {
                    db.execSQL("UPDATE favorites SET screen=(screen + 1);");
                    db.setTransactionSuccessful();
                } catch (SQLException ex2) {
                    Log.e(LauncherProvider.TAG, ex2.getMessage(), ex2);
                } finally {
                    db.endTransaction();
                }
                if (updateContactsShortcuts(db)) {
                    version = 6;
                }
            }
            if (version < 7) {
                convertWidgets(db);
                version = 7;
            }
            if (version < 8) {
                normalizeIcons(db);
                version = 8;
            }
            if (version < 9) {
                if (this.mMaxId == -1) {
                    this.mMaxId = initializeMaxId(db);
                }
                loadFavorites(db, (int) R.xml.update_workspace);
                version = 9;
            }
            if (version != 9) {
                Log.w(LauncherProvider.TAG, "Destroying all old data.");
                db.execSQL("DROP TABLE IF EXISTS favorites");
                onCreate(db);
            }
        }

        private boolean updateContactsShortcuts(SQLiteDatabase db) {
            Cursor c = null;
            String selectWhere = LauncherProvider.buildOrWhereString(BaseLauncherColumns.ITEM_TYPE, new int[]{1});
            db.beginTransaction();
            try {
                c = db.query(TAG_FAVORITES, new String[]{"_id", BaseLauncherColumns.INTENT}, selectWhere, null, null, null, null);
                Log.d(LauncherProvider.TAG, "found upgrade cursor count=" + c.getCount());
                ContentValues values = new ContentValues();
                int idIndex = c.getColumnIndex("_id");
                int intentIndex = c.getColumnIndex(BaseLauncherColumns.INTENT);
                while (c != null && c.moveToNext()) {
                    long favoriteId = c.getLong(idIndex);
                    String intentUri = c.getString(intentIndex);
                    if (intentUri != null) {
                        try {
                            Intent intent = Intent.parseUri(intentUri, 0);
                            Log.d("Home", intent.toString());
                            Uri uri = intent.getData();
                            String data = uri.toString();
                            if ("android.intent.action.VIEW".equals(intent.getAction()) && (data.startsWith("content://contacts/people/") || data.startsWith("content://com.android.contacts/contacts/lookup/"))) {
                                Intent intent2 = new Intent("com.android.contacts.action.QUICK_CONTACT");
                                intent2.setFlags(337641472);
                                intent2.setData(uri);
                                intent2.putExtra("mode", 3);
                                intent2.putExtra("exclude_mimes", null);
                                values.clear();
                                values.put(BaseLauncherColumns.INTENT, intent2.toUri(0));
                                SQLiteDatabase sQLiteDatabase = db;
                                sQLiteDatabase.update(TAG_FAVORITES, values, "_id=" + favoriteId, null);
                            }
                        } catch (RuntimeException ex) {
                            Log.e(LauncherProvider.TAG, "Problem upgrading shortcut", ex);
                        } catch (URISyntaxException e) {
                            Log.e(LauncherProvider.TAG, "Problem upgrading shortcut", e);
                        }
                    }
                }
                db.setTransactionSuccessful();
                db.endTransaction();
                if (c != null) {
                    c.close();
                }
                return true;
            } catch (SQLException ex2) {
                Log.w(LauncherProvider.TAG, "Problem while upgrading contacts", ex2);
                db.endTransaction();
                if (c != null) {
                    c.close();
                }
                return false;
            } catch (Throwable th) {
                db.endTransaction();
                if (c != null) {
                    c.close();
                }
                throw th;
            }
        }

        private void normalizeIcons(SQLiteDatabase db) {
            Log.d(LauncherProvider.TAG, "normalizing icons");
            db.beginTransaction();
            Cursor c = null;
            SQLiteStatement update = null;
            boolean logged = false;
            try {
                update = db.compileStatement("UPDATE favorites SET icon=? WHERE _id=?");
                c = db.rawQuery("SELECT _id, icon FROM favorites WHERE iconType=1", null);
                int idIndex = c.getColumnIndexOrThrow("_id");
                int iconIndex = c.getColumnIndexOrThrow(BaseLauncherColumns.ICON);
                while (c.moveToNext()) {
                    long id = c.getLong(idIndex);
                    byte[] data = c.getBlob(iconIndex);
                    try {
                        Bitmap bitmap = Utilities.resampleIconBitmap(BitmapFactory.decodeByteArray(data, 0, data.length), this.mContext);
                        if (bitmap != null) {
                            update.bindLong(1, id);
                            byte[] data2 = ItemInfo.flattenBitmap(bitmap);
                            if (data2 != null) {
                                update.bindBlob(2, data2);
                                update.execute();
                            }
                            bitmap.recycle();
                        }
                    } catch (Exception e) {
                        if (!logged) {
                            Log.e(LauncherProvider.TAG, "Failed normalizing icon " + id, e);
                        } else {
                            Log.e(LauncherProvider.TAG, "Also failed normalizing icon " + id);
                        }
                        logged = true;
                    }
                }
                db.setTransactionSuccessful();
                db.endTransaction();
                if (update != null) {
                    update.close();
                }
                if (c != null) {
                    c.close();
                }
            } catch (SQLException ex) {
                Log.w(LauncherProvider.TAG, "Problem while allocating appWidgetIds for existing widgets", ex);
                db.endTransaction();
                if (update != null) {
                    update.close();
                }
                if (c != null) {
                    c.close();
                }
            } catch (Throwable th) {
                db.endTransaction();
                if (update != null) {
                    update.close();
                }
                if (c != null) {
                    c.close();
                }
                throw th;
            }
        }

        public long generateNewId() {
            if (this.mMaxId < 0) {
                throw new RuntimeException("Error: max id was not initialized");
            }
            this.mMaxId++;
            return this.mMaxId;
        }

        private long initializeMaxId(SQLiteDatabase db) {
            Cursor c = db.rawQuery("SELECT MAX(_id) FROM favorites", null);
            long id = -1;
            if (c != null && c.moveToNext()) {
                id = c.getLong(0);
            }
            if (c != null) {
                c.close();
            }
            if (id != -1) {
                return id;
            }
            throw new RuntimeException("Error: could not query max id");
        }

        private void convertWidgets(SQLiteDatabase db) {
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this.mContext);
            String selectWhere = LauncherProvider.buildOrWhereString(BaseLauncherColumns.ITEM_TYPE, new int[]{ChannelDesk.max_dtv_count, 1002, 1001});
            Cursor c = null;
            db.beginTransaction();
            try {
                c = db.query(TAG_FAVORITES, new String[]{"_id", BaseLauncherColumns.ITEM_TYPE}, selectWhere, null, null, null, null);
                Log.d(LauncherProvider.TAG, "found upgrade cursor count=" + c.getCount());
                ContentValues values = new ContentValues();
                while (c != null && c.moveToNext()) {
                    long favoriteId = c.getLong(0);
                    int favoriteType = c.getInt(1);
                    try {
                        int appWidgetId = this.mAppWidgetHost.allocateAppWidgetId();
                        Log.d(LauncherProvider.TAG, "allocated appWidgetId=" + appWidgetId + " for favoriteId=" + favoriteId);
                        values.clear();
                        values.put(BaseLauncherColumns.ITEM_TYPE, Integer.valueOf(4));
                        values.put("appWidgetId", Integer.valueOf(appWidgetId));
                        if (favoriteType == 1001) {
                            values.put("spanX", Integer.valueOf(4));
                            values.put("spanY", Integer.valueOf(1));
                        } else {
                            values.put("spanX", Integer.valueOf(2));
                            values.put("spanY", Integer.valueOf(2));
                        }
                        SQLiteDatabase sQLiteDatabase = db;
                        sQLiteDatabase.update(TAG_FAVORITES, values, "_id=" + favoriteId, null);
                        if (favoriteType == 1000) {
                            appWidgetManager.bindAppWidgetId(appWidgetId, new ComponentName("com.android.alarmclock", "com.android.alarmclock.AnalogAppWidgetProvider"));
                        } else if (favoriteType == 1002) {
                            appWidgetManager.bindAppWidgetId(appWidgetId, new ComponentName("com.android.camera", "com.android.camera.PhotoAppWidgetProvider"));
                        } else if (favoriteType == 1001) {
                            appWidgetManager.bindAppWidgetId(appWidgetId, getSearchWidgetProvider());
                        }
                    } catch (RuntimeException ex) {
                        Log.e(LauncherProvider.TAG, "Problem allocating appWidgetId", ex);
                    }
                }
                db.setTransactionSuccessful();
                db.endTransaction();
                if (c != null) {
                    c.close();
                }
            } catch (SQLException ex2) {
                Log.w(LauncherProvider.TAG, "Problem while allocating appWidgetIds for existing widgets", ex2);
                db.endTransaction();
                if (c != null) {
                    c.close();
                }
            } catch (Throwable th) {
                db.endTransaction();
                if (c != null) {
                    c.close();
                }
                throw th;
            }
        }

        /* JADX WARNING: Removed duplicated region for block: B:21:0x0163 A[Catch:{ IOException -> 0x00d9, RuntimeException -> 0x020b }] */
        /* JADX WARNING: Removed duplicated region for block: B:26:0x01c3 A[Catch:{ IOException -> 0x00d9, RuntimeException -> 0x020b }] */
        /* JADX WARNING: Removed duplicated region for block: B:28:0x01ca A[Catch:{ IOException -> 0x00d9, RuntimeException -> 0x020b }] */
        /* JADX WARNING: Removed duplicated region for block: B:35:0x00a2 A[SYNTHETIC] */
        private int loadFavoritesIni(SQLiteDatabase db, String filename) {
            String sectionName;
            boolean added;
            Log.d(LauncherProvider.TAG, "favourites: filename=" + filename);
            Intent intent = new Intent("android.intent.action.MAIN", null);
            intent.addCategory("android.intent.category.LAUNCHER");
            ContentValues values = new ContentValues();
            PackageManager packageManager = this.mContext.getPackageManager();
            int i = 0;
            try {
                IniReader iniReader = new IniReader(filename);
                int iItemCounts = iniReader.getSectionCounts();
                boolean isWidgetFinished = false;
                Log.d(LauncherProvider.TAG, "the section counts===" + iItemCounts);
                for (int iItemID = 0; iItemID < iItemCounts; iItemID++) {
                    if (!isWidgetFinished) {
                        sectionName = new StringBuilder(TAG_APPWIDGET).append(iItemID).toString();
                        if (iniReader.getValue(sectionName, "packageName") == null) {
                            isWidgetFinished = true;
                            sectionName = new StringBuilder(TAG_FAVORITE).append(iItemID).toString();
                            if (iniReader.getValue(sectionName, "packageName") == null) {
                            }
                        }
                        Log.d(LauncherProvider.TAG, "the section name===" + sectionName);
                        added = false;
                        long container = (long) iniReader.getInt(sectionName, "container", -100);
                        String screen = iniReader.getValue(sectionName, "screen");
                        String x = iniReader.getValue(sectionName, "x");
                        String y = iniReader.getValue(sectionName, "y");
                        String lock = iniReader.getValue(sectionName, "lock");
                        values.clear();
                        values.put("container", Long.valueOf(container));
                        values.put("screen", screen);
                        values.put("cellX", x);
                        values.put("cellY", y);
                        values.put("lock", lock);
                        if (!sectionName.contains(TAG_FAVORITE)) {
                            added = addAppShortcut(db, values, iniReader.getValue(sectionName, "packageName"), iniReader.getValue(sectionName, "className"), packageManager, intent, container) >= 0;
                        } else {
                            if (sectionName.contains(TAG_APPWIDGET)) {
                                added = addAppWidget(db, values, iniReader.getValue(sectionName, "packageName"), iniReader.getValue(sectionName, "className"), iniReader.getInt(sectionName, "spanX", 1), iniReader.getInt(sectionName, "spanY", 1), packageManager);
                            }
                        }
                        Log.d(LauncherProvider.TAG, "fav add to screen " + screen + ": (" + x + ", " + y + ") - " + added);
                        if (!added) {
                            i++;
                        }
                    } else {
                        sectionName = new StringBuilder(TAG_FAVORITE).append(iItemID).toString();
                        if (iniReader.getValue(sectionName, "packageName") == null) {
                            Log.d(LauncherProvider.TAG, "the item is finish in advance,the idx===" + iItemID);
                        }
                        Log.d(LauncherProvider.TAG, "the section name===" + sectionName);
                        added = false;
                        long container2 = (long) iniReader.getInt(sectionName, "container", -100);
                        String screen2 = iniReader.getValue(sectionName, "screen");
                        String x2 = iniReader.getValue(sectionName, "x");
                        String y2 = iniReader.getValue(sectionName, "y");
                        String lock2 = iniReader.getValue(sectionName, "lock");
                        values.clear();
                        values.put("container", Long.valueOf(container2));
                        values.put("screen", screen2);
                        values.put("cellX", x2);
                        values.put("cellY", y2);
                        values.put("lock", lock2);
                        if (!sectionName.contains(TAG_FAVORITE)) {
                        }
                        Log.d(LauncherProvider.TAG, "fav add to screen " + screen2 + ": (" + x2 + ", " + y2 + ") - " + added);
                        if (!added) {
                        }
                    }
                }
            } catch (IOException e) {
                Log.w(LauncherProvider.TAG, "Got exception parsing favorites.", e);
            } catch (RuntimeException e2) {
                Log.w(LauncherProvider.TAG, "Got exception parsing favorites.", e2);
            }
            return i;
        }

        private int loadFavorites(SQLiteDatabase db, int workspaceResourceId) {
            int i = 0;
            try {
                return loadFavoritesInner(db, this.mContext.getResources().getXml(workspaceResourceId));
            } catch (NotFoundException e) {
                Log.w(LauncherProvider.TAG, "Got exception getting favorites xml.", e);
                return i;
            }
        }

        private static ActivityInfo getActivityInfo(PackageManager packageManager, ComponentName target, String packageName, String className) {
            ActivityInfo info = null;
            try {
                return packageManager.getActivityInfo(target, 0);
            } catch (NameNotFoundException e) {
                try {
                    return packageManager.getActivityInfo(new ComponentName(packageManager.currentToCanonicalPackageNames(new String[]{packageName})[0], className), 0);
                } catch (NameNotFoundException e2) {
                    return info;
                }
            }
        }

        /* JADX WARNING: Code restructure failed: missing block: B:125:0x03e7, code lost:
            throw new java.lang.RuntimeException("Folders can contain only shortcuts");
         */
        private int loadFavorites(SQLiteDatabase db, String filename) {
            Intent intent = new Intent("android.intent.action.MAIN", null);
            intent.addCategory("android.intent.category.LAUNCHER");
            ContentValues values = new ContentValues();
            PackageManager packageManager = this.mContext.getPackageManager();
            int i = 0;
            try {
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(true);
                XmlPullParser parser = factory.newPullParser();
                parser.setInput(new FileReader(filename));
                XmlUtils.beginDocument(parser, TAG_FAVORITES);
                int depth = parser.getDepth();
                loop0:
                while (true) {
                    int type = parser.next();
                    if ((type == 3 && parser.getDepth() <= depth) || type == 1) {
                        break;
                    } else if (type == 2) {
                        boolean added = false;
                        String name = parser.getName();
                        long container = -100;
                        String namespace = parser.getAttributeNamespace(0);
                        String screen = parser.getAttributeValue(namespace, "screen");
                        String x = parser.getAttributeValue(namespace, "x");
                        String y = parser.getAttributeValue(namespace, "y");
                        String lock = parser.getAttributeValue(namespace, "lock");
                        String containerString = parser.getAttributeValue(namespace, "container");
                        if (containerString != null) {
                            container = Long.valueOf(containerString).longValue();
                        }
                        values.clear();
                        values.put("container", Long.valueOf(container));
                        values.put("screen", screen);
                        values.put("cellX", x);
                        values.put("cellY", y);
                        values.put("lock", lock);
                        if (TAG_FAVORITE.equals(name)) {
                            String packageName = parser.getAttributeValue(namespace, "packageName");
                            String className = parser.getAttributeValue(namespace, "className");
                            long id = addAppShortcut(db, values, packageName, className, packageManager, intent, container);
                            if (id < 0) {
                                for (int ic = 0; ic < LauncherProvider.sCompatComponents.size(); ic++) {
                                    boolean inList = false;
                                    ComponentName[] compatList = (ComponentName[]) LauncherProvider.sCompatComponents.get(ic);
                                    int length = compatList.length;
                                    int i2 = 0;
                                    while (true) {
                                        if (i2 < length) {
                                            ComponentName cn = compatList[i2];
                                            if (cn.getPackageName().equals(packageName) && cn.getClassName().equals(className)) {
                                                inList = true;
                                                Log.d(LauncherProvider.TAG, "===============compat inList: " + cn);
                                                break;
                                            }
                                            i2++;
                                        } else {
                                            break;
                                        }
                                    }
                                    if (inList) {
                                        int length2 = compatList.length;
                                        int i3 = 0;
                                        while (true) {
                                            if (i3 >= length2) {
                                                break;
                                            }
                                            ComponentName cn2 = compatList[i3];
                                            if (getActivityInfo(packageManager, cn2, packageName, className) != null) {
                                                Log.d(LauncherProvider.TAG, "===============compat inList: exist: " + cn2);
                                                packageName = cn2.getPackageName();
                                                className = cn2.getClassName();
                                                id = addAppShortcut(db, values, packageName, className, packageManager, intent, container);
                                                break;
                                            }
                                            i3++;
                                        }
                                    }
                                }
                            }
                            added = id >= 0;
                        } else if (TAG_APPWIDGET.equals(name)) {
                            added = addAppWidget(db, values, parser.getAttributeValue(namespace, "packageName"), parser.getAttributeValue(namespace, "className"), Integer.valueOf(parser.getAttributeValue(namespace, "spanX")).intValue(), Integer.valueOf(parser.getAttributeValue(namespace, "spanY")).intValue(), packageManager);
                        } else if (TAG_SHORTCUT.equals(name)) {
                            String uri = parser.getAttributeValue(namespace, "uri");
                            String packageName2 = parser.getAttributeValue(namespace, "packageName");
                            String className2 = parser.getAttributeValue(namespace, "className");
                            int iconResId = 0;
                            int titleResId = 0;
                            try {
                                String iconResString = parser.getAttributeValue(namespace, BaseLauncherColumns.ICON);
                                if (iconResString != null) {
                                    iconResId = Integer.valueOf(iconResString).intValue();
                                }
                                String titleResString = parser.getAttributeValue(namespace, BaseLauncherColumns.TITLE);
                                if (titleResString != null) {
                                    titleResId = Integer.valueOf(titleResString).intValue();
                                }
                            } catch (NumberFormatException e) {
                            }
                            added = addUriShortcut(db, values, uri, packageName2, className2, iconResId, titleResId, packageManager) >= 0;
                        } else if (TAG_FOLDER.equals(name)) {
                            String title = parser.getAttributeValue(namespace, BaseLauncherColumns.TITLE);
                            if (TextUtils.isEmpty(title)) {
                                title = this.mContext.getResources().getString(R.string.folder_name);
                            }
                            values.put(BaseLauncherColumns.TITLE, title);
                            long folderId = addFolder(db, values);
                            added = folderId >= 0;
                            ArrayList<Long> folderItems = new ArrayList<>();
                            int folderDepth = parser.getDepth();
                            while (true) {
                                int type2 = parser.next();
                                if (type2 != 3 || parser.getDepth() > folderDepth) {
                                    if (type2 == 2) {
                                        String folder_item_name = parser.getName();
                                        values.clear();
                                        values.put("container", Long.valueOf(folderId));
                                        if (TAG_FAVORITE.equals(folder_item_name) && folderId >= 0) {
                                            long id2 = addAppShortcut(db, values, parser.getAttributeValue(namespace, "packageName"), parser.getAttributeValue(namespace, "className"), packageManager, intent, container);
                                            if (id2 >= 0) {
                                                folderItems.add(Long.valueOf(id2));
                                            }
                                        } else if (!TAG_SHORTCUT.equals(folder_item_name) || folderId < 0) {
                                            break loop0;
                                        } else {
                                            String uri2 = parser.getAttributeValue(namespace, "uri");
                                            String packageName3 = parser.getAttributeValue(namespace, "packageName");
                                            String className3 = parser.getAttributeValue(namespace, "className");
                                            int iconResId2 = 0;
                                            int titleResId2 = 0;
                                            try {
                                                String iconResString2 = parser.getAttributeValue(namespace, BaseLauncherColumns.ICON);
                                                if (iconResString2 != null) {
                                                    iconResId2 = Integer.valueOf(iconResString2).intValue();
                                                }
                                                String titleResString2 = parser.getAttributeValue(namespace, BaseLauncherColumns.TITLE);
                                                if (titleResString2 != null) {
                                                    titleResId2 = Integer.valueOf(titleResString2).intValue();
                                                }
                                            } catch (NumberFormatException e2) {
                                            }
                                            long id3 = addUriShortcut(db, values, uri2, packageName3, className3, iconResId2, titleResId2, packageManager);
                                            if (id3 >= 0) {
                                                folderItems.add(Long.valueOf(id3));
                                            }
                                        }
                                    }
                                } else if (folderItems.size() < 2 && folderId >= 0) {
                                    LauncherProvider.deleteId(db, folderId);
                                    if (folderItems.size() > 0) {
                                        LauncherProvider.deleteId(db, ((Long) folderItems.get(0)).longValue());
                                    }
                                    added = false;
                                }
                            }
                        }
                        if (added) {
                            i++;
                        }
                    }
                }
            } catch (XmlPullParserException e3) {
                Log.w(LauncherProvider.TAG, "Got exception parsing favorites.", e3);
            } catch (IOException e4) {
                Log.w(LauncherProvider.TAG, "Got exception parsing favorites.", e4);
            } catch (RuntimeException e5) {
                Log.w(LauncherProvider.TAG, "Got exception parsing favorites.", e5);
            }
            return i;
        }

        private int loadFavoritesInner(SQLiteDatabase db, XmlPullParser parser) {
            String title;
            Intent intent = new Intent("android.intent.action.MAIN", null);
            intent.addCategory("android.intent.category.LAUNCHER");
            ContentValues values = new ContentValues();
            PackageManager packageManager = this.mContext.getPackageManager();
            int i = 0;
            try {
                AttributeSet attrs = Xml.asAttributeSet(parser);
                XmlUtils.beginDocument(parser, TAG_FAVORITES);
                int depth = parser.getDepth();
                loop0:
                while (true) {
                    int type = parser.next();
                    if ((type == 3 && parser.getDepth() <= depth) || type == 1) {
                        break;
                    } else if (type == 2) {
                        boolean added = false;
                        String name = parser.getName();
                        TypedArray a = this.mContext.obtainStyledAttributes(attrs, R.styleable.Favorite);
                        long container = -100;
                        if (a.hasValue(2)) {
                            container = Long.valueOf(a.getString(2)).longValue();
                        }
                        String screen = a.getString(3);
                        String x = a.getString(4);
                        String y = a.getString(5);
                        String lock = a.getString(11);
                        if (container != -101 || !Hotseat.isAllAppsButtonRank(Integer.valueOf(screen).intValue())) {
                            values.clear();
                            values.put("container", Long.valueOf(container));
                            values.put("screen", screen);
                            values.put("cellX", x);
                            values.put("cellY", y);
                            values.put("lock", lock);
                            if (TAG_FAVORITE.equals(name)) {
                                added = addAppShortcut(db, values, a, packageManager, intent, container) >= 0;
                            } else if (TAG_SEARCH.equals(name)) {
                                added = addSearchWidget(db, values);
                            } else if (TAG_CLOCK.equals(name)) {
                                added = addClockWidget(db, values);
                            } else if (TAG_APPWIDGET.equals(name)) {
                                added = addAppWidget(db, values, a, packageManager);
                            } else if (TAG_SHORTCUT.equals(name)) {
                                added = addUriShortcut(db, values, a, packageManager) >= 0;
                            } else if (TAG_FOLDER.equals(name)) {
                                int titleResId = a.getResourceId(9, -1);
                                if (titleResId != -1) {
                                    title = this.mContext.getResources().getString(titleResId);
                                } else {
                                    title = this.mContext.getResources().getString(R.string.folder_name);
                                }
                                values.put(BaseLauncherColumns.TITLE, title);
                                long folderId = addFolder(db, values);
                                added = folderId >= 0;
                                ArrayList<Long> folderItems = new ArrayList<>();
                                int folderDepth = parser.getDepth();
                                while (true) {
                                    int type2 = parser.next();
                                    if (type2 != 3 || parser.getDepth() > folderDepth) {
                                        if (type2 == 2) {
                                            String folder_item_name = parser.getName();
                                            TypedArray ar = this.mContext.obtainStyledAttributes(attrs, R.styleable.Favorite);
                                            values.clear();
                                            values.put("container", Long.valueOf(folderId));
                                            if (TAG_FAVORITE.equals(folder_item_name) && folderId >= 0) {
                                                long id = addAppShortcut(db, values, ar, packageManager, intent, container);
                                                if (id >= 0) {
                                                    folderItems.add(Long.valueOf(id));
                                                }
                                            } else if (TAG_SHORTCUT.equals(folder_item_name) && folderId >= 0) {
                                                long id2 = addUriShortcut(db, values, ar, packageManager);
                                                if (id2 >= 0) {
                                                    folderItems.add(Long.valueOf(id2));
                                                }
                                            }
                                            ar.recycle();
                                        }
                                    } else if (folderItems.size() < 2 && folderId >= 0) {
                                        LauncherProvider.deleteId(db, folderId);
                                        if (folderItems.size() > 0) {
                                            LauncherProvider.deleteId(db, ((Long) folderItems.get(0)).longValue());
                                        }
                                        added = false;
                                    }
                                }
                            }
                            if (added) {
                                i++;
                            }
                            a.recycle();
                        } else {
                            throw new RuntimeException("Invalid screen position for hotseat item");
                        }
                    }
                }
                throw new RuntimeException("Folders can contain only shortcuts");
            } catch (XmlPullParserException e) {
                Log.w(LauncherProvider.TAG, "Got exception parsing favorites.", e);
            } catch (IOException e2) {
                Log.w(LauncherProvider.TAG, "Got exception parsing favorites.", e2);
            } catch (RuntimeException e3) {
                Log.w(LauncherProvider.TAG, "Got exception parsing favorites.", e3);
            }
            return i;
        }

        public static boolean isLaunchableActivity(Context context, PackageManager packageManager, ComponentName cn) {
            if (cn == null || cn.getPackageName() == null || cn.getClassName() == null) {
                return false;
            }
            return AllAppsList.findActivity(AllAppsList.findActivitiesForPackage(context, cn.getPackageName()), cn);
        }

        private long addAppShortcut(SQLiteDatabase db, ContentValues values, String packageName, String className, PackageManager packageManager, Intent intent, long container) {
            ComponentName cn;
            ActivityInfo info;
            long id = -1;
            try {
                cn = new ComponentName(packageName, className);
                if (container == -100 && !isLaunchableActivity(this.mContext, packageManager, cn)) {
                    return -1;
                }
                info = packageManager.getActivityInfo(cn, 0);
                try {
                    id = generateNewId();
                    intent.setComponent(cn);
                    intent.setFlags(270532608);
                    values.put(BaseLauncherColumns.INTENT, intent.toUri(0));
                    values.put(BaseLauncherColumns.TITLE, info.loadLabel(packageManager).toString());
                    values.put(BaseLauncherColumns.ITEM_TYPE, Integer.valueOf(0));
                    values.put("spanX", Integer.valueOf(1));
                    values.put("spanY", Integer.valueOf(1));
                    values.put("_id", Long.valueOf(generateNewId()));
                    if (LauncherProvider.dbInsertAndCheck(db, TAG_FAVORITES, null, values) < 0) {
                        return -1;
                    }
                } catch (NameNotFoundException e) {
                    Log.w(LauncherProvider.TAG, "Unable to add favorite: " + packageName + "/" + className, e);
                }
                return id;
            } catch (NameNotFoundException e2) {
                cn = new ComponentName(packageManager.currentToCanonicalPackageNames(new String[]{packageName})[0], className);
                info = packageManager.getActivityInfo(cn, 0);
            }
        }

        private long addAppShortcut(SQLiteDatabase db, ContentValues values, TypedArray a, PackageManager packageManager, Intent intent, long container) {
            ComponentName cn;
            ActivityInfo info;
            long id = -1;
            String packageName = a.getString(1);
            String className = a.getString(0);
            try {
                cn = new ComponentName(packageName, className);
                if (container == -100 && !isLaunchableActivity(this.mContext, packageManager, cn)) {
                    return -1;
                }
                info = packageManager.getActivityInfo(cn, 0);
                try {
                    id = generateNewId();
                    intent.setComponent(cn);
                    intent.setFlags(270532608);
                    values.put(BaseLauncherColumns.INTENT, intent.toUri(0));
                    values.put(BaseLauncherColumns.TITLE, info.loadLabel(packageManager).toString());
                    values.put(BaseLauncherColumns.ITEM_TYPE, Integer.valueOf(0));
                    values.put("spanX", Integer.valueOf(1));
                    values.put("spanY", Integer.valueOf(1));
                    values.put("_id", Long.valueOf(generateNewId()));
                    if (LauncherProvider.dbInsertAndCheck(db, TAG_FAVORITES, null, values) < 0) {
                        return -1;
                    }
                } catch (NameNotFoundException e) {
                    Log.w(LauncherProvider.TAG, "Unable to add favorite: " + packageName + "/" + className, e);
                }
                return id;
            } catch (NameNotFoundException e2) {
                cn = new ComponentName(packageManager.currentToCanonicalPackageNames(new String[]{packageName})[0], className);
                info = packageManager.getActivityInfo(cn, 0);
            }
        }

        private long addFolder(SQLiteDatabase db, ContentValues values) {
            values.put(BaseLauncherColumns.ITEM_TYPE, Integer.valueOf(2));
            values.put("spanX", Integer.valueOf(1));
            values.put("spanY", Integer.valueOf(1));
            long id = generateNewId();
            values.put("_id", Long.valueOf(id));
            if (LauncherProvider.dbInsertAndCheck(db, TAG_FAVORITES, null, values) <= 0) {
                return -1;
            }
            return id;
        }

        @SuppressLint({"NewApi"})
        private ComponentName getSearchWidgetProvider() {
            ComponentName searchComponent = ((SearchManager) this.mContext.getSystemService(TAG_SEARCH)).getGlobalSearchActivity();
            if (searchComponent == null) {
                return null;
            }
            return getProviderInPackage(searchComponent.getPackageName());
        }

        private ComponentName getProviderInPackage(String packageName) {
            List<AppWidgetProviderInfo> providers = AppWidgetManager.getInstance(this.mContext).getInstalledProviders();
            if (providers == null) {
                return null;
            }
            for (AppWidgetProviderInfo provider1 : providers) {
                ComponentName provider = provider1.provider;
                if (provider != null && provider.getPackageName().equals(packageName)) {
                    return provider;
                }
            }
            return null;
        }

        private boolean addSearchWidget(SQLiteDatabase db, ContentValues values) {
            return addAppWidget(db, values, getSearchWidgetProvider(), 4, 1);
        }

        private boolean addClockWidget(SQLiteDatabase db, ContentValues values) {
            return addAppWidget(db, values, new ComponentName("com.android.alarmclock", "com.android.alarmclock.AnalogAppWidgetProvider"), 2, 2);
        }

        private boolean addAppWidget(SQLiteDatabase db, ContentValues values, String packageName, String className, int spanX, int spanY, PackageManager packageManager) {
            if (packageName == null || className == null) {
                return false;
            }
            boolean hasPackage = true;
            ComponentName cn = new ComponentName(packageName, className);
            try {
                packageManager.getReceiverInfo(cn, 0);
            } catch (Exception e) {
                cn = new ComponentName(packageManager.currentToCanonicalPackageNames(new String[]{packageName})[0], className);
                try {
                    packageManager.getReceiverInfo(cn, 0);
                } catch (Exception e2) {
                    hasPackage = false;
                }
            }
            if (hasPackage) {
                return addAppWidget(db, values, cn, spanX, spanY);
            }
            return false;
        }

        private boolean addAppWidget(SQLiteDatabase db, ContentValues values, TypedArray a, PackageManager packageManager) {
            String packageName = a.getString(1);
            String className = a.getString(0);
            if (packageName == null || className == null) {
                return false;
            }
            boolean hasPackage = true;
            ComponentName cn = new ComponentName(packageName, className);
            try {
                packageManager.getReceiverInfo(cn, 0);
            } catch (Exception e) {
                cn = new ComponentName(packageManager.currentToCanonicalPackageNames(new String[]{packageName})[0], className);
                try {
                    packageManager.getReceiverInfo(cn, 0);
                } catch (Exception e2) {
                    hasPackage = false;
                }
            }
            if (!hasPackage) {
                return false;
            }
            return addAppWidget(db, values, cn, a.getInt(6, 0), a.getInt(7, 0));
        }

        private boolean addAppWidget(SQLiteDatabase db, ContentValues values, ComponentName cn, int spanX, int spanY) {
            boolean allocatedAppWidgets = false;
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this.mContext);
            try {
                int appWidgetId = this.mAppWidgetHost.allocateAppWidgetId();
                values.put(BaseLauncherColumns.ITEM_TYPE, Integer.valueOf(4));
                values.put("spanX", Integer.valueOf(spanX));
                values.put("spanY", Integer.valueOf(spanY));
                values.put("appWidgetId", Integer.valueOf(appWidgetId));
                values.put("_id", Long.valueOf(generateNewId()));
                LauncherProvider.dbInsertAndCheck(db, TAG_FAVORITES, null, values);
                allocatedAppWidgets = true;
                appWidgetManager.bindAppWidgetId(appWidgetId, cn);
                return true;
            } catch (RuntimeException ex) {
                Log.e(LauncherProvider.TAG, "Problem allocating appWidgetId", ex);
                return allocatedAppWidgets;
            }
        }

        private long addUriShortcut(SQLiteDatabase db, ContentValues values, String uri, String packageName, String className, int iconResId, int titleResId, PackageManager packageManager) {
            ComponentName cn;
            ActivityInfo info;
            Resources r = this.mContext.getResources();
            try {
                Intent intent = Intent.parseUri(uri, 0);
                if (iconResId == 0 || titleResId == 0) {
                    Log.w(LauncherProvider.TAG, "Shortcut is missing title or icon resource ID");
                    if (packageName == null || className == null) {
                        Log.w(LauncherProvider.TAG, "Shortcut is missing packagename or classname");
                        return -1;
                    }
                    try {
                        cn = new ComponentName(packageName, className);
                        info = packageManager.getActivityInfo(cn, 0);
                    } catch (NameNotFoundException e) {
                        cn = new ComponentName(packageManager.currentToCanonicalPackageNames(new String[]{packageName})[0], className);
                        info = packageManager.getActivityInfo(cn, 0);
                    }
                    try {
                        int iconResId2 = info.icon;
                        int titleResId2 = info.labelRes;
                        Resources r2 = packageManager.getResourcesForActivity(cn);
                        long id = generateNewId();
                        intent.setFlags(268435456);
                        values.put(BaseLauncherColumns.INTENT, intent.toUri(0));
                        values.put(BaseLauncherColumns.TITLE, r2.getString(titleResId2));
                        values.put(BaseLauncherColumns.ITEM_TYPE, Integer.valueOf(1));
                        values.put("spanX", Integer.valueOf(1));
                        values.put("spanY", Integer.valueOf(1));
                        values.put(BaseLauncherColumns.ICON_TYPE, Integer.valueOf(0));
                        values.put(BaseLauncherColumns.ICON_PACKAGE, packageName);
                        values.put(BaseLauncherColumns.ICON_RESOURCE, r2.getResourceName(iconResId2));
                        values.put("_id", Long.valueOf(id));
                        if (LauncherProvider.dbInsertAndCheck(db, TAG_FAVORITES, null, values) < 0) {
                            return -1;
                        }
                        return id;
                    } catch (NameNotFoundException e2) {
                        Log.w(LauncherProvider.TAG, "Unable to add shortcut: " + packageName + "/" + className, e2);
                        return -1;
                    }
                } else {
                    long id2 = generateNewId();
                    intent.setFlags(268435456);
                    values.put(BaseLauncherColumns.INTENT, intent.toUri(0));
                    values.put(BaseLauncherColumns.TITLE, r.getString(titleResId));
                    values.put(BaseLauncherColumns.ITEM_TYPE, Integer.valueOf(1));
                    values.put("spanX", Integer.valueOf(1));
                    values.put("spanY", Integer.valueOf(1));
                    values.put(BaseLauncherColumns.ICON_TYPE, Integer.valueOf(0));
                    values.put(BaseLauncherColumns.ICON_PACKAGE, this.mContext.getPackageName());
                    values.put(BaseLauncherColumns.ICON_RESOURCE, r.getResourceName(iconResId));
                    values.put("_id", Long.valueOf(id2));
                    if (LauncherProvider.dbInsertAndCheck(db, TAG_FAVORITES, null, values) < 0) {
                        return -1;
                    }
                    return id2;
                }
            } catch (URISyntaxException e3) {
                Log.w(LauncherProvider.TAG, "Shortcut has malformed uri: " + uri);
                return -1;
            }
        }

        private long addUriShortcut(SQLiteDatabase db, ContentValues values, TypedArray a, PackageManager packageManager) {
            ComponentName cn;
            ActivityInfo info;
            Resources r = this.mContext.getResources();
            int iconResId = a.getResourceId(8, 0);
            int titleResId = a.getResourceId(9, 0);
            String uri = null;
            try {
                uri = a.getString(10);
                Intent intent = Intent.parseUri(uri, 0);
                if (iconResId == 0 || titleResId == 0) {
                    Log.w(LauncherProvider.TAG, "Shortcut is missing title or icon resource ID");
                    String packageName = a.getString(1);
                    String className = a.getString(0);
                    if (packageName == null || className == null) {
                        Log.w(LauncherProvider.TAG, "Shortcut is missing packagename or classname");
                        return -1;
                    }
                    try {
                        cn = new ComponentName(packageName, className);
                        info = packageManager.getActivityInfo(cn, 0);
                        try {
                            int iconResId2 = info.icon;
                            int titleResId2 = info.labelRes;
                            Resources r2 = packageManager.getResourcesForActivity(cn);
                            long id = generateNewId();
                            intent.setFlags(268435456);
                            values.put(BaseLauncherColumns.INTENT, intent.toUri(0));
                            values.put(BaseLauncherColumns.TITLE, r2.getString(titleResId2));
                            values.put(BaseLauncherColumns.ITEM_TYPE, Integer.valueOf(1));
                            values.put("spanX", Integer.valueOf(1));
                            values.put("spanY", Integer.valueOf(1));
                            values.put(BaseLauncherColumns.ICON_TYPE, Integer.valueOf(0));
                            values.put(BaseLauncherColumns.ICON_PACKAGE, packageName);
                            values.put(BaseLauncherColumns.ICON_RESOURCE, r2.getResourceName(iconResId2));
                            values.put("_id", Long.valueOf(id));
                            if (LauncherProvider.dbInsertAndCheck(db, TAG_FAVORITES, null, values) < 0) {
                                return -1;
                            }
                            return id;
                        } catch (NameNotFoundException e) {
                            Log.w(LauncherProvider.TAG, "Unable to add shortcut: " + packageName + "/" + className, e);
                            return -1;
                        }
                    } catch (NameNotFoundException e2) {
                        cn = new ComponentName(packageManager.currentToCanonicalPackageNames(new String[]{packageName})[0], className);
                        info = packageManager.getActivityInfo(cn, 0);
                    }
                } else {
                    long id2 = generateNewId();
                    intent.setFlags(268435456);
                    values.put(BaseLauncherColumns.INTENT, intent.toUri(0));
                    values.put(BaseLauncherColumns.TITLE, r.getString(titleResId));
                    values.put(BaseLauncherColumns.ITEM_TYPE, Integer.valueOf(1));
                    values.put("spanX", Integer.valueOf(1));
                    values.put("spanY", Integer.valueOf(1));
                    values.put(BaseLauncherColumns.ICON_TYPE, Integer.valueOf(0));
                    values.put(BaseLauncherColumns.ICON_PACKAGE, this.mContext.getPackageName());
                    values.put(BaseLauncherColumns.ICON_RESOURCE, r.getResourceName(iconResId));
                    values.put("_id", Long.valueOf(id2));
                    if (LauncherProvider.dbInsertAndCheck(db, TAG_FAVORITES, null, values) < 0) {
                        return -1;
                    }
                    return id2;
                }
            } catch (URISyntaxException e3) {
                Log.w(LauncherProvider.TAG, "Shortcut has malformed uri: " + uri);
                return -1;
            }
        }
    }

    static class SqlArguments {
        public final String[] args;
        public final String table;
        public final String where;

        SqlArguments(Uri url, String where2, String[] args2) {
            if (url.getPathSegments().size() == 1) {
                this.table = (String) url.getPathSegments().get(0);
                this.where = where2;
                this.args = args2;
            } else if (url.getPathSegments().size() != 2) {
                throw new IllegalArgumentException("Invalid URI: " + url);
            } else if (!TextUtils.isEmpty(where2)) {
                throw new UnsupportedOperationException("WHERE clause not supported: " + url);
            } else {
                this.table = (String) url.getPathSegments().get(0);
                this.where = "_id=" + ContentUris.parseId(url);
                this.args = null;
            }
        }

        SqlArguments(Uri url) {
            if (url.getPathSegments().size() == 1) {
                this.table = (String) url.getPathSegments().get(0);
                this.where = null;
                this.args = null;
                return;
            }
            throw new IllegalArgumentException("Invalid URI: " + url);
        }
    }

    static {
        sCompatComponents.add(new ComponentName[]{new ComponentName("com.konka.multimedia", "com.konka.multimedia.modules.photo.AlbumActivity"), new ComponentName("com.konka.multimedia", "com.konka.multimedia.modules.AlbumActivity"), new ComponentName("com.konka.mm", "com.konka.mm.photo.PhotoDiskActivity")});
        sCompatComponents.add(new ComponentName[]{new ComponentName("com.konka.multimedia", "com.konka.multimedia.modules.BrowseTabActivity"), new ComponentName("com.konka.mm", "com.konka.mm.filemanager.FileDiskActivity")});
    }

    public boolean onCreate() {
        this.mOpenHelper = new DatabaseHelper(getContext());
        ((LauncherApplication) getContext()).setLauncherProvider(this);
        return true;
    }

    public String getType(Uri uri) {
        SqlArguments args = new SqlArguments(uri, null, null);
        if (TextUtils.isEmpty(args.where)) {
            return "vnd.android.cursor.dir/" + args.table;
        }
        return "vnd.android.cursor.item/" + args.table;
    }

    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SqlArguments args = new SqlArguments(uri, selection, selectionArgs);
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(args.table);
        Cursor result = qb.query(this.mOpenHelper.getWritableDatabase(), projection, args.where, args.args, null, null, sortOrder);
        result.setNotificationUri(getContext().getContentResolver(), uri);
        return result;
    }

    /* access modifiers changed from: private */
    public static long dbInsertAndCheck(SQLiteDatabase db, String table, String nullColumnHack, ContentValues values) {
        if (values.containsKey("_id")) {
            return db.insert(table, nullColumnHack, values);
        }
        throw new RuntimeException("Error: attempting to add item without specifying an id");
    }

    /* access modifiers changed from: private */
    public static void deleteId(SQLiteDatabase db, long id) {
        SqlArguments args = new SqlArguments(Favorites.getContentUri(id, false), null, null);
        db.delete(args.table, args.where, args.args);
    }

    public Uri insert(Uri uri, ContentValues initialValues) {
        long rowId = dbInsertAndCheck(this.mOpenHelper.getWritableDatabase(), new SqlArguments(uri).table, null, initialValues);
        if (rowId <= 0) {
            return null;
        }
        Uri uri2 = ContentUris.withAppendedId(uri, rowId);
        sendNotify(uri2);
        return uri2;
    }

    /* JADX INFO: finally extract failed */
    public int bulkInsert(Uri uri, ContentValues[] values) {
        SqlArguments args = new SqlArguments(uri);
        SQLiteDatabase db = this.mOpenHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            for (ContentValues dbInsertAndCheck : values) {
                if (dbInsertAndCheck(db, args.table, null, dbInsertAndCheck) < 0) {
                    db.endTransaction();
                    return 0;
                }
            }
            db.setTransactionSuccessful();
            db.endTransaction();
            sendNotify(uri);
            return values.length;
        } catch (Throwable th) {
            db.endTransaction();
            throw th;
        }
    }

    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SqlArguments args = new SqlArguments(uri, selection, selectionArgs);
        int count = this.mOpenHelper.getWritableDatabase().delete(args.table, args.where, args.args);
        if (count > 0) {
            sendNotify(uri);
        }
        return count;
    }

    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SqlArguments args = new SqlArguments(uri, selection, selectionArgs);
        int count = this.mOpenHelper.getWritableDatabase().update(args.table, values, args.where, args.args);
        if (count > 0) {
            sendNotify(uri);
        }
        return count;
    }

    private void sendNotify(Uri uri) {
        String notify = uri.getQueryParameter(PARAMETER_NOTIFY);
        if (notify == null || "true".equals(notify)) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
    }

    public long generateNewId() {
        return this.mOpenHelper.generateNewId();
    }

    static String buildOrWhereString(String column, int[] values) {
        StringBuilder selectWhere = new StringBuilder();
        for (int i = values.length - 1; i >= 0; i--) {
            selectWhere.append(column).append("=").append(values[i]);
            if (i > 0) {
                selectWhere.append(" OR ");
            }
        }
        return selectWhere.toString();
    }
}
