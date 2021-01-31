package com.e.wixmovies.viewmodel;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.e.wixmovies.model.MovieDO;
import com.e.wixmovies.repo.MovieRepository;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.CompositeDisposable;

public class MoviesViewModel extends ViewModel {

    private MovieRepository repository ;
    public MutableLiveData<String> errorMsg = new MutableLiveData<>();
    public MutableLiveData<List<MovieDO>> moviesList = new MutableLiveData<>();
    public MutableLiveData<List<MovieDO>> moviesWatchlist = new MutableLiveData<>();
    private int currentMoviesPage = 0;



    public MoviesViewModel() {
        super();
    }

    public void init(Application app) {
        repository = MovieRepository.getInstance(app);
    }

    public void refreshMovieList(LifecycleOwner cycleOwner, Context context, CompositeDisposable compositeDisposable) {
        currentMoviesPage = 0;
        repository.refreshMovieList(compositeDisposable, context).observe(cycleOwner, movies -> {
            if(movies.getErrorMsg() != null) {
                errorMsg.setValue(movies.getErrorMsg());
            }
            else {
                currentMoviesPage = movies.getMovieList().getPage();
                moviesList.setValue(movies.getMovieList().getMovies());
            }

        });
    }

    public void getMovies(LifecycleOwner cycleOwner, Context context, CompositeDisposable compositeDisposable) {
        currentMoviesPage = 0;
        repository.getMoviesList(compositeDisposable, context).observe(cycleOwner, movies -> {
            if(movies.getErrorMsg() != null) {
                errorMsg.setValue(movies.getErrorMsg());
            }
            else {
                currentMoviesPage = movies.getMovieList().getPage();
                moviesList.setValue(movies.getMovieList().getMovies());
            }

        });
    }

    public void loadMoreMovies(LifecycleOwner cycleOwner, Context context, CompositeDisposable compositeDisposable) {

        repository.getMoviesNextPage(compositeDisposable, context, currentMoviesPage + 1).observe(cycleOwner, movies -> {
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

    public void getWatchList(LifecycleOwner cycleOwner) {
        repository.getWatchList().observe(cycleOwner, movies -> {
            moviesWatchlist.setValue(movies);
        });
    }

    public boolean isOnWatchList(MovieDO movie) {
        return (getMovie(movie.getId()) != null);
    }

    public MovieDO getMovie(String id) {
        return repository.getMovie(id);
    }

    public void addToWatchlist(MovieDO movie) {
        movie.setOnWatchlist(true);
        List<MovieDO> watchlist = moviesWatchlist.getValue();
        if(watchlist == null) {
            watchlist = new ArrayList<>();
        }
        watchlist.add(movie);
        moviesWatchlist.setValue(watchlist);
        repository.addToWatchlist(movie);
    }

    public void removeFromWatchlist(MovieDO movie) {
        movie.setOnWatchlist(false);
        List<MovieDO> favMovies = moviesWatchlist.getValue();
        if(favMovies != null) {
            favMovies.remove(movie);
            moviesWatchlist.setValue(favMovies);
            repository.removeFromWatchlist(movie);
        }
    }
}