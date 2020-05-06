package com.konka.kkimplements.tv.mstar;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.text.format.Time;
import android.util.Log;
import com.konka.kkimplements.tv.vo.KonkaEventInfo;
import com.konka.kkimplements.tv.vo.KonkaProgCount;
import com.konka.kkimplements.tv.vo.KonkaProgInfo;
import com.konka.kkinterface.tv.ChannelDesk;
import com.konka.kkinterface.tv.CommonDesk;
import com.konka.kkinterface.tv.DtvInterface.EPG_SCHEDULE_EVENT_INFO;
import com.konka.kkinterface.tv.DtvInterface.RESULT_ADDEVENT;
import com.konka.kkinterface.tv.EpgDesk;
import com.mstar.android.tvapi.common.ChannelManager;
import com.mstar.android.tvapi.common.TvManager;
import com.mstar.android.tvapi.common.exception.TvCommonException;
import com.mstar.android.tvapi.common.vo.EnumEpgTimerActType;
import com.mstar.android.tvapi.common.vo.EnumProgramCountType;
import com.mstar.android.tvapi.common.vo.EnumProgramInfoType;
import com.mstar.android.tvapi.common.vo.EnumScalerWindow;
import com.mstar.android.tvapi.common.vo.EnumServiceType;
import com.mstar.android.tvapi.common.vo.EpgEventTimerInfo;
import com.mstar.android.tvapi.common.vo.PresentFollowingEventInfo;
import com.mstar.android.tvapi.common.vo.ProgramInfo;
import com.mstar.android.tvapi.common.vo.ProgramInfoQueryCriteria;
import com.mstar.android.tvapi.common.vo.TvOsType.EnumInputSource;
import com.mstar.android.tvapi.common.vo.TvOsType.EnumTimeZone;
import com.mstar.android.tvapi.common.vo.VideoWindowType;
import com.mstar.android.tvapi.dtv.common.DtvManager;
import com.mstar.android.tvapi.dtv.common.EpgManager;
import com.mstar.android.tvapi.dtv.vo.DtvEitInfo;
import com.mstar.android.tvapi.dtv.vo.DtvType.EnumEpgDescriptionType;
import com.mstar.android.tvapi.dtv.vo.EnumEpgTimerCheck;
import com.mstar.android.tvapi.dtv.vo.EpgCridEventInfo;
import com.mstar.android.tvapi.dtv.vo.EpgCridStatus;
import com.mstar.android.tvapi.dtv.vo.EpgEventInfo;
import com.mstar.android.tvapi.dtv.vo.EpgFirstMatchHdCast;
import com.mstar.android.tvapi.dtv.vo.EpgHdSimulcast;
import com.mstar.android.tvapi.dtv.vo.EpgTrailerLinkInfo;
import com.mstar.android.tvapi.dtv.vo.NvodEventInfo;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

public class EpgDeskImpl extends BaseDeskImpl implements EpgDesk {
    private static /* synthetic */ int[] $SWITCH_TABLE$com$mstar$android$tvapi$common$vo$EnumServiceType = null;
    private static final String TAG = "EpgDeskImpl";
    private static EpgDeskImpl epgMgrImpl = null;
    private final int TIME_OFFSET = 0;
    private KonkaProgInfo _mCurKonkaProgInfo = null;
    private EnumServiceType _mCurServiceType = EnumServiceType.E_SERVICETYPE_INVALID;
    private int _mEventDiffTime = 0;
    private ArrayList<KonkaEventInfo> _mEventList = null;
    private KonkaProgCount _mProgCount = null;
    private ArrayList<KonkaProgInfo> _mProgList = null;
    private ChannelManager channelManager = null;

    /* renamed from: com reason: collision with root package name */
    private CommonDesk f1com = null;
    private Context context;
    private EpgManager epgMgr = null;

