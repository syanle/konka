package com.umeng.common.net;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Environment;
import android.view.animation.Animation;
import android.widget.ImageView;
import com.tencent.stat.common.StatConstants;
import com.umeng.common.Log;
import com.umeng.common.util.h;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.Stack;
import java.util.WeakHashMap;

/* compiled from: ResUtil */
public class q {
    public static boolean a = false;
    /* access modifiers changed from: private */
    public static final String b = q.class.getName();
    private static final long c = 52428800;
    private static final long d = 10485760;
    private static final long e = 1800000;
    private static final Map<ImageView, String> f = Collections.synchronizedMap(new WeakHashMap());
    /* access modifiers changed from: private */
    public static Thread g;

    /* compiled from: ResUtil */
    public interface a {
        void a(com.umeng.common.net.p.a aVar);

        void a(b bVar);
    }

    /* compiled from: ResUtil */
    public enum b {
        BIND_FORM_CACHE,
        BIND_FROM_NET
    }

    /* compiled from: ResUtil */
    static class c extends AsyncTask<Object, Integer, Drawable> {
        private Context a;
        private String b;
        private ImageView c;
        private b d;
        private boolean e;
        private a f;
        private Animation g;
        private boolean h;
        private File i;

        public c(Context context, ImageView imageView, String str, b bVar, File file, boolean z, a aVar, Animation animation, boolean z2) {
            this.i = file;
            this.a = context;
            this.b = str;
            this.f = aVar;
            this.d = bVar;
            this.e = z;
            this.g = animation;
            this.c = imageView;
            this.h = z2;
        }

        /* access modifiers changed from: protected */
        public void onPreExecute() {
            super.onPreExecute();
            if (this.f != null) {
                this.f.a(this.d);
            }
        }

        /* access modifiers changed from: protected */
        /* renamed from: a */
        public void onPostExecute(Drawable drawable) {
            q.b(this.a, this.c, drawable, this.e, this.f, this.g, this.h, this.b);
        }

