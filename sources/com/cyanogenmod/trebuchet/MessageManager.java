package com.cyanogenmod.trebuchet;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import com.konka.ios7launcher.R;
import com.tencent.stat.common.StatConstants;
import java.util.ArrayList;
import java.util.Iterator;

public class MessageManager {
    public static final Uri MESSAGE_CONTENT_PROVIDER_URI = Uri.parse("content://com.konka.message.contentprovider/MessageTable");
    public static final int MESSAGE_ICON_RESOURCE_ID = 2130837569;
    public static final String MESSAGE_PACKAGE_NAME = "com.konka.message";
    public static final String TAG = "MessageManager";
    private static MessageManager instance = null;
    private static Context mContext = null;
    private FolderInfo folderInfo = null;
    private boolean isUserLogin;
    private ArrayList<IMessageListener> mListeners = new ArrayList<>();
    private BubbleTextView messageView = null;
    private String passportId;

    public interface IMessageListener {
        void onMessageCenterUpdate(int i);
    }

    public static MessageManager createInstance(Context context) {
        if (instance == null) {
            instance = new MessageManager(context);
        }
        return instance;
    }

    public static MessageManager getInstance() {
        return instance;
    }

    private MessageManager(Context context) {
        mContext = context;
        this.isUserLogin = false;
        this.passportId = null;
    }

    public boolean registerUpdateListener(IMessageListener listener) {
        if (listener == null || this.mListeners.contains(listener)) {
            return false;
        }
        this.mListeners.add(listener);
        listener.onMessageCenterUpdate(getMessageUnReadNumber());
        updateMessageView();
        return true;
    }

    public boolean unregisterUpdateListener(IMessageListener listener) {
        return this.mListeners.remove(listener);
    }

    public void updateListeners() {
        int unreadCount = getMessageUnReadNumber();
        Iterator it = this.mListeners.iterator();
        while (it.hasNext()) {
            ((IMessageListener) it.next()).onMessageCenterUpdate(unreadCount);
        }
    }

    public void updateUserCenterConnectivity(boolean isUserLogin2, String passportId2) {
        Log.d(TAG, "update usercenterconnect");
        this.isUserLogin = isUserLogin2;
        this.passportId = passportId2;
        updateMessageView();
    }

    public void setMessageView(BubbleTextView view) {
        Log.d(TAG, "set messageview");
        this.messageView = view;
    }

    public void setFolderInfo(FolderInfo info) {
        this.folderInfo = info;
    }

    public void updateMessageView() {
        Log.d(TAG, "update MessageView");
        if (this.messageView == null) {
            Log.d(TAG, "messageview is null");
            return;
        }
        AppInfoManager appInfoManager = AppInfoManager.getInstance();
        if (appInfoManager == null) {
            Log.d(TAG, "appinfomanager is null");
            return;
        }
        ShortcutInfo shortcutInfo = (ShortcutInfo) this.messageView.getTag();
        Log.d(TAG, "container=" + shortcutInfo.container + ",screen=" + shortcutInfo.screen + ",cellX=" + shortcutInfo.cellX + ",cellY=" + shortcutInfo.cellY);
        Bitmap icon = appInfoManager.setIconCornerNumber(mContext, R.drawable.com_konka_message, getMessageUnReadNumber());
        shortcutInfo.setIcon(icon);
        this.messageView.setCompoundDrawablesWithIntrinsicBounds(null, new FastBitmapDrawable(icon), null, null);
        if (this.folderInfo != null && shortcutInfo.container != -100 && shortcutInfo.container != -101) {
            this.folderInfo.itemsChanged();
        }
    }

    public int getMessageUnReadNumber() {
        String where;
        String str = StatConstants.MTA_COOPERATION_TAG;
        if (isSupportNewMessage(mContext)) {
            where = "AliveFlag=1 and DeletedFlag=0 and ReadedFlag=0";
        } else {
            where = "DeletedFlag=0 and ReadedFlag=0";
        }
        try {
            Log.d(TAG, "query:" + where);
            Cursor cursor = mContext.getContentResolver().query(MESSAGE_CONTENT_PROVIDER_URI, null, where, null, null);
            if (cursor != null) {
                int number = cursor.getCount();
                Log.d(TAG, "message count is " + number);
                return number;
            }
            Log.d(TAG, "query message cursor is null");
            return 0;
        } catch (Exception e) {
            Log.d(TAG, "query message error");
            return 0;
        }
    }

    public boolean isSupportNewMessage(Context context) {
        PackageInfo info = null;
        try {
            info = context.getPackageManager().getPackageInfo("com.konka.localserver", 0);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        if (info != null) {
            return true;
        }
        return false;
    }
}
