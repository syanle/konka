package com.konka.str;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Handler;
import android.os.Message;
import com.konka.ios7launcher.R;

public class PreLauncher extends Activity {
    public final int FLAG_CLOSE = 0;
    public final int FLAG_OPEN = 1;
    private Handler handler = new Handler() {
        public void handleMessage(Message mes) {
            switch (mes.what) {
                case 0:
                    PreLauncher.this.close();
                    return;
                case 1:
                    PreLauncher.this.open();
                    return;
                default:
                    return;
            }
        }
    };

    public void onResume() {
        super.onResume();
        this.handler.sendEmptyMessage(1);
    }

    public void onPause() {
        super.onPause();
        this.handler.sendEmptyMessage(0);
    }

    public Dialog onCreateDialog(int id) {
        switch (id) {
            case 1:
                ProgressDialog pbarDialog = new ProgressDialog(this);
                pbarDialog.setTitle(R.string.str_dialog_title);
                pbarDialog.setProgressStyle(0);
                pbarDialog.setMessage(getResources().getString(R.string.str_dialog_mes));
                pbarDialog.show();
                return pbarDialog;
            default:
                return null;
        }
    }

    /* access modifiers changed from: private */
    public void open() {
        showDialog(1);
    }

    public void close() {
        removeDialog(1);
    }
}
