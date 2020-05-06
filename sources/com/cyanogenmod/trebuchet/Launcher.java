package com.cyanogenmod.trebuchet;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.KeyguardManager;
import android.app.SearchManager;
import android.app.UiModeManager;
import android.app.WallpaperManager;
import android.appwidget.AppWidgetHostView;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProviderInfo;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ClipData.Item;
import android.content.ClipDescription;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.DialogInterface.OnShowListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.database.ContentObserver;
import android.database.Cursor;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Drawable.ConstantState;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Process;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.os.UserManager;
import android.text.Selection;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.method.TextKeyListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.IWindowManager.Stub;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.Advanceable;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import com.android.internal.widget.LockPatternUtils;
import com.cyanogenmod.trebuchet.AddAdapter.ListItem;
import com.cyanogenmod.trebuchet.AppsCustomizeView.SortMode;
import com.cyanogenmod.trebuchet.CellLayout.LayoutParams;
import com.cyanogenmod.trebuchet.ConnectivityService.ConnectivityBinder;
import com.cyanogenmod.trebuchet.DropTarget.DragObject;
import com.cyanogenmod.trebuchet.LauncherModel.Callbacks;
import com.cyanogenmod.trebuchet.RocketLauncher.Board;
import com.cyanogenmod.trebuchet.RocketLauncher.Board.FlyingIcon;
import com.cyanogenmod.trebuchet.Workspace.TransitionEffect;
import com.cyanogenmod.trebuchet.preference.Preferences;
import com.cyanogenmod.trebuchet.preference.PreferencesProvider;
import com.cyanogenmod.trebuchet.preference.PreferencesProvider.Interface.General;
import com.cyanogenmod.trebuchet.preference.PreferencesProvider.Interface.Homescreen;
import com.cyanogenmod.trebuchet.preference.PreferencesProvider.Interface.Homescreen.Indicator;
import com.konka.android.tv.KKCommonManager;
import com.konka.android.tv.KKCommonManager.EN_KK_INPUT_SOURCE;
import com.konka.appupgrade.constant.Provider.ToBeUpgraded;
import com.konka.avenger.utilities.Storage;
import com.konka.avenger.utilities.Tools;
import com.konka.ios7launcher.R;
import com.konka.launcherblacklist.BlackListFilter;
import com.konka.multiusers.LockScreenDialog;
import com.konka.passport.service.UserInfo;
import com.tencent.stat.common.StatConstants;
import com.tencent.tvMTA.report.ReportHelper;
import com.umeng.analytics.MobclickAgent;
import com.umeng.common.a;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

