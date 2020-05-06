package com.cyanogenmod.trebuchet;

import android.appwidget.AppWidgetProviderInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.PorterDuff.Mode;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import com.cyanogenmod.trebuchet.AppsCustomizeView.ContentType;
import com.konka.ios7launcher.R;
import greendroid.widget.PagedAdapter;
import java.util.ArrayList;
import java.util.Iterator;

public abstract class VidgetAdapter extends PagedAdapter {
    private static final String TAG = "VidgetAdapter";
    private static final int sPageSleepDelay = 200;
    protected ArrayList<VidgetGridAdapter> mAdapters = new ArrayList<>();
    protected int mAppIconSize;
    protected Drawable mDefaultWidgetBackground;
    protected int mDragViewMultiplyColor;
    protected HolographicOutlineHelper mHolographicOutlineHelper;
    protected IconCache mIconCache;
    protected final Launcher mLauncher;
    protected LayoutInflater mLayoutInflater;
    protected final PackageManager mPackageManager;
    protected ArrayList<AppsCustomizeAsyncTask> mRunningTasks;
    protected final int mVidgetCellHeight;
    protected final int mVidgetCellWidth;
    protected int mVidgetHSpan;
    protected int mVidgetVSpan;
    protected final float sWidgetPreviewIconPaddingPercentage = 0.25f;

    public abstract class VidgetGridAdapter extends BaseAdapter implements OnItemClickListener {
        protected int mCount = 0;
        protected int mStartIdx = 0;

        public VidgetGridAdapter() {
        }

        public void setRange(int startIdx, int count) {
            this.mStartIdx = startIdx;
            this.mCount = count;
        }

        public int getCount() {
            return Math.max(0, this.mCount);
        }

        public long getItemId(int position) {
            return (long) (this.mStartIdx + position);
        }
    }

    public VidgetAdapter(Context context, Launcher launcher, int cellWidth, int cellHeight, int hSpan, int vSpan) {
        this.mLayoutInflater = LayoutInflater.from(context);
        this.mHolographicOutlineHelper = new HolographicOutlineHelper();
        this.mVidgetCellWidth = cellWidth;
        this.mVidgetCellHeight = cellHeight;
        this.mVidgetHSpan = hSpan;
        this.mVidgetVSpan = vSpan;
        this.mLauncher = launcher;
        this.mPackageManager = context.getPackageManager();
        this.mRunningTasks = new ArrayList<>();
        this.mIconCache = ((LauncherApplication) context.getApplicationContext()).getIconCache();
        Resources resources = context.getResources();
        this.mDefaultWidgetBackground = resources.getDrawable(R.drawable.default_widget_preview_holo);
        this.mAppIconSize = resources.getDimensionPixelSize(R.dimen.app_icon_size);
        this.mDragViewMultiplyColor = resources.getColor(R.color.drag_view_multiply_color);
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return (long) position;
    }

