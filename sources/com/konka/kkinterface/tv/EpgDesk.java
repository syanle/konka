package com.konka.kkinterface.tv;

import android.text.format.Time;
import com.konka.kkimplements.tv.vo.KonkaEventInfo;
import com.konka.kkimplements.tv.vo.KonkaProgCount;
import com.konka.kkimplements.tv.vo.KonkaProgInfo;
import com.konka.kkinterface.tv.DtvInterface.EPG_SCHEDULE_EVENT_INFO;
import com.konka.kkinterface.tv.DtvInterface.RESULT_ADDEVENT;
import com.mstar.android.tvapi.common.exception.TvCommonException;
import com.mstar.android.tvapi.common.vo.EnumServiceType;
import com.mstar.android.tvapi.common.vo.EpgEventTimerInfo;
import com.mstar.android.tvapi.common.vo.PresentFollowingEventInfo;
import com.mstar.android.tvapi.common.vo.ProgramInfo;
import com.mstar.android.tvapi.common.vo.TvOsType.EnumInputSource;
import com.mstar.android.tvapi.common.vo.TvOsType.EnumTimeZone;
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

public interface EpgDesk extends BaseDesk {
    void ProgramSel(KonkaProgInfo konkaProgInfo);

    void UpdateEpgTimer(String str, String str2, int i);

    EnumEpgTimerCheck addEpgEvent(EpgEventTimerInfo epgEventTimerInfo) throws TvCommonException;

    RESULT_ADDEVENT addEpgTimerEvent(KonkaEventInfo konkaEventInfo);

    void cancelEpgTimerEvent(int i);

    void cancelEpgTimerEvent(int i, boolean z) throws TvCommonException;

    void closeDB();

    boolean delAllEpgEvent() throws TvCommonException;

    boolean delEpgEvent(int i) throws TvCommonException;

    boolean delEpgTimerEvent(int i, boolean z);

    boolean deletePastEpgTimer() throws TvCommonException;

    void deletePastScheduleInfo();

    void deleteScheduleInfoByStartTime(int i);

    boolean disableEpgBarkerChannel() throws TvCommonException;

    boolean enableEpgBarkerChannel() throws TvCommonException;

    boolean execEpgTimerAction() throws TvCommonException;

    void execEpgTimerEvent();

    ArrayList<EpgCridEventInfo> getCridAlternateList(short s, int i, Time time) throws TvCommonException;

    ArrayList<EpgCridEventInfo> getCridSeriesList(short s, int i, Time time) throws TvCommonException;

    ArrayList<EpgCridEventInfo> getCridSplitList(short s, int i, Time time) throws TvCommonException;

    EpgCridStatus getCridStatus(short s, int i, Time time) throws TvCommonException;

    Time getCurClkTime();

    EnumInputSource getCurInputSource();

    KonkaProgInfo getCurKonkaProgInfo();

    ProgramInfo getCurProgramInfo();

    KonkaProgCount getDtvProgCount();

    DtvEitInfo getEitInfo(boolean z) throws TvCommonException;

    int getEpgEventOffsetTime(Time time, boolean z) throws TvCommonException;

    ArrayList<KonkaProgInfo> getEpgSearchReslut(String str);

    EpgEventTimerInfo getEpgTimerEventByIndex(int i) throws TvCommonException;

    int getEpgTimerEventCount() throws TvCommonException;

    ArrayList<EPG_SCHEDULE_EVENT_INFO> getEpgTimerEventList();

    EpgEventTimerInfo getEpgTimerRecordingProgram() throws TvCommonException;

    EpgFirstMatchHdCast getEvent1stMatchHdBroadcast(short s, int i, Time time) throws TvCommonException;

    EpgFirstMatchHdCast getEvent1stMatchHdSimulcast(short s, int i, Time time) throws TvCommonException;

    int getEventCount(short s, int i, Time time);

    String getEventDescriptor(short s, int i, Time time, EnumEpgDescriptionType enumEpgDescriptionType) throws TvCommonException;

    String getEventDetailDescriptor(int i);

    ArrayList<EpgHdSimulcast> getEventHdSimulcast(short s, int i, Time time, short s2) throws TvCommonException;

    ArrayList<EpgEventInfo> getEventInfo(short s, int i, Time time, int i2) throws TvCommonException;

    EpgEventInfo getEventInfoById(short s, int i, short s2) throws TvCommonException;

    ArrayList<EpgCridEventInfo> getEventInfoByRctLink(EpgTrailerLinkInfo epgTrailerLinkInfo) throws TvCommonException;

    EpgEventInfo getEventInfoByTime(short s, int i, Time time) throws TvCommonException;

    ArrayList<KonkaEventInfo> getEventList(short s, int i);

    ArrayList<KonkaEventInfo> getEventListByDay(int i);

    int getNvodTimeShiftEventCount(short s, int i) throws TvCommonException;

    ArrayList<NvodEventInfo> getNvodTimeShiftEventInfo(short s, int i, int i2, EnumEpgDescriptionType enumEpgDescriptionType) throws TvCommonException;

    PresentFollowingEventInfo getPresentFollowingEventInfo(short s, int i, boolean z, EnumEpgDescriptionType enumEpgDescriptionType) throws TvCommonException;

    ArrayList<KonkaProgInfo> getProgListByServiceType(EnumServiceType enumServiceType);

    ArrayList<KonkaProgInfo> getProgListByServiceType(short s);

    ArrayList<EpgTrailerLinkInfo> getRctTrailerLink() throws TvCommonException;

    EPG_SCHEDULE_EVENT_INFO getScheduleInfoByIndex(int i);

    EnumTimeZone getTimeZone() throws TvCommonException;

    void insertScheduleInfo(String str, String str2, int i);

    EnumEpgTimerCheck isEpgTimerSettingValid(EpgEventTimerInfo epgEventTimerInfo) throws TvCommonException;

    void modifyTimerNotifyTime(int i);

    void openDB();

    void resetEpgProgramPriority() throws TvCommonException;

    void setDispalyWindow(int i, int i2, int i3, int i4);

    void setDtvInputSource();

    void setEpgProgramPriority(int i) throws TvCommonException;

    void setEpgProgramPriority(short s, int i) throws TvCommonException;

    void setScheduleNofityTime(int i);

    void updateTimerInfo(String str, String str2, int i);
}
