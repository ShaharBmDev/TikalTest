package il.co.sbm.tikaltest.utils

import android.annotation.SuppressLint
import android.content.Context
import android.text.TextUtils
import com.fasterxml.jackson.core.type.TypeReference
import il.co.sbm.tikaltest.model.network.core.Mapper
import java.io.IOException
import java.util.ArrayList

@SuppressLint("ApplySharedPref")
object PreferenceUtils {

    private const val sf_SHARED_PREFS: String = "TikalTestSharedPrefs"
    private const val ARRAY_PREFERENCES_KEY_INDEX_DELIMITER: String = "."

    //region keys
    const val LAST_UPDATE_DATE: String = "lastUpdateDate"
    const val LAST_PAGE_LOADED: String = "lastPageLoaded"
    //endregion


    fun getSharedPreferences(context: Context, key: String): String? {
        val settings = context.getSharedPreferences(sf_SHARED_PREFS, Context.MODE_PRIVATE)
        return settings.getString(key, null)
    }

    fun getSharedPreferencesBoolean(context: Context, key: String): Boolean {
        val settings = context.getSharedPreferences(sf_SHARED_PREFS, Context.MODE_PRIVATE)
        return settings.getBoolean(key, false)
    }

    fun getSharedPreferencesInt(context: Context, key: String): Int {
        val settings = context.getSharedPreferences(sf_SHARED_PREFS, Context.MODE_PRIVATE)
        return settings.getInt(key, 0)
    }

    fun getSharedPreferencesFloat(context: Context, key: String): Float {
        val settings = context.getSharedPreferences(sf_SHARED_PREFS, Context.MODE_PRIVATE)
        return settings.getFloat(key, 0.0f)
    }

    fun getSharedPreferencesLong(context: Context, key: String): Long {
        val settings = context.getSharedPreferences(sf_SHARED_PREFS, Context.MODE_PRIVATE)
        return settings.getLong(key, 0)
    }

    fun setSharedPreference(context: Context, key: String, value: String) {
        val settings = context.getSharedPreferences(sf_SHARED_PREFS, Context.MODE_PRIVATE)
        val prefEditor = settings.edit()

        prefEditor.putString(key, value)

        prefEditor.commit()
    }

    fun setSharedPreference(context: Context, key: String, value: Boolean) {
        val settings = context.getSharedPreferences(sf_SHARED_PREFS, Context.MODE_PRIVATE)
        val prefEditor = settings.edit()

        prefEditor.putBoolean(key, value)

        prefEditor.commit()
    }

    fun setSharedPreference(context: Context, key: String, value: Float) {
        val settings = context.getSharedPreferences(sf_SHARED_PREFS, Context.MODE_PRIVATE)
        val prefEditor = settings.edit()

        prefEditor.putFloat(key, value)

        prefEditor.commit()
    }

    fun setSharedPreference(context: Context, key: String, value: Int) {
        val settings = context.getSharedPreferences(sf_SHARED_PREFS, Context.MODE_PRIVATE)
        val prefEditor = settings.edit()

        prefEditor.putInt(key, value)

        prefEditor.commit()
    }

    fun setSharedPreference(context: Context, key: String, value: Long) {
        val settings = context.getSharedPreferences(sf_SHARED_PREFS, Context.MODE_PRIVATE)
        val prefEditor = settings.edit()

        prefEditor.putLong(key, value)

        prefEditor.commit()
    }

    fun setSharedPreferences(context: Context, key: String, values: ArrayList<String>) {
        // first, remove the old array
        removeSharedPreferencesArray(context, key)

        val settings = context.getSharedPreferences(sf_SHARED_PREFS, Context.MODE_PRIVATE)
        val prefEditor = settings.edit()

        var keyToInsert: String? = null

        for ((indexToInsert, value) in values.withIndex()) {

            keyToInsert = getSharedPreferencesArrayKey(key, indexToInsert)
            prefEditor.putString(keyToInsert, value)
        }

        prefEditor.commit()
    }

    fun getSharedPreferencesStringsArray(context: Context, key: String): ArrayList<String>? {
        val values = ArrayList<String>()

        var nextIndex = 0
        var nextKey = getSharedPreferencesArrayKey(key, nextIndex)
        var currentValue: String? = null
        currentValue = getSharedPreferences(context, nextKey)

        while (currentValue != null) {

            values.add(currentValue)

            nextIndex++
            nextKey = getSharedPreferencesArrayKey(key, nextIndex)
            currentValue = getSharedPreferences(context, nextKey)
        }

        return if (values.size < 1) {

            null
        } else values

    }

    private fun removeSharedPreferencesArray(context: Context, key: String) {
        val settings = context.getSharedPreferences(sf_SHARED_PREFS, Context.MODE_PRIVATE)
        val prefEditor = settings.edit()

        var indexToCheck = 0
        var keyToRemove = getSharedPreferencesArrayKey(key, indexToCheck)

        while (getSharedPreferences(context, keyToRemove) != null) {

            prefEditor.remove(keyToRemove)
            indexToCheck++
            keyToRemove = getSharedPreferencesArrayKey(key, indexToCheck)
        }

        prefEditor.commit()
    }

    private fun getSharedPreferencesArrayKey(key: String, index: Int): String {
        return key + ARRAY_PREFERENCES_KEY_INDEX_DELIMITER + index
    }

    fun setJsonMappedObject(context: Context, key: String, value: Any) {
        val settings = context.getSharedPreferences(sf_SHARED_PREFS, Context.MODE_PRIVATE)
        val prefEditor = settings.edit()

        prefEditor.putString(key, Mapper.string(value))

        prefEditor.commit()
    }

    fun <T> getJsonMappedObject(context: Context, key: String, valueClass: Class<T>): T? {
        var result: T? = null

        val settings = context.getSharedPreferences(sf_SHARED_PREFS, Context.MODE_PRIVATE)

        val sharedPrefsRawValue = settings.getString(key, null)

        if (!TextUtils.isEmpty(sharedPrefsRawValue)) {
            result = Mapper.`object`(sharedPrefsRawValue, valueClass)
        }

        return result
    }

    fun <T> getJsonMappedObject(context: Context, key: String, valueClass: TypeReference<T>): T? {
        var result: T? = null

        val settings = context.getSharedPreferences(sf_SHARED_PREFS, Context.MODE_PRIVATE)

        val sharedPrefsRawValue = settings.getString(key, null)

        if (!TextUtils.isEmpty(sharedPrefsRawValue)) {
            result = Mapper.`object`(sharedPrefsRawValue, valueClass)
        }

        return result
    }

    private fun setJsonMappedObjectArray(context: Context, key: String, value: ArrayList<*>) {
        val settings = context.getSharedPreferences(sf_SHARED_PREFS, Context.MODE_PRIVATE)
        val prefEditor = settings.edit()

        prefEditor.putString(key, Mapper.string(value))

        prefEditor.commit()
    }

    private fun <T> getJsonMappedObjectArray(context: Context, key: String, valueClass: Class<T>): ArrayList<T>? {
        var result: ArrayList<T>? = null
        val settings = context.getSharedPreferences(sf_SHARED_PREFS, Context.MODE_PRIVATE)

        try {
            val sharedPrefsRawValue = settings.getString(key, null)

            if (!TextUtils.isEmpty(sharedPrefsRawValue)) {
                result = Mapper.get().readValue(sharedPrefsRawValue, Mapper.get().typeFactory.constructCollectionType(List::class.java, valueClass))
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return result
    }
}