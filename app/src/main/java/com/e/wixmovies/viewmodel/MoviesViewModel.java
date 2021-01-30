package com.e.wixmovies.viewmodel;

import android.content.Context;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.e.wixmovies.repo.MovieRepository;
import com.e.wixmovies.model.MovieDO;

import java.util.ArrayList;
import java.util.List;

public class MoviesViewModel extends ViewModel {

    private MovieRepository repository = MovieRepository.getInstance();
    public MutableLiveData<String> errorMsg = new MutableLiveData<>();
    public MutableLiveData<List<MovieDO>> moviesList = new MutableLiveData<>();
    public MutableLiveData<List<MovieDO>> favMoviesList = new MutableLiveData<>();
    private int currentMoviesPage = 0;


    public void getMovies(LifecycleOwner cycleOwner, Context context) {
        currentMoviesPage = 0;
        repository.getMoviesList(context).observe(cycleOwner, movies -> {
            if(movies.getErrorMsg() != null) {
                errorMsg.setValue(movies.getErrorMsg());
            }
            else {
                currentMoviesPage = movies.getMovieList().getPage();
                moviesList.setValue(movies.getMovieList().getMovies());
            }

        });
    }

    public void loadMoreMovies(LifecycleOwner cycleOwner, Context context) {

        repository.getMoviesNextPage(context, currentMoviesPage + 1).observe(cycleOwner, movies -> {
            if(movies.getErrorMsg() != null) {
                errorMsg.setValue(movies.getErrorMsg());
            }
            else {
                currentMoviesPage = movies.getMovieList().getPage();
                List<MovieDO> currentMovies = moviesList.getValue();
                currentMovies.addAll(movies.getMovieList().getMovies());
                moviesList.setValue(currentMovies);
            }

        });
    }

    public List<MovieDO> getFavoriteMovies() {
        return null;
    }

    public void addToFavorite(MovieDO movie) {
        List<MovieDO> favMovies = favMoviesList.getValue();
        if(favMovies == null) {
            favMovies = new ArrayList<>();
        }
        favMovies.add(movie);
        favMoviesList.setValue(favMovies);
    }

    public void removeFromFavorite(MovieDO movie) {
        List<MovieDO> favMovies = favMoviesList.getValue();
        if(favMovies != null) {
            favMovies.remove(movie);
            favMoviesList.setValue(favMovies);
        }
    }
}