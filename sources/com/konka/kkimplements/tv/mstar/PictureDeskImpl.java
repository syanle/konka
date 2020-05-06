package com.konka.kkimplements.tv.mstar;

import android.content.Context;
import android.util.Log;
import com.konka.kkimplements.tv.mstar.GruleIndex.MST_GRule_COLOR_LEVEL_STRETCH_Index_Main;
import com.konka.kkimplements.tv.mstar.GruleIndex.MST_GRule_DETAILS_Index_Main;
import com.konka.kkimplements.tv.mstar.GruleIndex.MST_GRule_Index_Main;
import com.konka.kkimplements.tv.mstar.GruleIndex.MST_GRule_SKIN_TONE_Index_Main;
import com.konka.kkinterface.tv.CommonDesk;
import com.konka.kkinterface.tv.DataBaseDesk.EN_MS_COLOR_TEMP;
import com.konka.kkinterface.tv.DataBaseDesk.EN_MS_COLOR_TEMP_INPUT_SOURCE;
import com.konka.kkinterface.tv.DataBaseDesk.EN_MS_Dynamic_Contrast;
import com.konka.kkinterface.tv.DataBaseDesk.EN_MS_LOCALDIMMING;
import com.konka.kkinterface.tv.DataBaseDesk.EN_MS_MPEG_NR;
import com.konka.kkinterface.tv.DataBaseDesk.EN_MS_NR;
import com.konka.kkinterface.tv.DataBaseDesk.EN_MS_PICTURE;
import com.konka.kkinterface.tv.DataBaseDesk.EN_MS_VIDEOITEM;
import com.konka.kkinterface.tv.DataBaseDesk.EN_ThreeD_Video_DISPLAYFORMAT;
import com.konka.kkinterface.tv.DataBaseDesk.EN_ThreeD_Video_SELFADAPTIVE_DETECT;
import com.konka.kkinterface.tv.DataBaseDesk.MAPI_VIDEO_ARC_Type;
import com.konka.kkinterface.tv.DataBaseDesk.SkinToneMode;
import com.konka.kkinterface.tv.DataBaseDesk.T_MS_COLOR_TEMPEX_DATA;
import com.konka.kkinterface.tv.DataBaseDesk.T_MS_VIDEO;
import com.konka.kkinterface.tv.DtvInterface.EPG_EVENT_INFO;
import com.konka.kkinterface.tv.PictureDesk;
import com.mstar.android.tvapi.common.TvManager;
import com.mstar.android.tvapi.common.exception.TvCommonException;
import com.mstar.android.tvapi.common.vo.ColorTemperature;
import com.mstar.android.tvapi.common.vo.EnumColorTemperature;
import com.mstar.android.tvapi.common.vo.EnumScalerWindow;
import com.mstar.android.tvapi.common.vo.EnumScreenMuteType;
import com.mstar.android.tvapi.common.vo.EnumVideoArcType;
import com.mstar.android.tvapi.common.vo.MpegNoiseReduction.EnumMpegNoiseReduction;
import com.mstar.android.tvapi.common.vo.NoiseReduction.EnumNoiseReduction;
import com.mstar.android.tvapi.common.vo.TvOsType.EnumInputSource;
import com.mstar.android.tvapi.common.vo.VideoWindowType;
import com.mstar.android.tvapi.dtv.vo.DtvEventComponentInfo;
import com.mstar.android.tvapi.dtv.vo.DtvType.EnumAspectRatioCode;

public class PictureDeskImpl extends BaseDeskImpl implements PictureDesk {
    private static /* synthetic */ int[] $SWITCH_TABLE$com$konka$kkinterface$tv$DataBaseDesk$EN_MS_Dynamic_Contrast;
    private static /* synthetic */ int[] $SWITCH_TABLE$com$konka$kkinterface$tv$DataBaseDesk$EN_MS_LOCALDIMMING;
    private static /* synthetic */ int[] $SWITCH_TABLE$com$konka$kkinterface$tv$DataBaseDesk$EN_MS_MPEG_NR;
    private static /* synthetic */ int[] $SWITCH_TABLE$com$konka$kkinterface$tv$DataBaseDesk$EN_MS_NR;
    private static /* synthetic */ int[] $SWITCH_TABLE$com$konka$kkinterface$tv$DataBaseDesk$EN_MS_VIDEOITEM;
    private static /* synthetic */ int[] $SWITCH_TABLE$com$konka$kkinterface$tv$DataBaseDesk$SkinToneMode;
    private static /* synthetic */ int[] $SWITCH_TABLE$com$mstar$android$tvapi$common$vo$TvOsType$EnumInputSource;
    private static PictureDeskImpl pictureMgrImpl = null;

    /* renamed from: com reason: collision with root package name */
    private CommonDesk f2com = null;
    private Context context;
    private DataBaseDeskImpl databaseMgr = null;
    boolean enableSetBacklight = true;
    private T_MS_VIDEO videoPara = null;

    static /* synthetic */ int[] $SWITCH_TABLE$com$konka$kkinterface$tv$DataBaseDesk$EN_MS_Dynamic_Contrast() {
        int[] iArr = $SWITCH_TABLE$com$konka$kkinterface$tv$DataBaseDesk$EN_MS_Dynamic_Contrast;
        if (iArr == null) {
            iArr = new int[EN_MS_Dynamic_Contrast.values().length];
            try {
                iArr[EN_MS_Dynamic_Contrast.MS_Dynamic_Contrast_NUM.ordinal()] = 3;
            } catch (NoSuchFieldError e) {
            }
            try {
                iArr[EN_MS_Dynamic_Contrast.MS_Dynamic_Contrast_OFF.ordinal()] = 1;
            } catch (NoSuchFieldError e2) {
            }
            try {
                iArr[EN_MS_Dynamic_Contrast.MS_Dynamic_Contrast_ON.ordinal()] = 2;
            } catch (NoSuchFieldError e3) {
            }
            $SWITCH_TABLE$com$konka$kkinterface$tv$DataBaseDesk$EN_MS_Dynamic_Contrast = iArr;
        }
        return iArr;
    }

