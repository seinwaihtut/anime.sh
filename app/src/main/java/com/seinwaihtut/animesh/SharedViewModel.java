package com.seinwaihtut.animesh;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.seinwaihtut.animesh.DB.Anime;
import com.seinwaihtut.animesh.DB.EpisodePOJO;
import com.seinwaihtut.animesh.DB.Repository;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class SharedViewModel extends AndroidViewModel {
    private Repository repository;
    private LiveData<List<Anime>> currentSeason;
    private LiveData<List<Anime>> allAnimeWatching;

    private LiveData<List<Anime>> testJSON;

    public SharedViewModel(@NonNull Application application) {
        super(application);

        repository = new Repository(application);

        allAnimeWatching = repository.getAllAnime();
        currentSeason = repository.getJikanSeasonNow();

    }



    public LiveData<List<Anime>> getCurrentSeason(){
        return this.currentSeason;
    }

    public LiveData<List<Anime>> getAllAnimeWatching(){
        return this.allAnimeWatching;
    }

    public void insert(Anime anime){
        this.repository.insert(anime);
    }

    public void delete(Anime anime){
        this.repository.delete(anime);
    }

//    public LiveData<List<Anime>> getTestJSON(){return this.repository.getTestJSON();}

    public void update(Anime anime){this.repository.update(anime);}

    public List<EpisodePOJO> getNyaa(String queryString) throws ExecutionException, InterruptedException {
        return repository.getNyaaAsyncTask(queryString);
    }

    public LiveData<Anime> queryAnimeInDB(Integer mal_id){
        return repository.queryAnimeInDB(mal_id);
    }

}
