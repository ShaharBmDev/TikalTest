package il.co.sbm.tikaltest.model.repositories

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.support.annotation.MainThread
import android.support.annotation.NonNull
import android.support.annotation.Nullable
import android.support.annotation.WorkerThread
import il.co.sbm.tikaltest.R
import il.co.sbm.tikaltest.model.global.AppExecutors
import il.co.sbm.tikaltest.model.global.TikalTestApplication
import il.co.sbm.tikaltest.model.network.managers.NetworkResponse

/**
 * @param ResultType Type for the Resource data.
 * @param WsResponseType Type for the Ws response.
 */
abstract class NetworkBoundResource<ResultType, WsResponseType> {

    private val result = MediatorLiveData<Resource<ResultType>>()

    init {
        result.value = Resource.loading(null)
        @Suppress("LeakingThis")
        val dbSource = loadFromDb()
        result.addSource(dbSource) { data ->
            result.removeSource(dbSource)
            if (shouldFetch(data)) {
                loadFromNetwork(dbSource)
            } else {
                result.addSource(dbSource) { newData ->
                    setValue(Resource.success(newData))
                }
            }
        }
    }

    @MainThread
    private fun setValue(newValue: Resource<ResultType>) {
        if (result.value != newValue) {
            result.value = newValue
        }
    }

    private fun loadFromNetwork(dbSource: LiveData<ResultType>) {
        val apiResponse = createWsRequest()
        // we re-attach dbSource as a new source, it will dispatch its latest value quickly
        result.addSource(dbSource) { newData ->
            setValue(Resource.loading(newData))
        }
        result.addSource(apiResponse) { response ->
            result.removeSource(apiResponse)
            result.removeSource(dbSource)
            if (response?.mIsSuccess == true) {
                AppExecutors.diskIO().execute {
                    saveWsRequestResult(processResponse(response.mResponse))
                    AppExecutors.mainThread().execute {
                        // we specially request a new live data,
                        // otherwise we will get immediately last cached value,
                        // which may not be updated with latest results received from network.
                        result.addSource(loadFromDb()) { newData ->
                            setValue(Resource.success(newData))
                        }
                    }
                }
            } else {
                onFetchFailed()
                result.addSource(dbSource) { newData ->
                    setValue(Resource.error(response?.mErrorMessage ?: TikalTestApplication.mInstance.getString(R.string.unknown_error), newData))
                }
            }
        }
    }

    fun getAsLiveData() = result as LiveData<Resource<ResultType>>

    @WorkerThread
    protected abstract fun saveWsRequestResult(@NonNull iWsResponseType: WsResponseType?)

    @MainThread
    protected abstract fun shouldFetch(@Nullable iData: ResultType?): Boolean

    @NonNull
    @MainThread
    protected abstract fun loadFromDb(): LiveData<ResultType>

    @NonNull
    @MainThread
    protected abstract fun createWsRequest(): LiveData<NetworkResponse<WsResponseType>>

    @MainThread
    protected abstract fun onFetchFailed()

    @WorkerThread
    protected open fun processResponse(iResponse: WsResponseType?): WsResponseType? = iResponse
}