package com.konka.kkimplements.tv.mstar;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import com.konka.kkinterface.tv.ChannelDesk;
import com.konka.kkinterface.tv.ChannelDesk.EN_TUNING_SCAN_TYPE;
import com.konka.kkinterface.tv.ChannelDesk.TV_TS_STATUS;
import com.konka.kkinterface.tv.DataBaseDesk.MEMBER_COUNTRY;
import com.konka.kkinterface.tv.DtvInterface.DvbcScanParam;
import com.konka.kkinterface.tv.DtvInterface.EN_ANTENNA_TYPE;
import com.konka.kkinterface.tv.DtvInterface.MEMBER_SERVICETYPE;
import com.konka.kkinterface.tv.DtvInterface.ST_DTV_SPECIFIC_PROGINFO;
import com.mstar.android.tvapi.atv.AtvManager;
import com.mstar.android.tvapi.atv.vo.EnumAtvManualTuneMode;
import com.mstar.android.tvapi.atv.vo.EnumAutoScanState;
import com.mstar.android.tvapi.atv.vo.EnumGetProgramCtrl;
import com.mstar.android.tvapi.atv.vo.EnumGetProgramInfo;
import com.mstar.android.tvapi.atv.vo.EnumSetProgramCtrl;
import com.mstar.android.tvapi.atv.vo.EnumSetProgramInfo;
import com.mstar.android.tvapi.common.TvManager;
import com.mstar.android.tvapi.common.exception.TvCommonException;
import com.mstar.android.tvapi.common.vo.AtvProgramData;
import com.mstar.android.tvapi.common.vo.AtvSystemStandard.EnumAtvSystemStandard;
import com.mstar.android.tvapi.common.vo.DtvProgramSignalInfo;
import com.mstar.android.tvapi.common.vo.EnumAtvAudioModeType;
import com.mstar.android.tvapi.common.vo.EnumAudioVolumeSourceType;
import com.mstar.android.tvapi.common.vo.EnumAvdVideoStandardType;
import com.mstar.android.tvapi.common.vo.EnumCableOperator;
import com.mstar.android.tvapi.common.vo.EnumFavoriteId;
import com.mstar.android.tvapi.common.vo.EnumFirstServiceInputType;
import com.mstar.android.tvapi.common.vo.EnumFirstServiceType;
import com.mstar.android.tvapi.common.vo.EnumProgramAttribute;
import com.mstar.android.tvapi.common.vo.EnumProgramCountType;
import com.mstar.android.tvapi.common.vo.EnumProgramInfoType;
import com.mstar.android.tvapi.common.vo.EnumProgramLoopType;
import com.mstar.android.tvapi.common.vo.EnumTeletextCommand;
import com.mstar.android.tvapi.common.vo.EnumTeletextMode;
import com.mstar.android.tvapi.common.vo.ProgramInfo;
import com.mstar.android.tvapi.common.vo.ProgramInfoQueryCriteria;
import com.mstar.android.tvapi.common.vo.TvOsType.EnumCountry;
import com.mstar.android.tvapi.common.vo.TvOsType.EnumInputSource;
import com.mstar.android.tvapi.common.vo.TvOsType.EnumLanguage;
import com.mstar.android.tvapi.dtv.common.DtvManager;
import com.mstar.android.tvapi.dtv.common.SubtitleManager;
import com.mstar.android.tvapi.dtv.dvb.dvbc.vo.EnumCabConstelType;
import com.mstar.android.tvapi.dtv.dvb.dvbc.vo.EnumChinaDvbcRegion;
import com.mstar.android.tvapi.dtv.dvb.vo.DvbMuxInfo;
import com.mstar.android.tvapi.dtv.vo.DtvAudioInfo;
import com.mstar.android.tvapi.dtv.vo.DtvSubtitleInfo;
import com.mstar.android.tvapi.dtv.vo.RfInfo;
import com.mstar.android.tvapi.dtv.vo.RfInfo.EnumInfoType;

public class ChannelDeskImpl extends BaseDeskImpl implements ChannelDesk {
    private static ChannelDeskImpl channelMgrImpl;
    private Context context;
    private int curChannelNumber = 0;
    private short curDtvRoute = 0;
    DvbcScanParam dvbcsp;
    private int prevChannelNumber = 0;
    SubtitleManager sm = null;
    private EN_TUNING_SCAN_TYPE tuning_scan_type = EN_TUNING_SCAN_TYPE.E_SCAN_ALL;
    private TV_TS_STATUS tv_tuning_status = TV_TS_STATUS.E_TS_NONE;

    public static ChannelDeskImpl getChannelMgrInstance(Context context2) {
        if (channelMgrImpl == null) {
            channelMgrImpl = new ChannelDeskImpl(context2);
        }
        return channelMgrImpl;
    }

