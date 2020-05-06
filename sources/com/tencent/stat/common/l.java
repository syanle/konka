package com.tencent.stat.common;

import com.konka.kkinterface.tv.ChannelDesk;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;

class l {
    static int a() {
        try {
            return new File("/sys/devices/system/cpu/").listFiles(new m()).length;
        } catch (Exception e) {
            e.printStackTrace();
            return 1;
        }
    }

    static int b() {
        int i = 0;
        String str = StatConstants.MTA_COOPERATION_TAG;
        try {
            InputStream inputStream = new ProcessBuilder(new String[]{"/system/bin/cat", "/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq"}).start().getInputStream();
            byte[] bArr = new byte[24];
            while (inputStream.read(bArr) != -1) {
                str = str + new String(bArr);
            }
            inputStream.close();
            String trim = str.trim();
            if (trim.length() > 0) {
                i = Integer.valueOf(trim).intValue();
            }
        } catch (Exception e) {
            k.k.e((Throwable) e);
        }
        return i * ChannelDesk.max_dtv_count;
    }

    static int c() {
        int i = 0;
        String str = StatConstants.MTA_COOPERATION_TAG;
        try {
            InputStream inputStream = new ProcessBuilder(new String[]{"/system/bin/cat", "/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_min_freq"}).start().getInputStream();
            byte[] bArr = new byte[24];
            while (inputStream.read(bArr) != -1) {
                str = str + new String(bArr);
            }
            inputStream.close();
            String trim = str.trim();
            if (trim.length() > 0) {
                i = Integer.valueOf(trim).intValue();
            }
        } catch (Throwable th) {
            k.k.e(th);
        }
        return i * ChannelDesk.max_dtv_count;
    }

    static String d() {
        String str = "/proc/cpuinfo";
        String str2 = StatConstants.MTA_COOPERATION_TAG;
        String[] strArr = {StatConstants.MTA_COOPERATION_TAG, StatConstants.MTA_COOPERATION_TAG};
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(str), 8192);
            String[] split = bufferedReader.readLine().split("\\s+");
            for (int i = 2; i < split.length; i++) {
                strArr[0] = strArr[0] + split[i] + " ";
            }
            bufferedReader.close();
        } catch (IOException e) {
        }
        return strArr[0];
    }
}
