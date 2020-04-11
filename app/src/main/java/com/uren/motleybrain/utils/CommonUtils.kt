package com.uren.motleybrain.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.StateListDrawable
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.Uri
import android.text.format.DateUtils
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast

import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

import com.google.android.material.snackbar.Snackbar
import com.uren.motleybrain.Constants.CustomConstants.APP_GOOGLE_PLAY_DEFAULT_LINK
import com.uren.motleybrain.R

import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.Objects
import java.util.Random
import java.util.TimeZone
import java.util.concurrent.TimeUnit


object CommonUtils {

    val language: String
        get() = Locale.getDefault().language


    fun showToastShort(context: Context, message: String) {

        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    fun showToastLong(context: Context, message: String) {

        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    fun getPaddingInPixels(context: Context, dpSize: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dpSize * scale + 0.5f).toInt()
    }


    fun getVersionName(context: Context): String {

        var pInfo: PackageInfo? = null
        try {
            pInfo = context.packageManager.getPackageInfo(context.packageName, 0)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        return Objects.requireNonNull<PackageInfo>(pInfo).versionName

    }

    fun getVersion(context: Context): String {
        try {
            val packageManager = context.packageManager
            val packInfo = packageManager.getPackageInfo(context.packageName, 0)
            return packInfo.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            return "0"
        }

    }

    fun commentApp(context: Context) {
        try {
            val mAddress = "market://details?id=" + context.packageName
            val marketIntent = Intent("android.intent.action.VIEW")
            marketIntent.data = Uri.parse(mAddress)
            context.startActivity(marketIntent)
        } catch (e: Exception) {
            Toast.makeText(context, context.getString(R.string.commentFailed), Toast.LENGTH_SHORT)
                .show()
        }

    }

    fun checkCameraHardware(context: Context): Boolean {
        // this device has a camera
        return context.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA)
    }

    fun hideKeyBoard(context: Context) {
        val activity = context as Activity
        val inputMethodManager =
            context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        if (activity.currentFocus != null) {
            Objects.requireNonNull(inputMethodManager).hideSoftInputFromWindow(
                activity.currentFocus!!.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS
            )
        }
    }

    fun getGooglePlayAppLink(context: Context): String {
        return APP_GOOGLE_PLAY_DEFAULT_LINK + context.packageName
    }

    fun timeAgo(context: Context, createAt: String): String {

        var convTime = ""
        val resources = context.resources
        //String suffix = resources.getString(R.string.ago);
        val suffix = ""

        val nowTime = Date()
        val date = CommonUtils.fromISO8601UTC(createAt)

        val dateDiff = nowTime.time - Objects.requireNonNull<Date>(date).time

        val second = TimeUnit.MILLISECONDS.toSeconds(dateDiff)
        val minute = TimeUnit.MILLISECONDS.toMinutes(dateDiff)
        val hour = TimeUnit.MILLISECONDS.toHours(dateDiff)
        val day = TimeUnit.MILLISECONDS.toDays(dateDiff)

        if (second < 60) {
            convTime =
                second.toString() + " " + resources.getString(R.string.seconds) + " " + suffix
        } else if (minute < 60) {
            convTime =
                minute.toString() + " " + resources.getString(R.string.minutes) + " " + suffix
        } else if (hour < 24) {
            convTime = hour.toString() + " " + resources.getString(R.string.hours) + " " + suffix
        } else if (day >= 7) {
            if (day > 30) {
                convTime =
                    (day / 30).toString() + " " + resources.getString(R.string.months) + " " + suffix
            } else if (day > 360) {
                convTime =
                    (day / 360).toString() + " " + resources.getString(R.string.years) + " " + suffix
            } else {
                convTime =
                    (day / 7).toString() + " " + resources.getString(R.string.weeks) + " " + suffix
            }
        } else if (day < 7) {
            convTime = day.toString() + " " + resources.getString(R.string.days) + " " + suffix
        }

        return convTime
    }

    fun getMessageTime(context: Context, time: Long): String {
        val dateValueStr: String
        val hour: String

        val date = Date(time)

        val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
        format.timeZone = TimeZone.getDefault()
        val formatted = format.format(date)
        hour = formatted.substring(11, 16)

        val todayDate = Date(System.currentTimeMillis())
        val formattedTodayDate = format.format(todayDate)

        if (formatted.substring(0, 10) == formattedTodayDate.substring(0, 10))
            dateValueStr = context.resources.getString(R.string.TODAY)
        else if (isYesterday(date))
            dateValueStr = context.resources.getString(R.string.YESTERDAY)
        else {
            val monthArray = context.resources.getStringArray(R.array.months)
            val monthValue = monthArray[Integer.parseInt(formatted.substring(5, 7)) - 1]

            dateValueStr = (formatted.substring(8, 10) + " "
                    + monthValue.substring(0, 3) +
                    " " + formatted.substring(0, 4))
        }

        return "$dateValueStr  $hour"
    }


    fun isYesterday(d: Date): Boolean {
        return DateUtils.isToday(d.time + DateUtils.DAY_IN_MILLIS)
    }

    fun fromISO8601UTC(dateStr: String): Date? {

        val tz = TimeZone.getTimeZone("UTC")
        val df = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        df.timeZone = tz

        try {
            return df.parse(dateStr)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return null
    }

    fun isNetworkConnected(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)!!.state == NetworkInfo.State.CONNECTED || connectivityManager.getNetworkInfo(
            ConnectivityManager.TYPE_WIFI
        )!!.state == NetworkInfo.State.CONNECTED

    }

    fun connectionErrSnackbarShow(view: View, context: Context) {
        val snackbar = Snackbar.make(
            view,
            context.resources.getString(R.string.CHECK_YOUR_INTERNET_CONNECTION),
            Snackbar.LENGTH_SHORT
        )
        val snackBarView = snackbar.view
        snackBarView.setBackgroundColor(context.resources.getColor(R.color.Red))
        val tv = snackBarView.findViewById(com.google.android.material.R.id.snackbar_text) as TextView
        tv.setTextColor(context.resources.getColor(R.color.White))
        snackbar.show()
    }

    fun snackbarShow(view: View, context: Context, message: String, colorId: Int) {
        val snackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT)
        val snackBarView : View = snackbar.view
        snackBarView.setBackgroundColor(context.resources.getColor(colorId))
        val tv = snackBarView.findViewById(com.google.android.material.R.id.snackbar_text) as TextView
        tv.setTextColor(context.resources.getColor(R.color.White))
        snackbar.show()
    }

