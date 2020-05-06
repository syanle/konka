package com.konka.passport.service;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class PassportInfo implements Parcelable {
    public static final Creator<PassportInfo> CREATOR = new Creator<PassportInfo>() {
        public PassportInfo createFromParcel(Parcel in) {
            return new PassportInfo(in, null);
        }

        public PassportInfo[] newArray(int size) {
            return new PassportInfo[size];
        }
    };
    private Bitmap HeadPic;
    private String IpAddress;
    private String PassportId;

    public PassportInfo(String passportid, String ipaddress, Bitmap headpic) {
        this.PassportId = passportid;
        this.IpAddress = ipaddress;
        this.HeadPic = headpic;
    }

    public PassportInfo() {
    }

    public void SetPassportId(String passportid) {
        this.PassportId = passportid;
    }

    public String GetPassportId() {
        return this.PassportId;
    }

    public void SetIpAddress(String ipaddress) {
        this.IpAddress = ipaddress;
    }

    public String GetIpAddress() {
        return this.IpAddress;
    }

    public void SetHeadPic(Bitmap headpic) {
        this.HeadPic = headpic;
    }

    public Bitmap GetHeadPic() {
        return this.HeadPic;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeString(this.PassportId);
        out.writeString(this.IpAddress);
        out.writeParcelable(this.HeadPic, flags);
    }

    public void readFromParcel(Parcel in) {
        this.PassportId = in.readString();
        this.IpAddress = in.readString();
        this.HeadPic = (Bitmap) in.readParcelable(Bitmap.class.getClassLoader());
    }

    private PassportInfo(Parcel in) {
        this.PassportId = in.readString();
        this.IpAddress = in.readString();
        this.HeadPic = (Bitmap) in.readParcelable(Bitmap.class.getClassLoader());
    }

    /* synthetic */ PassportInfo(Parcel parcel, PassportInfo passportInfo) {
        this(parcel);
    }
}
