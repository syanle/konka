package com.konka.kkimplements.tv.mstar;

import com.konka.kkinterface.tv.CiDesk;
import com.mstar.android.tvapi.common.exception.TvCommonException;
import com.mstar.android.tvapi.dtv.common.CiManager.CredentialValidDateRange;
import com.mstar.android.tvapi.dtv.common.CiManager.OnCiEventListener;
import com.mstar.android.tvapi.dtv.common.DtvManager;
import com.mstar.android.tvapi.dtv.vo.EnumCardState;
import com.mstar.android.tvapi.dtv.vo.EnumMmiType;

public class CiDeskImpl extends BaseDeskImpl implements CiDesk {
    private static CiDeskImpl ciMgrImpl = null;

    private CiDeskImpl() {
    }

    public static CiDeskImpl getCiMgrInstance() {
        if (ciMgrImpl == null) {
            ciMgrImpl = new CiDeskImpl();
        }
        return ciMgrImpl;
    }

    public void setCiCredentialMode(short credentialMode) throws TvCommonException {
        DtvManager.getCiManager().setCiCredentialMode(credentialMode);
    }

    public boolean isCiCredentialModeValid(short credentialMode) throws TvCommonException {
        return DtvManager.getCiManager().isCiCredentialModeValid(credentialMode);
    }

    public void enterMenu() throws TvCommonException {
        DtvManager.getCiManager().enterMenu();
    }

    public void backMenu() throws TvCommonException {
        DtvManager.getCiManager().backMenu();
    }

    public boolean isDataExisted() throws TvCommonException {
        return DtvManager.getCiManager().isDataExisted();
    }

    public EnumMmiType getMmiType() throws TvCommonException {
        return DtvManager.getCiManager().getMmiType();
    }

    public String getMenuString() throws TvCommonException {
        return DtvManager.getCiManager().getMenuString();
    }

    public String getMenuTitleString() throws TvCommonException {
        return DtvManager.getCiManager().getMenuTitleString();
    }

    public String getMenuBottomString() throws TvCommonException {
        return DtvManager.getCiManager().getMenuBottomString();
    }

    public String getListTitleString() throws TvCommonException {
        return DtvManager.getCiManager().getListTitleString();
    }

    public int getListTitleLength() throws TvCommonException {
        return DtvManager.getCiManager().getListTitleLength();
    }

    public short getMenuChoiceNumber() throws TvCommonException {
        return DtvManager.getCiManager().getMenuChoiceNumber();
    }

    public short getListChoiceNumber() throws TvCommonException {
        return DtvManager.getCiManager().getListChoiceNumber();
    }

    public short getEnqBlindAnswer() throws TvCommonException {
        return DtvManager.getCiManager().getEnqBlindAnswer();
    }

    public int getListSubtitleLength() throws TvCommonException {
        return 1;
    }

    public String getListSubtitleString() throws TvCommonException {
        return DtvManager.getCiManager().getListSubtitleString();
    }

    public String getListBottomString() throws TvCommonException {
        return DtvManager.getCiManager().getListBottomString();
    }

    public String getMenuSelectionString(int index) throws TvCommonException {
        return DtvManager.getCiManager().getMenuSelectionString(index);
    }

    public String getListSelectionString(int index) throws TvCommonException {
        return DtvManager.getCiManager().getListSelectionString(index);
    }

    public String getEnqString() throws TvCommonException {
        return DtvManager.getCiManager().getEnqString();
    }

    public int getMenuTitleLength() throws TvCommonException {
        return DtvManager.getCiManager().getMenuTitleLength();
    }

    public int getMenuSubtitleLength() throws TvCommonException {
        return DtvManager.getCiManager().getMenuSubtitleLength();
    }

    public String getMenuSubtitleString() throws TvCommonException {
        return DtvManager.getCiManager().getMenuSubtitleString();
    }

    public int getMenuBottomLength() throws TvCommonException {
        return DtvManager.getCiManager().getMenuBottomLength();
    }

    public void close() throws TvCommonException {
        DtvManager.getCiManager().close();
    }

    public int getListBottomLength() throws TvCommonException {
        return DtvManager.getCiManager().getListBottomLength();
    }

    public short getEnqLength() throws TvCommonException {
        return DtvManager.getCiManager().getEnqLength();
    }

    public short getEnqAnsLength() throws TvCommonException {
        return DtvManager.getCiManager().getEnqAnsLength();
    }

    public boolean backEnq() throws TvCommonException {
        return DtvManager.getCiManager().backEnq();
    }

    public boolean answerEnq(String password) throws TvCommonException {
        return DtvManager.getCiManager().answerEnq(password);
    }

    public void answerMenu(short index) throws TvCommonException {
        DtvManager.getCiManager().answerMenu(index);
    }

    public EnumCardState getCardState() throws TvCommonException {
        return DtvManager.getCiManager().getCardState();
    }

    public boolean isCiMenuOn() throws TvCommonException {
        return DtvManager.getCiManager().isCiMenuOn();
    }

    public CredentialValidDateRange getCiCredentialValidRange() throws TvCommonException {
        return DtvManager.getCiManager().getCiCredentialValidRange();
    }

    public void setDebugMode(boolean mode) throws TvCommonException {
        DtvManager.getCiManager().setDebugMode(mode);
    }

    public void setOnCiEventListener(OnCiEventListener listener) {
        try {
            DtvManager.getCiManager().setOnCiEventListener(listener);
        } catch (TvCommonException e) {
            e.printStackTrace();
        }
    }
}
