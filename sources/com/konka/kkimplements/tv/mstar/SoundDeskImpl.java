package com.konka.kkimplements.tv.mstar;

import android.content.Context;
import android.util.Log;
import com.cyanogenmod.trebuchet.Launcher;
import com.konka.kkinterface.tv.CommonDesk;
import com.konka.kkinterface.tv.DataBaseDesk.EN_SOUND_MODE;
import com.konka.kkinterface.tv.DataBaseDesk.EN_SPDIF_MODE;
import com.konka.kkinterface.tv.DataBaseDesk.EN_SRS_SET_TYPE;
import com.konka.kkinterface.tv.DataBaseDesk.EN_SURROUND_MODE;
import com.konka.kkinterface.tv.DataBaseDesk.EnumSwitchOnOff;
import com.konka.kkinterface.tv.DataBaseDesk.HdmiAudioSource;
import com.konka.kkinterface.tv.DataBaseDesk.KK_SRS_SET;
import com.konka.kkinterface.tv.DataBaseDesk.MS_USER_SOUND_SETTING;
import com.konka.kkinterface.tv.DataBaseDesk.SPDIF_TYPE;
import com.konka.kkinterface.tv.SoundDesk;
import com.mstar.android.tvapi.common.TvManager;
import com.mstar.android.tvapi.common.exception.TvCommonException;
import com.mstar.android.tvapi.common.vo.AdvancedSoundParameter;
import com.mstar.android.tvapi.common.vo.AudioOutParameter;
import com.mstar.android.tvapi.common.vo.DtvSoundEffect;
import com.mstar.android.tvapi.common.vo.EnumAdvancedSoundParameterType;
import com.mstar.android.tvapi.common.vo.EnumAdvancedSoundSubProcessType;
import com.mstar.android.tvapi.common.vo.EnumAdvancedSoundType;
import com.mstar.android.tvapi.common.vo.EnumAtvAudioModeType;
import com.mstar.android.tvapi.common.vo.EnumAudioInputLevelSourceType;
import com.mstar.android.tvapi.common.vo.EnumAudioReturn;
import com.mstar.android.tvapi.common.vo.EnumAudioVolumeSourceType;
import com.mstar.android.tvapi.common.vo.EnumMuteStatusType;
import com.mstar.android.tvapi.common.vo.EnumSoundEffectType;
import com.mstar.android.tvapi.common.vo.EnumSoundSetParamType;
import com.mstar.android.tvapi.common.vo.EnumSpdifType;
import com.mstar.android.tvapi.common.vo.KtvInfoType.EnumKtvInfoType;
import com.mstar.android.tvapi.common.vo.MuteType.EnumMuteType;
import com.mstar.android.tvapi.common.vo.TvOsType.EnumInputSource;

public class SoundDeskImpl extends BaseDeskImpl implements SoundDesk {
    private static /* synthetic */ int[] $SWITCH_TABLE$com$konka$kkinterface$tv$DataBaseDesk$SPDIF_TYPE;
    private static /* synthetic */ int[] $SWITCH_TABLE$com$mstar$android$tvapi$common$vo$TvOsType$EnumInputSource;
    private static SoundDeskImpl soundMgrImpl = null;

    /* renamed from: com reason: collision with root package name */
    private CommonDesk f6com = null;
    private Context context;
    private DataBaseDeskImpl databaseMgr = null;
    private MS_USER_SOUND_SETTING userSoundSetting = null;

