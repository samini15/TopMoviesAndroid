package com.shayan.movies.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.shayan.movies.R;
import com.shayan.movies.config.Config;
import com.shayan.movies.model.Movie;

import java.util.ArrayList;
import java.util.List;


@SuppressWarnings("ALL")
public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.MovieListViewHolder> {

    private Context context;
    private LayoutInflater inflater;
    private List<Movie> movieList;

    private MovieListViewHolder.Listner listner;

    public MovieListAdapter(Context context, MovieListViewHolder.Listner listner) {
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        this.listner = listner;
        this.movieList = new ArrayList<Movie>();
    }

    @NonNull
    @Override
    public MovieListAdapter.MovieListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.movie_card_view, parent, false);
        return new MovieListViewHolder(itemView, listner);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieListAdapter.MovieListViewHolder holder, int position) {
        holder.movie = movieList.get(position);
        holder.bind(holder.movie, context);
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public void setMovies(List<Movie> movies){
        this.movieList.clear();
        this.movieList.addAll(movies);
        notifyDataSetChanged();
        notifyItemRangeInserted(0, movieList.size());
    }


    public static class MovieListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public interface Listner {
            void onMovieClick(Movie movie);

            void onStarClick(Movie movie);
        }

        ImageView thumbnail;
        ImageView toggleFav;

        Listner listner;

        Movie movie;

        public MovieListViewHolder(@NonNull View itemView, Listner listner) {
            super(itemView);
            thumbnail = (ImageView) itemView.findViewById(R.id.thumbnail);
            toggleFav = (ImageView) itemView.findViewById(R.id.starBtn);

            this.listner = listner;
            itemView.setOnClickListener(this);
            toggleFav.setOnClickListener(this);
        }

        public void bind(Movie movie, Context context) {
            String poster = String.format("%s%s", Config.IMAGE_URL, movie.getPosterPath());

            if (movie.isFavorite()) {
                toggleFav.setImageDrawable(toggleFav.getResources().getDrawable(R.drawable.favorite_star));
            } else {
                toggleFav.setImageDrawable(toggleFav.getResources().getDrawable(R.drawable.unfavorite_star));
            }

            // Picture loading => Glide
            Glide.with(context).load(poster).into(thumbnail);
        }

        @Override
        public void onClick(View v) {
            int pos = getAdapterPosition();
            if (pos != RecyclerView.NO_POSITION) {
                if (listner != null) {
                    if (v == toggleFav) {
                        listner.onStarClick(movie);
                    } else {
                        listner.onMovieClick(movie);
                    }
                }
            }
        }
    }
}
