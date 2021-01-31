package com.e.wixmovies.db;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.e.wixmovies.model.MovieDO;


@androidx.room.Database(entities = MovieDO.class, version = 2)
public abstract class MoviesDatabase extends RoomDatabase {
    public abstract WatchlistMoviesDAO getFDAO();

    private static MoviesDatabase instance;

    public static synchronized MoviesDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), MoviesDatabase.class, "TMDB")
                    .addCallback(callback).allowMainThreadQueries()
                    .build();
        }
        return instance;
    }

    private static RoomDatabase.Callback callback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
        }
    };
}
