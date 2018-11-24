package il.co.sbm.tikaltest.model.databases

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import il.co.sbm.tikaltest.model.global.TikalTestApplication
import il.co.sbm.tikaltest.model.network.objects.TmdbMovieObject

@Database(entities = [TmdbMovieObject::class], version = 1, exportSchema = false)
@TypeConverters(GenresConverter::class)
abstract class MovieDatabase : RoomDatabase() {

    abstract fun tmdbMovieObjectDao(): TmdbMovieObjectDao

    companion object {
        private var mInstance: MovieDatabase? = null

        @Synchronized
        fun getInstance(): MovieDatabase? {
            if (mInstance == null) {
                synchronized(MovieDatabase::class) {
                    mInstance = Room.databaseBuilder(
                        TikalTestApplication.mInstance,
                        MovieDatabase::class.java, MovieDatabase::class.simpleName.toString()
                    )
                        .build()
                }
            }
            return mInstance
        }

        fun destroyInstance() {
            mInstance = null
        }
    }
}