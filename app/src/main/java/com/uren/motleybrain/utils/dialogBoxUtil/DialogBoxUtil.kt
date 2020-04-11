package com.uren.motleybrain.utils.dialogBoxUtil

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Handler
import android.widget.ArrayAdapter
import com.uren.motleybrain.Constants.CustomConstants.CODE_CAMERA_POSITION
import com.uren.motleybrain.Constants.CustomConstants.CODE_GALLERY_POSITION
import com.uren.motleybrain.Constants.CustomConstants.CODE_PHOTO_EDIT
import com.uren.motleybrain.Constants.CustomConstants.CODE_PHOTO_REMOVE
import com.uren.motleybrain.Constants.CustomConstants.CODE_SCREENSHOT_POSITION
import com.uren.motleybrain.Constants.CustomConstants.REQUEST_CODE_ENABLE_LOCATION
import com.uren.motleybrain.R
import com.uren.motleybrain.utils.CommonUtils
import com.uren.motleybrain.utils.dialogBoxUtil.Interfaces.*


object DialogBoxUtil {

    fun photoChosenDialogBox(
        context: Context,
        title: String?,
        photoExist: Boolean,
        photoChosenCallback: PhotoChosenCallback
    ) {
        CommonUtils.hideKeyBoard(context)
        val adapter = ArrayAdapter<String>(context, android.R.layout.simple_list_item_1)
        adapter.add("  " + context.resources.getString(R.string.openGallery))
        adapter.add("  " + context.resources.getString(R.string.openCamera))

        if (photoExist)
            adapter.add("  " + context.resources.getString(R.string.REMOVE_PHOTO))

        val builder = AlertDialog.Builder(context)
        if (title != null && !title.isEmpty())
            builder.setTitle(title)

        builder.setAdapter(adapter) { dialog, item ->
            if (item == CODE_GALLERY_POSITION)
                photoChosenCallback.onGallerySelected()
            else if (item == CODE_CAMERA_POSITION)
                photoChosenCallback.onCameraSelected()
            else if (item == CODE_PHOTO_REMOVE) {
                photoChosenCallback.onPhotoRemoved()
            }
        }
        val alert = builder.create()
        alert.show()
    }

    fun photoChosenForProblemReportDialogBox(
        context: Context,
        title: String?,
        photoChosenForReportCallback: PhotoChosenForReportCallback
    ) {
        CommonUtils.hideKeyBoard(context)
        val adapter = ArrayAdapter<String>(context, android.R.layout.simple_list_item_1)
        adapter.add("  " + context.resources.getString(R.string.openGallery))
        adapter.add("  " + context.resources.getString(R.string.TAKE_SCREENSHOT))

        val builder = AlertDialog.Builder(context)
        if (title != null && !title.isEmpty())
            builder.setTitle(title)

        builder.setAdapter(adapter) { dialog, item ->
            if (item == CODE_GALLERY_POSITION)
                photoChosenForReportCallback.onGallerySelected()
            else if (item == CODE_SCREENSHOT_POSITION)
                photoChosenForReportCallback.onScreenShot()
        }
        val alert = builder.create()
        alert.show()
    }

    fun photoChosenForShareDialogBox(
        context: Context,
        photoExist: Boolean,
        callback: PhotoChosenForShareCallback
    ) {
        CommonUtils.hideKeyBoard(context)
        val adapter = ArrayAdapter<String>(context, android.R.layout.simple_list_item_1)
        adapter.add("  " + context.resources.getString(R.string.openGallery))
        adapter.add("  " + context.resources.getString(R.string.openCamera))

        if (photoExist) {
            adapter.add("  " + context.resources.getString(R.string.REMOVE_PHOTO))
            adapter.add("  " + context.resources.getString(R.string.EDIT))
        }

        val builder = AlertDialog.Builder(context)

        builder.setAdapter(adapter) { dialog, item ->
            if (item == CODE_GALLERY_POSITION)
                callback.onGallerySelected()
            else if (item == CODE_CAMERA_POSITION)
                callback.onCameraSelected()
            else if (item == CODE_PHOTO_REMOVE) {
                callback.onPhotoRemoved()
            } else if (item == CODE_PHOTO_EDIT) {
                callback.onEditted()
            }
        }
        val alert = builder.create()
        alert.show()
    }

