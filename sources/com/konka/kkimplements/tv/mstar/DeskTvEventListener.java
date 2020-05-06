package com.konka.kkimplements.tv.mstar;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import com.konka.kkinterface.tv.CommonDesk.EnumDeskEvent;
import com.mstar.android.tvapi.common.listener.OnTvEventListener;

public class DeskTvEventListener implements OnTvEventListener {
    private static DeskTvEventListener tvEventListener = null;
    private final int SYNC_TIME_ANDROID;
    private Handler m_handler;

    public DeskTvEventListener() {
        this.m_handler = null;
        this.SYNC_TIME_ANDROID = 1929;
        this.m_handler = null;
    }

    public void attachHandler(Handler handler) {
        this.m_handler = handler;
    }

    public void releaseHandler() {
        this.m_handler = null;
    }

    static DeskTvEventListener getInstance() {
        if (tvEventListener == null) {
            tvEventListener = new DeskTvEventListener();
        }
        return tvEventListener;
    }

    public boolean onSignalUnlock(int what) {
        Log.d("TvApp", "onSignalUnLock in DeskTvEventListener");
        if (this.m_handler != null) {
            Message msg = this.m_handler.obtainMessage();
            msg.what = EnumDeskEvent.EV_SIGNAL_UNLOCK.ordinal();
            Bundle b = new Bundle();
            b.putString("MsgSource", "DeskTvEventListener");
            msg.setData(b);
            this.m_handler.sendMessage(msg);
        }
        return false;
    }

    public boolean onSignalLock(int what) {
        Log.d("TvApp", "onSignalLock in DeskTvEventListener");
        if (this.m_handler != null) {
            Message msg = this.m_handler.obtainMessage();
            msg.what = EnumDeskEvent.EV_SIGNAL_LOCK.ordinal();
            Bundle b = new Bundle();
            b.putString("MsgSource", "DeskTvEventListener");
            msg.setData(b);
            this.m_handler.sendMessage(msg);
        }
        return false;
    }

    public boolean onDtvReadyPopupDialog(int what, int arg1, int arg2) {
        return false;
    }

    public boolean onScartMuteOsdMode(int what) {
        return false;
    }

    public boolean onUnityEvent(int what, int arg1, int arg2) {
        if (arg1 == 1929 && this.m_handler != null) {
            Message msg = this.m_handler.obtainMessage();
            msg.what = 1929;
            msg.arg2 = arg2;
            this.m_handler.sendMessage(msg);
        }
        return false;
    }

    public boolean onAtscPopupDialog(int arg0, int arg1, int arg2) {
        return false;
    }

    public boolean onDeadthEvent(int arg0, int arg1, int arg2) {
        return false;
    }

    public boolean onScreenSaverMode(int arg0, int arg1, int arg2) {
        return false;
    }
}
