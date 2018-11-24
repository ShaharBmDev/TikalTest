package il.co.sbm.tikaltest.model.network.objects.response

import com.fasterxml.jackson.annotation.JsonProperty

data class TmdbGetMovieExternalIdsResponse(
    @JsonProperty("facebook_id")
    var facebookId: String = "",
    @JsonProperty("id")
    var id: Int = 0,
    @JsonProperty("imdb_id")
    var imdbId: String = "",
    @JsonProperty("instagram_id")
    var instagramId: String = "",
    @JsonProperty("twitter_id")
    var twitterId: String = ""
)