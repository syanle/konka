package com.konka.kkimplements.tv;

import android.content.Context;
import com.konka.kkimplements.tv.mstar.CaDeskImpl;
import com.konka.kkimplements.tv.mstar.ChannelDeskImpl;
import com.konka.kkimplements.tv.mstar.CiDeskImpl;
import com.konka.kkimplements.tv.mstar.CommonDeskImpl;
import com.konka.kkimplements.tv.mstar.DataBaseDeskImpl;
import com.konka.kkimplements.tv.mstar.DemoDeskImpl;
import com.konka.kkimplements.tv.mstar.EpgDeskImpl;
import com.konka.kkimplements.tv.mstar.PictureDeskImpl;
import com.konka.kkimplements.tv.mstar.PvrDeskImpl;
import com.konka.kkimplements.tv.mstar.S3DDeskImpl;
import com.konka.kkimplements.tv.mstar.SettingDeskImpl;
import com.konka.kkimplements.tv.mstar.SoundDeskImpl;
import com.konka.kkinterface.tv.CaDesk;
import com.konka.kkinterface.tv.ChannelDesk;
import com.konka.kkinterface.tv.CiDesk;
import com.konka.kkinterface.tv.CommonDesk;
import com.konka.kkinterface.tv.DataBaseDesk;
import com.konka.kkinterface.tv.DemoDesk;
import com.konka.kkinterface.tv.EpgDesk;
import com.konka.kkinterface.tv.PictureDesk;
import com.konka.kkinterface.tv.PvrDesk;
import com.konka.kkinterface.tv.S3DDesk;
import com.konka.kkinterface.tv.SettingDesk;
import com.konka.kkinterface.tv.SoundDesk;
import com.konka.kkinterface.tv.TvDeskProvider;

public class TvDeskProviderImpl implements TvDeskProvider {
    private static TvDeskProvider instance = null;
    CaDesk caManager = null;
    ChannelDesk channelManager = null;
    CiDesk ciManager = null;
    CommonDesk comManager = null;
    private Context context;
    DataBaseDesk dataBaseManager = null;
    DemoDesk demoManager = null;
    EpgDesk epgManager = null;
    PictureDesk pictureManager = null;
    PvrDesk pvrManager = null;
    S3DDesk s3dManager = null;
    SettingDesk settingManager = null;
    SoundDesk soundManager = null;

    public static TvDeskProvider getInstance(Context context2) {
        if (instance == null) {
            instance = new TvDeskProviderImpl(context2);
        }
        return instance;
    }

    private TvDeskProviderImpl(Context context2) {
        this.context = context2;
    }

    public void initTvSrvProvider() {
    }

    public CommonDesk getCommonManagerInstance() {
        this.comManager = CommonDeskImpl.getInstance(this.context);
        return this.comManager;
    }

    public PictureDesk getPictureManagerInstance() {
        this.pictureManager = PictureDeskImpl.getPictureMgrInstance(this.context);
        return this.pictureManager;
    }

    public DataBaseDesk getDataBaseManagerInstance() {
        this.dataBaseManager = DataBaseDeskImpl.getDataBaseMgrInstance(this.context);
        return this.dataBaseManager;
    }

    public SettingDesk getSettingManagerInstance() {
        this.settingManager = SettingDeskImpl.getSettingMgrInstance(this.context);
        return this.settingManager;
    }

    public ChannelDesk getChannelManagerInstance() {
        this.channelManager = ChannelDeskImpl.getChannelMgrInstance(this.context);
        return this.channelManager;
    }

    public SoundDesk getSoundManagerInstance() {
        this.soundManager = SoundDeskImpl.getSoundMgrInstance(this.context);
        return this.soundManager;
    }

    public S3DDesk getS3DManagerInstance() {
        this.s3dManager = S3DDeskImpl.getS3DMgrInstance(this.context);
        return this.s3dManager;
    }

    public DemoDesk getDemoManagerInstance() {
        this.demoManager = DemoDeskImpl.getDemoMgrInstance(this.context);
        return this.demoManager;
    }

    public EpgDesk getEpgManagerInstance() {
        this.epgManager = EpgDeskImpl.getEpgMgrInstance(this.context);
        return this.epgManager;
    }

    public PvrDesk getPvrManagerInstance() {
        this.pvrManager = PvrDeskImpl.getPvrMgrInstance(this.context);
        return this.pvrManager;
    }

    public CiDesk getCiManagerInstance() {
        this.ciManager = CiDeskImpl.getCiMgrInstance();
        return this.ciManager;
    }

    public CaDesk getCaManagerInstance() {
        this.caManager = CaDeskImpl.getCaMgrInstance();
        return this.caManager;
    }
}
