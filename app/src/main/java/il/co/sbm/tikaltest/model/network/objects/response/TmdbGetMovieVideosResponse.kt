package il.co.sbm.tikaltest.model.network.objects.response

import com.fasterxml.jackson.annotation.JsonProperty
import il.co.sbm.tikaltest.model.network.objects.TmdbMovieVideo


data class TmdbGetMovieVideosResponse(
    @JsonProperty("id")
    var id: Int = 0,
    @JsonProperty("results")
    var results: List<TmdbMovieVideo> = ArrayList()
)
