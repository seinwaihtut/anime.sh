package com.seinwaihtut.animesh.DB;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class AnimeRepository {
    private AnimeDao mAnimeDao;
    private LiveData<List<Anime>> mAllAnime;

    AnimeRepository(Application application) {
        AnimeRoomDatabase db = AnimeRoomDatabase.getDatabase(application);
        mAnimeDao = db.animeDao();
        mAllAnime = mAnimeDao.getAllAnime();
    }

    LiveData<List<Anime>> getAllAnime() {
        return mAllAnime;
    }

    public void insert(Anime anime) {
        new insertAsyncTask(mAnimeDao).execute(anime);
    }

    public void deleteAnime(Anime anime) {
        new deleteAnimeAsyncTask(mAnimeDao).execute(anime);
    }

    private static class insertAsyncTask extends AsyncTask<Anime, Void, Void> {

        private AnimeDao mAsyncTaskDao;

        insertAsyncTask(AnimeDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Anime... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    private static class deleteAnimeAsyncTask extends AsyncTask<Anime, Void, Void> {
        private AnimeDao mAsyncTaskDao;

        deleteAnimeAsyncTask(AnimeDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Anime... params) {
            mAsyncTaskDao.deleteAnime(params[0]);
            return null;
        }
    }

//    private static class getAnimeAsyncTask extends AsyncTask<Anime, Void, Void>{
//        private AnimeDao mAsyncTaskDao;
//
//        getAnimeAsyncTask(AnimeDao dao){mAsyncTaskDao = dao}
//
//        @Override
//        protected Void doInBackground(Anime... anime) {
//            mAsyncTaskDao.getAnime(anime[0].getTitle());
//            return null;
//        }
//    }
}
