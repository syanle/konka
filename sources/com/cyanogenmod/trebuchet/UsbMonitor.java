package com.cyanogenmod.trebuchet;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.os.storage.StorageEventListener;
import android.os.storage.StorageManager;
import android.os.storage.StorageVolume;
import android.util.Log;
import android.util.Slog;
import com.cyanogenmod.trebuchet.ConnectivityService.IConnectivityMonitor;
import com.tencent.stat.common.StatConstants;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;

public class UsbMonitor extends StorageEventListener implements IConnectivityMonitor {
    private static final boolean DEBUG = true;
    public static final String KEY_USB_DEVICE_COUNT = "UsbMonitor.usbCount";
    public static final String KEY_USB_IS_CONNECTED = "UsbMonitor.isUsbConnected";
    public static final String KEY_USB_PATHLIST = "UsbMonitor.PathList";
    public static final String KEY_USB_SDCARD = "UsbMonitor.SDcardCount";
    public static final String KEY_USB_UDISK = "UsbMonitor.UdiskCount";
    private static final String TAG = "wzd_usb";
    private boolean mConnected;
    private Bundle mCurrentConnectivity = new Bundle();
    private IUsbUpdateListener mListener;
    private WeakReference<StorageManager> mStorageManagerRef;
    private HashMap<String, Integer> mUsbNameMap = new HashMap<>();
    private ArrayList<String> pathList;
    private int sdcardCount = 0;
    private int udiskCount = 0;

    public interface IUsbUpdateListener {
        void onUpdateUsbConnectivity(Bundle bundle);
    }

    public UsbMonitor(Context context, IUsbUpdateListener listener) {
        this.mListener = listener;
        StorageManager storageManager = (StorageManager) context.getSystemService("storage");
        this.mStorageManagerRef = new WeakReference<>(storageManager);
        this.mConnected = storageManager.isUsbMassStorageConnected();
        this.pathList = new ArrayList<>();
        this.pathList.add(StatConstants.MTA_COOPERATION_TAG);
        this.pathList.add(StatConstants.MTA_COOPERATION_TAG);
        Slog.d(TAG, "initial usb " + this.mConnected);
    }

    public void startMonitor() {
        queryUsbMassStorage();
        onUsbMassStorageConnectionChanged(this.mConnected);
        StorageManager sm = (StorageManager) this.mStorageManagerRef.get();
        if (sm != null) {
            sm.registerListener(this);
        }
    }

    public void stopMonitor() {
        StorageManager sm = (StorageManager) this.mStorageManagerRef.get();
        if (sm != null) {
            try {
                sm.unregisterListener(this);
            } catch (IllegalArgumentException e) {
                Log.e(TAG, "Unable to stop UsbMonitor", e);
            }
        }
    }

    private String getPartitionName(String path) {
        int partitionNameEndIndex = path.length() - 1;
        while (partitionNameEndIndex > 0 && Character.isDigit(path.charAt(partitionNameEndIndex))) {
            partitionNameEndIndex--;
        }
        return path.substring(0, partitionNameEndIndex + 1);
    }

    private void updateUsbMounted(String partitionName) {
        if (this.mUsbNameMap.containsKey(partitionName)) {
            Slog.d(TAG, "addUSB(" + partitionName + ") " + Integer.valueOf(((Integer) this.mUsbNameMap.get(partitionName)).intValue() + 1));
            return;
        }
        this.mUsbNameMap.put(partitionName, Integer.valueOf(1));
        updateUsbState();
    }

    private void updateUsbUnmounted(String partitionName) {
        if (this.mUsbNameMap.containsKey(partitionName)) {
            Integer partitionCount = Integer.valueOf(((Integer) this.mUsbNameMap.get(partitionName)).intValue() - 1);
            Slog.d(TAG, "removeUSB(" + partitionName + ") " + partitionCount);
            if (partitionCount.intValue() == 0) {
                this.mUsbNameMap.remove(partitionName);
                updateUsbState();
            }
        }
    }

