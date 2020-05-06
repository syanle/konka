package com.cyanogenmod.trebuchet;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import com.cyanogenmod.trebuchet.ConnectivityService.IConnectivityMonitor;
import java.lang.ref.WeakReference;
import java.util.TimeZone;

public class TimeMonitor extends BroadcastReceiver implements IConnectivityMonitor {
    public static final String KET_CURRENT_TIME = "TimeMonitor.CurrentTime";
    private static final String TAG = "TimeMonitor";
    public static final String TIME_FORMAT_TYPE = "%Y-%m-%d %H:%M";
    private Time mCalendar;
    private WeakReference<Context> mContextRef;
    private Bundle mCurrentConnectivity = new Bundle();
    private String mCurrentTime;
    private ITimeUpdateListener mListener;

    public interface ITimeUpdateListener {
        void onUpdateTime(Bundle bundle);
    }

    public TimeMonitor(Context context, ITimeUpdateListener listener) {
        this.mContextRef = new WeakReference<>(context);
        this.mListener = listener;
        this.mCalendar = new Time();
    }

    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.TIMEZONE_CHANGED")) {
            String timeZone = intent.getStringExtra("time-zone");
            Log.d("SearchDropTargetBar", timeZone);
            this.mCalendar = new Time(TimeZone.getTimeZone(timeZone).getID());
        }
        updateTime();
    }

    private void updateTime() {
        this.mCalendar.setToNow();
        this.mCurrentTime = this.mCalendar.format(TIME_FORMAT_TYPE);
        if (this.mListener != null) {
            this.mListener.onUpdateTime(getCurrentConnectivity());
        }
    }

    private Bundle getCurrentConnectivity() {
        this.mCurrentConnectivity.putString(KET_CURRENT_TIME, this.mCurrentTime);
        return this.mCurrentConnectivity;
    }

    public void startMonitor() {
        Context context = (Context) this.mContextRef.get();
        if (context != null) {
            IntentFilter filter = new IntentFilter();
            filter.addAction("android.intent.action.TIME_TICK");
            filter.addAction("android.intent.action.TIME_SET");
            filter.addAction("android.intent.action.TIMEZONE_CHANGED");
            context.registerReceiver(this, filter);
        }
    }

    public void stopMonitor() {
        Context context = (Context) this.mContextRef.get();
        if (context != null) {
            try {
                context.unregisterReceiver(this);
            } catch (IllegalArgumentException e) {
                Log.e(TAG, "cathed exception:Unable to stop TimeMonitor", e);
            }
        }
    }
}
