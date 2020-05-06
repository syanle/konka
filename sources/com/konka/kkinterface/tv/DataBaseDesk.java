package com.konka.kkinterface.tv;

import com.mstar.android.tvapi.common.vo.TvOsType.EnumInputSource;
import com.mstar.android.tvapi.common.vo.TvOsType.EnumLanguage;
import com.mstar.android.tvapi.dtv.dvb.dvbc.vo.EnumChinaDvbcRegion;
import java.lang.reflect.Array;

public interface DataBaseDesk {
    public static final int EN_3D_SELFADAPTIVE_LEVEL_HIGH = 2;
    public static final int EN_3D_SELFADAPTIVE_LEVEL_LOW = 0;
    public static final int EN_3D_SELFADAPTIVE_LEVEL_MAX = 3;
    public static final int EN_3D_SELFADAPTIVE_LEVEL_MIDDLE = 1;
    public static final int EN_AUDIO_HIDEV_BW_L1 = 1;
    public static final int EN_AUDIO_HIDEV_BW_L2 = 2;
    public static final int EN_AUDIO_HIDEV_BW_L3 = 3;
    public static final int EN_AUDIO_HIDEV_BW_MAX = 4;
    public static final int EN_AUDIO_HIDEV_OFF = 0;
    public static final int EN_POWER_MODE_DIRECT = 2;
    public static final int EN_POWER_MODE_MAX = 3;
    public static final int EN_POWER_MODE_MEMORY = 1;
    public static final int EN_POWER_MODE_SECONDARY = 0;
    public static final int FACTORY_PRE_SET_ATV = 0;
    public static final int FACTORY_PRE_SET_DTV = 1;
    public static final int FACTORY_PRE_SET_NUM = 2;
    public static final int TEST_PATTERN_MODE_BLACK = 4;
    public static final int TEST_PATTERN_MODE_BLUE = 3;
    public static final int TEST_PATTERN_MODE_GRAY = 0;
    public static final int TEST_PATTERN_MODE_GREEN = 2;
    public static final int TEST_PATTERN_MODE_OFF = 5;
    public static final int TEST_PATTERN_MODE_RED = 1;
    public static final short T_3DInfo_IDX = 0;
    public static final short T_3DSetting_IDX = 1;
    public static final short T_ADCAdjust_IDX = 36;
    public static final short T_ATVDefaultPrograms_IDX = 49;
    public static final short T_BlockSysSetting_IDX = 2;
    public static final short T_CECSetting_IDX = 3;
    public static final short T_CISettineUpInfo_IDX = 19;
    public static final short T_CISetting_IDX = 4;
    public static final short T_ChinaDVBCSetting_IDX = 4;
    public static final short T_DB_VERSION_IDX = 5;
    public static final short T_DTVDefaultPrograms_IDX = 50;
    public static final short T_DTVOverscanSetting_IDX = 48;
    public static final short T_DvbtPresetting_IDX = 6;
    public static final short T_EpgTimer_IDX = 7;
    public static final short T_FacrotyColorTempEx_IDX = 38;
    public static final short T_FacrotyColorTemp_IDX = 37;
    public static final short T_FactoryExtern_IDX = 39;
    public static final short T_Factory_DB_VERSION_IDX = 45;
    public static final short T_FavTypeName_IDX = 8;
    public static final short T_HDMIOverscanSetting_IDX = 46;
    public static final short T_InputSource_Type_IDX = 9;
    public static final short T_IsdbSysSetting_IDX = 10;
    public static final short T_IsdbUserSetting_IDX = 11;
    public static final short T_MAX_IDX = 52;
    public static final short T_MediumSetting_IDX = 12;
    public static final short T_MfcMode_IDX = 13;
    public static final short T_NRMode_IDX = 14;
    public static final short T_NitInfo_IDX = 15;
    public static final short T_Nit_TSInfo_IDX = 16;
    public static final short T_NonLinearAdjust_IDX = 42;
    public static final short T_NonStarndardAdjust_IDX = 40;
    public static final short T_OADInfo_IDX = 17;
    public static final short T_OADInfo_UntDescriptor_IDX = 18;
    public static final short T_OverscanAdjust_IDX = 43;
    public static final short T_PEQAdjust_IDX = 44;
    public static final short T_PicMode_Setting_IDX = 20;
    public static final short T_PipSetting_IDX = 21;
    public static final short T_SRSAdjust_IDX = 51;
    public static final short T_SSCAdjust_IDX = 41;
    public static final short T_SoundMode_Setting_IDX = 22;
    public static final short T_SoundSetting_IDX = 23;
    public static final short T_SubtitleSetting_IDX = 24;
    public static final short T_SystemSetting_IDX = 25;
    public static final short T_ThreeDVideoMode_IDX = 26;
    public static final short T_ThreeDVideoRouterSetting_IDX = 35;
    public static final short T_TimeSetting_IDX = 27;
    public static final short T_USER_COLORTEMP_EX_IDX = 29;
    public static final short T_USER_COLORTEMP_IDX = 28;
    public static final short T_UserLocationSetting_IDX = 30;
    public static final short T_UserMMSetting_IDX = 31;
    public static final short T_UserOverScanMode_IDX = 32;
    public static final short T_UserPCModeSetting_IDX = 33;
    public static final short T_VideoSetting_IDX = 34;
    public static final short T_YPbPrOverscanSetting_IDX = 47;

    public enum AUDIOMODE_TYPE_ {
        E_AUDIOMODE_INVALID_,
        E_AUDIOMODE_MONO_,
        E_AUDIOMODE_FORCED_MONO_,
        E_AUDIOMODE_G_STEREO_,
        E_AUDIOMODE_K_STEREO_,
        E_AUDIOMODE_MONO_SAP_,
        E_AUDIOMODE_STEREO_SAP_,
        E_AUDIOMODE_DUAL_A_,
        E_AUDIOMODE_DUAL_B_,
        E_AUDIOMODE_DUAL_AB_,
        E_AUDIOMODE_NICAM_MONO_,
        E_AUDIOMODE_NICAM_STEREO_,
        E_AUDIOMODE_NICAM_DUAL_A_,
        E_AUDIOMODE_NICAM_DUAL_B_,
        E_AUDIOMODE_NICAM_DUAL_AB_,
        E_AUDIOMODE_HIDEV_MONO_,
        E_AUDIOMODE_LEFT_LEFT_,
        E_AUDIOMODE_RIGHT_RIGHT_,
        E_AUDIOMODE_LEFT_RIGHT_
    }

    public static class AUDIO_PEQ_PARAM {
        public int Band = 3;
        public int Foh = 80;
        public int Fol = 45;
        public int Gain = 120;
        public int QValue = 80;
    }

    public enum ColorWheelMode {
        MODE_OFF,
        MODE_ON,
        MODE_DEMO
    }

    public static class CustomerCfgMiscSetting {
        public boolean energyEnable = false;
        public short energyPercent = 50;
    }

    public enum EN_3D_INPUT_TYPE {
        E_3D_INPUT_FRAME_PACKING,
        E_3D_INPUT_FIELD_ALTERNATIVE,
        E_3D_INPUT_LINE_ALTERNATIVE,
        E_3D_INPUT_SIDE_BY_SIDE_FULL,
        E_3D_INPUT_L_DEPTH,
        E_3D_INPUT_L_DEPTH_GRAPHICS_GRAPHICS_DEPTH,
        E_3D_INPUT_TOP_BOTTOM,
        E_3D_INPUT_SIDE_BY_SIDE_HALF,
        E_3D_INPUT_CHECK_BORAD,
        E_3D_INPUT_MODE_USER,
        E_3D_INPUT_FRAME_ALTERNATIVE,
        E_3D_INPUT_SIDE_BY_SIDE_HALF_INTERLACE,
        E_3D_INPUT_FRAME_PACKING_OPT,
        E_3D_INPUT_TOP_BOTTOM_OPT,
        E_3D_INPUT_NORMAL_2D,
        E_3D_INPUT_NORMAL_2D_INTERLACE,
        E_3D_INPUT_NORMAL_2D_INTERLACE_PTP,
        E_3D_INPUT_NORMAL_2D_HW,
        E_3D_INPUT_TYPE_NUM
    }

