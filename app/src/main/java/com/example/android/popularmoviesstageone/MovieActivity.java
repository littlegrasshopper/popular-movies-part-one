package com.example.android.popularmoviesstageone;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popularmoviesstageone.adapter.MovieArrayAdapter;
import com.example.android.popularmoviesstageone.model.Movie;
import com.example.android.popularmoviesstageone.utilities.MovieJsonUtils;
import com.example.android.popularmoviesstageone.utilities.NetworkUtils;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.net.URL;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

/**
 * Main activity to display a grid of movie poster images.
 * Credit: Udacity S03.01-Exercise-RecyclerView (MainActivity.java)
 */
public class MovieActivity extends AppCompatActivity
        implements MovieArrayAdapter.MovieArrayAdapterOnClickHandler {

    private MovieArrayAdapter mMovieAdapter;

    @BindView(R.id.rvMovies) RecyclerView mMoviesRecyclerView;
    @BindView(R.id.tbToolbar) android.support.v7.widget.Toolbar mToolbar;
    @BindView(R.id.spSortBy) Spinner mSpinner;
    @BindView(R.id.tvErrorMessage) TextView mErrorMessageDisplay;
    @BindView(R.id.pbLoadingIndicator) ProgressBar mLoadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);
        ButterKnife.bind(this);

        // Sets the mToolbar to act as the ActionBar for this Activity window.
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
        }
        mToolbar.setTitle(getResources().getString(R.string.app_name));
        initialize();
    }

    private void initialize() {
        mMovieAdapter = new MovieArrayAdapter(this, this);
        mMoviesRecyclerView.setHasFixedSize(true);
        mMoviesRecyclerView.setAdapter(mMovieAdapter);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(
                this,
                3,
                GridLayoutManager.VERTICAL,
                false
        );
        mMoviesRecyclerView.setLayoutManager(gridLayoutManager);

        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                int selected = adapterView.getSelectedItemPosition();
                fetchMovieData(Integer.toString(selected));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        fetchMovieData(Integer.toString(0));
    }

    private void fetchMovieData(String sortBy ) {
        if (isOnline()) {
            showMovieDataView();
            new FetchMoviesDataTask().execute(sortBy);
        } else {
            showErrorMessage();
        }
    }

    /**
     * Show the view for the movies data and hide the error message display.
     */
    private void showMovieDataView() {
        // hide the error message display
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        // show the list of movies
        mMoviesRecyclerView.setVisibility(View.VISIBLE);
    }

    /**
     * Show the error message display and hide the movies data.
     */
    private void showErrorMessage() {
        // hide the view for the list of movies
        mMoviesRecyclerView.setVisibility(View.INVISIBLE);
        // show the error message
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    /**
     * AsyncTask for fetching movies in the background.
     */
    public class FetchMoviesDataTask extends AsyncTask<String, Void, ArrayList<Movie>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected ArrayList<Movie> doInBackground(String... params) {
            int sortBy;

            // get the sort by filter
            if (params.length == 0) {
                sortBy = 0;
            } else {
                try {
                    sortBy = Integer.parseInt(params[0]);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    sortBy = 0;
                }
            }

            URL moviesDBUrl = NetworkUtils.buildUrl(sortBy);

            try {
                String httpResponse = NetworkUtils.getResponseFromHttpUrl(moviesDBUrl);
                JSONObject jsonObject = new JSONObject(httpResponse);

                ArrayList<Movie> jsonMovieData = MovieJsonUtils
                        .getMovieDataFromJson(jsonObject.getJSONArray("results"));
                return jsonMovieData;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<Movie> moviesData) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (moviesData != null) {
                showMovieDataView();
                mMovieAdapter.setMovieData(moviesData);
            } else {
                showErrorMessage();
            }
        }
    }

    /**
     * Check to make sure there is network connection
     * @return True if network is available, false otherwise.
     * Credit:
     * https://stackoverflow.com/questions/1560788/how-to-check-internet-access-on-android-inetaddress-never-times-out
     */
    // === Start ===
    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }
    // === End ===

    @Override
    public void onClick(Movie movie) {
        Class destinationActivity = DetailActivity.class;
        Context context = MovieActivity.this;
        Intent intent = new Intent(context, destinationActivity);

        intent.putExtra(Movie.MOVIE_EXTRA, Parcels.wrap(movie));
        startActivity(intent);
    }
}
