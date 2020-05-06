package com.konka.multiusers;

import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManagerNative;
import android.app.Dialog;
import android.app.IActivityManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Process;
import android.os.RemoteException;
import android.os.UserHandle;
import android.os.UserManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import com.android.internal.widget.LockPatternUtils;
import com.cyanogenmod.trebuchet.Launcher;
import com.konka.ios7launcher.R;

public class LockScreenDialog extends Dialog {
    private IActivityManager mAm;
    private Context mContext;
    /* access modifiers changed from: private */
    public LockPatternUtils mLockPatternUtils;
    private Button mLogoutCancelBtn;
    private Button mLogoutConfirmBtn;
    /* access modifiers changed from: private */
    public UserManager mUserManager;

    public LockScreenDialog(Context context) {
        super(context);
        this.mContext = context;
    }

    public LockScreenDialog(Context context, int theme) {
        super(context, theme);
        this.mContext = context;
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mutiusers_dialog);
        this.mLockPatternUtils = new LockPatternUtils(this.mContext);
        this.mUserManager = (UserManager) this.mContext.getSystemService("user");
        this.mAm = ActivityManagerNative.getDefault();
        initViews();
    }

    private void initViews() {
        this.mLogoutCancelBtn = (Button) findViewById(R.id.lock_cancel);
        this.mLogoutConfirmBtn = (Button) findViewById(R.id.lock_confirm);
        this.mLogoutConfirmBtn.requestFocus();
        this.mLogoutConfirmBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                Launcher.setScreenLockState(true);
                LockScreenDialog.this.mUserManager.setUserStatus(UserHandle.myUserId(), 1);
                Log.d("LOCK_TEST", "getUserStatus~~~~~~~~~~~~~~~" + LockScreenDialog.this.mUserManager.getUserStatus(UserHandle.myUserId()));
                LockScreenDialog.this.mLockPatternUtils.Show_Locked();
                Log.d("LOCK_TEST", "LOCK~~~~~~~~~~~~~~~");
                LockScreenDialog.this.dismiss();
            }
        });
        this.mLogoutCancelBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                LockScreenDialog.this.dismiss();
            }
        });
    }

    private void logout(int id) {
        String[] strArr;
        try {
            IActivityManager mAm2 = ActivityManagerNative.getDefault();
            for (RunningAppProcessInfo procInfo : mAm2.getRunningAppProcesses()) {
                int userId = UserHandle.getUserId(procInfo.uid);
                if (userId == id) {
                    for (String pkg : procInfo.pkgList) {
                        if (Process.myPid() != procInfo.pid && !pkg.equals("com.android.systemui")) {
                            mAm2.forceStopPackage(pkg, userId);
                        }
                    }
                }
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
