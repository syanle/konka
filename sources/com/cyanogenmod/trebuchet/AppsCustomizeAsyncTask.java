package com.cyanogenmod.trebuchet;

import android.os.AsyncTask;
import android.os.Process;
import com.cyanogenmod.trebuchet.AppsCustomizeView.ContentType;

/* compiled from: AppsCustomizePagedView */
class AppsCustomizeAsyncTask extends AsyncTask<AsyncTaskPageData, Void, AsyncTaskPageData> {
    Type dataType;
    int page;
    ContentType pageContentType;
    int threadPriority = 0;

    AppsCustomizeAsyncTask(int p, ContentType t, Type ty) {
        this.page = p;
        this.pageContentType = t;
        this.dataType = ty;
    }

    /* access modifiers changed from: protected */
    public AsyncTaskPageData doInBackground(AsyncTaskPageData... params) {
        if (params.length != 1) {
            return null;
        }
        params[0].doInBackgroundCallback.run(this, params[0]);
        return params[0];
    }

    /* access modifiers changed from: protected */
    public void onPostExecute(AsyncTaskPageData result) {
        result.postExecuteCallback.run(this, result);
    }

    /* access modifiers changed from: 0000 */
    public void setThreadPriority(int p) {
        this.threadPriority = p;
    }

    /* access modifiers changed from: 0000 */
    public void syncThreadPriority() {
        Process.setThreadPriority(this.threadPriority);
    }
}
