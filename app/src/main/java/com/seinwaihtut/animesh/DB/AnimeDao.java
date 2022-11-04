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

    @Update
    void update(Anime anime);

    @Delete
    void delete(Anime anime);

    @Query("DELETE FROM anime_table")
    void deleteAll();

    @Query("SELECT * FROM anime_table")
    LiveData<List<Anime>> getFavAnime();

    @Query("SELECT * FROM anime_table where anime_table.mal_id = :mal_id")
    LiveData<Anime> getAnime(Integer mal_id);

}
