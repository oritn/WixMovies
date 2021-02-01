package com.e.wixmovies.ui.movies;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.fragment.app.DialogFragment;

import com.e.wixmovies.R;
import com.e.wixmovies.model.MovieDO;

/**
 * display movie info
 */
public class MovieInfoDialog extends DialogFragment {

    private static final String PARCELABLE_KEY = "movie";

    public static MovieInfoDialog newInstance(MovieDO movie) {
        MovieInfoDialog frag = new MovieInfoDialog();
        Bundle args = new Bundle();
        args.putParcelable(PARCELABLE_KEY, movie);
        frag.setArguments(args);
        return frag;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.movie_info_dialog, container, false);
        // remove background dim
        getDialog().getWindow().setDimAmount(0);
        MovieDO movie = getArguments().getParcelable(PARCELABLE_KEY);

        TextView text = v.findViewById(R.id.info_title);
        TextView description = v.findViewById(R.id.info_description);
        ToggleButton addToWatchlistBtn = v.findViewById(R.id.addToWatchlistBtn);
        text.setText(movie.getTitle());
        description.setText(movie.getOverview());
        addToWatchlistBtn.setChecked(movie.getOnWatchlist());
        addToWatchlistBtn.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            ((IWatchlistBtnListener)getTargetFragment()).onWatchlistBtnClicked(movie, isChecked);
        });
        return v;
    }

        @Override
        public void onStart() {
            getDialog().getWindow().setWindowAnimations(R.style.DialogAnimation);
            super.onStart();
        }


    public interface IWatchlistBtnListener {
        void onWatchlistBtnClicked(MovieDO movie, boolean isFavorite);
    }

}
