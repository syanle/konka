package com.cyanogenmod.trebuchet;

import android.view.View;
import java.util.ArrayList;

public interface AppsCustomizeView {

    public enum ContentType {
        Apps,
        Widgets
    }

    public enum SortMode {
        KKDefault,
        Title,
        InstallDate
    }

    void addApps(ArrayList<ApplicationInfo> arrayList);

    void clearAllWidgetPreviews();

    void dumpState();

    ContentType getContentType();

    View getCurSelectView();

    int getSaveInstanceStateIndex();

    SortMode getSortMode();

    void hideIndicator(boolean z);

    boolean isContentType(ContentType contentType);

    void loadContent();

    void loadContent(boolean z);

    void onPackagesUpdated();

    void onTabChanged(ContentType contentType);

    void removeApps(ArrayList<ApplicationInfo> arrayList);

    void reset();

    void restore(int i);

    void setApps(ArrayList<ApplicationInfo> arrayList);

    void setContentType(ContentType contentType);

    void setCurrentToApps();

    void setCurrentToWidgets();

    void setSortMode(SortMode sortMode);

    void setup(Launcher launcher, DragController dragController);

    void showAllAppsCling();

    void showIndicator(boolean z);

    void surrender();

    void updateApps(ArrayList<ApplicationInfo> arrayList);
}
