package com.konka.kkimplements.tv.mstar;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import com.konka.kkinterface.tv.CaDesk;
import com.konka.kkinterface.tv.ChannelDesk;
import com.konka.kkinterface.tv.CiDesk;
import com.konka.kkinterface.tv.CommonDesk;
import com.konka.kkinterface.tv.DataBaseDesk;
import com.konka.kkinterface.tv.DemoDesk;
import com.konka.kkinterface.tv.EpgDesk;
import com.konka.kkinterface.tv.FactoryDesk;
import com.konka.kkinterface.tv.PictureDesk;
import com.konka.kkinterface.tv.PvrDesk;
import com.konka.kkinterface.tv.S3DDesk;
import com.konka.kkinterface.tv.SettingDesk;
import com.konka.kkinterface.tv.SoundDesk;
import com.konka.kkinterface.tv.TvDesk;
import com.konka.kkinterface.tv.TvDeskProvider;
import java.util.Timer;
import java.util.TimerTask;

public class TvDeskImpl extends Service implements TvDesk {
    static final int FIXVALUE = 10;
    static int siTestValue;
    /* access modifiers changed from: private */

    /* renamed from: com reason: collision with root package name */
    public CommonDesk f7com = null;
    /* access modifiers changed from: private */
    public Context context;
    int iTestValue;
    private TvServiceBinder tvServiceBinder;

    public class TvServiceBinder extends Binder implements TvDeskProvider {
        CaDesk caManager = null;
        ChannelDesk channelManager = null;
        CiDesk ciManager = null;
        CommonDesk comManager = null;
        DataBaseDesk dataBaseManager = null;
        DemoDesk demoManager = null;
        EpgDesk epgManager = null;
        FactoryDesk factoryManager = null;
        PictureDesk pictureManager = null;
        PvrDesk pvrManager = null;
        S3DDesk s3dManager = null;
        SettingDesk settingManager = null;
        SoundDesk soundManager = null;

        public TvServiceBinder() {
            this.dataBaseManager = DataBaseDeskImpl.getDataBaseMgrInstance(TvDeskImpl.this.context);
        }

        public void initTvSrvProvider() {
        }

        public CommonDesk getCommonManagerInstance() {
            this.comManager = CommonDeskImpl.getInstance(TvDeskImpl.this.context);
            return this.comManager;
        }

        public PictureDesk getPictureManagerInstance() {
            this.pictureManager = PictureDeskImpl.getPictureMgrInstance(TvDeskImpl.this.context);
            return this.pictureManager;
        }

        public DataBaseDesk getDataBaseManagerInstance() {
            return this.dataBaseManager;
        }

        public SettingDesk getSettingManagerInstance() {
            this.settingManager = SettingDeskImpl.getSettingMgrInstance(TvDeskImpl.this.context);
            return this.settingManager;
        }

        public ChannelDesk getChannelManagerInstance() {
            this.channelManager = ChannelDeskImpl.getChannelMgrInstance(TvDeskImpl.this.context);
            return this.channelManager;
        }

        public SoundDesk getSoundManagerInstance() {
            this.soundManager = SoundDeskImpl.getSoundMgrInstance(TvDeskImpl.this.context);
            return this.soundManager;
        }

        public S3DDesk getS3DManagerInstance() {
            this.s3dManager = S3DDeskImpl.getS3DMgrInstance(TvDeskImpl.this.context);
            return this.s3dManager;
        }

        public DemoDesk getDemoManagerInstance() {
            this.demoManager = DemoDeskImpl.getDemoMgrInstance(TvDeskImpl.this.context);
            return this.demoManager;
        }

        public EpgDesk getEpgManagerInstance() {
            this.epgManager = EpgDeskImpl.getEpgMgrInstance(TvDeskImpl.this.context);
            return this.epgManager;
        }

        public PvrDesk getPvrManagerInstance() {
            this.pvrManager = PvrDeskImpl.getPvrMgrInstance(TvDeskImpl.this.context);
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

    public TvDeskImpl(Context context2) {
        this.context = context2;
        this.f7com = CommonDeskImpl.getInstance(context2);
        this.f7com.printfE("TvService", "TvServiceImpl constructor!!");
        new Thread(new Runnable() {
            public void run() {
                TvDeskImpl.this.f7com.printfE("TvServiceImpl.....");
                try {
                    Thread.sleep(15000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        this.f7com.printfE("start timer...");
        new Timer().schedule(new TimerTask() {
            public void run() {
            }
        }, 1000, 1000);
    }

    public int test(int a) {
        this.f7com.printfE("TvService", "test!!");
        return 0;
    }

    public void showinfo() {
        this.f7com.printfE("TvService", "This is mock class!!!");
        selffunc1();
        selffunc2();
    }

    private void selffunc1() {
        this.f7com.printfE("TvService", "selffunc1:hello!!");
    }

    /* access modifiers changed from: protected */
    public void selffunc2() {
        this.f7com.printfE("TvService", "selffunc2:hello!!");
    }

    public void onCreate() {
        this.tvServiceBinder = new TvServiceBinder();
        super.onCreate();
    }

    public IBinder onBind(Intent arg0) {
        return this.tvServiceBinder;
    }

    public void onDestroy() {
        Log.d("service", "onDestroy");
        super.onDestroy();
    }

    public SQLiteDatabase openOrCreateDatabase(String name, int mode, CursorFactory factory) {
        return super.openOrCreateDatabase("data/data/mstar.tvsetting.factory/databases", mode, factory);
    }
}
