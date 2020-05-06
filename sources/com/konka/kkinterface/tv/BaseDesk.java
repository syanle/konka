package com.konka.kkinterface.tv;

import android.os.Handler;

public interface BaseDesk {
    Handler getHandler(int i);

    void releaseHandler(int i);

    boolean setHandler(Handler handler, int i);
}
