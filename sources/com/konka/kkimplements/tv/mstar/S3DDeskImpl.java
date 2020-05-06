package com.konka.kkimplements.tv.mstar;

import android.content.Context;
import com.konka.kkinterface.tv.CommonDesk;
import com.konka.kkinterface.tv.DataBaseDesk.EN_ThreeD_Video_3DDEPTH;
import com.konka.kkinterface.tv.DataBaseDesk.EN_ThreeD_Video_3DOFFSET;
import com.konka.kkinterface.tv.DataBaseDesk.EN_ThreeD_Video_3DOUTPUTASPECT;
import com.konka.kkinterface.tv.DataBaseDesk.EN_ThreeD_Video_3DTO2D;
import com.konka.kkinterface.tv.DataBaseDesk.EN_ThreeD_Video_AUTOSTART;
import com.konka.kkinterface.tv.DataBaseDesk.EN_ThreeD_Video_DISPLAYFORMAT;
import com.konka.kkinterface.tv.DataBaseDesk.EN_ThreeD_Video_LRVIEWSWITCH;
import com.konka.kkinterface.tv.DataBaseDesk.EN_ThreeD_Video_SELFADAPTIVE_DETECT;
import com.konka.kkinterface.tv.DataBaseDesk.ThreeD_Video_MODE;
import com.konka.kkinterface.tv.S3DDesk;
import com.mstar.android.tvapi.common.TvManager;
import com.mstar.android.tvapi.common.exception.TvCommonException;
import com.mstar.android.tvapi.common.vo.Enum3dAspectRatioType;
import com.mstar.android.tvapi.common.vo.Enum3dType;
import com.mstar.android.tvapi.common.vo.EnumScalerWindow;
import com.mstar.android.tvapi.common.vo.EnumScreenMuteType;
import com.mstar.android.tvapi.common.vo.TvOsType.EnumInputSource;

public class S3DDeskImpl extends BaseDeskImpl implements S3DDesk {
    private static S3DDeskImpl s3DMgrImpl;
    private ThreeD_Video_MODE ThreeDSetting = null;

    /* renamed from: com reason: collision with root package name */
    private CommonDesk f4com = null;
    private Context context;
    private DataBaseDeskImpl databaseMgrImpl = null;

    private S3DDeskImpl(Context context2) {
        this.f4com = CommonDeskImpl.getInstance(context2);
        this.f4com.printfI("TvService", "S3DManagerImpl constructor!!");
        this.databaseMgrImpl = DataBaseDeskImpl.getDataBaseMgrInstance(context2);
        this.ThreeDSetting = this.databaseMgrImpl.getVideo().ThreeDVideoMode;
    }

    public static S3DDeskImpl getS3DMgrInstance(Context context2) {
        if (s3DMgrImpl == null) {
            s3DMgrImpl = new S3DDeskImpl(context2);
        }
        return s3DMgrImpl;
    }

