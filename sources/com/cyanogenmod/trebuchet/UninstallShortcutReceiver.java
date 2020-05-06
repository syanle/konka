package com.cyanogenmod.trebuchet;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.widget.Toast;
import com.konka.ios7launcher.R;
import java.net.URISyntaxException;

public class UninstallShortcutReceiver extends BroadcastReceiver {
    private static final String ACTION_UNINSTALL_SHORTCUT = "com.android.launcher.action.UNINSTALL_SHORTCUT";

    public void onReceive(Context context, Intent data) {
        if (ACTION_UNINSTALL_SHORTCUT.equals(data.getAction())) {
            Intent intent = (Intent) data.getParcelableExtra("android.intent.extra.shortcut.INTENT");
            String name = data.getStringExtra("android.intent.extra.shortcut.NAME");
            boolean duplicate = data.getBooleanExtra("duplicate", true);
            if (intent != null && name != null) {
                ContentResolver cr = context.getContentResolver();
                Cursor c = cr.query(Favorites.CONTENT_URI, new String[]{"_id", BaseLauncherColumns.INTENT}, "title=?", new String[]{name}, null);
                int intentIndex = c.getColumnIndexOrThrow(BaseLauncherColumns.INTENT);
                int idIndex = c.getColumnIndexOrThrow("_id");
                boolean changed = false;
                while (c.moveToNext()) {
                    try {
                        try {
                            if (intent.filterEquals(Intent.parseUri(c.getString(intentIndex), 0))) {
                                cr.delete(Favorites.getContentUri(c.getLong(idIndex), false), null, null);
                                changed = true;
                                if (!duplicate) {
                                    break;
                                }
                            } else {
                                continue;
                            }
                        } catch (URISyntaxException e) {
                        }
                    } finally {
                        c.close();
                    }
                }
                if (changed) {
                    cr.notifyChange(Favorites.CONTENT_URI, null);
                    Toast.makeText(context, context.getString(R.string.shortcut_uninstalled, new Object[]{name}), 0).show();
                }
            }
        }
    }
}
