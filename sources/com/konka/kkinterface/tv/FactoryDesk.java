package com.konka.kkinterface.tv;

import com.konka.kkinterface.tv.DataBaseDesk.E_ADC_SET_INDEX;
import com.konka.kkinterface.tv.DataBaseDesk.MS_NLA_SET_INDEX;
import com.mstar.android.tvapi.common.vo.TvOsType.EnumInputSource;

public interface FactoryDesk extends BaseDesk {
    public static final int AUTOTUNE_END_FAILED = 20113;
    public static final int AUTOTUNE_END_SUCESSED = 20112;
    public static final int AUTOTUNE_START = 20111;

    boolean ExecAutoADC();

    boolean changeWBParaWhenSourceChange();

    int get3DSelfAdaptiveLevel();

    int getADCBlueGain();

    int getADCBlueOffset();

    int getADCGreenGain();

    int getADCGreenOffset();

    int getADCRedGain();

    int getADCRedOffset();

    short getAEFC_43();

    short getAEFC_44();

    short getAEFC_66Bit76();

    short getAEFC_6EBit3210();

    short getAEFC_6EBit7654();

    short getAEFC_A0();

    short getAEFC_A1();

    short getAEFC_CB();

    short getAEFC_D4();

    short getAEFC_D5Bit2();

    short getAEFC_D7LowBoun();

    short getAEFC_D8Bit3210();

    short getAEFC_D9Bit0();

    E_ADC_SET_INDEX getAdcIdx();

    short getAudioDspVersion();

    int getAudioHiDevMode();

    short getAudioNrThr();

    short getAudioPrescale();

    short getAudioSifThreshold();

    String getBoardType();

    short getChinaDescramblerBox();

    String getCompileTime();

    MS_NLA_SET_INDEX getCurveType();

    short getDelayReduce();

    boolean getDtvAvAbnormalDelay();

    int getFactoryPreSetFeature();

    int getGainDistributionThr();

    boolean getLVDSenalbe();

    int getLVDSmodulation();

    int getLVDSpercentage();

    boolean getMIUenalbe();

    int getMIUmodulation();

    int getMIUpercentage();

    short getOsdV0Nonlinear();

    short getOsdV100Nonlinear();

    short getOsdV25Nonlinear();

    short getOsdV50Nonlinear();

    short getOsdV75Nonlinear();

    short getOverScanHposition();

    short getOverScanHsize();

    EnumInputSource getOverScanSourceType();

    short getOverScanVposition();

    short getOverScanVsize();

    short getPanelSwing();

    String getPanelType();

    int getPeqFoCoarse(int i);

    int getPeqFoFine(int i);

    int getPeqGain(int i);

    int getPeqQ(int i);

    int getPowerOnMode();

    String getSoftWareVersion();

    int getTestPattern();

    boolean getUartOnOff();

    short getVdDspVersion();

    short getVifAgcRef();

    boolean getVifAsiaSignalOption();

    int getVifClampGainOvNegative();

    short getVifCrKi();

    short getVifCrKp();

    boolean getVifCrKpKiAdjust();

    int getVifCrThr();

    boolean getVifOverModulation();

    short getVifTop();

    short getVifVersion();

    int getVifVgaMaximum();

    short getWatchDogMode();

    short getWbBlueGain();

    short getWbBlueOffset();

    short getWbGreenGain();

    short getWbGreenOffset();

    short getWbRedGain();

    short getWbRedOffset();

    boolean restoreToDefault();

    boolean set3DSelfAdaptiveLevel(int i);

    boolean setADCBlueGain(int i);

    boolean setADCBlueOffset(int i);

    boolean setADCGreenGain(int i);

    boolean setADCGreenOffset(int i);

    boolean setADCRedGain(int i);

    boolean setADCRedOffset(int i);

    boolean setAEFC_43(short s);

    boolean setAEFC_44(short s);

    boolean setAEFC_66Bit76(short s);

    boolean setAEFC_6EBit3210(short s);

    boolean setAEFC_6EBit7654(short s);

    boolean setAEFC_A0(short s);

    boolean setAEFC_A1(short s);

    boolean setAEFC_CB(short s);

    boolean setAEFC_D4(short s);

    boolean setAEFC_D5Bit2(short s);

    boolean setAEFC_D7LowBoun(short s);

    boolean setAEFC_D8Bit3210(short s);

    boolean setAEFC_D9Bit0(short s);

    boolean setAdcIdx(E_ADC_SET_INDEX e_adc_set_index);

    boolean setAudioDspVersion(short s);

    boolean setAudioHiDevMode(int i);

    boolean setAudioNrThr(short s);

    boolean setAudioPrescale(short s);

    boolean setAudioSifThreshold(short s);

    boolean setChinaDescramblerBox(short s);

    boolean setCurveType(MS_NLA_SET_INDEX ms_nla_set_index);

    boolean setDelayReduce(short s);

    boolean setDtvAvAbnormalDelay(boolean z);

    boolean setFactoryPreSetFeature(int i);

    boolean setGainDistributionThr(int i);

    boolean setLVDSenable(boolean z);

    boolean setLVDSmodulation(int i);

    boolean setLVDSpercentage(int i);

    boolean setMIUenable(boolean z);

    boolean setMIUmodulation(int i);

    boolean setMIUpercentage(int i);

    boolean setOsdV0Nonlinear(short s);

    boolean setOsdV100Nonlinear(short s);

    boolean setOsdV25Nonlinear(short s);

    boolean setOsdV50Nonlinear(short s);

    boolean setOsdV75Nonlinear(short s);

    boolean setOverScanHposition(short s);

    boolean setOverScanHsize(short s);

    boolean setOverScanSourceType(EnumInputSource enumInputSource);

    boolean setOverScanVposition(short s);

    boolean setOverScanVsize(short s);

    boolean setPEQ();

    boolean setPanelSwing(short s);

    boolean setPeqFoCoarse(int i, int i2);

    boolean setPeqFoFine(int i, int i2);

    boolean setPeqGain(int i, int i2);

    boolean setPeqQ(int i, int i2);

    boolean setPowerOnMode(int i);

    boolean setTestPattern(int i);

    boolean setUartOnOff(boolean z);

    boolean setVdDspVersion(short s);

    boolean setVifAgcRef(short s);

    boolean setVifAsiaSignalOption(boolean z);

    boolean setVifClampGainOvNegative(int i);

    boolean setVifCrKi(short s);

    boolean setVifCrKp(short s);

    boolean setVifCrKpKiAdjust(boolean z);

    boolean setVifCrThr(int i);

    boolean setVifOverModulation(boolean z);

    boolean setVifTop(short s);

    boolean setVifVersion(short s);

    boolean setVifVgaMaximum(int i);

    boolean setWatchDogMode(short s);

    boolean setWbBlueGain(short s);

    boolean setWbBlueOffset(short s);

    boolean setWbGreenGain(short s);

    boolean setWbGreenOffset(short s);

    boolean setWbRedGain(short s);

    boolean setWbRedOffset(short s);
}
