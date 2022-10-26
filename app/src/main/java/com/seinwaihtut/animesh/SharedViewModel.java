package com.seinwaihtut.animesh;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.seinwaihtut.animesh.DB.Anime;
import com.seinwaihtut.animesh.DB.AnimeRepository;

import java.util.ArrayList;

public class SharedViewModel extends AndroidViewModel {
    private AnimeRepository repository;
    private LiveData<ArrayList<Anime>> currentSeason;

    public SharedViewModel(@NonNull Application application) {
        super(application);

        repository = new AnimeRepository(application);
        currentSeason = repository.getJikanSeasonNow();
    }

    public LiveData<ArrayList<Anime>> getCurrentSeason(){
        return this.currentSeason;
    }

}
