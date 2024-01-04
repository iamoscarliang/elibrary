package com.oscarliang.elibrary.vo;

public class Category {

    private String mName;
    private int mThumbnail;

    //--------------------------------------------------------
    // Constructors
    //--------------------------------------------------------
    public Category(String name, int thumbnail) {
        mName = name;
        mThumbnail = thumbnail;
    }
    //========================================================

    //--------------------------------------------------------
    // Getter and Setter
    //--------------------------------------------------------
    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public int getThumbnail() {
        return mThumbnail;
    }

    public void setThumbnail(int thumbnail) {
        mThumbnail = thumbnail;
    }
    //========================================================
}
