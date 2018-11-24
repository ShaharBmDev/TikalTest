package il.co.sbm.tikaltest.model.global

import android.app.Activity
import android.app.Application
import android.content.pm.ActivityInfo
import android.os.Bundle
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import il.co.sbm.tikaltest.R
import il.co.sbm.tikaltest.ui.view.activities.ABaseActivity

class TikalTestApplication : Application() {

    companion object {
        lateinit var mInstance: TikalTestApplication
            private set
    }

    private var mRequestQueue: RequestQueue? = null
    var mIsTablet: Boolean = false
        private set

    override fun onCreate() {
        super.onCreate()
        forceOrientationIfNeeded()
        mInstance = this
        mIsTablet = mInstance.resources.getBoolean(R.bool.isTablet)
    }

    fun addVolleyRequest(iRequest: Request<*>) {

        initVolleyRequestViewIfNeeded()

        mRequestQueue?.add(iRequest)
    }

    private fun initVolleyRequestViewIfNeeded() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(this)
        }
    }

    private fun forceOrientationIfNeeded() {

        registerActivityLifecycleCallbacks(object : Application.ActivityLifecycleCallbacks {

            override fun onActivityCreated(iActivity: Activity?, iBundle: Bundle?) {

                if (iActivity is ABaseActivity) {
                    if (iActivity.shouldForceLanscapeInTabletMode(mIsTablet)) {
                        iActivity.requestedOrientation = if (mIsTablet) ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                        else ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                    }
                }
            }

            override fun onActivityStarted(iActivity: Activity?) {
            }

            override fun onActivityResumed(iActivity: Activity?) {
            }

            override fun onActivityPaused(iActivity: Activity?) {
            }

            override fun onActivityStopped(iActivity: Activity?) {
            }

            override fun onActivityDestroyed(iActivity: Activity?) {
            }

            override fun onActivitySaveInstanceState(iActivity: Activity?, iBundle: Bundle?) {
            }
        })
    }
}