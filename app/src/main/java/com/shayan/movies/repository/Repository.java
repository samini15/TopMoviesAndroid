package com.shayan.movies.repository;

import androidx.annotation.WorkerThread;

import com.shayan.movies.api.RetrofitInstance;
import com.shayan.movies.api.Service;
import com.shayan.movies.config.Config;
import com.shayan.movies.initializer.AndroidApplication;
import com.shayan.movies.localDB.FavoriteDao;
import com.shayan.movies.localDB.FavoriteRoomDatabase;
import com.shayan.movies.model.Movie;
import com.shayan.movies.model.MoviesResponse;
import com.shayan.movies.model.Trailer;
import com.shayan.movies.model.TrailersResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

/**
 * A Repository abstract access to multiple Data source
 */
public class Repository {

    private FavoriteDao mFavoriteDao;  // Reference to DAO
    private Service service;  // Reference to SERVICE (API)
    private Call<MoviesResponse> call;
    private Call<Movie> movieCall;
    private Call<TrailersResponse> trailersCall;
    private  Call<Trailer> trailerCall;
    private static volatile Repository INSTANCE;

    public static Repository getRepository() {
        if (INSTANCE == null) {
            INSTANCE = new Repository();
        }
        return INSTANCE;
    }

    public Repository() {
        FavoriteRoomDatabase database = AndroidApplication.getDb();
        this.mFavoriteDao = database.favoriteDao();
        this.service = RetrofitInstance.getRetrofitInstance().create(Service.class);
    }

    @WorkerThread
    public List<Movie> getMovies(int sortStrategy) throws IOException {

        List<Movie> mv = new ArrayList<>();

        switch (sortStrategy) {
            case Config.SORT_POPULARITY:
                this.call = service.getPopularMovies(Config.API_KEY);

                mv.clear();
                // Sync call
                // Execute the call in the foreground task with execute method
                MoviesResponse movieResp = call.execute().body();
                mv = mergeFavorites(movieResp.getResults());
                break;
            case Config.SORT_TOP_RATED:
                this.call = service.getTopRatedMovies(Config.API_KEY);

                mv.clear();
                // Sync call
                // Execute the call in the foreground task with execute method
                movieResp = call.execute().body();
                mv = mergeFavorites(movieResp.getResults());
                break;
            case Config.SORT_FAVORITES:
                mv.clear();
                mv = this.mFavoriteDao.getAllFavorites();
                break;
        }
        return mv;
    }

    @WorkerThread
    public List<Trailer> getTrailers(String movieId) throws IOException {
        List<Trailer> tl = new ArrayList<>();

        this.trailersCall = service.getTrailers(movieId, Config.API_KEY);
        TrailersResponse result = trailersCall.execute().body();
        tl.addAll(result.getResults());
        return tl;
    }

    public List<Movie> mergeFavorites(List<Movie> mv) {
        List<Movie> favorites = this.mFavoriteDao.getAllFavorites();
        for (Movie m : mv) {
            for (Movie f : favorites) {
                m.setFavorite(m.getId().equals(f.getId()));
                if (m.isFavorite()) {
                    break;
                }
            }
        }
        return mv;
    }

    @WorkerThread
    public void toggleFavorite(Movie movie) {
        if (!movie.isFavorite()) { // Toggle favorite
            movie.setFavorite(true);
            mFavoriteDao.toggleFavorite(movie);
        } else {  // Untoggle favorite
            movie.setFavorite(false);
            mFavoriteDao.untoggleFavorite(movie.getId());
        }
    }

    @WorkerThread
    public Movie getMovieById(String id) throws IOException {
        List<Movie> fav = this.mFavoriteDao.getAllFavorites();
        this.movieCall = service.getMovieById(id, Config.API_KEY);
        Movie m = movieCall.execute().body();
        for (Movie f : fav) {
            if (f.getId().equals(m.getId())) {
                m.setFavorite(true);
                break;
            }
        }
        return m;
    }
}
