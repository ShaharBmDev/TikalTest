package il.co.sbm.tikaltest.utils

import android.text.TextUtils

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created with care by Shahar Ben-Moshe on 12/01/16.
 */
object DateTimeUtils {
    const val DATE_FORMAT_dd_MM_yyyy_T_HH_mm_ss = "yyyy-MM-dd'T'HH:mm:ss"
    const val DATE_FORMAT_dd_MM_yyyy = "dd-MM-yyyy"
    const val DATE_FORMAT_yyyy_MM_dd = "yyyy-MM-dd"
    const val DATE_FORMAT_SLASHES_dd_MM_yyyy = "dd/MM/yyyy"
    const val DATE_FORMAT_dd_MM_yy = "dd-MM-yy"
    const val DATE_FORMAT_dd_MM = "dd/MM"
    const val DATE_FORMAT_HH_mm = "HH:mm"
    const val DATE_FORMAT_DAY_OF_WEEK = "EEEE"
    const val DATE_FORMAT_YEAR = "yyyy"
    const val DATE_FORMAT_SQLITE_DB = "yyyy-MM-dd HH:mm:ss"

    const val LOCALE_HEBREW = "iw"

    const val HOURS_IN_A_DAY: Int = 24

    /**
     * @param iStartDate Start date of range.
     * @param iEndDate   End date of range.
     * @return All the dates in range of these two dates, including those dates, or empty list if there was a problem.
     */
    fun getDaysBetweenDates(iStartDate: Date?, iEndDate: Date?): ArrayList<Date> {
        val dates = ArrayList<Date>()

        if (iStartDate != null && iEndDate != null) {
            val calendar = GregorianCalendar()
            calendar.time = iStartDate

            while (!calendar.time.after(iEndDate)) {
                val result = calendar.time
                dates.add(result)
                calendar.add(Calendar.DATE, 1)
            }
        }

        return dates
    }

    private fun convertDateToStringFormatWithLocaleOrEmptyString(iDate: Date?, iFormat: String, iLocale: Locale?): String {
        var result = ""
        val locale = iLocale ?: Locale.getDefault()

        if (iDate != null && !TextUtils.isEmpty(iFormat)) {
            val simpleDateFormat = SimpleDateFormat(iFormat, locale)
            result = simpleDateFormat.format(iDate)
        }

        return result
    }

    fun convertStringWithFormatToDateWithLocaleOrNull(iDateInStringToConvert: String, iFormat: String, iLocale: Locale?): Date? {
        var result: Date? = null
        val locale = iLocale ?: Locale.getDefault()


        if (!TextUtils.isEmpty(iDateInStringToConvert) && !TextUtils.isEmpty(iFormat)) {
            val simpleDateFormat = SimpleDateFormat(iFormat, locale)
            try {
                result = simpleDateFormat.parse(iDateInStringToConvert)
            } catch (e: ParseException) {
                e.printStackTrace()
            }

        }

        return result
    }

    fun convertDateInServerDbFormatToString(iDateInStringToConvert: Date): String {
        return convertDateToStringFormatWithLocaleOrEmptyString(iDateInStringToConvert, DATE_FORMAT_dd_MM_yyyy, Locale(LOCALE_HEBREW))
    }

    fun convertDateInServerDbFormatToStringShortYear(iDateInStringToConvert: Date): String {
        return convertDateToStringFormatWithLocaleOrEmptyString(iDateInStringToConvert, DATE_FORMAT_dd_MM_yy, Locale(LOCALE_HEBREW))
    }

    fun convertDateTodayPickerFormat(iCurrentDateItem: Date): String {
        return convertDateToStringFormatWithLocaleOrEmptyString(iCurrentDateItem, DATE_FORMAT_dd_MM, Locale(LOCALE_HEBREW))
    }

    fun convertDateToDayOfWeek(iCurrentDateItem: Date): String {
        return convertDateToStringFormatWithLocaleOrEmptyString(iCurrentDateItem, DATE_FORMAT_DAY_OF_WEEK, Locale(LOCALE_HEBREW))
    }

    fun convertDateToYear(iCurrentDateItem: Date): String {
        return convertDateToStringFormatWithLocaleOrEmptyString(iCurrentDateItem, DATE_FORMAT_YEAR, Locale(LOCALE_HEBREW))
    }

    fun convertDateToStringStringInLocalDbFormat(iDateInStringToConvert: Date): String {
        return convertDateToStringFormatWithLocaleOrEmptyString(iDateInStringToConvert, DATE_FORMAT_SQLITE_DB, Locale(LOCALE_HEBREW))
    }

    /**
     * @param iDateStringInSlashSeparatedDayMonthYear String in the format [DateTimeUtils.DATE_FORMAT_SLASHES_dd_MM_yyyy]
     * @return a date object from the string or null.
     */
    fun convertStringInSlashSeparatedDayMonthYearToDate(iDateStringInSlashSeparatedDayMonthYear: String): Date? {
        return convertStringWithFormatToDateWithLocaleOrNull(iDateStringInSlashSeparatedDayMonthYear, DATE_FORMAT_SLASHES_dd_MM_yyyy, Locale(LOCALE_HEBREW))
    }

