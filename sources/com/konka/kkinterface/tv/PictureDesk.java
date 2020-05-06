package com.konka.kkinterface.tv;

import com.konka.kkinterface.tv.DataBaseDesk.EN_MS_COLOR_TEMP;
import com.konka.kkinterface.tv.DataBaseDesk.EN_MS_Dynamic_Contrast;
import com.konka.kkinterface.tv.DataBaseDesk.EN_MS_LOCALDIMMING;
import com.konka.kkinterface.tv.DataBaseDesk.EN_MS_MPEG_NR;
import com.konka.kkinterface.tv.DataBaseDesk.EN_MS_NR;
import com.konka.kkinterface.tv.DataBaseDesk.EN_MS_PICTURE;
import com.konka.kkinterface.tv.DataBaseDesk.EN_MS_VIDEOITEM;
import com.konka.kkinterface.tv.DataBaseDesk.MAPI_VIDEO_ARC_Type;
import com.konka.kkinterface.tv.DataBaseDesk.SkinToneMode;
import com.konka.kkinterface.tv.DataBaseDesk.T_MS_COLOR_TEMPEX_DATA;
import com.mstar.android.tvapi.common.vo.VideoWindowType;

public interface PictureDesk extends BaseDesk {
    public static final int AUTOPC_END_FAILED = 3;
    public static final int AUTOPC_END_SUCESSED = 2;
    public static final int AUTOPC_START = 1;

    boolean ExecAutoPc();

    boolean ExecVideoItem(EN_MS_VIDEOITEM en_ms_videoitem, short s);

    short GetBacklight();

    short GetBacklightOfPicMode(EN_MS_PICTURE en_ms_picture);

    EN_MS_COLOR_TEMP GetColorTempIdx();

    T_MS_COLOR_TEMPEX_DATA GetColorTempPara();

    EN_MS_MPEG_NR GetMpegNR();

    EN_MS_NR GetNR();

    short GetPCClock();

    short GetPCHPos();

    short GetPCModeIndex(int i);

    short GetPCPhase();

    short GetPCVPos();

    EN_MS_PICTURE GetPictureModeIdx();

    MAPI_VIDEO_ARC_Type GetVideoArc();

    short GetVideoItem(EN_MS_VIDEOITEM en_ms_videoitem);

    boolean SetBacklight(short s);

    boolean SetColorTempIdx(EN_MS_COLOR_TEMP en_ms_color_temp);

    boolean SetColorTempPara(T_MS_COLOR_TEMPEX_DATA t_ms_color_tempex_data);

    boolean SetKTVVideoArc();

    boolean SetMpegNR(EN_MS_MPEG_NR en_ms_mpeg_nr);

    boolean SetNR(EN_MS_NR en_ms_nr);

    boolean SetPCClock(short s);

    boolean SetPCHPos(short s);

    boolean SetPCPhase(short s);

    boolean SetPCVPos(short s);

    boolean SetPictureModeIdx(EN_MS_PICTURE en_ms_picture);

    boolean SetVideoArc(MAPI_VIDEO_ARC_Type mAPI_VIDEO_ARC_Type);

    void disableBacklight();

    void enableBacklight();

    boolean enableSetBacklight(boolean z);

    int getCustomerPqRuleNumber();

    EN_MS_NR getDNR();

    boolean getDetailEnhance();

    short getDlcAverageLuma();

    int[] getDlcLumArray(int i);

    int getDynamicBLModeIdx();

    EN_MS_Dynamic_Contrast getDynamicContrast();

    int[] getDynamicContrastCurve();

    boolean getEnergyEnable();

    short getEnergyPercent();

    SkinToneMode getSkinToneMode();

    int getStatusNumberByCustomerPqRule(int i);

    boolean refreshVideoPara();

    boolean setDNR(EN_MS_NR en_ms_nr);

    boolean setDetailEnhance(boolean z);

    void setDisplayWindow(VideoWindowType videoWindowType);

    boolean setDynamicBLModeIdx(EN_MS_LOCALDIMMING en_ms_localdimming);

    boolean setDynamicContrast(EN_MS_Dynamic_Contrast eN_MS_Dynamic_Contrast);

    boolean setEnergyEnable(boolean z);

    boolean setEnergyPercent(short s);

    boolean setSkinToneMode(SkinToneMode skinToneMode);

    boolean setStatusByCustomerPqRule(int i, int i2);
}
