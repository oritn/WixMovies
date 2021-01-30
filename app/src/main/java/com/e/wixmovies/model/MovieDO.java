package com.e.wixmovies.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.databinding.BaseObservable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MovieDO extends BaseObservable implements Parcelable {
    @SerializedName("release_date")
    private String releaseDate;
    @SerializedName("id")
    private String id;
    @SerializedName("title")
    private String title;
    @SerializedName("overview")
    private String overview;
    @SerializedName("poster_path")
    private String thumbnailUrl;
    @SerializedName("vote_average")
    private Double vote_average;

    public static final Creator<MovieDO> CREATOR = new Creator<MovieDO>() {
        @Override
        public MovieDO createFromParcel(Parcel in) {
            return new MovieDO(in);
        }

        @Override
        public MovieDO[] newArray(int size) {
            return new MovieDO[size];
        }
    };

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getRank() {
        return "rank" + vote_average;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getOverview() {
        return overview;
    }

    public String getThumbnailUrl() {
        return "https://image.tmdb.org/t/p/w500" + thumbnailUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeValue(releaseDate);
        parcel.writeValue(id);
        parcel.writeValue(title);
        parcel.writeValue(overview);
        parcel.writeValue(thumbnailUrl);
        parcel.writeValue(vote_average);
    }

    protected MovieDO(Parcel in) {
        this.releaseDate = ((String) in.readValue((String.class.getClassLoader())));
        this.id = ((String) in.readValue((String.class.getClassLoader())));
        this.title = ((String) in.readValue((String.class.getClassLoader())));
        this.overview = ((String) in.readValue((String.class.getClassLoader())));
        this.thumbnailUrl = ((String) in.readValue((String.class.getClassLoader())));
        this.vote_average = ((Double) in.readValue((Double.class.getClassLoader())));
    }
}
