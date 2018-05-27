package com.example.android.popularmoviesstageone.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;

/**
 * Movie is a template for the items in the data model.
 */
@Parcel
public class Movie {

    public static final String MOVIE_EXTRA = "movie";

    String posterPath;
    String backdropPath;
    String originalTitle;
    String overview;
    String releaseDate;
    double voteAverage;

    public Movie() {}

    public Movie(JSONObject jsonObject) {
        this.posterPath = jsonObject.optString("poster_path");
        this.originalTitle = jsonObject.optString("original_title");
        this.overview = jsonObject.optString("overview");
        this.voteAverage = jsonObject.optDouble("vote_average");
        this.releaseDate = jsonObject.optString("release_date");
        this.backdropPath = jsonObject.optString("backdrop_path");
    }

    /**
     * Returns a formatted URL for the poster image
     * @return Formatted Image URL
     */
    public String getPosterPath() {
        return String.format("https://image.tmdb.org/t/p/w342/%s", posterPath);
    }

    /**
     * Returns a formatted URL for the backdrop image
     * @return Formatted Image URL
     */
    public String getBackdropPath() {
        String path = backdropPath;
        if (backdropPath == null || backdropPath.isEmpty() || backdropPath == "null" ) {
            return getPosterPath();
        }
        return String.format("https://image.tmdb.org/t/p/w780/%s", path);
    }

    /**
     * Returns the movie title
     * @return String representing movie title
     */
    public String getOriginalTitle() {
        return originalTitle;
    }

    /**
     * Returns the movie overview
     * @return String respresenting movie overview
     */
    public String getOverview() {
        return overview;
    }

    /**
     * Returns the movie release date
     * @return String representing movie release date
     */
    public String getReleaseDate() {
        return releaseDate;
    }

    /**
     * Returns the movie vote average
     * @return Double representing movie vote average
     */
    public Double getVoteAverage() {
        return voteAverage;
    }
}
