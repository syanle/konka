package com.konka.kkimplements.tv.mstar;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import com.konka.kkinterface.tv.CommonDesk.EnumDeskEvent;
import com.mstar.android.tvapi.common.vo.TvOsType.EnumInputSource;
import com.mstar.android.tvapi.dtv.listener.OnDtvPlayerEventListener;
import com.mstar.android.tvapi.dtv.vo.DtvEventScan;

public class DeskDtvPlayerEventListener implements OnDtvPlayerEventListener {
    private static DeskDtvPlayerEventListener dtvEventListener = null;
    private Handler m_handler;

    public DeskDtvPlayerEventListener() {
        this.m_handler = null;
        this.m_handler = null;
    }

    public void attachHandler(Handler handler) {
        this.m_handler = handler;
    }

    public void releaseHandler() {
        this.m_handler = null;
    }

    static DeskDtvPlayerEventListener getInstance() {
        if (dtvEventListener == null) {
            dtvEventListener = new DeskDtvPlayerEventListener();
        }
        return dtvEventListener;
    }

    public boolean onDtvChannelNameReady(int what) {
        Log.d("TvApp", "onDtvChannelNameReady");
        if (this.m_handler == null) {
            return false;
        }
        Bundle b = new Bundle();
        Message msg = this.m_handler.obtainMessage();
        msg.what = 65;
        b.putInt("CmdIndex", EnumDeskEvent.EV_DTV_CHANNELNAME_READY.ordinal());
        this.m_handler.sendMessage(msg);
        return true;
    }

    public boolean onDtvAutoTuningScanInfo(int what, DtvEventScan extra) {
        Log.d("TvApp", "onDtvAutoTuningScanInfo");
        if (this.m_handler == null) {
            return false;
        }
        Bundle b = new Bundle();
        Message msg = this.m_handler.obtainMessage();
        msg.what = EnumInputSource.E_INPUT_SOURCE_DTV.ordinal();
        DtvEventScan dtv_scan_info = extra;
        b.putInt("dtvSrvCount", dtv_scan_info.dtvSrvCount);
        b.putInt("radioSrvCount", dtv_scan_info.radioSrvCount);
        b.putInt("dataSrvCount", dtv_scan_info.dataSrvCount);
        b.putInt("percent", dtv_scan_info.scanPercentageNum);
        b.putInt("quality", dtv_scan_info.signalQuality);
        b.putInt("strength", dtv_scan_info.signalStrength);
        b.putInt("scanstatus", dtv_scan_info.scanStatus);
        b.putInt("curFre", dtv_scan_info.currFrequency);
        msg.setData(b);
        this.m_handler.sendMessage(msg);
        Log.d("TvApp", "scanstatus" + dtv_scan_info.scanStatus);
        Log.d("TvApp", "dtvSrvCount" + dtv_scan_info.dtvSrvCount);
        Log.d("TvApp", "quality" + dtv_scan_info.signalQuality);
        Log.d("TvApp", "strength" + dtv_scan_info.signalStrength);
        return true;
    }

    public boolean onDtvProgramInfoReady(int what) {
        Log.d("TvApp", "onDtvProgramInfoReady");
        if (this.m_handler == null) {
            return false;
        }
        Bundle b = new Bundle();
        Message msg = this.m_handler.obtainMessage();
        msg.what = 65;
        b.putInt("CmdIndex", EnumDeskEvent.EV_DTV_PROGRAM_INFO_READY.ordinal());
        msg.setData(b);
        this.m_handler.sendMessage(msg);
        return true;
    }

    public boolean onCiLoadCredentialFail(int what) {
        Log.d("TvApp", "onCiLoadCredentialFail in DeskDtvPlayerEventListener");
        if (this.m_handler == null) {
            return false;
        }
        Message msg = this.m_handler.obtainMessage();
        msg.what = what;
        this.m_handler.sendMessage(msg);
        return true;
    }

    public boolean onEpgTimerSimulcast(int what, int arg1) {
        Log.d("TvApp", "onEpgTimerSimulcast in DeskDtvPlayerEventListener");
        if (this.m_handler == null) {
            return false;
        }
        Message msg = this.m_handler.obtainMessage();
        msg.what = what;
        Bundle b = new Bundle();
        b.putInt("EpgTimerSimulcast", arg1);
        msg.setData(b);
        this.m_handler.sendMessage(msg);
        return true;
    }

