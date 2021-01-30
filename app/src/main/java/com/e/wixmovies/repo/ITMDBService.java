package com.e.wixmovies.repo;


import com.e.wixmovies.model.MoviesList;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Api service for the retfit
 */
public interface ITMDBService
{
    @GET("movie/popular")
    Call<MoviesList> getPopularMovies(@Query("api_key") String apiKey, @Query("page") int pageIndex);
}
