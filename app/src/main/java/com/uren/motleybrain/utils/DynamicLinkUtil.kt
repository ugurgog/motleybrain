package com.uren.motleybrain.utils

import android.content.Context
import android.content.Intent
import android.net.Uri

import androidx.fragment.app.Fragment
import com.uren.motleybrain.Constants.CustomConstants.APP_INVITATION_LINK
import com.uren.motleybrain.Models.Contact
import com.uren.motleybrain.R

object DynamicLinkUtil {

    fun setAppInvitationLink(context: Context, fragment: Fragment) {

        val intent = Intent()
        val msg =
            context.resources.getString(R.string.CONTACT_INVITE_MESSAGE) + " " + APP_INVITATION_LINK
        intent.action = Intent.ACTION_SEND
        intent.putExtra(Intent.EXTRA_TEXT, msg)
        intent.type = "text/plain"
        if (intent.resolveActivity(context.packageManager) != null)
            fragment.startActivity(Intent.createChooser(intent, "Share"))
    }

    fun setAppInvitationLinkForSms(context: Context, contact: Contact?, fragment: Fragment) {
        if (contact != null && contact!!.phoneNumber != null) {
            val sendIntent = Intent(Intent.ACTION_VIEW)
            sendIntent.data = Uri.parse("sms:" + contact!!.phoneNumber)
            val msg =
                context.resources.getString(R.string.CONTACT_INVITE_MESSAGE) + " " + APP_INVITATION_LINK
            sendIntent.putExtra("sms_body", msg)
            fragment.startActivity(sendIntent)
        }
    }
}
