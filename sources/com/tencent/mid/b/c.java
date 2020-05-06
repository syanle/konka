package com.tencent.mid.b;

import android.content.Context;
import android.os.Environment;
import com.tencent.mid.util.Util;
import com.tencent.stat.common.StatConstants;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;

public class c extends f {
    public c(Context context) {
        super(context);
    }

    /* access modifiers changed from: protected */
    public void a(a aVar) {
    }

    /* access modifiers changed from: protected */
    public void a(String str) {
        synchronized (this) {
            Util.logInfo("write mid to InternalStorage");
            b.a(Environment.getExternalStorageDirectory() + "/" + e());
            File file = new File(Environment.getExternalStorageDirectory(), f());
            if (file != null) {
                try {
                    BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
                    bufferedWriter.write(k() + "," + str);
                    bufferedWriter.write("\n");
                    bufferedWriter.close();
                } catch (IOException e) {
                    Util.logWarn(e);
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public boolean a() {
        return Util.checkPermission(this.a, "android.permission.WRITE_EXTERNAL_STORAGE") && Environment.getExternalStorageState().equals("mounted");
    }

    /* access modifiers changed from: protected */
    public String b() {
        String str;
        String str2 = null;
        synchronized (this) {
            Util.logInfo("read mid from InternalStorage");
            File file = new File(Environment.getExternalStorageDirectory(), f());
            if (file != null) {
                try {
                    Iterator it = b.a(file).iterator();
                    while (true) {
                        if (!it.hasNext()) {
                            str = null;
                            break;
                        }
                        String[] split = ((String) it.next()).split(",");
                        if (split.length == 2 && split[0].equals(k())) {
                            Util.logInfo("read mid from InternalStorage:" + split[1]);
                            str = split[1];
                            break;
                        }
                    }
                    str2 = str;
                } catch (IOException e) {
                    Util.logWarn(e);
                }
            }
        }
        return str2;
    }

    /* access modifiers changed from: protected */
    public void c() {
        synchronized (this) {
            b.a(Environment.getExternalStorageDirectory() + "/" + e());
            File file = new File(Environment.getExternalStorageDirectory(), f());
            if (file != null) {
                try {
                    BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
                    bufferedWriter.write(StatConstants.MTA_COOPERATION_TAG);
                    bufferedWriter.close();
                } catch (IOException e) {
                    Util.logWarn(e);
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public a d() {
        return null;
    }
}
