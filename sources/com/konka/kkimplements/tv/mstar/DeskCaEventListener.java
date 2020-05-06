package com.konka.kkimplements.tv.mstar;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import com.mstar.android.tvapi.dtv.common.CaManager;
import com.mstar.android.tvapi.dtv.common.CaManager.OnCaEventListener;
import com.mstar.android.tvapi.dtv.vo.CaLockService;
import com.mstar.android.tvapi.dtv.vo.CaStartIPPVBuyDlgInfo;

public class DeskCaEventListener implements OnCaEventListener {
    private static DeskCaEventListener caEventListener = null;
    private Handler m_handler;

    public enum CA_EVENT {
        EV_CA_START_IPPV_BUY_DLG,
        EV_CA_HIDE_IPPV_DLG,
        EV_CA_EMAIL_NOTIFY_ICON,
        EV_CA_SHOW_OSD_MESSAGE,
        EV_CA_HIDE_OSD_MESSAGE,
        EV_CA_REQUEST_FEEDING,
        EV_CA_SHOW_BUY_MESSAGE,
        EV_CA_SHOW_FINGER_MESSAGE,
        EV_CA_SHOW_PROGRESS_STRIP,
        EV_CA_ACTION_REQUEST,
        EV_CA_ENTITLE_CHANGED,
        EV_CA_DETITLE_RECEVIED,
        EV_CA_LOCKSERVICE,
        EV_CA_UNLOCKSERVICE,
        EV_CA_OTASTATE
    }

    public DeskCaEventListener() {
        this.m_handler = null;
        this.m_handler = null;
    }

    public void attachHandler(Handler handler) {
        this.m_handler = handler;
    }

    public void releaseHandler() {
        this.m_handler = null;
    }

    public static DeskCaEventListener getInstance() {
        if (caEventListener == null) {
            caEventListener = new DeskCaEventListener();
        }
        return caEventListener;
    }

    public boolean onStartIppvBuyDlg(CaManager mgr, int what, int arg1, int arg2, CaStartIPPVBuyDlgInfo arg3) {
        if (this.m_handler != null) {
            Bundle b = new Bundle();
            Message msg = this.m_handler.obtainMessage();
            msg.what = CA_EVENT.EV_CA_START_IPPV_BUY_DLG.ordinal();
            b.putInt("MessageType", arg1);
            b.putInt("MessageType2", arg2);
            msg.setData(b);
            msg.obj = arg3;
            this.m_handler.sendMessage(msg);
        }
        return false;
    }

    public boolean onHideIPPVDlg(CaManager mgr, int what, int arg1, int arg2) {
        if (this.m_handler != null) {
            Bundle b = new Bundle();
            Message msg = this.m_handler.obtainMessage();
            msg.what = CA_EVENT.EV_CA_HIDE_IPPV_DLG.ordinal();
            b.putInt("MessageType", arg1);
            b.putInt("MessageType2", arg2);
            msg.setData(b);
            this.m_handler.sendMessage(msg);
        }
        return false;
    }

    public boolean onEmailNotifyIcon(CaManager mgr, int what, int arg1, int arg2) {
        if (this.m_handler != null) {
            Bundle b = new Bundle();
            Message msg = this.m_handler.obtainMessage();
            msg.what = CA_EVENT.EV_CA_EMAIL_NOTIFY_ICON.ordinal();
            b.putInt("MessageType", arg1);
            b.putInt("MessageType2", arg2);
            msg.setData(b);
            this.m_handler.sendMessage(msg);
        }
        return false;
    }

    public boolean onShowOSDMessage(CaManager mgr, int what, int arg1, int arg2, String arg3) {
        if (this.m_handler != null) {
            Bundle b = new Bundle();
            Message msg = this.m_handler.obtainMessage();
            msg.what = CA_EVENT.EV_CA_SHOW_OSD_MESSAGE.ordinal();
            b.putInt("MessageType", arg1);
            b.putInt("MessageType2", arg2);
            b.putString("StringType", arg3);
            System.out.print("onShowOSDMessage:" + arg3);
            msg.setData(b);
            this.m_handler.sendMessage(msg);
        }
        return false;
    }

    public boolean onHideOSDMessage(CaManager mgr, int what, int arg1, int arg2) {
        if (this.m_handler != null) {
            Bundle b = new Bundle();
            Message msg = this.m_handler.obtainMessage();
            msg.what = CA_EVENT.EV_CA_HIDE_OSD_MESSAGE.ordinal();
            b.putInt("MessageType", arg1);
            b.putInt("MessageType2", arg2);
            msg.setData(b);
            this.m_handler.sendMessage(msg);
        }
        return false;
    }

    public boolean onRequestFeeding(CaManager mgr, int what, int arg1, int arg2) {
        if (this.m_handler != null) {
            Bundle b = new Bundle();
            Message msg = this.m_handler.obtainMessage();
            msg.what = CA_EVENT.EV_CA_REQUEST_FEEDING.ordinal();
            b.putInt("MessageType", arg1);
            b.putInt("MessageType2", arg2);
            msg.setData(b);
            this.m_handler.sendMessage(msg);
        }
        return false;
    }

