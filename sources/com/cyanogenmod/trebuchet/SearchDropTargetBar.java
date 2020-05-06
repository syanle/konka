package com.cyanogenmod.trebuchet;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProviderInfo;
import android.bluetooth.BluetoothAdapter;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.DialogInterface.OnShowListener;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnHoverListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.AbsListView.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.cyanogenmod.trebuchet.AppsCustomizeView.SortMode;
import com.cyanogenmod.trebuchet.BatteryMonitor.IBatteryUpdateListener;
import com.cyanogenmod.trebuchet.BluetoothMonitor.IBluetoothUpdateListener;
import com.cyanogenmod.trebuchet.LauncherModel.WidgetAndShortcutNameComparator;
import com.cyanogenmod.trebuchet.MessageManager.IMessageListener;
import com.cyanogenmod.trebuchet.NetworkMonitor.INetworkUpdateListener;
import com.cyanogenmod.trebuchet.RocketLauncher.Board.FlyingIcon;
import com.cyanogenmod.trebuchet.TimeMonitor.ITimeUpdateListener;
import com.cyanogenmod.trebuchet.UsbMonitor.IUsbUpdateListener;
import com.cyanogenmod.trebuchet.UserCenterMonitor.IUserCenterUpdateListener;
import com.cyanogenmod.trebuchet.VidgetAdapter.VidgetGridAdapter;
import com.cyanogenmod.trebuchet.preference.PreferencesProvider.Interface.Homescreen;
import com.konka.android.storage.KKStorageManager;
import com.konka.ios7launcher.R;
import com.konka.launcherblacklist.BlackListFilter;
import com.tencent.stat.common.StatConstants;
import greendroid.widget.PagedAdapter;
import greendroid.widget.PagedDialog;
import greendroid.widget.PagedDialog.Builder;
import greendroid.widget.PagedView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import net.londatiga.android.ActionItem;
import net.londatiga.android.ArrowedPopupWindow;
import net.londatiga.android.QuickAction;
import net.londatiga.android.QuickAction.OnActionItemClickListener;

public class SearchDropTargetBar extends FrameLayout implements DragListener, ITimeUpdateListener, IUserCenterUpdateListener, INetworkUpdateListener, IUsbUpdateListener, IBatteryUpdateListener, IMessageListener, IBluetoothUpdateListener, OnActionItemClickListener, OnClickListener, OnHoverListener, OnFocusChangeListener {
    private static final boolean DEBUG = true;
    private static final int ID_QUICKACTION_ADD_APPS = 0;
    private static final int ID_QUICKACTION_ADD_FOLDER = 2;
    private static final int ID_QUICKACTION_ADD_SHORTCUT = 3;
    private static final int ID_QUICKACTION_ADD_WIDGET = 1;
    private static final int NUM_COLUMNS_APP = 7;
    private static final int NUM_COLUMNS_SHORTCUT = 4;
    private static final int NUM_COLUMNS_WIDGET = 4;
    private static final int NUM_ROWS_APP = 3;
    private static final int NUM_ROWS_SHORTCUT = 2;
    private static final int NUM_ROWS_WIDGET = 2;
    private static final int NUM_SHORTCUT_HSPAN = 3;
    private static final int NUM_SHORTCUT_VSPAN = 3;
    private static final int NUM_WIDGET_HSPAN = 3;
    private static final int NUM_WIDGET_VSPAN = 3;
    private static final String TAG = "SearchDropTargetBar";
    private static final int[][] WIFI_SIGNAL_STRENGTH = {new int[]{R.drawable.qsb_network_icon_wifi_signal_0, R.drawable.qsb_network_icon_wifi_signal_1, R.drawable.qsb_network_icon_wifi_signal_2, R.drawable.qsb_network_icon_wifi_signal_3, R.drawable.qsb_network_icon_wifi_signal_4}, new int[]{R.drawable.qsb_network_icon_wifi_signal_0, R.drawable.qsb_network_icon_wifi_signal_1, R.drawable.qsb_network_icon_wifi_signal_2, R.drawable.qsb_network_icon_wifi_signal_3, R.drawable.qsb_network_icon_wifi_signal_4}};
    private static final int sTransitionInDuration = 200;
    private static final int sTransitionOutDuration = 175;
    private final String[] USB_NAME_LIST;
    private final QuickAction mAddQuickAction;
    private ImageView mAllAppsButton;
    private int mAppUpgradeCount;
    private UpgradeItem mAppUpgradeItem;
    /* access modifiers changed from: private */
    public ArrayList<ApplicationInfo> mApps;
    /* access modifiers changed from: private */
    public PagedDialog mAppsDialog;
    private int mBarHeight;
    private ImageView mBatteryStatusIndicator;
    private ImageView mBluetoothButton;
    /* access modifiers changed from: private */
    public final int mCellHeight;
    /* access modifiers changed from: private */
    public final int mCellWidth;
    /* access modifiers changed from: private */
    public final ArrowedPopupWindow mChangeWallpaperWindow;
    private int mCurrentInterfaceId;
    private String mCurrentInterfaceText;
    private boolean mDeferOnDragEnd;
    private ButtonDropTarget mDeleteDropTarget;
    /* access modifiers changed from: private */
    public View mDropTargetBar;
    private AnimatorSet mDropTargetBarFadeInAnim;
    private AnimatorSet mDropTargetBarFadeOutAnim;
    private Handler mHandler;
    private ImageView mHomescreenAddButton;
    /* access modifiers changed from: private */
    public final ArrayList<Integer> mImages;
    private ButtonDropTarget mInfoDropTarget;
    private boolean mInitAddQuickAction;
    private boolean mInitChangeWallpaperWindow;
    private boolean mInitPackages;
    private boolean mInitUpgradeWindow;
    private boolean mIsLoggedIn;
    private boolean mIsNetWorkWindowInited;
    private boolean mIsSearchBarHidden;
    private boolean mIsSystemSettingsWindwoInited;
    private boolean mIsUsbWindowInited;
    private boolean mIsUserCenterWindowInited;
    private ImageView mKKSearchButton;
    /* access modifiers changed from: private */
    public Launcher mLauncher;
    private ImageView mLockScreenButton;
    private ImageView mMessageCenterButton;
    private TextView mMessageCenterCount;
    private ViewGroup mMessageCenterLayout;
    private final ArrowedPopupWindow mNetWorkWindow;
    private ViewGroup mNetWorkWindowContent;
    private ImageView mNetworkStatusIndicator;
    private String mNewSystemVersion;
    private final PackageManager mPackageManager;
    private Drawable mPreviousBackground;
    /* access modifiers changed from: private */
    public View mQSBSearchBar;
    private ObjectAnimator mQSBSearchBarFadeInAnim;
    private ObjectAnimator mQSBSearchBarFadeOutAnim;
    /* access modifiers changed from: private */
    public View mQSBStaticBar;
    private final Resources mResources;
    private int mSdcardCount;
    private ImageView mSettingsButton;
    /* access modifiers changed from: private */
    public List<ResolveInfo> mShortcuts;
    /* access modifiers changed from: private */
    public PagedDialog mShortcutsDialog;
    private final boolean mShowQSBSearchBar;
    private SortMode mSortMode;
    private ArrayList<String> mStoragePath;
    private final ArrowedPopupWindow mSystemSettingsWindow;
    private ViewGroup mSystemSettingsWindowContent;
    private UpgradeItem mSystemUpgradeItem;
    /* access modifiers changed from: private */
    public final int mTextFocusColor;
    /* access modifiers changed from: private */
    public final int mTextNormalColor;
    private ImageView mThemeChangeButton;
    private final ArrayList<Integer> mThumbs;
    private TextView mTimeView;
    private int mUdiskCount;
    private ImageView mUpgradeButton;
    private ImageView mUpgradeDivider;
    /* access modifiers changed from: private */
    public final ArrowedPopupWindow mUpgradeWindow;
    private TextView mUsbStatusCount;
    private ImageView mUsbStatusIndicator;
    /* access modifiers changed from: private */
    public final ArrowedPopupWindow mUsbWindow;
    private ViewGroup mUsbWindowContent;
    private ImageView mUserCenterButton;
    private final ArrowedPopupWindow mUserCenterWindow;
    private ViewGroup mUserCenterWindowContent;
    private ImageView mWallpaperChangeButton;
    /* access modifiers changed from: private */
    public final ArrayList<String> mWallpaperComponents;
    private final String[] mWidgetBlackList;
    /* access modifiers changed from: private */
    public List<AppWidgetProviderInfo> mWidgets;
    /* access modifiers changed from: private */
    public PagedDialog mWidgetsDialog;

