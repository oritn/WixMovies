package com.e.wixmovies.repo;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.e.wixmovies.BuildConfig;
import com.e.wixmovies.db.MoviesDatabase;
import com.e.wixmovies.db.WatchlistMoviesDAO;
import com.e.wixmovies.model.MovieDO;
import com.e.wixmovies.model.MoviesList;
import com.e.wixmovies.model.MoviesListWrapper;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class MovieRepository {

    private WatchlistMoviesDAO watchListDB;

    private static MovieRepository instance;

    private MovieRepository(Application application) {
        MoviesDatabase database = MoviesDatabase.getInstance(application);
        watchListDB = database.getFDAO();
    }

    public static MovieRepository getInstance(Application application) {
        if(instance == null) {
            instance = new MovieRepository(application);
        }
        return instance;
    }

    /**
     * refresh the movies data by getting the movies from the TMBD server
     * @param compositeDisposable
     * @param context
     * @return live data to obser for changes
     */
    public MutableLiveData<MoviesListWrapper> refreshMovieList(CompositeDisposable compositeDisposable, Context context) {
        TMDBRetrofitInstance.resetCache();
        return getMoviesList(compositeDisposable, context);
    }

    /**
     * get the movie list from the first page, it will get it from cache if exists
     * @param compositeDisposable
     * @param context
     * @return live data to obser for changes
     */
    public MutableLiveData<MoviesListWrapper> getMoviesList(CompositeDisposable compositeDisposable, Context context) {
      return getMoviesNextPage(compositeDisposable, context, 1);
    }

    /**
     * get the movie list according to the provided page num, it will get it from cache if exists
     * @param compositeDisposable
     * @param context
     * @param page
     * @return live data to obser for changes
     */
    public MutableLiveData<MoviesListWrapper> getMoviesNextPage(CompositeDisposable compositeDisposable, Context context, int page) {
        ITMDBService apiService = TMDBRetrofitInstance.getTMDBService(context);
        MutableLiveData<MoviesListWrapper> moviesMutableLiveData = new MutableLiveData<>();
        Observable<MoviesList> observable =  apiService.getPopularMovies(BuildConfig.ApiKey, page);
        compositeDisposable.add(observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(new DisposableObserver<MoviesList>() {

                   @Override
                   public void onNext(MoviesList value) {
                       if (value != null) {
                           moviesMutableLiveData.setValue(new MoviesListWrapper(value, null));
                       } else {
                           moviesMutableLiveData.setValue(new MoviesListWrapper(null, "an error accord while fetching data"));
                       }
                   }

                   @Override
                   public void onError(Throwable e) {
                       moviesMutableLiveData.setValue(new MoviesListWrapper(null, e.getMessage()));
                   }

                   @Override
                   public void onComplete() { }
           }));

        return moviesMutableLiveData;
    }


    public LiveData<List<MovieDO>> getWatchList() {
        return  watchListDB.getAllWatchlist();
    }

    public void addToWatchlist(MovieDO movie) {
        new Thread(() -> watchListDB.insertMovie(movie)).start();

    }
    public void removeFromWatchlist(MovieDO movie) {
        new Thread(() -> watchListDB.deleteMovie(movie)).start();
    }

    public MovieDO getMovie(String id) {
        return watchListDB.getMovie(id);
    }

}
