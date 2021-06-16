package com.seinwaihtut.animesh.DB;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface AnimeDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Anime anime);

    @Query("DELETE FROM anime_table")
    void deleteAll();

    @Delete
    void deleteAnime(Anime anime);

    @Query("SELECT * from anime_table LIMIT 1")
    Anime[] getAnyAnime();

    @Query("SELECT * from anime_table")
    LiveData<List<Anime>> getAllAnime();

//    @Query("SELECT mal_id from anime_table")
//    Anime getAnime(String mal_id);

    @Update
    void update(Anime... anime);
}
