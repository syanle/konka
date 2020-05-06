package com.cyanogenmod.trebuchet;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.database.ContentObserver;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.webkit.WebView;
import com.konka.passport.service.UserInfo;
import com.konka.passport.service.UserInfo.Stub;
import com.tencent.stat.MtaSDkException;
import com.tencent.stat.StatConfig;
import com.tencent.stat.StatReportStrategy;
import com.tencent.stat.StatService;
import com.tencent.stat.common.StatConstants;
import com.tencent.tvMTA.core.GlobalInfo;
import com.tencent.tvMTA.report.ReportHelper;
import com.tencent.tvMTA.utils.AppUtils;
import com.tencent.tvMTA.utils.Constant.License_plate;
import com.umeng.common.a;
import java.lang.ref.WeakReference;

public class LauncherApplication extends Application {
    private static final String ACTION_PASSPORT_SERVICE = "com.konka.passport.service.USERINFO_SERVICE";
    private static final String TAG = "LauncherApplication";
    private static Context sContext;
    private static boolean sIsScreenLarge;
    private static float sScreenDensity;
    private final ContentObserver mFavoritesObserver = new ContentObserver(new Handler()) {
        public void onChange(boolean selfChange) {
            LauncherApplication.this.mModel.startLoader(LauncherApplication.this, false);
        }
    };
    public IconCache mIconCache;
    WeakReference<LauncherProvider> mLauncherProvider;
    /* access modifiers changed from: private */
    public WeakReference<Launcher> mLauncherRef;
    /* access modifiers changed from: private */
    public final Object mLock = new Object();
    public LauncherModel mModel;
    private ServiceConnection mServiceConn;
    /* access modifiers changed from: private */
    public boolean mUserCenterCalledOnce = false;
    /* access modifiers changed from: private */
    public boolean mUserCenterConnected = false;
    /* access modifiers changed from: private */
    public UserInfo mUserInfo;

    public void onCreate() {
        super.onCreate();
        sContext = this;
        initGlobalConfig(this);
        initMTAConfig(true);
        reportEven();
        String str = License_plate.KONKA;
        try {
            StatService.startStatService(getApplicationContext(), a.h, StatConstants.VERSION);
        } catch (MtaSDkException e) {
            Log.e("TAG", "MTA start failed");
        }
        sIsScreenLarge = getResources().getConfiguration().smallestScreenWidthDp >= 600;
        sScreenDensity = getResources().getDisplayMetrics().density;
        this.mIconCache = new IconCache(this);
        AppInfoManager.createInstance(this);
        MessageManager.createInstance(this);
        MarketManager.createInstance(this);
        this.mModel = new LauncherModel(this, this.mIconCache);
        IntentFilter filter = new IntentFilter("android.intent.action.PACKAGE_ADDED");
        filter.addAction("android.intent.action.PACKAGE_REMOVED");
        filter.addAction("android.intent.action.PACKAGE_CHANGED");
        filter.addDataScheme(a.d);
        registerReceiver(this.mModel, filter);
        IntentFilter filter2 = new IntentFilter();
        filter2.addAction("android.intent.action.EXTERNAL_APPLICATIONS_AVAILABLE");
        filter2.addAction("android.intent.action.EXTERNAL_APPLICATIONS_UNAVAILABLE");
        filter2.addAction("android.intent.action.LOCALE_CHANGED");
        filter2.addAction("android.intent.action.CONFIGURATION_CHANGED");
        registerReceiver(this.mModel, filter2);
        IntentFilter filter3 = new IntentFilter();
        filter3.addAction("android.search.action.GLOBAL_SEARCH_ACTIVITY_CHANGED");
        registerReceiver(this.mModel, filter3);
        IntentFilter filter4 = new IntentFilter();
        filter4.addAction("android.search.action.SEARCHABLES_CHANGED");
        registerReceiver(this.mModel, filter4);
        getContentResolver().registerContentObserver(Favorites.CONTENT_URI, true, this.mFavoritesObserver);
        this.mServiceConn = new ServiceConnection() {
            public void onServiceDisconnected(ComponentName name) {
                LauncherApplication.this.mUserInfo = null;
            }

            public synchronized void onServiceConnected(ComponentName name, IBinder service) {
                LauncherApplication.this.mUserInfo = Stub.asInterface(service);
                if (LauncherApplication.this.mUserInfo == null) {
                    Log.d(LauncherApplication.TAG, "connect to passport failed, do it again!!!");
                    LauncherApplication.this.bindPassportService();
                } else {
                    synchronized (LauncherApplication.this.mLock) {
                        LauncherApplication.this.mUserCenterConnected = true;
                        if (LauncherApplication.this.mLauncherRef != null) {
                            Launcher launcher = (Launcher) LauncherApplication.this.mLauncherRef.get();
                            if (!(launcher == null || LauncherApplication.this.mUserCenterCalledOnce || LauncherApplication.this.mUserInfo == null)) {
                                LauncherApplication.this.mUserCenterCalledOnce = true;
                                launcher.onUserCenterConnected(LauncherApplication.this.mUserInfo);
                            }
                        }
                    }
                }
            }
        };
        bindPassportService();
    }

