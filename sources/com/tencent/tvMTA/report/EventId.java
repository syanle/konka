package com.tencent.tvMTA.report;

public class EventId {

    public static final class apk {
        public static final String KTOPIC_OPENAPP_TIMES = "ktopic_openapp_times";
    }

    public static final class app {
        public static final String APP_ACTION_COOL_START = "app_action_cool_start";
        public static final String APP_ACTION_START = "app_action_start";
    }

    public static final class cgi {
        public static final String ITIL_CGI_ACTION_REQUEST = "itil_cgi_action_request";
    }

    public static final class launch {
        public static final String APP_ACTION_LAUNCH = "app_action_launch";
    }

    public static final class layer {
        public static final String APP_ACTION_POWEROFF_BTN = "app_action_poweroff_btn";
        public static final String APP_ACTION_POWEROFF_LAYER = "app_action_poweroff_layer";
        public static final String APP_ACTION_REBOOT_BTN = "app_action_reboot_btn";
    }
}
