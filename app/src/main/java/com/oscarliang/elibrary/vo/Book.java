package com.oscarliang.elibrary.vo;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "books")
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

    @PrimaryKey()
    @ColumnInfo(name = "id")
    @NonNull
    @SerializedName("id")
    @Expose
    private String mId;

    @Embedded
    @SerializedName("volumeInfo")
    @Expose
    private VolumeInfo mVolumeInfo;

    @ColumnInfo(name = "category")
    private String mCategory;

    //--------------------------------------------------------
    // Constructors
    //--------------------------------------------------------
    public Book(String id, VolumeInfo volumeInfo, String category) {
        mId = id;
        mVolumeInfo = volumeInfo;
        mCategory = category;
    }

    protected Book(Parcel in) {
        mId = in.readString();
        mVolumeInfo = in.readParcelable(VolumeInfo.class.getClassLoader());
        mCategory = in.readString();
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

    public String getCategory() {
        return mCategory;
    }

    public void setCategory(String category) {
        mCategory = category;
    }
    //========================================================

    //--------------------------------------------------------
    // Overriding methods
    //--------------------------------------------------------
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mId);
        dest.writeParcelable(mVolumeInfo, flags);
        dest.writeString(mCategory);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id='" + mId + '\'' +
                ", volumeInfo=" + mVolumeInfo.toString() +
                ", category='" + mCategory + '\'' +
                '}';
    }
    //========================================================

}
