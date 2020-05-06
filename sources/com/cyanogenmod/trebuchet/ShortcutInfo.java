package com.cyanogenmod.trebuchet;

import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Intent;
import android.content.Intent.ShortcutIconResource;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.util.Log;
import java.util.ArrayList;
import java.util.Iterator;

class ShortcutInfo extends ItemInfo {
    private static final String TAG = ShortcutInfo.class.getSimpleName();
    boolean customIcon;
    ShortcutIconResource iconResource;
    Intent intent;
    private Bitmap mIcon;
    CharSequence title;
    boolean usingFallbackIcon;

    ShortcutInfo() {
        this.itemType = 1;
    }

    public ShortcutInfo(ShortcutInfo info) {
        super(info);
        this.title = info.title.toString();
        this.intent = new Intent(info.intent);
        if (info.iconResource != null) {
            this.iconResource = new ShortcutIconResource();
            this.iconResource.packageName = info.iconResource.packageName;
            this.iconResource.resourceName = info.iconResource.resourceName;
        }
        this.mIcon = info.mIcon;
        this.customIcon = info.customIcon;
    }

    public ShortcutInfo(ApplicationInfo info) {
        super(info);
        this.title = info.title.toString();
        this.intent = new Intent(info.intent);
        this.customIcon = false;
    }

    public void setIcon(Bitmap b) {
        this.mIcon = b;
    }

    public Bitmap getIcon(IconCache iconCache) {
        if (this.mIcon == null) {
            this.mIcon = iconCache.getIcon(this.intent);
            this.usingFallbackIcon = iconCache.isDefaultIcon(this.mIcon);
        }
        return this.mIcon;
    }

    /* access modifiers changed from: 0000 */
    public final void setActivity(ComponentName className, int launchFlags) {
        this.intent = new Intent("android.intent.action.MAIN");
        this.intent.addCategory("android.intent.category.LAUNCHER");
        this.intent.setComponent(className);
        this.intent.setFlags(launchFlags);
        this.itemType = 0;
    }

    public static boolean isUninstallable(PackageManager pm, ShortcutInfo info) {
        if (info == null || info.intent == null) {
            return true;
        }
        String packageName = info.intent.getComponent().getPackageName();
        try {
            ApplicationInfo ai = pm.getApplicationInfo(packageName, 0);
            if (ai == null) {
                return true;
            }
            int appFlags = ai.flags;
            if ((appFlags & 1) == 0) {
                if ((appFlags & 128) != 0) {
                }
                return true;
            }
            return false;
        } catch (NameNotFoundException e) {
            Log.d(TAG, "PackageManager.getApplicationInfo failed for " + packageName);
        }
    }

    /* access modifiers changed from: 0000 */
    public void onAddToDatabase(ContentValues values) {
        String titleStr;
        String uri;
        super.onAddToDatabase(values);
        if (this.title != null) {
            titleStr = this.title.toString();
        } else {
            titleStr = null;
        }
        values.put(BaseLauncherColumns.TITLE, titleStr);
        if (this.intent != null) {
            uri = this.intent.toUri(0);
        } else {
            uri = null;
        }
        values.put(BaseLauncherColumns.INTENT, uri);
        if (this.customIcon) {
            values.put(BaseLauncherColumns.ICON_TYPE, Integer.valueOf(1));
            writeBitmap(values, this.mIcon);
            return;
        }
        if (!this.usingFallbackIcon) {
            writeBitmap(values, this.mIcon);
        }
        values.put(BaseLauncherColumns.ICON_TYPE, Integer.valueOf(0));
        if (this.iconResource != null) {
            values.put(BaseLauncherColumns.ICON_PACKAGE, this.iconResource.packageName);
            values.put(BaseLauncherColumns.ICON_RESOURCE, this.iconResource.resourceName);
        }
    }

    public boolean checkShortcutPackageName(String name) {
        ComponentName componentName = this.intent.getComponent();
        if (componentName != null) {
            String packageName = componentName.getPackageName();
            if (packageName != null && packageName.equals(name)) {
                return true;
            }
        }
        return false;
    }

    public String toString() {
        return "ShortcutInfo(title=" + this.title.toString() + ")";
    }

    public static void dumpShortcutInfoList(String tag, String label, ArrayList<ShortcutInfo> list) {
        Log.d(tag, new StringBuilder(String.valueOf(label)).append(" size=").append(list.size()).toString());
        Iterator it = list.iterator();
        while (it.hasNext()) {
            ShortcutInfo info = (ShortcutInfo) it.next();
            Log.d(tag, "   title=\"" + info.title + " icon=" + info.mIcon + " customIcon=" + info.customIcon);
        }
    }
}
