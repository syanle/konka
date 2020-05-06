package com.tencent.mid.a;

import com.tencent.mid.api.MidCallback;
import com.tencent.mid.util.Util;

class i implements MidCallback {
    final /* synthetic */ h a;

    i(h hVar) {
        this.a = hVar;
    }

    public void onFail(int i, String str) {
        Util.logInfo("checkServer failed, errCode:" + i + ",msg:" + str);
    }

    public void onSuccess(Object obj) {
        Util.logInfo("checkServer success:" + obj);
    }
}
