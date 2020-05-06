package com.tencent.mid.api;

import android.util.Log;
import com.tencent.mid.util.Util;

final class a implements MidCallback {
    final /* synthetic */ MidCallback a;

    a(MidCallback midCallback) {
        this.a = midCallback;
    }

    public void onFail(int i, String str) {
        Log.e("MID", "failed to get mid, errorcode:" + i + " ,msg:" + str);
        this.a.onFail(i, str);
    }

    public void onSuccess(Object obj) {
        if (obj != null) {
            MidEntity parse = MidEntity.parse(obj.toString());
            Util.logInfo("success to get mid:" + parse.getMid());
            this.a.onSuccess(parse.getMid());
        }
    }
}
