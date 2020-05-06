package com.konka.launcherblacklist;

import android.content.ComponentName;
import android.util.JsonReader;
import android.util.Log;
import com.umeng.common.util.e;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class BlackListFormat {
    private static final String COMPONENT_CLS = "className";
    private static final String COMPONENT_PKG = "pkgName";
    private static final String KEY_APPS = "apps";
    private static final String KEY_SHORTCUTS = "shortcuts";
    private static final String KEY_VERSION = "version";
    private static final String KEY_WIDGETS = "widgets";
    private static final String TAG = "BlackListFormat";
    private String mVersion;

    /* JADX WARNING: Removed duplicated region for block: B:27:0x0048 A[SYNTHETIC, Splitter:B:27:0x0048] */
    /* JADX WARNING: Removed duplicated region for block: B:39:0x0065 A[SYNTHETIC, Splitter:B:39:0x0065] */
    /* JADX WARNING: Removed duplicated region for block: B:69:? A[RETURN, SYNTHETIC] */
    public boolean read(InputStream is, ArrayList<ComponentName> appsList, ArrayList<ComponentName> widgetsList, ArrayList<ComponentName> shortcutsList) {
        if (is == null || appsList == null || widgetsList == null || shortcutsList == null) {
            return false;
        }
        JsonReader reader = null;
        try {
            JsonReader reader2 = new JsonReader(new InputStreamReader(is, e.f));
            try {
                reader2.beginObject();
                while (reader2.hasNext()) {
                    String key = reader2.nextName();
                    if ("version".equals(key)) {
                        this.mVersion = reader2.nextString();
                    } else if (KEY_APPS.equals(key)) {
                        readComponentArray(reader2, appsList);
                    } else if (KEY_WIDGETS.equals(key)) {
                        readComponentArray(reader2, widgetsList);
                    } else if (KEY_SHORTCUTS.equals(key)) {
                        readComponentArray(reader2, shortcutsList);
                    } else {
                        reader2.skipValue();
                    }
                }
                reader2.endObject();
                if (reader2 != null) {
                    try {
                        reader2.close();
                    } catch (IOException e) {
                        Log.e(TAG, "close reader error:", e);
                    }
                }
                return true;
            } catch (IOException e2) {
                e = e2;
                reader = reader2;
                try {
                    Log.e(TAG, "inputStream read ioe, maybe wrong fromat?", e);
                    if (reader != null) {
                    }
                } catch (Throwable th) {
                    th = th;
                    if (reader != null) {
                    }
                    throw th;
                }
            } catch (Throwable th2) {
                th = th2;
                reader = reader2;
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e3) {
                        Log.e(TAG, "close reader error:", e3);
                    }
                }
                throw th;
            }
        } catch (IOException e4) {
            e = e4;
            Log.e(TAG, "inputStream read ioe, maybe wrong fromat?", e);
            if (reader != null) {
                return false;
            }
            try {
                reader.close();
                return false;
            } catch (IOException e5) {
                Log.e(TAG, "close reader error:", e5);
                return false;
            }
        }
    }

    public String getVersion() {
        return this.mVersion;
    }

    private static void readComponentArray(JsonReader reader, ArrayList<ComponentName> list) throws IOException {
        reader.beginArray();
        while (reader.hasNext()) {
            ComponentName cn = readComponent(reader);
            if (cn != null) {
                list.add(cn);
            }
        }
        reader.endArray();
    }

    private static ComponentName readComponent(JsonReader reader) throws IOException {
        String pkgName = null;
        String className = null;
        reader.beginObject();
        while (reader.hasNext()) {
            String key = reader.nextName();
            if ("pkgName".equals(key)) {
                pkgName = reader.nextString();
            } else if (COMPONENT_CLS.equals(key)) {
                className = reader.nextString();
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        if (pkgName == null || className == null) {
            return null;
        }
        return new ComponentName(pkgName, className);
    }
}