    private class AppsAdapter extends PagedAdapter implements OnDismissListener {
        private final ArrayList<AppsGridViewAdapter> mAdapters = new ArrayList<>();
        /* access modifiers changed from: private */
        public final Context mContext;
        /* access modifiers changed from: private */
        public final HolographicOutlineHelper mHolographicOutlineHelper;
        private final LayoutInflater mLayoutInflater;
        private final OnItemSelectedListener mOnItemSelectedListener = new OnItemSelectedListener() {
            private View mSelected;

            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                if (this.mSelected != null) {
                    ((TextView) this.mSelected.getTag()).setTextColor(SearchDropTargetBar.this.mTextNormalColor);
                }
                ((TextView) view.getTag()).setTextColor(SearchDropTargetBar.this.mTextFocusColor);
                this.mSelected = view;
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                if (this.mSelected != null) {
                    ((TextView) this.mSelected.getTag()).setTextColor(SearchDropTargetBar.this.mTextNormalColor);
                }
                this.mSelected = null;
            }
        };
        private final OnKeyListener mOnKeyListener = new OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == 93 || keyCode == 92) {
                    PagedView pagedView = (PagedView) v.getParent();
                    if (event.getAction() == 0) {
                        return pagedView.onKeyDown(keyCode, event);
                    }
                    return pagedView.onKeyUp(keyCode, event);
                } else if (event.getAction() == 0) {
                    return v.onKeyDown(keyCode, event);
                } else {
                    return v.onKeyUp(keyCode, event);
                }
            }
        };
        /* access modifiers changed from: private */
        public final HashSet<ApplicationInfo> mSelectedApps = new HashSet<>(5);

        private class AppsGridViewAdapter extends BaseAdapter implements OnItemClickListener {
            private int mCount;
            private final OnHoverListener mHoverListener;
            private int mStartIdx;

            private AppsGridViewAdapter() {
                this.mStartIdx = 0;
                this.mCount = 0;
                this.mHoverListener = new OnHoverListener() {
                    public boolean onHover(View v, MotionEvent event) {
                        switch (event.getAction()) {
                            case 9:
                                v.setBackgroundResource(R.drawable.app_hover);
                                return true;
                            case 10:
                                v.setBackgroundResource(0);
                                return true;
                            default:
                                return false;
                        }
                    }
                };
            }

            /* synthetic */ AppsGridViewAdapter(AppsAdapter appsAdapter, AppsGridViewAdapter appsGridViewAdapter) {
                this();
            }

            public void setRange(int startIdx, int count) {
                this.mStartIdx = startIdx;
                this.mCount = count;
            }

            public int getCount() {
                return Math.max(0, this.mCount);
            }

            public Object getItem(int position) {
                if (position < this.mCount) {
                    return SearchDropTargetBar.this.mApps.get(this.mStartIdx + position);
                }
                return null;
            }

            public long getItemId(int position) {
                return (long) (this.mStartIdx + position);
            }

            public View getView(int position, View convertView, ViewGroup parent) {
                if (this.mStartIdx + position >= SearchDropTargetBar.this.mApps.size()) {
                    return null;
                }
                ApplicationInfo info = (ApplicationInfo) SearchDropTargetBar.this.mApps.get(this.mStartIdx + position);
                if (convertView == null) {
                    convertView = new PagedDialogViewIcon(AppsAdapter.this.mContext);
                    convertView.setOnHoverListener(this.mHoverListener);
                    ((PagedDialogViewIcon) convertView).setLayoutParams(new LayoutParams(SearchDropTargetBar.this.mCellWidth, SearchDropTargetBar.this.mCellHeight));
                    convertView.setTag(convertView.findViewById(R.id.paged_view_icon));
                }
                ((PagedDialogViewIcon) convertView).applyFromApplicationInfo(info, AppsAdapter.this.mHolographicOutlineHelper);
                return convertView;
            }

            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                PagedDialogViewIcon icon = (PagedDialogViewIcon) view;
                ApplicationInfo info = icon.getApplicationInfo();
                Log.d(SearchDropTargetBar.TAG, "onItemClick componame=(" + info.componentName + ")");
                if (icon.isChecked()) {
                    AppsAdapter.this.mSelectedApps.add(info);
                } else {
                    AppsAdapter.this.mSelectedApps.remove(info);
                }
            }
        }

        public AppsAdapter(Context context) {
            this.mContext = context;
            this.mLayoutInflater = LayoutInflater.from(context);
            this.mHolographicOutlineHelper = new HolographicOutlineHelper();
        }

        public HashSet<ApplicationInfo> getSelectedApps() {
            return this.mSelectedApps;
        }

        public void onDismiss(DialogInterface d) {
            this.mSelectedApps.clear();
            notifyDataSetChanged();
        }

        public int getCount() {
            return ((SearchDropTargetBar.this.mApps.size() + 21) - 1) / 21;
        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return (long) position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            AppsGridViewAdapter adapter;
            if (convertView == null) {
                convertView = this.mLayoutInflater.inflate(R.layout.paged_dialog_apps, null);
                adapter = new AppsGridViewAdapter(this, null);
                this.mAdapters.add(adapter);
                GridView gv = (GridView) convertView;
                gv.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
                gv.setOnKeyListener(this.mOnKeyListener);
                gv.setNumColumns(7);
                gv.setChoiceMode(2);
                gv.setAdapter(adapter);
                gv.setOnItemClickListener(adapter);
                gv.setOnItemSelectedListener(this.mOnItemSelectedListener);
            } else {
                adapter = (AppsGridViewAdapter) ((GridView) convertView).getAdapter();
            }
            int startIdx = position * 21;
            int count = Math.min(21, SearchDropTargetBar.this.mApps.size() - startIdx);
            adapter.setRange(startIdx, count);
            ((GridView) convertView).clearChoices();
            for (int i = startIdx; i < startIdx + count; i++) {
                if (this.mSelectedApps.contains((ApplicationInfo) SearchDropTargetBar.this.mApps.get(i))) {
                    ((GridView) convertView).setItemChecked(i - startIdx, true);
                }
            }
            adapter.notifyDataSetChanged();
            convertView.requestFocus();
            return convertView;
        }
    }

    private class ShortcutsAdapter extends VidgetAdapter {
        private final OnItemSelectedListener mOnItemSelectedListener = new OnItemSelectedListener() {
            private View mSelected;

            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                if (this.mSelected != null) {
                    ViewHolder holder = (ViewHolder) this.mSelected.getTag();
                    holder.mNameView.setTextColor(SearchDropTargetBar.this.mTextNormalColor);
                    holder.mDimsView.setTextColor(SearchDropTargetBar.this.mTextNormalColor);
                }
                ViewHolder holder2 = (ViewHolder) view.getTag();
                holder2.mNameView.setTextColor(SearchDropTargetBar.this.mTextFocusColor);
                holder2.mDimsView.setTextColor(SearchDropTargetBar.this.mTextFocusColor);
                this.mSelected = view;
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                if (this.mSelected != null) {
                    ViewHolder holder = (ViewHolder) this.mSelected.getTag();
                    holder.mNameView.setTextColor(SearchDropTargetBar.this.mTextNormalColor);
                    holder.mDimsView.setTextColor(SearchDropTargetBar.this.mTextNormalColor);
                }
                this.mSelected = null;
            }
        };
        private final OnKeyListener mOnKeyListener = new OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == 93 || keyCode == 92) {
                    PagedView pagedView = (PagedView) v.getParent();
                    if (event.getAction() == 0) {
                        return pagedView.onKeyDown(keyCode, event);
                    }
                    return pagedView.onKeyUp(keyCode, event);
                } else if (event.getAction() == 0) {
                    return v.onKeyDown(keyCode, event);
                } else {
                    return v.onKeyUp(keyCode, event);
                }
            }
        };

        private class ShortcutsGridViewAdapter extends VidgetGridAdapter {
            private final OnHoverListener mHoverListener;

            private ShortcutsGridViewAdapter() {
                super();
                this.mHoverListener = new OnHoverListener() {
                    public boolean onHover(View v, MotionEvent event) {
                        switch (event.getAction()) {
                            case 9:
                                v.setBackgroundResource(R.drawable.app_hover);
                                return true;
                            case 10:
                                v.setBackgroundResource(0);
                                return true;
                            default:
                                return false;
                        }
                    }
                };
            }

            /* synthetic */ ShortcutsGridViewAdapter(ShortcutsAdapter shortcutsAdapter, ShortcutsGridViewAdapter shortcutsGridViewAdapter) {
                this();
            }

            public Object getItem(int position) {
                if (position < this.mCount) {
                    return SearchDropTargetBar.this.mShortcuts.get(this.mStartIdx + position);
                }
                return null;
            }

            public View getView(int position, View convertView, ViewGroup parent) {
                if (this.mStartIdx + position >= SearchDropTargetBar.this.mShortcuts.size()) {
                    return null;
                }
                ResolveInfo info = (ResolveInfo) SearchDropTargetBar.this.mShortcuts.get(this.mStartIdx + position);
                if (convertView == null) {
                    convertView = ShortcutsAdapter.this.mLayoutInflater.inflate(R.layout.paged_dialog_widget_preview, null, false);
                    convertView.setOnHoverListener(this.mHoverListener);
                    ((PagedViewWidget) convertView).setLayoutParams(new LayoutParams(ShortcutsAdapter.this.mVidgetHSpan * ShortcutsAdapter.this.mVidgetCellWidth, ShortcutsAdapter.this.mVidgetVSpan * ShortcutsAdapter.this.mVidgetCellHeight));
                    ViewHolder holder = new ViewHolder(ShortcutsAdapter.this, null);
                    holder.mInfo = new PendingAddItemInfo();
                    holder.mNameView = (TextView) convertView.findViewById(R.id.widget_name);
                    holder.mDimsView = (TextView) convertView.findViewById(R.id.widget_dims);
                    convertView.setTag(holder);
                }
                PendingAddItemInfo itemInfo = ((ViewHolder) convertView.getTag()).mInfo;
                itemInfo.itemType = 1;
                itemInfo.componentName = new ComponentName(info.activityInfo.packageName, info.activityInfo.name);
                ((PagedViewWidget) convertView).applyFromResolveInfo(ShortcutsAdapter.this.mPackageManager, info, ShortcutsAdapter.this.mHolographicOutlineHelper);
                return convertView;
            }

            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                PendingAddItemInfo info = ((ViewHolder) view.getTag()).mInfo;
                CellLayout currentCellLayout = ShortcutsAdapter.this.mLauncher.getWorkspace().getCurrentDropLayout();
                SearchDropTargetBar.this.mShortcutsDialog.dismiss();
                ShortcutsAdapter.this.mLauncher.addExternalItemToScreen(info, currentCellLayout);
            }
        }

        private class ViewHolder {
            TextView mDimsView;
            public PendingAddItemInfo mInfo;
            TextView mNameView;

            private ViewHolder() {
            }

            /* synthetic */ ViewHolder(ShortcutsAdapter shortcutsAdapter, ViewHolder viewHolder) {
                this();
            }
        }

        public ShortcutsAdapter(Context context, Launcher launcher, int cellWidth, int cellHeight, int cellCountX, int cellCountY) {
            super(context, launcher, cellWidth, cellHeight, cellCountX, cellCountY);
        }

        public int getCount() {
            return ((SearchDropTargetBar.this.mShortcuts.size() + 8) - 1) / 8;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ShortcutsGridViewAdapter adapter;
            if (convertView == null) {
                convertView = this.mLayoutInflater.inflate(R.layout.paged_dialog_apps, null);
                adapter = new ShortcutsGridViewAdapter(this, null);
                this.mAdapters.add(adapter);
                GridView gv = (GridView) convertView;
                gv.setOnKeyListener(this.mOnKeyListener);
                gv.setNumColumns(4);
                gv.setChoiceMode(0);
                gv.setAdapter(adapter);
                gv.setOnItemClickListener(adapter);
                gv.setOnItemSelectedListener(this.mOnItemSelectedListener);
            } else {
                adapter = (ShortcutsGridViewAdapter) ((GridView) convertView).getAdapter();
            }
            final PagedDialogGridView layout = (PagedDialogGridView) convertView;
            final int page = position;
            if (!layout.isSetOnLayoutListener()) {
                layout.setOnLayoutListener(new Runnable() {
                    public void run() {
                        int maxPreviewWidth = ShortcutsAdapter.this.mVidgetCellWidth;
                        int maxPreviewHeight = ShortcutsAdapter.this.mVidgetCellHeight;
                        if (layout.getChildCount() > 0) {
                            int[] maxSize = ((PagedViewWidget) layout.getChildAt(0)).getPreviewSize();
                            maxPreviewWidth = maxSize[0];
                            maxPreviewHeight = maxSize[1];
                        }
                        ArrayList<Object> items = new ArrayList<>();
                        int offset = page * 8;
                        for (int i = offset; i < Math.min(offset + 8, SearchDropTargetBar.this.mShortcuts.size()); i++) {
                            items.add(SearchDropTargetBar.this.mShortcuts.get(i));
                        }
                        ShortcutsAdapter.this.prepareLoadWidgetPreviewsTask(page, items, maxPreviewWidth, maxPreviewHeight, 0, layout);
                    }
                });
            }
            int startIdx = position * 8;
            adapter.setRange(startIdx, Math.min(8, SearchDropTargetBar.this.mShortcuts.size() - startIdx));
            adapter.notifyDataSetChanged();
            convertView.requestFocus();
            return convertView;
        }
    }

    private class WidgetsAdapter extends VidgetAdapter {
        private final OnItemSelectedListener mOnItemSelectedListener = new OnItemSelectedListener() {
            private View mSelected;

            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                if (this.mSelected != null) {
                    ViewHolder holder = (ViewHolder) this.mSelected.getTag();
                    holder.mNameView.setTextColor(SearchDropTargetBar.this.mTextNormalColor);
                    holder.mDimsView.setTextColor(SearchDropTargetBar.this.mTextNormalColor);
                }
                ViewHolder holder2 = (ViewHolder) view.getTag();
                holder2.mNameView.setTextColor(SearchDropTargetBar.this.mTextFocusColor);
                holder2.mDimsView.setTextColor(SearchDropTargetBar.this.mTextFocusColor);
                this.mSelected = view;
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                if (this.mSelected != null) {
                    ViewHolder holder = (ViewHolder) this.mSelected.getTag();
                    holder.mNameView.setTextColor(SearchDropTargetBar.this.mTextNormalColor);
                    holder.mDimsView.setTextColor(SearchDropTargetBar.this.mTextNormalColor);
                }
                this.mSelected = null;
            }
        };
        private final OnKeyListener mOnKeyListener = new OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == 93 || keyCode == 92) {
                    PagedView pagedView = (PagedView) v.getParent();
                    if (event.getAction() == 0) {
                        return pagedView.onKeyDown(keyCode, event);
                    }
                    return pagedView.onKeyUp(keyCode, event);
                } else if (event.getAction() == 0) {
                    return v.onKeyDown(keyCode, event);
                } else {
                    return v.onKeyUp(keyCode, event);
                }
            }
        };

        private class ViewHolder {
            TextView mDimsView;
            public PendingAddWidgetInfo mInfo;
            TextView mNameView;

            private ViewHolder() {
            }

            /* synthetic */ ViewHolder(WidgetsAdapter widgetsAdapter, ViewHolder viewHolder) {
                this();
            }
        }

        private class WidgetsGridViewAdapter extends VidgetGridAdapter {
            private final OnHoverListener mHoverListener;

            private WidgetsGridViewAdapter() {
                super();
                this.mHoverListener = new OnHoverListener() {
                    public boolean onHover(View v, MotionEvent event) {
                        switch (event.getAction()) {
                            case 9:
                                v.setBackgroundResource(R.drawable.app_hover);
                                return true;
                            case 10:
                                v.setBackgroundResource(0);
                                return true;
                            default:
                                return false;
                        }
                    }
                };
            }

            /* synthetic */ WidgetsGridViewAdapter(WidgetsAdapter widgetsAdapter, WidgetsGridViewAdapter widgetsGridViewAdapter) {
                this();
            }

            public Object getItem(int position) {
                if (position < this.mCount) {
                    return SearchDropTargetBar.this.mWidgets.get(this.mStartIdx + position);
                }
                return null;
            }

            public View getView(int position, View convertView, ViewGroup parent) {
                if (this.mStartIdx + position >= SearchDropTargetBar.this.mWidgets.size()) {
                    return null;
                }
                AppWidgetProviderInfo info = (AppWidgetProviderInfo) SearchDropTargetBar.this.mWidgets.get(this.mStartIdx + position);
                if (convertView == null) {
                    convertView = WidgetsAdapter.this.mLayoutInflater.inflate(R.layout.paged_dialog_widget_preview, null, false);
                    convertView.setOnHoverListener(this.mHoverListener);
                    ((PagedViewWidget) convertView).setLayoutParams(new LayoutParams(WidgetsAdapter.this.mVidgetHSpan * WidgetsAdapter.this.mVidgetCellWidth, WidgetsAdapter.this.mVidgetVSpan * WidgetsAdapter.this.mVidgetCellHeight));
                    ViewHolder holder = new ViewHolder(WidgetsAdapter.this, null);
                    holder.mInfo = new PendingAddWidgetInfo(info, null, null);
                    holder.mNameView = (TextView) convertView.findViewById(R.id.widget_name);
                    holder.mDimsView = (TextView) convertView.findViewById(R.id.widget_dims);
                    convertView.setTag(holder);
                }
                PendingAddWidgetInfo tagInfo = ((ViewHolder) convertView.getTag()).mInfo;
                int[] cellSpans = WidgetsAdapter.this.mLauncher.getSpanForWidget(info, (int[]) null);
                tagInfo.spanX = cellSpans[0];
                tagInfo.spanY = cellSpans[1];
                ((PagedViewWidget) convertView).applyFromAppWidgetProviderInfo(info, -1, cellSpans, WidgetsAdapter.this.mHolographicOutlineHelper);
                return convertView;
            }

            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                PendingAddWidgetInfo info = ((ViewHolder) view.getTag()).mInfo;
                CellLayout currentCellLayout = WidgetsAdapter.this.mLauncher.getWorkspace().getCurrentDropLayout();
                SearchDropTargetBar.this.mWidgetsDialog.dismiss();
                WidgetsAdapter.this.mLauncher.addExternalItemToScreen(info, currentCellLayout);
                Log.d(SearchDropTargetBar.TAG, "onItemClick Widget componame=(" + info.componentName + ")");
            }
        }

        public WidgetsAdapter(Context context, Launcher launcher, int cellWidth, int cellHeight, int cellCountX, int cellCountY) {
            super(context, launcher, cellWidth, cellHeight, cellCountX, cellCountY);
        }

        public int getCount() {
            return ((SearchDropTargetBar.this.mWidgets.size() + 8) - 1) / 8;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            WidgetsGridViewAdapter adapter;
            if (convertView == null) {
                convertView = this.mLayoutInflater.inflate(R.layout.paged_dialog_apps, null);
                adapter = new WidgetsGridViewAdapter(this, null);
                this.mAdapters.add(adapter);
                GridView gv = (GridView) convertView;
                gv.setOnKeyListener(this.mOnKeyListener);
                gv.setNumColumns(4);
                gv.setChoiceMode(0);
                gv.setAdapter(adapter);
                gv.setOnItemClickListener(adapter);
                gv.setOnItemSelectedListener(this.mOnItemSelectedListener);
            } else {
                adapter = (WidgetsGridViewAdapter) ((GridView) convertView).getAdapter();
            }
            final PagedDialogGridView layout = (PagedDialogGridView) convertView;
            final int page = position;
            if (!layout.isSetOnLayoutListener()) {
                layout.setOnLayoutListener(new Runnable() {
                    public void run() {
                        int maxPreviewWidth = WidgetsAdapter.this.mVidgetCellWidth;
                        int maxPreviewHeight = WidgetsAdapter.this.mVidgetCellHeight;
                        if (layout.getChildCount() > 0) {
                            int[] maxSize = ((PagedViewWidget) layout.getChildAt(0)).getPreviewSize();
                            maxPreviewWidth = maxSize[0];
                            maxPreviewHeight = maxSize[1];
                        }
                        ArrayList<Object> items = new ArrayList<>();
                        int offset = page * 8;
                        for (int i = offset; i < Math.min(offset + 8, SearchDropTargetBar.this.mWidgets.size()); i++) {
                            items.add(SearchDropTargetBar.this.mWidgets.get(i));
                        }
                        WidgetsAdapter.this.prepareLoadWidgetPreviewsTask(page, items, maxPreviewWidth, maxPreviewHeight, 0, layout);
                    }
                });
            }
            int startIdx = position * 8;
            adapter.setRange(startIdx, Math.min(8, SearchDropTargetBar.this.mWidgets.size() - startIdx));
            adapter.notifyDataSetChanged();
            convertView.requestFocus();
            return convertView;
        }
    }

    public void onMessageCenterUpdate(int unreadCount) {
        if (this.mMessageCenterButton != null) {
            if (unreadCount > 0) {
                this.mMessageCenterCount.setBackgroundResource(R.drawable.conn_stat_usb_count_bg);
                if (unreadCount < 100) {
                    this.mMessageCenterCount.setText(Integer.toString(unreadCount));
                } else {
                    this.mMessageCenterCount.setText("99+");
                }
            } else {
                this.mMessageCenterCount.setBackground(null);
                this.mMessageCenterCount.setText(StatConstants.MTA_COOPERATION_TAG);
            }
        }
    }

    public void onUpdateTime(Bundle newConnectivity) {
        String time = newConnectivity.getString(TimeMonitor.KET_CURRENT_TIME);
        Log.d(TAG, time);
        this.mTimeView.setText(time);
    }

    public void onUpdateUserCenterConnectivity(Bundle newConnectivity) {
        Log.d(TAG, "UpdateUserCenterConnectivity");
        this.mIsLoggedIn = newConnectivity.getBoolean(UserCenterMonitor.KEY_USERCENTER_IS_LOGGEDIN);
        int i = newConnectivity.getInt(UserCenterMonitor.KEY_USERCENTER_MSG_COUNT);
        String passid = newConnectivity.getString(UserCenterMonitor.KEY_USERCENTER_PASSID);
        String area = newConnectivity.getString(UserCenterMonitor.KEY_USERCENTER_AREA);
        Bitmap headpic = (Bitmap) newConnectivity.getParcelable(UserCenterMonitor.KEY_USERCENTER_HEADPIC);
        ImageView headPicView = (ImageView) this.mUserCenterWindowContent.findViewById(R.id.user_center_head_pic);
        TextView userNameView = (TextView) this.mUserCenterWindowContent.findViewById(R.id.user_center_name);
        TextView areaView = (TextView) this.mUserCenterWindowContent.findViewById(R.id.user_center_location);
        if (headpic != null) {
            headPicView.setImageBitmap(headpic);
        }
        userNameView.setText(passid);
        areaView.setText(area);
        if (this.mIsLoggedIn) {
        }
        MessageManager.getInstance().updateUserCenterConnectivity(this.mIsLoggedIn, passid);
    }

    public void onUpdateNetworkConnectivity(Bundle newConnectivity) {
        this.mCurrentInterfaceId = newConnectivity.getInt(NetworkMonitor.KEY_NET_INTERFACE_ID, 0);
        int statusId = newConnectivity.getInt(NetworkMonitor.KEY_NET_STATUS_ID, 0);
        boolean z = newConnectivity.getBoolean(NetworkMonitor.KEY_NET_WIFI_APEXIST, false);
        boolean z2 = newConnectivity.getBoolean(NetworkMonitor.KEY_NET_WIFI_HASDEVICE, false);
        TextView networkName = (TextView) this.mNetWorkWindowContent.findViewById(R.id.net_name);
        TextView networkStatus = (TextView) this.mNetWorkWindowContent.findViewById(R.id.net_status);
        switch (this.mCurrentInterfaceId) {
            case 0:
                CharSequence string = newConnectivity.getString(NetworkMonitor.KEY_NET_ETHERNET_IP);
                switch (statusId) {
                    case 0:
                        this.mNetworkStatusIndicator.setImageResource(R.drawable.qsb_network_icon_eth_disconnected);
                        networkStatus.setText(R.string.net_status_disconnected);
                        networkName.setText(getResources().getText(R.string.net_name));
                        break;
                    case 1:
                        this.mNetworkStatusIndicator.setImageResource(R.drawable.qsb_network_icon_eth_unreachable);
                        networkName.setText(getResources().getText(R.string.net_name));
                        networkStatus.setText(R.string.net_status_unreachable);
                        break;
                    case 2:
                        this.mNetworkStatusIndicator.setImageResource(R.drawable.qsb_network_icon_eth_connected);
                        networkStatus.setText(R.string.net_status_connected);
                        if (string == null) {
                            string = getResources().getText(R.string.net_name);
                        }
                        networkName.setText(string);
                        break;
                }
            case 1:
                String wifiSsid = newConnectivity.getString(NetworkMonitor.KEY_NET_WIFI_SSID);
                String unknownWifi = getResources().getString(R.string.net_name_wifi);
                switch (statusId) {
                    case 0:
                        networkName.setText(unknownWifi);
                        this.mNetworkStatusIndicator.setImageResource(R.drawable.qsb_network_icon_eth_disconnected);
                        networkStatus.setText(R.string.net_status_disconnected);
                        break;
                    case 1:
                        networkName.setText(unknownWifi);
                        this.mNetworkStatusIndicator.setImageResource(R.drawable.qsb_network_icon_eth_unreachable);
                        networkStatus.setText(R.string.net_status_unreachable);
                        break;
                    case 2:
                        networkName.setText(wifiSsid);
                        this.mNetworkStatusIndicator.setImageResource(R.drawable.qsb_network_icon_eth_connected);
                        networkStatus.setText(R.string.net_status_connected);
                        break;
                }
                int iNetCondiction = newConnectivity.getInt(NetworkMonitor.KEY_NET_WIFI_CONDICTION);
                this.mNetworkStatusIndicator.setImageResource(WIFI_SIGNAL_STRENGTH[iNetCondiction][newConnectivity.getInt(NetworkMonitor.KEY_NET_WIFI_LEVEL)]);
                break;
        }
        this.mNetWorkWindowContent.requestLayout();
        this.mNetWorkWindowContent.invalidate();
    }

    public void onUpdateWifiActivity(int wifiActivity) {
    }

    public void onClickNetworkStatusIndicator(View v) {
        initNetWorkWindow();
        this.mNetWorkWindow.show(this.mNetworkStatusIndicator);
    }

    public void onClickNetworkSettings() {
        Intent mSysIntent = new Intent();
        mSysIntent.setAction("com.konka.systemsetting.action.MainActivity");
        mSysIntent.setClassName("com.konka.systemsetting", "com.konka.systemsetting.MainActivity");
        mSysIntent.addFlags(536870912);
        mSysIntent.putExtra("first", 1);
        mSysIntent.putExtra("second", 0);
        mSysIntent.putExtra("third", -1);
        this.mLauncher.startActivity(mSysIntent);
        this.mNetWorkWindow.dismiss();
    }

    public void onUpdateUsbConnectivity(Bundle newConnectivity) {
        boolean z = newConnectivity.getBoolean(UsbMonitor.KEY_USB_IS_CONNECTED);
        this.mSdcardCount = newConnectivity.getInt(UsbMonitor.KEY_USB_SDCARD);
        this.mUdiskCount = newConnectivity.getInt(UsbMonitor.KEY_USB_UDISK);
        this.mStoragePath = newConnectivity.getStringArrayList(UsbMonitor.KEY_USB_PATHLIST);
        int pathListSize1 = 0;
        for (int i = 0; i < this.mStoragePath.size(); i++) {
            if (this.mStoragePath.get(i) != StatConstants.MTA_COOPERATION_TAG) {
                pathListSize1++;
            }
        }
        this.mStoragePath = (ArrayList) getUsbs(this.mLauncher);
        if (this.mStoragePath.size() > 0) {
            this.mUsbStatusIndicator.setImageResource(R.drawable.qsb_usb_icon_connected);
            int i2 = newConnectivity.getInt(UsbMonitor.KEY_USB_DEVICE_COUNT);
            this.mUsbStatusCount.setBackgroundResource(R.drawable.conn_stat_usb_count_bg);
            this.mUsbStatusCount.setText(Integer.toString(this.mStoragePath.size()));
        } else {
            this.mUsbStatusCount.setBackground(null);
            this.mUsbStatusCount.setText(StatConstants.MTA_COOPERATION_TAG);
        }
        updateStorageContentUI();
    }

    private void updateStorageContentUI() {
        if (this.mUsbWindow != null && this.mUsbWindowContent != null && this.mLauncher != null) {
            LinearLayout viewGroup = (LinearLayout) this.mUsbWindowContent.getChildAt(0);
            viewGroup.removeAllViews();
            List<String> curStoragePath = getUsbs(this.mLauncher);
            Log.d(TAG, "updateStorageContentUI: mSdcardCount=" + this.mSdcardCount + ", mUdiskCount=" + this.mUdiskCount + ", curStoragePath.size()=" + curStoragePath.size());
            LayoutInflater inflater = LayoutInflater.from(this.mContext);
            if (curStoragePath.size() == 0) {
                View v = inflater.inflate(R.layout.usb_item, null);
                ((TextView) v.findViewById(R.id.usb_item)).setText(R.string.usb_unconnected_hint);
                v.requestLayout();
                v.invalidate();
                v.setLayoutParams(new LinearLayout.LayoutParams(-1, (int) (getResources().getDisplayMetrics().density * 48.0f)));
                viewGroup.addView(v);
                viewGroup.requestLayout();
                viewGroup.invalidate();
                this.mUsbWindow.update(-2, -2);
                return;
            }
            List<String> mDiskName = changeDiskName(curStoragePath);
            for (int i = 0; i < curStoragePath.size(); i++) {
                Log.v(TAG, " disk name  " + i + " : " + ((String) mDiskName.get(i)));
            }
            List<String> mDiskNameText = sortDiskName(mDiskName);
            for (int i2 = 0; i2 < curStoragePath.size(); i2++) {
                Log.v(TAG, "  disk name on status bar " + i2 + " : " + ((String) mDiskNameText.get(i2)));
            }
            for (int i3 = 0; i3 < curStoragePath.size(); i3++) {
                ImageView separate = new ImageView(this.mLauncher);
                separate.setBackgroundResource(R.drawable.separate_line);
                View v2 = inflater.inflate(R.layout.usb_item, null);
                TextView usbItem = (TextView) v2.findViewById(R.id.usb_item);
                usbItem.setText((CharSequence) mDiskNameText.get(i3));
                usbItem.setTag(Integer.valueOf(i3));
                usbItem.setFocusable(true);
                usbItem.setClickable(true);
                usbItem.setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        SearchDropTargetBar.this.onClickUsbItem(((Integer) v.getTag()).intValue());
                        SearchDropTargetBar.this.mUsbWindow.dismiss();
                    }
                });
                if (i3 > 0) {
                    separate.setLayoutParams(new LinearLayout.LayoutParams(-1, -2));
                    viewGroup.addView(separate);
                    separate.requestLayout();
                    separate.invalidate();
                }
                v2.setLayoutParams(new LinearLayout.LayoutParams(-1, (int) (getResources().getDisplayMetrics().density * 48.0f)));
                viewGroup.addView(v2);
                v2.requestLayout();
                v2.invalidate();
            }
            viewGroup.requestLayout();
            viewGroup.invalidate();
            this.mUsbWindow.update(-2, -2);
        }
    }

    private List<String> changeDiskName(List<String> usbs) {
        List<String> usbList = null;
        if (usbs != null && usbs.size() > 0) {
            usbList = new ArrayList<>();
            for (int i = 0; i < usbs.size(); i++) {
                if (i < this.USB_NAME_LIST.length) {
                    usbList.add(this.USB_NAME_LIST[i]);
                } else {
                    usbList.add((String) usbs.get(i));
                }
            }
        }
        return usbList;
    }

    private List<String> sortDiskName(int sdcard, List<String> list) {
        if (list == null) {
            return null;
        }
        List<String> newList = new ArrayList<>();
        if (sdcard == 0) {
            for (int i = 0; i < list.size(); i++) {
                newList.add(new StringBuilder(String.valueOf(getResources().getString(R.string.status_usb_))).append((String) list.get(i)).append('\\').append(')').toString());
            }
            return newList;
        }
        newList.add(new StringBuilder(String.valueOf(getResources().getString(R.string.status_usb_sdcard))).append((String) list.get(0)).append('\\').append(')').toString());
        for (int i2 = 1; i2 < list.size(); i2++) {
            newList.add(new StringBuilder(String.valueOf(getResources().getString(R.string.status_usb_))).append((String) list.get(i2)).append('\\').append(')').toString());
        }
        return newList;
    }

    private List<String> sortDiskName(List<String> list) {
        if (list == null) {
            return null;
        }
        List<String> newList = new ArrayList<>();
        List<String> tempPathList = getUsbs(this.mLauncher);
        int sd = 0;
        if (tempPathList == null) {
            return null;
        }
        for (int i = 0; i < tempPathList.size(); i++) {
            Log.v(TAG, "===============path : " + ((String) tempPathList.get(i)));
            if (((String) tempPathList.get(i)).contains("/mnt/sd")) {
                sd++;
            }
        }
        if (sd == 0) {
            for (int i2 = 0; i2 < list.size(); i2++) {
                newList.add(new StringBuilder(String.valueOf(getResources().getString(R.string.status_usb_))).append((String) list.get(i2)).append('\\').append(')').toString());
            }
            return newList;
        }
        newList.add(new StringBuilder(String.valueOf(getResources().getString(R.string.status_usb_sdcard))).append((String) list.get(0)).append('\\').append(')').toString());
        for (int i3 = 1; i3 < list.size(); i3++) {
            newList.add(new StringBuilder(String.valueOf(getResources().getString(R.string.status_usb_))).append((String) list.get(i3)).append('\\').append(')').toString());
        }
        return newList;
    }

    private List<String> getUsbs(Context context) {
        KKStorageManager stm = KKStorageManager.getInstance(context);
        String[] volumePath = stm.getVolumePaths();
        if (volumePath == null) {
            return null;
        }
        List<String> arrayList = new ArrayList<>();
        for (int i = 0; i < volumePath.length; i++) {
            String state = stm.getVolumeState(volumePath[i]);
            if (state != null && state.equals("mounted")) {
                arrayList.add(volumePath[i]);
            }
        }
        return arrayList;
    }

    /* access modifiers changed from: private */
    public void onClickUsbItem(int index) {
        Intent intent;
        this.mStoragePath = (ArrayList) getUsbs(this.mLauncher);
        if (this.mStoragePath.size() > index) {
            if (this.mLauncher.isActivityExist("com.konka.mm", "com.konka.mm.filemanager.FileListActivity")) {
                intent = this.mLauncher.newIntentForStartActivity(new ComponentName("com.konka.mm", "com.konka.mm.filemanager.FileDiskActivity"));
            } else {
                intent = new Intent();
                intent.setAction("konka.action.START_MM");
            }
            String path = (String) this.mStoragePath.get(index);
            String curDiskName = (String) changeDiskName(this.mStoragePath).get(index);
            intent.putExtra("startFlag", true);
            intent.putExtra("diskPath", path);
            intent.putExtra("root_path", path);
            intent.putExtra("diskName", curDiskName);
            this.mLauncher.startActivitySafely(((ViewGroup) this.mUsbWindowContent.getChildAt(0)).getChildAt(index), intent, "mm");
            return;
        }
        updateStorageContentUI();
    }

    public void onBatterySupported(boolean isSupported) {
        this.mBatteryStatusIndicator.setVisibility(isSupported ? 0 : 8);
    }

    public void onUpdateBatteryUsage(Bundle newUsage) {
        BatteryMonitor.setImageByUsage(this.mBatteryStatusIndicator, newUsage);
    }

    public void onAppUpgradeChange(int appCount) {
    }

    public void onSystemUpgradeChange(String systemVer) {
        this.mNewSystemVersion = systemVer;
        if (systemVer != null) {
            this.mSystemUpgradeItem.setPrimaryText((CharSequence) this.mLauncher.getString(R.string.system_available, new Object[]{systemVer}));
        }
        setUpgradeButtonVisibility();
    }

    private void setUpgradeButtonVisibility() {
        int visibility;
        boolean appVis;
        boolean sysVis;
        boolean divVis;
        int i;
        int i2;
        int i3 = 0;
        if (this.mAppUpgradeCount > 0 || this.mNewSystemVersion != null) {
            visibility = 0;
        } else {
            visibility = 8;
        }
        if (this.mUpgradeButton.getVisibility() != visibility) {
            if (visibility == 8) {
                this.mUpgradeButton.clearFocus();
            }
            this.mUpgradeButton.setVisibility(visibility);
        }
        if (visibility == 0) {
            if (this.mAppUpgradeCount > 0) {
                appVis = true;
            } else {
                appVis = false;
            }
            if (this.mNewSystemVersion != null) {
                sysVis = true;
            } else {
                sysVis = false;
            }
            if (!appVis || !sysVis) {
                divVis = false;
            } else {
                divVis = true;
            }
            UpgradeItem upgradeItem = this.mAppUpgradeItem;
            if (appVis) {
                i = 0;
            } else {
                i = 8;
            }
            upgradeItem.setVisibility(i);
            UpgradeItem upgradeItem2 = this.mSystemUpgradeItem;
            if (sysVis) {
                i2 = 0;
            } else {
                i2 = 8;
            }
            upgradeItem2.setVisibility(i2);
            ImageView imageView = this.mUpgradeDivider;
            if (!divVis) {
                i3 = 8;
            }
            imageView.setVisibility(i3);
        }
    }

    public SearchDropTargetBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SearchDropTargetBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mDeferOnDragEnd = false;
        this.mCurrentInterfaceId = 0;
        this.mThumbs = new ArrayList<>(24);
        this.mImages = new ArrayList<>(24);
        this.mWallpaperComponents = new ArrayList<>(24);
        this.USB_NAME_LIST = new String[]{"C:", "D:", "E:", "F:", "G:", "H:", "I:", "J:", "K:", "L:", "M:", "N:"};
        this.mSortMode = SortMode.Title;
        this.mHandler = new Handler();
        LayoutInflater inflater = LayoutInflater.from(getContext());
        this.mShowQSBSearchBar = Homescreen.getShowSearchBar(context);
        this.mUpgradeWindow = new ArrowedPopupWindow(context, 3);
        this.mUserCenterWindow = new ArrowedPopupWindow(context, 3);
        this.mUserCenterWindowContent = (ViewGroup) inflater.inflate(R.layout.popup_user_center, null);
        this.mChangeWallpaperWindow = new ArrowedPopupWindow(context, 3);
        this.mAddQuickAction = new QuickAction(context, 0, 3);
        this.mSystemSettingsWindow = new ArrowedPopupWindow(context, 3);
        this.mSystemSettingsWindowContent = (ViewGroup) inflater.inflate(R.layout.popup_system_setting, null);
        this.mUsbWindow = new ArrowedPopupWindow(context, 3);
        this.mUsbWindowContent = (ViewGroup) inflater.inflate(R.layout.popup_usb, null, true);
        this.mNetWorkWindow = new ArrowedPopupWindow(context, 3);
        this.mNetWorkWindowContent = (ViewGroup) inflater.inflate(R.layout.popup_net_settings, null);
        this.mPackageManager = context.getPackageManager();
        this.mApps = new ArrayList<>();
        this.mWidgets = new ArrayList();
        this.mShortcuts = new ArrayList();
        this.mResources = context.getResources();
        this.mTextFocusColor = this.mResources.getColor(R.color.konka_orange);
        this.mTextNormalColor = this.mResources.getColor(17170444);
        this.mCellWidth = this.mResources.getDimensionPixelSize(R.dimen.apps_customize_cell_width);
        this.mCellHeight = this.mResources.getDimensionPixelSize(R.dimen.apps_customize_cell_height);
        this.mCurrentInterfaceText = this.mResources.getString(R.string.hint_network_wired);
        this.mWidgetBlackList = this.mResources.getStringArray(R.array.widget_blacklist);
    }

    public void setup(Launcher launcher, DragController dragController) {
        dragController.addDragListener(this);
        if (this.mInfoDropTarget != null) {
            dragController.addDragListener(this.mInfoDropTarget);
            dragController.addDropTarget(this.mInfoDropTarget);
            this.mInfoDropTarget.setLauncher(launcher);
        }
        dragController.addDragListener(this.mDeleteDropTarget);
        dragController.addDropTarget(this.mDeleteDropTarget);
        this.mDeleteDropTarget.setLauncher(launcher);
        this.mLauncher = launcher;
    }

    private void initAddQuickAction() {
        if (!this.mInitAddQuickAction) {
            Resources r = getResources();
            ActionItem appsItem = new ActionItem(0, r.getString(R.string.group_applications), getResources().getDrawable(R.drawable.ic_add_apps));
            ActionItem widgetItem = new ActionItem(1, r.getString(R.string.group_widgets), getResources().getDrawable(R.drawable.ic_add_widget));
            ActionItem folderItem = new ActionItem(2, r.getString(R.string.folder), getResources().getDrawable(R.drawable.ic_add_folder));
            ActionItem shortcutItem = new ActionItem(3, r.getString(R.string.group_shortcuts), getResources().getDrawable(R.drawable.ic_add_shortcut));
            this.mAddQuickAction.addActionItem(appsItem, R.drawable.qsb_add_quickaction_selector, 17170444);
            this.mAddQuickAction.addActionItem(widgetItem, R.drawable.qsb_add_quickaction_selector, 17170444);
            this.mAddQuickAction.addActionItem(folderItem, R.drawable.qsb_add_quickaction_selector, 17170444);
            this.mAddQuickAction.addActionItem(shortcutItem, R.drawable.qsb_add_quickaction_selector, 17170444);
            this.mAddQuickAction.setOnActionItemClickListener(this);
            this.mInitAddQuickAction = true;
        }
    }

    private void initUserCenterWindow() {
        if (!this.mIsUserCenterWindowInited) {
            this.mUserCenterWindow.setContentView((View) this.mUserCenterWindowContent);
            this.mIsUserCenterWindowInited = true;
        }
    }

    private void initSystemSettingsWindow() {
        if (!this.mIsSystemSettingsWindwoInited) {
            this.mSystemSettingsWindow.setContentView((View) this.mSystemSettingsWindowContent);
            this.mIsSystemSettingsWindwoInited = true;
        }
    }

    private void initUsbWindow() {
        if (!this.mIsUsbWindowInited) {
            this.mUsbWindow.setContentView((View) this.mUsbWindowContent);
            this.mIsUsbWindowInited = true;
        }
    }

    private void initNetWorkWindow() {
        if (!this.mIsNetWorkWindowInited) {
            this.mNetWorkWindow.setContentView((View) this.mNetWorkWindowContent);
            this.mNetWorkWindow.setContainerAlignParentRight();
            this.mIsNetWorkWindowInited = true;
        }
    }

    private void initChangeWallpaperWindow() {
        if (!this.mInitChangeWallpaperWindow) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            GridLayout wpGridLayout = (GridLayout) inflater.inflate(R.layout.wallpaper_chooser_popup, null);
            WallpaperChooserDialogFragment.findWallpapers(getContext(), this.mThumbs, this.mImages, this.mWallpaperComponents);
            OnClickListener wpOnClickListener = new OnClickListener() {
                public void onClick(View v) {
                    int position = ((Integer) v.getTag()).intValue();
                    String component = (String) SearchDropTargetBar.this.mWallpaperComponents.get(position);
                    if (component == null || component.equals("null")) {
                        WallpaperChooserDialogFragment.setWallpaper(SearchDropTargetBar.this.getContext(), position, SearchDropTargetBar.this.mImages);
                    } else {
                        WallpaperChooserDialogFragment.setLiveWallpaper(SearchDropTargetBar.this.getContext(), component);
                    }
                    SearchDropTargetBar.this.mChangeWallpaperWindow.dismiss();
                }
            };
            int size = this.mThumbs.size();
            for (int i = 0; i < size; i++) {
                final View view = inflater.inflate(R.layout.wallpaper_item, null);
                ImageView image = (ImageView) view.findViewById(R.id.wallpaper_image);
                TextView text = (TextView) view.findViewById(R.id.wallpaper_tag_live);
                if (this.mWallpaperComponents.get(i) == null || ((String) this.mWallpaperComponents.get(i)).equals("null")) {
                    text.setVisibility(8);
                }
                WallpaperChooserDialogFragment.setPreviewView(image, i, this.mThumbs);
                view.setFocusable(true);
                if (i != size - 1) {
                    view.setTag(Integer.valueOf(i));
                    view.setOnClickListener(wpOnClickListener);
                } else {
                    view.setOnClickListener(new OnClickListener() {
                        public void onClick(View v) {
                            SearchDropTargetBar.this.mChangeWallpaperWindow.dismiss();
                            SearchDropTargetBar.this.mLauncher.startActivitySafely(view, SearchDropTargetBar.this.mLauncher.newIntentForStartActivity(new ComponentName("com.konka.mm", "com.konka.mm.photo.PhotoDiskActivity")), "Photo-SetWallpaper");
                        }
                    });
                }
                wpGridLayout.addView(view);
            }
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.height = -2;
            params.width = -2;
            params.setGravity(1);
            wpGridLayout.setLayoutParams(params);
            this.mChangeWallpaperWindow.setContentView((View) wpGridLayout);
            this.mInitChangeWallpaperWindow = true;
        }
    }

    private void initUpgradeWindow() {
        if (!this.mInitUpgradeWindow) {
            View upgradeView = LayoutInflater.from(getContext()).inflate(R.layout.qsb_upgrade, null);
            upgradeView.setLayoutParams(new ViewGroup.LayoutParams(-2, -2));
            this.mUpgradeDivider = (ImageView) upgradeView.findViewById(R.id.divider);
            this.mAppUpgradeItem = (UpgradeItem) upgradeView.findViewById(R.id.appUpgradeItem);
            this.mAppUpgradeItem.setIcon(R.string.app_upgrade, R.drawable.qsb_upgrade_app).setSecondaryText((int) R.string.upgrade_hint).setButton(R.string.upgrade_detail, new OnClickListener() {
                public void onClick(View v) {
                    SearchDropTargetBar.this.mLauncher.startActivitySafely(v, SearchDropTargetBar.this.mLauncher.newIntentForStartActivity(new ComponentName("com.konka.appupgrade", "com.konka.appupgrade.HomeActivity")), "appUpgrader");
                    SearchDropTargetBar.this.mUpgradeWindow.dismiss();
                }
            });
            this.mSystemUpgradeItem = (UpgradeItem) upgradeView.findViewById(R.id.systemUpgradeItem);
            this.mSystemUpgradeItem.setIcon(R.string.system_upgrade, R.drawable.qsb_upgrade_system).setSecondaryText((int) R.string.upgrade_hint).setButton(R.string.upgrade_detail, new OnClickListener() {
                public void onClick(View v) {
                    SearchDropTargetBar.this.mLauncher.startActivitySafely(v, SearchDropTargetBar.this.mLauncher.newIntentForStartActivity(new ComponentName("com.konka.upgrade", "com.konka.upgrade.UpgradeActivity")), "sysUpgrader");
                    SearchDropTargetBar.this.mUpgradeWindow.dismiss();
                }
            });
            this.mUpgradeWindow.setContentView(upgradeView);
            this.mInitUpgradeWindow = true;
        }
    }

    public void onClickHomescreenAddButton() {
        if (!this.mLauncher.getWorkspace().isAllowAddItem(this.mLauncher.getWorkspace().getCurrentDropLayout())) {
            this.mLauncher.showNotAllowedAddMessage();
            return;
        }
        initAddQuickAction();
        this.mAddQuickAction.show(this.mHomescreenAddButton);
    }

    public void onClickUserCenterButton() {
        if (!this.mIsLoggedIn) {
            this.mLauncher.startActivitySafely(this.mUserCenterButton, this.mLauncher.newIntentForStartActivity(new ComponentName("com.konka.passport", "com.konka.passport.LoginCheckActivity")), "UserCenter");
            return;
        }
        initUserCenterWindow();
        this.mUserCenterWindow.show(this.mUserCenterButton);
    }

    public void onClickMessageCenterButton() {
        this.mLauncher.startActivitySafely(this.mMessageCenterButton, this.mLauncher.newIntentForStartActivity(new ComponentName(MessageManager.MESSAGE_PACKAGE_NAME, "com.konka.message.MessageCenterActivity")), "MessageCenter");
    }

    public void dismissUserCenterWindow() {
        if (this.mUserCenterWindow != null) {
            this.mUserCenterWindow.dismiss();
        }
    }

    public void onClickSystemSettingsButton() {
        initSystemSettingsWindow();
        this.mSystemSettingsWindow.show(this.mSettingsButton);
    }

    public void onClickUsbButton() {
        initUsbWindow();
        this.mUsbWindow.show(this.mUsbStatusIndicator);
    }

    public void onClickThemeChangeButton() {
        this.mLauncher.startActivitySafely(this.mThemeChangeButton, this.mLauncher.newIntentForStartActivity(new ComponentName("com.konka.thememanager", "com.konka.thememanager.main.ThememanagerActivity")), "Thememanager");
    }

    public void onClickWallpaperChangeButton() {
        initChangeWallpaperWindow();
        this.mChangeWallpaperWindow.show(this.mWallpaperChangeButton);
    }

    public void onClickUpgradeButton() {
        initUpgradeWindow();
        this.mUpgradeWindow.show(this.mUpgradeButton);
    }

    public void onClick(View v) {
        if (v == this.mHomescreenAddButton) {
            onClickHomescreenAddButton();
        } else if (v == this.mWallpaperChangeButton) {
            onClickWallpaperChangeButton();
        } else if (v == this.mUpgradeButton) {
            onClickUpgradeButton();
        } else if (v == this.mThemeChangeButton) {
            onClickThemeChangeButton();
        }
    }

    private void showHint(View v) {
        DragLayer dragLayer = (DragLayer) getParent();
        if (this.mCurrentInterfaceId == 0) {
            this.mCurrentInterfaceText = this.mResources.getString(R.string.hint_network_wired);
        } else {
            this.mCurrentInterfaceText = this.mResources.getString(R.string.hint_network_wireless);
        }
        this.mNetworkStatusIndicator.setTag(R.id.hint_text, this.mCurrentInterfaceText);
        dragLayer.showDelayedHint(v, (String) v.getTag(R.id.hint_text), 1000, 1000);
    }

    private void hideHint(View v) {
        ((DragLayer) getParent()).hideHint(v);
    }

    public boolean onHover(View v, MotionEvent event) {
        if (!this.mLauncher.isWorkspaceVisible()) {
            return false;
        }
        switch (event.getAction()) {
            case 9:
                v.onHoverEvent(event);
                showHint(v);
                return true;
            case 10:
                v.onHoverEvent(event);
                hideHint(v);
                return true;
            default:
                return false;
        }
    }

    public void onFocusChange(View v, boolean hasFocus) {
        Folder openFolder = this.mLauncher.getWorkspace().getOpenFolder();
        if (openFolder != null) {
            if (openFolder.isEditingName()) {
                openFolder.dismissEditingName();
            } else {
                openFolder.setFocusOnFirstChild();
            }
        } else if (!hasFocus) {
            hideHint(v);
        } else {
            showHint(v);
        }
    }

    private PagedDialog getAppsDialog() {
        if (this.mAppsDialog == null) {
            Builder builder = new Builder(getContext());
            Resources r = getResources();
            String title = r.getString(R.string.menu_item_add_item);
            String positiveText = r.getString(R.string.dialog_positive);
            String negativeText = r.getString(R.string.dialog_dismiss);
            final AppsAdapter adapter = new AppsAdapter(getContext());
            builder.setTitle((CharSequence) title).setCancelable(true).setContentLayout((this.mCellWidth * 7) + (r.getDimensionPixelSize(R.dimen.paged_dialog_apps_hspacing) * 6), (this.mCellHeight * 3) + (r.getDimensionPixelSize(R.dimen.paged_dialog_apps_vspacing) * 2)).setAdapter(adapter).setNegativeButton((CharSequence) negativeText, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).setPositiveButton((CharSequence) positiveText, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    HashSet<ApplicationInfo> mSelectedApps = adapter.getSelectedApps();
                    CellLayout currentCellLayout = SearchDropTargetBar.this.mLauncher.getWorkspace().getCurrentDropLayout();
                    Iterator it = mSelectedApps.iterator();
                    while (it.hasNext()) {
                        SearchDropTargetBar.this.mLauncher.addExternalItemToScreen((ApplicationInfo) it.next(), currentCellLayout);
                    }
                }
            });
            this.mAppsDialog = builder.create();
            adapter.registerDataSetObserver(new DataSetObserver() {
                private void updateShowProgressBar() {
                    SearchDropTargetBar.this.mAppsDialog.setShowProgress(adapter.getCount() <= 0);
                }

                public void onChanged() {
                    updateShowProgressBar();
                }

                public void onInvalidated() {
                    updateShowProgressBar();
                }
            });
            this.mAppsDialog.setOnShowListener(new OnShowListener() {
                public void onShow(DialogInterface dialog) {
                    if (adapter.getCount() <= 0) {
                        SearchDropTargetBar.this.mAppsDialog.setShowProgress(true);
                    }
                }
            });
            this.mAppsDialog.setOnDismissListener(adapter);
        }
        return this.mAppsDialog;
    }

    private PagedDialog getShortcutsDialog() {
        if (!this.mInitPackages) {
            onPackagesUpdated();
            this.mInitPackages = true;
        }
        if (this.mShortcutsDialog == null) {
            int cellWidth = this.mCellWidth / 2;
            int cellHeight = this.mCellHeight / 2;
            int widgetWidth = cellWidth * 3;
            int widgetHeight = cellHeight * 3;
            Builder builder = new Builder(getContext());
            Resources r = getResources();
            String title = r.getString(R.string.group_shortcuts);
            final ShortcutsAdapter adapter = new ShortcutsAdapter(getContext(), this.mLauncher, cellWidth, cellHeight, 3, 3);
            builder.setTitle((CharSequence) title).setCancelable(true).setContentLayout((widgetWidth * 4) + (r.getDimensionPixelSize(R.dimen.paged_dialog_widgetshortcut_hspacing) * 3), (widgetHeight * 2) + (r.getDimensionPixelSize(R.dimen.paged_dialog_widgetshortcut_vspacing) * 1));
            builder.setAdapter(adapter);
            this.mShortcutsDialog = builder.create();
            this.mShortcutsDialog.setShowProgress(false);
            this.mShortcutsDialog.setOnShowListener(new OnShowListener() {
                public void onShow(DialogInterface dialog) {
                    adapter.notifyDataSetChanged();
                }
            });
            this.mShortcutsDialog.setOnDismissListener(new OnDismissListener() {
                public void onDismiss(DialogInterface dialog) {
                    System.gc();
                }
            });
        }
        return this.mShortcutsDialog;
    }

    private PagedDialog getWidgetsDialog() {
        if (!this.mInitPackages) {
            onPackagesUpdated();
            this.mInitPackages = true;
        }
        if (this.mWidgetsDialog == null) {
            int cellWidth = this.mCellWidth / 2;
            int cellHeight = this.mCellHeight / 2;
            int widgetWidth = cellWidth * 3;
            int widgetHeight = cellHeight * 3;
            Builder builder = new Builder(getContext());
            Resources r = getResources();
            String title = r.getString(R.string.group_widgets);
            final WidgetsAdapter adapter = new WidgetsAdapter(getContext(), this.mLauncher, cellWidth, cellHeight, 3, 3);
            builder.setTitle((CharSequence) title).setCancelable(true).setContentLayout((widgetWidth * 4) + (r.getDimensionPixelSize(R.dimen.paged_dialog_widgetshortcut_hspacing) * 3), (widgetHeight * 2) + (r.getDimensionPixelSize(R.dimen.paged_dialog_widgetshortcut_vspacing) * 1));
            builder.setAdapter(adapter);
            this.mWidgetsDialog = builder.create();
            this.mWidgetsDialog.setShowProgress(false);
            this.mWidgetsDialog.setOnShowListener(new OnShowListener() {
                public void onShow(DialogInterface dialog) {
                    adapter.notifyDataSetChanged();
                }
            });
            this.mWidgetsDialog.setOnDismissListener(new OnDismissListener() {
                public void onDismiss(DialogInterface dialog) {
                    System.gc();
                }
            });
        }
        return this.mWidgetsDialog;
    }

    private boolean addFolderInCurrentScreen() {
        Workspace ws = this.mLauncher.getWorkspace();
        CellLayout currentCellLayout = ws.getCurrentDropLayout();
        int[] emptyCells = new int[2];
        boolean foundCell = currentCellLayout.findCellForSpan(emptyCells, 1, 1);
        if (foundCell) {
            this.mLauncher.addFolder(currentCellLayout, -100, ws.indexOfChild(currentCellLayout), emptyCells[0], emptyCells[1]);
        } else {
            this.mLauncher.showOutOfSpaceMessage();
        }
        return foundCell;
    }

    public void onItemClick(QuickAction source, int pos, int actionId) {
        if (source == this.mAddQuickAction) {
            switch (actionId) {
                case 0:
                    getAppsDialog().show();
                    return;
                case 1:
                    getWidgetsDialog().show();
                    return;
                case 2:
                    addFolderInCurrentScreen();
                    return;
                case 3:
                    getShortcutsDialog().show();
                    return;
                default:
                    return;
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onFinishInflate() {
        super.onFinishInflate();
        this.mQSBStaticBar = findViewById(R.id.qsb_static_bar);
        this.mQSBSearchBar = findViewById(R.id.qsb_search_bar);
        this.mDropTargetBar = findViewById(R.id.drag_target_bar);
        this.mTimeView = (TextView) findViewById(R.id.time);
        this.mUpgradeButton = (ImageView) findViewById(R.id.upgrade_button);
        if (this.mUpgradeButton != null) {
            this.mUpgradeButton.setTag(R.id.hint_text, this.mResources.getString(R.string.hint_upgrade));
            this.mUpgradeButton.setOnClickListener(this);
        }
        initUpgradeWindow();
        this.mUserCenterButton = (ImageView) findViewById(R.id.user_center_button);
        if (this.mUserCenterButton != null) {
            this.mUserCenterButton.setTag(R.id.hint_text, this.mResources.getString(R.string.hint_user_center));
        }
        this.mMessageCenterLayout = (ViewGroup) findViewById(R.id.message_center_layout);
        this.mMessageCenterButton = (ImageView) this.mMessageCenterLayout.findViewById(R.id.message_center_button);
        this.mMessageCenterCount = (TextView) this.mMessageCenterLayout.findViewById(R.id.message_center_count);
        if (this.mMessageCenterButton != null) {
        }
        this.mHomescreenAddButton = (ImageView) findViewById(R.id.homescreen_add_button);
        if (this.mHomescreenAddButton != null) {
            this.mHomescreenAddButton.setTag(R.id.hint_text, this.mResources.getString(R.string.hint_homescreen_add));
            this.mHomescreenAddButton.setOnClickListener(this);
        }
        this.mAllAppsButton = (ImageView) findViewById(R.id.all_apps_button);
        if (this.mAllAppsButton != null) {
            this.mAllAppsButton.setTag(R.id.hint_text, this.mResources.getString(R.string.hint_all_apps));
        }
        this.mThemeChangeButton = (ImageView) findViewById(R.id.theme_change_button);
        if (this.mThemeChangeButton != null) {
            this.mThemeChangeButton.setTag(R.id.hint_text, this.mResources.getString(R.string.hint_change_theme));
            this.mThemeChangeButton.setOnClickListener(this);
        }
        this.mWallpaperChangeButton = (ImageView) findViewById(R.id.wallpaper_change_button);
        if (this.mWallpaperChangeButton != null) {
            this.mWallpaperChangeButton.setTag(R.id.hint_text, this.mResources.getString(R.string.hint_change_wallpaper));
            this.mWallpaperChangeButton.setOnClickListener(this);
        }
        this.mKKSearchButton = (ImageView) findViewById(R.id.kk_search_button);
        if (this.mKKSearchButton != null) {
            this.mKKSearchButton.setTag(R.id.hint_text, this.mResources.getString(R.string.hint_kk_search));
        }
        this.mSettingsButton = (ImageView) findViewById(R.id.settings_button);
        if (this.mSettingsButton != null) {
            this.mSettingsButton.setTag(R.id.hint_text, this.mResources.getString(R.string.hint_settings));
        }
        this.mUsbStatusIndicator = (ImageView) findViewById(R.id.usb_status_indicator);
        this.mUsbStatusCount = (TextView) findViewById(R.id.usb_status_count);
        if (this.mSettingsButton != null) {
            this.mUsbStatusIndicator.setTag(R.id.hint_text, this.mResources.getString(R.string.hint_usb_indicator));
        }
        this.mNetworkStatusIndicator = (ImageView) findViewById(R.id.network_status_indicator);
        this.mLockScreenButton = (ImageView) findViewById(R.id.kk_user_lock);
        if (this.mLockScreenButton != null) {
            this.mLockScreenButton.setTag(R.id.hint_text, this.mResources.getString(R.string.mutiusers_logout_text));
        }
        this.mBluetoothButton = (ImageView) findViewById(R.id.bluetooth_button);
        if (this.mBluetoothButton != null) {
            this.mBluetoothButton.setTag(R.id.hint_text, this.mResources.getString(R.string.hint_bluetooth));
            updateBluetoothState();
        }
        this.mBatteryStatusIndicator = (ImageView) findViewById(R.id.battery_status_indicator);
        if (this.mBatteryStatusIndicator != null) {
            this.mBatteryStatusIndicator.setTag(R.id.hint_text, this.mResources.getString(R.string.hint_battery_indicator));
        }
        this.mInfoDropTarget = (ButtonDropTarget) this.mDropTargetBar.findViewById(R.id.info_target_text);
        this.mDeleteDropTarget = (ButtonDropTarget) this.mDropTargetBar.findViewById(R.id.delete_target_text);
        this.mBarHeight = getResources().getDimensionPixelSize(R.dimen.qsb_bar_height);
        if (this.mInfoDropTarget != null) {
            this.mInfoDropTarget.setSearchDropTargetBar(this);
        }
        this.mDeleteDropTarget.setSearchDropTargetBar(this);
        boolean enableDropDownDropTargets = getResources().getBoolean(R.bool.config_useDropTargetDownTransition);
        if (!this.mShowQSBSearchBar) {
            this.mQSBSearchBar.setVisibility(8);
        }
        this.mDropTargetBar.setAlpha(FlyingIcon.ANGULAR_VMIN);
        ObjectAnimator fadeInAlphaAnim = ObjectAnimator.ofFloat(this.mDropTargetBar, "alpha", new float[]{1.0f});
        fadeInAlphaAnim.setInterpolator(new DecelerateInterpolator());
        this.mDropTargetBarFadeInAnim = new AnimatorSet();
        AnimatorSet.Builder fadeInAnimators = this.mDropTargetBarFadeInAnim.play(fadeInAlphaAnim);
        ObjectAnimator staticBarFadeOutAlphaAnim = ObjectAnimator.ofFloat(this.mQSBStaticBar, "alpha", new float[]{0.0f});
        staticBarFadeOutAlphaAnim.setInterpolator(new AccelerateInterpolator());
        fadeInAnimators.with(staticBarFadeOutAlphaAnim);
        if (enableDropDownDropTargets) {
            this.mDropTargetBar.setTranslationY((float) (-this.mBarHeight));
            fadeInAnimators.with(ObjectAnimator.ofFloat(this.mDropTargetBar, "translationY", new float[]{0.0f}));
            fadeInAnimators.with(ObjectAnimator.ofFloat(this.mQSBStaticBar, "translationY", new float[]{(float) (-this.mBarHeight)}));
        }
        this.mDropTargetBarFadeInAnim.setDuration(200);
        this.mDropTargetBarFadeInAnim.addListener(new AnimatorListenerAdapter() {
            public void onAnimationStart(Animator animation) {
                SearchDropTargetBar.this.mDropTargetBar.setVisibility(0);
            }

            public void onAnimationEnd(Animator animation) {
                SearchDropTargetBar.this.mQSBStaticBar.setVisibility(4);
                SearchDropTargetBar.this.mQSBStaticBar.setLayerType(0, null);
            }
        });
        ObjectAnimator fadeOutAlphaAnim = ObjectAnimator.ofFloat(this.mDropTargetBar, "alpha", new float[]{0.0f});
        fadeOutAlphaAnim.setInterpolator(new AccelerateInterpolator());
        this.mDropTargetBarFadeOutAnim = new AnimatorSet();
        AnimatorSet.Builder fadeOutAnimators = this.mDropTargetBarFadeOutAnim.play(fadeOutAlphaAnim);
        ObjectAnimator staticBarFadeInAlphaAnim = ObjectAnimator.ofFloat(this.mQSBStaticBar, "alpha", new float[]{1.0f});
        staticBarFadeInAlphaAnim.setInterpolator(new DecelerateInterpolator());
        fadeOutAnimators.with(staticBarFadeInAlphaAnim);
        if (enableDropDownDropTargets) {
            fadeOutAnimators.with(ObjectAnimator.ofFloat(this.mDropTargetBar, "translationY", new float[]{(float) (-this.mBarHeight)}));
            fadeOutAnimators.with(ObjectAnimator.ofFloat(this.mQSBStaticBar, "translationY", new float[]{0.0f}));
        }
        this.mDropTargetBarFadeOutAnim.setDuration(175);
        this.mDropTargetBarFadeOutAnim.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animation) {
                SearchDropTargetBar.this.mDropTargetBar.setVisibility(4);
                SearchDropTargetBar.this.mDropTargetBar.setLayerType(0, null);
            }

            public void onAnimationStart(Animator animation) {
                SearchDropTargetBar.this.mQSBStaticBar.setVisibility(0);
            }
        });
        this.mQSBSearchBarFadeInAnim = ObjectAnimator.ofFloat(this.mQSBSearchBar, "alpha", new float[]{1.0f});
        this.mQSBSearchBarFadeInAnim.setDuration(200);
        this.mQSBSearchBarFadeInAnim.addListener(new AnimatorListenerAdapter() {
            public void onAnimationStart(Animator animation) {
                SearchDropTargetBar.this.mQSBSearchBar.setVisibility(0);
            }
        });
        this.mQSBSearchBarFadeOutAnim = ObjectAnimator.ofFloat(this.mQSBSearchBar, "alpha", new float[]{0.0f});
        this.mQSBSearchBarFadeOutAnim.setDuration(175);
        this.mQSBSearchBarFadeOutAnim.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animation) {
                SearchDropTargetBar.this.mQSBSearchBar.setVisibility(4);
            }
        });
    }

    private void cancelAnimations() {
        this.mDropTargetBarFadeInAnim.cancel();
        this.mDropTargetBarFadeOutAnim.cancel();
        this.mQSBSearchBarFadeInAnim.cancel();
        this.mQSBSearchBarFadeOutAnim.cancel();
    }

    public void showSearchBar(boolean animated) {
        cancelAnimations();
        if (animated) {
            if (this.mShowQSBSearchBar) {
                this.mQSBSearchBarFadeInAnim.start();
            }
        } else if (this.mShowQSBSearchBar) {
            this.mQSBSearchBar.setVisibility(0);
            this.mQSBSearchBar.setAlpha(1.0f);
        }
        this.mIsSearchBarHidden = false;
    }

    public void hideSearchBar(boolean animated) {
        cancelAnimations();
        if (animated) {
            if (this.mShowQSBSearchBar) {
                this.mQSBSearchBarFadeOutAnim.start();
            }
        } else if (this.mShowQSBSearchBar) {
            this.mQSBSearchBar.setVisibility(4);
            this.mQSBSearchBar.setAlpha(FlyingIcon.ANGULAR_VMIN);
        }
        this.mIsSearchBarHidden = true;
    }

    public int getTransitionInDuration() {
        return sTransitionInDuration;
    }

    public int getTransitionOutDuration() {
        return sTransitionOutDuration;
    }

    public void onDragStart(DragSource source, Object info, int dragAction) {
        if (((View) source).isInTouchMode()) {
            this.mDropTargetBar.setLayerType(2, null);
            this.mDropTargetBar.buildLayer();
            this.mDropTargetBarFadeOutAnim.cancel();
            this.mDropTargetBarFadeInAnim.start();
        }
        if (!this.mIsSearchBarHidden && this.mShowQSBSearchBar) {
            this.mQSBSearchBarFadeInAnim.cancel();
            this.mQSBSearchBarFadeOutAnim.start();
        }
    }

    public void deferOnDragEnd() {
        this.mDeferOnDragEnd = true;
    }

    public void onDragEnd() {
        if (!this.mDeferOnDragEnd) {
            this.mQSBStaticBar.setLayerType(2, null);
            this.mQSBStaticBar.buildLayer();
            this.mDropTargetBarFadeInAnim.cancel();
            this.mDropTargetBarFadeOutAnim.start();
            if (!this.mIsSearchBarHidden && this.mShowQSBSearchBar) {
                this.mQSBSearchBarFadeOutAnim.cancel();
                this.mQSBSearchBarFadeInAnim.start();
                return;
            }
            return;
        }
        this.mDeferOnDragEnd = false;
    }

    public SortMode getSortMode() {
        return this.mSortMode;
    }

    public void setSortMode(SortMode sortMode) {
        if (this.mSortMode != sortMode) {
            this.mSortMode = sortMode;
            if (this.mSortMode == SortMode.Title) {
                Collections.sort(this.mApps, LauncherModel.APP_NAME_COMPARATOR);
            } else if (this.mSortMode == SortMode.InstallDate) {
                Collections.sort(this.mApps, LauncherModel.APP_INSTALL_TIME_COMPARATOR);
            }
            if (this.mAppsDialog != null && this.mAppsDialog.isShowing()) {
                this.mAppsDialog.getAdapter().notifyDataSetChanged();
            }
        }
    }

    public void setApps(ArrayList<ApplicationInfo> list) {
        this.mApps = list;
        if (this.mSortMode == SortMode.Title) {
            Collections.sort(this.mApps, LauncherModel.APP_NAME_COMPARATOR);
        } else if (this.mSortMode == SortMode.InstallDate) {
            Collections.sort(this.mApps, LauncherModel.APP_INSTALL_TIME_COMPARATOR);
        }
        if (this.mAppsDialog != null && this.mAppsDialog.isShowing()) {
            this.mAppsDialog.getAdapter().notifyDataSetChanged();
        }
    }

    private void addAppsWithoutInvalidate(ArrayList<ApplicationInfo> list) {
        Iterator it = list.iterator();
        while (it.hasNext()) {
            ApplicationInfo info = (ApplicationInfo) it.next();
            int index = 0;
            if (this.mSortMode == SortMode.Title) {
                index = Collections.binarySearch(this.mApps, info, LauncherModel.APP_NAME_COMPARATOR);
            } else if (this.mSortMode == SortMode.InstallDate) {
                index = Collections.binarySearch(this.mApps, info, LauncherModel.APP_INSTALL_TIME_COMPARATOR);
            }
            if (index < 0) {
                this.mApps.add(-(index + 1), info);
            }
        }
    }

    public void addApps(ArrayList<ApplicationInfo> list) {
        addAppsWithoutInvalidate(list);
        if (this.mAppsDialog != null && this.mAppsDialog.isShowing()) {
            this.mAppsDialog.getAdapter().notifyDataSetChanged();
        }
    }

    private int findAppByComponent(List<ApplicationInfo> list, ApplicationInfo item) {
        ComponentName removeComponent = item.intent.getComponent();
        int length = list.size();
        for (int i = 0; i < length; i++) {
            if (((ApplicationInfo) list.get(i)).intent.getComponent().equals(removeComponent)) {
                return i;
            }
        }
        return -1;
    }

    private void removeAppsWithoutInvalidate(ArrayList<ApplicationInfo> list) {
        Iterator it = list.iterator();
        while (it.hasNext()) {
            int removeIndex = findAppByComponent(this.mApps, (ApplicationInfo) it.next());
            if (removeIndex > -1) {
                this.mApps.remove(removeIndex);
            }
        }
    }

    public void removeApps(ArrayList<ApplicationInfo> list) {
        removeAppsWithoutInvalidate(list);
        if (this.mAppsDialog != null && this.mAppsDialog.isShowing()) {
            this.mAppsDialog.getAdapter().notifyDataSetChanged();
        }
    }

    public void updateApps(ArrayList<ApplicationInfo> list) {
        removeAppsWithoutInvalidate(list);
        addAppsWithoutInvalidate(list);
        if (this.mAppsDialog != null && this.mAppsDialog.isShowing()) {
            this.mAppsDialog.getAdapter().notifyDataSetChanged();
        }
    }

    public void onPackagesUpdated() {
        postDelayed(new Runnable() {
            public void run() {
                SearchDropTargetBar.this.updatePackages();
            }
        }, 500);
    }

    private static boolean unflattenFromString(String str, String[] component) {
        int sep = str.indexOf(47);
        if (sep < 0 || sep + 1 >= str.length() || component.length < 2) {
            return false;
        }
        component[0] = str.substring(0, sep);
        component[1] = str.substring(sep + 1);
        if (component[1].length() <= 0 || component[1].charAt(0) != '.') {
            return true;
        }
        component[1] = component[0] + component[1];
        return true;
    }

    private static void filterWidget(List<AppWidgetProviderInfo> widgets, ArrayList<ComponentName> blackList) {
        if (widgets != null && blackList != null && blackList.size() > 0) {
            Collections.sort(blackList);
            int size = widgets.size();
            int j = 0;
            while (j < size) {
                if (Collections.binarySearch(blackList, ((AppWidgetProviderInfo) widgets.get(j)).provider) >= 0) {
                    widgets.remove(j);
                    j--;
                    size--;
                }
                j++;
            }
        }
    }

    private static void filterShortcut(List<ResolveInfo> shortcuts, ArrayList<ComponentName> blackList) {
        if (shortcuts != null && blackList != null && blackList.size() > 0) {
            Collections.sort(blackList);
            int size = shortcuts.size();
            int j = 0;
            while (j < size) {
                ResolveInfo info = (ResolveInfo) shortcuts.get(j);
                if (Collections.binarySearch(blackList, new ComponentName(info.activityInfo.packageName, info.activityInfo.name)) >= 0) {
                    shortcuts.remove(j);
                    j--;
                    size--;
                }
                j++;
            }
        }
    }

    public void updatePackages() {
        this.mWidgets.clear();
        this.mWidgets = AppWidgetManager.getInstance(this.mLauncher).getInstalledProviders();
        ArrayList<ComponentName> widgetBlackList = new ArrayList<>();
        for (String unflattenFromString : this.mWidgetBlackList) {
            String[] blComponent = new String[2];
            if (unflattenFromString(unflattenFromString, blComponent)) {
                widgetBlackList.add(new ComponentName(blComponent[0], blComponent[1]));
            }
        }
        filterWidget(this.mWidgets, widgetBlackList);
        BlackListFilter filter = BlackListFilter.getInstance(this.mLauncher.getApplication());
        filterWidget(this.mWidgets, filter.getWidgetsBlackList());
        Collections.sort(this.mWidgets, new WidgetAndShortcutNameComparator(this.mPackageManager));
        Log.d(TAG, "onPackagesUpdated: widgets num=" + this.mWidgets.size());
        for (AppWidgetProviderInfo info : this.mWidgets) {
            Log.d(TAG, info.provider.toString());
        }
        this.mShortcuts.clear();
        Intent shortcutsIntent = new Intent("android.intent.action.CREATE_SHORTCUT");
        filterShortcut(this.mShortcuts, filter.getShortcutsBlackList());
        this.mShortcuts = this.mPackageManager.queryIntentActivities(shortcutsIntent, 0);
        Log.d(TAG, "onPackagesUpdated: shortcuts num=" + this.mShortcuts.size());
        for (ResolveInfo info2 : this.mShortcuts) {
            Log.d(TAG, info2.activityInfo.packageName);
        }
        Collections.sort(this.mShortcuts, new WidgetAndShortcutNameComparator(this.mPackageManager));
        if (this.mWidgetsDialog != null && this.mWidgetsDialog.isShowing()) {
            this.mWidgetsDialog.getAdapter().notifyDataSetChanged();
        } else if (this.mShortcutsDialog != null && this.mShortcutsDialog.isShowing()) {
            this.mShortcutsDialog.getAdapter().notifyDataSetChanged();
        }
    }

    public void onSearchPackagesChanged(boolean searchVisible, boolean voiceVisible) {
        if (this.mQSBSearchBar != null) {
            Drawable bg = this.mQSBSearchBar.getBackground();
            if (bg != null && !searchVisible && !voiceVisible) {
                this.mPreviousBackground = bg;
                this.mQSBSearchBar.setBackgroundResource(0);
            } else if (this.mPreviousBackground == null) {
            } else {
                if (searchVisible || voiceVisible) {
                    this.mQSBSearchBar.setBackgroundDrawable(this.mPreviousBackground);
                }
            }
        }
    }

    public Rect getSearchBarBounds() {
        if (this.mQSBSearchBar == null) {
            return null;
        }
        float appScale = this.mQSBSearchBar.getContext().getResources().getCompatibilityInfo().applicationScale;
        int[] pos = new int[2];
        this.mQSBSearchBar.getLocationOnScreen(pos);
        Rect rect = new Rect();
        rect.left = (int) ((((float) pos[0]) * appScale) + 0.5f);
        rect.top = (int) ((((float) pos[1]) * appScale) + 0.5f);
        rect.right = (int) ((((float) (pos[0] + this.mQSBSearchBar.getWidth())) * appScale) + 0.5f);
        rect.bottom = (int) ((((float) (pos[1] + this.mQSBSearchBar.getHeight())) * appScale) + 0.5f);
        return rect;
    }

    public void closeAllWindows() {
        Log.d("czj-ios", "closeAllWindows");
        if (this.mUserCenterWindow != null && this.mUserCenterWindow.isShowing()) {
            this.mUserCenterWindow.dismiss();
        }
        if (this.mUsbWindow != null && this.mUsbWindow.isShowing()) {
            this.mUsbWindow.dismiss();
        }
        if (this.mNetWorkWindow != null && this.mNetWorkWindow.isShowing()) {
            this.mNetWorkWindow.dismiss();
        }
    }

    public void updateBluetoothState() {
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
            this.mBluetoothButton.setVisibility(8);
        } else {
            this.mBluetoothButton.setVisibility(0);
        }
    }

    public void onUpdateBluetooth(Bundle newConnectivity) {
        if (newConnectivity != null) {
            int bluetoothState = newConnectivity.getInt("android.bluetooth.adapter.extra.STATE", -1);
            if (bluetoothState == 12) {
                Log.d(TAG, "update bluetooth: state on");
                this.mBluetoothButton.setVisibility(0);
            } else if (bluetoothState == 10) {
                Log.d(TAG, "update bluetooth: state off");
                this.mBluetoothButton.setVisibility(8);
            }
        }
    }
}
