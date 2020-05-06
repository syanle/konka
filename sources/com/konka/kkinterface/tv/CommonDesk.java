package com.konka.kkinterface.tv;

import android.view.SurfaceHolder;
import com.mstar.android.tvapi.common.vo.TvOsType.EnumInputSource;
import com.mstar.android.tvapi.common.vo.TvOsType.EnumTimeZone;

public interface CommonDesk extends BaseDesk {
    public static final int Cmd_CommonVedio = 69;
    public static final int Cmd_SignalLock = 66;
    public static final int Cmd_SignalUnLock = 67;
    public static final int Cmd_SourceInfo = 65;
    public static final int Cmd_TvApkExit = 68;
    public static final int Cmd_XXX_Max = 96;
    public static final int Cmd_XXX_Min = 64;
    public static final boolean DEBUG_FLAG = true;
    public static final int SETIS_END_COMPLETE = -101;
    public static final int SETIS_START = -100;

    public static class ApplicationsInfo {
        public int enableCNTV;
    }

    public static class AudioInfo {
        public int enableCoaxial;
        public int enableSoundBox;
        public int enableSrs;
    }

    public static class BatteryInfo {
        public int enableBattery;
    }

    public static class CPUInfo {
        public int iCPUCoreType;
    }

    public static class CiCardInfo {
        public int enableCiCard;
    }

    public enum EN_SCAN_TYPE {
        E_PROGRESSIVE,
        E_INTERLACED
    }

    public enum EnumDeskEvent {
        EV_DTV_CHANNELNAME_READY,
        EV_ATV_AUTO_TUNING_SCAN_INFO,
        EV_ATV_MANUAL_TUNING_SCAN_INFO,
        EV_DTV_AUTO_TUNING_SCAN_INFO,
        EV_DTV_PROGRAM_INFO_READY,
        EV_SIGNAL_LOCK,
        EV_SIGNAL_UNLOCK,
        EV_POPUP_DIALOG,
        EV_SCREEN_SAVER_MODE,
        EV_CI_LOAD_CREDENTIAL_FAIL,
        EV_EPGTIMER_SIMULCAST,
        EV_HBBTV_STATUS_MODE,
        EV_MHEG5_STATUS_MODE,
        EV_MHEG5_RETURN_KEY,
        EV_OAD_HANDLER,
        EV_OAD_DOWNLOAD,
        EV_PVR_NOTIFY_PLAYBACK_TIME,
        EV_PVR_NOTIFY_PLAYBACK_SPEED_CHANGE,
        EV_PVR_NOTIFY_RECORD_TIME,
        EV_PVR_NOTIFY_RECORD_SIZE,
        EV_PVR_NOTIFY_RECORD_STOP,
        EV_PVR_NOTIFY_PLAYBACK_STOP,
        EV_PVR_NOTIFY_PLAYBACK_BEGIN,
        EV_PVR_NOTIFY_TIMESHIFT_OVERWRITES_BEFORE,
        EV_PVR_NOTIFY_TIMESHIFT_OVERWRITES_AFTER,
        EV_PVR_NOTIFY_OVER_RUN,
        EV_PVR_NOTIFY_USB_REMOVED,
        EV_PVR_NOTIFY_CI_PLUS_PROTECTION,
        EV_PVR_NOTIFY_PARENTAL_CONTROL,
        EV_PVR_NOTIFY_ALWAYS_TIMESHIFT_PROGRAM_READY,
        EV_PVR_NOTIFY_ALWAYS_TIMESHIFT_PROGRAM_NOTREADY,
        EV_PVR_NOTIFY_CI_PLUS_RETENTION_LIMIT_UPDATE,
        EV_DTV_AUTO_UPDATE_SCAN,
        EV_TS_CHANGE,
        EV_POPUP_SCAN_DIALOGE_LOSS_SIGNAL,
        EV_POPUP_SCAN_DIALOGE_NEW_MULTIPLEX,
        EV_POPUP_SCAN_DIALOGE_FREQUENCY_CHANGE,
        EV_RCT_PRESENCE,
        EV_CHANGE_TTX_STATUS,
        EV_DTV_PRI_COMPONENT_MISSING,
        EV_AUDIO_MODE_CHANGE,
        EV_MHEG5_EVENT_HANDLER,
        EV_OAD_TIMEOUT,
        EV_GINGA_STATUS_MODE,
        EV_HBBTV_UI_EVENT,
        EV_ATV_PROGRAM_INFO_READY
    }

