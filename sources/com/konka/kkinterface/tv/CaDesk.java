package com.konka.kkinterface.tv;

import com.mstar.android.tvapi.common.exception.TvCommonException;
import com.mstar.android.tvapi.dtv.common.CaManager.OnCaEventListener;
import com.mstar.android.tvapi.dtv.vo.CACardSNInfo;
import com.mstar.android.tvapi.dtv.vo.CARatingInfo;
import com.mstar.android.tvapi.dtv.vo.CaACListInfo;
import com.mstar.android.tvapi.dtv.vo.CaDetitleChkNums;
import com.mstar.android.tvapi.dtv.vo.CaEmailContentInfo;
import com.mstar.android.tvapi.dtv.vo.CaEmailHeadInfo;
import com.mstar.android.tvapi.dtv.vo.CaEmailHeadsInfo;
import com.mstar.android.tvapi.dtv.vo.CaEmailSpaceInfo;
import com.mstar.android.tvapi.dtv.vo.CaEntitleIDs;
import com.mstar.android.tvapi.dtv.vo.CaFeedDataInfo;
import com.mstar.android.tvapi.dtv.vo.CaIPPVProgramInfos;
import com.mstar.android.tvapi.dtv.vo.CaOperatorChildStatus;
import com.mstar.android.tvapi.dtv.vo.CaOperatorIds;
import com.mstar.android.tvapi.dtv.vo.CaOperatorInfo;
import com.mstar.android.tvapi.dtv.vo.CaServiceEntitles;
import com.mstar.android.tvapi.dtv.vo.CaSlotIDs;
import com.mstar.android.tvapi.dtv.vo.CaSlotInfo;
import com.mstar.android.tvapi.dtv.vo.CaStopIPPVBuyDlgInfo;
import com.mstar.android.tvapi.dtv.vo.CaWorkTimeInfo;

public interface CaDesk extends BaseDesk {
    short CaChangePin(String str, String str2) throws TvCommonException;

    boolean CaDelDetitleChkNum(short s, int i) throws TvCommonException;

    void CaDelEmail(int i) throws TvCommonException;

    CaACListInfo CaGetACList(short s) throws TvCommonException;

    CACardSNInfo CaGetCardSN() throws TvCommonException;

    CaDetitleChkNums CaGetDetitleChkNums(short s) throws TvCommonException;

    boolean CaGetDetitleReaded(short s) throws TvCommonException;

    CaEmailContentInfo CaGetEmailContent(int i) throws TvCommonException;

    CaEmailHeadInfo CaGetEmailHead(int i) throws TvCommonException;

    CaEmailHeadsInfo CaGetEmailHeads(short s, short s2) throws TvCommonException;

    CaEmailSpaceInfo CaGetEmailSpaceInfo() throws TvCommonException;

    CaEntitleIDs CaGetEntitleIDs(short s) throws TvCommonException;

    CaIPPVProgramInfos CaGetIPPVProgram(short s) throws TvCommonException;

    CaOperatorChildStatus CaGetOperatorChildStatus(short s) throws TvCommonException;

    CaOperatorIds CaGetOperatorIds() throws TvCommonException;

    CaOperatorInfo CaGetOperatorInfo(short s) throws TvCommonException;

    short CaGetPlatformID() throws TvCommonException;

    CARatingInfo CaGetRating() throws TvCommonException;

    CaServiceEntitles CaGetServiceEntitles(short s) throws TvCommonException;

    CaSlotIDs CaGetSlotIDs(short s) throws TvCommonException;

    CaSlotInfo CaGetSlotInfo(short s, short s2) throws TvCommonException;

    int CaGetVer() throws TvCommonException;

    CaWorkTimeInfo CaGetWorkTime() throws TvCommonException;

    short CaIsPaired(short s, String str) throws TvCommonException;

    boolean CaOTAStateConfirm(int i, int i2) throws TvCommonException;

    CaFeedDataInfo CaReadFeedDataFromParent(short s) throws TvCommonException;

    void CaRefreshInterface() throws TvCommonException;

    short CaSetRating(String str, short s) throws TvCommonException;

    short CaSetWorkTime(String str, CaWorkTimeInfo caWorkTimeInfo) throws TvCommonException;

    short CaStopIPPVBuyDlg(CaStopIPPVBuyDlgInfo caStopIPPVBuyDlgInfo) throws TvCommonException;

    short CaWriteFeedDataToChild(short s, CaFeedDataInfo caFeedDataInfo) throws TvCommonException;

    int getCurrentEvent();

    int getCurrentMsgType();

    void setCurrentEvent(int i);

    void setCurrentMsgType(int i);

    void setOnCaEventListener(OnCaEventListener onCaEventListener);
}
