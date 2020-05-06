package com.konka.kkimplements.tv.vo;

import com.tencent.stat.common.StatConstants;

public class KonkaEventInfo {
    public int _mEndTime;
    public int _mEventId;
    public String _mEventName;
    public int _mIndex;
    public boolean _mIsScheduled;
    public int _mProgNo;
    public int _mServiceType;
    public int _mStartTime;
    public int _mWhichDay;

    public KonkaEventInfo() {
        this._mStartTime = 0;
        this._mEndTime = 0;
        this._mEventName = null;
        this._mEventId = 0;
        this._mProgNo = 0;
        this._mIsScheduled = false;
        this._mWhichDay = 0;
        this._mIndex = 0;
        this._mServiceType = 5;
        this._mStartTime = 0;
        this._mEndTime = 0;
        this._mEventName = StatConstants.MTA_COOPERATION_TAG;
        this._mEventId = 0;
        this._mProgNo = 0;
        this._mIsScheduled = false;
        this._mWhichDay = 0;
        this._mIndex = 0;
        this._mServiceType = 5;
    }
}