    public boolean setSelfAdaptiveDetect(EN_ThreeD_Video_SELFADAPTIVE_DETECT selfAdaptiveDetect) {
        if (selfAdaptiveDetect == EN_ThreeD_Video_SELFADAPTIVE_DETECT.DB_ThreeD_Video_SELF_ADAPTIVE_DETECT_OFF) {
            this.ThreeDSetting.eThreeDVideoSelfAdaptiveDetect = selfAdaptiveDetect;
            this.ThreeDSetting.eThreeDVideoDisplayFormat = EN_ThreeD_Video_DISPLAYFORMAT.DB_ThreeD_Video_DISPLAYFORMAT_NONE;
        } else {
            this.ThreeDSetting.eThreeDVideoSelfAdaptiveDetect = EN_ThreeD_Video_SELFADAPTIVE_DETECT.DB_ThreeD_Video_SELF_ADAPTIVE_DETECT_WHEN_SOURCE_CHANGE;
            this.ThreeDSetting.eThreeDVideoDisplayFormat = EN_ThreeD_Video_DISPLAYFORMAT.DB_ThreeD_Video_DISPLAYFORMAT_AUTO;
        }
        this.databaseMgrImpl.updateVideo3DAdaptiveDetectMode(this.ThreeDSetting.eThreeDVideoSelfAdaptiveDetect, EnumInputSource.E_INPUT_SOURCE_HDMI.ordinal());
        this.databaseMgrImpl.updateVideo3DDisplayFormat(this.ThreeDSetting.eThreeDVideoDisplayFormat, this.f4com.GetCurrentInputSource().ordinal());
        try {
            if (TvManager.getInstance() != null) {
                TvManager.getInstance().setVideoMute(true, EnumScreenMuteType.E_BLACK, 0, this.f4com.GetCurrentInputSource());
            }
        } catch (TvCommonException e) {
            this.f4com.printfE("S3DDeskImpl", "setVideoMute False Exception");
            e.printStackTrace();
        }
        if (selfAdaptiveDetect == EN_ThreeD_Video_SELFADAPTIVE_DETECT.DB_ThreeD_Video_SELF_ADAPTIVE_DETECT_WHEN_SOURCE_CHANGE) {
            try {
                if (TvManager.getInstance() != null) {
                    TvManager.getInstance().getThreeDimensionManager().set3dFormatDetectFlag(true);
                }
            } catch (TvCommonException e2) {
                this.f4com.printfE("S3DDeskImpl", "Set set3dFormatDetectFlag True Exception");
                e2.printStackTrace();
            }
        } else if (selfAdaptiveDetect != EN_ThreeD_Video_SELFADAPTIVE_DETECT.DB_ThreeD_Video_SELF_ADAPTIVE_DETECT_RIGHT_NOW) {
            try {
                if (TvManager.getInstance() != null) {
                    TvManager.getInstance().getThreeDimensionManager().set3dFormatDetectFlag(false);
                    TvManager.getInstance().getThreeDimensionManager().enable3d(Enum3dType.EN_3D_NONE);
                }
            } catch (TvCommonException e3) {
                this.f4com.printfE("S3DDeskImpl", "Set set3dFormatDetectFlag False Exception");
                e3.printStackTrace();
            }
        } else if (this.f4com.isSignalStable()) {
            this.f4com.printfE("S3DDeskImpl", "Signal Stable!!!");
            try {
                if (TvManager.getInstance() != null) {
                    Enum3dType _3dtype = TvManager.getInstance().getThreeDimensionManager().detect3dFormat(EnumScalerWindow.E_MAIN_WINDOW);
                    this.f4com.printfE("S3DDeskImpl", "Detect 3D formate is" + _3dtype);
                    TvManager.getInstance().getThreeDimensionManager().enable3d(_3dtype);
                    TvManager.getInstance().getThreeDimensionManager().set3dFormatDetectFlag(true);
                }
            } catch (TvCommonException e4) {
                this.f4com.printfE("S3DDeskImpl", "Set set3dFormatDetectFlag True Exception");
                e4.printStackTrace();
            }
        } else {
            this.f4com.printfE("S3DDeskImpl", "Signal Not Stable!!!");
            try {
                if (TvManager.getInstance() != null) {
                    TvManager.getInstance().getThreeDimensionManager().set3dFormatDetectFlag(true);
                }
            } catch (TvCommonException e5) {
                this.f4com.printfE("S3DDeskImpl", "Set set3dFormatDetectFlag True Exception");
                e5.printStackTrace();
            }
        }
        try {
            if (TvManager.getInstance() != null) {
                TvManager.getInstance().setVideoMute(false, EnumScreenMuteType.E_BLACK, 0, this.f4com.GetCurrentInputSource());
            }
        } catch (TvCommonException e6) {
            this.f4com.printfE("S3DDeskImpl", "setVideoMute False Exception");
            e6.printStackTrace();
        }
        return true;
    }

    public EN_ThreeD_Video_SELFADAPTIVE_DETECT getSelfAdaptiveDetect() {
        return this.ThreeDSetting.eThreeDVideoSelfAdaptiveDetect;
    }