    /**
     * @param iDate date.
     * @return a string in the format [DateTimeUtils.DATE_FORMAT_SLASHES_dd_MM_yyyy] or null.
     */
    fun convertDateToSlashSeparatedDayMonthYearToString(iDate: Date): String {
        return convertDateToStringFormatWithLocaleOrEmptyString(iDate, DATE_FORMAT_SLASHES_dd_MM_yyyy, Locale(LOCALE_HEBREW))
    }

    /**
     * Compares two given dates <B>WITHOUT</B> consideration of time.
     *
     * @param iDate1 Mandatory.
     * @param iDate2 Mandatory.
     * @return One of the following results:<BR></BR>
     * <B>-2</B> If something went wrong.<BR></BR>
     * <B>-1</B> If iDate1 is before iDate2.<BR></BR>
     * <B>0</B> If dates are equal.<BR></BR>
     * <B>1</B> If iDate1 is after iDate2.
     */
    fun compareDatesOnly(iDate1: Date?, iDate2: Date?): Int {
        var result = -2

        if (iDate1 != null && iDate2 != null) {
            val c1 = Calendar.getInstance()
            c1.time = iDate1
            c1.set(Calendar.MILLISECOND, 0)
            c1.set(Calendar.SECOND, 0)
            c1.set(Calendar.MINUTE, 0)
            c1.set(Calendar.HOUR_OF_DAY, 0)

            val c2 = Calendar.getInstance()
            c2.time = iDate2
            c2.set(Calendar.MILLISECOND, 0)
            c2.set(Calendar.SECOND, 0)
            c2.set(Calendar.MINUTE, 0)
            c2.set(Calendar.HOUR_OF_DAY, 0)

            result = c1.compareTo(c2)
        }

        return result
    }

    fun isSameDate(iDate1: Date, iDate2: Date): Boolean {
        return compareDatesOnly(iDate1, iDate2) == 0
    }

    /**
     * Compares two given dates <B>WITHOUT</B> consideration of year, month, day and milliseconds.<BR></BR>
     * That means it only compares Hour of day and Minute.
     *
     * @param iDate1 Mandatory.
     * @param iDate2 Mandatory.
     * @return One of the following results:<BR></BR>
     * <B>-2</B> If something went wrong.<BR></BR>
     * <B>-1</B> If iDate1 is before iDate2.<BR></BR>
     * <B>0</B> If dates are equal.<BR></BR>
     * <B>1</B> If iDate1 is after iDate2.
     */
    fun compareTimeOnly(iDate1: Date?, iDate2: Date?): Int {
        var result = -2

        if (iDate1 != null && iDate2 != null) {
            val c1 = Calendar.getInstance()
            c1.time = iDate1
            c1.set(Calendar.YEAR, 0)
            c1.set(Calendar.MONTH, 0)
            c1.set(Calendar.DAY_OF_MONTH, 0)
            c1.set(Calendar.MILLISECOND, 0)

            val c2 = Calendar.getInstance()
            c2.time = iDate2
            c2.set(Calendar.YEAR, 0)
            c2.set(Calendar.MONTH, 0)
            c2.set(Calendar.DAY_OF_MONTH, 0)
            c2.set(Calendar.MILLISECOND, 0)

            result = c1.compareTo(c2)
        }

        return result
    }

    fun compareTimesOnly(iStartTime1: String, iStartTime2: String): Int {
        val timeOnly1 = convertStringWithFormatToDateWithLocaleOrNull(iStartTime1, DATE_FORMAT_HH_mm, Locale(LOCALE_HEBREW))
        val timeOnly2 = convertStringWithFormatToDateWithLocaleOrNull(iStartTime2, DATE_FORMAT_HH_mm, Locale(LOCALE_HEBREW))

        return compareTimeOnly(timeOnly1, timeOnly2)
    }

    /**
     * @param iTimeInDay                Time to check if is in range.
     * @param iRangeStartTime           Minimum of time range.
     * @param iRangeEndTime             Maximum of time range.
     * @param iShouldIncludeRangeValues Determines whether should also include values who are equal to the max or minimum.
     * @return True if iTimeInDay is between the min & max values, false if any other way or any of the parameters is null.
     */
    fun isTimeOfDayInRange(iTimeInDay: Date?, iRangeStartTime: Date?, iRangeEndTime: Date?, iShouldIncludeRangeValues: Boolean): Boolean {
        var result = false

        if (iTimeInDay != null && iRangeStartTime != null && iRangeEndTime != null) {
            val timeInDay = createTimeOnlyDateObject(iTimeInDay)
            val rangeStartTime = createTimeOnlyDateObject(iRangeStartTime)
            val rangeEndTime = createTimeOnlyDateObject(iRangeEndTime)

            if (timeInDay != null && rangeStartTime != null && rangeEndTime != null) {
                result = if (iShouldIncludeRangeValues) {
                    timeInDay.time >= rangeStartTime.time && timeInDay.time <= rangeEndTime.time
                } else {
                    timeInDay.time > rangeStartTime.time && timeInDay.time < rangeEndTime.time
                }
            }
        }

        return result
    }

