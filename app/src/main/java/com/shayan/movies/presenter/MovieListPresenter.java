package com.shayan.movies.presenter;

import com.shayan.movies.model.Movie;

import java.util.List;

public interface MovieListPresenter {
    interface View {
        void showData(List<Movie> movies);

        void showError();
    }

    void updateMovies(int sortStrategy);

    void toggleFavorite(Movie movie);

    void cancelAsyncTask();
}
