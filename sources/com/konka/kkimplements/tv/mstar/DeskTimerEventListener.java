package com.konka.kkimplements.tv.mstar;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import com.mstar.android.tvapi.common.TimerManager;
import com.mstar.android.tvapi.common.TimerManager.OnTimerEventListener;

public class DeskTimerEventListener implements OnTimerEventListener {
    private static DeskTimerEventListener timerEventListener = null;
    private Handler m_handler;

    public enum EVENT {
        EV_DESTROY_COUNTDOWN,
        EV_ONESECOND_BEAT,
        EV_LASTMINUTE_WARN,
        EV_UPDATE_LASTMINUTE,
        EV_SIGNAL_LOCK,
        EV_EPG_TIME_UP,
        EV_EPGTIMER_COUNTDOWN,
        EV_EPGTIMER_RECORD_START,
        EV_PVR_NOTIFY_RECORD_STOP,
        EV_OAD_TIMESCAN
    }

    public DeskTimerEventListener() {
        this.m_handler = null;
        this.m_handler = null;
    }

    public void attachHandler(Handler handler) {
        this.m_handler = handler;
    }

    public void releaseHandler() {
        this.m_handler = null;
    }

    public static DeskTimerEventListener getInstance() {
        if (timerEventListener == null) {
            timerEventListener = new DeskTimerEventListener();
        }
        return timerEventListener;
    }

    public boolean onDestroyCountDown(TimerManager mgr, int what, int arg1, int arg2) {
        if (this.m_handler != null) {
            Bundle b = new Bundle();
            Message msg = this.m_handler.obtainMessage();
            msg.what = EVENT.EV_DESTROY_COUNTDOWN.ordinal();
            msg.setData(b);
            this.m_handler.sendMessage(msg);
        }
        return false;
    }

    public boolean onOneSecondBeat(TimerManager mgr, int what, int arg1, int arg2) {
        if (this.m_handler != null) {
            Bundle b = new Bundle();
            b.putInt("LeftTime", arg1);
            Message msg = this.m_handler.obtainMessage();
            msg.what = EVENT.EV_ONESECOND_BEAT.ordinal();
            msg.setData(b);
            this.m_handler.sendMessage(msg);
        }
        return false;
    }

    public boolean onLastMinuteWarn(TimerManager mgr, int what, int arg1, int arg2) {
        if (this.m_handler != null) {
            Bundle b = new Bundle();
            b.putInt("LeftTime", arg1);
            Message msg = this.m_handler.obtainMessage();
            msg.what = EVENT.EV_LASTMINUTE_WARN.ordinal();
            msg.setData(b);
            this.m_handler.sendMessage(msg);
        }
        return false;
    }

    public boolean onUpdateLastMinute(TimerManager mgr, int what, int arg1, int arg2) {
        if (this.m_handler != null) {
            Bundle b = new Bundle();
            b.putInt("LeftTime", arg1);
            Message msg = this.m_handler.obtainMessage();
            msg.what = EVENT.EV_UPDATE_LASTMINUTE.ordinal();
            msg.setData(b);
            this.m_handler.sendMessage(msg);
        }
        return false;
    }

    public boolean onSignalLock(TimerManager mgr, int what, int arg1, int arg2) {
        if (this.m_handler != null) {
            Bundle b = new Bundle();
            Message msg = this.m_handler.obtainMessage();
            msg.what = EVENT.EV_SIGNAL_LOCK.ordinal();
            msg.setData(b);
            this.m_handler.sendMessage(msg);
        }
        return false;
    }

    public boolean onEpgTimeUp(TimerManager mgr, int what, int arg1, int arg2) {
        if (this.m_handler != null) {
            Bundle b = new Bundle();
            b.putInt("LeftTime", arg1);
            Message msg = this.m_handler.obtainMessage();
            msg.what = EVENT.EV_EPG_TIME_UP.ordinal();
            msg.setData(b);
            this.m_handler.sendMessage(msg);
        }
        return false;
    }

    public boolean onEpgTimerCountDown(TimerManager mgr, int what, int arg1, int arg2) {
        if (this.m_handler != null) {
            Bundle b = new Bundle();
            b.putInt("LeftTime", arg1);
            Message msg = this.m_handler.obtainMessage();
            msg.what = EVENT.EV_EPGTIMER_COUNTDOWN.ordinal();
            msg.setData(b);
            this.m_handler.sendMessage(msg);
        }
        return false;
    }

    public boolean onEpgTimerRecordStart(TimerManager mgr, int what, int arg1, int arg2) {
        if (this.m_handler != null) {
            Bundle b = new Bundle();
            b.putInt("LeftTime", arg1);
            Message msg = this.m_handler.obtainMessage();
            msg.what = EVENT.EV_EPGTIMER_RECORD_START.ordinal();
            msg.setData(b);
            this.m_handler.sendMessage(msg);
        }
        return false;
    }

    public boolean onPvrNotifyRecordStop(TimerManager mgr, int what, int arg1, int arg2) {
        if (this.m_handler != null) {
            Bundle b = new Bundle();
            b.putInt("LeftTime", arg1);
            Message msg = this.m_handler.obtainMessage();
            msg.what = EVENT.EV_PVR_NOTIFY_RECORD_STOP.ordinal();
            msg.setData(b);
            this.m_handler.sendMessage(msg);
        }
        return false;
    }

    public boolean onOadTimeScan(TimerManager mgr, int what, int arg1, int arg2) {
        if (this.m_handler != null) {
            Bundle b = new Bundle();
            b.putInt("LeftTime", arg1);
            Message msg = this.m_handler.obtainMessage();
            msg.what = EVENT.EV_OAD_TIMESCAN.ordinal();
            msg.setData(b);
            this.m_handler.sendMessage(msg);
        }
        return false;
    }

    public boolean onPowerDownTime(TimerManager mgr, int what, int arg1, int arg2) {
        return false;
    }

    public boolean onSystemClkChg(TimerManager arg0, int arg1, int arg2, int arg3) {
        return false;
    }
}
