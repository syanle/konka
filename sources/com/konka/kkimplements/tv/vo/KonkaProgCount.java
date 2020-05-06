package com.konka.kkimplements.tv.vo;

import com.mstar.android.tvapi.common.vo.EnumProgramCountType;
import com.mstar.android.tvapi.common.vo.EnumServiceType;

public class KonkaProgCount {
    private static /* synthetic */ int[] $SWITCH_TABLE$com$mstar$android$tvapi$common$vo$EnumProgramCountType;
    private static /* synthetic */ int[] $SWITCH_TABLE$com$mstar$android$tvapi$common$vo$EnumServiceType;
    public int DTVAllProgCount;
    public int DTVDataProgCount;
    public int DTVRadioProgCount;
    public int DTVTvProgCount;

    static /* synthetic */ int[] $SWITCH_TABLE$com$mstar$android$tvapi$common$vo$EnumProgramCountType() {
        int[] iArr = $SWITCH_TABLE$com$mstar$android$tvapi$common$vo$EnumProgramCountType;
        if (iArr == null) {
            iArr = new int[EnumProgramCountType.values().length];
            try {
                iArr[EnumProgramCountType.E_COUNT_ATV.ordinal()] = 2;
            } catch (NoSuchFieldError e) {
            }
            try {
                iArr[EnumProgramCountType.E_COUNT_ATV_DTV.ordinal()] = 1;
            } catch (NoSuchFieldError e2) {
            }
            try {
                iArr[EnumProgramCountType.E_COUNT_DTV.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                iArr[EnumProgramCountType.E_COUNT_DTV_DATA.ordinal()] = 6;
            } catch (NoSuchFieldError e4) {
            }
            try {
                iArr[EnumProgramCountType.E_COUNT_DTV_RADIO.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            try {
                iArr[EnumProgramCountType.E_COUNT_DTV_TV.ordinal()] = 4;
            } catch (NoSuchFieldError e6) {
            }
            try {
                iArr[EnumProgramCountType.E_COUNT_TYPE_MAX.ordinal()] = 7;
            } catch (NoSuchFieldError e7) {
            }
            $SWITCH_TABLE$com$mstar$android$tvapi$common$vo$EnumProgramCountType = iArr;
        }
        return iArr;
    }

    static /* synthetic */ int[] $SWITCH_TABLE$com$mstar$android$tvapi$common$vo$EnumServiceType() {
        int[] iArr = $SWITCH_TABLE$com$mstar$android$tvapi$common$vo$EnumServiceType;
        if (iArr == null) {
            iArr = new int[EnumServiceType.values().length];
            try {
                iArr[EnumServiceType.E_SERVICETYPE_ATV.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                iArr[EnumServiceType.E_SERVICETYPE_DATA.ordinal()] = 4;
            } catch (NoSuchFieldError e2) {
            }
            try {
                iArr[EnumServiceType.E_SERVICETYPE_DTV.ordinal()] = 2;
            } catch (NoSuchFieldError e3) {
            }
            try {
                iArr[EnumServiceType.E_SERVICETYPE_INVALID.ordinal()] = 6;
            } catch (NoSuchFieldError e4) {
            }
            try {
                iArr[EnumServiceType.E_SERVICETYPE_RADIO.ordinal()] = 3;
            } catch (NoSuchFieldError e5) {
            }
            try {
                iArr[EnumServiceType.E_SERVICETYPE_UNITED_TV.ordinal()] = 5;
            } catch (NoSuchFieldError e6) {
            }
            $SWITCH_TABLE$com$mstar$android$tvapi$common$vo$EnumServiceType = iArr;
        }
        return iArr;
    }

    public KonkaProgCount() {
        this.DTVTvProgCount = 0;
        this.DTVRadioProgCount = 0;
        this.DTVDataProgCount = 0;
        this.DTVAllProgCount = 0;
        this.DTVTvProgCount = 0;
        this.DTVRadioProgCount = 0;
        this.DTVDataProgCount = 0;
        this.DTVAllProgCount = 0;
    }

    public KonkaProgCount(int iDtv, int iRadio, int iData, int iDTVAll) {
        this.DTVTvProgCount = 0;
        this.DTVRadioProgCount = 0;
        this.DTVDataProgCount = 0;
        this.DTVAllProgCount = 0;
        this.DTVTvProgCount = iDtv;
        this.DTVRadioProgCount = iRadio;
        this.DTVDataProgCount = iData;
        this.DTVAllProgCount = iDTVAll;
    }

    public boolean isNoProgInServiceType(EnumProgramCountType eServiceType) {
        switch ($SWITCH_TABLE$com$mstar$android$tvapi$common$vo$EnumProgramCountType()[eServiceType.ordinal()]) {
            case 4:
                if (this.DTVTvProgCount == 0) {
                    return true;
                }
                return false;
            case 5:
                if (this.DTVRadioProgCount == 0) {
                    return true;
                }
                return false;
            case 6:
                if (this.DTVDataProgCount == 0) {
                    return true;
                }
                return false;
            default:
                if (this.DTVAllProgCount == 0) {
                    return true;
                }
                return false;
        }
    }

    public int getProgCountByServiceType(EnumServiceType eServiceType) {
        switch ($SWITCH_TABLE$com$mstar$android$tvapi$common$vo$EnumServiceType()[eServiceType.ordinal()]) {
            case 2:
                return this.DTVTvProgCount;
            case 3:
                return this.DTVRadioProgCount;
            case 4:
                return this.DTVDataProgCount;
            default:
                return 0;
        }
    }
}
