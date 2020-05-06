package com.konka.kkinterface.tv;

import com.konka.kkinterface.tv.DataBaseDesk.EN_SOUND_MODE;
import com.konka.kkinterface.tv.DataBaseDesk.EN_SPDIF_MODE;
import com.konka.kkinterface.tv.DataBaseDesk.EN_SRS_SET_TYPE;
import com.konka.kkinterface.tv.DataBaseDesk.EN_SURROUND_MODE;
import com.konka.kkinterface.tv.DataBaseDesk.HdmiAudioSource;
import com.mstar.android.tvapi.common.vo.EnumAtvAudioModeType;
import com.mstar.android.tvapi.common.vo.EnumAudioReturn;
import com.mstar.android.tvapi.common.vo.TvOsType.EnumInputSource;

public interface SoundDesk extends BaseDesk {
    boolean getAVCMode();

    EnumAtvAudioModeType getAtvMtsMode();

    short getBalance();

    short getBass();

    short getEqBand10k();

    short getEqBand120();

    short getEqBand1500();

    short getEqBand500();

    short getEqBand5k();

    HdmiAudioSource getHdmiAudioSource(EnumInputSource enumInputSource);

    boolean getMuteFlag();

    EN_SOUND_MODE getSoundMode();

    int getSoundSpeakerDelay();

    EN_SPDIF_MODE getSpdifOutMode();

    EN_SURROUND_MODE getSurroundMode();

    short getTreble();

    short getVolume();

    boolean setAVCMode(boolean z);

    EnumAudioReturn setAtvMtsMode(EnumAtvAudioModeType enumAtvAudioModeType);

    boolean setAudioInputSource(EnumInputSource enumInputSource);

    boolean setBalance(short s);

    boolean setBass(short s);

    boolean setEqBand10k(short s);

    boolean setEqBand120(short s);

    boolean setEqBand1500(short s);

    boolean setEqBand500(short s);

    boolean setEqBand5k(short s);

    boolean setHdmiAudioSource(EnumInputSource enumInputSource, HdmiAudioSource hdmiAudioSource);

    boolean setKaraADC();

    boolean setKaraAudioTrack(int i);

    boolean setKaraMicNR(int i);

    boolean setKaraMicVolume(short s);

    boolean setKaraMixEffect(boolean z);

    boolean setKaraMixVolume(short s);

    boolean setKaraSystemVolume(short s);

    boolean setKaraVolume(short s);

    boolean setMuteFlag(boolean z);

    void setSRSDefination(boolean z);

    void setSRSDynamicClarity(boolean z);

    void setSRSPara(EN_SRS_SET_TYPE en_srs_set_type, int i);

    void setSRSTSHD(boolean z);

    void setSRSTrueBass(boolean z);

    boolean setSoundMode(EN_SOUND_MODE en_sound_mode);

    boolean setSoundMode(EN_SOUND_MODE en_sound_mode, boolean z);

    boolean setSoundSpeakerDelay(int i);

    boolean setSpdifOutMode(EN_SPDIF_MODE en_spdif_mode);

    boolean setSurroundMode(EN_SURROUND_MODE en_surround_mode);

    EnumAudioReturn setToNextAtvMtsMode();

    boolean setTreble(short s);

    boolean setVolume(short s);
}