    public enum EN_3D_OUTPUT_TYPE {
        E_3D_OUTPUT_MODE_NONE,
        E_3D_OUTPUT_LINE_ALTERNATIVE,
        E_3D_OUTPUT_TOP_BOTTOM,
        E_3D_OUTPUT_SIDE_BY_SIDE_HALF,
        E_3D_OUTPUT_FRAME_ALTERNATIVE,
        E_3D_OUTPUT_FRAME_L,
        E_3D_OUTPUT_FRAME_R,
        E_3D_OUTPUT_FRAME_ALTERNATIVE_NOFRC,
        E_3D_OUTPUT_LINE_ALTERNATIVE_HW,
        E_3D_OUTPUT_TYPE_NUM
    }

    public enum EN_AUDYSSEY_DYNAMIC_VOLUME_MODE {
        AUDYSSEY_DYNAMIC_VOLUME_OFF,
        AUDYSSEY_DYNAMIC_VOLUME_ON,
        AUDYSSEY_DYNAMIC_VOLUME_NUM
    }

    public enum EN_AUDYSSEY_EQ_MODE {
        AUDYSSEY_EQ_OFF,
        AUDYSSEY_EQ_ON,
        AUDYSSEY_EQ_NUM
    }

    public enum EN_AUD_MODE {
        AUD_MODE_LR,
        AUD_MODE_LL,
        AUD_MODE_RR,
        AUD_MODE_NUM
    }

    public enum EN_CABLE_OPERATORS {
        EN_CABLEOP_OTHER,
        EN_CABLEOP_CDSMATV,
        EN_CABLEOP_CDCABLE,
        EN_CABLEOP_COMHEM,
        EN_CABLEOP_UPC,
        EN_CABLEOP_YOUSEE,
        EN_CABLEOP_CABLEREADY,
        EN_CABLEOP_ZIGGO,
        EN_CABLEOP_NUM
    }

    public enum EN_CI_FUNCTION {
        EN_CI_FUNCTION_RM,
        EN_CI_FUNCTION_APPINFO,
        EN_CI_FUNCTION_CAS,
        EN_CI_FUNCTION_HC,
        EN_CI_FUNCTION_DT,
        EN_CI_FUNCTION_MMI,
        EN_CI_FUNCTION_LSC,
        EN_CI_FUNCTION_CC,
        EN_CI_FUNCTION_HLC,
        EN_CI_FUNCTION_CU,
        EN_CI_FUNCTION_OP,
        EN_CI_FUNCTION_SAS,
        EN_CI_FUNCTION_APPMMI,
        EN_CI_FUNCTION_PMT,
        EN_CI_FUNCTION_HSS,
        EN_CI_FUNCTION_AUTH,
        EN_CI_FUNCTION_DEFAULT,
        EN_CI_FUNCTION_DEBUG_COUNT
    }

    public enum EN_DISPLAY_RES_TYPE {
        DISPLAY_SEC32_LE32A_FULLHD,
        DISPLAY_RES_SXGA,
        DISPLAY_RES_WXGA,
        DISPLAY_RES_WXGA_PLUS,
        DISPLAY_RES_WSXGA,
        DISPLAY_RES_FULL_HD,
        DISPLAY_DACOUT_576I,
        DISPLAY_DACOUT_576P,
        DISPLAY_DACOUT_720P_50,
        DISPLAY_DACOUT_1080P_24,
        DISPLAY_DACOUT_1080P_25,
        DISPLAY_DACOUT_1080I_50,
        DISPLAY_DACOUT_1080P_50,
        DISPLAY_DACOUT_480I,
        DISPLAY_DACOUT_480P,
        DISPLAY_DACOUT_720P_60,
        DISPLAY_DACOUT_1080P_30,
        DISPLAY_DACOUT_1080I_60,
        DISPLAY_DACOUT_1080P_60,
        DISPLAY_DACOUT_AUTO,
        DISPLAY_CMO_CMO260J2_WUXGA,
        DISPLAY_VGAOUT_640x480P_60,
        DISPLAY_TTLOUT_480X272_60,
        DISPLAY_RES_MAX_NUM
    }

    public enum EN_DISPLAY_TVFORMAT {
        DISPLAY_TVFORMAT_4TO3,
        DISPLAY_TVFORMAT_16TO9SD,
        DISPLAY_TVFORMAT_16TO9HD,
        DISPLAY_TVFORMAT_COUNT
    }

    public enum EN_MFC {
        MS_MFC_OFF,
        MS_MFC_LOW,
        MS_MFC_HIGH,
        MS_MFC_COUNT
    }

    public enum EN_MS_CHANNEL_SWITCH_MODE {
        MS_CHANNEL_SWM_BLACKSCREEN,
        MS_CHANNEL_SWM_FREEZE,
        MS_CHANNEL_SWM_NUM
    }

    public enum EN_MS_COLOR_TEMP {
        MS_COLOR_TEMP_COOL,
        MS_COLOR_TEMP_NATURE,
        MS_COLOR_TEMP_WARM,
        MS_COLOR_TEMP_USER,
        MS_COLOR_TEMP_NUM
    }

    public enum EN_MS_COLOR_TEMP_INPUT_SOURCE {
        E_INPUT_SOURCE_VGA,
        E_INPUT_SOURCE_ATV,
        E_INPUT_SOURCE_CVBS,
        E_INPUT_SOURCE_SVIDEO,
        E_INPUT_SOURCE_YPBPR,
        E_INPUT_SOURCE_SCART,
        E_INPUT_SOURCE_HDMI,
        E_INPUT_SOURCE_DTV,
        E_INPUT_SOURCE_OTHERS,
        E_INPUT_SOURCE_NUM,
        E_INPUT_SOURCE_NONE
    }

    public enum EN_MS_Dynamic_Contrast {
        MS_Dynamic_Contrast_OFF,
        MS_Dynamic_Contrast_ON,
        MS_Dynamic_Contrast_NUM
    }

    public enum EN_MS_FILM {
        MS_FILM_OFF,
        MS_FILM_ON,
        MS_FILM_NUM
    }

    public enum EN_MS_LOCALDIMMING {
        LOCALDIMMING_OFF,
        LOCALDIMMING_ON,
        LOCALDIMMING_NUMS
    }

    public enum EN_MS_MPEG_NR {
        MS_MPEG_NR_OFF,
        MS_MPEG_NR_LOW,
        MS_MPEG_NR_MIDDLE,
        MS_MPEG_NR_HIGH,
        MS_MPEG_NR_NUM
    }

    public enum EN_MS_NR {
        MS_NR_OFF,
        MS_NR_LOW,
        MS_NR_MIDDLE,
        MS_NR_HIGH,
        MS_NR_AUTO,
        MS_NR_NUM
    }

    public enum EN_MS_OFFLINE_DET_MODE {
        MS_OFFLINE_DET_OFF,
        MS_COFFLINE_DET_INDICATION,
        MS_COFFLINE_DET_AUTO,
        MS_OFFLINE_DET_NUM
    }

    public enum EN_MS_PICTURE {
        PICTURE_DYNAMIC,
        PICTURE_NORMAL,
        PICTURE_SOFT,
        PICTURE_USER,
        PICTURE_VIVID,
        PICTURE_NATURAL,
        PICTURE_SPORTS,
        PICTURE_NUMS
    }

    public enum EN_MS_PIC_ADV {
        MS_OFF,
        MS_LOW,
        MS_MIDDLE,
        MS_HIGH,
        MS_AUTO,
        MS_NUM
    }

    public enum EN_MS_POWERON_LOGO {
        MS_POWERON_LOGO_OFF,
        MS_POWERON_LOGO_DEFAULT,
        MS_POWERON_LOGO_USER,
        MS_POWERON_LOGO_NUM
    }

    public enum EN_MS_POWERON_MUSIC {
        MS_POWERON_MUSIC_OFF,
        MS_POWERON_MUSIC_DEFAULT,
        MS_POWERON_MUSIC_USER,
        MS_POWERON_MUSIC_NUM
    }

    public enum EN_MS_SUPER {
        MS_SUPER_MIN,
        MS_SUPER_OFF,
        MS_SUPER_ON,
        MS_SUPER_NUM
    }

    public enum EN_MS_VIDEOITEM {
        MS_VIDEOITEM_BRIGHTNESS,
        MS_VIDEOITEM_CONTRAST,
        MS_VIDEOITEM_SATURATION,
        MS_VIDEOITEM_SHARPNESS,
        MS_VIDEOITEM_HUE,
        MS_VIDEOITEM_BACKLIGHT,
        MS_VIDEOITEM_NUM
    }