    private void queryUsbMassStorage() {
        StorageManager sm = (StorageManager) this.mStorageManagerRef.get();
        if (sm != null) {
            this.mConnected = sm.isUsbMassStorageConnected();
            StorageVolume[] volumes = sm.getVolumeList();
            if (volumes != null) {
                for (StorageVolume path : volumes) {
                    String path2 = path.getPath();
                    if ("mounted".equals(sm.getVolumeState(path2))) {
                        Slog.i(TAG, "query USB:");
                        this.mConnected = true;
                        updateDetailCount(path2, true);
                        updateUsbMounted(path2);
                    }
                }
            }
        }
    }

    public void onStorageStateChanged(String path, String oldState, String newState) {
        Slog.i(TAG, String.format("Media {%s} state changed from {%s} -> {%s}", new Object[]{path, oldState, newState}));
        if (path == null || !path.contains("/storage/emulated")) {
            Slog.d(TAG, "partitionName=" + path);
            if (newState.equals("mounted")) {
                updateDetailCount(path, true);
                updateUsbMounted(path);
            } else if (newState.equals("removed") || newState.equals("bad_removal")) {
                updateDetailCount(path, false);
                updateUsbUnmounted(path);
            }
        } else {
            Log.d(TAG, "aiken=====the storage path is ignored===" + path);
        }
    }

    private void updateDetailCount(String path, boolean add) {
        if (add) {
            if (path.contains("/mnt/sdcard")) {
                if (this.sdcardCount < 1) {
                    this.sdcardCount++;
                    this.pathList.add(0, path);
                }
            } else if (path.contains("/mnt/usb/sd")) {
                int i = 1;
                while (i < this.pathList.size()) {
                    if (!((String) this.pathList.get(i)).equals(path)) {
                        i++;
                    } else {
                        return;
                    }
                }
                this.udiskCount++;
                this.pathList.add(this.udiskCount, path);
                Log.v(TAG, "=============================udiskCount add to " + this.udiskCount + " for  " + path);
            }
        } else if (this.sdcardCount > 0 && path.contains("/mnt/sdcard")) {
            this.pathList.set(0, StatConstants.MTA_COOPERATION_TAG);
            this.sdcardCount--;
        } else if (this.udiskCount > 0 && path.contains("/mnt/usb/sd")) {
            this.pathList.set(this.udiskCount, StatConstants.MTA_COOPERATION_TAG);
            this.udiskCount--;
        }
    }

    public void onUsbMassStorageConnectionChanged(boolean connected) {
        String st = Environment.getExternalStorageState();
        if (connected && (st.equals("removed") || st.equals("checking"))) {
            connected = false;
        }
        this.mConnected = connected;
        queryUsbMassStorage();
        Slog.i(TAG, String.format("connection changed to %s (media state %s)", new Object[]{Boolean.valueOf(connected), st}));
        if (this.mListener != null) {
            this.mListener.onUpdateUsbConnectivity(getCurrentConnectivity());
        }
    }

    private void updateUsbState() {
        this.mConnected = !this.mUsbNameMap.isEmpty();
        if (this.mListener != null) {
            this.mListener.onUpdateUsbConnectivity(getCurrentConnectivity());
        }
    }

    private Bundle getCurrentConnectivity() {
        this.mCurrentConnectivity.putBoolean(KEY_USB_IS_CONNECTED, this.mConnected);
        this.mCurrentConnectivity.putInt(KEY_USB_DEVICE_COUNT, this.mUsbNameMap.size());
        this.mCurrentConnectivity.putInt(KEY_USB_SDCARD, this.sdcardCount);
        this.mCurrentConnectivity.putInt(KEY_USB_UDISK, this.udiskCount);
        this.mCurrentConnectivity.putStringArrayList(KEY_USB_PATHLIST, this.pathList);
        return this.mCurrentConnectivity;
    }
}
