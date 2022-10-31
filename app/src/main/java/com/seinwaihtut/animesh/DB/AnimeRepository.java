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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AnimeRepository {
    private AnimeDao animeDao;
    private LiveData<List<Anime>> allAnime;
    private Integer pageNo = 1;

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

    public void update(Anime anime) {
        new UpdateAnimeAsyncTask(animeDao).execute(anime);
        //updateExecutor(animeDao, anime);
    }

    private static class UpdateAnimeAsyncTask extends AsyncTask<Anime, Void, Void> {
        private AnimeDao animeDao;

        private UpdateAnimeAsyncTask(AnimeDao dao) {
            animeDao = dao;
        }


        @Override
        protected Void doInBackground(Anime... anime) {
            animeDao.update(anime[0]);

            return null;
        }
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


    public MutableLiveData<List<Anime>> getJikanSeasonNow() {

        MutableLiveData<List<Anime>> currentSeason = new MutableLiveData<>();

        List<Anime> tempArrayList = new ArrayList<>();
        Gson gson = new GsonBuilder().serializeNulls().create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(JikanAPIEndpoint.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        JikanAPIEndpoint jikanAPIEndpoint = retrofit.create(JikanAPIEndpoint.class);

        Call<Jikanv4SeasonNowResponse> call = jikanAPIEndpoint.getSeasonNow(pageNo);
        call.enqueue(new Callback<Jikanv4SeasonNowResponse>() {
            @Override
            public void onResponse(Call<Jikanv4SeasonNowResponse> call, Response<Jikanv4SeasonNowResponse> response) {
                Jikanv4SeasonNowResponse jikanResponse = response.body();
                Boolean hasNextPage = false;
                if (jikanResponse.getPagination().getHasNextPage() != null) {
                    hasNextPage = jikanResponse.getPagination().getHasNextPage();
                }


                if (jikanResponse.getData() != null) {
                    for (Datum data : jikanResponse.getData()) {
                        if (data.getMalId() == null || data.getUrl() == null || data.getImages().getJpg().getImageUrl() == null || data.getTitle() == null) {
                            continue;
                        }
                        Anime anime = new Anime(data.getMalId(), data.getUrl(), data.getImages().getJpg().getImageUrl(), data.getTitle());
                        tempArrayList.add(anime);
                        //Log.i("anime repository", data.getTitle());
                    }
                }

                currentSeason.setValue(tempArrayList);
            }

            @Override
            public void onFailure(Call<Jikanv4SeasonNowResponse> call, Throwable t) {
                Log.e("AnimeRespository", "RetrofitJikanRequest:Failed");
            }
        });
        Log.i("31102022:repository", "getJikanSeasonNow");
        return currentSeason;
    }


    public MutableLiveData<List<Anime>> getTestJSON() {
        MutableLiveData<List<Anime>> testJSON = new MutableLiveData<>();

        List<Anime> tempArrayList = new ArrayList<>();
        Gson gson = new GsonBuilder().serializeNulls().create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(JikanAPIEndpoint.TEST_JSON_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        JikanAPIEndpoint jikanAPIEndpoint = retrofit.create(JikanAPIEndpoint.class);

        Call<Jikanv4SeasonNowResponse> call = jikanAPIEndpoint.getTestJSON();
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
                        tempArrayList.add(anime);
                        //Log.i("anime repository", data.getTitle());
                    }
                }
                Log.i("AnimeRepository:GetTestJSON", "finished");
                testJSON.setValue(tempArrayList);
            }

            @Override
            public void onFailure(Call<Jikanv4SeasonNowResponse> call, Throwable t) {
                Log.e("AnimeRespository", "RetrofitJikanRequest:Failed");
            }
        });
        return testJSON;
    }

    private void updateExecutor(AnimeDao dao, Anime anime) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                dao.update(anime);
            }
        });
        Log.i("31102022:repository", "updateExecutor");
    }

}