    public boolean onShowBuyMessage(CaManager mgr, int what, int arg1, int arg2) {
        if (this.m_handler != null) {
            Log.i("DeskCaEventListener", "//////////////////////////////////EV_CA_SHOW_BUY_MESSAGE/////////////////////////////////////////////////");
            Bundle b = new Bundle();
            Message msg = this.m_handler.obtainMessage();
            msg.what = CA_EVENT.EV_CA_SHOW_BUY_MESSAGE.ordinal();
            b.putInt("MessageType", arg2);
            b.putInt("MessageFrom", 0);
            msg.setData(b);
            this.m_handler.sendMessage(msg);
        }
        return false;
    }

    public boolean onShowFingerMessage(CaManager mgr, int what, int arg1, int arg2) {
        if (this.m_handler != null) {
            Bundle b = new Bundle();
            Message msg = this.m_handler.obtainMessage();
            msg.what = CA_EVENT.EV_CA_SHOW_FINGER_MESSAGE.ordinal();
            b.putInt("MessageType", arg1);
            b.putInt("MessageType2", arg2);
            msg.setData(b);
            this.m_handler.sendMessage(msg);
        }
        return false;
    }

    public boolean onShowProgressStrip(CaManager mgr, int what, int arg1, int arg2) {
        if (this.m_handler != null) {
            Bundle b = new Bundle();
            Message msg = this.m_handler.obtainMessage();
            msg.what = CA_EVENT.EV_CA_SHOW_PROGRESS_STRIP.ordinal();
            b.putInt("MessageType", arg1);
            b.putInt("MessageType2", arg2);
            msg.setData(b);
            this.m_handler.sendMessage(msg);
        }
        return false;
    }

    public boolean onActionRequest(CaManager mgr, int what, int arg1, int arg2) {
        if (this.m_handler != null) {
            Bundle b = new Bundle();
            Message msg = this.m_handler.obtainMessage();
            msg.what = CA_EVENT.EV_CA_ACTION_REQUEST.ordinal();
            b.putInt("MessageType", arg1);
            b.putInt("MessageType2", arg2);
            msg.setData(b);
            this.m_handler.sendMessage(msg);
        }
        return false;
    }

    public boolean onEntitleChanged(CaManager mgr, int what, int arg1, int arg2) {
        if (this.m_handler != null) {
            Bundle b = new Bundle();
            Message msg = this.m_handler.obtainMessage();
            msg.what = CA_EVENT.EV_CA_ENTITLE_CHANGED.ordinal();
            b.putInt("MessageType", arg1);
            b.putInt("MessageType2", arg2);
            msg.setData(b);
            this.m_handler.sendMessage(msg);
        }
        return false;
    }

    public boolean onDetitleReceived(CaManager mgr, int what, int arg1, int arg2) {
        if (this.m_handler != null) {
            Bundle b = new Bundle();
            Message msg = this.m_handler.obtainMessage();
            msg.what = CA_EVENT.EV_CA_DETITLE_RECEVIED.ordinal();
            b.putInt("MessageType", arg1);
            b.putInt("MessageType2", arg2);
            msg.setData(b);
            this.m_handler.sendMessage(msg);
        }
        return false;
    }

    public boolean onLockService(CaManager mgr, int what, int arg1, int arg2, CaLockService arg3) {
        if (this.m_handler != null) {
            Bundle b = new Bundle();
            Message msg = this.m_handler.obtainMessage();
            msg.what = CA_EVENT.EV_CA_LOCKSERVICE.ordinal();
            b.putInt("MessageType", arg1);
            b.putInt("MessageType2", arg2);
            msg.setData(b);
            msg.obj = arg3;
            this.m_handler.sendMessage(msg);
        }
        return false;
    }

    public boolean onUNLockService(CaManager mgr, int what, int arg1, int arg2) {
        if (this.m_handler != null) {
            Bundle b = new Bundle();
            Message msg = this.m_handler.obtainMessage();
            msg.what = CA_EVENT.EV_CA_LOCKSERVICE.ordinal();
            b.putInt("MessageType", arg1);
            b.putInt("MessageType2", arg2);
            msg.setData(b);
            this.m_handler.sendMessage(msg);
        }
        return false;
    }

    public boolean onOtaState(CaManager mgr, int what, int arg1, int arg2) {
        if (this.m_handler != null) {
            Bundle b = new Bundle();
            Message msg = this.m_handler.obtainMessage();
            msg.what = CA_EVENT.EV_CA_OTASTATE.ordinal();
            b.putInt("MessageType", arg1);
            b.putInt("MessageType2", arg2);
            msg.setData(b);
            this.m_handler.sendMessage(msg);
        }
        return false;
    }
}