    public enum EN_SATELLITE_PLATFORM {
        EN_SATEPF_OTHER,
        EN_SATEPF_HDPLUS,
        EN_SATEPF_FREESAT
    }

    public enum EN_SOUND_AD_OUTPUT {
        AD_SPEAKER,
        AD_HEADPHONE,
        AD_BOTH
    }

    public enum EN_SOUND_MODE {
        SOUND_MODE_STANDARD,
        SOUND_MODE_MUSIC,
        SOUND_MODE_MOVIE,
        SOUND_MODE_NEWS,
        SOUND_MODE_USER,
        SOUND_MODE_ONSITE1,
        SOUND_MODE_ONSITE2,
        SOUND_MODE_NUM
    }

    public enum EN_SPDIF_MODE {
        PDIF_MODE_OFF,
        PDIF_MODE_PCM,
        PDIF_MODE_RAW
    }

    public enum EN_SRS_SET_TYPE {
        E_SRS_INPUTGAIN,
        E_SRS_SURRLEVEL_CONTROL,
        E_SRS_SPEAKERAUDIO,
        E_SRS_SPEAKERANALYSIS,
        E_SRS_TRUBASS_CONTROL,
        E_SRS_DC_CONTROL,
        E_SRS_DEFINITION_CONTROL
    }

    public enum EN_SURROUND_MODE {
        E_SURROUND_MODE_OFF,
        E_SURROUND_MODE_ON
    }

    public enum EN_SURROUND_SYSTEM_TYPE {
        SURROUND_SYSTEM_OFF,
        SURROUND_SYSTEM_BBE,
        SURROUND_SYSTEM_SRS,
        SURROUND_SYSTEM_VDS,
        SURROUND_SYSTEM_VSPK,
        SURROUND_SYSTEM_SURROUNDMAX,
        SURROUND_SYSTEM_NUMS
    }

    public enum EN_SURROUND_TYPE {
        SURROUND_MODE_MOUNTAIN,
        SURROUND_MODE_CHAMPAIGN,
        SURROUND_MODE_CITY,
        SURROUND_MODE_THEATER,
        SURROUND_MODE_NUM
    }

    public enum EN_SYSTEM_FACTORY_DB_COMMAND {
        E_FACTORY_COLOR_TEMP_SET,
        E_FACTORY_VIDEO_ADC_SET,
        E_FACTORY_RESTORE_DEFAULT,
        E_USER_RESTORE_DEFAULT,
        E_FACTORY_COMMAND_NUM
    }

    public enum EN_ThreeD_Video {
        DB_ThreeD_Video_OFF,
        DB_ThreeD_Video_2D_TO_3D,
        DB_ThreeD_Video_SIDE_BY_SIDE,
        DB_ThreeD_Video_TOP_BOTTOM,
        DB_ThreeD_Video_FRAME_INTERLEAVING,
        DB_ThreeD_Video_PACKING_1080at24p,
        DB_ThreeD_Video_PACKING_720at60p,
        DB_ThreeD_Video_PACKING_720at50p,
        DB_ThreeD_Video_CHESS_BOARD,
        DB_ThreeD_Video_COUNT
    }

    public enum EN_ThreeD_Video_3DDEPTH {
        DB_ThreeD_Video_3DDEPTH_LEVEL_0,
        DB_ThreeD_Video_3DDEPTH_LEVEL_1,
        DB_ThreeD_Video_3DDEPTH_LEVEL_2,
        DB_ThreeD_Video_3DDEPTH_LEVEL_3,
        DB_ThreeD_Video_3DDEPTH_LEVEL_4,
        DB_ThreeD_Video_3DDEPTH_LEVEL_5,
        DB_ThreeD_Video_3DDEPTH_LEVEL_6,
        DB_ThreeD_Video_3DDEPTH_LEVEL_7,
        DB_ThreeD_Video_3DDEPTH_LEVEL_8,
        DB_ThreeD_Video_3DDEPTH_LEVEL_9,
        DB_ThreeD_Video_3DDEPTH_LEVEL_10,
        DB_ThreeD_Video_3DDEPTH_LEVEL_11,
        DB_ThreeD_Video_3DDEPTH_LEVEL_12,
        DB_ThreeD_Video_3DDEPTH_LEVEL_13,
        DB_ThreeD_Video_3DDEPTH_LEVEL_14,
        DB_ThreeD_Video_3DDEPTH_LEVEL_15,
        DB_ThreeD_Video_3DDEPTH_LEVEL_16,
        DB_ThreeD_Video_3DDEPTH_LEVEL_17,
        DB_ThreeD_Video_3DDEPTH_LEVEL_18,
        DB_ThreeD_Video_3DDEPTH_LEVEL_19,
        DB_ThreeD_Video_3DDEPTH_LEVEL_20,
        DB_ThreeD_Video_3DDEPTH_LEVEL_21,
        DB_ThreeD_Video_3DDEPTH_LEVEL_22,
        DB_ThreeD_Video_3DDEPTH_LEVEL_23,
        DB_ThreeD_Video_3DDEPTH_LEVEL_24,
        DB_ThreeD_Video_3DDEPTH_LEVEL_25,
        DB_ThreeD_Video_3DDEPTH_LEVEL_26,
        DB_ThreeD_Video_3DDEPTH_LEVEL_27,
        DB_ThreeD_Video_3DDEPTH_LEVEL_28,
        DB_ThreeD_Video_3DDEPTH_LEVEL_29,
        DB_ThreeD_Video_3DDEPTH_LEVEL_30,
        DB_ThreeD_Video_3DDEPTH_LEVEL_31,
        DB_ThreeD_Video_3DDEPTH_COUNT
    }

    public enum EN_ThreeD_Video_3DOFFSET {
        DB_ThreeD_Video_3DOFFSET_LEVEL_0,
        DB_ThreeD_Video_3DOFFSET_LEVEL_1,
        DB_ThreeD_Video_3DOFFSET_LEVEL_2,
        DB_ThreeD_Video_3DOFFSET_LEVEL_3,
        DB_ThreeD_Video_3DOFFSET_LEVEL_4,
        DB_ThreeD_Video_3DOFFSET_LEVEL_5,
        DB_ThreeD_Video_3DOFFSET_LEVEL_6,
        DB_ThreeD_Video_3DOFFSET_LEVEL_7,
        DB_ThreeD_Video_3DOFFSET_LEVEL_8,
        DB_ThreeD_Video_3DOFFSET_LEVEL_9,
        DB_ThreeD_Video_3DOFFSET_LEVEL_10,
        DB_ThreeD_Video_3DOFFSET_LEVEL_11,
        DB_ThreeD_Video_3DOFFSET_LEVEL_12,
        DB_ThreeD_Video_3DOFFSET_LEVEL_13,
        DB_ThreeD_Video_3DOFFSET_LEVEL_14,
        DB_ThreeD_Video_3DOFFSET_LEVEL_15,
        DB_ThreeD_Video_3DOFFSET_LEVEL_16,
        DB_ThreeD_Video_3DOFFSET_LEVEL_17,
        DB_ThreeD_Video_3DOFFSET_LEVEL_18,
        DB_ThreeD_Video_3DOFFSET_LEVEL_19,
        DB_ThreeD_Video_3DOFFSET_LEVEL_20,
        DB_ThreeD_Video_3DOFFSET_LEVEL_21,
        DB_ThreeD_Video_3DOFFSET_LEVEL_22,
        DB_ThreeD_Video_3DOFFSET_LEVEL_23,
        DB_ThreeD_Video_3DOFFSET_LEVEL_24,
        DB_ThreeD_Video_3DOFFSET_LEVEL_25,
        DB_ThreeD_Video_3DOFFSET_LEVEL_26,
        DB_ThreeD_Video_3DOFFSET_LEVEL_27,
        DB_ThreeD_Video_3DOFFSET_LEVEL_28,
        DB_ThreeD_Video_3DOFFSET_LEVEL_29,
        DB_ThreeD_Video_3DOFFSET_LEVEL_30,
        DB_ThreeD_Video_3DOFFSET_LEVEL_31,
        DB_ThreeD_Video_3DOFFSET_COUNT
    }

    public enum EN_ThreeD_Video_3DOUTPUTASPECT {
        DB_ThreeD_Video_3DOUTPUTASPECT_FULLSCREEN,
        DB_ThreeD_Video_3DOUTPUTASPECT_CENTER,
        DB_ThreeD_Video_3DOUTPUTASPECT_AUTOADAPTED,
        DB_ThreeD_Video_3DOUTPUTASPECT_COUNT
    }

