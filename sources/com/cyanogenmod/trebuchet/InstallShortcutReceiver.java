package com.cyanogenmod.trebuchet;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import java.lang.reflect.Array;
import java.util.Iterator;

public class InstallShortcutReceiver extends BroadcastReceiver {
    public static final String ACTION_INSTALL_SHORTCUT = "com.android.launcher.action.INSTALL_SHORTCUT";
    public static final String SHORTCUT_MIMETYPE = "com.android.launcher/shortcut";
    private static final String TAG = InstallShortcutReceiver.class.getSimpleName();
    private final int[] mCoordinates = new int[2];

    private enum INSTALL_RESULT {
        SUCCESS,
        FAILED,
        DULPLICATE,
        OUT_OF_SPACE
    }

    public void onReceive(Context context, Intent data) {
        if (ACTION_INSTALL_SHORTCUT.equals(data.getAction())) {
            Log.d(TAG, "InstallShortcutReceiver: " + data.getStringExtra("android.intent.extra.shortcut.NAME"));
        }
    }

    private INSTALL_RESULT installShortcut(Context context, Intent data, int screen) {
        INSTALL_RESULT result = INSTALL_RESULT.FAILED;
        String name = data.getStringExtra("android.intent.extra.shortcut.NAME");
        if (!findEmptyCell(context, this.mCoordinates, screen)) {
            return INSTALL_RESULT.OUT_OF_SPACE;
        }
        Intent intent = (Intent) data.getParcelableExtra("android.intent.extra.shortcut.INTENT");
        if (intent == null) {
            return result;
        }
        if (intent.getAction() == null) {
            intent.setAction("android.intent.action.VIEW");
        }
        if (data.getBooleanExtra("duplicate", true) || LauncherModel.shortcutExists(context, name, intent)) {
            return INSTALL_RESULT.DULPLICATE;
        }
        if (((LauncherApplication) context.getApplicationContext()).getModel().addShortcut(context, data, -100, screen, this.mCoordinates[0], this.mCoordinates[1], true) != null) {
            return INSTALL_RESULT.SUCCESS;
        }
        return INSTALL_RESULT.FAILED;
    }

    private static boolean findEmptyCell(Context context, int[] xy, int screen) {
        int xCount = LauncherModel.getCellCountX();
        int yCount = LauncherModel.getCellCountY();
        boolean[][] occupied = (boolean[][]) Array.newInstance(Boolean.TYPE, new int[]{xCount, yCount});
        Iterator it = LauncherModel.getItemsInLocalCoordinates(context).iterator();
        while (it.hasNext()) {
            ItemInfo item = (ItemInfo) it.next();
            if (item.container == -100 && item.screen == screen) {
                int cellX = item.cellX;
                int cellY = item.cellY;
                int spanX = item.spanX;
                int spanY = item.spanY;
                int x = cellX;
                while (x < cellX + spanX && x < xCount) {
                    int y = cellY;
                    while (y < cellY + spanY && y < yCount) {
                        occupied[x][y] = true;
                        y++;
                    }
                    x++;
                }
            }
        }
        return CellLayout.findVacantCell(xy, 1, 1, xCount, yCount, occupied);
    }
}
