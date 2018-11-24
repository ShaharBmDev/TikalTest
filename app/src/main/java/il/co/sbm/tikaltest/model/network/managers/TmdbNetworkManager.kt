package il.co.sbm.tikaltest.model.network.managers

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.RequestFuture
import il.co.sbm.tikaltest.R
import il.co.sbm.tikaltest.model.global.AppConsts
import il.co.sbm.tikaltest.model.global.AppExecutors
import il.co.sbm.tikaltest.model.global.TikalTestApplication
import il.co.sbm.tikaltest.model.network.core.JacksonRequest
import il.co.sbm.tikaltest.model.network.core.RequestStringBuilder
import il.co.sbm.tikaltest.model.network.objects.response.TmdbDiscoverResponse
import il.co.sbm.tikaltest.model.network.objects.response.TmdbGetMovieExternalIdsResponse
import il.co.sbm.tikaltest.model.network.objects.response.TmdbGetMovieVideosResponse
import java.util.concurrent.ExecutionException
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

object TmdbNetworkManager {

    fun discoverMovies(iTmdbApiKey: String, iPageNumber: Int, iResponseListener: Response.Listener<TmdbDiscoverResponse>, iErrorListener: Response.ErrorListener) {

        val request = JacksonRequest(Request.Method.GET, RequestStringBuilder.getPopularMoviesUrl(iTmdbApiKey, iPageNumber), null, TmdbDiscoverResponse::class.java, iResponseListener, iErrorListener)

        TikalTestApplication.mInstance.addVolleyRequest(request)
    }

    fun discoverMoviesSynchronous(iTmdbApiKey: String, iPageNumber: Int): LiveData<NetworkResponse<TmdbDiscoverResponse>> {

        val result: MutableLiveData<NetworkResponse<TmdbDiscoverResponse>> = MutableLiveData()
        var networkResponse: NetworkResponse<TmdbDiscoverResponse>

        val requestFuture: RequestFuture<TmdbDiscoverResponse> = RequestFuture.newFuture()
        val request = JacksonRequest(Request.Method.GET, RequestStringBuilder.getPopularMoviesUrl(iTmdbApiKey, iPageNumber), null, TmdbDiscoverResponse::class.java, requestFuture, requestFuture)

        TikalTestApplication.mInstance.addVolleyRequest(request)

        AppExecutors.networkIO().execute {
            networkResponse = try {
                NetworkResponse(true, requestFuture.get(AppConsts.VOLLEY_REQUEST_CONNECTION_TIMEOUT_SECONDS, TimeUnit.SECONDS))
            } catch (e: InterruptedException) {
                e.printStackTrace()
                NetworkResponse(e.message ?: TikalTestApplication.mInstance.getString(R.string.unknown_error))
            } catch (e: ExecutionException) {
                e.printStackTrace()
                NetworkResponse(e.message ?: TikalTestApplication.mInstance.getString(R.string.unknown_error))
            } catch (e: TimeoutException) {
                e.printStackTrace()
                NetworkResponse(e.message ?: TikalTestApplication.mInstance.getString(R.string.unknown_error))
            }

            AppExecutors.mainThread().execute {
                result.value = networkResponse
            }
        }

        return result
    }

    fun getMovieExternalIds(iTmdbApiKey: String, iMovieId: Int, iResponseListener: Response.Listener<TmdbGetMovieExternalIdsResponse>, iErrorListener: Response.ErrorListener) {

        val request = JacksonRequest(Request.Method.GET, RequestStringBuilder.getMovieExternalIdsUrl(iTmdbApiKey, iMovieId), null, TmdbGetMovieExternalIdsResponse::class.java, iResponseListener, iErrorListener)

        TikalTestApplication.mInstance.addVolleyRequest(request)
    }

    fun getMovieVideos(iTmdbApiKey: String, iMovieId: Int, iResponseListener: Response.Listener<TmdbGetMovieVideosResponse>, iErrorListener: Response.ErrorListener) {

        val request = JacksonRequest(Request.Method.GET, RequestStringBuilder.getMovieVideosUrl(iTmdbApiKey, iMovieId), null, TmdbGetMovieVideosResponse::class.java, iResponseListener, iErrorListener)

        TikalTestApplication.mInstance.addVolleyRequest(request)
    }
}