@SuppressLint({"NewApi"})
public final class Launcher extends Activity implements OnClickListener, OnLongClickListener, Callbacks, OnTouchListener {
    private static /* synthetic */ int[] $SWITCH_TABLE$com$cyanogenmod$trebuchet$AppsCustomizeView$SortMode = null;
    static final int APPWIDGET_HOST_ID = 2048;
    static final boolean DEBUG_WIDGETS = false;
    static final int DEFAULT_SCREEN = 0;
    static final int DIALOG_CREATE_SHORTCUT = 1;
    static final int DIALOG_RENAME_FOLDER = 2;
    private static final int DISMISS_CLING_DURATION = 250;
    private static final int EXIT_SPRINGLOADED_MODE_LONG_TIMEOUT = 600;
    private static final int EXIT_SPRINGLOADED_MODE_SHORT_TIMEOUT = 300;
    static final String EXTRA_SHORTCUT_DUPLICATE = "duplicate";
    static final String INTENT_EXTRA_IGNORE_LAUNCH_ANIMATION = "com.android.launcher.intent.extra.shortcut.INGORE_LAUNCH_ANIMATION";
    static final boolean LOGD = false;
    static final int MAX_SCREEN_COUNT = 7;
    private static final int MENU_GROUP_MARKET = 2;
    private static final int MENU_GROUP_WALLPAPER = 1;
    private static final int MENU_HELP = 7;
    private static final int MENU_MANAGE_APPS = 3;
    private static final int MENU_MARKET = 4;
    private static final int MENU_PREFERENCES = 5;
    private static final int MENU_SYSTEM_SETTINGS = 6;
    private static final int MENU_WALLPAPER_SETTINGS = 2;
    public static final String ONLINE_GAME_PACKAGE = "com.ktcp.game";
    public static final String ONLINE_MUSIC_PACKAGE = "com.ktcp.music";
    public static final String ONLINE_PHOTO_PACKAGE = "com.ktcp.photo";
    public static final String ONLINE_VIDEO_PACKAGE = "com.ktcp.video";
    private static final String PREFERENCES = "launcher.preferences";
    static final boolean PROFILE_STARTUP = false;
    private static final int REQUEST_CREATE_APPWIDGET = 5;
    private static final int REQUEST_CREATE_SHORTCUT = 1;
    private static final int REQUEST_PICK_APPLICATION = 6;
    private static final int REQUEST_PICK_APPWIDGET = 9;
    private static final int REQUEST_PICK_SHORTCUT = 7;
    private static final int REQUEST_PICK_WALLPAPER = 10;
    private static final String RUNTIME_STATE = "launcher.state";
    private static final String RUNTIME_STATE_CURRENT_SCREEN = "launcher.current_screen";
    private static final String RUNTIME_STATE_PENDING_ADD_CELL_X = "launcher.add_cell_x";
    private static final String RUNTIME_STATE_PENDING_ADD_CELL_Y = "launcher.add_cell_y";
    private static final String RUNTIME_STATE_PENDING_ADD_CONTAINER = "launcher.add_container";
    private static final String RUNTIME_STATE_PENDING_ADD_SCREEN = "launcher.add_screen";
    private static final String RUNTIME_STATE_PENDING_FOLDER_RENAME = "launcher.rename_folder";
    private static final String RUNTIME_STATE_PENDING_FOLDER_RENAME_ID = "launcher.rename_folder_id";
    private static final int SHOW_CLING_DURATION = 550;
    static final String TAG = "Launcher";
    public static final int TIME_DELAY_MUTE_TV_VIDEO = 2000;
    public static final int TIME_DELAY_SHOW_TV_FROM_WORKSPACE = 300;
    private static final String TOOLBAR_ICON_METADATA_NAME = "com.android.launcher.toolbar_icon";
    private static boolean isScreenLocked = true;
    static DisplayMetrics sDisplayMetrics = new DisplayMetrics();
    static final ArrayList<String> sDumpLogs = new ArrayList<>();
    /* access modifiers changed from: private */
    public static HashMap<Long, FolderInfo> sFolders = new HashMap<>();
    private static ConstantState[] sGlobalSearchIcon = new ConstantState[2];
    /* access modifiers changed from: private */
    public static LocaleConfiguration sLocaleConfiguration = null;
    private static final Object sLock = new Object();
    private static ArrayList<PendingAddArguments> sPendingAddList = new ArrayList<>();
    private static int sScreen = 0;
    private static ConstantState[] sVoiceSearchIcon = new ConstantState[2];
    private final int ADVANCE_MSG = 1;
    private final int MSG_STR_OK = 2;
    private int TV_WINDOW_PADDING = 6;
    private final int mAdvanceInterval = 20000;
    private final int mAdvanceStagger = DISMISS_CLING_DURATION;
    private View mAllAppsButton;
    private Intent mAppMarketIntent = null;
    private final ContentObserver mAppUpgraderObserver = new AppUpgradeObserver();
    /* access modifiers changed from: private */
    public LauncherAppWidgetHost mAppWidgetHost;
    private AppWidgetManager mAppWidgetManager;
    /* access modifiers changed from: private */
    public AppsCustomizeView mAppsCustomizeContent;
    /* access modifiers changed from: private */
    public AppsCustomizeTabHost mAppsCustomizeTabHost;
    private boolean mAttached = false;
    private boolean mAutoAdvanceRunning = false;
    private long mAutoAdvanceSentTime;
    private long mAutoAdvanceTimeLeft = -1;
    private boolean mAutoRotate;
    BlackListFilter mBlackListFilter;
    /* access modifiers changed from: private */
    public final Runnable mBuildLayersRunnable = new Runnable() {
        public void run() {
            if (Launcher.this.mWorkspace != null) {
                Launcher.this.mWorkspace.buildPageHardwareLayers();
            }
        }
    };
    private ImageView mCibnLogo;
    private final BroadcastReceiver mCloseSystemDialogsReceiver = new CloseSystemDialogsIntentReceiver(this, null);
    /* access modifiers changed from: private */
    public ConnectivityService mConnectivityServiceBinder;
    private ServiceConnection mConnectivityServiceConnection;
    /* access modifiers changed from: private */
    public final Object mConnectivityServiceLock = new Object();
    private SpannableStringBuilder mDefaultKeySsb = null;
    private AnimatorSet mDividerAnimator;
    private View mDockDivider;
    private DragController mDragController;
    /* access modifiers changed from: private */
    public DragLayer mDragLayer;
    private int mEasterEggKeyDetectCount = 0;
    private boolean mEnforceLockItemPolicy;
    /* access modifiers changed from: private */
    public FolderInfo mFolderInfo;
    private int mFolderState = Folder.STATE_TO_RUN;
    /* access modifiers changed from: private */
    public final Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                int i = 0;
                for (View key : Launcher.this.mWidgetsToAdvance.keySet()) {
                    final View v = key.findViewById(((AppWidgetProviderInfo) Launcher.this.mWidgetsToAdvance.get(key)).autoAdvanceViewId);
                    int delay = i * Launcher.DISMISS_CLING_DURATION;
                    if (v instanceof Advanceable) {
                        postDelayed(new Runnable() {
                            public void run() {
                                ((Advanceable) v).advance();
                            }
                        }, (long) delay);
                    }
                    i++;
                }
                Launcher.this.sendAdvanceMessage(20000);
            } else if (msg.what == 2) {
                Launcher.this.ClearTVManager_RestorSTR();
                Launcher.this.SetPropertyForSTR("0");
            }
        }
    };
    private boolean mHideIconLabels;
    private Hotseat mHotseat;
    private IconCache mIconCache;
    private LayoutInflater mInflater;
    private String mIsV500Flag;
    private final BroadcastReceiver mMarketReceiver = new MarketReceiver();
    private final BroadcastReceiver mMessageReceiver = new MessageReceiver();
    /* access modifiers changed from: private */
    public LauncherModel mModel;
    private View mNextPageBtn;
    private boolean mOnResumeNeedsLoad;
    private boolean mPaused = true;
    /* access modifiers changed from: private */
    public final ItemInfo mPendingAddInfo = new ItemInfo();
    private View mPrevPageBtn;
    private View mQsbDivider;
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.d(Launcher.TAG, "receive broadcast=======" + action);
            if ("android.intent.action.SCREEN_OFF".equals(action)) {
                Launcher.this.mUserPresent = false;
                Launcher.this.mDragLayer.clearAllResizeFrames();
                Launcher.this.updateRunning();
                if (Launcher.this.mAppsCustomizeTabHost != null && Launcher.this.mPendingAddInfo.container == -1) {
                    Launcher.this.mAppsCustomizeTabHost.reset();
                    Launcher.this.showWorkspace(true);
                }
            } else if ("android.intent.action.USER_PRESENT".equals(action)) {
                Launcher.this.mUserPresent = true;
                Launcher.this.updateRunning();
            } else if (action.equals("com.konka.launcher.action.SHOW_ALL_APP")) {
                Launcher.this.showAllApps(true);
            } else if ("android.intent.action.USER_ADDED".equals(action)) {
                Launcher.this.updateLockView();
            } else if ("android.intent.action.USER_REMOVED".equals(action)) {
                Launcher.this.updateLockView();
            } else if (action.equals("com.konka.action.CHANGE_PASSWORD")) {
                Launcher.this.updateLockView();
            } else if (action.equals("konka.action.SCREEN_UNLOCK")) {
                Launcher.this.setSourceToMM();
            }
        }
    };
    private final int mRestoreScreenOrientationDelay = 500;
    private boolean mRestoring;
    private Bundle mSavedInstanceState;
    private Bundle mSavedState;
    /* access modifiers changed from: private */
    public SearchDropTargetBar mSearchDropTargetBar;
    private boolean mShowDockDivider;
    private boolean mShowSearchBar;
    private State mState = State.WORKSPACE;
    private AnimatorSet mStateAnimation;
    private final BroadcastReceiver mSystemUpgradeReceiver = new SystemUpgradeReceiver(this, null);
    private ImageView mThememanagerButton;
    private final int[] mTmpAddItemCellCoordinates = new int[2];
    /* access modifiers changed from: private */
    public boolean mUserCenterConnectCalledOnce = false;
    /* access modifiers changed from: private */
    public UserInfo mUserInfo;
    /* access modifiers changed from: private */
    public boolean mUserPresent = true;
    private boolean mVisible = false;
    /* access modifiers changed from: private */
    public boolean mWaitingForResult;
    private BubbleTextView mWaitingForResume;
    private final ContentObserver mWidgetObserver = new AppWidgetResetObserver();
    /* access modifiers changed from: private */
    public final HashMap<View, AppWidgetProviderInfo> mWidgetsToAdvance = new HashMap<>();
    /* access modifiers changed from: private */
    public Workspace mWorkspace;
    /* access modifiers changed from: private */
    public boolean mWorkspaceLoading = true;
    private ImageView selectboxView;

    private class AppUpgradeObserver extends ContentObserver {
        public AppUpgradeObserver() {
            super(new Handler());
        }

        public void onChange(boolean selfChange) {
            Log.d(Launcher.TAG, "onChange postRet=" + Launcher.this.mSearchDropTargetBar.post(new Runnable() {
                public void run() {
                    Launcher.this.mSearchDropTargetBar.onAppUpgradeChange(Launcher.this.queryUpgradeInfo());
                }
            }));
        }
    }

    private class AppWidgetResetObserver extends ContentObserver {
        public AppWidgetResetObserver() {
            super(new Handler());
        }

        public void onChange(boolean selfChange) {
            Launcher.this.onAppWidgetReset();
        }
    }

    private class CloseSystemDialogsIntentReceiver extends BroadcastReceiver {
        private CloseSystemDialogsIntentReceiver() {
        }

        /* synthetic */ CloseSystemDialogsIntentReceiver(Launcher launcher, CloseSystemDialogsIntentReceiver closeSystemDialogsIntentReceiver) {
            this();
        }

        public void onReceive(Context context, Intent intent) {
            Launcher.this.closeSystemDialogs();
        }
    }

    private class CreateShortcut implements DialogInterface.OnClickListener, OnCancelListener, OnDismissListener, OnShowListener {
        private AddAdapter mAdapter;

        private CreateShortcut() {
        }

        /* synthetic */ CreateShortcut(Launcher launcher, CreateShortcut createShortcut) {
            this();
        }

        /* access modifiers changed from: 0000 */
        public Dialog createDialog() {
            this.mAdapter = new AddAdapter(Launcher.this);
            Builder builder = new Builder(Launcher.this, 2);
            builder.setAdapter(this.mAdapter, this);
            AlertDialog dialog = builder.create();
            dialog.setOnCancelListener(this);
            dialog.setOnDismissListener(this);
            dialog.setOnShowListener(this);
            return dialog;
        }

        public void onCancel(DialogInterface dialog) {
            Launcher.this.mWaitingForResult = false;
            cleanup();
        }

        public void onDismiss(DialogInterface dialog) {
            Launcher.this.mWaitingForResult = false;
            cleanup();
        }

        private void cleanup() {
            try {
                Launcher.this.dismissDialog(1);
            } catch (Exception e) {
            }
        }

        public void onClick(DialogInterface dialog, int which) {
            cleanup();
            switch (((ListItem) this.mAdapter.getItem(which)).actionTag) {
                case 1:
                    if (Launcher.this.mAppsCustomizeTabHost != null) {
                        Launcher.this.mAppsCustomizeTabHost.selectWidgetsTab();
                    }
                    Launcher.this.showAllApps(true);
                    return;
                case 2:
                    if (Launcher.this.mAppsCustomizeTabHost != null) {
                        Launcher.this.mAppsCustomizeTabHost.selectAppsTab();
                    }
                    Launcher.this.showAllApps(true);
                    return;
                case 3:
                    Launcher.this.startWallpaper();
                    return;
                default:
                    return;
            }
        }

        public void onShow(DialogInterface dialog) {
            Launcher.this.mWaitingForResult = true;
        }
    }

    private static class LocaleConfiguration {
        public String locale;
        public int mcc;
        public int mnc;

        private LocaleConfiguration() {
            this.mcc = -1;
            this.mnc = -1;
        }

        /* synthetic */ LocaleConfiguration(LocaleConfiguration localeConfiguration) {
            this();
        }
    }

    private static class PendingAddArguments {
        int cellX;
        int cellY;
        long container;
        Intent intent;
        int requestCode;
        int screen;

        private PendingAddArguments() {
        }

        /* synthetic */ PendingAddArguments(PendingAddArguments pendingAddArguments) {
            this();
        }
    }

    private class RenameFolder {
        /* access modifiers changed from: private */
        public EditText mInput;

        private RenameFolder() {
        }

        /* synthetic */ RenameFolder(Launcher launcher, RenameFolder renameFolder) {
            this();
        }

        /* access modifiers changed from: 0000 */
        public Dialog createDialog() {
            View layout = View.inflate(Launcher.this, R.layout.rename_folder, null);
            this.mInput = (EditText) layout.findViewById(R.id.folder_name);
            Builder builder = new Builder(Launcher.this);
            builder.setIcon(0);
            builder.setTitle(Launcher.this.getString(R.string.rename_folder_title));
            builder.setCancelable(true);
            builder.setOnCancelListener(new OnCancelListener() {
                public void onCancel(DialogInterface dialog) {
                    RenameFolder.this.cleanup();
                }
            });
            builder.setNegativeButton(Launcher.this.getString(R.string.cancel_action), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    RenameFolder.this.cleanup();
                }
            });
            builder.setPositiveButton(Launcher.this.getString(R.string.rename_action), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    RenameFolder.this.changeFolderName();
                }
            });
            builder.setView(layout);
            AlertDialog dialog = builder.create();
            dialog.setOnShowListener(new OnShowListener() {
                public void onShow(DialogInterface dialog) {
                    Launcher.this.mWaitingForResult = true;
                    RenameFolder.this.mInput.requestFocus();
                    ((InputMethodManager) Launcher.this.getSystemService("input_method")).showSoftInput(RenameFolder.this.mInput, 0);
                }
            });
            return dialog;
        }

        /* access modifiers changed from: private */
        public void changeFolderName() {
            String name = this.mInput.getText().toString();
            if (!TextUtils.isEmpty(name)) {
                Launcher.this.mFolderInfo = (FolderInfo) Launcher.sFolders.get(Long.valueOf(Launcher.this.mFolderInfo.id));
                Launcher.this.mFolderInfo.title = name;
                LauncherModel.updateItemInDatabase(Launcher.this, Launcher.this.mFolderInfo);
                if (Launcher.this.mWorkspaceLoading) {
                    Launcher.this.lockAllApps();
                    Launcher.this.mModel.startLoader(Launcher.this, false);
                } else if (((FolderIcon) Launcher.this.mWorkspace.getViewForTag(Launcher.this.mFolderInfo)) != null) {
                    Launcher.this.getWorkspace().requestLayout();
                } else {
                    Launcher.this.lockAllApps();
                    Launcher.this.mWorkspaceLoading = true;
                    Launcher.this.mModel.startLoader(Launcher.this, false);
                }
            }
            cleanup();
        }

        /* access modifiers changed from: private */
        public void cleanup() {
            Launcher.this.dismissDialog(2);
            Launcher.this.mWaitingForResult = false;
            Launcher.this.mFolderInfo = null;
        }
    }

    private class STRTVWdigetThread extends Thread {
        private STRTVWdigetThread() {
        }

        public void run() {
            while (true) {
                String suspending = SystemProperties.get("mstar.str.suspending");
                Log.d("STRTVWdigetThread", "STRTVWdigetThread suspending str suspending = " + suspending);
                if ("2".equals(suspending)) {
                    Launcher.this.mHandler.sendEmptyMessage(2);
                    return;
                }
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private enum State {
        WORKSPACE,
        APPS_CUSTOMIZE,
        APPS_CUSTOMIZE_SPRING_LOADED
    }

    private class SystemUpgradeReceiver extends BroadcastReceiver {
        private SystemUpgradeReceiver() {
        }

        /* synthetic */ SystemUpgradeReceiver(Launcher launcher, SystemUpgradeReceiver systemUpgradeReceiver) {
            this();
        }

        public void onReceive(Context context, Intent intent) {
            String string = intent.getExtras().getString("upgrade_version");
        }
    }

    static /* synthetic */ int[] $SWITCH_TABLE$com$cyanogenmod$trebuchet$AppsCustomizeView$SortMode() {
        int[] iArr = $SWITCH_TABLE$com$cyanogenmod$trebuchet$AppsCustomizeView$SortMode;
        if (iArr == null) {
            iArr = new int[SortMode.values().length];
            try {
                iArr[SortMode.InstallDate.ordinal()] = 3;
            } catch (NoSuchFieldError e) {
            }
            try {
                iArr[SortMode.KKDefault.ordinal()] = 1;
            } catch (NoSuchFieldError e2) {
            }
            try {
                iArr[SortMode.Title.ordinal()] = 2;
            } catch (NoSuchFieldError e3) {
            }
            $SWITCH_TABLE$com$cyanogenmod$trebuchet$AppsCustomizeView$SortMode = iArr;
        }
        return iArr;
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "====onCreate====");
        this.mIsV500Flag = KKCommonManager.getInstance(getApplicationContext()).getPlatform();
        Log.d(TAG, "Launcher.onCreate.Platform = " + this.mIsV500Flag);
        if (!this.mIsV500Flag.equals("hisiv500")) {
            setWallPaper();
        }
        MobclickAgent.onError(this);
        getWindowDPI();
        LauncherApplication app = (LauncherApplication) getApplication();
        this.mModel = app.setLauncher(this);
        this.mIconCache = app.getIconCache();
        this.mDragController = new DragController(this);
        this.mInflater = getLayoutInflater();
        this.mAppWidgetManager = AppWidgetManager.getInstance(this);
        this.mAppWidgetHost = new LauncherAppWidgetHost(this, APPWIDGET_HOST_ID);
        this.mAppWidgetHost.startListening();
        this.mShowSearchBar = Homescreen.getShowSearchBar(this);
        this.mShowDockDivider = Indicator.getShowDockDivider(this);
        this.mHideIconLabels = Homescreen.getHideIconLabels(this);
        this.mAutoRotate = General.getAutoRotate(this, getResources().getBoolean(R.bool.config_defaultAutoRotate));
        this.mEnforceLockItemPolicy = Homescreen.getEnforceLockItemPolicy(this);
        this.mBlackListFilter = BlackListFilter.getInstance(app);
        checkForLocaleChange();
        setContentView(R.layout.launcher);
        setupViews();
        showFirstRunWorkspaceCling();
        this.mConnectivityServiceConnection = new ServiceConnection() {
            public void onServiceConnected(ComponentName className, IBinder service) {
                synchronized (Launcher.this.mConnectivityServiceLock) {
                    Launcher.this.mConnectivityServiceBinder = ((ConnectivityBinder) service).getService();
                    Launcher.this.mConnectivityServiceBinder.registerTimeListener(Launcher.this.mSearchDropTargetBar).registerUserCenterListener(Launcher.this.mSearchDropTargetBar).registerNetworkListener(Launcher.this.mSearchDropTargetBar).registerUsbListener(Launcher.this.mSearchDropTargetBar).registerBatteryListener(Launcher.this.mSearchDropTargetBar).registerBluetoothListener(Launcher.this.mSearchDropTargetBar);
                    Launcher.this.mConnectivityServiceBinder.startMonitors();
                    if (!Launcher.this.mUserCenterConnectCalledOnce) {
                        Launcher.this.mUserCenterConnectCalledOnce = true;
                        Launcher.this.mConnectivityServiceBinder.onUserCenterConnected(Launcher.this.mUserInfo);
                    }
                }
            }

            public void onServiceDisconnected(ComponentName className) {
                Launcher.this.mConnectivityServiceBinder = null;
            }
        };
        registerContentObservers();
        lockAllApps();
        if (this.mAppsCustomizeContent != null) {
            this.mAppsCustomizeContent.onPackagesUpdated();
        }
        if (!this.mRestoring) {
            this.mModel.startLoader(this, true);
        }
        if (!this.mModel.isAllAppsLoaded()) {
            this.mInflater.inflate(R.layout.apps_customize_progressbar, (ViewGroup) ((View) this.mAppsCustomizeContent).getParent());
        }
        this.mDefaultKeySsb = new SpannableStringBuilder();
        Selection.setSelection(this.mDefaultKeySsb, 0);
        registerReceiver(this.mCloseSystemDialogsReceiver, new IntentFilter("android.intent.action.CLOSE_SYSTEM_DIALOGS"));
        registerUpgradeInfo();
        IntentFilter messageFilter = new IntentFilter();
        messageFilter.addAction(MessageReceiver.SYSTEM_MESSAGE_ACTION);
        messageFilter.addAction(MessageReceiver.PASSPORT_MESSAGE_ACTION);
        registerReceiver(this.mMessageReceiver, messageFilter);
        registerReceiver(this.mMarketReceiver, new IntentFilter("com.konka.market.main"));
        boolean searchVisible = false;
        boolean voiceVisible = false;
        int coi = getCurrentOrientationIndexForGlobalIcons();
        if (sGlobalSearchIcon[coi] == null || sVoiceSearchIcon[coi] == null) {
            searchVisible = updateGlobalSearchIcon();
            voiceVisible = updateVoiceSearchIcon(searchVisible);
        }
        if (sGlobalSearchIcon[coi] != null) {
            updateGlobalSearchIcon(sGlobalSearchIcon[coi]);
            searchVisible = true;
        }
        if (sVoiceSearchIcon[coi] != null) {
            updateVoiceSearchIcon(sVoiceSearchIcon[coi]);
            voiceVisible = true;
        }
        this.mSearchDropTargetBar.onSearchPackagesChanged(searchVisible, voiceVisible);
    }

    private void getWindowDPI() {
        getWindowManager().getDefaultDisplay().getMetrics(sDisplayMetrics);
    }

    /* access modifiers changed from: private */
    public void syncOrientation() {
        UiModeManager uiModeManager = (UiModeManager) getSystemService("uimode");
        if (this.mAutoRotate || uiModeManager.getCurrentModeType() != 1) {
            setRequestedOrientation(-1);
        } else {
            setRequestedOrientation(5);
        }
    }

    /* access modifiers changed from: private */
    public void checkForLocaleChange() {
        boolean localeChanged = false;
        if (sLocaleConfiguration == null) {
            new AsyncTask<Void, Void, LocaleConfiguration>() {
                /* access modifiers changed from: protected */
                public LocaleConfiguration doInBackground(Void... unused) {
                    LocaleConfiguration localeConfiguration = new LocaleConfiguration(null);
                    Launcher.readConfiguration(Launcher.this, localeConfiguration);
                    return localeConfiguration;
                }

                /* access modifiers changed from: protected */
                public void onPostExecute(LocaleConfiguration result) {
                    Launcher.sLocaleConfiguration = result;
                    Launcher.this.checkForLocaleChange();
                }
            }.execute(new Void[0]);
            return;
        }
        Configuration configuration = getResources().getConfiguration();
        String previousLocale = sLocaleConfiguration.locale;
        String locale = configuration.locale.toString();
        int previousMcc = sLocaleConfiguration.mcc;
        int mcc = configuration.mcc;
        int previousMnc = sLocaleConfiguration.mnc;
        int mnc = configuration.mnc;
        if (!(locale.equals(previousLocale) && mcc == previousMcc && mnc == previousMnc)) {
            localeChanged = true;
        }
        if (localeChanged) {
            sLocaleConfiguration.locale = locale;
            sLocaleConfiguration.mcc = mcc;
            sLocaleConfiguration.mnc = mnc;
            this.mIconCache.flush();
            final LocaleConfiguration localeConfiguration = sLocaleConfiguration;
            new Thread("WriteLocaleConfiguration") {
                public void run() {
                    Launcher.writeConfiguration(Launcher.this, localeConfiguration);
                }
            }.start();
        }
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Removed duplicated region for block: B:11:0x0028 A[SYNTHETIC, Splitter:B:11:0x0028] */
    /* JADX WARNING: Removed duplicated region for block: B:16:0x0031 A[SYNTHETIC, Splitter:B:16:0x0031] */
    /* JADX WARNING: Removed duplicated region for block: B:21:0x003a A[SYNTHETIC, Splitter:B:21:0x003a] */
    /* JADX WARNING: Removed duplicated region for block: B:35:? A[RETURN, SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:36:? A[RETURN, SYNTHETIC] */
    public static void readConfiguration(Context context, LocaleConfiguration configuration) {
        DataInputStream in = null;
        try {
            DataInputStream in2 = new DataInputStream(context.openFileInput(PREFERENCES));
            try {
                configuration.locale = in2.readUTF();
                configuration.mcc = in2.readInt();
                configuration.mnc = in2.readInt();
                if (in2 != null) {
                    try {
                        in2.close();
                        DataInputStream dataInputStream = in2;
                    } catch (IOException e) {
                        DataInputStream dataInputStream2 = in2;
                    }
                }
            } catch (FileNotFoundException e2) {
                in = in2;
                if (in == null) {
                    try {
                        in.close();
                    } catch (IOException e3) {
                    }
                }
            } catch (IOException e4) {
                in = in2;
                if (in == null) {
                    try {
                        in.close();
                    } catch (IOException e5) {
                    }
                }
            } catch (Throwable th) {
                th = th;
                in = in2;
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e6) {
                    }
                }
                throw th;
            }
        } catch (FileNotFoundException e7) {
            if (in == null) {
            }
        } catch (IOException e8) {
            if (in == null) {
            }
        } catch (Throwable th2) {
            th = th2;
            if (in != null) {
            }
            throw th;
        }
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Removed duplicated region for block: B:11:0x0029 A[SYNTHETIC, Splitter:B:11:0x0029] */
    /* JADX WARNING: Removed duplicated region for block: B:19:0x003b A[SYNTHETIC, Splitter:B:19:0x003b] */
    /* JADX WARNING: Removed duplicated region for block: B:24:0x0044 A[SYNTHETIC, Splitter:B:24:0x0044] */
    /* JADX WARNING: Removed duplicated region for block: B:38:? A[RETURN, SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:39:? A[RETURN, SYNTHETIC] */
    public static void writeConfiguration(Context context, LocaleConfiguration configuration) {
        DataOutputStream out = null;
        try {
            DataOutputStream out2 = new DataOutputStream(context.openFileOutput(PREFERENCES, 0));
            try {
                out2.writeUTF(configuration.locale);
                out2.writeInt(configuration.mcc);
                out2.writeInt(configuration.mnc);
                out2.flush();
                if (out2 != null) {
                    try {
                        out2.close();
                        DataOutputStream dataOutputStream = out2;
                    } catch (IOException e) {
                        DataOutputStream dataOutputStream2 = out2;
                    }
                }
            } catch (FileNotFoundException e2) {
                out = out2;
                if (out == null) {
                }
            } catch (IOException e3) {
                out = out2;
                try {
                    context.getFileStreamPath(PREFERENCES).delete();
                    if (out == null) {
                    }
                } catch (Throwable th) {
                    th = th;
                    if (out != null) {
                        try {
                            out.close();
                        } catch (IOException e4) {
                        }
                    }
                    throw th;
                }
            } catch (Throwable th2) {
                th = th2;
                out = out2;
                if (out != null) {
                }
                throw th;
            }
        } catch (FileNotFoundException e5) {
            if (out == null) {
                try {
                    out.close();
                } catch (IOException e6) {
                }
            }
        } catch (IOException e7) {
            context.getFileStreamPath(PREFERENCES).delete();
            if (out == null) {
                try {
                    out.close();
                } catch (IOException e8) {
                }
            }
        }
    }

    public DragLayer getDragLayer() {
        return this.mDragLayer;
    }

    static int getScreen() {
        int i;
        synchronized (sLock) {
            i = sScreen;
        }
        return i;
    }

    static void setScreen(int screen) {
        synchronized (sLock) {
            sScreen = screen;
        }
    }

    static boolean isScreenLocked() {
        boolean z;
        synchronized (sLock) {
            z = isScreenLocked;
        }
        return z;
    }

    private boolean isKeyguardLocked() {
        KeyguardManager keyguardManager = (KeyguardManager) getSystemService("keyguard");
        return keyguardManager != null && keyguardManager.isKeyguardLocked();
    }

    public static void setScreenLockState(boolean lock) {
        synchronized (sLock) {
            isScreenLocked = lock;
        }
    }

    private boolean completeAdd(PendingAddArguments args) {
        boolean result = false;
        switch (args.requestCode) {
            case 1:
                completeAddShortcut(args.intent, args.container, args.screen, args.cellX, args.cellY);
                result = true;
                break;
            case 5:
                completeAddAppWidget(args.intent.getIntExtra("appWidgetId", -1), args.container, args.screen);
                result = true;
                break;
            case 6:
                completeAddApplication(args.intent, args.container, args.screen, args.cellX, args.cellY);
                break;
            case 7:
                processShortcut(args.intent);
                break;
            case 9:
                addAppWidgetFromPick(args.intent);
                break;
        }
        resetAddInfo();
        return result;
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        boolean z = false;
        boolean delayExitSpringLoadedMode = false;
        this.mWaitingForResult = false;
        if (resultCode == -1 && this.mPendingAddInfo.container != -1) {
            PendingAddArguments args = new PendingAddArguments(null);
            args.requestCode = requestCode;
            args.intent = data;
            args.container = this.mPendingAddInfo.container;
            args.screen = this.mPendingAddInfo.screen;
            args.cellX = this.mPendingAddInfo.cellX;
            args.cellY = this.mPendingAddInfo.cellY;
            if (isWorkspaceLocked()) {
                sPendingAddList.add(args);
            } else {
                delayExitSpringLoadedMode = completeAdd(args);
            }
        } else if ((requestCode == 9 || requestCode == 5) && resultCode == 0 && data != null) {
            int appWidgetId = data.getIntExtra("appWidgetId", -1);
            if (appWidgetId != -1) {
                this.mAppWidgetHost.deleteAppWidgetId(appWidgetId);
            }
        }
        if (resultCode != 0) {
            z = true;
        }
        exitSpringLoadedDragModeDelayed(z, delayExitSpringLoadedMode);
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        bindConnectivityService();
        MessageManager.getInstance().registerUpdateListener(this.mSearchDropTargetBar);
        MobclickAgent.onResume(this);
        setSourceToMM();
        Log.d(TAG, "======onResume");
        this.mPaused = false;
        if (preferencesChanged()) {
            Process.killProcess(Process.myPid());
        }
        if (this.mRestoring || this.mOnResumeNeedsLoad) {
            this.mWorkspaceLoading = true;
            Log.d("czj-ios", "call onResume");
            Log.d("czj-ios", "mRestoring:" + this.mRestoring + ";mOnResumeNeedsLoad:" + this.mOnResumeNeedsLoad);
            this.mModel.startLoader(this, true);
            this.mRestoring = false;
            this.mOnResumeNeedsLoad = false;
        }
        if (this.mWaitingForResume != null) {
            this.mWaitingForResume.setStayPressed(false);
        }
        this.mAppsCustomizeTabHost.onResume();
        if (!this.mWorkspaceLoading) {
            final ViewTreeObserver observer = this.mWorkspace.getViewTreeObserver();
            final Workspace workspace = this.mWorkspace;
            observer.addOnPreDrawListener(new OnPreDrawListener() {
                boolean mFirstTime = true;

                public boolean onPreDraw() {
                    if (this.mFirstTime) {
                        this.mFirstTime = false;
                    } else {
                        workspace.postDelayed(Launcher.this.mBuildLayersRunnable, 500);
                        observer.removeOnPreDrawListener(this);
                    }
                    return true;
                }
            });
        }
        clearTypedText();
        this.mHandler.postDelayed(new Runnable() {
            public void run() {
                Launcher.this.mDragLayer.snapToWorkspace();
            }
        }, 500);
    }

    /* access modifiers changed from: private */
    public void setSourceToMM() {
        if (!isScreenLockEnable() || !isKeyguardLocked()) {
            KKCommonManager kkcm = KKCommonManager.getInstance(this);
            if (kkcm != null && !kkcm.getCurrentInputSource().equals(EN_KK_INPUT_SOURCE.STORAGE)) {
                kkcm.setInputSource(EN_KK_INPUT_SOURCE.STORAGE);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        super.onPause();
        this.mConnectivityServiceBinder = null;
        unbindService(this.mConnectivityServiceConnection);
        MessageManager.getInstance().unregisterUpdateListener(this.mSearchDropTargetBar);
        MobclickAgent.onPause(this);
        Log.d(TAG, "======onPause");
        this.mPaused = true;
        this.mDragController.cancelDrag();
        this.mAppsCustomizeTabHost.onPause();
    }

    /* access modifiers changed from: protected */
    public void onStart() {
        Log.d(TAG, "========onStart");
        super.onStart();
    }

    /* access modifiers changed from: protected */
    public void onStop() {
        Log.d(TAG, "========onStop");
        super.onStop();
    }

    public Object onRetainNonConfigurationInstance() {
        this.mModel.stopLoader();
        if (this.mAppsCustomizeContent != null) {
            this.mAppsCustomizeContent.surrender();
        }
        return Boolean.TRUE;
    }

    private boolean acceptFilter() {
        return !((InputMethodManager) getSystemService("input_method")).isFullscreenMode();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.d(TAG, "onKeyDown");
        int uniChar = event.getUnicodeChar();
        if (keyCode == 22) {
            ViewGroup statusBar = (ViewGroup) this.mSearchDropTargetBar.findViewById(R.id.qsb_static_bar);
            if (statusBar.hasFocus()) {
                int statusBarLastViewId = -1;
                int i = statusBar.getChildCount() - 1;
                while (true) {
                    if (i < 0) {
                        break;
                    }
                    View v = statusBar.getChildAt(i);
                    if (v.isShown()) {
                        statusBarLastViewId = v.getId();
                        break;
                    }
                    i--;
                }
                if (statusBarLastViewId == statusBar.getFocusedChild().getId()) {
                    return true;
                }
            }
        }
        boolean handled = super.onKeyDown(keyCode, event);
        if (!handled) {
            switch (keyCode) {
                case 7:
                    if (this.mEasterEggKeyDetectCount == 1 || this.mEasterEggKeyDetectCount == 2) {
                        this.mEasterEggKeyDetectCount++;
                        return true;
                    }
                case 8:
                    if (this.mEasterEggKeyDetectCount == 2) {
                        this.mEasterEggKeyDetectCount++;
                        return true;
                    }
                    break;
                case 9:
                    if (this.mEasterEggKeyDetectCount == 0) {
                        this.mEasterEggKeyDetectCount++;
                        return true;
                    }
                    break;
                case 11:
                    break;
                case 15:
                    if (this.mEasterEggKeyDetectCount == 3) {
                        startActivitySafely(null, new Intent(this, Preferences.class), new String("Easter Egg config"));
                        this.mEasterEggKeyDetectCount = 0;
                        return true;
                    }
                    break;
            }
            if (this.mEasterEggKeyDetectCount == 3) {
                startActivitySafely(null, new Intent(this, RocketLauncher.class), new String("Easter Egg showtime"));
                this.mEasterEggKeyDetectCount = 0;
                return true;
            }
            this.mEasterEggKeyDetectCount = 0;
        }
        boolean isKeyNotWhitespace = uniChar > 0 && !Character.isWhitespace(uniChar);
        if (!handled && acceptFilter() && isKeyNotWhitespace && TextKeyListener.getInstance().onKeyDown(this.mWorkspace, this.mDefaultKeySsb, keyCode, event) && this.mDefaultKeySsb != null && this.mDefaultKeySsb.length() > 0) {
            return onSearchRequested();
        }
        if (keyCode == 82 && event.isLongPress()) {
            return true;
        }
        if (!this.mWorkspace.isInSearchScreen() || !this.mWorkspace.getSearchScreen().hasFocus() || handled) {
            return handled;
        }
        switch (keyCode) {
            case 19:
                return this.mSearchDropTargetBar.requestFocus();
            case Board.NUM_ICONS /*20*/:
                return this.mWorkspace.getScrollingIndicator().requestFocus();
            case 92:
                this.mWorkspace.scrollLeft();
                return true;
            case 93:
                this.mWorkspace.scrollRight();
                return true;
            default:
                return false;
        }
    }

    private String getTypedText() {
        return this.mDefaultKeySsb.toString();
    }

    private void clearTypedText() {
        this.mDefaultKeySsb.clear();
        this.mDefaultKeySsb.clearSpans();
        Selection.setSelection(this.mDefaultKeySsb, 0);
    }

    private static State intToState(int stateOrdinal) {
        State state = State.WORKSPACE;
        State[] stateValues = State.values();
        for (int i = 0; i < stateValues.length; i++) {
            if (stateValues[i].ordinal() == stateOrdinal) {
                return stateValues[i];
            }
        }
        return state;
    }

    private void restoreState(Bundle savedState) {
        if (savedState != null) {
            if (intToState(savedState.getInt(RUNTIME_STATE, State.WORKSPACE.ordinal())) == State.APPS_CUSTOMIZE) {
                showAllApps(false);
            }
            int currentScreen = savedState.getInt(RUNTIME_STATE_CURRENT_SCREEN, -1);
            if (currentScreen > -1) {
                this.mWorkspace.setCurrentPage(currentScreen);
            }
            long pendingAddContainer = savedState.getLong(RUNTIME_STATE_PENDING_ADD_CONTAINER, -1);
            int pendingAddScreen = savedState.getInt(RUNTIME_STATE_PENDING_ADD_SCREEN, -1);
            if (pendingAddContainer != -1 && pendingAddScreen > -1) {
                this.mPendingAddInfo.container = pendingAddContainer;
                this.mPendingAddInfo.screen = pendingAddScreen;
                this.mPendingAddInfo.cellX = savedState.getInt(RUNTIME_STATE_PENDING_ADD_CELL_X);
                this.mPendingAddInfo.cellY = savedState.getInt(RUNTIME_STATE_PENDING_ADD_CELL_Y);
                this.mRestoring = true;
            }
            if (savedState.getBoolean(RUNTIME_STATE_PENDING_FOLDER_RENAME, false)) {
                this.mFolderInfo = LauncherModel.getFolderById(this, sFolders, savedState.getLong(RUNTIME_STATE_PENDING_FOLDER_RENAME_ID));
                this.mRestoring = true;
            }
            if (this.mAppsCustomizeTabHost != null) {
                String curTab = savedState.getString("apps_customize_currentTab");
                if (curTab != null) {
                    this.mAppsCustomizeContent.setContentType(this.mAppsCustomizeTabHost.getContentTypeForTabTag(curTab));
                    this.mAppsCustomizeTabHost.setCurrentTabByTag(curTab);
                }
                this.mAppsCustomizeContent.restore(savedState.getInt("apps_customize_currentIndex"));
            }
        }
    }

    private void setupViews() {
        DragController dragController = this.mDragController;
        this.mDragLayer = (DragLayer) findViewById(R.id.drag_layer);
        this.mWorkspace = (Workspace) this.mDragLayer.findViewById(R.id.workspace);
        this.mQsbDivider = findViewById(R.id.qsb_divider);
        this.mDockDivider = findViewById(R.id.dock_divider);
        this.mDragLayer.setup(this, dragController);
        this.mHotseat = (Hotseat) findViewById(R.id.hotseat);
        if (this.mHotseat != null) {
            this.mHotseat.setup(this);
        }
        this.mWorkspace.setHapticFeedbackEnabled(false);
        this.mWorkspace.setOnLongClickListener(this);
        this.mWorkspace.setup(dragController);
        dragController.addDragListener(this.mWorkspace);
        updateLockView();
        updateThemeMangerView();
        updateCIBNLogoView();
        this.mSearchDropTargetBar = (SearchDropTargetBar) this.mDragLayer.findViewById(R.id.qsb_bar);
        View qsbDivider = findViewById(R.id.qsb_divider);
        View dockDivider = findViewById(R.id.dock_divider);
        if (!this.mShowSearchBar && qsbDivider != null) {
            qsbDivider.setVisibility(8);
        }
        if (!this.mShowDockDivider && dockDivider != null) {
            dockDivider.setVisibility(8);
        }
        this.mAppsCustomizeTabHost = (AppsCustomizeTabHost) findViewById(R.id.apps_customize_pane);
        this.mAppsCustomizeContent = (AppsCustomizeView) this.mAppsCustomizeTabHost.findViewById(R.id.apps_customize_pane_content);
        this.mAppsCustomizeContent.setup(this, dragController);
        this.mAllAppsButton = findViewById(R.id.all_apps_button);
        if (this.mAllAppsButton != null) {
            this.mAllAppsButton.setOnTouchListener(new OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    if ((event.getAction() & 255) == 0) {
                        Launcher.this.onTouchDownAllAppsButton(v);
                    }
                    return false;
                }
            });
        }
        dragController.setDragScoller(this.mWorkspace);
        dragController.setScrollView(this.mDragLayer);
        dragController.setMoveTarget(this.mWorkspace);
        dragController.addDropTarget(this.mWorkspace);
        if (this.mSearchDropTargetBar != null) {
            this.mSearchDropTargetBar.setup(this, dragController);
        }
        this.mPrevPageBtn = findViewById(R.id.workspace_prev_page_button);
        if (this.mPrevPageBtn != null) {
            this.mPrevPageBtn.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    if (Launcher.this.mWorkspace.getCurrentPage() > 0) {
                        Launcher.this.mWorkspace.scrollLeft();
                    } else if (Launcher.this.mDragLayer != null) {
                        Launcher.this.mDragLayer.snapToTVPage();
                    }
                }
            });
        }
        this.mNextPageBtn = findViewById(R.id.workspace_next_page_button);
        if (this.mNextPageBtn != null) {
            this.mNextPageBtn.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    Launcher.this.mWorkspace.scrollRight();
                }
            });
        }
        refreshPageBtn();
    }

    public void refreshPageBtn() {
        refreshPageBtn(this.mWorkspace.getCurrentPage(), this.mWorkspace.getPageCount());
    }

    private void refreshPageBtn(int curPage, int pageCount) {
        boolean z;
        boolean z2 = true;
        if (curPage >= 0) {
            z = true;
        } else {
            z = false;
        }
        setPrevPageBtnVisiable(z);
        if (curPage >= pageCount - 1) {
            z2 = false;
        }
        setNextPageBtnVisiable(z2);
    }

    private void setPrevPageBtnVisiable(boolean visiable) {
        if (this.mPrevPageBtn != null) {
            this.mPrevPageBtn.setVisibility(visiable ? 0 : 4);
        }
    }

    private void setNextPageBtnVisiable(boolean visiable) {
        if (this.mNextPageBtn != null) {
            this.mNextPageBtn.setVisibility(visiable ? 0 : 4);
        }
    }

    /* access modifiers changed from: 0000 */
    public View createShortcut(ShortcutInfo info) {
        return createShortcut(R.layout.application, (ViewGroup) this.mWorkspace.getChildAt(this.mWorkspace.getCurrentPage()), info);
    }

    /* access modifiers changed from: 0000 */
    public View createShortcut(int layoutResId, ViewGroup parent, ShortcutInfo info) {
        BubbleTextView favorite = (BubbleTextView) this.mInflater.inflate(layoutResId, parent, false);
        favorite.applyFromShortcutInfo(info, this.mIconCache);
        if (this.mHideIconLabels) {
            favorite.setTextVisible(false);
        }
        favorite.setOnClickListener(this);
        if (info.checkShortcutPackageName(MessageManager.MESSAGE_PACKAGE_NAME)) {
            Log.d(TAG, "create message shortcut");
            MessageManager messageManager = MessageManager.getInstance();
            messageManager.setMessageView(favorite);
            messageManager.updateMessageView();
        } else if (info.checkShortcutPackageName("com.konka.market.main")) {
            Log.d(TAG, "create market shortcut");
            MarketManager marketManager = MarketManager.getInstance();
            marketManager.setMarketView(favorite);
            marketManager.updateMarketView();
        }
        return favorite;
    }

    /* access modifiers changed from: 0000 */
    public void completeAddApplication(Intent data, long container, int screen, int cellX, int cellY) {
        int[] cellXY = this.mTmpAddItemCellCoordinates;
        CellLayout layout = getCellLayout(container, screen);
        if (!this.mWorkspace.isAllowAddItem(layout)) {
            showNotAllowedAddMessage();
            return;
        }
        if (cellX >= 0 && cellY >= 0) {
            cellXY[0] = cellX;
            cellXY[1] = cellY;
        } else if (!layout.findCellForSpan(cellXY, 1, 1)) {
            showOutOfSpaceMessage();
            return;
        }
        ShortcutInfo info = this.mModel.getShortcutInfo(getPackageManager(), data, this);
        if (info != null) {
            info.setActivity(data.getComponent(), 270532608);
            info.container = -1;
            this.mWorkspace.addApplicationShortcut(info, layout, container, screen, cellXY[0], cellXY[1], isWorkspaceLocked(), cellX, cellY);
            return;
        }
        Log.e(TAG, "Couldn't find ActivityInfo for selected application: " + data);
    }

    private void completeAddShortcut(Intent data, long container, int screen, int cellX, int cellY) {
        boolean foundCellSpan;
        int[] cellXY = this.mTmpAddItemCellCoordinates;
        int[] touchXY = this.mPendingAddInfo.dropPos;
        CellLayout layout = getCellLayout(container, screen);
        if (!this.mWorkspace.isAllowAddItem(layout)) {
            showNotAllowedAddMessage();
            return;
        }
        ShortcutInfo info = this.mModel.infoFromShortcutIntent(this, data, null);
        if (info != null) {
            View view = createShortcut(info);
            if (cellX >= 0 && cellY >= 0) {
                cellXY[0] = cellX;
                cellXY[1] = cellY;
                foundCellSpan = true;
                if (!this.mWorkspace.createUserFolderIfNecessary(view, container, layout, cellXY, true, null, null)) {
                    DragObject dragObject = new DragObject();
                    dragObject.dragInfo = info;
                    if (this.mWorkspace.addToExistingFolderIfNecessary(view, layout, cellXY, dragObject, true)) {
                        return;
                    }
                } else {
                    return;
                }
            } else if (touchXY != null) {
                foundCellSpan = layout.findNearestVacantArea(touchXY[0], touchXY[1], 1, 1, cellXY) != null;
            } else {
                foundCellSpan = layout.findCellForSpan(cellXY, 1, 1);
            }
            if (!foundCellSpan) {
                showOutOfSpaceMessage();
                return;
            }
            Log.d(TAG, "completeAddShortcut call addItemToDatabase");
            LauncherModel.addItemToDatabase(this, info, container, screen, cellXY[0], cellXY[1], false);
            if (!this.mRestoring) {
                this.mWorkspace.addInScreen(view, container, screen, cellXY[0], cellXY[1], 1, 1, isWorkspaceLocked());
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public Rect getDefaultPaddingForWidget(Context context, ComponentName component, Rect rect) {
        if (VERSION.SDK_INT > 14) {
            Rect r = AppWidgetHostView.getDefaultPaddingForWidget(context, component, rect);
            Log.d(TAG, "!!!!get the rect=====" + r);
            return r;
        }
        Rect p = new Rect();
        Resources r2 = getResources();
        p.left = r2.getDimensionPixelSize(R.dimen.app_widget_padding_left);
        p.right = r2.getDimensionPixelSize(R.dimen.app_widget_padding_right);
        p.top = r2.getDimensionPixelSize(R.dimen.app_widget_padding_top);
        p.bottom = r2.getDimensionPixelSize(R.dimen.app_widget_padding_bottom);
        Log.d(TAG, "get the rect=====" + p);
        return p;
    }

    /* access modifiers changed from: 0000 */
    public int[] getSpanForWidget(ComponentName component, int minWidth, int minHeight, int[] spanXY) {
        if (spanXY == null) {
            int[] spanXY2 = new int[2];
        }
        Rect padding = getDefaultPaddingForWidget(this, component, null);
        return CellLayout.rectToCell(getResources(), padding.left + minWidth + padding.right, padding.top + minHeight + padding.bottom, null);
    }

    /* access modifiers changed from: 0000 */
    public int[] getSpanForWidget(AppWidgetProviderInfo info, int[] spanXY) {
        return getSpanForWidget(info.provider, info.minWidth, info.minHeight, spanXY);
    }

    /* access modifiers changed from: 0000 */
    public int[] getMinResizeSpanForWidget(AppWidgetProviderInfo info, int[] spanXY) {
        return getSpanForWidget(info.provider, info.minResizeWidth, info.minResizeHeight, spanXY);
    }

    /* access modifiers changed from: 0000 */
    public int[] getSpanForWidget(PendingAddWidgetInfo info, int[] spanXY) {
        return getSpanForWidget(info.componentName, info.minWidth, info.minHeight, spanXY);
    }

    private void completeAddAppWidget(int appWidgetId, long container, int screen) {
        boolean foundCellSpan;
        AppWidgetProviderInfo appWidgetInfo = this.mAppWidgetManager.getAppWidgetInfo(appWidgetId);
        CellLayout layout = getCellLayout(container, screen);
        if (!this.mWorkspace.isAllowAddItem(layout)) {
            showNotAllowedAddMessage();
            return;
        }
        int[] spanXY = getSpanForWidget(appWidgetInfo, (int[]) null);
        int[] cellXY = this.mTmpAddItemCellCoordinates;
        int[] touchXY = this.mPendingAddInfo.dropPos;
        if (this.mPendingAddInfo.cellX < 0 || this.mPendingAddInfo.cellY < 0) {
            foundCellSpan = touchXY != null ? layout.findNearestVacantArea(touchXY[0], touchXY[1], spanXY[0], spanXY[1], cellXY) != null : layout.findCellForSpan(cellXY, spanXY[0], spanXY[1]);
        } else {
            cellXY[0] = this.mPendingAddInfo.cellX;
            cellXY[1] = this.mPendingAddInfo.cellY;
            foundCellSpan = true;
        }
        if (!foundCellSpan) {
            if (appWidgetId != -1) {
                final int i = appWidgetId;
                new Thread("deleteAppWidgetId") {
                    public void run() {
                        Launcher.this.mAppWidgetHost.deleteAppWidgetId(i);
                    }
                }.start();
            }
            showOutOfSpaceMessage();
            return;
        }
        LauncherAppWidgetInfo launcherInfo = new LauncherAppWidgetInfo(appWidgetId);
        launcherInfo.spanX = spanXY[0];
        launcherInfo.spanY = spanXY[1];
        LauncherModel.addItemToDatabase(this, launcherInfo, container, screen, cellXY[0], cellXY[1], false);
        if (!this.mRestoring) {
            launcherInfo.hostView = this.mAppWidgetHost.createView(this, appWidgetId, appWidgetInfo);
            launcherInfo.hostView.setAppWidget(appWidgetId, appWidgetInfo);
            launcherInfo.hostView.setTag(launcherInfo);
            this.mWorkspace.addInScreen(launcherInfo.hostView, container, screen, cellXY[0], cellXY[1], launcherInfo.spanX, launcherInfo.spanY, isWorkspaceLocked());
            addWidgetToAutoAdvanceIfNeeded(launcherInfo.hostView, appWidgetInfo);
        }
    }

    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.SCREEN_OFF");
        filter.addAction("android.intent.action.SCREEN_ON");
        filter.addAction("android.intent.action.USER_PRESENT");
        filter.addAction("com.konka.launcher.action.SHOW_ALL_APP");
        filter.addAction("android.intent.action.USER_ADDED");
        filter.addAction("android.intent.action.USER_REMOVED");
        filter.addAction("com.konka.action.CHANGE_PASSWORD");
        filter.addAction("konka.action.SCREEN_UNLOCK");
        registerReceiver(this.mReceiver, filter);
        this.mAttached = true;
        this.mVisible = true;
    }

    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.mVisible = false;
        this.mDragLayer.clearAllResizeFrames();
        if (this.mAttached) {
            unregisterReceiver(this.mReceiver);
            this.mAttached = false;
        }
        updateRunning();
    }

    public void onWindowVisibilityChanged(int visibility) {
        this.mVisible = visibility == 0;
        updateRunning();
    }

    /* access modifiers changed from: private */
    public void sendAdvanceMessage(long delay) {
        this.mHandler.removeMessages(1);
        this.mHandler.sendMessageDelayed(this.mHandler.obtainMessage(1), delay);
        this.mAutoAdvanceSentTime = System.currentTimeMillis();
    }

    /* access modifiers changed from: private */
    public void updateRunning() {
        boolean autoAdvanceRunning;
        long delay = 20000;
        if (!this.mVisible || !this.mUserPresent || this.mWidgetsToAdvance.isEmpty()) {
            autoAdvanceRunning = false;
        } else {
            autoAdvanceRunning = true;
        }
        if (autoAdvanceRunning != this.mAutoAdvanceRunning) {
            this.mAutoAdvanceRunning = autoAdvanceRunning;
            if (autoAdvanceRunning) {
                if (this.mAutoAdvanceTimeLeft != -1) {
                    delay = this.mAutoAdvanceTimeLeft;
                }
                sendAdvanceMessage(delay);
                return;
            }
            if (!this.mWidgetsToAdvance.isEmpty()) {
                this.mAutoAdvanceTimeLeft = Math.max(0, 20000 - (System.currentTimeMillis() - this.mAutoAdvanceSentTime));
            }
            this.mHandler.removeMessages(1);
            this.mHandler.removeMessages(0);
        }
    }

    /* access modifiers changed from: 0000 */
    public void addWidgetToAutoAdvanceIfNeeded(View hostView, AppWidgetProviderInfo appWidgetInfo) {
        if (appWidgetInfo != null && appWidgetInfo.autoAdvanceViewId != -1) {
            View v = hostView.findViewById(appWidgetInfo.autoAdvanceViewId);
            if (v instanceof Advanceable) {
                this.mWidgetsToAdvance.put(hostView, appWidgetInfo);
                ((Advanceable) v).fyiWillBeAdvancedByHostKThx();
                updateRunning();
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public void removeWidgetToAutoAdvance(View hostView) {
        if (this.mWidgetsToAdvance.containsKey(hostView)) {
            this.mWidgetsToAdvance.remove(hostView);
            updateRunning();
        }
    }

    public void removeAppWidget(LauncherAppWidgetInfo launcherInfo) {
        removeWidgetToAutoAdvance(launcherInfo.hostView);
        launcherInfo.hostView = null;
    }

    /* access modifiers changed from: 0000 */
    public void showOutOfSpaceMessage() {
        Toast.makeText(this, getString(R.string.out_of_space), 0).show();
    }

    /* access modifiers changed from: 0000 */
    public void showNotAllowedAddMessage() {
        Toast.makeText(this, getString(R.string.not_allowed_add), 0).show();
    }

    /* access modifiers changed from: 0000 */
    public void warnTargetisLocked() {
        Toast.makeText(this, getString(R.string.target_is_locked), 0).show();
    }

    public LauncherAppWidgetHost getAppWidgetHost() {
        return this.mAppWidgetHost;
    }

    public LauncherModel getModel() {
        return this.mModel;
    }

    /* access modifiers changed from: 0000 */
    public void closeSystemDialogs() {
        getWindow().closeAllPanels();
        this.mWaitingForResult = false;
    }

    /* access modifiers changed from: protected */
    public void onNewIntent(Intent intent) {
        boolean z = true;
        super.onNewIntent(intent);
        Log.d(TAG, "====================onNewIntent");
        if ("android.intent.action.MAIN".equals(intent.getAction())) {
            closeSystemDialogs();
            boolean alreadyOnHome = (intent.getFlags() & 4194304) != 4194304;
            Folder openFolder = this.mWorkspace.getOpenFolder();
            this.mWorkspace.exitWidgetResizeMode();
            boolean isGoHomePage = intent.getBooleanExtra("goHomePage", false);
            Log.d(TAG, "====================isGoHomePage===" + isGoHomePage + "  The state===" + this.mState);
            closeFolder();
            exitSpringLoadedDragMode();
            if (!alreadyOnHome || isGoHomePage) {
                z = false;
            }
            showWorkspace(z);
            if ((alreadyOnHome || isGoHomePage) && !this.mWorkspace.isTouchActive() && openFolder == null && getCurrentWorkspaceScreen() != this.mWorkspace.getHomeScreenIndex()) {
                Log.d(TAG, "moveToDefaultScreen");
                this.mWorkspace.moveToDefaultScreen(false);
            }
            View v = getWindow().peekDecorView();
            if (!(v == null || v.getWindowToken() == null)) {
                ((InputMethodManager) getSystemService("input_method")).hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
            if (!alreadyOnHome && this.mAppsCustomizeTabHost != null) {
                this.mAppsCustomizeTabHost.reset();
            }
            this.mSearchDropTargetBar.closeAllWindows();
            this.mWorkspace.closeAllQuickActions();
        }
    }

    /* access modifiers changed from: protected */
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        this.mSavedInstanceState = savedInstanceState;
    }

    /* access modifiers changed from: protected */
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(RUNTIME_STATE_CURRENT_SCREEN, this.mWorkspace.getCurrentPage());
        super.onSaveInstanceState(outState);
        outState.putInt(RUNTIME_STATE, this.mState.ordinal());
        closeFolder();
        if (this.mPendingAddInfo.container != -1 && this.mPendingAddInfo.screen > -1 && this.mWaitingForResult) {
            outState.putLong(RUNTIME_STATE_PENDING_ADD_CONTAINER, this.mPendingAddInfo.container);
            outState.putInt(RUNTIME_STATE_PENDING_ADD_SCREEN, this.mPendingAddInfo.screen);
            outState.putInt(RUNTIME_STATE_PENDING_ADD_CELL_X, this.mPendingAddInfo.cellX);
            outState.putInt(RUNTIME_STATE_PENDING_ADD_CELL_Y, this.mPendingAddInfo.cellY);
        }
        if (this.mFolderInfo != null && this.mWaitingForResult) {
            outState.putBoolean(RUNTIME_STATE_PENDING_FOLDER_RENAME, true);
            outState.putLong(RUNTIME_STATE_PENDING_FOLDER_RENAME_ID, this.mFolderInfo.id);
        }
        if (this.mAppsCustomizeTabHost != null) {
            String currentTabTag = this.mAppsCustomizeTabHost.getCurrentTabTag();
            if (currentTabTag != null) {
                outState.putString("apps_customize_currentTab", currentTabTag);
            }
            outState.putInt("apps_customize_currentIndex", this.mAppsCustomizeContent.getSaveInstanceStateIndex());
        }
    }

    public void onDestroy() {
        super.onDestroy();
        this.mHandler.removeMessages(1);
        this.mHandler.removeMessages(0);
        this.mWorkspace.removeCallbacks(this.mBuildLayersRunnable);
        LauncherApplication app = (LauncherApplication) getApplication();
        this.mModel.stopLoader();
        app.setLauncher(null);
        try {
            this.mAppWidgetHost.stopListening();
        } catch (NullPointerException ex) {
            Log.w(TAG, "problem while stopping AppWidgetHost during Launcher destruction", ex);
        }
        this.mAppWidgetHost = null;
        this.mWidgetsToAdvance.clear();
        TextKeyListener.getInstance().release();
        unbindWorkspaceAndHotseatItems();
        getContentResolver().unregisterContentObserver(this.mWidgetObserver);
        unregisterReceiver(this.mCloseSystemDialogsReceiver);
        unregisterReceiver(this.mSystemUpgradeReceiver);
        unregisterReceiver(this.mMessageReceiver);
        unregisterReceiver(this.mMarketReceiver);
        ((ViewGroup) this.mWorkspace.getParent()).removeAllViews();
        this.mWorkspace.removeAllViews();
        this.mWorkspace = null;
        this.mDragController = null;
        ValueAnimator.clearAllAnimations();
    }

    public DragController getDragController() {
        return this.mDragController;
    }

    public void startActivityForResult(Intent intent, int requestCode) {
        if (requestCode >= 0) {
            this.mWaitingForResult = true;
        }
        super.startActivityForResult(intent, requestCode);
    }

    public void startSearch(String initialQuery, boolean selectInitialQuery, Bundle appSearchData, boolean globalSearch) {
        showWorkspace(true);
        if (initialQuery == null) {
            initialQuery = getTypedText();
        }
        if (appSearchData == null) {
            appSearchData = new Bundle();
            appSearchData.putString("source", "launcher-search");
        }
        String str = initialQuery;
        boolean z = selectInitialQuery;
        ((SearchManager) getSystemService("search")).startSearch(str, z, getComponentName(), appSearchData, globalSearch, this.mSearchDropTargetBar.getSearchBarBounds());
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        if (!getResources().getBoolean(R.bool.config_optionMenuEnable) || isWorkspaceLocked()) {
            return false;
        }
        super.onCreateOptionsMenu(menu);
        new Intent("android.settings.MANAGE_ALL_APPLICATIONS_SETTINGS").setFlags(276824064);
        Intent preferences = new Intent().setClass(this, Preferences.class);
        preferences.setFlags(276824064);
        Intent settings = new Intent("android.settings.SETTINGS");
        settings.setFlags(270532608);
        String helpUrl = getString(R.string.help_url);
        Intent help = new Intent("android.intent.action.VIEW", Uri.parse(helpUrl));
        help.setFlags(276824064);
        menu.add(1, 2, 0, R.string.menu_wallpaper).setIcon(17301567).setAlphabeticShortcut('W');
        menu.add(2, 4, 0, R.string.menu_market).setAlphabeticShortcut('A').setOnMenuItemClickListener(new OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                Launcher.this.onClickAppMarketButton(null);
                return true;
            }
        });
        if (!getResources().getBoolean(R.bool.config_cyanogenmod)) {
            menu.add(0, 5, 0, R.string.menu_preferences).setIcon(17301577).setIntent(preferences).setAlphabeticShortcut('O');
        }
        menu.add(0, 6, 0, R.string.menu_settings).setIcon(17301577).setIntent(settings).setAlphabeticShortcut('P');
        if (!helpUrl.isEmpty()) {
            menu.add(0, 7, 0, R.string.menu_help).setIcon(17301568).setIntent(help).setAlphabeticShortcut('H');
        }
        return true;
    }

    public boolean onPrepareOptionsMenu(Menu menu) {
        boolean allAppsVisible;
        boolean z;
        boolean z2 = false;
        super.onPrepareOptionsMenu(menu);
        if (this.mAppsCustomizeTabHost.isTransitioning()) {
            return false;
        }
        if (this.mAppsCustomizeTabHost.getVisibility() == 0) {
            allAppsVisible = true;
        } else {
            allAppsVisible = false;
        }
        if (allAppsVisible) {
            z = false;
        } else {
            z = true;
        }
        menu.setGroupVisible(1, z);
        if (allAppsVisible && !ViewConfiguration.get(this).hasPermanentMenuKey() && this.mAppMarketIntent != null) {
            z2 = true;
        }
        menu.setGroupVisible(2, z2);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 2:
                startWallpaper();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public boolean onSearchRequested() {
        return true;
    }

    public boolean isWorkspaceLocked() {
        return this.mWorkspaceLoading || this.mWaitingForResult;
    }

    private void resetAddInfo() {
        this.mPendingAddInfo.container = -1;
        this.mPendingAddInfo.screen = -1;
        ItemInfo itemInfo = this.mPendingAddInfo;
        this.mPendingAddInfo.cellY = -1;
        itemInfo.cellX = -1;
        ItemInfo itemInfo2 = this.mPendingAddInfo;
        this.mPendingAddInfo.spanY = -1;
        itemInfo2.spanX = -1;
        this.mPendingAddInfo.dropPos = null;
    }

    /* access modifiers changed from: 0000 */
    public void addAppWidgetFromPick(Intent data) {
        addAppWidgetImpl(data.getIntExtra("appWidgetId", -1), null);
    }

    /* access modifiers changed from: 0000 */
    public void addAppWidgetImpl(int appWidgetId, PendingAddWidgetInfo info) {
        AppWidgetProviderInfo appWidget = this.mAppWidgetManager.getAppWidgetInfo(appWidgetId);
        if (appWidget.configure != null) {
            Intent intent = new Intent("android.appwidget.action.APPWIDGET_CONFIGURE");
            intent.setComponent(appWidget.configure);
            intent.putExtra("appWidgetId", appWidgetId);
            if (info != null && info.mimeType != null && !info.mimeType.isEmpty()) {
                intent.putExtra(InstallWidgetReceiver.EXTRA_APPWIDGET_CONFIGURATION_DATA_MIME_TYPE, info.mimeType);
                String mimeType = info.mimeType;
                ClipData clipData = (ClipData) info.configurationData;
                ClipDescription clipDesc = clipData.getDescription();
                int i = 0;
                while (true) {
                    if (i >= clipDesc.getMimeTypeCount()) {
                        break;
                    } else if (clipDesc.getMimeType(i).equals(mimeType)) {
                        Item item = clipData.getItemAt(i);
                        CharSequence stringData = item.getText();
                        Uri uriData = item.getUri();
                        Intent intentData = item.getIntent();
                        String str = InstallWidgetReceiver.EXTRA_APPWIDGET_CONFIGURATION_DATA;
                        if (uriData != null) {
                            intent.putExtra(InstallWidgetReceiver.EXTRA_APPWIDGET_CONFIGURATION_DATA, uriData);
                        } else if (intentData != null) {
                            intent.putExtra(InstallWidgetReceiver.EXTRA_APPWIDGET_CONFIGURATION_DATA, intentData);
                        } else if (stringData != null) {
                            intent.putExtra(InstallWidgetReceiver.EXTRA_APPWIDGET_CONFIGURATION_DATA, stringData);
                        }
                    } else {
                        i++;
                    }
                }
            }
            startActivityForResultSafely(intent, 5);
            return;
        }
        completeAddAppWidget(appWidgetId, info.container, info.screen);
        exitSpringLoadedDragModeDelayed(true, false);
    }

    /* access modifiers changed from: 0000 */
    public void processShortcutFromDrop(ComponentName componentName, long container, int screen, int[] cell, int[] loc) {
        resetAddInfo();
        this.mPendingAddInfo.container = container;
        this.mPendingAddInfo.screen = screen;
        this.mPendingAddInfo.dropPos = loc;
        if (cell != null) {
            this.mPendingAddInfo.cellX = cell[0];
            this.mPendingAddInfo.cellY = cell[1];
        }
        Intent createShortcutIntent = new Intent("android.intent.action.CREATE_SHORTCUT");
        createShortcutIntent.setComponent(componentName);
        processShortcut(createShortcutIntent);
    }

    /* access modifiers changed from: 0000 */
    public void addAppWidgetFromDrop(PendingAddWidgetInfo info, long container, int screen, int[] cell, int[] loc) {
        resetAddInfo();
        ItemInfo itemInfo = this.mPendingAddInfo;
        info.container = container;
        itemInfo.container = container;
        ItemInfo itemInfo2 = this.mPendingAddInfo;
        info.screen = screen;
        itemInfo2.screen = screen;
        this.mPendingAddInfo.dropPos = loc;
        if (cell != null) {
            this.mPendingAddInfo.cellX = cell[0];
            this.mPendingAddInfo.cellY = cell[1];
        }
        int appWidgetId = getAppWidgetHost().allocateAppWidgetId();
        AppWidgetManager.getInstance(this).bindAppWidgetId(appWidgetId, info.componentName);
        addAppWidgetImpl(appWidgetId, info);
    }

    /* access modifiers changed from: 0000 */
    public void processShortcut(Intent intent) {
        String applicationName = getResources().getString(R.string.group_applications);
        String shortcutName = intent.getStringExtra("android.intent.extra.shortcut.NAME");
        if (applicationName == null || !applicationName.equals(shortcutName)) {
            startActivityForResultSafely(intent, 1);
            return;
        }
        Intent mainIntent = new Intent("android.intent.action.MAIN", null);
        mainIntent.addCategory("android.intent.category.LAUNCHER");
        Intent pickIntent = new Intent("android.intent.action.PICK_ACTIVITY");
        pickIntent.putExtra("android.intent.extra.INTENT", mainIntent);
        pickIntent.putExtra("android.intent.extra.TITLE", getText(R.string.title_select_application));
        startActivityForResultSafely(pickIntent, 6);
    }

    /* access modifiers changed from: 0000 */
    public void processWallpaper(Intent intent) {
        startActivityForResult(intent, 10);
    }

    /* access modifiers changed from: 0000 */
    public FolderIcon addFolder(CellLayout layout, long container, int screen, int cellX, int cellY) {
        FolderInfo folderInfo = new FolderInfo();
        folderInfo.title = getText(R.string.folder_name);
        LauncherModel.addItemToDatabase(this, folderInfo, container, screen, cellX, cellY, false);
        sFolders.put(Long.valueOf(folderInfo.id), folderInfo);
        FolderIcon newFolder = FolderIcon.fromXml(R.layout.folder_icon, this, layout, folderInfo, this.mIconCache);
        if (this.mHideIconLabels) {
            newFolder.setTextVisible(false);
        }
        this.mWorkspace.addInScreen(newFolder, container, screen, cellX, cellY, 1, 1, isWorkspaceLocked());
        return newFolder;
    }

    /* access modifiers changed from: 0000 */
    public void removeFolder(FolderInfo folder) {
        sFolders.remove(Long.valueOf(folder.id));
    }

    /* access modifiers changed from: private */
    public void startWallpaper() {
        showWorkspace(true);
        startActivityForResult(Intent.createChooser(new Intent("android.intent.action.SET_WALLPAPER"), getText(R.string.chooser_wallpaper)), 10);
    }

    private void bindConnectivityService() {
        bindService(new Intent(this, ConnectivityService.class), this.mConnectivityServiceConnection, 1);
    }

    public void onUserCenterConnected(UserInfo userInfo) {
        synchronized (this.mConnectivityServiceLock) {
            this.mUserInfo = userInfo;
            if (this.mConnectivityServiceBinder != null && !this.mUserCenterConnectCalledOnce) {
                this.mUserCenterConnectCalledOnce = true;
                this.mConnectivityServiceBinder.onUserCenterConnected(userInfo);
            }
        }
        if (this.mBlackListFilter == null) {
            this.mBlackListFilter = BlackListFilter.getInstance(getApplication());
        }
        this.mBlackListFilter.getBlackListFromServer(userInfo, this.mModel);
    }

    private void registerUpgradeInfo() {
        Log.d(TAG, "first run postRet=" + this.mSearchDropTargetBar.post(new Runnable() {
            public void run() {
                Launcher.this.mSearchDropTargetBar.onAppUpgradeChange(Launcher.this.queryUpgradeInfo());
            }
        }));
        registerReceiver(this.mSystemUpgradeReceiver, new IntentFilter("com.konka.upgrade.NEW_VERSION_AVAILABLE"));
    }

    /* access modifiers changed from: private */
    public int queryUpgradeInfo() {
        int upgraderCount = 0;
        String str = "upgradeType=?";
        Cursor upgradeCursor = getContentResolver().query(ToBeUpgraded.CONTENT_URI, null, "upgradeType=?", new String[]{Integer.toString(0)}, null);
        if (upgradeCursor != null) {
            upgraderCount = upgradeCursor.getCount();
        }
        if (upgradeCursor != null) {
            upgradeCursor.close();
        }
        Log.d(TAG, "upgradeInfo count=" + upgraderCount);
        return upgraderCount;
    }

    private void registerContentObservers() {
        ContentResolver resolver = getContentResolver();
        resolver.registerContentObserver(LauncherProvider.CONTENT_APPWIDGET_RESET_URI, true, this.mWidgetObserver);
        resolver.registerContentObserver(ToBeUpgraded.CONTENT_URI, true, this.mAppUpgraderObserver);
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == 0) {
            switch (event.getKeyCode()) {
                case 3:
                    return true;
                case 25:
                    if (SystemProperties.getInt("debug.launcher2.dumpstate", 0) != 0) {
                        dumpState();
                        return true;
                    }
                    break;
            }
        } else if (event.getAction() == 1) {
            switch (event.getKeyCode()) {
                case 3:
                    return true;
            }
        }
        return super.dispatchKeyEvent(event);
    }

    public void onBackPressed() {
        if (this.mState == State.APPS_CUSTOMIZE) {
            showWorkspace(true);
        } else if (this.mWorkspace.getOpenFolder() != null) {
            Folder openFolder = this.mWorkspace.getOpenFolder();
            if (openFolder.isEditingName()) {
                openFolder.dismissEditingName();
            } else {
                closeFolder();
            }
        } else {
            this.mWorkspace.exitWidgetResizeMode();
            if (getCurrentWorkspaceScreen() == this.mWorkspace.getHomeScreenIndex()) {
                Log.w(TAG, "the back press is not response in the home page!");
            } else {
                this.mWorkspace.showOutlinesTemporarily();
            }
        }
    }

    /* access modifiers changed from: private */
    public void onAppWidgetReset() {
        if (this.mAppWidgetHost != null) {
            this.mAppWidgetHost.startListening();
        }
    }

    private void unbindWorkspaceAndHotseatItems() {
        if (this.mModel != null) {
            this.mModel.unbindWorkspaceItems();
        }
    }

    public void onClick(View v) {
        if (v.getWindowToken() != null && !this.mWorkspace.isSwitchingState()) {
            Object tag = v.getTag();
            if (tag instanceof ShortcutInfo) {
                Intent intent = ((ShortcutInfo) tag).intent;
                int[] pos = new int[2];
                v.getLocationOnScreen(pos);
                intent.setSourceBounds(new Rect(pos[0], pos[1], pos[0] + v.getWidth(), pos[1] + v.getHeight()));
                if (((ShortcutInfo) tag).checkShortcutPackageName("com.konka.market.main") && MarketManager.getInstance().getMarketUgradeNumber() > 0) {
                    Bundle bundle = new Bundle();
                    bundle.putString("Goto", "Upgrade");
                    intent.putExtras(bundle);
                }
                boolean success = startActivitySafely(v, intent, tag);
                if (((ShortcutInfo) tag).checkShortcutPackageName("com.ktcp.video")) {
                    ReportHelper.reportApkClick(this, "com.ktcp.video", "com.ktcp.video", System.currentTimeMillis());
                } else if (((ShortcutInfo) tag).checkShortcutPackageName("com.ktcp.music")) {
                    ReportHelper.reportApkClick(this, "com.ktcp.music", "com.ktcp.music", System.currentTimeMillis());
                } else if (((ShortcutInfo) tag).checkShortcutPackageName("com.ktcp.game")) {
                    ReportHelper.reportApkClick(this, "com.ktcp.game", "com.ktcp.game", System.currentTimeMillis());
                } else if (((ShortcutInfo) tag).checkShortcutPackageName("com.ktcp.photo")) {
                    ReportHelper.reportApkClick(this, "com.ktcp.photo", "com.ktcp.photo", System.currentTimeMillis());
                }
                if (success && (v instanceof BubbleTextView)) {
                    this.mWaitingForResume = (BubbleTextView) v;
                    this.mWaitingForResume.setStayPressed(true);
                }
            } else if (tag instanceof FolderInfo) {
                int[] pos2 = new int[2];
                v.getLocationOnScreen(pos2);
                System.out.println("[wjx]" + v.getWidth());
                if (v instanceof FolderIcon) {
                    FolderIcon fi = (FolderIcon) v;
                    fi.mFolder.setFolderState(this.mFolderState);
                    handleFolderClick(fi, pos2);
                    this.mFolderState = Folder.STATE_TO_RUN;
                }
            } else if (v != this.mAllAppsButton) {
            } else {
                if (this.mState == State.APPS_CUSTOMIZE) {
                    showWorkspace(true);
                } else {
                    onClickAllAppsButton(v);
                }
            }
        }
    }

    public boolean onTouch(View v, MotionEvent event) {
        showWorkspace(true);
        return false;
    }

    public void onClickSearchButton(View v) {
        v.performHapticFeedback(1);
        onSearchRequested();
    }

    public void onClickVoiceButton(View v) {
        v.performHapticFeedback(1);
        Intent intent = new Intent("android.speech.action.WEB_SEARCH");
        intent.setFlags(276824064);
        startActivity(intent);
    }

    public void onClickAllAppsButton(View v) {
        showAllApps(true);
    }

    public Intent newIntentForStartActivity(ComponentName className) {
        Intent intent = new Intent("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.LAUNCHER");
        intent.setComponent(className);
        intent.setFlags(270532608);
        return intent;
    }

    public void onClickUserCenterButton(View v) {
        this.mSearchDropTargetBar.onClickUserCenterButton();
    }

    public void onClickMessageCenterButton(View v) {
        this.mSearchDropTargetBar.onClickMessageCenterButton();
    }

    public void onClickUserSettingsButton(View v) {
        startActivitySafely(v, newIntentForStartActivity(new ComponentName("com.konka.passport", "com.konka.passport.LoginCheckActivity")), "UserCenter");
        this.mSearchDropTargetBar.dismissUserCenterWindow();
    }

    public void onClickUserLogoutButton(View v) {
        if (this.mUserInfo != null) {
            try {
                this.mUserInfo.LogoutPassport();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        this.mSearchDropTargetBar.dismissUserCenterWindow();
    }

    public void onClickKKSearchButton(View v) {
        startActivity(new Intent(this, SearchActivity.class));
    }

    public void onClickSettingsButton(View v) {
        onClickSettingItem(v);
    }

    public void onClickThemeManagerButton(View v) {
        startActivitySafely(v, newIntentForStartActivity(new ComponentName("com.konka.thememanager", "com.konka.thememanager.main.ThememanagerActivity")), "ThemeManager");
    }

    public void onClickSettingItem(View v) {
        startActivitySafely(v, newIntentForStartActivity(new ComponentName("com.konka.systemsetting", "com.konka.systemsetting.MainActivity")), "Settings");
    }

    public void onClickNetworkStatusIndicator(View v) {
        this.mSearchDropTargetBar.onClickNetworkStatusIndicator(v);
    }

    public void onClickNetworkSettingsButton(View v) {
        this.mSearchDropTargetBar.onClickNetworkSettings();
    }

    public void onClickBluetoothButton(View v) {
        Intent mSysIntent = new Intent();
        mSysIntent.setAction("com.konka.systemsetting.action.MainActivity");
        mSysIntent.setClassName("com.konka.systemsetting", "com.konka.systemsetting.MainActivity");
        mSysIntent.addFlags(536870912);
        mSysIntent.putExtra("first", 1);
        mSysIntent.putExtra("second", 1);
        mSysIntent.putExtra("third", -1);
        mSysIntent.putExtra("from_help", true);
        startActivity(mSysIntent);
    }

    public void startTV() {
        startActivitySafely(null, newIntentForStartActivity(new ComponentName("com.konka.tvsettings", "com.konka.tvsettings.RootActivity")), "TV");
    }

    public void onClickTvDot(View v) {
        if (this.mDragLayer != null) {
            this.mDragLayer.snapToTVPage();
        }
    }

    public void onClickMutiUsersLock(View v) {
        new LockScreenDialog(this, R.style.LockScreenDialog).show();
    }

    public void onClickUsbStatusIndicator(View v) {
        this.mSearchDropTargetBar.onClickUsbButton();
    }

    public void onTouchDownAllAppsButton(View v) {
        v.performHapticFeedback(1);
    }

    public void onClickAppMarketButton(View v) {
        if (this.mAppMarketIntent != null) {
            startActivitySafely(v, this.mAppMarketIntent, "app market");
        }
    }

    public void onClickAppLatestButton(View v) {
        Log.d(TAG, "onClickAppLatestButton");
        showOrHideRecentAppsDialog(this, v.isInTouchMode());
    }

    private void showOrHideRecentAppsDialog(Context context, boolean isInTouchMode) {
        Log.d(TAG, "showOrHideRecentAppsDialog");
        Intent intent = new Intent("konka.action.SHOW_RECENT_TASK");
        intent.putExtra("isInTouchMode", isInTouchMode);
        sendBroadcastAsUser(intent, UserHandle.ALL);
    }

    public void onClickOverflowMenuButton(View v) {
        PopupMenu popupMenu = new PopupMenu(this, v);
        Menu menu = popupMenu.getMenu();
        onCreateOptionsMenu(menu);
        onPrepareOptionsMenu(menu);
        popupMenu.show();
    }

    public void onLongClickAppsTab(View v) {
        PopupMenu popupMenu = new PopupMenu(this, v);
        Menu menu = popupMenu.getMenu();
        dismissAllAppsSortCling(null);
        popupMenu.inflate(R.menu.apps_tab);
        switch ($SWITCH_TABLE$com$cyanogenmod$trebuchet$AppsCustomizeView$SortMode()[this.mAppsCustomizeContent.getSortMode().ordinal()]) {
            case 1:
            case 2:
                menu.findItem(R.id.apps_sort_title).setChecked(true);
                break;
            case 3:
                menu.findItem(R.id.apps_sort_install_date).setChecked(true);
                break;
        }
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.apps_sort_title /*2131558562*/:
                        Launcher.this.mAppsCustomizeContent.setSortMode(SortMode.Title);
                        break;
                    case R.id.apps_sort_install_date /*2131558563*/:
                        Launcher.this.mAppsCustomizeContent.setSortMode(SortMode.InstallDate);
                        break;
                }
                return true;
            }
        });
        popupMenu.show();
    }

    /* access modifiers changed from: 0000 */
    public void startApplicationDetailsActivity(ComponentName componentName) {
        Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS", Uri.fromParts(a.d, componentName.getPackageName(), null));
        intent.setFlags(276824064);
        startActivity(intent);
    }

    /* access modifiers changed from: 0000 */
    public void startApplicationUninstallActivity(ApplicationInfo appInfo) {
        if ((appInfo.flags & 1) == 0) {
            Toast.makeText(this, R.string.uninstall_system_app_text, 0).show();
            return;
        }
        Intent intent = new Intent("android.intent.action.DELETE", Uri.fromParts(a.d, appInfo.componentName.getPackageName(), appInfo.componentName.getClassName()));
        intent.setFlags(276824064);
        startActivity(intent);
    }

    /* access modifiers changed from: 0000 */
    public void startShortcutUninstallActivity(ShortcutInfo shortcutInfo) {
        if ((getPackageManager().resolveActivity(shortcutInfo.intent, 0).activityInfo.applicationInfo.flags & 1) != 0) {
            Toast.makeText(this, R.string.uninstall_system_app_text, 0).show();
        } else if (isItemLocked(shortcutInfo)) {
            warnTargetisLocked();
        } else {
            Intent intent = new Intent("android.intent.action.DELETE", Uri.fromParts(a.d, shortcutInfo.intent.getComponent().getPackageName(), shortcutInfo.intent.getComponent().getClassName()));
            intent.setFlags(276824064);
            startActivity(intent);
        }
    }

    public boolean isActivityExist(String pk_name, String activity_name) {
        Intent intent = new Intent();
        intent.setClassName(pk_name, activity_name);
        if (getPackageManager().resolveActivity(intent, 0) != null) {
            return true;
        }
        Log.v(TAG, "====not have activity :" + activity_name + " in package:" + pk_name);
        return false;
    }

    /* access modifiers changed from: 0000 */
    /* JADX WARNING: Removed duplicated region for block: B:11:0x0034 A[Catch:{ SecurityException -> 0x003f }] */
    /* JADX WARNING: Removed duplicated region for block: B:8:0x0014 A[Catch:{ SecurityException -> 0x003f }] */
    public boolean startActivity(View v, Intent intent, Object tag) {
        boolean useLaunchAnimation;
        intent.addFlags(268435456);
        if (v != null) {
            try {
                if (!intent.hasExtra(INTENT_EXTRA_IGNORE_LAUNCH_ANIMATION)) {
                    useLaunchAnimation = true;
                    if (!useLaunchAnimation) {
                        startActivity(intent, ActivityOptions.makeScaleUpAnimation(v, 0, 0, v.getMeasuredWidth(), v.getMeasuredHeight()).toBundle());
                        Log.v(TAG, "startactivity//////");
                    } else {
                        Log.v(TAG, "startactivity!!!!!!");
                        startActivity(intent);
                    }
                    return true;
                }
            } catch (SecurityException e) {
                Toast.makeText(this, R.string.activity_not_found, 0).show();
                Log.e(TAG, "Launcher does not have the permission to launch " + intent + ". Make sure to create a MAIN intent-filter for the corresponding activity " + "or use the exported attribute for this activity. " + "tag=" + tag + " intent=" + intent, e);
                Log.v(TAG, "startactivity.....");
                return false;
            }
        }
        useLaunchAnimation = false;
        if (!useLaunchAnimation) {
        }
        return true;
    }

    /* access modifiers changed from: 0000 */
    public boolean startActivitySafely(View v, Intent intent, Object tag) {
        boolean success = false;
        try {
            return startActivity(v, intent, tag);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, R.string.activity_not_found, 0).show();
            Log.e(TAG, "Unable to launch. tag=" + tag + " intent=" + intent, e);
            return success;
        }
    }

    /* access modifiers changed from: 0000 */
    public void startActivityForResultSafely(Intent intent, int requestCode) {
        try {
            startActivityForResult(intent, requestCode);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, R.string.activity_not_found, 0).show();
        } catch (SecurityException e2) {
            Toast.makeText(this, R.string.activity_not_found, 0).show();
            Log.e(TAG, "Launcher does not have the permission to launch " + intent + ". Make sure to create a MAIN intent-filter for the corresponding activity " + "or use the exported attribute for this activity.", e2);
        }
    }

    private void handleFolderClick(FolderIcon folderIcon, int[] pos) {
        FolderInfo info = folderIcon.mInfo;
        Folder openFolder = this.mWorkspace.getFolderForTag(info);
        if (info.opened && openFolder == null) {
            Log.d(TAG, "Folder info marked as open, but associated folder is not open. Screen: " + info.screen + " (" + info.cellX + ", " + info.cellY + ")");
            info.opened = false;
        }
        if (!info.opened) {
            closeFolder();
            if (info.contents.size() > 0) {
                openFolder(folderIcon, pos);
            } else {
                Toast.makeText(this, getResources().getString(R.string.folder_empty), 0).show();
            }
        } else if (openFolder != null) {
            int folderScreen = this.mWorkspace.getPageForView(openFolder);
            closeFolder(openFolder);
            if (folderScreen != this.mWorkspace.getCurrentPage()) {
                closeFolder();
                openFolder(folderIcon, pos);
            }
        }
    }

    private void growAndFadeOutFolderIcon(FolderIcon fi) {
        if (fi != null) {
            PropertyValuesHolder alpha = PropertyValuesHolder.ofFloat("alpha", new float[]{0.0f});
            PropertyValuesHolder scaleX = PropertyValuesHolder.ofFloat("scaleX", new float[]{1.5f});
            PropertyValuesHolder scaleY = PropertyValuesHolder.ofFloat("scaleY", new float[]{1.5f});
            if (((FolderInfo) fi.getTag()).container == -101) {
                LayoutParams lp = (LayoutParams) fi.getLayoutParams();
                ((CellLayout) fi.getParent().getParent()).setFolderLeaveBehindCell(lp.cellX, lp.cellY);
            }
            ObjectAnimator oa = ObjectAnimator.ofPropertyValuesHolder(fi, new PropertyValuesHolder[]{alpha, scaleX, scaleY});
            oa.setDuration((long) getResources().getInteger(R.integer.config_folderAnimDuration));
            oa.start();
        }
    }

    private void shrinkAndFadeInFolderIcon(FolderIcon fi) {
        if (fi != null) {
            PropertyValuesHolder alpha = PropertyValuesHolder.ofFloat("alpha", new float[]{1.0f});
            PropertyValuesHolder scaleX = PropertyValuesHolder.ofFloat("scaleX", new float[]{1.0f});
            PropertyValuesHolder scaleY = PropertyValuesHolder.ofFloat("scaleY", new float[]{1.0f});
            CellLayout cl = null;
            if (((FolderInfo) fi.getTag()).container == -101) {
                cl = (CellLayout) fi.getParent().getParent();
            }
            final CellLayout layout = cl;
            ObjectAnimator oa = ObjectAnimator.ofPropertyValuesHolder(fi, new PropertyValuesHolder[]{alpha, scaleX, scaleY});
            oa.setDuration((long) getResources().getInteger(R.integer.config_folderAnimDuration));
            oa.addListener(new AnimatorListenerAdapter() {
                public void onAnimationEnd(Animator animation) {
                    if (layout != null) {
                        layout.clearFolderLeaveBehind();
                    }
                }
            });
            oa.start();
        }
    }

    public void openFolder(FolderIcon folderIcon, int[] pos) {
        Folder folder = folderIcon.mFolder;
        folder.mWorkspace = this.mWorkspace;
        folder.mInfo.opened = true;
        if (folder.getParent() == null) {
            this.mDragLayer.addView(folder);
            this.mDragController.addDropTarget(folder);
        } else {
            Log.w(TAG, "Opening folder (" + folder + ") which already has a parent (" + folder.getParent() + ").");
        }
        folder.animateOpen(folder, pos);
    }

    public void closeFolder() {
        Folder folder = this.mWorkspace.getOpenFolder();
        if (folder != null) {
            if (folder.isEditingName()) {
                folder.dismissEditingName();
            }
            closeFolder(folder);
            dismissFolderCling(null);
        }
    }

    /* access modifiers changed from: 0000 */
    public void closeFolder(Folder folder) {
        folder.getInfo().opened = false;
        if (((ViewGroup) folder.getParent().getParent()) != null) {
            shrinkAndFadeInFolderIcon((FolderIcon) this.mWorkspace.getViewForTag(folder.mInfo));
        }
        folder.animateClosed();
    }

    public boolean isItemLocked(ItemInfo item) {
        return item != null && item.isLock && this.mEnforceLockItemPolicy;
    }

    public boolean onLongClick(View v) {
        boolean allowLongPress;
        if (this.mState != State.WORKSPACE || isWorkspaceLocked()) {
            return false;
        }
        if (!(v instanceof CellLayout)) {
            v = (View) v.getParent().getParent();
        }
        resetAddInfo();
        CellInfo longClickCellInfo = (CellInfo) v.getTag();
        if (longClickCellInfo == null) {
            return true;
        }
        View itemUnderLongClick = longClickCellInfo.cell;
        if (isHotseatLayout(v) || this.mWorkspace.allowLongPress()) {
            allowLongPress = true;
        } else {
            allowLongPress = false;
        }
        if (allowLongPress && !this.mDragController.isDragging() && itemUnderLongClick != null && !(itemUnderLongClick instanceof Folder) && !isItemLocked((ItemInfo) itemUnderLongClick.getTag())) {
            this.mWorkspace.startDrag(longClickCellInfo);
        }
        return true;
    }

    /* access modifiers changed from: 0000 */
    public boolean isHotseatLayout(View layout) {
        return this.mHotseat != null && layout != null && (layout instanceof CellLayout) && layout == this.mHotseat.getLayout();
    }

    /* access modifiers changed from: 0000 */
    public Hotseat getHotseat() {
        return this.mHotseat;
    }

    /* access modifiers changed from: 0000 */
    public SearchDropTargetBar getSearchBar() {
        return this.mSearchDropTargetBar;
    }

    /* access modifiers changed from: 0000 */
    public CellLayout getCellLayout(long container, int screen) {
        if (container != -101) {
            return (CellLayout) this.mWorkspace.getChildAt(screen);
        }
        if (this.mHotseat != null) {
            return this.mHotseat.getLayout();
        }
        return null;
    }

    /* access modifiers changed from: 0000 */
    public Workspace getWorkspace() {
        return this.mWorkspace;
    }

    /* access modifiers changed from: protected */
    public Dialog onCreateDialog(int id) {
        switch (id) {
            case 1:
                return new CreateShortcut(this, null).createDialog();
            case 2:
                return new RenameFolder(this, null).createDialog();
            default:
                return super.onCreateDialog(id);
        }
    }

    /* access modifiers changed from: protected */
    public void onPrepareDialog(int id, Dialog dialog) {
        switch (id) {
            case 2:
                if (this.mFolderInfo != null) {
                    EditText input = (EditText) dialog.findViewById(R.id.folder_name);
                    CharSequence text = this.mFolderInfo.title;
                    input.setText(text);
                    input.setSelection(0, text.length());
                    return;
                }
                return;
            default:
                return;
        }
    }

    /* access modifiers changed from: 0000 */
    public void showRenameDialog(FolderInfo info) {
        this.mFolderInfo = info;
        this.mWaitingForResult = true;
        showDialog(2);
    }

    private void showAddDialog() {
        resetAddInfo();
        this.mPendingAddInfo.container = -100;
        this.mPendingAddInfo.screen = this.mWorkspace.getCurrentPage();
        this.mWaitingForResult = true;
        showDialog(1);
    }

    public boolean isWorkspaceVisible() {
        return this.mState == State.WORKSPACE;
    }

    public boolean isAllAppsVisible() {
        return this.mState == State.APPS_CUSTOMIZE;
    }

    private void setPivotsForZoom(View view, float scaleFactor) {
        view.setPivotX(((float) view.getWidth()) / 2.0f);
        view.setPivotY(((float) view.getHeight()) / 2.0f);
    }

    /* access modifiers changed from: 0000 */
    public void updateWallpaperVisibility(boolean visible) {
        int wpflags = visible ? 1048576 : 0;
        if (wpflags != (getWindow().getAttributes().flags & 1048576)) {
            getWindow().setFlags(wpflags, 1048576);
        }
    }

    private void showAppsCustomizeHelper(boolean animated, boolean springLoaded) {
        if (this.mStateAnimation != null) {
            this.mStateAnimation.cancel();
            this.mStateAnimation = null;
        }
        Resources res = getResources();
        int duration = res.getInteger(R.integer.config_appsCustomizeZoomInTime);
        int fadeDuration = res.getInteger(R.integer.config_appsCustomizeFadeInTime);
        final float scale = (float) res.getInteger(R.integer.config_appsCustomizeZoomScaleFactor);
        final AppsCustomizeTabHost appsCustomizeTabHost = this.mAppsCustomizeTabHost;
        int startDelay = res.getInteger(R.integer.config_workspaceAppsCustomizeAnimationStagger);
        setPivotsForZoom(appsCustomizeTabHost, scale);
        TransitionEffect transitionEffect = this.mWorkspace.getTransitionEffect();
        if ((transitionEffect == TransitionEffect.RotateUp || transitionEffect == TransitionEffect.RotateDown) ? false : true) {
            this.mWorkspace.changeState(State.SMALL, animated);
        }
        if (animated) {
            final ValueAnimator scaleAnim = ValueAnimator.ofFloat(new float[]{FlyingIcon.ANGULAR_VMIN, 1.0f}).setDuration((long) duration);
            scaleAnim.setInterpolator(new ZoomOutInterpolator());
            scaleAnim.addUpdateListener(new LauncherAnimatorUpdateListener() {
                public void onAnimationUpdate(float a, float b) {
                    appsCustomizeTabHost.setScaleX((scale * a) + (b * 1.0f));
                    appsCustomizeTabHost.setScaleY((scale * a) + (b * 1.0f));
                }
            });
            appsCustomizeTabHost.setVisibility(0);
            appsCustomizeTabHost.setAlpha(FlyingIcon.ANGULAR_VMIN);
            ValueAnimator alphaAnim = ValueAnimator.ofFloat(new float[]{FlyingIcon.ANGULAR_VMIN, 1.0f}).setDuration((long) fadeDuration);
            alphaAnim.setInterpolator(new DecelerateInterpolator(1.5f));
            alphaAnim.addUpdateListener(new LauncherAnimatorUpdateListener() {
                public void onAnimationUpdate(float a, float b) {
                    appsCustomizeTabHost.setAlpha((FlyingIcon.ANGULAR_VMIN * a) + (1.0f * b));
                }
            });
            alphaAnim.setStartDelay((long) startDelay);
            alphaAnim.start();
            final boolean z = springLoaded;
            scaleAnim.addListener(new AnimatorListenerAdapter() {
                boolean animationCancelled = false;

                public void onAnimationStart(Animator animation) {
                    Launcher.this.updateWallpaperVisibility(true);
                    appsCustomizeTabHost.setTranslationX(FlyingIcon.ANGULAR_VMIN);
                    appsCustomizeTabHost.setTranslationY(FlyingIcon.ANGULAR_VMIN);
                    appsCustomizeTabHost.setVisibility(0);
                    appsCustomizeTabHost.bringToFront();
                }

                public void onAnimationEnd(Animator animation) {
                    appsCustomizeTabHost.setScaleX(1.0f);
                    appsCustomizeTabHost.setScaleY(1.0f);
                    if (appsCustomizeTabHost instanceof LauncherTransitionable) {
                        ((LauncherTransitionable) appsCustomizeTabHost).onLauncherTransitionEnd(this, scaleAnim, false);
                    }
                    if (!z && !LauncherApplication.isScreenLarge()) {
                        Launcher.this.mWorkspace.hideScrollingIndicator(true);
                        Launcher.this.hideDockDivider();
                    }
                    if (!this.animationCancelled) {
                        Launcher.this.updateWallpaperVisibility(false);
                    }
                }

                public void onAnimationCancel(Animator animation) {
                    this.animationCancelled = true;
                }
            });
            this.mStateAnimation = new AnimatorSet();
            this.mStateAnimation.play(scaleAnim).after((long) startDelay);
            boolean delayAnim = false;
            if (appsCustomizeTabHost instanceof LauncherTransitionable) {
                delayAnim = appsCustomizeTabHost.onLauncherTransitionStart(this, this.mStateAnimation, false);
            }
            if (!delayAnim) {
                this.mStateAnimation.start();
                return;
            }
            return;
        }
        appsCustomizeTabHost.setTranslationX(FlyingIcon.ANGULAR_VMIN);
        appsCustomizeTabHost.setTranslationY(FlyingIcon.ANGULAR_VMIN);
        appsCustomizeTabHost.setScaleX(1.0f);
        appsCustomizeTabHost.setScaleY(1.0f);
        appsCustomizeTabHost.setVisibility(0);
        appsCustomizeTabHost.bringToFront();
        if (appsCustomizeTabHost instanceof LauncherTransitionable) {
            appsCustomizeTabHost.onLauncherTransitionStart(this, null, false);
            appsCustomizeTabHost.onLauncherTransitionEnd(this, null, false);
            if (!springLoaded && !LauncherApplication.isScreenLarge()) {
                this.mWorkspace.hideScrollingIndicator(true);
                hideDockDivider();
            }
        }
        updateWallpaperVisibility(false);
    }

    private void hideAppsCustomizeHelper(boolean animated, boolean springLoaded) {
        if (this.mStateAnimation != null) {
            this.mStateAnimation.cancel();
            this.mStateAnimation = null;
        }
        Resources res = getResources();
        int duration = res.getInteger(R.integer.config_appsCustomizeZoomOutTime);
        final float scaleFactor = (float) res.getInteger(R.integer.config_appsCustomizeZoomScaleFactor);
        final AppsCustomizeTabHost appsCustomizeTabHost = this.mAppsCustomizeTabHost;
        setPivotsForZoom(appsCustomizeTabHost, scaleFactor);
        updateWallpaperVisibility(true);
        showHotseat(animated);
        if (animated) {
            final float oldScaleX = appsCustomizeTabHost.getScaleX();
            final float oldScaleY = appsCustomizeTabHost.getScaleY();
            ValueAnimator scaleAnim = ValueAnimator.ofFloat(new float[]{FlyingIcon.ANGULAR_VMIN, 1.0f}).setDuration((long) duration);
            scaleAnim.setInterpolator(new ZoomInInterpolator());
            scaleAnim.addUpdateListener(new LauncherAnimatorUpdateListener() {
                public void onAnimationUpdate(float a, float b) {
                    appsCustomizeTabHost.setScaleX((oldScaleX * a) + (scaleFactor * b));
                    appsCustomizeTabHost.setScaleY((oldScaleY * a) + (scaleFactor * b));
                }
            });
            final ValueAnimator alphaAnim = ValueAnimator.ofFloat(new float[]{FlyingIcon.ANGULAR_VMIN, 1.0f});
            alphaAnim.setDuration((long) res.getInteger(R.integer.config_appsCustomizeFadeOutTime));
            alphaAnim.setInterpolator(new AccelerateDecelerateInterpolator());
            alphaAnim.addUpdateListener(new LauncherAnimatorUpdateListener() {
                public void onAnimationUpdate(float a, float b) {
                    appsCustomizeTabHost.setAlpha((1.0f * a) + (FlyingIcon.ANGULAR_VMIN * b));
                }
            });
            if (appsCustomizeTabHost instanceof LauncherTransitionable) {
                appsCustomizeTabHost.onLauncherTransitionStart(this, alphaAnim, true);
            }
            alphaAnim.addListener(new AnimatorListenerAdapter() {
                public void onAnimationEnd(Animator animation) {
                    Launcher.this.updateWallpaperVisibility(true);
                    appsCustomizeTabHost.setVisibility(8);
                    if (appsCustomizeTabHost instanceof LauncherTransitionable) {
                        ((LauncherTransitionable) appsCustomizeTabHost).onLauncherTransitionEnd(this, alphaAnim, true);
                    }
                }
            });
            this.mStateAnimation = new AnimatorSet();
            this.mStateAnimation.playTogether(new Animator[]{scaleAnim, alphaAnim});
            this.mStateAnimation.start();
            return;
        }
        appsCustomizeTabHost.setVisibility(8);
        if (appsCustomizeTabHost instanceof LauncherTransitionable) {
            appsCustomizeTabHost.onLauncherTransitionStart(this, null, true);
            appsCustomizeTabHost.onLauncherTransitionEnd(this, null, true);
        }
        this.mWorkspace.hideScrollingIndicator(false);
    }

    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        if (level == 20) {
            this.mAppsCustomizeTabHost.onTrimMemory();
        }
    }

    /* access modifiers changed from: 0000 */
    public void showWorkspace(boolean animated) {
        boolean animateWorkspace;
        boolean z = false;
        int stagger = getResources().getInteger(R.integer.config_appsCustomizeWorkspaceAnimationStagger);
        TransitionEffect transitionEffect = this.mWorkspace.getTransitionEffect();
        if ((transitionEffect == TransitionEffect.RotateUp || transitionEffect == TransitionEffect.RotateDown) && this.mWorkspace.getState() == State.SMALL) {
            animateWorkspace = false;
        } else {
            animateWorkspace = true;
        }
        if (animateWorkspace) {
            this.mWorkspace.changeState(State.NORMAL, animated, stagger);
        }
        if (this.mState != State.WORKSPACE) {
            this.mWorkspace.setVisibility(0);
            hideAppsCustomizeHelper(animated, false);
            this.mSearchDropTargetBar.showSearchBar(animated);
            if (animated && this.mState == State.APPS_CUSTOMIZE_SPRING_LOADED) {
                z = true;
            }
            showDockDivider(z);
            if (this.mAllAppsButton != null) {
                this.mAllAppsButton.requestFocus();
            }
        }
        this.mWorkspace.flashScrollingIndicator(animated);
        this.mState = State.WORKSPACE;
        this.mUserPresent = true;
        updateRunning();
        getWindow().getDecorView().sendAccessibilityEvent(4);
        Log.d(TAG, "showWorkspace=========mState===" + this.mState);
    }

    /* access modifiers changed from: 0000 */
    public void showAllApps(boolean animated) {
        if (this.mState == State.WORKSPACE) {
            DisplayMetrics dm = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(dm);
            Log.e("xshj_test", "density = " + dm.density);
            Log.e("xshj_test", "densityDpi = " + dm.densityDpi);
            Log.e("xshj_test", "widthPixels = " + dm.widthPixels);
            Log.e("xshj_test", "heightPixels = " + dm.heightPixels);
            Log.d(TAG, "HIDE_TV_WINDOW======showAllApps");
            showAppsCustomizeHelper(animated, false);
            this.mSearchDropTargetBar.hideSearchBar(animated);
            this.mState = State.APPS_CUSTOMIZE;
            this.mUserPresent = false;
            updateRunning();
            closeFolder();
            getWindow().getDecorView().sendAccessibilityEvent(4);
        }
    }

    /* access modifiers changed from: 0000 */
    public void enterSpringLoadedDragMode() {
        if (this.mState == State.APPS_CUSTOMIZE) {
            this.mWorkspace.changeState(State.SPRING_LOADED);
            hideAppsCustomizeHelper(true, true);
            hideDockDivider();
            this.mState = State.APPS_CUSTOMIZE_SPRING_LOADED;
        }
    }

    /* access modifiers changed from: 0000 */
    public void exitSpringLoadedDragModeDelayed(final boolean successfulDrop, boolean extendedDelay) {
        int i;
        if (this.mState == State.APPS_CUSTOMIZE_SPRING_LOADED) {
            Handler handler = this.mHandler;
            AnonymousClass23 r2 = new Runnable() {
                public void run() {
                    if (successfulDrop) {
                        Launcher.this.mAppsCustomizeTabHost.setVisibility(8);
                        Launcher.this.mSearchDropTargetBar.showSearchBar(true);
                        Launcher.this.showWorkspace(true);
                        return;
                    }
                    Launcher.this.exitSpringLoadedDragMode();
                }
            };
            if (extendedDelay) {
                i = EXIT_SPRINGLOADED_MODE_LONG_TIMEOUT;
            } else {
                i = 300;
            }
            handler.postDelayed(r2, (long) i);
        }
    }

    /* access modifiers changed from: 0000 */
    public void exitSpringLoadedDragMode() {
        if (this.mState == State.APPS_CUSTOMIZE_SPRING_LOADED) {
            showAppsCustomizeHelper(true, true);
            this.mState = State.APPS_CUSTOMIZE;
        }
    }

    /* access modifiers changed from: 0000 */
    public void hideDockDivider() {
        if (this.mQsbDivider != null && this.mDockDivider != null) {
            if (this.mShowSearchBar) {
                this.mQsbDivider.setVisibility(4);
            }
            if (this.mShowDockDivider) {
                this.mDockDivider.setVisibility(4);
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public void showDockDivider(boolean animated) {
        if (this.mQsbDivider != null && this.mDockDivider != null) {
            if (this.mShowSearchBar) {
                this.mQsbDivider.setVisibility(0);
            }
            if (this.mShowDockDivider) {
                this.mDockDivider.setVisibility(0);
            }
            if (this.mDividerAnimator != null) {
                this.mDividerAnimator.cancel();
                if (this.mShowSearchBar) {
                    this.mQsbDivider.setAlpha(1.0f);
                }
                this.mDockDivider.setAlpha(1.0f);
                this.mDividerAnimator = null;
            }
            if (animated) {
                this.mDividerAnimator = new AnimatorSet();
                if (this.mShowSearchBar && this.mShowDockDivider) {
                    this.mDividerAnimator.playTogether(new Animator[]{ObjectAnimator.ofFloat(this.mQsbDivider, "alpha", new float[]{1.0f}), ObjectAnimator.ofFloat(this.mDockDivider, "alpha", new float[]{1.0f})});
                } else if (this.mShowSearchBar) {
                    this.mDividerAnimator.play(ObjectAnimator.ofFloat(this.mQsbDivider, "alpha", new float[]{1.0f}));
                } else if (this.mShowDockDivider) {
                    this.mDividerAnimator.play(ObjectAnimator.ofFloat(this.mDockDivider, "alpha", new float[]{1.0f}));
                }
                this.mDividerAnimator.setDuration((long) this.mSearchDropTargetBar.getTransitionInDuration());
                this.mDividerAnimator.start();
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public void lockAllApps() {
    }

    /* access modifiers changed from: 0000 */
    public void unlockAllApps() {
    }

    public boolean isAllAppsCustomizeOpen() {
        return this.mState == State.APPS_CUSTOMIZE;
    }

    /* access modifiers changed from: 0000 */
    public void showHotseat(boolean animated) {
        if (LauncherApplication.isScreenLarge()) {
            return;
        }
        if (animated) {
            this.mHotseat.animate().alpha(1.0f).setDuration((long) this.mSearchDropTargetBar.getTransitionInDuration());
            return;
        }
        this.mHotseat.setAlpha(1.0f);
    }

    /* access modifiers changed from: 0000 */
    public void hideHotseat(boolean animated) {
        if (LauncherApplication.isScreenLarge()) {
            return;
        }
        if (animated) {
            this.mHotseat.animate().alpha(FlyingIcon.ANGULAR_VMIN).setDuration((long) this.mSearchDropTargetBar.getTransitionOutDuration());
            return;
        }
        this.mHotseat.setAlpha(FlyingIcon.ANGULAR_VMIN);
    }

    /* access modifiers changed from: 0000 */
    public void addExternalItemToScreen(ItemInfo itemInfo, CellLayout layout) {
        if (this.mWorkspace != null) {
            this.mWorkspace.addExternalItemToScreen(itemInfo, layout);
        }
    }

    /* access modifiers changed from: 0000 */
    public void addExternalItems(ArrayList<? extends ItemInfo> items, boolean willSnapToPageWhenDone) {
        if (this.mWorkspace != null) {
            this.mWorkspace.addExternalItems(items, willSnapToPageWhenDone);
        }
    }

    /* access modifiers changed from: 0000 */
    public void addExternalItemsStartingFromPage(ArrayList<? extends ItemInfo> items, int startPage, boolean willSnapToPageWhenDone) {
        if (this.mWorkspace != null) {
            this.mWorkspace.addExternalItemsStartingFromPage(items, startPage, willSnapToPageWhenDone);
        }
    }

    public int getCurrentOrientation() {
        return getResources().getConfiguration().orientation;
    }

    private int getCurrentOrientationIndexForGlobalIcons() {
        switch (getCurrentOrientation()) {
            case 2:
                return 1;
            default:
                return 0;
        }
    }

    private Drawable getExternalPackageToolbarIcon(ComponentName activityName) {
        try {
            PackageManager packageManager = getPackageManager();
            Bundle metaData = packageManager.getActivityInfo(activityName, 128).metaData;
            if (metaData != null) {
                int iconResId = metaData.getInt(TOOLBAR_ICON_METADATA_NAME);
                if (iconResId != 0) {
                    return packageManager.getResourcesForActivity(activityName).getDrawable(iconResId);
                }
            }
        } catch (NameNotFoundException e) {
            Log.w(TAG, "Failed to load toolbar icon; " + activityName.flattenToShortString() + " not found", e);
        } catch (NotFoundException nfe) {
            Log.w(TAG, "Failed to load toolbar icon from " + activityName.flattenToShortString(), nfe);
        }
        return null;
    }

    private ConstantState updateTextButtonWithIconFromExternalActivity(int buttonId, ComponentName activityName, int fallbackDrawableId) {
        Drawable toolbarIcon = getExternalPackageToolbarIcon(activityName);
        Resources r = getResources();
        int w = r.getDimensionPixelSize(R.dimen.toolbar_external_icon_width);
        int h = r.getDimensionPixelSize(R.dimen.toolbar_external_icon_height);
        TextView button = (TextView) findViewById(buttonId);
        if (toolbarIcon == null) {
            Log.d(TAG, "toolbarIcon is null");
            Drawable toolbarIcon2 = r.getDrawable(fallbackDrawableId);
            toolbarIcon2.setBounds(0, 0, w, h);
            if (button == null) {
                return null;
            }
            button.setCompoundDrawables(toolbarIcon2, null, null, null);
            return null;
        }
        Log.d(TAG, "toolbarIcon is not null");
        toolbarIcon.setBounds(0, 0, w, h);
        if (button != null) {
            button.setCompoundDrawables(toolbarIcon, null, null, null);
        }
        return toolbarIcon.getConstantState();
    }

    private ConstantState updateButtonWithIconFromExternalActivity(int buttonId, ComponentName activityName, int fallbackDrawableId) {
        ImageView button = (ImageView) findViewById(buttonId);
        Drawable toolbarIcon = getExternalPackageToolbarIcon(activityName);
        if (button != null) {
            if (toolbarIcon == null) {
                button.setImageResource(fallbackDrawableId);
            } else {
                button.setImageDrawable(toolbarIcon);
            }
        }
        if (toolbarIcon != null) {
            return toolbarIcon.getConstantState();
        }
        return null;
    }

    private void updateTextButtonWithDrawable(int buttonId, ConstantState d) {
        ((TextView) findViewById(buttonId)).setCompoundDrawables(d.newDrawable(getResources()), null, null, null);
    }

    private void updateButtonWithDrawable(int buttonId, ConstantState d) {
        ((ImageView) findViewById(buttonId)).setImageDrawable(d.newDrawable(getResources()));
    }

    private void invalidatePressedFocusedStates(View container, View button) {
        if (container instanceof HolographicLinearLayout) {
            ((HolographicLinearLayout) container).invalidatePressedFocusedStates();
        } else if (button instanceof HolographicImageView) {
            ((HolographicImageView) button).invalidatePressedFocusedStates();
        }
    }

    private boolean updateGlobalSearchIcon() {
        View searchButtonContainer = findViewById(R.id.search_button_container);
        ImageView searchButton = (ImageView) findViewById(R.id.search_button);
        View searchDivider = findViewById(R.id.search_divider);
        View voiceButtonContainer = findViewById(R.id.voice_button_container);
        View voiceButton = findViewById(R.id.voice_button);
        ComponentName activityName = ((SearchManager) getSystemService("search")).getGlobalSearchActivity();
        if (activityName != null) {
            sGlobalSearchIcon[getCurrentOrientationIndexForGlobalIcons()] = updateButtonWithIconFromExternalActivity(R.id.search_button, activityName, R.drawable.ic_home_search_normal_holo);
            if (searchDivider != null) {
                searchDivider.setVisibility(0);
            }
            if (searchButtonContainer != null) {
                searchButtonContainer.setVisibility(0);
            }
            searchButton.setVisibility(0);
            invalidatePressedFocusedStates(searchButtonContainer, searchButton);
            return true;
        }
        if (searchDivider != null) {
            searchDivider.setVisibility(8);
        }
        if (searchButtonContainer != null) {
            searchButtonContainer.setVisibility(8);
        }
        if (voiceButtonContainer != null) {
            voiceButtonContainer.setVisibility(8);
        }
        searchButton.setVisibility(8);
        voiceButton.setVisibility(8);
        return false;
    }

    private void updateGlobalSearchIcon(ConstantState d) {
        View searchButtonContainer = findViewById(R.id.search_button_container);
        View searchButton = findViewById(R.id.search_button);
        updateButtonWithDrawable(R.id.search_button, d);
        invalidatePressedFocusedStates(searchButtonContainer, searchButton);
    }

    private boolean updateVoiceSearchIcon(boolean searchVisible) {
        View searchDivider = findViewById(R.id.search_divider);
        View voiceButtonContainer = findViewById(R.id.voice_button_container);
        View voiceButton = findViewById(R.id.voice_button);
        ComponentName activityName = new Intent("android.speech.action.WEB_SEARCH").resolveActivity(getPackageManager());
        if (!searchVisible || activityName == null) {
            if (searchDivider != null) {
                searchDivider.setVisibility(8);
            }
            if (voiceButtonContainer != null) {
                voiceButtonContainer.setVisibility(8);
            }
            voiceButton.setVisibility(8);
            return false;
        }
        sVoiceSearchIcon[getCurrentOrientationIndexForGlobalIcons()] = updateButtonWithIconFromExternalActivity(R.id.voice_button, activityName, R.drawable.ic_home_voice_search_holo);
        if (searchDivider != null) {
            searchDivider.setVisibility(0);
        }
        if (voiceButtonContainer != null) {
            voiceButtonContainer.setVisibility(0);
        }
        voiceButton.setVisibility(0);
        invalidatePressedFocusedStates(voiceButtonContainer, voiceButton);
        return true;
    }

    private void updateVoiceSearchIcon(ConstantState d) {
        View voiceButtonContainer = findViewById(R.id.voice_button_container);
        View voiceButton = findViewById(R.id.voice_button);
        updateButtonWithDrawable(R.id.voice_button, d);
        invalidatePressedFocusedStates(voiceButtonContainer, voiceButton);
    }

    private void updateAppMemSize() {
        TextView appMemSize = (TextView) findViewById(R.id.memsize_info);
        Storage storageInfo = new Storage();
        appMemSize.setText("已使用" + Tools.sizeToAutoUnit(storageInfo.getInternalStorage().mUsedStorage) + "/" + Tools.sizeToAutoUnit(storageInfo.getInternalStorage().mFreeStrorage) + "可用");
        appMemSize.setVisibility(0);
    }

    private void updateAppLatestButton() {
        ImageView appLatestButton = (ImageView) findViewById(R.id.applist_latest);
        if (appLatestButton != null) {
            appLatestButton.setVisibility(0);
        }
    }

    private void updateAppMarketIcon() {
        View marketButton = findViewById(R.id.market_button);
        Intent intent = new Intent();
        intent.setClassName("com.konka.market.main", "com.konka.market.main.Main");
        ComponentName activityName = intent.getComponent();
        if (activityName != null) {
            this.mAppMarketIntent = intent;
        }
        if (activityName != null) {
            marketButton.setVisibility(0);
            return;
        }
        marketButton.setVisibility(8);
        marketButton.setEnabled(false);
    }

    public boolean setLoadOnResume() {
        if (!this.mPaused) {
            return false;
        }
        Log.d("czj-ios", "setLoadOnResume true");
        Log.i(TAG, "setLoadOnResume");
        this.mOnResumeNeedsLoad = true;
        return true;
    }

    public int getCurrentWorkspaceScreen() {
        if (this.mWorkspace != null) {
            return this.mWorkspace.getCurrentPage();
        }
        return 0;
    }

    public void startBinding(int maxOccupiedScreenIdx) {
        Workspace workspace = this.mWorkspace;
        this.mWorkspace.clearDropTargets();
        this.mWorkspace.removeViews(maxOccupiedScreenIdx + 1, workspace.getChildCount() - (maxOccupiedScreenIdx + 1));
        int count = maxOccupiedScreenIdx + 1;
        for (int i = 0; i < count; i++) {
            CellLayout layoutParent = (CellLayout) workspace.getChildAt(i);
            if (layoutParent != null) {
                layoutParent.removeAllViewsInLayout();
            }
        }
        this.mWidgetsToAdvance.clear();
        if (this.mHotseat != null) {
            this.mHotseat.clearLayout();
        }
    }

    public void bindItems(ArrayList<ItemInfo> shortcuts, int start, int end) {
        setLoadOnResume();
        Workspace workspace = this.mWorkspace;
        for (int i = start; i < end; i++) {
            ItemInfo item = (ItemInfo) shortcuts.get(i);
            if (item.container != -101 || this.mHotseat != null) {
                switch (item.itemType) {
                    case 0:
                    case 1:
                        workspace.addInScreen(createShortcut((ShortcutInfo) item), item.container, item.screen, item.cellX, item.cellY, 1, 1, false);
                        break;
                    case 2:
                        FolderIcon newFolder = FolderIcon.fromXml(R.layout.folder_icon, this, (ViewGroup) workspace.getChildAt(workspace.getCurrentPage()), (FolderInfo) item, this.mIconCache);
                        if (!this.mHideIconLabels) {
                            newFolder.setTextVisible(false);
                        }
                        workspace.addInScreen(newFolder, item.container, item.screen, item.cellX, item.cellY, 1, 1, false);
                        break;
                }
            }
        }
        workspace.requestLayout();
    }

    public void bindFolders(HashMap<Long, FolderInfo> folders) {
        setLoadOnResume();
        sFolders.clear();
        sFolders.putAll(folders);
    }

    public void bindAppWidget(LauncherAppWidgetInfo item) {
        setLoadOnResume();
        Workspace workspace = this.mWorkspace;
        int appWidgetId = item.appWidgetId;
        AppWidgetProviderInfo appWidgetInfo = this.mAppWidgetManager.getAppWidgetInfo(appWidgetId);
        item.hostView = this.mAppWidgetHost.createView(this, appWidgetId, appWidgetInfo);
        item.hostView.setAppWidget(appWidgetId, appWidgetInfo);
        item.hostView.setTag(item);
        workspace.addInScreen(item.hostView, item.container, item.screen, item.cellX, item.cellY, item.spanX, item.spanY, false);
        addWidgetToAutoAdvanceIfNeeded(item.hostView, appWidgetInfo);
        workspace.requestLayout();
    }

    public void finishBindingItems() {
        setLoadOnResume();
        if (this.mSavedState != null) {
            if (!this.mWorkspace.hasFocus()) {
                this.mWorkspace.getChildAt(this.mWorkspace.getCurrentPage()).requestFocus();
            }
            this.mSavedState = null;
        }
        if (this.mSavedInstanceState != null) {
            super.onRestoreInstanceState(this.mSavedInstanceState);
            this.mSavedInstanceState = null;
        }
        this.mWorkspaceLoading = false;
        for (int i = 0; i < sPendingAddList.size(); i++) {
            completeAdd((PendingAddArguments) sPendingAddList.get(i));
        }
        sPendingAddList.clear();
        updateAppMarketIcon();
        updateAppMemSize();
        updateAppLatestButton();
        this.mWorkspace.post(this.mBuildLayersRunnable);
        this.mWorkspace.refreshAddGuide();
        focusToUpperLeftItem();
    }

    private void focusToUpperLeftItem() {
        int homescreenIndex = this.mWorkspace.getHomeScreenIndex();
        if (getCurrentWorkspaceScreen() == homescreenIndex) {
            Log.d(TAG, "focus to the upper left item");
            CellLayoutChildren cl = (CellLayoutChildren) ((CellLayout) this.mWorkspace.getChildAt(homescreenIndex)).getChildAt(0);
            if (cl.getChildCount() <= 0) {
                return;
            }
            if (cl.getChildAt(5, 0) instanceof LauncherAppWidgetHostView) {
                ((LauncherAppWidgetHostView) cl.getChildAt(5, 0)).requestFocusFromTouch();
            } else {
                cl.requestFocusFromTouch();
            }
        }
    }

    public void showSelectboxOnWorkspace() {
    }

    public void showSelectboxInFolder(Folder folder) {
    }

    public Rect getTVWindowRect() {
        Rect rTV = new Rect();
        CellLayoutChildren cl = (CellLayoutChildren) ((CellLayout) this.mWorkspace.getChildAt(this.mWorkspace.getHomeScreenIndex())).getChildAt(0);
        if (cl.getChildCount() != 0) {
            View child = cl.getChildAt(5, 0);
            if (child != null) {
                int[] location = new int[2];
                child.getLocationOnScreen(location);
                rTV.set(location[0], location[1], location[0] + child.getWidth(), location[1] + child.getHeight());
            }
        }
        return rTV;
    }

    public void bindSearchablesChanged() {
        boolean searchVisible = updateGlobalSearchIcon();
        this.mSearchDropTargetBar.onSearchPackagesChanged(searchVisible, updateVoiceSearchIcon(searchVisible));
    }

    public void bindAllApplications(final ArrayList<ApplicationInfo> apps) {
        if (this.mWorkspace != null) {
            Log.d("czj-ios", "call bindAllApplications");
            this.mWorkspace.addExternalItems(this.mModel.mMissedOnWorkspace, false);
        }
        View progressBar = this.mAppsCustomizeTabHost.findViewById(R.id.apps_customize_progress_bar);
        if (progressBar != null) {
            ((ViewGroup) progressBar.getParent()).removeView(progressBar);
        }
        this.mAppsCustomizeTabHost.post(new Runnable() {
            public void run() {
                if (Launcher.this.mAppsCustomizeContent != null) {
                    Launcher.this.mAppsCustomizeContent.setApps(apps);
                }
            }
        });
        final ArrayList<ApplicationInfo> appsCopy = (ArrayList) apps.clone();
        this.mSearchDropTargetBar.post(new Runnable() {
            public void run() {
                Launcher.this.mSearchDropTargetBar.setApps(appsCopy);
            }
        });
    }

    public void bindAppsAdded(ArrayList<ApplicationInfo> apps) {
        setLoadOnResume();
        removeDialog(1);
        if (this.mWorkspace != null) {
            Log.d("czj-ios", "call bindAppsAdded");
            Log.d("czj-ios", "mMissedOnWorkspace size: " + this.mModel.mMissedOnWorkspace.size());
            for (int i = 0; i < this.mModel.mMissedOnWorkspace.size(); i++) {
                Log.d("czj-ios", "mMissedOnWorkspace package " + i + " : " + ((ApplicationInfo) this.mModel.mMissedOnWorkspace.get(i)).componentName.getPackageName());
            }
            this.mWorkspace.addExternalItems(this.mModel.mMissedOnWorkspace, true);
        }
        if (this.mAppsCustomizeContent != null) {
            this.mAppsCustomizeContent.addApps(apps);
        }
        this.mSearchDropTargetBar.addApps(apps);
    }

    public void bindAppsUpdated(ArrayList<ApplicationInfo> apps) {
        setLoadOnResume();
        removeDialog(1);
        if (this.mWorkspace != null) {
            this.mWorkspace.updateShortcuts(apps);
        }
        if (this.mAppsCustomizeContent != null) {
            this.mAppsCustomizeContent.updateApps(apps);
        }
        this.mSearchDropTargetBar.updateApps(apps);
    }

    public void bindAppsRemoved(ArrayList<? extends ItemInfo> apps, boolean permanent) {
        boolean z = false;
        removeDialog(1);
        if (apps.size() > 0) {
            if (!(apps.get(0) instanceof ApplicationInfo)) {
                z = true;
            }
            Log.d("Hfeng", "Launcher.bindAppsRemoved apps");
            this.mWorkspace.removeItems(apps);
            if (!z) {
                ArrayList<ApplicationInfo> items = new ArrayList<>(apps.size());
                Iterator it = apps.iterator();
                while (it.hasNext()) {
                    items.add((ApplicationInfo) ((ItemInfo) it.next()));
                }
                if (this.mAppsCustomizeContent != null) {
                    this.mAppsCustomizeContent.removeApps(items);
                }
                this.mSearchDropTargetBar.removeApps(items);
                this.mDragController.onAppsRemoved(items, this);
            }
        }
    }

    public void bindPackagesUpdated() {
        if (this.mAppsCustomizeContent != null) {
            this.mAppsCustomizeContent.onPackagesUpdated();
        }
        if (this.mSearchDropTargetBar != null) {
            this.mSearchDropTargetBar.onPackagesUpdated();
        }
    }

    private int mapConfigurationOriActivityInfoOri(int configOri) {
        Display d = getWindowManager().getDefaultDisplay();
        int naturalOri = 2;
        switch (d.getRotation()) {
            case 0:
            case 2:
                naturalOri = configOri;
                break;
            case 1:
            case 3:
                if (configOri != 2) {
                    naturalOri = 2;
                    break;
                } else {
                    naturalOri = 1;
                    break;
                }
        }
        int[] oriMap = new int[4];
        oriMap[0] = 1;
        oriMap[2] = 9;
        oriMap[3] = 8;
        int indexOffset = 0;
        if (naturalOri == 2) {
            indexOffset = 1;
        }
        return oriMap[(d.getRotation() + indexOffset) % 4];
    }

    public void lockScreenOrientationOnLargeUI() {
        if (LauncherApplication.isScreenLarge()) {
            setRequestedOrientation(mapConfigurationOriActivityInfoOri(getCurrentOrientation()));
        }
    }

    public void unlockScreenOrientationOnLargeUI() {
        if (LauncherApplication.isScreenLarge()) {
            this.mHandler.postDelayed(new Runnable() {
                public void run() {
                    Launcher.this.syncOrientation();
                }
            }, 500);
        }
    }

    private boolean isGuideEnabled() {
        if (ActivityManager.isRunningInTestHarness()) {
            return false;
        }
        return getResources().getBoolean(R.bool.config_guideEnable);
    }

    public void hideAddGuide() {
        int count = this.mWorkspace.getChildCount();
        for (int i = 0; i < count; i++) {
            ((CellLayout) this.mWorkspace.getChildAt(i)).hideGuideView();
        }
    }

    private boolean isClingsEnabled() {
        if (ActivityManager.isRunningInTestHarness()) {
            return false;
        }
        return getResources().getBoolean(R.bool.config_clingEnable);
    }

    private Cling initCling(int clingId, int[] positionData, boolean animate, int delay) {
        Cling cling = (Cling) findViewById(clingId);
        if (cling != null) {
            cling.init(this, positionData);
            cling.setVisibility(0);
            cling.setLayerType(2, null);
            if (animate) {
                cling.buildLayer();
                cling.setAlpha(FlyingIcon.ANGULAR_VMIN);
                cling.animate().alpha(1.0f).setInterpolator(new AccelerateInterpolator()).setDuration(550).setStartDelay((long) delay).start();
            } else {
                cling.setAlpha(1.0f);
            }
        }
        return cling;
    }

    private void dismissCling(final Cling cling, final String flag, int duration) {
        if (cling != null) {
            cling.dismiss();
            ObjectAnimator anim = ObjectAnimator.ofFloat(cling, "alpha", new float[]{0.0f});
            anim.setDuration((long) duration);
            anim.addListener(new AnimatorListenerAdapter() {
                public void onAnimationEnd(Animator animation) {
                    cling.setVisibility(8);
                    cling.cleanup();
                    Editor editor = Launcher.this.getSharedPreferences(PreferencesProvider.PREFERENCES_KEY, 0).edit();
                    editor.putBoolean(flag, true);
                    editor.commit();
                }
            });
            anim.start();
        }
    }

    private void removeCling(int id) {
        final View cling = findViewById(id);
        if (cling != null) {
            final ViewGroup parent = (ViewGroup) cling.getParent();
            parent.post(new Runnable() {
                public void run() {
                    parent.removeView(cling);
                }
            });
        }
    }

    public void showFirstRunWorkspaceCling() {
        SharedPreferences prefs = getSharedPreferences(PreferencesProvider.PREFERENCES_KEY, 0);
        if (!isClingsEnabled() || prefs.getBoolean("cling.workspace.dismissed", false)) {
            removeCling(R.id.workspace_cling);
        } else {
            initCling(R.id.workspace_cling, null, false, 0);
        }
    }

    public void showFirstRunAllAppsCling(int[] position) {
        SharedPreferences prefs = getSharedPreferences(PreferencesProvider.PREFERENCES_KEY, 0);
        if (!isClingsEnabled() || prefs.getBoolean("cling.allapps.dismissed", false)) {
            removeCling(R.id.all_apps_cling);
        } else {
            initCling(R.id.all_apps_cling, position, true, 0);
        }
    }

    public void showFirstRunAllAppsSortCling() {
        SharedPreferences prefs = getSharedPreferences(PreferencesProvider.PREFERENCES_KEY, 0);
        if (!isClingsEnabled() || prefs.getBoolean("cling.allappssort.dismissed", false)) {
            removeCling(R.id.all_apps_sort_cling);
        } else {
            initCling(R.id.all_apps_sort_cling, null, true, 0);
        }
    }

    public Cling showFirstRunFoldersCling() {
        SharedPreferences prefs = getSharedPreferences(PreferencesProvider.PREFERENCES_KEY, 0);
        if (isClingsEnabled() && !prefs.getBoolean("cling.folder.dismissed", false)) {
            return initCling(R.id.folder_cling, null, true, 0);
        }
        removeCling(R.id.folder_cling);
        return null;
    }

    public boolean isFolderClingVisible() {
        Cling cling = (Cling) findViewById(R.id.folder_cling);
        if (cling == null || cling.getVisibility() != 0) {
            return false;
        }
        return true;
    }

    public void dismissWorkspaceCling(View v) {
        dismissCling((Cling) findViewById(R.id.workspace_cling), "cling.workspace.dismissed", DISMISS_CLING_DURATION);
    }

    public void dismissAllAppsCling(View v) {
        dismissCling((Cling) findViewById(R.id.all_apps_cling), "cling.allapps.dismissed", DISMISS_CLING_DURATION);
    }

    public void dismissAllAppsSortCling(View v) {
        dismissCling((Cling) findViewById(R.id.all_apps_sort_cling), "cling.allappssort.dismissed", DISMISS_CLING_DURATION);
    }

    public void dismissFolderCling(View v) {
        dismissCling((Cling) findViewById(R.id.folder_cling), "cling.folder.dismissed", DISMISS_CLING_DURATION);
    }

    public boolean preferencesChanged() {
        SharedPreferences prefs = getSharedPreferences(PreferencesProvider.PREFERENCES_KEY, 0);
        boolean preferencesChanged = prefs.getBoolean(PreferencesProvider.PREFERENCES_CHANGED, false);
        if (preferencesChanged) {
            Editor editor = prefs.edit();
            editor.putBoolean(PreferencesProvider.PREFERENCES_CHANGED, false);
            editor.commit();
        }
        return preferencesChanged;
    }

    public void dumpState() {
        Log.d(TAG, "BEGIN launcher2 dump state for launcher " + this);
        Log.d(TAG, "mSavedState=" + this.mSavedState);
        Log.d(TAG, "mWorkspaceLoading=" + this.mWorkspaceLoading);
        Log.d(TAG, "mRestoring=" + this.mRestoring);
        Log.d(TAG, "mWaitingForResult=" + this.mWaitingForResult);
        Log.d(TAG, "mSavedInstanceState=" + this.mSavedInstanceState);
        Log.d(TAG, "sFolders.size=" + sFolders.size());
        this.mModel.dumpState();
        if (this.mAppsCustomizeContent != null) {
            this.mAppsCustomizeContent.dumpState();
        }
        Log.d(TAG, "END launcher2 dump state");
    }

    public void dump(String prefix, FileDescriptor fd, PrintWriter writer, String[] args) {
        super.dump(prefix, fd, writer, args);
        writer.println(" ");
        writer.println("Debug logs: ");
        for (int i = 0; i < sDumpLogs.size(); i++) {
            writer.println("  " + ((String) sDumpLogs.get(i)));
        }
    }

    public int getFolderState() {
        return this.mFolderState;
    }

    public void setFolderState(int state) {
        this.mFolderState = state;
    }

    public static int getWidthPixels() {
        return sDisplayMetrics.widthPixels;
    }

    public static float getDensity() {
        return sDisplayMetrics.density;
    }

    public static int getHeightPixels() {
        return sDisplayMetrics.heightPixels;
    }

    /* access modifiers changed from: private */
    public void updateLockView() {
        ImageView lock = (ImageView) findViewById(R.id.kk_user_lock);
        if (lock != null) {
            if (isScreenLockEnable()) {
                lock.setVisibility(0);
            } else {
                lock.setVisibility(8);
            }
        }
    }

    private boolean isScreenLockEnable() {
        UserManager um = (UserManager) getSystemService("user");
        android.content.pm.UserInfo admin = (android.content.pm.UserInfo) um.getUsers(true).get(0);
        try {
            if (UserManager.getMaxSupportedUsers() > 1 && um.getUserCount() > 1) {
                return true;
            }
            if (um.getUserCount() == 1 && admin.isAdmin() && !new LockPatternUtils(this).checkPassword(StatConstants.MTA_COOPERATION_TAG)) {
                return true;
            }
            return false;
        } catch (RuntimeException e) {
            Log.w(TAG, "isScreenLockEnable: ", e);
        }
    }

    private Rect getRealTVWindowRect() {
        return getTVWindowRect();
    }

    public void ClearTVManager_RestorSTR() {
        KKCommonManager.getInstance(this).finalizeAllTVManager();
    }

    /* access modifiers changed from: private */
    public void SetPropertyForSTR(String value) {
        if (Stub.asInterface(ServiceManager.checkService("window")) == null) {
            Log.w(TAG, "Unable to find IWindowManger interface.");
        } else {
            SystemProperties.set("mstar.str.suspending", value);
        }
    }

    private boolean isLaunchBySTR() {
        String suspending = SystemProperties.get("mstar.str.suspending", "0");
        Log.d(TAG, "mstar.str.suspending====" + suspending);
        return "2".equals(suspending) || "1".equals(suspending);
    }

    private void updateThemeMangerView() {
        boolean flag;
        Log.d(TAG, "===========updateThemeManagerView");
        this.mThememanagerButton = (ImageView) findViewById(R.id.theme_manager_button);
        try {
            getPackageManager().getPackageInfo("com.konka.thememanager", 1);
            flag = true;
        } catch (NameNotFoundException e) {
            flag = false;
            e.printStackTrace();
        }
        if (flag) {
            this.mThememanagerButton.setVisibility(0);
            this.mThememanagerButton.setEnabled(true);
            return;
        }
        this.mThememanagerButton.setVisibility(8);
    }

    private void updateCIBNLogoView() {
    }

    private void setWallPaper() {
        WallpaperManager manager = WallpaperManager.getInstance(this);
        Point size = new Point();
        getWindowManager().getDefaultDisplay().getSize(size);
        manager.suggestDesiredDimensions(size.x, size.y);
    }
}
