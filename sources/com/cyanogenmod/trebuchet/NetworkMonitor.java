package com.cyanogenmod.trebuchet;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;
import android.util.Slog;
import com.android.internal.util.AsyncChannel;
import com.cyanogenmod.trebuchet.ConnectivityService.IConnectivityMonitor;
import com.konka.android.net.NetworkUtils;
import com.konka.android.net.ethernet.EthernetDevInfo;
import com.konka.android.net.ethernet.EthernetManager;
import com.konka.android.net.pppoe.PppoeManager;
import com.konka.android.net.wifi.KKWifiManager;
import com.konka.android.tv.KKCommonManager;
import com.konka.kkinterface.tv.ChannelDesk;
import com.tencent.stat.common.StatConstants;
import java.lang.ref.WeakReference;
import java.net.InetAddress;

public class NetworkMonitor extends BroadcastReceiver implements IConnectivityMonitor {
    private static final boolean DEBUG = true;
    private static final int ETHERNET_IP_CHANGED = 1;
    private static final int ETHERNET_IP_NOT_CHANGED = 0;
    public static final int ID_INTERFACE_ETHERNET = 0;
    public static final int ID_INTERFACE_WIFI = 1;
    public static final int ID_STATUS_CONNECTED = 2;
    public static final int ID_STATUS_DISCONNECTED = 0;
    public static final int ID_STATUS_UNREACHABLE = 1;
    private static final int INET_CONDITION_THRESHOLD = 50;
    public static final String KEY_NET_ETHERNET_IP = "NetworkMonitor.ethernetIp";
    public static final String KEY_NET_INFO = "NetworkMonitor.networkInfo";
    public static final String KEY_NET_INTERFACE_ID = "NetworkMonitor.interfaceId";
    public static final String KEY_NET_STATUS_ID = "NetworkMonitor.Status";
    public static final String KEY_NET_WIFI_APEXIST = "NetworkMonitor.wifi.hasAP";
    public static final String KEY_NET_WIFI_CONDICTION = "NetworkMonitor.wifi.Condiction";
    public static final String KEY_NET_WIFI_HASDEVICE = "NetworkMonitor.wifi.hasDevice";
    public static final String KEY_NET_WIFI_LEVEL = "NetworkMonitor.wifi.level";
    public static final String KEY_NET_WIFI_SSID = "NetworkMonitor.wifi.ssid";
    private static final int MSG_SET_NET_REACHABLE_STATUS = 1001;
    private static final int MSG_TEST_REACHABLE = 2001;
    private static final int REACHABLE_TEST_DELAY_MILLIS = 5000;
    private static final String TAG = "Krebuchet.NetworkMonitor";
    private static final int mWifiLevelCount = 5;
    private final String PPPOE_STATE_ACTION = "com.mstar.android.pppoe.PPPOE_STATE_ACTION";
    /* access modifiers changed from: private */
    public boolean isEthernetIpChanged = true;
    /* access modifiers changed from: private */
    public int mActiveInterface = 0;
    private NetworkInfo mActiveNetworkInfo;
    /* access modifiers changed from: private */
    public int mActiveStatus = 0;
    private final WeakReference<Context> mContextRef;
    private final Bundle mCurrentConnectivity = new Bundle();
    /* access modifiers changed from: private */
    public boolean mEthHWConnected;
    /* access modifiers changed from: private */
    public boolean mEthReachable = true;
    private String mEthernetIp;
    private EthernetManager mEthernetManager;
    private boolean mHasWifiDevice;
    private int mInetCondition = 0;
    /* access modifiers changed from: private */
    public final INetworkUpdateListener mListener;
    /* access modifiers changed from: private */
    public final NetworkMonitorHandler mMonitorHandler;
    private HandlerThread mMonitorThread;
    private MonitorThreadHandler mMonitorThreadHandler;
    /* access modifiers changed from: private */
    public int mWifiActivity = 0;
    private boolean mWifiApExist = false;
    /* access modifiers changed from: private */
    public final AsyncChannel mWifiChannel;
    private boolean mWifiConnected;
    private boolean mWifiEnabled;
    private int mWifiLevel;
    private final WifiManager mWifiManager;
    private int mWifiRssi;
    private String mWifiSsid;

