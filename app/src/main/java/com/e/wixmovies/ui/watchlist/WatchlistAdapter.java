package com.e.wixmovies.ui.watchlist;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.e.wixmovies.R;
import com.e.wixmovies.databinding.WatchlistItemBinding;
import com.e.wixmovies.model.MovieDO;

import java.util.ArrayList;
import java.util.List;

public class WatchlistAdapter extends RecyclerView.Adapter<WatchlistAdapter.WatchlistViewHolder> {


    private List<MovieDO> watchlistMovies;
    private WatchlistFragment.IODeleteMovieListener listener;

    public WatchlistAdapter(WatchlistFragment.IODeleteMovieListener listener) {
        this.listener = listener;
    }

    public void updateMoviesData(List<MovieDO> dataList) {
        if(dataList == null) return;
        this.watchlistMovies = new ArrayList<>(dataList);
    }
    
    @NonNull
    @Override
    public WatchlistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        WatchlistItemBinding movieListItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.watchlist_item, parent, false);
        return new WatchlistViewHolder(movieListItemBinding);
    }

    @BindingAdapter({"watchlistUrl"})
    public static void loadWatchlistImage(ImageView view, String url) {
        Glide.with(view.getContext())
                .load(url)
                .placeholder(R.drawable.ic_launcher_background)
                .into(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WatchlistViewHolder holder, int position) {
        holder.watchlistItemBinding.setMovie(watchlistMovies.get(position));
    }

    @Override
    public int getItemCount() {
        return watchlistMovies == null? 0 : watchlistMovies.size();
    }

    /**
     * view holder class
     */
    protected class WatchlistViewHolder extends RecyclerView.ViewHolder
    {
        private WatchlistItemBinding watchlistItemBinding;
        private View mView;

        WatchlistViewHolder(@NonNull final WatchlistItemBinding viewBinding)
        {
            super(viewBinding.getRoot());
            this.watchlistItemBinding = viewBinding;
            this.mView = itemView;
            Button deleteBtn = this.mView.findViewById(R.id.delete);
            deleteBtn.setOnClickListener(view -> {
                int position = getLayoutPosition();
                MovieDO movie = WatchlistAdapter.this.watchlistMovies.get(position);
                listener.onDeleteMovie(movie);
            });
        }
    }
}
