package com.tencent.tvMTA.report;

import android.text.TextUtils;
import com.tencent.stat.common.StatConstants;
import com.tencent.tvMTA.core.GlobalInfo;
import com.tencent.tvMTA.report.ExParamKeys.common;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

public class CommonParams {
    public static Properties getCommonProps(long time) {
        Properties commonPros = new Properties();
        commonPros.put("guid", getNotNullString(GlobalInfo.getGUID()));
        commonPros.put(common.COMMON_OPENID_TYPE, getNotNullString(GlobalInfo.getOpenIDType()));
        commonPros.put(common.COMMON_OPENID, getNotNullString(GlobalInfo.getOpenID()));
        commonPros.put(common.COMMON_PACKAGE, getNotNullString(GlobalInfo.getPackageName()));
        commonPros.put(common.COMMON_QUA, getQUA(GlobalInfo.getQua()));
        return commonPros;
    }

    public static Properties getCustomProps(long time, String open_apk_name, KV... propKVs) {
        Properties customPros = new Properties();
        customPros.put("guid", getNotNullString(GlobalInfo.getGUID()));
        customPros.put(common.COMMON_OPENID_TYPE, getNotNullString(GlobalInfo.getOpenIDType()));
        customPros.put(common.COMMON_OPENID, getNotNullString(GlobalInfo.getOpenID()));
        customPros.put(common.COMMON_PACKAGE, getNotNullString(GlobalInfo.getPackageName()));
        customPros.put(common.COMMON_QUA, getQUA(GlobalInfo.getQua()));
        customPros.put(common.COMMON_OPEN_APK_NAME, open_apk_name);
        int length = propKVs.length;
        for (int i = 0; i < length; i++) {
            KV kv = propKVs[i];
            customPros.put(kv.getKey(), kv.getValue() != null ? kv.getValue() : StatConstants.MTA_COOPERATION_TAG);
        }
        return customPros;
    }

    private static String gatDate(long time) {
        return new SimpleDateFormat("yyyy年MM月dd日HH时mm分ss秒").format(new Date(time));
    }

    public static Properties getCGIConnectProps() {
        return new Properties();
    }

    private static String getNotNullString(String originalString) {
        if (TextUtils.isEmpty(originalString)) {
            return StatConstants.MTA_COOPERATION_TAG;
        }
        return originalString;
    }

    private static String getQUA(String originalString) {
        if (TextUtils.isEmpty(originalString)) {
            return StatConstants.MTA_COOPERATION_TAG;
        }
        return originalString;
    }
}
