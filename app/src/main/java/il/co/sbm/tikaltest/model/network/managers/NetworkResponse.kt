package il.co.sbm.tikaltest.model.network.managers


class NetworkResponse<T> {

    constructor(iIsSuccess: Boolean, iResponse: T) {
        mIsSuccess = iIsSuccess
        mResponse = iResponse
    }

    constructor(iErrorMessage: String) {
        mIsSuccess = false
        mErrorMessage = iErrorMessage
    }

    var mIsSuccess = false
    var mResponse: T? = null
    var mErrorMessage: String? = null
}