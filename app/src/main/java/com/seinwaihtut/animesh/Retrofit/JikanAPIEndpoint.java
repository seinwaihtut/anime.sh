package com.seinwaihtut.animesh.Retrofit;

import com.seinwaihtut.animesh.Retrofit.JikanResponsePOJOs.Jikanv4SeasonNowResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface JikanAPIEndpoint {
    String BASE_URL = "https://api.jikan.moe/v4/seasons/";

    String TEST_JSON_URL = "https://www.seinwaihtut.com/json/";

    @GET("now")
    Call<Jikanv4SeasonNowResponse> getSeasonNow(@Query("page") Integer pageNo);

    @GET("page1.json")
    Call<Jikanv4SeasonNowResponse> getTestJSON();
}
