package com.shayan.movies.presenter;

import com.shayan.movies.model.Movie;
import com.shayan.movies.model.Trailer;

import java.util.List;

public interface MovieDetailPresenter {

    interface View {
        void showData(Movie movieSelected);

        void showData(List<Trailer> trailers);

        void showError();
    }

    void updateMovie();

    void toggleFavorite();

    void cancelAsyncTask();

}
