package com.seinwaihtut.animesh.DB;
// TODO Remove redundant and unused codes

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.seinwaihtut.animesh.Retrofit.APIEndpoint;
import com.seinwaihtut.animesh.Retrofit.JikanResponsePOJOs.Datum;
import com.seinwaihtut.animesh.Retrofit.JikanResponsePOJOs.Genre;
import com.seinwaihtut.animesh.Retrofit.JikanResponsePOJOs.Jikanv4SeasonNowResponse;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.nio.channels.AsynchronousChannelGroup;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Repository {
    private AnimeDao animeDao;
    private LiveData<List<Anime>> allAnime;
    private Integer pageNo = 1;

    public Repository(Application application) {
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

    public LiveData queryAnimeInDB(Integer mal_id) {
        return animeDao.getAnime(mal_id);
    }


//    private static class QueryAnime extends AsyncTask<Integer, Void, LiveData>{
//        private AnimeDao animeDao;
//        private QueryAnime(AnimeDao dao){
//            animeDao = dao;
//        }
//
//        @Override
//        protected LiveData<Anime> doInBackground(Integer... integers) {
//            return animeDao.getAnime(integers[0]);
//        }
//    }


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

    public List<EpisodePOJO> getNyaaAsyncTask(String queryString) throws ExecutionException, InterruptedException {
        return new NyaaAsyncTask().execute().get();
    }


    private static class NyaaAsyncTask extends AsyncTask<Void, Void, List<EpisodePOJO>> {
        private NyaaAsyncTask() {
            //Constructor
        }

        @Override
        protected List<EpisodePOJO> doInBackground(Void... voids) {
            List<EpisodePOJO> episodeList = new ArrayList<>();

            try {
                Document document = Jsoup.connect("https://nyaa.si/?f=0&c=1_2&q=yourself").get();
                Elements rows = document.select("table tbody tr");
                for (Element row : rows) {
                    String nyaa_url = row.select("td:nth-of-type(2)>a:last-of-type").attr("href");
                    String title = row.select("td:nth-of-type(2)>a:last-of-type").text();
                    String torrent_url = row.select("td:nth-of-type(3) > a:first-of-type").attr("href");
                    String magnet_url = row.select("td:nth-of-type(3) > a:last-of-type").attr("href");
                    String upload_time = row.select("td:nth-of-type(5)").text();
                    String size = row.select("td:nth-of-type(4)").text();
                    Integer seeders = Integer.valueOf(row.select("td:nth-of-type(6)").text());
                    Integer leechers = Integer.valueOf(row.select("td:nth-of-type(7").text());
                    Integer completed_downloads = Integer.valueOf(row.select("td:nth-of-type(8)").text());

                    EpisodePOJO episodePOJO = new EpisodePOJO(title, magnet_url);
                    episodePOJO.setNyaa_url(nyaa_url);
                    episodePOJO.setTorrent_url(torrent_url);
                    episodePOJO.setUpload_time(upload_time);
                    episodePOJO.setSize(size);
                    episodePOJO.setSeeders(seeders);
                    episodePOJO.setLeechers(leechers);
                    episodePOJO.setCompleted_downloads(completed_downloads);
                    episodeList.add(episodePOJO);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return episodeList;
        }
    }



    public MutableLiveData<List<Anime>> getJikanSeasonNow() {

        MutableLiveData<List<Anime>> currentSeason = new MutableLiveData<>();

        List<Anime> tempArrayList = new ArrayList<>();
        Gson gson = new GsonBuilder().serializeNulls().create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(APIEndpoint.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        APIEndpoint APIEndpoint = retrofit.create(APIEndpoint.class);

        Call<Jikanv4SeasonNowResponse> call = APIEndpoint.getSeasonNow(pageNo);
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

                        if (data.getScore() != null) {
                            anime.setScore(data.getScore());
                        } else {
                            anime.setScore(Float.valueOf(0));
                        }

                        if (data.getSynopsis() != null) {
                            anime.setSynopsis(data.getSynopsis());
                        } else {
                            anime.setSynopsis("");
                        }
                        if (data.getTitleJapanese() != null) {
                            anime.setTitle_jp(data.getTitleJapanese());
                        } else {
                            anime.setTitle_jp("");
                        }
                        if (data.getType() != null) {
                            anime.setType(data.getType());
                        } else anime.setType("");
                        if (data.getSource() != null) {
                            anime.setSource(data.getSource());
                        } else anime.setType("");
                        if (data.getEpisodes() != null) {
                            anime.setEpisodes(data.getEpisodes());
                        } else anime.setEpisodes(0);
                        if (data.getAired().getString() != null) {
                            anime.setAired_string(data.getAired().getString());
                            //Log.i("Repository", data.getAired().getString());
                        } else anime.setAired_string("");
                        if (data.getSeason() != null) {
                            anime.setSeason(data.getSeason());
                        } else anime.setSeason("");
                        if (data.getYear() != null) {
                            anime.setYear(data.getYear());
                        } else anime.setYear(0);
                        if (data.getBroadcast().getString() != null) {
                            anime.setBroadcast_string(data.getBroadcast().getString());
                        } else anime.setBroadcast_string("");
                        if (data.getGenres() != null) {
                            List<String> genres = new ArrayList<>();
                            for (Genre genre : data.getGenres()) {
                                genres.add(genre.getName());
                            }
                            anime.setGenres(String.join(", ", genres));
                        }
                    }
                }
                currentSeason.setValue(tempArrayList);
            }


            @Override
            public void onFailure(Call<Jikanv4SeasonNowResponse> call, Throwable t) {
                Log.e("AnimeRespository", "RetrofitJikanRequest:Failed");
            }
        });
        return currentSeason;
    }



