package il.co.sbm.tikaltest.model.repositories

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.android.volley.Response
import com.android.volley.VolleyError
import il.co.sbm.tikaltest.model.databases.MovieDatabase
import il.co.sbm.tikaltest.model.databases.TmdbMovieObjectDao
import il.co.sbm.tikaltest.model.global.AppConsts
import il.co.sbm.tikaltest.model.global.TikalTestApplication
import il.co.sbm.tikaltest.model.network.managers.NetworkResponse
import il.co.sbm.tikaltest.model.network.managers.TmdbNetworkManager
import il.co.sbm.tikaltest.model.network.objects.TmdbMovieObject
import il.co.sbm.tikaltest.model.network.objects.response.TmdbDiscoverResponse
import il.co.sbm.tikaltest.model.network.objects.response.TmdbGetMovieExternalIdsResponse
import il.co.sbm.tikaltest.model.network.objects.response.TmdbGetMovieVideosResponse
import il.co.sbm.tikaltest.utils.DateTimeUtils
import il.co.sbm.tikaltest.utils.PreferenceUtils
import java.util.*

object TmdbMoviesRepository : Response.ErrorListener {

    private var mPageNumberIndex = PreferenceUtils.getSharedPreferencesInt(TikalTestApplication.mInstance, PreferenceUtils.LAST_PAGE_LOADED).plus(1)
    private val mTmdbMovieObjectDao: TmdbMovieObjectDao = MovieDatabase.getInstance()?.tmdbMovieObjectDao()!!
    private val mMovieExternalIdsResponse: MutableLiveData<TmdbGetMovieExternalIdsResponse> = MutableLiveData()
    private val mMovieVideos: MutableLiveData<TmdbGetMovieVideosResponse> = MutableLiveData()
    private var mUserInvokeMoreResults: Boolean = false

    fun loadMovies(): LiveData<Resource<List<TmdbMovieObject>>> {

        return object : NetworkBoundResource<List<TmdbMovieObject>, TmdbDiscoverResponse>() {

            override fun saveWsRequestResult(iWsResponseType: TmdbDiscoverResponse?) {
                if (iWsResponseType != null) {

                    if (shouldUpdate()) {
                        mTmdbMovieObjectDao.deleteAll()
                    }
                    mTmdbMovieObjectDao.saveAll(*iWsResponseType.mResults.toTypedArray())
                }
            }

            override fun shouldFetch(iData: List<TmdbMovieObject>?): Boolean {
                return shouldUpdate() || mUserInvokeMoreResults
            }

            override fun loadFromDb(): LiveData<List<TmdbMovieObject>> {
                return mTmdbMovieObjectDao.loadAll()
            }

            override fun createWsRequest(): LiveData<NetworkResponse<TmdbDiscoverResponse>> {

                if (shouldUpdate()) {
                    mPageNumberIndex = AppConsts.INDEX_FIRST_PAGE
                }

                return TmdbNetworkManager.discoverMoviesSynchronous(AppConsts.theMovieDataBaseApiKey, mPageNumberIndex)
            }

            override fun onFetchFailed() {
            }

            override fun processResponse(iResponse: TmdbDiscoverResponse?): TmdbDiscoverResponse? {

                mPageNumberIndex = (iResponse?.mPage ?: mPageNumberIndex)
                PreferenceUtils.setSharedPreference(TikalTestApplication.mInstance, PreferenceUtils.LAST_PAGE_LOADED, mPageNumberIndex)
                mPageNumberIndex = mPageNumberIndex.plus(1)

                if (shouldUpdate()) {
                    PreferenceUtils.setSharedPreference(TikalTestApplication.mInstance, PreferenceUtils.LAST_UPDATE_DATE, Calendar.getInstance().timeInMillis)
                }

                return super.processResponse(iResponse)
            }

        }.getAsLiveData()
    }

    private fun shouldUpdate(): Boolean {

        val lastUpdateDateLong = PreferenceUtils.getSharedPreferencesLong(TikalTestApplication.mInstance, PreferenceUtils.LAST_UPDATE_DATE)

        return DateTimeUtils.getTimeDifferenceInHours(Date(lastUpdateDateLong), Date())!! > DateTimeUtils.HOURS_IN_A_DAY
    }

    fun getMovieExternalIds(iMovieId: Int?): MutableLiveData<TmdbGetMovieExternalIdsResponse> {
        if (iMovieId != null) {
            TmdbNetworkManager.getMovieExternalIds(AppConsts.theMovieDataBaseApiKey, iMovieId, Response.Listener { iResponse -> mMovieExternalIdsResponse.value = iResponse }, this)
        }

        return mMovieExternalIdsResponse
    }

    fun getMovieVideos(iMovieId: Int?): MutableLiveData<TmdbGetMovieVideosResponse> {
        if (iMovieId != null) {
            TmdbNetworkManager.getMovieVideos(AppConsts.theMovieDataBaseApiKey, iMovieId, Response.Listener { iResponse -> mMovieVideos.value = iResponse }, this)
        }

        return mMovieVideos
    }

    override fun onErrorResponse(error: VolleyError?) {
        // this is for the Trailers and the IMDB links. if fails - do nothing.
    }

}