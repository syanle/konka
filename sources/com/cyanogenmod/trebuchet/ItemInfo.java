package com.cyanogenmod.trebuchet;

import android.content.ContentValues;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.util.Log;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

class ItemInfo {
    static final int NO_ID = -1;
    int cellX = -1;
    int cellY = -1;
    long container = -1;
    int[] dropPos = null;
    long id = -1;
    boolean isGesture = false;
    boolean isLock = false;
    int itemType;
    int screen = -1;
    int spanX = 1;
    int spanY = 1;

    ItemInfo() {
    }

    ItemInfo(ItemInfo info) {
        this.id = info.id;
        this.cellX = info.cellX;
        this.cellY = info.cellY;
        this.spanX = info.spanX;
        this.spanY = info.spanY;
        this.screen = info.screen;
        this.itemType = info.itemType;
        this.container = info.container;
        this.isLock = info.isLock;
    }

    /* access modifiers changed from: 0000 */
    public void onAddToDatabase(ContentValues values) {
        values.put(BaseLauncherColumns.ITEM_TYPE, Integer.valueOf(this.itemType));
        if (!this.isGesture) {
            values.put("container", Long.valueOf(this.container));
            values.put("screen", Integer.valueOf(this.screen));
            values.put("cellX", Integer.valueOf(this.cellX));
            values.put("cellY", Integer.valueOf(this.cellY));
            values.put("spanX", Integer.valueOf(this.spanX));
            values.put("spanY", Integer.valueOf(this.spanY));
            values.put("lock", Integer.valueOf(this.isLock ? 1 : 0));
        }
    }

    /* access modifiers changed from: 0000 */
    public void updateValuesWithCoordinates(ContentValues values, int cellX2, int cellY2) {
        values.put("cellX", Integer.valueOf(cellX2));
        values.put("cellY", Integer.valueOf(cellY2));
    }

    static byte[] flattenBitmap(Bitmap bitmap) {
        ByteArrayOutputStream out = new ByteArrayOutputStream(bitmap.getWidth() * bitmap.getHeight() * 4);
        try {
            bitmap.compress(CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
            return out.toByteArray();
        } catch (IOException e) {
            Log.w("Favorite", "Could not write icon");
            return null;
        }
    }

    static void writeBitmap(ContentValues values, Bitmap bitmap) {
        if (bitmap != null) {
            values.put(BaseLauncherColumns.ICON, flattenBitmap(bitmap));
        }
    }

    /* access modifiers changed from: 0000 */
    public void unbind() {
    }

    public String toString() {
        return "Item(id=" + this.id + " type=" + this.itemType + " container=" + this.container + " screen=" + this.screen + " cellX=" + this.cellX + " cellY=" + this.cellY + " spanX=" + this.spanX + " spanY=" + this.spanY + " isGesture=" + this.isGesture + " dropPos=" + this.dropPos + " isLock=" + this.isLock + ")";
    }
}
