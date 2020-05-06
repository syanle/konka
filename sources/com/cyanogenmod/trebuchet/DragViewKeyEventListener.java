package com.cyanogenmod.trebuchet;

import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;

/* compiled from: FocusHelper */
class DragViewKeyEventListener implements OnKeyListener {
    DragViewKeyEventListener() {
    }

    public boolean onKey(View v, int keyCode, KeyEvent event) {
        return FocusHelper.handleDragViewKeyEvent(v, keyCode, event);
    }
}
