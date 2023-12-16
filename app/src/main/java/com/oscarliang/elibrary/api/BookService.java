package com.oscarliang.elibrary.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface BookService {

    @GET("/books/v1/volumes")
    Call<BookResponse> searchBook(
            @Query("q") String query,
            @Query("max_results") String maxResults,
            @Query("start_index") String startIndex);

}
