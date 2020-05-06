package com.konka.kkinterface.tv;

public interface TvDeskProvider {
    CaDesk getCaManagerInstance();

    ChannelDesk getChannelManagerInstance();

    CiDesk getCiManagerInstance();

    CommonDesk getCommonManagerInstance();

    DataBaseDesk getDataBaseManagerInstance();

    DemoDesk getDemoManagerInstance();

    EpgDesk getEpgManagerInstance();

    PictureDesk getPictureManagerInstance();

    PvrDesk getPvrManagerInstance();

    S3DDesk getS3DManagerInstance();

    SettingDesk getSettingManagerInstance();

    SoundDesk getSoundManagerInstance();

    void initTvSrvProvider();
}
