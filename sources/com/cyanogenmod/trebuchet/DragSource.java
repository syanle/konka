package com.cyanogenmod.trebuchet;

import android.view.View;
import com.cyanogenmod.trebuchet.DropTarget.DragObject;

public interface DragSource {
    void onDropCompleted(View view, DragObject dragObject, boolean z);
}
