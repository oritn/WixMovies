package com.e.wixmovies.ui.watchlist;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.e.wixmovies.R;
import com.e.wixmovies.databinding.FragmentMoviesBinding;
import com.e.wixmovies.databinding.FragmentWatchlistBinding;
import com.e.wixmovies.model.MovieDO;
import com.e.wixmovies.ui.movies.MoviesListAdapter;
import com.e.wixmovies.viewmodel.MoviesViewModel;

public class WatchlistFragment extends Fragment {

    private MoviesViewModel moviesViewModel;
    private WatchlistAdapter moviesAdapter;
    private FragmentWatchlistBinding fragmentBinding;

    public static WatchlistFragment newInstance() {
        WatchlistFragment fragment = new WatchlistFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        moviesAdapter = new WatchlistAdapter(movie -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            String text = String.format(getResources().getString(R.string.delete_movie_watchlist), movie.getTitle());
            builder.setTitle(text)
                    .setPositiveButton(R.string.yes, (dialogInterface, i) -> {
                        moviesViewModel.removeFromWatchlist(movie);
                    })
                    .setNegativeButton(R.string.no, null);
          builder.create().show();
        });
    }


    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        fragmentBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_watchlist, container, false);
        return fragmentBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        moviesViewModel = new ViewModelProvider(getActivity()).get(MoviesViewModel.class);
        moviesViewModel.init(getActivity().getApplication());
        RecyclerView recyclerView = fragmentBinding.watchList;
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(moviesAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        ProgressBar pb = fragmentBinding.progressBar;

        moviesViewModel.errorMsg.observe(this, error -> {
            pb.setVisibility(View.GONE);
            Toast.makeText(getContext(), R.string.watchlist_error, Toast.LENGTH_LONG);
        });
        moviesViewModel.moviesWatchlist.observe(this, movies -> {
            pb.setVisibility(View.GONE);
            fragmentBinding.emptyView.setVisibility(movies.isEmpty()? View.VISIBLE: View.GONE);
            moviesAdapter.updateMoviesData(movies);
            moviesAdapter.notifyDataSetChanged();
        });
        if (savedInstanceState == null) {
            pb.setVisibility(View.VISIBLE);
            moviesViewModel.getWatchList(WatchlistFragment.this);
        } else {
            moviesAdapter.updateMoviesData(moviesViewModel.moviesList.getValue());
            moviesAdapter.notifyDataSetChanged();
        }
    }

    public interface IODeleteMovieListener {
        void onDeleteMovie(MovieDO movie);
    }

}
