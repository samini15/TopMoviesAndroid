package com.shayan.movies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.shayan.movies.adapter.MovieListAdapter;
import com.shayan.movies.config.Config;
import com.shayan.movies.model.Movie;
import com.shayan.movies.presenter.MovieListPresenter;
import com.shayan.movies.presenter.MovieListPresenterImpl;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;

public class MovieListActivity extends AppCompatActivity implements MovieListPresenter.View, MovieListAdapter.MovieListViewHolder.Listner {

    @BindView(R.id.recyclerView) RecyclerView recyclerView;

    private MovieListPresenter presenter;
    private MovieListAdapter adapter;
    private int sortStrategy;  // Actual sortStrategy before rotation (Bundle)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);

        ButterKnife.bind(this);
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        adapter = new MovieListAdapter(getApplicationContext(), this);
        recyclerView.setAdapter(adapter);
        this.presenter = new MovieListPresenterImpl(this);

        if (savedInstanceState == null) {
            presenter.updateMovies(Config.SORT_POPULARITY); // Start point
        } else {
            presenter.updateMovies(savedInstanceState.getInt(Config.SORT_STATE_ACTIVITY)); // Start point
        }
    }

    @Override
    public void showData(List<Movie> movies) {
        adapter.setMovies(movies);
    }

    @Override
    public void showError() {
        // Display a user-friendly Message (Toast)
        Toasty.error(this, getString(R.string.error_message), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMovieClick(Movie movie) {
        Intent intent = new Intent(this, MovieDetailActivity.class);
        intent.putExtra(Config.MOVIE_KEY, movie.getId());
        startActivity(intent);
        Toasty.info(this, "You clicked " + movie.getOriginalTitle(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStarClick(Movie movie) {
        this.presenter.toggleFavorite(movie);
        Toasty.info(this, "You masked " + movie.getOriginalTitle() + " as favorite", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.sort_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sort_by_popularity:
                presenter.updateMovies(Config.SORT_POPULARITY);

                sortStrategy = Config.SORT_POPULARITY;
                break;
            case R.id.sort_by_top:
                presenter.updateMovies(Config.SORT_TOP_RATED);
                sortStrategy = Config.SORT_TOP_RATED;
                break;
            case R.id.sort_by_favorites:
                presenter.updateMovies(Config.SORT_FAVORITES);
                sortStrategy = Config.SORT_FAVORITES;
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        outState.putInt(Config.SORT_STATE_ACTIVITY, sortStrategy);
        super.onSaveInstanceState(outState, outPersistentState);
    }

    /**
     * Activity becomes visible and data binding is executing
     */
    @Override
    protected void onStart() {
        super.onStart();
        if (sortStrategy != 0) {
            presenter.updateMovies(sortStrategy); // Bundle value
        } else {
            presenter.updateMovies(Config.SORT_POPULARITY); // Default value
        }
    }

    /**
     * Destroy unused references
     *
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.presenter.cancelAsyncTask(); // Clean-up the memory (unused references)
    }
}
