package com.konka.passport.service;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class xmlData implements Parcelable {
    public static final Creator<xmlData> CREATOR = new Creator<xmlData>() {
        public xmlData createFromParcel(Parcel in) {
            return new xmlData(in, null);
        }

        public xmlData[] newArray(int size) {
            return new xmlData[size];
        }
    };
    private String strXml;

    public xmlData(String str) {
        this.strXml = str;
    }

    public xmlData() {
    }

    public void SetXmlData(String xmlData) {
        this.strXml = xmlData;
    }

    public String GetXmlData() {
        return this.strXml;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeString(this.strXml);
    }

    public void readFromParcel(Parcel in) {
        this.strXml = in.readString();
    }

    private xmlData(Parcel in) {
        this.strXml = in.readString();
    }

    /* synthetic */ xmlData(Parcel parcel, xmlData xmldata) {
        this(parcel);
    }
}