        /* access modifiers changed from: protected */
        /* renamed from: a */
        public Drawable doInBackground(Object... objArr) {
            Drawable drawable;
            if (q.a) {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e2) {
                    e2.printStackTrace();
                }
            }
            if (this.i == null || !this.i.exists()) {
                try {
                    q.a(this.a, this.b);
                    File b2 = q.b(this.a, this.b);
                    if (b2 == null || !b2.exists()) {
                        drawable = null;
                    } else {
                        drawable = q.c(b2.getAbsolutePath());
                    }
                    Log.c(q.b, "get drawable from net else file.");
                    return drawable;
                } catch (Exception e3) {
                    Log.e(q.b, e3.toString(), e3);
                    return null;
                }
            } else {
                Drawable a2 = q.c(this.i.getAbsolutePath());
                if (a2 == null) {
                    this.i.delete();
                }
                Log.c(q.b, "get drawable from cacheFile.");
                return a2;
            }
        }
    }

    private static String b(String str) {
        int lastIndexOf = str.lastIndexOf(".");
        String str2 = StatConstants.MTA_COOPERATION_TAG;
        if (lastIndexOf >= 0) {
            str2 = str.substring(lastIndexOf);
        }
        return h.a(str) + str2;
    }

    public static String a(Context context, String str) {
        File file;
        String canonicalPath;
        long j;
        if (h.d(str)) {
            return null;
        }
        try {
            String str2 = b(str) + ".tmp";
            if (com.umeng.common.b.b()) {
                canonicalPath = Environment.getExternalStorageDirectory().getCanonicalPath();
                j = c;
            } else {
                canonicalPath = context.getCacheDir().getCanonicalPath();
                j = d;
            }
            File file2 = new File(new StringBuilder(String.valueOf(canonicalPath)).append(com.umeng.common.a.a).toString());
            a(file2, j, (long) e);
            file = new File(file2, str2);
            try {
                file.createNewFile();
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                InputStream inputStream = (InputStream) new URL(str).openConnection().getContent();
                byte[] bArr = new byte[4096];
                while (true) {
                    int read = inputStream.read(bArr);
                    if (read == -1) {
                        fileOutputStream.flush();
                        inputStream.close();
                        fileOutputStream.close();
                        File file3 = new File(file.getParent(), file.getName().replace(".tmp", StatConstants.MTA_COOPERATION_TAG));
                        file.renameTo(file3);
                        Log.a(b, "download img[" + str + "]  to " + file3.getCanonicalPath());
                        return file3.getCanonicalPath();
                    }
                    fileOutputStream.write(bArr, 0, read);
                }
            } catch (Exception e2) {
                e = e2;
            }
        } catch (Exception e3) {
            e = e3;
            file = null;
            Log.a(b, new StringBuilder(String.valueOf(e.getStackTrace().toString())).append("\t url:\t").append(h.a).append(str).toString());
            if (file != null && file.exists()) {
                file.deleteOnExit();
            }
            return null;
        }
    }

    public static File a(String str, Context context, boolean[] zArr) throws IOException {
        if (com.umeng.common.b.b()) {
            File file = new File(new StringBuilder(String.valueOf(Environment.getExternalStorageDirectory().getCanonicalPath())).append(com.umeng.common.a.a).append(str).toString());
            file.mkdirs();
            if (file.exists()) {
                zArr[0] = true;
                return file;
            }
        }
        String absolutePath = context.getCacheDir().getAbsolutePath();
        new File(absolutePath).mkdir();
        a(absolutePath, 505, -1, -1);
        String sb = new StringBuilder(String.valueOf(absolutePath)).append(com.umeng.common.a.b).toString();
        new File(sb).mkdir();
        a(sb, 505, -1, -1);
        File file2 = new File(sb);
        zArr[0] = false;
        return file2;
    }

    public static boolean a(String str, int i, int i2, int i3) {
        try {
            Class.forName("android.os.FileUtils").getMethod("setPermissions", new Class[]{String.class, Integer.TYPE, Integer.TYPE, Integer.TYPE}).invoke(null, new Object[]{str, Integer.valueOf(i), Integer.valueOf(-1), Integer.valueOf(-1)});
            return true;
        } catch (ClassNotFoundException e2) {
            Log.b(b, "error when set permissions:", e2);
        } catch (NoSuchMethodException e3) {
            Log.b(b, "error when set permissions:", e3);
        } catch (IllegalArgumentException e4) {
            Log.b(b, "error when set permissions:", e4);
        } catch (IllegalAccessException e5) {
            Log.b(b, "error when set permissions:", e5);
        } catch (InvocationTargetException e6) {
            Log.b(b, "error when set permissions:", e6);
        }
        return false;
    }

    public static boolean a(String str, int i) {
        int i2 = 432;
        if ((i & 1) != 0) {
            i2 = 436;
        }
        if ((i & 2) != 0) {
            i2 |= 2;
        }
        return a(str, i2, -1, -1);
    }

    public static void a(File file, long j, long j2) throws IOException {
        if (file.exists()) {
            if (a(file.getCanonicalFile()) > j) {
                if (g == null) {
                    g = new Thread(new r(file, j2));
                }
                synchronized (g) {
                    g.start();
                }
            }
        } else if (!file.mkdirs()) {
            Log.b(b, "Failed to create directory" + file.getAbsolutePath() + ". Check permission. Make sure WRITE_EXTERNAL_STORAGE is added in your Manifest.xml");
        }
    }

    private static long a(File file) {
        File[] listFiles;
        long j = 0;
        if (file == null || !file.exists() || !file.isDirectory()) {
            return 0;
        }
        Stack stack = new Stack();
        stack.clear();
        stack.push(file);
        while (true) {
            long j2 = j;
            if (stack.isEmpty()) {
                return j2;
            }
            j = j2;
            for (File file2 : ((File) stack.pop()).listFiles()) {
                if (!file2.isDirectory()) {
                    j += file2.length();
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public static void b(File file, long j) {
        File[] listFiles;
        if (file != null && file.exists() && file.canWrite() && file.isDirectory()) {
            for (File file2 : file.listFiles()) {
                if (!file2.isDirectory() && new Date().getTime() - file2.lastModified() > j) {
                    file2.delete();
                }
            }
        }
    }

    protected static File b(Context context, String str) throws IOException {
        String canonicalPath;
        String b2 = b(str);
        if (com.umeng.common.b.b()) {
            canonicalPath = Environment.getExternalStorageDirectory().getCanonicalPath();
        } else {
            canonicalPath = context.getCacheDir().getCanonicalPath();
        }
        File file = new File(new File(new StringBuilder(String.valueOf(canonicalPath)).append(com.umeng.common.a.a).toString()), b2);
        if (file.exists()) {
            return file;
        }
        return null;
    }

    public static void a(Context context, ImageView imageView, String str, boolean z) {
        a(context, imageView, str, z, null, null, false);
    }

    public static void a(Context context, ImageView imageView, String str, boolean z, a aVar) {
        a(context, imageView, str, z, aVar, null, false);
    }

    public static void a(Context context, ImageView imageView, String str, boolean z, a aVar, Animation animation) {
        a(context, imageView, str, z, aVar, null, false);
    }

    public static void a(Context context, ImageView imageView, String str, boolean z, a aVar, Animation animation, boolean z2) {
        if (imageView != null) {
            f.put(imageView, str);
            try {
                File b2 = b(context, str);
                if (b2 == null || !b2.exists() || a) {
                    new c(context, imageView, str, b.BIND_FROM_NET, null, z, aVar, animation, z2).execute(new Object[0]);
                    return;
                }
                if (aVar != null) {
                    aVar.a(b.BIND_FORM_CACHE);
                }
                Drawable c2 = c(b2.getAbsolutePath());
                if (c2 == null) {
                    b2.delete();
                }
                b(context, imageView, c2, z, aVar, animation, z2, str);
            } catch (Exception e2) {
                Log.b(b, StatConstants.MTA_COOPERATION_TAG, e2);
                if (aVar != null) {
                    aVar.a(com.umeng.common.net.p.a.FAIL);
                }
            }
        }
    }

    private static boolean a(ImageView imageView, String str) {
        String str2 = (String) f.get(imageView);
        if (str2 == null || str2.equals(str)) {
            return false;
        }
        return true;
    }

    /* access modifiers changed from: private */
    public static synchronized void b(Context context, ImageView imageView, Drawable drawable, boolean z, a aVar, Animation animation, boolean z2, String str) {
        synchronized (q.class) {
            if (z2 && drawable != null) {
                try {
                    drawable = new BitmapDrawable(a(((BitmapDrawable) drawable).getBitmap()));
                } catch (Exception e2) {
                    Log.b(b, "bind failed", e2);
                    if (aVar != null) {
                        aVar.a(com.umeng.common.net.p.a.FAIL);
                    }
                }
            }
            if (drawable == null || imageView == null) {
                if (aVar != null) {
                    aVar.a(com.umeng.common.net.p.a.FAIL);
                }
                Log.e(b, "bind drawable failed. drawable [" + drawable + "]  imageView[+" + imageView + "+]");
            } else if (!a(imageView, str)) {
                if (z) {
                    imageView.setBackgroundDrawable(drawable);
                } else {
                    imageView.setImageDrawable(drawable);
                }
                if (animation != null) {
                    imageView.startAnimation(animation);
                }
                if (aVar != null) {
                    aVar.a(com.umeng.common.net.p.a.SUCCESS);
                }
            } else if (aVar != null) {
                aVar.a(com.umeng.common.net.p.a.FAIL);
            }
        }
    }

    /* access modifiers changed from: private */
    public static Drawable c(String str) {
        boolean z = false;
        try {
            return Drawable.createFromPath(str);
        } catch (OutOfMemoryError e2) {
            Log.e(b, "Resutil fetchImage OutOfMemoryError:" + e2.toString());
            return z;
        }
    }

    private static Bitmap a(Bitmap bitmap) {
        try {
            Bitmap createBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
            Canvas canvas = new Canvas(createBitmap);
            Paint paint = new Paint();
            Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
            RectF rectF = new RectF(rect);
            paint.setAntiAlias(true);
            canvas.drawARGB(0, 0, 0, 0);
            paint.setColor(-12434878);
            canvas.drawRoundRect(rectF, (float) (bitmap.getWidth() / 6), (float) (bitmap.getHeight() / 6), paint);
            paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
            canvas.drawBitmap(bitmap, rect, rect, paint);
            bitmap.recycle();
            return createBitmap;
        } catch (OutOfMemoryError e2) {
            Log.e(b, "Cant`t create round corner bitmap. [OutOfMemoryError] ");
            return null;
        }
    }
}
