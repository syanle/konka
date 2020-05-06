package com.konka.market.upgrade;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class UpgradeInfo implements Parcelable {
    public static final Creator<UpgradeInfo> CREATOR = new Creator<UpgradeInfo>() {
        public UpgradeInfo createFromParcel(Parcel source) {
            try {
                int id = source.readInt();
                String name = source.readString();
                long size = source.readLong();
                String date = source.readString();
                String pack = source.readString();
                String versionname = source.readString();
                int versioncode = source.readInt();
                String downloadurl = source.readString();
                String md5 = source.readString();
                boolean third = source.readInt() > 0;
                UpgradeInfo info = new UpgradeInfo();
                info.mID = id;
                info.mName = name;
                info.mSize = size;
                info.mDate = date;
                info.mPackageName = pack;
                info.mVersionName = versionname;
                info.mVersionCode = versioncode;
                info.mDownloadURL = downloadurl;
                info.mMD5 = md5;
                info.bThirdParty = third;
                return info;
            } catch (Exception e) {
                return null;
            }
        }

        public UpgradeInfo[] newArray(int size) {
            return new UpgradeInfo[size];
        }
    };
    public boolean bThirdParty;
    public String mDate;
    public String mDownloadURL;
    public int mID;
    public String mMD5;
    public String mName;
    public String mPackageName;
    public long mSize;
    public int mVersionCode;
    public String mVersionName;

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        try {
            dest.writeInt(this.mID);
            dest.writeString(this.mName);
            dest.writeLong(this.mSize);
            dest.writeString(this.mDate);
            dest.writeString(this.mPackageName);
            dest.writeString(this.mVersionName);
            dest.writeInt(this.mVersionCode);
            dest.writeString(this.mDownloadURL);
            dest.writeString(this.mMD5);
            dest.writeInt(this.bThirdParty ? 1 : 0);
        } catch (Exception e) {
        }
    }
}
