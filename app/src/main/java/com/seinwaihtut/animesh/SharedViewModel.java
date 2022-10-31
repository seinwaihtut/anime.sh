package com.seinwaihtut.animesh;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.seinwaihtut.animesh.DB.Anime;
import com.seinwaihtut.animesh.DB.AnimeRepository;

import java.util.ArrayList;
import java.util.List;

public class SharedViewModel extends AndroidViewModel {
    private AnimeRepository repository;
    private LiveData<List<Anime>> currentSeason;
    private LiveData<List<Anime>> allAnimeWatching;

    private LiveData<List<Anime>> testJSON;

    public SharedViewModel(@NonNull Application application) {
        super(application);

        repository = new AnimeRepository(application);

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

    public LiveData<List<Anime>> getTestJSON(){return this.repository.getTestJSON();}

    public void update(Anime anime){this.repository.update(anime);}
}
