package com.cookandroid.finalproject2;

import com.cookandroid.finalproject2.BookResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
public interface GoogleBooksApi {
    @GET("volumes")
    Call<BookResponse> getBestSellers(@Query("q") String query, @Query("key") String apiKey);
}