    public enum EN_ThreeD_Video_3DTO2D {
        DB_ThreeD_Video_3DTO2D_NONE,
        DB_ThreeD_Video_3DTO2D_SIDE_BY_SIDE,
        DB_ThreeD_Video_3DTO2D_TOP_BOTTOM,
        DB_ThreeD_Video_3DTO2D_FRAME_PACKING,
        DB_ThreeD_Video_3DTO2D_LINE_ALTERNATIVE,
        DB_ThreeD_Video_3DTO2D_AUTO,
        DB_ThreeD_Video_3DTO2D_COUNT
    }

    public enum EN_ThreeD_Video_AUTOSTART {
        DB_ThreeD_Video_AUTOSTART_OFF,
        DB_ThreeD_Video_AUTOSTART_2D,
        DB_ThreeD_Video_AUTOSTART_3D,
        DB_ThreeD_Video_AUTOSTART_COUNT
    }

    public enum EN_ThreeD_Video_DISPLAYFORMAT {
        DB_ThreeD_Video_DISPLAYFORMAT_NONE,
        DB_ThreeD_Video_DISPLAYFORMAT_SIDE_BY_SIDE,
        DB_ThreeD_Video_DISPLAYFORMAT_TOP_BOTTOM,
        DB_ThreeD_Video_DISPLAYFORMAT_FRAME_PACKING,
        DB_ThreeD_Video_DISPLAYFORMAT_LINE_ALTERNATIVE,
        DB_ThreeD_Video_DISPLAYFORMAT_2DTO3D,
        DB_ThreeD_Video_DISPLAYFORMAT_AUTO,
        DB_ThreeD_Video_DISPLAYFORMAT_CHECK_BOARD,
        DB_ThreeD_Video_DISPLAYFORMAT_PIXEL_ALTERNATIVE,
        DB_ThreeD_Video_DISPLAYFORMAT_COUNT
    }

    public enum EN_ThreeD_Video_LRVIEWSWITCH {
        DB_ThreeD_Video_LRVIEWSWITCH_EXCHANGE,
        DB_ThreeD_Video_LRVIEWSWITCH_NOTEXCHANGE,
        DB_ThreeD_Video_LRVIEWSWITCH_COUNT
    }

    public enum EN_ThreeD_Video_SELFADAPTIVE_DETECT {
        DB_ThreeD_Video_SELF_ADAPTIVE_DETECT_OFF,
        DB_ThreeD_Video_SELF_ADAPTIVE_DETECT_RIGHT_NOW,
        DB_ThreeD_Video_SELF_ADAPTIVE_DETECT_WHEN_SOURCE_CHANGE,
        DB_ThreeD_Video_DISPLAYFORMAT_COUNT
    }

    public enum EN_ThreeD_Video_SELFADAPTIVE_LEVEL {
        DB_ThreeD_Video_SELF_ADAPTIVE_LOW,
        DB_ThreeD_Video_SELF_ADAPTIVE_MIDDLE,
        DB_ThreeD_Video_SELF_ADAPTIVE_HIGH,
        DB_ThreeD_Video_DISPLAYFORMAT_COUNT
    }

    public enum EN_VD_SIGNALTYPE {
        SIG_NTSC,
        SIG_PAL,
        SIG_SECAM,
        SIG_NTSC_443,
        SIG_PAL_M,
        SIG_PAL_NC,
        SIG_NUMS,
        SIG_NONE
    }

    public enum E_ADC_SET_INDEX {
        ADC_SET_VGA,
        ADC_SET_YPBPR_SD,
        ADC_SET_YPBPR_HD,
        ADC_SET_SCART_RGB,
        ADC_SET_YPBPR2_SD,
        ADC_SET_YPBPR2_HD,
        ADC_SET_YPBPR3_SD,
        ADC_SET_YPBPR3_HD,
        ADC_SET_NUMS
    }

    public enum EnumSwitchOnOff {
        SWITCH_OFF,
        SWITCH_ON
    }

    public enum HdmiAudioSource {
        AUDIO_SOURCE_HDMI,
        AUDIO_SOURCE_VGA
    }

    public static class KK_SRS_SET {
        public int srs_DCControl = 7;
        public int srs_DefinitionControl = 3;
        public int srs_InputGain = 6;
        public int srs_SpeakerAnalysis = 4;
        public int srs_SpeakerAudio = 9;
        public int srs_SurrLevelControl = 7;
        public int srs_TrubassControl = 5;
    }

    public enum MAPI_AVD_VideoStandardType {
        E_MAPI_VIDEOSTANDARD_PAL_BGHI,
        E_MAPI_VIDEOSTANDARD_NTSC_M,
        E_MAPI_VIDEOSTANDARD_SECAM,
        E_MAPI_VIDEOSTANDARD_NTSC_44,
        E_MAPI_VIDEOSTANDARD_PAL_M,
        E_MAPI_VIDEOSTANDARD_PAL_N,
        E_MAPI_VIDEOSTANDARD_PAL_60,
        E_MAPI_VIDEOSTANDARD_NOTSTANDARD,
        E_MAPI_VIDEOSTANDARD_AUTO,
        E_MAPI_VIDEOSTANDARD_MAX
    }

    public enum MAPI_VIDEO_3D_ARC_Type {
        E_3D_AR_FULL,
        E_3D_AR_AUTO,
        E_3D_AR_CENTER
    }

    public enum MAPI_VIDEO_ARC_Type {
        E_AR_DEFAULT,
        E_AR_16x9,
        E_AR_4x3,
        E_AR_AUTO,
        E_AR_Panorama,
        E_AR_JustScan,
        E_AR_Zoom1,
        E_AR_Zoom2,
        E_AR_14x9,
        E_AR_DotByDot,
        E_AR_Subtitle,
        E_AR_Movie,
        E_AR_Personal,
        E_AR_4x3_PanScan,
        E_AR_4x3_LetterBox,
        E_AR_16x9_PillarBox,
        E_AR_16x9_PanScan,
        E_AR_4x3_Combind,
        E_AR_16x9_Combind,
        E_AR_Zoom_2x,
        E_AR_Zoom_3x,
        E_AR_Zoom_4x,
        E_AR_MAX
    }

    public enum MAPI_VIDEO_OUT_VE_SYS {
        MAPI_VIDEO_OUT_VE_NTSC,
        MAPI_VIDEO_OUT_VE_PAL,
        MAPI_VIDEO_OUT_VE_AUTO
    }

    public enum MAX_DTV_Resolution_Info {
        E_DTV480i_60,
        E_DTV480p_60,
        E_DTV576i_50,
        E_DTV576p_50,
        E_DTV720p_60,
        E_DTV720p_50,
        E_DTV1080i_60,
        E_DTV1080i_50,
        E_DTV1080p_60,
        E_DTV1080p_50,
        E_DTV1080p_30,
        E_DTV1080p_24,
        E_DTV_MAX
    }

    public enum MAX_HDMI_Resolution_Info {
        E_HDMI480i_60,
        E_HDMI480p_60,
        E_HDMI576i_50,
        E_HDMI576p_50,
        E_HDMI720p_60,
        E_HDMI720p_50,
        E_HDMI1080i_60,
        E_HDMI1080i_50,
        E_HDMI1080p_60,
        E_HDMI1080p_50,
        E_HDMI1080p_30,
        E_HDMI1080p_24,
        E_HDMI1440x480i_60,
        E_HDMI1440x480p_60,
        E_HDMI1440x576i_50,
        E_HDMI1440x576p_50,
        E_HDMI_MAX
    }

    public enum MAX_YPbPr_Resolution_Info {
        E_YPbPr480i_60,
        E_YPbPr480p_60,
        E_YPbPr576i_50,
        E_YPbPr576p_50,
        E_YPbPr720p_60,
        E_YPbPr720p_50,
        E_YPbPr1080i_60,
        E_YPbPr1080i_50,
        E_YPbPr1080p_60,
        E_YPbPr1080p_50,
        E_YPbPr1080p_30,
        E_YPbPr1080p_24,
        E_YPbPr1080p_25,
        E_YPbPr_MAX
    }

