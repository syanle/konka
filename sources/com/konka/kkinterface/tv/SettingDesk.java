package com.konka.kkinterface.tv;

import com.konka.kkinterface.tv.DataBaseDesk.ColorWheelMode;
import com.konka.kkinterface.tv.DataBaseDesk.EN_MS_CHANNEL_SWITCH_MODE;
import com.konka.kkinterface.tv.DataBaseDesk.EN_MS_FILM;
import com.konka.kkinterface.tv.DataBaseDesk.EN_MS_OFFLINE_DET_MODE;
import com.konka.kkinterface.tv.DataBaseDesk.SmartEnergySavingMode;
import com.mstar.android.tvapi.common.vo.CecSetting;
import com.mstar.android.tvapi.common.vo.EnumPowerOnLogoMode;
import com.mstar.android.tvapi.common.vo.EnumPowerOnMusicMode;
import com.mstar.android.tvapi.common.vo.TvOsType.EnumLanguage;

public interface SettingDesk extends BaseDesk {
    boolean ExecRestoreToDefault();

    boolean GetBlueScreenFlag();

    CecSetting GetCecStatus();

    EN_MS_CHANNEL_SWITCH_MODE GetChannelSWMode();

    short GetColorRange();

    EnumPowerOnLogoMode GetEnvironmentPowerOnLogoMode();

    EnumPowerOnMusicMode GetEnvironmentPowerOnMusicMode();

    short GetEnvironmentPowerOnMusicVolume();

    EN_MS_FILM GetFilmMode();

    EN_MS_OFFLINE_DET_MODE GetOffDetMode();

    short GetOsdDuration();

    EnumLanguage GetOsdLanguage();

    int GetSystemAutoTimeType();

    boolean SetBlueScreenFlag(boolean z);

    boolean SetCecStatus(CecSetting cecSetting);

    boolean SetChannelSWMode(EN_MS_CHANNEL_SWITCH_MODE en_ms_channel_switch_mode);

    boolean SetColorRanger(short s);

    boolean SetEnvironmentPowerOnLogoMode(EnumPowerOnLogoMode enumPowerOnLogoMode);

    boolean SetEnvironmentPowerOnMusicMode(EnumPowerOnMusicMode enumPowerOnMusicMode);

    boolean SetEnvironmentPowerOnMusicVolume(short s);

    boolean SetFilmMode(EN_MS_FILM en_ms_film);

    boolean SetOffDetMode(EN_MS_OFFLINE_DET_MODE en_ms_offline_det_mode);

    boolean SetOsdDuration(short s);

    boolean SetOsdLanguage(EnumLanguage enumLanguage);

    boolean SetSystemAutoTimeType(int i);

    boolean getBurnInMode();

    ColorWheelMode getColorWheelMode();

    int getOsdTimeoutSecond();

    boolean getScreenSaveModeStatus();

    SmartEnergySavingMode getSmartEnergySaving();

    int getStandbyNoOperation();

    boolean getStandbyNoSignal();

    boolean setColorWheelMode(ColorWheelMode colorWheelMode);

    boolean setScreenSaveModeStatus(boolean z);

    boolean setSmartEnergySaving(SmartEnergySavingMode smartEnergySavingMode);

    boolean setStandbyNoOperation(int i);

    boolean setStandbyNoSignal(boolean z);
}
