package com.konka.kkimplements.tv.mstar;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import com.konka.kkinterface.tv.CommonDesk.EnumDeskEvent;
import com.mstar.android.tvapi.common.listener.OnTvPlayerEventListener;
import com.mstar.android.tvapi.common.vo.HbbtvEventInfo;

public class DeskTvPlayerEventListener implements OnTvPlayerEventListener {
    private static DeskTvPlayerEventListener tvEventListener;
    private Handler m_handler;

    public DeskTvPlayerEventListener() {
        this.m_handler = null;
        this.m_handler = null;
    }

    public void attachHandler(Handler handler) {
        this.m_handler = handler;
    }

    public void releaseHandler() {
        this.m_handler = null;
    }

    static DeskTvPlayerEventListener getInstance() {
        if (tvEventListener == null) {
            tvEventListener = new DeskTvPlayerEventListener();
        }
        return tvEventListener;
    }

    public boolean onScreenSaverMode(int what, int arg1) {
        if (this.m_handler != null) {
            if (what != EnumDeskEvent.EV_SCREEN_SAVER_MODE.ordinal()) {
                Log.e("TvApp", "!!!!onScreenSaverMode msg match error");
            }
            Message msg = this.m_handler.obtainMessage();
            msg.what = EnumDeskEvent.EV_SCREEN_SAVER_MODE.ordinal();
            Bundle b = new Bundle();
            b.putInt("Status", arg1);
            msg.setData(b);
            this.m_handler.sendMessage(msg);
        }
        return false;
    }

    public boolean onSignalUnLock(int what) {
        if (this.m_handler != null) {
            if (what != EnumDeskEvent.EV_SIGNAL_UNLOCK.ordinal()) {
                Log.e("TvApp", "!!!!onSignalUnLock msg match error");
            }
            Message msg = this.m_handler.obtainMessage();
            msg.what = EnumDeskEvent.EV_SIGNAL_UNLOCK.ordinal();
            Bundle b = new Bundle();
            b.putString("MsgSource", "DeskTvPlayerEventListener");
            msg.setData(b);
            this.m_handler.sendMessage(msg);
        }
        return false;
    }

    public boolean onSignalLock(int what) {
        if (this.m_handler != null) {
            if (what != EnumDeskEvent.EV_SIGNAL_LOCK.ordinal()) {
                Log.e("TvApp", "!!!!onSignalLock msg match error");
            }
            Message msg = this.m_handler.obtainMessage();
            msg.what = EnumDeskEvent.EV_SIGNAL_LOCK.ordinal();
            Bundle b = new Bundle();
            b.putString("MsgSource", "DeskTvPlayerEventListener");
            msg.setData(b);
            this.m_handler.sendMessage(msg);
        }
        return false;
    }

    public boolean onEpgUpdateList(int arg0, int arg1) {
        return false;
    }

    public boolean onHbbtvUiEvent(int arg0, HbbtvEventInfo arg1) {
        return false;
    }

    public boolean onPopupDialog(int arg0, int arg1, int arg2) {
        return false;
    }

    public boolean onPvrNotifyAlwaysTimeShiftProgramNotReady(int arg0) {
        return false;
    }

    public boolean onPvrNotifyAlwaysTimeShiftProgramReady(int arg0) {
        return false;
    }

    public boolean onPvrNotifyCiPlusProtection(int arg0) {
        return false;
    }

    public boolean onPvrNotifyCiPlusRetentionLimitUpdate(int arg0, int arg1) {
        return false;
    }

    public boolean onPvrNotifyOverRun(int arg0) {
        Log.d("TvApp", "onPvrOverRun in DeskTvPlayerEventListener");
        if (this.m_handler == null) {
            return false;
        }
        if (arg0 != EnumDeskEvent.EV_PVR_NOTIFY_OVER_RUN.ordinal()) {
            Log.e("TvApp", "!!!!onPvrOverRun msg match error");
            return false;
        }
        Message msg = this.m_handler.obtainMessage();
        msg.what = EnumDeskEvent.EV_PVR_NOTIFY_OVER_RUN.ordinal();
        Bundle b = new Bundle();
        b.putString("MsgSource", "DeskTvPlayerEventListener");
        msg.setData(b);
        this.m_handler.sendMessage(msg);
        return true;
    }

    public boolean onPvrNotifyParentalControl(int arg0, int arg1) {
        return false;
    }

    public boolean onPvrNotifyPlaybackBegin(int arg0) {
        return false;
    }

    public boolean onPvrNotifyPlaybackSpeedChange(int arg0) {
        return false;
    }

    public boolean onPvrNotifyPlaybackStop(int arg0) {
        return false;
    }

    public boolean onPvrNotifyPlaybackTime(int arg0, int arg1) {
        return false;
    }

    public boolean onPvrNotifyRecordSize(int arg0, int arg1) {
        return false;
    }

    public boolean onPvrNotifyRecordStop(int arg0) {
        return false;
    }

    public boolean onPvrNotifyRecordTime(int arg0, int arg1) {
        return false;
    }

    public boolean onPvrNotifyTimeShiftOverwritesAfter(int arg0, int arg1) {
        return false;
    }

    public boolean onPvrNotifyTimeShiftOverwritesBefore(int arg0, int arg1) {
        return false;
    }

    public boolean onPvrNotifyUsbRemoved(int arg0, int arg1) {
        return false;
    }

    public boolean onTvProgramInfoReady(int arg0) {
        return false;
    }
}
