package com.konka.kkimplements.tv.mstar;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.util.Log;
import com.konka.kkinterface.tv.ChannelDesk;
import com.konka.kkinterface.tv.CommonDesk;
import com.konka.kkinterface.tv.DataBaseDesk;
import com.konka.kkinterface.tv.DataBaseDesk.AUDIOMODE_TYPE_;
import com.konka.kkinterface.tv.DataBaseDesk.AUDIO_PEQ_PARAM;
import com.konka.kkinterface.tv.DataBaseDesk.ColorWheelMode;
import com.konka.kkinterface.tv.DataBaseDesk.CustomerCfgMiscSetting;
import com.konka.kkinterface.tv.DataBaseDesk.EN_AUDYSSEY_DYNAMIC_VOLUME_MODE;
import com.konka.kkinterface.tv.DataBaseDesk.EN_AUDYSSEY_EQ_MODE;
import com.konka.kkinterface.tv.DataBaseDesk.EN_AUD_MODE;
import com.konka.kkinterface.tv.DataBaseDesk.EN_CABLE_OPERATORS;
import com.konka.kkinterface.tv.DataBaseDesk.EN_DISPLAY_RES_TYPE;
import com.konka.kkinterface.tv.DataBaseDesk.EN_DISPLAY_TVFORMAT;
import com.konka.kkinterface.tv.DataBaseDesk.EN_MS_CHANNEL_SWITCH_MODE;
import com.konka.kkinterface.tv.DataBaseDesk.EN_MS_COLOR_TEMP;
import com.konka.kkinterface.tv.DataBaseDesk.EN_MS_COLOR_TEMP_INPUT_SOURCE;
import com.konka.kkinterface.tv.DataBaseDesk.EN_MS_Dynamic_Contrast;
import com.konka.kkinterface.tv.DataBaseDesk.EN_MS_FILM;
import com.konka.kkinterface.tv.DataBaseDesk.EN_MS_LOCALDIMMING;
import com.konka.kkinterface.tv.DataBaseDesk.EN_MS_MPEG_NR;
import com.konka.kkinterface.tv.DataBaseDesk.EN_MS_NR;
import com.konka.kkinterface.tv.DataBaseDesk.EN_MS_OFFLINE_DET_MODE;
import com.konka.kkinterface.tv.DataBaseDesk.EN_MS_PICTURE;
import com.konka.kkinterface.tv.DataBaseDesk.EN_MS_PIC_ADV;
import com.konka.kkinterface.tv.DataBaseDesk.EN_MS_POWERON_LOGO;
import com.konka.kkinterface.tv.DataBaseDesk.EN_MS_POWERON_MUSIC;
import com.konka.kkinterface.tv.DataBaseDesk.EN_MS_SUPER;
import com.konka.kkinterface.tv.DataBaseDesk.EN_MS_VIDEOITEM;
import com.konka.kkinterface.tv.DataBaseDesk.EN_SATELLITE_PLATFORM;
import com.konka.kkinterface.tv.DataBaseDesk.EN_SOUND_AD_OUTPUT;
import com.konka.kkinterface.tv.DataBaseDesk.EN_SOUND_MODE;
import com.konka.kkinterface.tv.DataBaseDesk.EN_SRS_SET_TYPE;
import com.konka.kkinterface.tv.DataBaseDesk.EN_SURROUND_MODE;
import com.konka.kkinterface.tv.DataBaseDesk.EN_SURROUND_SYSTEM_TYPE;
import com.konka.kkinterface.tv.DataBaseDesk.EN_SYSTEM_FACTORY_DB_COMMAND;
import com.konka.kkinterface.tv.DataBaseDesk.EN_ThreeD_Video;
import com.konka.kkinterface.tv.DataBaseDesk.EN_ThreeD_Video_3DDEPTH;
import com.konka.kkinterface.tv.DataBaseDesk.EN_ThreeD_Video_3DOFFSET;
import com.konka.kkinterface.tv.DataBaseDesk.EN_ThreeD_Video_3DOUTPUTASPECT;
import com.konka.kkinterface.tv.DataBaseDesk.EN_ThreeD_Video_3DTO2D;
import com.konka.kkinterface.tv.DataBaseDesk.EN_ThreeD_Video_AUTOSTART;
import com.konka.kkinterface.tv.DataBaseDesk.EN_ThreeD_Video_DISPLAYFORMAT;
import com.konka.kkinterface.tv.DataBaseDesk.EN_ThreeD_Video_LRVIEWSWITCH;
import com.konka.kkinterface.tv.DataBaseDesk.EN_ThreeD_Video_SELFADAPTIVE_DETECT;
import com.konka.kkinterface.tv.DataBaseDesk.EN_VD_SIGNALTYPE;
import com.konka.kkinterface.tv.DataBaseDesk.EnumSwitchOnOff;
import com.konka.kkinterface.tv.DataBaseDesk.HdmiAudioSource;
import com.konka.kkinterface.tv.DataBaseDesk.KK_SRS_SET;
import com.konka.kkinterface.tv.DataBaseDesk.MAPI_AVD_VideoStandardType;
import com.konka.kkinterface.tv.DataBaseDesk.MAPI_VIDEO_ARC_Type;
import com.konka.kkinterface.tv.DataBaseDesk.MAPI_VIDEO_OUT_VE_SYS;
import com.konka.kkinterface.tv.DataBaseDesk.MAX_DTV_Resolution_Info;
import com.konka.kkinterface.tv.DataBaseDesk.MAX_HDMI_Resolution_Info;
import com.konka.kkinterface.tv.DataBaseDesk.MAX_YPbPr_Resolution_Info;
import com.konka.kkinterface.tv.DataBaseDesk.MEMBER_COUNTRY;
import com.konka.kkinterface.tv.DataBaseDesk.MS_ADC_SETTING;
import com.konka.kkinterface.tv.DataBaseDesk.MS_CEC_SETTING;
import com.konka.kkinterface.tv.DataBaseDesk.MS_FACTORY_EXTERN_SETTING;
import com.konka.kkinterface.tv.DataBaseDesk.MS_FACTORY_SSC_SET;
import com.konka.kkinterface.tv.DataBaseDesk.MS_Factory_NS_VD_SET;
import com.konka.kkinterface.tv.DataBaseDesk.MS_Factory_NS_VIF_SET;
import com.konka.kkinterface.tv.DataBaseDesk.MS_NLA_POINT;
import com.konka.kkinterface.tv.DataBaseDesk.MS_NLA_SETTING;
import com.konka.kkinterface.tv.DataBaseDesk.MS_USER_LOCATION_SETTING;
import com.konka.kkinterface.tv.DataBaseDesk.MS_USER_SOUND_SETTING;
import com.konka.kkinterface.tv.DataBaseDesk.MS_USER_SUBTITLE_SETTING;
import com.konka.kkinterface.tv.DataBaseDesk.MS_USER_SYSTEM_SETTING;
import com.konka.kkinterface.tv.DataBaseDesk.SPDIF_TYPE;
import com.konka.kkinterface.tv.DataBaseDesk.ST_FACTORY_CI_SETTING;
import com.konka.kkinterface.tv.DataBaseDesk.ST_FACTORY_PEQ_SETTING;
import com.konka.kkinterface.tv.DataBaseDesk.ST_MAPI_BLOCK_SYS_SETTING;
import com.konka.kkinterface.tv.DataBaseDesk.ST_MAPI_VIDEO_WINDOW_INFO;
import com.konka.kkinterface.tv.DataBaseDesk.SkinToneMode;
import com.konka.kkinterface.tv.DataBaseDesk.SmartEnergySavingMode;
import com.konka.kkinterface.tv.DataBaseDesk.SoundModeSeting;
import com.konka.kkinterface.tv.DataBaseDesk.T_MS_CALIBRATION_DATA;
import com.konka.kkinterface.tv.DataBaseDesk.T_MS_COLOR_TEMP;
import com.konka.kkinterface.tv.DataBaseDesk.T_MS_COLOR_TEMPEX;
import com.konka.kkinterface.tv.DataBaseDesk.T_MS_COLOR_TEMPEX_DATA;
import com.konka.kkinterface.tv.DataBaseDesk.T_MS_COLOR_TEMP_DATA;
import com.konka.kkinterface.tv.DataBaseDesk.T_MS_NR_MODE;
import com.konka.kkinterface.tv.DataBaseDesk.T_MS_OVERSCAN_SETTING_USER;
import com.konka.kkinterface.tv.DataBaseDesk.T_MS_PICTURE;
import com.konka.kkinterface.tv.DataBaseDesk.T_MS_SUB_COLOR;
import com.konka.kkinterface.tv.DataBaseDesk.T_MS_VIDEO;
import com.konka.kkinterface.tv.DataBaseDesk.ThreeD_Video_MODE;
import com.mstar.android.tvapi.common.TvManager;
import com.mstar.android.tvapi.common.exception.TvCommonException;
import com.mstar.android.tvapi.common.vo.TvOsType.EnumInputSource;
import com.mstar.android.tvapi.common.vo.TvOsType.EnumLanguage;
import com.mstar.android.tvapi.dtv.dvb.dvbc.vo.EnumChinaDvbcRegion;
import com.tencent.stat.common.StatConstants;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.lang.reflect.Array;
import java.util.Properties;

public class DataBaseDeskImpl implements DataBaseDesk {
    private static /* synthetic */ int[] $SWITCH_TABLE$com$konka$kkinterface$tv$DataBaseDesk$EN_MS_VIDEOITEM;
    private static /* synthetic */ int[] $SWITCH_TABLE$com$konka$kkinterface$tv$DataBaseDesk$EN_SYSTEM_FACTORY_DB_COMMAND;
    private static DataBaseDeskImpl dataBaseMgrImpl;
    private final String TAG = "com.konka.kkimplements.tv.mstar.DataBaseDeskImpl";
    public SoundModeSeting[] astSoundModeSetting;
    ST_MAPI_BLOCK_SYS_SETTING blockSysSetting;
    T_MS_COLOR_TEMPEX_DATA colorParaEx;

    /* renamed from: com reason: collision with root package name */
    private CommonDesk f0com = null;
    private Context context;
    private ContentResolver cr;
    CustomerCfgMiscSetting customerCfgMiscSetting;
    private String factoryCusSchema = "content://konka.tv.factory";
    private String factorySchema = "content://mstar.tv.factory";
    MS_Factory_NS_VD_SET mNoStandSet;
    KK_SRS_SET mSRSSet;
    MS_FACTORY_SSC_SET mSscSet;
    MS_Factory_NS_VIF_SET mVifSet;
    ST_MAPI_VIDEO_WINDOW_INFO[][] m_DTVOverscanSet = ((ST_MAPI_VIDEO_WINDOW_INFO[][]) Array.newInstance(ST_MAPI_VIDEO_WINDOW_INFO.class, new int[]{MAX_DTV_Resolution_Info.E_DTV_MAX.ordinal(), MAPI_VIDEO_ARC_Type.E_AR_MAX.ordinal()}));
    ST_MAPI_VIDEO_WINDOW_INFO[][] m_HDMIOverscanSet = ((ST_MAPI_VIDEO_WINDOW_INFO[][]) Array.newInstance(ST_MAPI_VIDEO_WINDOW_INFO.class, new int[]{MAX_HDMI_Resolution_Info.E_HDMI_MAX.ordinal(), MAPI_VIDEO_ARC_Type.E_AR_MAX.ordinal()}));
    ST_MAPI_VIDEO_WINDOW_INFO[][] m_VDOverscanSet = ((ST_MAPI_VIDEO_WINDOW_INFO[][]) Array.newInstance(ST_MAPI_VIDEO_WINDOW_INFO.class, new int[]{EN_VD_SIGNALTYPE.SIG_NUMS.ordinal(), MAPI_VIDEO_ARC_Type.E_AR_MAX.ordinal()}));
    ST_MAPI_VIDEO_WINDOW_INFO[][] m_YPbPrOverscanSet = ((ST_MAPI_VIDEO_WINDOW_INFO[][]) Array.newInstance(ST_MAPI_VIDEO_WINDOW_INFO.class, new int[]{MAX_YPbPr_Resolution_Info.E_YPbPr_MAX.ordinal(), MAPI_VIDEO_ARC_Type.E_AR_MAX.ordinal()}));
    boolean m_bADCAutoTune;
    MS_NLA_SETTING m_pastNLASet;
    ST_FACTORY_CI_SETTING m_stCISet;
    MS_ADC_SETTING m_stFactoryAdc;
    T_MS_COLOR_TEMP m_stFactoryColorTemp;
    T_MS_COLOR_TEMPEX m_stFactoryColorTempEx;
    MS_FACTORY_EXTERN_SETTING m_stFactoryExt;
    ST_FACTORY_PEQ_SETTING m_stPEQSet;
    MS_USER_SOUND_SETTING soundpara;
    MS_CEC_SETTING stCECPara;
    MS_USER_SUBTITLE_SETTING stSubtitleSet;
    MS_USER_LOCATION_SETTING stUserLocationSetting;
    T_MS_COLOR_TEMP_DATA stUsrColorTemp;
    T_MS_COLOR_TEMPEX_DATA[] stUsrColorTempEx;
    MS_USER_SYSTEM_SETTING stUsrData;
    private Boolean[] tableDirtyFlags;
    private String userSettingSchema = "content://mstar.tv.usersetting";
    T_MS_VIDEO videopara;

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

