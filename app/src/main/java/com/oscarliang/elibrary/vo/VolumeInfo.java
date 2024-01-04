package com.oscarliang.elibrary.vo;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.TypeConverters;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.oscarliang.elibrary.db.BookConverter;

import java.util.List;

@Entity(tableName = "volume_info")
public class VolumeInfo implements Parcelable {

    public static final Creator<VolumeInfo> CREATOR = new Creator<VolumeInfo>() {
        @Override
        public VolumeInfo createFromParcel(Parcel in) {
            return new VolumeInfo(in);
        }

        @Override
        public VolumeInfo[] newArray(int size) {
            return new VolumeInfo[size];
        }
    };

    @ColumnInfo(name = "title")
    @SerializedName("title")
    @Expose
    private String mTitle;

    @ColumnInfo(name = "subtitle")
    @SerializedName("subtitle")
    @Expose
    private String mSubtitle;

    @ColumnInfo(name = "authors")
    @TypeConverters(BookConverter.class)
    @SerializedName("authors")
    @Expose
    private List<String> mAuthors;

    @ColumnInfo(name = "publisher")
    @SerializedName("publisher")
    @Expose
    private String mPublisher;

    @ColumnInfo(name = "publishedDate")
    @SerializedName("publishedDate")
    @Expose
    private String mPublishedDate;

    @ColumnInfo(name = "averageRating")
    @SerializedName("averageRating")
    @Expose
    private Float mAverageRating;

    @ColumnInfo(name = "ratingsCount")
    @SerializedName("ratingsCount")
    @Expose
    private Integer mRatingsCount;

    @ColumnInfo(name = "pageCount")
    @SerializedName("pageCount")
    @Expose
    private Integer mPageCount;

    @ColumnInfo(name = "description")
    @SerializedName("description")
    @Expose
    private String mDescription;

    @ColumnInfo(name = "previewLink")
    @SerializedName("previewLink")
    @Expose
    private String mPreviewLink;

    @ColumnInfo(name = "infoLink")
    @SerializedName("infoLink")
    @Expose
    private String mInfoLink;

    @Embedded
    @SerializedName("imageLinks")
    @Expose
    private ImageLinks mImageLinks;

    //--------------------------------------------------------
    // Constructors
    //--------------------------------------------------------
    public VolumeInfo(String title,
                      String subtitle,
                      List<String> authors,
                      String publisher,
                      String publishedDate,
                      Float averageRating,
                      Integer ratingsCount,
                      Integer pageCount,
                      String description,
                      String previewLink,
                      String infoLink,
                      ImageLinks imageLinks) {
        mTitle = title;
        mSubtitle = subtitle;
        mAuthors = authors;
        mPublisher = publisher;
        mPublishedDate = publishedDate;
        mAverageRating = averageRating;
        mRatingsCount = ratingsCount;
        mPageCount = pageCount;
        mDescription = description;
        mPreviewLink = previewLink;
        mInfoLink = infoLink;
        mImageLinks = imageLinks;
    }

    protected VolumeInfo(Parcel in) {
        mTitle = in.readString();
        mSubtitle = in.readString();
        mAuthors = in.createStringArrayList();
        mPublisher = in.readString();
        mPublishedDate = in.readString();
        if (in.readByte() == 0) {
            mAverageRating = null;
        } else {
            mAverageRating = in.readFloat();
        }
        if (in.readByte() == 0) {
            mRatingsCount = null;
        } else {
            mRatingsCount = in.readInt();
        }
        if (in.readByte() == 0) {
            mPageCount = null;
        } else {
            mPageCount = in.readInt();
        }
        mDescription = in.readString();
        mPreviewLink = in.readString();
        mInfoLink = in.readString();
        mImageLinks = in.readParcelable(ImageLinks.class.getClassLoader());
    }
    //========================================================

    //--------------------------------------------------------
    // Getter and Setter
    //--------------------------------------------------------
    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getSubtitle() {
        return mSubtitle;
    }

    public void setSubtitle(String subtitle) {
        mSubtitle = subtitle;
    }

    public List<String> getAuthors() {
        return mAuthors;
    }

    public void setAuthors(List<String> authors) {
        mAuthors = authors;
    }

    public String getPublisher() {
        return mPublisher;
    }

    public void setPublisher(String publisher) {
        mPublisher = publisher;
    }

    public String getPublishedDate() {
        return mPublishedDate;
    }

    public void setPublishedDate(String publishedDate) {
        mPublishedDate = publishedDate;
    }

    public Float getAverageRating() {
        return mAverageRating;
    }

    public void setAverageRating(Float averageRating) {
        mAverageRating = averageRating;
    }

    public Integer getRatingsCount() {
        return mRatingsCount;
    }

    public void setRatingsCount(Integer ratingsCount) {
        mRatingsCount = ratingsCount;
    }

    public Integer getPageCount() {
        return mPageCount;
    }

    public void setPageCount(Integer pageCount) {
        mPageCount = pageCount;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public String getPreviewLink() {
        return mPreviewLink;
    }

    public void setPreviewLink(String previewLink) {
        mPreviewLink = previewLink;
    }

    public String getInfoLink() {
        return mInfoLink;
    }

    public void setInfoLink(String infoLink) {
        mInfoLink = infoLink;
    }

    public ImageLinks getImageLinks() {
        return mImageLinks;
    }

    public void setImageLinks(ImageLinks imageLinks) {
        mImageLinks = imageLinks;
    }
    //========================================================

    //--------------------------------------------------------
    // Overriding methods
    //--------------------------------------------------------
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mTitle);
        dest.writeString(mSubtitle);
        dest.writeStringList(mAuthors);
        dest.writeString(mPublisher);
        dest.writeString(mPublishedDate);
        if (mAverageRating == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeFloat(mAverageRating);
        }
        if (mRatingsCount == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(mRatingsCount);
        }
        if (mPageCount == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(mPageCount);
        }
        dest.writeString(mDescription);
        dest.writeString(mPreviewLink);
        dest.writeString(mInfoLink);
        dest.writeParcelable(mImageLinks, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public String toString() {
        return "VolumeInfo{" +
                "title='" + mTitle + '\'' +
                ", subtitle='" + mSubtitle + '\'' +
                ", authors=" + mAuthors +
                ", publisher='" + mPublisher + '\'' +
                ", publishedDate='" + mPublishedDate + '\'' +
                ", averageRating=" + mAverageRating +
                ", ratingsCount=" + mRatingsCount +
                ", pageCount=" + mPageCount +
                ", description='" + mDescription + '\'' +
                ", previewLink='" + mPreviewLink + '\'' +
                ", infoLink='" + mInfoLink + '\'' +
                ", imageLinks=" + mImageLinks.toString() +
                '}';
    }
    //========================================================

}
