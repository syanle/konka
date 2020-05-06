package com.konka.avenger.utilities;

public class StorageInfor {
    public long mFreeStrorage;
    public long mTotalStorage;
    public long mUsedStorage;

    StorageInfor(long totalStorage, long freeStorage, long usedStorage) {
        this.mFreeStrorage = freeStorage;
        this.mTotalStorage = totalStorage;
        this.mUsedStorage = usedStorage;
    }
}