    public boolean setDisplayFormat(EN_ThreeD_Video_DISPLAYFORMAT displayFormat) {
        this.ThreeDSetting.eThreeDVideoDisplayFormat = displayFormat;
        this.databaseMgrImpl.updateVideo3DMode(this.ThreeDSetting, this.f4com.GetCurrentInputSource().ordinal());
        this.f4com.printfI("TvS3DManagerIml", "Display format is " + this.ThreeDSetting.eThreeDVideoDisplayFormat);
        try {
            if (TvManager.getInstance() != null) {
                TvManager.getInstance().setVideoMute(true, EnumScreenMuteType.E_BLACK, 0, this.f4com.GetCurrentInputSource());
            }
        } catch (TvCommonException e) {
            this.f4com.printfE("S3DDeskImpl", "setVideoMute False Exception");
            e.printStackTrace();
        }
        if (displayFormat == EN_ThreeD_Video_DISPLAYFORMAT.DB_ThreeD_Video_DISPLAYFORMAT_SIDE_BY_SIDE) {
            try {
                if (TvManager.getInstance() != null) {
                    TvManager.getInstance().getThreeDimensionManager().enable3d(Enum3dType.EN_3D_SIDE_BY_SIDE_HALF);
                }
            } catch (TvCommonException e2) {
                this.f4com.printfE("S3DDeskImpl", "setDisplayFormat False Exception");
                e2.printStackTrace();
            }
        } else if (displayFormat == EN_ThreeD_Video_DISPLAYFORMAT.DB_ThreeD_Video_DISPLAYFORMAT_TOP_BOTTOM) {
            try {
                if (TvManager.getInstance() != null) {
                    TvManager.getInstance().getThreeDimensionManager().enable3d(Enum3dType.EN_3D_TOP_BOTTOM);
                }
            } catch (TvCommonException e3) {
                this.f4com.printfE("S3DDeskImpl", "setDisplayFormat False Exception");
                e3.printStackTrace();
            }
        } else if (displayFormat == EN_ThreeD_Video_DISPLAYFORMAT.DB_ThreeD_Video_DISPLAYFORMAT_FRAME_PACKING) {
            try {
                if (TvManager.getInstance() != null) {
                    TvManager.getInstance().getThreeDimensionManager().enable3d(Enum3dType.EN_3D_FRAME_PACKING_1080P);
                }
            } catch (TvCommonException e4) {
                this.f4com.printfE("S3DDeskImpl", "setDisplayFormat False Exception");
                e4.printStackTrace();
            }
        } else if (displayFormat == EN_ThreeD_Video_DISPLAYFORMAT.DB_ThreeD_Video_DISPLAYFORMAT_LINE_ALTERNATIVE) {
            try {
                if (TvManager.getInstance() != null) {
                    TvManager.getInstance().getThreeDimensionManager().enable3d(Enum3dType.EN_3D_LINE_ALTERNATIVE);
                }
            } catch (TvCommonException e5) {
                this.f4com.printfE("S3DDeskImpl", "setDisplayFormat False Exception");
                e5.printStackTrace();
            }
        } else if (displayFormat == EN_ThreeD_Video_DISPLAYFORMAT.DB_ThreeD_Video_DISPLAYFORMAT_2DTO3D) {
            try {
                if (TvManager.getInstance() != null) {
                    TvManager.getInstance().getThreeDimensionManager().enable3d(Enum3dType.EN_3D_2DTO3D);
                    TvManager.getInstance().getThreeDimensionManager().set3dGain(get3DDepthMode());
                    TvManager.getInstance().getThreeDimensionManager().set3dOffset(get3DOffsetMode());
                }
            } catch (TvCommonException e6) {
                this.f4com.printfE("S3DDeskImpl", "setDisplayFormat False Exception");
                e6.printStackTrace();
            }
        } else if (displayFormat == EN_ThreeD_Video_DISPLAYFORMAT.DB_ThreeD_Video_DISPLAYFORMAT_AUTO) {
            if (this.f4com.isSignalStable()) {
                this.f4com.printfE("S3DDeskImpl", "Signal Stable!!!");
                try {
                    if (TvManager.getInstance() != null) {
                        TvManager.getInstance().getThreeDimensionManager().enable3d(Enum3dType.EN_3D_NONE);
                    }
                } catch (TvCommonException e7) {
                    this.f4com.printfE("S3DDeskImpl", "setDisplayFormat False Exception");
                    e7.printStackTrace();
                }
                try {
                    if (TvManager.getInstance() != null) {
                        Enum3dType _3dtype = TvManager.getInstance().getThreeDimensionManager().detect3dFormat(EnumScalerWindow.E_MAIN_WINDOW);
                        this.f4com.printfE("S3DDeskImpl", "Detect 3D formate is" + _3dtype);
                        TvManager.getInstance().getThreeDimensionManager().enable3d(_3dtype);
                    }
                } catch (TvCommonException e8) {
                    this.f4com.printfE("S3DDeskImpl", "setDisplayFormat False Exception");
                    e8.printStackTrace();
                }
            } else {
                this.f4com.printfE("S3DDeskImpl", "Signal Not Stable!!!");
            }
        } else if (displayFormat == EN_ThreeD_Video_DISPLAYFORMAT.DB_ThreeD_Video_DISPLAYFORMAT_CHECK_BOARD) {
            try {
                if (TvManager.getInstance() != null) {
                    TvManager.getInstance().getThreeDimensionManager().enable3d(Enum3dType.EN_3D_CHECK_BORAD);
                }
            } catch (TvCommonException e9) {
                this.f4com.printfE("S3DDeskImpl", "setDisplayFormat False Exception");
                e9.printStackTrace();
            }
        } else if (displayFormat == EN_ThreeD_Video_DISPLAYFORMAT.DB_ThreeD_Video_DISPLAYFORMAT_PIXEL_ALTERNATIVE) {
            try {
                if (TvManager.getInstance() != null) {
                    TvManager.getInstance().getThreeDimensionManager().enable3d(Enum3dType.EN_3D_PIXEL_ALTERNATIVE);
                }
            } catch (TvCommonException e10) {
                this.f4com.printfE("S3DDeskImpl", "setDisplayFormat False Exception");
                e10.printStackTrace();
            }
        } else {
            try {
                if (TvManager.getInstance() != null) {
                    TvManager.getInstance().getThreeDimensionManager().enable3d(Enum3dType.EN_3D_NONE);
                }
            } catch (TvCommonException e11) {
                this.f4com.printfE("S3DDeskImpl", "setDisplayFormat False Exception");
                e11.printStackTrace();
            }
        }
        try {
            if (TvManager.getInstance() != null) {
                TvManager.getInstance().setVideoMute(false, EnumScreenMuteType.E_BLACK, 0, this.f4com.GetCurrentInputSource());
            }
        } catch (TvCommonException e12) {
            this.f4com.printfE("S3DDeskImpl", "setVideoMute False Exception");
            e12.printStackTrace();
        }
        return true;
    }

