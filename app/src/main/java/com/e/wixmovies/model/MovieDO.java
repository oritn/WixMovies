package com.e.wixmovies.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.databinding.BaseObservable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.e.wixmovies.repo.TMDBRetrofitInstance;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "watchlist")
public class MovieDO extends BaseObservable implements Parcelable {

    @PrimaryKey
    @SerializedName("id")
    @NonNull
    private String id = "";

    @ColumnInfo(name = "release_date")
    @SerializedName("release_date")
    private String releaseDate;

    @ColumnInfo(name = "title")
    @SerializedName("title")
    private String title;

    @ColumnInfo(name = "overview")
    @SerializedName("overview")
    private String overview;

    @ColumnInfo(name = "poster_path")
    @SerializedName("poster_path")
    private String thumbnailUrl;

    @ColumnInfo(name = "vote_average")
    @SerializedName("vote_average")
    private Double voteAverage;

    @ColumnInfo(name = "isOnWatchlist")
    @SerializedName("isOnWatchlist")
    private Boolean isOnWatchlist = false;

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


    public void setId(String id) {
        this.id = id;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public void setVoteAverage(Double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public MovieDO(String id, String releaseDate, String title, String overview, String thumbnailUrl, Double voteAverage, Boolean isOnWatchlist) {
        this.id = id;
        this.releaseDate = releaseDate;
        this.title = title;
        this.overview = overview;
        this.thumbnailUrl = thumbnailUrl;
        this.voteAverage = voteAverage;
        this.isOnWatchlist = isOnWatchlist;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getRank() {
        return Double.toString(voteAverage);
    }
    public int getRankPercent() {
        return (int)(voteAverage *10);
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

    public Double getVoteAverage() {
        return voteAverage;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public String getFullThumbnailUrl() {
        return TMDBRetrofitInstance.BASE_THUMBNAIL_URL + thumbnailUrl;
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
        parcel.writeValue(voteAverage);
        parcel.writeValue(isOnWatchlist);
    }

    public MovieDO() {
    }
    public MovieDO(Parcel in) {
        this.releaseDate = ((String) in.readValue((String.class.getClassLoader())));
        this.id = ((String) in.readValue((String.class.getClassLoader())));
        this.title = ((String) in.readValue((String.class.getClassLoader())));
        this.overview = ((String) in.readValue((String.class.getClassLoader())));
        this.thumbnailUrl = ((String) in.readValue((String.class.getClassLoader())));
        this.voteAverage = ((Double) in.readValue((Double.class.getClassLoader())));
        this.isOnWatchlist = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
    }

    public Boolean getOnWatchlist() {
        return isOnWatchlist;
    }
    public void setOnWatchlist(Boolean onWatchlist) {
        isOnWatchlist = onWatchlist;
    }
}
