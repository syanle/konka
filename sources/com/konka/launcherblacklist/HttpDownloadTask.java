package com.konka.launcherblacklist;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class HttpDownloadTask extends AsyncTask<String, Integer, Integer> {
    private static final int DEFAULT_TIMEOUT = 30000;
    private static final int STATUS_CANCELLED = 3;
    private static final int STATUS_CONTEXT_NOT_EXIST = 2;
    private static final int STATUS_GENERAL_ERROR = 1;
    private static final String TAG = "HttpDownloadTask";
    private final ICallback mCallback;
    private final WeakReference<Context> mCtxRef;
    private boolean mIsIndeterminate = true;
    private OutputStream mOutputStream;
    private int mTimeout = DEFAULT_TIMEOUT;

    public interface ICallback {
        void onPostExecute(Integer num);

        void onPreExecute();

        void onProgressUpdate(Integer... numArr);
    }

    public HttpDownloadTask(Context ctx, ICallback callback) {
        this.mCtxRef = new WeakReference<>(ctx);
        this.mCallback = callback;
    }

    public void setConnectTimeout(int millis) {
        this.mTimeout = millis;
    }

    public void setOutputStream(OutputStream os) {
        this.mOutputStream = os;
    }

    /* access modifiers changed from: protected */
    public void onPreExecute() {
        super.onPreExecute();
        if (((Context) this.mCtxRef.get()) != null && this.mCallback != null) {
            this.mCallback.onPreExecute();
        }
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Removed duplicated region for block: B:137:? A[RETURN, SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:140:? A[RETURN, SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:142:? A[RETURN, SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:43:0x0142 A[SYNTHETIC, Splitter:B:43:0x0142] */
    /* JADX WARNING: Removed duplicated region for block: B:46:0x0147 A[SYNTHETIC, Splitter:B:46:0x0147] */
    /* JADX WARNING: Removed duplicated region for block: B:77:0x01d8 A[SYNTHETIC, Splitter:B:77:0x01d8] */
    /* JADX WARNING: Removed duplicated region for block: B:80:0x01dd A[SYNTHETIC, Splitter:B:80:0x01dd] */
    /* JADX WARNING: Removed duplicated region for block: B:91:0x0214 A[SYNTHETIC, Splitter:B:91:0x0214] */
    /* JADX WARNING: Removed duplicated region for block: B:94:0x0219 A[SYNTHETIC, Splitter:B:94:0x0219] */
    public Integer doInBackground(String... urls) {
        InputStream input = null;
        OutputStream output = null;
        try {
            Log.d(TAG, "DownloadTask:" + urls[0]);
            URL url = new URL(urls[0]);
            URLConnection conn = url.openConnection();
            conn.setConnectTimeout(this.mTimeout);
            conn.connect();
            Context ctx = (Context) this.mCtxRef.get();
            int fileLength = conn.getContentLength();
            if (!(ctx == null || this.mCallback == null || fileLength <= 0)) {
                this.mIsIndeterminate = false;
            }
            InputStream input2 = new BufferedInputStream(url.openStream());
            try {
                output = this.mOutputStream;
                byte[] data = new byte[1024];
                int total = 0;
                int lastTotal = 0;
                int lastProgress = 0;
                long startMillis = System.currentTimeMillis();
                while (!isCancelled() && ((Context) this.mCtxRef.get()) != null) {
                    int count = input2.read(data);
                    if (count == -1) {
                        break;
                    }
                    total += count;
                    if (!this.mIsIndeterminate) {
                        int progress = (int) ((((double) total) / ((double) fileLength)) * 100.0d);
                        if (((double) (total - lastTotal)) / 1000.0d > 0.5d || progress != lastProgress) {
                            publishProgress(new Integer[]{Integer.valueOf(progress), Integer.valueOf(total), Integer.valueOf(fileLength)});
                            lastTotal = total;
                            lastProgress = progress;
                        }
                    }
                    output.write(data, 0, count);
                }
                Log.d(TAG, "DownloadTask use time: " + (System.currentTimeMillis() - startMillis));
                if (isCancelled()) {
                    Integer valueOf = Integer.valueOf(3);
                    if (input2 != null) {
                        try {
                            input2.close();
                        } catch (IOException e) {
                            Log.e(TAG, "input close ioe", e);
                        }
                    }
                    if (output != null) {
                        try {
                            output.flush();
                            output.close();
                        } catch (IOException e2) {
                            Log.e(TAG, "output finally ioe", e2);
                        }
                    }
                    InputStream inputStream = input2;
                    return valueOf;
                } else if (((Context) this.mCtxRef.get()) == null) {
                    Integer valueOf2 = Integer.valueOf(2);
                    if (input2 != null) {
                        try {
                            input2.close();
                        } catch (IOException e3) {
                            Log.e(TAG, "input close ioe", e3);
                        }
                    }
                    if (output != null) {
                        try {
                            output.flush();
                            output.close();
                        } catch (IOException e4) {
                            Log.e(TAG, "output finally ioe", e4);
                        }
                    }
                    InputStream inputStream2 = input2;
                    return valueOf2;
                } else {
                    if (input2 != null) {
                        try {
                            input2.close();
                        } catch (IOException e5) {
                            Log.e(TAG, "input close ioe", e5);
                        }
                    }
                    if (output != null) {
                        try {
                            output.flush();
                            output.close();
                        } catch (IOException e6) {
                            Log.e(TAG, "output finally ioe", e6);
                        }
                    }
                    InputStream inputStream3 = input2;
                    return Integer.valueOf(200);
                }
            } catch (MalformedURLException e7) {
                e = e7;
                input = input2;
                try {
                    Log.e(TAG, "malformed", e);
                    Integer valueOf3 = Integer.valueOf(1);
                    if (input != null) {
                        try {
                            input.close();
                        } catch (IOException e8) {
                            Log.e(TAG, "input close ioe", e8);
                        }
                    }
                    if (output != null) {
                        return valueOf3;
                    }
                    try {
                        output.flush();
                        output.close();
                        return valueOf3;
                    } catch (IOException e9) {
                        Log.e(TAG, "output finally ioe", e9);
                        return valueOf3;
                    }
                } catch (Throwable th) {
                    th = th;
                }
            } catch (IOException e10) {
                e = e10;
                input = input2;
                Log.e(TAG, "ioe", e);
                Integer valueOf4 = Integer.valueOf(1);
                if (input != null) {
                    try {
                        input.close();
                    } catch (IOException e11) {
                        Log.e(TAG, "input close ioe", e11);
                    }
                }
                if (output != null) {
                    return valueOf4;
                }
                try {
                    output.flush();
                    output.close();
                    return valueOf4;
                } catch (IOException e12) {
                    Log.e(TAG, "output finally ioe", e12);
                    return valueOf4;
                }
            } catch (Exception e13) {
                e = e13;
                input = input2;
                Log.e(TAG, "wtf", e);
                Integer valueOf5 = Integer.valueOf(1);
                if (input != null) {
                    try {
                        input.close();
                    } catch (IOException e14) {
                        Log.e(TAG, "input close ioe", e14);
                    }
                }
                if (output != null) {
                    return valueOf5;
                }
                try {
                    output.flush();
                    output.close();
                    return valueOf5;
                } catch (IOException e15) {
                    Log.e(TAG, "output finally ioe", e15);
                    return valueOf5;
                }
            } catch (Throwable th2) {
                th = th2;
                input = input2;
                if (input != null) {
                    try {
                        input.close();
                    } catch (IOException e16) {
                        Log.e(TAG, "input close ioe", e16);
                    }
                }
                if (output != null) {
                    try {
                        output.flush();
                        output.close();
                    } catch (IOException e17) {
                        Log.e(TAG, "output finally ioe", e17);
                    }
                }
                throw th;
            }
        } catch (MalformedURLException e18) {
            e = e18;
            Log.e(TAG, "malformed", e);
            Integer valueOf32 = Integer.valueOf(1);
            if (input != null) {
            }
            if (output != null) {
            }
        } catch (IOException e19) {
            e = e19;
            Log.e(TAG, "ioe", e);
            Integer valueOf42 = Integer.valueOf(1);
            if (input != null) {
            }
            if (output != null) {
            }
        } catch (Exception e20) {
            e = e20;
            Log.e(TAG, "wtf", e);
            Integer valueOf52 = Integer.valueOf(1);
            if (input != null) {
            }
            if (output != null) {
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onProgressUpdate(Integer... progresses) {
        if (((Context) this.mCtxRef.get()) != null && this.mCallback != null) {
            this.mCallback.onProgressUpdate(progresses);
        }
    }

    /* access modifiers changed from: protected */
    public void onPostExecute(Integer statusCode) {
        if (((Context) this.mCtxRef.get()) != null && this.mCallback != null) {
            this.mCallback.onPostExecute(statusCode);
        }
    }
}
