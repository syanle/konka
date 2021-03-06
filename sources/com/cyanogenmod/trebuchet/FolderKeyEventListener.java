package com.cyanogenmod.trebuchet;

import android.content.Context;
import android.media.AudioManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;

/* compiled from: FocusHelper */
class FolderKeyEventListener implements OnKeyListener {
    private Context mContext;

    public FolderKeyEventListener(Context context) {
        this.mContext = context;
    }

    public boolean onKey(View v, int keyCode, KeyEvent event) {
        boolean wasHandled = FocusHelper.handleFolderKeyEvent(v, keyCode, event);
        if (wasHandled && event.getAction() != 1) {
            new AudioManager(this.mContext).playSoundEffect(0);
        }
        return wasHandled;
    }
}
