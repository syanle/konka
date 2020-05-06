package com.konka.kkinterface.tv;

import com.konka.kkinterface.tv.DataBaseDesk.EN_ThreeD_Video_3DOUTPUTASPECT;
import com.konka.kkinterface.tv.DataBaseDesk.EN_ThreeD_Video_3DTO2D;
import com.konka.kkinterface.tv.DataBaseDesk.EN_ThreeD_Video_AUTOSTART;
import com.konka.kkinterface.tv.DataBaseDesk.EN_ThreeD_Video_DISPLAYFORMAT;
import com.konka.kkinterface.tv.DataBaseDesk.EN_ThreeD_Video_LRVIEWSWITCH;
import com.konka.kkinterface.tv.DataBaseDesk.EN_ThreeD_Video_SELFADAPTIVE_DETECT;

public interface S3DDesk extends BaseDesk {
    void disableLow3dQuality();

    void enableLow3dQuality();

    int get3DDepthMode();

    int get3DOffsetMode();

    EN_ThreeD_Video_3DOUTPUTASPECT get3DOutputAspectMode();

    EN_ThreeD_Video_AUTOSTART getAutoStartMode();

    EN_ThreeD_Video_3DTO2D getDisplay3DTo2DMode();

    EN_ThreeD_Video_DISPLAYFORMAT getDisplayFormat();

    EN_ThreeD_Video_LRVIEWSWITCH getLRViewSwitch();

    EN_ThreeD_Video_SELFADAPTIVE_DETECT getSelfAdaptiveDetect();

    boolean set3DDepthMode(int i);

    boolean set3DOffsetMode(int i);

    boolean set3DOutputAspectMode(EN_ThreeD_Video_3DOUTPUTASPECT eN_ThreeD_Video_3DOUTPUTASPECT);

    boolean set3DTo2D(EN_ThreeD_Video_3DTO2D eN_ThreeD_Video_3DTO2D);

    boolean setAutoStartMode(EN_ThreeD_Video_AUTOSTART eN_ThreeD_Video_AUTOSTART);

    boolean setDisplayFormat(EN_ThreeD_Video_DISPLAYFORMAT eN_ThreeD_Video_DISPLAYFORMAT);

    boolean setLRViewSwitch(EN_ThreeD_Video_LRVIEWSWITCH eN_ThreeD_Video_LRVIEWSWITCH);

    boolean setSelfAdaptiveDetect(EN_ThreeD_Video_SELFADAPTIVE_DETECT eN_ThreeD_Video_SELFADAPTIVE_DETECT);

    boolean setVideoScaleforKTV();
}