//
//    public MutableLiveData<List<Anime>> getTestJSON() {
//        MutableLiveData<List<Anime>> testJSON = new MutableLiveData<>();
//
//        List<Anime> tempArrayList = new ArrayList<>();
//        Gson gson = new GsonBuilder().serializeNulls().create();
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(JikanAPIEndpoint.TEST_JSON_URL)
//                .addConverterFactory(GsonConverterFactory.create(gson))
//                .build();
//
//        JikanAPIEndpoint jikanAPIEndpoint = retrofit.create(JikanAPIEndpoint.class);
//
//        Call<Jikanv4SeasonNowResponse> call = jikanAPIEndpoint.getTestJSON();
//        call.enqueue(new Callback<Jikanv4SeasonNowResponse>() {
//            @Override
//            public void onResponse(Call<Jikanv4SeasonNowResponse> call, Response<Jikanv4SeasonNowResponse> response) {
//                Jikanv4SeasonNowResponse jikanResponse = response.body();
//
//                if (jikanResponse.getData() != null) {
//                    for (Datum data : jikanResponse.getData()) {
//                        if (data.getMalId() == null || data.getUrl() == null || data.getImages().getJpg().getImageUrl() == null || data.getTitle() == null) {
//                            continue;
//                        }
//                        Anime anime = new Anime(data.getMalId(), data.getUrl(), data.getImages().getJpg().getImageUrl(), data.getTitle());
//                        tempArrayList.add(anime);
//                        //Log.i("anime repository", data.getTitle());
//                    }
//                }
//                Log.i("AnimeRepository:GetTestJSON", "finished");
//                testJSON.setValue(tempArrayList);
//            }
//
//            @Override
//            public void onFailure(Call<Jikanv4SeasonNowResponse> call, Throwable t) {
//                Log.e("AnimeRespository", "RetrofitJikanRequest:Failed");
//            }
//        });
//        return testJSON;
//    }
//
//    private void updateExecutor(AnimeDao dao, Anime anime) {
//        ExecutorService executor = Executors.newSingleThreadExecutor();
//        executor.execute(new Runnable() {
//            @Override
//            public void run() {
//                dao.update(anime);
//            }
//        });
//        Log.i("31102022:repository", "updateExecutor");
//    }

    public void getFromNyaa(String queryString) {
        String BASE_URL = "https://nyaa.si/";
        OkHttpClient client = new OkHttpClient();

        HttpUrl.Builder urlBuilder = HttpUrl.parse(BASE_URL)
                .newBuilder();
        urlBuilder.addQueryParameter("f", String.valueOf(0));
        urlBuilder.addQueryParameter("c", "1_2");
        urlBuilder.addQueryParameter("q", queryString);
        urlBuilder.addQueryParameter("p", String.valueOf(1));

        String url = urlBuilder.build().toString();

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(@NonNull okhttp3.Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull okhttp3.Call call, @NonNull okhttp3.Response response) throws IOException {
                if (response.isSuccessful()) {
                    Log.i("Repository", response.body().string());
                }
            }
        });

    }
}

