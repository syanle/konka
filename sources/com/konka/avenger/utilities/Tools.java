package com.konka.avenger.utilities;

import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Build.VERSION;
import android.util.Log;
import com.tencent.stat.common.StatConstants;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;

public class Tools {
    private static final int DEFAULT_BUFFER_SIZE = 1024;
    private static final String TAG = Tools.class.getSimpleName();

    public static String sizeToAutoUnit(long size) {
        if (((size / 1024) / 1024) / 1024 >= 1) {
            return new StringBuilder(String.valueOf(((size / 1024) / 1024) / 1024)).append(new StringBuilder(String.valueOf(((double) (((size / 1024) / 1024) % 1024)) / 1024.0d)).toString().substring(1, 3)).append("G").toString();
        }
        if ((size / 1024) / 1024 >= 1) {
            return new StringBuilder(String.valueOf((size / 1024) / 1024)).append(new StringBuilder(String.valueOf(((double) ((size / 1024) % 1024)) / 1024.0d)).toString().substring(1, 3)).append("M").toString();
        }
        return (size / 1024) + new StringBuilder(String.valueOf(((double) (size % 1024)) / 1024.0d)).toString().substring(1, 3) + "K";
    }

    public static void intentForward(Context context, Class<?> forwardClass) {
        Intent intent = new Intent();
        intent.setClass(context, forwardClass);
        intent.addFlags(268435456);
        try {
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Log.e(TAG, "Unable to launch. intent=" + intent, e);
        } catch (SecurityException e2) {
            Log.e(TAG, "Launcher does not have the permission to launch " + intent + ". Make sure to create a MAIN intent-filter for the corresponding activity " + "or use the exported attribute for this activity. " + " intent=" + intent, e2);
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:27:0x0058 A[SYNTHETIC, Splitter:B:27:0x0058] */
    /* JADX WARNING: Removed duplicated region for block: B:34:0x0065 A[SYNTHETIC, Splitter:B:34:0x0065] */
    public static boolean string2File(String res, String filePath) {
        BufferedReader bufferedReader = null;
        try {
            File distFile = new File(filePath);
            if (!distFile.getParentFile().exists()) {
                distFile.getParentFile().mkdirs();
            }
            BufferedReader bufferedReader2 = new BufferedReader(new StringReader(res));
            try {
                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(distFile));
                try {
                    char[] buf = new char[DEFAULT_BUFFER_SIZE];
                    while (true) {
                        int len = bufferedReader2.read(buf);
                        if (len == -1) {
                            break;
                        }
                        bufferedWriter.write(buf, 0, len);
                    }
                    bufferedWriter.flush();
                    bufferedReader2.close();
                    bufferedWriter.close();
                    if (bufferedReader2 != null) {
                        try {
                            bufferedReader2.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    BufferedWriter bufferedWriter2 = bufferedWriter;
                    BufferedReader bufferedReader3 = bufferedReader2;
                    return true;
                } catch (IOException e2) {
                    e = e2;
                    BufferedWriter bufferedWriter3 = bufferedWriter;
                    bufferedReader = bufferedReader2;
                } catch (Throwable th) {
                    th = th;
                    BufferedWriter bufferedWriter4 = bufferedWriter;
                    bufferedReader = bufferedReader2;
                    if (bufferedReader != null) {
                        try {
                            bufferedReader.close();
                        } catch (IOException e3) {
                            e3.printStackTrace();
                        }
                    }
                    throw th;
                }
            } catch (IOException e4) {
                e = e4;
                bufferedReader = bufferedReader2;
                try {
                    e.printStackTrace();
                    if (bufferedReader != null) {
                        try {
                            bufferedReader.close();
                        } catch (IOException e5) {
                            e5.printStackTrace();
                        }
                    }
                    return false;
                } catch (Throwable th2) {
                    th = th2;
                    if (bufferedReader != null) {
                    }
                    throw th;
                }
            } catch (Throwable th3) {
                th = th3;
                bufferedReader = bufferedReader2;
                if (bufferedReader != null) {
                }
                throw th;
            }
        } catch (IOException e6) {
            e = e6;
            e.printStackTrace();
            if (bufferedReader != null) {
            }
            return false;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:11:0x002e A[SYNTHETIC, Splitter:B:11:0x002e] */
    /* JADX WARNING: Removed duplicated region for block: B:14:0x0033  */
    /* JADX WARNING: Removed duplicated region for block: B:17:0x0044 A[Catch:{ Exception -> 0x0049, all -> 0x0058 }, LOOP:0: B:8:0x0025->B:17:0x0044, LOOP_END] */
    /* JADX WARNING: Removed duplicated region for block: B:35:0x002c A[EDGE_INSN: B:35:0x002c->B:10:0x002c ?: BREAK  
EDGE_INSN: B:35:0x002c->B:10:0x002c ?: BREAK  , SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:37:? A[RETURN, SYNTHETIC] */
    public static String file2String(File file, String encoding) {
        InputStreamReader reader;
        char[] buffer;
        int n;
        boolean reader2 = null;
        StringWriter writer = new StringWriter();
        if (encoding != null) {
            try {
                reader2 = StatConstants.MTA_COOPERATION_TAG.equals(encoding.trim());
                if (!reader2) {
                    reader = new InputStreamReader(new FileInputStream(file));
                    buffer = new char[DEFAULT_BUFFER_SIZE];
                    while (true) {
                        n = reader.read(buffer);
                        if (-1 != n) {
                            break;
                        }
                        writer.write(buffer, 0, n);
                    }
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (writer == null) {
                        return writer.toString();
                    }
                    return null;
                }
            } catch (Exception e2) {
                e2.printStackTrace();
                if (reader2 == null) {
                    return null;
                }
                try {
                    reader2.close();
                    return null;
                } catch (IOException e3) {
                    e3.printStackTrace();
                    return null;
                }
            } finally {
                if (reader2 != null) {
                    try {
                        reader2.close();
                    } catch (IOException e4) {
                        e4.printStackTrace();
                    }
                }
            }
        }
        reader = new InputStreamReader(new FileInputStream(file), encoding);
        buffer = new char[DEFAULT_BUFFER_SIZE];
        while (true) {
            n = reader.read(buffer);
            if (-1 != n) {
            }
            writer.write(buffer, 0, n);
        }
        if (reader != null) {
        }
        if (writer == null) {
        }
    }

    public static String currentSystemVersion() {
        return VERSION.RELEASE;
    }

    public static Bitmap byte2Bitmap(byte[] b) {
        if (b.length != 0) {
            return BitmapFactory.decodeByteArray(b, 0, b.length);
        }
        return null;
    }

    public static byte[] bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    public static String getAvailMemory(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService("activity");
        MemoryInfo mi = new MemoryInfo();
        am.getMemoryInfo(mi);
        System.out.println("mi.availMem;" + mi.availMem);
        return sizeToAutoUnit(mi.availMem * 8);
    }

    public static String getTotalMemory(Context context) {
        long initial_memory = 0;
        try {
            BufferedReader localBufferedReader = new BufferedReader(new FileReader("/proc/meminfo"), 8192);
            String str2 = localBufferedReader.readLine();
            String[] arrayOfString = str2.split("\\s+");
            int length = arrayOfString.length;
            for (int i = 0; i < length; i++) {
                Log.i(str2, arrayOfString[i] + "\t");
            }
            initial_memory = (long) (Integer.valueOf(arrayOfString[1]).intValue() * DEFAULT_BUFFER_SIZE);
            localBufferedReader.close();
        } catch (IOException e) {
        }
        return sizeToAutoUnit(8 * initial_memory);
    }
}
