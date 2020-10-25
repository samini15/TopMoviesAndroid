package com.shayan.movies.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.recyclerview.widget.RecyclerView
import com.google.android.youtube.player.*
import com.shayan.movies.R
import com.shayan.movies.config.Config
import com.shayan.movies.model.Trailer

class MovieTrailerListAdapter(private val context: Context, private val trailers: MutableList<Trailer>) : RecyclerView.Adapter<MovieTrailerListAdapter.ViewHolder>() {
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(inflater.inflate(R.layout.trailer_item_view_holder, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(trailers[position], context)
    }

    override fun getItemCount(): Int {
        return this.trailers.size
    }

    fun setTrailers(trailers: MutableList<Trailer>) {
        this.trailers.clear()
        this.trailers.addAll(trailers)
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var youtubeVideoThumbnail = itemView.findViewById<YouTubeThumbnailView>(R.id.trailer_player)
        var relativeLayout = itemView.findViewById<RelativeLayout>(R.id.relativeLayout_over_youtube_thumbnail)
        var playButton = itemView.findViewById<ImageView>(R.id.btn_youtube_player).setOnClickListener(this)

        lateinit var context: Context
        lateinit var trailer: Trailer

        lateinit var onThumbnailLoadedListener: YouTubeThumbnailLoader.OnThumbnailLoadedListener

        fun bind(trailer: Trailer, context: Context) {
            this.context = context
            this.trailer = trailer

            // Trailer content
            onThumbnailLoadedListener = object: YouTubeThumbnailLoader.OnThumbnailLoadedListener {
                override fun onThumbnailLoaded(youtubeThumbnailView: YouTubeThumbnailView?, string: String?) {
                    youtubeThumbnailView?.visibility = View.VISIBLE
                    relativeLayout.visibility = View.VISIBLE
                }

                override fun onThumbnailError(p0: YouTubeThumbnailView?, p1: YouTubeThumbnailLoader.ErrorReason?) {
                    TODO("Not yet implemented")
                }

            }

            youtubeVideoThumbnail.initialize(Config.YOUTUBE_API_KEY, object: YouTubeThumbnailView.OnInitializedListener {
                override fun onInitializationSuccess(youTubeThumbnailView: YouTubeThumbnailView?, youTubeThumbnailLoader: YouTubeThumbnailLoader?) {
                    youTubeThumbnailLoader?.setVideo(trailer.key)
                    youTubeThumbnailLoader?.setOnThumbnailLoadedListener(onThumbnailLoadedListener)
                }

                override fun onInitializationFailure(p0: YouTubeThumbnailView?, p1: YouTubeInitializationResult?) {
                    TODO("Not yet implemented")
                }

            })
        }

        override fun onClick(v: View?) {
            val intent: Intent = YouTubeStandalonePlayer.createVideoIntent(context as Activity, Config.YOUTUBE_API_KEY, trailer.key, 100, false, false)
            context.startActivity(intent)
        }
    }
}