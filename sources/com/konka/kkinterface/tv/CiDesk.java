package com.konka.kkinterface.tv;

import com.mstar.android.tvapi.common.exception.TvCommonException;
import com.mstar.android.tvapi.dtv.common.CiManager.CredentialValidDateRange;
import com.mstar.android.tvapi.dtv.common.CiManager.OnCiEventListener;
import com.mstar.android.tvapi.dtv.vo.EnumCardState;
import com.mstar.android.tvapi.dtv.vo.EnumMmiType;

public interface CiDesk extends BaseDesk {
    boolean answerEnq(String str) throws TvCommonException;

    void answerMenu(short s) throws TvCommonException;

    boolean backEnq() throws TvCommonException;

    void backMenu() throws TvCommonException;

    void close() throws TvCommonException;

    void enterMenu() throws TvCommonException;

    EnumCardState getCardState() throws TvCommonException;

    CredentialValidDateRange getCiCredentialValidRange() throws TvCommonException;

    short getEnqAnsLength() throws TvCommonException;

    short getEnqBlindAnswer() throws TvCommonException;

    short getEnqLength() throws TvCommonException;

    String getEnqString() throws TvCommonException;

    int getListBottomLength() throws TvCommonException;

    String getListBottomString() throws TvCommonException;

    short getListChoiceNumber() throws TvCommonException;

    String getListSelectionString(int i) throws TvCommonException;

    int getListSubtitleLength() throws TvCommonException;

    String getListSubtitleString() throws TvCommonException;

    int getListTitleLength() throws TvCommonException;

    String getListTitleString() throws TvCommonException;

    int getMenuBottomLength() throws TvCommonException;

    String getMenuBottomString() throws TvCommonException;

    short getMenuChoiceNumber() throws TvCommonException;

    String getMenuSelectionString(int i) throws TvCommonException;

    String getMenuString() throws TvCommonException;

    int getMenuSubtitleLength() throws TvCommonException;

    String getMenuSubtitleString() throws TvCommonException;

    int getMenuTitleLength() throws TvCommonException;

    String getMenuTitleString() throws TvCommonException;

    EnumMmiType getMmiType() throws TvCommonException;

    boolean isCiCredentialModeValid(short s) throws TvCommonException;

    boolean isCiMenuOn() throws TvCommonException;

    boolean isDataExisted() throws TvCommonException;

    void setCiCredentialMode(short s) throws TvCommonException;

    void setDebugMode(boolean z) throws TvCommonException;

    void setOnCiEventListener(OnCiEventListener onCiEventListener);
}
