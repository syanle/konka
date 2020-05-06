package com.cyanogenmod.trebuchet;

import android.appwidget.AppWidgetProviderInfo;
import android.os.Parcelable;

/* compiled from: PendingAddItemInfo */
class PendingAddWidgetInfo extends PendingAddItemInfo {
    Parcelable configurationData;
    int icon;
    String mimeType;
    int minHeight;
    int minWidth;
    int previewImage;

    public PendingAddWidgetInfo(AppWidgetProviderInfo i, String dataMimeType, Parcelable data) {
        this.itemType = 4;
        this.componentName = i.provider;
        this.minWidth = i.minWidth;
        this.minHeight = i.minHeight;
        this.previewImage = i.previewImage;
        this.icon = i.icon;
        if (dataMimeType != null && data != null) {
            this.mimeType = dataMimeType;
            this.configurationData = data;
        }
    }
}
