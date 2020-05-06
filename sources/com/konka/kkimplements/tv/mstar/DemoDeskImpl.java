package com.konka.kkimplements.tv.mstar;

import android.content.Context;
import android.util.Log;
import com.cyanogenmod.trebuchet.Launcher;
import com.konka.kkinterface.tv.ChannelDesk;
import com.konka.kkinterface.tv.DataBaseDesk.EN_MS_NR;
import com.konka.kkinterface.tv.DataBaseDesk.T_MS_VIDEO;
import com.konka.kkinterface.tv.DemoDesk;
import com.konka.kkinterface.tv.DemoDesk.EN_MS_DBC_TYPE;
import com.konka.kkinterface.tv.DemoDesk.EN_MS_DCC_TYPE;
import com.konka.kkinterface.tv.DemoDesk.EN_MS_DLC_TYPE;
import com.konka.kkinterface.tv.DemoDesk.EN_MS_UCLEAR_TYPE;
import com.mstar.android.tvapi.common.PictureManager;
import com.mstar.android.tvapi.common.TvManager;
import com.mstar.android.tvapi.common.exception.TvCommonException;
import com.mstar.android.tvapi.common.vo.MweType.EnumMweType;
import com.mstar.android.tvapi.common.vo.NoiseReduction.EnumNoiseReduction;

public class DemoDeskImpl implements DemoDesk {
    private static final int PBLV1 = 20;
    private static final int PBLV2 = 40;
    private static final int PBLV3 = 60;
    private static final int PBLV4 = 80;
    private static final int PBLV5 = 100;
    private static final int cmvalue1 = 17;
    private static final int cmvalue2 = 48;
    private static final int cmvalue3 = 88;
    private static final int cmvalue4 = 112;
    private static final int cmvalue5 = 133;
    private static final int cmvalue6 = 160;
    private static final int cmvalue7 = 240;
    private static final int cmvalue8 = 255;
    private static DemoDeskImpl demoMgrImpl;
    private static Thread mythread = null;
    /* access modifiers changed from: private */
    public static int sleeptimeCnt = 500;
    /* access modifiers changed from: private */
    public boolean bforceThreadSleep = true;
    private Context context;
    /* access modifiers changed from: private */
    public EN_MS_DBC_TYPE e_DBC_TYPE = EN_MS_DBC_TYPE.E_MS_DBC_OFF;
    /* access modifiers changed from: private */
    public EN_MS_DCC_TYPE e_DCC_TYPE = EN_MS_DCC_TYPE.E_MS_DCC_OFF;
    private EN_MS_DLC_TYPE e_DLC_TYPE = EN_MS_DLC_TYPE.E_MS_DLC_ON;
    private EnumMweType e_MWE_TYPE = EnumMweType.E_OFF;
    private EnumNoiseReduction e_NR_TYPE = EnumNoiseReduction.E_NR_OFF;
    private EN_MS_UCLEAR_TYPE e_UCLEAR_TYPE = EN_MS_UCLEAR_TYPE.E_MS_UCLEAR_ON;
    PictureManager pm = null;

    class MyThread extends Thread {
        MyThread() {
        }