    public enum MEMBER_COUNTRY {
        E_AUSTRALIA,
        E_AUSTRIA,
        E_BELGIUM,
        E_BULGARIA,
        E_CROATIA,
        E_CZECH,
        E_DENMARK,
        E_FINLAND,
        E_FRANCE,
        E_GERMANY,
        E_GREECE,
        E_HUNGARY,
        E_ITALY,
        E_LUXEMBOURG,
        E_NETHERLANDS,
        E_NORWAY,
        E_POLAND,
        E_PORTUGAL,
        E_RUMANIA,
        E_RUSSIA,
        E_SERBIA,
        E_SLOVENIA,
        E_SPAIN,
        E_SWEDEN,
        E_SWITZERLAND,
        E_UK,
        E_NEWZEALAND,
        E_ARAB,
        E_ESTONIA,
        E_HEBREW,
        E_LATVIA,
        E_SLOVAKIA,
        E_TURKEY,
        E_IRELAND,
        E_JAPAN,
        E_PHILIPPINES,
        E_THAILAND,
        E_MALDIVES,
        E_URUGUAY,
        E_PERU,
        E_ARGENTINA,
        E_CHILE,
        E_VENEZUELA,
        E_ECUADOR,
        E_COSTARICA,
        E_PARAGUAY,
        E_BOLIVIA,
        E_BELIZE,
        E_NICARAGUA,
        E_GUATEMALA,
        E_CHINA,
        E_TAIWAN,
        E_BRAZIL,
        E_CANADA,
        E_MEXICO,
        E_US,
        E_SOUTHKOREA,
        E_FIJI,
        E_UZBEK,
        E_TAJIKISTAN,
        E_ETHIOPIA,
        E_AZERBAIJAN,
        E_SOUTHAFRICA,
        E_ALGERIA,
        E_EGYPT,
        E_SAUDI_ARABIA,
        E_IRAN,
        E_IRAQ,
        E_NAMIBIA,
        E_JORDAN,
        E_KUWAIT,
        E_INDONESIA,
        E_ISRAEL,
        E_QATAR,
        E_NIGERIA,
        E_ZEMBABWE,
        E_LITHUANIA,
        E_MOROCCO,
        E_TUNIS,
        E_INDIA,
        E_OTHERS,
        E_COUNTRY_NUM
    }

    public static class MFC_MODE {
        EN_MFC eMFC;

        public MFC_MODE(EN_MFC eMode) {
            this.eMFC = eMode;
        }
    }

    public static class MS_ADC_SETTING {
        public T_MS_CALIBRATION_DATA[] stAdcGainOffsetSetting = new T_MS_CALIBRATION_DATA[E_ADC_SET_INDEX.ADC_SET_NUMS.ordinal()];
        public int u16CheckSum;

        public MS_ADC_SETTING() {
            for (int i = 0; i < E_ADC_SET_INDEX.ADC_SET_NUMS.ordinal(); i++) {
                this.stAdcGainOffsetSetting[i] = new T_MS_CALIBRATION_DATA(128, 128, 128, 128, 128, 128);
            }
        }
    }

    public static class MS_CEC_SETTING {
        public short mARCStatus;
        public short mAudioModeStatus;
        public short mAutoStandby;
        public short mCECStatus;
        public int mCheckSum;

        public MS_CEC_SETTING(int v1, short v2, short v3, short v4, short v5) {
            this.mCheckSum = v1;
            this.mCECStatus = v2;
            this.mAutoStandby = v3;
            this.mARCStatus = v4;
            this.mAudioModeStatus = v5;
        }
    }

    public static class MS_FACTORY_DTV_OVERSCAN_SETTING {
        public ST_MAPI_VIDEO_WINDOW_INFO[][] stVideoWinInfo = ((ST_MAPI_VIDEO_WINDOW_INFO[][]) Array.newInstance(ST_MAPI_VIDEO_WINDOW_INFO.class, new int[]{MAX_DTV_Resolution_Info.E_DTV_MAX.ordinal(), MAPI_VIDEO_ARC_Type.E_AR_MAX.ordinal()}));
        public int u16CheckSum;

        public MS_FACTORY_DTV_OVERSCAN_SETTING() {
            for (int i = 0; i < MAX_DTV_Resolution_Info.E_DTV_MAX.ordinal(); i++) {
                for (int j = 0; j < MAPI_VIDEO_ARC_Type.E_AR_MAX.ordinal(); j++) {
                    this.stVideoWinInfo[i][j] = new ST_MAPI_VIDEO_WINDOW_INFO();
                }
            }
        }
    }

    public static class MS_FACTORY_EXTERN_SETTING {
        public static String boardType = "A3";
        public static String dayAndTime = "2011.9.22 12:00";
        public static String panelType = "Full-HD";
        public static String softVersion = "0.0.1";
        public short audioDspVersion = 0;
        public short audioNrThr = 0;
        public short audioPreScale = 0;
        public short audioSifThreshold = 0;
        public boolean bBurnIn = false;
        public boolean dtvAvAbnormalDelay = false;
        public int eHidevMode = 0;
        public int factoryPreset = 0;
        public boolean m_bAgingMode = false;
        public short panelSwingVal = 0;
        public int st3DSelfAdaptiveLevel = 1;
        public int stPowerMode = 1;
        public int testPatternMode = 5;
        public int u16CheckSum;
        public short vdDspVersion = 0;
    }

    public static class MS_FACTORY_HDMI_OVERSCAN_SETTING {
        public ST_MAPI_VIDEO_WINDOW_INFO[][] stVideoWinInfo = ((ST_MAPI_VIDEO_WINDOW_INFO[][]) Array.newInstance(ST_MAPI_VIDEO_WINDOW_INFO.class, new int[]{MAX_HDMI_Resolution_Info.E_HDMI_MAX.ordinal(), MAPI_VIDEO_ARC_Type.E_AR_MAX.ordinal()}));
        public int u16CheckSum;

        public MS_FACTORY_HDMI_OVERSCAN_SETTING() {
            for (int i = 0; i < MAX_DTV_Resolution_Info.E_DTV_MAX.ordinal(); i++) {
                for (int j = 0; j < MAPI_VIDEO_ARC_Type.E_AR_MAX.ordinal(); j++) {
                    this.stVideoWinInfo[i][j] = new ST_MAPI_VIDEO_WINDOW_INFO();
                }
            }
        }
    }

    public static class MS_FACTORY_SSC_SET {
        public boolean Lvds_SscEnable = false;
        public int Lvds_SscSpan = 128;
        public int Lvds_SscStep = 128;
        public int Miu0_SscSpan = 128;
        public int Miu0_SscStep = 128;
        public int Miu1_SscSpan = 128;
        public int Miu1_SscStep = 128;
        public int Miu2_SscSpan;
        public int Miu2_SscStep;
        public boolean Miu_SscEnable = false;
    }

    public static class MS_FACTORY_VD_OVERSCAN_SETTING {
        public ST_MAPI_VIDEO_WINDOW_INFO[][] stVideoWinInfo = ((ST_MAPI_VIDEO_WINDOW_INFO[][]) Array.newInstance(ST_MAPI_VIDEO_WINDOW_INFO.class, new int[]{EN_VD_SIGNALTYPE.SIG_NUMS.ordinal(), MAPI_VIDEO_ARC_Type.E_AR_MAX.ordinal()}));
        public int u16CheckSum;

        public MS_FACTORY_VD_OVERSCAN_SETTING() {
            for (int i = 0; i < MAX_DTV_Resolution_Info.E_DTV_MAX.ordinal(); i++) {
                for (int j = 0; j < MAPI_VIDEO_ARC_Type.E_AR_MAX.ordinal(); j++) {
                    this.stVideoWinInfo[i][j] = new ST_MAPI_VIDEO_WINDOW_INFO();
                }
            }
        }
    }

    public static class MS_FACTORY_YPbPr_OVERSCAN_SETTING {
        public ST_MAPI_VIDEO_WINDOW_INFO[][] stVideoWinInfo = ((ST_MAPI_VIDEO_WINDOW_INFO[][]) Array.newInstance(ST_MAPI_VIDEO_WINDOW_INFO.class, new int[]{MAX_YPbPr_Resolution_Info.E_YPbPr_MAX.ordinal(), MAPI_VIDEO_ARC_Type.E_AR_MAX.ordinal()}));
        public int u16CheckSum;