    public boolean setVideoScaleforKTV() {
        try {
            if (TvManager.getInstance() != null) {
                TvManager.getInstance().getThreeDimensionManager().enable3d(Enum3dType.EN_3D_NONE);
            }
        } catch (TvCommonException e) {
            this.f4com.printfE("S3DDeskImpl", "setDisplayFormat False Exception");
            e.printStackTrace();
        }
        return true;
    }

    public EN_ThreeD_Video_DISPLAYFORMAT getDisplayFormat() {
        if (this.ThreeDSetting.eThreeDVideoSelfAdaptiveDetect == EN_ThreeD_Video_SELFADAPTIVE_DETECT.DB_ThreeD_Video_SELF_ADAPTIVE_DETECT_OFF) {
            this.ThreeDSetting.eThreeDVideoDisplayFormat = this.databaseMgrImpl.queryThreeDVideoDisplayFormat(this.f4com.GetCurrentInputSource().ordinal());
        } else {
            this.ThreeDSetting.eThreeDVideoDisplayFormat = EN_ThreeD_Video_DISPLAYFORMAT.DB_ThreeD_Video_DISPLAYFORMAT_AUTO;
        }
        return this.ThreeDSetting.eThreeDVideoDisplayFormat;
    }

    public boolean set3DTo2D(EN_ThreeD_Video_3DTO2D display3dto2dMode) {
        this.ThreeDSetting.eThreeDVideo3DTo2D = display3dto2dMode;
        this.databaseMgrImpl.updateVideo3DMode(this.ThreeDSetting, this.f4com.GetCurrentInputSource().ordinal());
        this.f4com.printfI("TvS3DManagerIml", "Display 3DTo2D Mode is " + this.ThreeDSetting.eThreeDVideo3DTo2D);
        try {
            if (TvManager.getInstance() != null) {
                TvManager.getInstance().setVideoMute(true, EnumScreenMuteType.E_BLACK, 0, this.f4com.GetCurrentInputSource());
            }
        } catch (TvCommonException e) {
            this.f4com.printfE("S3DDeskImpl", "setVideoMute False Exception");
            e.printStackTrace();
        }
        if (display3dto2dMode == EN_ThreeD_Video_3DTO2D.DB_ThreeD_Video_3DTO2D_SIDE_BY_SIDE) {
            try {
                if (TvManager.getInstance() != null) {
                    TvManager.getInstance().getThreeDimensionManager().enable3dTo2d(Enum3dType.EN_3D_SIDE_BY_SIDE_HALF);
                }
            } catch (TvCommonException e2) {
                this.f4com.printfE("S3DDeskImpl", "Set set3DTo2D False Exception");
                e2.printStackTrace();
            }
        } else if (display3dto2dMode == EN_ThreeD_Video_3DTO2D.DB_ThreeD_Video_3DTO2D_TOP_BOTTOM) {
            try {
                if (TvManager.getInstance() != null) {
                    TvManager.getInstance().getThreeDimensionManager().enable3dTo2d(Enum3dType.EN_3D_TOP_BOTTOM);
                }
            } catch (TvCommonException e3) {
                this.f4com.printfE("S3DDeskImpl", "Set set3DTo2D False Exception");
                e3.printStackTrace();
            }
        } else if (display3dto2dMode == EN_ThreeD_Video_3DTO2D.DB_ThreeD_Video_3DTO2D_FRAME_PACKING) {
            try {
                if (TvManager.getInstance() != null) {
                    TvManager.getInstance().getThreeDimensionManager().enable3dTo2d(Enum3dType.EN_3D_FRAME_PACKING_1080P);
                }
            } catch (TvCommonException e4) {
                this.f4com.printfE("S3DDeskImpl", "Set set3DTo2D False Exception");
                e4.printStackTrace();
            }
        } else if (display3dto2dMode == EN_ThreeD_Video_3DTO2D.DB_ThreeD_Video_3DTO2D_LINE_ALTERNATIVE) {
            try {
                if (TvManager.getInstance() != null) {
                    TvManager.getInstance().getThreeDimensionManager().enable3dTo2d(Enum3dType.EN_3D_LINE_ALTERNATIVE);
                }
            } catch (TvCommonException e5) {
                this.f4com.printfE("S3DDeskImpl", "Set set3DTo2D False Exception");
                e5.printStackTrace();
            }
        } else if (display3dto2dMode != EN_ThreeD_Video_3DTO2D.DB_ThreeD_Video_3DTO2D_AUTO) {
            try {
                if (TvManager.getInstance() != null) {
                    TvManager.getInstance().getThreeDimensionManager().enable3dTo2d(Enum3dType.EN_3D_NONE);
                }
            } catch (TvCommonException e6) {
                this.f4com.printfE("S3DDeskImpl", "Set set3DTo2D False Exception");
                e6.printStackTrace();
            }
        } else if (this.f4com.isSignalStable()) {
            this.f4com.printfE("S3DDeskImpl", "Signal Stable!!!");
            try {
                if (TvManager.getInstance() != null) {
                    Enum3dType _3dtype = TvManager.getInstance().getThreeDimensionManager().detect3dFormat(EnumScalerWindow.E_MAIN_WINDOW);
                    this.f4com.printfE("S3DDeskImpl", "Detect 3D formate is" + _3dtype);
                    TvManager.getInstance().getThreeDimensionManager().enable3dTo2d(_3dtype);
                }
            } catch (TvCommonException e7) {
                this.f4com.printfE("S3DDeskImpl", "Set set3dFormatDetectFlag True Exception");
                e7.printStackTrace();
            }
        } else {
            this.f4com.printfE("S3DDeskImpl", "Signal Not Stable!!!");
        }
        try {
            if (TvManager.getInstance() != null) {
                TvManager.getInstance().setVideoMute(false, EnumScreenMuteType.E_BLACK, 0, this.f4com.GetCurrentInputSource());
            }
        } catch (TvCommonException e8) {
            this.f4com.printfE("S3DDeskImpl", "setVideoMute False Exception");
            e8.printStackTrace();
        }
        return true;
    }

