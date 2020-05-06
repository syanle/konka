package com.konka.kkimplements.tv.mstar;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.SurfaceHolder;
import com.konka.kkimplements.common.IniEditor;
import com.konka.kkimplements.common.IniReader;
import com.konka.kkinterface.tv.CommonDesk;
import com.konka.kkinterface.tv.CommonDesk.ApplicationsInfo;
import com.konka.kkinterface.tv.CommonDesk.AudioInfo;
import com.konka.kkinterface.tv.CommonDesk.BatteryInfo;
import com.konka.kkinterface.tv.CommonDesk.CPUInfo;
import com.konka.kkinterface.tv.CommonDesk.CiCardInfo;
import com.konka.kkinterface.tv.CommonDesk.EN_SCAN_TYPE;
import com.konka.kkinterface.tv.CommonDesk.ExternalStorageInfo;
import com.konka.kkinterface.tv.CommonDesk.InputSourceInfo;
import com.konka.kkinterface.tv.CommonDesk.LocalDimmingInfo;
import com.konka.kkinterface.tv.CommonDesk.Panel4K2K;
import com.konka.kkinterface.tv.CommonDesk.PanelSupportInfo;
import com.konka.kkinterface.tv.CommonDesk.PipInfo;
import com.konka.kkinterface.tv.CommonDesk.ST_VIDEO_INFO;
import com.konka.kkinterface.tv.CommonDesk.SystemBoardInfo;
import com.konka.kkinterface.tv.CommonDesk.SystemBuildInfo;
import com.konka.kkinterface.tv.CommonDesk.WifiDeviceInfo;
import com.mstar.android.tvapi.atv.AtvManager;
import com.mstar.android.tvapi.common.TvManager;
import com.mstar.android.tvapi.common.exception.TvCommonException;
import com.mstar.android.tvapi.common.vo.EnumFirstServiceInputType;
import com.mstar.android.tvapi.common.vo.EnumFirstServiceType;
import com.mstar.android.tvapi.common.vo.TvOsType.EnumInputSource;
import com.mstar.android.tvapi.common.vo.TvOsType.EnumTimeZone;
import com.mstar.android.tvapi.common.vo.VideoInfo;
import com.mstar.android.tvapi.dtv.common.CaManager;
import com.mstar.android.tvapi.dtv.common.DtvManager;
import java.io.IOException;

public class CommonDeskImpl extends BaseDeskImpl implements CommonDesk {
    public static boolean bThreadIsrunning = false;
    private static CommonDesk commonService = null;
    private ApplicationsInfo applicationsInfo = null;
    private AudioInfo audioInfo = null;
    private BatteryInfo batteryInfo = null;
    /* access modifiers changed from: private */
    public boolean bforcechangesource = false;
    private CiCardInfo ciCardInfo = null;
    /* access modifiers changed from: private */
    public Context context;
    private CPUInfo cpuInfo = null;
    /* access modifiers changed from: private */
    public EnumInputSource curSourceType = EnumInputSource.E_INPUT_SOURCE_NONE;
    DeskAtvPlayerEventListener deskAtvPlayerLister;
    DeskCaEventListener deskCaLister;
    DeskCiEventListener deskCiLister;
    DeskDtvPlayerEventListener deskDtvPlayerLister;
    DeskTimerEventListener deskTimerLister;
    DeskTvEventListener deskTvLister;
    DeskTvPlayerEventListener deskTvPlayerLister;
    private ExternalStorageInfo exStorageInfo = null;
    private InputSourceInfo inputSourceInfo = null;
    private LocalDimmingInfo localdimmingInfo = null;
    private Panel4K2K panel4k2k = null;
    private PanelSupportInfo panelsupportInfo = null;
    private PipInfo pipInfo = null;
    int[] psl = null;
    private SystemBoardInfo systemBoardInfo = null;
    private SystemBuildInfo systemBuildInfo = null;
    private ST_VIDEO_INFO videoInfo = null;
    private WifiDeviceInfo wifiDeviceInfo = null;

