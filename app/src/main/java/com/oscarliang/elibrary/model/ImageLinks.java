package com.oscarliang.elibrary.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ImageLinks implements Parcelable {

    public static final Creator<ImageLinks> CREATOR = new Creator<ImageLinks>() {
        @Override
        public ImageLinks createFromParcel(Parcel in) {
            return new ImageLinks(in);
        }

        @Override
        public ImageLinks[] newArray(int size) {
            return new ImageLinks[size];
        }
    };

    @SerializedName("thumbnail")
    @Expose
    private String mThumbnail;

    //--------------------------------------------------------
    // Constructors
    //--------------------------------------------------------
    public ImageLinks(String thumbnail) {
        mThumbnail = thumbnail;
    }

    protected ImageLinks(Parcel in) {
        mThumbnail = in.readString();
    }
    //========================================================

    //--------------------------------------------------------
    // Getter and Setter
    //--------------------------------------------------------
    public String getThumbnail() {
        return mThumbnail;
    }

    public void setThumbnail(String thumbnail) {
        mThumbnail = thumbnail;
    }
    //========================================================

    //--------------------------------------------------------
    // Overriding methods
    //--------------------------------------------------------
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mThumbnail);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public String toString() {
        return "ImageLinks{" +
                "thumbnail='" + mThumbnail + '\'' +
                '}';
    }
    //========================================================

}
