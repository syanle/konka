package com.cyanogenmod.trebuchet;

import android.content.ContentValues;
import java.util.ArrayList;
import java.util.Iterator;

class FolderInfo extends ItemInfo {
    ArrayList<ShortcutInfo> contents;
    ArrayList<FolderListener> listeners;
    boolean opened;
    CharSequence title;

    interface FolderListener {
        void onAdd(ShortcutInfo shortcutInfo);

        void onItemsChanged();

        void onRemove(ShortcutInfo shortcutInfo);

        void onTitleChanged(CharSequence charSequence);
    }

    FolderInfo() {
        this.contents = new ArrayList<>();
        this.listeners = new ArrayList<>();
        this.itemType = 2;
    }

    public void add(ShortcutInfo item) {
        this.contents.add(item);
        Iterator it = this.listeners.iterator();
        while (it.hasNext()) {
            ((FolderListener) it.next()).onAdd(item);
        }
        itemsChanged();
    }

    public void remove(ShortcutInfo item) {
        this.contents.remove(item);
        Iterator it = this.listeners.iterator();
        while (it.hasNext()) {
            ((FolderListener) it.next()).onRemove(item);
        }
        itemsChanged();
    }

    public void setTitle(CharSequence title2) {
        this.title = title2;
        Iterator it = this.listeners.iterator();
        while (it.hasNext()) {
            ((FolderListener) it.next()).onTitleChanged(title2);
        }
    }

    /* access modifiers changed from: 0000 */
    public void onAddToDatabase(ContentValues values) {
        super.onAddToDatabase(values);
        values.put(BaseLauncherColumns.TITLE, this.title.toString());
    }

    /* access modifiers changed from: 0000 */
    public void addListener(FolderListener listener) {
        this.listeners.add(listener);
    }

    /* access modifiers changed from: 0000 */
    public void removeListener(FolderListener listener) {
        if (this.listeners.contains(listener)) {
            this.listeners.remove(listener);
        }
    }

    /* access modifiers changed from: 0000 */
    public void itemsChanged() {
        Iterator it = this.listeners.iterator();
        while (it.hasNext()) {
            ((FolderListener) it.next()).onItemsChanged();
        }
    }

    /* access modifiers changed from: 0000 */
    public void unbind() {
        super.unbind();
        this.listeners.clear();
    }
}
