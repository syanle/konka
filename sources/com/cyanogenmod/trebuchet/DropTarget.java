package com.cyanogenmod.trebuchet;

import android.graphics.Rect;

public interface DropTarget {

    public static class DragObject {
        public boolean cancelled = false;
        public boolean dragComplete = false;
        public Object dragInfo = null;
        public DragSource dragSource = null;
        public DragView dragView = null;
        public boolean isInTouchMode = true;
        public Runnable postAnimationRunnable = null;
        public int x = -1;
        public int xOffset = -1;
        public int y = -1;
        public int yOffset = -1;
    }

    boolean acceptDrop(DragObject dragObject);

    DropTarget getDropTargetDelegate(DragObject dragObject);

    void getHitRect(Rect rect);

    int getLeft();

    void getLocationInDragLayer(int[] iArr);

    int getTop();

    boolean isDropEnabled();

    void onDragEnter(DragObject dragObject);

    void onDragExit(DragObject dragObject);

    void onDragOver(DragObject dragObject);

    void onDrop(DragObject dragObject);
}
