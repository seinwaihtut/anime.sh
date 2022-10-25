package com.seinwaihtut.animesh.Airing;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClientAiring {
    private static RetrofitClientAiring instance = null;
    private JikanAPIInterface jikanAPIInterface;
    GsonBuilder gsonBuilder = new GsonBuilder().serializeNulls();
    Gson gson = gsonBuilder.create();

    private RetrofitClientAiring(){
        Retrofit retrofit = new Retrofit.Builder().baseUrl(jikanAPIInterface.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        jikanAPIInterface = retrofit.create(JikanAPIInterface.class);
    }
    public static synchronized RetrofitClientAiring getInstance(){
        if(instance==null){
            instance = new RetrofitClientAiring();
        }
        return instance;
    }
    public JikanAPIInterface getJikanAPIInterface(){return jikanAPIInterface;}

}
