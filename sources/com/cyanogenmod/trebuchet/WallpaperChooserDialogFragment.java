package com.cyanogenmod.trebuchet;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.WallpaperManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.SpinnerAdapter;
import com.cyanogenmod.trebuchet.RocketLauncher.Board.FlyingIcon;
import com.konka.ios7launcher.R;
import java.io.IOException;
import java.util.ArrayList;

public class WallpaperChooserDialogFragment extends DialogFragment implements OnItemSelectedListener, OnItemClickListener {
    private static final String EMBEDDED_KEY = "com.cyanogenmod.trebuchet.WallpaperChooserDialogFragment.EMBEDDED_KEY";
    private static final String TAG = "Launcher.WallpaperChooserDialogFragment";
    /* access modifiers changed from: private */
    public Bitmap mBitmap = null;
    private boolean mEmbedded;
    /* access modifiers changed from: private */
    public ArrayList<Integer> mImages = new ArrayList<>(24);
    /* access modifiers changed from: private */
    public WallpaperLoader mLoader;
    /* access modifiers changed from: private */
    public ArrayList<Integer> mThumbs = new ArrayList<>(24);
    /* access modifiers changed from: private */
    public WallpaperDrawable mWallpaperDrawable = new WallpaperDrawable();

    private class ImageAdapter extends BaseAdapter implements ListAdapter, SpinnerAdapter {
        private LayoutInflater mLayoutInflater;

        ImageAdapter(Activity activity) {
            this.mLayoutInflater = activity.getLayoutInflater();
        }

        public int getCount() {
            return WallpaperChooserDialogFragment.this.mThumbs.size();
        }

        public Object getItem(int position) {
            return Integer.valueOf(position);
        }

        public long getItemId(int position) {
            return (long) position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            View view;
            if (convertView == null) {
                view = this.mLayoutInflater.inflate(R.layout.wallpaper_item, parent, false);
            } else {
                view = convertView;
            }
            WallpaperChooserDialogFragment.setPreviewView((ImageView) view.findViewById(R.id.wallpaper_image), position, WallpaperChooserDialogFragment.this.mThumbs);
            return view;
        }
    }

    static class WallpaperDrawable extends Drawable {
        Bitmap mBitmap;
        int mIntrinsicHeight;
        int mIntrinsicWidth;

        WallpaperDrawable() {
        }

        /* access modifiers changed from: 0000 */
        public void setBitmap(Bitmap bitmap) {
            this.mBitmap = bitmap;
            if (this.mBitmap != null) {
                this.mIntrinsicWidth = this.mBitmap.getWidth();
                this.mIntrinsicHeight = this.mBitmap.getHeight();
            }
        }

        public void draw(Canvas canvas) {
            if (this.mBitmap != null) {
                int width = canvas.getWidth();
                canvas.drawBitmap(this.mBitmap, (float) ((width - this.mIntrinsicWidth) / 2), (float) ((canvas.getHeight() - this.mIntrinsicHeight) / 2), null);
            }
        }

        public int getOpacity() {
            return -1;
        }

        public void setAlpha(int alpha) {
        }

        public void setColorFilter(ColorFilter cf) {
        }
    }

    class WallpaperLoader extends AsyncTask<Integer, Void, Bitmap> {
        Options mOptions;

        WallpaperLoader() {
            this.mOptions = new Options();
            this.mOptions.inDither = false;
            this.mOptions.inPreferredConfig = Config.ARGB_8888;
        }

        /* access modifiers changed from: protected */
        public Bitmap doInBackground(Integer... params) {
            if (isCancelled()) {
                return null;
            }
            try {
                return BitmapFactory.decodeResource(WallpaperChooserDialogFragment.this.getResources(), ((Integer) WallpaperChooserDialogFragment.this.mImages.get(params[0].intValue())).intValue(), this.mOptions);
            } catch (OutOfMemoryError e) {
                return null;
            }
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(Bitmap b) {
            if (b != null) {
                if (isCancelled() || this.mOptions.mCancel) {
                    b.recycle();
                    return;
                }
                if (WallpaperChooserDialogFragment.this.mBitmap != null) {
                    WallpaperChooserDialogFragment.this.mBitmap.recycle();
                }
                View v = WallpaperChooserDialogFragment.this.getView();
                if (v != null) {
                    WallpaperChooserDialogFragment.this.mBitmap = b;
                    WallpaperChooserDialogFragment.this.mWallpaperDrawable.setBitmap(b);
                    v.postInvalidate();
                } else {
                    WallpaperChooserDialogFragment.this.mBitmap = null;
                    WallpaperChooserDialogFragment.this.mWallpaperDrawable.setBitmap(null);
                }
                WallpaperChooserDialogFragment.this.mLoader = null;
            }
        }

        /* access modifiers changed from: 0000 */
        public void cancel() {
            this.mOptions.requestCancelDecode();
            super.cancel(true);
        }
    }

