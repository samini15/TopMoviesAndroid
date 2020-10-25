package com.shayan.movies.presenter;

import android.os.AsyncTask;
import android.util.Log;
import android.util.Pair;

import com.shayan.movies.config.Config;
import com.shayan.movies.model.Movie;
import com.shayan.movies.model.Trailer;
import com.shayan.movies.repository.Repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MovieDetailPresenterImpl implements MovieDetailPresenter {

    private Integer movieId;
    private MovieDetailPresenter.View view;

    private ToggleFavoriteAsyncTask toggleAsyncTask;
    private FetchMovieAsyncTask fetchAsyncTask;
    private FetchTrailersAsyncTask fetchTrailersAsyncTask;

    public MovieDetailPresenterImpl(View view, Integer movieId) {
        this.view = view;
        this.movieId = movieId;
    }

    /**
     * Retrieve the movie selected in MovieListActivity
     */
    @Override
    public void updateMovie() {
        // If a previous task is running, interrupt that
        cancelAsyncTask();
        fetchAsyncTask = new FetchMovieAsyncTask(view);
        fetchAsyncTask.execute(movieId);

        fetchTrailersAsyncTask = new FetchTrailersAsyncTask(view);
        fetchTrailersAsyncTask.execute(movieId);
    }

    @Override
    public void toggleFavorite() {
        // If a previous task is running, interrupt that
        cancelAsyncTask();
        toggleAsyncTask = new ToggleFavoriteAsyncTask(view);
        toggleAsyncTask.execute(movieId);
    }


    /**
     * #################################### TOGGLE ASYNC #########################################
     */
    private static class ToggleFavoriteAsyncTask extends AsyncTask<Integer, Void, Movie> {

        private Repository repository;
        private MovieDetailPresenter.View view;
        private Pair<String, Boolean> error;

        ToggleFavoriteAsyncTask(MovieDetailPresenter.View view) {
            this.repository = Repository.getRepository();
            this.view = view;
        }

        @Override
        protected Movie doInBackground(Integer... movieId) {
            Movie movie = null;
            try {
                movie = repository.getMovieById(movieId[0].toString());
                repository.toggleFavorite(movie);
                movie = repository.getMovieById(movieId[0].toString());
            } catch (IOException e) {
                Log.e(Config.EXCEPTION, e.getMessage()); // For developers
                this.error = new Pair<>(Config.EXCEPTION, true); // For user
            }
            return movie;
        }

        @Override
        protected void onPostExecute(Movie movieSelected) {
            if (error != null) {
                view.showError();
                cancel(true);
            } else {
                view.showData(movieSelected);
            }
        }
    }

    /**
     * ################################# FETCH ASYNC #############################################
     */

    private static class FetchMovieAsyncTask extends AsyncTask<Integer, Void, Movie> {

        private Repository repository;
        private MovieDetailPresenter.View view;
        private Pair<String, Boolean> error;

        FetchMovieAsyncTask(MovieDetailPresenter.View view) {
            this.repository = Repository.getRepository();
            this.view = view;
        }

        @Override
        protected Movie doInBackground(Integer... movieId) {
            Movie movie = null;
            try {
                movie = repository.getMovieById(movieId[0].toString());
            } catch (IOException e) {
                Log.e(Config.EXCEPTION, e.getMessage()); // For developers
                this.error = new Pair<>(Config.EXCEPTION, true); // For user
            }
            return movie;
        }

        @Override
        protected void onPostExecute(Movie movie) {
            if (error != null) {
                view.showError();
                cancel(true);
            } else {
                view.showData(movie);
            }
        }
    }

    /**
     * ################################# FETCH TRAILERS ASYNC #############################################
     */

    private static class FetchTrailersAsyncTask extends AsyncTask<Integer, Void, List<Trailer>> {

        private Repository repository;
        private MovieDetailPresenter.View view;
        private Pair<String, Boolean> error;

        FetchTrailersAsyncTask(MovieDetailPresenter.View view) {
            this.repository = Repository.getRepository();
            this.view = view;
        }

        @Override
        protected List<Trailer> doInBackground(Integer... movieId) {
            List<Trailer> trailers = new ArrayList<>();
            try {
                trailers = repository.getTrailers(movieId[0].toString());
            } catch (IOException e) {
                Log.e(Config.EXCEPTION, e.getMessage()); // For developers
                this.error = new Pair<>(Config.EXCEPTION, true); // For user
            }
            return trailers;
        }

        @Override
        protected void onPostExecute(List<Trailer> trailers) {
            if (error != null) {
                view.showError();
                cancel(true);
            } else {
                view.showData(trailers);
            }
        }
    }


    /** #################################### CANCEL ############################################### */
    /**
     * Interrupt the task in process that invokes garbage collecting (remove unused Object references) if activity is destroyed for example
     */
    @Override
    public void cancelAsyncTask() {
        if (fetchAsyncTask != null && fetchAsyncTask.getStatus() == FetchMovieAsyncTask.Status.RUNNING) {
            fetchAsyncTask.cancel(true);
        }
        if (toggleAsyncTask != null && toggleAsyncTask.getStatus() == ToggleFavoriteAsyncTask.Status.RUNNING) {
            toggleAsyncTask.cancel(true);
        }
    }
}
