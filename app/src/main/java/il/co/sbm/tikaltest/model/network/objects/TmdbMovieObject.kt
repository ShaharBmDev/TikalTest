package il.co.sbm.tikaltest.model.network.objects

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.os.Parcel
import android.os.Parcelable
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("vote_count", "id", "video", "vote_average", "title", "popularity", "poster_path", "original_language", "original_title", "genre_ids", "backdrop_path", "adult", "overview", "release_date")
@Entity(tableName = "movies")
class TmdbMovieObject : Parcelable {

    @ColumnInfo(name = "vote_count")
    @JsonProperty("vote_count")
    var mVoteCount: Int = 0

    @PrimaryKey
    @ColumnInfo(name = "id")
    @JsonProperty("id")
    var mId: Int = 0

    @ColumnInfo(name = "video")
    @JsonProperty("video")
    var mVideo: Boolean = false

    @ColumnInfo(name = "vote_average")
    @JsonProperty("vote_average")
    var mVoteAverage: Double = 0.0

    @ColumnInfo(name = "title")
    @JsonProperty("title")
    var mTitle: String = ""

    @ColumnInfo(name = "popularity")
    @JsonProperty("popularity")
    var mPopularity: Double = 0.0

    @ColumnInfo(name = "poster_path")
    @JsonProperty("poster_path")
    var mPposterPath: String = ""

    @ColumnInfo(name = "original_language")
    @JsonProperty("original_language")
    var mOriginalLanguage: String = ""

    @ColumnInfo(name = "original_title")
    @JsonProperty("original_title")
    var mOPriginalTitle: String = ""

    @ColumnInfo(name = "genre_ids")
    @JsonProperty("genre_ids")
    var mGenreIds: List<Int> = ArrayList()

    @ColumnInfo(name = "backdrop_path")
    @JsonProperty("backdrop_path")
    var mBackdropPath: String? = ""

    @ColumnInfo(name = "adult")
    @JsonProperty("adult")
    var mAdult: Boolean = false

    @ColumnInfo(name = "overview")
    @JsonProperty("overview")
    var mOverview: String = ""

    @ColumnInfo(name = "releaseDate")
    @JsonProperty("release_date")
    var mReleaseDate: String = ""

//    constructor(source: Parcel) : this()

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {}

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<TmdbMovieObject> = object : Parcelable.Creator<TmdbMovieObject> {
            override fun createFromParcel(source: Parcel): TmdbMovieObject = TmdbMovieObject(/*source*/)
            override fun newArray(size: Int): Array<TmdbMovieObject?> = arrayOfNulls(size)
        }
    }
}
