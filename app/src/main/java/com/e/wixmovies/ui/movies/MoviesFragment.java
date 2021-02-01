package com.e.wixmovies.ui.movies;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.e.wixmovies.R;
import com.e.wixmovies.databinding.FragmentMoviesBinding;
import com.e.wixmovies.model.MovieDO;
import com.e.wixmovies.ui.PaginationScrollListener;
import com.e.wixmovies.viewmodel.MoviesViewModel;
import com.google.android.material.snackbar.Snackbar;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Fragment showing the list of movies
 */
public class MoviesFragment extends Fragment implements MovieInfoDialog.IWatchlistBtnListener {

    private MoviesViewModel moviesViewModel;
    private FragmentMoviesBinding fragmentBinding;
    private MoviesListAdapter moviesAdapter;
    private PaginationScrollListener scrollListener;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    public static MoviesFragment newInstance() {
        return new MoviesFragment();
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        fragmentBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_movies, container, false);
        return fragmentBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //set adapter
        moviesAdapter = new MoviesListAdapter(movie -> {
            movie.setOnWatchlist(moviesViewModel.isOnWatchList(movie));
            MovieInfoDialog infoDialog = MovieInfoDialog.newInstance(movie);
            infoDialog.setTargetFragment(MoviesFragment.this, 0);
            infoDialog.show(getFragmentManager(), movie.getId());
        });
        //set view model
        moviesViewModel = new ViewModelProvider(getActivity()).get(MoviesViewModel.class);
        moviesViewModel.init(getActivity().getApplication());
        //set swipe refresh view
        SwipeRefreshLayout swipeRefreshLayout = fragmentBinding.swiperefresh;
        swipeRefreshLayout.setOnRefreshListener(() -> {
            scrollListener.resetState();
            moviesViewModel.refreshMovieList(MoviesFragment.this, getContext(), compositeDisposable);
            swipeRefreshLayout.setRefreshing(false);
        });
        //set recycler view
        RecyclerView recyclerView = fragmentBinding.moviesRV;
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(moviesAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //set scroll for fetching more movies on scroll
        scrollListener = new PaginationScrollListener(gridLayoutManager) {
            @Override
            public void onLoadMore(int totalItemsCount, RecyclerView view) {
                fragmentBinding.loadMoreProgressBar.setVisibility(View.VISIBLE);
                moviesViewModel.loadMoreMovies(MoviesFragment.this, getContext(), compositeDisposable);
            }
        };
        recyclerView.addOnScrollListener(scrollListener);
        ProgressBar pb = fragmentBinding.progressBar;
        //observe on data change
        moviesViewModel.errorMsg.observe(this, error -> {
            pb.setVisibility(View.GONE);
            fragmentBinding.loadMoreProgressBar.setVisibility(View.GONE);
            Snackbar.make(fragmentBinding.getRoot(), getString(R.string.internet_error), Snackbar.LENGTH_LONG).show();
        });
        moviesViewModel.moviesList.observe(this, movies -> {
            fragmentBinding.loadMoreProgressBar.setVisibility(View.GONE);
            pb.setVisibility(View.GONE);
            moviesAdapter.updateMoviesData(movies);
            moviesAdapter.notifyDataSetChanged();
        });
        if (savedInstanceState == null) {
            pb.setVisibility(View.VISIBLE);
            moviesViewModel.getMovies(MoviesFragment.this, getContext(), compositeDisposable);
        }
        else {
            moviesAdapter.updateMoviesData(moviesViewModel.moviesList.getValue());
            moviesAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.search_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView)searchItem.getActionView();
        //set done in the keyboard and not find symbol
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener()
        {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                moviesAdapter.getFilter().filter(newText);
                scrollListener.setDisableScrolling(!newText.equals(""));
                return false;
            }
        });
    }

    @Override
    public void onWatchlistBtnClicked(MovieDO movie, boolean isOnWatchlist) {
        if(isOnWatchlist) {
            moviesViewModel.addToWatchlist(movie);
            String text = String.format(getResources().getString(R.string.added_watchlist), movie.getTitle());
            Snackbar.make(fragmentBinding.getRoot(), text, Snackbar.LENGTH_LONG).show();
        }
        else {
            moviesViewModel.removeFromWatchlist(movie);
            String text = String.format(getResources().getString(R.string.removed_watchlist), movie.getTitle());
            Snackbar.make(fragmentBinding.getRoot(), text, Snackbar.LENGTH_LONG).show();
        }
    }

    public interface IOnMovieClickedListener {
        void onMovieClicked(MovieDO movie);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }
}