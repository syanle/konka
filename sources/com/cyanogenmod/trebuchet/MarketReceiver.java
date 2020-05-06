package com.cyanogenmod.trebuchet;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class MarketReceiver extends BroadcastReceiver {
    public static final String MARKET_UPGRADE_ACTION = "com.konka.market.main";
    public static final String MARKET_UPGRADE_SIZE = "UpgradeSize";
    public static final String TAG = "MarketReceiver";

    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Log.v(TAG, "Receive Broadcast:" + action);
        if (action.equals("com.konka.market.main")) {
            MarketManager marketManager = MarketManager.getInstance();
            if (marketManager == null) {
                Log.v(TAG, "marketmanager is null");
            } else {
                marketManager.updateMarketView(intent.getExtras().getInt(MARKET_UPGRADE_SIZE));
            }
        }
    }
}
