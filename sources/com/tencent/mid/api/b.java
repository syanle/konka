package com.tencent.mid.api;

import android.util.Log;
import com.tencent.mid.util.Util;

final class b implements MidCallback {
    b() {
    }

    public void onFail(int i, String str) {
        Log.e("MID", "failed to get mid, errorcode:" + i + " ,msg:" + str);
    }

    public void onSuccess(Object obj) {
        if (obj != null) {
            Util.logInfo("success to get mid:" + MidEntity.parse(obj.toString()).getMid());
        }
    }
}
