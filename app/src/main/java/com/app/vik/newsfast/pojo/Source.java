package com.app.vik.newsfast.pojo;

import android.os.Parcel;
import android.os.Parcelable;

public class Source implements Parcelable {

    private String mId;
    private String mName;

    public Source(String id, String name) {
        this.mId = id;
        this.mName = name;
    }

    protected Source(Parcel in) {
        mId = in.readString();
        mName = in.readString();
    }

    public static final Creator<Source> CREATOR = new Creator<Source>() {
        @Override
        public Source createFromParcel(Parcel in) {
            return new Source(in);
        }

        @Override
        public Source[] newArray(int size) {
            return new Source[size];
        }
    };

    public String getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mId);
        parcel.writeString(mName);
    }
}
