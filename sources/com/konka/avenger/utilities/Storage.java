package com.konka.avenger.utilities;

import android.os.Environment;
import android.os.StatFs;
import android.util.Log;

public class Storage {
    static final String TAG = "Storage";
    private StatFs mDataFileStats;
    private long mFreeStorage = 0;
    private StatFs mSDCardFileStats;
    private long mTotalStorage = 0;
    private long mUsedStorage = 0;

    public StorageInfor getInternalStorage() {
        this.mDataFileStats = new StatFs("/data");
        this.mDataFileStats.restat("/data");
        try {
            this.mTotalStorage = ((long) this.mDataFileStats.getBlockCount()) * ((long) this.mDataFileStats.getBlockSize());
            this.mFreeStorage = ((long) this.mDataFileStats.getAvailableBlocks()) * ((long) this.mDataFileStats.getBlockSize());
            this.mUsedStorage = this.mTotalStorage - this.mFreeStorage;
        } catch (IllegalArgumentException e) {
        }
        return new StorageInfor(this.mTotalStorage, this.mFreeStorage, this.mUsedStorage);
    }

    public boolean isSDMounted() {
        if (Environment.getExternalStorageState().equals("mounted")) {
            return true;
        }
        return false;
    }

    public StorageInfor getDiskStorage() {
        String path = Environment.getExternalStorageDirectory().toString();
        Log.d(TAG, "the disk storage path is ===" + path + "the status is ===" + Environment.getExternalStorageState());
        this.mSDCardFileStats = new StatFs(path);
        this.mSDCardFileStats.restat(path);
        try {
            this.mTotalStorage = ((long) this.mSDCardFileStats.getBlockCount()) * ((long) this.mSDCardFileStats.getBlockSize());
            this.mFreeStorage = ((long) this.mSDCardFileStats.getAvailableBlocks()) * ((long) this.mSDCardFileStats.getBlockSize());
            this.mUsedStorage = this.mTotalStorage - this.mFreeStorage;
        } catch (IllegalArgumentException e) {
        }
        return new StorageInfor(this.mTotalStorage, this.mFreeStorage, this.mUsedStorage);
    }
}
