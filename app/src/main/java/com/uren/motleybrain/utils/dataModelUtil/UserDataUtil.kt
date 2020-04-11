package com.uren.motleybrain.utils.dataModelUtil

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView

import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.uren.motleybrain.Constants.CustomConstants.CHAR_AMPERSAND
import com.uren.motleybrain.Models.User
import com.uren.motleybrain.R
import com.uren.motleybrain.utils.CommonUtils
import com.uren.motleybrain.utils.ShapeUtil

object UserDataUtil {

    fun setNameOrUserName(name: String?, textView: TextView) {
        val nameMaxLen = 25

        if (name != null && !name.isEmpty()) {
            if (name.length > nameMaxLen)
                textView.text = name.trim { it <= ' ' }.substring(0, nameMaxLen) + "..."
            else
                textView.text = name
        } else
            textView.visibility = View.GONE
    }

    fun getNameOrUsername(name: String?, username: String?): String {
        val nameMaxLen = 25

        return if (name != null && !name.isEmpty()) {
            if (name.length > nameMaxLen)
                name.trim { it <= ' ' }.substring(0, nameMaxLen) + "..."
            else
                name
        } else if (username != null && !username.isEmpty()) {
            if (username.length > nameMaxLen)
                CHAR_AMPERSAND + username.trim { it <= ' ' }.substring(0, nameMaxLen) + "..."
            else
                CHAR_AMPERSAND + username
        } else
            "unknown"
    }

    fun getNameOrUsernameFromUser(user: User?): String {
        val nameMaxLen = 25

        if (user == null) return "unknown"

        return if (user.name != null && !user.name!!.isEmpty()) {
            if (user.name?.length!! > nameMaxLen)
                user.name?.trim()?.substring(0, nameMaxLen) + "..."
            else
                user.name!!
        } else
            "unknown"
    }

    fun setName(name: String?, nameTextView: TextView?) {
        val nameMaxLen = 25
        if (name != null && nameTextView != null && !name.isEmpty()) {
            nameTextView.visibility = View.VISIBLE
            if (name.length > nameMaxLen)
                nameTextView.text = name.trim { it <= ' ' }.substring(0, nameMaxLen) + "..."
            else
                nameTextView.text = name
        } else if (nameTextView != null)
            nameTextView.visibility = View.GONE
    }

    fun getShortenUserName(name: String?): String {
        val returnValue = StringBuilder()
        if (name != null && !name.trim { it <= ' ' }.isEmpty()) {
            val seperatedName =
                name.trim { it <= ' ' }.split(" ".toRegex()).dropLastWhile { it.isEmpty() }
                    .toTypedArray()
            for (word in seperatedName) {
                if (returnValue.length < 3)
                    returnValue.append(word.substring(0, 1).toUpperCase())
            }
        }

        return returnValue.toString()
    }

    fun getUsernameFromNameWhenLoginWithGoogle(name: String?): String {
        val returnValue = StringBuilder()
        if (name != null && !name.trim { it <= ' ' }.isEmpty()) {
            val seperatedName =
                name.trim { it <= ' ' }.split(" ".toRegex()).dropLastWhile { it.isEmpty() }
                    .toTypedArray()
            for (word in seperatedName) {
                returnValue.append(word)
            }
        }

        return returnValue.toString()
    }

    fun setProfilePicture(
        context: Context?, url: String?, name: String?, shortNameTv: TextView,
        profilePicImgView: ImageView, circleColorVal: Boolean
    ): Int {
        if (context == null) return 0

        var picExist = false
        if (url != null && !url.trim { it <= ' ' }.isEmpty()) {
            shortNameTv.visibility = View.GONE
            Glide.with(context)
                .load(url)
                .apply(RequestOptions.circleCropTransform())
                .into(profilePicImgView)
            picExist = true
            //profilePicImgView.setPadding(1, 1, 1, 1); // degerler asagidaki imageShape strokeWidth ile aynı tutulmalı
        } else {
            if (name != null && !name.trim { it <= ' ' }.isEmpty()) {
                shortNameTv.visibility = View.VISIBLE
                shortNameTv.text = UserDataUtil.getShortenUserName(name)
                profilePicImgView.setImageDrawable(null)
            } else {
                shortNameTv.visibility = View.GONE
                Glide.with(context)
                    .load(R.drawable.ic_person_white_24dp)
                    .apply(RequestOptions.circleCropTransform())
                    .into(profilePicImgView)
            }
        }

        val imageShape: GradientDrawable
        val colorCode = CommonUtils.getDarkRandomColor(context)

        if (circleColorVal) {
            if (picExist) {
                imageShape = ShapeUtil.getShape(
                    context.resources.getColor(R.color.White),
                    context.resources.getColor(R.color.DodgerBlue),
                    GradientDrawable.OVAL, 50F, 3
                )
            } else {
                imageShape = ShapeUtil.getShape(
                    context.resources.getColor(R.color.DodgerBlue),
                    context.resources.getColor(R.color.DodgerBlue),
                    GradientDrawable.OVAL, 50F, 3
                )
            }
        } else
            imageShape = ShapeUtil.getShape(
                context.resources.getColor(colorCode),
                context.resources.getColor(R.color.White),
                GradientDrawable.OVAL, 50F, 3
            )

        profilePicImgView.background = imageShape

        return if (!picExist)
            colorCode
        else
            0
    }
}