        public MS_FACTORY_YPbPr_OVERSCAN_SETTING() {
            for (int i = 0; i < MAX_DTV_Resolution_Info.E_DTV_MAX.ordinal(); i++) {
                for (int j = 0; j < MAPI_VIDEO_ARC_Type.E_AR_MAX.ordinal(); j++) {
                    this.stVideoWinInfo[i][j] = new ST_MAPI_VIDEO_WINDOW_INFO();
                }
            }
        }
    }

    public static class MS_Factory_NS_VD_SET {
        public short u8AFEC_43 = 0;
        public short u8AFEC_44 = 0;
        public short u8AFEC_66_Bit76 = 0;
        public short u8AFEC_6E_Bit3210 = 0;
        public short u8AFEC_6E_Bit7654 = 0;
        public short u8AFEC_A0 = 0;
        public short u8AFEC_A1 = 0;
        public short u8AFEC_CB = 0;
        public short u8AFEC_D4 = 0;
        public short u8AFEC_D5_Bit2 = 0;
        public short u8AFEC_D7_HIGH_BOUND = 0;
        public short u8AFEC_D7_LOW_BOUND = 0;
        public short u8AFEC_D8_Bit3210 = 0;
        public short u8AFEC_D9_Bit0 = 0;
    }

    public static class MS_Factory_NS_VIF_SET {
        public short ChinaDescramblerBox = 0;
        public int GainDistributionThr = 0;
        public short VifACIAGCREF = 0;
        public short VifAgcRefNegative = 96;
        public short VifAgcVgaBase = 0;
        public boolean VifAsiaSignalOption = false;
        public int VifClampgainClampOvNegative = 0;
        public int VifClampgainGainOvNegative = 0;
        public short VifCrKi = 0;
        public short VifCrKi1 = 0;
        public short VifCrKi2 = 0;
        public short VifCrKp = 0;
        public short VifCrKp1 = 0;
        public short VifCrKp2 = 0;
        public boolean VifCrKpKiAdjust = false;
        public int VifCrLockThr = 0;
        public int VifCrThr = 0;
        public short VifDelayReduce = 0;
        public boolean VifOverModulation = false;
        public short VifTop = 0;
        public short VifVersion = 0;
        public int VifVgaMaximum = 0;
    }

    public static class MS_NLA_POINT {
        public short u8OSD_V0 = 128;
        public short u8OSD_V100 = 128;
        public short u8OSD_V25 = 128;
        public short u8OSD_V50 = 128;
        public short u8OSD_V75 = 128;
    }

    public static class MS_NLA_SETTING {
        public MS_NLA_SET_INDEX msNlaSetIndex;
        public MS_NLA_POINT[] stNLASetting = new MS_NLA_POINT[MS_NLA_SET_INDEX.EN_NLA_NUMS.ordinal()];
        public int u16CheckSum;

        public MS_NLA_SETTING() {
            for (int i = 0; i < MS_NLA_SET_INDEX.EN_NLA_NUMS.ordinal(); i++) {
                this.stNLASetting[i] = new MS_NLA_POINT();
            }
            this.msNlaSetIndex = MS_NLA_SET_INDEX.EN_NLA_VOLUME;
        }
    }

    public enum MS_NLA_SET_INDEX {
        EN_NLA_VOLUME,
        EN_NLA_BRIGHTNESS,
        EN_NLA_CONTRAST,
        EN_NLA_SATURATION,
        EN_NLA_SHARPNESS,
        EN_NLA_HUE,
        EN_NLA_NUMS
    }

    public static class MS_USER_LOCATION_SETTING {
        public int mLocationNo;
        public int mManualLatitude;
        public int mManualLongitude;

        public MS_USER_LOCATION_SETTING(int v1, int v2, int v3) {
            this.mLocationNo = v1;
            this.mManualLongitude = v2;
            this.mManualLatitude = v3;
        }
    }

    public static class MS_USER_SOUND_SETTING {
        public EN_SOUND_AD_OUTPUT ADOutput;
        public short ADVolume;
        public EN_AUDYSSEY_DYNAMIC_VOLUME_MODE AudysseyDynamicVolume;
        public EN_AUDYSSEY_EQ_MODE AudysseyEQ;
        public short Balance;
        public short CH1PreScale;
        public short HPVolume;
        public short MUTE_Flag;
        public short Primary_Flag;
        public short SPDIF_Delay;
        public EN_SOUND_MODE SoundMode;
        public short Speaker_Delay;
        public EN_SURROUND_TYPE Surround;
        public EN_SURROUND_MODE SurroundMode;
        public EN_SURROUND_SYSTEM_TYPE SurroundSoundMode;
        public short Volume;
        public boolean bEnableAD;
        public boolean bEnableAVC;
        public EN_AUD_MODE enSoundAudioChannel;
        public EnumLanguage enSoundAudioLan1;
        public EnumLanguage enSoundAudioLan2;
        public HdmiAudioSource hdmi1AudioSource;
        public HdmiAudioSource hdmi2AudioSource;
        public HdmiAudioSource hdmi3AudioSource;
        public HdmiAudioSource hdmi4AudioSource;
        public EN_SPDIF_MODE spdifMode;
        public int u16CheckSum;

        public MS_USER_SOUND_SETTING() {
            this.SoundMode = EN_SOUND_MODE.SOUND_MODE_STANDARD;
            this.bEnableAVC = false;
            this.spdifMode = EN_SPDIF_MODE.PDIF_MODE_RAW;
            this.SurroundMode = EN_SURROUND_MODE.E_SURROUND_MODE_OFF;
            this.SoundMode = EN_SOUND_MODE.SOUND_MODE_STANDARD;
            this.Balance = 50;
        }
    }

    public static class MS_USER_SUBTITLE_SETTING {
        public EnumLanguage SubtitleDefaultLanguage;
        public EnumLanguage SubtitleDefaultLanguage_2;
        public boolean fEnableSubTitle;
        public boolean fHardOfHearing;

        public MS_USER_SUBTITLE_SETTING(EnumLanguage eLang1, EnumLanguage eLang2, boolean bHearing, boolean bSubtitle) {
            this.SubtitleDefaultLanguage = eLang1;
            this.SubtitleDefaultLanguage_2 = eLang2;
            this.fHardOfHearing = bHearing;
            this.fEnableSubTitle = bSubtitle;
        }
    }

    public static class MS_USER_SYSTEM_SETTING {
        public short AudioOnly;
        public MEMBER_COUNTRY Country;
        public short DtvRoute;
        public short ScartOutRGB;
        public int U8SystemAutoTimeType;
        public short U8Transparency;
        public boolean bBlueScreen;
        public boolean bDisableSiAutoUpdate;
        public short bEnableAlwaysTimeshift;
        public boolean bEnableAutoChannelUpdate;
        public short bEnablePVRRecordAll;
        public short bEnableWDT;
        public boolean bOverScan;
        public boolean bUartBus;
        public int checkSum;
        public ColorWheelMode colorWheelMode;
        public EN_MS_CHANNEL_SWITCH_MODE eChSwMode;
        public EN_MS_OFFLINE_DET_MODE eOffDetMode;
        public EN_MS_POWERON_LOGO ePWR_Logo;
        public EN_MS_POWERON_MUSIC ePWR_Music;
        public EN_MS_SUPER eSUPER;
        public EN_CABLE_OPERATORS enCableOperators;
        public EnumInputSource enInputSourceType;
        public EnumLanguage enLanguage;
        public SPDIF_TYPE enSPDIFMODE;
        public EN_SATELLITE_PLATFORM enSatellitePlatform;
        public short fAutoVolume;
        public short fDcPowerOFFMode;
        public boolean fNoChannel;
        public short fOADScanAfterWakeup;
        public short fOadScan;
        public boolean fRunInstallationGuide;
        public short fSoftwareUpdate;
        public short m_AutoZoom;
        public boolean m_MessageBoxExist;
        public short m_u8BrazilVideoStandardType;
        public short m_u8SoftwareUpdateMode;
        public boolean screenSaveMode;
        public SmartEnergySavingMode smartEnergySaving;
        public int standbyNoOperation;
        public boolean standbyNoSignal;
        public int u16LastOADVersion;
        public long u32MenuTimeOut;
        public long u32OSD_Active_Time;
        public short u8Bandwidth;
        public short u8ColorRangeMode;
        public short u8FavoriteRegion;
        public short u8HDMIAudioSource;
        public short u8OADTime;
        public short u8OsdDuration;
        public short u8TimeShiftSizeType;
    }

    public enum SPDIF_TYPE {
        MSAPI_AUD_SPDIF_PCM_,
        MSAPI_AUD_SPDIF_OFF_,
        MSAPI_AUD_SPDIF_NONPCM_
    }

