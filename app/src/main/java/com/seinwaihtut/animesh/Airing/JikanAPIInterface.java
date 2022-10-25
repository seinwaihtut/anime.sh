package com.seinwaihtut.animesh.Airing;

import com.seinwaihtut.animesh.Airing.POJO.Jikanv4SeasonNowResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface JikanAPIInterface {
    String BASE_URL = "https://api.jikan.moe/v4/seasons/";
    @GET("now")
    Call<Jikanv4SeasonNowResponse> getSeasonNow();

}
