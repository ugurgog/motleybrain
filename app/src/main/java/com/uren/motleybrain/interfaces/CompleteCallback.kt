package com.uren.motleybrain.interfaces

interface CompleteCallback {
    fun onComplete(`object`: Any)
    fun onFailed(message: String)
}
