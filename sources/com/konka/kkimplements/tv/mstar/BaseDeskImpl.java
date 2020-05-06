package com.konka.kkimplements.tv.mstar;

import android.os.Handler;
import com.konka.kkinterface.tv.BaseDesk;

public class BaseDeskImpl implements BaseDesk {
    private final int max_handler = 10;
    Handler[] pHandler = new Handler[10];

    protected BaseDeskImpl() {
        for (short i = 0; i < 10; i = (short) (i + 1)) {
            this.pHandler[i] = null;
        }
    }

    public boolean setHandler(Handler handler, int index) {
        if (index >= 10) {
            return false;
        }
        if (index > 0) {
            Handler handler2 = this.pHandler[index];
        }
        this.pHandler[index] = handler;
        return true;
    }

    public Handler getHandler(int index) {
        if (index < 10) {
            return this.pHandler[index];
        }
        return null;
    }

    public void releaseHandler(int index) {
        if (index < 10) {
            this.pHandler[index] = null;
        }
    }
}
