package com.konka.appupgrade.constant;

import android.net.Uri;
import java.text.SimpleDateFormat;

public final class Provider {
    public static final String AUTHORITY = "com.konka.appupgrade.provider";
    private static final String PK_PKG_NAME = "pkgName";
    private static final String PK_VERSION_CODE = "versionCode";
    public static final String TBL_LastUpgraded = "lastupgraded";
    public static final String TBL_ToBeUpgraded = "tobeupgraded";

    public static final class LastUpgraded {
        public static final String CONTENT_TYPE = "vnd.com.konka.dir/appupgrade.provider.lastupgraded";
        public static final Uri CONTENT_URI = Uri.parse("content://com.konka.appupgrade.provider/lastupgraded");
        public static final String PKG_NAME = "pkgName";
        public static final String UPGRADING_STATUS = "upgradingStatus";
        public static final String VERSION_CODE = "versionCode";

        public static final class UpgradingStatus {
            public static final int COMPLETED = 3;
            public static final int DOWNLOAD_FAILED = 1;
            public static final int SCHEDULING = 0;
            public static final int UPGRADING = 2;
            public static final int _COUNT = 4;
        }
    }

    public static final class ToBeUpgraded {
        public static final String CONTENT_TYPE = "vnd.com.konka.dir/appupgrade.provider.tobeupgraded";
        public static final Uri CONTENT_URI = Uri.parse("content://com.konka.appupgrade.provider/tobeupgraded");
        public static final String DATE = "date";
        public static final String ICON_LINK = "iconLink";
        public static final String LINK = "link";
        public static final String PKG_NAME = "pkgName";
        public static final String UPGRADE_TYPE = "upgradeType";
        public static final String VERSION_CODE = "versionCode";
        public static final String VERSION_NAME = "versionName";
        public static final String _ID = "_ID";
        public static SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        public static final class UpgradeType {
            public static final int FORCE_INSTALL = 2;
            public static final int FORCE_REPLACE = 1;
            public static final int NORMAL = 0;
            public static final int _COUNT = 3;
        }
    }
}