    static /* synthetic */ int[] $SWITCH_TABLE$com$konka$kkinterface$tv$DataBaseDesk$EN_SYSTEM_FACTORY_DB_COMMAND() {
        int[] iArr = $SWITCH_TABLE$com$konka$kkinterface$tv$DataBaseDesk$EN_SYSTEM_FACTORY_DB_COMMAND;
        if (iArr == null) {
            iArr = new int[EN_SYSTEM_FACTORY_DB_COMMAND.values().length];
            try {
                iArr[EN_SYSTEM_FACTORY_DB_COMMAND.E_FACTORY_COLOR_TEMP_SET.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                iArr[EN_SYSTEM_FACTORY_DB_COMMAND.E_FACTORY_COMMAND_NUM.ordinal()] = 5;
            } catch (NoSuchFieldError e2) {
            }
            try {
                iArr[EN_SYSTEM_FACTORY_DB_COMMAND.E_FACTORY_RESTORE_DEFAULT.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                iArr[EN_SYSTEM_FACTORY_DB_COMMAND.E_FACTORY_VIDEO_ADC_SET.ordinal()] = 2;
            } catch (NoSuchFieldError e4) {
            }
            try {
                iArr[EN_SYSTEM_FACTORY_DB_COMMAND.E_USER_RESTORE_DEFAULT.ordinal()] = 4;
            } catch (NoSuchFieldError e5) {
            }
            $SWITCH_TABLE$com$konka$kkinterface$tv$DataBaseDesk$EN_SYSTEM_FACTORY_DB_COMMAND = iArr;
        }
        return iArr;
    }

    public ContentResolver getContentResolver() {
        if (this.cr == null) {
            this.cr = this.context.getContentResolver();
        }
        return this.cr;
    }

    public Boolean getTableDirtyFlags(int tableIdx) {
        return this.tableDirtyFlags[tableIdx];
    }

    public void setTableDirty(int tableIdx) {
        this.tableDirtyFlags[tableIdx] = Boolean.valueOf(true);
    }

    public static DataBaseDeskImpl getDataBaseMgrInstance(Context context2) {
        if (dataBaseMgrImpl == null) {
            Log.d("KKJAVAAPI", "create databasedesk now");
            dataBaseMgrImpl = new DataBaseDeskImpl(context2);
            Log.d("KKJAVAAPI", "DataBaseDeskImpl ");
        }
        openDB();
        Log.d("KKJAVAAPI", "open DB");
        return dataBaseMgrImpl;
    }

    private DataBaseDeskImpl(Context context2) {
        this.context = context2;
        System.out.println("\n context  \n" + context2);
        this.cr = context2.getContentResolver();
        System.out.println("\n getContentResolver cr  \n" + this.cr);
        this.f0com = CommonDeskImpl.getInstance(context2);
        System.out.println("\n getInstance   com \n" + this.f0com);
        initVarPicture();
        InitSettingVar();
        initCECVar();
        initVarSound();
        initVarFactory();
        this.tableDirtyFlags = new Boolean[88];
        for (int i = 0; i < 88; i++) {
            this.tableDirtyFlags[i] = Boolean.valueOf(false);
        }
        openDB();
        loadEssentialDataFromDB();
    }

    public void syncDirtyDataOnResume() {
        if (this.tableDirtyFlags[34].booleanValue() || this.tableDirtyFlags[20].booleanValue() || this.tableDirtyFlags[14].booleanValue() || this.tableDirtyFlags[26].booleanValue() || this.tableDirtyFlags[32].booleanValue()) {
            getDataBaseMgrInstance(this.context).queryAllVideoPara(CommonDeskImpl.getInstance(this.context).GetCurrentInputSource().ordinal());
            this.tableDirtyFlags[34] = Boolean.valueOf(false);
            this.tableDirtyFlags[20] = Boolean.valueOf(false);
            this.tableDirtyFlags[14] = Boolean.valueOf(false);
            this.tableDirtyFlags[26] = Boolean.valueOf(false);
            this.tableDirtyFlags[32] = Boolean.valueOf(false);
        }
        if (this.tableDirtyFlags[37].booleanValue()) {
            this.tableDirtyFlags[37] = Boolean.valueOf(false);
            getDataBaseMgrInstance(this.context).queryFactoryColorTempData();
        }
        if (this.tableDirtyFlags[36].booleanValue()) {
            this.tableDirtyFlags[36] = Boolean.valueOf(false);
            getDataBaseMgrInstance(this.context).queryADCAdjusts();
        }
        if (this.tableDirtyFlags[42].booleanValue()) {
            this.tableDirtyFlags[42] = Boolean.valueOf(false);
            getDataBaseMgrInstance(this.context).queryNonLinearAdjusts();
        }
        if (this.tableDirtyFlags[39].booleanValue()) {
            this.tableDirtyFlags[39] = Boolean.valueOf(false);
            getDataBaseMgrInstance(this.context).queryFactoryExtern();
        }
        if (this.tableDirtyFlags[40].booleanValue()) {
            this.tableDirtyFlags[40] = Boolean.valueOf(false);
            getDataBaseMgrInstance(this.context).queryNoStandSet();
            getDataBaseMgrInstance(this.context).queryNoStandVifSet();
        }
        if (this.tableDirtyFlags[41].booleanValue()) {
            this.tableDirtyFlags[41] = Boolean.valueOf(false);
            getDataBaseMgrInstance(this.context).querySSCAdjust();
        }
        if (this.tableDirtyFlags[43].booleanValue()) {
            this.tableDirtyFlags[43] = Boolean.valueOf(false);
            getDataBaseMgrInstance(this.context).queryOverscanAdjusts(0);
            getDataBaseMgrInstance(this.context).queryOverscanAdjusts(1);
            getDataBaseMgrInstance(this.context).queryOverscanAdjusts(2);
            getDataBaseMgrInstance(this.context).queryOverscanAdjusts(3);
        }
        if (this.tableDirtyFlags[44].booleanValue()) {
            this.tableDirtyFlags[44] = Boolean.valueOf(false);
            getDataBaseMgrInstance(this.context).queryPEQAdjusts();
        }
        if (this.tableDirtyFlags[30].booleanValue()) {
            this.tableDirtyFlags[30] = Boolean.valueOf(false);
            getDataBaseMgrInstance(this.context).queryUserLocSetting();
        }
        if (this.tableDirtyFlags[28].booleanValue()) {
            this.tableDirtyFlags[28] = Boolean.valueOf(false);
            getDataBaseMgrInstance(this.context).queryUsrColorTmpData();
        }
        if (this.tableDirtyFlags[25].booleanValue()) {
            this.tableDirtyFlags[25] = Boolean.valueOf(false);
            getDataBaseMgrInstance(this.context).queryUserSysSetting();
        }
        if (this.tableDirtyFlags[24].booleanValue()) {
            this.tableDirtyFlags[24] = Boolean.valueOf(false);
            getDataBaseMgrInstance(this.context).queryUserSubtitleSetting();
        }
        if (this.tableDirtyFlags[29].booleanValue()) {
            this.tableDirtyFlags[29] = Boolean.valueOf(false);
            getDataBaseMgrInstance(this.context).queryUsrColorTmpExData();
        }
        if (this.tableDirtyFlags[22].booleanValue()) {
            this.tableDirtyFlags[22] = Boolean.valueOf(false);
            getDataBaseMgrInstance(this.context).querySoundModeSettings();
        }
        if (this.tableDirtyFlags[23].booleanValue()) {
            this.tableDirtyFlags[23] = Boolean.valueOf(false);
            getDataBaseMgrInstance(this.context).querySoundSetting();
            getDataBaseMgrInstance(this.context).querySoundModeSettings();
        }
        if (this.tableDirtyFlags[38].booleanValue()) {
            this.tableDirtyFlags[38] = Boolean.valueOf(false);
            getDataBaseMgrInstance(this.context).queryFactoryColorTempExData();
        }
        if (this.tableDirtyFlags[51].booleanValue()) {
            this.tableDirtyFlags[51] = Boolean.valueOf(false);
            getDataBaseMgrInstance(this.context).querySRSAdjust();
        }
    }

    public void loadEssentialDataFromDB() {
        int CurrentDBInputSourceIdex = 34;
        Cursor cursor = this.cr.query(Uri.parse(this.userSettingSchema + "/systemsetting"), null, null, null, null);
        if (cursor.moveToFirst()) {
            CurrentDBInputSourceIdex = cursor.getInt(cursor.getColumnIndex("enInputSourceType"));
        }
        cursor.close();
        queryAllVideoPara(CurrentDBInputSourceIdex);
        queryFactoryColorTempData();
        queryADCAdjusts();
        queryNonLinearAdjusts();
        queryFactoryExtern();
        queryNoStandSet();
        queryNoStandVifSet();
        querySSCAdjust();
        queryOverscanAdjusts(0);
        queryOverscanAdjusts(1);
        queryOverscanAdjusts(2);
        queryOverscanAdjusts(3);
        queryPEQAdjusts();
        queryUserLocSetting();
        queryUsrColorTmpData();
        queryUserSysSetting();
        queryUserSubtitleSetting();
        queryUsrColorTmpExData();
        querySoundModeSettings();
        querySoundSetting();
        queryFactoryColorTempExData();
        querySRSAdjust();
        queryCustomerCfgMiscSetting();
        queryBlockSysSetting();
    }

    private static void openDB() {
    }

    public void closeDB() {
    }

    public MS_ADC_SETTING queryADCAdjusts() {
        Cursor cursor = this.cr.query(Uri.parse("content://mstar.tv.factory/adcadjust"), null, null, null, null);
        int i = 0;
        int length = this.m_stFactoryAdc.stAdcGainOffsetSetting.length;
        while (cursor.moveToNext() && i <= length - 1) {
            this.m_stFactoryAdc.stAdcGainOffsetSetting[i].redgain = cursor.getInt(cursor.getColumnIndex("u16RedGain"));
            this.m_stFactoryAdc.stAdcGainOffsetSetting[i].greengain = cursor.getInt(cursor.getColumnIndex("u16GreenGain"));
            this.m_stFactoryAdc.stAdcGainOffsetSetting[i].bluegain = cursor.getInt(cursor.getColumnIndex("u16BlueGain"));
            this.m_stFactoryAdc.stAdcGainOffsetSetting[i].redoffset = cursor.getInt(cursor.getColumnIndex("u16RedOffset"));
            this.m_stFactoryAdc.stAdcGainOffsetSetting[i].greenoffset = cursor.getInt(cursor.getColumnIndex("u16GreenOffset"));
            this.m_stFactoryAdc.stAdcGainOffsetSetting[i].blueoffset = cursor.getInt(cursor.getColumnIndex("u16BlueOffset"));
            i++;
        }
        cursor.close();
        return this.m_stFactoryAdc;
    }

    public void updateADCAdjust(T_MS_CALIBRATION_DATA model, int sourceId) {
        ContentValues vals = new ContentValues();
        vals.put("u16RedGain", Integer.valueOf(model.redgain));
        vals.put("u16GreenGain", Integer.valueOf(model.greengain));
        vals.put("u16BlueGain", Integer.valueOf(model.bluegain));
        vals.put("u16RedOffset", Integer.valueOf(model.redoffset));
        vals.put("u16GreenOffset", Integer.valueOf(model.greenoffset));
        vals.put("u16BlueOffset", Integer.valueOf(model.blueoffset));
        long ret = -1;
        try {
            ret = (long) this.cr.update(Uri.parse("content://mstar.tv.factory/adcadjust/sourceid/" + sourceId), vals, null, null);
        } catch (SQLException e) {
            Log.e("DataBaseDeskImpl", "update failed");
        }
        if (ret != -1) {
            try {
                if (TvManager.getInstance() != null) {
                    TvManager.getInstance().getDatabaseManager().setDatabaseDirtyByApplication(36);
                }
            } catch (TvCommonException e2) {
                e2.printStackTrace();
            }
        }
    }

    public MS_NLA_SETTING queryNonLinearAdjusts() {
        String[] selectionArgs = {String.valueOf(this.f0com.GetCurrentInputSource().ordinal())};
        Cursor cursor = this.cr.query(Uri.parse(this.factoryCusSchema + "/nonlinearadjust"), null, " InputSrcType = ? ", selectionArgs, "CurveTypeIndex");
        int i = 0;
        int length = this.m_pastNLASet.stNLASetting.length;
        while (cursor.moveToNext() && i <= length - 1) {
            this.m_pastNLASet.stNLASetting[i].u8OSD_V0 = (short) cursor.getInt(cursor.getColumnIndex("u8OSD_V0"));
            this.m_pastNLASet.stNLASetting[i].u8OSD_V25 = (short) cursor.getInt(cursor.getColumnIndex("u8OSD_V25"));
            this.m_pastNLASet.stNLASetting[i].u8OSD_V50 = (short) cursor.getInt(cursor.getColumnIndex("u8OSD_V50"));
            this.m_pastNLASet.stNLASetting[i].u8OSD_V75 = (short) cursor.getInt(cursor.getColumnIndex("u8OSD_V75"));
            this.m_pastNLASet.stNLASetting[i].u8OSD_V100 = (short) cursor.getInt(cursor.getColumnIndex("u8OSD_V100"));
            i++;
        }
        cursor.close();
        return this.m_pastNLASet;
    }

    public void updateNonLinearAdjust(MS_NLA_POINT dataModel, int curveTypeIndex) {
        ContentValues vals = new ContentValues();
        vals.put("u8OSD_V0", Short.valueOf(dataModel.u8OSD_V0));
        vals.put("u8OSD_V25", Short.valueOf(dataModel.u8OSD_V25));
        vals.put("u8OSD_V50", Short.valueOf(dataModel.u8OSD_V50));
        vals.put("u8OSD_V75", Short.valueOf(dataModel.u8OSD_V75));
        vals.put("u8OSD_V100", Short.valueOf(dataModel.u8OSD_V100));
        long ret = -1;
        try {
            ret = (long) this.cr.update(Uri.parse(this.factoryCusSchema + "/nonlinearadjust"), vals, " CurveTypeIndex = ? and InputSrcType = ? ", new String[]{String.valueOf(curveTypeIndex), String.valueOf(this.f0com.GetCurrentInputSource().ordinal())});
        } catch (SQLException e) {
            Log.e("DataBaseDeskImpl", "update failed");
        }
        if (ret != -1) {
            try {
                if (TvManager.getInstance() != null) {
                    TvManager.getInstance().getDatabaseManager().setDatabaseDirtyByApplication(42);
                }
            } catch (TvCommonException e2) {
                e2.printStackTrace();
            }
        }
    }

    public MS_Factory_NS_VD_SET queryNoStandSet() {
        Cursor cursor = this.cr.query(Uri.parse("content://mstar.tv.factory/nonstandardadjust"), null, null, null, null);
        if (cursor.moveToFirst()) {
            this.mNoStandSet.u8AFEC_D4 = (short) cursor.getInt(cursor.getColumnIndex("u8AFEC_D4"));
            this.mNoStandSet.u8AFEC_D5_Bit2 = (short) cursor.getInt(cursor.getColumnIndex("u8AFEC_D5_Bit2"));
            this.mNoStandSet.u8AFEC_D8_Bit3210 = (short) cursor.getInt(cursor.getColumnIndex("u8AFEC_D8_Bit3210"));
            this.mNoStandSet.u8AFEC_D9_Bit0 = (short) cursor.getInt(cursor.getColumnIndex("u8AFEC_D9_Bit0"));
            this.mNoStandSet.u8AFEC_D7_LOW_BOUND = (short) cursor.getInt(cursor.getColumnIndex("u8AFEC_D7_LOW_BOUND"));
            this.mNoStandSet.u8AFEC_D7_HIGH_BOUND = (short) cursor.getInt(cursor.getColumnIndex("u8AFEC_D7_HIGH_BOUND"));
            this.mNoStandSet.u8AFEC_A0 = (short) cursor.getInt(cursor.getColumnIndex("u8AFEC_A0"));
            this.mNoStandSet.u8AFEC_A1 = (short) cursor.getInt(cursor.getColumnIndex("u8AFEC_A1"));
            this.mNoStandSet.u8AFEC_66_Bit76 = (short) cursor.getInt(cursor.getColumnIndex("u8AFEC_66_Bit76"));
            this.mNoStandSet.u8AFEC_6E_Bit7654 = (short) cursor.getInt(cursor.getColumnIndex("u8AFEC_6E_Bit7654"));
            this.mNoStandSet.u8AFEC_6E_Bit3210 = (short) cursor.getInt(cursor.getColumnIndex("u8AFEC_6E_Bit3210"));
            this.mNoStandSet.u8AFEC_44 = (short) cursor.getInt(cursor.getColumnIndex("u8AFEC_44"));
            this.mNoStandSet.u8AFEC_CB = (short) cursor.getInt(cursor.getColumnIndex("u8AFEC_CB"));
        }
        cursor.close();
        return this.mNoStandSet;
    }

    public MS_Factory_NS_VIF_SET queryNoStandVifSet() {
        boolean z;
        boolean z2;
        boolean z3 = false;
        Cursor cursor = this.cr.query(Uri.parse("content://mstar.tv.factory/nonstandardadjust"), null, null, null, null);
        if (cursor.moveToFirst()) {
            this.mVifSet.VifTop = (short) cursor.getInt(cursor.getColumnIndex("VifTop"));
            this.mVifSet.VifVgaMaximum = cursor.getInt(cursor.getColumnIndex("VifVgaMaximum"));
            this.mVifSet.VifCrKp = (short) cursor.getInt(cursor.getColumnIndex("VifCrKp"));
            this.mVifSet.VifCrKi = (short) cursor.getInt(cursor.getColumnIndex("VifCrKi"));
            this.mVifSet.VifCrKp1 = (short) cursor.getInt(cursor.getColumnIndex("VifCrKp1"));
            this.mVifSet.VifCrKi1 = (short) cursor.getInt(cursor.getColumnIndex("VifCrKi1"));
            this.mVifSet.VifCrKp2 = (short) cursor.getInt(cursor.getColumnIndex("VifCrKp2"));
            this.mVifSet.VifCrKi2 = (short) cursor.getInt(cursor.getColumnIndex("VifCrKi2"));
            MS_Factory_NS_VIF_SET mS_Factory_NS_VIF_SET = this.mVifSet;
            if (cursor.getInt(cursor.getColumnIndex("VifAsiaSignalOption")) == 0) {
                z = false;
            } else {
                z = true;
            }
            mS_Factory_NS_VIF_SET.VifAsiaSignalOption = z;
            MS_Factory_NS_VIF_SET mS_Factory_NS_VIF_SET2 = this.mVifSet;
            if (((short) cursor.getInt(cursor.getColumnIndex("VifCrKpKiAdjust"))) == 0) {
                z2 = false;
            } else {
                z2 = true;
            }
            mS_Factory_NS_VIF_SET2.VifCrKpKiAdjust = z2;
            MS_Factory_NS_VIF_SET mS_Factory_NS_VIF_SET3 = this.mVifSet;
            if (cursor.getInt(cursor.getColumnIndex("VifOverModulation")) != 0) {
                z3 = true;
            }
            mS_Factory_NS_VIF_SET3.VifOverModulation = z3;
            this.mVifSet.VifClampgainGainOvNegative = cursor.getInt(cursor.getColumnIndex("VifClampgainGainOvNegative"));
            this.mVifSet.ChinaDescramblerBox = (short) cursor.getInt(cursor.getColumnIndex("ChinaDescramblerBox"));
            this.mVifSet.VifDelayReduce = (short) cursor.getInt(cursor.getColumnIndex("VifDelayReduce"));
            this.mVifSet.VifCrThr = (short) cursor.getInt(cursor.getColumnIndex("VifCrThr"));
            this.mVifSet.VifVersion = (short) cursor.getInt(cursor.getColumnIndex("VifVersion"));
            this.mVifSet.VifACIAGCREF = (short) cursor.getInt(cursor.getColumnIndex("VifACIAGCREF"));
            this.mVifSet.GainDistributionThr = cursor.getInt(cursor.getColumnIndex("GainDistributionThr"));
        }
        cursor.close();
        return this.mVifSet;
    }

    public void updateNonStandardAdjust(MS_Factory_NS_VD_SET nonStandSet) {
        ContentValues vals = new ContentValues();
        vals.put("u8AFEC_D4", Short.valueOf(nonStandSet.u8AFEC_D4));
        vals.put("u8AFEC_D5_Bit2", Short.valueOf(nonStandSet.u8AFEC_D5_Bit2));
        vals.put("u8AFEC_D8_Bit3210", Short.valueOf(nonStandSet.u8AFEC_D8_Bit3210));
        vals.put("u8AFEC_D9_Bit0", Short.valueOf(nonStandSet.u8AFEC_D9_Bit0));
        vals.put("u8AFEC_D7_LOW_BOUND", Short.valueOf(nonStandSet.u8AFEC_D7_LOW_BOUND));
        vals.put("u8AFEC_D7_HIGH_BOUND", Short.valueOf(nonStandSet.u8AFEC_D7_HIGH_BOUND));
        vals.put("u8AFEC_A0", Short.valueOf(nonStandSet.u8AFEC_A0));
        vals.put("u8AFEC_A1", Short.valueOf(nonStandSet.u8AFEC_A1));
        vals.put("u8AFEC_66_Bit76", Short.valueOf(nonStandSet.u8AFEC_66_Bit76));
        vals.put("u8AFEC_6E_Bit7654", Short.valueOf(nonStandSet.u8AFEC_6E_Bit7654));
        vals.put("u8AFEC_6E_Bit3210", Short.valueOf(nonStandSet.u8AFEC_6E_Bit3210));
        vals.put("u8AFEC_43", Short.valueOf(nonStandSet.u8AFEC_43));
        vals.put("u8AFEC_44", Short.valueOf(nonStandSet.u8AFEC_44));
        vals.put("u8AFEC_CB", Short.valueOf(nonStandSet.u8AFEC_CB));
        long ret = -1;
        try {
            ret = (long) this.cr.update(Uri.parse("content://mstar.tv.facory/nonstandardadjust"), vals, null, null);
        } catch (SQLException e) {
            Log.e("DataBaseDeskImpl", "update failed");
        }
        if (ret != -1) {
            try {
                if (TvManager.getInstance() != null) {
                    TvManager.getInstance().getDatabaseManager().setDatabaseDirtyByApplication(40);
                }
            } catch (TvCommonException e2) {
                e2.printStackTrace();
            }
        }
    }

    public void updateNonStandardAdjust(MS_Factory_NS_VIF_SET vifSet) {
        int i;
        int i2;
        int i3 = 1;
        ContentValues vals = new ContentValues();
        vals.put("VifTop", Short.valueOf(vifSet.VifTop));
        vals.put("VifVgaMaximum", Integer.valueOf(vifSet.VifVgaMaximum));
        vals.put("VifCrKp", Short.valueOf(vifSet.VifCrKp));
        vals.put("VifCrKi", Short.valueOf(vifSet.VifCrKi));
        vals.put("VifCrKp1", Short.valueOf(vifSet.VifCrKp1));
        vals.put("VifCrKi1", Short.valueOf(vifSet.VifCrKi1));
        vals.put("VifCrKp2", Short.valueOf(vifSet.VifCrKp2));
        vals.put("VifCrKi2", Short.valueOf(vifSet.VifCrKi2));
        String str = "VifAsiaSignalOption";
        if (vifSet.VifAsiaSignalOption) {
            i = 1;
        } else {
            i = 0;
        }
        vals.put(str, Integer.valueOf(i));
        String str2 = "VifCrKpKiAdjust";
        if (vifSet.VifCrKpKiAdjust) {
            i2 = 1;
        } else {
            i2 = 0;
        }
        vals.put(str2, Integer.valueOf(i2));
        String str3 = "VifOverModulation";
        if (!vifSet.VifOverModulation) {
            i3 = 0;
        }
        vals.put(str3, Integer.valueOf(i3));
        vals.put("VifClampgainGainOvNegative", Integer.valueOf(vifSet.VifClampgainGainOvNegative));
        vals.put("ChinaDescramblerBox", Short.valueOf(vifSet.ChinaDescramblerBox));
        vals.put("VifDelayReduce", Short.valueOf(vifSet.VifDelayReduce));
        vals.put("VifCrThr", Short.valueOf(vifSet.VifVersion));
        vals.put("VifVersion", Short.valueOf(vifSet.VifVersion));
        vals.put("VifACIAGCREF", Short.valueOf(vifSet.VifACIAGCREF));
        vals.put("GainDistributionThr", Integer.valueOf(vifSet.GainDistributionThr));
        long ret = -1;
        try {
            ret = (long) this.cr.update(Uri.parse("content://mstar.tv.facory/nonstandardadjust"), vals, null, null);
        } catch (SQLException e) {
            Log.e("DataBaseDeskImpl", "update failed");
        }
        if (ret != -1) {
            try {
                if (TvManager.getInstance() != null) {
                    TvManager.getInstance().getDatabaseManager().setDatabaseDirtyByApplication(40);
                }
            } catch (TvCommonException e2) {
                e2.printStackTrace();
            }
        }
    }

    public MS_FACTORY_EXTERN_SETTING queryFactoryExtern() {
        boolean z;
        boolean z2 = false;
        Cursor cursor = this.cr.query(Uri.parse("content://mstar.tv.factory/factoryextern"), null, null, null, null);
        if (cursor.moveToFirst()) {
            MS_FACTORY_EXTERN_SETTING.softVersion = cursor.getString(cursor.getColumnIndex("SoftWareVersion"));
            MS_FACTORY_EXTERN_SETTING.boardType = cursor.getString(cursor.getColumnIndex("BoardType"));
            MS_FACTORY_EXTERN_SETTING.panelType = cursor.getString(cursor.getColumnIndex("PanelType"));
            MS_FACTORY_EXTERN_SETTING.dayAndTime = cursor.getString(cursor.getColumnIndex("CompileTime"));
            this.m_stFactoryExt.testPatternMode = cursor.getInt(cursor.getColumnIndex("TestPatternMode"));
            this.m_stFactoryExt.stPowerMode = cursor.getInt(cursor.getColumnIndex("stPowerMode"));
            MS_FACTORY_EXTERN_SETTING ms_factory_extern_setting = this.m_stFactoryExt;
            if (cursor.getInt(cursor.getColumnIndex("DtvAvAbnormalDelay")) == 0) {
                z = false;
            } else {
                z = true;
            }
            ms_factory_extern_setting.dtvAvAbnormalDelay = z;
            this.m_stFactoryExt.factoryPreset = cursor.getInt(cursor.getColumnIndex("FactoryPreSetFeature"));
            this.m_stFactoryExt.panelSwingVal = (short) cursor.getInt(cursor.getColumnIndex("PanelSwing"));
            this.m_stFactoryExt.audioPreScale = (short) cursor.getInt(cursor.getColumnIndex("AudioPrescale"));
            this.m_stFactoryExt.vdDspVersion = (short) cursor.getInt(cursor.getColumnIndex("vdDspVersion"));
            this.m_stFactoryExt.eHidevMode = (short) cursor.getInt(cursor.getColumnIndex("eHidevMode"));
            this.m_stFactoryExt.audioNrThr = (short) cursor.getInt(cursor.getColumnIndex("audioNrThr"));
            this.m_stFactoryExt.audioSifThreshold = (short) cursor.getInt(cursor.getColumnIndex("audioSifThreshold"));
            this.m_stFactoryExt.audioDspVersion = (short) cursor.getInt(cursor.getColumnIndex("audioDspVersion"));
            MS_FACTORY_EXTERN_SETTING ms_factory_extern_setting2 = this.m_stFactoryExt;
            if (cursor.getInt(cursor.getColumnIndex("bBurnIn")) != 0) {
                z2 = true;
            }
            ms_factory_extern_setting2.bBurnIn = z2;
        }
        cursor.close();
        return this.m_stFactoryExt;
    }

    public void updateFactoryExtern(MS_FACTORY_EXTERN_SETTING model) {
        int i;
        int i2 = 1;
        ContentValues vals = new ContentValues();
        vals.put("SoftWareVersion", MS_FACTORY_EXTERN_SETTING.softVersion);
        vals.put("BoardType", MS_FACTORY_EXTERN_SETTING.boardType);
        vals.put("PanelType", MS_FACTORY_EXTERN_SETTING.panelType);
        vals.put("CompileTime", MS_FACTORY_EXTERN_SETTING.dayAndTime);
        vals.put("TestPatternMode", Integer.valueOf(model.testPatternMode));
        vals.put("stPowerMode", Integer.valueOf(model.stPowerMode));
        String str = "DtvAvAbnormalDelay";
        if (model.dtvAvAbnormalDelay) {
            i = 1;
        } else {
            i = 0;
        }
        vals.put(str, Integer.valueOf(i));
        vals.put("FactoryPreSetFeature", Integer.valueOf(model.factoryPreset));
        vals.put("PanelSwing", Short.valueOf(model.panelSwingVal));
        vals.put("AudioPrescale", Short.valueOf(model.audioPreScale));
        vals.put("vdDspVersion", Short.valueOf(model.vdDspVersion));
        vals.put("eHidevMode", Integer.valueOf(model.eHidevMode));
        vals.put("audioNrThr", Short.valueOf(model.audioNrThr));
        vals.put("audioSifThreshold", Short.valueOf(model.audioSifThreshold));
        vals.put("audioDspVersion", Short.valueOf(model.audioDspVersion));
        String str2 = "bBurnIn";
        if (!model.bBurnIn) {
            i2 = 0;
        }
        vals.put(str2, Integer.valueOf(i2));
        long ret = -1;
        try {
            ret = (long) this.cr.update(Uri.parse("content://mstar.tv.factory/factoryextern"), vals, null, null);
        } catch (SQLException e) {
            Log.e("DataBaseDeskImpl", "update failed");
        }
        if (ret != -1) {
            try {
                if (TvManager.getInstance() != null) {
                    TvManager.getInstance().getDatabaseManager().setDatabaseDirtyByApplication(39);
                }
            } catch (TvCommonException e2) {
                e2.printStackTrace();
            }
        }
    }

    public ST_MAPI_VIDEO_WINDOW_INFO[][] queryOverscanAdjusts(int FactoryOverScanType) {
        switch (FactoryOverScanType) {
            case 0:
                String[] selectionArgs = {String.valueOf(FactoryOverScanType)};
                Cursor cursor = this.cr.query(Uri.parse("content://mstar.tv.factory/overscanadjust"), null, " FactoryOverScanType = ? ", selectionArgs, "_id");
                int maxDTV1 = MAX_DTV_Resolution_Info.E_DTV_MAX.ordinal();
                int maxDTV2 = MAPI_VIDEO_ARC_Type.E_AR_MAX.ordinal();
                for (int i = 0; i < maxDTV1; i++) {
                    for (int j = 0; j < maxDTV2; j++) {
                        if (cursor.moveToNext()) {
                            this.m_DTVOverscanSet[i][j].u16H_CapStart = cursor.getInt(cursor.getColumnIndex("u16H_CapStart"));
                            this.m_DTVOverscanSet[i][j].u16V_CapStart = cursor.getInt(cursor.getColumnIndex("u16V_CapStart"));
                            this.m_DTVOverscanSet[i][j].u8HCrop_Left = (short) cursor.getInt(cursor.getColumnIndex("u8HCrop_Left"));
                            this.m_DTVOverscanSet[i][j].u8HCrop_Right = (short) cursor.getInt(cursor.getColumnIndex("u8HCrop_Right"));
                            this.m_DTVOverscanSet[i][j].u8VCrop_Up = (short) cursor.getInt(cursor.getColumnIndex("u8VCrop_Up"));
                            this.m_DTVOverscanSet[i][j].u8VCrop_Down = (short) cursor.getInt(cursor.getColumnIndex("u8VCrop_Down"));
                        }
                    }
                }
                cursor.close();
                return this.m_DTVOverscanSet;
            case 1:
                String[] selectionArgs2 = {String.valueOf(FactoryOverScanType)};
                Cursor cursor1 = this.cr.query(Uri.parse("content://mstar.tv.factory/overscanadjust"), null, " FactoryOverScanType = ? ", selectionArgs2, "_id");
                int maxHDMI1 = MAX_HDMI_Resolution_Info.E_HDMI_MAX.ordinal();
                int maxHDMI2 = MAPI_VIDEO_ARC_Type.E_AR_MAX.ordinal();
                for (int i2 = 0; i2 < maxHDMI1; i2++) {
                    for (int j2 = 0; j2 < maxHDMI2; j2++) {
                        if (cursor1.moveToNext()) {
                            this.m_HDMIOverscanSet[i2][j2].u16H_CapStart = cursor1.getInt(cursor1.getColumnIndex("u16H_CapStart"));
                            this.m_HDMIOverscanSet[i2][j2].u16V_CapStart = cursor1.getInt(cursor1.getColumnIndex("u16V_CapStart"));
                            this.m_HDMIOverscanSet[i2][j2].u8HCrop_Left = (short) cursor1.getInt(cursor1.getColumnIndex("u8HCrop_Left"));
                            this.m_HDMIOverscanSet[i2][j2].u8HCrop_Right = (short) cursor1.getInt(cursor1.getColumnIndex("u8HCrop_Right"));
                            this.m_HDMIOverscanSet[i2][j2].u8VCrop_Up = (short) cursor1.getInt(cursor1.getColumnIndex("u8VCrop_Up"));
                            this.m_HDMIOverscanSet[i2][j2].u8VCrop_Down = (short) cursor1.getInt(cursor1.getColumnIndex("u8VCrop_Down"));
                        }
                    }
                }
                cursor1.close();
                return this.m_HDMIOverscanSet;
            case 2:
                String[] selectionArgs3 = {String.valueOf(FactoryOverScanType)};
                Cursor cursor2 = this.cr.query(Uri.parse("content://mstar.tv.factory/overscanadjust"), null, " FactoryOverScanType = ? ", selectionArgs3, "_id");
                int maxYPbPr1 = MAX_YPbPr_Resolution_Info.E_YPbPr_MAX.ordinal();
                int maxYPbPr2 = MAPI_VIDEO_ARC_Type.E_AR_MAX.ordinal();
                for (int i3 = 0; i3 < maxYPbPr1; i3++) {
                    for (int j3 = 0; j3 < maxYPbPr2; j3++) {
                        if (cursor2.moveToNext()) {
                            this.m_YPbPrOverscanSet[i3][j3].u16H_CapStart = cursor2.getInt(cursor2.getColumnIndex("u16H_CapStart"));
                            this.m_YPbPrOverscanSet[i3][j3].u16V_CapStart = cursor2.getInt(cursor2.getColumnIndex("u16V_CapStart"));
                            this.m_YPbPrOverscanSet[i3][j3].u8HCrop_Left = (short) cursor2.getInt(cursor2.getColumnIndex("u8HCrop_Left"));
                            this.m_YPbPrOverscanSet[i3][j3].u8HCrop_Right = (short) cursor2.getInt(cursor2.getColumnIndex("u8HCrop_Right"));
                            this.m_YPbPrOverscanSet[i3][j3].u8VCrop_Up = (short) cursor2.getInt(cursor2.getColumnIndex("u8VCrop_Up"));
                            this.m_YPbPrOverscanSet[i3][j3].u8VCrop_Down = (short) cursor2.getInt(cursor2.getColumnIndex("u8VCrop_Down"));
                        }
                    }
                }
                cursor2.close();
                return this.m_YPbPrOverscanSet;
            case 3:
                String[] selectionArgs4 = {String.valueOf(FactoryOverScanType)};
                Cursor cursor3 = this.cr.query(Uri.parse("content://mstar.tv.factory/overscanadjust"), null, " FactoryOverScanType = ? ", selectionArgs4, "_id");
                int maxVD1 = EN_VD_SIGNALTYPE.SIG_NUMS.ordinal();
                int maxVD2 = MAPI_VIDEO_ARC_Type.E_AR_MAX.ordinal();
                for (int i4 = 0; i4 < maxVD1; i4++) {
                    for (int j4 = 0; j4 < maxVD2; j4++) {
                        if (cursor3.moveToNext()) {
                            this.m_VDOverscanSet[i4][j4].u16H_CapStart = cursor3.getInt(cursor3.getColumnIndex("u16H_CapStart"));
                            this.m_VDOverscanSet[i4][j4].u16V_CapStart = cursor3.getInt(cursor3.getColumnIndex("u16V_CapStart"));
                            this.m_VDOverscanSet[i4][j4].u8HCrop_Left = (short) cursor3.getInt(cursor3.getColumnIndex("u8HCrop_Left"));
                            this.m_VDOverscanSet[i4][j4].u8HCrop_Right = (short) cursor3.getInt(cursor3.getColumnIndex("u8HCrop_Right"));
                            this.m_VDOverscanSet[i4][j4].u8VCrop_Up = (short) cursor3.getInt(cursor3.getColumnIndex("u8VCrop_Up"));
                            this.m_VDOverscanSet[i4][j4].u8VCrop_Down = (short) cursor3.getInt(cursor3.getColumnIndex("u8VCrop_Down"));
                        }
                    }
                }
                cursor3.close();
                return this.m_VDOverscanSet;
            default:
                return null;
        }
    }

    public void updateOverscanAdjust(int FactoryOverScanType, ST_MAPI_VIDEO_WINDOW_INFO[][] model) {
        int max1 = 0;
        switch (FactoryOverScanType) {
            case 0:
                max1 = MAX_DTV_Resolution_Info.E_DTV_MAX.ordinal();
                break;
            case 1:
                max1 = MAX_HDMI_Resolution_Info.E_HDMI_MAX.ordinal();
                break;
            case 2:
                max1 = MAX_YPbPr_Resolution_Info.E_YPbPr_MAX.ordinal();
                break;
            case 3:
                max1 = EN_VD_SIGNALTYPE.SIG_NUMS.ordinal();
                break;
        }
        int max2 = MAPI_VIDEO_ARC_Type.E_AR_MAX.ordinal();
        for (int i = 0; i < max1; i++) {
            int j = 0;
            while (j < max2) {
                ContentValues vals = new ContentValues();
                vals.put("u16H_CapStart", Integer.valueOf(model[i][j].u16H_CapStart));
                vals.put("u16V_CapStart", Integer.valueOf(model[i][j].u16V_CapStart));
                vals.put("u8HCrop_Left", Short.valueOf(model[i][j].u8HCrop_Left));
                vals.put("u8HCrop_Right", Short.valueOf(model[i][j].u8HCrop_Right));
                vals.put("u8VCrop_Up", Short.valueOf(model[i][j].u8VCrop_Up));
                vals.put("u8VCrop_Down", Short.valueOf(model[i][j].u8VCrop_Down));
                long ret = -1;
                try {
                    ret = (long) this.cr.update(Uri.parse("content://mstar.tv.factory/overscanadjust/factoryoverscantype/" + FactoryOverScanType + "/_id/" + ((i * max2) + j)), vals, null, null);
                } catch (SQLException e) {
                    Log.e("DataBaseDeskImpl", "update failed");
                }
                if (ret != -1) {
                    j++;
                } else {
                    return;
                }
            }
        }
        try {
            if (TvManager.getInstance() != null) {
                TvManager.getInstance().getDatabaseManager().setDatabaseDirtyByApplication(43);
            }
        } catch (TvCommonException e2) {
            e2.printStackTrace();
        }
    }

    public AUDIO_PEQ_PARAM queryPEQAdjust(int index) {
        AUDIO_PEQ_PARAM model = new AUDIO_PEQ_PARAM();
        Cursor cursor = this.cr.query(Uri.parse("content://mstar.tv.factory/peqadjust/" + index), null, null, null, null);
        if (cursor.moveToFirst()) {
            model.Band = cursor.getInt(cursor.getColumnIndex("Band"));
            model.Gain = cursor.getInt(cursor.getColumnIndex("Gain"));
            model.Foh = cursor.getInt(cursor.getColumnIndex("Foh"));
            model.Fol = cursor.getInt(cursor.getColumnIndex("Fol"));
            model.QValue = cursor.getInt(cursor.getColumnIndex("QValue"));
        }
        cursor.close();
        return model;
    }

    public ST_FACTORY_PEQ_SETTING queryPEQAdjusts() {
        Cursor cursor = this.cr.query(Uri.parse("content://mstar.tv.factory/peqadjust"), null, null, null, null);
        int i = 0;
        int length = this.m_stPEQSet.stPEQParam.length;
        while (cursor.moveToNext() && i <= length - 1) {
            this.m_stPEQSet.stPEQParam[i].Band = cursor.getInt(cursor.getColumnIndex("Band"));
            this.m_stPEQSet.stPEQParam[i].Gain = cursor.getInt(cursor.getColumnIndex("Gain"));
            this.m_stPEQSet.stPEQParam[i].Foh = cursor.getInt(cursor.getColumnIndex("Foh"));
            this.m_stPEQSet.stPEQParam[i].Fol = cursor.getInt(cursor.getColumnIndex("Fol"));
            this.m_stPEQSet.stPEQParam[i].QValue = cursor.getInt(cursor.getColumnIndex("QValue"));
            i++;
        }
        cursor.close();
        return this.m_stPEQSet;
    }

    public void updatePEQAdjust(AUDIO_PEQ_PARAM model, int index) {
        ContentValues vals = new ContentValues();
        vals.put("Band", Integer.valueOf(model.Band));
        vals.put("Gain", Integer.valueOf(model.Gain));
        vals.put("Foh", Integer.valueOf(model.Foh));
        vals.put("Fol", Integer.valueOf(model.Fol));
        vals.put("QValue", Integer.valueOf(model.QValue));
        long ret = -1;
        try {
            ret = (long) this.cr.update(Uri.parse("content://mstar.tv.factory/peqadjust/" + index), vals, null, null);
        } catch (SQLException e) {
            Log.e("DataBaseDeskImpl", "update failed");
        }
        if (ret != -1) {
            try {
                if (TvManager.getInstance() != null) {
                    TvManager.getInstance().getDatabaseManager().setDatabaseDirtyByApplication(44);
                }
            } catch (TvCommonException e2) {
                e2.printStackTrace();
            }
        }
    }

    public MS_FACTORY_SSC_SET querySSCAdjust() {
        boolean z = false;
        Cursor cursor = this.cr.query(Uri.parse("content://mstar.tv.factory/sscadjust"), null, null, null, null);
        if (cursor.moveToFirst()) {
            this.mSscSet.Miu_SscEnable = cursor.getInt(cursor.getColumnIndex("Miu_SscEnable")) != 0;
            MS_FACTORY_SSC_SET ms_factory_ssc_set = this.mSscSet;
            if (cursor.getInt(cursor.getColumnIndex("Lvds_SscEnable")) != 0) {
                z = true;
            }
            ms_factory_ssc_set.Lvds_SscEnable = z;
            this.mSscSet.Lvds_SscSpan = cursor.getInt(cursor.getColumnIndex("Lvds_SscSpan"));
            this.mSscSet.Lvds_SscStep = cursor.getInt(cursor.getColumnIndex("Lvds_SscStep"));
            this.mSscSet.Miu0_SscSpan = cursor.getInt(cursor.getColumnIndex("Miu_SscSpan"));
            this.mSscSet.Miu0_SscStep = cursor.getInt(cursor.getColumnIndex("Miu_SscStep"));
            this.mSscSet.Miu1_SscSpan = cursor.getInt(cursor.getColumnIndex("Miu1_SscSpan"));
            this.mSscSet.Miu1_SscStep = cursor.getInt(cursor.getColumnIndex("Miu1_SscStep"));
            this.mSscSet.Miu2_SscSpan = cursor.getInt(cursor.getColumnIndex("Miu2_SscSpan"));
            this.mSscSet.Miu2_SscStep = cursor.getInt(cursor.getColumnIndex("Miu2_SscStep"));
        }
        cursor.close();
        return this.mSscSet;
    }

    public void updateSSCAdjust(MS_FACTORY_SSC_SET model) {
        int i;
        int i2 = 1;
        ContentValues vals = new ContentValues();
        String str = "Miu_SscEnable";
        if (model.Miu_SscEnable) {
            i = 1;
        } else {
            i = 0;
        }
        vals.put(str, Integer.valueOf(i));
        String str2 = "Lvds_SscEnable";
        if (!model.Lvds_SscEnable) {
            i2 = 0;
        }
        vals.put(str2, Integer.valueOf(i2));
        vals.put("Lvds_SscSpan", Integer.valueOf(model.Lvds_SscSpan));
        vals.put("Lvds_SscStep", Integer.valueOf(model.Lvds_SscStep));
        vals.put("Miu_SscSpan", Integer.valueOf(model.Miu0_SscSpan));
        vals.put("Miu_SscStep", Integer.valueOf(model.Miu0_SscStep));
        vals.put("Miu1_SscSpan", Integer.valueOf(model.Miu1_SscSpan));
        vals.put("Miu1_SscStep", Integer.valueOf(model.Miu1_SscStep));
        vals.put("Miu2_SscSpan", Integer.valueOf(model.Miu2_SscSpan));
        vals.put("Miu2_SscStep", Integer.valueOf(model.Miu2_SscStep));
        long ret = -1;
        try {
            ret = (long) this.cr.update(Uri.parse("content://mstar.tv.factory/sscadjust"), vals, null, null);
        } catch (SQLException e) {
            Log.e("DataBaseDeskImpl", "update failed");
        }
        if (ret != -1) {
            try {
                if (TvManager.getInstance() != null) {
                    TvManager.getInstance().getDatabaseManager().setDatabaseDirtyByApplication(41);
                }
            } catch (TvCommonException e2) {
                e2.printStackTrace();
            }
        }
    }

    public T_MS_COLOR_TEMP queryFactoryColorTempData() {
        Cursor cursor = this.cr.query(Uri.parse("content://mstar.tv.factory/factorycolortemp"), null, null, null, "ColorTemperatureID");
        int i = 0;
        int length = this.m_stFactoryColorTemp.astColorTemp.length;
        while (cursor.moveToNext() && i <= length - 1) {
            this.m_stFactoryColorTemp.astColorTemp[i].redgain = (short) cursor.getInt(cursor.getColumnIndex("u8RedGain"));
            this.m_stFactoryColorTemp.astColorTemp[i].greengain = (short) cursor.getInt(cursor.getColumnIndex("u8GreenGain"));
            this.m_stFactoryColorTemp.astColorTemp[i].bluegain = (short) cursor.getInt(cursor.getColumnIndex("u8BlueGain"));
            this.m_stFactoryColorTemp.astColorTemp[i].redoffset = (short) cursor.getInt(cursor.getColumnIndex("u8RedOffset"));
            this.m_stFactoryColorTemp.astColorTemp[i].greenoffset = (short) cursor.getInt(cursor.getColumnIndex("u8GreenOffset"));
            this.m_stFactoryColorTemp.astColorTemp[i].blueoffset = (short) cursor.getInt(cursor.getColumnIndex("u8BlueOffset"));
            i++;
        }
        cursor.close();
        return this.m_stFactoryColorTemp;
    }

    public void updateFactoryColorTempData(T_MS_COLOR_TEMP_DATA model, int colorTmpId) {
        ContentValues vals = new ContentValues();
        vals.put("u8RedGain", Short.valueOf(model.redgain));
        vals.put("u8GreenGain", Short.valueOf(model.greengain));
        vals.put("u8BlueGain", Short.valueOf(model.bluegain));
        vals.put("u8RedOffset", Short.valueOf(model.redoffset));
        vals.put("u8GreenOffset", Short.valueOf(model.greenoffset));
        vals.put("u8BlueOffset", Short.valueOf(model.blueoffset));
        long ret = -1;
        try {
            ret = (long) this.cr.update(Uri.parse("content://mstar.tv.factory/factorycolortemp/colortemperatureid/" + colorTmpId), vals, null, null);
        } catch (SQLException e) {
            Log.e("DataBaseDeskImpl", "update failed");
        }
        if (ret != -1) {
            try {
                if (TvManager.getInstance() != null) {
                    TvManager.getInstance().getDatabaseManager().setDatabaseDirtyByApplication(37);
                }
            } catch (TvCommonException e2) {
                e2.printStackTrace();
            }
        }
    }

    public T_MS_COLOR_TEMPEX queryFactoryColorTempExData() {
        String str = StatConstants.MTA_COOPERATION_TAG;
        String[] selectionArgs = new String[1];
        for (int sourceIdx = 0; sourceIdx < EN_MS_COLOR_TEMP_INPUT_SOURCE.E_INPUT_SOURCE_NUM.ordinal(); sourceIdx++) {
            selectionArgs[0] = String.valueOf(sourceIdx);
            Cursor cursor = this.cr.query(Uri.parse(this.factoryCusSchema + "/factorycolortempex"), null, " InputSourceID = ? ", selectionArgs, "ColorTemperatureID");
            for (int colorTmpIdx = 0; colorTmpIdx < EN_MS_COLOR_TEMP.MS_COLOR_TEMP_NUM.ordinal(); colorTmpIdx++) {
                if (cursor.moveToNext()) {
                    this.m_stFactoryColorTempEx.astColorTempEx[colorTmpIdx][sourceIdx].redgain = cursor.getInt(cursor.getColumnIndex("u16RedGain"));
                    this.m_stFactoryColorTempEx.astColorTempEx[colorTmpIdx][sourceIdx].greengain = cursor.getInt(cursor.getColumnIndex("u16GreenGain"));
                    this.m_stFactoryColorTempEx.astColorTempEx[colorTmpIdx][sourceIdx].bluegain = cursor.getInt(cursor.getColumnIndex("u16BlueGain"));
                    this.m_stFactoryColorTempEx.astColorTempEx[colorTmpIdx][sourceIdx].redoffset = cursor.getInt(cursor.getColumnIndex("u16RedOffset"));
                    this.m_stFactoryColorTempEx.astColorTempEx[colorTmpIdx][sourceIdx].greenoffset = cursor.getInt(cursor.getColumnIndex("u16GreenOffset"));
                    this.m_stFactoryColorTempEx.astColorTempEx[colorTmpIdx][sourceIdx].blueoffset = cursor.getInt(cursor.getColumnIndex("u16BlueOffset"));
                }
            }
            cursor.close();
        }
        return this.m_stFactoryColorTempEx;
    }

    public void updateFactoryColorTempExData(T_MS_COLOR_TEMPEX_DATA model, int sourceId, int colorTmpId) {
        ContentValues vals = new ContentValues();
        vals.put("u16RedGain", Integer.valueOf(model.redgain));
        vals.put("u16GreenGain", Integer.valueOf(model.greengain));
        vals.put("u16BlueGain", Integer.valueOf(model.bluegain));
        vals.put("u16RedOffset", Integer.valueOf(model.redoffset));
        vals.put("u16GreenOffset", Integer.valueOf(model.greenoffset));
        vals.put("u16BlueOffset", Integer.valueOf(model.blueoffset));
        long ret = -1;
        try {
            ret = (long) this.cr.update(Uri.parse(this.factoryCusSchema + "/factorycolortempex"), vals, " InputSourceID = ? and ColorTemperatureID = ? ", new String[]{String.valueOf(sourceId), String.valueOf(colorTmpId)});
        } catch (SQLException e) {
            Log.e("DataBaseDeskImpl", "update failed");
        }
        if (ret != -1) {
            try {
                if (TvManager.getInstance() != null) {
                    TvManager.getInstance().getDatabaseManager().setDatabaseDirtyByApplication(38);
                }
            } catch (TvCommonException e2) {
                e2.printStackTrace();
            }
        }
    }

    public T_MS_VIDEO queryAllVideoPara(int inputSrcType) {
        Cursor cursorVideo = this.cr.query(Uri.parse(this.userSettingSchema + "/videosetting/inputsrc/" + inputSrcType), null, null, null, null);
        while (cursorVideo.moveToNext()) {
            this.videopara.ePicture = EN_MS_PICTURE.values()[cursorVideo.getInt(cursorVideo.getColumnIndex("ePicture"))];
            this.videopara.enARCType = MAPI_VIDEO_ARC_Type.values()[cursorVideo.getInt(cursorVideo.getColumnIndex("enARCType"))];
            this.videopara.fOutput_RES = EN_DISPLAY_RES_TYPE.values()[cursorVideo.getInt(cursorVideo.getColumnIndex("fOutput_RES"))];
            this.videopara.tvsys = MAPI_VIDEO_OUT_VE_SYS.values()[cursorVideo.getInt(cursorVideo.getColumnIndex("tvsys"))];
            this.videopara.LastVideoStandardMode = MAPI_AVD_VideoStandardType.values()[cursorVideo.getInt(cursorVideo.getColumnIndex("LastVideoStandardMode"))];
            this.videopara.LastAudioStandardMode = AUDIOMODE_TYPE_.values()[cursorVideo.getInt(cursorVideo.getColumnIndex("LastAudioStandardMode"))];
            this.videopara.eDynamic_Contrast = EN_MS_Dynamic_Contrast.values()[cursorVideo.getInt(cursorVideo.getColumnIndex("eDynamic_Contrast"))];
            this.videopara.eFilm = EN_MS_FILM.values()[cursorVideo.getInt(cursorVideo.getColumnIndex("eFilm"))];
            this.videopara.eTvFormat = EN_DISPLAY_TVFORMAT.values()[cursorVideo.getInt(cursorVideo.getColumnIndex("eTvFormat"))];
            this.videopara.skinTone = SkinToneMode.values()[cursorVideo.getInt(cursorVideo.getColumnIndex("skinTone"))];
            this.videopara.detailEnhance = cursorVideo.getInt(cursorVideo.getColumnIndex("detailEnhance")) != 0;
            this.videopara.DNR = EN_MS_NR.values()[cursorVideo.getInt(cursorVideo.getColumnIndex("DNR"))];
            this.videopara.g_astSubColor.SubBrightness = (short) cursorVideo.getInt(cursorVideo.getColumnIndex("u8SubBrightness"));
            this.videopara.g_astSubColor.SubContrast = (short) cursorVideo.getInt(cursorVideo.getColumnIndex("u8SubContrast"));
        }
        cursorVideo.close();
        String[] selectionArgs = {String.valueOf(inputSrcType)};
        Cursor cursorPicMode = this.cr.query(Uri.parse(this.userSettingSchema + "/picmode_setting"), null, " InputSrcType = ? ", selectionArgs, "PictureModeType");
        int picModeIdx = 0;
        int length = this.videopara.astPicture.length;
        while (cursorPicMode.moveToNext() && picModeIdx <= length - 1) {
            this.videopara.astPicture[picModeIdx].backlight = (short) cursorPicMode.getInt(cursorPicMode.getColumnIndex("u8Backlight"));
            this.videopara.astPicture[picModeIdx].contrast = (short) cursorPicMode.getInt(cursorPicMode.getColumnIndex("u8Contrast"));
            this.videopara.astPicture[picModeIdx].brightness = (short) cursorPicMode.getInt(cursorPicMode.getColumnIndex("u8Brightness"));
            this.videopara.astPicture[picModeIdx].saturation = (short) cursorPicMode.getInt(cursorPicMode.getColumnIndex("u8Saturation"));
            this.videopara.astPicture[picModeIdx].sharpness = (short) cursorPicMode.getInt(cursorPicMode.getColumnIndex("u8Sharpness"));
            this.videopara.astPicture[picModeIdx].hue = (short) cursorPicMode.getInt(cursorPicMode.getColumnIndex("u8Hue"));
            this.videopara.astPicture[picModeIdx].eColorTemp = EN_MS_COLOR_TEMP.values()[cursorPicMode.getInt(cursorPicMode.getColumnIndex("eColorTemp"))];
            this.videopara.astPicture[picModeIdx].eVibrantColour = EN_MS_PIC_ADV.values()[cursorPicMode.getInt(cursorPicMode.getColumnIndex("eVibrantColour"))];
            this.videopara.astPicture[picModeIdx].ePerfectClear = EN_MS_PIC_ADV.values()[cursorPicMode.getInt(cursorPicMode.getColumnIndex("ePerfectClear"))];
            this.videopara.astPicture[picModeIdx].eDynamicContrast = EN_MS_PIC_ADV.values()[cursorPicMode.getInt(cursorPicMode.getColumnIndex("eDynamicContrast"))];
            this.videopara.astPicture[picModeIdx].eDynamicBacklight = EN_MS_PIC_ADV.values()[cursorPicMode.getInt(cursorPicMode.getColumnIndex("eDynamicBacklight"))];
            picModeIdx++;
        }
        cursorPicMode.close();
        selectionArgs[0] = String.valueOf(inputSrcType);
        Cursor cursorNRMode = this.cr.query(Uri.parse(this.userSettingSchema + "/nrmode"), null, " InputSrcType = ? ", selectionArgs, "NRMode");
        int NRModeIdx = 0;
        int length1 = this.videopara.eNRMode.length;
        while (cursorNRMode.moveToNext() && NRModeIdx <= length1 - 1) {
            this.videopara.eNRMode[NRModeIdx].eNR = EN_MS_NR.values()[cursorNRMode.getInt(cursorNRMode.getColumnIndex("eNR"))];
            this.videopara.eNRMode[NRModeIdx].eMPEG_NR = EN_MS_MPEG_NR.values()[cursorNRMode.getInt(cursorNRMode.getColumnIndex("eMPEG_NR"))];
            NRModeIdx++;
        }
        cursorNRMode.close();
        Cursor cursor3DMode = this.cr.query(Uri.parse(this.userSettingSchema + "/threedvideomode/inputsrc/" + inputSrcType), null, null, null, null);
        if (cursor3DMode.moveToFirst()) {
            this.videopara.ThreeDVideoMode.eThreeDVideo = EN_ThreeD_Video.values()[cursor3DMode.getInt(cursor3DMode.getColumnIndex("eThreeDVideo"))];
            this.videopara.ThreeDVideoMode.eThreeDVideo3DDepth = EN_ThreeD_Video_3DDEPTH.values()[cursor3DMode.getInt(cursor3DMode.getColumnIndex("eThreeDVideo3DDepth"))];
            this.videopara.ThreeDVideoMode.eThreeDVideo3DOffset = EN_ThreeD_Video_3DOFFSET.values()[cursor3DMode.getInt(cursor3DMode.getColumnIndex("eThreeDVideo3DOffset"))];
            this.videopara.ThreeDVideoMode.eThreeDVideoAutoStart = EN_ThreeD_Video_AUTOSTART.values()[cursor3DMode.getInt(cursor3DMode.getColumnIndex("eThreeDVideoAutoStart"))];
            this.videopara.ThreeDVideoMode.eThreeDVideo3DOutputAspect = EN_ThreeD_Video_3DOUTPUTASPECT.values()[cursor3DMode.getInt(cursor3DMode.getColumnIndex("eThreeDVideo3DOutputAspect"))];
            this.videopara.ThreeDVideoMode.eThreeDVideoLRViewSwitch = EN_ThreeD_Video_LRVIEWSWITCH.values()[cursor3DMode.getInt(cursor3DMode.getColumnIndex("eThreeDVideoLRViewSwitch"))];
        }
        cursor3DMode.close();
        Cursor cursor3DSelfAdaptiveMode = this.cr.query(Uri.parse(this.userSettingSchema + "/threedvideomode/inputsrc/" + EnumInputSource.E_INPUT_SOURCE_HDMI.ordinal()), null, null, null, null);
        if (cursor3DSelfAdaptiveMode.moveToFirst()) {
            this.videopara.ThreeDVideoMode.eThreeDVideoSelfAdaptiveDetect = EN_ThreeD_Video_SELFADAPTIVE_DETECT.values()[cursor3DSelfAdaptiveMode.getInt(cursor3DSelfAdaptiveMode.getColumnIndex("eThreeDVideoSelfAdaptiveDetect"))];
        }
        cursor3DSelfAdaptiveMode.close();
        Cursor cursorUserOverScanMode = this.cr.query(Uri.parse(this.userSettingSchema + "/useroverscanmode/inputsrc/" + inputSrcType), null, null, null, null);
        if (cursorUserOverScanMode.moveToFirst()) {
            this.videopara.stUserOverScanMode.OverScanHposition = (short) cursorUserOverScanMode.getInt(cursorUserOverScanMode.getColumnIndex("OverScanHposition"));
            this.videopara.stUserOverScanMode.OverScanVposition = (short) cursorUserOverScanMode.getInt(cursorUserOverScanMode.getColumnIndex("OverScanVposition"));
            this.videopara.stUserOverScanMode.OverScanHRatio = (short) cursorUserOverScanMode.getInt(cursorUserOverScanMode.getColumnIndex("OverScanHRatio"));
            this.videopara.stUserOverScanMode.OverScanVRatio = (short) cursorUserOverScanMode.getInt(cursorUserOverScanMode.getColumnIndex("OverScanVRatio"));
        }
        cursorUserOverScanMode.close();
        return this.videopara;
    }

    public EN_ThreeD_Video_DISPLAYFORMAT queryThreeDVideoDisplayFormat(int inputSrcType) {
        EN_ThreeD_Video_DISPLAYFORMAT threeDVideoDisplayFormat = EN_ThreeD_Video_DISPLAYFORMAT.DB_ThreeD_Video_DISPLAYFORMAT_NONE;
        Cursor cursor3DMode = this.cr.query(Uri.parse(this.userSettingSchema + "/threedvideomode/inputsrc/" + inputSrcType), null, null, null, null);
        if (cursor3DMode.moveToFirst()) {
            threeDVideoDisplayFormat = EN_ThreeD_Video_DISPLAYFORMAT.values()[cursor3DMode.getInt(cursor3DMode.getColumnIndex("eThreeDVideoDisplayFormat"))];
        }
        cursor3DMode.close();
        return threeDVideoDisplayFormat;
    }

    public EN_ThreeD_Video_3DTO2D queryThreeDVideo3DTo2D(int inputSrcType) {
        EN_ThreeD_Video_3DTO2D threeDVideo3DTo2D = EN_ThreeD_Video_3DTO2D.DB_ThreeD_Video_3DTO2D_NONE;
        Cursor cursor3DMode = this.cr.query(Uri.parse(this.userSettingSchema + "/threedvideomode/inputsrc/" + inputSrcType), null, null, null, null);
        if (cursor3DMode.moveToFirst()) {
            threeDVideo3DTo2D = EN_ThreeD_Video_3DTO2D.values()[cursor3DMode.getInt(cursor3DMode.getColumnIndex("eThreeDVideo3DTo2D"))];
        }
        cursor3DMode.close();
        return threeDVideo3DTo2D;
    }

    public void updatePicModeSetting(EN_MS_VIDEOITEM eIndex, int inputSrcType, int pictureModeType, int value) {
        ContentValues vals = new ContentValues();
        switch ($SWITCH_TABLE$com$konka$kkinterface$tv$DataBaseDesk$EN_MS_VIDEOITEM()[eIndex.ordinal()]) {
            case 1:
                vals.put("u8Brightness", Integer.valueOf(value));
                break;
            case 2:
                vals.put("u8Contrast", Integer.valueOf(value));
                break;
            case 3:
                vals.put("u8Saturation", Integer.valueOf(value));
                break;
            case 4:
                vals.put("u8Sharpness", Integer.valueOf(value));
                break;
            case 5:
                vals.put("u8Hue", Integer.valueOf(value));
                break;
            case 6:
                vals.put("u8Backlight", Integer.valueOf(value));
                break;
        }
        long ret = -1;
        try {
            ret = (long) this.cr.update(Uri.parse(this.userSettingSchema + "/picmode_setting/inputsrc/" + inputSrcType + "/picmode/" + pictureModeType), vals, null, null);
        } catch (SQLException e) {
            Log.e("DataBaseDeskImpl", "update failed");
        }
        if (ret == -1) {
        }
    }

    public int queryPicModeSetting(EN_MS_VIDEOITEM eIndex, int inputSrcType, int pictureModeType) {
        Cursor cursorPicMode = this.cr.query(Uri.parse(this.userSettingSchema + "/picmode_setting/inputsrc/" + inputSrcType + "/picmode/" + pictureModeType), null, null, null, "PictureModeType");
        cursorPicMode.moveToFirst();
        int value = 0;
        switch ($SWITCH_TABLE$com$konka$kkinterface$tv$DataBaseDesk$EN_MS_VIDEOITEM()[eIndex.ordinal()]) {
            case 1:
                value = cursorPicMode.getInt(cursorPicMode.getColumnIndex("u8Brightness"));
                break;
            case 2:
                value = cursorPicMode.getInt(cursorPicMode.getColumnIndex("u8Contrast"));
                break;
            case 3:
                value = cursorPicMode.getInt(cursorPicMode.getColumnIndex("u8Saturation"));
                break;
            case 4:
                value = cursorPicMode.getInt(cursorPicMode.getColumnIndex("u8Sharpness"));
                break;
            case 5:
                value = cursorPicMode.getInt(cursorPicMode.getColumnIndex("u8Hue"));
                break;
            case 6:
                value = cursorPicMode.getInt(cursorPicMode.getColumnIndex("u8Backlight"));
                break;
        }
        cursorPicMode.close();
        return value;
    }

    public void updateVideoBasePara(T_MS_VIDEO model, int inputSrcType) {
        ContentValues vals = new ContentValues();
        vals.put("ePicture", Integer.valueOf(model.ePicture.ordinal()));
        vals.put("enARCType", Integer.valueOf(model.enARCType.ordinal()));
        vals.put("fOutput_RES", Integer.valueOf(model.fOutput_RES.ordinal()));
        vals.put("tvsys", Integer.valueOf(model.tvsys.ordinal()));
        vals.put("LastVideoStandardMode", Integer.valueOf(model.LastVideoStandardMode.ordinal()));
        vals.put("LastAudioStandardMode", Integer.valueOf(model.LastAudioStandardMode.ordinal()));
        vals.put("eDynamic_Contrast", Integer.valueOf(model.eDynamic_Contrast.ordinal()));
        vals.put("eFilm", Integer.valueOf(model.eFilm.ordinal()));
        vals.put("eTvFormat", Integer.valueOf(model.eTvFormat.ordinal()));
        vals.put("skinTone", Integer.valueOf(model.skinTone.ordinal()));
        vals.put("detailEnhance", Integer.valueOf(model.detailEnhance ? 1 : 0));
        vals.put("DNR", Integer.valueOf(model.DNR.ordinal()));
        long ret = -1;
        try {
            ret = (long) this.cr.update(Uri.parse(this.userSettingSchema + "/videosetting/inputsrc/" + inputSrcType), vals, null, null);
        } catch (SQLException e) {
            Log.e("DataBaseDeskImpl", "update failed");
        }
        if (ret != -1) {
            try {
                if (TvManager.getInstance() != null) {
                    TvManager.getInstance().getDatabaseManager().setDatabaseDirtyByApplication(34);
                }
            } catch (TvCommonException e2) {
                e2.printStackTrace();
            }
        }
    }

    public int queryPicMode(int inputSrcType) {
        Cursor cursor = this.cr.query(Uri.parse(this.userSettingSchema + "/videosetting/inputsrc/" + inputSrcType), null, null, null, null);
        cursor.moveToNext();
        int result = cursor.getInt(cursor.getColumnIndex("ePicture"));
        cursor.close();
        return result;
    }

    public int querySoundMode() {
        Cursor cursor = this.cr.query(Uri.parse(this.userSettingSchema + "/soundsetting"), null, null, null, null);
        cursor.moveToNext();
        int result = cursor.getInt(cursor.getColumnIndex("SoundMode"));
        cursor.close();
        return result;
    }

    public int queryVideoArcMode(int value) {
        Cursor cursor = this.cr.query(Uri.parse(this.userSettingSchema + "/videosetting/inputsrc/" + value), null, null, null, null);
        cursor.moveToNext();
        int result = cursor.getInt(cursor.getColumnIndex("enARCType"));
        cursor.close();
        return result;
    }

    public ST_MAPI_BLOCK_SYS_SETTING queryBlockSysSetting() {
        Cursor cursor = this.cr.query(Uri.parse(this.userSettingSchema + "/blocksyssetting"), new String[]{"_id", "u8BlockSysLockMode", "u8UnratedLoack", "u8VideoBlockMode", "u8BlockSysPWSetStatus", "u8ParentalControl", "u8EnterLockPage", "u16BlockSysPassword"}, null, null, null);
        if (cursor.moveToNext()) {
            this.blockSysSetting.blockSysLockMode = cursor.getInt(1);
            this.blockSysSetting.unrateLock = cursor.getInt(2);
            this.blockSysSetting.videoBlockMode = cursor.getInt(3);
            this.blockSysSetting.blockSysPWSetStatus = cursor.getInt(4);
            this.blockSysSetting.parentalControl = cursor.getInt(5);
            this.blockSysSetting.enterLockPage = cursor.getInt(6);
            this.blockSysSetting.blockSysPassword = cursor.getInt(7);
        }
        cursor.close();
        return this.blockSysSetting;
    }

    public int queryAutoTimeMode() {
        Cursor cursor = this.cr.query(Uri.parse(this.userSettingSchema + "/timesetting"), null, null, null, null);
        int result = -1;
        if (cursor.moveToNext()) {
            result = cursor.getInt(cursor.getColumnIndex("bClockMode"));
        }
        cursor.close();
        return result;
    }

    public void setAutoTimeMode(int mode) {
        ContentValues vals = new ContentValues();
        vals.put("bClockMode", Integer.valueOf(mode));
        this.cr.update(Uri.parse(this.userSettingSchema + "/timesetting"), vals, null, null);
        Log.e("DataBaseDeskImpl", "update bClockMode:" + mode);
        try {
            if (TvManager.getInstance() != null) {
                TvManager.getInstance().getDatabaseManager().setDatabaseDirtyByApplication(27);
            }
        } catch (TvCommonException e) {
            e.printStackTrace();
        }
    }

    public void updateVideoAstPicture(T_MS_PICTURE model, int inputSrcType, int pictureModeType) {
        ContentValues vals = new ContentValues();
        vals.put("u8Backlight", Short.valueOf(model.backlight));
        vals.put("u8Contrast", Short.valueOf(model.contrast));
        vals.put("u8Brightness", Short.valueOf(model.brightness));
        vals.put("u8Saturation", Short.valueOf(model.saturation));
        vals.put("u8Sharpness", Short.valueOf(model.sharpness));
        vals.put("u8Hue", Short.valueOf(model.hue));
        vals.put("eColorTemp", Integer.valueOf(model.eColorTemp.ordinal()));
        vals.put("eVibrantColour", Integer.valueOf(model.eVibrantColour.ordinal()));
        vals.put("ePerfectClear", Integer.valueOf(model.ePerfectClear.ordinal()));
        vals.put("eDynamicContrast", Integer.valueOf(model.eDynamicContrast.ordinal()));
        vals.put("eDynamicBacklight", Integer.valueOf(model.eDynamicBacklight.ordinal()));
        long ret = -1;
        try {
            ret = (long) this.cr.update(Uri.parse(this.userSettingSchema + "/picmode_setting/inputsrc/" + inputSrcType + "/picmode/" + pictureModeType), vals, null, null);
        } catch (SQLException e) {
            Log.e("DataBaseDeskImpl", "update failed");
        }
        if (ret != -1) {
            try {
                if (TvManager.getInstance() != null) {
                    TvManager.getInstance().getDatabaseManager().setDatabaseDirtyByApplication(20);
                }
            } catch (TvCommonException e2) {
                e2.printStackTrace();
            }
        }
    }

    public void updateVideoNRMode(T_MS_NR_MODE model, int inputSrcType, int NRModeIdx) {
        ContentValues vals = new ContentValues();
        vals.put("eNR", Integer.valueOf(model.eNR.ordinal()));
        vals.put("eMPEG_NR", Integer.valueOf(model.eMPEG_NR.ordinal()));
        long ret = -1;
        try {
            ret = (long) this.cr.update(Uri.parse(this.userSettingSchema + "/nrmode/nrmode/" + NRModeIdx + "/inputsrc/" + inputSrcType), vals, null, null);
        } catch (SQLException e) {
            Log.e("DataBaseDeskImpl", "update failed");
        }
        if (ret != -1) {
            try {
                if (TvManager.getInstance() != null) {
                    TvManager.getInstance().getDatabaseManager().setDatabaseDirtyByApplication(14);
                }
            } catch (TvCommonException e2) {
                e2.printStackTrace();
            }
        }
    }

    public void updateVideoAstSubColor(T_MS_SUB_COLOR model, int inputSrcType) {
        ContentValues vals = new ContentValues();
        vals.put("u8SubBrightness", Short.valueOf(model.SubBrightness));
        vals.put("u8SubContrast", Short.valueOf(model.SubContrast));
        long ret = -1;
        try {
            ret = (long) this.cr.update(Uri.parse(this.userSettingSchema + "/videosetting/inputsrc/" + inputSrcType), vals, null, null);
        } catch (SQLException e) {
            Log.e("DataBaseDeskImpl", "update failed");
        }
        if (ret != -1) {
            try {
                if (TvManager.getInstance() != null) {
                    TvManager.getInstance().getDatabaseManager().setDatabaseDirtyByApplication(34);
                }
            } catch (TvCommonException e2) {
                e2.printStackTrace();
            }
        }
    }

    public void updateVideo3DMode(ThreeD_Video_MODE model, int inputSrcType) {
        ContentValues vals = new ContentValues();
        vals.put("eThreeDVideo", Integer.valueOf(model.eThreeDVideo.ordinal()));
        vals.put("eThreeDVideoSelfAdaptiveDetect", Integer.valueOf(model.eThreeDVideoSelfAdaptiveDetect.ordinal()));
        vals.put("eThreeDVideoDisplayFormat", Integer.valueOf(model.eThreeDVideoDisplayFormat.ordinal()));
        vals.put("eThreeDVideo3DTo2D", Integer.valueOf(model.eThreeDVideo3DTo2D.ordinal()));
        vals.put("eThreeDVideo3DDepth", Integer.valueOf(model.eThreeDVideo3DDepth.ordinal()));
        vals.put("eThreeDVideo3DOffset", Integer.valueOf(model.eThreeDVideo3DOffset.ordinal()));
        vals.put("eThreeDVideoAutoStart", Integer.valueOf(model.eThreeDVideoAutoStart.ordinal()));
        vals.put("eThreeDVideo3DOutputAspect", Integer.valueOf(model.eThreeDVideo3DOutputAspect.ordinal()));
        vals.put("eThreeDVideoLRViewSwitch", Integer.valueOf(model.eThreeDVideoLRViewSwitch.ordinal()));
        long ret = -1;
        try {
            ret = (long) this.cr.update(Uri.parse(this.userSettingSchema + "/threedvideomode/inputsrc/" + inputSrcType), vals, null, null);
        } catch (SQLException e) {
            Log.e("DataBaseDeskImpl", "update failed");
        }
        if (ret != -1) {
            try {
                if (TvManager.getInstance() != null) {
                    TvManager.getInstance().getDatabaseManager().setDatabaseDirtyByApplication(26);
                }
            } catch (TvCommonException e2) {
                e2.printStackTrace();
            }
        }
    }

    public void updateVideo3DAdaptiveDetectMode(EN_ThreeD_Video_SELFADAPTIVE_DETECT model, int inputSrcType) {
        ContentValues vals = new ContentValues();
        vals.put("eThreeDVideoSelfAdaptiveDetect", Integer.valueOf(model.ordinal()));
        long ret = -1;
        try {
            ret = (long) this.cr.update(Uri.parse(this.userSettingSchema + "/threedvideomode/inputsrc/" + inputSrcType), vals, null, null);
        } catch (SQLException e) {
            Log.e("DataBaseDeskImpl", "update failed");
        }
        if (ret != -1) {
            try {
                if (TvManager.getInstance() != null) {
                    TvManager.getInstance().getDatabaseManager().setDatabaseDirtyByApplication(26);
                }
            } catch (TvCommonException e2) {
                e2.printStackTrace();
            }
        }
    }

    public EN_ThreeD_Video_SELFADAPTIVE_DETECT queryVideo3DSelfAdaptiveDetectMode(int inputSrcType) {
        EN_ThreeD_Video_SELFADAPTIVE_DETECT mode = EN_ThreeD_Video_SELFADAPTIVE_DETECT.DB_ThreeD_Video_SELF_ADAPTIVE_DETECT_OFF;
        Cursor cursor = this.cr.query(Uri.parse(this.userSettingSchema + "/threedvideomode/inputsrc/" + inputSrcType), null, null, null, null);
        if (cursor.moveToNext()) {
            int index = cursor.getColumnIndex("eThreeDVideoSelfAdaptiveDetect");
            Log.d("DEBUG", "===========================index============================" + index);
            mode = EN_ThreeD_Video_SELFADAPTIVE_DETECT.values()[cursor.getInt(index)];
        }
        cursor.close();
        return mode;
    }

    public void updateVideo3DDisplayFormat(EN_ThreeD_Video_DISPLAYFORMAT model, int inputSrcType) {
        ContentValues vals = new ContentValues();
        vals.put("eThreeDVideoDisplayFormat", Integer.valueOf(model.ordinal()));
        long ret = -1;
        try {
            ret = (long) this.cr.update(Uri.parse(this.userSettingSchema + "/threedvideomode/inputsrc/" + inputSrcType), vals, null, null);
        } catch (SQLException e) {
            Log.e("DataBaseDeskImpl", "update failed");
        }
        if (ret != -1) {
            try {
                if (TvManager.getInstance() != null) {
                    TvManager.getInstance().getDatabaseManager().setDatabaseDirtyByApplication(26);
                }
            } catch (TvCommonException e2) {
                e2.printStackTrace();
            }
        }
    }

    public void updateVideoUserOverScanMode(T_MS_OVERSCAN_SETTING_USER model, int inputSrcType) {
        ContentValues vals = new ContentValues();
        vals.put("OverScanHposition", Short.valueOf(model.OverScanHposition));
        vals.put("OverScanVposition", Short.valueOf(model.OverScanVposition));
        vals.put("OverScanHRatio", Short.valueOf(model.OverScanHRatio));
        vals.put("OverScanVRatio", Short.valueOf(model.OverScanVRatio));
        long ret = -1;
        try {
            ret = (long) this.cr.update(Uri.parse(this.userSettingSchema + "/useroverscanmode/inputsrc/" + inputSrcType), vals, null, null);
        } catch (SQLException e) {
            Log.e("DataBaseDeskImpl", "update failed");
        }
        if (ret != -1) {
            try {
                if (TvManager.getInstance() != null) {
                    TvManager.getInstance().getDatabaseManager().setDatabaseDirtyByApplication(32);
                }
            } catch (TvCommonException e2) {
                e2.printStackTrace();
            }
        }
    }

    public T_MS_COLOR_TEMP_DATA queryUsrColorTmpData() {
        Cursor cursor = this.cr.query(Uri.parse(this.userSettingSchema + "/usercolortemp"), null, null, null, null);
        int i = 0;
        while (cursor.moveToNext() && i <= EnumInputSource.E_INPUT_SOURCE_NUM.ordinal() - 1) {
            this.stUsrColorTemp.redgain = (short) cursor.getInt(cursor.getColumnIndex("u8RedGain"));
            this.stUsrColorTemp.greengain = (short) cursor.getInt(cursor.getColumnIndex("u8GreenGain"));
            this.stUsrColorTemp.bluegain = (short) cursor.getInt(cursor.getColumnIndex("u8BlueGain"));
            this.stUsrColorTemp.redoffset = (short) cursor.getInt(cursor.getColumnIndex("u8RedOffset"));
            this.stUsrColorTemp.greenoffset = (short) cursor.getInt(cursor.getColumnIndex("u8GreenOffset"));
            this.stUsrColorTemp.blueoffset = (short) cursor.getInt(cursor.getColumnIndex("u8BlueOffset"));
            i++;
        }
        cursor.close();
        return this.stUsrColorTemp;
    }

    public void updateUsrColorTmpData(T_MS_COLOR_TEMP_DATA model) {
        ContentValues vals = new ContentValues();
        vals.put("u8RedGain", Short.valueOf(model.redgain));
        vals.put("u8GreenGain", Short.valueOf(model.greengain));
        vals.put("u8BlueGain", Short.valueOf(model.bluegain));
        vals.put("u8RedOffset", Short.valueOf(model.redoffset));
        vals.put("u8GreenOffset", Short.valueOf(model.greenoffset));
        vals.put("u8BlueOffset", Short.valueOf(model.blueoffset));
        long ret = -1;
        try {
            ret = (long) this.cr.update(Uri.parse(this.userSettingSchema + "/usercolortemp"), vals, null, null);
        } catch (SQLException e) {
            Log.e("DataBaseDeskImpl", "update failed");
        }
        if (ret != -1) {
            try {
                if (TvManager.getInstance() != null) {
                    TvManager.getInstance().getDatabaseManager().setDatabaseDirtyByApplication(28);
                }
            } catch (TvCommonException e2) {
                e2.printStackTrace();
            }
        }
    }

    public T_MS_COLOR_TEMPEX_DATA[] queryUsrColorTmpExData() {
        Cursor cursor = this.cr.query(Uri.parse(this.userSettingSchema + "/usercolortempex"), null, null, null, null);
        int i = 0;
        while (cursor.moveToNext() && i <= EnumInputSource.E_INPUT_SOURCE_NUM.ordinal() - 1) {
            this.stUsrColorTempEx[i].redgain = cursor.getInt(cursor.getColumnIndex("u16RedGain"));
            this.stUsrColorTempEx[i].greengain = cursor.getInt(cursor.getColumnIndex("u16GreenGain"));
            this.stUsrColorTempEx[i].bluegain = cursor.getInt(cursor.getColumnIndex("u16BlueGain"));
            this.stUsrColorTempEx[i].redoffset = cursor.getInt(cursor.getColumnIndex("u16RedOffset"));
            this.stUsrColorTempEx[i].greenoffset = cursor.getInt(cursor.getColumnIndex("u16GreenOffset"));
            this.stUsrColorTempEx[i].blueoffset = cursor.getInt(cursor.getColumnIndex("u16BlueOffset"));
            i++;
        }
        cursor.close();
        return this.stUsrColorTempEx;
    }

    public void updateUsrColorTmpExData(T_MS_COLOR_TEMPEX_DATA model, int colorTmpIdx) {
        ContentValues vals = new ContentValues();
        vals.put("u16RedGain", Integer.valueOf(model.redgain));
        vals.put("u16GreenGain", Integer.valueOf(model.greengain));
        vals.put("u16BlueGain", Integer.valueOf(model.bluegain));
        vals.put("u16RedOffset", Integer.valueOf(model.redoffset));
        vals.put("u16GreenOffset", Integer.valueOf(model.greenoffset));
        vals.put("u16BlueOffset", Integer.valueOf(model.blueoffset));
        long ret = -1;
        try {
            ret = (long) this.cr.update(Uri.parse(this.userSettingSchema + "/usercolortempex/" + colorTmpIdx), vals, null, null);
        } catch (SQLException e) {
            Log.e("DataBaseDeskImpl", "update failed");
        }
        if (ret != -1) {
            try {
                if (TvManager.getInstance() != null) {
                    TvManager.getInstance().getDatabaseManager().setDatabaseDirtyByApplication(29);
                }
            } catch (TvCommonException e2) {
                e2.printStackTrace();
            }
        }
    }

    public MS_USER_SYSTEM_SETTING queryUserSysSetting() {
        boolean z;
        boolean z2;
        boolean z3;
        boolean z4;
        boolean z5;
        boolean z6;
        boolean z7;
        boolean z8 = false;
        Cursor cursor = this.cr.query(Uri.parse(this.userSettingSchema + "/systemsetting"), null, null, null, null);
        String str = "CN";
        if (cursor.moveToFirst()) {
            MS_USER_SYSTEM_SETTING ms_user_system_setting = this.stUsrData;
            if (cursor.getInt(cursor.getColumnIndex("fRunInstallationGuide")) == 0) {
                z = false;
            } else {
                z = true;
            }
            ms_user_system_setting.fRunInstallationGuide = z;
            MS_USER_SYSTEM_SETTING ms_user_system_setting2 = this.stUsrData;
            if (cursor.getInt(cursor.getColumnIndex("fNoChannel")) == 0) {
                z2 = false;
            } else {
                z2 = true;
            }
            ms_user_system_setting2.fNoChannel = z2;
            MS_USER_SYSTEM_SETTING ms_user_system_setting3 = this.stUsrData;
            if (cursor.getInt(cursor.getColumnIndex("bDisableSiAutoUpdate")) == 0) {
                z3 = false;
            } else {
                z3 = true;
            }
            ms_user_system_setting3.bDisableSiAutoUpdate = z3;
            this.stUsrData.enInputSourceType = EnumInputSource.values()[cursor.getInt(cursor.getColumnIndex("enInputSourceType"))];
            this.stUsrData.Country = MEMBER_COUNTRY.values()[cursor.getInt(cursor.getColumnIndex("Country"))];
            this.stUsrData.enCableOperators = EN_CABLE_OPERATORS.values()[cursor.getInt(cursor.getColumnIndex("enCableOperators"))];
            this.stUsrData.enSatellitePlatform = EN_SATELLITE_PLATFORM.values()[cursor.getInt(cursor.getColumnIndex("enSatellitePlatform"))];
            EnumLanguage eLang = EnumLanguage.values()[cursor.getInt(cursor.getColumnIndex("Language"))];
            if (!readValue("/system/build.prop", "persist.sys.country").equals("CN")) {
                this.stUsrData.enLanguage = eLang;
            } else if (eLang == EnumLanguage.E_ENGLISH) {
                this.stUsrData.enLanguage = EnumLanguage.E_ENGLISH;
            } else if (eLang == EnumLanguage.E_CHINESE) {
                this.stUsrData.enLanguage = EnumLanguage.E_CHINESE;
            } else if (eLang == EnumLanguage.E_ACHINESE) {
                this.stUsrData.enLanguage = EnumLanguage.E_ACHINESE;
            } else {
                this.stUsrData.enLanguage = EnumLanguage.E_CHINESE;
                ContentValues vals = new ContentValues();
                vals.put("Language", Integer.valueOf(this.stUsrData.enLanguage.ordinal()));
                try {
                    long ret = (long) this.cr.update(Uri.parse(this.userSettingSchema + "/systemsetting"), vals, null, null);
                } catch (SQLException e) {
                    Log.e("DataBaseDeskImpl", "update failed");
                }
            }
            this.stUsrData.enSPDIFMODE = SPDIF_TYPE.values()[cursor.getInt(cursor.getColumnIndex("enSPDIFMODE"))];
            this.stUsrData.fSoftwareUpdate = (short) cursor.getInt(cursor.getColumnIndex("fSoftwareUpdate"));
            this.stUsrData.u8OADTime = (short) cursor.getInt(cursor.getColumnIndex("U8OADTime"));
            this.stUsrData.fOADScanAfterWakeup = (short) cursor.getInt(cursor.getColumnIndex("fOADScanAfterWakeup"));
            this.stUsrData.fAutoVolume = (short) cursor.getInt(cursor.getColumnIndex("fAutoVolume"));
            this.stUsrData.fDcPowerOFFMode = (short) cursor.getInt(cursor.getColumnIndex("fDcPowerOFFMode"));
            this.stUsrData.DtvRoute = (short) cursor.getInt(cursor.getColumnIndex("DtvRoute"));
            this.stUsrData.ScartOutRGB = (short) cursor.getInt(cursor.getColumnIndex("ScartOutRGB"));
            this.stUsrData.U8Transparency = (short) cursor.getInt(cursor.getColumnIndex("U8Transparency"));
            this.stUsrData.u32MenuTimeOut = cursor.getLong(cursor.getColumnIndex("u32MenuTimeOut"));
            this.stUsrData.AudioOnly = (short) cursor.getInt(cursor.getColumnIndex("AudioOnly"));
            this.stUsrData.bEnableWDT = (short) cursor.getInt(cursor.getColumnIndex("bEnableWDT"));
            this.stUsrData.u8FavoriteRegion = (short) cursor.getInt(cursor.getColumnIndex("u8FavoriteRegion"));
            this.stUsrData.u8Bandwidth = (short) cursor.getInt(cursor.getColumnIndex("u8Bandwidth"));
            this.stUsrData.u8TimeShiftSizeType = (short) cursor.getInt(cursor.getColumnIndex("u8TimeShiftSizeType"));
            this.stUsrData.fOadScan = (short) cursor.getInt(cursor.getColumnIndex("fOadScan"));
            this.stUsrData.bEnablePVRRecordAll = (short) cursor.getInt(cursor.getColumnIndex("bEnablePVRRecordAll"));
            this.stUsrData.u8ColorRangeMode = (short) cursor.getInt(cursor.getColumnIndex("u8ColorRangeMode"));
            this.stUsrData.u8HDMIAudioSource = (short) cursor.getInt(cursor.getColumnIndex("u8HDMIAudioSource"));
            this.stUsrData.bEnableAlwaysTimeshift = (short) cursor.getInt(cursor.getColumnIndex("bEnableAlwaysTimeshift"));
            this.stUsrData.eSUPER = EN_MS_SUPER.values()[cursor.getInt(cursor.getColumnIndex("eSUPER"))];
            this.stUsrData.bUartBus = cursor.getInt(cursor.getColumnIndex("bUartBus")) != 0;
            this.stUsrData.m_AutoZoom = (short) cursor.getInt(cursor.getColumnIndex("m_AutoZoom"));
            MS_USER_SYSTEM_SETTING ms_user_system_setting4 = this.stUsrData;
            if (cursor.getInt(cursor.getColumnIndex("bOverScan")) == 0) {
                z4 = false;
            } else {
                z4 = true;
            }
            ms_user_system_setting4.bOverScan = z4;
            this.stUsrData.m_u8BrazilVideoStandardType = (short) cursor.getInt(cursor.getColumnIndex("m_u8BrazilVideoStandardType"));
            this.stUsrData.m_u8SoftwareUpdateMode = (short) cursor.getInt(cursor.getColumnIndex("m_u8SoftwareUpdateMode"));
            this.stUsrData.u32OSD_Active_Time = cursor.getLong(cursor.getColumnIndex("OSD_Active_Time"));
            MS_USER_SYSTEM_SETTING ms_user_system_setting5 = this.stUsrData;
            if (cursor.getInt(cursor.getColumnIndex("m_MessageBoxExist")) == 0) {
                z5 = false;
            } else {
                z5 = true;
            }
            ms_user_system_setting5.m_MessageBoxExist = z5;
            this.stUsrData.u16LastOADVersion = cursor.getInt(cursor.getColumnIndex("u16LastOADVersion"));
            MS_USER_SYSTEM_SETTING ms_user_system_setting6 = this.stUsrData;
            if (cursor.getInt(cursor.getColumnIndex("bEnableAutoChannelUpdate")) == 0) {
                z6 = false;
            } else {
                z6 = true;
            }
            ms_user_system_setting6.bEnableAutoChannelUpdate = z6;
            this.stUsrData.standbyNoOperation = cursor.getInt(cursor.getColumnIndex("standbyNoOperation"));
            MS_USER_SYSTEM_SETTING ms_user_system_setting7 = this.stUsrData;
            if (cursor.getInt(cursor.getColumnIndex("standbyNoSignal")) == 0) {
                z7 = false;
            } else {
                z7 = true;
            }
            ms_user_system_setting7.standbyNoSignal = z7;
            MS_USER_SYSTEM_SETTING ms_user_system_setting8 = this.stUsrData;
            if (cursor.getInt(cursor.getColumnIndex("screenSaveMode")) != 0) {
                z8 = true;
            }
            ms_user_system_setting8.screenSaveMode = z8;
            this.stUsrData.U8SystemAutoTimeType = cursor.getInt(cursor.getColumnIndex("U8SystemAutoTimeType"));
            this.stUsrData.smartEnergySaving = SmartEnergySavingMode.values()[cursor.getInt(cursor.getColumnIndex("smartEnergySaving"))];
            this.stUsrData.colorWheelMode = ColorWheelMode.values()[cursor.getInt(cursor.getColumnIndex("colorWheelMode"))];
        }
        cursor.close();
        return this.stUsrData;
    }

    private String readValue(String filePath, String key) {
        Properties props = new Properties();
        try {
            props.load(new BufferedInputStream(new FileInputStream(filePath)));
            return props.getProperty(key);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void updateUserSysSetting(MS_USER_SYSTEM_SETTING model) {
        int i;
        int i2;
        int i3;
        EnumLanguage eLang;
        int i4;
        int i5;
        int i6;
        int i7;
        int i8 = 1;
        String str = "CN";
        String country = readValue("/system/build.prop", "persist.sys.country");
        ContentValues vals = new ContentValues();
        String str2 = "fRunInstallationGuide";
        if (model.fRunInstallationGuide) {
            i = 1;
        } else {
            i = 0;
        }
        vals.put(str2, Integer.valueOf(i));
        String str3 = "fNoChannel";
        if (model.fNoChannel) {
            i2 = 1;
        } else {
            i2 = 0;
        }
        vals.put(str3, Integer.valueOf(i2));
        String str4 = "bDisableSiAutoUpdate";
        if (model.bDisableSiAutoUpdate) {
            i3 = 1;
        } else {
            i3 = 0;
        }
        vals.put(str4, Integer.valueOf(i3));
        vals.put("enInputSourceType", Integer.valueOf(model.enInputSourceType.ordinal()));
        vals.put("Country", Integer.valueOf(model.Country.ordinal()));
        vals.put("enCableOperators", Integer.valueOf(model.enCableOperators.ordinal()));
        vals.put("enSatellitePlatform", Integer.valueOf(model.enSatellitePlatform.ordinal()));
        if (!country.equals("CN")) {
            eLang = model.enLanguage;
        } else if (model.enLanguage == EnumLanguage.E_ENGLISH) {
            eLang = EnumLanguage.E_ENGLISH;
        } else if (model.enLanguage == EnumLanguage.E_ACHINESE) {
            eLang = EnumLanguage.E_ACHINESE;
        } else {
            eLang = EnumLanguage.E_CHINESE;
        }
        vals.put("Language", Integer.valueOf(eLang.ordinal()));
        vals.put("enSPDIFMODE", Integer.valueOf(model.enSPDIFMODE.ordinal()));
        vals.put("fSoftwareUpdate", Short.valueOf(model.fSoftwareUpdate));
        vals.put("U8OADTime", Short.valueOf(model.u8OADTime));
        vals.put("fOADScanAfterWakeup", Short.valueOf(model.fOADScanAfterWakeup));
        vals.put("fAutoVolume", Short.valueOf(model.fAutoVolume));
        vals.put("fDcPowerOFFMode", Short.valueOf(model.fDcPowerOFFMode));
        vals.put("DtvRoute", Short.valueOf(model.DtvRoute));
        vals.put("ScartOutRGB", Short.valueOf(model.ScartOutRGB));
        vals.put("U8Transparency", Short.valueOf(model.U8Transparency));
        vals.put("u32MenuTimeOut", Long.valueOf(model.u32MenuTimeOut));
        vals.put("AudioOnly", Short.valueOf(model.AudioOnly));
        vals.put("bEnableWDT", Short.valueOf(model.bEnableWDT));
        vals.put("u8FavoriteRegion", Short.valueOf(model.u8FavoriteRegion));
        vals.put("u8Bandwidth", Short.valueOf(model.u8Bandwidth));
        vals.put("u8TimeShiftSizeType", Short.valueOf(model.u8TimeShiftSizeType));
        vals.put("fOadScan", Short.valueOf(model.fOadScan));
        vals.put("bEnablePVRRecordAll", Short.valueOf(model.bEnablePVRRecordAll));
        vals.put("u8ColorRangeMode", Short.valueOf(model.u8ColorRangeMode));
        vals.put("u8HDMIAudioSource", Short.valueOf(model.u8HDMIAudioSource));
        vals.put("bEnableAlwaysTimeshift", Short.valueOf(model.bEnableAlwaysTimeshift));
        vals.put("eSUPER", Integer.valueOf(model.eSUPER.ordinal()));
        vals.put("bUartBus", Integer.valueOf(model.bUartBus ? 1 : 0));
        vals.put("m_AutoZoom", Short.valueOf(model.m_AutoZoom));
        String str5 = "bOverScan";
        if (model.bOverScan) {
            i4 = 1;
        } else {
            i4 = 0;
        }
        vals.put(str5, Integer.valueOf(i4));
        vals.put("m_u8BrazilVideoStandardType", Short.valueOf(model.m_u8BrazilVideoStandardType));
        vals.put("m_u8SoftwareUpdateMode", Short.valueOf(model.m_u8SoftwareUpdateMode));
        vals.put("OSD_Active_Time", Long.valueOf(model.u32OSD_Active_Time));
        String str6 = "m_MessageBoxExist";
        if (model.m_MessageBoxExist) {
            i5 = 1;
        } else {
            i5 = 0;
        }
        vals.put(str6, Integer.valueOf(i5));
        vals.put("u16LastOADVersion", Integer.valueOf(model.u16LastOADVersion));
        String str7 = "bEnableAutoChannelUpdate";
        if (model.bEnableAutoChannelUpdate) {
            i6 = 1;
        } else {
            i6 = 0;
        }
        vals.put(str7, Integer.valueOf(i6));
        vals.put("standbyNoOperation", Integer.valueOf(model.standbyNoOperation));
        String str8 = "standbyNoSignal";
        if (model.standbyNoSignal) {
            i7 = 1;
        } else {
            i7 = 0;
        }
        vals.put(str8, Integer.valueOf(i7));
        String str9 = "screenSaveMode";
        if (!model.screenSaveMode) {
            i8 = 0;
        }
        vals.put(str9, Integer.valueOf(i8));
        vals.put("U8SystemAutoTimeType", Integer.valueOf(model.U8SystemAutoTimeType));
        vals.put("smartEnergySaving", Integer.valueOf(model.smartEnergySaving.ordinal()));
        vals.put("colorWheelMode", Integer.valueOf(model.colorWheelMode.ordinal()));
        long ret = -1;
        try {
            ret = (long) this.cr.update(Uri.parse(this.userSettingSchema + "/systemsetting"), vals, null, null);
        } catch (SQLException e) {
            Log.e("DataBaseDeskImpl", "update failed");
        }
        if (ret != -1) {
            try {
                if (TvManager.getInstance() != null) {
                    TvManager.getInstance().getDatabaseManager().setDatabaseDirtyByApplication(25);
                }
            } catch (TvCommonException e2) {
                e2.printStackTrace();
            }
        }
    }

    public MS_USER_SUBTITLE_SETTING queryUserSubtitleSetting() {
        boolean z;
        boolean z2 = false;
        Cursor cursor = this.cr.query(Uri.parse(this.userSettingSchema + "/subtitlesetting"), null, null, null, null);
        if (cursor.moveToFirst()) {
            EnumLanguage eLang1 = EnumLanguage.values()[cursor.getInt(cursor.getColumnIndex("SubtitleDefaultLanguage"))];
            EnumLanguage eLang2 = EnumLanguage.values()[cursor.getInt(cursor.getColumnIndex("SubtitleDefaultLanguage_2"))];
            if (eLang1 == EnumLanguage.E_ENGLISH) {
                this.stSubtitleSet.SubtitleDefaultLanguage = EnumLanguage.E_ENGLISH;
            } else if (eLang1 == EnumLanguage.E_CHINESE) {
                this.stSubtitleSet.SubtitleDefaultLanguage = EnumLanguage.E_CHINESE;
            } else if (eLang1 == EnumLanguage.E_ACHINESE) {
                this.stSubtitleSet.SubtitleDefaultLanguage = EnumLanguage.E_ACHINESE;
            } else {
                this.stSubtitleSet.SubtitleDefaultLanguage = EnumLanguage.E_CHINESE;
                ContentValues vals = new ContentValues();
                vals.put("SubtitleDefaultLanguage", Integer.valueOf(this.stUsrData.enLanguage.ordinal()));
                try {
                    long ret = (long) this.cr.update(Uri.parse(this.userSettingSchema + "/subtitlesetting"), vals, null, null);
                } catch (SQLException e) {
                    Log.e("DataBaseDeskImpl", "update failed");
                }
            }
            if (eLang2 == EnumLanguage.E_ENGLISH) {
                this.stSubtitleSet.SubtitleDefaultLanguage_2 = EnumLanguage.E_ENGLISH;
            } else if (eLang2 == EnumLanguage.E_CHINESE) {
                this.stSubtitleSet.SubtitleDefaultLanguage_2 = EnumLanguage.E_CHINESE;
            } else if (eLang2 == EnumLanguage.E_ACHINESE) {
                this.stSubtitleSet.SubtitleDefaultLanguage_2 = EnumLanguage.E_ACHINESE;
            } else {
                this.stSubtitleSet.SubtitleDefaultLanguage_2 = EnumLanguage.E_CHINESE;
                ContentValues vals2 = new ContentValues();
                vals2.put("SubtitleDefaultLanguage_2", Integer.valueOf(this.stUsrData.enLanguage.ordinal()));
                try {
                    long ret2 = (long) this.cr.update(Uri.parse(this.userSettingSchema + "/subtitlesetting"), vals2, null, null);
                } catch (SQLException e2) {
                    Log.e("DataBaseDeskImpl", "update failed");
                }
            }
            MS_USER_SUBTITLE_SETTING ms_user_subtitle_setting = this.stSubtitleSet;
            if (cursor.getInt(cursor.getColumnIndex("fHardOfHearing")) == 0) {
                z = false;
            } else {
                z = true;
            }
            ms_user_subtitle_setting.fHardOfHearing = z;
            MS_USER_SUBTITLE_SETTING ms_user_subtitle_setting2 = this.stSubtitleSet;
            if (cursor.getInt(cursor.getColumnIndex("fEnableSubTitle")) != 0) {
                z2 = true;
            }
            ms_user_subtitle_setting2.fEnableSubTitle = z2;
        }
        cursor.close();
        return this.stSubtitleSet;
    }

    public void updateUserSubtitleSetting(MS_USER_SUBTITLE_SETTING model) {
        EnumLanguage eLang1;
        EnumLanguage eLang2;
        int i;
        int i2 = 1;
        ContentValues vals = new ContentValues();
        if (model.SubtitleDefaultLanguage == EnumLanguage.E_ENGLISH) {
            eLang1 = EnumLanguage.E_ENGLISH;
        } else if (model.SubtitleDefaultLanguage == EnumLanguage.E_ACHINESE) {
            eLang1 = EnumLanguage.E_ACHINESE;
        } else {
            eLang1 = EnumLanguage.E_CHINESE;
        }
        if (model.SubtitleDefaultLanguage_2 == EnumLanguage.E_ENGLISH) {
            eLang2 = EnumLanguage.E_ENGLISH;
        } else if (model.SubtitleDefaultLanguage_2 == EnumLanguage.E_ACHINESE) {
            eLang2 = EnumLanguage.E_ACHINESE;
        } else {
            eLang2 = EnumLanguage.E_CHINESE;
        }
        vals.put("SubtitleDefaultLanguage", Integer.valueOf(eLang1.ordinal()));
        vals.put("SubtitleDefaultLanguage_2", Integer.valueOf(eLang2.ordinal()));
        String str = "fHardOfHearing";
        if (model.fHardOfHearing) {
            i = 1;
        } else {
            i = 0;
        }
        vals.put(str, Integer.valueOf(i));
        String str2 = "fEnableSubTitle";
        if (!model.fEnableSubTitle) {
            i2 = 0;
        }
        vals.put(str2, Integer.valueOf(i2));
        long ret = -1;
        try {
            ret = (long) this.cr.update(Uri.parse(this.userSettingSchema + "/subtitlesetting"), vals, null, null);
        } catch (SQLException e) {
            Log.e("DataBaseDeskImpl", "update failed");
        }
        if (ret != -1) {
            try {
                if (TvManager.getInstance() != null) {
                    TvManager.getInstance().getDatabaseManager().setDatabaseDirtyByApplication(24);
                }
            } catch (TvCommonException e2) {
                e2.printStackTrace();
            }
        }
    }

    public MS_USER_LOCATION_SETTING queryUserLocSetting() {
        Cursor cursor = this.cr.query(Uri.parse(this.userSettingSchema + "/userlocationsetting"), null, null, null, null);
        while (cursor.moveToNext()) {
            this.stUserLocationSetting.mLocationNo = (short) cursor.getInt(cursor.getColumnIndex("u16LocationNo"));
            this.stUserLocationSetting.mManualLongitude = (short) cursor.getInt(cursor.getColumnIndex("s16ManualLongitude"));
            this.stUserLocationSetting.mManualLatitude = (short) cursor.getInt(cursor.getColumnIndex("s16ManualLatitude"));
        }
        cursor.close();
        return this.stUserLocationSetting;
    }

    public void updateUserLocSetting(MS_USER_LOCATION_SETTING model) {
        ContentValues vals = new ContentValues();
        vals.put("u16LocationNo", Integer.valueOf(model.mLocationNo));
        vals.put("s16ManualLongitude", Integer.valueOf(model.mManualLongitude));
        vals.put("s16ManualLatitude", Integer.valueOf(model.mManualLatitude));
        long ret = -1;
        try {
            ret = (long) this.cr.update(Uri.parse(this.userSettingSchema + "/userlocationsetting"), vals, null, null);
        } catch (SQLException e) {
            Log.e("DataBaseDeskImpl", "update failed");
        }
        if (ret != -1) {
            try {
                if (TvManager.getInstance() != null) {
                    TvManager.getInstance().getDatabaseManager().setDatabaseDirtyByApplication(30);
                }
            } catch (TvCommonException e2) {
                e2.printStackTrace();
            }
        }
    }

    public MS_USER_SOUND_SETTING querySoundSetting() {
        boolean z = false;
        Cursor cursor = this.cr.query(Uri.parse(this.userSettingSchema + "/soundsetting"), null, null, null, null);
        if (cursor.moveToFirst()) {
            this.soundpara.SoundMode = EN_SOUND_MODE.values()[cursor.getInt(cursor.getColumnIndex("SoundMode"))];
            this.soundpara.AudysseyDynamicVolume = EN_AUDYSSEY_DYNAMIC_VOLUME_MODE.values()[cursor.getInt(cursor.getColumnIndex("AudysseyDynamicVolume"))];
            this.soundpara.AudysseyEQ = EN_AUDYSSEY_EQ_MODE.values()[cursor.getInt(cursor.getColumnIndex("AudysseyEQ"))];
            this.soundpara.SurroundSoundMode = EN_SURROUND_SYSTEM_TYPE.values()[cursor.getInt(cursor.getColumnIndex("SurroundSoundMode"))];
            this.soundpara.SurroundMode = EN_SURROUND_MODE.values()[cursor.getInt(cursor.getColumnIndex("Surround"))];
            this.soundpara.bEnableAVC = cursor.getInt(cursor.getColumnIndex("bEnableAVC")) != 0;
            this.soundpara.Volume = (short) cursor.getInt(cursor.getColumnIndex("Volume"));
            this.soundpara.HPVolume = (short) cursor.getInt(cursor.getColumnIndex("HPVolume"));
            this.soundpara.Balance = (short) cursor.getInt(cursor.getColumnIndex("Balance"));
            this.soundpara.Primary_Flag = (short) cursor.getInt(cursor.getColumnIndex("Primary_Flag"));
            EnumLanguage eLang1 = EnumLanguage.values()[cursor.getInt(cursor.getColumnIndex("enSoundAudioLan1"))];
            EnumLanguage eLang2 = EnumLanguage.values()[cursor.getInt(cursor.getColumnIndex("enSoundAudioLan2"))];
            if (eLang1 == EnumLanguage.E_ENGLISH) {
                this.soundpara.enSoundAudioLan1 = EnumLanguage.E_ENGLISH;
            } else if (eLang1 == EnumLanguage.E_CHINESE) {
                this.soundpara.enSoundAudioLan1 = EnumLanguage.E_CHINESE;
            } else if (eLang1 == EnumLanguage.E_ACHINESE) {
                this.soundpara.enSoundAudioLan1 = EnumLanguage.E_ACHINESE;
            } else {
                this.soundpara.enSoundAudioLan1 = EnumLanguage.E_CHINESE;
                ContentValues vals = new ContentValues();
                vals.put("enSoundAudioLan1", Integer.valueOf(this.soundpara.enSoundAudioLan1.ordinal()));
                try {
                    long ret = (long) this.cr.update(Uri.parse(this.userSettingSchema + "/soundsetting"), vals, null, null);
                } catch (SQLException e) {
                    Log.e("DataBaseDeskImpl", "update failed");
                }
            }
            if (eLang2 == EnumLanguage.E_ENGLISH) {
                this.soundpara.enSoundAudioLan2 = EnumLanguage.E_ENGLISH;
            } else if (eLang2 == EnumLanguage.E_CHINESE) {
                this.soundpara.enSoundAudioLan2 = EnumLanguage.E_CHINESE;
            } else if (eLang2 == EnumLanguage.E_ACHINESE) {
                this.soundpara.enSoundAudioLan2 = EnumLanguage.E_ACHINESE;
            } else {
                this.soundpara.enSoundAudioLan2 = EnumLanguage.E_CHINESE;
                ContentValues vals2 = new ContentValues();
                vals2.put("enSoundAudioLan2", Integer.valueOf(this.soundpara.enSoundAudioLan2.ordinal()));
                try {
                    long ret2 = (long) this.cr.update(Uri.parse(this.userSettingSchema + "/soundsetting"), vals2, null, null);
                } catch (SQLException e2) {
                    Log.e("DataBaseDeskImpl", "update failed");
                }
            }
            this.soundpara.MUTE_Flag = (short) cursor.getInt(cursor.getColumnIndex("MUTE_Flag"));
            this.soundpara.enSoundAudioChannel = EN_AUD_MODE.values()[cursor.getInt(cursor.getColumnIndex("enSoundAudioChannel"))];
            MS_USER_SOUND_SETTING ms_user_sound_setting = this.soundpara;
            if (cursor.getInt(cursor.getColumnIndex("bEnableAD")) != 0) {
                z = true;
            }
            ms_user_sound_setting.bEnableAD = z;
            this.soundpara.ADVolume = (short) cursor.getInt(cursor.getColumnIndex("ADVolume"));
            this.soundpara.ADOutput = EN_SOUND_AD_OUTPUT.values()[cursor.getInt(cursor.getColumnIndex("ADOutput"))];
            this.soundpara.SPDIF_Delay = (short) cursor.getInt(cursor.getColumnIndex("SPDIF_Delay"));
            this.soundpara.Speaker_Delay = (short) cursor.getInt(cursor.getColumnIndex("Speaker_Delay"));
            this.soundpara.hdmi1AudioSource = HdmiAudioSource.values()[cursor.getInt(cursor.getColumnIndex("hdmi1AudioSource"))];
            this.soundpara.hdmi2AudioSource = HdmiAudioSource.values()[cursor.getInt(cursor.getColumnIndex("hdmi2AudioSource"))];
            this.soundpara.hdmi3AudioSource = HdmiAudioSource.values()[cursor.getInt(cursor.getColumnIndex("hdmi3AudioSource"))];
            this.soundpara.hdmi4AudioSource = HdmiAudioSource.values()[cursor.getInt(cursor.getColumnIndex("hdmi4AudioSource"))];
        }
        cursor.close();
        return this.soundpara;
    }

    public void updateSoundSetting(MS_USER_SOUND_SETTING model) {
        int i;
        EnumLanguage eLang1;
        EnumLanguage eLang2;
        int i2 = 1;
        ContentValues vals = new ContentValues();
        vals.put("SoundMode", Integer.valueOf(model.SoundMode.ordinal()));
        vals.put("AudysseyDynamicVolume", Integer.valueOf(model.AudysseyDynamicVolume.ordinal()));
        vals.put("AudysseyEQ", Integer.valueOf(model.AudysseyEQ.ordinal()));
        vals.put("SurroundSoundMode", Integer.valueOf(model.SurroundSoundMode.ordinal()));
        vals.put("Surround", Integer.valueOf(model.SurroundMode.ordinal()));
        String str = "bEnableAVC";
        if (model.bEnableAVC) {
            i = 1;
        } else {
            i = 0;
        }
        vals.put(str, Integer.valueOf(i));
        vals.put("Volume", Short.valueOf(model.Volume));
        vals.put("HPVolume", Short.valueOf(model.HPVolume));
        vals.put("Balance", Short.valueOf(model.Balance));
        vals.put("Primary_Flag", Short.valueOf(model.Primary_Flag));
        if (model.enSoundAudioLan1 == EnumLanguage.E_ENGLISH) {
            eLang1 = EnumLanguage.E_ENGLISH;
        } else if (model.enSoundAudioLan1 == EnumLanguage.E_ACHINESE) {
            eLang1 = EnumLanguage.E_ACHINESE;
        } else {
            eLang1 = EnumLanguage.E_CHINESE;
        }
        if (model.enSoundAudioLan2 == EnumLanguage.E_ENGLISH) {
            eLang2 = EnumLanguage.E_ENGLISH;
        } else if (model.enSoundAudioLan2 == EnumLanguage.E_ACHINESE) {
            eLang2 = EnumLanguage.E_ACHINESE;
        } else {
            eLang2 = EnumLanguage.E_CHINESE;
        }
        vals.put("enSoundAudioLan1", Integer.valueOf(eLang1.ordinal()));
        vals.put("enSoundAudioLan2", Integer.valueOf(eLang2.ordinal()));
        vals.put("MUTE_Flag", Short.valueOf(model.MUTE_Flag));
        vals.put("enSoundAudioChannel", Integer.valueOf(model.enSoundAudioChannel.ordinal()));
        String str2 = "bEnableAD";
        if (!model.bEnableAD) {
            i2 = 0;
        }
        vals.put(str2, Integer.valueOf(i2));
        vals.put("ADVolume", Short.valueOf(model.ADVolume));
        vals.put("ADOutput", Integer.valueOf(model.ADOutput.ordinal()));
        vals.put("SPDIF_Delay", Short.valueOf(model.SPDIF_Delay));
        vals.put("Speaker_Delay", Short.valueOf(model.Speaker_Delay));
        vals.put("hdmi1AudioSource", Integer.valueOf(model.hdmi1AudioSource.ordinal()));
        vals.put("hdmi2AudioSource", Integer.valueOf(model.hdmi2AudioSource.ordinal()));
        vals.put("hdmi3AudioSource", Integer.valueOf(model.hdmi3AudioSource.ordinal()));
        vals.put("hdmi4AudioSource", Integer.valueOf(model.hdmi4AudioSource.ordinal()));
        long ret = -1;
        try {
            ret = (long) this.cr.update(Uri.parse(this.userSettingSchema + "/soundsetting"), vals, null, null);
        } catch (SQLException e) {
            Log.e("DataBaseDeskImpl", "update failed");
        }
        if (ret != -1) {
            try {
                if (TvManager.getInstance() != null) {
                    TvManager.getInstance().getDatabaseManager().setDatabaseDirtyByApplication(23);
                }
            } catch (TvCommonException e2) {
                e2.printStackTrace();
            }
        }
    }

    public SoundModeSeting[] querySoundModeSettings() {
        Cursor cursor = this.cr.query(Uri.parse(this.userSettingSchema + "/soundmodesetting"), null, null, null, null);
        int i = 0;
        int length = this.astSoundModeSetting.length;
        while (cursor.moveToNext() && i <= length - 1) {
            this.astSoundModeSetting[i].Bass = (short) cursor.getInt(cursor.getColumnIndex("Bass"));
            this.astSoundModeSetting[i].Treble = (short) cursor.getInt(cursor.getColumnIndex("Treble"));
            this.astSoundModeSetting[i].EqBand1 = (short) cursor.getInt(cursor.getColumnIndex("EqBand1"));
            this.astSoundModeSetting[i].EqBand2 = (short) cursor.getInt(cursor.getColumnIndex("EqBand2"));
            this.astSoundModeSetting[i].EqBand3 = (short) cursor.getInt(cursor.getColumnIndex("EqBand3"));
            this.astSoundModeSetting[i].EqBand4 = (short) cursor.getInt(cursor.getColumnIndex("EqBand4"));
            this.astSoundModeSetting[i].EqBand5 = (short) cursor.getInt(cursor.getColumnIndex("EqBand5"));
            this.astSoundModeSetting[i].EqBand6 = (short) cursor.getInt(cursor.getColumnIndex("EqBand6"));
            this.astSoundModeSetting[i].EqBand7 = (short) cursor.getInt(cursor.getColumnIndex("EqBand7"));
            this.astSoundModeSetting[i].UserMode = cursor.getInt(cursor.getColumnIndex("UserMode")) != 0;
            this.astSoundModeSetting[i].Balance = (short) cursor.getInt(cursor.getColumnIndex("Balance"));
            this.astSoundModeSetting[i].enSoundAudioChannel = EN_AUD_MODE.values()[cursor.getInt(cursor.getColumnIndex("enSoundAudioChannel"))];
            i++;
        }
        cursor.close();
        return this.astSoundModeSetting;
    }

    public void updateSoundModeSetting(SoundModeSeting model, int soundModeType) {
        ContentValues vals = new ContentValues();
        vals.put("Bass", Short.valueOf(model.Bass));
        vals.put("Treble", Short.valueOf(model.Treble));
        vals.put("EqBand1", Short.valueOf(model.EqBand1));
        vals.put("EqBand2", Short.valueOf(model.EqBand2));
        vals.put("EqBand3", Short.valueOf(model.EqBand3));
        vals.put("EqBand4", Short.valueOf(model.EqBand4));
        vals.put("EqBand5", Short.valueOf(model.EqBand5));
        vals.put("EqBand6", Short.valueOf(model.EqBand6));
        vals.put("EqBand7", Short.valueOf(model.EqBand7));
        vals.put("UserMode", Integer.valueOf(model.UserMode ? 1 : 0));
        vals.put("Balance", Short.valueOf(model.Balance));
        vals.put("enSoundAudioChannel", Integer.valueOf(model.enSoundAudioChannel.ordinal()));
        long ret = -1;
        try {
            ret = (long) this.cr.update(Uri.parse(this.userSettingSchema + "/soundmodesetting/" + soundModeType), vals, null, null);
        } catch (SQLException e) {
            Log.e("DataBaseDeskImpl", "update failed");
        }
        if (ret != -1) {
            try {
                if (TvManager.getInstance() != null) {
                    TvManager.getInstance().getDatabaseManager().setDatabaseDirtyByApplication(22);
                }
            } catch (TvCommonException e2) {
                e2.printStackTrace();
            }
        }
    }

    public int queryPCHPos() {
        int id = 0;
        while (true) {
            if (id >= 10) {
                break;
            } else if (((short) queryPCModeIndex(id)) == this.f0com.getVideoInfo().s16ModeIndex) {
                Log.i("DataBaseDeskImpl", "~~~~~~~~Hpos id is " + id + "~~~~~~~~~~~");
                break;
            } else {
                id++;
            }
        }
        Cursor cursor = this.cr.query(Uri.parse("content://mstar.tv.usersetting/userpcmodesetting/" + String.valueOf(id)), null, null, null, null);
        int value = -1;
        if (cursor.moveToFirst()) {
            value = cursor.getInt(cursor.getColumnIndex("u16UI_HorizontalStart"));
        }
        cursor.close();
        return value;
    }

    public int queryPCVPos() {
        int id = 0;
        while (true) {
            if (id >= 10) {
                break;
            } else if (((short) queryPCModeIndex(id)) == this.f0com.getVideoInfo().s16ModeIndex) {
                Log.i("DataBaseDeskImpl", "~~~~~~~~Vpos id is " + id + "~~~~~~~~~~~");
                break;
            } else {
                id++;
            }
        }
        Cursor cursor = this.cr.query(Uri.parse("content://mstar.tv.usersetting/userpcmodesetting/" + String.valueOf(id)), null, null, null, null);
        int value = -1;
        if (cursor.moveToFirst()) {
            value = cursor.getInt(cursor.getColumnIndex("u16UI_VorizontalStart"));
        }
        cursor.close();
        return value;
    }

    public int queryPCClock() {
        int id = 0;
        while (true) {
            if (id >= 10) {
                break;
            } else if (((short) queryPCModeIndex(id)) == this.f0com.getVideoInfo().s16ModeIndex) {
                Log.i("DataBaseDeskImpl", "~~~~~~~~clock id is " + id + "~~~~~~~~~~~");
                break;
            } else {
                id++;
            }
        }
        Cursor cursor = this.cr.query(Uri.parse("content://mstar.tv.usersetting/userpcmodesetting/" + String.valueOf(id)), null, null, null, null);
        int value = -1;
        if (cursor.moveToFirst()) {
            value = cursor.getInt(cursor.getColumnIndex("u16UI_Clock"));
        }
        cursor.close();
        return value;
    }

    public int queryPCModeIndex(int id) {
        Cursor cursor = this.cr.query(Uri.parse("content://mstar.tv.usersetting/userpcmodesetting/" + String.valueOf(id)), null, null, null, null);
        int value = -1;
        if (cursor.moveToFirst()) {
            value = cursor.getInt(cursor.getColumnIndex("u8ModeIndex"));
        }
        cursor.close();
        return value;
    }

    public boolean isPCTimingNew() {
        Log.i("DataBaseDeskImpl", "c video info ModeIndex is :" + this.f0com.getVideoInfo().s16ModeIndex + "~~~~~~~~~~~");
        for (int id = 0; id < 9; id++) {
            Log.i("DataBaseDeskImpl", "~method(isPCTimingNew)~~~~~~~ id is :" + id + "||||||   ModeIndex is:" + queryPCModeIndex(id) + "~~~~~~~~~~~");
            if (((short) queryPCModeIndex(id)) == this.f0com.getVideoInfo().s16ModeIndex) {
                Log.i("DataBaseDeskImpl", "~~~~~~~~isPCTimingNew id is " + id + "~~~~~~~~~~~");
                return true;
            }
        }
        return false;
    }

    public int queryPCPhase() {
        Log.i("DataBaseDeskImpl", "video info ModeIndex is :" + this.f0com.getVideoInfo().s16ModeIndex + "~~~~~~~~~~~");
        int id = 0;
        while (true) {
            if (id >= 10) {
                break;
            }
            Log.i("DataBaseDeskImpl", "~~~~~~~~ id is :" + id + "||||||   ModeIndex is:" + queryPCModeIndex(id) + "~~~~~~~~~~~");
            if (((short) queryPCModeIndex(id)) == this.f0com.getVideoInfo().s16ModeIndex) {
                Log.i("DataBaseDeskImpl", "~~~~~~~~Phase id is " + id + "~~~~~~~~~~~");
                break;
            }
            id++;
        }
        Cursor cursor = this.cr.query(Uri.parse("content://mstar.tv.usersetting/userpcmodesetting/" + String.valueOf(id)), null, null, null, null);
        int value = -1;
        if (cursor.moveToFirst()) {
            value = cursor.getInt(cursor.getColumnIndex("u16UI_Phase"));
        }
        cursor.close();
        return value;
    }

    public EnumChinaDvbcRegion getDTVCity() {
        EnumChinaDvbcRegion value = null;
        Cursor cursor = this.cr.query(Uri.parse(this.userSettingSchema + "/chinadvbcsetting"), null, null, null, null);
        if (cursor.moveToFirst()) {
            value = EnumChinaDvbcRegion.values()[cursor.getInt(cursor.getColumnIndex("eDVBCRegion"))];
        }
        cursor.close();
        return value;
    }

    public void setDTVCity(EnumChinaDvbcRegion eType) {
        ContentValues v = new ContentValues();
        v.put("eDVBCRegion", Integer.valueOf(eType.ordinal()));
        long ret = -1;
        try {
            System.out.println("\n########### update \n");
            ret = (long) this.cr.update(Uri.parse(this.userSettingSchema + "/chinadvbcsetting"), v, null, null);
        } catch (SQLException e) {
            Log.e("DataBaseDeskImpl", "update failed");
        }
        if (ret == -1) {
            System.out.println("\n########### update failed \n");
            return;
        }
        try {
            if (TvManager.getInstance() != null) {
                TvManager.getInstance().getDatabaseManager().setDatabaseDirtyByApplication(4);
            }
        } catch (TvCommonException e2) {
            e2.printStackTrace();
        }
    }

    public void setSystemLock(int systemLock) {
        ContentValues v = new ContentValues();
        v.put("u8BlockSysLockMode", Integer.valueOf(systemLock));
        long ret = -1;
        try {
            ret = (long) this.cr.update(Uri.parse(this.userSettingSchema + "/blocksyssetting"), v, null, null);
            this.blockSysSetting.blockSysLockMode = systemLock;
        } catch (SQLException e) {
            Log.e("DataBaseDeskImpl", "update system lock  failed");
        }
        if (ret != -1) {
            try {
                if (TvManager.getInstance() != null) {
                    TvManager.getInstance().getDatabaseManager().setDatabaseDirtyByApplication(2);
                }
            } catch (TvCommonException e2) {
                e2.printStackTrace();
            }
        }
    }

    public void setParentalControl(int parentalControl) {
        ContentValues v = new ContentValues();
        v.put("u8ParentalControl", Integer.valueOf(parentalControl));
        long ret = -1;
        try {
            ret = (long) this.cr.update(Uri.parse(this.userSettingSchema + "/blocksyssetting"), v, null, null);
            this.blockSysSetting.parentalControl = parentalControl;
        } catch (SQLException e) {
            Log.e("DataBaseDeskImpl", "update parental control  failed");
        }
        if (ret != -1) {
            try {
                if (TvManager.getInstance() != null) {
                    TvManager.getInstance().getDatabaseManager().setDatabaseDirtyByApplication(2);
                }
            } catch (TvCommonException e2) {
                e2.printStackTrace();
            }
        }
    }

    public void updateChannelNameAndEventName(ContentValues value, int startTime) {
        long ret = -1;
        try {
            System.out.println("\n########### update \n");
            String[] selectionArgs = {String.valueOf(startTime)};
            ret = (long) this.cr.update(Uri.parse(this.userSettingSchema + "/epgtimer"), value, "u32StartTime = ? ", selectionArgs);
        } catch (SQLException e) {
            Log.e("DataBaseDeskImpl", "update block system password  failed");
        }
        if (ret == -1) {
            System.out.println("\n########### update failed\n");
        }
    }

    public void setBlockSysPassword(int blockSysPassword) {
        ContentValues v = new ContentValues();
        v.put("u16BlockSysPassword", Integer.valueOf(blockSysPassword));
        long ret = -1;
        try {
            ret = (long) this.cr.update(Uri.parse(this.userSettingSchema + "/blocksyssetting"), v, null, null);
            this.blockSysSetting.blockSysPassword = blockSysPassword;
        } catch (SQLException e) {
            Log.e("DataBaseDeskImpl", "update block system password  failed");
        }
        if (ret != -1) {
            try {
                if (TvManager.getInstance() != null) {
                    TvManager.getInstance().getDatabaseManager().setDatabaseDirtyByApplication(2);
                }
            } catch (TvCommonException e2) {
                e2.printStackTrace();
            }
        }
    }

    public int getParentalControlRating() {
        return this.blockSysSetting.parentalControl;
    }

    public int getSystemLockPassword() {
        return this.blockSysSetting.blockSysPassword;
    }

    public boolean IsSystemLocked() {
        return this.blockSysSetting.blockSysLockMode == 1;
    }

    public int getDVBCNetTableFrequency() {
        int value = 0;
        Cursor cursor = this.cr.query(Uri.parse(this.userSettingSchema + "/chinadvbcsetting"), null, null, null, null);
        if (cursor.moveToFirst()) {
            value = cursor.getInt(cursor.getColumnIndex("u32NITFreq"));
        }
        cursor.close();
        return value;
    }

    public void setDVBCNetTableFrequency(int iFre) {
        ContentValues v = new ContentValues();
        v.put("u32NITFreq", Integer.valueOf(iFre * ChannelDesk.max_dtv_count));
        long ret = -1;
        try {
            ret = (long) this.cr.update(Uri.parse(this.userSettingSchema + "/chinadvbcsetting"), v, null, null);
        } catch (SQLException e) {
            Log.e("DataBaseDeskImpl", "update failed");
        }
        if (ret != -1) {
            try {
                if (TvManager.getInstance() != null) {
                    TvManager.getInstance().getDatabaseManager().setDatabaseDirtyByApplication(4);
                }
            } catch (TvCommonException e2) {
                e2.printStackTrace();
            }
        }
    }

    public void setDynamicBLMode(EN_MS_LOCALDIMMING DynamicMode) {
        ContentValues v = new ContentValues();
        v.put("enLocalDimm", Integer.valueOf(DynamicMode.ordinal()));
        long ret = -1;
        try {
            ret = (long) this.cr.update(Uri.parse(this.userSettingSchema + "/systemsetting"), v, null, null);
        } catch (SQLException e) {
            Log.e("DataBaseDeskImpl", "update failed");
        }
        if (ret == -1) {
        }
    }

    public int getDynamicBLMode() {
        int value = 0;
        Cursor cursor = this.cr.query(Uri.parse(this.userSettingSchema + "/systemsetting"), null, null, null, null);
        if (cursor.moveToFirst()) {
            value = cursor.getInt(cursor.getColumnIndex("enLocalDimm"));
        }
        cursor.close();
        Log.d("enLocalDimm", "value=======" + value);
        return value;
    }

    public Cursor queryEpg(boolean bIsWithStartTime, int iStartTime) {
        if (!bIsWithStartTime) {
            return this.cr.query(Uri.parse(this.userSettingSchema + "/epgtimer"), null, null, null, null);
        }
        String[] selectionArgs = {String.valueOf(iStartTime)};
        return this.cr.query(Uri.parse(this.userSettingSchema + "/epgtimer"), null, "u32StartTime = ? ", selectionArgs, null);
    }

    public int deleteEpg(int iStartTime) {
        try {
            return this.cr.delete(Uri.parse(this.userSettingSchema + "/epgtimer"), " u32StartTime = ? ", new String[]{String.valueOf(iStartTime)});
        } catch (SQLiteException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public long insertEpg(ContentValues value) {
        try {
            Uri insert = this.cr.insert(Uri.parse(this.userSettingSchema + "/epgtimer"), value);
            return 0;
        } catch (SQLiteException e) {
            e.printStackTrace();
            return -1;
        }
    }

    private boolean initVarFactory() {
        this.m_stFactoryColorTemp = new T_MS_COLOR_TEMP();
        this.m_stFactoryAdc = new MS_ADC_SETTING();
        this.m_pastNLASet = new MS_NLA_SETTING();
        this.m_stFactoryExt = new MS_FACTORY_EXTERN_SETTING();
        this.mNoStandSet = new MS_Factory_NS_VD_SET();
        this.mVifSet = new MS_Factory_NS_VIF_SET();
        this.mSscSet = new MS_FACTORY_SSC_SET();
        this.mSRSSet = new KK_SRS_SET();
        this.blockSysSetting = new ST_MAPI_BLOCK_SYS_SETTING();
        this.m_bADCAutoTune = false;
        int max1 = MAX_DTV_Resolution_Info.E_DTV_MAX.ordinal();
        int max2 = MAPI_VIDEO_ARC_Type.E_AR_MAX.ordinal();
        for (int i = 0; i < max1; i++) {
            for (int j = 0; j < max2; j++) {
                this.m_DTVOverscanSet[i][j] = new ST_MAPI_VIDEO_WINDOW_INFO();
            }
        }
        int max12 = MAX_HDMI_Resolution_Info.E_HDMI_MAX.ordinal();
        int max22 = MAPI_VIDEO_ARC_Type.E_AR_MAX.ordinal();
        for (int i1 = 0; i1 < max12; i1++) {
            for (int j1 = 0; j1 < max22; j1++) {
                this.m_HDMIOverscanSet[i1][j1] = new ST_MAPI_VIDEO_WINDOW_INFO();
            }
        }
        int max13 = MAX_YPbPr_Resolution_Info.E_YPbPr_MAX.ordinal();
        int max23 = MAPI_VIDEO_ARC_Type.E_AR_MAX.ordinal();
        for (int i2 = 0; i2 < max13; i2++) {
            for (int j2 = 0; j2 < max23; j2++) {
                this.m_YPbPrOverscanSet[i2][j2] = new ST_MAPI_VIDEO_WINDOW_INFO();
            }
        }
        int max14 = EN_VD_SIGNALTYPE.SIG_NUMS.ordinal();
        int max24 = MAPI_VIDEO_ARC_Type.E_AR_MAX.ordinal();
        for (int i3 = 0; i3 < max14; i3++) {
            for (int j3 = 0; j3 < max24; j3++) {
                this.m_VDOverscanSet[i3][j3] = new ST_MAPI_VIDEO_WINDOW_INFO();
            }
        }
        this.m_stPEQSet = new ST_FACTORY_PEQ_SETTING();
        this.m_stCISet = new ST_FACTORY_CI_SETTING();
        this.m_stFactoryColorTemp = new T_MS_COLOR_TEMP();
        this.m_stFactoryColorTempEx = new T_MS_COLOR_TEMPEX();
        this.customerCfgMiscSetting = new CustomerCfgMiscSetting();
        return true;
    }

    private boolean initVarSound() {
        this.soundpara = new MS_USER_SOUND_SETTING();
        this.soundpara.u16CheckSum = 65535;
        this.astSoundModeSetting = new SoundModeSeting[EN_SOUND_MODE.SOUND_MODE_NUM.ordinal()];
        this.astSoundModeSetting[0] = new SoundModeSeting(50, 50, 50, 50, 50, 50, 50);
        this.astSoundModeSetting[1] = new SoundModeSeting(70, 40, 70, 60, 50, 50, 40);
        this.astSoundModeSetting[2] = new SoundModeSeting(60, 30, 60, 50, 45, 40, 30);
        this.astSoundModeSetting[3] = new SoundModeSeting(40, 80, 40, 45, 50, 60, 80);
        this.astSoundModeSetting[4] = new SoundModeSeting(50, 50, 50, 50, 50, 50, 50);
        this.astSoundModeSetting[5] = new SoundModeSeting(50, 50, 50, 50, 50, 50, 50);
        this.astSoundModeSetting[6] = new SoundModeSeting(50, 50, 50, 50, 50, 50, 50);
        return true;
    }

    private boolean initVarPicture() {
        short[][] initPicData = {new short[]{50, 50, 50, 50, 50}, new short[]{60, 55, 60, 60, 50}, new short[]{40, 45, 45, 40, 50}, new short[]{50, 50, 50, 50, 50}, new short[]{50, 50, 50, 50, 50}, new short[]{50, 50, 50, 50, 50}, new short[]{50, 50, 50, 50, 50}};
        this.videopara = new T_MS_VIDEO();
        this.videopara.CheckSum = 65535;
        this.videopara.ePicture = EN_MS_PICTURE.PICTURE_NORMAL;
        int count = EN_MS_PICTURE.PICTURE_NUMS.ordinal();
        this.videopara.astPicture = new T_MS_PICTURE[count];
        for (int i = 0; i < count; i++) {
            int j = 0 + 1;
            short contrast = initPicData[i][0];
            int j2 = j + 1;
            short brightness = initPicData[i][j];
            int j3 = j2 + 1;
            short color = initPicData[i][j2];
            int j4 = j3 + 1;
            this.videopara.astPicture[i] = new T_MS_PICTURE(100, contrast, brightness, color, initPicData[i][j3], initPicData[i][j4], EN_MS_COLOR_TEMP.MS_COLOR_TEMP_NATURE, EN_MS_PIC_ADV.MS_MIDDLE, EN_MS_PIC_ADV.MS_MIDDLE, EN_MS_PIC_ADV.MS_MIDDLE, EN_MS_PIC_ADV.MS_MIDDLE);
        }
        int count2 = EN_MS_COLOR_TEMP.MS_COLOR_TEMP_NUM.ordinal();
        this.videopara.eNRMode = new T_MS_NR_MODE[count2];
        for (int i2 = 0; i2 < count2; i2++) {
            this.videopara.eNRMode[i2] = new T_MS_NR_MODE(EN_MS_NR.MS_NR_MIDDLE, EN_MS_MPEG_NR.MS_MPEG_NR_MIDDLE);
        }
        this.videopara.g_astSubColor = new T_MS_SUB_COLOR(65535, 0, 0);
        this.videopara.enARCType = MAPI_VIDEO_ARC_Type.E_AR_16x9;
        this.videopara.fOutput_RES = EN_DISPLAY_RES_TYPE.DISPLAY_RES_FULL_HD;
        this.videopara.tvsys = MAPI_VIDEO_OUT_VE_SYS.MAPI_VIDEO_OUT_VE_AUTO;
        this.videopara.LastVideoStandardMode = MAPI_AVD_VideoStandardType.E_MAPI_VIDEOSTANDARD_AUTO;
        this.videopara.LastAudioStandardMode = AUDIOMODE_TYPE_.E_AUDIOMODE_MONO_;
        this.videopara.eDynamic_Contrast = EN_MS_Dynamic_Contrast.MS_Dynamic_Contrast_ON;
        this.videopara.eFilm = EN_MS_FILM.MS_FILM_OFF;
        this.videopara.ThreeDVideoMode = new ThreeD_Video_MODE(EN_ThreeD_Video.DB_ThreeD_Video_OFF, EN_ThreeD_Video_SELFADAPTIVE_DETECT.DB_ThreeD_Video_SELF_ADAPTIVE_DETECT_OFF, EN_ThreeD_Video_DISPLAYFORMAT.DB_ThreeD_Video_DISPLAYFORMAT_NONE, EN_ThreeD_Video_3DTO2D.DB_ThreeD_Video_3DTO2D_NONE, EN_ThreeD_Video_3DDEPTH.DB_ThreeD_Video_3DDEPTH_LEVEL_15, EN_ThreeD_Video_3DOFFSET.DB_ThreeD_Video_3DOFFSET_LEVEL_15, EN_ThreeD_Video_AUTOSTART.DB_ThreeD_Video_AUTOSTART_OFF, EN_ThreeD_Video_3DOUTPUTASPECT.DB_ThreeD_Video_3DOUTPUTASPECT_FULLSCREEN, EN_ThreeD_Video_LRVIEWSWITCH.DB_ThreeD_Video_LRVIEWSWITCH_NOTEXCHANGE);
        this.videopara.stUserOverScanMode = new T_MS_OVERSCAN_SETTING_USER(0, 0, 0, 0);
        this.videopara.eTvFormat = EN_DISPLAY_TVFORMAT.DISPLAY_TVFORMAT_16TO9HD;
        this.videopara.skinTone = SkinToneMode.SKIN_TONE_OFF;
        this.videopara.detailEnhance = false;
        this.videopara.DNR = EN_MS_NR.MS_NR_AUTO;
        this.colorParaEx = new T_MS_COLOR_TEMPEX_DATA(128, 128, 128, 128, 128, 128);
        return true;
    }

    private boolean InitSettingVar() {
        Log.e("TvService", "SettingServiceImpl InitVar!!");
        this.stUsrData = new MS_USER_SYSTEM_SETTING();
        this.stUsrData.checkSum = 65535;
        this.stUsrData.fRunInstallationGuide = true;
        this.stUsrData.fNoChannel = false;
        this.stUsrData.bDisableSiAutoUpdate = false;
        this.stUsrData.enInputSourceType = EnumInputSource.E_INPUT_SOURCE_ATV;
        this.stUsrData.Country = MEMBER_COUNTRY.E_AUSTRALIA;
        this.stUsrData.enCableOperators = EN_CABLE_OPERATORS.EN_CABLEOP_CDSMATV;
        this.stUsrData.enSatellitePlatform = EN_SATELLITE_PLATFORM.EN_SATEPF_HDPLUS;
        this.stUsrData.u8OADTime = 0;
        this.stUsrData.fOADScanAfterWakeup = 0;
        this.stUsrData.fAutoVolume = 0;
        this.stUsrData.fDcPowerOFFMode = 0;
        this.stUsrData.DtvRoute = 2;
        this.stUsrData.ScartOutRGB = 0;
        this.stUsrData.U8Transparency = 0;
        this.stUsrData.u32MenuTimeOut = 0;
        this.stUsrData.AudioOnly = 0;
        this.stUsrData.bEnableWDT = 0;
        this.stUsrData.u8FavoriteRegion = 0;
        this.stUsrData.u8Bandwidth = 0;
        this.stUsrData.u8TimeShiftSizeType = 0;
        this.stUsrData.fOadScan = 0;
        this.stUsrData.bEnablePVRRecordAll = 0;
        this.stUsrData.u8ColorRangeMode = 0;
        this.stUsrData.u8HDMIAudioSource = 0;
        this.stUsrData.bEnableAlwaysTimeshift = 0;
        this.stUsrData.eSUPER = EN_MS_SUPER.MS_SUPER_OFF;
        this.stUsrData.bUartBus = false;
        this.stUsrData.m_AutoZoom = 0;
        this.stUsrData.bOverScan = false;
        this.stUsrData.m_u8BrazilVideoStandardType = 0;
        this.stUsrData.m_u8SoftwareUpdateMode = 0;
        this.stUsrData.u32OSD_Active_Time = 0;
        this.stUsrData.m_MessageBoxExist = false;
        this.stUsrData.u16LastOADVersion = 0;
        this.stUsrData.bEnableAutoChannelUpdate = false;
        this.stUsrData.u8OsdDuration = 2;
        this.stUsrData.eChSwMode = EN_MS_CHANNEL_SWITCH_MODE.MS_CHANNEL_SWM_BLACKSCREEN;
        this.stUsrData.eOffDetMode = EN_MS_OFFLINE_DET_MODE.MS_OFFLINE_DET_OFF;
        this.stUsrData.bBlueScreen = false;
        this.stUsrData.ePWR_Music = EN_MS_POWERON_MUSIC.MS_POWERON_MUSIC_DEFAULT;
        this.stUsrData.ePWR_Logo = EN_MS_POWERON_LOGO.MS_POWERON_LOGO_DEFAULT;
        this.stUsrData.enLanguage = EnumLanguage.E_ENGLISH;
        this.stUsrData.standbyNoOperation = 0;
        this.stUsrData.standbyNoSignal = false;
        this.stUsrData.screenSaveMode = false;
        this.stUsrData.U8SystemAutoTimeType = 2;
        this.stUsrData.smartEnergySaving = SmartEnergySavingMode.MODE_OFF;
        this.stUsrData.colorWheelMode = ColorWheelMode.MODE_OFF;
        this.stUsrColorTemp = new T_MS_COLOR_TEMP_DATA(128, 128, 128, 128, 128, 128);
        this.stUsrColorTempEx = new T_MS_COLOR_TEMPEX_DATA[EnumInputSource.E_INPUT_SOURCE_NUM.ordinal()];
        for (int i = 0; i < EnumInputSource.E_INPUT_SOURCE_NUM.ordinal(); i++) {
            this.stUsrColorTempEx[i] = new T_MS_COLOR_TEMPEX_DATA(128, 128, 128, 128, 128, 128);
        }
        this.stSubtitleSet = new MS_USER_SUBTITLE_SETTING(EnumLanguage.E_ENGLISH, EnumLanguage.E_ENGLISH, false, false);
        this.stUserLocationSetting = new MS_USER_LOCATION_SETTING(0, 0, 0);
        return true;
    }

    public boolean saveInputSource(EnumInputSource source) {
        this.stUsrData.enInputSourceType = source;
        updateUserSysSetting(this.stUsrData);
        return false;
    }

    public int queryCurInputSrc() {
        int value = 0;
        Cursor cursor = this.cr.query(Uri.parse(this.userSettingSchema + "/systemsetting"), null, null, null, null);
        if (cursor.moveToFirst()) {
            value = cursor.getInt(cursor.getColumnIndex("enInputSourceType"));
        }
        cursor.close();
        return value;
    }

    public int queryCurCountry() {
        int value = 0;
        Cursor cursor = this.cr.query(Uri.parse(this.userSettingSchema + "/systemsetting"), null, null, null, null);
        if (cursor.moveToFirst()) {
            value = cursor.getInt(cursor.getColumnIndex("Country"));
        }
        cursor.close();
        return value;
    }

    public void updateCurInputSrc(int inputSrc) {
        ContentValues vals = new ContentValues();
        vals.put("enInputSourceType", Integer.valueOf(inputSrc));
        long ret = -1;
        try {
            ret = (long) this.cr.update(Uri.parse(this.userSettingSchema + "/systemsetting"), vals, null, null);
        } catch (SQLException e) {
            Log.e("DataBaseDeskImpl", "update failed");
        }
        if (ret == -1) {
        }
    }

    private boolean initCECVar() {
        this.stCECPara = new MS_CEC_SETTING(65535, 0, 0, 0, 0);
        return true;
    }

    public T_MS_VIDEO getVideo() {
        this.f0com.printfE("TvService", "DataBaseServiceImpl getVideo!!");
        return this.videopara;
    }

    public boolean setVideo(T_MS_VIDEO video) {
        this.videopara = video;
        return true;
    }

    public T_MS_COLOR_TEMPEX_DATA getVideoTempEx() {
        return this.colorParaEx;
    }

    public boolean setVideoTempEx(T_MS_COLOR_TEMPEX_DATA videotmp) {
        this.colorParaEx = videotmp;
        return true;
    }

    public T_MS_COLOR_TEMP getVideoTemp() {
        return this.m_stFactoryColorTemp;
    }

    public boolean setVideoTemp(T_MS_COLOR_TEMP videotmp) {
        this.m_stFactoryColorTemp = videotmp;
        return true;
    }

    public MS_ADC_SETTING getAdcSetting() {
        return this.m_stFactoryAdc;
    }

    public boolean setAdcSetting(MS_ADC_SETTING adcSet) {
        this.m_stFactoryAdc = adcSet;
        return true;
    }

    public MS_USER_SYSTEM_SETTING getUsrData() {
        return this.stUsrData;
    }

    public boolean setUsrData(MS_USER_SYSTEM_SETTING stData) {
        this.stUsrData = stData;
        return true;
    }

    public MS_USER_SUBTITLE_SETTING getSubtitleSet() {
        return this.stSubtitleSet;
    }

    public boolean setSubtitleSet(MS_USER_SUBTITLE_SETTING stData) {
        this.stSubtitleSet = stData;
        return true;
    }

    public MS_USER_LOCATION_SETTING getLocationSet() {
        return this.stUserLocationSetting;
    }

    public boolean setLocationSet(MS_USER_LOCATION_SETTING stData) {
        this.stUserLocationSetting = stData;
        return true;
    }

    public MS_CEC_SETTING getCECVar() {
        return this.stCECPara;
    }

    public boolean setCECVar(MS_CEC_SETTING stCec) {
        this.stCECPara = stCec;
        return true;
    }

    public MS_USER_SOUND_SETTING getSound() {
        return this.soundpara;
    }

    public boolean setSound(MS_USER_SOUND_SETTING stMode) {
        this.soundpara = stMode;
        return false;
    }

    public SoundModeSeting getSoundMode(EN_SOUND_MODE eMode) {
        return this.astSoundModeSetting[eMode.ordinal()];
    }

    public boolean setSoundMode(EN_SOUND_MODE eMode, SoundModeSeting stSoundMode) {
        this.astSoundModeSetting[eMode.ordinal()] = stSoundMode;
        return true;
    }

    public boolean setSoundMode(EN_SOUND_MODE eMode) {
        ContentValues vals = new ContentValues();
        vals.put("SoundMode", Integer.valueOf(eMode.ordinal()));
        int iRet = -1;
        try {
            iRet = this.cr.update(Uri.parse(this.userSettingSchema + "/soundsetting"), vals, null, null);
        } catch (SQLException e) {
            Log.e("DataBaseDeskImpl", "update failed");
        }
        if (iRet == -1) {
            return false;
        }
        return true;
    }

    public boolean restoreUsrDB(EN_SYSTEM_FACTORY_DB_COMMAND eType) {
        switch ($SWITCH_TABLE$com$konka$kkinterface$tv$DataBaseDesk$EN_SYSTEM_FACTORY_DB_COMMAND()[eType.ordinal()]) {
            case 3:
                this.f0com.printfE("TvService", "restoreUsrDB : E_FACTORY_RESTORE_DEFAULT!!" + this.videopara);
                initVarPicture();
                InitSettingVar();
                initCECVar();
                initVarSound();
                this.f0com.printfE("TvService", "restoreUsrDB : E_FACTORY_RESTORE_DEFAULT!!" + this.videopara);
                break;
        }
        return true;
    }

    public MS_FACTORY_EXTERN_SETTING getFactoryExt() {
        return this.m_stFactoryExt;
    }

    public boolean setFactoryExt(MS_FACTORY_EXTERN_SETTING stFactory) {
        this.m_stFactoryExt = stFactory;
        return true;
    }

    public MS_Factory_NS_VD_SET getNoStandSet() {
        return this.mNoStandSet;
    }

    public boolean setNoStandSet(MS_Factory_NS_VD_SET stNonStand) {
        this.mNoStandSet = stNonStand;
        return true;
    }

    public MS_Factory_NS_VIF_SET getNoStandVifSet() {
        return this.mVifSet;
    }

    public boolean setNoStandVifSet(MS_Factory_NS_VIF_SET stVif) {
        this.mVifSet = stVif;
        return true;
    }

    public MS_FACTORY_SSC_SET getSscSet() {
        return this.mSscSet;
    }

    public boolean setSscSet(MS_FACTORY_SSC_SET stLvdsSsc) {
        this.mSscSet = stLvdsSsc;
        return true;
    }

    public void SyncUserSettingDB() {
        queryUserSysSetting();
        queryAllVideoPara(this.f0com.GetCurrentInputSource().ordinal());
    }

    public void setSRSOnOff(EnumSwitchOnOff eOnOff) {
        ContentValues values = new ContentValues();
        values.put("bSRSOn", Integer.valueOf(eOnOff.ordinal()));
        try {
            this.cr.update(Uri.parse(this.userSettingSchema + "/srssetting"), values, null, null);
        } catch (Exception e) {
            Log.e("KKJAVAAPI", "update tbl_SRSSetting error");
        }
    }

    public EnumSwitchOnOff getSRSOnOff() {
        EnumSwitchOnOff value = EnumSwitchOnOff.SWITCH_OFF;
        Cursor cursor = this.cr.query(Uri.parse(this.userSettingSchema + "/srssetting"), null, null, null, null);
        if (cursor.moveToFirst()) {
            value = EnumSwitchOnOff.values()[cursor.getInt(cursor.getColumnIndex("bSRSOn"))];
        }
        cursor.close();
        return value;
    }

    public void setTruebaseOnOff(EnumSwitchOnOff eOnOff) {
        ContentValues values = new ContentValues();
        values.put("bTruebassOn", Integer.valueOf(eOnOff.ordinal()));
        try {
            this.cr.update(Uri.parse(this.userSettingSchema + "/srssetting"), values, null, null);
        } catch (Exception e) {
            Log.e("KKJAVAAPI", "update tbl_SRSSetting error");
        }
    }

    public EnumSwitchOnOff getTruebaseOnOff() {
        EnumSwitchOnOff value = EnumSwitchOnOff.SWITCH_OFF;
        Cursor cursor = this.cr.query(Uri.parse(this.userSettingSchema + "/srssetting"), null, null, null, null);
        if (cursor.moveToFirst()) {
            value = EnumSwitchOnOff.values()[cursor.getInt(cursor.getColumnIndex("bTruebassOn"))];
        }
        cursor.close();
        return value;
    }

    public void setDialogClarityOnOff(EnumSwitchOnOff eOnOff) {
        ContentValues values = new ContentValues();
        values.put("bDialogClarityOn", Integer.valueOf(eOnOff.ordinal()));
        try {
            this.cr.update(Uri.parse(this.userSettingSchema + "/srssetting"), values, null, null);
        } catch (Exception e) {
            Log.e("KKJAVAAPI", "update tbl_SRSSetting error");
        }
    }

    public EnumSwitchOnOff getDialogClarityOnOff() {
        EnumSwitchOnOff value = EnumSwitchOnOff.SWITCH_OFF;
        Cursor cursor = this.cr.query(Uri.parse(this.userSettingSchema + "/srssetting"), null, null, null, null);
        if (cursor.moveToFirst()) {
            value = EnumSwitchOnOff.values()[cursor.getInt(cursor.getColumnIndex("bDialogClarityOn"))];
        }
        cursor.close();
        return value;
    }

    public void setDefinitionOnOff(EnumSwitchOnOff eOnOff) {
        ContentValues values = new ContentValues();
        values.put("bDefinitionOn", Integer.valueOf(eOnOff.ordinal()));
        try {
            this.cr.update(Uri.parse(this.userSettingSchema + "/srssetting"), values, null, null);
        } catch (Exception e) {
            Log.e("KKJAVAAPI", "update tbl_SRSSetting error");
        }
    }

    public EnumSwitchOnOff getDefinitionOnOff() {
        EnumSwitchOnOff value = EnumSwitchOnOff.SWITCH_OFF;
        Cursor cursor = this.cr.query(Uri.parse(this.userSettingSchema + "/srssetting"), null, null, null, null);
        if (cursor.moveToFirst()) {
            value = EnumSwitchOnOff.values()[cursor.getInt(cursor.getColumnIndex("bDefinitionOn"))];
        }
        cursor.close();
        return value;
    }

    public KK_SRS_SET querySRSAdjust() {
        Cursor cursor = this.cr.query(Uri.parse(this.factoryCusSchema + "/srsadjust"), null, null, null, null);
        if (cursor.moveToFirst()) {
            this.mSRSSet.srs_InputGain = cursor.getInt(cursor.getColumnIndex("iInputGain"));
            this.mSRSSet.srs_SurrLevelControl = cursor.getInt(cursor.getColumnIndex("iSurrLevelCtl"));
            this.mSRSSet.srs_SpeakerAudio = cursor.getInt(cursor.getColumnIndex("iSpeakerAudio"));
            this.mSRSSet.srs_SpeakerAnalysis = cursor.getInt(cursor.getColumnIndex("iSpeakerAnalysis"));
            this.mSRSSet.srs_TrubassControl = cursor.getInt(cursor.getColumnIndex("iTrubassCtl"));
            this.mSRSSet.srs_DCControl = cursor.getInt(cursor.getColumnIndex("iDCCtl"));
            this.mSRSSet.srs_DefinitionControl = cursor.getInt(cursor.getColumnIndex("iDefinitionCtl"));
        }
        cursor.close();
        return this.mSRSSet;
    }

    public KK_SRS_SET getSRSSet() {
        return this.mSRSSet;
    }

    public boolean setSRSSet(KK_SRS_SET stSRS, EN_SRS_SET_TYPE eSRS) {
        ContentValues values = new ContentValues();
        if (eSRS == EN_SRS_SET_TYPE.E_SRS_INPUTGAIN) {
            values.put("iInputGain", Integer.valueOf(stSRS.srs_InputGain));
            try {
                this.cr.update(Uri.parse(this.factoryCusSchema + "/srsadjust"), values, null, null);
            } catch (Exception e) {
                Log.e("KKJAVAAPI", "update tbl_SRSAdjust error");
            }
            this.mSRSSet.srs_InputGain = stSRS.srs_InputGain;
        } else if (eSRS == EN_SRS_SET_TYPE.E_SRS_SURRLEVEL_CONTROL) {
            values.put("iSurrLevelCtl", Integer.valueOf(stSRS.srs_SurrLevelControl));
            try {
                this.cr.update(Uri.parse(this.factoryCusSchema + "/srsadjust"), values, null, null);
            } catch (Exception e2) {
                Log.e("KKJAVAAPI", "update tbl_SRSAdjust error");
            }
            this.mSRSSet.srs_SurrLevelControl = stSRS.srs_SurrLevelControl;
        } else if (eSRS == EN_SRS_SET_TYPE.E_SRS_SPEAKERAUDIO) {
            values.put("iSpeakerAudio", Integer.valueOf(stSRS.srs_SpeakerAudio));
            try {
                this.cr.update(Uri.parse(this.factoryCusSchema + "/srsadjust"), values, null, null);
            } catch (Exception e3) {
                Log.e("KKJAVAAPI", "update tbl_SRSAdjust error");
            }
            this.mSRSSet.srs_SpeakerAudio = stSRS.srs_SpeakerAudio;
        } else if (eSRS == EN_SRS_SET_TYPE.E_SRS_SPEAKERANALYSIS) {
            values.put("iSpeakerAnalysis", Integer.valueOf(stSRS.srs_SpeakerAnalysis));
            try {
                this.cr.update(Uri.parse(this.factoryCusSchema + "/srsadjust"), values, null, null);
            } catch (Exception e4) {
                Log.e("KKJAVAAPI", "update tbl_SRSAdjust error");
            }
            this.mSRSSet.srs_SpeakerAnalysis = stSRS.srs_SpeakerAnalysis;
        } else if (eSRS == EN_SRS_SET_TYPE.E_SRS_TRUBASS_CONTROL) {
            values.put("iTrubassCtl", Integer.valueOf(stSRS.srs_TrubassControl));
            try {
                this.cr.update(Uri.parse(this.factoryCusSchema + "/srsadjust"), values, null, null);
            } catch (Exception e5) {
                Log.e("KKJAVAAPI", "update tbl_SRSAdjust error");
            }
            this.mSRSSet.srs_TrubassControl = stSRS.srs_TrubassControl;
        } else if (eSRS == EN_SRS_SET_TYPE.E_SRS_DC_CONTROL) {
            values.put("iDCCtl", Integer.valueOf(stSRS.srs_DCControl));
            try {
                this.cr.update(Uri.parse(this.factoryCusSchema + "/srsadjust"), values, null, null);
            } catch (Exception e6) {
                Log.e("KKJAVAAPI", "update tbl_SRSAdjust error");
            }
            this.mSRSSet.srs_DCControl = stSRS.srs_DCControl;
        } else if (eSRS == EN_SRS_SET_TYPE.E_SRS_DEFINITION_CONTROL) {
            values.put("iDefinitionCtl", Integer.valueOf(stSRS.srs_DefinitionControl));
            try {
                this.cr.update(Uri.parse(this.factoryCusSchema + "/srsadjust"), values, null, null);
            } catch (Exception e7) {
                Log.e("KKJAVAAPI", "update tbl_SRSAdjust error");
            }
            this.mSRSSet.srs_DefinitionControl = stSRS.srs_DefinitionControl;
        }
        return true;
    }

    public void queryCustomerCfgMiscSetting() {
        boolean z;
        Cursor cursor = this.cr.query(Uri.parse(this.factoryCusSchema + "/miscsetting"), null, null, null, null);
        if (cursor.moveToFirst()) {
            System.out.printf("getColumnIndex=%x\n", new Object[]{Integer.valueOf(cursor.getColumnIndex("EnergyEnable"))});
            System.out.printf("getInt=%x\n", new Object[]{Integer.valueOf(cursor.getInt(cursor.getColumnIndex("EnergyEnable")))});
            CustomerCfgMiscSetting customerCfgMiscSetting2 = this.customerCfgMiscSetting;
            if (cursor.getInt(cursor.getColumnIndex("EnergyEnable")) == 0) {
                z = false;
            } else {
                z = true;
            }
            customerCfgMiscSetting2.energyEnable = z;
            this.customerCfgMiscSetting.energyPercent = (short) cursor.getInt(cursor.getColumnIndex("EnergyPercent"));
        }
        cursor.close();
    }

    public void updateCustomerCfgMiscSetting(CustomerCfgMiscSetting model) {
        ContentValues vals = new ContentValues();
        vals.put("EnergyEnable", Integer.valueOf(model.energyEnable ? 1 : 0));
        vals.put("EnergyPercent", Short.valueOf(model.energyPercent));
        long ret = -1;
        try {
            ret = (long) this.cr.update(Uri.parse(this.factoryCusSchema + "/miscsetting"), vals, null, null);
        } catch (SQLException e) {
            Log.e("DataBaseDeskImpl", "update failed");
        }
        if (ret == -1) {
        }
    }

    public CustomerCfgMiscSetting getCustomerCfgMiscSetting() {
        return this.customerCfgMiscSetting;
    }

    public boolean setCustomerCfgMiscSetting(CustomerCfgMiscSetting miscSetting) {
        this.customerCfgMiscSetting = miscSetting;
        return true;
    }

    public void updateCurCountry(int country) {
        long ret = -1;
        ContentValues vals = new ContentValues();
        vals.put("Country", Integer.valueOf(country));
        try {
            ret = (long) this.cr.update(Uri.parse("content://mstar.tv.usersetting/systemsetting"), vals, null, null);
        } catch (SQLException e) {
        }
        if (ret == -1) {
            System.out.println("update tbl_SystemSetting field Country ignored");
        }
    }

    public void UpdateDB() {
        dataBaseMgrImpl.queryAllVideoPara(this.f0com.GetCurrentInputSource().ordinal());
    }

    public String queryServiceName(short epgTimerIndex) {
        Cursor cursor = getContentResolver().query(Uri.parse("content://mstar.tv.usersetting/epgtimer/" + epgTimerIndex), null, null, null, null);
        cursor.moveToFirst();
        new String();
        String serviceName = cursor.getString(cursor.getColumnIndex("sServiceName"));
        System.out.println("\n=====>>serviceName " + serviceName + " @epgTimerIndex " + epgTimerIndex);
        cursor.close();
        return serviceName;
    }

    public String queryEventName(short epgTimerIndex) {
        Cursor cursor = getContentResolver().query(Uri.parse("content://mstar.tv.usersetting/epgtimer/" + epgTimerIndex), null, null, null, null);
        cursor.moveToFirst();
        new String();
        String eventName = cursor.getString(cursor.getColumnIndex("sEventName"));
        System.out.println("\n=====>>eventName " + eventName + " @epgTimerIndex " + epgTimerIndex);
        cursor.close();
        return eventName;
    }

    public void updateServiceName(String serviceName, short epgTimerIndex) {
        long ret = -1;
        ContentValues vals = new ContentValues();
        System.out.println("\n====>>updateServiceName--serviceName " + serviceName + " @epgTimerIndex " + epgTimerIndex);
        vals.put("sServiceName", serviceName);
        try {
            ret = (long) getContentResolver().update(Uri.parse("content://mstar.tv.usersetting/epgtimer/" + epgTimerIndex), vals, null, null);
        } catch (SQLException e) {
        }
        if (ret == -1) {
            System.out.println("update tbl_EpgTimer field sServiceName ignored");
        }
        try {
            if (TvManager.getInstance() != null) {
                TvManager.getInstance().getDatabaseManager().setDatabaseDirtyByApplication(7);
            }
        } catch (TvCommonException e2) {
            e2.printStackTrace();
        }
    }

    public void updateEventName(String eventName, short epgTimerIndex) {
        long ret = -1;
        ContentValues vals = new ContentValues();
        System.out.println("\n====>>updateEventName--eventName " + eventName + " @epgTimerIndex " + epgTimerIndex);
        vals.put("sEventName", eventName);
        try {
            ret = (long) getContentResolver().update(Uri.parse("content://mstar.tv.usersetting/epgtimer/" + epgTimerIndex), vals, null, null);
        } catch (SQLException e) {
        }
        if (ret == -1) {
            System.out.println("update tbl_EpgTimer field sEventName ignored");
        }
        try {
            if (TvManager.getInstance() != null) {
                TvManager.getInstance().getDatabaseManager().setDatabaseDirtyByApplication(7);
            }
        } catch (TvCommonException e2) {
            e2.printStackTrace();
        }
    }

    public SPDIF_TYPE getSpdifMode() {
        this.stUsrData = queryUserSysSetting();
        return this.stUsrData.enSPDIFMODE;
    }

    public void setSpdifMode(SPDIF_TYPE mode) {
        this.stUsrData = queryUserSysSetting();
        this.stUsrData.enSPDIFMODE = mode;
        updateUserSysSetting(this.stUsrData);
    }

    public int querySourceIdent() {
        Cursor cursor = getContentResolver().query(Uri.parse("content://mstar.tv.usersetting/systemsetting"), null, null, null, null);
        int result = 0;
        if (cursor.moveToFirst()) {
            result = cursor.getInt(cursor.getColumnIndex("bSourceDetectEnable"));
        }
        cursor.close();
        return result;
    }

    public void updateSourceIdent(short curStatue) {
        int ret = -1;
        ContentValues vals = new ContentValues();
        vals.put("bSourceDetectEnable", Short.valueOf(curStatue));
        try {
            ret = getContentResolver().update(Uri.parse("content://mstar.tv.usersetting/systemsetting"), vals, null, null);
        } catch (SQLException e) {
        }
        if (ret == -1) {
            System.out.println("update tbl_SystemSetting ignored");
        }
        try {
            if (TvManager.getInstance() != null) {
                TvManager.getInstance().getDatabaseManager().setDatabaseDirtyByApplication(25);
            }
        } catch (TvCommonException e2) {
            e2.printStackTrace();
        }
    }

    public int querySourceSwit() {
        Cursor cursor = getContentResolver().query(Uri.parse("content://mstar.tv.usersetting/systemsetting"), null, null, null, null);
        int result = 0;
        if (cursor.moveToFirst()) {
            result = cursor.getInt(cursor.getColumnIndex("bAutoSourceSwitch"));
        }
        cursor.close();
        return result;
    }

    public void updateSourceSwit(short curStatue) {
        int ret = -1;
        ContentValues vals = new ContentValues();
        vals.put("bAutoSourceSwitch", Short.valueOf(curStatue));
        try {
            ret = getContentResolver().update(Uri.parse("content://mstar.tv.usersetting/systemsetting"), vals, null, null);
        } catch (SQLException e) {
        }
        if (ret == -1) {
            System.out.println("update tbl_SystemSetting ignored");
        }
        try {
            if (TvManager.getInstance() != null) {
                TvManager.getInstance().getDatabaseManager().setDatabaseDirtyByApplication(25);
            }
        } catch (TvCommonException e2) {
            e2.printStackTrace();
        }
    }

    public int queryAutoMHLSwitch() {
        Cursor cursor = getContentResolver().query(Uri.parse("content://mstar.tv.usersetting/systemsetting"), null, null, null, null);
        int result = 0;
        if (cursor.moveToFirst()) {
            result = cursor.getInt(cursor.getColumnIndex("bAutoMHLSwitch"));
        }
        cursor.close();
        return result;
    }

    public void updateAutoMHLSwitch(short curStatue) {
        Log.d("Charles", "updateAutoMHLSwitch curStatue : " + curStatue);
        int ret = -1;
        ContentValues vals = new ContentValues();
        vals.put("bAutoMHLSwitch", Short.valueOf(curStatue));
        try {
            ret = getContentResolver().update(Uri.parse("content://mstar.tv.usersetting/systemsetting"), vals, null, null);
        } catch (SQLException e) {
        }
        if (ret == -1) {
            System.out.println("update tbl_SystemSetting ignored");
        }
        try {
            if (TvManager.getInstance() != null) {
                TvManager.getInstance().getDatabaseManager().setDatabaseDirtyByApplication(25);
            }
        } catch (TvCommonException e2) {
            e2.printStackTrace();
        }
    }

    public int queryColorTemp(int inputSrcType, int picModeIdx) {
        Cursor cursorPicMode = this.cr.query(Uri.parse(this.userSettingSchema + "/picmode_setting/inputsrc/" + inputSrcType + "/picmode/" + picModeIdx), null, null, null, "PictureModeType");
        cursorPicMode.moveToFirst();
        int value = cursorPicMode.getInt(cursorPicMode.getColumnIndex("eColorTemp"));
        cursorPicMode.close();
        return value;
    }
}
