package com.shayan.movies;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.shayan.movies.adapter.MovieTrailerListAdapter;
import com.shayan.movies.config.Config;
import com.shayan.movies.model.Movie;
import com.shayan.movies.model.Trailer;
import com.shayan.movies.presenter.MovieDetailPresenter;
import com.shayan.movies.presenter.MovieDetailPresenterImpl;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;

public class MovieDetailActivity extends YouTubeBaseActivity implements MovieDetailPresenter.View {

    @BindView(R.id.original_title)
    TextView originalTitle;

    @BindView(R.id.thumbnailImage)
    ImageView thumbnail;

    @BindView(R.id.release_date)
    TextView releaseDate;

    @BindView(R.id.vote_average)
    TextView voteAverage;

    @BindView(R.id.overview)
    TextView overview;

    @BindView(R.id.rv_trailers)
    RecyclerView trailer;

    @BindView(R.id.favoriteToggle)
    ImageView starBtn;

    private MovieDetailPresenter presenter;
    private MovieTrailerListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        ButterKnife.bind(this);

        trailer.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MovieTrailerListAdapter(this, new ArrayList<>());
        trailer.setAdapter(adapter);

        Intent intent = getIntent();
        Integer movieId = intent.getExtras().getInt(Config.MOVIE_KEY);

        this.presenter = new MovieDetailPresenterImpl(this, movieId);

        presenter.updateMovie(); // Start point
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if(id == android.R.id.home){
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    // Add Back button to ActionBar in order to enable navigation
    private void addBackButton() {
        //getSupportActionBar().setDisplayShowHomeEnabled(true);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void showData(Movie movieSelected) {
        // Add back button
        addBackButton();
        originalTitle.setText(movieSelected.getOriginalTitle());
        String poster = String.format("%s%s", getResources().getString(R.string.image_url), movieSelected.getPosterPath());
        Glide.with(this).load(poster).into(thumbnail);
        String year = movieSelected.getReleaseDate().substring(0,4); // Extract year
        releaseDate.setText(year);
        voteAverage.setText(movieSelected.getVoteAverage() + "/10");
        overview.setText(movieSelected.getOverview());

        if (movieSelected.isFavorite()) {
            starBtn.setImageDrawable(starBtn.getResources().getDrawable(R.drawable.favorite_star));
        } else {
            starBtn.setImageDrawable(starBtn.getResources().getDrawable(R.drawable.unfavorite_star));
        }
    }

    // Update the trailers
    @Override
    public void showData(List<Trailer> trailers) {
        adapter.setTrailers(trailers);
    }

    @Override
    public void showError() {
        // Display a user-friendly Message (Toast)
        Toasty.error(this, getString(R.string.error_message), Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.favoriteToggle)
    void markAsFavorite() {
        this.presenter.toggleFavorite();
        Toasty.success(this, "You masked as favorite", Toast.LENGTH_SHORT).show();
    }

    /**
     * Destroy unused references
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.presenter.cancelAsyncTask(); // Clean-up the memory (unused references)
    }
}
