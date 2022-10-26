package com.seinwaihtut.animesh.DB;


import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.seinwaihtut.animesh.Retrofit.JikanAPIEndpoint;
import com.seinwaihtut.animesh.Retrofit.JikanResponsePOJOs.Datum;
import com.seinwaihtut.animesh.Retrofit.JikanResponsePOJOs.Jikanv4SeasonNowResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AnimeRepository {
    private AnimeDao animeDao;
    private LiveData<List<Anime>> allAnime;

    public AnimeRepository(Application application) {
        AnimeRoomDatabase database = AnimeRoomDatabase.getInstance(application);
        animeDao = database.animeDao();
        allAnime = animeDao.getFavAnime();
    }

    public void insert(Anime anime) {
        new InsertAnimeAsyncTask(animeDao).execute(anime);
    }

    public LiveData<List<Anime>> getAllAnime() {
        return allAnime;
    }

    public void delete(Anime anime) {
        new DeleteAnimeAsyncTask(animeDao).execute(anime);
    }


    private static class InsertAnimeAsyncTask extends AsyncTask<Anime, Void, Void> {
        private AnimeDao animeDao;

        private InsertAnimeAsyncTask(AnimeDao dao) {
            this.animeDao = dao;
        }

        @Override
        protected Void doInBackground(Anime... anime) {
            animeDao.insert(anime[0]);
            return null;
        }
    }

    private static class DeleteAnimeAsyncTask extends AsyncTask<Anime, Void, Void> {
        private AnimeDao animeDao;

        private DeleteAnimeAsyncTask(AnimeDao dao) {
            this.animeDao = dao;
        }

        @Override
        protected Void doInBackground(Anime... anime) {
            animeDao.delete(anime[0]);
            return null;
        }
    }


    public MutableLiveData<ArrayList<Anime>> getJikanSeasonNow() {
        MutableLiveData<ArrayList<Anime>> currentSeason = new MutableLiveData<>();
        ArrayList<Anime> arrayList = new ArrayList<>();
        Gson gson = new GsonBuilder().serializeNulls().create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(JikanAPIEndpoint.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        JikanAPIEndpoint jikanAPIEndpoint = retrofit.create(JikanAPIEndpoint.class);

        Call<Jikanv4SeasonNowResponse> call = jikanAPIEndpoint.getSeasonNow();
        call.enqueue(new Callback<Jikanv4SeasonNowResponse>() {
            @Override
            public void onResponse(Call<Jikanv4SeasonNowResponse> call, Response<Jikanv4SeasonNowResponse> response) {
                Jikanv4SeasonNowResponse jikanResponse = response.body();

                if (jikanResponse.getData() != null) {
                    for (Datum data : jikanResponse.getData()) {
                        if (data.getMalId() == null || data.getUrl() == null || data.getImages().getJpg().getImageUrl() == null || data.getTitle() == null) {
                            continue;
                        }
                        Anime anime = new Anime(data.getMalId(), data.getUrl(), data.getImages().getJpg().getImageUrl(), data.getTitle());
                        arrayList.add(anime);
                    }
                }
                currentSeason.setValue(arrayList);
            }

            @Override
            public void onFailure(Call<Jikanv4SeasonNowResponse> call, Throwable t) {
                Log.e("AnimeRespository", "RetrofitJikanRequest:Failed");
            }
        });
        return currentSeason;
    }


}

