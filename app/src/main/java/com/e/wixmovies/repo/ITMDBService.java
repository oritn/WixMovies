package com.e.wixmovies.repo;


import com.e.wixmovies.model.MoviesList;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Api service for the retrofit
 */
public interface ITMDBService
{
    @GET("movie/popular")
    Observable<MoviesList> getPopularMovies(@Query("api_key") String apiKey, @Query("page") int pageIndex);
}
