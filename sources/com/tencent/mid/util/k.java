package com.tencent.mid.util;

import android.net.wifi.ScanResult;
import java.util.Comparator;

final class k implements Comparator<ScanResult> {
    k() {
    }

    /* renamed from: a */
    public int compare(ScanResult scanResult, ScanResult scanResult2) {
        int abs = Math.abs(scanResult.level);
        int abs2 = Math.abs(scanResult2.level);
        if (abs > abs2) {
            return 1;
        }
        return abs == abs2 ? 0 : -1;
    }
}
