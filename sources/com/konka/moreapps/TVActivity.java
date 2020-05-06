package com.konka.moreapps;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;

public class TVActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!startTv()) {
            sendBroadcast(new Intent("com.konka.GO_TO_TV"));
        }
        finish();
    }

    private boolean startTv() {
        ComponentName cn = new ComponentName("com.konka.tvsettings", "com.konka.tvsettings.RootActivity");
        Intent intent = new Intent("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.LAUNCHER");
        intent.setComponent(cn);
        intent.setFlags(270532608);
        try {
            startActivity(intent);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
