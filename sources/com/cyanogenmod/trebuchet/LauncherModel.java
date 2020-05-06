package com.cyanogenmod.trebuchet;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProviderInfo;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.Intent.ShortcutIconResource;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Parcelable;
import android.os.Process;
import android.os.RemoteException;
import android.os.SystemClock;
import android.util.Log;
import com.cyanogenmod.trebuchet.InstallWidgetReceiver.WidgetMimeTypeHandlerData;
import com.cyanogenmod.trebuchet.preference.PreferencesProvider.Interface.Homescreen;
import com.konka.kkinterface.tv.CommonDesk;
import com.konka.launcherblacklist.BlackListFilter.OnChangeListener;
import com.tencent.stat.common.StatConstants;
import java.lang.ref.WeakReference;
import java.lang.reflect.Array;
import java.net.URISyntaxException;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class LauncherModel extends BroadcastReceiver implements OnChangeListener {
    public static final Comparator<ApplicationInfo> APP_INSTALL_TIME_COMPARATOR = new Comparator<ApplicationInfo>() {
        public final int compare(ApplicationInfo a, ApplicationInfo b) {
            if (a.firstInstallTime < b.firstInstallTime) {
                return 1;
            }
            if (a.firstInstallTime > b.firstInstallTime) {
                return -1;
            }
            return 0;
        }
    };
    public static final Comparator<ApplicationInfo> APP_KONKA_DEFAULT_COMPARATOR = new Comparator<ApplicationInfo>() {
        public final int compare(ApplicationInfo a, ApplicationInfo b) {
            if (a.sortOrderID > b.sortOrderID) {
                return 1;
            }
            if (a.sortOrderID < b.sortOrderID) {
                return -1;
            }
            if (a.sortOrderID != b.sortOrderID || a.sortOrderID != 99) {
                return 0;
            }
            int result = LauncherModel.sCollator.compare(a.title.toString(), b.title.toString());
            if (result == 0) {
                return a.componentName.compareTo(b.componentName);
            }
            return result;
        }
    };
    public static final Comparator<ApplicationInfo> APP_NAME_COMPARATOR = new Comparator<ApplicationInfo>() {
        public final int compare(ApplicationInfo a, ApplicationInfo b) {
            int result = LauncherModel.sCollator.compare(a.title.toString(), b.title.toString());
            if (result == 0) {
                return a.componentName.compareTo(b.componentName);
            }
            return result;
        }
    };
    static final boolean DEBUG_LOADERS = true;
    private static final int ITEMS_CHUNK = 10;
    static final String TAG = "Launcher.Model";
    public static final Comparator<AppWidgetProviderInfo> WIDGET_NAME_COMPARATOR = new Comparator<AppWidgetProviderInfo>() {
        public final int compare(AppWidgetProviderInfo a, AppWidgetProviderInfo b) {
            return LauncherModel.sCollator.compare(a.label.toString(), b.label.toString());
        }
    };
    /* access modifiers changed from: private */
    public static int mCellCountX;
    /* access modifiers changed from: private */
    public static int mCellCountY;
    static int mNumberHomescreens = 7;
    static final ArrayList<LauncherAppWidgetInfo> sAppWidgets = new ArrayList<>();
    /* access modifiers changed from: private */
    public static final Collator sCollator = Collator.getInstance();
    static final HashMap<Object, byte[]> sDbIconCache = new HashMap<>();
    static final HashMap<Long, FolderInfo> sFolders = new HashMap<>();
    static final HashMap<Long, ItemInfo> sItemsIdMap = new HashMap<>();
    private static final Handler sWorker = new Handler(sWorkerThread.getLooper());
    private static final HandlerThread sWorkerThread = new HandlerThread("launcher-loader");
    static final ArrayList<ItemInfo> sWorkspaceItems = new ArrayList<>();
    /* access modifiers changed from: private */
    public AllAppsList mAllAppsList;
    /* access modifiers changed from: private */
    public boolean mAllAppsLoaded;
    /* access modifiers changed from: private */
    public final LauncherApplication mApp;
    private final boolean mAppsCanBeOnExternalStorage;
    /* access modifiers changed from: private */
    public WeakReference<Callbacks> mCallbacks;
    private Bitmap mDefaultIcon;
    /* access modifiers changed from: private */
    public DeferredHandler mHandler = new DeferredHandler();
    /* access modifiers changed from: private */
    public IconCache mIconCache;
    /* access modifiers changed from: private */
    public LoaderTask mLoaderTask;
    /* access modifiers changed from: private */
    public final Object mLock = new Object();
    ArrayList<ApplicationInfo> mMissedOnWorkspace = new ArrayList<>();
    ArrayList<ShortcutInfo> mNotExistingItems = new ArrayList<>();
    protected int mPreviousConfigMcc;
    /* access modifiers changed from: private */
    public boolean mWorkspaceLoaded;

    public interface Callbacks {
        void bindAllApplications(ArrayList<ApplicationInfo> arrayList);

        void bindAppWidget(LauncherAppWidgetInfo launcherAppWidgetInfo);

        void bindAppsAdded(ArrayList<ApplicationInfo> arrayList);

        void bindAppsRemoved(ArrayList<? extends ItemInfo> arrayList, boolean z);

        void bindAppsUpdated(ArrayList<ApplicationInfo> arrayList);

        void bindFolders(HashMap<Long, FolderInfo> hashMap);

        void bindItems(ArrayList<ItemInfo> arrayList, int i, int i2);

        void bindPackagesUpdated();

        void bindSearchablesChanged();

        void finishBindingItems();

        int getCurrentWorkspaceScreen();

        boolean isAllAppsVisible();

        boolean setLoadOnResume();

        void startBinding(int i);
    }

    private class LoaderTask implements Runnable {
        private Context mContext;
        private boolean mIsLaunching;
        private HashMap<Object, CharSequence> mLabelCache = new HashMap<>();
        /* access modifiers changed from: private */
        public boolean mLoadAndBindStepFinished;
        private boolean mStopped;
        private Thread mWaitThread;

        LoaderTask(Context context, boolean isLaunching) {
            this.mContext = context;
            this.mIsLaunching = isLaunching;
        }

        /* access modifiers changed from: 0000 */
        public boolean isLaunching() {
            return this.mIsLaunching;
        }

        private void loadAndBindWorkspace() {
            Log.d(LauncherModel.TAG, "loadAndBindWorkspace mWorkspaceLoaded=" + LauncherModel.this.mWorkspaceLoaded);
            if (!LauncherModel.this.mWorkspaceLoaded) {
                loadWorkspace();
                synchronized (this) {
                    if (!this.mStopped) {
                        LauncherModel.this.mWorkspaceLoaded = true;
                    } else {
                        return;
                    }
                }
            }
            LauncherModel.this.removeNotExisitingItems();
            bindWorkspace();
        }

        private void waitForIdle() {
            synchronized (this) {
                long workspaceWaitTime = SystemClock.uptimeMillis();
                LauncherModel.this.mHandler.postIdle(new Runnable() {
                    public void run() {
                        synchronized (LoaderTask.this) {
                            LoaderTask.this.mLoadAndBindStepFinished = true;
                            Log.d(LauncherModel.TAG, "done with previous binding step");
                            LoaderTask.this.notify();
                        }
                    }
                });
                while (!this.mStopped && !this.mLoadAndBindStepFinished) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                    }
                }
                Log.d(LauncherModel.TAG, "waited " + (SystemClock.uptimeMillis() - workspaceWaitTime) + "ms for previous step to finish binding");
            }
        }

        public void run() {
            boolean loadWorkspaceFirst = true;
            int i = 0;
            Callbacks cbk = (Callbacks) LauncherModel.this.mCallbacks.get();
            if (cbk != null && cbk.isAllAppsVisible()) {
                loadWorkspaceFirst = false;
            }
            synchronized (LauncherModel.this.mLock) {
                Log.d(LauncherModel.TAG, "Setting thread priority to " + (this.mIsLaunching ? "FOREGROUND" : "DEFAULT"));
                if (this.mIsLaunching) {
                    i = -2;
                }
                Process.setThreadPriority(i);
            }
            if (loadWorkspaceFirst) {
                Log.d(LauncherModel.TAG, "step 1: loading workspace");
                loadAndBindWorkspace();
            } else {
                Log.d(LauncherModel.TAG, "step 1: special: loading all apps");
                loadAndBindAllApps();
            }
            if (!this.mStopped) {
                synchronized (LauncherModel.this.mLock) {
                    if (this.mIsLaunching) {
                        Log.d(LauncherModel.TAG, "Setting thread priority to BACKGROUND");
                        Process.setThreadPriority(10);
                    }
                }
                waitForIdle();
                if (loadWorkspaceFirst) {
                    Log.d(LauncherModel.TAG, "step 2: loading all apps");
                    loadAndBindAllApps();
                } else {
                    Log.d(LauncherModel.TAG, "step 2: special: loading workspace");
                    loadAndBindWorkspace();
                }
                synchronized (LauncherModel.this.mLock) {
                    Process.setThreadPriority(0);
                }
            }
            Log.d(LauncherModel.TAG, "Comparing loaded icons to database icons");
            for (Object key : LauncherModel.sDbIconCache.keySet()) {
                LauncherModel.this.updateSavedIcon(this.mContext, (ShortcutInfo) key, (byte[]) LauncherModel.sDbIconCache.get(key));
            }
            LauncherModel.sDbIconCache.clear();
            this.mContext = null;
            synchronized (LauncherModel.this.mLock) {
                if (LauncherModel.this.mLoaderTask == this) {
                    LauncherModel.this.mLoaderTask = null;
                }
            }
        }

        public void stopLocked() {
            synchronized (this) {
                this.mStopped = true;
                notify();
            }
        }

        /* access modifiers changed from: 0000 */
        public Callbacks tryGetCallbacks(Callbacks oldCallbacks) {
            synchronized (LauncherModel.this.mLock) {
                if (this.mStopped) {
                    return null;
                }
                if (LauncherModel.this.mCallbacks == null) {
                    return null;
                }
                Callbacks callbacks = (Callbacks) LauncherModel.this.mCallbacks.get();
                if (callbacks != oldCallbacks) {
                    return null;
                }
                if (callbacks != null) {
                    return callbacks;
                }
                Log.w(LauncherModel.TAG, "no mCallbacks");
                return null;
            }
        }

        private boolean checkItemPlacement(ItemInfo[][][] occupied, ItemInfo item) {
            int containerIndex = item.screen;
            if (item.container == -101) {
                if (occupied[LauncherModel.mNumberHomescreens][item.cellX][item.cellY] != null) {
                    Log.e(LauncherModel.TAG, "Error loading shortcut into hotseat " + item + " into position (" + item.screen + ":" + item.cellX + "," + item.cellY + ") occupied by " + occupied[LauncherModel.mNumberHomescreens][item.cellX][item.cellY]);
                    return false;
                }
                occupied[LauncherModel.mNumberHomescreens][item.cellX][item.cellY] = item;
                return true;
            } else if (item.container != -100) {
                return true;
            } else {
                for (int x = item.cellX; x < item.cellX + item.spanX; x++) {
                    for (int y = item.cellY; y < item.cellY + item.spanY; y++) {
                        if (occupied[containerIndex][x][y] != null) {
                            Log.e(LauncherModel.TAG, "Error loading shortcut " + item + " into cell (" + containerIndex + "-" + item.screen + ":" + x + "," + y + ") occupied by " + occupied[containerIndex][x][y]);
                            return false;
                        }
                    }
                }
                for (int x2 = item.cellX; x2 < item.cellX + item.spanX; x2++) {
                    for (int y2 = item.cellY; y2 < item.cellY + item.spanY; y2++) {
                        occupied[containerIndex][x2][y2] = item;
                    }
                }
                return true;
            }
        }

        private void loadWorkspace() {
            ShortcutInfo info;
            long t = SystemClock.uptimeMillis();
            Context context = this.mContext;
            ContentResolver contentResolver = context.getContentResolver();
            PackageManager manager = context.getPackageManager();
            AppWidgetManager instance = AppWidgetManager.getInstance(context);
            boolean isSafeMode = manager.isSafeMode();
            LauncherModel.sWorkspaceItems.clear();
            LauncherModel.sAppWidgets.clear();
            LauncherModel.sFolders.clear();
            LauncherModel.sItemsIdMap.clear();
            LauncherModel.sDbIconCache.clear();
            ArrayList<Long> itemsToRemove = new ArrayList<>();
            Cursor c = contentResolver.query(Favorites.CONTENT_URI, null, null, null, null);
            ItemInfo[][][] occupied = (ItemInfo[][][]) Array.newInstance(ItemInfo.class, new int[]{LauncherModel.mNumberHomescreens + 1, LauncherModel.mCellCountX + 1, LauncherModel.mCellCountY + 1});
            try {
                int idIndex = c.getColumnIndexOrThrow("_id");
                int intentIndex = c.getColumnIndexOrThrow(BaseLauncherColumns.INTENT);
                int titleIndex = c.getColumnIndexOrThrow(BaseLauncherColumns.TITLE);
                int iconTypeIndex = c.getColumnIndexOrThrow(BaseLauncherColumns.ICON_TYPE);
                int iconIndex = c.getColumnIndexOrThrow(BaseLauncherColumns.ICON);
                int iconPackageIndex = c.getColumnIndexOrThrow(BaseLauncherColumns.ICON_PACKAGE);
                int iconResourceIndex = c.getColumnIndexOrThrow(BaseLauncherColumns.ICON_RESOURCE);
                int containerIndex = c.getColumnIndexOrThrow("container");
                int itemTypeIndex = c.getColumnIndexOrThrow(BaseLauncherColumns.ITEM_TYPE);
                int columnIndexOrThrow = c.getColumnIndexOrThrow("appWidgetId");
                int screenIndex = c.getColumnIndexOrThrow("screen");
                int cellXIndex = c.getColumnIndexOrThrow("cellX");
                int cellYIndex = c.getColumnIndexOrThrow("cellY");
                int columnIndexOrThrow2 = c.getColumnIndexOrThrow("spanX");
                int columnIndexOrThrow3 = c.getColumnIndexOrThrow("spanY");
                int columnIndexOrThrow4 = c.getColumnIndexOrThrow("uri");
                int columnIndexOrThrow5 = c.getColumnIndexOrThrow("displayMode");
                int lockIndex = c.getColumnIndexOrThrow("lock");
                Log.d(LauncherModel.TAG, "app size = " + c.getCount());
                while (!this.mStopped && c.moveToNext()) {
                    int itemType = c.getInt(itemTypeIndex);
                    Log.d(LauncherModel.TAG, "itemType=" + itemType);
                    switch (itemType) {
                        case 0:
                        case 1:
                            try {
                                Intent intent = Intent.parseUri(c.getString(intentIndex), 0);
                                if (itemType == 0) {
                                    info = LauncherModel.this.getShortcutInfo(manager, intent, context, c, iconIndex, titleIndex, this.mLabelCache);
                                } else {
                                    info = LauncherModel.this.getShortcutInfo(c, context, iconTypeIndex, iconPackageIndex, iconResourceIndex, iconIndex, titleIndex);
                                }
                                if (info == null) {
                                    long id = c.getLong(idIndex);
                                    Log.e(LauncherModel.TAG, "Error loading shortcut " + id + ", removing it");
                                    contentResolver.delete(Favorites.getContentUri(id, false), null, null);
                                    break;
                                } else {
                                    info.intent = intent;
                                    info.id = c.getLong(idIndex);
                                    int container = c.getInt(containerIndex);
                                    info.container = (long) container;
                                    info.screen = c.getInt(screenIndex);
                                    info.cellX = c.getInt(cellXIndex);
                                    info.cellY = c.getInt(cellYIndex);
                                    info.isLock = c.getInt(lockIndex) != 0;
                                    Log.e(LauncherModel.TAG, "title = " + info.title);
                                    AppInfoManager.getInstance().setIOS7Icon(info, LauncherModel.this.mIconCache);
                                    if (!checkItemPlacement(occupied, info)) {
                                        break;
                                    } else {
                                        switch (container) {
                                            case CommonDesk.SETIS_END_COMPLETE /*-101*/:
                                            case CommonDesk.SETIS_START /*-100*/:
                                                LauncherModel.sWorkspaceItems.add(info);
                                                break;
                                            default:
                                                LauncherModel.findOrMakeFolder(LauncherModel.sFolders, (long) container).add(info);
                                                break;
                                        }
                                        LauncherModel.sItemsIdMap.put(Long.valueOf(info.id), info);
                                        LauncherModel.this.queueIconToBeChecked(LauncherModel.sDbIconCache, info, c, iconIndex);
                                        break;
                                    }
                                }
                            } catch (URISyntaxException e) {
                                Log.d(LauncherModel.TAG, "uri error");
                                break;
                            }
                        case 2:
                            long id2 = c.getLong(idIndex);
                            FolderInfo folderInfo = LauncherModel.findOrMakeFolder(LauncherModel.sFolders, id2);
                            folderInfo.title = c.getString(titleIndex);
                            folderInfo.id = id2;
                            int container2 = c.getInt(containerIndex);
                            folderInfo.container = (long) container2;
                            folderInfo.screen = c.getInt(screenIndex);
                            folderInfo.cellX = c.getInt(cellXIndex);
                            folderInfo.cellY = c.getInt(cellYIndex);
                            folderInfo.isLock = c.getInt(lockIndex) != 0;
                            if (!checkItemPlacement(occupied, folderInfo)) {
                                break;
                            } else {
                                switch (container2) {
                                    case CommonDesk.SETIS_END_COMPLETE /*-101*/:
                                    case CommonDesk.SETIS_START /*-100*/:
                                        LauncherModel.sWorkspaceItems.add(folderInfo);
                                        break;
                                }
                                LauncherModel.sItemsIdMap.put(Long.valueOf(folderInfo.id), folderInfo);
                                LauncherModel.sFolders.put(Long.valueOf(folderInfo.id), folderInfo);
                                break;
                            }
                    }
                }
                if (this.mStopped) {
                    Log.d(LauncherModel.TAG, "mStopped is true");
                }
                if (c.isAfterLast()) {
                    Log.d(LauncherModel.TAG, "cursor is at the last");
                }
                c.close();
                if (itemsToRemove.size() > 0) {
                    ContentProviderClient client = contentResolver.acquireContentProviderClient(Favorites.CONTENT_URI);
                    Iterator it = itemsToRemove.iterator();
                    while (it.hasNext()) {
                        long id3 = ((Long) it.next()).longValue();
                        Log.d(LauncherModel.TAG, "Removed id = " + id3);
                        try {
                            client.delete(Favorites.getContentUri(id3, false), null, null);
                        } catch (RemoteException e2) {
                            Log.w(LauncherModel.TAG, "Could not remove id = " + id3);
                        }
                    }
                }
                Log.d(LauncherModel.TAG, "loaded workspace in " + (SystemClock.uptimeMillis() - t) + "ms");
                Log.d(LauncherModel.TAG, "workspace layout: ");
                for (int y = 0; y < LauncherModel.mCellCountY; y++) {
                    String line = StatConstants.MTA_COOPERATION_TAG;
                    for (int s = 0; s < LauncherModel.mNumberHomescreens; s++) {
                        if (s > 0) {
                            line = new StringBuilder(String.valueOf(line)).append(" | ").toString();
                        }
                        for (int x = 0; x < LauncherModel.mCellCountX; x++) {
                            line = new StringBuilder(String.valueOf(line)).append(occupied[s][x][y] != null ? "#" : ".").toString();
                        }
                    }
                    Log.d(LauncherModel.TAG, "[ " + line + " ]");
                }
            } catch (Exception e3) {
                Log.w(LauncherModel.TAG, "Desktop items loading interrupted:", e3);
            } catch (Throwable th) {
                c.close();
                throw th;
            }
        }

        private int getMaxOccupiedWorkspaceIndex() {
            int maxOccupiedWorkspaceIdx = 0;
            for (ItemInfo info : LauncherModel.sItemsIdMap.values()) {
                switch (info.itemType) {
                    case 0:
                    case 1:
                        if (info.container == -100 && info.screen > maxOccupiedWorkspaceIdx) {
                            maxOccupiedWorkspaceIdx = info.screen;
                            break;
                        }
                }
            }
            return maxOccupiedWorkspaceIdx;
        }

        private void bindWorkspace() {
            final long t = SystemClock.uptimeMillis();
            final Callbacks oldCallbacks = (Callbacks) LauncherModel.this.mCallbacks.get();
            if (oldCallbacks == null) {
                Log.w(LauncherModel.TAG, "LoaderTask running with no launcher");
                return;
            }
            final int maxOccupiedWorkspaceIndex = getMaxOccupiedWorkspaceIndex();
            LauncherModel.this.mHandler.post(new Runnable() {
                public void run() {
                    Callbacks callbacks = LoaderTask.this.tryGetCallbacks(oldCallbacks);
                    if (callbacks != null) {
                        callbacks.startBinding(maxOccupiedWorkspaceIndex);
                    }
                }
            });
            final ArrayList<ItemInfo> workspaceItems = LauncherModel.this.unbindWorkspaceItemsOnMainThread();
            int N = workspaceItems.size();
            int i = 0;
            while (i < N) {
                final int start = i;
                final int chunkSize = i + 10 <= N ? 10 : N - i;
                LauncherModel.this.mHandler.post(new Runnable() {
                    public void run() {
                        Callbacks callbacks = LoaderTask.this.tryGetCallbacks(oldCallbacks);
                        if (callbacks != null) {
                            callbacks.bindItems(workspaceItems, start, start + chunkSize);
                        }
                    }
                });
                i += 10;
            }
            final HashMap<Long, FolderInfo> folders = new HashMap<>(LauncherModel.sFolders);
            LauncherModel.this.mHandler.post(new Runnable() {
                public void run() {
                    Callbacks callbacks = LoaderTask.this.tryGetCallbacks(oldCallbacks);
                    if (callbacks != null) {
                        callbacks.bindFolders(folders);
                    }
                }
            });
            LauncherModel.this.mHandler.post(new Runnable() {
                public void run() {
                    Log.d(LauncherModel.TAG, "Going to start binding widgets soon.");
                }
            });
            int currentScreen = oldCallbacks.getCurrentWorkspaceScreen();
            int N2 = LauncherModel.sAppWidgets.size();
            for (int i2 = 0; i2 < N2; i2++) {
                final LauncherAppWidgetInfo widget = (LauncherAppWidgetInfo) LauncherModel.sAppWidgets.get(i2);
                if (widget.screen == currentScreen) {
                    LauncherModel.this.mHandler.post(new Runnable() {
                        public void run() {
                            Callbacks callbacks = LoaderTask.this.tryGetCallbacks(oldCallbacks);
                            if (callbacks != null) {
                                callbacks.bindAppWidget(widget);
                            }
                        }
                    });
                }
            }
            for (int i3 = 0; i3 < N2; i3++) {
                final LauncherAppWidgetInfo widget2 = (LauncherAppWidgetInfo) LauncherModel.sAppWidgets.get(i3);
                if (widget2.screen != currentScreen) {
                    LauncherModel.this.mHandler.post(new Runnable() {
                        public void run() {
                            Callbacks callbacks = LoaderTask.this.tryGetCallbacks(oldCallbacks);
                            if (callbacks != null) {
                                callbacks.bindAppWidget(widget2);
                            }
                        }
                    });
                }
            }
            LauncherModel.this.mHandler.post(new Runnable() {
                public void run() {
                    Callbacks callbacks = LoaderTask.this.tryGetCallbacks(oldCallbacks);
                    if (callbacks != null) {
                        callbacks.finishBindingItems();
                    }
                }
            });
            if (LauncherModel.this.mNotExistingItems.size() > 0) {
                LauncherModel.this.mHandler.post(new Runnable() {
                    public void run() {
                        Callbacks callbacks = LoaderTask.this.tryGetCallbacks(oldCallbacks);
                        if (callbacks != null) {
                            callbacks.bindAppsRemoved(LauncherModel.this.mNotExistingItems, false);
                        }
                    }
                });
            }
            LauncherModel.this.mHandler.post(new Runnable() {
                public void run() {
                    Log.d(LauncherModel.TAG, "bound workspace in " + (SystemClock.uptimeMillis() - t) + "ms");
                }
            });
        }

        private void loadAndBindAllApps() {
            Log.d(LauncherModel.TAG, "loadAndBindAllApps mAllAppsLoaded=" + LauncherModel.this.mAllAppsLoaded);
            if (!LauncherModel.this.mAllAppsLoaded) {
                loadAllAppsByBatch();
                synchronized (this) {
                    if (!this.mStopped) {
                        LauncherModel.this.mAllAppsLoaded = true;
                        return;
                    }
                    return;
                }
            }
            onlyBindAllApps();
        }

        private void onlyBindAllApps() {
            final Callbacks oldCallbacks = (Callbacks) LauncherModel.this.mCallbacks.get();
            if (oldCallbacks == null) {
                Log.w(LauncherModel.TAG, "LoaderTask running with no launcher (onlyBindAllApps)");
                return;
            }
            final ArrayList<ApplicationInfo> list = (ArrayList) LauncherModel.this.mAllAppsList.data.clone();
            Iterator it = list.iterator();
            while (it.hasNext()) {
                ApplicationInfo info = (ApplicationInfo) it.next();
                Log.d("czj-ios", "appinfo:" + info.toString() + ";id=" + info.id + ";intent=" + info.intent.toUri(0));
            }
            LauncherModel.this.findMissedOnWorkspace(this.mContext, list);
            LauncherModel.this.mHandler.post(new Runnable() {
                public void run() {
                    long t = SystemClock.uptimeMillis();
                    Callbacks callbacks = LoaderTask.this.tryGetCallbacks(oldCallbacks);
                    if (callbacks != null) {
                        Log.d("czj-ios", "call onlyBindAllApps");
                        callbacks.bindAllApplications(list);
                    }
                    Log.d(LauncherModel.TAG, "bound all " + list.size() + " apps from cache in " + (SystemClock.uptimeMillis() - t) + "ms");
                }
            });
        }

        private void loadAllAppsByBatch() {
            long t = SystemClock.uptimeMillis();
            Callbacks oldCallbacks = (Callbacks) LauncherModel.this.mCallbacks.get();
            if (oldCallbacks == null) {
                Log.w(LauncherModel.TAG, "LoaderTask running with no launcher (loadAllAppsByBatch)");
                return;
            }
            Intent mainIntent = new Intent("android.intent.action.MAIN", null);
            mainIntent.addCategory("android.intent.category.LAUNCHER");
            PackageManager packageManager = this.mContext.getPackageManager();
            List<ResolveInfo> apps = null;
            int N = Integer.MAX_VALUE;
            int i = 0;
            while (i < N && !this.mStopped) {
                if (i == 0) {
                    LauncherModel.this.mAllAppsList.clear();
                    long qiaTime = SystemClock.uptimeMillis();
                    apps = packageManager.queryIntentActivities(mainIntent, 0);
                    Log.d(LauncherModel.TAG, "queryIntentActivities took " + (SystemClock.uptimeMillis() - qiaTime) + "ms");
                    if (apps != null) {
                        N = apps.size();
                        Log.d(LauncherModel.TAG, "queryIntentActivities got " + N + " apps");
                        if (N != 0) {
                            long sortTime = SystemClock.uptimeMillis();
                            ShortcutNameComparator shortcutNameComparator = new ShortcutNameComparator(packageManager, this.mLabelCache);
                            Collections.sort(apps, shortcutNameComparator);
                            Log.d(LauncherModel.TAG, "sort took " + (SystemClock.uptimeMillis() - sortTime) + "ms");
                        } else {
                            return;
                        }
                    } else {
                        return;
                    }
                }
                long t2 = SystemClock.uptimeMillis();
                int startIndex = i;
                int j = 0;
                while (i < N && j < N) {
                    AllAppsList access$17 = LauncherModel.this.mAllAppsList;
                    ApplicationInfo applicationInfo = new ApplicationInfo(packageManager, (ResolveInfo) apps.get(i), LauncherModel.this.mIconCache, this.mLabelCache);
                    access$17.add(applicationInfo);
                    i++;
                    j++;
                }
                final boolean first = i <= N;
                final Callbacks callbacks = tryGetCallbacks(oldCallbacks);
                final ArrayList<ApplicationInfo> added = LauncherModel.this.mAllAppsList.added;
                LauncherModel.this.mAllAppsList.added = new ArrayList();
                LauncherModel.this.findMissedOnWorkspace(this.mContext, added);
                DeferredHandler access$4 = LauncherModel.this.mHandler;
                AnonymousClass12 r0 = new Runnable() {
                    public void run() {
                        long t = SystemClock.uptimeMillis();
                        if (callbacks != null) {
                            if (first) {
                                Log.d("czj-ios", "call loadAllAppsByBatch");
                                callbacks.bindAllApplications(added);
                            } else {
                                callbacks.bindAppsAdded(added);
                            }
                            Log.d(LauncherModel.TAG, "bound " + added.size() + " apps in " + (SystemClock.uptimeMillis() - t) + "ms");
                            return;
                        }
                        Log.i(LauncherModel.TAG, "not binding apps: no Launcher activity");
                    }
                };
                access$4.post(r0);
                Log.d(LauncherModel.TAG, "batch of " + (i - startIndex) + " icons processed in " + (SystemClock.uptimeMillis() - t2) + "ms");
            }
            Log.d(LauncherModel.TAG, "cached all " + N + " apps in " + (SystemClock.uptimeMillis() - t) + "ms");
        }

        public void dumpState() {
            Log.d(LauncherModel.TAG, "mLoaderTask.mContext=" + this.mContext);
            Log.d(LauncherModel.TAG, "mLoaderTask.mWaitThread=" + this.mWaitThread);
            Log.d(LauncherModel.TAG, "mLoaderTask.mIsLaunching=" + this.mIsLaunching);
            Log.d(LauncherModel.TAG, "mLoaderTask.mStopped=" + this.mStopped);
            Log.d(LauncherModel.TAG, "mLoaderTask.mLoadAndBindStepFinished=" + this.mLoadAndBindStepFinished);
            Log.d(LauncherModel.TAG, "mItems size=" + LauncherModel.sWorkspaceItems.size());
        }
    }

    private class PackageUpdatedTask implements Runnable {
        public static final int OP_ADD = 1;
        public static final int OP_NONE = 0;
        public static final int OP_REMOVE = 3;
        public static final int OP_UNAVAILABLE = 4;
        public static final int OP_UPDATE = 2;
        int mOp;
        String[] mPackages;

        public PackageUpdatedTask(int op, String[] packages) {
            this.mOp = op;
            this.mPackages = packages;
        }

        public void run() {
            Context context = LauncherModel.this.mApp;
            String[] packages = this.mPackages;
            int N = packages.length;
            switch (this.mOp) {
                case 1:
                    for (int i = 0; i < N; i++) {
                        Log.d(LauncherModel.TAG, "mAllAppsList.addPackage " + packages[i]);
                        LauncherModel.this.mAllAppsList.addPackage(context, packages[i]);
                    }
                    break;
                case 2:
                    for (int i2 = 0; i2 < N; i2++) {
                        Log.d(LauncherModel.TAG, "mAllAppsList.updatePackage " + packages[i2]);
                        LauncherModel.this.mAllAppsList.updatePackage(context, packages[i2]);
                    }
                    break;
                case 3:
                case 4:
                    for (int i3 = 0; i3 < N; i3++) {
                        Log.d(LauncherModel.TAG, "mAllAppsList.removePackage " + packages[i3]);
                        LauncherModel.this.mAllAppsList.removePackage(packages[i3]);
                    }
                    break;
            }
            ArrayList<ApplicationInfo> added = null;
            ArrayList<ApplicationInfo> removed = null;
            ArrayList<ApplicationInfo> modified = null;
            if (LauncherModel.this.mAllAppsList.added.size() > 0) {
                added = LauncherModel.this.mAllAppsList.added;
                LauncherModel.this.mAllAppsList.added = new ArrayList<>();
            }
            if (LauncherModel.this.mAllAppsList.removed.size() > 0) {
                removed = LauncherModel.this.mAllAppsList.removed;
                LauncherModel.this.mAllAppsList.removed = new ArrayList<>();
                Iterator it = removed.iterator();
                while (it.hasNext()) {
                    LauncherModel.this.mIconCache.remove(((ApplicationInfo) it.next()).intent.getComponent());
                }
            }
            if (LauncherModel.this.mAllAppsList.modified.size() > 0) {
                modified = LauncherModel.this.mAllAppsList.modified;
                LauncherModel.this.mAllAppsList.modified = new ArrayList<>();
            }
            final Callbacks callbacks = LauncherModel.this.mCallbacks != null ? (Callbacks) LauncherModel.this.mCallbacks.get() : null;
            if (callbacks == null) {
                Log.w(LauncherModel.TAG, "Nobody to tell about the new app.  Launcher is probably loading.");
                return;
            }
            if (added != null) {
                final ArrayList<ApplicationInfo> addedFinal = added;
                LauncherModel.this.findMissedOnWorkspace(context, added);
                LauncherModel.this.mHandler.post(new Runnable() {
                    public void run() {
                        Callbacks cb = LauncherModel.this.mCallbacks != null ? (Callbacks) LauncherModel.this.mCallbacks.get() : null;
                        if (callbacks == cb && cb != null) {
                            callbacks.bindAppsAdded(addedFinal);
                        }
                    }
                });
            }
            if (modified != null) {
                final ArrayList<ApplicationInfo> modifiedFinal = modified;
                LauncherModel.this.mHandler.post(new Runnable() {
                    public void run() {
                        Callbacks cb = LauncherModel.this.mCallbacks != null ? (Callbacks) LauncherModel.this.mCallbacks.get() : null;
                        if (callbacks == cb && cb != null) {
                            callbacks.bindAppsUpdated(modifiedFinal);
                        }
                    }
                });
            }
            if (removed != null) {
                final boolean permanent = this.mOp != 4;
                final ArrayList<ApplicationInfo> removedFinal = removed;
                LauncherModel.this.mHandler.post(new Runnable() {
                    public void run() {
                        Callbacks cb = LauncherModel.this.mCallbacks != null ? (Callbacks) LauncherModel.this.mCallbacks.get() : null;
                        if (callbacks == cb && cb != null) {
                            callbacks.bindAppsRemoved(removedFinal, permanent);
                        }
                    }
                });
            }
            LauncherModel.this.mHandler.post(new Runnable() {
                public void run() {
                    Callbacks cb = LauncherModel.this.mCallbacks != null ? (Callbacks) LauncherModel.this.mCallbacks.get() : null;
                    if (callbacks == cb && cb != null) {
                        callbacks.bindPackagesUpdated();
                    }
                }
            });
        }
    }

    public static class ShortcutNameComparator implements Comparator<ResolveInfo> {
        private HashMap<Object, CharSequence> mLabelCache;
        private PackageManager mPackageManager;

        ShortcutNameComparator(PackageManager pm) {
            this.mPackageManager = pm;
            this.mLabelCache = new HashMap<>();
        }

        ShortcutNameComparator(PackageManager pm, HashMap<Object, CharSequence> labelCache) {
            this.mPackageManager = pm;
            this.mLabelCache = labelCache;
        }

        public final int compare(ResolveInfo a, ResolveInfo b) {
            CharSequence labelA;
            CharSequence labelB;
            ComponentName keyA = LauncherModel.getComponentNameFromResolveInfo(a);
            ComponentName keyB = LauncherModel.getComponentNameFromResolveInfo(b);
            if (this.mLabelCache.containsKey(keyA)) {
                labelA = (CharSequence) this.mLabelCache.get(keyA);
            } else {
                labelA = a.loadLabel(this.mPackageManager).toString();
                this.mLabelCache.put(keyA, labelA);
            }
            if (this.mLabelCache.containsKey(keyB)) {
                labelB = (CharSequence) this.mLabelCache.get(keyB);
            } else {
                labelB = b.loadLabel(this.mPackageManager).toString();
                this.mLabelCache.put(keyB, labelB);
            }
            return LauncherModel.sCollator.compare(labelA, labelB);
        }
    }

    public static class WidgetAndShortcutNameComparator implements Comparator<Object> {
        private HashMap<Object, String> mLabelCache = new HashMap<>();
        private PackageManager mPackageManager;

        WidgetAndShortcutNameComparator(PackageManager pm) {
            this.mPackageManager = pm;
        }

        public final int compare(Object a, Object b) {
            String labelA;
            String labelB;
            if (this.mLabelCache.containsKey(a)) {
                labelA = (String) this.mLabelCache.get(a);
            } else {
                if (a instanceof AppWidgetProviderInfo) {
                    labelA = ((AppWidgetProviderInfo) a).label;
                } else {
                    labelA = ((ResolveInfo) a).loadLabel(this.mPackageManager).toString();
                }
                this.mLabelCache.put(a, labelA);
            }
            if (this.mLabelCache.containsKey(b)) {
                labelB = (String) this.mLabelCache.get(b);
            } else {
                if (b instanceof AppWidgetProviderInfo) {
                    labelB = ((AppWidgetProviderInfo) b).label;
                } else {
                    labelB = ((ResolveInfo) b).loadLabel(this.mPackageManager).toString();
                }
                this.mLabelCache.put(b, labelB);
            }
            return LauncherModel.sCollator.compare(labelA, labelB);
        }
    }

    static {
        sWorkerThread.start();
    }

    LauncherModel(LauncherApplication app, IconCache iconCache) {
        this.mAppsCanBeOnExternalStorage = !Environment.isExternalStorageEmulated();
        this.mApp = app;
        this.mAllAppsList = new AllAppsList(iconCache, this.mApp);
        this.mIconCache = iconCache;
        this.mDefaultIcon = Utilities.createIconBitmap(this.mIconCache.getFullResDefaultActivityIcon(), (Context) app);
        this.mPreviousConfigMcc = app.getResources().getConfiguration().mcc;
    }

    public void onAppsBlackListChange(ArrayList<ComponentName> newList) {
        final ArrayList<ApplicationInfo> removedApps = new ArrayList<>();
        ArrayList<String> removedPkgs = new ArrayList<>();
        Iterator it = newList.iterator();
        while (it.hasNext()) {
            ComponentName cn = (ComponentName) it.next();
            if (StatConstants.MTA_COOPERATION_TAG.equals(cn.getClassName())) {
                removedPkgs.add(cn.getPackageName());
            } else {
                ApplicationInfo info = new ApplicationInfo();
                info.componentName = cn;
                info.intent = new Intent();
                info.intent.setComponent(info.componentName);
                removedApps.add(info);
            }
        }
        if (removedApps.size() > 0) {
            final Callbacks callbacks = this.mCallbacks != null ? (Callbacks) this.mCallbacks.get() : null;
            this.mHandler.post(new Runnable() {
                public void run() {
                    Callbacks cb = LauncherModel.this.mCallbacks != null ? (Callbacks) LauncherModel.this.mCallbacks.get() : null;
                    if (callbacks == cb && cb != null) {
                        callbacks.bindAppsRemoved(removedApps, true);
                    }
                }
            });
        }
        if (removedPkgs.size() > 0) {
            enqueuePackageUpdated(new PackageUpdatedTask(3, (String[]) removedPkgs.toArray()));
        }
    }

    public void onWidgetsBlackListChange(ArrayList<ComponentName> newList) {
        if (newList != null && newList.size() > 0) {
            final Callbacks callbacks = this.mCallbacks != null ? (Callbacks) this.mCallbacks.get() : null;
            final ArrayList<ApplicationInfo> removeList = new ArrayList<>();
            Iterator it = newList.iterator();
            while (it.hasNext()) {
                ComponentName cn = (ComponentName) it.next();
                ApplicationInfo info = new ApplicationInfo();
                info.componentName = cn;
                removeList.add(info);
            }
            this.mHandler.post(new Runnable() {
                public void run() {
                    Callbacks cb = LauncherModel.this.mCallbacks != null ? (Callbacks) LauncherModel.this.mCallbacks.get() : null;
                    if (callbacks == cb && cb != null) {
                        callbacks.bindPackagesUpdated();
                        if (callbacks instanceof Launcher) {
                            Workspace ws = ((Launcher) callbacks).getWorkspace();
                            if (ws != null) {
                                ws.removeItems(removeList);
                            }
                        }
                    }
                }
            });
        }
    }

    public void onShortcutsBlackListChange(ArrayList<ComponentName> newList) {
        onWidgetsBlackListChange(newList);
    }

    public Bitmap getFallbackIcon() {
        return Bitmap.createBitmap(this.mDefaultIcon);
    }

    public void unbindWorkspaceItems() {
        sWorker.post(new Runnable() {
            public void run() {
                LauncherModel.this.unbindWorkspaceItemsOnMainThread();
            }
        });
    }

    /* access modifiers changed from: private */
    public ArrayList<ItemInfo> unbindWorkspaceItemsOnMainThread() {
        final ArrayList<ItemInfo> workspaceItems = new ArrayList<>(sWorkspaceItems);
        final ArrayList<ItemInfo> appWidgets = new ArrayList<>(sAppWidgets);
        this.mHandler.post(new Runnable() {
            public void run() {
                Iterator it = workspaceItems.iterator();
                while (it.hasNext()) {
                    ((ItemInfo) it.next()).unbind();
                }
                Iterator it2 = appWidgets.iterator();
                while (it2.hasNext()) {
                    ((ItemInfo) it2.next()).unbind();
                }
            }
        });
        return workspaceItems;
    }

    static void addOrMoveItemInDatabase(Context context, ItemInfo item, long container, int screen, int cellX, int cellY) {
        if (item.container == -1) {
            Log.d(TAG, "addOrMoveItemInDatabase call addItemToDatabase");
            addItemToDatabase(context, item, container, screen, cellX, cellY, false);
            return;
        }
        moveItemInDatabase(context, item, container, screen, cellX, cellY);
    }

    static void updateItemInDatabaseHelper(Context context, ContentValues values, ItemInfo item, String callingFunction) {
        final long itemId = item.id;
        final Uri uri = Favorites.getContentUri(itemId, false);
        final ContentResolver cr = context.getContentResolver();
        final ContentValues contentValues = values;
        final ItemInfo itemInfo = item;
        final String str = callingFunction;
        Runnable r = new Runnable() {
            public void run() {
                Log.d(LauncherModel.TAG, "updateItemInDatabase: ret=" + cr.update(uri, contentValues, null, null) + ", uri=" + uri + ", values=" + contentValues);
                ItemInfo modelItem = (ItemInfo) LauncherModel.sItemsIdMap.get(Long.valueOf(itemId));
                if (itemInfo != modelItem) {
                    Log.d("czj-ios", "item:id=" + itemInfo.id);
                    for (Long longValue : LauncherModel.sItemsIdMap.keySet()) {
                        long key = longValue.longValue();
                        Log.d("czj-ios", "map:id=" + key + ";value:" + ((ItemInfo) LauncherModel.sItemsIdMap.get(Long.valueOf(key))).toString());
                    }
                    throw new RuntimeException("item: " + (itemInfo != null ? itemInfo.toString() : "null") + "modelItem: " + (modelItem != null ? modelItem.toString() : "null") + "Error: ItemInfo passed to " + str + " doesn't match original");
                } else if (modelItem.container != -100 && modelItem.container != -101) {
                    LauncherModel.sWorkspaceItems.remove(modelItem);
                } else if (!LauncherModel.sWorkspaceItems.contains(modelItem)) {
                    LauncherModel.sWorkspaceItems.add(modelItem);
                }
            }
        };
        if (sWorkerThread.getThreadId() == Process.myTid()) {
            r.run();
        } else {
            sWorker.post(r);
        }
    }

    static void moveItemInDatabase(Context context, ItemInfo item, long container, int screen, int cellX, int cellY) {
        item.container = container;
        item.cellX = cellX;
        item.cellY = cellY;
        if (!(context instanceof Launcher) || screen >= 0 || container != -101) {
            item.screen = screen;
        } else {
            item.screen = ((Launcher) context).getHotseat().getOrderInHotseat(cellX, cellY);
        }
        ContentValues values = new ContentValues();
        values.put("container", Long.valueOf(item.container));
        values.put("cellX", Integer.valueOf(item.cellX));
        values.put("cellY", Integer.valueOf(item.cellY));
        values.put("screen", Integer.valueOf(item.screen));
        updateItemInDatabaseHelper(context, values, item, "moveItemInDatabase");
    }

    static void resizeItemInDatabase(Context context, ItemInfo item, int cellX, int cellY, int spanX, int spanY) {
        item.spanX = spanX;
        item.spanY = spanY;
        item.cellX = cellX;
        item.cellY = cellY;
        ContentValues values = new ContentValues();
        values.put("container", Long.valueOf(item.container));
        values.put("spanX", Integer.valueOf(spanX));
        values.put("spanY", Integer.valueOf(spanY));
        values.put("cellX", Integer.valueOf(cellX));
        values.put("cellY", Integer.valueOf(cellY));
        updateItemInDatabaseHelper(context, values, item, "resizeItemInDatabase");
    }

    static void updateItemInDatabase(Context context, ItemInfo item) {
        ContentValues values = new ContentValues();
        item.onAddToDatabase(values);
        item.updateValuesWithCoordinates(values, item.cellX, item.cellY);
        updateItemInDatabaseHelper(context, values, item, "updateItemInDatabase");
    }

    static void updateItemsInDatabaseAfterScreen(Context context, final int screenIndex) {
        final ContentResolver cr = context.getContentResolver();
        Runnable r = new Runnable() {
            public void run() {
                Iterator it = LauncherModel.sWorkspaceItems.iterator();
                while (it.hasNext()) {
                    ItemInfo item = (ItemInfo) it.next();
                    if (item.container == -100 && item.screen > screenIndex) {
                        item.screen--;
                        ContentValues values = new ContentValues();
                        long itemId = item.id;
                        item.onAddToDatabase(values);
                        item.updateValuesWithCoordinates(values, item.cellX, item.cellY);
                        cr.update(Favorites.getContentUri(itemId, false), values, null, null);
                        ItemInfo modelItem = (ItemInfo) LauncherModel.sItemsIdMap.get(Long.valueOf(itemId));
                        if (item != modelItem) {
                            throw new RuntimeException("item: " + (item != null ? item.toString() : "null") + "modelItem: " + (modelItem != null ? modelItem.toString() : "null") + "Error: ItemInfo passed to " + "updateItemsInDatabaseAfterScreen" + " doesn't match original");
                        }
                    }
                }
            }
        };
        if (sWorkerThread.getThreadId() == Process.myTid()) {
            r.run();
        } else {
            sWorker.post(r);
        }
    }

    static boolean shortcutExists(Context context, String title, Intent intent) {
        Cursor c = context.getContentResolver().query(Favorites.CONTENT_URI, new String[]{BaseLauncherColumns.TITLE, BaseLauncherColumns.INTENT}, "title=? and intent=?", new String[]{title, intent.toUri(0)}, null);
        try {
            return c.moveToFirst();
        } finally {
            c.close();
        }
    }

    static boolean shortcutExistsByIntent(Context context, Intent intent) {
        Cursor c = context.getContentResolver().query(Favorites.CONTENT_URI, new String[]{BaseLauncherColumns.INTENT}, "intent=?", new String[]{intent.toUri(0)}, null);
        try {
            return c.moveToFirst();
        } finally {
            c.close();
        }
    }

    static ArrayList<ItemInfo> getItemsInLocalCoordinates(Context context) {
        ArrayList<ItemInfo> items = new ArrayList<>();
        Cursor c = context.getContentResolver().query(Favorites.CONTENT_URI, new String[]{BaseLauncherColumns.ITEM_TYPE, "container", "screen", "cellX", "cellY", "spanX", "spanY", "lock"}, null, null, null);
        int itemTypeIndex = c.getColumnIndexOrThrow(BaseLauncherColumns.ITEM_TYPE);
        int containerIndex = c.getColumnIndexOrThrow("container");
        int screenIndex = c.getColumnIndexOrThrow("screen");
        int cellXIndex = c.getColumnIndexOrThrow("cellX");
        int cellYIndex = c.getColumnIndexOrThrow("cellY");
        int spanXIndex = c.getColumnIndexOrThrow("spanX");
        int spanYIndex = c.getColumnIndexOrThrow("spanY");
        int lockIndex = c.getColumnIndexOrThrow("lock");
        while (c.moveToNext()) {
            try {
                ItemInfo item = new ItemInfo();
                item.cellX = c.getInt(cellXIndex);
                item.cellY = c.getInt(cellYIndex);
                item.spanX = c.getInt(spanXIndex);
                item.spanY = c.getInt(spanYIndex);
                item.container = (long) c.getInt(containerIndex);
                item.itemType = c.getInt(itemTypeIndex);
                item.screen = c.getInt(screenIndex);
                item.isLock = c.getInt(lockIndex) != 0;
                items.add(item);
            } catch (Exception e) {
                items.clear();
            } finally {
                c.close();
            }
        }
        return items;
    }

    static FolderInfo getFolderById(Context context, HashMap<Long, FolderInfo> folderList, long id) {
        Cursor c = context.getContentResolver().query(Favorites.CONTENT_URI, null, "_id=? and (itemType=? or itemType=?)", new String[]{String.valueOf(id), String.valueOf(2)}, null);
        try {
            if (c.moveToFirst()) {
                int itemTypeIndex = c.getColumnIndexOrThrow(BaseLauncherColumns.ITEM_TYPE);
                int titleIndex = c.getColumnIndexOrThrow(BaseLauncherColumns.TITLE);
                int containerIndex = c.getColumnIndexOrThrow("container");
                int screenIndex = c.getColumnIndexOrThrow("screen");
                int cellXIndex = c.getColumnIndexOrThrow("cellX");
                int cellYIndex = c.getColumnIndexOrThrow("cellY");
                int lockIndex = c.getColumnIndexOrThrow("lock");
                FolderInfo folderInfo = null;
                switch (c.getInt(itemTypeIndex)) {
                    case 2:
                        folderInfo = findOrMakeFolder(folderList, id);
                        break;
                }
                folderInfo.title = c.getString(titleIndex);
                folderInfo.id = id;
                folderInfo.container = (long) c.getInt(containerIndex);
                folderInfo.screen = c.getInt(screenIndex);
                folderInfo.cellX = c.getInt(cellXIndex);
                folderInfo.cellY = c.getInt(cellYIndex);
                folderInfo.isLock = c.getInt(lockIndex) != 0;
                return folderInfo;
            }
            c.close();
            return null;
        } finally {
            c.close();
        }
    }

    static void addItemToDatabase(Context context, final ItemInfo item, long container, int screen, int cellX, int cellY, final boolean notify) {
        if (!(item instanceof ShortcutInfo) || !shortcutExistsByIntent(context, ((ShortcutInfo) item).intent)) {
            item.container = container;
            item.cellX = cellX;
            item.cellY = cellY;
            if (!(context instanceof Launcher) || screen >= 0 || container != -101) {
                item.screen = screen;
            } else {
                item.screen = ((Launcher) context).getHotseat().getOrderInHotseat(cellX, cellY);
            }
            final ContentValues values = new ContentValues();
            final ContentResolver cr = context.getContentResolver();
            item.onAddToDatabase(values);
            item.id = ((LauncherApplication) context.getApplicationContext()).getLauncherProvider().generateNewId();
            values.put("_id", Long.valueOf(item.id));
            item.updateValuesWithCoordinates(values, item.cellX, item.cellY);
            Runnable r = new Runnable() {
                public void run() {
                    Uri uri;
                    ContentResolver contentResolver = cr;
                    if (notify) {
                        uri = Favorites.CONTENT_URI;
                    } else {
                        uri = Favorites.CONTENT_URI_NO_NOTIFICATION;
                    }
                    contentResolver.insert(uri, values);
                    if (LauncherModel.sItemsIdMap.containsKey(Long.valueOf(item.id))) {
                        throw new RuntimeException("Error: ItemInfo id (" + item.id + ") passed to " + "addItemToDatabase already exists." + item.toString());
                    }
                    LauncherModel.sItemsIdMap.put(Long.valueOf(item.id), item);
                    switch (item.itemType) {
                        case 0:
                        case 1:
                            break;
                        case 2:
                            LauncherModel.sFolders.put(Long.valueOf(item.id), (FolderInfo) item);
                            break;
                        case 4:
                            LauncherModel.sAppWidgets.add((LauncherAppWidgetInfo) item);
                            return;
                        default:
                            return;
                    }
                    if (item.container == -100 || item.container == -101) {
                        LauncherModel.sWorkspaceItems.add(item);
                    }
                }
            };
            if (sWorkerThread.getThreadId() == Process.myTid()) {
                r.run();
            } else {
                sWorker.post(r);
            }
        }
    }

    static int getCellLayoutChildId(long container, int screen, int localCellX, int localCellY, int spanX, int spanY) {
        return ((((int) container) & 255) << 24) | ((screen & 255) << 16) | ((localCellX & 255) << 8) | (localCellY & 255);
    }

    static int getCellCountX() {
        return mCellCountX;
    }

    static int getCellCountY() {
        return mCellCountY;
    }

    static void updateWorkspaceLayoutCells(int shortAxisCellCount, int longAxisCellCount) {
        mCellCountX = shortAxisCellCount;
        mCellCountY = longAxisCellCount;
    }

    static void updateWorkspaceNumberHomescreens(Context context, int number) {
        mNumberHomescreens = Math.max(number, mNumberHomescreens);
        Homescreen.setNumberHomescreens(context, number);
    }

    static void deleteItemFromDatabase(Context context, final ItemInfo item) {
        final ContentResolver cr = context.getContentResolver();
        final Uri uriToDelete = Favorites.getContentUri(item.id, false);
        Runnable r = new Runnable() {
            public void run() {
                cr.delete(uriToDelete, null, null);
                switch (item.itemType) {
                    case 0:
                    case 1:
                        LauncherModel.sWorkspaceItems.remove(item);
                        break;
                    case 2:
                        LauncherModel.sFolders.remove(Long.valueOf(item.id));
                        LauncherModel.sWorkspaceItems.remove(item);
                        break;
                    case 4:
                        LauncherModel.sAppWidgets.remove(item);
                        break;
                }
                LauncherModel.sItemsIdMap.remove(Long.valueOf(item.id));
                LauncherModel.sDbIconCache.remove(item);
            }
        };
        if (sWorkerThread.getThreadId() == Process.myTid()) {
            r.run();
        } else {
            sWorker.post(r);
        }
    }

    static void deleteFolderContentsFromDatabase(Context context, final FolderInfo info) {
        final ContentResolver cr = context.getContentResolver();
        Runnable r = new Runnable() {
            public void run() {
                cr.delete(Favorites.getContentUri(info.id, false), null, null);
                LauncherModel.sItemsIdMap.remove(Long.valueOf(info.id));
                LauncherModel.sFolders.remove(Long.valueOf(info.id));
                LauncherModel.sDbIconCache.remove(info);
                LauncherModel.sWorkspaceItems.remove(info);
                cr.delete(Favorites.CONTENT_URI_NO_NOTIFICATION, "container=" + info.id, null);
            }
        };
        Runnable readd = new Runnable(info.contents, (Launcher) context, info.screen) {
            /* access modifiers changed from: private */
            public ArrayList<ShortcutInfo> readdList = new ArrayList<>();
            private Runnable realWork;
            private final /* synthetic */ ArrayList val$contents;
            private final /* synthetic */ Launcher val$launcher;

            {
                this.val$contents = r2;
                this.val$launcher = r3;
                this.realWork = new Runnable() {
                    public void run() {
                        r3.addExternalItemsStartingFromPage(AnonymousClass14.this.readdList, r4, false);
                        Workspace ws = r3.getWorkspace();
                        if (ws != null) {
                            ws.requestFocus();
                        }
                    }
                };
            }

            public void run() {
                Iterator it = this.val$contents.iterator();
                while (it.hasNext()) {
                    ShortcutInfo info = (ShortcutInfo) it.next();
                    if (!LauncherModel.shortcutExistsByIntent(this.val$launcher, info.intent)) {
                        this.readdList.add(info);
                        info.container = -1;
                    } else {
                        Log.d(LauncherModel.TAG, "WTF? already back on: " + info);
                    }
                }
                this.val$launcher.runOnUiThread(this.realWork);
            }
        };
        if (sWorkerThread.getThreadId() == Process.myTid()) {
            r.run();
            readd.run();
            return;
        }
        sWorker.post(r);
        sWorker.post(readd);
    }

    public void initialize(Callbacks callbacks) {
        synchronized (this.mLock) {
            this.mCallbacks = new WeakReference<>(callbacks);
        }
    }

    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive intent=" + intent);
        String action = intent.getAction();
        if ("android.intent.action.PACKAGE_CHANGED".equals(action) || "android.intent.action.PACKAGE_REMOVED".equals(action) || "android.intent.action.PACKAGE_ADDED".equals(action)) {
            String packageName = intent.getData().getSchemeSpecificPart();
            boolean replacing = intent.getBooleanExtra("android.intent.extra.REPLACING", false);
            int op = 0;
            if (packageName != null && packageName.length() != 0) {
                if ("android.intent.action.PACKAGE_CHANGED".equals(action)) {
                    op = 2;
                } else if ("android.intent.action.PACKAGE_REMOVED".equals(action)) {
                    if (!replacing) {
                        op = 3;
                    }
                } else if ("android.intent.action.PACKAGE_ADDED".equals(action)) {
                    if (!replacing) {
                        op = 1;
                    } else {
                        op = 2;
                        AppInfoManager manager = AppInfoManager.getInstance();
                        if (manager != null && manager.isDefaultSystemApp(packageName)) {
                            forceReload();
                        }
                    }
                }
                if (op != 0) {
                    enqueuePackageUpdated(new PackageUpdatedTask(op, new String[]{packageName}));
                }
            }
        } else if ("android.intent.action.EXTERNAL_APPLICATIONS_AVAILABLE".equals(action)) {
            String[] packages = intent.getStringArrayExtra("android.intent.extra.changed_package_list");
            Log.d("czj-ios", "receive broadcast Intent.ACTION_EXTERNAL_APPLICATIONS_AVAILABLE");
            for (int i = 0; i < packages.length; i++) {
                Log.d("czj-ios", "changed_package:" + packages[i]);
            }
            enqueuePackageUpdated(new PackageUpdatedTask(1, packages));
        } else if ("android.intent.action.EXTERNAL_APPLICATIONS_UNAVAILABLE".equals(action)) {
            enqueuePackageUpdated(new PackageUpdatedTask(4, intent.getStringArrayExtra("android.intent.extra.changed_package_list")));
        } else if ("android.intent.action.LOCALE_CHANGED".equals(action)) {
            forceReload();
        } else if ("android.intent.action.CONFIGURATION_CHANGED".equals(action)) {
            Configuration currentConfig = context.getResources().getConfiguration();
            if (this.mPreviousConfigMcc != currentConfig.mcc) {
                Log.d(TAG, "Reload apps on config change. curr_mcc:" + currentConfig.mcc + " prevmcc:" + this.mPreviousConfigMcc);
                forceReload();
            }
            this.mPreviousConfigMcc = currentConfig.mcc;
        } else if (("android.search.action.GLOBAL_SEARCH_ACTIVITY_CHANGED".equals(action) || "android.search.action.SEARCHABLES_CHANGED".equals(action)) && this.mCallbacks != null) {
            Callbacks callbacks = (Callbacks) this.mCallbacks.get();
            if (callbacks != null) {
                callbacks.bindSearchablesChanged();
            }
        }
    }

    private void forceReload() {
        synchronized (this.mLock) {
            stopLoaderLocked();
            this.mAllAppsLoaded = false;
            this.mWorkspaceLoaded = false;
        }
        startLoaderFromBackground();
    }

    public void startLoaderFromBackground() {
        boolean runLoader = false;
        if (this.mCallbacks != null) {
            Callbacks callbacks = (Callbacks) this.mCallbacks.get();
            if (callbacks != null && !callbacks.setLoadOnResume()) {
                runLoader = true;
            }
        }
        if (runLoader) {
            startLoader(this.mApp, false);
        }
    }

    private boolean stopLoaderLocked() {
        boolean isLaunching = false;
        LoaderTask oldTask = this.mLoaderTask;
        if (oldTask != null) {
            if (oldTask.isLaunching()) {
                isLaunching = true;
            }
            oldTask.stopLocked();
        }
        return isLaunching;
    }

    public void startLoader(Context context, boolean isLaunching) {
        synchronized (this.mLock) {
            Log.d(TAG, "startLoader isLaunching=" + isLaunching);
            if (!(this.mCallbacks == null || this.mCallbacks.get() == null)) {
                this.mLoaderTask = new LoaderTask(context, isLaunching || stopLoaderLocked());
                sWorkerThread.setPriority(5);
                sWorker.post(this.mLoaderTask);
            }
        }
    }

    public void stopLoader() {
        synchronized (this.mLock) {
            if (this.mLoaderTask != null) {
                this.mLoaderTask.stopLocked();
            }
        }
    }

    public boolean isAllAppsLoaded() {
        return this.mAllAppsLoaded;
    }

    /* access modifiers changed from: private */
    public void findMissedOnWorkspace(Context context, ArrayList<ApplicationInfo> added) {
        Iterator it = added.iterator();
        while (it.hasNext()) {
            ApplicationInfo ai = (ApplicationInfo) it.next();
            if (!shortcutExistsByIntent(context, ai.intent)) {
                boolean toAdd = true;
                Iterator it2 = this.mMissedOnWorkspace.iterator();
                while (true) {
                    if (it2.hasNext()) {
                        ApplicationInfo missedAi = (ApplicationInfo) it2.next();
                        if (missedAi.componentName.getPackageName().equals(ai.componentName.getPackageName()) && missedAi.componentName.getClassName().equals(ai.componentName.getClassName())) {
                            toAdd = false;
                            break;
                        }
                    } else {
                        break;
                    }
                }
                if (toAdd) {
                    this.mMissedOnWorkspace.add(ai);
                }
            }
        }
        Log.d(TAG, "added " + added.size() + ", missed " + this.mMissedOnWorkspace.size());
    }

    /* access modifiers changed from: private */
    public void removeNotExisitingItems() {
        PackageManager pm = this.mApp.getPackageManager();
        this.mNotExistingItems.clear();
        for (ItemInfo info : sItemsIdMap.values()) {
            if (info instanceof ShortcutInfo) {
                String packageName = ((ShortcutInfo) info).intent.getComponent().getPackageName();
                ApplicationInfo ai = null;
                try {
                    ai = pm.getApplicationInfo(packageName, 0);
                } catch (NameNotFoundException e) {
                    Log.d(TAG, "removeNotExistingItems: name not found for " + packageName);
                }
                if (ai == null) {
                    this.mNotExistingItems.add((ShortcutInfo) info);
                }
            }
        }
        Log.d(TAG, "removeNotExistingItems: " + this.mNotExistingItems.size() + ", " + this.mNotExistingItems);
    }

    /* access modifiers changed from: 0000 */
    public void enqueuePackageUpdated(PackageUpdatedTask task) {
        sWorker.post(task);
    }

    public ShortcutInfo getShortcutInfo(PackageManager manager, Intent intent, Context context) {
        return getShortcutInfo(manager, intent, context, (Cursor) null, -1, -1, null);
    }

    public ShortcutInfo getShortcutInfo(PackageManager manager, Intent intent, Context context, Cursor c, int iconIndex, int titleIndex, HashMap<Object, CharSequence> labelCache) {
        Bitmap icon = null;
        ShortcutInfo info = new ShortcutInfo();
        ComponentName componentName = intent.getComponent();
        if (componentName == null) {
            return null;
        }
        try {
            if (!manager.getPackageInfo(componentName.getPackageName(), 0).applicationInfo.enabled) {
                return null;
            }
        } catch (NameNotFoundException e) {
            Log.d(TAG, "getPackInfo failed for package " + componentName.getPackageName());
        }
        ResolveInfo resolveInfo = manager.resolveActivity(intent, 0);
        if (resolveInfo != null) {
            icon = this.mIconCache.getIcon(componentName, resolveInfo, labelCache);
        }
        if (icon == null && c != null) {
            icon = getIconFromCursor(c, iconIndex, context);
        }
        if (icon == null) {
            icon = getFallbackIcon();
            info.usingFallbackIcon = true;
        }
        info.setIcon(icon);
        if (resolveInfo != null) {
            ComponentName key = getComponentNameFromResolveInfo(resolveInfo);
            if (labelCache == null || !labelCache.containsKey(key)) {
                info.title = resolveInfo.activityInfo.loadLabel(manager);
                if (labelCache != null) {
                    labelCache.put(key, info.title);
                }
            } else {
                info.title = (CharSequence) labelCache.get(key);
            }
        }
        if (info.title == null && c != null) {
            info.title = c.getString(titleIndex);
        }
        if (info.title == null) {
            info.title = componentName.getClassName();
        }
        info.itemType = 0;
        return info;
    }

    /* access modifiers changed from: private */
    public ShortcutInfo getShortcutInfo(Cursor c, Context context, int iconTypeIndex, int iconPackageIndex, int iconResourceIndex, int iconIndex, int titleIndex) {
        Bitmap icon = null;
        ShortcutInfo info = new ShortcutInfo();
        info.itemType = 1;
        info.title = c.getString(titleIndex);
        switch (c.getInt(iconTypeIndex)) {
            case 0:
                String packageName = c.getString(iconPackageIndex);
                String resourceName = c.getString(iconResourceIndex);
                PackageManager packageManager = context.getPackageManager();
                info.customIcon = false;
                try {
                    Resources resources = packageManager.getResourcesForApplication(packageName);
                    if (resources != null) {
                        icon = Utilities.createIconBitmap(this.mIconCache.getFullResIcon(resources, resources.getIdentifier(resourceName, null, null)), context);
                    }
                } catch (Exception e) {
                }
                if (icon == null) {
                    icon = getIconFromCursor(c, iconIndex, context);
                }
                if (icon == null) {
                    icon = getFallbackIcon();
                    info.usingFallbackIcon = true;
                    break;
                }
                break;
            case 1:
                icon = getIconFromCursor(c, iconIndex, context);
                if (icon != null) {
                    info.customIcon = true;
                    break;
                } else {
                    icon = getFallbackIcon();
                    info.customIcon = false;
                    info.usingFallbackIcon = true;
                    break;
                }
            default:
                icon = getFallbackIcon();
                info.usingFallbackIcon = true;
                info.customIcon = false;
                break;
        }
        info.setIcon(icon);
        return info;
    }

    /* access modifiers changed from: 0000 */
    public Bitmap getIconFromCursor(Cursor c, int iconIndex, Context context) {
        byte[] data = c.getBlob(iconIndex);
        try {
            return Utilities.createIconBitmap(BitmapFactory.decodeByteArray(data, 0, data.length), context);
        } catch (Exception e) {
            return null;
        }
    }

    /* access modifiers changed from: 0000 */
    public ShortcutInfo addShortcut(Context context, Intent data, long container, int screen, int cellX, int cellY, boolean notify) {
        ShortcutInfo info = infoFromShortcutIntent(context, data, null);
        if (info == null) {
            return null;
        }
        Log.d(TAG, "addShortcut call addItemToDatabase");
        addItemToDatabase(context, info, container, screen, cellX, cellY, notify);
        return info;
    }

    /* access modifiers changed from: 0000 */
    public AppWidgetProviderInfo findAppWidgetProviderInfoWithComponent(Context context, ComponentName component) {
        for (AppWidgetProviderInfo info : AppWidgetManager.getInstance(context).getInstalledProviders()) {
            if (info.provider.equals(component)) {
                return info;
            }
        }
        return null;
    }

    /* access modifiers changed from: 0000 */
    public List<WidgetMimeTypeHandlerData> resolveWidgetsForMimeType(Context context, String mimeType) {
        PackageManager packageManager = context.getPackageManager();
        List<WidgetMimeTypeHandlerData> supportedConfigurationActivities = new ArrayList<>();
        Intent supportsIntent = new Intent(InstallWidgetReceiver.ACTION_SUPPORTS_CLIPDATA_MIMETYPE);
        supportsIntent.setType(mimeType);
        List<AppWidgetProviderInfo> widgets = AppWidgetManager.getInstance(context).getInstalledProviders();
        HashMap<ComponentName, AppWidgetProviderInfo> configurationComponentToWidget = new HashMap<>();
        for (AppWidgetProviderInfo info : widgets) {
            configurationComponentToWidget.put(info.configure, info);
        }
        for (ResolveInfo info2 : packageManager.queryIntentActivities(supportsIntent, 65536)) {
            ActivityInfo activityInfo = info2.activityInfo;
            ComponentName infoComponent = new ComponentName(activityInfo.packageName, activityInfo.name);
            if (configurationComponentToWidget.containsKey(infoComponent)) {
                supportedConfigurationActivities.add(new WidgetMimeTypeHandlerData(info2, (AppWidgetProviderInfo) configurationComponentToWidget.get(infoComponent)));
            }
        }
        return supportedConfigurationActivities;
    }

    /* access modifiers changed from: 0000 */
    public ShortcutInfo infoFromShortcutIntent(Context context, Intent data, Bitmap fallbackIcon) {
        Intent intent = (Intent) data.getParcelableExtra("android.intent.extra.shortcut.INTENT");
        String name = data.getStringExtra("android.intent.extra.shortcut.NAME");
        Parcelable bitmap = data.getParcelableExtra("android.intent.extra.shortcut.ICON");
        if (intent == null) {
            Log.e(TAG, "Can't construct ShorcutInfo with null intent");
            return null;
        }
        Bitmap icon = null;
        boolean customIcon = false;
        ShortcutIconResource iconResource = null;
        if (bitmap == null || !(bitmap instanceof Bitmap)) {
            Parcelable extra = data.getParcelableExtra("android.intent.extra.shortcut.ICON_RESOURCE");
            if (extra != null && (extra instanceof ShortcutIconResource)) {
                try {
                    iconResource = (ShortcutIconResource) extra;
                    Resources resources = context.getPackageManager().getResourcesForApplication(iconResource.packageName);
                    icon = Utilities.createIconBitmap(this.mIconCache.getFullResIcon(resources, resources.getIdentifier(iconResource.resourceName, null, null)), context);
                } catch (Exception e) {
                    Log.w(TAG, "Could not load shortcut icon: " + extra);
                }
            }
        } else {
            icon = Utilities.createIconBitmap((Drawable) new FastBitmapDrawable((Bitmap) bitmap), context);
            customIcon = true;
        }
        ShortcutInfo info = new ShortcutInfo();
        if (icon == null) {
            if (fallbackIcon != null) {
                icon = fallbackIcon;
            } else {
                icon = getFallbackIcon();
                info.usingFallbackIcon = true;
            }
        }
        info.setIcon(icon);
        info.title = name;
        info.intent = intent;
        info.customIcon = customIcon;
        info.iconResource = iconResource;
        return info;
    }

    /* access modifiers changed from: 0000 */
    public boolean queueIconToBeChecked(HashMap<Object, byte[]> cache, ShortcutInfo info, Cursor c, int iconIndex) {
        if (!this.mAppsCanBeOnExternalStorage || info.customIcon || info.usingFallbackIcon) {
            return false;
        }
        cache.put(info, c.getBlob(iconIndex));
        return true;
    }

    /* access modifiers changed from: 0000 */
    public void updateSavedIcon(Context context, ShortcutInfo info, byte[] data) {
        boolean needSave;
        if (data != null) {
            try {
                needSave = !BitmapFactory.decodeByteArray(data, 0, data.length).sameAs(info.getIcon(this.mIconCache));
            } catch (Exception e) {
                needSave = true;
            }
        } else {
            needSave = true;
        }
        if (needSave) {
            Log.d(TAG, "going to save icon bitmap for info=" + info);
            updateItemInDatabase(context, info);
        }
    }

    /* access modifiers changed from: private */
    public static FolderInfo findOrMakeFolder(HashMap<Long, FolderInfo> folders, long id) {
        FolderInfo folderInfo = (FolderInfo) folders.get(Long.valueOf(id));
        if (folderInfo != null) {
            return folderInfo;
        }
        FolderInfo folderInfo2 = new FolderInfo();
        folders.put(Long.valueOf(id), folderInfo2);
        return folderInfo2;
    }

    static ComponentName getComponentNameFromResolveInfo(ResolveInfo info) {
        if (info.activityInfo != null) {
            return new ComponentName(info.activityInfo.packageName, info.activityInfo.name);
        }
        return new ComponentName(info.serviceInfo.packageName, info.serviceInfo.name);
    }

    public void dumpState() {
        Log.d(TAG, "mCallbacks=" + this.mCallbacks);
        ApplicationInfo.dumpApplicationInfoList(TAG, "mAllAppsList.data", this.mAllAppsList.data);
        ApplicationInfo.dumpApplicationInfoList(TAG, "mAllAppsList.added", this.mAllAppsList.added);
        ApplicationInfo.dumpApplicationInfoList(TAG, "mAllAppsList.removed", this.mAllAppsList.removed);
        ApplicationInfo.dumpApplicationInfoList(TAG, "mAllAppsList.modified", this.mAllAppsList.modified);
        if (this.mLoaderTask != null) {
            this.mLoaderTask.dumpState();
        } else {
            Log.d(TAG, "mLoaderTask=null");
        }
    }

    public AllAppsList getAllAppsList() {
        return this.mAllAppsList;
    }
}
