package com.shayan.movies.presenter;

import android.os.AsyncTask;
import android.util.Log;
import android.util.Pair;

import com.shayan.movies.config.Config;
import com.shayan.movies.model.Movie;
import com.shayan.movies.repository.Repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("ALL")
public class MovieListPresenterImpl implements MovieListPresenter {

    private MovieListPresenter.View view;

    private FetchMoviesAsyncTask fetchAsyncTask;
    private ToggleFavoriteAsyncTask toggleAsyncTask;


    public MovieListPresenterImpl(View view) {
        this.view = view;
    }

    /**
     * This method gets all movies of TMDB by a network call
     * @return
     */
    @Override
    public void updateMovies(int sortStrategy) {
        // If a previous task is running, interrupt that
        cancelAsyncTask();
        fetchAsyncTask = new FetchMoviesAsyncTask(view);
        fetchAsyncTask.execute(sortStrategy);
    }

    @Override
    public void toggleFavorite(Movie movie) {
        // If a previous task is running, interrupt that
        cancelAsyncTask();
        toggleAsyncTask = new ToggleFavoriteAsyncTask(view);
        toggleAsyncTask.execute(movie);
    }


    /** ################################ TOGGLE ASYNC ###################################################*/
    /**
     * Toggle a movie as favorite and updates UI with the List of movies
     */
    private static class ToggleFavoriteAsyncTask extends AsyncTask<Movie, Void, List<Movie>> {

        private Repository repository;
        private MovieListPresenter.View view;
        private Pair<String, Boolean> error;

        ToggleFavoriteAsyncTask(MovieListPresenter.View view) {
            this.repository = Repository.getRepository();
            this.view = view;
        }

        @Override
        protected List<Movie> doInBackground(Movie... movies) {
            List<Movie> mv = new ArrayList<Movie>();
            repository.toggleFavorite(movies[0]);

            try {
                mv = repository.getMovies(Config.SORT_POPULARITY);
            } catch (IOException e) {
                Log.e(Config.EXCEPTION, e.getMessage()); // For developers
                this.error = new Pair<>(Config.EXCEPTION, true); // For user
            }
            return mv;
        }

        @Override
        protected void onPostExecute(List<Movie> movies) {
            if (error != null) {
                view.showError();
                cancel(true);
            } else {
                view.showData(movies);
            }
        }

    }

    /**
     * ################################## FETCH ASYNC #################################################
     */
    private static class FetchMoviesAsyncTask extends AsyncTask<Integer, Void, List<Movie>> {

        private Repository repository;
        private MovieListPresenter.View view;
        private Pair<String, Boolean> error;

        FetchMoviesAsyncTask(MovieListPresenter.View view) {
            this.repository = Repository.getRepository();
            this.view = view;
        }

        @Override
        protected List<Movie> doInBackground(Integer... sortStrategy) {
            // Call synchronous methods in repository
            List<Movie> movies = new ArrayList<Movie>();
            try {
                movies = repository.getMovies(sortStrategy[0]);
            } catch (IOException e) {
                Log.e(Config.EXCEPTION, e.getMessage()); // For developers
                this.error = new Pair<>(Config.EXCEPTION, true); // For user
            }
            return movies;
        }

        // This method is executed on Main thread
        @Override
        protected void onPostExecute(List<Movie> movies) {
            if (error != null) {
                view.showError();
                cancel(true);
            } else {
                view.showData(movies);
            }
        }
    }


    /** #################################### CANCEL ############################################### */
    /**
     * Interrupt the task in process that invokes garbage collecting (remove unused Object references) if activity is destroyed for example
     */
    @Override
    public void cancelAsyncTask() {
        if (fetchAsyncTask != null && fetchAsyncTask.getStatus() == FetchMoviesAsyncTask.Status.RUNNING) {
            fetchAsyncTask.cancel(true);
        }
        if (toggleAsyncTask != null && toggleAsyncTask.getStatus() == ToggleFavoriteAsyncTask.Status.RUNNING) {
            toggleAsyncTask.cancel(true);
        }
    }
}
