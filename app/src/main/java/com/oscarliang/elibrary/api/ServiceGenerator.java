package com.oscarliang.elibrary.api;

import com.oscarliang.elibrary.util.Constant;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceGenerator {

    //--------------------------------------------------------
    // Static methods
    //--------------------------------------------------------
    private static final Retrofit RETROFIT = new Retrofit.Builder()
            .baseUrl(Constant.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    private static final BookService BOOKAPI = RETROFIT.create(BookService.class);

    public static BookService getBookApi() {
        return BOOKAPI;
    }
    //========================================================

}