    public interface INetworkUpdateListener {
        void onUpdateNetworkConnectivity(Bundle bundle);

        void onUpdateWifiActivity(int i);
    }

    private final class MonitorThreadHandler extends Handler {
        public MonitorThreadHandler(Looper looper) {
            super(looper);
        }

        public void handleMessage(Message msg) {
            int newStatus;
            int i = 1;
            switch (msg.what) {
                case NetworkMonitor.MSG_TEST_REACHABLE /*2001*/:
                    if (NetworkMonitor.this.testReachable()) {
                        newStatus = 2;
                    } else {
                        newStatus = 1;
                    }
                    Message m = NetworkMonitor.this.mMonitorHandler.obtainMessage(NetworkMonitor.MSG_SET_NET_REACHABLE_STATUS);
                    m.arg1 = newStatus;
                    if (!NetworkMonitor.this.isEthernetIpChanged) {
                        i = 0;
                    }
                    m.arg2 = i;
                    Log.d("huangfeng", "m.arg1=" + m.arg1);
                    NetworkMonitor.this.mMonitorHandler.sendMessage(m);
                    sendEmptyMessageDelayed(NetworkMonitor.MSG_TEST_REACHABLE, 5000);
                    return;
                default:
                    return;
            }
        }
    }

    private final class NetworkMonitorHandler extends Handler {
        private NetworkMonitorHandler() {
        }

