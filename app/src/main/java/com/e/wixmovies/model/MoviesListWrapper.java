package com.e.wixmovies.model;

public class MoviesListWrapper {
    private String errorMsg;
    private MoviesList movieList;
    public MoviesListWrapper(MoviesList movieList, String errorMsg) {
        this.movieList = movieList;
        this.errorMsg = errorMsg;
    }

    public MoviesList getMovieList() {
        return movieList;
    }

    public void setMovieList(MoviesList movieList) {
        this.movieList = movieList;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
