package com.cyanogenmod.trebuchet;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.util.Log;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

class ApplicationInfo extends ItemInfo {
    static final int DEFAULT_SORT_ORDER_ID = 99;
    static final int DOWNLOADED_FLAG = 1;
    private static final String TAG = "Launcher.ApplicationInfo";
    static final int UPDATED_SYSTEM_APP_FLAG = 2;
    ComponentName componentName;
    long firstInstallTime;
    int flags;
    Bitmap iconBitmap;
    Intent intent;
    int sortOrderID;
    CharSequence title;

    ApplicationInfo() {
        this.sortOrderID = DEFAULT_SORT_ORDER_ID;
        this.flags = 0;
        this.itemType = 1;
    }

    public ApplicationInfo(PackageManager pm, ResolveInfo info, IconCache iconCache, HashMap<Object, CharSequence> labelCache) {
        this.sortOrderID = DEFAULT_SORT_ORDER_ID;
        this.flags = 0;
        String packageName = info.activityInfo.applicationInfo.packageName;
        this.componentName = new ComponentName(packageName, info.activityInfo.name);
        this.container = -1;
        setActivity(this.componentName, 270532608);
        try {
            int appFlags = pm.getApplicationInfo(packageName, 0).flags;
            if ((appFlags & 1) == 0) {
                this.flags |= 1;
                if ((appFlags & 128) != 0) {
                    this.flags |= 2;
                }
            }
            this.firstInstallTime = pm.getPackageInfo(packageName, 0).firstInstallTime;
        } catch (NameNotFoundException e) {
            Log.d(TAG, "PackageManager.getApplicationInfo failed for " + packageName);
        }
        iconCache.getTitleAndIcon(this, info, labelCache);
    }

    public ApplicationInfo(ApplicationInfo info) {
        super(info);
        this.sortOrderID = DEFAULT_SORT_ORDER_ID;
        this.flags = 0;
        this.componentName = info.componentName;
        this.title = info.title.toString();
        this.intent = new Intent(info.intent);
        this.flags = info.flags;
        this.firstInstallTime = info.firstInstallTime;
    }

    /* access modifiers changed from: 0000 */
    public final void setActivity(ComponentName className, int launchFlags) {
        this.intent = new Intent("android.intent.action.MAIN");
        this.intent.addCategory("android.intent.category.LAUNCHER");
        this.intent.setComponent(className);
        this.intent.setFlags(launchFlags);
        this.itemType = 0;
    }

    public String toString() {
        return "ApplicationInfo(title=" + this.title.toString() + ")";
    }

    public static void dumpApplicationInfoList(String tag, String label, ArrayList<ApplicationInfo> list) {
        Log.d(tag, new StringBuilder(String.valueOf(label)).append(" size=").append(list.size()).toString());
        Iterator it = list.iterator();
        while (it.hasNext()) {
            ApplicationInfo info = (ApplicationInfo) it.next();
            Log.d(tag, "   title=\"" + info.title + "\" iconBitmap=" + info.iconBitmap + " firstInstallTime=" + info.firstInstallTime);
        }
    }

    public ShortcutInfo makeShortcut() {
        return new ShortcutInfo(this);
    }

    public boolean equals(Object obj) {
        if (obj != null) {
            try {
                return ((ApplicationInfo) obj).componentName.equals(this.componentName);
            } catch (ClassCastException e) {
            }
        }
        return false;
    }

    public int hashCode() {
        return this.componentName.hashCode();
    }
}
