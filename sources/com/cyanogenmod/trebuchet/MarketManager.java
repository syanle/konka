package com.cyanogenmod.trebuchet;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.os.IBinder;
import android.util.Log;
import com.konka.ios7launcher.R;
import com.konka.market.upgrade.IUpgrade;
import com.konka.market.upgrade.IUpgrade.Stub;

public class MarketManager {
    public static final int MARKET_ICON_RESOURCE_ID = 2130837568;
    public static final String MARKET_PACKAGE_NAME = "com.konka.market.main";
    public static final String MARKET_UPGRADE_SERVICE_ACTION = "com.konka.market.upgrade.UpgradeService";
    public static final String TAG = "MarketManager";
    private static MarketManager instance = null;
    private static Context mContext = null;
    private ServiceConnection conn = new ServiceConnection() {
        public void onServiceConnected(ComponentName name, IBinder service) {
            MarketManager.this.marketUpgradeService = Stub.asInterface(service);
            Log.d(MarketManager.TAG, "market upgrade service connected success");
            MarketManager.this.updateMarketView();
        }

        public void onServiceDisconnected(ComponentName name) {
            Log.d(MarketManager.TAG, "market upgrade service disconnected");
            MarketManager.this.marketUpgradeService = null;
        }
    };
    private FolderInfo folderInfo = null;
    /* access modifiers changed from: private */
    public IUpgrade marketUpgradeService = null;
    private BubbleTextView marketView = null;

    public static MarketManager createInstance(Context context) {
        if (instance == null) {
            instance = new MarketManager(context);
        }
        return instance;
    }

    public static MarketManager getInstance() {
        return instance;
    }

    private MarketManager(Context context) {
        mContext = context;
        context.bindService(new Intent(MARKET_UPGRADE_SERVICE_ACTION), this.conn, 1);
    }

    public void setMarketView(BubbleTextView view) {
        Log.d(TAG, "set Marketview");
        this.marketView = view;
    }

    public void setMarketFolderInfo(FolderInfo info) {
        Log.d(TAG, "set MarketFolderInfo");
        this.folderInfo = info;
    }

    public void updateMarketView() {
        updateMarketView(getMarketUgradeNumber());
    }

    public void updateMarketView(int number) {
        Log.d(TAG, "update MarketView, number is " + number);
        if (this.marketView == null) {
            Log.d(TAG, "Marketview is null");
            return;
        }
        AppInfoManager appInfoManager = AppInfoManager.getInstance();
        if (appInfoManager == null) {
            Log.d(TAG, "appinfomanager is null");
            return;
        }
        ShortcutInfo shortcutInfo = (ShortcutInfo) this.marketView.getTag();
        Bitmap icon = appInfoManager.setIconCornerNumber(mContext, R.drawable.com_konka_market_main, number);
        shortcutInfo.setIcon(icon);
        this.marketView.setCompoundDrawablesWithIntrinsicBounds(null, new FastBitmapDrawable(icon), null, null);
        if (this.folderInfo != null && shortcutInfo.container != -100 && shortcutInfo.container != -101) {
            this.folderInfo.itemsChanged();
        }
    }

    public int getMarketUgradeNumber() {
        int number = 0;
        if (this.marketUpgradeService == null) {
            Log.d(TAG, "getMarketUgrade failed, marketUpgradeService is null");
            return 0;
        }
        try {
            number = this.marketUpgradeService.getSize();
            Log.d(TAG, "getMarketUgrade success, MarketUgradeNumber is " + number);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return number;
    }
}