    public boolean onHbbtvStatusMode(int what, boolean arg1) {
        Log.d("TvApp", "onHbbtvStatusMode in DeskDtvPlayerEventListener");
        if (this.m_handler == null) {
            return false;
        }
        Message msg = this.m_handler.obtainMessage();
        msg.what = what;
        Bundle b = new Bundle();
        b.putBoolean("HbbtvStatusMode", arg1);
        msg.setData(b);
        this.m_handler.sendMessage(msg);
        return true;
    }

    public boolean onMheg5StatusMode(int what, int arg1) {
        Log.d("TvApp", "onMheg5StatusMode in DeskDtvPlayerEventListener");
        if (this.m_handler == null) {
            return false;
        }
        Bundle b = new Bundle();
        Message msg = this.m_handler.obtainMessage();
        msg.what = EnumDeskEvent.EV_MHEG5_STATUS_MODE.ordinal();
        b.putInt("Mheg5StatusMode", arg1);
        Log.d("onMheg5StatusMode", Integer.toString(arg1));
        msg.setData(b);
        this.m_handler.sendMessage(msg);
        return true;
    }

    public boolean onMheg5ReturnKey(int what, int arg1) {
        Log.d("TvApp", "onMheg5ReturnKey in DeskDtvPlayerEventListener");
        if (this.m_handler == null) {
            return false;
        }
        Message msg = this.m_handler.obtainMessage();
        msg.what = what;
        Bundle b = new Bundle();
        b.putInt("Mheg5ReturnKey", arg1);
        msg.setData(b);
        this.m_handler.sendMessage(msg);
        return true;
    }

    public boolean onOadHandler(int what, int arg1, int arg2) {
        Log.d("TvApp", "onOadHandler in DeskDtvPlayerEventListener");
        if (this.m_handler == null) {
            return false;
        }
        Message msg = this.m_handler.obtainMessage();
        msg.what = what;
        Bundle b = new Bundle();
        b.putInt("OadHandler", arg1);
        msg.setData(b);
        this.m_handler.sendMessage(msg);
        return true;
    }

    public boolean onOadDownload(int what, int arg1) {
        Log.d("TvApp", "onOadDownload in DeskDtvPlayerEventListener");
        if (this.m_handler == null) {
            return false;
        }
        Message msg = this.m_handler.obtainMessage();
        msg.what = what;
        Bundle b = new Bundle();
        b.putInt("OadDownload", arg1);
        msg.setData(b);
        this.m_handler.sendMessage(msg);
        return true;
    }

    public boolean onDtvAutoUpdateScan(int what) {
        Log.d("TvApp", "onDtvAutoUpdateScan in DeskDtvPlayerEventListener");
        if (this.m_handler == null) {
            return false;
        }
        Message msg = this.m_handler.obtainMessage();
        msg.what = what;
        this.m_handler.sendMessage(msg);
        return true;
    }

    public boolean onTsChange(int what) {
        Log.d("TvApp", "onTsChange in DeskDtvPlayerEventListener");
        if (this.m_handler == null) {
            return false;
        }
        Message msg = this.m_handler.obtainMessage();
        msg.what = what;
        this.m_handler.sendMessage(msg);
        return true;
    }

    public boolean onPopupScanDialogLossSignal(int what) {
        Log.d("TvApp", "onPopupScanDialogLossSignal in DeskDtvPlayerEventListener");
        if (this.m_handler == null) {
            return false;
        }
        Message msg = this.m_handler.obtainMessage();
        msg.what = what;
        this.m_handler.sendMessage(msg);
        return true;
    }

    public boolean onPopupScanDialogNewMultiplex(int what) {
        Log.d("TvApp", "onPopupScanDialogNewMultiplex in DeskDtvPlayerEventListener");
        if (this.m_handler == null) {
            return false;
        }
        Message msg = this.m_handler.obtainMessage();
        msg.what = what;
        this.m_handler.sendMessage(msg);
        return true;
    }

    public boolean onPopupScanDialogFrequencyChange(int what) {
        Log.d("TvApp", "onPopupScanDialogFrequencyChange in DeskDtvPlayerEventListener");
        if (this.m_handler == null) {
            return false;
        }
        Message msg = this.m_handler.obtainMessage();
        msg.what = what;
        this.m_handler.sendMessage(msg);
        return true;
    }