    static /* synthetic */ int[] $SWITCH_TABLE$com$mstar$android$tvapi$common$vo$EnumServiceType() {
        int[] iArr = $SWITCH_TABLE$com$mstar$android$tvapi$common$vo$EnumServiceType;
        if (iArr == null) {
            iArr = new int[EnumServiceType.values().length];
            try {
                iArr[EnumServiceType.E_SERVICETYPE_ATV.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                iArr[EnumServiceType.E_SERVICETYPE_DATA.ordinal()] = 4;
            } catch (NoSuchFieldError e2) {
            }
            try {
                iArr[EnumServiceType.E_SERVICETYPE_DTV.ordinal()] = 2;
            } catch (NoSuchFieldError e3) {
            }
            try {
                iArr[EnumServiceType.E_SERVICETYPE_INVALID.ordinal()] = 6;
            } catch (NoSuchFieldError e4) {
            }
            try {
                iArr[EnumServiceType.E_SERVICETYPE_RADIO.ordinal()] = 3;
            } catch (NoSuchFieldError e5) {
            }
            try {
                iArr[EnumServiceType.E_SERVICETYPE_UNITED_TV.ordinal()] = 5;
            } catch (NoSuchFieldError e6) {
            }
            $SWITCH_TABLE$com$mstar$android$tvapi$common$vo$EnumServiceType = iArr;
        }
        return iArr;
    }

    private EpgDeskImpl(Context context2) {
        this.context = context2;
        this.f1com = CommonDeskImpl.getInstance(context2);
        this.f1com.printfI("TvService", "EpgManagerImpl constructor!!");
        try {
            this.epgMgr = DtvManager.getEpgManager();
        } catch (TvCommonException e) {
            e.printStackTrace();
        }
        if (TvManager.getInstance() != null) {
            this.channelManager = TvManager.getInstance().getChannelManager();
        }
    }

    public static EpgDeskImpl getEpgMgrInstance(Context context2) {
        if (epgMgrImpl == null) {
            epgMgrImpl = new EpgDeskImpl(context2);
        }
        return epgMgrImpl;
    }

    public ArrayList<EpgEventInfo> getEventInfo(short serviceType, int serviceNo, Time baseTime, int maxEventInfoCount) throws TvCommonException {
        return this.epgMgr.getEventInfo(serviceType, serviceNo, baseTime, maxEventInfoCount);
    }

    public int getEventCount(short serviceType, int serviceNo, Time baseTime) {
        try {
            return this.epgMgr.getEventCount(serviceType, serviceNo, baseTime);
        } catch (TvCommonException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public EpgEventInfo getEventInfoByTime(short serviceType, int serviceNo, Time baseTime) throws TvCommonException {
        return this.epgMgr.getEventInfoByTime(serviceType, serviceNo, baseTime);
    }

    public EpgEventInfo getEventInfoById(short serviceType, int serviceNo, short eventID) throws TvCommonException {
        return this.epgMgr.getEventInfoById(serviceType, serviceNo, eventID);
    }

    public String getEventDescriptor(short serviceType, int serviceNumber, Time baseTime, EnumEpgDescriptionType epgDescriptionType) throws TvCommonException {
        return this.epgMgr.getEventDescriptor(serviceType, serviceNumber, baseTime, epgDescriptionType);
    }

    public ArrayList<EpgHdSimulcast> getEventHdSimulcast(short serviceType, int serviceNo, Time baseTime, short maxCount) throws TvCommonException {
        return this.epgMgr.getEventHdSimulcast(serviceType, serviceNo, baseTime, maxCount);
    }

    public void resetEpgProgramPriority() throws TvCommonException {
        this.epgMgr.resetEpgProgramPriority();
    }

    public EpgCridStatus getCridStatus(short serviceType, int serviceNumber, Time eventStartTime) throws TvCommonException {
        return this.epgMgr.getCridStatus(serviceType, serviceNumber, eventStartTime);
    }

    public ArrayList<EpgCridEventInfo> getCridSeriesList(short serviceType, int serviceNumber, Time eventStartTime) throws TvCommonException {
        return this.epgMgr.getCridSeriesList(serviceType, serviceNumber, eventStartTime);
    }

    public ArrayList<EpgCridEventInfo> getCridSplitList(short serviceType, int serviceNumber, Time eventStartTime) throws TvCommonException {
        return this.epgMgr.getCridSplitList(serviceType, serviceNumber, eventStartTime);
    }

    public ArrayList<EpgCridEventInfo> getCridAlternateList(short serviceType, int serviceNumber, Time eventStartTime) throws TvCommonException {
        return this.epgMgr.getCridAlternateList(serviceType, serviceNumber, eventStartTime);
    }

    public ArrayList<EpgTrailerLinkInfo> getRctTrailerLink() throws TvCommonException {
        return this.epgMgr.getRctTrailerLink();
    }

    public ArrayList<EpgCridEventInfo> getEventInfoByRctLink(EpgTrailerLinkInfo epgTrailerLinkInfo) throws TvCommonException {
        return this.epgMgr.getEventInfoByRctLink(epgTrailerLinkInfo);
    }

    public boolean enableEpgBarkerChannel() throws TvCommonException {
        return this.epgMgr.enableEpgBarkerChannel();
    }

    public boolean disableEpgBarkerChannel() throws TvCommonException {
        return this.epgMgr.disableEpgBarkerChannel();
    }

    public int getEpgEventOffsetTime(Time utcTime, boolean isStartTime) throws TvCommonException {
        return this.epgMgr.getEpgEventOffsetTime(utcTime, isStartTime);
    }

    public PresentFollowingEventInfo getPresentFollowingEventInfo(short serviceType, int serviceNo, boolean present, EnumEpgDescriptionType descriptionType) {
        try {
            return this.epgMgr.getPresentFollowingEventInfo(serviceType, serviceNo, present, descriptionType);
        } catch (TvCommonException e) {
            e.printStackTrace();
            Log.d("TvApp", "!!!getPresentFollowingEventInfo");
            return null;
        }
    }

    public EpgFirstMatchHdCast getEvent1stMatchHdSimulcast(short serviceType, int serviceNo, Time baseTime) throws TvCommonException {
        return this.epgMgr.getEvent1stMatchHdSimulcast(serviceType, serviceNo, baseTime);
    }

    public EpgFirstMatchHdCast getEvent1stMatchHdBroadcast(short serviceType, int serviceNo, Time baseTime) throws TvCommonException {
        return this.epgMgr.getEvent1stMatchHdBroadcast(serviceType, serviceNo, baseTime);
    }

    public DtvEitInfo getEitInfo(boolean bPresent) throws TvCommonException {
        return this.epgMgr.getEitInfo(bPresent);
    }

    public void setEpgProgramPriority(int programIndex) throws TvCommonException {
        this.epgMgr.setEpgProgramPriority(programIndex);
    }

    public void setEpgProgramPriority(short serviceType, int serviceNo) throws TvCommonException {
        this.epgMgr.setEpgProgramPriority(serviceType, serviceNo);
    }

    public int getNvodTimeShiftEventCount(short serviceType, int serviceNumber) throws TvCommonException {
        return this.epgMgr.getNvodTimeShiftEventCount(serviceType, serviceNumber);
    }

    public ArrayList<NvodEventInfo> getNvodTimeShiftEventInfo(short serviceType, int serviceNumber, int maxEventNum, EnumEpgDescriptionType eEpgDescritionType) throws TvCommonException {
        return this.epgMgr.getNvodTimeShiftEventInfo(serviceType, serviceNumber, maxEventNum, eEpgDescritionType);
    }

    public EnumEpgTimerCheck addEpgEvent(EpgEventTimerInfo vo) throws TvCommonException {
        if (TvManager.getInstance() != null) {
            return TvManager.getInstance().getTimerManager().addEpgEvent(vo);
        }
        return null;
    }

    public EpgEventTimerInfo getEpgTimerEventByIndex(int index) throws TvCommonException {
        if (TvManager.getInstance() != null) {
            return TvManager.getInstance().getTimerManager().getEpgTimerEventByIndex(index);
        }
        return null;
    }

    public int getEpgTimerEventCount() throws TvCommonException {
        if (TvManager.getInstance() != null) {
            return TvManager.getInstance().getTimerManager().getEpgTimerEventCount();
        }
        return 0;
    }

    public EnumEpgTimerCheck isEpgTimerSettingValid(EpgEventTimerInfo timerInfoVo) throws TvCommonException {
        if (TvManager.getInstance() != null) {
            return TvManager.getInstance().getTimerManager().isEpgTimerSettingValid(timerInfoVo);
        }
        return null;
    }

    public boolean delAllEpgEvent() throws TvCommonException {
        if (TvManager.getInstance() != null) {
            return TvManager.getInstance().getTimerManager().delAllEpgEvent();
        }
        return false;
    }

    public boolean delEpgEvent(int epgEvent) throws TvCommonException {
        if (TvManager.getInstance() != null) {
            return TvManager.getInstance().getTimerManager().delEpgEvent(epgEvent);
        }
        return false;
    }

    public boolean deletePastEpgTimer() throws TvCommonException {
        if (TvManager.getInstance() != null) {
            return TvManager.getInstance().getTimerManager().deletePastEpgTimer();
        }
        return false;
    }

    public boolean execEpgTimerAction() throws TvCommonException {
        if (TvManager.getInstance() != null) {
            return TvManager.getInstance().getTimerManager().execEpgTimerAction();
        }
        return false;
    }

    public void cancelEpgTimerEvent(int timeActing, boolean checkEndTime) throws TvCommonException {
        if (TvManager.getInstance() != null) {
            TvManager.getInstance().getTimerManager().cancelEpgTimerEvent(timeActing, checkEndTime);
        }
    }

    public EpgEventTimerInfo getEpgTimerRecordingProgram() throws TvCommonException {
        if (TvManager.getInstance() != null) {
            return TvManager.getInstance().getTimerManager().getEpgTimerRecordingProgram();
        }
        return null;
    }

    public EnumTimeZone getTimeZone() throws TvCommonException {
        if (TvManager.getInstance() != null) {
            return TvManager.getInstance().getTimerManager().getTimeZone();
        }
        return null;
    }

    public ProgramInfo getCurProgramInfo() {
        this.f1com.printfI(TAG, "getCurProgramInfo");
        ProgramInfo programInfo = null;
        try {
            programInfo = this.channelManager.getProgramInfo(new ProgramInfoQueryCriteria(), EnumProgramInfoType.E_INFO_CURRENT);
            this._mCurServiceType = EnumServiceType.values()[programInfo.serviceType];
            this.f1com.printfI(TAG, "Cur ProgNo is -------->" + programInfo.number + "ProgramInfo ServiceType is" + programInfo.serviceType);
            this.f1com.printfI(TAG, "CurServiceType is -------->" + this._mCurServiceType.ordinal());
            return programInfo;
        } catch (TvCommonException e) {
            e.printStackTrace();
            return programInfo;
        }
    }

    public EnumInputSource getCurInputSource() {
        EnumInputSource eCurSource = EnumInputSource.E_INPUT_SOURCE_NONE;
        try {
            if (TvManager.getInstance() != null) {
                return TvManager.getInstance().getCurrentInputSource();
            }
            return eCurSource;
        } catch (TvCommonException e) {
            e.printStackTrace();
            return eCurSource;
        }
    }

    public KonkaProgInfo getCurKonkaProgInfo() {
        ProgramInfo info = getCurProgramInfo();
        KonkaProgInfo kpi = null;
        if (info != null) {
            kpi = new KonkaProgInfo();
            kpi._mProgDbId = info.progId;
            kpi._mProgIndex = info.queryIndex;
            kpi._mProgName = info.serviceName;
            this.f1com.printfI(TAG, "initPosInChannelList Kpi ProgName is ------>" + kpi._mProgName);
            kpi._mProgNo = info.number;
            this.f1com.printfI(TAG, "initPosInChannelList Kpi ProgNumber is ------>" + kpi._mProgNo);
            kpi._mProgType = info.serviceType;
        }
        this._mCurKonkaProgInfo = kpi;
        return kpi;
    }

    public void setDtvInputSource() {
        try {
            if (EnumInputSource.E_INPUT_SOURCE_DTV.ordinal() != getCurInputSource().ordinal() && TvManager.getInstance() != null) {
                TvManager.getInstance().setInputSource(EnumInputSource.E_INPUT_SOURCE_DTV);
            }
        } catch (TvCommonException e) {
            e.printStackTrace();
        }
    }

    public KonkaProgCount getDtvProgCount() {
        int iDtvOnly = 0;
        int iRadio = 0;
        int iData = 0;
        int iDtvAll = 0;
        try {
            iDtvAll = this.channelManager.getProgramCount(EnumProgramCountType.E_COUNT_DTV);
            if (iDtvAll == 0) {
                iDtvOnly = 0;
                iRadio = 0;
                iData = 0;
            } else {
                iDtvOnly = this.channelManager.getProgramCount(EnumProgramCountType.E_COUNT_DTV_TV);
                iRadio = this.channelManager.getProgramCount(EnumProgramCountType.E_COUNT_DTV_RADIO);
                iData = this.channelManager.getProgramCount(EnumProgramCountType.E_COUNT_DTV_DATA);
            }
        } catch (TvCommonException e) {
            e.printStackTrace();
        }
        this._mProgCount = new KonkaProgCount(iDtvOnly, iRadio, iData, iDtvAll);
        return this._mProgCount;
    }

    public synchronized ArrayList<KonkaProgInfo> getProgListByServiceType(EnumServiceType eServiceType) {
        ArrayList<KonkaProgInfo> arrayList = null;
        synchronized (this) {
            getCurProgramInfo();
            this.f1com.printfI(TAG, "End getCurProgramInfo()");
            if (this._mProgCount.isNoProgInServiceType(EnumProgramCountType.E_COUNT_DTV)) {
                this.f1com.printfI(TAG, "NoProgInServiceType");
            } else {
                this.f1com.printfI(TAG, "HasProgInServiceType");
                if (isProgCountEqualToZeroByServiceType(eServiceType)) {
                    this.f1com.printfI(TAG, "ProgCount is Zero in ------>" + eServiceType.ordinal());
                } else {
                    int iProgCount = this._mProgCount.getProgCountByServiceType(eServiceType);
                    this.f1com.printfI(TAG, "ProgCount is ------>" + iProgCount);
                    new ProgramInfo();
                    ProgramInfoQueryCriteria piQc = new ProgramInfoQueryCriteria();
                    if (this._mProgList == null) {
                        this._mProgList = new ArrayList<>();
                        this.f1com.printfI(TAG, "ProgList is ------> a new one");
                    } else {
                        this._mProgList.clear();
                        this.f1com.printfI(TAG, "ProgList is ------> clear");
                    }
                    this.f1com.printfI(TAG, "the type count is " + String.valueOf(iProgCount));
                    for (int i = 0; i < iProgCount; i++) {
                        if (eServiceType == EnumServiceType.E_SERVICETYPE_RADIO) {
                            piQc.queryIndex = this._mProgCount.getProgCountByServiceType(EnumServiceType.E_SERVICETYPE_DTV) + i;
                        } else {
                            piQc.queryIndex = i;
                        }
                        piQc.setServiceType(eServiceType);
                        ProgramInfo tvProgInfo = getProgramInfoByIndex(piQc);
                        if (tvProgInfo == null) {
                            this.f1com.printfI(TAG, "TvProgInfo is ------> null");
                        } else {
                            this.f1com.printfI(TAG, "TvProgInfo is ------>" + tvProgInfo.toString());
                        }
                        if (tvProgInfo != null) {
                            this.f1com.printfI(TAG, "TvProgInfo is ------> not null");
                            KonkaProgInfo kkProgInfo = new KonkaProgInfo();
                            kkProgInfo._mProgDbId = tvProgInfo.progId;
                            kkProgInfo._mProgIndex = tvProgInfo.queryIndex;
                            kkProgInfo._mProgName = tvProgInfo.serviceName;
                            kkProgInfo._mProgNo = tvProgInfo.number;
                            kkProgInfo._mProgType = tvProgInfo.serviceType;
                            this.f1com.printfI(TAG, "name[" + kkProgInfo._mProgName + "]no[" + String.valueOf(kkProgInfo._mProgNo) + "]");
                            this._mProgList.add(kkProgInfo);
                            this.f1com.printfI(TAG, "_mProgList.add(kkProgInfo)");
                        }
                    }
                    arrayList = this._mProgList;
                }
            }
        }
        return arrayList;
    }

    public ArrayList<KonkaProgInfo> getProgListByServiceType(short sServiceType) {
        EnumServiceType eType = getServiceTypeByShort(sServiceType);
        if (eType.ordinal() == EnumServiceType.E_SERVICETYPE_INVALID.ordinal()) {
            return null;
        }
        return getProgListByServiceType(eType);
    }

    private EnumServiceType getServiceTypeByShort(short sServiceType) {
        if (sServiceType < EnumServiceType.E_SERVICETYPE_DTV.ordinal() || sServiceType > EnumServiceType.E_SERVICETYPE_DATA.ordinal()) {
            return EnumServiceType.E_SERVICETYPE_INVALID;
        }
        return EnumServiceType.values()[sServiceType];
    }

    private boolean isProgCountEqualToZeroByServiceType(EnumServiceType eServiceType) {
        if (this._mProgCount == null) {
            getDtvProgCount();
        }
        switch ($SWITCH_TABLE$com$mstar$android$tvapi$common$vo$EnumServiceType()[eServiceType.ordinal()]) {
            case 2:
                return this._mProgCount.isNoProgInServiceType(EnumProgramCountType.E_COUNT_DTV_TV);
            case 3:
                return this._mProgCount.isNoProgInServiceType(EnumProgramCountType.E_COUNT_DTV_RADIO);
            case 4:
                return this._mProgCount.isNoProgInServiceType(EnumProgramCountType.E_COUNT_DTV_DATA);
            default:
                return true;
        }
    }

    private ProgramInfo getProgramInfoByNumber(ProgramInfoQueryCriteria piQC) {
        ProgramInfo pi = null;
        try {
            pi = this.channelManager.getProgramInfo(piQC, EnumProgramInfoType.E_INFO_PROGRAM_NUMBER);
            if (pi == null) {
                this.f1com.printfI(TAG, "TvProgInfo is ------> null");
            } else {
                this.f1com.printfI(TAG, "TvProgInfo is ------>" + pi.serviceName);
            }
        } catch (TvCommonException e) {
            e.printStackTrace();
            this.f1com.printfI(TAG, "getProgramInfoByNumber =========");
        }
        return pi;
    }

    private ProgramInfo getProgramInfoByIndex(ProgramInfoQueryCriteria piQC) {
        ProgramInfo pi = null;
        try {
            return this.channelManager.getProgramInfo(piQC, EnumProgramInfoType.E_INFO_DATABASE_INDEX);
        } catch (TvCommonException e) {
            e.printStackTrace();
            this.f1com.printfI(TAG, "getProgramInfoByNumber =========");
            return pi;
        }
    }

    private int getHourOffset() {
        String id = TimeZone.getDefault().getID();
        return TimeZone.getTimeZone(id).getOffset(Calendar.getInstance().getTimeInMillis()) / 3600000;
    }

    private int getMinuteOffset() {
        String id = TimeZone.getDefault().getID();
        return (TimeZone.getTimeZone(id).getOffset(Calendar.getInstance().getTimeInMillis()) / 60000) % 60;
    }

    public synchronized ArrayList<KonkaEventInfo> getEventList(short sServiceType, int iProgNo) {
        ArrayList<KonkaEventInfo> arrayList;
        int iResult;
        Time baseTime = new Time("GMT+0");
        Time zeroTime = new Time("GMT+0");
        int timeoffset = (getHourOffset() * 3600) + (getMinuteOffset() * 60);
        baseTime.setToNow();
        zeroTime.set(0);
        if (getServiceTypeByShort(sServiceType).ordinal() == EnumServiceType.E_SERVICETYPE_INVALID.ordinal()) {
            arrayList = null;
        } else if (this._mCurKonkaProgInfo._mProgNo != iProgNo) {
            this.f1com.printfE(TAG, "error program number !");
            arrayList = null;
        } else if (baseTime.equals(zeroTime)) {
            arrayList = null;
        } else {
            ArrayList<EpgEventInfo> tempEventList = new ArrayList<>();
            baseTime.set(baseTime.toMillis(true) + 0);
            baseTime.setToNow();
            int iDay = 0;
            int iEventCount = getEventCount(sServiceType, iProgNo, baseTime);
            if (iEventCount <= 0) {
                this.f1com.printfI(TAG, "errrrrrrrrrrroooooo");
                arrayList = null;
            } else {
                try {
                    tempEventList.clear();
                    tempEventList = getEventInfo(sServiceType, iProgNo, baseTime, iEventCount);
                } catch (TvCommonException e) {
                    e.printStackTrace();
                }
                baseTime.set(baseTime.toMillis(true) + ((long) (timeoffset * ChannelDesk.max_dtv_count)));
                int iPrevDay = baseTime.weekDay;
                if (tempEventList == null || tempEventList.size() <= 0) {
                    arrayList = null;
                } else {
                    if (this._mEventList == null) {
                        this._mEventList = new ArrayList();
                    } else {
                        this._mEventList.clear();
                    }
                    int j = 0;
                    int iEpgTimerCount = 0;
                    try {
                        iEpgTimerCount = getEpgTimerEventCount();
                    } catch (TvCommonException e2) {
                        this.f1com.printfE(TAG, "getEpgTimerEventCount error");
                    }
                    int i = 0;
                    while (true) {
                        if (i >= tempEventList.size()) {
                            arrayList = this._mEventList;
                            break;
                        } else if (this._mCurKonkaProgInfo._mProgNo != iProgNo) {
                            this.f1com.printfE(TAG, "last program number !");
                            arrayList = null;
                            break;
                        } else {
                            KonkaEventInfo tempinfo = new KonkaEventInfo();
                            EpgEventInfo info = (EpgEventInfo) tempEventList.get(i);
                            tempinfo._mStartTime = info.startTime + 0;
                            tempinfo._mEndTime = tempinfo._mStartTime + info.durationTime;
                            tempinfo._mEventId = info.eventId;
                            tempinfo._mEventName = info.name;
                            tempinfo._mProgNo = iProgNo;
                            tempinfo._mIndex = i;
                            tempinfo._mServiceType = sServiceType;
                            baseTime.set(((long) (tempinfo._mStartTime + timeoffset)) * 1000);
                            if (iPrevDay != baseTime.weekDay) {
                                iDay++;
                                iPrevDay = baseTime.weekDay;
                            }
                            tempinfo._mWhichDay = iDay;
                            if (j < iEpgTimerCount) {
                                do {
                                    Log.d("EPG", "===========>>>> 222 Info.startTime[" + info.startTime + "] Info.endTime[" + info.endTime + "] tempInfo._mStartTime[" + tempinfo._mStartTime + "] tempInfo._mEndTime[" + tempinfo._mEndTime + "]");
                                    iResult = isEventScheduled(tempinfo, j);
                                    if (iResult != 1) {
                                        j++;
                                    }
                                    if (iResult != 2) {
                                        break;
                                    }
                                } while (j < iEpgTimerCount);
                                if (iResult == 0) {
                                    tempinfo._mIsScheduled = true;
                                } else {
                                    tempinfo._mIsScheduled = false;
                                }
                            } else {
                                tempinfo._mIsScheduled = false;
                            }
                            this._mEventList.add(i, tempinfo);
                            i++;
                        }
                    }
                }
            }
        }
        return arrayList;
    }

    public ArrayList<KonkaEventInfo> getEventListByDay(int iDay) {
        ProgramInfo pi = getCurProgramInfo();
        getEventList(pi.serviceType, pi.number);
        if (this._mEventList == null || this._mEventList.size() <= 0) {
            return null;
        }
        ArrayList<KonkaEventInfo> list = new ArrayList<>();
        for (int i = 0; i < this._mEventList.size(); i++) {
            if (((KonkaEventInfo) this._mEventList.get(i))._mWhichDay == iDay) {
                list.add((KonkaEventInfo) this._mEventList.get(i));
            } else if (((KonkaEventInfo) this._mEventList.get(i))._mWhichDay <= iDay && ((KonkaEventInfo) this._mEventList.get(i))._mWhichDay >= iDay) {
            }
        }
        return list;
    }

    private int isEventScheduled(KonkaEventInfo eventInfo, int iTimerIndex) {
        EpgEventTimerInfo timerInfo = null;
        try {
            timerInfo = getEpgTimerEventByIndex(iTimerIndex);
        } catch (TvCommonException e) {
            this.f1com.printfE(TAG, "getEpgTimerEventByIndex error");
        }
        timerInfo.startTime = timerInfo.startTime + this._mEventDiffTime + 0;
        if (timerInfo != null && timerInfo.startTime >= eventInfo._mStartTime && timerInfo.startTime < eventInfo._mEndTime && timerInfo.serviceNumber == eventInfo._mProgNo && timerInfo.serviceType == eventInfo._mServiceType) {
            return 0;
        }
        if (timerInfo.startTime > eventInfo._mStartTime) {
            return 1;
        }
        return 2;
    }

    public String getEventDetailDescriptor(int iStartTime) {
        String tempString = null;
        Time baseTime = new Time();
        baseTime.set(((long) (iStartTime + 0)) * 1000);
        ProgramInfo tempProgramInfo = getCurProgramInfo();
        this.f1com.printfE(TAG, "获取节目信息=======当前频道信息" + tempProgramInfo.number + tempProgramInfo.serviceName + tempProgramInfo.queryIndex);
        try {
            tempString = getEventDescriptor(tempProgramInfo.serviceType, tempProgramInfo.number, baseTime, EnumEpgDescriptionType.E_DETAIL_DESCRIPTION);
        } catch (TvCommonException e) {
            e.printStackTrace();
        }
        this.f1com.printfE(TAG, "获取节目信息=======当前节目" + tempString);
        return tempString;
    }

    public Time getCurClkTime() {
        Time curTime = new Time();
        curTime.setToNow();
        curTime.set(curTime.toMillis(true) + 0);
        return curTime;
    }

    public void ProgramSel(KonkaProgInfo kpi) {
        try {
            this.channelManager.selectProgram(kpi._mProgNo, kpi._mProgType, kpi._mProgIndex);
            this._mCurKonkaProgInfo = kpi;
            this.f1com.printfE(TAG, "program name sis ---------->" + kpi._mProgName);
        } catch (TvCommonException e) {
            e.printStackTrace();
        }
    }

    private int getEpgTimerIndex(KonkaEventInfo kei) {
        int iTimerCount = 0;
        EpgEventTimerInfo timerInfo = null;
        boolean bFind = false;
        try {
            iTimerCount = getEpgTimerEventCount();
        } catch (TvCommonException e) {
            e.printStackTrace();
        }
        int iIndex = 0;
        while (true) {
            if (iIndex >= iTimerCount) {
                break;
            }
            try {
                timerInfo = getEpgTimerEventByIndex(iIndex);
            } catch (TvCommonException e2) {
                e2.printStackTrace();
            }
            timerInfo.startTime = timerInfo.startTime + this._mEventDiffTime + 0;
            if (timerInfo != null) {
                if (kei._mStartTime == timerInfo.startTime && timerInfo.serviceNumber == kei._mProgNo) {
                    bFind = true;
                    break;
                } else if (kei._mStartTime < timerInfo.startTime) {
                    bFind = false;
                    break;
                }
            }
            iIndex++;
        }
        if (bFind) {
            return iIndex;
        }
        return -1;
    }

    public boolean delEpgTimerEvent(int iIndex, boolean bIsEpgMenu) {
        if (!bIsEpgMenu) {
            int iTimerCount = 0;
            try {
                iTimerCount = getEpgTimerEventCount();
            } catch (TvCommonException e) {
                e.printStackTrace();
            }
            if (iIndex < 0 && iIndex >= iTimerCount) {
                return false;
            }
            EpgEventTimerInfo info = null;
            try {
                info = getEpgTimerEventByIndex(iIndex);
            } catch (TvCommonException e2) {
                e2.printStackTrace();
            }
            deleteScheduleInfoByStartTime(info.startTime);
            try {
                delEpgEvent(iIndex);
            } catch (TvCommonException e3) {
                e3.printStackTrace();
            }
            return true;
        } else if (this._mEventList == null) {
            return false;
        } else {
            int iTimerIndex = getEpgTimerIndex((KonkaEventInfo) this._mEventList.get(iIndex));
            if (iTimerIndex == -1) {
                return false;
            }
            EpgEventTimerInfo info2 = null;
            try {
                info2 = getEpgTimerEventByIndex(iTimerIndex);
            } catch (TvCommonException e4) {
                e4.printStackTrace();
            }
            deleteScheduleInfoByStartTime(info2.startTime);
            try {
                delEpgEvent(iTimerIndex);
                ((KonkaEventInfo) this._mEventList.get(iIndex))._mIsScheduled = false;
            } catch (TvCommonException e5) {
                e5.printStackTrace();
            }
            return true;
        }
    }

    private boolean checkIsScheduledInTwoMinutes(int iStartTime) {
        int iTimerCount = 0;
        try {
            if (TvManager.getInstance() != null) {
                iTimerCount = TvManager.getInstance().getTimerManager().getEpgTimerEventCount();
            }
        } catch (TvCommonException e) {
            e.printStackTrace();
        }
        EpgEventTimerInfo timerInfo = null;
        for (int i = 0; i < iTimerCount; i++) {
            try {
                timerInfo = getEpgTimerEventByIndex(i);
            } catch (TvCommonException e2) {
                e2.printStackTrace();
            }
            timerInfo.startTime += 0;
            if (timerInfo.startTime > iStartTime - 120 && timerInfo.startTime < iStartTime + 120) {
                return true;
            }
            if (timerInfo.startTime >= iStartTime + 120) {
                return false;
            }
        }
        return false;
    }

    public RESULT_ADDEVENT addEpgTimerEvent(KonkaEventInfo kei) {
        EpgEventTimerInfo info = new EpgEventTimerInfo();
        new Time();
        int ibaseTime = (int) (getCurClkTime().toMillis(true) / 1000);
        this.f1com.printfE(TAG, "================ibaseTime" + ibaseTime);
        this.f1com.printfE(TAG, "================_mStartTime" + kei._mStartTime);
        if (kei._mStartTime <= this._mEventDiffTime + ibaseTime + 62) {
            return RESULT_ADDEVENT.E_ADDEVENT_FAIL_TIMEOUT;
        }
        if (kei._mStartTime < ibaseTime + 120) {
            return RESULT_ADDEVENT.E_ADDEVENT_FAIL_OF_ATHAND;
        }
        if (checkIsScheduledInTwoMinutes(kei._mStartTime - this._mEventDiffTime)) {
            return RESULT_ADDEVENT.E_ADDEVENT_FAIL_OF_EXIST;
        }
        EnumEpgTimerCheck eCheck = EnumEpgTimerCheck.E_NONE;
        info.startTime = (kei._mStartTime - this._mEventDiffTime) + 0;
        info.eventID = kei._mEventId;
        info.durationTime = kei._mEndTime - kei._mStartTime;
        info.enTimerType = (short) EnumEpgTimerActType.EN_EPGTIMER_ACT_REMINDER.ordinal();
        info.serviceNumber = kei._mProgNo;
        info.serviceType = kei._mServiceType;
        info.enRepeatMode = 129;
        info.isEndTimeBeforeStart = false;
        try {
            eCheck = addEpgEvent(info);
        } catch (TvCommonException e) {
            e.printStackTrace();
        }
        this.f1com.printfE(TAG, "add event result is [" + eCheck + "] here ");
        if (eCheck.ordinal() == EnumEpgTimerCheck.E_FULL.ordinal()) {
            return RESULT_ADDEVENT.E_ADDEVENT_FAIL_FULL;
        }
        if (eCheck.ordinal() == EnumEpgTimerCheck.E_SUCCESS.ordinal()) {
            ProgramInfoQueryCriteria piQc = new ProgramInfoQueryCriteria();
            piQc.number = info.serviceNumber;
            piQc.setServiceType(EnumServiceType.values()[info.serviceType]);
            ProgramInfo progInfo = getProgramInfoByNumber(piQc);
            System.out.println("the name is " + progInfo.serviceName);
            insertScheduleInfo(progInfo.serviceName, kei._mEventName, info.startTime);
            return RESULT_ADDEVENT.E_ADDEVENT_SUCCESSS;
        } else if (eCheck.ordinal() == EnumEpgTimerCheck.E_OVERLAY.ordinal()) {
            return RESULT_ADDEVENT.E_ADDEVENT_FAIL_OVERLAY;
        } else {
            return RESULT_ADDEVENT.E_ADDEVENT_FAIL_OTHER_EXCEPTION;
        }
    }

    public void updateTimerInfo(String servicename, String eventname, int starttime) {
        insertScheduleInfo(servicename, eventname, starttime);
    }

    public ArrayList<EPG_SCHEDULE_EVENT_INFO> getEpgTimerEventList() {
        ArrayList<EPG_SCHEDULE_EVENT_INFO> scheduleList = new ArrayList<>();
        int iTimerCount = 0;
        try {
            if (TvManager.getInstance() != null) {
                iTimerCount = TvManager.getInstance().getTimerManager().getEpgTimerEventCount();
            }
        } catch (TvCommonException e) {
            e.printStackTrace();
        }
        Time baseTime = new Time();
        EpgEventTimerInfo info = null;
        for (int i = 0; i < iTimerCount; i++) {
            EPG_SCHEDULE_EVENT_INFO scheduleInfo = new EPG_SCHEDULE_EVENT_INFO();
            try {
                info = getEpgTimerEventByIndex(i);
            } catch (TvCommonException e2) {
                e2.printStackTrace();
            }
            if (info != null) {
                scheduleInfo.eventId = info.eventID;
                scheduleInfo.progNumber = info.serviceNumber;
                scheduleInfo.startTime = info.startTime + 0;
                scheduleInfo.eventName = queryEventName(info.startTime);
                scheduleInfo.progName = queryServiceName(info.startTime);
                scheduleInfo.serviceType = info.serviceType;
                scheduleInfo.timerType = info.enTimerType;
                scheduleInfo.repeatMode = info.enRepeatMode;
                this.f1com.printfE("timer start time is [" + info.startTime + "]");
                long start = (((long) info.startTime) * 1000) + 0 + ((long) (this._mEventDiffTime * ChannelDesk.max_dtv_count));
                this.f1com.printfE("timer start time is [" + start + "]");
                baseTime.set(start);
                this.f1com.printfE(TAG, "加入预约时间============" + baseTime.year + "/" + baseTime.month + "/" + baseTime.monthDay + "/" + baseTime.hour + ":" + baseTime.minute);
                if (baseTime.toMillis(true) <= getCurClkTime().toMillis(true)) {
                    delEpgTimerEvent(i, false);
                } else {
                    scheduleList.add(scheduleInfo);
                }
            }
        }
        return scheduleList;
    }

    public void setDispalyWindow(int x, int y, int width, int height) {
        VideoWindowType vwType = new VideoWindowType();
        vwType.x = x;
        vwType.y = y;
        vwType.width = width;
        vwType.height = height;
        try {
            if (TvManager.getInstance() != null && TvManager.getInstance().getPictureManager() != null) {
                TvManager.getInstance().getPictureManager().selectWindow(EnumScalerWindow.E_MAIN_WINDOW);
                TvManager.getInstance().getPictureManager().setDisplayWindow(vwType);
                TvManager.getInstance().getPictureManager().scaleWindow();
            }
        } catch (TvCommonException e) {
            e.printStackTrace();
        }
    }

    public synchronized void modifyTimerNotifyTime(int iDiffTime) {
        int iTimerCount = 0;
        ArrayList<EpgEventTimerInfo> timerList = new ArrayList<>();
        EpgEventTimerInfo info = null;
        this.f1com.printfE("timer iDiffTime is [" + iDiffTime + "] =============");
        try {
            iTimerCount = getEpgTimerEventCount();
        } catch (TvCommonException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < iTimerCount; i++) {
            try {
                info = getEpgTimerEventByIndex(i);
            } catch (TvCommonException e2) {
                e2.printStackTrace();
            }
            if (info != null) {
                timerList.add(info);
            }
        }
        if (iTimerCount > 0) {
            for (int i2 = 0; i2 < iTimerCount; i2++) {
                delEpgTimerEvent(0, false);
            }
        }
        int iTimerCount2 = timerList.size();
        Time current = new Time();
        current.setToNow();
        int iCurTime = (int) (current.toMillis(true) / 1000);
        for (int i3 = 0; i3 < iTimerCount2; i3++) {
            EpgEventTimerInfo info2 = (EpgEventTimerInfo) timerList.get(i3);
            info2.startTime -= iDiffTime;
            if (info2.startTime >= iCurTime + 120) {
                try {
                    addEpgEvent(info2);
                } catch (TvCommonException e3) {
                    e3.printStackTrace();
                }
            }
        }
        return;
    }

    public void setScheduleNofityTime(int iNowNotifyTime) {
        this.f1com.printfE("timer iNowNotifyTime is [" + iNowNotifyTime + "]");
        this._mEventDiffTime = iNowNotifyTime;
    }

    public void cancelEpgTimerEvent(int iLeftTime) {
        try {
            if (TvManager.getInstance() != null) {
                TvManager.getInstance().getTimerManager().cancelEpgTimerEvent(iLeftTime, false);
            }
        } catch (TvCommonException e) {
            e.printStackTrace();
        }
    }

    public void execEpgTimerEvent() {
        try {
            execEpgTimerAction();
        } catch (TvCommonException e) {
            e.printStackTrace();
        }
    }

    private String queryEventName(int starttime) {
        Cursor cursor = DataBaseDeskImpl.getDataBaseMgrInstance(this.context).queryEpg(true, starttime);
        String eventName = new String();
        if (cursor.moveToFirst()) {
            eventName = cursor.getString(cursor.getColumnIndex("sEventName"));
            System.out.println("\n=====>>eventName " + eventName + " @starttime " + starttime);
        }
        cursor.close();
        return eventName;
    }

    public void deleteScheduleInfoByStartTime(int starttime) {
        DataBaseDeskImpl.getDataBaseMgrInstance(this.context).deleteEpg(starttime);
    }

    public void deletePastScheduleInfo() {
        Cursor c = DataBaseDeskImpl.getDataBaseMgrInstance(this.context).queryEpg(false, 0);
        List<Integer> starttimes = new ArrayList<>();
        int iCurTime = ((((int) (getCurClkTime().toMillis(true) / 1000)) + 0) - this._mEventDiffTime) - 10;
        if (c.moveToFirst()) {
            int index = c.getColumnIndex("u32StartTime");
            do {
                int starttime = c.getInt(index);
                if (starttime < iCurTime) {
                    starttimes.add(new Integer(starttime));
                }
            } while (c.moveToNext());
        }
        if (!starttimes.isEmpty()) {
            int size = starttimes.size();
            for (int i = 0; i < size; i++) {
                DataBaseDeskImpl.getDataBaseMgrInstance(this.context).deleteEpg(((Integer) starttimes.get(i)).intValue());
            }
        }
        starttimes.clear();
        c.close();
    }

    public void insertScheduleInfo(String serviceName, String eventName, int starttime) {
        deletePastScheduleInfo();
        ContentValues value = new ContentValues();
        value.put("sEventName", eventName);
        value.put("u32StartTime", Integer.valueOf(starttime));
        value.put("sServiceName", serviceName);
        try {
            DataBaseDeskImpl.getDataBaseMgrInstance(this.context).insertEpg(value);
        } catch (SQLiteException e) {
            e.printStackTrace();
        }
    }

    public void UpdateEpgTimer(String serviceName, String eventName, int startTime) {
        ContentValues value = new ContentValues();
        value.put("sEventName", eventName);
        value.put("sServiceName", serviceName);
        try {
            System.out.println("\n########### getDataBaseMgrInstance \n");
            DataBaseDeskImpl.getDataBaseMgrInstance(this.context).updateChannelNameAndEventName(value, startTime);
        } catch (SQLiteException e) {
            e.printStackTrace();
        }
    }

    private String queryServiceName(int starttime) {
        Cursor cursor = DataBaseDeskImpl.getDataBaseMgrInstance(this.context).queryEpg(true, starttime);
        String serviceName = new String();
        if (cursor.moveToFirst()) {
            serviceName = cursor.getString(cursor.getColumnIndex("sServiceName"));
            System.out.println("\n=====>>serviceName " + serviceName + " @starttime " + starttime);
        }
        cursor.close();
        return serviceName;
    }

    public void openDB() {
    }

    public void closeDB() {
    }

    public EPG_SCHEDULE_EVENT_INFO getScheduleInfoByIndex(int index) {
        EpgEventTimerInfo timerInfo = null;
        try {
            timerInfo = getEpgTimerEventByIndex(index);
        } catch (TvCommonException e) {
            e.printStackTrace();
        }
        if (timerInfo == null) {
            return null;
        }
        EPG_SCHEDULE_EVENT_INFO info = new EPG_SCHEDULE_EVENT_INFO();
        info.progNumber = timerInfo.serviceNumber;
        info.startTime = timerInfo.startTime + 0;
        info.eventId = timerInfo.eventID;
        info.eventName = queryEventName(timerInfo.startTime);
        info.progName = queryServiceName(timerInfo.startTime);
        info.serviceType = (short) timerInfo.serviceType;
        System.out.println("\n EpgEventTimerInfo programNum " + info.progNumber + "[eventName] " + info.eventName + "[ProgName] " + info.progName);
        return info;
    }

    public ArrayList<KonkaProgInfo> getEpgSearchReslut(String inputString) {
        return null;
    }
}
