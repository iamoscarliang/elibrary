package com.oscarliang.elibrary.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Book implements Parcelable {

    public static final Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

    @SerializedName("id")
    @Expose
    private String mId;

    @SerializedName("volumeInfo")
    @Expose
    private VolumeInfo mVolumeInfo;

    //--------------------------------------------------------
    // Constructors
    //--------------------------------------------------------
    public Book(String id, VolumeInfo volumeInfo) {
        mId = id;
        mVolumeInfo = volumeInfo;
    }

    protected Book(Parcel in) {
        mId = in.readString();
        mVolumeInfo = in.readParcelable(VolumeInfo.class.getClassLoader());
    }
    //========================================================

    //--------------------------------------------------------
    // Getter and Setter
    //--------------------------------------------------------
    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public VolumeInfo getVolumeInfo() {
        return mVolumeInfo;
    }

    public void setVolumeInfo(VolumeInfo volumeInfo) {
        mVolumeInfo = volumeInfo;
    }
    //========================================================

    //--------------------------------------------------------
    // Overriding methods
    //--------------------------------------------------------
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mId);
        dest.writeParcelable(mVolumeInfo, flags);
    }

    @Override
    public String toString() {
        return "Book{" +
                "id='" + mId + '\'' +
                ", volumeInfo=" + mVolumeInfo.toString() +
                '}';
    }
    //========================================================

}