    private ChannelDeskImpl(Context context2) {
        this.context = context2;
        this.curChannelNumber = 0;
        this.dvbcsp = new DvbcScanParam();
        this.dvbcsp.u16SymbolRate = 6875;
        this.dvbcsp.QAM_Type = EnumCabConstelType.E_CAB_QAM64;
        this.dvbcsp.u32NITFrequency = 0;
        try {
            this.sm = DtvManager.getSubtitleManager();
        } catch (TvCommonException e) {
            e.printStackTrace();
        }
    }

    public void sendAtvScaninfo() {
    }

    public void sendDtvScaninfo() {
    }

    public boolean atvSetManualTuningStart(int EventIntervalMs, int Frequency, EnumAtvManualTuneMode eMode) {
        makeSourceAtv();
        if (CommonDeskImpl.getInstance(this.context).GetCurrentInputSource() != EnumInputSource.E_INPUT_SOURCE_ATV) {
            CommonDeskImpl.getInstance(this.context).SetInputSource(EnumInputSource.E_INPUT_SOURCE_ATV);
            try {
                Thread.sleep(1000);
                changeToFirstService(EnumFirstServiceInputType.E_FIRST_SERVICE_ATV, EnumFirstServiceType.E_DEFAULT);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        boolean result = false;
        try {
            result = AtvManager.getAtvScanManager().setManualTuningStart(EventIntervalMs, Frequency, eMode);
            if (eMode == EnumAtvManualTuneMode.E_MANUAL_TUNE_MODE_SEARCH_ONE_TO_UP) {
                this.tv_tuning_status = TV_TS_STATUS.E_TS_ATV_MANU_TUNING_RIGHT;
            } else if (eMode == EnumAtvManualTuneMode.E_MANUAL_TUNE_MODE_SEARCH_ONE_TO_DOWN) {
                this.tv_tuning_status = TV_TS_STATUS.E_TS_ATV_MANU_TUNING_LEFT;
            }
        } catch (TvCommonException e2) {
            e2.printStackTrace();
        }
        return result;
    }

    public boolean atvSetAutoTuningStart(int EventIntervalMs, int FrequencyStart, int FrequencyEnd) {
        Log.d("TuningService", "atvSetAutoTuningStart");
        makeSourceAtv();
        boolean result = false;
        if (CommonDeskImpl.getInstance(this.context).GetCurrentInputSource() != EnumInputSource.E_INPUT_SOURCE_ATV) {
            CommonDeskImpl.getInstance(this.context).SetInputSource(EnumInputSource.E_INPUT_SOURCE_ATV);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try {
            result = AtvManager.getAtvScanManager().setAutoTuningStart(EventIntervalMs, FrequencyStart, FrequencyEnd, EnumAutoScanState.E_NONE_NTSC_AUTO_SCAN);
            this.tv_tuning_status = TV_TS_STATUS.E_TS_ATV_AUTO_TUNING;
            return result;
        } catch (TvCommonException e2) {
            e2.printStackTrace();
            return result;
        }
    }

    public void atvSetManualTuningEnd() {
        Log.d("TuningService", "atvSetManualTuningEnd");
        this.tv_tuning_status = TV_TS_STATUS.E_TS_NONE;
        try {
            AtvManager.getAtvScanManager().setManualTuningEnd();
        } catch (TvCommonException e) {
            e.printStackTrace();
        }
    }

    public int atvGetCurrentFrequency() {
        Log.d("TuningService", "atvGetCurrentFrequency");
        int pll = 0;
        try {
            return AtvManager.getAtvScanManager().getCurrentFrequency();
        } catch (TvCommonException e) {
            e.printStackTrace();
            return pll;
        }
    }

    public boolean atvSetFrequency(int Frequency) {
        Log.d("TuningService", "atvSetFrequency!!!!!!! no such api ");
        return false;
    }

    public boolean atvSetAutoTuningPause() {
        Log.d("TuningService", "atvSetAutoTuningPause");
        boolean result = false;
        try {
            result = AtvManager.getAtvScanManager().setAutoTuningPause();
            this.tv_tuning_status = TV_TS_STATUS.E_TS_ATV_SCAN_PAUSING;
            return result;
        } catch (TvCommonException e) {
            e.printStackTrace();
            return result;
        }
    }

    public boolean atvSetAutoTuningResume() {
        Log.d("TuningService", "atvSetAutoTuningResume");
        boolean result = false;
        try {
            result = AtvManager.getAtvScanManager().setAutoTuningResume();
            this.tv_tuning_status = TV_TS_STATUS.E_TS_ATV_AUTO_TUNING;
            return result;
        } catch (TvCommonException e) {
            e.printStackTrace();
            return result;
        }
    }

    public boolean atvSetAutoTuningEnd() {
        Log.d("TuningService", "atvSetAutoTuningStart");
        boolean result = false;
        try {
            result = AtvManager.getAtvScanManager().setAutoTuningEnd();
            this.tv_tuning_status = TV_TS_STATUS.E_TS_NONE;
            return result;
        } catch (TvCommonException e) {
            e.printStackTrace();
            return result;
        }
    }

    public TV_TS_STATUS GetTsStatus() {
        return this.tv_tuning_status;
    }

    public EnumAtvSystemStandard atvGetSoundSystem() {
        int index = 0;
        try {
            index = EnumAtvSystemStandard.getOrdinalThroughValue(AtvManager.getAtvScanManager().getAtvProgramInfo(EnumGetProgramInfo.E_GET_AUDIO_STANDARD, getCurrentChannelNumber()));
        } catch (TvCommonException e) {
            e.printStackTrace();
        }
        EnumAtvSystemStandard[] values = EnumAtvSystemStandard.values();
        if (index == -1) {
            index = 0;
        }
        return values[index];
    }

    public boolean atvSetForceSoundSystem(EnumAtvSystemStandard eSoundSystem) {
        try {
            if (TvManager.getInstance() != null) {
                TvManager.getInstance().getAudioManager().setAtvSoundSystem(eSoundSystem);
            }
        } catch (TvCommonException e) {
            e.printStackTrace();
        }
        return true;
    }

    public EnumAvdVideoStandardType atvGetVideoSystem() {
        int res = 0;
        try {
            res = AtvManager.getAtvScanManager().getAtvProgramInfo(EnumGetProgramInfo.E_GET_VIDEO_STANDARD_OF_PROGRAM, getCurrentChannelNumber());
        } catch (TvCommonException e) {
            e.printStackTrace();
        }
        return EnumAvdVideoStandardType.values()[res];
    }

    public void atvSetForceVedioSystem(EnumAvdVideoStandardType eVideoSystem) {
        int curNum = getCurrentChannelNumber();
        int res = eVideoSystem.ordinal();
        try {
            AtvManager.getAtvPlayerManager().forceVideoStandard(eVideoSystem);
            AtvManager.getAtvScanManager().setAtvProgramInfo(EnumSetProgramInfo.E_SET_VIDEO_STANDARD_OF_PROGRAM, curNum, res);
        } catch (TvCommonException e) {
            e.printStackTrace();
        }
    }

    public int atvSetProgramInfo(EnumSetProgramInfo Cmd, int Program, int Param2, String str) {
        try {
            AtvManager.getAtvScanManager().setAtvProgramInfo(Cmd, Program, Param2);
        } catch (TvCommonException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int atvGetProgramInfo(EnumGetProgramInfo Cmd, int u16Program, int u16Param2, StringBuffer str) {
        int res = 0;
        try {
            return AtvManager.getAtvScanManager().getAtvProgramInfo(Cmd, u16Program);
        } catch (TvCommonException e) {
            e.printStackTrace();
            return res;
        }
    }

    public int setProgramCtrl(EnumSetProgramCtrl Cmd, int u16Param2, int u16Param3, String pVoid) {
        try {
            AtvManager.getAtvScanManager().setProgramControl(Cmd, u16Param2, u16Param3);
        } catch (TvCommonException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getProgramCtrl(EnumGetProgramCtrl Cmd, int u16Param2, int u16Param3, String pVoid) {
        int res = 0;
        try {
            return AtvManager.getAtvScanManager().getProgramControl(Cmd, u16Param2, u16Param3);
        } catch (TvCommonException e) {
            e.printStackTrace();
            return res;
        }
    }

    public int atvSetChannel(short ChannelNum, boolean bCheckBlock) {
        programSel(ChannelNum, MEMBER_SERVICETYPE.E_SERVICETYPE_ATV);
        return 0;
    }

    public int getCurrentChannelNumber() {
        int res = 0;
        try {
            if (TvManager.getInstance() != null) {
                res = TvManager.getInstance().getChannelManager().getCurrChannelNumber();
            }
            if (CommonDeskImpl.getInstance(this.context).GetCurrentInputSource() == EnumInputSource.E_INPUT_SOURCE_ATV) {
                if (res <= 255 && res >= 0) {
                    return res;
                }
                Log.d("Mapp", "getatvCurrentChannelNumber error:" + res);
                return 255;
            } else if (res <= 1000 && res >= 0) {
                return res;
            } else {
                Log.d("Mapp", "getdtvCurrentChannelNumber error:" + res);
                return ChannelDesk.max_dtv_count;
            }
        } catch (TvCommonException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public void makeSourceDtv() {
        if (CommonDeskImpl.getInstance(this.context).GetCurrentInputSource() != EnumInputSource.E_INPUT_SOURCE_DTV) {
            Log.d("Tvapp", "makeSourceDtv");
            CommonDeskImpl.getInstance(this.context).SetInputSource(EnumInputSource.E_INPUT_SOURCE_DTV);
        }
    }

    public void makeSourceAtv() {
        if (CommonDeskImpl.getInstance(this.context).GetCurrentInputSource() != EnumInputSource.E_INPUT_SOURCE_ATV) {
            Log.d("Tvapp", "makeSourceAtv");
            CommonDeskImpl.getInstance(this.context).SetInputSource(EnumInputSource.E_INPUT_SOURCE_ATV);
        }
    }

    public void dtvSetAntennaType(EN_ANTENNA_TYPE type) {
        switchMSrvDtvRouteCmd((short) type.ordinal());
    }

    public EN_ANTENNA_TYPE dtvGetAntennaType() {
        return EN_ANTENNA_TYPE.values()[getMSrvDtvRoute()];
    }

    public boolean dtvAutoScan() {
        makeSourceDtv();
        try {
            Log.d("Tvapp", "use dvbt's scaning method");
            DtvManager.getDvbtScanManager().startAutoScan();
            this.tv_tuning_status = TV_TS_STATUS.E_TS_DTV_AUTO_TUNING;
        } catch (TvCommonException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean dtvFullScan() {
        makeSourceDtv();
        try {
            DtvManager.getDvbcScanManager().startFullScan();
            this.tv_tuning_status = TV_TS_STATUS.E_TS_DTV_AUTO_TUNING;
        } catch (TvCommonException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean dtvManualScanRF(short RFNum) {
        makeSourceDtv();
        try {
            Log.d("===========================dtvManualScanRF", "RFNum:" + RFNum);
            DtvManager.getDvbPlayerManager().setManualTuneByRf(RFNum);
        } catch (TvCommonException e) {
            e.printStackTrace();
        }
        return true;
    }

    public boolean dtvManualScanFreq(int FrequencyKHz) {
        Log.d("TuningService", "dtvManualScanFreq:" + FrequencyKHz);
        makeSourceDtv();
        try {
            DtvManager.getDvbPlayerManager().setManualTuneByFreq(FrequencyKHz);
        } catch (TvCommonException e) {
            e.printStackTrace();
        }
        return true;
    }

    public boolean dtvChangeManualScanRF(short RFNum) {
        dtvManualScanRF(RFNum);
        return false;
    }

    public boolean dtvPauseScan() {
        Log.d("TuningService", "dtvPauseScan");
        try {
            DtvManager.getDvbcScanManager().pauseScan();
            this.tv_tuning_status = TV_TS_STATUS.E_TS_DTV_SCAN_PAUSING;
        } catch (TvCommonException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean dtvResumeScan() {
        Log.d("TuningService", "dtvResumeScan");
        try {
            DtvManager.getDvbcScanManager().resumeScan();
            this.tv_tuning_status = TV_TS_STATUS.E_TS_DTV_AUTO_TUNING;
        } catch (TvCommonException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean DtvStopScan() {
        Log.d("TuningService", "DtvStopScan");
        try {
            DtvManager.getDvbcScanManager().stopScan();
            this.tv_tuning_status = TV_TS_STATUS.E_TS_NONE;
        } catch (TvCommonException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean dtvStartManualScan() {
        Log.d("TuningService", "dtvStartManualScan");
        makeSourceDtv();
        try {
            DtvManager.getDvbcScanManager().startManualScan();
            this.tv_tuning_status = TV_TS_STATUS.E_TS_DTV_MANU_TUNING;
        } catch (TvCommonException e) {
            e.printStackTrace();
        }
        return false;
    }

    public RfInfo dtvGetRFInfo(EnumInfoType enInfoType, int RFChNum) {
        RfInfo result = null;
        try {
            return DtvManager.getDvbPlayerManager().getRfInfo(enInfoType, RFChNum);
        } catch (TvCommonException e) {
            e.printStackTrace();
            return result;
        }
    }

    public boolean changeToFirstService(EnumFirstServiceInputType enInputType, EnumFirstServiceType enServiceType) {
        Log.d("TuningService", "ChangeToFirstService");
        this.curChannelNumber = 0;
        try {
            if (TvManager.getInstance() != null) {
                TvManager.getInstance().getChannelManager().changeToFirstService(enInputType, enServiceType);
            }
        } catch (TvCommonException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean programUp() {
        Log.d("TuningService", "programUp");
        try {
            if (TvManager.getInstance() != null) {
                TvManager.getInstance().getChannelManager().programUp(EnumProgramLoopType.E_PROG_LOOP_ALL);
            }
        } catch (TvCommonException e) {
            e.printStackTrace();
        }
        return true;
    }

    public boolean programDown() {
        Log.d("TuningService", "programDown");
        try {
            if (TvManager.getInstance() != null) {
                TvManager.getInstance().getChannelManager().programDown(EnumProgramLoopType.E_PROG_LOOP_ALL);
            }
        } catch (TvCommonException e) {
            e.printStackTrace();
        }
        return true;
    }

    public boolean programReturn() {
        Log.d("TuningService", "programReturn");
        this.curChannelNumber = this.prevChannelNumber;
        try {
            if (TvManager.getInstance() != null) {
                TvManager.getInstance().getChannelManager().returnToPreviousProgram();
            }
        } catch (TvCommonException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean programSel(int u32Number, MEMBER_SERVICETYPE u8ServiceType) {
        Log.d("TuningService", "programSel:" + u32Number + "u8ServiceType" + u8ServiceType);
        try {
            if (TvManager.getInstance() != null) {
                TvManager.getInstance().getChannelManager().selectProgram(u32Number, (short) u8ServiceType.ordinal(), 0);
            }
        } catch (TvCommonException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean getCurrentProgramSpecificInfo(ST_DTV_SPECIFIC_PROGINFO cResult) {
        Log.d("TuningService", "getCurrentProgramSpecificInfo!!!!!!! no such api ");
        return false;
    }

    public boolean getProgramSpecificInfoByIndex(int programIndex, ST_DTV_SPECIFIC_PROGINFO cResult) {
        Log.d("TuningService", "getCurrentProgramSpecificInfo!!!!!!! no such api ");
        return false;
    }

    public ProgramInfo getCurrProgramInfo() {
        return getProgramInfo(new ProgramInfoQueryCriteria(), EnumProgramInfoType.E_INFO_CURRENT);
    }

    public ProgramInfo getProgramInfoByIndex(int programIndex) {
        ProgramInfoQueryCriteria qc = new ProgramInfoQueryCriteria();
        qc.queryIndex = programIndex;
        return getProgramInfo(qc, EnumProgramInfoType.E_INFO_DATABASE_INDEX);
    }

    public int getPogramCount(EnumProgramCountType programCountType) {
        try {
            if (TvManager.getInstance() != null) {
                return TvManager.getInstance().getChannelManager().getProgramCount(programCountType);
            }
            return 0;
        } catch (TvCommonException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public void setUserScanType(EN_TUNING_SCAN_TYPE scantype) {
        this.tuning_scan_type = scantype;
    }

    public EN_TUNING_SCAN_TYPE getUserScanType() {
        return this.tuning_scan_type;
    }

    public void setSystemCountry(MEMBER_COUNTRY mem_country) {
        Log.d("TuningService", "mem_country :" + mem_country);
        try {
            DtvManager.getDvbPlayerManager().setCountry(EnumCountry.values()[mem_country.ordinal()]);
        } catch (TvCommonException e) {
            e.printStackTrace();
        }
    }

    public MEMBER_COUNTRY getSystemCountry() {
        return DataBaseDeskImpl.getDataBaseMgrInstance(this.context).getUsrData().Country;
    }

    public boolean switchMSrvDtvRouteCmd(short u8DtvRoute) {
        Log.d("TuningService", "switchMSrvDtvRouteCmd:" + u8DtvRoute);
        makeSourceDtv();
        try {
            DtvManager.getDvbPlayerManager().switchDtvRoute(u8DtvRoute);
        } catch (TvCommonException e) {
            e.printStackTrace();
        }
        return false;
    }

    public short getMSrvDtvRoute() {
        try {
            if (TvManager.getInstance() != null) {
                this.curDtvRoute = (short) TvManager.getInstance().getCurrentDtvRoute();
            }
        } catch (TvCommonException e) {
            e.printStackTrace();
        }
        Log.d("TuningService", "getMSrvDtvRoute:" + this.curDtvRoute);
        return this.curDtvRoute;
    }

    public boolean dvbcsetScanParam(short u16SymbolRate, EnumCabConstelType enConstellation, int u32nitFrequency, int u32EndFrequncy, short u16NetworkID) {
        Log.d("TuningService", "dvbcsetScanParam:S_" + u16SymbolRate + "Q_" + enConstellation);
        makeSourceDtv();
        this.dvbcsp.u16SymbolRate = u16SymbolRate;
        this.dvbcsp.QAM_Type = enConstellation;
        this.dvbcsp.u32NITFrequency = u32nitFrequency;
        try {
            DtvManager.getDvbcScanManager().setScanParam(u16SymbolRate, enConstellation, u32nitFrequency, u32EndFrequncy, u16NetworkID, false);
        } catch (TvCommonException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean dvbcgetScanParam(DvbcScanParam sp) {
        sp.u16SymbolRate = this.dvbcsp.u16SymbolRate;
        sp.QAM_Type = this.dvbcsp.QAM_Type;
        sp.u32NITFrequency = this.dvbcsp.u32NITFrequency;
        return false;
    }

    public boolean dtvplayCurrentProgram() {
        Log.d("TuningService", "dtvplayCurrentProgram");
        try {
            DtvManager.getDvbPlayerManager().playCurrentProgram();
        } catch (TvCommonException e) {
            e.printStackTrace();
        }
        return false;
    }

    public int getProgramCount(EnumProgramCountType programCountType) {
        int pc = 0;
        try {
            if (TvManager.getInstance() != null) {
                pc = TvManager.getInstance().getChannelManager().getProgramCount(programCountType);
            }
        } catch (TvCommonException e) {
            e.printStackTrace();
        }
        Log.d("TuningService", "getProgramCount:" + pc);
        return pc;
    }

    public String getProgramName(int progNo, MEMBER_SERVICETYPE progType, short progrID) {
        Log.d("TuningService", "getProgramName:" + progNo + " " + progType);
        String progNm = null;
        try {
            if (TvManager.getInstance() != null) {
                progNm = TvManager.getInstance().getChannelManager().getProgramName(progNo, (short) progType.ordinal(), progrID);
            }
            Log.d("TuningService", "getProgramName:" + progNm);
        } catch (TvCommonException e) {
            e.printStackTrace();
        }
        return progNm;
    }

    public ProgramInfo getProgramInfo(ProgramInfoQueryCriteria criteria, EnumProgramInfoType programInfoType) {
        Log.d("TuningService", "getdtvProgramInfo");
        try {
            if (TvManager.getInstance() != null) {
                return TvManager.getInstance().getChannelManager().getProgramInfo(criteria, programInfoType);
            }
            return null;
        } catch (TvCommonException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setProgramAttribute(EnumProgramAttribute enpa, int programNo, short pt, int pd, boolean bv) {
        try {
            if (TvManager.getInstance() != null) {
                TvManager.getInstance().getChannelManager().setProgramAttribute(enpa, programNo, pt, pd, bv);
            }
        } catch (TvCommonException e) {
            e.printStackTrace();
        }
    }

    public boolean getProgramAttribute(EnumProgramAttribute enpa, int programNo, short pt, int pd, boolean bv) {
        try {
            if (TvManager.getInstance() != null) {
                return TvManager.getInstance().getChannelManager().getProgramAttribute(enpa, programNo, pt, pd);
            }
            return false;
        } catch (TvCommonException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void addProgramToFavorite(EnumFavoriteId favoriteId, int programNo, short programType, int programId) {
        try {
            if (TvManager.getInstance() != null) {
                TvManager.getInstance().getChannelManager().addProgramToFavorite(favoriteId, programNo, programType, programId);
            }
        } catch (TvCommonException e) {
            e.printStackTrace();
        }
    }

    public void deleteProgramFromFavorite(EnumFavoriteId favoriteId, int programNo, short programType, int programId) {
        try {
            if (TvManager.getInstance() != null) {
                TvManager.getInstance().getChannelManager().deleteProgramFromFavorite(favoriteId, programNo, programType, programId);
            }
        } catch (TvCommonException e) {
            e.printStackTrace();
        }
    }

    public boolean isSignalStabled() {
        boolean z = false;
        if (TvManager.getInstance() == null || TvManager.getInstance().getPlayerManager() == null) {
            return z;
        }
        try {
            return TvManager.getInstance().getPlayerManager().isSignalStable();
        } catch (TvCommonException e) {
            e.printStackTrace();
            return z;
        }
    }

    public EnumAtvAudioModeType getSIFMtsMode() {
        try {
            if (TvManager.getInstance() != null) {
                return TvManager.getInstance().getAudioManager().getAtvMtsMode();
            }
        } catch (TvCommonException e) {
            e.printStackTrace();
        }
        return EnumAtvAudioModeType.E_ATV_AUDIOMODE_MONO;
    }

    public boolean isTtxChannel() {
        boolean z = false;
        if (TvManager.getInstance() == null || TvManager.getInstance().getPlayerManager() == null) {
            return z;
        }
        try {
            return TvManager.getInstance().getPlayerManager().hasTeletextSignal();
        } catch (TvCommonException e) {
            e.printStackTrace();
            return z;
        }
    }

    public boolean openTeletext(EnumTeletextMode eMode) {
        boolean z = false;
        if (TvManager.getInstance() == null || TvManager.getInstance().getPlayerManager() == null) {
            return z;
        }
        try {
            return TvManager.getInstance().getPlayerManager().openTeletext(eMode);
        } catch (TvCommonException e) {
            e.printStackTrace();
            return z;
        }
    }

    public boolean closeTeletext() {
        boolean z = false;
        if (TvManager.getInstance() == null || TvManager.getInstance().getPlayerManager() == null) {
            return z;
        }
        try {
            return TvManager.getInstance().getPlayerManager().closeTeletext();
        } catch (TvCommonException e) {
            e.printStackTrace();
            return z;
        }
    }

    public boolean sendTeletextCommand(EnumTeletextCommand eCmd) {
        boolean z = false;
        if (TvManager.getInstance() == null || TvManager.getInstance().getPlayerManager() == null) {
            return z;
        }
        try {
            return TvManager.getInstance().getPlayerManager().sendTeletextCommand(eCmd);
        } catch (TvCommonException e) {
            e.printStackTrace();
            return z;
        }
    }

    public boolean isTeletextDisplayed() {
        boolean z = false;
        if (TvManager.getInstance() == null || TvManager.getInstance().getPlayerManager() == null) {
            return z;
        }
        try {
            return TvManager.getInstance().getPlayerManager().isTeletextDisplayed();
        } catch (TvCommonException e) {
            e.printStackTrace();
            return z;
        }
    }

    public boolean hasTeletextSignal() {
        boolean z = false;
        if (TvManager.getInstance() == null || TvManager.getInstance().getPlayerManager() == null) {
            return z;
        }
        try {
            return TvManager.getInstance().getPlayerManager().hasTeletextSignal();
        } catch (TvCommonException e) {
            e.printStackTrace();
            return z;
        }
    }

    public boolean hasTeletextClockSignal() {
        boolean z = false;
        if (TvManager.getInstance() == null || TvManager.getInstance().getPlayerManager() == null) {
            return z;
        }
        try {
            return TvManager.getInstance().getPlayerManager().hasTeletextClockSignal();
        } catch (TvCommonException e) {
            e.printStackTrace();
            return z;
        }
    }

    public boolean isTeletextSubtitleChannel() {
        boolean z = false;
        if (TvManager.getInstance() == null || TvManager.getInstance().getPlayerManager() == null) {
            return z;
        }
        try {
            return TvManager.getInstance().getPlayerManager().isTeletextSubtitleChannel();
        } catch (TvCommonException e) {
            e.printStackTrace();
            return z;
        }
    }

    public EnumAvdVideoStandardType getVideoStandard() {
        try {
            if (TvManager.getInstance() != null) {
                return TvManager.getInstance().getPlayerManager().getVideoStandard();
            }
        } catch (TvCommonException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getAtvStationName(int programNo) {
        String sn = null;
        int atvProgCount = getProgramCount(EnumProgramCountType.E_COUNT_ATV);
        if (atvProgCount == 0 || programNo >= atvProgCount) {
            Log.e("TvApp", "getAtvStationName null");
            return " ";
        }
        try {
            sn = AtvManager.getAtvScanManager().getAtvStationName(programNo);
        } catch (TvCommonException e) {
            e.printStackTrace();
        }
        return sn;
    }

    public void moveProgram(int progSourcePosition, int progTargetPosition) {
        try {
            if (TvManager.getInstance() != null) {
                TvManager.getInstance().getChannelManager().moveProgram(progSourcePosition, progTargetPosition);
            }
        } catch (TvCommonException e) {
            e.printStackTrace();
        }
    }

    public void setProgramName(int programNum, short programType, String porgramName) {
        Log.d("TvApp", "setProgramName:" + porgramName);
        try {
            if (TvManager.getInstance() != null) {
                TvManager.getInstance().getChannelManager().setProgramName(programNum, programType, 0, porgramName);
            }
        } catch (TvCommonException e) {
            e.printStackTrace();
        }
    }

    public DtvAudioInfo getAudioInfo() {
        try {
            return DtvManager.getDvbPlayerManager().getAudioInfo();
        } catch (TvCommonException e) {
            e.printStackTrace();
            return null;
        }
    }

    public EnumLanguage getCurrentLanguageIndex(String languageCode) {
        try {
            if (TvManager.getInstance() != null) {
                return TvManager.getInstance().getCurrentLanguageIndex(languageCode);
            }
            return EnumLanguage.E_CHINESE;
        } catch (TvCommonException e) {
            e.printStackTrace();
            return EnumLanguage.E_CHINESE;
        }
    }

    public void switchAudioTrack(int track) {
        try {
            DtvManager.getDvbPlayerManager().switchAudioTrack(track);
        } catch (TvCommonException e) {
            e.printStackTrace();
        }
    }

    public DtvSubtitleInfo getSubtitleInfo() {
        if (this.sm != null) {
            try {
                return this.sm.getSubtitleInfo();
            } catch (TvCommonException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public boolean openSubtitle(int index) {
        if (this.sm != null) {
            try {
                return this.sm.openSubtitle(index);
            } catch (TvCommonException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public boolean closeSubtitle() {
        if (this.sm != null) {
            try {
                return this.sm.closeSubtitle();
            } catch (TvCommonException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public DvbMuxInfo getCurrentMuxInfo() {
        try {
            return DtvManager.getDvbPlayerManager().getCurrentMuxInfo();
        } catch (TvCommonException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean saveAtvProgram(int currentProgramNo) {
        try {
            return AtvManager.getAtvPlayerManager().saveAtvProgram(currentProgramNo);
        } catch (TvCommonException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void setChannelChangeFreezeMode(boolean freezeMode) {
        try {
            AtvManager.getAtvPlayerManager().setChannelChangeFreezeMode(freezeMode);
        } catch (TvCommonException e) {
            e.printStackTrace();
        }
    }

    public void setCableOperator(EnumCableOperator cableOperators) {
        try {
            DtvManager.getDvbcScanManager().setCableOperator(cableOperators);
        } catch (TvCommonException e) {
            e.printStackTrace();
        }
    }

    public boolean startQuickScan() {
        try {
            return DtvManager.getDvbcScanManager().startQuickScan();
        } catch (TvCommonException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void startAutoUpdateScan() {
        try {
            DtvManager.getDvbcScanManager().startAutoUpdateScan();
        } catch (TvCommonException e) {
            e.printStackTrace();
        }
    }

    public DtvProgramSignalInfo getCurrentSignalInformation() {
        try {
            return DtvManager.getDvbPlayerManager().getCurrentSignalInformation();
        } catch (TvCommonException e) {
            e.printStackTrace();
            return null;
        }
    }

    public int getAtvVolumeCompensation(int programNO) {
        try {
            return AtvManager.getAtvScanManager().getAtvProgramMiscInfo(programNO).misc.eVolumeCompensation;
        } catch (TvCommonException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public boolean setAtvVolumeCompensation(int programNo, int eVolumeCompensation) {
        try {
            AtvProgramData apd = AtvManager.getAtvScanManager().getAtvProgramMiscInfo(programNo);
            apd.misc.eVolumeCompensation = (byte) eVolumeCompensation;
            boolean val = AtvManager.getAtvScanManager().setAtvProgramMiscInfo(programNo, apd);
            if (TvManager.getInstance() != null) {
                TvManager.getInstance().getAudioManager().setAudioVolume(EnumAudioVolumeSourceType.E_VOL_SOURCE_COMPENSATION, (byte) eVolumeCompensation);
            }
            SoundDeskImpl.getSoundMgrInstance(this.context).setVolume((short) SoundDeskImpl.getSoundMgrInstance(this.context).getVolume());
            return val;
        } catch (TvCommonException e) {
            e.printStackTrace();
            return false;
        }
    }

    public int readTurnChannelInterval(Context context2) {
        Uri uri = Uri.parse("content://endy.eyeguard.provider.confprovider/t_system_config/3000");
        Cursor cursor = context2.getContentResolver().query(uri, new String[]{"item_id", "item_name", "field_value1", "field_value2", "field_value3", "field_value4", "field_value5", "field_value6", "field_value7"}, null, null, "item_id");
        int turnChannelInter = 0;
        if (cursor.moveToNext()) {
            turnChannelInter = cursor.getInt(cursor.getColumnIndex("field_value4"));
        }
        cursor.close();
        return turnChannelInter;
    }

    public int[] getNitFrequencyByDtvRegion(EnumChinaDvbcRegion eRegion) throws TvCommonException {
        if (TvManager.getInstance() != null) {
            return TvManager.getInstance().getPlayerManager().getNitFrequencyByDtvRegion(eRegion);
        }
        return null;
    }

    public EnumChinaDvbcRegion getChinaDvbcRegion() throws TvCommonException {
        if (TvManager.getInstance() != null) {
            return TvManager.getInstance().getPlayerManager().getChinaDvbcRegion();
        }
        return EnumChinaDvbcRegion.E_CN_OTHERS;
    }

    public void setChinaDvbcRegion(EnumChinaDvbcRegion eRegion) throws TvCommonException {
        if (TvManager.getInstance() != null) {
            TvManager.getInstance().getPlayerManager().setChinaDvbcRegion(eRegion);
        }
    }
}