    public enum EnumScreenMode {
        MSRV_DTV_SS_INVALID_SERVICE,
        MSRV_DTV_SS_NO_CI_MODULE,
        MSRV_DTV_SS_CI_PLUS_AUTHENTICATION,
        MSRV_DTV_SS_SCRAMBLED_PROGRAM,
        MSRV_DTV_SS_CH_BLOCK,
        MSRV_DTV_SS_PARENTAL_BLOCK,
        MSRV_DTV_SS_AUDIO_ONLY,
        MSRV_DTV_SS_DATA_ONLY,
        MSRV_DTV_SS_COMMON_VIDEO,
        MSRV_DTV_SS_UNSUPPORTED_FORMAT,
        MSRV_DTV_SS_INVALID_PMT,
        MSRV_DTV_SS_MAX,
        MSRV_DTV_SS_CA_NOTIFY
    }

    public enum EnumSignalProgSyncStatus {
        E_SIGNALPROC_NOSYNC,
        E_SIGNALPROC_STABLE_SUPPORT_MODE,
        E_SIGNALPROC_STABLE_UN_SUPPORT_MODE,
        E_SIGNALPROC_UNSTABLE,
        E_SIGNALPROC_AUTO_ADJUST
    }

    public static class ExternalStorageInfo {
        public int hasSDCardSlot;
    }

    public static class InputSourceInfo {
        public int sourceCountATV;
        public int sourceCountAV;
        public int sourceCountDTV;
        public int sourceCountHDMI;
        public int sourceCountSTORAGE;
        public int sourceCountVGA;
        public int sourceCountYPbPr;
    }

    public static class LocalDimmingInfo {
        public int localdimmingenable;
    }

    public enum MSRV_SIGNALPROC_SYNC_STATUS {
        MSRV_SIGNALPROC_NOSYNC,
        MSRV_SIGNALPROC_STABLE_SUPPORT_MODE,
        MSRV_SIGNALPROC_STABLE_UN_SUPPORT_MODE,
        MSRV_SIGNALPROC_UNSTABLE,
        MSRV_SIGNALPROC_AUTO_ADJUST,
        MSRV_SIGNALPROC_MAX
    }

    public static class Panel4K2K {
        public int isPanelWidth4K = 0;
    }

    public static class PanelSupportInfo {
        public int isBLAdjustableFor3D;
    }

    public static class PipInfo {
        public int enableDualView;
        public int enablePip;
    }

    public static class ST_VIDEO_INFO {
        public EN_SCAN_TYPE enScanType;
        public short s16FrameRate;
        public short s16HResolution;
        public short s16ModeIndex;
        public short s16VResolution;

        public ST_VIDEO_INFO(short hRes, short vRes, short fRate, short modeIndx, EN_SCAN_TYPE eType) {
            this.s16HResolution = hRes;
            this.s16VResolution = vRes;
            this.s16FrameRate = fRate;
            this.enScanType = eType;
            this.s16ModeIndex = modeIndx;
        }
    }

    public static class SystemBoardInfo {
        public String strBoard;
        public String strBuild;
        public String strCode;
        public String strDate;
        public String strPanelName;
        public String strPlatform;
        public String strSerial;
        public String strVersion;
        public String strVersion6m30;
        public String strVersionMboot;
    }

    public static class SystemBuildInfo {
        public String strBuild;
        public String strDate;
        public String strDateUTC;
        public String strHost;
        public String strTVVersion;
    }

    public static class WifiDeviceInfo {
        public int isDeviceInternal;
    }

    boolean ExecSetInputSource(EnumInputSource enumInputSource);

    EnumInputSource GetCurrentInputSource();

    void SetInputSource(EnumInputSource enumInputSource);

    void SetInputSource(EnumInputSource enumInputSource, boolean z);

    void disableTvosIr();

    boolean enterSleepMode(boolean z, boolean z2);

    ApplicationsInfo getApplicationsInfo();

    AudioInfo getAudioInfo();

    BatteryInfo getBatteryInfo();

    CPUInfo getCPUInfo();

    CiCardInfo getCiCardInfo();

    ExternalStorageInfo getExternalStorageInfo();

    InputSourceInfo getInputSourceInfo();

    LocalDimmingInfo getLocalDimmingInfo();

    Panel4K2K getPanel4K2K();

    PanelSupportInfo getPanelSupportInfo();

    PipInfo getPipInfo();

    int[] getSourceList();

    SystemBoardInfo getSystemBoardInfo();

    SystemBuildInfo getSystemBuildInfo();

    ST_VIDEO_INFO getVideoInfo();

    WifiDeviceInfo getWifiDeviceInfo();

    boolean isHdmiSignalMode();

    boolean isSignalStable();

    boolean isSupport3D();

    void printfE(String str);

    void printfE(String str, String str2);

    void printfI(String str);

    void printfI(String str, String str2);

    void printfV(String str);

    void printfV(String str, String str2);

    void printfW(String str);

    void printfW(String str, String str2);

    boolean setDisplayHolder(SurfaceHolder surfaceHolder);

    boolean setGpioDeviceStatus(int i, boolean z);

    void setTimeZone(EnumTimeZone enumTimeZone, boolean z);

    boolean startMsrv();
}
