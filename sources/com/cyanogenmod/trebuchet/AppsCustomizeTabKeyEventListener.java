package com.cyanogenmod.trebuchet;

import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;

/* compiled from: FocusHelper */
class AppsCustomizeTabKeyEventListener implements OnKeyListener {
    AppsCustomizeTabKeyEventListener() {
    }

    public boolean onKey(View v, int keyCode, KeyEvent event) {
        return FocusHelper.handleAppsCustomizeTabKeyEvent(v, keyCode, event);
    }
}
