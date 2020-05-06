package com.tencent.tvMTA.report;

import android.content.Context;
import com.tencent.stat.StatService;
import com.tencent.tvMTA.report.EventId.apk;
import com.tencent.tvMTA.report.EventId.app;
import com.tencent.tvMTA.report.EventId.layer;

public class ReportHelper {
    public static void reportAppCoolStartAction(Context ctx, long time) {
        StatService.trackCustomKVEvent(ctx, app.APP_ACTION_COOL_START, CommonParams.getCommonProps(time));
    }

    public static void reportAppStartAction(Context ctx, long time) {
        StatService.trackCustomKVEvent(ctx, app.APP_ACTION_START, CommonParams.getCommonProps(time));
    }

    public static void reportApkClick(Context ctx, String apkPackage, String open_apk_name, long time) {
        StatService.trackCustomKVEvent(ctx, apk.KTOPIC_OPENAPP_TIMES, CommonParams.getCustomProps(time, open_apk_name, new KV(ExParamKeys.apk.TENCENT_APK, apkPackage)));
    }

    public static void reportPowerOffLayyer(Context ctx, long time) {
        StatService.trackCustomKVEvent(ctx, layer.APP_ACTION_POWEROFF_BTN, CommonParams.getCommonProps(time));
    }

    public static void reportRebootBtn(Context ctx, long time) {
        StatService.trackCustomKVEvent(ctx, layer.APP_ACTION_REBOOT_BTN, CommonParams.getCommonProps(time));
    }

    public static void reportPoweroffBtn(Context ctx, long time) {
        StatService.trackCustomKVEvent(ctx, layer.APP_ACTION_POWEROFF_LAYER, CommonParams.getCommonProps(time));
    }
}
