package com.e.wixmovies.ui.movies;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
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
import com.e.wixmovies.viewmodel.MoviesViewModel;
import com.e.wixmovies.ui.PaginationScrollListener;

/**
 * A placeholder fragment containing a simple view.
 */
public class MoviesFragment extends Fragment implements MovieInfoDialog.IFavoriteBtnClicked {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private MoviesViewModel moviesViewModel;
    private FragmentMoviesBinding fragmentBinding;
    private MoviesListAdapter moviesAdapter;
    private PaginationScrollListener scrollListener;

    public static MoviesFragment newInstance(int index) {
        MoviesFragment fragment = new MoviesFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        moviesViewModel = new ViewModelProvider(this).get(MoviesViewModel.class);
        moviesAdapter = new MoviesListAdapter(movie -> MovieInfoDialog.newInstance(movie).show(getFragmentManager(), movie.getId()));
    }


    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        fragmentBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_movies, container, false);
        return fragmentBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = fragmentBinding.moviesRV;
        SwipeRefreshLayout swipeRefreshLayout = fragmentBinding.swiperefresh;
        swipeRefreshLayout.setOnRefreshListener(() -> {
            scrollListener.resetState();
            moviesViewModel.getMovies(MoviesFragment.this, getContext());
            swipeRefreshLayout.setRefreshing(false);
        });
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);

        gridLayoutManager.scrollToPosition(0);
        gridLayoutManager.scrollToPositionWithOffset(0, 0);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(moviesAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        scrollListener = new PaginationScrollListener(gridLayoutManager) {
            @Override
            public void onLoadMore(int totalItemsCount, RecyclerView view) {
                fragmentBinding.loadMoreProgressBar.setVisibility(View.VISIBLE);
                moviesViewModel.loadMoreMovies(MoviesFragment.this, getContext());
            }
        };
        recyclerView.addOnScrollListener(scrollListener);
      /*  recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) { //check for scroll down
                    int visibleItemCount = gridLayoutManager.getChildCount();
                    int totalItemCount = gridLayoutManager.getItemCount();
                    int pastVisibleItems = gridLayoutManager.findFirstVisibleItemPosition();

                    if (!isLoading && (visibleItemCount + pastVisibleItems) >= totalItemCount) {
                        isLoading = true;
                        fragmentBinding.loadMoreProgressBar.setVisibility(View.VISIBLE);
                        moviesViewModel.loadMoreMovies(MoviesFragment.this, getContext());
                    }
                }
            }
        });*/
        ProgressBar pb = fragmentBinding.progressBar;

        moviesViewModel.errorMsg.observe(this, error -> {
            pb.setVisibility(View.GONE);
            fragmentBinding.loadMoreProgressBar.setVisibility(View.GONE);
            Toast.makeText(getContext(), "There was an error retrieving the movies", Toast.LENGTH_LONG);
        });
        moviesViewModel.moviesList.observe(this, movies -> {
            fragmentBinding.loadMoreProgressBar.setVisibility(View.GONE);
            pb.setVisibility(View.GONE);
            moviesAdapter.updateMoviesData(movies);
            moviesAdapter.notifyDataSetChanged();
        });
        if (savedInstanceState == null) {
            pb.setVisibility(View.VISIBLE);
            moviesViewModel.getMovies(MoviesFragment.this, getContext());
        }
        else {
            moviesAdapter.updateMoviesData(moviesViewModel.moviesList.getValue());
            moviesAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onFavoriteBtnClicked(MovieDO movie, boolean isFavorite) {
        if(isFavorite) {
            moviesViewModel.addToFavorite(movie);
        }
        else {
            moviesViewModel.removeFromFavorite(movie);
        }
    }

    public interface IOnMovieClickedListener {
        void onMovieClicked(MovieDO movie);
    }
}