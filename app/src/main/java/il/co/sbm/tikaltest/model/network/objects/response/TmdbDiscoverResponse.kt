package il.co.sbm.tikaltest.model.network.objects.response

import com.fasterxml.jackson.annotation.JsonProperty
import il.co.sbm.tikaltest.model.network.objects.TmdbMovieObject

data class TmdbDiscoverResponse(
    @JsonProperty("page")
    var mPage: Int = 0,
    @JsonProperty("total_results")
    var mTotalResults: Int = 0,
    @JsonProperty("total_pages")
    var mTotalPages: Int = 0,
    @JsonProperty("results")
    var mResults: List<TmdbMovieObject> = ArrayList()
)