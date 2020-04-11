package com.uren.motleybrain.login

import android.content.Context
import android.text.TextUtils
import com.uren.motleybrain.R


class Validation private constructor() {
    var errorMessage: String? = null
        private set

    fun isValidUserName(context: Context, userName: String): Boolean {

        if (TextUtils.isEmpty(userName)) {
            errorMessage = context.getString(R.string.USERNAME_ERR_REQUIRED)
            return false
        }

        return true
    }

    fun isValidEmail(context: Context, email: String): Boolean {

        if (TextUtils.isEmpty(email)) {
            errorMessage = context.getString(R.string.EMAIL_ERR_REQUIRED)
            return false
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            errorMessage = context.getString(R.string.EMAIL_ERR_INVALID)
            return false
        }

        return true
    }

    fun isValidPassword(context: Context, password: String): Boolean {

        if (TextUtils.isEmpty(password)) {
            errorMessage = context.getString(R.string.PASSWORD_ERR_REQUIRED)
            return false
        }

        if (password.length < PASSWORD_MAX_LENGTH) {
            errorMessage = context.getString(R.string.PASSWORD_ERR_LENGTH)
            return false
        }

        return true
    }

    companion object {

        private var instance: Validation? = null

        private val PASSWORD_MAX_LENGTH = 6

        fun getInstance(): Validation {
            if (instance == null) {
                instance = Validation()
            }
            return instance as Validation
        }
    }
}
