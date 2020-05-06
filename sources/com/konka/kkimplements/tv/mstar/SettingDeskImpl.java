package com.konka.kkimplements.tv.mstar;

import android.content.Context;
import android.util.Log;
import com.cyanogenmod.trebuchet.RocketLauncher.Board;
import com.konka.kkimplements.tv.mstar.GruleIndex.MST_GRule_COLOR_ROLL_Index_Main;
import com.konka.kkimplements.tv.mstar.GruleIndex.MST_GRule_Index_Main;
import com.konka.kkinterface.tv.ChannelDesk;
import com.konka.kkinterface.tv.CommonDesk;
import com.konka.kkinterface.tv.DataBaseDesk.ColorWheelMode;
import com.konka.kkinterface.tv.DataBaseDesk.EN_MS_CHANNEL_SWITCH_MODE;
import com.konka.kkinterface.tv.DataBaseDesk.EN_MS_FILM;
import com.konka.kkinterface.tv.DataBaseDesk.EN_MS_OFFLINE_DET_MODE;
import com.konka.kkinterface.tv.DataBaseDesk.MS_USER_SYSTEM_SETTING;
import com.konka.kkinterface.tv.DataBaseDesk.SmartEnergySavingMode;
import com.konka.kkinterface.tv.SettingDesk;
import com.mstar.android.tvapi.atv.AtvManager;
import com.mstar.android.tvapi.common.TvManager;
import com.mstar.android.tvapi.common.exception.TvCommonException;
import com.mstar.android.tvapi.common.vo.CecSetting;
import com.mstar.android.tvapi.common.vo.EnumPowerOnLogoMode;
import com.mstar.android.tvapi.common.vo.EnumPowerOnMusicMode;
import com.mstar.android.tvapi.common.vo.Film.EnumFilm;
import com.mstar.android.tvapi.common.vo.TvOsType.EnumLanguage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class SettingDeskImpl extends BaseDeskImpl implements SettingDesk {
    private static /* synthetic */ int[] $SWITCH_TABLE$com$mstar$android$tvapi$common$vo$TvOsType$EnumLanguage;
    private static int menuTimeOut;
    private static SettingDeskImpl settingMgrImpl;

    /* renamed from: com reason: collision with root package name */
    private CommonDesk f5com = null;
    private Context context;
    private DataBaseDeskImpl databaseMgr;

    static /* synthetic */ int[] $SWITCH_TABLE$com$mstar$android$tvapi$common$vo$TvOsType$EnumLanguage() {
        int[] iArr = $SWITCH_TABLE$com$mstar$android$tvapi$common$vo$TvOsType$EnumLanguage;
        if (iArr == 0) {
            int[] iArr2 = new int[EnumLanguage.values().length];
            try {
                iArr2[EnumLanguage.E_ABKHAZIAN.ordinal()] = 52;
            } catch (NoSuchFieldError e) {
            }
            try {
                iArr2[EnumLanguage.E_ACHINESE.ordinal()] = 53;
            } catch (NoSuchFieldError e2) {
            }
            try {
                iArr2[EnumLanguage.E_ACOLI.ordinal()] = 54;
            } catch (NoSuchFieldError e3) {
            }
            try {
                iArr2[EnumLanguage.E_AD.ordinal()] = 49;
            } catch (NoSuchFieldError e4) {
            }
            try {
                iArr2[EnumLanguage.E_ADANGME.ordinal()] = 55;
            } catch (NoSuchFieldError e5) {
            }
            try {
                iArr2[EnumLanguage.E_ADYGHE.ordinal()] = 56;
            } catch (NoSuchFieldError e6) {
            }
            try {
                iArr2[EnumLanguage.E_AFAR.ordinal()] = 57;
            } catch (NoSuchFieldError e7) {
            }
            try {
                iArr2[EnumLanguage.E_AFRIHILI.ordinal()] = 58;
            } catch (NoSuchFieldError e8) {
            }
            try {
                iArr2[EnumLanguage.E_AFRIKAANS.ordinal()] = 59;
            } catch (NoSuchFieldError e9) {
            }
            try {
                iArr2[EnumLanguage.E_AFRO.ordinal()] = 60;
            } catch (NoSuchFieldError e10) {
            }
            try {
                iArr2[EnumLanguage.E_AINU.ordinal()] = 61;
            } catch (NoSuchFieldError e11) {
            }
            try {
                iArr2[EnumLanguage.E_AKAN.ordinal()] = 62;
            } catch (NoSuchFieldError e12) {
            }
            try {
                iArr2[EnumLanguage.E_AKKADIAN.ordinal()] = 63;
            } catch (NoSuchFieldError e13) {
            }
            try {
                iArr2[EnumLanguage.E_ALBANIAN.ordinal()] = 64;
            } catch (NoSuchFieldError e14) {
            }
            try {
                iArr2[EnumLanguage.E_ALEUT.ordinal()] = 65;
            } catch (NoSuchFieldError e15) {
            }
            try {
                iArr2[EnumLanguage.E_ALGONQUIAN.ordinal()] = 66;
            } catch (NoSuchFieldError e16) {
            }
            try {
                iArr2[EnumLanguage.E_ALTAIC.ordinal()] = 68;
            } catch (NoSuchFieldError e17) {
            }
            try {
                iArr2[EnumLanguage.E_ALTAISOUTHERN.ordinal()] = 67;
            } catch (NoSuchFieldError e18) {
            }
            try {
                iArr2[EnumLanguage.E_AMHARIC.ordinal()] = 69;
            } catch (NoSuchFieldError e19) {
            }
            try {
                iArr2[EnumLanguage.E_ANGIKA.ordinal()] = 166;
            } catch (NoSuchFieldError e20) {
            }
            try {
                iArr2[EnumLanguage.E_APACHE.ordinal()] = 70;
            } catch (NoSuchFieldError e21) {
            }
            try {
                iArr2[EnumLanguage.E_ARABIC.ordinal()] = 32;
            } catch (NoSuchFieldError e22) {
            }
            try {
                iArr2[EnumLanguage.E_ARAGONESE.ordinal()] = 72;
            } catch (NoSuchFieldError e23) {
            }
            try {
                iArr2[EnumLanguage.E_ARAMAIC.ordinal()] = 71;
            } catch (NoSuchFieldError e24) {
            }
            try {
                iArr2[EnumLanguage.E_ARAPAHO.ordinal()] = 73;
            } catch (NoSuchFieldError e25) {
            }
            try {
                iArr2[EnumLanguage.E_ARAUCANIAN.ordinal()] = 74;
            } catch (NoSuchFieldError e26) {
            }
            try {
                iArr2[EnumLanguage.E_ARAWAK.ordinal()] = 75;
            } catch (NoSuchFieldError e27) {
            }
            try {
                iArr2[EnumLanguage.E_ARMENIAN.ordinal()] = 76;
            } catch (NoSuchFieldError e28) {
            }
            try {
                iArr2[EnumLanguage.E_AROMANIAN.ordinal()] = 379;
            } catch (NoSuchFieldError e29) {
            }
            try {
                iArr2[EnumLanguage.E_ARTIFICIAL.ordinal()] = 77;
            } catch (NoSuchFieldError e30) {
            }
            try {
                iArr2[EnumLanguage.E_ASSAMESE.ordinal()] = 78;
            } catch (NoSuchFieldError e31) {
            }
            try {
                iArr2[EnumLanguage.E_ASTURIAN.ordinal()] = 79;
            } catch (NoSuchFieldError e32) {
            }
            try {
                iArr2[EnumLanguage.E_ATHAPASCAN.ordinal()] = 80;
            } catch (NoSuchFieldError e33) {
            }
            try {
                iArr2[EnumLanguage.E_AUSTRALIAN.ordinal()] = 82;
            } catch (NoSuchFieldError e34) {
            }
            try {
                iArr2[EnumLanguage.E_AUSTRONESIAN.ordinal()] = 81;
            } catch (NoSuchFieldError e35) {
            }
            try {
                iArr2[EnumLanguage.E_AVARIC.ordinal()] = 83;
            } catch (NoSuchFieldError e36) {
            }
            try {
                iArr2[EnumLanguage.E_AVESTAN.ordinal()] = 84;
            } catch (NoSuchFieldError e37) {
            }
            try {
                iArr2[EnumLanguage.E_AWADHI.ordinal()] = 85;
            } catch (NoSuchFieldError e38) {
            }
            try {
                iArr2[EnumLanguage.E_AYMARA.ordinal()] = 86;
            } catch (NoSuchFieldError e39) {
            }
            try {
                iArr2[EnumLanguage.E_AZERBAIJANI.ordinal()] = 87;
            } catch (NoSuchFieldError e40) {
            }
            try {
                iArr2[EnumLanguage.E_AZTEC.ordinal()] = 88;
            } catch (NoSuchFieldError e41) {
            }
            try {
                iArr2[EnumLanguage.E_BALINESE.ordinal()] = 89;
            } catch (NoSuchFieldError e42) {
            }
            try {
                iArr2[EnumLanguage.E_BALTIC.ordinal()] = 90;
            } catch (NoSuchFieldError e43) {
            }
            try {
                iArr2[EnumLanguage.E_BALUCHI.ordinal()] = 91;
            } catch (NoSuchFieldError e44) {
            }
            try {
                iArr2[EnumLanguage.E_BAMBARA.ordinal()] = 92;
            } catch (NoSuchFieldError e45) {
            }
            try {
                iArr2[EnumLanguage.E_BAMILEKE.ordinal()] = 93;
            } catch (NoSuchFieldError e46) {
            }
            try {
                iArr2[EnumLanguage.E_BANDA.ordinal()] = 94;
            } catch (NoSuchFieldError e47) {
            }
            try {
                iArr2[EnumLanguage.E_BANTU.ordinal()] = 95;
            } catch (NoSuchFieldError e48) {
            }
            try {
                iArr2[EnumLanguage.E_BASA.ordinal()] = 96;
            } catch (NoSuchFieldError e49) {
            }
            try {
                iArr2[EnumLanguage.E_BASHKIR.ordinal()] = 97;
            } catch (NoSuchFieldError e50) {
            }
            try {
                iArr2[EnumLanguage.E_BASQUE.ordinal()] = 98;
            } catch (NoSuchFieldError e51) {
            }
            try {
                iArr2[EnumLanguage.E_BEJA.ordinal()] = 99;
            } catch (NoSuchFieldError e52) {
            }
            try {
                iArr2[EnumLanguage.E_BEMBA.ordinal()] = 100;
            } catch (NoSuchFieldError e53) {
            }
            try {
                iArr2[EnumLanguage.E_BENGALI.ordinal()] = 101;
            } catch (NoSuchFieldError e54) {
            }
            try {
                iArr2[EnumLanguage.E_BERBER.ordinal()] = 102;
            } catch (NoSuchFieldError e55) {
            }
            try {
                iArr2[EnumLanguage.E_BHOJPURI.ordinal()] = 103;
            } catch (NoSuchFieldError e56) {
            }
            try {
                iArr2[EnumLanguage.E_BIHARI.ordinal()] = 104;
            } catch (NoSuchFieldError e57) {
            }
            try {
                iArr2[EnumLanguage.E_BIKOL.ordinal()] = 105;
            } catch (NoSuchFieldError e58) {
            }
            try {
                iArr2[EnumLanguage.E_BINI.ordinal()] = 106;
            } catch (NoSuchFieldError e59) {
            }
            try {
                iArr2[EnumLanguage.E_BISLAMA.ordinal()] = 107;
            } catch (NoSuchFieldError e60) {
            }
            try {
                iArr2[EnumLanguage.E_BLIN.ordinal()] = 114;
            } catch (NoSuchFieldError e61) {
            }
            try {
                iArr2[EnumLanguage.E_BLISSYMBOLICS.ordinal()] = 492;
            } catch (NoSuchFieldError e62) {
            }
            try {
                iArr2[EnumLanguage.E_BOSNIAN.ordinal()] = 108;
            } catch (NoSuchFieldError e63) {
            }
            try {
                iArr2[EnumLanguage.E_BRAJ.ordinal()] = 109;
            } catch (NoSuchFieldError e64) {
            }
            try {
                iArr2[EnumLanguage.E_BRETON.ordinal()] = 110;
            } catch (NoSuchFieldError e65) {
            }
            try {
                iArr2[EnumLanguage.E_BUGINESE.ordinal()] = 111;
            } catch (NoSuchFieldError e66) {
            }
            try {
                iArr2[EnumLanguage.E_BULGARIAN.ordinal()] = 25;
            } catch (NoSuchFieldError e67) {
            }
            try {
                iArr2[EnumLanguage.E_BURIAT.ordinal()] = 112;
            } catch (NoSuchFieldError e68) {
            }
            try {
                iArr2[EnumLanguage.E_BURMESE.ordinal()] = 113;
            } catch (NoSuchFieldError e69) {
            }
            try {
                iArr2[EnumLanguage.E_BYELORUSSIAN.ordinal()] = 115;
            } catch (NoSuchFieldError e70) {
            }
            try {
                iArr2[EnumLanguage.E_CADDO.ordinal()] = 116;
            } catch (NoSuchFieldError e71) {
            }
            try {
                iArr2[EnumLanguage.E_CANTONESE.ordinal()] = 45;
            } catch (NoSuchFieldError e72) {
            }
            try {
                iArr2[EnumLanguage.E_CARIB.ordinal()] = 117;
            } catch (NoSuchFieldError e73) {
            }
            try {
                iArr2[EnumLanguage.E_CATALAN.ordinal()] = 118;
            } catch (NoSuchFieldError e74) {
            }
            try {
                iArr2[EnumLanguage.E_CAUCASIAN.ordinal()] = 119;
            } catch (NoSuchFieldError e75) {
            }
            try {
                iArr2[EnumLanguage.E_CEBUANO.ordinal()] = 120;
            } catch (NoSuchFieldError e76) {
            }
            try {
                iArr2[EnumLanguage.E_CELTIC.ordinal()] = 121;
            } catch (NoSuchFieldError e77) {
            }
            try {
                iArr2[EnumLanguage.E_CENTRALAMERICANINDIAN.ordinal()] = 122;
            } catch (NoSuchFieldError e78) {
            }
            try {
                iArr2[EnumLanguage.E_CHAGATAI.ordinal()] = 123;
            } catch (NoSuchFieldError e79) {
            }
            try {
                iArr2[EnumLanguage.E_CHAMIC.ordinal()] = 2;
            } catch (NoSuchFieldError e80) {
            }
            try {
                iArr2[EnumLanguage.E_CHAMORRO.ordinal()] = 124;
            } catch (NoSuchFieldError e81) {
            }
            try {
                iArr2[EnumLanguage.E_CHECHEN.ordinal()] = 125;
            } catch (NoSuchFieldError e82) {
            }
            try {
                iArr2[EnumLanguage.E_CHEROKEE.ordinal()] = 126;
            } catch (NoSuchFieldError e83) {
            }
            try {
                iArr2[EnumLanguage.E_CHEYENNE.ordinal()] = 127;
            } catch (NoSuchFieldError e84) {
            }
            try {
                iArr2[EnumLanguage.E_CHIBCHA.ordinal()] = 128;
            } catch (NoSuchFieldError e85) {
            }
            try {
                iArr2[EnumLanguage.E_CHINESE.ordinal()] = 27;
            } catch (NoSuchFieldError e86) {
            }
            try {
                iArr2[EnumLanguage.E_CHINOOKJARGON.ordinal()] = 129;
            } catch (NoSuchFieldError e87) {
            }
            try {
                iArr2[EnumLanguage.E_CHIPEWYAN.ordinal()] = 131;
            } catch (NoSuchFieldError e88) {
            }
            try {
                iArr2[EnumLanguage.E_CHOCTAW.ordinal()] = 130;
            } catch (NoSuchFieldError e89) {
            }
            try {
                iArr2[EnumLanguage.E_CHURCHSLAVIC.ordinal()] = 132;
            } catch (NoSuchFieldError e90) {
            }
            try {
                iArr2[EnumLanguage.E_CHUUKESE.ordinal()] = 28;
            } catch (NoSuchFieldError e91) {
            }
            try {
                iArr2[EnumLanguage.E_CHUVASH.ordinal()] = 133;
            } catch (NoSuchFieldError e92) {
            }
            try {
                iArr2[EnumLanguage.E_CIRCASSIAN.ordinal()] = 248;
            } catch (NoSuchFieldError e93) {
            }
            try {
                iArr2[EnumLanguage.E_COPTIC.ordinal()] = 134;
            } catch (NoSuchFieldError e94) {
            }
            try {
                iArr2[EnumLanguage.E_CORNISH.ordinal()] = 135;
            } catch (NoSuchFieldError e95) {
            }
            try {
                iArr2[EnumLanguage.E_CORSICAN.ordinal()] = 136;
            } catch (NoSuchFieldError e96) {
            }
            try {
                iArr2[EnumLanguage.E_CREE.ordinal()] = 137;
            } catch (NoSuchFieldError e97) {
            }
            try {
                iArr2[EnumLanguage.E_CREEK.ordinal()] = 138;
            } catch (NoSuchFieldError e98) {
            }
            try {
                iArr2[EnumLanguage.E_CREOLESANDPIDGINS.ordinal()] = 139;
            } catch (NoSuchFieldError e99) {
            }
            try {
                iArr2[EnumLanguage.E_CREOLESANDPIDGINSENGLISH.ordinal()] = 141;
            } catch (NoSuchFieldError e100) {
            }
            try {
                iArr2[EnumLanguage.E_CREOLESANDPIDGINSFRENCH.ordinal()] = 142;
            } catch (NoSuchFieldError e101) {
            }
            try {
                iArr2[EnumLanguage.E_CREOLESANDPIDGINSPORTUGUESE.ordinal()] = 143;
            } catch (NoSuchFieldError e102) {
            }
            try {
                iArr2[EnumLanguage.E_CRIMEANTATAR.ordinal()] = 140;
            } catch (NoSuchFieldError e103) {
            }
            try {
                iArr2[EnumLanguage.E_CROATIAN.ordinal()] = 11;
            } catch (NoSuchFieldError e104) {
            }
            try {
                iArr2[EnumLanguage.E_CUSHITIC.ordinal()] = 145;
            } catch (NoSuchFieldError e105) {
            }
            try {
                iArr2[EnumLanguage.E_CZECH.ordinal()] = 1;
            } catch (NoSuchFieldError e106) {
            }
            try {
                iArr2[EnumLanguage.E_DAKOTA.ordinal()] = 146;
            } catch (NoSuchFieldError e107) {
            }
            try {
                iArr2[EnumLanguage.E_DANISH.ordinal()] = 3;
            } catch (NoSuchFieldError e108) {
            }
            try {
                iArr2[EnumLanguage.E_DARGWA.ordinal()] = 4;
            } catch (NoSuchFieldError e109) {
            }
            try {
                iArr2[EnumLanguage.E_DELAWARE.ordinal()] = 147;
            } catch (NoSuchFieldError e110) {
            }
            try {
                iArr2[EnumLanguage.E_DHIVEHI.ordinal()] = 150;
            } catch (NoSuchFieldError e111) {
            }
            try {
                iArr2[EnumLanguage.E_DINKA.ordinal()] = 151;
            } catch (NoSuchFieldError e112) {
            }
            try {
                iArr2[EnumLanguage.E_DIVEHI.ordinal()] = 152;
            } catch (NoSuchFieldError e113) {
            }
            try {
                iArr2[EnumLanguage.E_DOGRI.ordinal()] = 153;
            } catch (NoSuchFieldError e114) {
            }
            try {
                iArr2[EnumLanguage.E_DOGRIB.ordinal()] = 149;
            } catch (NoSuchFieldError e115) {
            }
            try {
                iArr2[EnumLanguage.E_DRAVIDIAN.ordinal()] = 154;
            } catch (NoSuchFieldError e116) {
            }
            try {
                iArr2[EnumLanguage.E_DUALA.ordinal()] = 156;
            } catch (NoSuchFieldError e117) {
            }
            try {
                iArr2[EnumLanguage.E_DUTCH.ordinal()] = 15;
            } catch (NoSuchFieldError e118) {
            }
            try {
                iArr2[EnumLanguage.E_DUTCHMIDDLE.ordinal()] = 157;
            } catch (NoSuchFieldError e119) {
            }
            try {
                iArr2[EnumLanguage.E_DYULA.ordinal()] = 158;
            } catch (NoSuchFieldError e120) {
            }
            try {
                iArr2[EnumLanguage.E_DZONGKHA.ordinal()] = 159;
            } catch (NoSuchFieldError e121) {
            }
            try {
                iArr2[EnumLanguage.E_EFIK.ordinal()] = 160;
            } catch (NoSuchFieldError e122) {
            }
            try {
                iArr2[EnumLanguage.E_EGYPTIAN.ordinal()] = 161;
            } catch (NoSuchFieldError e123) {
            }
            try {
                iArr2[EnumLanguage.E_EKAJUK.ordinal()] = 162;
            } catch (NoSuchFieldError e124) {
            }
            try {
                iArr2[EnumLanguage.E_ELAMITE.ordinal()] = 163;
            } catch (NoSuchFieldError e125) {
            }
            try {
                iArr2[EnumLanguage.E_ENGLISH.ordinal()] = 7;
            } catch (NoSuchFieldError e126) {
            }
            try {
                iArr2[EnumLanguage.E_ENGLISHMIDDLE.ordinal()] = 164;
            } catch (NoSuchFieldError e127) {
            }
            try {
                iArr2[EnumLanguage.E_ENGLISHOLD.ordinal()] = 165;
            } catch (NoSuchFieldError e128) {
            }
            try {
                iArr2[EnumLanguage.E_ERZYA.ordinal()] = 311;
            } catch (NoSuchFieldError e129) {
            }
            try {
                iArr2[EnumLanguage.E_ESKIMO.ordinal()] = 167;
            } catch (NoSuchFieldError e130) {
            }
            try {
                iArr2[EnumLanguage.E_ESPERANTO.ordinal()] = 168;
            } catch (NoSuchFieldError e131) {
            }
            try {
                iArr2[EnumLanguage.E_ESTONIAN.ordinal()] = 37;
            } catch (NoSuchFieldError e132) {
            }
            try {
                iArr2[EnumLanguage.E_EWE.ordinal()] = 169;
            } catch (NoSuchFieldError e133) {
            }
            try {
                iArr2[EnumLanguage.E_EWONDO.ordinal()] = 170;
            } catch (NoSuchFieldError e134) {
            }
            try {
                iArr2[EnumLanguage.E_FANG.ordinal()] = 171;
            } catch (NoSuchFieldError e135) {
            }
            try {
                iArr2[EnumLanguage.E_FANTI.ordinal()] = 172;
            } catch (NoSuchFieldError e136) {
            }
            try {
                iArr2[EnumLanguage.E_FAROESE.ordinal()] = 173;
            } catch (NoSuchFieldError e137) {
            }
            try {
                iArr2[EnumLanguage.E_FIJIAN.ordinal()] = 174;
            } catch (NoSuchFieldError e138) {
            }
            try {
                iArr2[EnumLanguage.E_FILIPINO.ordinal()] = 175;
            } catch (NoSuchFieldError e139) {
            }
            try {
                iArr2[EnumLanguage.E_FINNISH.ordinal()] = 23;
            } catch (NoSuchFieldError e140) {
            }
            try {
                iArr2[EnumLanguage.E_FINNOUGRIAN.ordinal()] = 176;
            } catch (NoSuchFieldError e141) {
            }
            try {
                iArr2[EnumLanguage.E_FON.ordinal()] = 177;
            } catch (NoSuchFieldError e142) {
            }
            try {
                iArr2[EnumLanguage.E_FRENCH.ordinal()] = 10;
            } catch (NoSuchFieldError e143) {
            }
            try {
                iArr2[EnumLanguage.E_FRENCHMIDDLE.ordinal()] = 178;
            } catch (NoSuchFieldError e144) {
            }
            try {
                iArr2[EnumLanguage.E_FRENCHOLD.ordinal()] = 179;
            } catch (NoSuchFieldError e145) {
            }
            try {
                iArr2[EnumLanguage.E_FRISIAN.ordinal()] = 182;
            } catch (NoSuchFieldError e146) {
            }
            try {
                iArr2[EnumLanguage.E_FRISIANEASTERN.ordinal()] = 181;
            } catch (NoSuchFieldError e147) {
            }
            try {
                iArr2[EnumLanguage.E_FRISIANNORTHERN.ordinal()] = 180;
            } catch (NoSuchFieldError e148) {
            }
            try {
                iArr2[EnumLanguage.E_FRIULIAN.ordinal()] = 184;
            } catch (NoSuchFieldError e149) {
            }
            try {
                iArr2[EnumLanguage.E_FULAH.ordinal()] = 183;
            } catch (NoSuchFieldError e150) {
            }
            try {
                iArr2[EnumLanguage.E_GA.ordinal()] = 185;
            } catch (NoSuchFieldError e151) {
            }
            try {
                iArr2[EnumLanguage.E_GAELIC.ordinal()] = 29;
            } catch (NoSuchFieldError e152) {
            }
            try {
                iArr2[EnumLanguage.E_GALLEGAN.ordinal()] = 186;
            } catch (NoSuchFieldError e153) {
            }
            try {
                iArr2[EnumLanguage.E_GANDA.ordinal()] = 187;
            } catch (NoSuchFieldError e154) {
            }
            try {
                iArr2[EnumLanguage.E_GAYO.ordinal()] = 188;
            } catch (NoSuchFieldError e155) {
            }
            try {
                iArr2[EnumLanguage.E_GBAYA.ordinal()] = 30;
            } catch (NoSuchFieldError e156) {
            }
            try {
                iArr2[EnumLanguage.E_GEEZ.ordinal()] = 189;
            } catch (NoSuchFieldError e157) {
            }
            try {
                iArr2[EnumLanguage.E_GEORGIAN.ordinal()] = 190;
            } catch (NoSuchFieldError e158) {
            }
            try {
                iArr2[EnumLanguage.E_GERMAN.ordinal()] = 6;
            } catch (NoSuchFieldError e159) {
            }
            try {
                iArr2[EnumLanguage.E_GERMANIC.ordinal()] = 194;
            } catch (NoSuchFieldError e160) {
            }
            try {
                iArr2[EnumLanguage.E_GERMANLOW.ordinal()] = 330;
            } catch (NoSuchFieldError e161) {
            }
            try {
                iArr2[EnumLanguage.E_GERMANMIDDLEHIGH.ordinal()] = 191;
            } catch (NoSuchFieldError e162) {
            }
            try {
                iArr2[EnumLanguage.E_GERMANOLDHIGH.ordinal()] = 192;
            } catch (NoSuchFieldError e163) {
            }
            try {
                iArr2[EnumLanguage.E_GILBERTESE.ordinal()] = 195;
            } catch (NoSuchFieldError e164) {
            }
            try {
                iArr2[EnumLanguage.E_GONDI.ordinal()] = 196;
            } catch (NoSuchFieldError e165) {
            }
            try {
                iArr2[EnumLanguage.E_GORONTALO.ordinal()] = 193;
            } catch (NoSuchFieldError e166) {
            }
            try {
                iArr2[EnumLanguage.E_GOTHIC.ordinal()] = 197;
            } catch (NoSuchFieldError e167) {
            }
            try {
                iArr2[EnumLanguage.E_GREBO.ordinal()] = 198;
            } catch (NoSuchFieldError e168) {
            }
            try {
                iArr2[EnumLanguage.E_GREEK.ordinal()] = 9;
            } catch (NoSuchFieldError e169) {
            }
            try {
                iArr2[EnumLanguage.E_GREEKANCIENT.ordinal()] = 199;
            } catch (NoSuchFieldError e170) {
            }
            try {
                iArr2[EnumLanguage.E_GREENLANDIC.ordinal()] = 200;
            } catch (NoSuchFieldError e171) {
            }
            try {
                iArr2[EnumLanguage.E_GUARANI.ordinal()] = 201;
            } catch (NoSuchFieldError e172) {
            }
            try {
                iArr2[EnumLanguage.E_GUJARATI.ordinal()] = 203;
            } catch (NoSuchFieldError e173) {
            }
            try {
                iArr2[EnumLanguage.E_GWICHIN.ordinal()] = 204;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr2[EnumLanguage.E_HAIDA.ordinal()] = 205;
            } catch (NoSuchFieldError e175) {
            }
            try {
                iArr2[EnumLanguage.E_HAITIANCREOLE.ordinal()] = 206;
            } catch (NoSuchFieldError e176) {
            }
            try {
                iArr2[EnumLanguage.E_HAUSA.ordinal()] = 207;
            } catch (NoSuchFieldError e177) {
            }
            try {
                iArr2[EnumLanguage.E_HAWAIIAN.ordinal()] = 208;
            } catch (NoSuchFieldError e178) {
            }
            try {
                iArr2[EnumLanguage.E_HEBREW.ordinal()] = 35;
            } catch (NoSuchFieldError e179) {
            }
            try {
                iArr2[EnumLanguage.E_HERERO.ordinal()] = 209;
            } catch (NoSuchFieldError e180) {
            }
            try {
                iArr2[EnumLanguage.E_HILIGAYNON.ordinal()] = 210;
            } catch (NoSuchFieldError e181) {
            }
            try {
                iArr2[EnumLanguage.E_HIMACHALI.ordinal()] = 211;
            } catch (NoSuchFieldError e182) {
            }
            try {
                iArr2[EnumLanguage.E_HINDI.ordinal()] = 41;
            } catch (NoSuchFieldError e183) {
            }
            try {
                iArr2[EnumLanguage.E_HIRIMOTU.ordinal()] = 212;
            } catch (NoSuchFieldError e184) {
            }
            try {
                iArr2[EnumLanguage.E_HITTITE.ordinal()] = 42;
            } catch (NoSuchFieldError e185) {
            }
            try {
                iArr2[EnumLanguage.E_HMONG.ordinal()] = 43;
            } catch (NoSuchFieldError e186) {
            }
            try {
                iArr2[EnumLanguage.E_HUNGARIAN.ordinal()] = 14;
            } catch (NoSuchFieldError e187) {
            }
            try {
                iArr2[EnumLanguage.E_HUPA.ordinal()] = 213;
            } catch (NoSuchFieldError e188) {
            }
            try {
                iArr2[EnumLanguage.E_IBAN.ordinal()] = 214;
            } catch (NoSuchFieldError e189) {
            }
            try {
                iArr2[EnumLanguage.E_ICELANDIC.ordinal()] = 215;
            } catch (NoSuchFieldError e190) {
            }
            try {
                iArr2[EnumLanguage.E_IDO.ordinal()] = 216;
            } catch (NoSuchFieldError e191) {
            }
            try {
                iArr2[EnumLanguage.E_IGBO.ordinal()] = 218;
            } catch (NoSuchFieldError e192) {
            }
            try {
                iArr2[EnumLanguage.E_IJO.ordinal()] = 219;
            } catch (NoSuchFieldError e193) {
            }
            try {
                iArr2[EnumLanguage.E_ILOKO.ordinal()] = 220;
            } catch (NoSuchFieldError e194) {
            }
            try {
                iArr2[EnumLanguage.E_INARISAMI.ordinal()] = 386;
            } catch (NoSuchFieldError e195) {
            }
            try {
                iArr2[EnumLanguage.E_INDIC.ordinal()] = 221;
            } catch (NoSuchFieldError e196) {
            }
            try {
                iArr2[EnumLanguage.E_INDOEUROPEAN.ordinal()] = 222;
            } catch (NoSuchFieldError e197) {
            }
            try {
                iArr2[EnumLanguage.E_INDONESIAN.ordinal()] = 224;
            } catch (NoSuchFieldError e198) {
            }
            try {
                iArr2[EnumLanguage.E_INGUSH.ordinal()] = 223;
            } catch (NoSuchFieldError e199) {
            }
            try {
                iArr2[EnumLanguage.E_INTERLINGUA.ordinal()] = 225;
            } catch (NoSuchFieldError e200) {
            }
            try {
                iArr2[EnumLanguage.E_INTERLINGUE.ordinal()] = 226;
            } catch (NoSuchFieldError e201) {
            }
            try {
                iArr2[EnumLanguage.E_INUKTITUT.ordinal()] = 227;
            } catch (NoSuchFieldError e202) {
            }
            try {
                iArr2[EnumLanguage.E_INUPIAK.ordinal()] = 228;
            } catch (NoSuchFieldError e203) {
            }
            try {
                iArr2[EnumLanguage.E_IRANIAN.ordinal()] = 229;
            } catch (NoSuchFieldError e204) {
            }
            try {
                iArr2[EnumLanguage.E_IRISH.ordinal()] = 33;
            } catch (NoSuchFieldError e205) {
            }
            try {
                iArr2[EnumLanguage.E_IRISHMIDDLE.ordinal()] = 231;
            } catch (NoSuchFieldError e206) {
            }
            try {
                iArr2[EnumLanguage.E_IRISHOLD.ordinal()] = 230;
            } catch (NoSuchFieldError e207) {
            }
            try {
                iArr2[EnumLanguage.E_IROQUOIAN.ordinal()] = 232;
            } catch (NoSuchFieldError e208) {
            }
            try {
                iArr2[EnumLanguage.E_ITALIAN.ordinal()] = 13;
            } catch (NoSuchFieldError e209) {
            }
            try {
                iArr2[EnumLanguage.E_JAPANESE.ordinal()] = 233;
            } catch (NoSuchFieldError e210) {
            }
            try {
                iArr2[EnumLanguage.E_JAVANESE.ordinal()] = 234;
            } catch (NoSuchFieldError e211) {
            }
            try {
                iArr2[EnumLanguage.E_JUDEOARABIC.ordinal()] = 236;
            } catch (NoSuchFieldError e212) {
            }
            try {
                iArr2[EnumLanguage.E_JUDEOPERSIAN.ordinal()] = 237;
            } catch (NoSuchFieldError e213) {
            }
            try {
                iArr2[EnumLanguage.E_KABYLE.ordinal()] = 238;
            } catch (NoSuchFieldError e214) {
            }
            try {
                iArr2[EnumLanguage.E_KACHIN.ordinal()] = 239;
            } catch (NoSuchFieldError e215) {
            }
            try {
                iArr2[EnumLanguage.E_KALMYK.ordinal()] = 483;
            } catch (NoSuchFieldError e216) {
            }
            try {
                iArr2[EnumLanguage.E_KAMBA.ordinal()] = 240;
            } catch (NoSuchFieldError e217) {
            }
            try {
                iArr2[EnumLanguage.E_KANNADA.ordinal()] = 241;
            } catch (NoSuchFieldError e218) {
            }
            try {
                iArr2[EnumLanguage.E_KANURI.ordinal()] = 242;
            } catch (NoSuchFieldError e219) {
            }
            try {
                iArr2[EnumLanguage.E_KARACHAYBALKAR.ordinal()] = 261;
            } catch (NoSuchFieldError e220) {
            }
            try {
                iArr2[EnumLanguage.E_KARAKALPAK.ordinal()] = 243;
            } catch (NoSuchFieldError e221) {
            }
            try {
                iArr2[EnumLanguage.E_KARELIAN.ordinal()] = 262;
            } catch (NoSuchFieldError e222) {
            }
            try {
                iArr2[EnumLanguage.E_KAREN.ordinal()] = 244;
            } catch (NoSuchFieldError e223) {
            }
            try {
                iArr2[EnumLanguage.E_KASHMIRI.ordinal()] = 245;
            } catch (NoSuchFieldError e224) {
            }
            try {
                iArr2[EnumLanguage.E_KASHUBIAN.ordinal()] = 144;
            } catch (NoSuchFieldError e225) {
            }
            try {
                iArr2[EnumLanguage.E_KAWI.ordinal()] = 246;
            } catch (NoSuchFieldError e226) {
            }
            try {
                iArr2[EnumLanguage.E_KAZAKH.ordinal()] = 247;
            } catch (NoSuchFieldError e227) {
            }
            try {
                iArr2[EnumLanguage.E_KHASI.ordinal()] = 249;
            } catch (NoSuchFieldError e228) {
            }
            try {
                iArr2[EnumLanguage.E_KHMER.ordinal()] = 250;
            } catch (NoSuchFieldError e229) {
            }
            try {
                iArr2[EnumLanguage.E_KHOISAN.ordinal()] = 251;
            } catch (NoSuchFieldError e230) {
            }
            try {
                iArr2[EnumLanguage.E_KHOTANESE.ordinal()] = 252;
            } catch (NoSuchFieldError e231) {
            }
            try {
                iArr2[EnumLanguage.E_KIKUYU.ordinal()] = 253;
            } catch (NoSuchFieldError e232) {
            }
            try {
                iArr2[EnumLanguage.E_KIMBUNDU.ordinal()] = 256;
            } catch (NoSuchFieldError e233) {
            }
            try {
                iArr2[EnumLanguage.E_KINYARWANDA.ordinal()] = 254;
            } catch (NoSuchFieldError e234) {
            }
            try {
                iArr2[EnumLanguage.E_KIRGHIZ.ordinal()] = 255;
            } catch (NoSuchFieldError e235) {
            }
            try {
                iArr2[EnumLanguage.E_KLINGON.ordinal()] = 449;
            } catch (NoSuchFieldError e236) {
            }
            try {
                iArr2[EnumLanguage.E_KOMI.ordinal()] = 257;
            } catch (NoSuchFieldError e237) {
            }
            try {
                iArr2[EnumLanguage.E_KONGO.ordinal()] = 258;
            } catch (NoSuchFieldError e238) {
            }
            try {
                iArr2[EnumLanguage.E_KONKANI.ordinal()] = 259;
            } catch (NoSuchFieldError e239) {
            }
            try {
                iArr2[EnumLanguage.E_KOREAN.ordinal()] = 39;
            } catch (NoSuchFieldError e240) {
            }
            try {
                iArr2[EnumLanguage.E_KOSRAEAN.ordinal()] = 40;
            } catch (NoSuchFieldError e241) {
            }
            try {
                iArr2[EnumLanguage.E_KPELLE.ordinal()] = 260;
            } catch (NoSuchFieldError e242) {
            }
            try {
                iArr2[EnumLanguage.E_KRU.ordinal()] = 263;
            } catch (NoSuchFieldError e243) {
            }
            try {
                iArr2[EnumLanguage.E_KUANYAMA.ordinal()] = 264;
            } catch (NoSuchFieldError e244) {
            }
            try {
                iArr2[EnumLanguage.E_KUMYK.ordinal()] = 265;
            } catch (NoSuchFieldError e245) {
            }
            try {
                iArr2[EnumLanguage.E_KURDISH.ordinal()] = 266;
            } catch (NoSuchFieldError e246) {
            }
            try {
                iArr2[EnumLanguage.E_KURUKH.ordinal()] = 267;
            } catch (NoSuchFieldError e247) {
            }
            try {
                iArr2[EnumLanguage.E_KUSAIE.ordinal()] = 268;
            } catch (NoSuchFieldError e248) {
            }
            try {
                iArr2[EnumLanguage.E_KUTENAI.ordinal()] = 269;
            } catch (NoSuchFieldError e249) {
            }
            try {
                iArr2[EnumLanguage.E_LADINO.ordinal()] = 270;
            } catch (NoSuchFieldError e250) {
            }
            try {
                iArr2[EnumLanguage.E_LAHNDA.ordinal()] = 271;
            } catch (NoSuchFieldError e251) {
            }
            try {
                iArr2[EnumLanguage.E_LAMBA.ordinal()] = 272;
            } catch (NoSuchFieldError e252) {
            }
            try {
                iArr2[EnumLanguage.E_LANDDAYAK.ordinal()] = 5;
            } catch (NoSuchFieldError e253) {
            }
            try {
                iArr2[EnumLanguage.E_LANGUE.ordinal()] = 273;
            } catch (NoSuchFieldError e254) {
            }
            try {
                iArr2[EnumLanguage.E_LAO.ordinal()] = 274;
            } catch (NoSuchFieldError e255) {
            }
            try {
                iArr2[EnumLanguage.E_LATIN.ordinal()] = 275;
            } catch (NoSuchFieldError e256) {
            }
            try {
                iArr2[EnumLanguage.E_LATVIAN.ordinal()] = 34;
            } catch (NoSuchFieldError e257) {
            }
            try {
                iArr2[EnumLanguage.E_LETZEBURGESCH.ordinal()] = 276;
            } catch (NoSuchFieldError e258) {
            }
            try {
                iArr2[EnumLanguage.E_LEZGHIAN.ordinal()] = 277;
            } catch (NoSuchFieldError e259) {
            }
            try {
                iArr2[EnumLanguage.E_LIMBURGISH.ordinal()] = 278;
            } catch (NoSuchFieldError e260) {
            }
            try {
                iArr2[EnumLanguage.E_LINGALA.ordinal()] = 279;
            } catch (NoSuchFieldError e261) {
            }
            try {
                iArr2[EnumLanguage.E_LITHUANIAN.ordinal()] = 280;
            } catch (NoSuchFieldError e262) {
            }
            try {
                iArr2[EnumLanguage.E_LOJBAN.ordinal()] = 235;
            } catch (NoSuchFieldError e263) {
            }
            try {
                iArr2[EnumLanguage.E_LOZI.ordinal()] = 281;
            } catch (NoSuchFieldError e264) {
            }
            try {
                iArr2[EnumLanguage.E_LUBAKATANGA.ordinal()] = 283;
            } catch (NoSuchFieldError e265) {
            }
            try {
                iArr2[EnumLanguage.E_LUBALULUA.ordinal()] = 282;
            } catch (NoSuchFieldError e266) {
            }
            try {
                iArr2[EnumLanguage.E_LUISENO.ordinal()] = 284;
            } catch (NoSuchFieldError e267) {
            }
            try {
                iArr2[EnumLanguage.E_LULESAMI.ordinal()] = 385;
            } catch (NoSuchFieldError e268) {
            }
            try {
                iArr2[EnumLanguage.E_LUNDA.ordinal()] = 285;
            } catch (NoSuchFieldError e269) {
            }
            try {
                iArr2[EnumLanguage.E_LUO.ordinal()] = 286;
            } catch (NoSuchFieldError e270) {
            }
            try {
                iArr2[EnumLanguage.E_LUSHAI.ordinal()] = 287;
            } catch (NoSuchFieldError e271) {
            }
            try {
                iArr2[EnumLanguage.E_MACEDONIAN.ordinal()] = 288;
            } catch (NoSuchFieldError e272) {
            }
            try {
                iArr2[EnumLanguage.E_MADURESE.ordinal()] = 289;
            } catch (NoSuchFieldError e273) {
            }
            try {
                iArr2[EnumLanguage.E_MAGAHI.ordinal()] = 290;
            } catch (NoSuchFieldError e274) {
            }
            try {
                iArr2[EnumLanguage.E_MAITHILI.ordinal()] = 291;
            } catch (NoSuchFieldError e275) {
            }
            try {
                iArr2[EnumLanguage.E_MAKASAR.ordinal()] = 292;
            } catch (NoSuchFieldError e276) {
            }
            try {
                iArr2[EnumLanguage.E_MALAGASY.ordinal()] = 293;
            } catch (NoSuchFieldError e277) {
            }
            try {
                iArr2[EnumLanguage.E_MALAY.ordinal()] = 294;
            } catch (NoSuchFieldError e278) {
            }
            try {
                iArr2[EnumLanguage.E_MALAYALAM.ordinal()] = 297;
            } catch (NoSuchFieldError e279) {
            }
            try {
                iArr2[EnumLanguage.E_MALTESE.ordinal()] = 298;
            } catch (NoSuchFieldError e280) {
            }
            try {
                iArr2[EnumLanguage.E_MANCHU.ordinal()] = 299;
            } catch (NoSuchFieldError e281) {
            }
            try {
                iArr2[EnumLanguage.E_MANDAR.ordinal()] = 296;
            } catch (NoSuchFieldError e282) {
            }
            try {
                iArr2[EnumLanguage.E_MANDARIN.ordinal()] = 44;
            } catch (NoSuchFieldError e283) {
            }
            try {
                iArr2[EnumLanguage.E_MANDINGO.ordinal()] = 300;
            } catch (NoSuchFieldError e284) {
            }
            try {
                iArr2[EnumLanguage.E_MANIPURI.ordinal()] = 301;
            } catch (NoSuchFieldError e285) {
            }
            try {
                iArr2[EnumLanguage.E_MANOBO.ordinal()] = 302;
            } catch (NoSuchFieldError e286) {
            }
            try {
                iArr2[EnumLanguage.E_MANX.ordinal()] = 303;
            } catch (NoSuchFieldError e287) {
            }
            try {
                iArr2[EnumLanguage.E_MAORI.ordinal()] = 46;
            } catch (NoSuchFieldError e288) {
            }
            try {
                iArr2[EnumLanguage.E_MARATHI.ordinal()] = 304;
            } catch (NoSuchFieldError e289) {
            }
            try {
                iArr2[EnumLanguage.E_MARI.ordinal()] = 305;
            } catch (NoSuchFieldError e290) {
            }
            try {
                iArr2[EnumLanguage.E_MARSHALL.ordinal()] = 306;
            } catch (NoSuchFieldError e291) {
            }
            try {
                iArr2[EnumLanguage.E_MARWARI.ordinal()] = 308;
            } catch (NoSuchFieldError e292) {
            }
            try {
                iArr2[EnumLanguage.E_MASAI.ordinal()] = 309;
            } catch (NoSuchFieldError e293) {
            }
            try {
                iArr2[EnumLanguage.E_MAX.ordinal()] = 500;
            } catch (NoSuchFieldError e294) {
            }
            try {
                iArr2[EnumLanguage.E_MAYAN.ordinal()] = 310;
            } catch (NoSuchFieldError e295) {
            }
            try {
                iArr2[EnumLanguage.E_MENDE.ordinal()] = 312;
            } catch (NoSuchFieldError e296) {
            }
            try {
                iArr2[EnumLanguage.E_MICMAC.ordinal()] = 313;
            } catch (NoSuchFieldError e297) {
            }
            try {
                iArr2[EnumLanguage.E_MINANGKABAU.ordinal()] = 314;
            } catch (NoSuchFieldError e298) {
            }
            try {
                iArr2[EnumLanguage.E_MIRANDESE.ordinal()] = 307;
            } catch (NoSuchFieldError e299) {
            }
            try {
                iArr2[EnumLanguage.E_MISCELLANEOUS.ordinal()] = 315;
            } catch (NoSuchFieldError e300) {
            }
            try {
                iArr2[EnumLanguage.E_MOHAWK.ordinal()] = 316;
            } catch (NoSuchFieldError e301) {
            }
            try {
                iArr2[EnumLanguage.E_MOKSHA.ordinal()] = 295;
            } catch (NoSuchFieldError e302) {
            }
            try {
                iArr2[EnumLanguage.E_MOLDAVIAN.ordinal()] = 317;
            } catch (NoSuchFieldError e303) {
            }
            try {
                iArr2[EnumLanguage.E_MONGO.ordinal()] = 319;
            } catch (NoSuchFieldError e304) {
            }
            try {
                iArr2[EnumLanguage.E_MONGOLIAN.ordinal()] = 320;
            } catch (NoSuchFieldError e305) {
            }
            try {
                iArr2[EnumLanguage.E_MONKMER.ordinal()] = 318;
            } catch (NoSuchFieldError e306) {
            }
            try {
                iArr2[EnumLanguage.E_MOSSI.ordinal()] = 321;
            } catch (NoSuchFieldError e307) {
            }
            try {
                iArr2[EnumLanguage.E_MULTIPLE.ordinal()] = 322;
            } catch (NoSuchFieldError e308) {
            }
            try {
                iArr2[EnumLanguage.E_MUNDA.ordinal()] = 323;
            } catch (NoSuchFieldError e309) {
            }
            try {
                iArr2[EnumLanguage.E_NAURU.ordinal()] = 325;
            } catch (NoSuchFieldError e310) {
            }
            try {
                iArr2[EnumLanguage.E_NAVAJO.ordinal()] = 326;
            } catch (NoSuchFieldError e311) {
            }
            try {
                iArr2[EnumLanguage.E_NDEBELENORTH.ordinal()] = 327;
            } catch (NoSuchFieldError e312) {
            }
            try {
                iArr2[EnumLanguage.E_NDEBELESOUTH.ordinal()] = 328;
            } catch (NoSuchFieldError e313) {
            }
            try {
                iArr2[EnumLanguage.E_NDONGO.ordinal()] = 329;
            } catch (NoSuchFieldError e314) {
            }
            try {
                iArr2[EnumLanguage.E_NEAPOLITAN.ordinal()] = 324;
            } catch (NoSuchFieldError e315) {
            }
            try {
                iArr2[EnumLanguage.E_NEPALI.ordinal()] = 331;
            } catch (NoSuchFieldError e316) {
            }
            try {
                iArr2[EnumLanguage.E_NETHERLANDS.ordinal()] = 38;
            } catch (NoSuchFieldError e317) {
            }
            try {
                iArr2[EnumLanguage.E_NEWARI.ordinal()] = 332;
            } catch (NoSuchFieldError e318) {
            }
            try {
                iArr2[EnumLanguage.E_NEWARICLASSICAL.ordinal()] = 344;
            } catch (NoSuchFieldError e319) {
            }
            try {
                iArr2[EnumLanguage.E_NIAS.ordinal()] = 333;
            } catch (NoSuchFieldError e320) {
            }
            try {
                iArr2[EnumLanguage.E_NIGERKORDOFANIAN.ordinal()] = 334;
            } catch (NoSuchFieldError e321) {
            }
            try {
                iArr2[EnumLanguage.E_NILOSAHARAN.ordinal()] = 335;
            } catch (NoSuchFieldError e322) {
            }
            try {
                iArr2[EnumLanguage.E_NIUEAN.ordinal()] = 336;
            } catch (NoSuchFieldError e323) {
            }
            try {
                iArr2[EnumLanguage.E_NKO.ordinal()] = 419;
            } catch (NoSuchFieldError e324) {
            }
            try {
                iArr2[EnumLanguage.E_NOGAI.ordinal()] = 338;
            } catch (NoSuchFieldError e325) {
            }
            try {
                iArr2[EnumLanguage.E_NONE.ordinal()] = 51;
            } catch (NoSuchFieldError e326) {
            }
            try {
                iArr2[EnumLanguage.E_NORSEOLD.ordinal()] = 339;
            } catch (NoSuchFieldError e327) {
            }
            try {
                iArr2[EnumLanguage.E_NORTHAMERICANINDIAN.ordinal()] = 340;
            } catch (NoSuchFieldError e328) {
            }
            try {
                iArr2[EnumLanguage.E_NORWEGIAN.ordinal()] = 16;
            } catch (NoSuchFieldError e329) {
            }
            try {
                iArr2[EnumLanguage.E_NORWEGIANBOKMAL.ordinal()] = 337;
            } catch (NoSuchFieldError e330) {
            }
            try {
                iArr2[EnumLanguage.E_NORWEGIANNYNORSK.ordinal()] = 341;
            } catch (NoSuchFieldError e331) {
            }
            try {
                iArr2[EnumLanguage.E_NUBIAN.ordinal()] = 342;
            } catch (NoSuchFieldError e332) {
            }
            try {
                iArr2[EnumLanguage.E_NUOSU.ordinal()] = 217;
            } catch (NoSuchFieldError e333) {
            }
            try {
                iArr2[EnumLanguage.E_NYAMWEZI.ordinal()] = 343;
            } catch (NoSuchFieldError e334) {
            }
            try {
                iArr2[EnumLanguage.E_NYANJA.ordinal()] = 345;
            } catch (NoSuchFieldError e335) {
            }
            try {
                iArr2[EnumLanguage.E_NYANKOLE.ordinal()] = 346;
            } catch (NoSuchFieldError e336) {
            }
            try {
                iArr2[EnumLanguage.E_NYORO.ordinal()] = 347;
            } catch (NoSuchFieldError e337) {
            }
            try {
                iArr2[EnumLanguage.E_NZIMA.ordinal()] = 348;
            } catch (NoSuchFieldError e338) {
            }
            try {
                iArr2[EnumLanguage.E_OJIBWA.ordinal()] = 349;
            } catch (NoSuchFieldError e339) {
            }
            try {
                iArr2[EnumLanguage.E_ORIYA.ordinal()] = 350;
            } catch (NoSuchFieldError e340) {
            }
            try {
                iArr2[EnumLanguage.E_OROMO.ordinal()] = 351;
            } catch (NoSuchFieldError e341) {
            }
            try {
                iArr2[EnumLanguage.E_OSAGE.ordinal()] = 352;
            } catch (NoSuchFieldError e342) {
            }
            try {
                iArr2[EnumLanguage.E_OSSETIC.ordinal()] = 353;
            } catch (NoSuchFieldError e343) {
            }
            try {
                iArr2[EnumLanguage.E_OTOMIAN.ordinal()] = 354;
            } catch (NoSuchFieldError e344) {
            }
            try {
                iArr2[EnumLanguage.E_PAHLAVI.ordinal()] = 355;
            } catch (NoSuchFieldError e345) {
            }
            try {
                iArr2[EnumLanguage.E_PALAUAN.ordinal()] = 356;
            } catch (NoSuchFieldError e346) {
            }
            try {
                iArr2[EnumLanguage.E_PALI.ordinal()] = 357;
            } catch (NoSuchFieldError e347) {
            }
            try {
                iArr2[EnumLanguage.E_PAMPANGA.ordinal()] = 358;
            } catch (NoSuchFieldError e348) {
            }
            try {
                iArr2[EnumLanguage.E_PANGASINAN.ordinal()] = 359;
            } catch (NoSuchFieldError e349) {
            }
            try {
                iArr2[EnumLanguage.E_PANJABI.ordinal()] = 360;
            } catch (NoSuchFieldError e350) {
            }
            try {
                iArr2[EnumLanguage.E_PAPIAMENTO.ordinal()] = 361;
            } catch (NoSuchFieldError e351) {
            }
            try {
                iArr2[EnumLanguage.E_PAPUANAUSTRALIAN.ordinal()] = 362;
            } catch (NoSuchFieldError e352) {
            }
            try {
                iArr2[EnumLanguage.E_PERSIAN.ordinal()] = 363;
            } catch (NoSuchFieldError e353) {
            }
            try {
                iArr2[EnumLanguage.E_PERSIANOLD.ordinal()] = 364;
            } catch (NoSuchFieldError e354) {
            }
            try {
                iArr2[EnumLanguage.E_PHILIPPINE.ordinal()] = 366;
            } catch (NoSuchFieldError e355) {
            }
            try {
                iArr2[EnumLanguage.E_PHOENICIAN.ordinal()] = 365;
            } catch (NoSuchFieldError e356) {
            }
            try {
                iArr2[EnumLanguage.E_POLISH.ordinal()] = 17;
            } catch (NoSuchFieldError e357) {
            }
            try {
                iArr2[EnumLanguage.E_PONAPE.ordinal()] = 367;
            } catch (NoSuchFieldError e358) {
            }
            try {
                iArr2[EnumLanguage.E_PORTUGUESE.ordinal()] = 18;
            } catch (NoSuchFieldError e359) {
            }
            try {
                iArr2[EnumLanguage.E_PRAKRIT.ordinal()] = 368;
            } catch (NoSuchFieldError e360) {
            }
            try {
                iArr2[EnumLanguage.E_PROVENCALOLD.ordinal()] = 369;
            } catch (NoSuchFieldError e361) {
            }
            try {
                iArr2[EnumLanguage.E_PUSHTO.ordinal()] = 370;
            } catch (NoSuchFieldError e362) {
            }
            try {
                iArr2[EnumLanguage.E_QAA.ordinal()] = 47;
            } catch (NoSuchFieldError e363) {
            }
            try {
                iArr2[EnumLanguage.E_QUECHUA.ordinal()] = 371;
            } catch (NoSuchFieldError e364) {
            }
            try {
                iArr2[EnumLanguage.E_RAJASTHANI.ordinal()] = 373;
            } catch (NoSuchFieldError e365) {
            }
            try {
                iArr2[EnumLanguage.E_RAPANUI.ordinal()] = 374;
            } catch (NoSuchFieldError e366) {
            }
            try {
                iArr2[EnumLanguage.E_RAROTONGAN.ordinal()] = 375;
            } catch (NoSuchFieldError e367) {
            }
            try {
                iArr2[EnumLanguage.E_RHAETOROMANCE.ordinal()] = 372;
            } catch (NoSuchFieldError e368) {
            }
            try {
                iArr2[EnumLanguage.E_ROMANCE.ordinal()] = 376;
            } catch (NoSuchFieldError e369) {
            }
            try {
                iArr2[EnumLanguage.E_ROMANIAN.ordinal()] = 20;
            } catch (NoSuchFieldError e370) {
            }
            try {
                iArr2[EnumLanguage.E_ROMANY.ordinal()] = 377;
            } catch (NoSuchFieldError e371) {
            }
            try {
                iArr2[EnumLanguage.E_RUNDI.ordinal()] = 378;
            } catch (NoSuchFieldError e372) {
            }
            try {
                iArr2[EnumLanguage.E_RUSSIAN.ordinal()] = 19;
            } catch (NoSuchFieldError e373) {
            }
            try {
                iArr2[EnumLanguage.E_SALISHAN.ordinal()] = 380;
            } catch (NoSuchFieldError e374) {
            }
            try {
                iArr2[EnumLanguage.E_SAMARITANARAMAIC.ordinal()] = 381;
            } catch (NoSuchFieldError e375) {
            }
            try {
                iArr2[EnumLanguage.E_SAMI.ordinal()] = 384;
            } catch (NoSuchFieldError e376) {
            }
            try {
                iArr2[EnumLanguage.E_SAMINORTHERN.ordinal()] = 383;
            } catch (NoSuchFieldError e377) {
            }
            try {
                iArr2[EnumLanguage.E_SAMISOUTHERN.ordinal()] = 382;
            } catch (NoSuchFieldError e378) {
            }
            try {
                iArr2[EnumLanguage.E_SAMOAN.ordinal()] = 387;
            } catch (NoSuchFieldError e379) {
            }
            try {
                iArr2[EnumLanguage.E_SANDAWE.ordinal()] = 389;
            } catch (NoSuchFieldError e380) {
            }
            try {
                iArr2[EnumLanguage.E_SANGO.ordinal()] = 390;
            } catch (NoSuchFieldError e381) {
            }
            try {
                iArr2[EnumLanguage.E_SANSKRIT.ordinal()] = 391;
            } catch (NoSuchFieldError e382) {
            }
            try {
                iArr2[EnumLanguage.E_SANTALI.ordinal()] = 394;
            } catch (NoSuchFieldError e383) {
            }
            try {
                iArr2[EnumLanguage.E_SARDINIAN.ordinal()] = 392;
            } catch (NoSuchFieldError e384) {
            }
            try {
                iArr2[EnumLanguage.E_SASAK.ordinal()] = 393;
            } catch (NoSuchFieldError e385) {
            }
            try {
                iArr2[EnumLanguage.E_SCOTS.ordinal()] = 396;
            } catch (NoSuchFieldError e386) {
            }
            try {
                iArr2[EnumLanguage.E_SELKUP.ordinal()] = 397;
            } catch (NoSuchFieldError e387) {
            }
            try {
                iArr2[EnumLanguage.E_SEMITIC.ordinal()] = 398;
            } catch (NoSuchFieldError e388) {
            }
            try {
                iArr2[EnumLanguage.E_SERBIAN.ordinal()] = 22;
            } catch (NoSuchFieldError e389) {
            }
            try {
                iArr2[EnumLanguage.E_SERBOCROATIAN.ordinal()] = 400;
            } catch (NoSuchFieldError e390) {
            }
            try {
                iArr2[EnumLanguage.E_SERER.ordinal()] = 401;
            } catch (NoSuchFieldError e391) {
            }
            try {
                iArr2[EnumLanguage.E_SHAN.ordinal()] = 403;
            } catch (NoSuchFieldError e392) {
            }
            try {
                iArr2[EnumLanguage.E_SHONA.ordinal()] = 404;
            } catch (NoSuchFieldError e393) {
            }
            try {
                iArr2[EnumLanguage.E_SICILIAN.ordinal()] = 395;
            } catch (NoSuchFieldError e394) {
            }
            try {
                iArr2[EnumLanguage.E_SIDAMO.ordinal()] = 405;
            } catch (NoSuchFieldError e395) {
            }
            try {
                iArr2[EnumLanguage.E_SIGN.ordinal()] = 402;
            } catch (NoSuchFieldError e396) {
            }
            try {
                iArr2[EnumLanguage.E_SIKSIKA.ordinal()] = 406;
            } catch (NoSuchFieldError e397) {
            }
            try {
                iArr2[EnumLanguage.E_SINDHI.ordinal()] = 407;
            } catch (NoSuchFieldError e398) {
            }
            try {
                iArr2[EnumLanguage.E_SINGHALESE.ordinal()] = 409;
            } catch (NoSuchFieldError e399) {
            }
            try {
                iArr2[EnumLanguage.E_SINOTIBETAN.ordinal()] = 410;
            } catch (NoSuchFieldError e400) {
            }
            try {
                iArr2[EnumLanguage.E_SIOUAN.ordinal()] = 411;
            } catch (NoSuchFieldError e401) {
            }
            try {
                iArr2[EnumLanguage.E_SISWANT.ordinal()] = 413;
            } catch (NoSuchFieldError e402) {
            }
            try {
                iArr2[EnumLanguage.E_SKOLTSAMI.ordinal()] = 388;
            } catch (NoSuchFieldError e403) {
            }
            try {
                iArr2[EnumLanguage.E_SLAVEY.ordinal()] = 148;
            } catch (NoSuchFieldError e404) {
            }
            try {
                iArr2[EnumLanguage.E_SLAVIC.ordinal()] = 412;
            } catch (NoSuchFieldError e405) {
            }
            try {
                iArr2[EnumLanguage.E_SLOVAK.ordinal()] = 26;
            } catch (NoSuchFieldError e406) {
            }
            try {
                iArr2[EnumLanguage.E_SLOVENIAN.ordinal()] = 21;
            } catch (NoSuchFieldError e407) {
            }
            try {
                iArr2[EnumLanguage.E_SOGDIAN.ordinal()] = 414;
            } catch (NoSuchFieldError e408) {
            }
            try {
                iArr2[EnumLanguage.E_SOMALI.ordinal()] = 415;
            } catch (NoSuchFieldError e409) {
            }
            try {
                iArr2[EnumLanguage.E_SONGHAI.ordinal()] = 416;
            } catch (NoSuchFieldError e410) {
            }
            try {
                iArr2[EnumLanguage.E_SONINKE.ordinal()] = 408;
            } catch (NoSuchFieldError e411) {
            }
            try {
                iArr2[EnumLanguage.E_SORBIAN.ordinal()] = 417;
            } catch (NoSuchFieldError e412) {
            }
            try {
                iArr2[EnumLanguage.E_SORBIANLOWER.ordinal()] = 155;
            } catch (NoSuchFieldError e413) {
            }
            try {
                iArr2[EnumLanguage.E_SORBIANUPPER.ordinal()] = 12;
            } catch (NoSuchFieldError e414) {
            }
            try {
                iArr2[EnumLanguage.E_SOTHONORTHERN.ordinal()] = 420;
            } catch (NoSuchFieldError e415) {
            }
            try {
                iArr2[EnumLanguage.E_SOTHOSOUTHERN.ordinal()] = 421;
            } catch (NoSuchFieldError e416) {
            }
            try {
                iArr2[EnumLanguage.E_SOUTHAMERICANINDIAN.ordinal()] = 422;
            } catch (NoSuchFieldError e417) {
            }
            try {
                iArr2[EnumLanguage.E_SPANISH.ordinal()] = 8;
            } catch (NoSuchFieldError e418) {
            }
            try {
                iArr2[EnumLanguage.E_SRANANTONGO.ordinal()] = 399;
            } catch (NoSuchFieldError e419) {
            }
            try {
                iArr2[EnumLanguage.E_SUDANESE.ordinal()] = 425;
            } catch (NoSuchFieldError e420) {
            }
            try {
                iArr2[EnumLanguage.E_SUKUMA.ordinal()] = 423;
            } catch (NoSuchFieldError e421) {
            }
            try {
                iArr2[EnumLanguage.E_SUMERIAN.ordinal()] = 424;
            } catch (NoSuchFieldError e422) {
            }
            try {
                iArr2[EnumLanguage.E_SUSU.ordinal()] = 426;
            } catch (NoSuchFieldError e423) {
            }
            try {
                iArr2[EnumLanguage.E_SWAHILI.ordinal()] = 427;
            } catch (NoSuchFieldError e424) {
            }
            try {
                iArr2[EnumLanguage.E_SWAZI.ordinal()] = 428;
            } catch (NoSuchFieldError e425) {
            }
            try {
                iArr2[EnumLanguage.E_SWEDISH.ordinal()] = 24;
            } catch (NoSuchFieldError e426) {
            }
            try {
                iArr2[EnumLanguage.E_SWISSGERMAN.ordinal()] = 202;
            } catch (NoSuchFieldError e427) {
            }
            try {
                iArr2[EnumLanguage.E_SWIZE.ordinal()] = 429;
            } catch (NoSuchFieldError e428) {
            }
            try {
                iArr2[EnumLanguage.E_SYRIAC.ordinal()] = 431;
            } catch (NoSuchFieldError e429) {
            }
            try {
                iArr2[EnumLanguage.E_SYRIACCLASSICAL.ordinal()] = 430;
            } catch (NoSuchFieldError e430) {
            }
            try {
                iArr2[EnumLanguage.E_TAGALOG.ordinal()] = 432;
            } catch (NoSuchFieldError e431) {
            }
            try {
                iArr2[EnumLanguage.E_TAHITIAN.ordinal()] = 433;
            } catch (NoSuchFieldError e432) {
            }
            try {
                iArr2[EnumLanguage.E_TAI.ordinal()] = 437;
            } catch (NoSuchFieldError e433) {
            }
            try {
                iArr2[EnumLanguage.E_TAJIK.ordinal()] = 435;
            } catch (NoSuchFieldError e434) {
            }
            try {
                iArr2[EnumLanguage.E_TAMASHEK.ordinal()] = 436;
            } catch (NoSuchFieldError e435) {
            }
            try {
                iArr2[EnumLanguage.E_TAMIL.ordinal()] = 438;
            } catch (NoSuchFieldError e436) {
            }
            try {
                iArr2[EnumLanguage.E_TATAR.ordinal()] = 439;
            } catch (NoSuchFieldError e437) {
            }
            try {
                iArr2[EnumLanguage.E_TELETEXT.ordinal()] = 440;
            } catch (NoSuchFieldError e438) {
            }
            try {
                iArr2[EnumLanguage.E_TELUGU.ordinal()] = 441;
            } catch (NoSuchFieldError e439) {
            }
            try {
                iArr2[EnumLanguage.E_TERENO.ordinal()] = 442;
            } catch (NoSuchFieldError e440) {
            }
            try {
                iArr2[EnumLanguage.E_TETUM.ordinal()] = 434;
            } catch (NoSuchFieldError e441) {
            }
            try {
                iArr2[EnumLanguage.E_THAI.ordinal()] = 443;
            } catch (NoSuchFieldError e442) {
            }
            try {
                iArr2[EnumLanguage.E_TIBETAN.ordinal()] = 444;
            } catch (NoSuchFieldError e443) {
            }
            try {
                iArr2[EnumLanguage.E_TIGRE.ordinal()] = 445;
            } catch (NoSuchFieldError e444) {
            }
            try {
                iArr2[EnumLanguage.E_TIGRINYA.ordinal()] = 446;
            } catch (NoSuchFieldError e445) {
            }
            try {
                iArr2[EnumLanguage.E_TIMNE.ordinal()] = 447;
            } catch (NoSuchFieldError e446) {
            }
            try {
                iArr2[EnumLanguage.E_TIVI.ordinal()] = 448;
            } catch (NoSuchFieldError e447) {
            }
            try {
                iArr2[EnumLanguage.E_TLINGIT.ordinal()] = 451;
            } catch (NoSuchFieldError e448) {
            }
            try {
                iArr2[EnumLanguage.E_TOKELAU.ordinal()] = 450;
            } catch (NoSuchFieldError e449) {
            }
            try {
                iArr2[EnumLanguage.E_TOKPISIN.ordinal()] = 455;
            } catch (NoSuchFieldError e450) {
            }
            try {
                iArr2[EnumLanguage.E_TONGAISLANDS.ordinal()] = 453;
            } catch (NoSuchFieldError e451) {
            }
            try {
                iArr2[EnumLanguage.E_TONGANYASA.ordinal()] = 452;
            } catch (NoSuchFieldError e452) {
            }
            try {
                iArr2[EnumLanguage.E_TRUK.ordinal()] = 454;
            } catch (NoSuchFieldError e453) {
            }
            try {
                iArr2[EnumLanguage.E_TSIMSHIAN.ordinal()] = 456;
            } catch (NoSuchFieldError e454) {
            }
            try {
                iArr2[EnumLanguage.E_TSONGA.ordinal()] = 457;
            } catch (NoSuchFieldError e455) {
            }
            try {
                iArr2[EnumLanguage.E_TSWANA.ordinal()] = 458;
            } catch (NoSuchFieldError e456) {
            }
            try {
                iArr2[EnumLanguage.E_TUMBUKA.ordinal()] = 459;
            } catch (NoSuchFieldError e457) {
            }
            try {
                iArr2[EnumLanguage.E_TUPIAN.ordinal()] = 460;
            } catch (NoSuchFieldError e458) {
            }
            try {
                iArr2[EnumLanguage.E_TURKISH.ordinal()] = 36;
            } catch (NoSuchFieldError e459) {
            }
            try {
                iArr2[EnumLanguage.E_TURKISHOTTOMAN.ordinal()] = 461;
            } catch (NoSuchFieldError e460) {
            }
            try {
                iArr2[EnumLanguage.E_TURKMEN.ordinal()] = 462;
            } catch (NoSuchFieldError e461) {
            }
            try {
                iArr2[EnumLanguage.E_TUVALU.ordinal()] = 464;
            } catch (NoSuchFieldError e462) {
            }
            try {
                iArr2[EnumLanguage.E_TUVINIAN.ordinal()] = 463;
            } catch (NoSuchFieldError e463) {
            }
            try {
                iArr2[EnumLanguage.E_TWI.ordinal()] = 465;
            } catch (NoSuchFieldError e464) {
            }
            try {
                iArr2[EnumLanguage.E_UDMURT.ordinal()] = 466;
            } catch (NoSuchFieldError e465) {
            }
            try {
                iArr2[EnumLanguage.E_UGARITIC.ordinal()] = 467;
            } catch (NoSuchFieldError e466) {
            }
            try {
                iArr2[EnumLanguage.E_UIGHUR.ordinal()] = 468;
            } catch (NoSuchFieldError e467) {
            }
            try {
                iArr2[EnumLanguage.E_UKRAINIAN.ordinal()] = 469;
            } catch (NoSuchFieldError e468) {
            }
            try {
                iArr2[EnumLanguage.E_UMBUNDU.ordinal()] = 470;
            } catch (NoSuchFieldError e469) {
            }
            try {
                iArr2[EnumLanguage.E_UNDETERMINED.ordinal()] = 48;
            } catch (NoSuchFieldError e470) {
            }
            try {
                iArr2[EnumLanguage.E_UNKNOWN.ordinal()] = 50;
            } catch (NoSuchFieldError e471) {
            }
            try {
                iArr2[EnumLanguage.E_URDU.ordinal()] = 471;
            } catch (NoSuchFieldError e472) {
            }
            try {
                iArr2[EnumLanguage.E_UZBEK.ordinal()] = 472;
            } catch (NoSuchFieldError e473) {
            }
            try {
                iArr2[EnumLanguage.E_VAI.ordinal()] = 473;
            } catch (NoSuchFieldError e474) {
            }
            try {
                iArr2[EnumLanguage.E_VALENCIAN.ordinal()] = 499;
            } catch (NoSuchFieldError e475) {
            }
            try {
                iArr2[EnumLanguage.E_VENDA.ordinal()] = 474;
            } catch (NoSuchFieldError e476) {
            }
            try {
                iArr2[EnumLanguage.E_VIETNAMESE.ordinal()] = 475;
            } catch (NoSuchFieldError e477) {
            }
            try {
                iArr2[EnumLanguage.E_VOLAPUK.ordinal()] = 476;
            } catch (NoSuchFieldError e478) {
            }
            try {
                iArr2[EnumLanguage.E_VOTIC.ordinal()] = 477;
            } catch (NoSuchFieldError e479) {
            }
            try {
                iArr2[EnumLanguage.E_WAKASHAN.ordinal()] = 478;
            } catch (NoSuchFieldError e480) {
            }
            try {
                iArr2[EnumLanguage.E_WALAMO.ordinal()] = 479;
            } catch (NoSuchFieldError e481) {
            }
            try {
                iArr2[EnumLanguage.E_WALLOON.ordinal()] = 418;
            } catch (NoSuchFieldError e482) {
            }
            try {
                iArr2[EnumLanguage.E_WARAY.ordinal()] = 480;
            } catch (NoSuchFieldError e483) {
            }
            try {
                iArr2[EnumLanguage.E_WASHO.ordinal()] = 481;
            } catch (NoSuchFieldError e484) {
            }
            try {
                iArr2[EnumLanguage.E_WELSH.ordinal()] = 31;
            } catch (NoSuchFieldError e485) {
            }
            try {
                iArr2[EnumLanguage.E_WOLOF.ordinal()] = 482;
            } catch (NoSuchFieldError e486) {
            }
            try {
                iArr2[EnumLanguage.E_XHOSA.ordinal()] = 484;
            } catch (NoSuchFieldError e487) {
            }
            try {
                iArr2[EnumLanguage.E_YAKUT.ordinal()] = 485;
            } catch (NoSuchFieldError e488) {
            }
            try {
                iArr2[EnumLanguage.E_YAO.ordinal()] = 486;
            } catch (NoSuchFieldError e489) {
            }
            try {
                iArr2[EnumLanguage.E_YAP.ordinal()] = 487;
            } catch (NoSuchFieldError e490) {
            }
            try {
                iArr2[EnumLanguage.E_YIDDISH.ordinal()] = 488;
            } catch (NoSuchFieldError e491) {
            }
            try {
                iArr2[EnumLanguage.E_YORUBA.ordinal()] = 489;
            } catch (NoSuchFieldError e492) {
            }
            try {
                iArr2[EnumLanguage.E_YUPIK.ordinal()] = 490;
            } catch (NoSuchFieldError e493) {
            }
            try {
                iArr2[EnumLanguage.E_ZANDE.ordinal()] = 495;
            } catch (NoSuchFieldError e494) {
            }
            try {
                iArr2[EnumLanguage.E_ZAPOTEC.ordinal()] = 491;
            } catch (NoSuchFieldError e495) {
            }
            try {
                iArr2[EnumLanguage.E_ZAZAKI.ordinal()] = 498;
            } catch (NoSuchFieldError e496) {
            }
            try {
                iArr2[EnumLanguage.E_ZENAGA.ordinal()] = 493;
            } catch (NoSuchFieldError e497) {
            }
            try {
                iArr2[EnumLanguage.E_ZHUANG.ordinal()] = 494;
            } catch (NoSuchFieldError e498) {
            }
            try {
                iArr2[EnumLanguage.E_ZULU.ordinal()] = 496;
            } catch (NoSuchFieldError e499) {
            }
            try {
                iArr2[EnumLanguage.E_ZUNI.ordinal()] = 497;
            } catch (NoSuchFieldError e500) {
            }
            $SWITCH_TABLE$com$mstar$android$tvapi$common$vo$TvOsType$EnumLanguage = iArr2;
            iArr = iArr2;
        }
        return iArr;
    }

    public static SettingDeskImpl getSettingMgrInstance(Context context2) {
        if (settingMgrImpl == null) {
            settingMgrImpl = new SettingDeskImpl(context2);
        }
        return settingMgrImpl;
    }

    private SettingDeskImpl(Context context2) {
        this.databaseMgr = DataBaseDeskImpl.getDataBaseMgrInstance(context2);
        this.f5com = CommonDeskImpl.getInstance(context2);
        this.f5com.printfE("TvService", "SettingServiceImpl constructor!!");
        int value = this.databaseMgr.getUsrData().u8OsdDuration;
        menuTimeOut = (value * 5) + 5;
        if (value == 4) {
            menuTimeOut += 10;
        }
        if (value >= 5) {
            menuTimeOut = 65535;
        }
    }

    public EnumLanguage GetOsdLanguage() {
        this.f5com.printfE("TvService", "GetOsdLanguage Parameter:" + this.databaseMgr.getUsrData().enLanguage + "!!");
        return this.databaseMgr.getUsrData().enLanguage;
    }

    public boolean SetOsdLanguage(EnumLanguage eLang) {
        int value;
        this.databaseMgr.getUsrData().enLanguage = eLang;
        this.databaseMgr.getSubtitleSet().SubtitleDefaultLanguage = eLang;
        this.databaseMgr.getSubtitleSet().SubtitleDefaultLanguage_2 = eLang;
        switch ($SWITCH_TABLE$com$mstar$android$tvapi$common$vo$TvOsType$EnumLanguage()[eLang.ordinal()]) {
            case 27:
                value = EnumLanguage.E_CHINESE.ordinal();
                break;
            case 53:
                value = EnumLanguage.E_ACHINESE.ordinal();
                break;
            default:
                value = EnumLanguage.E_ENGLISH.ordinal();
                break;
        }
        this.databaseMgr.updateUserSysSetting(this.databaseMgr.getUsrData());
        this.databaseMgr.updateUserSubtitleSetting(this.databaseMgr.getSubtitleSet());
        try {
            if (TvManager.getInstance() != null) {
                TvManager.getInstance().setLanguage(EnumLanguage.values()[value]);
            }
        } catch (TvCommonException e) {
            e.printStackTrace();
        }
        return true;
    }

    public short GetOsdDuration() {
        this.f5com.printfE("TvService", "GetOsdDuration Parameter:" + this.databaseMgr.getUsrData().u8OsdDuration + "!!");
        switch ((int) (this.databaseMgr.getUsrData().u32MenuTimeOut / 1000)) {
            case 5:
                this.databaseMgr.getUsrData().u8OsdDuration = 0;
                break;
            case 10:
                this.databaseMgr.getUsrData().u8OsdDuration = 1;
                break;
            case 15:
                this.databaseMgr.getUsrData().u8OsdDuration = 2;
                break;
            case Board.NUM_ICONS /*20*/:
                this.databaseMgr.getUsrData().u8OsdDuration = 3;
                break;
            case 30:
                this.databaseMgr.getUsrData().u8OsdDuration = 4;
                break;
            default:
                this.databaseMgr.getUsrData().u8OsdDuration = 5;
                break;
        }
        return this.databaseMgr.getUsrData().u8OsdDuration;
    }

    public boolean SetOsdDuration(short value) {
        this.f5com.printfE("TvService", "SetOsdDuration Parameter: Nothing to do in mock!!");
        this.databaseMgr.getUsrData().u8OsdDuration = value;
        this.databaseMgr.getUsrData().u32MenuTimeOut = (long) (((value * 5) + 5) * ChannelDesk.max_dtv_count);
        menuTimeOut = (value * 5) + 5;
        if (value == 4) {
            MS_USER_SYSTEM_SETTING usrData = this.databaseMgr.getUsrData();
            usrData.u32MenuTimeOut += 5000;
            menuTimeOut += 5;
        }
        if (value >= 5) {
            menuTimeOut = 65535;
            this.databaseMgr.getUsrData().u32MenuTimeOut = 65535;
        }
        this.f5com.printfE("TvService", "SetOsdDuration : TimeOut = " + this.databaseMgr.getUsrData().u32MenuTimeOut + "ms!!");
        this.databaseMgr.updateUserSysSetting(this.databaseMgr.getUsrData());
        return true;
    }

    public int getOsdTimeoutSecond() {
        menuTimeOut = (int) (this.databaseMgr.getUsrData().u32MenuTimeOut / 1000);
        return menuTimeOut;
    }

    public EN_MS_CHANNEL_SWITCH_MODE GetChannelSWMode() {
        this.f5com.printfE("TvService", "GetChannelSWMode Parameter:" + this.databaseMgr.getUsrData().eChSwMode + "!!");
        return this.databaseMgr.getUsrData().eChSwMode;
    }

    public boolean SetChannelSWMode(EN_MS_CHANNEL_SWITCH_MODE eMode) {
        this.f5com.printfE("TvService", "SetOsdLanguage SetChannelSWMode: Nothing to do in mock!!");
        this.databaseMgr.getUsrData().eChSwMode = eMode;
        this.databaseMgr.updateUserSysSetting(this.databaseMgr.getUsrData());
        if (eMode == EN_MS_CHANNEL_SWITCH_MODE.MS_CHANNEL_SWM_BLACKSCREEN) {
            try {
                AtvManager.getAtvPlayerManager().setChannelChangeFreezeMode(false);
            } catch (TvCommonException e) {
                e.printStackTrace();
            }
        } else {
            try {
                AtvManager.getAtvPlayerManager().setChannelChangeFreezeMode(true);
            } catch (TvCommonException e2) {
                e2.printStackTrace();
            }
        }
        return true;
    }

    public EN_MS_OFFLINE_DET_MODE GetOffDetMode() {
        this.f5com.printfE("TvService", "GetOffDetMode Parameter:" + this.databaseMgr.getUsrData().eOffDetMode + "!!");
        return this.databaseMgr.getUsrData().eOffDetMode;
    }

    public boolean SetOffDetMode(EN_MS_OFFLINE_DET_MODE eMode) {
        this.f5com.printfE("TvService", "SetOsdLanguage SetOffDetMode: Nothing to do in mock!!");
        this.databaseMgr.getUsrData().eOffDetMode = eMode;
        this.databaseMgr.updateUserSysSetting(this.databaseMgr.getUsrData());
        return true;
    }

    public short GetColorRange() {
        return this.databaseMgr.getUsrData().u8ColorRangeMode;
    }

    public boolean SetColorRanger(short value) {
        boolean colorRange0_255;
        this.databaseMgr.getUsrData().u8ColorRangeMode = value;
        this.databaseMgr.updateUserSysSetting(this.databaseMgr.getUsrData());
        if (value == 2) {
            try {
                if (!(TvManager.getInstance() == null || TvManager.getInstance().getPictureManager() == null)) {
                    TvManager.getInstance().getPictureManager().autoHDMIColorRange();
                }
            } catch (TvCommonException e) {
                e.printStackTrace();
            }
        } else {
            if (value != 0) {
                colorRange0_255 = false;
            } else {
                colorRange0_255 = true;
            }
            if (!(TvManager.getInstance() == null || TvManager.getInstance().getPictureManager() == null)) {
                TvManager.getInstance().getPictureManager().setColorRange(colorRange0_255);
            }
        }
        return true;
    }

    public EN_MS_FILM GetFilmMode() {
        return this.databaseMgr.getVideo().eFilm;
    }

    public boolean SetFilmMode(EN_MS_FILM eMode) {
        this.databaseMgr.getVideo().eFilm = eMode;
        this.databaseMgr.updateVideoBasePara(this.databaseMgr.getVideo(), this.f5com.GetCurrentInputSource().ordinal());
        try {
            if (!(TvManager.getInstance() == null || TvManager.getInstance().getPictureManager() == null)) {
                TvManager.getInstance().getPictureManager().setFilm(EnumFilm.values()[eMode.ordinal() + 1]);
            }
        } catch (TvCommonException e) {
            e.printStackTrace();
        }
        return true;
    }

    public boolean GetBlueScreenFlag() {
        return this.databaseMgr.getUsrData().bBlueScreen;
    }

    public boolean SetBlueScreenFlag(boolean bFlag) {
        this.databaseMgr.getUsrData().bBlueScreen = bFlag;
        this.databaseMgr.updateUserSysSetting(this.databaseMgr.getUsrData());
        return true;
    }

    public CecSetting GetCecStatus() {
        try {
            if (TvManager.getInstance() != null) {
                return TvManager.getInstance().getCecManager().getCecConfiguration();
            }
            return null;
        } catch (TvCommonException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean SetCecStatus(CecSetting set_Cecsetting) {
        if (!(set_Cecsetting == null || TvManager.getInstance() == null)) {
            TvManager.getInstance().getCecManager().setCecConfiguration(set_Cecsetting);
        }
        return true;
    }

    public boolean ExecRestoreToDefault() {
        boolean result = copyFile(new File("/tvdatabase/DatabaseBackup/", "user_setting.db"), new File("/tvdatabase/Database/", "user_setting.db"));
        if (0 == 0) {
            return false;
        }
        return true;
    }

    /* JADX WARNING: Unknown top exception splitter block from list: {B:15:0x0023=Splitter:B:15:0x0023, B:26:0x003f=Splitter:B:26:0x003f} */
    private boolean copyToFile(InputStream inputStream, File destFile) {
        FileOutputStream out;
        try {
            if (destFile.exists()) {
                destFile.delete();
            }
            out = new FileOutputStream(destFile);
            byte[] buffer = new byte[4096];
            while (true) {
                int bytesRead = inputStream.read(buffer);
                if (bytesRead < 0) {
                    break;
                }
                Log.d(" out.write(buffer, 0, bytesRead);", " out.write(buffer, 0, bytesRead);");
                out.write(buffer, 0, bytesRead);
            }
            out.flush();
            try {
                out.getFD().sync();
            } catch (IOException e) {
            }
            out.close();
            return true;
        } catch (IOException e2) {
            Log.d("copyToFile(InputStream inputStream, File destFile)", e2.getMessage());
            return false;
        } catch (Throwable th) {
            out.flush();
            try {
                out.getFD().sync();
            } catch (IOException e3) {
            }
            out.close();
            throw th;
        }
    }

    private boolean copyFile(File srcFile, File destFile) {
        boolean result;
        InputStream in;
        try {
            in = new FileInputStream(srcFile);
            result = copyToFile(in, destFile);
            in.close();
        } catch (IOException e) {
            Log.d("copyFile(File srcFile, File destFile)", e.getMessage());
            result = false;
        } catch (Throwable th) {
            in.close();
            throw th;
        }
        chmodFile(destFile);
        return result;
    }

    private void chmodFile(File destFile) {
        try {
            String command = "chmod 666 " + destFile.getAbsolutePath();
            Log.i("zyl", "command = " + command);
            Runtime.getRuntime().exec(command);
        } catch (IOException e) {
            Log.i("zyl", "chmod fail!!!!");
            e.printStackTrace();
        }
    }

    public EnumPowerOnMusicMode GetEnvironmentPowerOnMusicMode() {
        EnumPowerOnMusicMode ret = EnumPowerOnMusicMode.E_POWERON_MUSIC_OFF;
        try {
            if (TvManager.getInstance() != null) {
                return TvManager.getInstance().getEnvironmentPowerOnMusicMode();
            }
            return ret;
        } catch (TvCommonException e) {
            e.printStackTrace();
            return ret;
        }
    }

    public boolean SetEnvironmentPowerOnMusicMode(EnumPowerOnMusicMode eMusicMode) {
        boolean ret = false;
        try {
            if (TvManager.getInstance() != null) {
                ret = TvManager.getInstance().setEnvironmentPowerOnMusicMode(eMusicMode);
            }
        } catch (TvCommonException e) {
            e.printStackTrace();
        }
        Log.d("SetEnvironmentPowerOnMusicMode", "Set Music Mode:" + eMusicMode);
        return ret;
    }

    public EnumPowerOnLogoMode GetEnvironmentPowerOnLogoMode() {
        EnumPowerOnLogoMode ret = EnumPowerOnLogoMode.E_POWERON_LOGO_OFF;
        try {
            if (TvManager.getInstance() != null) {
                return TvManager.getInstance().getEnvironmentPowerOnLogoMode();
            }
            return ret;
        } catch (TvCommonException e) {
            e.printStackTrace();
            return ret;
        }
    }

    public boolean SetEnvironmentPowerOnLogoMode(EnumPowerOnLogoMode eLogoMode) {
        boolean ret = false;
        try {
            if (TvManager.getInstance() != null) {
                ret = TvManager.getInstance().setEnvironmentPowerOnLogoMode(eLogoMode);
            }
        } catch (TvCommonException e) {
            e.printStackTrace();
        }
        Log.d("SetEnvironmentPowerOnLogoMode", "Set Logo Mode:" + eLogoMode);
        return ret;
    }

    public short GetEnvironmentPowerOnMusicVolume() {
        short ret = 0;
        try {
            if (TvManager.getInstance() != null) {
                ret = TvManager.getInstance().getFactoryManager().getEnvironmentPowerOnMusicVolume();
            }
        } catch (TvCommonException e) {
            e.printStackTrace();
        }
        Log.d("GetEnvironmentPowerOnMusicVolume", "Get Music Volume: 0x" + Integer.toHexString(ret));
        return ret;
    }

    public boolean SetEnvironmentPowerOnMusicVolume(short volume) {
        boolean ret = false;
        try {
            if (TvManager.getInstance() != null) {
                ret = TvManager.getInstance().getFactoryManager().setEnvironmentPowerOnMusicVolume(volume);
            }
        } catch (TvCommonException e) {
            e.printStackTrace();
        }
        Log.d("SetEnvironmentPowerOnMusicVolume", "Set Music Volume: 0x" + Integer.toHexString(volume));
        return ret;
    }

    public int getStandbyNoOperation() {
        return this.databaseMgr.getUsrData().standbyNoOperation;
    }

    public boolean setStandbyNoOperation(int minutes) {
        this.databaseMgr.getUsrData().standbyNoOperation = minutes;
        this.databaseMgr.updateUserSysSetting(this.databaseMgr.getUsrData());
        return true;
    }

    public boolean getStandbyNoSignal() {
        return this.databaseMgr.getUsrData().standbyNoSignal;
    }

    public boolean setStandbyNoSignal(boolean status) {
        this.databaseMgr.getUsrData().standbyNoSignal = status;
        this.databaseMgr.updateUserSysSetting(this.databaseMgr.getUsrData());
        return true;
    }

    public boolean getScreenSaveModeStatus() {
        return this.databaseMgr.getUsrData().screenSaveMode;
    }

    public boolean setScreenSaveModeStatus(boolean status) {
        this.databaseMgr.getUsrData().screenSaveMode = status;
        this.databaseMgr.updateUserSysSetting(this.databaseMgr.getUsrData());
        return true;
    }

    public int GetSystemAutoTimeType() {
        return this.databaseMgr.getUsrData().U8SystemAutoTimeType;
    }

    public boolean SetSystemAutoTimeType(int value) {
        this.databaseMgr.getUsrData().U8SystemAutoTimeType = value;
        this.databaseMgr.updateUserSysSetting(this.databaseMgr.getUsrData());
        return true;
    }

    public SmartEnergySavingMode getSmartEnergySaving() {
        return this.databaseMgr.getUsrData().smartEnergySaving;
    }

    public boolean setSmartEnergySaving(SmartEnergySavingMode mode) {
        this.databaseMgr.getUsrData().smartEnergySaving = mode;
        this.databaseMgr.updateUserSysSetting(this.databaseMgr.getUsrData());
        try {
            if (mode == SmartEnergySavingMode.MODE_OFF) {
                if (!(TvManager.getInstance() == null || TvManager.getInstance().getPictureManager() == null)) {
                    TvManager.getInstance().getPictureManager().setBacklight(this.databaseMgr.getVideo().astPicture[0].backlight);
                }
            } else if (!(TvManager.getInstance() == null || TvManager.getInstance().getPictureManager() == null)) {
                TvManager.getInstance().getPictureManager().setBacklight(100);
            }
        } catch (TvCommonException e) {
            e.printStackTrace();
        }
        return true;
    }

    public ColorWheelMode getColorWheelMode() {
        return this.databaseMgr.getUsrData().colorWheelMode;
    }

    public boolean setColorWheelMode(ColorWheelMode mode) {
        MST_GRule_COLOR_ROLL_Index_Main index;
        this.databaseMgr.getUsrData().colorWheelMode = mode;
        this.databaseMgr.updateUserSysSetting(this.databaseMgr.getUsrData());
        MST_GRule_Index_Main type = MST_GRule_Index_Main.PQ_GRule_COLOR_ROLL_Main_Color;
        if (mode == ColorWheelMode.MODE_OFF) {
            index = MST_GRule_COLOR_ROLL_Index_Main.PQ_GRule_COLOR_ROLL_Off_Main;
        } else {
            index = MST_GRule_COLOR_ROLL_Index_Main.PQ_GRule_COLOR_ROLL_On_Main;
        }
        try {
            if (!(TvManager.getInstance() == null || TvManager.getInstance().getPictureManager() == null)) {
                TvManager.getInstance().getPictureManager().setStatusByCustomerPqRule(type.ordinal(), index.ordinal());
            }
        } catch (TvCommonException e) {
            e.printStackTrace();
        }
        return true;
    }

    public boolean getBurnInMode() {
        return this.databaseMgr.getFactoryExt().bBurnIn;
    }
}
