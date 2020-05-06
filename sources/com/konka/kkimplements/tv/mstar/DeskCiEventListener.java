package com.konka.kkimplements.tv.mstar;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import com.mstar.android.tvapi.dtv.common.CiManager;
import com.mstar.android.tvapi.dtv.common.CiManager.OnCiEventListener;

public class DeskCiEventListener implements OnCiEventListener {
    private static DeskCiEventListener ciEventListener = null;
    private Handler m_handler;

    public enum EVENT {
        EV_CIMMI_UI_DATA_READY,
        EV_CIMMI_UI_CLOSEMMI,
        EV_CIMMI_UI_CARD_INSERTED,
        EV_CIMMI_UI_CARD_REMOVED,
        EV_CIMMI_UI_AUTOTEST_MESSAGE_SHOWN
    }

    public DeskCiEventListener() {
        this.m_handler = null;
        this.m_handler = null;
    }

    public void attachHandler(Handler handler) {
        this.m_handler = handler;
    }

    public void releaseHandler() {
        this.m_handler = null;
    }

    public static DeskCiEventListener getInstance() {
        if (ciEventListener == null) {
            ciEventListener = new DeskCiEventListener();
        }
        return ciEventListener;
    }

    public boolean onUiDataReady(CiManager mgr, int what) {
        if (this.m_handler != null) {
            Bundle b = new Bundle();
            Message msg = this.m_handler.obtainMessage();
            msg.what = EVENT.EV_CIMMI_UI_DATA_READY.ordinal();
            msg.setData(b);
            this.m_handler.sendMessage(msg);
        }
        return false;
    }

    public boolean onUiCloseMmi(CiManager mgr, int what) {
        if (this.m_handler != null) {
            Bundle b = new Bundle();
            Message msg = this.m_handler.obtainMessage();
            msg.what = EVENT.EV_CIMMI_UI_CLOSEMMI.ordinal();
            msg.setData(b);
            this.m_handler.sendMessage(msg);
        }
        return false;
    }

    public boolean onUiCardInserted(CiManager mgr, int what) {
        if (this.m_handler != null) {
            Bundle b = new Bundle();
            Message msg = this.m_handler.obtainMessage();
            msg.what = EVENT.EV_CIMMI_UI_CARD_INSERTED.ordinal();
            msg.setData(b);
            this.m_handler.sendMessage(msg);
        }
        return false;
    }

    public boolean onUiCardRemoved(CiManager mgr, int what) {
        if (this.m_handler != null) {
            Bundle b = new Bundle();
            Message msg = this.m_handler.obtainMessage();
            msg.what = EVENT.EV_CIMMI_UI_CARD_REMOVED.ordinal();
            msg.setData(b);
            this.m_handler.sendMessage(msg);
        }
        return false;
    }

    public boolean onUiAutotestMessageShown(CiManager mgr, int what) {
        if (this.m_handler != null) {
            Bundle b = new Bundle();
            Message msg = this.m_handler.obtainMessage();
            msg.what = EVENT.EV_CIMMI_UI_AUTOTEST_MESSAGE_SHOWN.ordinal();
            msg.setData(b);
            this.m_handler.sendMessage(msg);
        }
        return false;
    }
}
