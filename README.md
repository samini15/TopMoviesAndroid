# top-movies-Android
Top movies is an Android application created using Java (and a few Kotlin code) and is a projet for the purpose of training.
It displays the list of movies in 3 categories: Popular, Top movies and favorites.
The app communicates with a public API called TMDB (The Movie Database) to retrieve the first 20 popular and top movies.
User can mark a movie as favorit and it is saved in the SQLite database (using Room).

The following technologies and concepts are used:
  * MVP architecture
  * Single responsibility principle
  
  * Retrofit
  * Room
  
  * Butterknife
  
  * Glide (for picture displaying)
  
  * RecyclerView and Adapter
  
  * Android alternative resources (for tablet display support)

In order to run the app, please generate the appropriate keys:

   * TMDB : https://developers.themoviedb.org/
   * Youtube : https://console.developers.google.com/