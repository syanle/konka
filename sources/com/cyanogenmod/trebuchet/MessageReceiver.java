package com.cyanogenmod.trebuchet;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class MessageReceiver extends BroadcastReceiver {
    private static final String MESSAGE_CONTENT = "msgcontent";
    private static final String MESSAGE_FROM = "msgfrom";
    private static final String MESSAGE_ID = "msgid";
    public static final String MESSAGE_POPUP_ACTION = "com.konka.message.intent.action.POPUP";
    private static final String MESSAGE_PUSH_MODE = "msgmode";
    private static final String MESSAGE_TITLE = "msgtitle";
    public static final String PASSPORT_MESSAGE_ACTION = "com.konka.message.PASSPORT_MSG";
    public static final String SYSTEM_MESSAGE_ACTION = "com.konka.message.SYSTEM_MSG";
    private static final String TAG = "MessageReceiver";

    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Log.v(TAG, "Receive Broadcast:" + action);
        if (action.equals(SYSTEM_MESSAGE_ACTION) || action.equals(PASSPORT_MESSAGE_ACTION)) {
            MessageManager messageManager = MessageManager.getInstance();
            if (messageManager == null) {
                Log.v(TAG, "messagemanager is null");
                return;
            }
            messageManager.updateMessageView();
            messageManager.updateListeners();
        }
    }
}