    private CommonDeskImpl(Context context2) {
        this.context = context2;
        this.videoInfo = new ST_VIDEO_INFO(1920, 1080, 60, 12, EN_SCAN_TYPE.E_PROGRESSIVE);
        InitSourceList();
        this.deskAtvPlayerLister = DeskAtvPlayerEventListener.getInstance();
        this.deskDtvPlayerLister = DeskDtvPlayerEventListener.getInstance();
        this.deskTvPlayerLister = DeskTvPlayerEventListener.getInstance();
        this.deskTvLister = DeskTvEventListener.getInstance();
        this.deskCiLister = DeskCiEventListener.getInstance();
        this.deskCaLister = DeskCaEventListener.getInstance();
        this.deskTimerLister = DeskTimerEventListener.getInstance();
        if (TvManager.getInstance() != null) {
            TvManager.getInstance().getPlayerManager().setOnTvPlayerEventListener(this.deskTvPlayerLister);
            TvManager.getInstance().setOnTvEventListener(this.deskTvLister);
        }
        if (DtvManager.getDvbPlayerManager() != null) {
            DtvManager.getDvbPlayerManager().setOnDtvPlayerEventListener(this.deskDtvPlayerLister);
        }
        if (AtvManager.getAtvPlayerManager() != null) {
            AtvManager.getAtvPlayerManager().setOnAtvPlayerEventListener(this.deskAtvPlayerLister);
        }
        try {
            if (DtvManager.getCiManager() != null) {
                DtvManager.getCiManager().setOnCiEventListener(this.deskCiLister);
            }
        } catch (TvCommonException e) {
            e.printStackTrace();
        }
        if (CaManager.getInstance() != null) {
            CaManager.getInstance().setOnCaEventListener(this.deskCaLister);
        }
        if (TvManager.getInstance() != null && TvManager.getInstance().getTimerManager() != null && TvManager.getInstance() != null) {
            TvManager.getInstance().getTimerManager().setOnTimerEventListener(this.deskTimerLister);
        }
    }

    public static CommonDesk getInstance(Context context2) {
        if (commonService == null) {
            commonService = new CommonDeskImpl(context2);
        }
        return commonService;
    }

    public boolean setHandler(Handler handler, int index) {
        Log.d("TvApp", "setHandler:" + index);
        if (index == 0) {
            this.deskTvLister.attachHandler(handler);
            this.deskTvPlayerLister.attachHandler(handler);
        } else if (index == 1) {
            this.deskAtvPlayerLister.attachHandler(handler);
            this.deskDtvPlayerLister.attachHandler(handler);
        } else if (index == 2) {
            this.deskCiLister.attachHandler(handler);
        } else if (index == 3) {
            this.deskTimerLister.attachHandler(handler);
        } else if (index == 4) {
            this.deskCaLister.attachHandler(handler);
        }
        return super.setHandler(handler, index);
    }

    public void releaseHandler(int index) {
        Log.d("TvApp", "releaseHandler:" + index);
        while (bThreadIsrunning) {
            Log.e("TvApp", "tv System not stable!!!");
        }
        if (index == 0) {
            this.deskTvLister.releaseHandler();
            this.deskTvPlayerLister.releaseHandler();
        } else if (index == 1) {
            this.deskAtvPlayerLister.releaseHandler();
            this.deskDtvPlayerLister.releaseHandler();
        } else if (index == 2) {
            this.deskCiLister.releaseHandler();
        } else if (index == 3) {
            this.deskTimerLister.releaseHandler();
        } else if (index == 4) {
            this.deskCaLister.releaseHandler();
        }
        super.releaseHandler(index);
    }

    public EnumInputSource GetCurrentInputSource() {
        try {
            if (TvManager.getInstance() != null) {
                this.curSourceType = TvManager.getInstance().getCurrentInputSource();
            }
        } catch (TvCommonException e) {
            e.printStackTrace();
        }
        return this.curSourceType;
    }

