package com.cyanogenmod.trebuchet;

import android.appwidget.AppWidgetHostView;
import android.content.ComponentName;
import android.content.ContentValues;

class LauncherAppWidgetInfo extends ItemInfo {
    static final int NO_ID = -1;
    int appWidgetId;
    AppWidgetHostView hostView;
    int minHeight;
    int minWidth;
    ComponentName providerName;

    LauncherAppWidgetInfo(ComponentName providerName2) {
        this.appWidgetId = -1;
        this.minWidth = -1;
        this.minHeight = -1;
        this.hostView = null;
        this.itemType = 4;
        this.providerName = providerName2;
        this.spanX = -1;
        this.spanY = -1;
    }

    LauncherAppWidgetInfo(int appWidgetId2) {
        this.appWidgetId = -1;
        this.minWidth = -1;
        this.minHeight = -1;
        this.hostView = null;
        this.itemType = 4;
        this.appWidgetId = appWidgetId2;
    }

    /* access modifiers changed from: 0000 */
    public void onAddToDatabase(ContentValues values) {
        super.onAddToDatabase(values);
        values.put("appWidgetId", Integer.valueOf(this.appWidgetId));
    }

    public String toString() {
        return "AppWidget(id=" + Integer.toString(this.appWidgetId) + ")";
    }

    /* access modifiers changed from: 0000 */
    public void unbind() {
        super.unbind();
        this.hostView = null;
    }
}
