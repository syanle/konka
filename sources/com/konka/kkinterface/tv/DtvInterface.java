package com.konka.kkinterface.tv;

import com.mstar.android.tvapi.common.vo.EnumFavoriteId;
import com.mstar.android.tvapi.common.vo.EnumProgramAttribute;
import com.mstar.android.tvapi.dtv.dvb.dvbc.vo.EnumCabConstelType;
import com.mstar.android.tvapi.dtv.dvb.vo.DvbMuxInfo;
import com.mstar.android.tvapi.dtv.vo.RfInfo;
import com.mstar.android.tvapi.dtv.vo.RfInfo.EnumInfoType;
import com.umeng.common.util.g;
import java.util.ArrayList;

public interface DtvInterface {
    public static final short MAPI_SI_MAX_SERVICE_NAME = 50;
    public static final short MAX_AUD_LANG_NUM = 16;
    public static final short MAX_SERVICE_NAME = 50;
    public static final String[] dtvantentype = {"DTMB", "DVB-C", "DVB-T", "MAX"};

    public enum AUDIO_MODE {
        E_AUDIOMODE_STEREO,
        E_AUDIOMODE_LL,
        E_AUDIOMODE_RR
    }

    public enum AUDIO_TYPE {
        E_AUDIOTYPE_MPEG(0),
        E_AUDIOTYPE_AC3(1),
        E_AUDIOTYPE_AAC(2),
        E_AUDIOTYPE_AC3P(3),
        E_AUDIOTYPE_AACP(4),
        E_AUDIOTYPE_HEAAC(16),
        E_AUDIOTYPE_AAC_V2(17),
        E_AUDIOTYPE_AAC_V4(18),
        E_AUDIOTYPE_HEAAC_V2(19),
        E_AUDIOTYPE_HEAAC_V4(20);
        
        private int keyValue;

        private AUDIO_TYPE(int key) {
            this.keyValue = key;
        }

        public int key_value() {
            return this.keyValue;
        }
    }

    public static class AUD_INFO {
        public LANG_ISO639 aISOLangInfo;
        public boolean bBroadcastMixAD;
        public short u16AudPID;
        public byte u8AACProfileAndLevel;
        public byte u8AACType;
        public byte u8AudType;
    }

    public static class DTV_EIT_INFO {
        public EIT_CURRENT_EVENT_PF eitCurrentEventPf;
        public boolean present;
    }

    public static class DTV_SCAN_EVENT {
        public EN_SCAN_RET_STATUS enScanStatus = EN_SCAN_RET_STATUS.STATUS_SCAN_NONE;
        public short u16DTVSrvCount = 0;
        public short u16DataSrvCount = 0;
        public short u16RadioSrvCount = 0;
        public short u16SignalQuality = 0;
        public short u16SignalStrength = 0;
        public int u32CurrFrequency = 0;
        public int u32UsrData = 0;
        public short u8CurrRFCh = 0;
        public short u8ScanPercentageNum = 0;

        public enum EN_SCAN_RET_STATUS {
            STATUS_SCAN_NONE,
            STATUS_AUTOTUNING_PROGRESS,
            STATUS_SIGNAL_QUALITY,
            STATUS_GET_PROGRAMS,
            STATUS_SET_REGION,
            STATUS_SET_FAVORITE_REGION,
            STATUS_EXIT_TO_DL,
            STATUS_LCN_CONFLICT,
            STATUS_SCAN_END,
            STATUS_SCAN_SETFIRSTPROG_DONE
        }
    }

    public static class DTV_TRIPLE_ID {
        public int originalNetworkId;
        public int serviceId;
        public int transportStreamId;
    }

    public static class DvbcScanParam {
        public EnumCabConstelType QAM_Type;
        public short u16SymbolRate;
        public int u32NITFrequency;
    }

    public static class EIT_CURRENT_EVENT_PF {
        public short contentNibbleLevel1;
        public short contentNibbleLevel2;
        public int durationInSeconds;
        public String eventName;
        public String extendedEventItem;
        public String extendedEventText;
        boolean isEndTimeDayLightTime;
        public boolean isScrambled;
        public boolean isStartTimeDayLightTime;
        public short parentalControl;
        public short parentalObjectiveContent;
        public String shortEventText;
        long stEndTime;
        long stStartTime;
    }

    public enum EN_ANTENNA_TYPE {
        E_ROUTE_DTMB,
        E_ROUTE_DVBC,
        E_ROUTE_DVBT,
        E_ROUTE_MAX
    }

    public enum EN_EPG_DESCRIPTION_TYPE {
        E_SHORT_DESCRIPTION,
        E_DETAIL_DESCRIPTION,
        E_GUIDANCE_DESCRIPTION,
        E_NONE_DESCRIPTION,
        E_DESCRIPTION_MAX
    }

    public enum EN_SCAN_TYPE {
        E_SCAN_ATV,
        E_SCAN_DTV,
        E_SCAN_ATVDTV,
        E_SCAN_MAX
    }

