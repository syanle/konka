package com.konka.launcherblacklist;

import android.text.TextUtils;
import android.util.Log;
import android.util.Xml;
import java.io.IOException;
import java.io.Reader;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class NetRequestFormat {
    private static final String TAG = "NetRequestFormat";
    private static final String TAG_BLACKLISTFILE = "blacklistfile";
    private static final String TAG_CMD = "cmd";
    private static final String TAG_RESULT = "result";
    private static final String TAG_SERVERADDR = "serveraddr";
    private static final String TAG_SUCCESSFUL = "successful";
    private static final String TAG_VERSION = "version";
    private static final String VALUE_CMD = "getlauncherblacklist";
    private static final String VALUE_SUCCESSFUL = "yes";
    private String mBlackListFile;
    private boolean mIsSuccess = false;
    private String mServerAddr;
    private String mVersion;

    public boolean read(Reader reader) {
        XmlPullParser parser = Xml.newPullParser();
        try {
            parser.setFeature("http://xmlpull.org/v1/doc/features.html#process-namespaces", false);
            parser.setInput(reader);
            parser.nextTag();
            parser.require(2, null, TAG_RESULT);
            while (true) {
                int curTag = parser.next();
                if (curTag == 1) {
                    break;
                } else if (curTag == 2) {
                    String tagName = parser.getName();
                    if (TAG_CMD.equals(tagName)) {
                        if (!VALUE_CMD.equals(parser.nextText())) {
                            return false;
                        }
                    } else if (TAG_SUCCESSFUL.equals(tagName)) {
                        boolean equals = VALUE_SUCCESSFUL.equals(parser.nextText());
                        this.mIsSuccess = equals;
                        if (!equals) {
                            return false;
                        }
                    } else if (TAG_SERVERADDR.equals(tagName)) {
                        this.mServerAddr = parser.nextText();
                    } else if ("version".equals(tagName)) {
                        this.mVersion = parser.nextText();
                    } else if (TAG_BLACKLISTFILE.equals(tagName)) {
                        this.mBlackListFile = parser.nextText();
                    } else {
                        parser.nextText();
                    }
                }
            }
        } catch (XmlPullParserException e) {
            Log.e(TAG, "parse error:", e);
        } catch (IOException e2) {
            Log.e(TAG, "no nextTag:", e2);
        }
        return this.mIsSuccess;
    }

    public boolean hasNewerList(String oldVersion) {
        int oldVal = -1;
        int newVal = -1;
        try {
            oldVal = Integer.parseInt(oldVersion);
        } catch (NumberFormatException e) {
            Log.e(TAG, "parseInt error, maybe no oldVal?", e);
        }
        try {
            newVal = Integer.parseInt(this.mVersion);
        } catch (NumberFormatException e2) {
            Log.e(TAG, "parseInt error, maybe corrupted?", e2);
        }
        if (!this.mIsSuccess || TextUtils.isEmpty(this.mServerAddr) || TextUtils.isEmpty(this.mBlackListFile) || this.mVersion == null || newVal <= oldVal) {
            return false;
        }
        return true;
    }

    public String getListAddress() {
        return this.mServerAddr + "/" + this.mBlackListFile;
    }
}
