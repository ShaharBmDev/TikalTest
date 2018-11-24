package il.co.sbm.tikaltest.model.network.core;

import android.text.TextUtils;
import android.util.Log;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;
import il.co.sbm.tikaltest.BuildConfig;
import il.co.sbm.tikaltest.utils.AppUtils;

import java.util.HashMap;
import java.util.Map;

public class JacksonRequest<T> extends JsonRequest<T> {
    private static final String TAG = JacksonRequest.class.getSimpleName();

    protected Class<T> responseType;

    @Override
    protected Map<String, String> getParams() {
        Map<String, String> map = new HashMap<>();
        map.put("Content-Type", "application/json");
        return map;
    }

    /**
     * Creates a new request.
     *
     * @param method        the HTTP method to use
     * @param url           URL to fetch the JSON from
     * @param requestData   A {@link Object} to post and convert into json as the request. Null is allowed and indicates no parameters will be posted along with request.
     * @param listener      Listener to receive the JSON response
     * @param errorListener Error listener, or null to ignore errors.
     */
    public JacksonRequest(int method, String url, Object requestData, Class<T> responseType,
                          Response.Listener<T> listener, Response.ErrorListener errorListener) {
        super(method, url, (requestData == null) ?
                null :
                Mapper.string(requestData), listener, errorListener);
        this.responseType = responseType;

        if (BuildConfig.DEBUG) {
            AppUtils.INSTANCE.printLog(Log.DEBUG, TAG, "RequestJsonString = " + ((requestData == null) ? "" : Mapper.string(requestData)));
            AppUtils.INSTANCE.printLog(Log.INFO, TAG, "RequestWsUrl = " + (getUrl() == null ? "" : getUrl()));
        }
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));

            if (BuildConfig.DEBUG) {
                AppUtils.INSTANCE.printLog(Log.DEBUG, TAG, "ResponseJsonString = " + jsonString);
                AppUtils.INSTANCE.printLog(Log.INFO, TAG, "ResponseWsUrl = " + (getUrl() == null ? "" : getUrl()));
            }

            return Response.success(Mapper.objectOrThrow(jsonString, responseType), HttpHeaderParser.parseCacheHeaders(response));
        } catch (Exception e) {
            return Response.error(new VolleyError(e));
        }
    }

    @Override
    protected VolleyError parseNetworkError(VolleyError volleyError) {
        if (BuildConfig.DEBUG) {
            AppUtils.INSTANCE.printLog(Log.ERROR, TAG, "ResponseErrorCode = " + String.valueOf((volleyError != null && volleyError.networkResponse != null) ? volleyError.networkResponse.statusCode : -1));
            AppUtils.INSTANCE.printLog(Log.ERROR, TAG, "ResponseErrorMessage = " + ((volleyError != null && !TextUtils.isEmpty(volleyError.toString())) ? volleyError.toString() : ""));
            AppUtils.INSTANCE.printLog(Log.INFO, TAG, "ResponseWsUrl = " + (getUrl() == null ? "" : getUrl()));
        }

        return super.parseNetworkError(volleyError);
    }
}