    public static class ST_FACTORY_CI_SETTING {
        public boolean bPerformanceMonitor;
        public short enCredentialMode;
        public int u16CheckSum;
        public short[] u8CIFunctionDebugLevel = new short[EN_CI_FUNCTION.EN_CI_FUNCTION_DEBUG_COUNT.ordinal()];

        public ST_FACTORY_CI_SETTING() {
            for (int i = 0; i < EN_CI_FUNCTION.EN_CI_FUNCTION_DEBUG_COUNT.ordinal(); i++) {
                this.u8CIFunctionDebugLevel[i] = 128;
            }
        }
    }

    public static class ST_FACTORY_PEQ_SETTING {
        public AUDIO_PEQ_PARAM[] stPEQParam = new AUDIO_PEQ_PARAM[3];
        public int u16CheckSum;

        public ST_FACTORY_PEQ_SETTING() {
            for (int i = 0; i < 3; i++) {
                this.stPEQParam[i] = new AUDIO_PEQ_PARAM();
            }
        }
    }

    public static class ST_MAPI_3D_INFO {
        boolean bEnable3D;
        EN_3D_INPUT_TYPE enInput3DMode;
        EN_3D_OUTPUT_TYPE enOutput3DMode;

        public ST_MAPI_3D_INFO(boolean bEnable, EN_3D_INPUT_TYPE eInput, EN_3D_OUTPUT_TYPE eOutput) {
            this.bEnable3D = bEnable;
            this.enInput3DMode = eInput;
            this.enOutput3DMode = eOutput;
        }
    }

    public static class ST_MAPI_BLOCK_SYS_SETTING {
        public int blockSysLockMode;
        public int blockSysPWSetStatus;
        public int blockSysPassword;
        public int enterLockPage;
        public int parentalControl;
        public int unrateLock;
        public int videoBlockMode;
    }

    public static class ST_MAPI_VIDEO_WINDOW_INFO {
        public int u16H_CapStart = 0;
        public int u16V_CapStart = 0;
        public short u8HCrop_Left = 50;
        public short u8HCrop_Right = 50;
        public short u8VCrop_Down = 50;
        public short u8VCrop_Up = 50;
    }

    public enum SkinToneMode {
        SKIN_TONE_OFF,
        SKIN_TONE_RED,
        SKIN_TONE_YELLOW
    }

    public enum SmartEnergySavingMode {
        MODE_OFF,
        MODE_ON,
        MODE_DEMO
    }

    public static class SoundModeSeting {
        public short Balance;
        public short Bass;
        public short EqBand1;
        public short EqBand2;
        public short EqBand3;
        public short EqBand4;
        public short EqBand5;
        public short EqBand6;
        public short EqBand7;
        public short Treble;
        public boolean UserMode;
        public EN_AUD_MODE enSoundAudioChannel;

        public SoundModeSeting(short Bass2, short treble, short EqBand12, short EqBand22, short EqBand32, short EqBand42, short EqBand52) {
            this.Bass = Bass2;
            this.Treble = treble;
            this.EqBand1 = EqBand12;
            this.EqBand2 = EqBand22;
            this.EqBand3 = EqBand32;
            this.EqBand4 = EqBand42;
            this.EqBand5 = EqBand52;
        }
    }

    public static class T_MS_CALIBRATION_DATA {
        public int bluegain;
        public int blueoffset;
        public int greengain;
        public int greenoffset;
        public int redgain;
        public int redoffset;

        public T_MS_CALIBRATION_DATA(int v1, int v2, int v3, int v4, int v5, int v6) {
            this.redgain = v1;
            this.greengain = v2;
            this.bluegain = v3;
            this.redoffset = v4;
            this.greenoffset = v5;
            this.blueoffset = v6;
        }
    }

    public static class T_MS_COLOR_TEMP {
        public T_MS_COLOR_TEMP_DATA[] astColorTemp = new T_MS_COLOR_TEMP_DATA[EN_MS_COLOR_TEMP.MS_COLOR_TEMP_NUM.ordinal()];
        public int u16CheckSum;

        public T_MS_COLOR_TEMP() {
            for (int i = 0; i < EN_MS_COLOR_TEMP.MS_COLOR_TEMP_NUM.ordinal(); i++) {
                this.astColorTemp[i] = new T_MS_COLOR_TEMP_DATA(128, 128, 128, 128, 128, 128);
            }
            this.u16CheckSum = 65535;
        }
    }

    public static class T_MS_COLOR_TEMPEX {
        public T_MS_COLOR_TEMPEX_DATA[][] astColorTempEx = ((T_MS_COLOR_TEMPEX_DATA[][]) Array.newInstance(T_MS_COLOR_TEMPEX_DATA.class, new int[]{EN_MS_COLOR_TEMP.MS_COLOR_TEMP_NUM.ordinal(), EN_MS_COLOR_TEMP_INPUT_SOURCE.E_INPUT_SOURCE_NUM.ordinal()}));
        public int u16CheckSum;

        public T_MS_COLOR_TEMPEX() {
            for (int i = 0; i < EN_MS_COLOR_TEMP.MS_COLOR_TEMP_NUM.ordinal(); i++) {
                for (int j = 0; j < EN_MS_COLOR_TEMP_INPUT_SOURCE.E_INPUT_SOURCE_NUM.ordinal(); j++) {
                    this.astColorTempEx[i][j] = new T_MS_COLOR_TEMPEX_DATA(128, 128, 128, 128, 128, 128);
                }
            }
            this.u16CheckSum = 65535;
        }
    }

    public static class T_MS_COLOR_TEMPEX_DATA {
        public int bluegain;
        public int blueoffset;
        public int greengain;
        public int greenoffset;
        public int redgain;
        public int redoffset;

        public T_MS_COLOR_TEMPEX_DATA(int v1, int v2, int v3, int v4, int v5, int v6) {
            this.redgain = v1;
            this.greengain = v2;
            this.bluegain = v3;
            this.redoffset = v4;
            this.greenoffset = v5;
            this.blueoffset = v6;
        }
    }

    public static class T_MS_COLOR_TEMP_DATA {
        public short bluegain;
        public short blueoffset;
        public short greengain;
        public short greenoffset;
        public short redgain;
        public short redoffset;

        public T_MS_COLOR_TEMP_DATA(short v1, short v2, short v3, short v4, short v5, short v6) {
            this.redgain = v1;
            this.greengain = v2;
            this.bluegain = v3;
            this.redoffset = v4;
            this.greenoffset = v5;
            this.blueoffset = v6;
        }
    }

    public static class T_MS_NR_MODE {
        public EN_MS_MPEG_NR eMPEG_NR;
        public EN_MS_NR eNR;

        public T_MS_NR_MODE(EN_MS_NR evalue1, EN_MS_MPEG_NR evalue2) {
            this.eNR = evalue1;
            this.eMPEG_NR = evalue2;
        }
    }

    public static class T_MS_OVERSCAN_SETTING_USER {
        public short OverScanHRatio;
        public short OverScanHposition;
        public short OverScanVRatio;
        public short OverScanVposition;

        public T_MS_OVERSCAN_SETTING_USER(short x, short y, short w, short h) {
            this.OverScanHposition = x;
            this.OverScanVposition = y;
            this.OverScanHRatio = w;
            this.OverScanVRatio = h;
        }
    }

    public static class T_MS_PICTURE {
        public short backlight;
        public short brightness;
        public short contrast;
        public EN_MS_COLOR_TEMP eColorTemp;
        public EN_MS_PIC_ADV eDynamicBacklight;
        public EN_MS_PIC_ADV eDynamicContrast;
        public EN_MS_PIC_ADV ePerfectClear;
        public EN_MS_PIC_ADV eVibrantColour;
        public short hue;
        public short saturation;
        public short sharpness;

        public T_MS_PICTURE(short backlight2, short con, short bri, short sat, short sha, short hue2, EN_MS_COLOR_TEMP colortemp, EN_MS_PIC_ADV evcolor, EN_MS_PIC_ADV epClear, EN_MS_PIC_ADV edcontrast, EN_MS_PIC_ADV edbackling) {
            this.backlight = backlight2;
            this.contrast = con;
            this.brightness = bri;
            this.saturation = sat;
            this.sharpness = sha;
            this.hue = hue2;
            this.eColorTemp = colortemp;
            this.eVibrantColour = evcolor;
            this.ePerfectClear = epClear;
            this.eDynamicContrast = edcontrast;
            this.eDynamicBacklight = edbackling;
        }
    }

