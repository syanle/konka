package com.cyanogenmod.trebuchet;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import java.util.HashMap;

public class IconCache {
    private static final int INITIAL_ICON_CACHE_CAPACITY = 50;
    private static final String TAG = "Launcher.IconCache";
    private final HashMap<ComponentName, CacheEntry> mCache = new HashMap<>(INITIAL_ICON_CACHE_CAPACITY);
    private final LauncherApplication mContext;
    private final Bitmap mDefaultIcon;
    private int mIconDpi;
    private final PackageManager mPackageManager;

    private static class CacheEntry {
        public Bitmap icon;
        public String title;

        private CacheEntry() {
        }

        /* synthetic */ CacheEntry(CacheEntry cacheEntry) {
            this();
        }
    }

    public IconCache(LauncherApplication context) {
        this.mContext = context;
        this.mPackageManager = context.getPackageManager();
        int density = context.getResources().getDisplayMetrics().densityDpi;
        if (!LauncherApplication.isScreenLarge()) {
            this.mIconDpi = context.getResources().getDisplayMetrics().densityDpi;
        } else if (density == 120) {
            this.mIconDpi = 160;
        } else if (density == 160) {
            this.mIconDpi = 240;
        } else if (density == 240) {
            this.mIconDpi = 320;
        } else if (density == 320) {
            this.mIconDpi = 320;
        } else {
            this.mIconDpi = 240;
        }
        this.mDefaultIcon = makeDefaultIcon();
    }

    public Drawable getFullResDefaultActivityIcon() {
        return getFullResIcon(Resources.getSystem(), 17629184);
    }

    public Drawable getFullResIcon(Resources resources, int iconId) {
        Drawable d;
        try {
            d = resources.getDrawableForDensity(iconId, this.mIconDpi);
        } catch (NotFoundException e) {
            d = null;
        }
        return d != null ? d : getFullResDefaultActivityIcon();
    }

    public Drawable getFullResIcon(String packageName, int iconId) {
        Resources resources;
        try {
            resources = this.mPackageManager.getResourcesForApplication(packageName);
        } catch (NameNotFoundException e) {
            resources = null;
        }
        if (resources == null || iconId == 0) {
            return getFullResDefaultActivityIcon();
        }
        return getFullResIcon(resources, iconId);
    }

    public Drawable getFullResIcon(ResolveInfo info) {
        return info.activityInfo.loadIcon(this.mPackageManager);
    }

    private Bitmap makeDefaultIcon() {
        Drawable d = getFullResDefaultActivityIcon();
        Bitmap b = Bitmap.createBitmap(Math.max(d.getIntrinsicWidth(), 1), Math.max(d.getIntrinsicHeight(), 1), Config.ARGB_8888);
        Canvas c = new Canvas(b);
        d.setBounds(0, 0, b.getWidth(), b.getHeight());
        d.draw(c);
        c.setBitmap(null);
        return b;
    }

    public void remove(ComponentName componentName) {
        synchronized (this.mCache) {
            this.mCache.remove(componentName);
        }
    }

    public void flush() {
        synchronized (this.mCache) {
            this.mCache.clear();
        }
    }

    public void getTitleAndIcon(ApplicationInfo application, ResolveInfo info, HashMap<Object, CharSequence> labelCache) {
        synchronized (this.mCache) {
            CacheEntry entry = cacheLocked(application.componentName, info, labelCache);
            application.title = entry.title;
            application.iconBitmap = entry.icon;
        }
    }

    public Bitmap getIcon(Intent intent) {
        Bitmap bitmap;
        synchronized (this.mCache) {
            ResolveInfo resolveInfo = this.mPackageManager.resolveActivity(intent, 0);
            ComponentName component = intent.getComponent();
            if (resolveInfo == null || component == null) {
                bitmap = this.mDefaultIcon;
            } else {
                bitmap = cacheLocked(component, resolveInfo, null).icon;
            }
        }
        return bitmap;
    }

    public Bitmap getIcon(ComponentName component, ResolveInfo resolveInfo, HashMap<Object, CharSequence> labelCache) {
        synchronized (this.mCache) {
            if (resolveInfo == null || component == null) {
                return null;
            }
            Bitmap bitmap = cacheLocked(component, resolveInfo, labelCache).icon;
            return bitmap;
        }
    }

    public boolean isDefaultIcon(Bitmap icon) {
        return this.mDefaultIcon == icon;
    }

    private CacheEntry cacheLocked(ComponentName componentName, ResolveInfo info, HashMap<Object, CharSequence> labelCache) {
        CacheEntry entry = (CacheEntry) this.mCache.get(componentName);
        if (entry == null) {
            entry = new CacheEntry(null);
            this.mCache.put(componentName, entry);
            ComponentName key = LauncherModel.getComponentNameFromResolveInfo(info);
            if (labelCache == null || !labelCache.containsKey(key)) {
                entry.title = info.loadLabel(this.mPackageManager).toString();
                if (labelCache != null) {
                    labelCache.put(key, entry.title);
                }
            } else {
                entry.title = ((CharSequence) labelCache.get(key)).toString();
            }
            if (entry.title == null) {
                entry.title = info.activityInfo.name;
            }
            entry.icon = Utilities.createIconBitmap(getFullResIcon(info), (Context) this.mContext);
        }
        return entry;
    }

    public HashMap<ComponentName, Bitmap> getAllIcons() {
        HashMap<ComponentName, Bitmap> set;
        synchronized (this.mCache) {
            set = new HashMap<>();
            for (ComponentName cn : this.mCache.keySet()) {
                set.put(cn, ((CacheEntry) this.mCache.get(cn)).icon);
            }
        }
        return set;
    }
}
