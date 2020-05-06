package com.konka.kkimplements.tv.mstar;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import com.konka.kkinterface.tv.CommonDesk.EnumDeskEvent;
import com.mstar.android.tvapi.atv.listener.OnAtvPlayerEventListener;
import com.mstar.android.tvapi.atv.vo.AtvEventScan;
import com.mstar.android.tvapi.common.vo.TvOsType.EnumInputSource;

public class DeskAtvPlayerEventListener implements OnAtvPlayerEventListener {
    private static DeskAtvPlayerEventListener atvEventListener = null;
    private Handler m_handler;

    public DeskAtvPlayerEventListener() {
        this.m_handler = null;
        this.m_handler = null;
    }

    public void attachHandler(Handler handler) {
        this.m_handler = handler;
    }

    public void releaseHandler() {
        this.m_handler = null;
    }

    static DeskAtvPlayerEventListener getInstance() {
        if (atvEventListener == null) {
            atvEventListener = new DeskAtvPlayerEventListener();
        }
        return atvEventListener;
    }

    public boolean onAtvAutoTuningScanInfo(int what, AtvEventScan extra) {
        if (this.m_handler == null) {
            return false;
        }
        Bundle b = new Bundle();
        Message msg = this.m_handler.obtainMessage();
        msg.what = EnumInputSource.E_INPUT_SOURCE_ATV.ordinal();
        b.putInt("percent", extra.percent);
        b.putInt("frequency", extra.frequencyKHz);
        b.putInt("scanNum", extra.scannedChannelNum);
        msg.setData(b);
        this.m_handler.sendMessage(msg);
        return true;
    }

    public boolean onAtvManualTuningScanInfo(int what, AtvEventScan extra) {
        if (this.m_handler == null) {
            return false;
        }
        Bundle b = new Bundle();
        Message msg = this.m_handler.obtainMessage();
        msg.what = EnumInputSource.E_INPUT_SOURCE_ATV.ordinal();
        b.putInt("percent", extra.percent);
        b.putInt("frequency", extra.frequencyKHz);
        b.putInt("scanNum", extra.scannedChannelNum);
        msg.setData(b);
        this.m_handler.sendMessage(msg);
        return true;
    }

    public boolean onSignalUnLock(int what) {
        Log.d("TvApp", "onSignalUnLock in DeskAtvPlayerEventListener");
        if (this.m_handler != null) {
            Message msg = this.m_handler.obtainMessage();
            msg.what = EnumDeskEvent.EV_SIGNAL_UNLOCK.ordinal();
            Bundle b = new Bundle();
            b.putString("MsgSource", "DeskAtvPlayerEventListener");
            msg.setData(b);
            this.m_handler.sendMessage(msg);
        }
        return false;
    }

    public boolean onSignalLock(int what) {
        Log.d("TvApp", "onSignalLock in DeskAtvPlayerEventListener");
        if (this.m_handler != null) {
            Message msg = this.m_handler.obtainMessage();
            msg.what = EnumDeskEvent.EV_SIGNAL_LOCK.ordinal();
            Bundle b = new Bundle();
            b.putString("MsgSource", "DeskAtvPlayerEventListener");
            msg.setData(b);
            this.m_handler.sendMessage(msg);
        }
        return false;
    }

    public boolean onAtvProgramInfoReady(int what) {
        Log.e("TvApp", "onAtvProgramInfoReady");
        if (this.m_handler == null) {
            return false;
        }
        Bundle b = new Bundle();
        Message msg = this.m_handler.obtainMessage();
        msg.what = 65;
        b.putInt("CmdIndex", EnumDeskEvent.EV_ATV_PROGRAM_INFO_READY.ordinal());
        this.m_handler.sendMessage(msg);
        return true;
    }
}