    public EN_ThreeD_Video_3DTO2D getDisplay3DTo2DMode() {
        return this.ThreeDSetting.eThreeDVideo3DTo2D;
    }

    public boolean set3DDepthMode(int mode3DDepth) {
        this.ThreeDSetting.eThreeDVideo3DDepth = EN_ThreeD_Video_3DDEPTH.values()[mode3DDepth];
        this.databaseMgrImpl.updateVideo3DMode(this.ThreeDSetting, this.f4com.GetCurrentInputSource().ordinal());
        this.f4com.printfI("TvS3DManagerIml", "3D Depth mode is " + this.ThreeDSetting.eThreeDVideo3DDepth);
        try {
            if (TvManager.getInstance() != null) {
                TvManager.getInstance().getThreeDimensionManager().set3dGain(mode3DDepth);
            }
        } catch (TvCommonException e) {
            this.f4com.printfE("S3DDeskImpl", "Set set3DDepthMode False Exception");
            e.printStackTrace();
        }
        return true;
    }

    public int get3DDepthMode() {
        return this.ThreeDSetting.eThreeDVideo3DDepth.ordinal();
    }

    public boolean set3DOffsetMode(int mode3DOffset) {
        this.ThreeDSetting.eThreeDVideo3DOffset = EN_ThreeD_Video_3DOFFSET.values()[mode3DOffset];
        this.databaseMgrImpl.updateVideo3DMode(this.ThreeDSetting, this.f4com.GetCurrentInputSource().ordinal());
        this.f4com.printfI("TvS3DManagerIml", "3D Depth mode is " + this.ThreeDSetting.eThreeDVideo3DDepth);
        try {
            if (TvManager.getInstance() != null) {
                TvManager.getInstance().getThreeDimensionManager().set3dOffset(mode3DOffset);
            }
        } catch (TvCommonException e) {
            this.f4com.printfE("S3DDeskImpl", "Set set3DDepthMode False Exception");
            e.printStackTrace();
        }
        return true;
    }

