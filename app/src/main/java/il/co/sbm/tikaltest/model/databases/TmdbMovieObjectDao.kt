package il.co.sbm.tikaltest.model.databases

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import android.arch.persistence.room.Query
import il.co.sbm.tikaltest.model.network.objects.TmdbMovieObject

@Dao
interface TmdbMovieObjectDao {

    @Insert(onConflict = REPLACE)
    fun save(iMovieObject: TmdbMovieObject)

    /**
     * @param iMovieObjects if it is a [List] - use the [List.toTypedArray] method, plus the kotlin spread operator - *.
     *
     * For example:
     *
     * val movies: ArrayList<TmdbMovieObject> = listOf<Item>(movie1, movie2, movie3)
     * tmdbMovieObjectDao.insertAll(*movies.toTypedArray())]
     *
     */
    @Insert(onConflict = REPLACE)
    fun saveAll(vararg iMovieObjects: TmdbMovieObject)

    @Query("SELECT * FROM movies WHERE id = :iMovieId")
    fun load(iMovieId: Int): LiveData<TmdbMovieObject>

    @Query("SELECT * FROM movies")
    fun loadAll(): LiveData<List<TmdbMovieObject>>

    @Query("DELETE FROM movies")
    fun deleteAll(): Int
}