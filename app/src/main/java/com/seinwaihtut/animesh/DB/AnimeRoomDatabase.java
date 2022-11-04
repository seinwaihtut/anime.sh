package com.seinwaihtut.animesh.DB;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Anime.class}, version = 4, exportSchema = false)
public abstract class AnimeRoomDatabase extends RoomDatabase {

    private static AnimeRoomDatabase instance;

    public abstract AnimeDao animeDao();

    public static synchronized AnimeRoomDatabase getInstance(Context context){
        if (instance==null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    AnimeRoomDatabase.class, "anime_room_database"
            ).fallbackToDestructiveMigration().build();
        }

        return instance;
    }
}
