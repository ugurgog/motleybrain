package com.uren.motleybrain.interfaces

interface FileSaveCallback {
    fun Saved(realPath: String)
    fun OnError(e: Exception)
}
