package com.oscarliang.elibrary.api;

import com.oscarliang.elibrary.util.Constant;
import com.oscarliang.elibrary.util.LiveDataCallAdapterFactory;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceGenerator {

    //--------------------------------------------------------
    // Static methods
    //--------------------------------------------------------
    private static final Retrofit RETROFIT = new Retrofit.Builder()
            .baseUrl(Constant.BASE_URL)
            .addCallAdapterFactory(new LiveDataCallAdapterFactory())
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    private static final BookService BOOK_SERVICE = RETROFIT.create(BookService.class);

    public static BookService getBookService() {
        return BOOK_SERVICE;
    }
    //========================================================

}
