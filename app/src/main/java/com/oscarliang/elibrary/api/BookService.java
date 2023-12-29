package com.oscarliang.elibrary.api;

import androidx.lifecycle.LiveData;

import retrofit2.http.GET;
import retrofit2.http.Query;

public interface BookService {

    @GET("/books/v1/volumes")
    LiveData<ApiResponse<BookResponse>> getBooks(
            @Query("q") String query,
            @Query("max_results") String maxResults,
            @Query("start_index") String startIndex);

}
