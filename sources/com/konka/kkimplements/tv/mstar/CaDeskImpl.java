package com.konka.kkimplements.tv.mstar;

import com.konka.kkinterface.tv.CaDesk;
import com.mstar.android.tvapi.common.exception.TvCommonException;
import com.mstar.android.tvapi.dtv.common.CaManager;
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

public class CaDeskImpl extends BaseDeskImpl implements CaDesk {
    private static CaDeskImpl caMgrImpl = null;
    private CaManager caMgr;

    private CaDeskImpl() {
        this.caMgr = null;
        this.caMgr = CaManager.getInstance();
    }

    public static CaDeskImpl getCaMgrInstance() {
        if (caMgrImpl == null) {
            caMgrImpl = new CaDeskImpl();
        }
        return caMgrImpl;
    }

    public CACardSNInfo CaGetCardSN() throws TvCommonException {
        return CaManager.CaGetCardSN();
    }

    public short CaChangePin(String pbyOldPin, String pbyNewPin) throws TvCommonException {
        return CaManager.CaChangePin(pbyOldPin, pbyNewPin);
    }

    public short CaSetRating(String pbyPin, short byRating) throws TvCommonException {
        return CaManager.CaSetRating(pbyPin, byRating);
    }

    public CARatingInfo CaGetRating() throws TvCommonException {
        return CaManager.CaGetRating();
    }

    public short CaSetWorkTime(String pbyPin, CaWorkTimeInfo worktime) throws TvCommonException {
        return CaManager.CaSetWorkTime(pbyPin, worktime);
    }

    public CaWorkTimeInfo CaGetWorkTime() throws TvCommonException {
        return CaManager.CaGetWorkTime();
    }

    public int CaGetVer() throws TvCommonException {
        return CaManager.CaGetVer();
    }

    public CaOperatorIds CaGetOperatorIds() throws TvCommonException {
        return CaManager.CaGetOperatorIds();
    }

    public CaOperatorInfo CaGetOperatorInfo(short wTVSID) throws TvCommonException {
        return CaManager.CaGetOperatorInfo(wTVSID);
    }

    public CaACListInfo CaGetACList(short wTVSID) throws TvCommonException {
        return CaManager.CaGetACList(wTVSID);
    }

    public CaSlotIDs CaGetSlotIDs(short wTVSID) throws TvCommonException {
        return CaManager.CaGetSlotIDs(wTVSID);
    }

    public CaSlotInfo CaGetSlotInfo(short wTVSID, short bySlotID) throws TvCommonException {
        return CaManager.CaGetSlotInfo(wTVSID, bySlotID);
    }

    public CaServiceEntitles CaGetServiceEntitles(short wTVSID) throws TvCommonException {
        return CaManager.CaGetServiceEntitles(wTVSID);
    }

    public CaEntitleIDs CaGetEntitleIDs(short wTVSID) throws TvCommonException {
        return CaManager.CaGetEntitleIDs(wTVSID);
    }

    public CaDetitleChkNums CaGetDetitleChkNums(short wTVSID) throws TvCommonException {
        return CaManager.CaGetDetitleChkNums(wTVSID);
    }

    public boolean CaGetDetitleReaded(short wTvsID) throws TvCommonException {
        return CaManager.CaGetDetitleReaded(wTvsID);
    }

    public boolean CaDelDetitleChkNum(short wTvsID, int dwDetitleChkNum) throws TvCommonException {
        return CaManager.CaDelDetitleChkNum(wTvsID, dwDetitleChkNum);
    }

    public short CaIsPaired(short pbyNum, String pbySTBID_List) throws TvCommonException {
        return CaManager.CaIsPaired(pbyNum, pbySTBID_List);
    }

    public short CaGetPlatformID() throws TvCommonException {
        return CaManager.CaGetPlatformID();
    }

    public short CaStopIPPVBuyDlg(CaStopIPPVBuyDlgInfo IppvInfo) throws TvCommonException {
        return CaManager.CaStopIPPVBuyDlg(IppvInfo);
    }

    public CaIPPVProgramInfos CaGetIPPVProgram(short wTvsID) throws TvCommonException {
        return CaManager.CaGetIPPVProgram(wTvsID);
    }

    public CaEmailHeadsInfo CaGetEmailHeads(short byCount, short byFromIndex) throws TvCommonException {
        return CaManager.CaGetEmailHeads(byCount, byFromIndex);
    }

    public CaEmailHeadInfo CaGetEmailHead(int dwEmailID) throws TvCommonException {
        return CaManager.CaGetEmailHead(dwEmailID);
    }

    public CaEmailContentInfo CaGetEmailContent(int dwEmailID) throws TvCommonException {
        return CaManager.CaGetEmailContent(dwEmailID);
    }

    public void CaDelEmail(int dwEmailID) throws TvCommonException {
        CaManager.CaDelEmail(dwEmailID);
    }

    public CaEmailSpaceInfo CaGetEmailSpaceInfo() throws TvCommonException {
        return CaManager.CaGetEmailSpaceInfo();
    }

    public CaOperatorChildStatus CaGetOperatorChildStatus(short wTVSID) throws TvCommonException {
        return CaManager.CaGetOperatorChildStatus(wTVSID);
    }

    public CaFeedDataInfo CaReadFeedDataFromParent(short wTVSID) throws TvCommonException {
        return CaManager.CaReadFeedDataFromParent(wTVSID);
    }

    public short CaWriteFeedDataToChild(short wTVSID, CaFeedDataInfo FeedData) throws TvCommonException {
        return CaManager.CaWriteFeedDataToChild(wTVSID, FeedData);
    }

    public void CaRefreshInterface() throws TvCommonException {
        CaManager.CaRefreshInterface();
    }

    public boolean CaOTAStateConfirm(int arg1, int arg2) throws TvCommonException {
        return CaManager.CaOTAStateConfirm(arg1, arg2);
    }

    public void setOnCaEventListener(OnCaEventListener listener) {
        this.caMgr.setOnCaEventListener(listener);
    }

    public int getCurrentEvent() {
        return CaManager.getCurrentEvent();
    }

    public int getCurrentMsgType() {
        return CaManager.getCurrentMsgType();
    }

    public void setCurrentEvent(int CurrentEvent) {
        CaManager.setCurrentEvent(CurrentEvent);
    }

    public void setCurrentMsgType(int MsgType) {
        CaManager.setCurrentMsgType(MsgType);
    }
}
