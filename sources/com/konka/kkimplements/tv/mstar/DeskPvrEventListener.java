package com.konka.kkimplements.tv.mstar;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import com.mstar.android.tvapi.common.PvrManager;
import com.mstar.android.tvapi.common.PvrManager.OnPvrEventListener;

public class DeskPvrEventListener implements OnPvrEventListener {
    private Handler m_handler;

    public DeskPvrEventListener() {
        this.m_handler = null;
        this.m_handler = null;
    }

    public void attachHandler(Handler handler) {
        this.m_handler = handler;
    }

    public void releaseHandler() {
        this.m_handler = null;
    }

    public boolean onPvrNotifyUsbInserted(PvrManager mgr, int what, int arg1, int arg2) {
        if (this.m_handler != null) {
            Bundle b = new Bundle();
            Message msg = this.m_handler.obtainMessage();
            msg.what = what;
            b.putInt("usbInserted", arg2);
            msg.setData(b);
            this.m_handler.sendMessage(msg);
        }
        return false;
    }

    public boolean onPvrNotifyUsbRemoved(PvrManager mgr, int what, int arg1, int arg2) {
        if (this.m_handler != null) {
            Bundle b = new Bundle();
            Message msg = this.m_handler.obtainMessage();
            msg.what = what;
            b.putInt("usbInserted", arg2);
            msg.setData(b);
            this.m_handler.sendMessage(msg);
        }
        return false;
    }

    public boolean onPvrNotifyFormatFinished(PvrManager mgr, int what, int arg1, int arg2) {
        if (this.m_handler != null) {
            Bundle b = new Bundle();
            Message msg = this.m_handler.obtainMessage();
            msg.what = what;
            b.putInt("usbInserted", arg2);
            msg.setData(b);
            this.m_handler.sendMessage(msg);
        }
        return false;
    }
}