    public static class EPG_CRID_EVENT_INFO {
        public EPG_EVENT_INFO eventInfo;
        public int serviceNumber;
        public short serviceType;
    }

    public static class EPG_CRID_STATUS {
        public boolean isAlternate;
        public boolean isRecommend;
        public boolean isSeries;
        public boolean isSplit;
        public short seriesCount;
    }

    public static class EPG_EVENT_INFO {
        public static final int EN_EPG_FUNC_STATUS_CRID_NOT_FOUND = 5;
        public static final int EN_EPG_FUNC_STATUS_DB_NO_CHANNEL_DB = 12;
        public static final int EN_EPG_FUNC_STATUS_DB_NO_CONNECT = 10;
        public static final int EN_EPG_FUNC_STATUS_DB_NO_LOCK = 11;
        public static final int EN_EPG_FUNC_STATUS_INVALID = 1;
        public static final int EN_EPG_FUNC_STATUS_NO_CHANNEL = 4;
        public static final int EN_EPG_FUNC_STATUS_NO_EVENT = 2;
        public static final int EN_EPG_FUNC_STATUS_NO_FUNCTION = 255;
        public static final int EN_EPG_FUNC_STATUS_NO_STRING = 3;
        public static final int EN_EPG_FUNC_STATUS_SUCCESS = 0;
        public String description;
        public int durationTime;
        public int endTime;
        public int eventId;
        public int functionStatus;
        public short genre;
        public boolean isScrambled;
        public String name;
        public int originalStartTime;
        public short parentalRating;
        public int startTime;
    }

    public static class EPG_EVENT_TIMER_INFO {
        public int checkSum;
        public int durationTime;
        public short enRepeatMode;
        public short enTimerType;
        public int eventID;
        public boolean isEndTimeBeforeStart;
        public int majorNumber;
        public int minorNumber;
        public int serviceNumber;
        public short serviceType;
        public int startTime;
    }

    public static class EPG_FIRST_MATCH_HDCAST {
        public EPG_EVENT_INFO epgEventInfoVO;
        public boolean isResolvable;
        public String serviceName;
    }

    public static class EPG_HD_SIMULCAST {
        public short count;
        public ArrayList<?> epgEventInfos;
        public boolean isResolvable;
        public boolean isSimulcast;
        public String serviceName;
    }

    public static class EPG_SCHEDULE_EVENT_INFO {
        public int eventId;
        public String eventName;
        public String progName;
        public int progNumber;
        public int repeatMode;
        public int serviceType;
        public int startTime;
        public int timerType;
    }

    public static class EPG_TRAILER_LINK_INFO {
        public int cridType;
        public short iconId;
        public char[] pEventTitle = new char[g.b];
        public char[] promotionText = new char[g.b];
        public char[] trailerCrid = new char[64];
    }

    public static class LANG_ISO639 {
        byte u8AudMode;
        byte u8AudType;
        String[] u8ISOLangInfo;
        byte u8IsValid;
    }

    public enum MEMBER_SERVICETYPE {
        E_SERVICETYPE_ATV,
        E_SERVICETYPE_DTV,
        E_SERVICETYPE_RADIO,
        E_SERVICETYPE_DATA,
        E_SERVICETYPE_UNITED_TV,
        E_SERVICETYPE_INVALID
    }

    public static class MW_DVB_PROGRAM_INFO {
        public String au8ServiceName;
        public boolean bIsCAExist;
        public boolean bIsDataBroadcastService;
        public boolean bIsDataService;
        public boolean bIsSelectable;
        public boolean bIsServiceIDOnly;
        public boolean bIsSpecialSrv;
        public boolean bIsStillPic;
        public boolean bIsVisible;
        AUD_INFO[] stAudInfo;
        public short u16LCN;
        public short u16PCRPid;
        public short u16PmtPID;
        public short u16ServiceID;
        public short u16SimuLCN;
        public byte u8NitVer;
        public short u8PatVer;
        public byte u8PmtVer;
        public byte u8RealServiceType;
        public byte u8SdtVer;
        public byte u8ServiceType;
        public byte u8ServiceTypePrio;
    }

    public static class NVOD_EVENT_INFO {
        EPG_EVENT_INFO epgEventInfo;
        DTV_TRIPLE_ID timeShiftedServiceIds;
    }

    public enum RESULT_ADDEVENT {
        E_ADDEVENT_SUCCESSS,
        E_ADDEVENT_FAIL_OF_EXIST,
        E_ADDEVENT_FAIL_OF_ATHAND,
        E_ADDEVENT_FAIL_FULL,
        E_ADDEVENT_FAIL_OVERLAY,
        E_ADDEVENT_FAIL_TIMEOUT,
        E_ADDEVENT_FAIL_OTHER_EXCEPTION
    }

