package com.konka.kkinterface.tv;

import com.mstar.android.tvapi.common.vo.MweType.EnumMweType;
import com.mstar.android.tvapi.common.vo.NoiseReduction.EnumNoiseReduction;

public interface DemoDesk {

    public enum EN_MS_DBC_TYPE {
        E_MS_DBC_OFF,
        E_MS_DBC_ON
    }

    public enum EN_MS_DCC_TYPE {
        E_MS_DCC_OFF,
        E_MS_DCC_ON
    }

    public enum EN_MS_DLC_TYPE {
        E_MS_DLC_OFF,
        E_MS_DLC_ON
    }

    public enum EN_MS_UCLEAR_TYPE {
        E_MS_UCLEAR_OFF,
        E_MS_UCLEAR_ON
    }

    void forceThreadSleep(boolean z);

    EnumNoiseReduction get3DNRStatus();

    EN_MS_DBC_TYPE getDBCStatus();

    EN_MS_DCC_TYPE getDCCStatus();

    EN_MS_DLC_TYPE getDLCStatus();

    EnumMweType getMWEStatus();

    EN_MS_UCLEAR_TYPE getUClearStatus();

    void set3DNR(EnumNoiseReduction enumNoiseReduction);

    void setDBCStatus(EN_MS_DBC_TYPE en_ms_dbc_type);

    void setDCCStatus(EN_MS_DCC_TYPE en_ms_dcc_type);

    void setDLCStatus(EN_MS_DLC_TYPE en_ms_dlc_type);

    void setMWEStatus(EnumMweType enumMweType);

    void setUClearStatus(EN_MS_UCLEAR_TYPE en_ms_uclear_type);
}
