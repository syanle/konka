package com.cyanogenmod.trebuchet;

public interface ScrollIndicator {
    void cancelAnimations();

    void hide(boolean z, int i);

    void init(PagedView pagedView);

    boolean isElasticScrollIndicator();

    void show(boolean z, int i);

    void update();
}
