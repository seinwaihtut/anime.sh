package com.seinwaihtut.animesh.Retrofit;

import com.seinwaihtut.animesh.Retrofit.JikanResponsePOJOs.Jikanv4SeasonNowResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface JikanAPIEndpoint {
    String BASE_URL = "https://api.jikan.moe/v4/seasons/";
    @GET("now")
    Call<Jikanv4SeasonNowResponse> getSeasonNow();
}
