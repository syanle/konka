package com.cyanogenmod.trebuchet;

import android.appwidget.AppWidgetHost;
import android.appwidget.AppWidgetHostView;
import android.appwidget.AppWidgetProviderInfo;
import android.content.Context;

public class LauncherAppWidgetHost extends AppWidgetHost {
    public LauncherAppWidgetHost(Context context, int hostId) {
        super(context, hostId);
    }

    /* access modifiers changed from: protected */
    public AppWidgetHostView onCreateView(Context context, int appWidgetId, AppWidgetProviderInfo appWidget) {
        return new LauncherAppWidgetHostView(context, appWidget);
    }

    public void stopListening() {
        super.stopListening();
        clearViews();
    }
}
