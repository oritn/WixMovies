package com.e.wixmovies.repo;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;

import com.e.wixmovies.BuildConfig;
import com.e.wixmovies.model.MoviesList;
import com.e.wixmovies.model.MoviesListWrapper;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieRepository {

    //https://api.themoviedb.org/3/movie/popular?api_key=40ab4b29399a2e3f961acf68acc457e8&language=en-US&page=1

    private static MovieRepository instance;

    private MovieRepository() { }

    public static MovieRepository getInstance() {
        if(instance == null) {
            instance = new MovieRepository();
        }
        return instance;
    }

    public MutableLiveData<MoviesListWrapper> getMoviesList(Context context) {
      return getMoviesNextPage(context, 1);
    }

    public MutableLiveData<MoviesListWrapper> getMoviesNextPage(Context context, int page) {
        ITMDBService apiService = RetrofitInstance.getTMDBService(context);
        MutableLiveData<MoviesListWrapper>   moviesMutableLiveData = new MutableLiveData<>();
        apiService.getPopularMovies(BuildConfig.ApiKey, page).enqueue(new Callback<MoviesList>() {
            @Override
            public void onResponse(Call<MoviesList> call, Response<MoviesList> response) {
                if (response.isSuccessful() && response.body() != null) {
                    moviesMutableLiveData.setValue(new MoviesListWrapper(response.body(), null));
                } else {
                    moviesMutableLiveData.setValue(new MoviesListWrapper(null, response.message()));
                }
            }

            @Override
            public void onFailure(Call<MoviesList> call, Throwable t) {
                moviesMutableLiveData.setValue(new MoviesListWrapper(null, t.getMessage()));
            }
        });

        return moviesMutableLiveData;
    }


}
