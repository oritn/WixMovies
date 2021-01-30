package com.e.wixmovies.ui.movies;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.fragment.app.DialogFragment;

import com.e.wixmovies.R;
import com.e.wixmovies.model.MovieDO;

public class MovieInfoDialog extends DialogFragment {

    public static MovieInfoDialog newInstance(MovieDO movie) {
        MovieInfoDialog frag = new MovieInfoDialog();
        Bundle args = new Bundle();
        args.putParcelable("movie", movie);
        frag.setArguments(args);
        return frag;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.movie_info_dialog, container, false);
        MovieDO movie = getArguments().getParcelable("movie");

        TextView text = v.findViewById(R.id.info_title);
        TextView description = v.findViewById(R.id.info_description);
        ToggleButton favoriteBtn = v.findViewById(R.id.favoriteBtn);
        text.setText(movie.getTitle());
        description.setText(movie.getOverview());
        favoriteBtn.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            ((IFavoriteBtnClicked)getParentFragment()).onFavoriteBtnClicked(movie, isChecked);
        });
        return v;
    }

    public interface IFavoriteBtnClicked {
        void onFavoriteBtnClicked(MovieDO movie, boolean isFavorite);
    }

}
