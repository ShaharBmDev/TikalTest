package il.co.sbm.tikaltest.ui.view.activities

import android.os.Bundle
import il.co.sbm.tikaltest.R
import il.co.sbm.tikaltest.ui.view.fragments.MovieDetailsFragment
import il.co.sbm.tikaltest.ui.view.fragments.MoviesListFragment

class MainActivity : ABaseActivity() {

    override fun shouldForceLanscapeInTabletMode(iIsTablet: Boolean): Boolean {
        return iIsTablet
    }

    override fun onCreate(iSavedInstanceState: Bundle?) {

        super.onCreate(iSavedInstanceState)
        setContentView(R.layout.main_activity)

        if (iSavedInstanceState == null) {
            resolveLayoutsByDeviceType()
        }
    }

    /**
     * changes behaviour and relations between the fragments according to the device type
     */
    private fun resolveLayoutsByDeviceType() {
        if (mIsTablet) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fl_main_overview, MoviesListFragment.newInstance())
                .commitNow()

            supportFragmentManager.beginTransaction()
                .replace(R.id.fl_main_content, MovieDetailsFragment.newInstance())
                .commitNow()
        } else {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MoviesListFragment.newInstance())
                .commitNow()
        }
    }
}
