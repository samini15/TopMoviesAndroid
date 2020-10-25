package com.shayan.movies.localDB;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.shayan.movies.model.Movie;

import java.util.List;

@Dao
public interface FavoriteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void toggleFavorite(Movie movie);

    @Query("DELETE FROM movie_table WHERE id = :movieId")
    void untoggleFavorite(int movieId);

    @Query("SELECT * FROM movie_table ORDER BY original_title ASC")
    List<Movie> getAllFavorites();
}
