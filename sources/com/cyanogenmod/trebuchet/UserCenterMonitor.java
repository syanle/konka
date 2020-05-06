package com.cyanogenmod.trebuchet;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import com.cyanogenmod.trebuchet.ConnectivityService.IConnectivityMonitor;
import com.konka.passport.service.PassportInfo;
import com.konka.passport.service.UserInfo;
import com.tencent.stat.common.StatConstants;
import java.lang.ref.WeakReference;

public class UserCenterMonitor extends BroadcastReceiver implements IConnectivityMonitor {
    private static final String ACTION_PASSPORT_MSG = "com.konka.message.PASSPORT_MSG";
    private static final String ACTION_USERLOGIN = "com.konka.passport.USERLOGIN";
    private static final boolean DEBUG = true;
    private static final String EXTRA_COUNT = "count";
    private static final String EXTRA_HAS_MSG = "hasMsg";
    private static final String EXTRA_STATUS = "status";
    private static final String EXTRA_VALUE_LOGIN = "login";
    public static final String KEY_USERCENTER_AREA = "UserCenterMonitor.area";
    public static final String KEY_USERCENTER_EMAILADDR = "UserCenterMonitor.emailAddr";
    public static final String KEY_USERCENTER_HEADPIC = "UserCenterMonitor.headpic";
    public static final String KEY_USERCENTER_IS_LOGGEDIN = "UserCenterMonitor.isLoggedIn";
    public static final String KEY_USERCENTER_MSG_COUNT = "UserCenterMonitor.msgCount";
    public static final String KEY_USERCENTER_PASSID = "UserCenterMonitor.passid";
    private static final String TAG = "wzd_usercenter";
    private WeakReference<Context> mContextRef;
    private Bundle mCurrentConnectivity = new Bundle();
    /* access modifiers changed from: private */
    public boolean mIsLoggedIn = false;
    private IUserCenterUpdateListener mListener;
    private int mMsgCount = 0;
    /* access modifiers changed from: private */
    public PassportInfo mPassportInfo;
    private UserInfo mUserInfo;

    public interface IUserCenterUpdateListener {
        void onUpdateUserCenterConnectivity(Bundle bundle);
    }

    private static class QueryTask extends AsyncTask<UserInfo, Void, PassportInfo> {
        private final WeakReference<UserCenterMonitor> mMonitorRef;

        public QueryTask(UserCenterMonitor monitor) {
            this.mMonitorRef = new WeakReference<>(monitor);
        }

        /* access modifiers changed from: protected */
        public PassportInfo doInBackground(UserInfo... params) {
            UserInfo userInfo = params[0];
            if (userInfo == null) {
                try {
                    Log.d(UserCenterMonitor.TAG, "GetPassportInfo failed:user Info is NUll");
                } catch (RemoteException e) {
                    Log.e(UserCenterMonitor.TAG, "GetPassportInfo failed:", e);
                    return null;
                } catch (NullPointerException e2) {
                    Log.e(UserCenterMonitor.TAG, "GetPassportInfo failed:", e2);
                    return null;
                } catch (IllegalStateException e3) {
                    Log.e(UserCenterMonitor.TAG, "GetPassportInfo failed:", e3);
                    return null;
                }
            } else {
                PassportInfo passportInfo = new PassportInfo();
                int iRet = userInfo.GetPassportInfo(passportInfo);
                if (iRet == 0) {
                    return passportInfo;
                }
                Log.d(UserCenterMonitor.TAG, "GetPassportInfo failed:" + iRet);
            }
            return null;
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(PassportInfo passportInfo) {
            boolean z = true;
            UserCenterMonitor monitor = (UserCenterMonitor) this.mMonitorRef.get();
            Log.d(UserCenterMonitor.TAG, "query login == " + (passportInfo != null));
            if (monitor != null) {
                if (passportInfo == null) {
                    z = false;
                }
                monitor.mIsLoggedIn = z;
                monitor.mPassportInfo = passportInfo;
                monitor.updateUserCenterState();
            }
        }
    }

    public UserCenterMonitor(Context context, IUserCenterUpdateListener iListener) {
        this.mContextRef = new WeakReference<>(context);
        this.mListener = iListener;
    }

    public void startQuery(UserInfo userinfo) {
        this.mUserInfo = userinfo;
        new QueryTask(this).execute(new UserInfo[]{userinfo});
    }

    public void startMonitor() {
        Context context = (Context) this.mContextRef.get();
        if (context != null) {
            IntentFilter filter = new IntentFilter();
            filter.addAction(ACTION_USERLOGIN);
            filter.addAction("com.konka.message.PASSPORT_MSG");
            context.registerReceiver(this, filter);
            UserInfo userInfo = ((LauncherApplication) context.getApplicationContext()).getUserInfo();
            if (userInfo == null) {
                Log.d(TAG, "userinfo is null");
                return;
            }
            try {
                this.mPassportInfo = new PassportInfo();
                userInfo.GetPassportInfo(this.mPassportInfo);
                this.mIsLoggedIn = this.mPassportInfo.GetPassportId() != null && !this.mPassportInfo.GetPassportId().equals(StatConstants.MTA_COOPERATION_TAG);
                this.mListener.onUpdateUserCenterConnectivity(getCurrentConnectivity());
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public void stopMonitor() {
        Context context = (Context) this.mContextRef.get();
        if (context != null) {
            try {
                context.unregisterReceiver(this);
            } catch (IllegalArgumentException e) {
                Log.e(TAG, "cathed exception:Unable to stop UserCenterMonitor", e);
            }
        }
    }

    public void onReceive(Context context, Intent intent) {
        boolean z;
        if (intent.getAction().equals(ACTION_USERLOGIN)) {
            String status = intent.getStringExtra(EXTRA_STATUS);
            if (status == null || !status.equals(EXTRA_VALUE_LOGIN)) {
                z = false;
            } else {
                z = true;
            }
            this.mIsLoggedIn = z;
            this.mMsgCount = 0;
            if (!this.mIsLoggedIn || this.mUserInfo == null) {
                this.mPassportInfo = null;
            } else {
                new QueryTask(this).execute(new UserInfo[]{this.mUserInfo});
            }
            updateUserCenterState();
        }
    }

    /* access modifiers changed from: private */
    public void updateUserCenterState() {
        if (this.mListener != null) {
            this.mListener.onUpdateUserCenterConnectivity(getCurrentConnectivity());
        }
    }

    private Bundle getCurrentConnectivity() {
        this.mCurrentConnectivity.putBoolean(KEY_USERCENTER_IS_LOGGEDIN, this.mIsLoggedIn);
        this.mCurrentConnectivity.putInt(KEY_USERCENTER_MSG_COUNT, this.mMsgCount);
        if (this.mIsLoggedIn && this.mPassportInfo != null) {
            this.mCurrentConnectivity.putString(KEY_USERCENTER_EMAILADDR, this.mPassportInfo.GetPassportId());
            this.mCurrentConnectivity.putString(KEY_USERCENTER_PASSID, this.mPassportInfo.GetPassportId());
            this.mCurrentConnectivity.putString(KEY_USERCENTER_AREA, this.mPassportInfo.GetIpAddress());
            this.mCurrentConnectivity.putParcelable(KEY_USERCENTER_HEADPIC, this.mPassportInfo.GetHeadPic());
        }
        return this.mCurrentConnectivity;
    }
}
