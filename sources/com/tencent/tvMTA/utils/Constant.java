package com.tencent.tvMTA.utils;

public class Constant {
    public static final String FILE_NAME = "boot_settings";
    public static final String SET_KEY = "set_key";
    public static final int VERSION_CODE = 10;
    public static final String VERSION_NAME = "1.1.0";

    public interface Access {
        public static final int DISACCESS = 0;
        public static final int INTERNET = 2;
        public static final int WIFI = 1;
    }

    public interface CGIPrefix {
        public static final String UPGRADE = "https://tv.ptyg.gitv.tv/i-tvbin/upgrade_rom/get_upgrade_info?";
    }

    public interface CGI_VERSION {
        public static final int CGI_VERSION_10004 = 10004;
    }

    public interface License_plate {
        public static final String KONKA = "A3UV54VWX6GY";
        public static final String TENCENT = "A3V1CMVI43UD";
    }

    public interface PLATFORM {
        public static final int CHID = 10009;
        public static final int TV = 42;
    }
}