    /* access modifiers changed from: private */
    public void bindPassportService() {
        Intent i = new Intent();
        i.setAction(ACTION_PASSPORT_SERVICE);
        try {
            bindService(i, this.mServiceConn, 1);
        } catch (SecurityException e) {
            Log.e(TAG, "WTF: can't bind to passport service!");
        }
    }

    public void onTerminate() {
        super.onTerminate();
        unregisterReceiver(this.mModel);
        getContentResolver().unregisterContentObserver(this.mFavoritesObserver);
        unbindService(this.mServiceConn);
    }

    /* access modifiers changed from: 0000 */
    public LauncherModel setLauncher(Launcher launcher) {
        synchronized (this.mLock) {
            this.mLauncherRef = new WeakReference<>(launcher);
            if (this.mUserCenterConnected && !this.mUserCenterCalledOnce && this.mUserInfo != null) {
                this.mUserCenterCalledOnce = true;
                launcher.onUserCenterConnected(this.mUserInfo);
            }
        }
        this.mModel.initialize(launcher);
        return this.mModel;
    }

    /* access modifiers changed from: 0000 */
    public IconCache getIconCache() {
        return this.mIconCache;
    }

    /* access modifiers changed from: 0000 */
    public LauncherModel getModel() {
        return this.mModel;
    }

    /* access modifiers changed from: 0000 */
    public void setLauncherProvider(LauncherProvider provider) {
        this.mLauncherProvider = new WeakReference<>(provider);
    }

    /* access modifiers changed from: 0000 */
    public LauncherProvider getLauncherProvider() {
        return (LauncherProvider) this.mLauncherProvider.get();
    }

    public static boolean isScreenLarge() {
        return sIsScreenLarge;
    }

    public static boolean isScreenLandscape(Context context) {
        return context.getResources().getConfiguration().orientation == 2;
    }

    public static float getScreenDensity() {
        return sScreenDensity;
    }

    public UserInfo getUserInfo() {
        return this.mUserInfo;
    }

    private void initGlobalConfig(Context ctx) {
        GlobalInfo.setGUID(AppUtils.getGUID());
        GlobalInfo.setQQ(StatConstants.MTA_COOPERATION_TAG);
        GlobalInfo.setOpenID(StatConstants.MTA_COOPERATION_TAG);
        GlobalInfo.setOpenIDType(StatConstants.MTA_COOPERATION_TAG);
        GlobalInfo.setLicenseID(StatConstants.MTA_COOPERATION_TAG);
        GlobalInfo.setMACAdress(AppUtils.getLocalMacAddress(ctx));
        GlobalInfo.setIPInfo(AppUtils.getLocalIpAddress());
        GlobalInfo.setDeviceID(AppUtils.getDeviceID());
        GlobalInfo.setAppVersion(AppUtils.getAppVersionName(ctx));
        GlobalInfo.setVersionCode(AppUtils.getAppVersionCode(ctx));
        GlobalInfo.setSysVesion(AppUtils.getSystemVersion());
        GlobalInfo.setAppInstallTime(AppUtils.getAppInstallTime(ctx));
        GlobalInfo.setSdkNum(AppUtils.getSdkVersion());
        GlobalInfo.setUserAgent(getUserAgent());
        GlobalInfo.setMD5(AppUtils.getMD5());
        GlobalInfo.setResource(this);
        GlobalInfo.setPackageName(this);
        GlobalInfo.setQua();
    }

    private String getUserAgent() {
        WebView webview = new WebView(this);
        webview.layout(0, 0, 0, 0);
        return webview.getSettings().getUserAgentString();
    }

    private void initMTAConfig(boolean isDebugMode) {
        if (isDebugMode) {
            StatConfig.setDebugEnable(true);
            StatConfig.setReportEventsByOrder(false);
            StatConfig.setStatSendStrategy(StatReportStrategy.INSTANT);
            return;
        }
        StatConfig.setReportEventsByOrder(true);
        StatConfig.setDebugEnable(false);
        StatConfig.setAutoExceptionCaught(true);
        StatConfig.setStatSendStrategy(StatReportStrategy.BATCH);
    }

    private void reportEven() {
        ReportHelper.reportAppCoolStartAction(sContext, System.currentTimeMillis());
    }
}
