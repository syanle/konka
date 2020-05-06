package com.konka.launcherblacklist;

import android.content.ComponentName;
import android.content.Context;
import android.os.AsyncTask;
import android.os.RemoteException;
import android.util.Log;
import com.konka.launcherblacklist.HttpDownloadTask.ICallback;
import com.konka.passport.service.UserInfo;
import com.konka.passport.service.xmlData;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;

public class BlackListFilter implements ICallback {
    private static final String BLACKLIST_FILENAME = "blacklist.json";
    private static final String TAG = "BlackListFilter";
    private static BlackListFilter sFilter;
    private ArrayList<ComponentName> mAppsBlackList;
    private final BlackListFormat mBlackListFormat = new BlackListFormat();
    /* access modifiers changed from: private */
    public final WeakReference<Context> mContextRef;
    /* access modifiers changed from: private */
    public HttpDownloadTask mDownloadTask;
    private GetBlackListTask mGetTask;
    private OnChangeListener mListener;
    /* access modifiers changed from: private */
    public final NetRequestFormat mNetRequestFormat = new NetRequestFormat();
    /* access modifiers changed from: private */
    public WeakReference<UserInfo> mPersonRef;
    private ArrayList<ComponentName> mShortcutsBlackList;
    /* access modifiers changed from: private */
    public String mVersion;
    private ArrayList<ComponentName> mWidgetsBlackList;

    private class GetBlackListTask extends AsyncTask<Void, Void, Boolean> {
        private GetBlackListTask() {
        }

        /* synthetic */ GetBlackListTask(BlackListFilter blackListFilter, GetBlackListTask getBlackListTask) {
            this();
        }

        /* access modifiers changed from: protected */
        public Boolean doInBackground(Void... params) {
            UserInfo person = (UserInfo) BlackListFilter.this.mPersonRef.get();
            if (person == null) {
                return null;
            }
            xmlData data = new xmlData();
            try {
                int resultCode = person.SendNetRequest("getlauncherblacklist?secplatform=0", 1, 0, data);
                Log.d(BlackListFilter.TAG, "doInBackground: ret=" + resultCode + "\n" + data.GetXmlData());
                return (resultCode != 0 || !BlackListFilter.this.mNetRequestFormat.read(new StringReader(data.GetXmlData())) || !BlackListFilter.this.mNetRequestFormat.hasNewerList(BlackListFilter.this.mVersion)) ? Boolean.valueOf(false) : Boolean.valueOf(true);
            } catch (RemoteException e) {
                Log.e(BlackListFilter.TAG, "SendNetRequest failed!", e);
                return Boolean.valueOf(false);
            }
        }

        public void onPostExecute(Boolean hasNewerList) {
            Context ctx = (Context) BlackListFilter.this.mContextRef.get();
            if (ctx != null && hasNewerList.booleanValue()) {
                try {
                    BlackListFilter.this.mDownloadTask = new HttpDownloadTask((Context) BlackListFilter.this.mContextRef.get(), BlackListFilter.this);
                    BlackListFilter.this.mDownloadTask.setOutputStream(ctx.openFileOutput(BlackListFilter.BLACKLIST_FILENAME, 0));
                    BlackListFilter.this.mDownloadTask.execute(new String[]{BlackListFilter.this.mNetRequestFormat.getListAddress()});
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public interface OnChangeListener {
        void onAppsBlackListChange(ArrayList<ComponentName> arrayList);

        void onShortcutsBlackListChange(ArrayList<ComponentName> arrayList);

        void onWidgetsBlackListChange(ArrayList<ComponentName> arrayList);
    }

    public static synchronized BlackListFilter getInstance(Context ctx) {
        BlackListFilter blackListFilter;
        synchronized (BlackListFilter.class) {
            if (sFilter == null) {
                sFilter = new BlackListFilter(ctx);
            }
            blackListFilter = sFilter;
        }
        return blackListFilter;
    }

    private BlackListFilter(Context ctx) {
        this.mContextRef = new WeakReference<>(ctx);
        getBlackListFromFile();
    }

    private void getBlackListFromFile() {
        this.mAppsBlackList = new ArrayList<>();
        this.mWidgetsBlackList = new ArrayList<>();
        this.mShortcutsBlackList = new ArrayList<>();
        Context ctx = (Context) this.mContextRef.get();
        if (ctx != null) {
            boolean isReadSuccess = null;
            try {
                FileInputStream is = ctx.openFileInput(BLACKLIST_FILENAME);
                isReadSuccess = this.mBlackListFormat.read(is, this.mAppsBlackList, this.mWidgetsBlackList, this.mShortcutsBlackList);
                if (isReadSuccess) {
                    this.mVersion = this.mBlackListFormat.getVersion();
                    Collections.sort(this.mAppsBlackList);
                    Collections.sort(this.mWidgetsBlackList);
                    Collections.sort(this.mShortcutsBlackList);
                }
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e) {
                    }
                }
            } catch (FileNotFoundException e2) {
                Log.e(TAG, "fileNotFound, maybe first time?", e2);
                if (isReadSuccess != null) {
                    try {
                        isReadSuccess.close();
                    } catch (IOException e3) {
                    }
                }
            } finally {
                if (isReadSuccess != null) {
                    try {
                        isReadSuccess.close();
                    } catch (IOException e4) {
                    }
                }
            }
        }
    }

    public ArrayList<ComponentName> getAppsBlackList() {
        if (this.mAppsBlackList == null) {
            getBlackListFromFile();
        }
        return this.mAppsBlackList;
    }

    public ArrayList<ComponentName> getWidgetsBlackList() {
        if (this.mWidgetsBlackList == null) {
            getBlackListFromFile();
        }
        return this.mWidgetsBlackList;
    }

    public ArrayList<ComponentName> getShortcutsBlackList() {
        if (this.mShortcutsBlackList == null) {
            getBlackListFromFile();
        }
        return this.mShortcutsBlackList;
    }

    public void getBlackListFromServer(UserInfo person, OnChangeListener listener) {
        this.mPersonRef = new WeakReference<>(person);
        this.mListener = listener;
        if (this.mGetTask != null) {
            this.mGetTask.cancel(true);
        }
        if (this.mDownloadTask != null) {
            this.mDownloadTask.cancel(false);
            this.mDownloadTask = null;
        }
        this.mGetTask = new GetBlackListTask(this, null);
        this.mGetTask.execute(new Void[0]);
    }

    public void onPreExecute() {
    }

    public void onProgressUpdate(Integer... progresses) {
    }

    public void onPostExecute(Integer statusCode) {
        this.mGetTask = null;
        if (statusCode.intValue() == 200) {
            getBlackListFromFile();
            if (this.mListener != null) {
                this.mListener.onAppsBlackListChange(this.mAppsBlackList);
                this.mListener.onWidgetsBlackListChange(this.mWidgetsBlackList);
                this.mListener.onShortcutsBlackListChange(this.mShortcutsBlackList);
            }
        }
    }
}
