package com.oscarliang.elibrary.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.oscarliang.elibrary.model.Book;

import java.util.List;

public class BookResponse {

    @SerializedName("items")
    @Expose()
    private List<Book> mBooks;

    //--------------------------------------------------------
    // Getter and Setter
    //--------------------------------------------------------
    public List<Book> getBooks() {
        return mBooks;
    }
    //========================================================

}