    public int get3DOffsetMode() {
        return this.ThreeDSetting.eThreeDVideo3DOffset.ordinal();
    }

    public boolean setAutoStartMode(EN_ThreeD_Video_AUTOSTART autoStartMode) {
        this.ThreeDSetting.eThreeDVideoAutoStart = autoStartMode;
        this.databaseMgrImpl.updateVideo3DMode(this.ThreeDSetting, this.f4com.GetCurrentInputSource().ordinal());
        this.f4com.printfI("TvS3DManagerIml", "auto start mode is " + this.ThreeDSetting.eThreeDVideoAutoStart);
        return true;
    }

    public EN_ThreeD_Video_AUTOSTART getAutoStartMode() {
        return this.ThreeDSetting.eThreeDVideoAutoStart;
    }

    public boolean set3DOutputAspectMode(EN_ThreeD_Video_3DOUTPUTASPECT outputAspectMode) {
        Enum3dAspectRatioType arType;
        Enum3dAspectRatioType enum3dAspectRatioType = Enum3dAspectRatioType.E_3D_ASPECTRATIO_FULL;
        this.ThreeDSetting.eThreeDVideo3DOutputAspect = outputAspectMode;
        this.databaseMgrImpl.updateVideo3DMode(this.ThreeDSetting, this.f4com.GetCurrentInputSource().ordinal());
        this.f4com.printfI("TvS3DManagerIml", "output aspect mode is " + this.ThreeDSetting.eThreeDVideo3DOutputAspect);
        try {
            if (TvManager.getInstance() != null) {
                TvManager.getInstance().setVideoMute(true, EnumScreenMuteType.E_BLACK, 0, this.f4com.GetCurrentInputSource());
            }
        } catch (TvCommonException e) {
            this.f4com.printfE("S3DDeskImpl", "setVideoMute False Exception");
            e.printStackTrace();
        }
        if (outputAspectMode == EN_ThreeD_Video_3DOUTPUTASPECT.DB_ThreeD_Video_3DOUTPUTASPECT_CENTER) {
            arType = Enum3dAspectRatioType.E_3D_ASPECTRATIO_CENTER;
        } else if (outputAspectMode == EN_ThreeD_Video_3DOUTPUTASPECT.DB_ThreeD_Video_3DOUTPUTASPECT_AUTOADAPTED) {
            arType = Enum3dAspectRatioType.E_3D_ASPECTRATIO_AUTO;
        } else {
            arType = Enum3dAspectRatioType.E_3D_ASPECTRATIO_FULL;
        }
        try {
            if (TvManager.getInstance() != null) {
                TvManager.getInstance().getThreeDimensionManager().set3dArc(arType);
            }
        } catch (TvCommonException e2) {
            this.f4com.printfE("S3DDeskImpl", "Set set3DDepthMode False Exception");
            e2.printStackTrace();
        }
        try {
            if (TvManager.getInstance() != null) {
                TvManager.getInstance().setVideoMute(false, EnumScreenMuteType.E_BLACK, 0, this.f4com.GetCurrentInputSource());
            }
        } catch (TvCommonException e3) {
            this.f4com.printfE("S3DDeskImpl", "setVideoMute False Exception");
            e3.printStackTrace();
        }
        return true;
    }