    static /* synthetic */ int[] $SWITCH_TABLE$com$konka$kkinterface$tv$DataBaseDesk$EN_MS_LOCALDIMMING() {
        int[] iArr = $SWITCH_TABLE$com$konka$kkinterface$tv$DataBaseDesk$EN_MS_LOCALDIMMING;
        if (iArr == null) {
            iArr = new int[EN_MS_LOCALDIMMING.values().length];
            try {
                iArr[EN_MS_LOCALDIMMING.LOCALDIMMING_NUMS.ordinal()] = 3;
            } catch (NoSuchFieldError e) {
            }
            try {
                iArr[EN_MS_LOCALDIMMING.LOCALDIMMING_OFF.ordinal()] = 1;
            } catch (NoSuchFieldError e2) {
            }
            try {
                iArr[EN_MS_LOCALDIMMING.LOCALDIMMING_ON.ordinal()] = 2;
            } catch (NoSuchFieldError e3) {
            }
            $SWITCH_TABLE$com$konka$kkinterface$tv$DataBaseDesk$EN_MS_LOCALDIMMING = iArr;
        }
        return iArr;
    }

    static /* synthetic */ int[] $SWITCH_TABLE$com$konka$kkinterface$tv$DataBaseDesk$EN_MS_MPEG_NR() {
        int[] iArr = $SWITCH_TABLE$com$konka$kkinterface$tv$DataBaseDesk$EN_MS_MPEG_NR;
        if (iArr == null) {
            iArr = new int[EN_MS_MPEG_NR.values().length];
            try {
                iArr[EN_MS_MPEG_NR.MS_MPEG_NR_HIGH.ordinal()] = 4;
            } catch (NoSuchFieldError e) {
            }
            try {
                iArr[EN_MS_MPEG_NR.MS_MPEG_NR_LOW.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                iArr[EN_MS_MPEG_NR.MS_MPEG_NR_MIDDLE.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                iArr[EN_MS_MPEG_NR.MS_MPEG_NR_NUM.ordinal()] = 5;
            } catch (NoSuchFieldError e4) {
            }
            try {
                iArr[EN_MS_MPEG_NR.MS_MPEG_NR_OFF.ordinal()] = 1;
            } catch (NoSuchFieldError e5) {
            }
            $SWITCH_TABLE$com$konka$kkinterface$tv$DataBaseDesk$EN_MS_MPEG_NR = iArr;
        }
        return iArr;
    }

    static /* synthetic */ int[] $SWITCH_TABLE$com$konka$kkinterface$tv$DataBaseDesk$EN_MS_NR() {
        int[] iArr = $SWITCH_TABLE$com$konka$kkinterface$tv$DataBaseDesk$EN_MS_NR;
        if (iArr == null) {
            iArr = new int[EN_MS_NR.values().length];
            try {
                iArr[EN_MS_NR.MS_NR_AUTO.ordinal()] = 5;
            } catch (NoSuchFieldError e) {
            }
            try {
                iArr[EN_MS_NR.MS_NR_HIGH.ordinal()] = 4;
            } catch (NoSuchFieldError e2) {
            }
            try {
                iArr[EN_MS_NR.MS_NR_LOW.ordinal()] = 2;
            } catch (NoSuchFieldError e3) {
            }
            try {
                iArr[EN_MS_NR.MS_NR_MIDDLE.ordinal()] = 3;
            } catch (NoSuchFieldError e4) {
            }
            try {
                iArr[EN_MS_NR.MS_NR_NUM.ordinal()] = 6;
            } catch (NoSuchFieldError e5) {
            }
            try {
                iArr[EN_MS_NR.MS_NR_OFF.ordinal()] = 1;
            } catch (NoSuchFieldError e6) {
            }
            $SWITCH_TABLE$com$konka$kkinterface$tv$DataBaseDesk$EN_MS_NR = iArr;
        }
        return iArr;
    }

    static /* synthetic */ int[] $SWITCH_TABLE$com$konka$kkinterface$tv$DataBaseDesk$EN_MS_VIDEOITEM() {
        int[] iArr = $SWITCH_TABLE$com$konka$kkinterface$tv$DataBaseDesk$EN_MS_VIDEOITEM;
        if (iArr == null) {
            iArr = new int[EN_MS_VIDEOITEM.values().length];
            try {
                iArr[EN_MS_VIDEOITEM.MS_VIDEOITEM_BACKLIGHT.ordinal()] = 6;
            } catch (NoSuchFieldError e) {
            }
            try {
                iArr[EN_MS_VIDEOITEM.MS_VIDEOITEM_BRIGHTNESS.ordinal()] = 1;
            } catch (NoSuchFieldError e2) {
            }
            try {
                iArr[EN_MS_VIDEOITEM.MS_VIDEOITEM_CONTRAST.ordinal()] = 2;
            } catch (NoSuchFieldError e3) {
            }
            try {
                iArr[EN_MS_VIDEOITEM.MS_VIDEOITEM_HUE.ordinal()] = 5;
            } catch (NoSuchFieldError e4) {
            }
            try {
                iArr[EN_MS_VIDEOITEM.MS_VIDEOITEM_NUM.ordinal()] = 7;
            } catch (NoSuchFieldError e5) {
            }
            try {
                iArr[EN_MS_VIDEOITEM.MS_VIDEOITEM_SATURATION.ordinal()] = 3;
            } catch (NoSuchFieldError e6) {
            }
            try {
                iArr[EN_MS_VIDEOITEM.MS_VIDEOITEM_SHARPNESS.ordinal()] = 4;
            } catch (NoSuchFieldError e7) {
            }
            $SWITCH_TABLE$com$konka$kkinterface$tv$DataBaseDesk$EN_MS_VIDEOITEM = iArr;
        }
        return iArr;
    }

    static /* synthetic */ int[] $SWITCH_TABLE$com$konka$kkinterface$tv$DataBaseDesk$SkinToneMode() {
        int[] iArr = $SWITCH_TABLE$com$konka$kkinterface$tv$DataBaseDesk$SkinToneMode;
        if (iArr == null) {
            iArr = new int[SkinToneMode.values().length];
            try {
                iArr[SkinToneMode.SKIN_TONE_OFF.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                iArr[SkinToneMode.SKIN_TONE_RED.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                iArr[SkinToneMode.SKIN_TONE_YELLOW.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            $SWITCH_TABLE$com$konka$kkinterface$tv$DataBaseDesk$SkinToneMode = iArr;
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

    public static PictureDeskImpl getPictureMgrInstance(Context context2) {
        if (pictureMgrImpl == null) {
            pictureMgrImpl = new PictureDeskImpl(context2);
        }
        return pictureMgrImpl;
    }

    private PictureDeskImpl(Context context2) {
        this.context = context2;
        this.databaseMgr = DataBaseDeskImpl.getDataBaseMgrInstance(context2);
        this.videoPara = this.databaseMgr.getVideo();
        this.f2com = CommonDeskImpl.getInstance(context2);
    }

    public boolean ExecVideoItem(EN_MS_VIDEOITEM eIndex, short value) {
        int idx = this.videoPara.ePicture.ordinal();
        if (eIndex.ordinal() == EN_MS_VIDEOITEM.MS_VIDEOITEM_BRIGHTNESS.ordinal()) {
            this.videoPara.astPicture[idx].brightness = value;
            this.databaseMgr.updateVideoAstPicture(this.videoPara.astPicture[idx], this.f2com.GetCurrentInputSource().ordinal(), idx);
            try {
                if (!(TvManager.getInstance() == null || TvManager.getInstance().getPictureManager() == null)) {
                    TvManager.getInstance().getPictureManager().setPictureModeBrightness(value);
                }
            } catch (TvCommonException e) {
                e.printStackTrace();
            }
        } else if (eIndex.ordinal() == EN_MS_VIDEOITEM.MS_VIDEOITEM_CONTRAST.ordinal()) {
            this.videoPara.astPicture[idx].contrast = value;
            this.databaseMgr.updateVideoAstPicture(this.videoPara.astPicture[idx], this.f2com.GetCurrentInputSource().ordinal(), idx);
            try {
                if (!(TvManager.getInstance() == null || TvManager.getInstance().getPictureManager() == null)) {
                    TvManager.getInstance().getPictureManager().setPictureModeContrast(value);
                }
            } catch (TvCommonException e2) {
                e2.printStackTrace();
            }
        } else if (eIndex.ordinal() == EN_MS_VIDEOITEM.MS_VIDEOITEM_SATURATION.ordinal()) {
            this.videoPara.astPicture[idx].saturation = value;
            this.databaseMgr.updateVideoAstPicture(this.videoPara.astPicture[idx], this.f2com.GetCurrentInputSource().ordinal(), idx);
            try {
                if (!(TvManager.getInstance() == null || TvManager.getInstance().getPictureManager() == null)) {
                    TvManager.getInstance().getPictureManager().setPictureModeColor(value);
                }
            } catch (TvCommonException e3) {
                e3.printStackTrace();
            }
        } else if (eIndex.ordinal() == EN_MS_VIDEOITEM.MS_VIDEOITEM_SHARPNESS.ordinal()) {
            this.videoPara.astPicture[idx].sharpness = value;
            this.databaseMgr.updateVideoAstPicture(this.videoPara.astPicture[idx], this.f2com.GetCurrentInputSource().ordinal(), idx);
            try {
                if (!(TvManager.getInstance() == null || TvManager.getInstance().getPictureManager() == null)) {
                    TvManager.getInstance().getPictureManager().setPictureModeSharpness(value);
                }
            } catch (TvCommonException e4) {
                e4.printStackTrace();
            }
        } else if (eIndex.ordinal() == EN_MS_VIDEOITEM.MS_VIDEOITEM_HUE.ordinal()) {
            this.videoPara.astPicture[idx].hue = value;
            this.databaseMgr.updateVideoAstPicture(this.videoPara.astPicture[idx], this.f2com.GetCurrentInputSource().ordinal(), idx);
            try {
                if (!(TvManager.getInstance() == null || TvManager.getInstance().getPictureManager() == null)) {
                    TvManager.getInstance().getPictureManager().setPictureModeTint(value);
                }
            } catch (TvCommonException e5) {
                e5.printStackTrace();
            }
        } else if (eIndex.ordinal() == EN_MS_VIDEOITEM.MS_VIDEOITEM_BACKLIGHT.ordinal()) {
            this.videoPara.astPicture[idx].backlight = value;
            this.databaseMgr.updateVideoAstPicture(this.videoPara.astPicture[idx], this.f2com.GetCurrentInputSource().ordinal(), idx);
            try {
                if (!(TvManager.getInstance() == null || TvManager.getInstance().getPictureManager() == null)) {
                    TvManager.getInstance().getPictureManager().setBacklight(value);
                }
            } catch (TvCommonException e6) {
                e6.printStackTrace();
            }
        }
        this.databaseMgr.updateVideoAstPicture(this.videoPara.astPicture[idx], this.f2com.GetCurrentInputSource().ordinal(), this.videoPara.ePicture.ordinal());
        return true;
    }

    public short GetVideoItem(EN_MS_VIDEOITEM eIndex) {
        int idx = this.videoPara.ePicture.ordinal();
        switch ($SWITCH_TABLE$com$konka$kkinterface$tv$DataBaseDesk$EN_MS_VIDEOITEM()[eIndex.ordinal()]) {
            case 1:
                return (short) this.databaseMgr.queryPicModeSetting(eIndex, this.f2com.GetCurrentInputSource().ordinal(), idx);
            case 2:
                return (short) this.databaseMgr.queryPicModeSetting(eIndex, this.f2com.GetCurrentInputSource().ordinal(), idx);
            case 3:
                return (short) this.databaseMgr.queryPicModeSetting(eIndex, this.f2com.GetCurrentInputSource().ordinal(), idx);
            case 4:
                return (short) this.databaseMgr.queryPicModeSetting(eIndex, this.f2com.GetCurrentInputSource().ordinal(), idx);
            case 5:
                return (short) this.databaseMgr.queryPicModeSetting(eIndex, this.f2com.GetCurrentInputSource().ordinal(), idx);
            case 6:
                return (short) this.databaseMgr.queryPicModeSetting(eIndex, this.f2com.GetCurrentInputSource().ordinal(), 0);
            default:
                this.f2com.printfE("TvService", "Haven't this item internal error !!");
                return 80;
        }
    }

    public boolean SetPictureModeIdx(EN_MS_PICTURE ePicMode) {
        this.videoPara.ePicture = ePicMode;
        try {
            if (!(TvManager.getInstance() == null || TvManager.getInstance().getPictureManager() == null)) {
                TvManager.getInstance().getPictureManager().setPictureModeBrightness(this.videoPara.astPicture[ePicMode.ordinal()].brightness);
                TvManager.getInstance().getPictureManager().setPictureModeContrast(this.videoPara.astPicture[ePicMode.ordinal()].contrast);
                TvManager.getInstance().getPictureManager().setPictureModeColor(this.videoPara.astPicture[ePicMode.ordinal()].saturation);
                TvManager.getInstance().getPictureManager().setPictureModeSharpness(this.videoPara.astPicture[ePicMode.ordinal()].sharpness);
                TvManager.getInstance().getPictureManager().setPictureModeTint(this.videoPara.astPicture[ePicMode.ordinal()].hue);
            }
        } catch (TvCommonException e) {
            e.printStackTrace();
        }
        this.databaseMgr.updateVideoBasePara(this.videoPara, this.f2com.GetCurrentInputSource().ordinal());
        this.databaseMgr.updateVideoAstPicture(this.videoPara.astPicture[ePicMode.ordinal()], this.f2com.GetCurrentInputSource().ordinal(), ePicMode.ordinal());
        return true;
    }

    public int getDynamicBLModeIdx() {
        return this.databaseMgr.getDynamicBLMode();
    }

    public boolean setDynamicBLModeIdx(EN_MS_LOCALDIMMING Localdimminglevel) {
        switch ($SWITCH_TABLE$com$konka$kkinterface$tv$DataBaseDesk$EN_MS_LOCALDIMMING()[Localdimminglevel.ordinal()]) {
            case 1:
                try {
                    if (TvManager.getInstance() != null) {
                        TvManager.getInstance().setTvosCommonCommand("SetLocalDimmingMode_Off");
                        break;
                    }
                } catch (TvCommonException e) {
                    e.printStackTrace();
                    break;
                }
                break;
            case 2:
                try {
                    if (TvManager.getInstance() != null) {
                        TvManager.getInstance().setTvosCommonCommand("SetLocalDimmingMode_On");
                        break;
                    }
                } catch (TvCommonException e2) {
                    e2.printStackTrace();
                    break;
                }
                break;
        }
        return true;
    }

    public EN_MS_PICTURE GetPictureModeIdx() {
        this.videoPara = this.databaseMgr.queryAllVideoPara(this.f2com.GetCurrentInputSource().ordinal());
        return this.videoPara.ePicture;
    }

    public boolean enableSetBacklight(boolean enable) {
        this.enableSetBacklight = enable;
        return true;
    }

    public boolean SetBacklight(short value) {
        this.videoPara.astPicture[0].backlight = value;
        this.databaseMgr.updateVideoAstPicture(this.videoPara.astPicture[0], this.f2com.GetCurrentInputSource().ordinal(), 0);
        if (this.enableSetBacklight) {
            try {
                if (!(TvManager.getInstance() == null || TvManager.getInstance().getPictureManager() == null)) {
                    TvManager.getInstance().getPictureManager().setBacklight(value);
                }
            } catch (TvCommonException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    public short GetBacklight() {
        this.videoPara.astPicture[0].backlight = GetVideoItem(EN_MS_VIDEOITEM.MS_VIDEOITEM_BACKLIGHT);
        this.f2com.printfE("!!!!!!!!!!!!!!!!!!!!!!!!!TvService", "GetBacklight = " + this.videoPara.astPicture[0].backlight + " !!");
        return this.videoPara.astPicture[0].backlight;
    }

    public short GetBacklightOfPicMode(EN_MS_PICTURE ePicture) {
        return this.videoPara.astPicture[ePicture.ordinal()].backlight;
    }

    public boolean SetColorTempIdx(EN_MS_COLOR_TEMP eColorTemp) {
        DataBaseDeskImpl.getDataBaseMgrInstance(this.context).queryFactoryColorTempExData();
        this.f2com.printfE("TvService", "SetColorTempIdx nothing to do!!");
        this.videoPara.astPicture[0].eColorTemp = eColorTemp;
        ColorTemperature vo = new ColorTemperature();
        EnumInputSource curSource = null;
        T_MS_COLOR_TEMPEX_DATA temp = null;
        try {
            if (TvManager.getInstance() != null) {
                curSource = TvManager.getInstance().getCurrentInputSource();
            }
        } catch (TvCommonException e) {
            e.printStackTrace();
        }
        switch ($SWITCH_TABLE$com$mstar$android$tvapi$common$vo$TvOsType$EnumInputSource()[curSource.ordinal()]) {
            case 1:
                temp = DataBaseDeskImpl.getDataBaseMgrInstance(this.context).m_stFactoryColorTempEx.astColorTempEx[eColorTemp.ordinal()][EN_MS_COLOR_TEMP_INPUT_SOURCE.E_INPUT_SOURCE_VGA.ordinal()];
                break;
            case 2:
                temp = DataBaseDeskImpl.getDataBaseMgrInstance(this.context).m_stFactoryColorTempEx.astColorTempEx[eColorTemp.ordinal()][EN_MS_COLOR_TEMP_INPUT_SOURCE.E_INPUT_SOURCE_ATV.ordinal()];
                break;
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
                temp = DataBaseDeskImpl.getDataBaseMgrInstance(this.context).m_stFactoryColorTempEx.astColorTempEx[eColorTemp.ordinal()][EN_MS_COLOR_TEMP_INPUT_SOURCE.E_INPUT_SOURCE_CVBS.ordinal()];
                break;
            case EPG_EVENT_INFO.EN_EPG_FUNC_STATUS_DB_NO_CHANNEL_DB /*12*/:
            case 13:
            case 14:
            case 15:
                temp = DataBaseDeskImpl.getDataBaseMgrInstance(this.context).m_stFactoryColorTempEx.astColorTempEx[eColorTemp.ordinal()][EN_MS_COLOR_TEMP_INPUT_SOURCE.E_INPUT_SOURCE_SVIDEO.ordinal()];
                break;
            case 17:
            case 18:
            case 19:
                temp = DataBaseDeskImpl.getDataBaseMgrInstance(this.context).m_stFactoryColorTempEx.astColorTempEx[eColorTemp.ordinal()][EN_MS_COLOR_TEMP_INPUT_SOURCE.E_INPUT_SOURCE_YPBPR.ordinal()];
                break;
            case 21:
            case 22:
                temp = DataBaseDeskImpl.getDataBaseMgrInstance(this.context).m_stFactoryColorTempEx.astColorTempEx[eColorTemp.ordinal()][EN_MS_COLOR_TEMP_INPUT_SOURCE.E_INPUT_SOURCE_SCART.ordinal()];
                break;
            case 24:
            case 25:
            case 26:
            case 27:
                temp = DataBaseDeskImpl.getDataBaseMgrInstance(this.context).m_stFactoryColorTempEx.astColorTempEx[eColorTemp.ordinal()][EN_MS_COLOR_TEMP_INPUT_SOURCE.E_INPUT_SOURCE_HDMI.ordinal()];
                break;
            case 29:
            case 38:
                temp = DataBaseDeskImpl.getDataBaseMgrInstance(this.context).m_stFactoryColorTempEx.astColorTempEx[eColorTemp.ordinal()][EN_MS_COLOR_TEMP_INPUT_SOURCE.E_INPUT_SOURCE_DTV.ordinal()];
                break;
            case 30:
            case 31:
            case 32:
            case 33:
            case 35:
            case 36:
            case 37:
            case 39:
                temp = DataBaseDeskImpl.getDataBaseMgrInstance(this.context).m_stFactoryColorTempEx.astColorTempEx[eColorTemp.ordinal()][EN_MS_COLOR_TEMP_INPUT_SOURCE.E_INPUT_SOURCE_OTHERS.ordinal()];
                break;
            default:
                this.f2com.printfE("TvService", "Haven't this input source type !!");
                break;
        }
        vo.redGain = (short) temp.redgain;
        vo.greenGain = (short) temp.greengain;
        vo.buleGain = (short) temp.bluegain;
        vo.redOffset = (short) temp.redoffset;
        vo.greenOffset = (short) temp.greenoffset;
        vo.blueOffset = (short) temp.blueoffset;
        try {
            if (TvManager.getInstance() != null) {
                TvManager.getInstance().getFactoryManager().setWbGainOffsetEx(EnumColorTemperature.values()[eColorTemp.ordinal() + 1], vo.redGain, vo.greenGain, vo.buleGain, vo.redOffset, vo.greenOffset, vo.blueOffset, curSource);
            }
        } catch (TvCommonException e1) {
            e1.printStackTrace();
        }
        this.databaseMgr.updateVideoAstPicture(this.videoPara.astPicture[0], this.f2com.GetCurrentInputSource().ordinal(), 0);
        return true;
    }

    public EN_MS_COLOR_TEMP GetColorTempIdx() {
        int bColorTempIdx = this.databaseMgr.queryColorTemp(this.f2com.GetCurrentInputSource().ordinal(), 0);
        this.videoPara.astPicture[0].eColorTemp = EN_MS_COLOR_TEMP.values()[bColorTempIdx];
        return this.videoPara.astPicture[0].eColorTemp;
    }

    public boolean SetColorTempPara(T_MS_COLOR_TEMPEX_DATA stColorTemp) {
        boolean bRet = this.databaseMgr.setVideoTempEx(stColorTemp);
        this.databaseMgr.updateUsrColorTmpExData(stColorTemp, this.databaseMgr.getVideo().astPicture[this.databaseMgr.getVideo().ePicture.ordinal()].eColorTemp.ordinal());
        return bRet;
    }

    public T_MS_COLOR_TEMPEX_DATA GetColorTempPara() {
        return this.databaseMgr.getVideoTempEx();
    }

    public boolean SetVideoArc(MAPI_VIDEO_ARC_Type eArcIdx) {
        EnumVideoArcType arcType = EnumVideoArcType.E_DEFAULT;
        this.videoPara.enARCType = eArcIdx;
        this.databaseMgr.updateVideoBasePara(this.videoPara, this.f2com.GetCurrentInputSource().ordinal());
        if (!(S3DDeskImpl.getS3DMgrInstance(this.context).getSelfAdaptiveDetect() == EN_ThreeD_Video_SELFADAPTIVE_DETECT.DB_ThreeD_Video_SELF_ADAPTIVE_DETECT_OFF && S3DDeskImpl.getS3DMgrInstance(this.context).getDisplayFormat() == EN_ThreeD_Video_DISPLAYFORMAT.DB_ThreeD_Video_DISPLAYFORMAT_NONE)) {
            try {
                if (TvManager.getInstance() != null) {
                    TvManager.getInstance().setVideoMute(true, EnumScreenMuteType.E_BLACK, 0, this.f2com.GetCurrentInputSource());
                }
            } catch (TvCommonException e) {
                e.printStackTrace();
            }
        }
        if (eArcIdx.ordinal() < MAPI_VIDEO_ARC_Type.E_AR_MAX.ordinal()) {
            arcType = EnumVideoArcType.values()[eArcIdx.ordinal()];
        } else if (eArcIdx.ordinal() == MAPI_VIDEO_ARC_Type.E_AR_MAX.ordinal()) {
            arcType = EnumVideoArcType.E_16x9;
        }
        try {
            Log.e("zoom", "arcType===========" + arcType);
            if (!(TvManager.getInstance() == null || TvManager.getInstance().getPictureManager() == null)) {
                TvManager.getInstance().getPictureManager().setAspectRatio(arcType);
            }
        } catch (TvCommonException e2) {
            e2.printStackTrace();
        }
        if (!(S3DDeskImpl.getS3DMgrInstance(this.context).getSelfAdaptiveDetect() == EN_ThreeD_Video_SELFADAPTIVE_DETECT.DB_ThreeD_Video_SELF_ADAPTIVE_DETECT_OFF && S3DDeskImpl.getS3DMgrInstance(this.context).getDisplayFormat() == EN_ThreeD_Video_DISPLAYFORMAT.DB_ThreeD_Video_DISPLAYFORMAT_NONE)) {
            try {
                if (TvManager.getInstance() != null) {
                    TvManager.getInstance().setVideoMute(false, EnumScreenMuteType.E_BLACK, 0, this.f2com.GetCurrentInputSource());
                }
            } catch (TvCommonException e3) {
                this.f2com.printfE("S3DDeskImpl", "setVideoMute False Exception");
                e3.printStackTrace();
            }
        }
        return true;
    }

    public MAPI_VIDEO_ARC_Type GetVideoArc() {
        this.videoPara.enARCType = MAPI_VIDEO_ARC_Type.values()[this.databaseMgr.queryVideoArcMode(this.f2com.GetCurrentInputSource().ordinal())];
        this.f2com.printfE("TvService", "GetVideoArcIdx:" + this.videoPara.enARCType + "!!");
        return this.videoPara.enARCType;
    }

    public boolean SetNR(EN_MS_NR eNRIdx) {
        EnumNoiseReduction nrType = EnumNoiseReduction.E_NR_OFF;
        int idx = this.videoPara.astPicture[this.videoPara.ePicture.ordinal()].eColorTemp.ordinal();
        this.videoPara.eNRMode[idx].eNR = eNRIdx;
        this.databaseMgr.updateVideoNRMode(this.videoPara.eNRMode[idx], this.f2com.GetCurrentInputSource().ordinal(), idx);
        switch ($SWITCH_TABLE$com$konka$kkinterface$tv$DataBaseDesk$EN_MS_NR()[eNRIdx.ordinal()]) {
            case 1:
                nrType = EnumNoiseReduction.E_NR_OFF;
                break;
            case 2:
                nrType = EnumNoiseReduction.E_NR_LOW;
                break;
            case 3:
                nrType = EnumNoiseReduction.E_NR_MIDDLE;
                break;
            case 4:
                nrType = EnumNoiseReduction.E_NR_HIGH;
                break;
            case 5:
                nrType = EnumNoiseReduction.E_NR_AUTO;
                break;
        }
        try {
            if (!(TvManager.getInstance() == null || TvManager.getInstance().getPictureManager() == null)) {
                TvManager.getInstance().getPictureManager().setNoiseReduction(nrType);
            }
        } catch (TvCommonException e) {
            e.printStackTrace();
        }
        return true;
    }

    public EN_MS_NR GetNR() {
        int idx = this.videoPara.astPicture[this.videoPara.ePicture.ordinal()].eColorTemp.ordinal();
        this.f2com.printfE("TvService", "GetNR:" + this.videoPara.eNRMode[idx].eNR + "!!");
        return this.videoPara.eNRMode[idx].eNR;
    }

    public boolean SetMpegNR(EN_MS_MPEG_NR eMpNRIdx) {
        EnumMpegNoiseReduction mpegnrType = EnumMpegNoiseReduction.E_MPEG_NR_OFF;
        int idx = this.videoPara.astPicture[this.videoPara.ePicture.ordinal()].eColorTemp.ordinal();
        this.f2com.printfE("TvService", "SetMpegNR nothing to do!!");
        this.videoPara.eNRMode[idx].eMPEG_NR = eMpNRIdx;
        this.databaseMgr.updateVideoNRMode(this.videoPara.eNRMode[idx], this.f2com.GetCurrentInputSource().ordinal(), idx);
        switch ($SWITCH_TABLE$com$konka$kkinterface$tv$DataBaseDesk$EN_MS_MPEG_NR()[eMpNRIdx.ordinal()]) {
            case 1:
                mpegnrType = EnumMpegNoiseReduction.E_MPEG_NR_OFF;
                break;
            case 2:
                mpegnrType = EnumMpegNoiseReduction.E_MPEG_NR_LOW;
                break;
            case 3:
                mpegnrType = EnumMpegNoiseReduction.E_MPEG_NR_MIDDLE;
                break;
            case 4:
                mpegnrType = EnumMpegNoiseReduction.E_MPEG_NR_HIGH;
                break;
            case 5:
                mpegnrType = EnumMpegNoiseReduction.E_MPEG_NR_NUM;
                break;
        }
        try {
            if (!(TvManager.getInstance() == null || TvManager.getInstance().getPictureManager() == null)) {
                TvManager.getInstance().getPictureManager().setMpegNoiseReduction(mpegnrType);
            }
        } catch (TvCommonException e) {
            e.printStackTrace();
        }
        return true;
    }

    public EN_MS_MPEG_NR GetMpegNR() {
        int idx = this.videoPara.astPicture[this.videoPara.ePicture.ordinal()].eColorTemp.ordinal();
        this.f2com.printfE("TvService", "GetMpegNR:" + this.videoPara.eNRMode[idx].eMPEG_NR + "!!");
        return this.videoPara.eNRMode[idx].eMPEG_NR;
    }

    public short GetPCHPos() {
        return (short) this.databaseMgr.queryPCHPos();
    }

    public short GetPCModeIndex(int id) {
        return (short) this.databaseMgr.queryPCModeIndex(id);
    }

    public boolean SetPCHPos(short hpos) {
        this.f2com.printfE("TvService", "SetPCHPos Parameter!!");
        try {
            if (TvManager.getInstance() != null) {
                TvManager.getInstance().getPlayerManager().setHPosition(hpos);
            }
        } catch (TvCommonException e) {
            e.printStackTrace();
        }
        return true;
    }

    public short GetPCVPos() {
        return (short) this.databaseMgr.queryPCVPos();
    }

    public boolean SetPCVPos(short vpos) {
        this.f2com.printfE("TvService", "SetPCVPos Parameter!!");
        try {
            if (TvManager.getInstance() != null) {
                TvManager.getInstance().getPlayerManager().setVPosition(vpos);
            }
        } catch (TvCommonException e) {
            e.printStackTrace();
        }
        return true;
    }

    public short GetPCClock() {
        return (short) this.databaseMgr.queryPCClock();
    }

    public boolean SetPCClock(short clock) {
        this.f2com.printfE("TvService", "SetPCVPos Parameter!!");
        try {
            if (TvManager.getInstance() != null) {
                TvManager.getInstance().getPlayerManager().setSize(clock);
            }
        } catch (TvCommonException e) {
            e.printStackTrace();
        }
        return true;
    }

    public short GetPCPhase() {
        return (short) this.databaseMgr.queryPCPhase();
    }

    public boolean SetPCPhase(short phase) {
        this.f2com.printfE("TvService", "SetPCPhase Parameter!!");
        try {
            if (TvManager.getInstance() != null) {
                TvManager.getInstance().getPlayerManager().setPhase(phase);
            }
        } catch (TvCommonException e) {
            e.printStackTrace();
        }
        return true;
    }

    public boolean ExecAutoPc() {
        this.f2com.printfE("TvService", "ExecAutoPc: Start!!");
        new Thread(new Runnable() {
            public void run() {
                if (PictureDeskImpl.this.getHandler(1) != null) {
                    boolean autotune_flag = false;
                    System.out.println("start run");
                    PictureDeskImpl.this.getHandler(1).sendEmptyMessage(1);
                    try {
                        if (TvManager.getInstance() != null) {
                            autotune_flag = TvManager.getInstance().getPlayerManager().startPcModeAtuoTune();
                        }
                    } catch (TvCommonException e) {
                        e.printStackTrace();
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e2) {
                        e2.printStackTrace();
                    }
                    if (autotune_flag) {
                        if (PictureDeskImpl.this.getHandler(1) != null) {
                            PictureDeskImpl.this.getHandler(1).sendEmptyMessage(2);
                        }
                    } else if (PictureDeskImpl.this.getHandler(1) != null) {
                        PictureDeskImpl.this.getHandler(1).sendEmptyMessage(3);
                    }
                }
            }
        }).start();
        this.f2com.printfE("TvService", "ExecAutoPc: End!!");
        return true;
    }

    public void setDisplayWindow(VideoWindowType videoWindowType) {
        try {
            if (TvManager.getInstance() != null && TvManager.getInstance().getPictureManager() != null) {
                TvManager.getInstance().getPictureManager().selectWindow(EnumScalerWindow.E_MAIN_WINDOW);
                TvManager.getInstance().getPictureManager().setDisplayWindow(videoWindowType);
                TvManager.getInstance().getPictureManager().scaleWindow();
            }
        } catch (TvCommonException e) {
            e.printStackTrace();
        }
    }

    public short getDlcAverageLuma() {
        try {
            if (TvManager.getInstance() == null || TvManager.getInstance().getPictureManager() == null) {
                return 0;
            }
            return TvManager.getInstance().getPictureManager().getDlcAverageLuma();
        } catch (TvCommonException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public void enableBacklight() {
        try {
            if (TvManager.getInstance() != null && TvManager.getInstance().getPictureManager() != null) {
                TvManager.getInstance().getPictureManager().enableBacklight();
            }
        } catch (TvCommonException e) {
            e.printStackTrace();
        }
    }

    public void disableBacklight() {
        try {
            if (TvManager.getInstance() != null && TvManager.getInstance().getPictureManager() != null) {
                TvManager.getInstance().getPictureManager().disableBacklight();
            }
        } catch (TvCommonException e) {
            e.printStackTrace();
        }
    }

    public int[] getDlcLumArray(int dlcLumArrayLength) {
        try {
            if (!(TvManager.getInstance() == null || TvManager.getInstance().getPictureManager() == null)) {
                return TvManager.getInstance().getPictureManager().getDlcLumArray(dlcLumArrayLength);
            }
        } catch (TvCommonException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int getCustomerPqRuleNumber() {
        try {
            if (TvManager.getInstance() == null || TvManager.getInstance().getPictureManager() == null) {
                return 0;
            }
            return TvManager.getInstance().getPictureManager().getCustomerPqRuleNumber();
        } catch (TvCommonException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public int getStatusNumberByCustomerPqRule(int ruleType) {
        try {
            if (TvManager.getInstance() == null || TvManager.getInstance().getPictureManager() == null) {
                return 0;
            }
            return TvManager.getInstance().getPictureManager().getStatusNumberByCustomerPqRule(ruleType);
        } catch (TvCommonException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public boolean setStatusByCustomerPqRule(int ruleType, int ruleStatus) {
        try {
            if (!(TvManager.getInstance() == null || TvManager.getInstance().getPictureManager() == null)) {
                return TvManager.getInstance().getPictureManager().setStatusByCustomerPqRule(ruleType, ruleStatus);
            }
        } catch (TvCommonException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean SetKTVVideoArc() {
        new DtvEventComponentInfo().setAspectRatioCode(EnumAspectRatioCode.E_ASP_16TO9);
        return true;
    }

    public int[] getDynamicContrastCurve() {
        int[] ret = null;
        try {
            if (TvManager.getInstance() == null || TvManager.getInstance().getPictureManager() == null) {
                return ret;
            }
            return TvManager.getInstance().getPictureManager().getDynamicContrastCurve();
        } catch (TvCommonException e) {
            e.printStackTrace();
            return ret;
        }
    }

    public SkinToneMode getSkinToneMode() {
        return this.videoPara.skinTone;
    }

    public boolean setSkinToneMode(SkinToneMode mode) {
        MST_GRule_SKIN_TONE_Index_Main index;
        this.videoPara.skinTone = mode;
        this.databaseMgr.updateVideoBasePara(this.videoPara, this.f2com.GetCurrentInputSource().ordinal());
        MST_GRule_Index_Main type = MST_GRule_Index_Main.PQ_GRule_SKIN_TONE_Main_Color;
        switch ($SWITCH_TABLE$com$konka$kkinterface$tv$DataBaseDesk$SkinToneMode()[mode.ordinal()]) {
            case 2:
                index = MST_GRule_SKIN_TONE_Index_Main.PQ_GRule_SKIN_TONE_Red_Main;
                break;
            case 3:
                index = MST_GRule_SKIN_TONE_Index_Main.PQ_GRule_SKIN_TONE_Yellow_Main;
                break;
            default:
                index = MST_GRule_SKIN_TONE_Index_Main.PQ_GRule_SKIN_TONE_Off_Main;
                break;
        }
        try {
            if (!(TvManager.getInstance() == null || TvManager.getInstance().getPictureManager() == null)) {
                TvManager.getInstance().getPictureManager().setStatusByCustomerPqRule(type.ordinal(), index.ordinal());
            }
        } catch (TvCommonException e) {
            e.printStackTrace();
        }
        return true;
    }

    public boolean getDetailEnhance() {
        return this.videoPara.detailEnhance;
    }

    public boolean setDetailEnhance(boolean status) {
        MST_GRule_DETAILS_Index_Main index;
        this.videoPara.detailEnhance = status;
        this.databaseMgr.updateVideoBasePara(this.videoPara, this.f2com.GetCurrentInputSource().ordinal());
        MST_GRule_Index_Main type = MST_GRule_Index_Main.PQ_GRule_DETAILS_Main_Color;
        if (status) {
            index = MST_GRule_DETAILS_Index_Main.PQ_GRule_DETAILS_On_Main;
        } else {
            index = MST_GRule_DETAILS_Index_Main.PQ_GRule_DETAILS_Off_Main;
        }
        try {
            if (!(TvManager.getInstance() == null || TvManager.getInstance().getPictureManager() == null)) {
                TvManager.getInstance().getPictureManager().setStatusByCustomerPqRule(type.ordinal(), index.ordinal());
            }
        } catch (TvCommonException e) {
            e.printStackTrace();
        }
        return true;
    }

    public EN_MS_NR getDNR() {
        return this.videoPara.DNR;
    }

    public boolean setDNR(EN_MS_NR mode) {
        this.videoPara.DNR = mode;
        this.databaseMgr.updateVideoBasePara(this.videoPara, this.f2com.GetCurrentInputSource().ordinal());
        try {
            EnumNoiseReduction nr = EnumNoiseReduction.E_NR_AUTO;
            switch ($SWITCH_TABLE$com$konka$kkinterface$tv$DataBaseDesk$EN_MS_NR()[mode.ordinal()]) {
                case 1:
                    nr = EnumNoiseReduction.E_NR_OFF;
                    break;
                case 2:
                    nr = EnumNoiseReduction.E_NR_LOW;
                    break;
                case 3:
                    nr = EnumNoiseReduction.E_NR_MIDDLE;
                    break;
                case 4:
                    nr = EnumNoiseReduction.E_NR_HIGH;
                    break;
                case 5:
                    nr = EnumNoiseReduction.E_NR_AUTO;
                    break;
            }
            if (!(TvManager.getInstance() == null || TvManager.getInstance().getPictureManager() == null)) {
                TvManager.getInstance().getPictureManager().setNoiseReduction(nr);
            }
        } catch (TvCommonException e) {
            e.printStackTrace();
        }
        return true;
    }

    public EN_MS_Dynamic_Contrast getDynamicContrast() {
        return this.videoPara.eDynamic_Contrast;
    }

    public boolean setDynamicContrast(EN_MS_Dynamic_Contrast mode) {
        MST_GRule_COLOR_LEVEL_STRETCH_Index_Main index;
        this.videoPara.eDynamic_Contrast = mode;
        this.databaseMgr.updateVideoBasePara(this.videoPara, this.f2com.GetCurrentInputSource().ordinal());
        MST_GRule_Index_Main type = MST_GRule_Index_Main.PQ_GRule_COLOR_LEVEL_STRTECH_Main_Color;
        switch ($SWITCH_TABLE$com$konka$kkinterface$tv$DataBaseDesk$EN_MS_Dynamic_Contrast()[mode.ordinal()]) {
            case 2:
                index = MST_GRule_COLOR_LEVEL_STRETCH_Index_Main.PQ_GRule_COLOR_LEVEL_STRETCH_On_Main;
                break;
            default:
                index = MST_GRule_COLOR_LEVEL_STRETCH_Index_Main.PQ_GRule_COLOR_LEVEL_STRETCH_Off_Main;
                break;
        }
        try {
            if (!(TvManager.getInstance() == null || TvManager.getInstance().getPictureManager() == null)) {
                TvManager.getInstance().getPictureManager().setStatusByCustomerPqRule(type.ordinal(), index.ordinal());
            }
        } catch (TvCommonException e) {
            e.printStackTrace();
        }
        return true;
    }

    public boolean getEnergyEnable() {
        return this.databaseMgr.getCustomerCfgMiscSetting().energyEnable;
    }

    public boolean setEnergyEnable(boolean enable) {
        return false;
    }

    public short getEnergyPercent() {
        return this.databaseMgr.getCustomerCfgMiscSetting().energyPercent;
    }

    public boolean setEnergyPercent(short percent) {
        return false;
    }

    public boolean refreshVideoPara() {
        this.videoPara = this.databaseMgr.getVideo();
        return true;
    }
}