        public void run() {
            while (true) {
                if (DemoDeskImpl.this.bforceThreadSleep) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    if (DemoDeskImpl.this.e_DBC_TYPE == EN_MS_DBC_TYPE.E_MS_DBC_ON) {
                        DemoDeskImpl.this.dbchandler();
                    }
                    if (DemoDeskImpl.this.e_DCC_TYPE == EN_MS_DCC_TYPE.E_MS_DCC_ON) {
                        DemoDeskImpl.this.dcchandler();
                    }
                    try {
                        Thread.sleep((long) DemoDeskImpl.sleeptimeCnt);
                    } catch (InterruptedException e2) {
                        e2.printStackTrace();
                    }
                }
            }
        }
    }

    public static DemoDeskImpl getDemoMgrInstance(Context context2) {
        if (demoMgrImpl == null) {
            demoMgrImpl = new DemoDeskImpl(context2);
        }
        return demoMgrImpl;
    }

    private DemoDeskImpl(Context context2) {
        this.context = context2;
        if (!(TvManager.getInstance() == null || TvManager.getInstance().getPictureManager() == null)) {
            this.pm = TvManager.getInstance().getPictureManager();
        }
        this.e_MWE_TYPE = EnumMweType.E_OFF;
        this.e_DBC_TYPE = EN_MS_DBC_TYPE.E_MS_DBC_OFF;
        this.e_DCC_TYPE = EN_MS_DCC_TYPE.E_MS_DCC_OFF;
        this.e_DLC_TYPE = EN_MS_DLC_TYPE.E_MS_DLC_ON;
        this.e_UCLEAR_TYPE = EN_MS_UCLEAR_TYPE.E_MS_UCLEAR_ON;
        this.e_NR_TYPE = EnumNoiseReduction.E_NR_OFF;
        if (mythread == null) {
            mythread = new MyThread();
            mythread.start();
            this.bforceThreadSleep = false;
        }
    }

    public void setMWEStatus(EnumMweType eMWE) {
        this.e_MWE_TYPE = eMWE;
        try {
            if (TvManager.getInstance() != null && TvManager.getInstance().getPictureManager() != null) {
                TvManager.getInstance().getPictureManager().setDemoMode(eMWE);
            }
        } catch (TvCommonException e) {
            e.printStackTrace();
        }
    }

    public EnumMweType getMWEStatus() {
        try {
            if (!(TvManager.getInstance() == null || TvManager.getInstance().getPictureManager() == null)) {
                this.e_MWE_TYPE = TvManager.getInstance().getPictureManager().getDemoMode();
            }
        } catch (TvCommonException e) {
            e.printStackTrace();
        }
        return this.e_MWE_TYPE;
    }

    public void setDBCStatus(EN_MS_DBC_TYPE eDBC) {
        this.e_DBC_TYPE = eDBC;
        this.bforceThreadSleep = false;
        ReCntSt();
    }

    public EN_MS_DBC_TYPE getDBCStatus() {
        return this.e_DBC_TYPE;
    }

    public void setDLCStatus(EN_MS_DLC_TYPE eDLC) {
        if (eDLC == EN_MS_DLC_TYPE.E_MS_DLC_ON) {
            try {
                if (!(TvManager.getInstance() == null || TvManager.getInstance().getPictureManager() == null)) {
                    TvManager.getInstance().getPictureManager().enableDlc();
                }
            } catch (TvCommonException e) {
                e.printStackTrace();
            }
        } else {
            try {
                if (!(TvManager.getInstance() == null || TvManager.getInstance().getPictureManager() == null)) {
                    TvManager.getInstance().getPictureManager().disableDlc();
                }
            } catch (TvCommonException e2) {
                e2.printStackTrace();
            }
        }
        this.e_DLC_TYPE = eDLC;
    }

    public EN_MS_DLC_TYPE getDLCStatus() {
        return this.e_DLC_TYPE;
    }

    public void setDCCStatus(EN_MS_DCC_TYPE eDCC) {
        Log.d("TvApp", "setDCCStatus:" + eDCC);
        this.e_DCC_TYPE = eDCC;
        this.bforceThreadSleep = false;
        ReCntSt();
    }

    public EN_MS_DCC_TYPE getDCCStatus() {
        return this.e_DCC_TYPE;
    }

    public void setUClearStatus(EN_MS_UCLEAR_TYPE eUClear) {
        if (eUClear == EN_MS_UCLEAR_TYPE.E_MS_UCLEAR_ON) {
            try {
                if (!(TvManager.getInstance() == null || TvManager.getInstance().getPictureManager() == null)) {
                    TvManager.getInstance().getPictureManager().setUltraClear(true);
                }
            } catch (TvCommonException e) {
                e.printStackTrace();
            }
        } else {
            try {
                if (!(TvManager.getInstance() == null || TvManager.getInstance().getPictureManager() == null)) {
                    TvManager.getInstance().getPictureManager().setUltraClear(false);
                }
            } catch (TvCommonException e2) {
                e2.printStackTrace();
            }
        }
        this.e_UCLEAR_TYPE = eUClear;
    }

    public EN_MS_UCLEAR_TYPE getUClearStatus() {
        return this.e_UCLEAR_TYPE;
    }

    public void set3DNR(EnumNoiseReduction eNR) {
        try {
            T_MS_VIDEO stVedioTemp = DataBaseDeskImpl.getDataBaseMgrInstance(this.context).getVideo();
            int ct_index = stVedioTemp.astPicture[stVedioTemp.ePicture.ordinal()].eColorTemp.ordinal();
            stVedioTemp.eNRMode[ct_index].eNR = EN_MS_NR.values()[ct_index];
            if (!(TvManager.getInstance() == null || TvManager.getInstance().getPictureManager() == null)) {
                TvManager.getInstance().getPictureManager().setNoiseReduction(eNR);
            }
            DataBaseDeskImpl.getDataBaseMgrInstance(this.context).updateVideoNRMode(stVedioTemp.eNRMode[ct_index], CommonDeskImpl.getInstance(this.context).GetCurrentInputSource().ordinal(), ct_index);
        } catch (TvCommonException e) {
            e.printStackTrace();
        }
        this.e_NR_TYPE = eNR;
    }

    public EnumNoiseReduction get3DNRStatus() {
        T_MS_VIDEO stVedioTemp = DataBaseDeskImpl.getDataBaseMgrInstance(this.context).getVideo();
        int db_nrmode = stVedioTemp.eNRMode[stVedioTemp.astPicture[stVedioTemp.ePicture.ordinal()].eColorTemp.ordinal()].eNR.ordinal();
        if (db_nrmode < EnumNoiseReduction.E_NR_NUM.getValue()) {
            this.e_NR_TYPE = EnumNoiseReduction.values()[db_nrmode];
        } else {
            Log.e("Tvapp", "get3DNRStatus error:" + db_nrmode);
        }
        return this.e_NR_TYPE;
    }

    private int getrandom() {
        return (int) (Math.random() * 1234.0d);
    }

    private int mapi_GetRealValue(int MaxLVal, int MinLVal, int ADVal, int MaxADVal, int MinADVal) {
        int denominator = MaxADVal - MinADVal;
        if (denominator == 0) {
            denominator = 1;
        }
        return ((ADVal - MinADVal) * (MaxLVal - MinLVal)) / denominator;
    }

    private int mapi_GetImageBackLight() {
        int ucTmp = 0;
        try {
            if (!(TvManager.getInstance() == null || TvManager.getInstance().getPictureManager() == null)) {
                ucTmp = TvManager.getInstance().getPictureManager().getDlcAverageLuma();
            }
            Log.d("TvApp", "getDlcAverageLuma:" + ucTmp);
        } catch (TvCommonException e) {
            e.printStackTrace();
        }
        if (ucTmp < cmvalue1) {
            return 20 - mapi_GetRealValue(PBLV2, 20, ucTmp, cmvalue1, 0);
        }
        if (ucTmp < cmvalue2) {
            return 40 - mapi_GetRealValue(PBLV3, PBLV2, ucTmp, cmvalue2, cmvalue1);
        }
        if (ucTmp < cmvalue3) {
            return 60 - mapi_GetRealValue(80, PBLV3, ucTmp, cmvalue3, cmvalue2);
        }
        if (ucTmp < cmvalue4) {
            return 80 - mapi_GetRealValue(PBLV5, 80, ucTmp, cmvalue4, cmvalue3);
        }
        if (ucTmp < cmvalue5) {
            return mapi_GetRealValue(PBLV5, 80, ucTmp, cmvalue5, cmvalue4) + PBLV5;
        }
        if (ucTmp < cmvalue6) {
            return mapi_GetRealValue(80, PBLV3, ucTmp, cmvalue6, cmvalue6) + 80;
        }
        if (ucTmp < cmvalue7) {
            return mapi_GetRealValue(PBLV3, PBLV2, ucTmp, cmvalue7, cmvalue6) + PBLV3;
        }
        return mapi_GetRealValue(PBLV2, 20, ucTmp, 255, cmvalue7) + PBLV2;
    }

    /* access modifiers changed from: private */
    public void dbchandler() {
        int value = mapi_GetImageBackLight();
        try {
            if (!(TvManager.getInstance() == null || TvManager.getInstance().getPictureManager() == null)) {
                TvManager.getInstance().getPictureManager().setBacklight(value);
            }
        } catch (TvCommonException e) {
            e.printStackTrace();
        }
        Log.d("TvApp", "dbchandler:" + value);
    }

    /* access modifiers changed from: private */
    public void dcchandler() {
        int value = mapi_GetImageBackLight();
        try {
            if (!(TvManager.getInstance() == null || TvManager.getInstance().getPictureManager() == null)) {
                TvManager.getInstance().getPictureManager().setPictureModeContrast((short) value);
            }
        } catch (TvCommonException e) {
            e.printStackTrace();
        }
        Log.d("TvApp", "dcchandler:" + value);
    }

    private void ReCntSt() {
        if (this.e_DBC_TYPE == EN_MS_DBC_TYPE.E_MS_DBC_OFF && this.e_DCC_TYPE == EN_MS_DCC_TYPE.E_MS_DCC_OFF) {
            sleeptimeCnt = Launcher.TIME_DELAY_MUTE_TV_VIDEO;
        } else if (this.e_DBC_TYPE == EN_MS_DBC_TYPE.E_MS_DBC_ON && this.e_DCC_TYPE == EN_MS_DCC_TYPE.E_MS_DCC_ON) {
            sleeptimeCnt = 500;
        } else {
            sleeptimeCnt = ChannelDesk.max_dtv_count;
        }
    }

    public void forceThreadSleep(boolean flag) {
        this.bforceThreadSleep = flag;
    }
}
