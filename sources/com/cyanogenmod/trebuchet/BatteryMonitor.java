package com.cyanogenmod.trebuchet;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import com.cyanogenmod.trebuchet.ConnectivityService.IConnectivityMonitor;
import com.konka.ios7launcher.R;
import java.lang.ref.WeakReference;

public class BatteryMonitor extends BroadcastReceiver implements IConnectivityMonitor {
    private static final String TAG = "BatteryMonitor";
    private final WeakReference<Context> mContextRef;
    private final Bundle mCurrentUsage = new Bundle();
    private final IBatteryUpdateListener mListener;

    public interface IBatteryUpdateListener {
        void onBatterySupported(boolean z);

        void onUpdateBatteryUsage(Bundle bundle);
    }

    public BatteryMonitor(Context context, IBatteryUpdateListener listener) {
        this.mContextRef = new WeakReference<>(context);
        this.mListener = listener;
    }

    public void startMonitor() {
        Context ctx = (Context) this.mContextRef.get();
        if (ctx != null) {
            IntentFilter filter = new IntentFilter();
            filter.addAction("android.intent.action.BATTERY_CHANGED");
            ctx.registerReceiver(this, filter);
        }
    }

    public void stopMonitor() {
        Context context = (Context) this.mContextRef.get();
        if (context != null) {
            try {
                context.unregisterReceiver(this);
            } catch (IllegalArgumentException e) {
                Log.e(TAG, "cathed exception:Unable to stop BatteryMonitor", e);
            }
        }
    }

    public static boolean isBatterySupported() {
        return false;
    }

    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BATTERY_CHANGED")) {
            int level = intent.getIntExtra("level", 0);
            int plugType = intent.getIntExtra("plugged", 0);
            int status = intent.getIntExtra("status", 0);
            this.mCurrentUsage.putInt("level", level);
            this.mCurrentUsage.putInt("plugged", plugType);
            this.mCurrentUsage.putInt("status", status);
            if (this.mListener != null) {
                this.mListener.onUpdateBatteryUsage(this.mCurrentUsage);
            }
        }
    }

    public static void setImageByUsage(ImageView v, Bundle usage) {
        int icon;
        boolean plugged = false;
        int plugType = usage.getInt("plugged", 0);
        int level = usage.getInt("level", 0);
        int status = usage.getInt("status", 0);
        if (plugType != 0) {
            plugged = true;
        }
        if (!plugged) {
            icon = R.drawable.stat_sys_battery;
        } else if (plugType != 1 || status == 2 || status == 5) {
            icon = R.drawable.stat_sys_battery_charge;
        } else {
            icon = R.drawable.stat_sys_battery_ac_plugged;
        }
        v.setImageResource(icon);
        v.setImageLevel(level);
    }

    public static void setTextByUsage(TextView v, Bundle usage) {
        int text;
        boolean plugged = false;
        int plugType = usage.getInt("plugged", 0);
        int i = usage.getInt("level", 0);
        int status = usage.getInt("status", 0);
        if (plugType != 0) {
            plugged = true;
        }
        if (!plugged) {
            text = R.drawable.stat_sys_battery;
        } else if (plugType != 1 || status == 2 || status == 5) {
            text = R.drawable.stat_sys_battery_charge;
        } else {
            text = R.drawable.stat_sys_battery_ac_plugged;
        }
        v.setText(text);
    }
}