    /* access modifiers changed from: protected */
    public void prepareLoadWidgetPreviewsTask(int page, ArrayList<Object> widgets, int cellWidth, int cellHeight, int currentPage, PagedDialogGridView currentView) {
        Iterator<AppsCustomizeAsyncTask> iter = this.mRunningTasks.iterator();
        while (iter.hasNext()) {
            AppsCustomizeAsyncTask task = (AppsCustomizeAsyncTask) iter.next();
            int taskPage = task.page;
            if (taskPage < 0 || taskPage >= getCount()) {
                task.cancel(false);
                iter.remove();
            } else {
                task.setThreadPriority(getThreadPriorityForPage(taskPage, currentPage));
            }
        }
        final int sleepMs = getSleepForPage(page, currentPage);
        final PagedDialogGridView pagedDialogGridView = currentView;
        AsyncTaskPageData pageData = new AsyncTaskPageData(page, widgets, cellWidth, cellHeight, new AsyncTaskCallback() {
            public void run(AppsCustomizeAsyncTask task, AsyncTaskPageData data) {
                try {
                    Thread.sleep((long) sleepMs);
                } catch (Exception e) {
                }
                try {
                    VidgetAdapter.this.loadWidgetPreviewsInBackground(task, data);
                } finally {
                    if (task.isCancelled()) {
                        data.cleanup(true);
                    }
                }
            }
        }, new AsyncTaskCallback() {
            public void run(AppsCustomizeAsyncTask task, AsyncTaskPageData data) {
                try {
                    VidgetAdapter.this.mRunningTasks.remove(task);
                    if (!task.isCancelled()) {
                        VidgetAdapter.this.onSyncWidgetPageItems(data, pagedDialogGridView);
                        data.cleanup(task.isCancelled());
                    }
                } finally {
                    data.cleanup(task.isCancelled());
                }
            }
        });
        AppsCustomizeAsyncTask t = new AppsCustomizeAsyncTask(page, ContentType.Widgets, Type.LoadWidgetPreviewData);
        t.setThreadPriority(getThreadPriorityForPage(page, currentPage));
        Log.d(TAG, "new asynctask");
        t.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new AsyncTaskPageData[]{pageData});
        this.mRunningTasks.add(t);
    }

    private int getWidgetPageLoadPriority(int page, int currentPage) {
        int toPage = currentPage;
        Iterator<AppsCustomizeAsyncTask> iter = this.mRunningTasks.iterator();
        int minPageDiff = Integer.MAX_VALUE;
        while (iter.hasNext()) {
            minPageDiff = Math.abs(((AppsCustomizeAsyncTask) iter.next()).page - toPage);
        }
        int rawPageDiff = Math.abs(page - toPage);
        return rawPageDiff - Math.min(rawPageDiff, minPageDiff);
    }

    private static int getThreadPriorityForPage(int page, int currentPage) {
        int pageDiff = Math.abs(page - currentPage);
        if (pageDiff <= 0) {
            return -2;
        }
        if (pageDiff <= 1) {
            return -1;
        }
        return 0;
    }

    private int getSleepForPage(int page, int currentPage) {
        return Math.max(0, getWidgetPageLoadPriority(page, currentPage) * sPageSleepDelay);
    }

    private void renderDrawableToBitmap(Drawable d, Bitmap bitmap, int x, int y, int w, int h) {
        renderDrawableToBitmap(d, bitmap, x, y, w, h, 1.0f, -1);
    }

    private void renderDrawableToBitmap(Drawable d, Bitmap bitmap, int x, int y, int w, int h, float scale, int multiplyColor) {
        if (bitmap != null) {
            Canvas c = new Canvas(bitmap);
            c.scale(scale, scale);
            Rect oldBounds = d.copyBounds();
            d.setBounds(x, y, x + w, y + h);
            d.draw(c);
            d.setBounds(oldBounds);
            if (multiplyColor != -1) {
                c.drawColor(this.mDragViewMultiplyColor, Mode.MULTIPLY);
            }
            c.setBitmap(null);
        }
    }

    private Bitmap getShortcutPreview(ResolveInfo info) {
        int bitmapSize = this.mAppIconSize;
        Bitmap preview = Bitmap.createBitmap(bitmapSize, bitmapSize, Config.ARGB_8888);
        renderDrawableToBitmap(this.mIconCache.getFullResIcon(info), preview, 0, 0, this.mAppIconSize, this.mAppIconSize);
        return preview;
    }

    private Bitmap getWidgetPreview(ComponentName provider, int previewImage, int iconId, int cellHSpan, int cellVSpan, int maxWidth, int maxHeight) {
        int bitmapWidth;
        int bitmapHeight;
        String packageName = provider.getPackageName();
        if (maxWidth < 0) {
            maxWidth = Integer.MAX_VALUE;
        }
        if (maxHeight < 0) {
            maxHeight = Integer.MAX_VALUE;
        }
        Drawable drawable = null;
        if (previewImage != 0) {
            drawable = this.mPackageManager.getDrawable(packageName, previewImage, null);
            if (drawable == null) {
                Log.w(TAG, "Can't load widget preview drawable 0x" + Integer.toHexString(previewImage) + " for provider: " + provider);
            }
        }
        boolean widgetPreviewExists = drawable != null;
        if (widgetPreviewExists) {
            bitmapWidth = drawable.getIntrinsicWidth();
            bitmapHeight = drawable.getIntrinsicHeight();
            maxWidth = Math.min(maxWidth, this.mVidgetCellWidth * cellHSpan);
            maxHeight = Math.min(maxHeight, this.mVidgetCellHeight * cellVSpan);
        } else {
            bitmapWidth = cellHSpan * this.mVidgetCellWidth;
            bitmapHeight = cellVSpan * this.mVidgetCellHeight;
            if (cellHSpan == cellVSpan) {
                int minOffset = (int) (((float) this.mAppIconSize) * 0.25f);
                if (cellHSpan <= 1) {
                    bitmapHeight = this.mAppIconSize + (minOffset * 2);
                    bitmapWidth = bitmapHeight;
                } else {
                    bitmapHeight = this.mAppIconSize + (minOffset * 4);
                    bitmapWidth = bitmapHeight;
                }
            }
        }
        float scale = 1.0f;
        if (bitmapWidth > maxWidth) {
            scale = ((float) maxWidth) / ((float) bitmapWidth);
        }
        if (((float) bitmapHeight) * scale > ((float) maxHeight)) {
            scale = ((float) maxHeight) / ((float) bitmapHeight);
        }
        if (scale != 1.0f) {
            bitmapWidth = (int) (((float) bitmapWidth) * scale);
            bitmapHeight = (int) (((float) bitmapHeight) * scale);
        }
        Bitmap preview = Bitmap.createBitmap(bitmapWidth, bitmapHeight, Config.ARGB_8888);
        if (widgetPreviewExists) {
            renderDrawableToBitmap(drawable, preview, 0, 0, bitmapWidth, bitmapHeight);
        } else {
            float iconScale = Math.min(((float) Math.min(bitmapWidth, bitmapHeight)) / ((float) (this.mAppIconSize + (((int) (((float) this.mAppIconSize) * 0.25f)) * 2))), 1.0f);
            if (!(cellHSpan == 1 && cellVSpan == 1)) {
                renderDrawableToBitmap(this.mDefaultWidgetBackground, preview, 0, 0, bitmapWidth, bitmapHeight);
            }
            Drawable icon = null;
            try {
                int hoffset = (int) (((float) (bitmapWidth / 2)) - ((((float) this.mAppIconSize) * iconScale) / 2.0f));
                int yoffset = (int) (((float) (bitmapHeight / 2)) - ((((float) this.mAppIconSize) * iconScale) / 2.0f));
                if (iconId > 0) {
                    icon = this.mIconCache.getFullResIcon(packageName, iconId);
                }
                Resources resources = this.mLauncher.getResources();
                if (icon == null) {
                    icon = resources.getDrawable(R.drawable.ic_launcher_application);
                }
                renderDrawableToBitmap(icon, preview, hoffset, yoffset, (int) (((float) this.mAppIconSize) * iconScale), (int) (((float) this.mAppIconSize) * iconScale));
            } catch (NotFoundException e) {
            }
        }
        return preview;
    }

    /* access modifiers changed from: private */
    public void loadWidgetPreviewsInBackground(AppsCustomizeAsyncTask task, AsyncTaskPageData data) {
        if (task != null) {
            task.syncThreadPriority();
        }
        ArrayList<Object> items = data.items;
        ArrayList<Bitmap> images = data.generatedImages;
        Iterator it = items.iterator();
        while (it.hasNext()) {
            Object item = it.next();
            if (task != null) {
                if (!task.isCancelled()) {
                    task.syncThreadPriority();
                } else {
                    return;
                }
            }
            if (item instanceof AppWidgetProviderInfo) {
                AppWidgetProviderInfo info = (AppWidgetProviderInfo) item;
                int[] cellSpans = this.mLauncher.getSpanForWidget(info, (int[]) null);
                images.add(getWidgetPreview(info.provider, info.previewImage, info.icon, cellSpans[0], cellSpans[1], data.maxImageWidth, data.maxImageHeight));
            } else if (item instanceof ResolveInfo) {
                images.add(getShortcutPreview((ResolveInfo) item));
            }
        }
    }

    /* access modifiers changed from: private */
    public void onSyncWidgetPageItems(AsyncTaskPageData data, PagedDialogGridView layout) {
        int i = data.page;
        int count = data.items.size();
        for (int i2 = 0; i2 < count; i2++) {
            PagedViewWidget widget = (PagedViewWidget) layout.getChildAt(i2);
            if (widget != null) {
                widget.applyPreview(new FastBitmapDrawable((Bitmap) data.generatedImages.get(i2)));
            }
        }
        layout.invalidate();
        Iterator it = this.mRunningTasks.iterator();
        while (it.hasNext()) {
            AppsCustomizeAsyncTask task = (AppsCustomizeAsyncTask) it.next();
            task.setThreadPriority(getThreadPriorityForPage(task.page, 0));
        }
    }
}