        /* synthetic */ NetworkMonitorHandler(NetworkMonitor networkMonitor, NetworkMonitorHandler networkMonitorHandler) {
            this();
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case NetworkMonitor.MSG_SET_NET_REACHABLE_STATUS /*1001*/:
                    if (NetworkMonitor.this.mActiveInterface == 0 && NetworkMonitor.this.mEthHWConnected) {
                        NetworkMonitor.this.mActiveStatus = msg.arg1;
                        Log.d("huangfeng", "NetworkMonitorHandler mActiveStatus=" + NetworkMonitor.this.mActiveStatus);
                        boolean newReachable = NetworkMonitor.this.mActiveStatus == 2;
                        Log.d("huangfeng", "NetworkMonitorHandler mEthReachable=" + NetworkMonitor.this.mEthReachable);
                        if (NetworkMonitor.this.mListener == null) {
                            return;
                        }
                        if (NetworkMonitor.this.mEthReachable != newReachable || msg.arg2 == 1) {
                            Slog.d("wzd_wire", "MSG_SET_NET_REACHABLE_STATUS");
                            NetworkMonitor.this.mEthReachable = newReachable;
                            Log.d("huangfeng", "NetworkMonitorHandler onUpdateNetworkConnectivity");
                            Log.d("czj-ios", "onUpdateNetworkConnectivity at handle MSG_SET_NET_REACHABLE_STATUS");
                            NetworkMonitor.this.mListener.onUpdateNetworkConnectivity(NetworkMonitor.this.getCurrentConnectivityInfo());
                            return;
                        }
                        return;
                    }
                    return;
                default:
                    return;
            }
        }
    }

    private final class WifiHandler extends Handler {
        private WifiHandler() {
        }

        /* synthetic */ WifiHandler(NetworkMonitor networkMonitor, WifiHandler wifiHandler) {
            this();
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    if (msg.arg1 != NetworkMonitor.this.mWifiActivity) {
                        NetworkMonitor.this.mWifiActivity = msg.arg1;
                        if (NetworkMonitor.this.mListener != null) {
                            NetworkMonitor.this.mListener.onUpdateWifiActivity(NetworkMonitor.this.mWifiActivity);
                            return;
                        }
                        return;
                    }
                    return;
                case 69632:
                    if (msg.arg1 == 0) {
                        NetworkMonitor.this.mWifiChannel.sendMessage(Message.obtain(this, 69633));
                        return;
                    } else {
                        Slog.e(NetworkMonitor.TAG, "Failed to connect to wifi");
                        return;
                    }
                default:
                    return;
            }
        }
    }

    public NetworkMonitor(Context context, INetworkUpdateListener iListener) {
        this.mContextRef = new WeakReference<>(context);
        this.mListener = iListener;
        this.mMonitorHandler = new NetworkMonitorHandler(this, null);
        try {
            this.mEthernetManager = EthernetManager.getInstance(context);
        } catch (NoClassDefFoundError e) {
        }
        this.mWifiManager = (WifiManager) context.getSystemService("wifi");
        Handler handler = new WifiHandler(this, null);
        this.mWifiChannel = new AsyncChannel();
        Messenger wifiMessenger = this.mWifiManager.getWifiServiceMessenger();
        if (wifiMessenger != null) {
            this.mWifiChannel.connect(context, handler, wifiMessenger);
        }
    }

    public void startMonitor() {
        this.mMonitorThread = new HandlerThread("NetworkMonitor", 10);
        this.mMonitorThread.start();
        this.mMonitorThreadHandler = new MonitorThreadHandler(this.mMonitorThread.getLooper());
        IntentFilter filter = new IntentFilter();
        EthernetManager.addFilterActions(filter);
        filter.addAction("android.net.wifi.RSSI_CHANGED");
        filter.addAction("android.net.wifi.WIFI_STATE_CHANGED");
        filter.addAction("android.net.wifi.STATE_CHANGE");
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        filter.addAction("android.net.conn.INET_CONDITION_ACTION");
        filter.addAction("android.intent.action.CONFIGURATION_CHANGED");
        filter.addAction("com.mstar.android.pppoe.PPPOE_STATE_ACTION");
        Context context = (Context) this.mContextRef.get();
        if (context != null) {
            context.registerReceiver(this, filter);
        }
    }

    public void stopMonitor() {
        Context context = (Context) this.mContextRef.get();
        if (context != null) {
            try {
                context.unregisterReceiver(this);
            } catch (IllegalArgumentException e) {
                Log.e(TAG, "cathed exception:Unable to stop NetworkMonitor", e);
            }
        }
        this.mMonitorThread.quit();
    }

    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Log.d("czj-ios", "receive broadcast : " + action);
        if (EthernetManager.isMatchFilterActions(intent)) {
            updateEthernetState(intent);
            this.mMonitorThreadHandler.removeMessages(MSG_TEST_REACHABLE);
            if (this.mEthHWConnected) {
                this.mMonitorThreadHandler.sendEmptyMessage(MSG_TEST_REACHABLE);
            } else if (this.mListener != null) {
                Slog.d("wzd_wire", "WIRE DISCONNECTED");
                Log.d("czj-ios", "onUpdateNetworkConnectivity at receive EthernetManager.isMatchFilterActions");
                this.mListener.onUpdateNetworkConnectivity(getCurrentConnectivityInfo());
            }
        } else if (action.equals("android.net.wifi.RSSI_CHANGED") || action.equals("android.net.wifi.WIFI_STATE_CHANGED") || action.equals("android.net.wifi.STATE_CHANGE")) {
            updateWifiState(intent);
            if (this.mListener != null) {
                Slog.d("wzd_wire", "NETWORK_STATE_CHANGED_ACTION");
                Log.d("czj-ios", "onUpdateNetworkConnectivity at receive RSSI_CHANGED_ACTION");
                this.mListener.onUpdateNetworkConnectivity(getCurrentConnectivityInfo());
            }
        } else if (action.equals("android.net.conn.CONNECTIVITY_CHANGE") || action.equals("android.net.conn.INET_CONDITION_ACTION")) {
            if (action.equals("android.net.conn.CONNECTIVITY_CHANGE")) {
                NetworkInfo activeNetInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
                int activeNetType = -1;
                if (activeNetInfo != null) {
                    activeNetType = activeNetInfo.getType();
                }
                Log.d("huangfeng", "NetworkMonitor onReceive activeNetType=" + activeNetType);
                Log.d("huangfeng", "NetworkMonitor onReceive PppoeManager.TYPE_PPPOE=14");
                Log.d("huangfeng", "NetworkMonitor onReceive EthernetManager.TYPE_ETHERNET=9");
                if (activeNetType == 9 || isPppoeNetType(activeNetType)) {
                    this.mEthHWConnected = true;
                    this.mMonitorThreadHandler.sendEmptyMessage(MSG_TEST_REACHABLE);
                }
            }
            updateConnectivity(intent);
            if (this.mListener != null) {
                Slog.d("wzd_wire", "CONNECTIVITY_ACTION|INET_CONDITION_ACTION");
                Log.d("czj-ios", "onUpdateNetworkConnectivity at receive CONNECTIVITY_ACTION");
                this.mListener.onUpdateNetworkConnectivity(getCurrentConnectivityInfo());
            }
        } else if (action.equals("android.intent.action.CONFIGURATION_CHANGED") && this.mListener != null) {
            Slog.d("wzd_wire", "ACTION_CONFIGURATION_CHANGED");
            Log.d("czj-ios", "onUpdateNetworkConnectivity at receive ACTION_CONFIGURATION_CHANGED");
            this.mListener.onUpdateNetworkConnectivity(getCurrentConnectivityInfo());
        }
    }

    private void updateEthernetState(Intent intent) {
        this.mEthHWConnected = EthernetManager.isCableConnected(intent);
        updateActiveNetwork();
    }

    /* access modifiers changed from: private */
    public boolean testReachable() {
        String str;
        if (this.mEthernetManager == null) {
            Log.d("czj-ios", "mEthernetManager == null");
            return false;
        } else if (this.mActiveNetworkInfo != null && isPppoeNetType(this.mActiveNetworkInfo.getType())) {
            return true;
        } else {
            if (!this.mEthernetManager.isConfigured()) {
                Log.d("czj-ios", "!mEthernetManager.isConfigured()");
                return false;
            }
            EthernetDevInfo info = this.mEthernetManager.getConfig();
            String ip = info.getRouteAddr();
            if (info.getIpAddress() == null || !info.getIpAddress().equals(this.mEthernetIp)) {
                this.isEthernetIpChanged = true;
                this.mEthernetIp = info.getIpAddress();
            } else {
                this.isEthernetIpChanged = false;
            }
            String str2 = "czj-ios";
            if (("testReachable ip:" + ip) == null) {
                str = "null";
            } else {
                str = ip;
            }
            Log.d(str2, str);
            if (ip == null || ip.equals(StatConstants.MTA_COOPERATION_TAG) || ip.equals("0.0.0.0")) {
                return false;
            }
            try {
                return InetAddress.getByName(ip).isReachable(ChannelDesk.max_dtv_count) || NetworkUtils.pingHost(ip, 2);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
    }

    private void updateWifiState(Intent intent) {
        boolean z = true;
        this.mHasWifiDevice = true;
        if (!KKWifiManager.getInstance(this.mWifiManager).isDeviceExist()) {
            this.mHasWifiDevice = false;
        }
        this.mWifiApExist = false;
        if (this.mWifiManager.getScanResults() != null) {
            this.mWifiApExist = true;
        }
        String action = intent.getAction();
        if (action.equals("android.net.wifi.WIFI_STATE_CHANGED")) {
            if (intent.getIntExtra("wifi_state", 4) != 3) {
                z = false;
            }
            this.mWifiEnabled = z;
            int wifiLevel = 0;
            try {
                wifiLevel = KKWifiManager.getInstance(this.mWifiManager).getWifiLevel();
            } catch (NoSuchMethodError e) {
                Log.d(TAG, "no such method of getWifiLevel");
            }
            if (wifiLevel > 0) {
                this.mWifiLevel = wifiLevel;
            }
            Log.d(TAG, "#####mWifiLevel: " + this.mWifiLevel);
        } else if (action.equals("android.net.wifi.STATE_CHANGE")) {
            NetworkInfo networkInfo = (NetworkInfo) intent.getParcelableExtra("networkInfo");
            boolean wasConnected = this.mWifiConnected;
            if (networkInfo == null || !networkInfo.isConnected()) {
                z = false;
            }
            this.mWifiConnected = z;
            if (this.mWifiConnected && !wasConnected) {
                WifiInfo info = (WifiInfo) intent.getParcelableExtra("wifiInfo");
                if (info == null) {
                    info = this.mWifiManager.getConnectionInfo();
                }
                if (info != null) {
                    this.mWifiSsid = huntForSsid(info);
                } else {
                    this.mWifiSsid = null;
                }
            } else if (!this.mWifiConnected) {
                this.mWifiSsid = null;
            }
            this.mWifiLevel = 0;
            this.mWifiRssi = -200;
            int wifiLevel2 = 0;
            try {
                wifiLevel2 = KKWifiManager.getInstance(this.mWifiManager).getWifiLevel();
            } catch (NoSuchMethodError e2) {
                Log.d(TAG, "no such method of getWifiLevel");
            }
            if (wifiLevel2 > 0) {
                this.mWifiLevel = wifiLevel2;
            }
            Log.d(TAG, "#####mWifiLevel: " + this.mWifiLevel);
        } else if (action.equals("android.net.wifi.RSSI_CHANGED") && this.mWifiConnected) {
            this.mWifiRssi = intent.getIntExtra("newRssi", -200);
            this.mWifiLevel = WifiManager.calculateSignalLevel(this.mWifiRssi, 5);
        }
        updateActiveNetwork();
    }

    private String huntForSsid(WifiInfo info) {
        String ssid = info.getSSID();
        if (ssid != null) {
            return ssid;
        }
        for (WifiConfiguration net2 : this.mWifiManager.getConfiguredNetworks()) {
            if (net2.networkId == info.getNetworkId()) {
                return net2.SSID;
            }
        }
        return null;
    }

    /* access modifiers changed from: private */
    public Bundle getCurrentConnectivityInfo() {
        Slog.d("wzd_wire", "currentNetInfo: if=" + this.mActiveInterface + ", status=" + this.mActiveStatus);
        Log.v("huangfeng", "in getCurrentConnectivityInfo");
        Log.v("huangfeng", "CurrentConnectivityInfo mActiveInterface=" + this.mActiveInterface);
        Log.v("huangfeng", "CurrentConnectivityInfo mActiveStatus=" + this.mActiveStatus);
        Log.v("huangfeng", "CurrentConnectivityInfo mEthernetIp=" + this.mEthernetIp);
        this.mCurrentConnectivity.putInt(KEY_NET_INTERFACE_ID, this.mActiveInterface);
        this.mCurrentConnectivity.putInt(KEY_NET_STATUS_ID, this.mActiveStatus);
        this.mCurrentConnectivity.putString(KEY_NET_ETHERNET_IP, this.mEthernetIp);
        this.mCurrentConnectivity.putParcelable(KEY_NET_INFO, this.mActiveNetworkInfo);
        if (this.mActiveInterface == 1) {
            Slog.d("wzd_wire", "currentNetInfo: mInetCondiction=" + this.mInetCondition + ", wifi level=" + this.mWifiLevel);
            this.mCurrentConnectivity.putInt(KEY_NET_WIFI_CONDICTION, this.mInetCondition);
            this.mCurrentConnectivity.putInt(KEY_NET_WIFI_LEVEL, this.mWifiLevel);
            this.mCurrentConnectivity.putString(KEY_NET_WIFI_SSID, this.mWifiSsid);
            this.mCurrentConnectivity.putBoolean(KEY_NET_WIFI_APEXIST, this.mWifiApExist);
            this.mCurrentConnectivity.putBoolean(KEY_NET_WIFI_HASDEVICE, this.mHasWifiDevice);
        }
        return this.mCurrentConnectivity;
    }

    private void updateActiveNetwork() {
        int i = 0;
        int activeNetType = -1;
        Context ctx = (Context) this.mContextRef.get();
        if (ctx != null) {
            NetworkInfo info = ((ConnectivityManager) ctx.getSystemService("connectivity")).getActiveNetworkInfo();
            if (info != null) {
                this.mActiveNetworkInfo = info;
                activeNetType = info.getType();
                Log.d("huangfeng", "updateActiveNetwork activeNetType=" + activeNetType);
                Log.d("huangfeng", "updateActiveNetwork PppoeManager.TYPE_PPPOE=14");
                Log.d("huangfeng", "updateActiveNetwork EthernetManager.TYPE_ETHERNET=9");
            }
        }
        if (activeNetType > 0) {
            if (isPppoeNetType(activeNetType) || activeNetType == 9) {
                this.mEthHWConnected = true;
            }
            if (activeNetType == 1) {
                i = 1;
            }
            this.mActiveInterface = i;
            Log.d("huangfeng", "updateActiveNetwork mActiveInterface=" + this.mActiveInterface);
            if (this.mActiveInterface == 1) {
                updateActiveWifi();
            } else {
                updateActiveEthernet();
            }
        } else if (this.mWifiEnabled) {
            Log.d("huangfeng", "updateActiveNetwork mWidiEnabled=" + this.mWifiEnabled);
            this.mActiveInterface = 1;
            updateActiveWifi();
        } else {
            this.mActiveInterface = 0;
            updateActiveEthernet();
        }
    }

    private void updateActiveWifi() {
        Log.d("huangfeng", "updateActiveWifi mWifiConnected=" + this.mWifiConnected);
        if (this.mWifiConnected) {
            this.mActiveStatus = 2;
        } else {
            this.mActiveStatus = 1;
        }
        Log.d("huangfeng", "updateActiveWifi mActiveStatus=" + this.mActiveStatus);
    }

    private void updateActiveEthernet() {
        Log.d("huangfeng", "updateActiveEthernet mEthHWConnected=" + this.mEthHWConnected);
        Log.d("huangfeng", "updateActiveEthernet mEthReachable=" + this.mEthReachable);
        if (this.mEthHWConnected) {
            try {
                if (isPppoeNetType(this.mActiveNetworkInfo.getType())) {
                    this.mEthernetIp = PppoeManager.getInstance((Context) this.mContextRef.get()).getConfig().getIpAddress();
                }
            } catch (Exception e) {
            }
            if (this.mEthReachable) {
                Log.d("huangfeng", "updateActiveEthernet mEthReachable is ture");
                this.mActiveStatus = 2;
            } else {
                Log.d("huangfeng", "updateActiveEthernet mEthReachable is false");
                this.mActiveStatus = 1;
            }
        } else {
            this.mActiveStatus = 0;
        }
        Log.d("huangfeng", "updateActiveEthernet mActiveStatus=" + this.mActiveStatus);
    }

    private void updateConnectivity(Intent intent) {
        int i = 0;
        Slog.d(TAG, "updateConnectivity: intent=" + intent);
        NetworkInfo info = (NetworkInfo) intent.getParcelableExtra("networkInfo");
        int connectionStatus = intent.getIntExtra("inetCondition", 0);
        Slog.d(TAG, "updateConnectivity: networkInfo=" + info);
        Slog.d(TAG, "updateConnectivity: connectionStatus=" + connectionStatus);
        Log.d("huangfeng", "updateConnectivity:connectionStatus=" + connectionStatus);
        if (connectionStatus > INET_CONDITION_THRESHOLD) {
            i = 1;
        }
        this.mInetCondition = i;
        updateActiveNetwork();
    }

    private boolean isPppoeNetType(int netType) {
        String str = StatConstants.MTA_COOPERATION_TAG;
        Context context = (Context) this.mContextRef.get();
        if (context == null) {
            return false;
        }
        if (KKCommonManager.getInstance(context.getApplicationContext()).getPlatform().equals("hisiv500")) {
            if (netType == 14 || netType == 15) {
                return true;
            }
            return false;
        } else if (netType == 14 || netType == 18) {
            return true;
        } else {
            return false;
        }
    }
}