    fun isTimeOfDayInRange(iTimeInDay: Date, iRangeStartTime: String, iRangeEndTime: String, iShouldIncludeRangeValues: Boolean): Boolean {
        val rangeStartTime = convertStringWithFormatToDateWithLocaleOrNull(iRangeStartTime, DATE_FORMAT_HH_mm, Locale(LOCALE_HEBREW))
        val rangeEndTime = convertStringWithFormatToDateWithLocaleOrNull(iRangeEndTime, DATE_FORMAT_HH_mm, Locale(LOCALE_HEBREW))

        return isTimeOfDayInRange(iTimeInDay, rangeStartTime, rangeEndTime, iShouldIncludeRangeValues)
    }

    fun createTimeOnlyDateObject(iDate: Date?): Date? {
        var result: Date? = null

        if (iDate != null) {
            val calendar = Calendar.getInstance()
            calendar.time = iDate
            calendar.set(0, 0, 0, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), 0)
            result = calendar.time
        }

        return result
    }

    fun createDateOnlyDateObject(iDate: Date?): Date? {
        var result: Date? = null

        if (iDate != null) {
            val calendar = Calendar.getInstance()
            calendar.time = iDate
            calendar.set(Calendar.MILLISECOND, 0)
            calendar.set(Calendar.SECOND, 0)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.HOUR_OF_DAY, 0)
            result = calendar.time
        }

        return result
    }

    fun convertYearMonthDayToDate(iYear: Int, iMonthOfYear: Int, iDayOfMonth: Int): Date {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, iYear)
        calendar.set(Calendar.MONTH, iMonthOfYear)
        calendar.set(Calendar.DAY_OF_MONTH, iDayOfMonth)

        return calendar.time
    }

    fun tryParseLongStringToDate(iLongInString: String): Date? {
        var result: Date? = null

        if (!TextUtils.isEmpty(iLongInString)) {
            var timeInMillisecondsAnswer: Long? = null

            try {
                timeInMillisecondsAnswer = java.lang.Long.parseLong(iLongInString)
            } catch (ignored: Exception) {
            }

            if (timeInMillisecondsAnswer != null) {
                val calendar = Calendar.getInstance()
                calendar.timeInMillis = timeInMillisecondsAnswer

                result = calendar.time
            }
        }

        return result
    }

    fun tryParseSQLiteDateStringToDate(iSQLiteString: String): Date? {
        var result: Date? = null

        if (!TextUtils.isEmpty(iSQLiteString)) {
            result = convertStringWithFormatToDateWithLocaleOrNull(iSQLiteString, DATE_FORMAT_SQLITE_DB, Locale(LOCALE_HEBREW))
        }

        return result
    }

    fun tryParseDateToSQLiteDateString(iDateToParse: Date): String {
        return convertDateToStringFormatWithLocaleOrEmptyString(iDateToParse, DATE_FORMAT_SQLITE_DB, Locale(LOCALE_HEBREW))
    }

    /**
     * Comapres time difference between two dates.
     * @param iDate1
     * @param iDate2
     *
     * @return How much [iDate2] is after [iDate1] in milliseconds
     */
    fun getTimeDifferenceInMilliseconds(iDate1: Date?, iDate2: Date?): Long? {

        var result: Long? = null

        if (iDate1 != null && iDate2 != null) {
            result = iDate2.time - iDate1.time
        }

        return result

    }

    /**
     * Comapres time difference between two dates.
     * @param iDate1
     * @param iDate2
     *
     * @return How much [iDate2] is after [iDate1] in seconds
     */
    fun getTimeDifferenceInSseconds(iDate1: Date?, iDate2: Date?): Long? {
        return getTimeDifferenceInMilliseconds(iDate1, iDate2)?.div(1000)
    }

    /**
     * Comapres time difference between two dates.
     * @param iDate1
     * @param iDate2
     *
     * @return How much [iDate2] is after [iDate1] in minutes
     */
    fun getTimeDifferenceInMinutes(iDate1: Date?, iDate2: Date?): Long? {
        return getTimeDifferenceInSseconds(iDate1, iDate2)?.div(60)
    }

    /**
     * Comapres time difference between two dates.
     * @param iDate1
     * @param iDate2
     *
     * @return How much [iDate2] is after [iDate1] in hours
     */
    fun getTimeDifferenceInHours(iDate1: Date?, iDate2: Date?): Long? {
        return getTimeDifferenceInMinutes(iDate1, iDate2)?.div(60)
    }
}
