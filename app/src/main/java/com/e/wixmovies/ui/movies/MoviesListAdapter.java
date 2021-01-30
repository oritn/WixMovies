package com.e.wixmovies.ui.movies;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;

import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.e.wixmovies.R;
import com.e.wixmovies.databinding.MovieItemBinding;
import com.e.wixmovies.model.MovieDO;

import java.util.ArrayList;
import java.util.List;

public class MoviesListAdapter extends RecyclerView.Adapter<MoviesListAdapter.MovieViewHolder> implements Filterable {

    private List<MovieDO> filteredDataList;
 
    private List<MovieDO> dataListFull;
    private Filter filter;
    private MoviesFragment.IOnMovieClickedListener listener;
    
    public MoviesListAdapter(MoviesFragment.IOnMovieClickedListener listener) {
        setListFilter();
        this.listener = listener;
    }

    public void updateMoviesData(List<MovieDO> dataList) {
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
    public static void loadImage(ImageView view, String url) {
        Glide.with(view.getContext())
                .load(url)
                .placeholder(R.drawable.ic_launcher_background)
                .into(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        holder.movieListItemBinding.setMovie(filteredDataList.get(position));
    }



    @Override
    public int getItemCount() {
        return filteredDataList == null? 0 : filteredDataList.size();
    }

    @Override
    public Filter getFilter() {
        return null;
    }

    /**
     * view holder class
     */
    protected class MovieViewHolder extends RecyclerView.ViewHolder
    {
        private MovieItemBinding movieListItemBinding;
        private View mView;
        private TextView title;
        private ImageView coverImage;
        private TextView rank;

        MovieViewHolder(@NonNull final MovieItemBinding viewBinding)
        {
            super(viewBinding.getRoot());
            this.movieListItemBinding = viewBinding;
            this.mView = itemView;

            this.title = this.mView.findViewById(R.id.title);
            this.coverImage = this.mView.findViewById(R.id.coverImage);
            this.rank = this.mView.findViewById(R.id.rank);
            this.mView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    //get the position of the clicked row ad update the listener
                    int position = getLayoutPosition();
                    MovieDO movie = MoviesListAdapter.this.filteredDataList.get(position);
                    listener.onMovieClicked(movie);
                }
            });
        }
    }




    private void setListFilter()
    {
        this.filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                ArrayList<MovieDO> filteredList = new ArrayList<MovieDO>();
                //if there is no data in the search box show the full list
                if(charSequence == null || charSequence.length() == 0) {
                    filteredList.addAll(dataListFull);
                }
                else {
                    String filternPattern = charSequence.toString().toLowerCase().trim();
                    // go over the items and diplay only the ones containing the pattern in their title
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