    public EN_ThreeD_Video_3DOUTPUTASPECT get3DOutputAspectMode() {
        return this.ThreeDSetting.eThreeDVideo3DOutputAspect;
    }

    public boolean setLRViewSwitch(EN_ThreeD_Video_LRVIEWSWITCH LRViewSwitchMode) {
        this.ThreeDSetting.eThreeDVideoLRViewSwitch = LRViewSwitchMode;
        this.databaseMgrImpl.updateVideo3DMode(this.ThreeDSetting, this.f4com.GetCurrentInputSource().ordinal());
        this.f4com.printfV("TvS3DManagerIml", "set View switch is " + this.ThreeDSetting.eThreeDVideoLRViewSwitch);
        try {
            if (this.ThreeDSetting.eThreeDVideoLRViewSwitch == EN_ThreeD_Video_LRVIEWSWITCH.DB_ThreeD_Video_LRVIEWSWITCH_EXCHANGE) {
                if (TvManager.getInstance() != null) {
                    TvManager.getInstance().getThreeDimensionManager().enable3dLrSwitch();
                }
            } else if (TvManager.getInstance() != null) {
                TvManager.getInstance().getThreeDimensionManager().disable3dLrSwitch();
            }
        } catch (TvCommonException e) {
            this.f4com.printfE("S3DDeskImpl", "Get LRViewSwitch False Exception");
            e.printStackTrace();
        }
        return true;
    }

    public EN_ThreeD_Video_LRVIEWSWITCH getLRViewSwitch() {
        boolean bLRSwitch = false;
        try {
            if (TvManager.getInstance() != null) {
                bLRSwitch = TvManager.getInstance().getThreeDimensionManager().is3dLrSwitched();
            }
        } catch (TvCommonException e) {
            this.f4com.printfE("S3DDeskImpl", "Get LRViewSwitch False Exception");
            e.printStackTrace();
        }
        if (bLRSwitch) {
            this.ThreeDSetting.eThreeDVideoLRViewSwitch = EN_ThreeD_Video_LRVIEWSWITCH.DB_ThreeD_Video_LRVIEWSWITCH_EXCHANGE;
        } else {
            this.ThreeDSetting.eThreeDVideoLRViewSwitch = EN_ThreeD_Video_LRVIEWSWITCH.DB_ThreeD_Video_LRVIEWSWITCH_NOTEXCHANGE;
        }
        return this.ThreeDSetting.eThreeDVideoLRViewSwitch;
    }

    public void enableLow3dQuality() {
        try {
            if (TvManager.getInstance() != null) {
                TvManager.getInstance().getThreeDimensionManager().enableLow3dQuality();
            }
        } catch (TvCommonException e) {
            e.printStackTrace();
        }
    }

    public void disableLow3dQuality() {
        try {
            if (TvManager.getInstance() != null) {
                TvManager.getInstance().getThreeDimensionManager().disableLow3dQuality();
            }
        } catch (TvCommonException e) {
            e.printStackTrace();
        }
    }
}
