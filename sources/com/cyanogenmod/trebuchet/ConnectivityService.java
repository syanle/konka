package com.cyanogenmod.trebuchet;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import com.cyanogenmod.trebuchet.BatteryMonitor.IBatteryUpdateListener;
import com.cyanogenmod.trebuchet.BluetoothMonitor.IBluetoothUpdateListener;
import com.cyanogenmod.trebuchet.NetworkMonitor.INetworkUpdateListener;
import com.cyanogenmod.trebuchet.TimeMonitor.ITimeUpdateListener;
import com.cyanogenmod.trebuchet.UsbMonitor.IUsbUpdateListener;
import com.cyanogenmod.trebuchet.UserCenterMonitor.IUserCenterUpdateListener;
import com.konka.passport.service.UserInfo;

public class ConnectivityService extends Service {
    public static final String KEY_ACTIVE_NETWORK_ID = "connectivityService.activeNetwork";
    private static final String TAG = "ConnectivityService";
    private static final boolean WILL_MONITOR = true;
    private IBatteryUpdateListener mBatteryListener;
    private BatteryMonitor mBatteryMonitor;
    private final IBinder mBinder = new ConnectivityBinder();
    private IBluetoothUpdateListener mBluetoothListener;
    private BluetoothMonitor mBluetoothMonitor;
    private INetworkUpdateListener mNetworkListener;
    private NetworkMonitor mNetworkMonitor;
    private TimeMonitor mTimeMonitor;
    private ITimeUpdateListener mTimeUpdateListener;
    private IUsbUpdateListener mUsbListener;
    private UsbMonitor mUsbMonitor;
    private IUserCenterUpdateListener mUserCenterListener;
    private UserCenterMonitor mUserCenterMonitor;

    public class ConnectivityBinder extends Binder {
        public ConnectivityBinder() {
        }

        /* access modifiers changed from: 0000 */
        public ConnectivityService getService() {
            return ConnectivityService.this;
        }
    }

    public interface IConnectivityMonitor {
        void startMonitor();

        void stopMonitor();
    }

    public ConnectivityService registerTimeListener(ITimeUpdateListener listener) {
        this.mTimeUpdateListener = listener;
        return this;
    }

    public ConnectivityService registerUserCenterListener(IUserCenterUpdateListener listener) {
        this.mUserCenterListener = listener;
        return this;
    }

    public ConnectivityService registerNetworkListener(INetworkUpdateListener listener) {
        this.mNetworkListener = listener;
        return this;
    }

    public ConnectivityService registerUsbListener(IUsbUpdateListener listener) {
        this.mUsbListener = listener;
        return this;
    }

    public ConnectivityService registerBatteryListener(IBatteryUpdateListener listener) {
        this.mBatteryListener = listener;
        return this;
    }

    public ConnectivityService registerBluetoothListener(IBluetoothUpdateListener listener) {
        this.mBluetoothListener = listener;
        return this;
    }

    public void onUserCenterConnected(UserInfo userInfo) {
        this.mUserCenterMonitor.startQuery(userInfo);
    }

    public void startMonitors() {
        if (this.mTimeUpdateListener != null && this.mTimeMonitor == null) {
            this.mTimeMonitor = new TimeMonitor(this, this.mTimeUpdateListener);
        }
        this.mTimeMonitor.startMonitor();
        if (this.mUserCenterListener != null && this.mUserCenterMonitor == null) {
            this.mUserCenterMonitor = new UserCenterMonitor(this, this.mUserCenterListener);
        }
        this.mUserCenterMonitor.startMonitor();
        if (this.mNetworkListener != null && this.mNetworkMonitor == null) {
            this.mNetworkMonitor = new NetworkMonitor(this, this.mNetworkListener);
        }
        this.mNetworkMonitor.startMonitor();
        if (this.mUsbListener != null && this.mUsbMonitor == null) {
            this.mUsbMonitor = new UsbMonitor(this, this.mUsbListener);
        }
        this.mUsbMonitor.startMonitor();
        if (this.mBatteryListener != null && this.mBatteryMonitor == null) {
            this.mBatteryMonitor = new BatteryMonitor(this, this.mBatteryListener);
        }
        Log.d(TAG, "batterySupported:" + BatteryMonitor.isBatterySupported());
        if (BatteryMonitor.isBatterySupported()) {
            if (this.mBatteryListener != null) {
                this.mBatteryListener.onBatterySupported(true);
            }
            this.mBatteryMonitor.startMonitor();
        }
        if (this.mBluetoothListener != null && this.mBluetoothMonitor == null) {
            this.mBluetoothMonitor = new BluetoothMonitor(this, this.mBluetoothListener);
        }
        this.mBluetoothMonitor.startMonitor();
    }

    private void stopMonitors() {
        if (this.mTimeMonitor != null) {
            this.mTimeMonitor.stopMonitor();
        }
        if (this.mUserCenterMonitor != null) {
            this.mUserCenterMonitor.stopMonitor();
        }
        if (this.mNetworkMonitor != null) {
            this.mNetworkMonitor.stopMonitor();
        }
        if (this.mUsbMonitor != null) {
            this.mUsbMonitor.stopMonitor();
        }
        if (this.mBatteryMonitor != null) {
            this.mBatteryMonitor.stopMonitor();
        }
        if (this.mBluetoothMonitor != null) {
            this.mBluetoothMonitor.stopMonitor();
        }
    }

    public void onCreate() {
        super.onCreate();
    }

    public void onDestroy() {
        stopMonitors();
        super.onDestroy();
    }

    public IBinder onBind(Intent intent) {
        return this.mBinder;
    }

    public boolean onUnbind(Intent intent) {
        stopMonitors();
        return super.onUnbind(intent);
    }
}
