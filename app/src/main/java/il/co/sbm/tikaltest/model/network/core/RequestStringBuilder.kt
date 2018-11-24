package il.co.sbm.tikaltest.model.network.core

object RequestStringBuilder {

    //region Tmdb api
    private const val TMDB_BASE_URL = "https://api.themoviedb.org/"
    private const val DISCOVER_POPULAR_MOVIES = "/3/discover/movie?api_key=%s&language=en-US&sort_by=popularity.desc&include_adult=false&include_video=false&page=%s"
    private const val TMDB_POSTER_URL = "https://image.tmdb.org/t/p/w500/"
    private const val TMDB_EXTERNAL_IDS_URL = "/3/movie/%s/external_ids?api_key=%s"
    private const val TMDB_VIDEOS_URL = "/3/movie/%s/videos?api_key=%s&language=en-US"
    //endregion

    //region Imdb api
    private const val IMDB_MOVIE_URL = "https://www.imdb.com/title/%s/"
    //endregion

    //region Youtube api
    private const val YOUTUBE_VIDEO_URL = "https://www.youtube.com/watch?v=%s"
    //endregion

    fun getPopularMoviesUrl(iTmdbApiKey: String, iPageNumber: Int): String {
        return TMDB_BASE_URL + String.format(DISCOVER_POPULAR_MOVIES, iTmdbApiKey, iPageNumber)
    }

    fun getPosterUrl(iPosterFileUrl: String): String {
        return TMDB_POSTER_URL + iPosterFileUrl
    }

    fun getMovieExternalIdsUrl(iTmdbApiKey: String, iMovieId: Int): String {
        return TMDB_BASE_URL + String.format(TMDB_EXTERNAL_IDS_URL, iMovieId, iTmdbApiKey)
    }

    fun getMovieVideosUrl(iTmdbApiKey: String, iMovieId: Int): String {
        return TMDB_BASE_URL + String.format(TMDB_VIDEOS_URL, iMovieId, iTmdbApiKey)
    }

    fun getImdbMovieUrl(iImdbId: String): String {
        return String.format(IMDB_MOVIE_URL, iImdbId)
    }

    fun getYoutubeVideoUrl(iYoutubeVideoId: String): String {
        return String.format(YOUTUBE_VIDEO_URL, iYoutubeVideoId)
    }
}
