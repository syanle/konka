package com.konka.kkinterface.tv;

import android.content.Context;
import com.konka.kkinterface.tv.DataBaseDesk.MEMBER_COUNTRY;
import com.konka.kkinterface.tv.DtvInterface.MEMBER_SERVICETYPE;
import com.mstar.android.tvapi.common.exception.TvCommonException;
import com.mstar.android.tvapi.common.vo.DtvProgramSignalInfo;
import com.mstar.android.tvapi.common.vo.EnumAtvAudioModeType;
import com.mstar.android.tvapi.common.vo.EnumAvdVideoStandardType;
import com.mstar.android.tvapi.common.vo.EnumCableOperator;
import com.mstar.android.tvapi.common.vo.EnumFirstServiceInputType;
import com.mstar.android.tvapi.common.vo.EnumFirstServiceType;
import com.mstar.android.tvapi.common.vo.EnumProgramCountType;
import com.mstar.android.tvapi.common.vo.EnumProgramInfoType;
import com.mstar.android.tvapi.common.vo.EnumTeletextCommand;
import com.mstar.android.tvapi.common.vo.EnumTeletextMode;
import com.mstar.android.tvapi.common.vo.ProgramInfo;
import com.mstar.android.tvapi.common.vo.ProgramInfoQueryCriteria;
import com.mstar.android.tvapi.common.vo.TvOsType.EnumLanguage;
import com.mstar.android.tvapi.dtv.dvb.dvbc.vo.EnumChinaDvbcRegion;
import com.mstar.android.tvapi.dtv.vo.DtvAudioInfo;
import com.mstar.android.tvapi.dtv.vo.DtvSubtitleInfo;

public interface ChannelDesk extends AtvInterface, DtvInterface, BaseDesk {
    public static final int max_atv_count = 255;
    public static final int max_dtv_count = 1000;
    public static final Object msrvPlayer = null;

    public enum EN_TUNING_SCAN_TYPE {
        E_SCAN_ATV,
        E_SCAN_DTV,
        E_SCAN_ALL
    }

    public enum TV_TS_STATUS {
        E_TS_NONE,
        E_TS_ATV_MANU_TUNING_LEFT,
        E_TS_ATV_MANU_TUNING_RIGHT,
        E_TS_ATV_AUTO_TUNING,
        E_TS_ATV_SCAN_PAUSING,
        E_TS_DTV_MANU_TUNING,
        E_TS_DTV_AUTO_TUNING,
        E_TS_DTV_FULL_TUNING,
        E_TS_DTV_SCAN_PAUSING
    }

    TV_TS_STATUS GetTsStatus();

    boolean changeToFirstService(EnumFirstServiceInputType enumFirstServiceInputType, EnumFirstServiceType enumFirstServiceType);

    boolean closeSubtitle();

    boolean closeTeletext();

    int getAtvVolumeCompensation(int i);

    DtvAudioInfo getAudioInfo();

    EnumChinaDvbcRegion getChinaDvbcRegion() throws TvCommonException;

    ProgramInfo getCurrProgramInfo();

    EnumLanguage getCurrentLanguageIndex(String str);

    DtvProgramSignalInfo getCurrentSignalInformation();

    int[] getNitFrequencyByDtvRegion(EnumChinaDvbcRegion enumChinaDvbcRegion) throws TvCommonException;

    int getProgramCount(EnumProgramCountType enumProgramCountType);

    ProgramInfo getProgramInfo(ProgramInfoQueryCriteria programInfoQueryCriteria, EnumProgramInfoType enumProgramInfoType);

    ProgramInfo getProgramInfoByIndex(int i);

    String getProgramName(int i, MEMBER_SERVICETYPE member_servicetype, short s);

    EnumAtvAudioModeType getSIFMtsMode();

    DtvSubtitleInfo getSubtitleInfo();

    MEMBER_COUNTRY getSystemCountry();

    EN_TUNING_SCAN_TYPE getUserScanType();

    EnumAvdVideoStandardType getVideoStandard();

    boolean hasTeletextClockSignal() throws TvCommonException;

    boolean hasTeletextSignal();

    boolean isSignalStabled();

    boolean isTeletextDisplayed();

    boolean isTeletextSubtitleChannel() throws TvCommonException;

    boolean isTtxChannel();

    void makeSourceAtv();

    void makeSourceDtv();

    void moveProgram(int i, int i2);

    boolean openSubtitle(int i);

    boolean openTeletext(EnumTeletextMode enumTeletextMode);

    boolean programDown();

    boolean programReturn();

    boolean programSel(int i, MEMBER_SERVICETYPE member_servicetype);

    boolean programUp();

    int readTurnChannelInterval(Context context);

    boolean saveAtvProgram(int i);

    boolean sendTeletextCommand(EnumTeletextCommand enumTeletextCommand);

    boolean setAtvVolumeCompensation(int i, int i2);

    void setCableOperator(EnumCableOperator enumCableOperator);

    void setChannelChangeFreezeMode(boolean z);

    void setChinaDvbcRegion(EnumChinaDvbcRegion enumChinaDvbcRegion) throws TvCommonException;

    void setProgramName(int i, short s, String str);

    void setSystemCountry(MEMBER_COUNTRY member_country);

    void setUserScanType(EN_TUNING_SCAN_TYPE en_tuning_scan_type);

    void startAutoUpdateScan();

    boolean startQuickScan();

    void switchAudioTrack(int i);
}