    fun getRandomColor(context: Context): Int {

        val colorList = intArrayOf(
            R.color.yellow_green_color_picker,
            R.color.dot_light_screen2,
            R.color.PeachPuff,
            R.color.Gold,
            R.color.Pink,
            R.color.LightPink,
            R.color.dot_light_screen3,
            R.color.dot_light_screen4,
            R.color.dot_light_screen1,
            R.color.LemonChiffon,
            R.color.PapayaWhip,
            R.color.Wheat,
            R.color.Azure,
            R.color.PaleGoldenrod,
            R.color.Thistle,
            R.color.LightBlue,
            R.color.LightCoral,
            R.color.PaleGoldenrod,
            R.color.Violet,
            R.color.DarkSalmon,
            R.color.Lavender,
            R.color.Yellow,
            R.color.LightBlue,
            R.color.DarkGray,
            R.color.SharedPostEndColor,
            R.color.CaughtPostEndColor,
            R.color.Yellow,
            R.color.Violet,
            R.color.PaleGreen,
            R.color.LightCyan
        )

        val rand = Random()
        return colorList[rand.nextInt(colorList.size)]
    }

    fun getDarkRandomColor(context: Context): Int {

        val colorList = intArrayOf(
            R.color.style_color_primary,
            R.color.style_color_accent,
            R.color.fab_color_pressed,
            R.color.blue_color_picker,
            R.color.brown_color_picker,
            R.color.green_color_picker,
            R.color.orange_color_picker,
            R.color.red_color_picker,
            R.color.red_orange_color_picker,
            R.color.violet_color_picker,
            R.color.dot_dark_screen1,
            R.color.dot_dark_screen2,
            R.color.dot_dark_screen3,
            R.color.dot_dark_screen4,
            R.color.Fuchsia,
            R.color.DarkRed,
            R.color.Olive,
            R.color.Purple,
            R.color.gplus_color_1,
            R.color.gplus_color_2,
            R.color.gplus_color_3,
            R.color.gplus_color_4,
            R.color.MediumTurquoise,
            R.color.RoyalBlue,
            R.color.Green
        )

        val rand = Random()
        return colorList[rand.nextInt(colorList.size)]
    }

    fun setDrawableSelector(context: Context, normal: Int, selected: Int): Drawable {

        val drawable: StateListDrawable

        val state_normal = ContextCompat.getDrawable(context, normal)
        val state_pressed = ContextCompat.getDrawable(context, selected)

        val state_normal_bitmap =
            (Objects.requireNonNull<Drawable>(state_normal) as BitmapDrawable).bitmap

        // Setting alpha directly just didn't work, so we draw a new bitmap!
        val disabledBitmap = Bitmap.createBitmap(
            state_normal!!.intrinsicWidth,
            state_normal.intrinsicHeight, Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(disabledBitmap)

        val paint = Paint()
        paint.alpha = 126
        canvas.drawBitmap(state_normal_bitmap, 0f, 0f, paint)

        val state_normal_drawable = BitmapDrawable(context.resources, disabledBitmap)

        drawable = StateListDrawable()

        drawable.addState(
            intArrayOf(android.R.attr.state_selected),
            state_pressed
        )
        drawable.addState(
            intArrayOf(android.R.attr.state_enabled),
            state_normal_drawable
        )

        return drawable
    }

    /*fun readCountryCodes(context: Context): String {
        val inputStream = context.resources.openRawResource(R.raw.country_codes)
        val outputStream = ByteArrayOutputStream()

        val buf = ByteArray(1024)
        var len: Int
        try {
            while ((len = inputStream.read(buf)) != -1) {
                outputStream.write(buf, 0, len)
            }
            outputStream.close()
            inputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return outputStream.toString()
    }*/

    fun showKeyboard(context: Context, showKeyboard: Boolean, editText: EditText) {

        if (showKeyboard) {
            val imm =
                Objects.requireNonNull(context).getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            Objects.requireNonNull(imm).toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
        } else {
            val imm = Objects.requireNonNull(context).getSystemService(
                Context.INPUT_METHOD_SERVICE
            ) as InputMethodManager
            Objects.requireNonNull(imm).hideSoftInputFromWindow(editText.windowToken, 0)
            editText.isFocusable = false
            editText.isFocusableInTouchMode = true
        }
    }
}