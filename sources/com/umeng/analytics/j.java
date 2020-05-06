package com.umeng.analytics;

import android.content.Context;
import android.content.SharedPreferences;
import com.tencent.stat.common.StatConstants;
import com.umeng.analytics.a.g;
import com.umeng.common.Log;
import com.umeng.common.util.e;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.URLEncoder;
import org.json.JSONException;
import org.json.JSONObject;

/* compiled from: UmengStoreHelper */
public final class j {
    static long a = 1209600000;
    static long b = 2097152;
    private static final String c = "cache_version";
    private static final String d = "error";
    private static final String e = "mobclick_agent_user_";
    private static final String f = "mobclick_agent_online_setting_";
    private static final String g = "mobclick_agent_header_";
    private static final String h = "mobclick_agent_update_";
    private static final String i = "mobclick_agent_state_";
    private static final String j = "mobclick_agent_cached_";

    static SharedPreferences a(Context context) {
        return context.getSharedPreferences(new StringBuilder(e).append(context.getPackageName()).toString(), 0);
    }

    public static SharedPreferences b(Context context) {
        return context.getSharedPreferences(new StringBuilder(f).append(context.getPackageName()).toString(), 0);
    }

    public static SharedPreferences c(Context context) {
        return context.getSharedPreferences(new StringBuilder(g).append(context.getPackageName()).toString(), 0);
    }

    static SharedPreferences d(Context context) {
        return context.getSharedPreferences(new StringBuilder(h).append(context.getPackageName()).toString(), 0);
    }

    public static SharedPreferences e(Context context) {
        return context.getSharedPreferences(new StringBuilder(i).append(context.getPackageName()).toString(), 0);
    }

    static String f(Context context) {
        return new StringBuilder(g).append(context.getPackageName()).toString();
    }

    static String g(Context context) {
        return new StringBuilder(j).append(context.getPackageName()).toString();
    }

    static JSONObject h(Context context) {
        JSONObject jSONObject = new JSONObject();
        SharedPreferences a2 = a(context);
        try {
            if (a2.getInt("gender", -1) != -1) {
                jSONObject.put("sex", a2.getInt("gender", -1));
            }
            if (a2.getInt("age", -1) != -1) {
                jSONObject.put("age", a2.getInt("age", -1));
            }
            if (!StatConstants.MTA_COOPERATION_TAG.equals(a2.getString("user_id", StatConstants.MTA_COOPERATION_TAG))) {
                jSONObject.put("id", a2.getString("user_id", StatConstants.MTA_COOPERATION_TAG));
            }
            if (!StatConstants.MTA_COOPERATION_TAG.equals(a2.getString("id_source", StatConstants.MTA_COOPERATION_TAG))) {
                jSONObject.put("url", URLEncoder.encode(a2.getString("id_source", StatConstants.MTA_COOPERATION_TAG), e.f));
            }
            if (jSONObject.length() > 0) {
                return jSONObject;
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        return null;
    }

    public static int[] i(Context context) {
        SharedPreferences b2 = b(context);
        int[] iArr = new int[2];
        if (b2.getInt(g.A, -1) != -1) {
            iArr[0] = b2.getInt(g.A, 1);
            iArr[1] = (int) b2.getLong(g.B, (long) g.h);
        } else {
            iArr[0] = b2.getInt(g.D, 1);
            iArr[1] = (int) b2.getLong(g.E, (long) g.h);
        }
        return iArr;
    }

    static boolean a(File file) {
        long length = file.length();
        if (!file.exists() || length <= b) {
            return false;
        }
        return true;
    }

    /* JADX INFO: finally extract failed */
    /* JADX WARNING: No exception handlers in catch block: Catch:{  } */
    static JSONObject a(Context context, String str) {
        String g2 = g(context);
        try {
            File file = new File(context.getFilesDir(), g2);
            if (a(file)) {
                file.delete();
                return null;
            }
            FileInputStream openFileInput = context.openFileInput(g2);
            try {
                String a2 = a((InputStream) openFileInput);
                openFileInput.close();
                try {
                    JSONObject jSONObject = new JSONObject(a2);
                    if (!jSONObject.optString(c).equals(str)) {
                        jSONObject.remove(d);
                    }
                    jSONObject.remove(c);
                    if (jSONObject.length() != 0) {
                        return jSONObject;
                    }
                    return null;
                } catch (JSONException e2) {
                    j(context);
                    e2.printStackTrace();
                    return null;
                }
            } catch (Exception e3) {
                e3.printStackTrace();
                openFileInput.close();
                return null;
            } catch (Throwable th) {
                openFileInput.close();
                throw th;
            }
        } catch (FileNotFoundException e4) {
            return null;
        } catch (IOException e5) {
            return null;
        } catch (Throwable th2) {
            return null;
        }
    }

    static String a(InputStream inputStream) throws IOException {
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        char[] cArr = new char[1024];
        StringWriter stringWriter = new StringWriter();
        while (true) {
            int read = inputStreamReader.read(cArr);
            if (-1 == read) {
                return stringWriter.toString();
            }
            stringWriter.write(cArr, 0, read);
        }
    }

    static void a(Context context, JSONObject jSONObject, String str) throws Exception {
        if (jSONObject != null) {
            String g2 = g(context);
            jSONObject.put(c, str);
            FileOutputStream openFileOutput = context.openFileOutput(g2, 0);
            openFileOutput.write(jSONObject.toString().getBytes());
            openFileOutput.flush();
            openFileOutput.close();
            Log.c(g.q, "cache buffer success");
        }
    }

    static void a(Context context, g gVar, String str) {
        if (gVar != null) {
            try {
                JSONObject jSONObject = new JSONObject();
                gVar.b(jSONObject);
                a(context, jSONObject, str);
            } catch (Exception e2) {
                Log.b(g.q, "cache message error", e2);
            }
        }
    }

    static void b(Context context, JSONObject jSONObject, String str) {
        if (jSONObject != null) {
            try {
                a(context, jSONObject.optJSONObject("body"), str);
            } catch (Exception e2) {
                Log.b(g.q, "cache message error", e2);
            }
        }
    }

    static void j(Context context) {
        context.deleteFile(f(context));
        context.deleteFile(g(context));
    }
}