    public void SetInputSource(EnumInputSource st) {
        this.curSourceType = st;
        Log.e("TvApp", "SetSourceType:" + this.curSourceType.toString());
        try {
            if (TvManager.getInstance() != null) {
                TvManager.getInstance().setInputSource(st);
            }
        } catch (TvCommonException e) {
            e.printStackTrace();
        }
    }

    public void SetInputSource(EnumInputSource st, boolean bWriteDB) {
        this.curSourceType = st;
        Log.e("TvApp", "SetSourceType:" + this.curSourceType.toString());
        if (bWriteDB) {
            try {
                if (TvManager.getInstance() != null) {
                    TvManager.getInstance().setInputSource(st);
                }
            } catch (TvCommonException e) {
                e.printStackTrace();
            }
        } else if (TvManager.getInstance() != null) {
            TvManager.getInstance().setInputSource(st, false, false, false);
        }
    }

    private void InitSourceList() {
        try {
            if (TvManager.getInstance() != null) {
                this.psl = TvManager.getInstance().getSourceList();
            }
        } catch (TvCommonException e) {
            e.printStackTrace();
        }
    }

    public int[] getSourceList() {
        if (this.psl != null) {
            return this.psl;
        }
        return null;
    }

    public boolean isSignalStable() {
        try {
            if (TvManager.getInstance() != null) {
                return TvManager.getInstance().getPlayerManager().isSignalStable();
            }
            return false;
        } catch (TvCommonException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean isHdmiSignalMode() {
        if (TvManager.getInstance() != null) {
            return TvManager.getInstance().getPlayerManager().isHdmiMode();
        }
        return false;
    }

    public ST_VIDEO_INFO getVideoInfo() {
        VideoInfo snvideoinfo = null;
        try {
            if (TvManager.getInstance() != null) {
                snvideoinfo = TvManager.getInstance().getPlayerManager().getVideoInfo();
            }
            this.videoInfo.s16FrameRate = (short) snvideoinfo.frameRate;
            this.videoInfo.s16HResolution = (short) snvideoinfo.hResolution;
            this.videoInfo.s16VResolution = (short) snvideoinfo.vResolution;
            this.videoInfo.s16ModeIndex = (short) snvideoinfo.modeIndex;
            this.videoInfo.enScanType = EN_SCAN_TYPE.values()[snvideoinfo.getScanType().ordinal()];
        } catch (TvCommonException e) {
            e.printStackTrace();
        }
        return this.videoInfo;
    }

    public boolean setDisplayHolder(SurfaceHolder sh) {
        try {
            if (TvManager.getInstance() != null) {
                TvManager.getInstance().getPlayerManager().setDisplay(sh);
            }
        } catch (TvCommonException e) {
            e.printStackTrace();
        }
        return true;
    }

    public void printfE(String sTag, String sMessage) {
        Log.e(sTag, sMessage);
    }

    public void printfE(String sMessage) {
        Log.e("TvApp", sMessage);
    }

    public void printfV(String sTag, String sMessage) {
        Log.v(sTag, sMessage);
    }

    public void printfV(String sMessage) {
        Log.v("TvApp", sMessage);
    }

    public void printfI(String sTag, String sMessage) {
        Log.i(sTag, sMessage);
    }

    public void printfI(String sMessage) {
        Log.i("TvApp", sMessage);
    }

    public void printfW(String sTag, String sMessage) {
        Log.w(sTag, sMessage);
    }

    public void printfW(String sMessage) {
        Log.w("TvApp", sMessage);
    }

    public boolean ExecSetInputSource(EnumInputSource st) {
        this.curSourceType = st;
        new Thread(new Runnable() {
            public void run() {
                if (CommonDeskImpl.this.getHandler(1) != null) {
                    CommonDeskImpl.this.getHandler(1).sendEmptyMessage(-100);
                    CommonDeskImpl.this.SetInputSource(CommonDeskImpl.this.curSourceType);
                    CommonDeskImpl.this.getHandler(1).sendEmptyMessage(CommonDesk.SETIS_END_COMPLETE);
                }
            }
        }).start();
        return true;
    }

    private boolean execStartMsrv(EnumInputSource st, boolean forcechangesource) {
        this.curSourceType = st;
        this.bforcechangesource = forcechangesource;
        new Thread(new Runnable() {
            public void run() {
                CommonDeskImpl.bThreadIsrunning = true;
                if (CommonDeskImpl.this.bforcechangesource) {
                    CommonDeskImpl.this.SetInputSource(CommonDeskImpl.this.curSourceType);
                }
                if (CommonDeskImpl.this.curSourceType == EnumInputSource.E_INPUT_SOURCE_ATV) {
                    ChannelDeskImpl.getChannelMgrInstance(CommonDeskImpl.this.context).changeToFirstService(EnumFirstServiceInputType.E_FIRST_SERVICE_ATV, EnumFirstServiceType.E_DEFAULT);
                } else if (CommonDeskImpl.this.curSourceType == EnumInputSource.E_INPUT_SOURCE_DTV) {
                    ChannelDeskImpl.getChannelMgrInstance(CommonDeskImpl.this.context).changeToFirstService(EnumFirstServiceInputType.E_FIRST_SERVICE_DTV, EnumFirstServiceType.E_DEFAULT);
                }
                CommonDeskImpl.bThreadIsrunning = false;
            }
        }).start();
        return true;
    }

    public boolean startMsrv() {
        EnumInputSource GetCurrentInputSource = GetCurrentInputSource();
        EnumInputSource curInputSource = EnumInputSource.values()[DataBaseDeskImpl.getDataBaseMgrInstance(this.context).queryCurInputSrc()];
        Log.d("TvApp", "5.Start Msrv------------GetCurrentInputSource:" + curInputSource);
        execStartMsrv(curInputSource, true);
        return false;
    }

    public boolean enterSleepMode(boolean bMode, boolean bNoSignalPwDn) {
        try {
            Log.d("TvApp", "enterSleepMode:" + bMode);
            if (TvManager.getInstance() != null) {
                TvManager.getInstance().enterSleepMode(bMode, bNoSignalPwDn);
            }
        } catch (TvCommonException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean setGpioDeviceStatus(int mGpio, boolean bEnable) {
        try {
            if (TvManager.getInstance() != null) {
                return TvManager.getInstance().setGpioDeviceStatus(mGpio, bEnable);
            }
        } catch (TvCommonException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void setTimeZone(EnumTimeZone timezone, boolean isSaved) {
        try {
            if (TvManager.getInstance() != null) {
                TvManager.getInstance().getTimerManager().setTimeZone(timezone, isSaved);
            }
        } catch (TvCommonException e) {
            e.printStackTrace();
        }
    }

    public void disableTvosIr() {
        try {
            if (TvManager.getInstance() != null) {
                TvManager.getInstance().disableTvosIr();
            }
        } catch (TvCommonException e) {
            e.printStackTrace();
        }
    }

    public SystemBoardInfo getSystemBoardInfo() {
        if (this.systemBoardInfo == null) {
            this.systemBoardInfo = new SystemBoardInfo();
            IniEditor iniEditor = new IniEditor();
            iniEditor.loadFile("/customercfg/model/config.ini");
            this.systemBoardInfo.strPlatform = iniEditor.getValue("MISC_BOARD_CFG:PLATFORM", "6a801");
            this.systemBoardInfo.strBoard = iniEditor.getValue("MISC_BOARD_CFG:BOARD", "LEDxx");
            iniEditor.unloadFile();
            try {
                if (TvManager.getInstance() != null) {
                    this.systemBoardInfo.strPanelName = TvManager.getInstance().getSystemPanelName();
                    this.systemBoardInfo.strVersionMboot = TvManager.getInstance().getEnvironment("MBoot_VER");
                    this.systemBoardInfo.strVersion6m30 = "No";
                } else {
                    this.systemBoardInfo.strPanelName = "panel";
                    this.systemBoardInfo.strVersionMboot = "V1.0.00";
                    this.systemBoardInfo.strVersion6m30 = "No";
                }
            } catch (TvCommonException e) {
                this.systemBoardInfo.strPanelName = "panel";
                this.systemBoardInfo.strVersionMboot = "V1.0.00";
                this.systemBoardInfo.strVersion6m30 = "No";
            }
            iniEditor.loadFile("/customercfg/model/version.ini");
            String strKey = new StringBuilder(String.valueOf(this.systemBoardInfo.strPanelName)).append(":MODEL").toString();
            this.systemBoardInfo.strSerial = iniEditor.getValue(strKey, "LED");
            String strKey2 = new StringBuilder(String.valueOf(this.systemBoardInfo.strPanelName)).append(":VERSION").toString();
            this.systemBoardInfo.strVersion = iniEditor.getValue(strKey2, "V1.0.00");
            String strKey3 = new StringBuilder(String.valueOf(this.systemBoardInfo.strPanelName)).append(":BUILD").toString();
            this.systemBoardInfo.strBuild = iniEditor.getValue(strKey3, "1234");
            String strKey4 = new StringBuilder(String.valueOf(this.systemBoardInfo.strPanelName)).append(":CODE").toString();
            this.systemBoardInfo.strCode = iniEditor.getValue(strKey4, "99000000");
            String strKey5 = new StringBuilder(String.valueOf(this.systemBoardInfo.strPanelName)).append(":DATE").toString();
            this.systemBoardInfo.strDate = iniEditor.getValue(strKey5, "00:00:00 2012-02-25");
            iniEditor.unloadFile();
        }
        System.out.println(this.systemBoardInfo.strPlatform);
        System.out.println(this.systemBoardInfo.strSerial);
        System.out.println(this.systemBoardInfo.strPanelName);
        System.out.println(this.systemBoardInfo.strVersion);
        System.out.println(this.systemBoardInfo.strBuild);
        System.out.println(this.systemBoardInfo.strCode);
        System.out.println(this.systemBoardInfo.strDate);
        return this.systemBoardInfo;
    }

    public InputSourceInfo getInputSourceInfo() {
        if (this.inputSourceInfo == null) {
            this.inputSourceInfo = new InputSourceInfo();
            IniEditor iniEditor = new IniEditor();
            iniEditor.loadFile("customercfg/model/config.ini");
            this.inputSourceInfo.sourceCountATV = Integer.valueOf(iniEditor.getValue("MISC_SYSTEM_OPTIONS:INPUT_SOURCE_ATV_COUNT", "1")).intValue();
            this.inputSourceInfo.sourceCountDTV = Integer.valueOf(iniEditor.getValue("MISC_SYSTEM_OPTIONS:INPUT_SOURCE_DTV_COUNT", "1")).intValue();
            this.inputSourceInfo.sourceCountAV = Integer.valueOf(iniEditor.getValue("MISC_SYSTEM_OPTIONS:INPUT_SOURCE_AV_COUNT", "2")).intValue();
            this.inputSourceInfo.sourceCountYPbPr = Integer.valueOf(iniEditor.getValue("MISC_SYSTEM_OPTIONS:INPUT_SOURCE_YPBPR_COUNT", "1")).intValue();
            this.inputSourceInfo.sourceCountVGA = Integer.valueOf(iniEditor.getValue("MISC_SYSTEM_OPTIONS:INPUT_SOURCE_VGA_COUNT", "1")).intValue();
            this.inputSourceInfo.sourceCountHDMI = Integer.valueOf(iniEditor.getValue("MISC_SYSTEM_OPTIONS:INPUT_SOURCE_HDMI_COUNT", "3")).intValue();
            this.inputSourceInfo.sourceCountSTORAGE = Integer.valueOf(iniEditor.getValue("MISC_SYSTEM_OPTIONS:INPUT_SOURCE_STORAGE_COUNT", "1")).intValue();
            iniEditor.unloadFile();
        }
        return this.inputSourceInfo;
    }

    public AudioInfo getAudioInfo() {
        if (this.audioInfo == null) {
            this.audioInfo = new AudioInfo();
            IniEditor iniEditor = new IniEditor();
            iniEditor.loadFile("customercfg/model/config.ini");
            this.audioInfo.enableSrs = Integer.valueOf(iniEditor.getValue("MISC_SYSTEM_OPTIONS:AUDIO_SRS_ENABLE", "1")).intValue();
            this.audioInfo.enableCoaxial = Integer.valueOf(iniEditor.getValue("MISC_SYSTEM_OPTIONS:AUDIO_COAXIAL_ENABLE", "1")).intValue();
            this.audioInfo.enableSoundBox = Integer.valueOf(iniEditor.getValue("MISC_SYSTEM_OPTIONS:SOUND_BOX_ENABLE", "1")).intValue();
        }
        return this.audioInfo;
    }

    public CiCardInfo getCiCardInfo() {
        if (this.ciCardInfo == null) {
            this.ciCardInfo = new CiCardInfo();
            IniEditor iniEditor = new IniEditor();
            iniEditor.loadFile("customercfg/model/config.ini");
            this.ciCardInfo.enableCiCard = Integer.valueOf(iniEditor.getValue("MISC_SYSTEM_OPTIONS:CICARD_ENABLE", "1")).intValue();
        }
        return this.ciCardInfo;
    }

    public BatteryInfo getBatteryInfo() {
        if (this.batteryInfo == null) {
            this.batteryInfo = new BatteryInfo();
            IniEditor iniEditor = new IniEditor();
            iniEditor.loadFile("customercfg/model/config.ini");
            this.batteryInfo.enableBattery = Integer.valueOf(iniEditor.getValue("MISC_SYSTEM_OPTIONS:BATTERY_ENABLE", "0")).intValue();
        }
        return this.batteryInfo;
    }

    public PipInfo getPipInfo() {
        if (this.pipInfo == null) {
            this.pipInfo = new PipInfo();
            IniEditor iniEditor = new IniEditor();
            iniEditor.loadFile("customercfg/model/config.ini");
            this.pipInfo.enablePip = Integer.valueOf(iniEditor.getValue("MISC_SYSTEM_OPTIONS:PIP_ENABLE", "0")).intValue();
            this.pipInfo.enableDualView = Integer.valueOf(iniEditor.getValue("MISC_SYSTEM_OPTIONS:DUALVIEW_ENABLE", "0")).intValue();
        }
        return this.pipInfo;
    }

    public SystemBuildInfo getSystemBuildInfo() {
        if (this.systemBuildInfo == null) {
            this.systemBuildInfo = new SystemBuildInfo();
            IniReader iniReader = null;
            try {
                iniReader = new IniReader("/tvcustomer/Customer/sn_build.ini");
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (iniReader != null) {
                this.systemBuildInfo.strTVVersion = iniReader.getValue("SN_BUILD", "VERSION");
                this.systemBuildInfo.strBuild = iniReader.getValue("SN_BUILD", "BUILD");
                this.systemBuildInfo.strDate = iniReader.getValue("SN_BUILD", "DATE");
                this.systemBuildInfo.strDateUTC = iniReader.getValue("SN_BUILD", "DATE_UTC");
                this.systemBuildInfo.strHost = iniReader.getValue("SN_BUILD", "HOST");
            }
        }
        return this.systemBuildInfo;
    }

    public ApplicationsInfo getApplicationsInfo() {
        if (this.applicationsInfo == null) {
            this.applicationsInfo = new ApplicationsInfo();
            IniEditor iniEditor = new IniEditor();
            iniEditor.loadFile("customercfg/model/config.ini");
            this.applicationsInfo.enableCNTV = Integer.valueOf(iniEditor.getValue("APPLICATION_OPTIONS:CNTV_ENABLE", "0")).intValue();
        }
        return this.applicationsInfo;
    }

    public LocalDimmingInfo getLocalDimmingInfo() {
        Log.d("DEBUG", "getLocalDimmingInfo1");
        if (this.localdimmingInfo == null) {
            Log.d("DEBUG", "getLocalDimmingInfo2");
            this.localdimmingInfo = new LocalDimmingInfo();
            IniEditor inieditor = new IniEditor();
            inieditor.loadFile("/customercfg/panel/panel.ini");
            this.localdimmingInfo.localdimmingenable = Integer.parseInt(inieditor.getValue("LocalDIMMING:bLocalDIMMINGEnable", "0"));
        }
        return this.localdimmingInfo;
    }

    public boolean isSupport3D() {
        IniEditor iniEditor = new IniEditor();
        iniEditor.loadFile("/customercfg/model/config.ini");
        if (Integer.parseInt(iniEditor.getValue("MISC_SYSTEM_OPTIONS:IS_SUPPORT_3D", "1")) == 1) {
            return true;
        }
        return false;
    }

    public Panel4K2K getPanel4K2K() {
        if (this.panel4k2k == null) {
            this.panel4k2k = new Panel4K2K();
            IniEditor iniEditor = new IniEditor();
            iniEditor.loadFile("/customercfg/panel/panel.ini");
            this.panel4k2k.isPanelWidth4K = Integer.valueOf(iniEditor.getValue("ursa:Toshiba4K2KEnable", "0")).intValue();
        }
        return this.panel4k2k;
    }

    public WifiDeviceInfo getWifiDeviceInfo() {
        if (this.wifiDeviceInfo == null) {
            this.wifiDeviceInfo = new WifiDeviceInfo();
            IniEditor iniEditor = new IniEditor();
            iniEditor.loadFile("customercfg/model/config.ini");
            this.wifiDeviceInfo.isDeviceInternal = Integer.valueOf(iniEditor.getValue("MISC_SYSTEM_OPTIONS:WIFIDEVICE_INTERNAL", "1")).intValue();
        }
        return this.wifiDeviceInfo;
    }

    public PanelSupportInfo getPanelSupportInfo() {
        if (this.panelsupportInfo == null) {
            this.panelsupportInfo = new PanelSupportInfo();
            IniEditor iniEditor = new IniEditor();
            iniEditor.loadFile("customercfg/panel/panel.ini");
            this.panelsupportInfo.isBLAdjustableFor3D = Integer.valueOf(iniEditor.getValue("PANEL_SUPPORT:3DBL_ADJUSTABLE", "0")).intValue();
        }
        return this.panelsupportInfo;
    }

    public CPUInfo getCPUInfo() {
        if (this.cpuInfo == null) {
            this.cpuInfo = new CPUInfo();
            IniEditor iniEditor = new IniEditor();
            iniEditor.loadFile("customercfg/model/config.ini");
            this.cpuInfo.iCPUCoreType = Integer.valueOf(iniEditor.getValue("MISC_SYSTEM_OPTIONS:CPUCORE_TYPE", "0")).intValue();
        }
        return this.cpuInfo;
    }

    public ExternalStorageInfo getExternalStorageInfo() {
        if (this.exStorageInfo == null) {
            this.exStorageInfo = new ExternalStorageInfo();
            IniEditor iniEditor = new IniEditor();
            iniEditor.loadFile("customercfg/model/config.ini");
            this.exStorageInfo.hasSDCardSlot = Integer.valueOf(iniEditor.getValue("MISC_SYSTEM_OPTIONS:SDCARD_SLOT_ENABLE", "1")).intValue();
        }
        return this.exStorageInfo;
    }
}
