package com.uren.motleybrain.utils


import android.app.ProgressDialog
import android.content.Context
import com.uren.motleybrain.R


class ProgressDialogUtil(
    internal var context: Context,
    internal var message: String?,
    internal var cancelableValue: Boolean) {

    internal lateinit var progressDialog: ProgressDialog

    init {
        setProgressDialog()
    }

    fun setProgressDialog() {
        progressDialog = ProgressDialog(context)
        if (message != null && !message!!.trim { it <= ' ' }.isEmpty())
            progressDialog.setMessage(message)
        else
            progressDialog.setMessage(context.resources.getString(R.string.loading))
        progressDialog.setCancelable(cancelableValue)
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER)
    }

    fun dialogShow() {
        if (!progressDialog.isShowing) progressDialog.show()
    }

    fun dialogDismiss() {
        if (progressDialog.isShowing) progressDialog.dismiss()
    }
}
