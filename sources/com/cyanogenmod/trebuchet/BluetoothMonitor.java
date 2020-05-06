package com.cyanogenmod.trebuchet;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import com.cyanogenmod.trebuchet.ConnectivityService.IConnectivityMonitor;
import java.lang.ref.WeakReference;

public class BluetoothMonitor extends BroadcastReceiver implements IConnectivityMonitor {
    private String TAG = "BluetoothMonitor";
    private WeakReference<Context> mContext;
    private IBluetoothUpdateListener mListener;

    public interface IBluetoothUpdateListener {
        void onUpdateBluetooth(Bundle bundle);
    }

    public BluetoothMonitor(Context context, IBluetoothUpdateListener listener) {
        this.mContext = new WeakReference<>(context);
        this.mListener = listener;
    }

    public void startMonitor() {
        Context context = (Context) this.mContext.get();
        if (context != null) {
            IntentFilter filter = new IntentFilter();
            filter.addAction("android.bluetooth.adapter.action.STATE_CHANGED");
            filter.addAction("android.bluetooth.adapter.action.CONNECTION_STATE_CHANGED");
            context.registerReceiver(this, filter);
            this.mListener.onUpdateBluetooth(getBluetoothStateBundle());
        }
    }

    public void stopMonitor() {
        Context context = (Context) this.mContext.get();
        if (context != null) {
            try {
                context.unregisterReceiver(this);
            } catch (IllegalArgumentException e) {
                Log.e(this.TAG, "cathed exception:Unable to stop bluetooth monitor", e);
            }
        }
    }

    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.bluetooth.adapter.action.STATE_CHANGED") || intent.getAction().equals("android.bluetooth.adapter.action.CONNECTION_STATE_CHANGED")) {
            Log.d(this.TAG, "receive bluetooth broadcast:action=" + intent.getAction());
            Log.d(this.TAG, "bluetooth state changed:" + intent.getExtras().getInt("android.bluetooth.adapter.extra.STATE"));
            this.mListener.onUpdateBluetooth(intent.getExtras());
        }
    }

    public int getBluetoothState() {
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter != null) {
            return mBluetoothAdapter.getState();
        }
        Log.d(this.TAG, "bluetooth is null");
        return -1;
    }

    private Bundle getBluetoothStateBundle() {
        Bundle bundle = new Bundle();
        bundle.putInt("android.bluetooth.adapter.extra.STATE", getBluetoothState());
        return bundle;
    }
}
