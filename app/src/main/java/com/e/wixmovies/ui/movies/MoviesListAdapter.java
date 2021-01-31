package com.e.wixmovies.ui.movies;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.e.wixmovies.R;
import com.e.wixmovies.databinding.MovieItemBinding;
import com.e.wixmovies.model.MovieDO;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class MoviesListAdapter extends RecyclerView.Adapter<MoviesListAdapter.MovieViewHolder> implements Filterable {

    private List<MovieDO> filteredDataList = new ArrayList<>();
 
    private List<MovieDO> dataListFull = new ArrayList<>();
    private Filter filter;
    private MoviesFragment.IOnMovieClickedListener listener;
    
    public MoviesListAdapter(MoviesFragment.IOnMovieClickedListener listener) {
        setListFilter();
        this.listener = listener;
    }

    public void updateMoviesData(List<MovieDO> dataList) {
        if(dataList == null) return;
        this.filteredDataList = dataList;
        this.dataListFull = new ArrayList<>(dataList);
    }
    
    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MovieItemBinding movieListItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.movie_item, parent, false);
        return new MovieViewHolder(movieListItemBinding);
    }

    @BindingAdapter({"imageUrl"})
    public static void loadImage(@NotNull ImageView view, String url) {
        Glide.with(view.getContext())
                .load(url)
                .placeholder(R.drawable.ic_launcher_background)
                .into(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        MovieDO movie = filteredDataList.get(position);
        holder.movieListItemBinding.setMovie(movie);
        holder.movieListItemBinding.progress.setProgress(movie.getRankPercent());
    }

    @Override
    public int getItemCount() {
        return filteredDataList == null? 0 : filteredDataList.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    /**
     * view holder class
     */
    protected class MovieViewHolder extends RecyclerView.ViewHolder
    {
        private MovieItemBinding movieListItemBinding;

        MovieViewHolder(@NonNull final MovieItemBinding viewBinding)
        {
            super(viewBinding.getRoot());
            this.movieListItemBinding = viewBinding;

            movieListItemBinding.moreInfoBtn.setOnClickListener(view -> {
                //get the position of the clicked row ad update the listener
                int position = getLayoutPosition();
                MovieDO movie = MoviesListAdapter.this.filteredDataList.get(position);
                listener.onMovieClicked(movie);
            });
        }
    }

    private void setListFilter()
    {
        this.filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                ArrayList<MovieDO> filteredList = new ArrayList<>();
                //if there is no data in the search box show the full list
                if(charSequence == null || charSequence.length() == 0) {
                    filteredList.addAll(dataListFull);
                }
                else {
                    String filternPattern = charSequence.toString().toLowerCase().trim();
                    // go over the items and display only the ones containing the pattern in their title
                    for (MovieDO entry:dataListFull) {
                        if(entry.getTitle().toLowerCase().contains(filternPattern)) {
                            filteredList.add(entry);
                        }
                    }
                }
                FilterResults results = new FilterResults();
                results.values = filteredList;
                return results;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                MoviesListAdapter.this.filteredDataList.clear();
                MoviesListAdapter.this.filteredDataList.addAll((List)filterResults.values);
                notifyDataSetChanged();;
            }
        };
    }
}