    public boolean onRctPresence(int what) {
        Log.d("TvApp", "onRctPresence in DeskDtvPlayerEventListener");
        if (this.m_handler == null) {
            return false;
        }
        Message msg = this.m_handler.obtainMessage();
        msg.what = what;
        this.m_handler.sendMessage(msg);
        return true;
    }

    public boolean onChangeTtxStatus(int what, boolean arg1) {
        Log.d("TvApp", "onChangeTtxStatus in DeskDtvPlayerEventListener");
        if (this.m_handler == null) {
            return false;
        }
        Message msg = this.m_handler.obtainMessage();
        msg.what = what;
        Bundle b = new Bundle();
        b.putBoolean("ChangeTtxStatus", arg1);
        msg.setData(b);
        this.m_handler.sendMessage(msg);
        return true;
    }

    public boolean onDtvPriComponentMissing(int what) {
        Log.d("TvApp", "onDtvPriComponentMissing in DeskDtvPlayerEventListener");
        if (this.m_handler == null) {
            return false;
        }
        Message msg = this.m_handler.obtainMessage();
        msg.what = what;
        this.m_handler.sendMessage(msg);
        return true;
    }

    public boolean onAudioModeChange(int what, boolean arg1) {
        Log.d("TvApp", "onAudioModeChange in DeskDtvPlayerEventListener");
        if (this.m_handler == null) {
            return false;
        }
        Message msg = this.m_handler.obtainMessage();
        msg.what = what;
        Bundle b = new Bundle();
        b.putBoolean("AudioModeChange", arg1);
        msg.setData(b);
        this.m_handler.sendMessage(msg);
        return true;
    }

    public boolean onMheg5EventHandler(int what, int arg1) {
        Log.d("TvApp", "onMheg5EventHandler in DeskDtvPlayerEventListener");
        if (this.m_handler == null) {
            return false;
        }
        Message msg = this.m_handler.obtainMessage();
        msg.what = what;
        Bundle b = new Bundle();
        b.putInt("Mheg5EventHandler", arg1);
        msg.setData(b);
        this.m_handler.sendMessage(msg);
        return true;
    }

    public boolean onOadTimeout(int what, int arg1) {
        Log.d("TvApp", "onOadTimeout in DeskDtvPlayerEventListener");
        if (this.m_handler == null) {
            return false;
        }
        Message msg = this.m_handler.obtainMessage();
        msg.what = what;
        Bundle b = new Bundle();
        b.putInt("OadTimeout", arg1);
        msg.setData(b);
        this.m_handler.sendMessage(msg);
        return true;
    }

    public boolean onGingaStatusMode(int what, boolean arg1) {
        Log.d("TvApp", "onGingaStatusMode in DeskDtvPlayerEventListener");
        if (this.m_handler == null) {
            return false;
        }
        Message msg = this.m_handler.obtainMessage();
        msg.what = what;
        Bundle b = new Bundle();
        b.putBoolean("GingaStatusMode", arg1);
        msg.setData(b);
        this.m_handler.sendMessage(msg);
        return true;
    }

    public boolean onSignalUnLock(int what) {
        Log.d("TvApp", "onSignalUnLock in DeskDtvPlayerEventListener");
        if (this.m_handler == null) {
            return false;
        }
        Message msg = this.m_handler.obtainMessage();
        msg.what = EnumDeskEvent.EV_SIGNAL_UNLOCK.ordinal();
        Bundle b = new Bundle();
        b.putString("MsgSource", "DeskDtvPlayerEventListener");
        msg.setData(b);
        this.m_handler.sendMessage(msg);
        return true;
    }

    public boolean onSignalLock(int what) {
        Log.d("TvApp", "onSignalLock in DeskDtvPlayerEventListener");
        if (this.m_handler == null) {
            return false;
        }
        Message msg = this.m_handler.obtainMessage();
        msg.what = EnumDeskEvent.EV_SIGNAL_LOCK.ordinal();
        Bundle b = new Bundle();
        b.putString("MsgSource", "DeskDtvPlayerEventListener");
        msg.setData(b);
        this.m_handler.sendMessage(msg);
        return true;
    }
}
