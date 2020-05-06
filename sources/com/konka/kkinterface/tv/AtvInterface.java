package com.konka.kkinterface.tv;

import com.mstar.android.tvapi.atv.vo.EnumAtvManualTuneMode;
import com.mstar.android.tvapi.atv.vo.EnumGetProgramCtrl;
import com.mstar.android.tvapi.atv.vo.EnumGetProgramInfo;
import com.mstar.android.tvapi.atv.vo.EnumSetProgramCtrl;
import com.mstar.android.tvapi.atv.vo.EnumSetProgramInfo;
import com.mstar.android.tvapi.common.vo.AtvSystemStandard.EnumAtvSystemStandard;
import com.mstar.android.tvapi.common.vo.EnumAvdVideoStandardType;

public interface AtvInterface {
    public static final String[] atvcolorsystem = {"PAL", "SECAM", "AUTO"};
    public static final String[] atvsoundsystem = {"BG", "DK", "I", "L", "M"};

    public static class ATV_DATABASE {
        public EnumAtvSystemStandard asys;
        public boolean block;
        public boolean bneedaft;
        public boolean bskip;
        public int plldata;
        public String sname;
        public EnumAvdVideoStandardType vsys;
    }

    public enum ATV_MANUALTUNE_MODE {
        E_MANUAL_TUNE_MODE_SEARCH_ONE_TO_UP,
        E_MANUAL_TUNE_MODE_SEARCH_ONE_TO_DOWN,
        E_MANUAL_TUNE_MODE_FINE_TUNE_ONE_FREQUENCY,
        E_MANUAL_TUNE_MODE_FINE_TUNE_UP,
        E_MANUAL_TUNE_MODE_FINE_TUNE_DOWN,
        E_MANUAL_TUNE_MODE_UNDEFINE
    }

    public static class ATV_SCAN_EVENT {
        public boolean bIsScaningEnable;
        public int u16CurScannedChannel;
        public int u16ScannedChannelNum;
        public int u32FrequencyKHz;
        public int u8Percent;
    }

    int atvGetCurrentFrequency();

    int atvGetProgramInfo(EnumGetProgramInfo enumGetProgramInfo, int i, int i2, StringBuffer stringBuffer);

    EnumAtvSystemStandard atvGetSoundSystem();

    EnumAvdVideoStandardType atvGetVideoSystem();

    boolean atvSetAutoTuningEnd();

    boolean atvSetAutoTuningPause();

    boolean atvSetAutoTuningResume();

    boolean atvSetAutoTuningStart(int i, int i2, int i3);

    int atvSetChannel(short s, boolean z);

    boolean atvSetForceSoundSystem(EnumAtvSystemStandard enumAtvSystemStandard);

    void atvSetForceVedioSystem(EnumAvdVideoStandardType enumAvdVideoStandardType);

    boolean atvSetFrequency(int i);

    void atvSetManualTuningEnd();

    boolean atvSetManualTuningStart(int i, int i2, EnumAtvManualTuneMode enumAtvManualTuneMode);

    int atvSetProgramInfo(EnumSetProgramInfo enumSetProgramInfo, int i, int i2, String str);

    String getAtvStationName(int i);

    int getCurrentChannelNumber();

    int getProgramCtrl(EnumGetProgramCtrl enumGetProgramCtrl, int i, int i2, String str);

    void sendAtvScaninfo();

    int setProgramCtrl(EnumSetProgramCtrl enumSetProgramCtrl, int i, int i2, String str);
}