    public static class ST_DTV_SPECIFIC_PROGINFO {
        public boolean m_bAD;
        public boolean m_bCCService;
        public boolean m_bHD;
        public boolean m_bInterlace;
        public boolean m_bMHEG5Service;
        public boolean m_bSubtitleService;
        public boolean m_bTTXService;
        public MEMBER_SERVICETYPE m_eServiceType;
        public VIDEO_TYPE m_eVideoType;
        public String m_sServiceName;
        public AUD_INFO m_stAudioInfo;
        public short m_u16FrameRate;
        public short m_u16Height;
        public short m_u16Width;
        public int m_u32Number;
        public byte m_u8AudioTrackNum;
        public byte m_u8SubtitleNum;
    }

    public static class ST_DVB_CHANNEL_ATTRIBUTE {
        public int u16SignalStrength;
        public boolean u8Favorite;
        public byte u8InvalidCell;
        public boolean u8IsDataServiceAvailable;
        public boolean u8IsDelete;
        public boolean u8IsLock;
        public boolean u8IsMHEGIncluded;
        public boolean u8IsMove;
        public boolean u8IsReName;
        public boolean u8IsReplaceDel;
        public boolean u8IsScramble;
        public boolean u8IsServiceIdOnly;
        public boolean u8IsSkipped;
        public boolean u8IsSpecialSrv;
        public boolean u8IsStillPicture;
        public boolean u8NumericSelectionFlag;
        public byte u8RealServiceType;
        public byte u8Region;
        public byte u8ServiceType;
        public byte u8ServiceTypePrio;
        public boolean u8UnconfirmedService;
        public byte u8VideoType;
        public boolean u8VisibleServiceFlag;
    }

    public static class ST_DVB_PROGRAMINFO {
        public short m_u16MuxTableID;
        public int m_u32ID;
        public String pServiceName;
        public ST_DVB_CHANNEL_ATTRIBUTE stCHAttribute;
        public short u16LCN;
        public short u16LCNAssignmentType;
        public short u16Number;
        public short u16PCRPID;
        public short u16PmtPID;
        public short u16ServiceID;
        public short u16SimuLCN;
        public short u16VideoPID;
    }

    public static class TIME {
        public static final int E_TIMER_BOOT_ON_TIMER = 0;
        public static final int E_TIMER_BOOT_RECORDER = 2;
        public static final int E_TIMER_BOOT_REMINDER = 1;
        public boolean autoSleepFlag;
        public boolean clockMode;
        public short eTimeZoneInfo;
        public short enOnTimeState;
        public boolean is12Hour;
        public boolean isAutoSync;
        public boolean isDaylightSaving;
        public int offTimeFlag;
        public short offTimeState;
        public short offTimerInfoHour;
        public short offTimerInfoMin;
        public int offsetTime;
        public short onTimeFlag;
        public short onTimeTvSrc;
        public int onTimerChannel;
        public short onTimerInfoHour;
        public short onTimerInfoMin;
        public short onTimerVolume;
        public int timeDataCS;
        public int timerBootMode;
    }

    public enum VIDEO_TYPE {
        E_VIDEOTYPE_NONE,
        E_VIDEOTYPE_MPEG,
        E_VIDEOTYPE_H264,
        E_VIDEOTYPE_AVS,
        E_VIDEOTYPE_VC1
    }

    boolean DtvStopScan();

    void addProgramToFavorite(EnumFavoriteId enumFavoriteId, int i, short s, int i2);

    void deleteProgramFromFavorite(EnumFavoriteId enumFavoriteId, int i, short s, int i2);

    boolean dtvAutoScan();

    boolean dtvChangeManualScanRF(short s);

    boolean dtvFullScan();

    EN_ANTENNA_TYPE dtvGetAntennaType();

    RfInfo dtvGetRFInfo(EnumInfoType enumInfoType, int i);

    boolean dtvManualScanFreq(int i);

    boolean dtvManualScanRF(short s);

    boolean dtvPauseScan();

    boolean dtvResumeScan();

    void dtvSetAntennaType(EN_ANTENNA_TYPE en_antenna_type);

    boolean dtvStartManualScan();

    boolean dtvplayCurrentProgram();

    boolean dvbcgetScanParam(DvbcScanParam dvbcScanParam);

    boolean dvbcsetScanParam(short s, EnumCabConstelType enumCabConstelType, int i, int i2, short s2);

    DvbMuxInfo getCurrentMuxInfo();

    boolean getCurrentProgramSpecificInfo(ST_DTV_SPECIFIC_PROGINFO st_dtv_specific_proginfo);

    short getMSrvDtvRoute();

    boolean getProgramAttribute(EnumProgramAttribute enumProgramAttribute, int i, short s, int i2, boolean z);

    boolean getProgramSpecificInfoByIndex(int i, ST_DTV_SPECIFIC_PROGINFO st_dtv_specific_proginfo);

    void sendDtvScaninfo();

    void setProgramAttribute(EnumProgramAttribute enumProgramAttribute, int i, short s, int i2, boolean z);

    boolean switchMSrvDtvRouteCmd(short s);
}
