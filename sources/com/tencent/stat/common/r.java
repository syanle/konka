package com.tencent.stat.common;

import android.net.wifi.ScanResult;
import java.util.Comparator;

final class r implements Comparator<ScanResult> {
    r() {
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
