package com.seinwaihtut.animesh.DB;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class AnimeViewModel extends AndroidViewModel {
    private AnimeRepository mRepository;
    private LiveData<List<Anime>> mAllAnime;
    private LiveData<List<Anime>> testJSON;


    public AnimeViewModel(Application application) {
        super(application);
        mRepository = new AnimeRepository(application);
        mAllAnime = mRepository.getAllAnime();
        testJSON = mRepository.getTestJSON();
    }

    public LiveData<List<Anime>> getAllAnime() {
        return mAllAnime;
    }

    public void insert(Anime anime) {
        mRepository.insert(anime);
    }

    public void deleteAnime(Anime anime) {
        mRepository.delete(anime);
    }

    public LiveData<List<Anime>> getTestJSON(){return this.testJSON;}
}

