package com.e.wixmovies.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.e.wixmovies.model.MovieDO;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface WatchlistMoviesDAO {

    @Insert(onConflict = REPLACE)
    void insertMovie(MovieDO movie);

    @Delete
    void deleteMovie(MovieDO movie);

    @Query("select * from watchlist")
    LiveData<List<MovieDO>> getAllWatchlist();

    @Query("select * from watchlist where id==:id")
    MovieDO getMovie(String id);


}
