package il.co.sbm.tikaltest.model.databases

import android.arch.persistence.room.TypeConverter

class GenresConverter {

    @TypeConverter
    fun storedStringToGenresIds(iGenresCommaSeperatedString: String?): List<Int>? {
        val result: ArrayList<Int> = ArrayList()

        if (iGenresCommaSeperatedString != null) {
            result.addAll(iGenresCommaSeperatedString.split("\\s*,\\s*".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray().map { it.toInt() })
        }
        return result
    }

    @TypeConverter
    fun genresIdsToDbString(iGenresIds: List<Int>?): String? {
        var value = ""

        if (iGenresIds != null) {
            for (genreId in iGenresIds)
                value += genreId.toString() + ","
        }

        return value
    }
}