    public static WallpaperChooserDialogFragment newInstance() {
        WallpaperChooserDialogFragment fragment = new WallpaperChooserDialogFragment();
        fragment.setCancelable(true);
        return fragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null || !savedInstanceState.containsKey(EMBEDDED_KEY)) {
            this.mEmbedded = isInLayout();
        } else {
            this.mEmbedded = savedInstanceState.getBoolean(EMBEDDED_KEY);
        }
    }

    public void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(EMBEDDED_KEY, this.mEmbedded);
    }

    public void onDestroy() {
        super.onDestroy();
        if (this.mLoader != null && this.mLoader.getStatus() != Status.FINISHED) {
            this.mLoader.cancel(true);
            this.mLoader = null;
        }
    }

    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        Activity activity = getActivity();
        if (activity != null) {
            activity.finish();
        }
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        findWallpapers(getActivity(), this.mThumbs, this.mImages, null);
        return null;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        findWallpapers(getActivity(), this.mThumbs, this.mImages, null);
        if (!this.mEmbedded) {
            return null;
        }
        View view = inflater.inflate(R.layout.wallpaper_chooser, container, false);
        view.setBackgroundDrawable(this.mWallpaperDrawable);
        final Gallery gallery = (Gallery) view.findViewById(R.id.gallery);
        gallery.setCallbackDuringFling(false);
        gallery.setOnItemSelectedListener(this);
        gallery.setAdapter(new ImageAdapter(getActivity()));
        view.findViewById(R.id.set).setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                WallpaperChooserDialogFragment.this.selectWallpaper(gallery.getSelectedItemPosition());
            }
        });
        return view;
    }

    static boolean setWallpaper(Context context, int position, ArrayList<Integer> mImages2) {
        try {
            ((WallpaperManager) context.getSystemService("wallpaper")).setResource(((Integer) mImages2.get(position)).intValue());
            return true;
        } catch (IOException e) {
            Log.e(TAG, "Failed to set wallpaper: " + e);
            return false;
        }
    }

    static boolean setLiveWallpaper(Context context, String component) {
        int iLine = component.indexOf(47);
        if (iLine == -1) {
            return false;
        }
        String packageName = component.substring(0, iLine);
        String className = component.substring(iLine + 1, component.length());
        Log.d(TAG, "the componentName===" + packageName + "/" + className);
        ComponentName cn = new ComponentName(packageName, className);
        try {
            WallpaperManager mWallpaperManager = WallpaperManager.getInstance(context);
            mWallpaperManager.getIWallpaperManager().setWallpaperComponent(cn);
            mWallpaperManager.setWallpaperOffsetSteps(0.5f, FlyingIcon.ANGULAR_VMIN);
            return true;
        } catch (RemoteException e) {
            return false;
        } catch (RuntimeException e2) {
            Log.w(TAG, "Failure setting wallpaper", e2);
            return false;
        }
    }

    /* access modifiers changed from: private */
    public void selectWallpaper(int position) {
        if (setWallpaper(getActivity(), position, this.mImages)) {
            Activity activity = getActivity();
            activity.setResult(-1);
            activity.finish();
        }
    }

    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        selectWallpaper(position);
    }

    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
        if (!(this.mLoader == null || this.mLoader.getStatus() == Status.FINISHED)) {
            this.mLoader.cancel();
        }
        this.mLoader = (WallpaperLoader) new WallpaperLoader().execute(new Integer[]{Integer.valueOf(position)});
    }

    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    static void findWallpapers(Context context, ArrayList<Integer> mThumbs2, ArrayList<Integer> mImages2, ArrayList<String> wallpaperIntents) {
        Resources resources = context.getResources();
        String packageName = resources.getResourcePackageName(R.array.wallpapers_image);
        addWallpapersIntent(resources, R.array.livewallpaper_intent_with_live, wallpaperIntents);
        addWallpapers(resources, packageName, R.array.wallpapers_image_with_live, mThumbs2, mImages2);
    }

    static void addWallpapers(Resources resources, String packageName, int list, ArrayList<Integer> mThumbs2, ArrayList<Integer> mImages2) {
        String[] extras;
        for (String extra : resources.getStringArray(list)) {
            int res = resources.getIdentifier(extra, "drawable", packageName);
            if (res != 0 || extra.contains("live")) {
                int thumbRes = resources.getIdentifier(new StringBuilder(String.valueOf(extra)).append("_small").toString(), "drawable", packageName);
                if (thumbRes != 0) {
                    mThumbs2.add(Integer.valueOf(thumbRes));
                    mImages2.add(Integer.valueOf(res));
                }
            }
        }
    }

    static void addWallpapersIntent(Resources resources, int list, ArrayList<String> wallpaperIntents) {
        for (String intent : resources.getStringArray(list)) {
            wallpaperIntents.add(intent);
        }
    }

    static void setPreviewView(ImageView image, int position, ArrayList<Integer> mThumbs2) {
        int thumbRes = ((Integer) mThumbs2.get(position)).intValue();
        image.setImageResource(thumbRes);
        Drawable thumbDrawable = image.getDrawable();
        if (thumbDrawable != null) {
            thumbDrawable.setDither(true);
        } else {
            Log.e(TAG, "Error decoding thumbnail resId=" + thumbRes + " for wallpaper #" + position);
        }
    }
}
