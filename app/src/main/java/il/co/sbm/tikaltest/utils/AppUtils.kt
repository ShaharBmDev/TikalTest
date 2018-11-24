package il.co.sbm.tikaltest.utils

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.text.TextUtils
import android.util.Log
import android.support.v4.content.ContextCompat.startActivity

object AppUtils {

    /**
     * @param iLogType One of the following log options: [Log.VERBOSE], [Log.DEBUG], [Log.INFO], [Log.WARN], [Log.ERROR].
     * @param iTag     Name of log tag.
     * @param iString  Log value to print.
     */
    fun printLog(iLogType: Int, iTag: String, iString: String) {
        var dataToPrint = iString

        if (!TextUtils.isEmpty(dataToPrint)) {
            while (dataToPrint.isNotEmpty()) {
                val currentPrint: String

                if (dataToPrint.length > 3500) {
                    currentPrint = dataToPrint.substring(0, 3500)
                    dataToPrint = dataToPrint.substring(3500)
                } else {
                    currentPrint = dataToPrint
                    dataToPrint = ""
                }

                when (iLogType) {
                    Log.VERBOSE -> Log.v(iTag, currentPrint)
                    Log.DEBUG -> Log.d(iTag, currentPrint)
                    Log.INFO -> Log.i(iTag, currentPrint)
                    Log.WARN -> Log.w(iTag, currentPrint)
                    Log.ERROR -> Log.e(iTag, currentPrint)
                    else -> Log.wtf(iTag, currentPrint)
                }
            }
        }
    }

    fun watchYoutubeVideo(iContext: Context, iYoutubeVideoId: String) {
        val appIntent = Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:$iYoutubeVideoId"))
        val webIntent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("http://www.youtube.com/watch?v=$iYoutubeVideoId")
        )
        try {
            iContext.startActivity(appIntent)
        } catch (ex: ActivityNotFoundException) {
            iContext.startActivity(webIntent)
        }
    }

    fun openUrlIntent(iUrl: String): Intent {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(iUrl)
        return intent
    }
}
