package il.co.sbm.tikaltest.ui.view.activities

import android.support.v7.app.AppCompatActivity
import il.co.sbm.tikaltest.model.global.TikalTestApplication

abstract class ABaseActivity : AppCompatActivity() {

    abstract fun shouldForceLanscapeInTabletMode(iIsTablet: Boolean): Boolean

    protected val mIsTablet = TikalTestApplication.mInstance.mIsTablet
}