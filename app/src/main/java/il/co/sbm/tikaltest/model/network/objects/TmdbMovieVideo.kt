package il.co.sbm.tikaltest.model.network.objects

import com.fasterxml.jackson.annotation.JsonProperty

data class TmdbMovieVideo(
    @JsonProperty("id")
    var id: String = "",
    @JsonProperty("iso_3166_1")
    var iso31661: String = "",
    @JsonProperty("iso_639_1")
    var iso6391: String = "",
    @JsonProperty("key")
    var key: String = "",
    @JsonProperty("name")
    var name: String = "",
    @JsonProperty("site")
    var site: String = "",
    @JsonProperty("size")
    var size: Int = 0,
    @JsonProperty("type")
    var type: String = ""
)