    static /* synthetic */ int[] $SWITCH_TABLE$com$konka$kkinterface$tv$DataBaseDesk$SPDIF_TYPE() {
        int[] iArr = $SWITCH_TABLE$com$konka$kkinterface$tv$DataBaseDesk$SPDIF_TYPE;
        if (iArr == null) {
            iArr = new int[SPDIF_TYPE.values().length];
            try {
                iArr[SPDIF_TYPE.MSAPI_AUD_SPDIF_NONPCM_.ordinal()] = 3;
            } catch (NoSuchFieldError e) {
            }
            try {
                iArr[SPDIF_TYPE.MSAPI_AUD_SPDIF_OFF_.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                iArr[SPDIF_TYPE.MSAPI_AUD_SPDIF_PCM_.ordinal()] = 1;
            } catch (NoSuchFieldError e3) {
            }
            $SWITCH_TABLE$com$konka$kkinterface$tv$DataBaseDesk$SPDIF_TYPE = iArr;
        }
        return iArr;
    }

    static /* synthetic */ int[] $SWITCH_TABLE$com$mstar$android$tvapi$common$vo$TvOsType$EnumInputSource() {
        int[] iArr = $SWITCH_TABLE$com$mstar$android$tvapi$common$vo$TvOsType$EnumInputSource;
        if (iArr == null) {
            iArr = new int[EnumInputSource.values().length];
            try {
                iArr[EnumInputSource.E_INPUT_SOURCE_ATV.ordinal()] = 2;
            } catch (NoSuchFieldError e) {
            }
            try {
                iArr[EnumInputSource.E_INPUT_SOURCE_CVBS.ordinal()] = 3;
            } catch (NoSuchFieldError e2) {
            }
            try {
                iArr[EnumInputSource.E_INPUT_SOURCE_CVBS2.ordinal()] = 4;
            } catch (NoSuchFieldError e3) {
            }
            try {
                iArr[EnumInputSource.E_INPUT_SOURCE_CVBS3.ordinal()] = 5;
            } catch (NoSuchFieldError e4) {
            }
            try {
                iArr[EnumInputSource.E_INPUT_SOURCE_CVBS4.ordinal()] = 6;
            } catch (NoSuchFieldError e5) {
            }
            try {
                iArr[EnumInputSource.E_INPUT_SOURCE_CVBS5.ordinal()] = 7;
            } catch (NoSuchFieldError e6) {
            }
            try {
                iArr[EnumInputSource.E_INPUT_SOURCE_CVBS6.ordinal()] = 8;
            } catch (NoSuchFieldError e7) {
            }
            try {
                iArr[EnumInputSource.E_INPUT_SOURCE_CVBS7.ordinal()] = 9;
            } catch (NoSuchFieldError e8) {
            }
            try {
                iArr[EnumInputSource.E_INPUT_SOURCE_CVBS8.ordinal()] = 10;
            } catch (NoSuchFieldError e9) {
            }
            try {
                iArr[EnumInputSource.E_INPUT_SOURCE_CVBS_MAX.ordinal()] = 11;
            } catch (NoSuchFieldError e10) {
            }
            try {
                iArr[EnumInputSource.E_INPUT_SOURCE_DTV.ordinal()] = 29;
            } catch (NoSuchFieldError e11) {
            }
            try {
                iArr[EnumInputSource.E_INPUT_SOURCE_DTV2.ordinal()] = 38;
            } catch (NoSuchFieldError e12) {
            }
            try {
                iArr[EnumInputSource.E_INPUT_SOURCE_DVI.ordinal()] = 30;
            } catch (NoSuchFieldError e13) {
            }
            try {
                iArr[EnumInputSource.E_INPUT_SOURCE_DVI2.ordinal()] = 31;
            } catch (NoSuchFieldError e14) {
            }
            try {
                iArr[EnumInputSource.E_INPUT_SOURCE_DVI3.ordinal()] = 32;
            } catch (NoSuchFieldError e15) {
            }
            try {
                iArr[EnumInputSource.E_INPUT_SOURCE_DVI4.ordinal()] = 33;
            } catch (NoSuchFieldError e16) {
            }
            try {
                iArr[EnumInputSource.E_INPUT_SOURCE_DVI_MAX.ordinal()] = 34;
            } catch (NoSuchFieldError e17) {
            }
            try {
                iArr[EnumInputSource.E_INPUT_SOURCE_HDMI.ordinal()] = 24;
            } catch (NoSuchFieldError e18) {
            }
            try {
                iArr[EnumInputSource.E_INPUT_SOURCE_HDMI2.ordinal()] = 25;
            } catch (NoSuchFieldError e19) {
            }
            try {
                iArr[EnumInputSource.E_INPUT_SOURCE_HDMI3.ordinal()] = 26;
            } catch (NoSuchFieldError e20) {
            }
            try {
                iArr[EnumInputSource.E_INPUT_SOURCE_HDMI4.ordinal()] = 27;
            } catch (NoSuchFieldError e21) {
            }
            try {
                iArr[EnumInputSource.E_INPUT_SOURCE_HDMI_MAX.ordinal()] = 28;
            } catch (NoSuchFieldError e22) {
            }
            try {
                iArr[EnumInputSource.E_INPUT_SOURCE_JPEG.ordinal()] = 37;
            } catch (NoSuchFieldError e23) {
            }
            try {
                iArr[EnumInputSource.E_INPUT_SOURCE_KTV.ordinal()] = 36;
            } catch (NoSuchFieldError e24) {
            }
            try {
                iArr[EnumInputSource.E_INPUT_SOURCE_NONE.ordinal()] = 41;
            } catch (NoSuchFieldError e25) {
            }
            try {
                iArr[EnumInputSource.E_INPUT_SOURCE_NUM.ordinal()] = 40;
            } catch (NoSuchFieldError e26) {
            }
            try {
                iArr[EnumInputSource.E_INPUT_SOURCE_SCART.ordinal()] = 21;
            } catch (NoSuchFieldError e27) {
            }
            try {
                iArr[EnumInputSource.E_INPUT_SOURCE_SCART2.ordinal()] = 22;
            } catch (NoSuchFieldError e28) {
            }
            try {
                iArr[EnumInputSource.E_INPUT_SOURCE_SCART_MAX.ordinal()] = 23;
            } catch (NoSuchFieldError e29) {
            }
            try {
                iArr[EnumInputSource.E_INPUT_SOURCE_STORAGE.ordinal()] = 35;
            } catch (NoSuchFieldError e30) {
            }
            try {
                iArr[EnumInputSource.E_INPUT_SOURCE_STORAGE2.ordinal()] = 39;
            } catch (NoSuchFieldError e31) {
            }
            try {
                iArr[EnumInputSource.E_INPUT_SOURCE_SVIDEO.ordinal()] = 12;
            } catch (NoSuchFieldError e32) {
            }
            try {
                iArr[EnumInputSource.E_INPUT_SOURCE_SVIDEO2.ordinal()] = 13;
            } catch (NoSuchFieldError e33) {
            }
            try {
                iArr[EnumInputSource.E_INPUT_SOURCE_SVIDEO3.ordinal()] = 14;
            } catch (NoSuchFieldError e34) {
            }
            try {
                iArr[EnumInputSource.E_INPUT_SOURCE_SVIDEO4.ordinal()] = 15;
            } catch (NoSuchFieldError e35) {
            }
            try {
                iArr[EnumInputSource.E_INPUT_SOURCE_SVIDEO_MAX.ordinal()] = 16;
            } catch (NoSuchFieldError e36) {
            }
            try {
                iArr[EnumInputSource.E_INPUT_SOURCE_VGA.ordinal()] = 1;
            } catch (NoSuchFieldError e37) {
            }
            try {
                iArr[EnumInputSource.E_INPUT_SOURCE_YPBPR.ordinal()] = 17;
            } catch (NoSuchFieldError e38) {
            }
            try {
                iArr[EnumInputSource.E_INPUT_SOURCE_YPBPR2.ordinal()] = 18;
            } catch (NoSuchFieldError e39) {
            }
            try {
                iArr[EnumInputSource.E_INPUT_SOURCE_YPBPR3.ordinal()] = 19;
            } catch (NoSuchFieldError e40) {
            }
            try {
                iArr[EnumInputSource.E_INPUT_SOURCE_YPBPR_MAX.ordinal()] = 20;
            } catch (NoSuchFieldError e41) {
            }
            $SWITCH_TABLE$com$mstar$android$tvapi$common$vo$TvOsType$EnumInputSource = iArr;
        }
        return iArr;
    }

    private SoundDeskImpl(Context context2) {
        this.context = context2;
        this.f6com = CommonDeskImpl.getInstance(context2);
        this.f6com.printfI("TvService", "SoundManagerImpl constructor!!");
        this.databaseMgr = DataBaseDeskImpl.getDataBaseMgrInstance(context2);
        this.userSoundSetting = this.databaseMgr.getSound();
    }

    public static SoundDeskImpl getSoundMgrInstance(Context context2) {
        if (soundMgrImpl == null) {
            soundMgrImpl = new SoundDeskImpl(context2);
        }
        return soundMgrImpl;
    }

    public boolean setSoundMode(EN_SOUND_MODE SoundMode, boolean bWriteDB) {
        this.databaseMgr.querySoundSetting();
        this.userSoundSetting = this.databaseMgr.getSound();
        DtvSoundEffect dtvSoundEff = new DtvSoundEffect();
        this.userSoundSetting.SoundMode = SoundMode;
        dtvSoundEff.eqBandNumber = (short) EN_SOUND_MODE.SOUND_MODE_NUM.ordinal();
        this.f6com.printfI("TvSoundServiceImpl", "SoundMode is" + this.userSoundSetting.SoundMode);
        this.f6com.printfI("TvSoundServiceImpl", "Value:" + this.databaseMgr.getSoundMode(SoundMode).EqBand1 + ":" + this.databaseMgr.getSoundMode(SoundMode).EqBand2 + ":" + this.databaseMgr.getSoundMode(SoundMode).EqBand3 + ":" + this.databaseMgr.getSoundMode(SoundMode).EqBand4 + ":" + this.databaseMgr.getSoundMode(SoundMode).EqBand5);
        try {
            dtvSoundEff.soundParameterEqs[0].eqLevel = this.databaseMgr.getSoundMode(SoundMode).EqBand1;
            dtvSoundEff.soundParameterEqs[1].eqLevel = this.databaseMgr.getSoundMode(SoundMode).EqBand2;
            dtvSoundEff.soundParameterEqs[2].eqLevel = this.databaseMgr.getSoundMode(SoundMode).EqBand3;
            dtvSoundEff.soundParameterEqs[3].eqLevel = this.databaseMgr.getSoundMode(SoundMode).EqBand4;
            dtvSoundEff.soundParameterEqs[4].eqLevel = this.databaseMgr.getSoundMode(SoundMode).EqBand5;
            dtvSoundEff.eqBandNumber = 5;
            if (TvManager.getInstance() != null) {
                TvManager.getInstance().getAudioManager().setBasicSoundEffect(EnumSoundEffectType.E_EQ, dtvSoundEff);
            }
        } catch (TvCommonException e) {
            this.f6com.printfE("TvSoundServiceImpl", "Set Sound Mode Exception");
            e.printStackTrace();
        }
        if (bWriteDB) {
            return this.databaseMgr.setSoundMode(this.userSoundSetting.SoundMode);
        }
        return true;
    }

    public boolean setSoundMode(EN_SOUND_MODE SoundMode) {
        return setSoundMode(SoundMode, true);
    }

    public EN_SOUND_MODE getSoundMode() {
        this.userSoundSetting.SoundMode = EN_SOUND_MODE.values()[this.databaseMgr.querySoundMode()];
        return this.userSoundSetting.SoundMode;
    }

    public boolean setBass(short BassValue) {
        this.databaseMgr.querySoundSetting();
        this.userSoundSetting = this.databaseMgr.getSound();
        DtvSoundEffect dtvSoundEff = new DtvSoundEffect();
        this.databaseMgr.getSoundMode(this.userSoundSetting.SoundMode).Bass = BassValue;
        this.f6com.printfI("TvSoundServiceImpl", "Bass is" + this.databaseMgr.getSoundMode(this.userSoundSetting.SoundMode).Bass);
        try {
            this.f6com.printfI("TvSoundServiceImpl", "Bass is$$$$$$$$" + BassValue);
            this.databaseMgr.getSoundMode(this.userSoundSetting.SoundMode).EqBand1 = BassValue;
            dtvSoundEff.soundParameterEqs[0].eqLevel = BassValue;
            dtvSoundEff.soundParameterEqs[1].eqLevel = this.databaseMgr.getSoundMode(this.userSoundSetting.SoundMode).EqBand2;
            dtvSoundEff.soundParameterEqs[2].eqLevel = this.databaseMgr.getSoundMode(this.userSoundSetting.SoundMode).EqBand3;
            dtvSoundEff.soundParameterEqs[3].eqLevel = this.databaseMgr.getSoundMode(this.userSoundSetting.SoundMode).EqBand4;
            dtvSoundEff.soundParameterEqs[4].eqLevel = this.databaseMgr.getSoundMode(this.userSoundSetting.SoundMode).EqBand5;
            dtvSoundEff.eqBandNumber = 5;
            if (TvManager.getInstance() != null) {
                TvManager.getInstance().getAudioManager().setBasicSoundEffect(EnumSoundEffectType.E_EQ, dtvSoundEff);
            }
        } catch (TvCommonException e) {
            this.f6com.printfE("TvSoundServiceImpl", "Set Sound Mode Exception");
            e.printStackTrace();
        }
        this.databaseMgr.updateSoundModeSetting(this.databaseMgr.getSoundMode(this.userSoundSetting.SoundMode), this.userSoundSetting.SoundMode.ordinal());
        return true;
    }

    public short getBass() {
        return this.databaseMgr.getSoundMode(this.userSoundSetting.SoundMode).Bass;
    }

    public boolean setTreble(short TrebleValue) {
        this.databaseMgr.querySoundSetting();
        this.userSoundSetting = this.databaseMgr.getSound();
        DtvSoundEffect dtvSoundEff = new DtvSoundEffect();
        this.databaseMgr.getSoundMode(this.userSoundSetting.SoundMode).Treble = TrebleValue;
        this.f6com.printfI("TvSoundServiceImpl", "Treble is" + this.databaseMgr.getSoundMode(this.userSoundSetting.SoundMode).Treble);
        try {
            dtvSoundEff.soundParameterEqs[0].eqLevel = this.databaseMgr.getSoundMode(this.userSoundSetting.SoundMode).EqBand1;
            dtvSoundEff.soundParameterEqs[1].eqLevel = this.databaseMgr.getSoundMode(this.userSoundSetting.SoundMode).EqBand2;
            dtvSoundEff.soundParameterEqs[2].eqLevel = this.databaseMgr.getSoundMode(this.userSoundSetting.SoundMode).EqBand3;
            dtvSoundEff.soundParameterEqs[3].eqLevel = this.databaseMgr.getSoundMode(this.userSoundSetting.SoundMode).EqBand4;
            this.databaseMgr.getSoundMode(this.userSoundSetting.SoundMode).EqBand5 = TrebleValue;
            dtvSoundEff.soundParameterEqs[4].eqLevel = TrebleValue;
            dtvSoundEff.eqBandNumber = 5;
            if (TvManager.getInstance() != null) {
                TvManager.getInstance().getAudioManager().setBasicSoundEffect(EnumSoundEffectType.E_EQ, dtvSoundEff);
            }
        } catch (TvCommonException e) {
            this.f6com.printfE("TvSoundServiceImpl", "Set Sound Mode Exception");
            e.printStackTrace();
        }
        this.databaseMgr.updateSoundModeSetting(this.databaseMgr.getSoundMode(this.userSoundSetting.SoundMode), this.userSoundSetting.SoundMode.ordinal());
        return true;
    }

    public short getTreble() {
        return this.databaseMgr.getSoundMode(this.userSoundSetting.SoundMode).Treble;
    }

    public boolean setBalance(short BalanceValue) {
        this.databaseMgr.querySoundSetting();
        this.userSoundSetting = this.databaseMgr.getSound();
        DtvSoundEffect dtvSoundEff = new DtvSoundEffect();
        this.userSoundSetting.Balance = BalanceValue;
        this.f6com.printfI("TvSoundServiceImpl", "Set Balance is" + this.userSoundSetting.Balance);
        try {
            dtvSoundEff.balance = BalanceValue;
            if (TvManager.getInstance() != null) {
                TvManager.getInstance().getAudioManager().setBasicSoundEffect(EnumSoundEffectType.E_BALANCE, dtvSoundEff);
            }
        } catch (TvCommonException e) {
            this.f6com.printfE("TvSoundServiceImpl", "Set Balance Exception");
            e.printStackTrace();
        }
        this.databaseMgr.updateSoundSetting(this.userSoundSetting);
        return true;
    }

    public short getBalance() {
        this.f6com.printfI("TvSoundServiceImpl", "Get Balance is" + this.userSoundSetting.Balance);
        return this.userSoundSetting.Balance;
    }

    public boolean setAVCMode(boolean AvcEnable) {
        this.databaseMgr.querySoundSetting();
        this.userSoundSetting = this.databaseMgr.getSound();
        this.userSoundSetting.bEnableAVC = AvcEnable;
        this.f6com.printfI("TvSoundServiceImpl", "bEnableAVC is" + this.userSoundSetting.bEnableAVC);
        this.databaseMgr.updateSoundSetting(this.userSoundSetting);
        try {
            if (TvManager.getInstance() != null) {
                TvManager.getInstance().getAudioManager().enableBasicSoundEffect(EnumSoundEffectType.E_AVC, AvcEnable);
            }
        } catch (TvCommonException e) {
            this.f6com.printfE("TvSoundServiceImpl", "Set Balance Exception");
            e.printStackTrace();
        }
        return true;
    }

    public boolean getAVCMode() {
        return this.userSoundSetting.bEnableAVC;
    }

    public boolean setSurroundMode(EN_SURROUND_MODE SurroundMode) {
        boolean bEnable;
        this.databaseMgr.querySoundSetting();
        this.userSoundSetting = this.databaseMgr.getSound();
        this.userSoundSetting.SurroundMode = SurroundMode;
        this.f6com.printfI("TvSoundServiceImpl", "SurroundMode is" + this.userSoundSetting.SurroundMode);
        if (SurroundMode == EN_SURROUND_MODE.E_SURROUND_MODE_OFF) {
            bEnable = false;
        } else {
            bEnable = true;
        }
        try {
            if (TvManager.getInstance() != null) {
                TvManager.getInstance().getAudioManager().enableBasicSoundEffect(EnumSoundEffectType.E_Surround, bEnable);
            }
        } catch (TvCommonException e) {
            this.f6com.printfE("TvSoundServiceImpl", "Set Balance Exception");
            e.printStackTrace();
        }
        this.databaseMgr.updateSoundSetting(this.userSoundSetting);
        return true;
    }

    public EN_SURROUND_MODE getSurroundMode() {
        return this.userSoundSetting.SurroundMode;
    }

    public boolean setSpdifOutMode(EN_SPDIF_MODE SpdifMode) {
        EnumSpdifType mode;
        SPDIF_TYPE type;
        this.databaseMgr.querySoundSetting();
        this.userSoundSetting = this.databaseMgr.getSound();
        this.userSoundSetting.spdifMode = SpdifMode;
        new AudioOutParameter().setspdifOutModeInUi(EnumSpdifType.values()[SpdifMode.ordinal()]);
        if (SpdifMode == EN_SPDIF_MODE.PDIF_MODE_PCM) {
            mode = EnumSpdifType.E_PCM;
            type = SPDIF_TYPE.MSAPI_AUD_SPDIF_PCM_;
        } else if (SpdifMode == EN_SPDIF_MODE.PDIF_MODE_RAW) {
            mode = EnumSpdifType.E_NONPCM;
            type = SPDIF_TYPE.MSAPI_AUD_SPDIF_NONPCM_;
        } else {
            mode = EnumSpdifType.E_OFF;
            type = SPDIF_TYPE.MSAPI_AUD_SPDIF_OFF_;
        }
        this.databaseMgr.setSpdifMode(type);
        try {
            if (TvManager.getInstance() != null) {
                TvManager.getInstance().getAudioManager().setDigitalOut(mode);
            }
        } catch (TvCommonException e) {
            this.f6com.printfE("TvSoundServiceImpl", "Set Volume Exception");
            e.printStackTrace();
        }
        return true;
    }

    public EN_SPDIF_MODE getSpdifOutMode() {
        EN_SPDIF_MODE mode = EN_SPDIF_MODE.PDIF_MODE_OFF;
        switch ($SWITCH_TABLE$com$konka$kkinterface$tv$DataBaseDesk$SPDIF_TYPE()[this.databaseMgr.getSpdifMode().ordinal()]) {
            case 1:
                return EN_SPDIF_MODE.PDIF_MODE_PCM;
            case 2:
                return EN_SPDIF_MODE.PDIF_MODE_OFF;
            case 3:
                return EN_SPDIF_MODE.PDIF_MODE_RAW;
            default:
                return mode;
        }
    }

    public boolean setEqBand120(short eqValue) {
        this.databaseMgr.querySoundSetting();
        this.userSoundSetting = this.databaseMgr.getSound();
        this.databaseMgr.getSoundMode(this.userSoundSetting.SoundMode).EqBand1 = eqValue;
        this.f6com.printfI("TvSoundServiceImpl", "EqBand1 is" + this.databaseMgr.getSoundMode(this.userSoundSetting.SoundMode).EqBand1);
        setSoundMode(this.userSoundSetting.SoundMode);
        this.databaseMgr.updateSoundModeSetting(this.databaseMgr.getSoundMode(this.userSoundSetting.SoundMode), this.userSoundSetting.SoundMode.ordinal());
        return true;
    }

    public short getEqBand120() {
        return this.databaseMgr.getSoundMode(this.userSoundSetting.SoundMode).EqBand1;
    }

    public boolean setEqBand500(short eqValue) {
        this.databaseMgr.querySoundSetting();
        this.userSoundSetting = this.databaseMgr.getSound();
        this.databaseMgr.getSoundMode(this.userSoundSetting.SoundMode).EqBand2 = eqValue;
        this.f6com.printfI("TvSoundServiceImpl", "EqBand2 is" + this.databaseMgr.getSoundMode(this.userSoundSetting.SoundMode).EqBand2);
        setSoundMode(this.userSoundSetting.SoundMode);
        this.databaseMgr.updateSoundModeSetting(this.databaseMgr.getSoundMode(this.userSoundSetting.SoundMode), this.userSoundSetting.SoundMode.ordinal());
        return true;
    }

    public short getEqBand500() {
        return this.databaseMgr.getSoundMode(this.userSoundSetting.SoundMode).EqBand2;
    }

    public boolean setEqBand1500(short eqValue) {
        this.databaseMgr.querySoundSetting();
        this.userSoundSetting = this.databaseMgr.getSound();
        this.databaseMgr.getSoundMode(this.userSoundSetting.SoundMode).EqBand3 = eqValue;
        this.f6com.printfI("TvSoundServiceImpl", "EqBand3 is" + this.databaseMgr.getSoundMode(this.userSoundSetting.SoundMode).EqBand3);
        setSoundMode(this.userSoundSetting.SoundMode);
        this.databaseMgr.updateSoundModeSetting(this.databaseMgr.getSoundMode(this.userSoundSetting.SoundMode), this.userSoundSetting.SoundMode.ordinal());
        return true;
    }

    public short getEqBand1500() {
        return this.databaseMgr.getSoundMode(this.userSoundSetting.SoundMode).EqBand3;
    }

    public boolean setEqBand5k(short eqValue) {
        this.databaseMgr.querySoundSetting();
        this.userSoundSetting = this.databaseMgr.getSound();
        this.databaseMgr.getSoundMode(this.userSoundSetting.SoundMode).EqBand4 = eqValue;
        this.f6com.printfI("TvSoundServiceImpl", "EqBand4 is" + this.databaseMgr.getSoundMode(this.userSoundSetting.SoundMode).EqBand4);
        setSoundMode(this.userSoundSetting.SoundMode);
        this.databaseMgr.updateSoundModeSetting(this.databaseMgr.getSoundMode(this.userSoundSetting.SoundMode), this.userSoundSetting.SoundMode.ordinal());
        return true;
    }

    public short getEqBand5k() {
        return this.databaseMgr.getSoundMode(this.userSoundSetting.SoundMode).EqBand4;
    }

    public boolean setEqBand10k(short eqValue) {
        this.databaseMgr.querySoundSetting();
        this.userSoundSetting = this.databaseMgr.getSound();
        this.databaseMgr.getSoundMode(this.userSoundSetting.SoundMode).EqBand5 = eqValue;
        this.f6com.printfI("TvSoundServiceImpl", "EqBand5 is" + this.databaseMgr.getSoundMode(this.userSoundSetting.SoundMode).EqBand5);
        setSoundMode(this.userSoundSetting.SoundMode);
        this.databaseMgr.updateSoundModeSetting(this.databaseMgr.getSoundMode(this.userSoundSetting.SoundMode), this.userSoundSetting.SoundMode.ordinal());
        return true;
    }

    public short getEqBand10k() {
        return this.databaseMgr.getSoundMode(this.userSoundSetting.SoundMode).EqBand5;
    }

    public boolean setVolume(short volume) {
        this.databaseMgr.querySoundSetting();
        this.userSoundSetting = this.databaseMgr.getSound();
        this.userSoundSetting.Volume = volume;
        this.f6com.printfI("TvSoundServiceImpl", "setVolume is" + this.userSoundSetting.Volume);
        try {
            if (TvManager.getInstance() != null) {
                TvManager.getInstance().getAudioManager().setAudioVolume(EnumAudioVolumeSourceType.E_VOL_SOURCE_SPEAKER_OUT, (byte) volume);
                TvManager.getInstance().getAudioManager().setAudioVolume(EnumAudioVolumeSourceType.E_VOL_SOURCE_HP_OUT, (byte) volume);
            }
        } catch (TvCommonException e) {
            this.f6com.printfE("TvSoundServiceImpl", "Set Volume Exception");
            e.printStackTrace();
        }
        this.databaseMgr.updateSoundSetting(this.userSoundSetting);
        return true;
    }

    public short getVolume() {
        this.userSoundSetting = this.databaseMgr.querySoundSetting();
        Log.d("TvSoundServiceImpl", "getVolume is" + this.userSoundSetting.Volume);
        return this.userSoundSetting.Volume;
    }

    public boolean getMuteFlag() {
        try {
            if (TvManager.getInstance() != null) {
                return TvManager.getInstance().getAudioManager().isMuteEnabled(EnumMuteStatusType.MUTE_STATUS_BBYUSERAUDIOMUTE);
            }
        } catch (TvCommonException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean setMuteFlag(boolean muteFlag) {
        if (muteFlag) {
            try {
                if (TvManager.getInstance() != null) {
                    TvManager.getInstance().getAudioManager().enableMute(EnumMuteType.E_BYUSER);
                }
            } catch (TvCommonException e) {
                e.printStackTrace();
            }
        } else {
            try {
                if (TvManager.getInstance() != null) {
                    TvManager.getInstance().getAudioManager().disableMute(EnumMuteType.E_BYUSER);
                }
            } catch (TvCommonException e2) {
                e2.printStackTrace();
            }
        }
        Log.d("SoundDeskImpl", "set muteFlag is" + muteFlag);
        return true;
    }

    public EnumAudioReturn setAtvMtsMode(EnumAtvAudioModeType enAtvMtsMode) {
        EnumAudioReturn result = EnumAudioReturn.E_RETURN_NOTOK;
        try {
            if (TvManager.getInstance() != null) {
                return TvManager.getInstance().getAudioManager().setAtvMtsMode(enAtvMtsMode);
            }
            return result;
        } catch (TvCommonException e) {
            e.printStackTrace();
            return result;
        }
    }

    public EnumAtvAudioModeType getAtvMtsMode() {
        EnumAtvAudioModeType result = EnumAtvAudioModeType.E_ATV_AUDIOMODE_INVALID;
        try {
            if (TvManager.getInstance() != null) {
                return TvManager.getInstance().getAudioManager().getAtvMtsMode();
            }
            return result;
        } catch (TvCommonException e) {
            e.printStackTrace();
            return result;
        }
    }

    public EnumAudioReturn setToNextAtvMtsMode() {
        EnumAudioReturn result = EnumAudioReturn.E_RETURN_NOTOK;
        try {
            if (TvManager.getInstance() != null) {
                return TvManager.getInstance().getAudioManager().setToNextAtvMtsMode();
            }
            return result;
        } catch (TvCommonException e) {
            e.printStackTrace();
            return result;
        }
    }

    public boolean setAudioInputSource(EnumInputSource enAudioInputSource) {
        Log.d("setAudioInputSource", "source is" + enAudioInputSource);
        try {
            if (TvManager.getInstance() != null) {
                TvManager.getInstance().getAudioManager().setInputSource(enAudioInputSource);
            }
        } catch (TvCommonException e) {
            e.printStackTrace();
        }
        return true;
    }

    public boolean setKaraVolume(short volume) {
        System.out.println("setKaraVolume SoundDeskImpl before try");
        try {
            System.out.println("setKaraVolume SoundDeskImpl accvolume = " + volume);
            if (TvManager.getInstance() != null) {
                TvManager.getInstance().getAudioManager().setInputLevel(EnumAudioInputLevelSourceType.E_VOL_SOURCE_PREMIXER_KTV_MP3_IN, volume);
            }
        } catch (TvCommonException e) {
            System.out.println("setKaraVolume SoundDeskImpl Error");
            this.f6com.printfE("TvSoundServiceImpl", "Set Volume Exception");
            e.printStackTrace();
        }
        return true;
    }

    public boolean setKaraSystemVolume(short volume) {
        System.out.println("setKaraSystemVolume SoundDeskImpl before try");
        try {
            System.out.println("setKaraSystemVolume SoundDeskImpl accvolume = " + volume);
            if (TvManager.getInstance() != null) {
                TvManager.getInstance().getAudioManager().setAudioVolume(EnumAudioVolumeSourceType.E_VOL_SOURCE_SPEAKER_OUT, (byte) volume);
            }
        } catch (TvCommonException e) {
            System.out.println("setKaraSystemVolume SoundDeskImpl Error");
            this.f6com.printfE("TvSoundServiceImpl", "Set Volume Exception");
            e.printStackTrace();
        }
        return true;
    }

    public boolean setKaraMicVolume(short volume) {
        System.out.println("setKaraMicVolume SoundDeskImpl before try");
        try {
            System.out.println("setKaraVolume SoundDeskImpl micvolume = " + volume);
            if (TvManager.getInstance() != null) {
                TvManager.getInstance().getAudioManager().setInputLevel(EnumAudioInputLevelSourceType.E_VOL_SOURCE_PREMIXER_KTV_MIC_IN, volume);
            }
        } catch (TvCommonException e) {
            System.out.println("setKaraVolume SoundDeskImpl Error");
            this.f6com.printfE("TvSoundServiceImpl", "Set Volume Exception");
            e.printStackTrace();
        }
        return true;
    }

    public boolean setKaraAudioTrack(int mode) {
        System.out.println("setKaraAudioMode SoundDeskImpl");
        try {
            if (TvManager.getInstance() != null) {
                TvManager.getInstance().getAudioManager().setKtvSoundInfo(EnumKtvInfoType.E_KTV_SETINFO_BG_MUSIC_SOUNDMODE, mode, 0);
            }
        } catch (TvCommonException e) {
            e.printStackTrace();
        }
        return true;
    }

    public boolean setSoundSpeakerDelay(int delay) {
        if (TvManager.getInstance() != null) {
            TvManager.getInstance().getAudioManager().setSoundSpeakerDelay(delay);
        }
        return true;
    }

    public int getSoundSpeakerDelay() {
        if (TvManager.getInstance() != null) {
            TvManager.getInstance().getAudioManager().getSoundParameter(EnumSoundSetParamType.E_SOUND_SET_PARAM_AUDIODELAY_, 0);
        }
        return 0;
    }

    public boolean setKaraMixVolume(short volume) {
        try {
            if (TvManager.getInstance() != null) {
                TvManager.getInstance().getAudioManager().setInputLevel(EnumAudioInputLevelSourceType.E_VOL_SOURCE_PREMIXER_ECHO1_IN, volume);
            }
        } catch (TvCommonException e) {
            this.f6com.printfE("TvSoundServiceImpl", "Set Volume Exception");
            e.printStackTrace();
        }
        return true;
    }

    public boolean setHdmiAudioSource(EnumInputSource inputSource, HdmiAudioSource audioSource) {
        EnumInputSource audioSrc;
        this.databaseMgr.querySoundSetting();
        this.userSoundSetting = this.databaseMgr.getSound();
        switch ($SWITCH_TABLE$com$mstar$android$tvapi$common$vo$TvOsType$EnumInputSource()[inputSource.ordinal()]) {
            case 24:
                this.userSoundSetting.hdmi1AudioSource = audioSource;
                break;
            case 25:
                this.userSoundSetting.hdmi2AudioSource = audioSource;
                break;
            case 26:
                this.userSoundSetting.hdmi3AudioSource = audioSource;
                break;
            case 27:
                this.userSoundSetting.hdmi4AudioSource = audioSource;
                break;
            default:
                return false;
        }
        this.f6com.printfI("TvSoundServiceImpl", "hdmiAudioSource is" + audioSource);
        this.databaseMgr.updateSoundSetting(this.userSoundSetting);
        try {
            if (audioSource == HdmiAudioSource.AUDIO_SOURCE_VGA) {
                audioSrc = EnumInputSource.E_INPUT_SOURCE_VGA;
            } else {
                audioSrc = inputSource;
            }
            if (TvManager.getInstance() != null) {
                TvManager.getInstance().getAudioManager().setInputSource(audioSrc);
            }
        } catch (TvCommonException e) {
            this.f6com.printfE("TvSoundServiceImpl", "Set hdmiAudioSource Exception");
            e.printStackTrace();
        }
        return true;
    }

    public HdmiAudioSource getHdmiAudioSource(EnumInputSource inputSource) {
        switch ($SWITCH_TABLE$com$mstar$android$tvapi$common$vo$TvOsType$EnumInputSource()[inputSource.ordinal()]) {
            case 24:
                return this.userSoundSetting.hdmi1AudioSource;
            case 25:
                return this.userSoundSetting.hdmi2AudioSource;
            case 26:
                return this.userSoundSetting.hdmi3AudioSource;
            case 27:
                return this.userSoundSetting.hdmi4AudioSource;
            default:
                return HdmiAudioSource.AUDIO_SOURCE_HDMI;
        }
    }

    public boolean setKaraMixEffect(boolean flag) {
        try {
            System.out.println("setKaraAudioMode SoundDeskImpl");
            if (TvManager.getInstance() != null) {
                TvManager.getInstance().getAudioManager().enableBasicSoundEffect(EnumSoundEffectType.E_ECHO, flag);
            }
        } catch (TvCommonException e) {
            this.f6com.printfE("TvSoundServiceImpl", "Set Volume Exception");
            e.printStackTrace();
        }
        return true;
    }

    public boolean setKaraADC() {
        try {
            System.out.printf("setKaraMicNR", new Object[0]);
            if (TvManager.getInstance() != null) {
                TvManager.getInstance().getAudioManager().setKtvSoundInfo(EnumKtvInfoType.E_KTV_SETINFO_ADC_GAIN, 6, 0);
                TvManager.getInstance().getAudioManager().setKtvSoundInfo(EnumKtvInfoType.E_KTV_SETINFO_ADC1_GAIN, 6, 0);
            }
        } catch (TvCommonException e) {
            e.printStackTrace();
        }
        return true;
    }

    public boolean setKaraMicNR(int value) {
        System.out.printf("setKaraMicNR", new Object[0]);
        if (TvManager.getInstance() != null) {
            TvManager.getInstance().getAudioManager().setSoundParameter(EnumSoundSetParamType.E_SOUND_SET_PARAM_NR_THRESHOLD_, value, 0);
        }
        return true;
    }

    public void setSRSTSHD(boolean bOnOff) {
        if (bOnOff) {
            try {
                Log.d("KKJAVAAPI", "set SRS TSHD on");
                setSoundMode(EN_SOUND_MODE.SOUND_MODE_STANDARD, false);
                if (TvManager.getInstance() != null) {
                    TvManager.getInstance().getAudioManager().enableAdvancedSoundEffect(EnumAdvancedSoundType.E_SRS_TSHD, EnumAdvancedSoundSubProcessType.E_SRS_THEATERSOUND_TSHD_ON);
                }
                this.databaseMgr.setSRSOnOff(EnumSwitchOnOff.SWITCH_ON);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Log.d("KKJAVAAPI", "set SRS TSHD off");
            if (TvManager.getInstance() != null) {
                TvManager.getInstance().getAudioManager().enableAdvancedSoundEffect(EnumAdvancedSoundType.E_SRS_TSHD, EnumAdvancedSoundSubProcessType.E_SRS_THEATERSOUND_TSHD_OFF);
            }
            this.databaseMgr.setSRSOnOff(EnumSwitchOnOff.SWITCH_OFF);
            setSoundMode(getSoundMode());
        }
    }

    public void setSRSTrueBass(boolean bOnOff) {
        if (bOnOff) {
            try {
                Log.d("KKJAVAAPI", "set SRS TrueBass on");
                if (TvManager.getInstance() != null) {
                    TvManager.getInstance().getAudioManager().enableAdvancedSoundEffect(EnumAdvancedSoundType.E_SRS_TSHD, EnumAdvancedSoundSubProcessType.E_SRS_THEATERSOUND_TRUEBASS_ON);
                }
                this.databaseMgr.setTruebaseOnOff(EnumSwitchOnOff.SWITCH_ON);
                AdvancedSoundParameter stPara = new AdvancedSoundParameter();
                stPara.paramSrsTheaterSoundTrubassControl = 500;
                if (TvManager.getInstance() != null) {
                    TvManager.getInstance().getAudioManager().setAdvancedSoundEffect(EnumAdvancedSoundParameterType.E_ADVSND_SRS_THEATERSOUND_TRUBASS_CONTROL, stPara);
                }
                int iTrubass = 0;
                if (TvManager.getInstance() != null) {
                    iTrubass = TvManager.getInstance().getAudioManager().getAdvancedSoundEffect(EnumAdvancedSoundParameterType.E_ADVSND_SRS_THEATERSOUND_TRUBASS_CONTROL);
                }
                Log.d("KKJAVAAPI", "the iTrubass==" + iTrubass);
            } catch (TvCommonException e) {
                e.printStackTrace();
            }
        } else {
            Log.d("KKJAVAAPI", "set SRS TrueBass off!");
            if (TvManager.getInstance() != null) {
                TvManager.getInstance().getAudioManager().enableAdvancedSoundEffect(EnumAdvancedSoundType.E_SRS_TSHD, EnumAdvancedSoundSubProcessType.E_SRS_THEATERSOUND_TRUEBASS_OFF);
            }
            this.databaseMgr.setTruebaseOnOff(EnumSwitchOnOff.SWITCH_OFF);
        }
    }

    public void setSRSDynamicClarity(boolean bOnOff) {
        if (bOnOff) {
            try {
                Log.d("KKJAVAAPI", "set SRS DynamicClarity on");
                if (TvManager.getInstance() != null) {
                    TvManager.getInstance().getAudioManager().enableAdvancedSoundEffect(EnumAdvancedSoundType.E_SRS_TSHD, EnumAdvancedSoundSubProcessType.E_SRS_THEATERSOUND_DYNAMIC_CLARITY_ON);
                }
                this.databaseMgr.setDialogClarityOnOff(EnumSwitchOnOff.SWITCH_ON);
                AdvancedSoundParameter stPara = new AdvancedSoundParameter();
                stPara.paramSrsTheaterSoundDcControl = 700;
                if (TvManager.getInstance() != null) {
                    TvManager.getInstance().getAudioManager().setAdvancedSoundEffect(EnumAdvancedSoundParameterType.E_ADVSND_SRS_THEATERSOUND_DC_CONTROL, stPara);
                }
            } catch (TvCommonException e) {
                e.printStackTrace();
            }
        } else {
            Log.d("KKJAVAAPI", "set SRS DynamicClarity off");
            if (TvManager.getInstance() != null) {
                TvManager.getInstance().getAudioManager().enableAdvancedSoundEffect(EnumAdvancedSoundType.E_SRS_TSHD, EnumAdvancedSoundSubProcessType.E_SRS_THEATERSOUND_DYNAMIC_CLARITY_OFF);
            }
            this.databaseMgr.setDialogClarityOnOff(EnumSwitchOnOff.SWITCH_OFF);
        }
    }

    public void setSRSDefination(boolean bOnOff) {
        if (bOnOff) {
            try {
                Log.d("KKJAVAAPI", "set SRS Defination on!");
                if (TvManager.getInstance() != null) {
                    TvManager.getInstance().getAudioManager().enableAdvancedSoundEffect(EnumAdvancedSoundType.E_SRS_TSHD, EnumAdvancedSoundSubProcessType.E_SRS_THEATERSOUND_DEFINITION_ON);
                }
                this.databaseMgr.setDefinitionOnOff(EnumSwitchOnOff.SWITCH_ON);
                AdvancedSoundParameter stPara = new AdvancedSoundParameter();
                stPara.paramSrsTheaterSoundDefinitionControl = Launcher.TIME_DELAY_SHOW_TV_FROM_WORKSPACE;
                if (TvManager.getInstance() != null) {
                    TvManager.getInstance().getAudioManager().setAdvancedSoundEffect(EnumAdvancedSoundParameterType.E_ADVSND_SRS_THEATERSOUND_DEFINITION_CONTROL, stPara);
                }
            } catch (TvCommonException e) {
                e.printStackTrace();
            }
        } else {
            Log.d("KKJAVAAPI", "set SRS Defination off");
            if (TvManager.getInstance() != null) {
                TvManager.getInstance().getAudioManager().enableAdvancedSoundEffect(EnumAdvancedSoundType.E_SRS_TSHD, EnumAdvancedSoundSubProcessType.E_SRS_THEATERSOUND_DEFINITION_OFF);
            }
            this.databaseMgr.setDefinitionOnOff(EnumSwitchOnOff.SWITCH_OFF);
        }
    }

    public void setSRSPara(EN_SRS_SET_TYPE eSRS, int iValue) {
        AdvancedSoundParameter stPara = new AdvancedSoundParameter();
        KK_SRS_SET stSRS = new KK_SRS_SET();
        if (eSRS == EN_SRS_SET_TYPE.E_SRS_INPUTGAIN) {
            stPara.paramSrsTheaterSoundInputGain = this.databaseMgr.getSRSSet().srs_InputGain * 100;
            try {
                if (TvManager.getInstance() != null) {
                    TvManager.getInstance().getAudioManager().setAdvancedSoundEffect(EnumAdvancedSoundParameterType.E_ADVSND_SRS_THEATERSOUND_INPUT_GAIN, stPara);
                }
            } catch (TvCommonException e) {
                e.printStackTrace();
            }
            stSRS.srs_InputGain = iValue;
            this.databaseMgr.setSRSSet(stSRS, eSRS);
        } else if (eSRS == EN_SRS_SET_TYPE.E_SRS_SURRLEVEL_CONTROL) {
            stPara.paramSrsTheaterasoundSurrLevelControl = this.databaseMgr.getSRSSet().srs_SurrLevelControl * 100;
            try {
                if (TvManager.getInstance() != null) {
                    TvManager.getInstance().getAudioManager().setAdvancedSoundEffect(EnumAdvancedSoundParameterType.ADVSND_SRS_THEATERSOUND_SURR_LEVEL_CONTROL, stPara);
                }
            } catch (TvCommonException e2) {
                e2.printStackTrace();
            }
            stSRS.srs_SurrLevelControl = iValue;
            this.databaseMgr.setSRSSet(stSRS, eSRS);
        } else if (eSRS == EN_SRS_SET_TYPE.E_SRS_SPEAKERAUDIO) {
            stPara.paramSrsTheaterasoundTrubassSpeakerAudio = this.databaseMgr.getSRSSet().srs_SpeakerAudio;
            try {
                if (TvManager.getInstance() != null) {
                    TvManager.getInstance().getAudioManager().setAdvancedSoundEffect(EnumAdvancedSoundParameterType.ADVSND_SRS_THEATERSOUND_TRUBASS_SPEAKER_AUDIO, stPara);
                }
            } catch (TvCommonException e3) {
                e3.printStackTrace();
            }
            stSRS.srs_SpeakerAudio = iValue;
            this.databaseMgr.setSRSSet(stSRS, eSRS);
        } else if (eSRS == EN_SRS_SET_TYPE.E_SRS_SPEAKERANALYSIS) {
            stPara.paramSrsTheaterasoundTrubassSpeakerAnalysis = this.databaseMgr.getSRSSet().srs_SpeakerAnalysis;
            try {
                if (TvManager.getInstance() != null) {
                    TvManager.getInstance().getAudioManager().setAdvancedSoundEffect(EnumAdvancedSoundParameterType.ADVSND_SRS_THEATERSOUND_SPEAKER_ANALYSIS, stPara);
                }
            } catch (TvCommonException e4) {
                e4.printStackTrace();
            }
            stSRS.srs_SpeakerAnalysis = iValue;
            this.databaseMgr.setSRSSet(stSRS, eSRS);
        } else if (eSRS == EN_SRS_SET_TYPE.E_SRS_TRUBASS_CONTROL) {
            stPara.paramSrsTheaterSoundTrubassControl = this.databaseMgr.getSRSSet().srs_TrubassControl * 100;
            try {
                if (TvManager.getInstance() != null) {
                    TvManager.getInstance().getAudioManager().setAdvancedSoundEffect(EnumAdvancedSoundParameterType.E_ADVSND_SRS_THEATERSOUND_TRUBASS_CONTROL, stPara);
                }
            } catch (TvCommonException e5) {
                e5.printStackTrace();
            }
            stSRS.srs_TrubassControl = iValue;
            this.databaseMgr.setSRSSet(stSRS, eSRS);
        } else if (eSRS == EN_SRS_SET_TYPE.E_SRS_DC_CONTROL) {
            stPara.paramSrsTheaterSoundDcControl = this.databaseMgr.getSRSSet().srs_DCControl * 100;
            try {
                if (TvManager.getInstance() != null) {
                    TvManager.getInstance().getAudioManager().setAdvancedSoundEffect(EnumAdvancedSoundParameterType.E_ADVSND_SRS_THEATERSOUND_DC_CONTROL, stPara);
                }
            } catch (TvCommonException e6) {
                e6.printStackTrace();
            }
            stSRS.srs_DCControl = iValue;
            this.databaseMgr.setSRSSet(stSRS, eSRS);
        } else if (eSRS == EN_SRS_SET_TYPE.E_SRS_DEFINITION_CONTROL) {
            stPara.paramSrsTheaterSoundDefinitionControl = this.databaseMgr.getSRSSet().srs_DefinitionControl * 100;
            try {
                if (TvManager.getInstance() != null) {
                    TvManager.getInstance().getAudioManager().setAdvancedSoundEffect(EnumAdvancedSoundParameterType.E_ADVSND_SRS_THEATERSOUND_DEFINITION_CONTROL, stPara);
                }
            } catch (TvCommonException e7) {
                e7.printStackTrace();
            }
            stSRS.srs_DefinitionControl = iValue;
            this.databaseMgr.setSRSSet(stSRS, eSRS);
        }
    }
}