    fun showErrorDialog(
        context: Context,
        errMessage: String,
        infoDialogBoxCallback: InfoDialogBoxCallback
    ) {
        CommonUtils.hideKeyBoard(context)
        val builder = AlertDialog.Builder(context)
        builder.setTitle(context.resources.getString(R.string.errorUpper))
        builder.setIcon(context.resources.getDrawable(R.drawable.ic_error_white_24dp))
        builder.setMessage(errMessage)

        builder.setNeutralButton(context.resources.getString(R.string.ok)) { dialog, which ->
            dialog.dismiss()
            infoDialogBoxCallback.okClick()
        }

        val alert = builder.create()
        alert.show()
    }

    fun showInfoDialogBox(
        context: Context,
        message: String,
        title: String?,
        infoDialogBoxCallback: InfoDialogBoxCallback
    ) {
        CommonUtils.hideKeyBoard(context)
        val builder = AlertDialog.Builder(context)
        builder.setIcon(context.resources.getDrawable(R.drawable.ic_info_outline_white_24dp))
        builder.setMessage(message)

        if (title != null && !title.trim { it <= ' ' }.isEmpty())
            builder.setTitle(title)

        builder.setNeutralButton(context.resources.getString(R.string.ok)) { dialog, which ->
            dialog.dismiss()
            infoDialogBoxCallback.okClick()
        }

        val alert = builder.create()
        alert.show()
    }

    fun showSuccessDialogBox(
        context: Context,
        message: String,
        title: String?,
        infoDialogBoxCallback: InfoDialogBoxCallback
    ) {
        CommonUtils.hideKeyBoard(context)
        val alertDialog = AlertDialog.Builder(context).create()
        alertDialog.setIcon(R.drawable.ic_check_white_24dp)

        if (title != null && !title.isEmpty())
            alertDialog.setTitle(title)

        alertDialog.setMessage(message)
        alertDialog.setButton(
            AlertDialog.BUTTON_NEUTRAL, context.resources.getString(R.string.ok)
        ) { dialog, which ->
            dialog.dismiss()
            infoDialogBoxCallback.okClick()
        }
        alertDialog.show()
    }

    fun showYesNoDialog(
        context: Context,
        title: String?,
        message: String,
        yesNoDialogBoxCallback: YesNoDialogBoxCallback
    ) {
        CommonUtils.hideKeyBoard(context)
        val builder = AlertDialog.Builder(context)
        builder.setIcon(R.drawable.ic_warning_white_24dp)
        builder.setMessage(message)
        builder.setCancelable(false)

        if (title != null && !title.isEmpty())
            builder.setTitle(title)

        builder.setPositiveButton(context.resources.getString(R.string.upperYes)) { dialog, which ->
            dialog.dismiss()
            yesNoDialogBoxCallback.yesClick()
        }

        builder.setNegativeButton(context.resources.getString(R.string.upperNo)) { dialog, which ->
            dialog.dismiss()
            yesNoDialogBoxCallback.noClick()
        }

        val alert = builder.create()
        alert.show()
    }


    fun showInfoDialogWithLimitedTime(
        context: Context,
        title: String?,
        message: String,
        timeInMs: Long,
        infoDialogBoxCallback: InfoDialogBoxCallback
    ) {
        CommonUtils.hideKeyBoard(context)
        val builder = AlertDialog.Builder(context)

        if (title != null && !title.isEmpty())
            builder.setTitle(title)

        builder.setIcon(R.drawable.ic_check_white_24dp)
        builder.setMessage(message)
        builder.setCancelable(false)
        val alert = builder.create()
        alert.show()

        Handler().postDelayed({
            alert.dismiss()
            infoDialogBoxCallback.okClick()
        }, timeInMs)
    }

    fun showSettingsAlert(act: Activity) {
        val alertDialog = AlertDialog.Builder(act)
        alertDialog.setTitle(act.resources.getString(R.string.gpsSettings))
        alertDialog.setMessage(act.resources.getString(R.string.gpsSettingMessage))
        alertDialog.setCancelable(false)
        alertDialog.setPositiveButton(act.resources.getString(R.string.settings)) { dialog, which ->
            act.startActivityForResult(
                Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS),
                REQUEST_CODE_ENABLE_LOCATION
            )
        }
        alertDialog.show()
    }

    fun showDialogWithJustPositiveButton(
        context: Context, title: String,
        message: String, buttonDesc: String, infoDialogBoxCallback: InfoDialogBoxCallback
    ) {
        val alertDialog = AlertDialog.Builder(context)
        alertDialog.setTitle(title)
        alertDialog.setMessage(message)
        alertDialog.setCancelable(false)
        alertDialog.setPositiveButton(buttonDesc) { dialog, which -> infoDialogBoxCallback.okClick() }
        alertDialog.show()
    }

}