    public static class T_MS_SUB_COLOR {
        int Checksum;
        public short SubBrightness;
        public short SubContrast;

        public T_MS_SUB_COLOR(int v1, short v2, short v3) {
            this.Checksum = v1;
            this.SubBrightness = v2;
            this.SubContrast = v3;
        }
    }

    public static class T_MS_VIDEO {
        public int CheckSum;
        public EN_MS_NR DNR;
        public AUDIOMODE_TYPE_ LastAudioStandardMode;
        public MAPI_AVD_VideoStandardType LastVideoStandardMode;
        public ThreeD_Video_MODE ThreeDVideoMode;
        public T_MS_PICTURE[] astPicture;
        public boolean detailEnhance;
        public EN_MS_Dynamic_Contrast eDynamic_Contrast;
        public EN_MS_FILM eFilm;
        public T_MS_NR_MODE[] eNRMode;
        public EN_MS_PICTURE ePicture;
        public EN_DISPLAY_TVFORMAT eTvFormat;
        public MAPI_VIDEO_ARC_Type enARCType;
        public EN_DISPLAY_RES_TYPE fOutput_RES;
        public T_MS_SUB_COLOR g_astSubColor;
        public SkinToneMode skinTone;
        public T_MS_OVERSCAN_SETTING_USER stUserOverScanMode;
        public MAPI_VIDEO_OUT_VE_SYS tvsys;
    }

    public static class ThreeD_Video_MODE {
        public EN_ThreeD_Video eThreeDVideo;
        public EN_ThreeD_Video_3DDEPTH eThreeDVideo3DDepth;
        public EN_ThreeD_Video_3DOFFSET eThreeDVideo3DOffset;
        public EN_ThreeD_Video_3DOUTPUTASPECT eThreeDVideo3DOutputAspect;
        public EN_ThreeD_Video_3DTO2D eThreeDVideo3DTo2D;
        public EN_ThreeD_Video_AUTOSTART eThreeDVideoAutoStart;
        public EN_ThreeD_Video_DISPLAYFORMAT eThreeDVideoDisplayFormat;
        public EN_ThreeD_Video_LRVIEWSWITCH eThreeDVideoLRViewSwitch;
        public EN_ThreeD_Video_SELFADAPTIVE_DETECT eThreeDVideoSelfAdaptiveDetect;

        public ThreeD_Video_MODE(EN_ThreeD_Video eValue1, EN_ThreeD_Video_SELFADAPTIVE_DETECT eValue2, EN_ThreeD_Video_DISPLAYFORMAT eValue4, EN_ThreeD_Video_3DTO2D eValue5, EN_ThreeD_Video_3DDEPTH eValue6, EN_ThreeD_Video_3DOFFSET eValue7, EN_ThreeD_Video_AUTOSTART eValue8, EN_ThreeD_Video_3DOUTPUTASPECT eValue9, EN_ThreeD_Video_LRVIEWSWITCH eValue10) {
            this.eThreeDVideo = eValue1;
            this.eThreeDVideoSelfAdaptiveDetect = eValue2;
            this.eThreeDVideoDisplayFormat = eValue4;
            this.eThreeDVideo3DTo2D = eValue5;
            this.eThreeDVideo3DDepth = eValue6;
            this.eThreeDVideo3DOffset = eValue7;
            this.eThreeDVideoAutoStart = eValue8;
            this.eThreeDVideo3DOutputAspect = eValue9;
            this.eThreeDVideoLRViewSwitch = eValue10;
        }
    }

    boolean IsSystemLocked();

    void SyncUserSettingDB();

    void UpdateDB();

    MS_ADC_SETTING getAdcSetting();

    MS_CEC_SETTING getCECVar();

    CustomerCfgMiscSetting getCustomerCfgMiscSetting();

    EnumChinaDvbcRegion getDTVCity();

    int getDVBCNetTableFrequency();

    EnumSwitchOnOff getDefinitionOnOff();

    EnumSwitchOnOff getDialogClarityOnOff();

    int getDynamicBLMode();

    MS_FACTORY_EXTERN_SETTING getFactoryExt();

    MS_USER_LOCATION_SETTING getLocationSet();

    MS_Factory_NS_VD_SET getNoStandSet();

    MS_Factory_NS_VIF_SET getNoStandVifSet();

    int getParentalControlRating();

    EnumSwitchOnOff getSRSOnOff();

    KK_SRS_SET getSRSSet();

    MS_USER_SOUND_SETTING getSound();

    SoundModeSeting getSoundMode(EN_SOUND_MODE en_sound_mode);

    SPDIF_TYPE getSpdifMode();

    MS_FACTORY_SSC_SET getSscSet();

    MS_USER_SUBTITLE_SETTING getSubtitleSet();

    int getSystemLockPassword();

    EnumSwitchOnOff getTruebaseOnOff();

    MS_USER_SYSTEM_SETTING getUsrData();

    T_MS_VIDEO getVideo();

    T_MS_COLOR_TEMP getVideoTemp();

    T_MS_COLOR_TEMPEX_DATA getVideoTempEx();

    void loadEssentialDataFromDB();

    int queryAutoMHLSwitch();

    int queryColorTemp(int i, int i2);

    String queryEventName(short s);

    String queryServiceName(short s);

    int querySourceIdent();

    int querySourceSwit();

    EN_ThreeD_Video_SELFADAPTIVE_DETECT queryVideo3DSelfAdaptiveDetectMode(int i);

    boolean restoreUsrDB(EN_SYSTEM_FACTORY_DB_COMMAND en_system_factory_db_command);

    boolean saveInputSource(EnumInputSource enumInputSource);

    boolean setAdcSetting(MS_ADC_SETTING ms_adc_setting);

    void setBlockSysPassword(int i);

    boolean setCECVar(MS_CEC_SETTING ms_cec_setting);

    boolean setCustomerCfgMiscSetting(CustomerCfgMiscSetting customerCfgMiscSetting);

    void setDTVCity(EnumChinaDvbcRegion enumChinaDvbcRegion);

    void setDVBCNetTableFrequency(int i);

    void setDefinitionOnOff(EnumSwitchOnOff enumSwitchOnOff);

    void setDialogClarityOnOff(EnumSwitchOnOff enumSwitchOnOff);

    void setDynamicBLMode(EN_MS_LOCALDIMMING en_ms_localdimming);

    boolean setFactoryExt(MS_FACTORY_EXTERN_SETTING ms_factory_extern_setting);

    boolean setLocationSet(MS_USER_LOCATION_SETTING ms_user_location_setting);

    boolean setNoStandSet(MS_Factory_NS_VD_SET mS_Factory_NS_VD_SET);

    boolean setNoStandVifSet(MS_Factory_NS_VIF_SET mS_Factory_NS_VIF_SET);

    void setParentalControl(int i);

    void setSRSOnOff(EnumSwitchOnOff enumSwitchOnOff);

    boolean setSRSSet(KK_SRS_SET kk_srs_set, EN_SRS_SET_TYPE en_srs_set_type);

    boolean setSound(MS_USER_SOUND_SETTING ms_user_sound_setting);

    boolean setSoundMode(EN_SOUND_MODE en_sound_mode);

    boolean setSoundMode(EN_SOUND_MODE en_sound_mode, SoundModeSeting soundModeSeting);

    void setSpdifMode(SPDIF_TYPE spdif_type);

    boolean setSscSet(MS_FACTORY_SSC_SET ms_factory_ssc_set);

    boolean setSubtitleSet(MS_USER_SUBTITLE_SETTING ms_user_subtitle_setting);

    void setSystemLock(int i);

    void setTruebaseOnOff(EnumSwitchOnOff enumSwitchOnOff);

    boolean setUsrData(MS_USER_SYSTEM_SETTING ms_user_system_setting);

    boolean setVideo(T_MS_VIDEO t_ms_video);

    boolean setVideoTemp(T_MS_COLOR_TEMP t_ms_color_temp);

    boolean setVideoTempEx(T_MS_COLOR_TEMPEX_DATA t_ms_color_tempex_data);

    void updateAutoMHLSwitch(short s);

    void updateEventName(String str, short s);

    void updateServiceName(String str, short s);

    void updateSourceIdent(short s);

    void updateSourceSwit(short s);
}
