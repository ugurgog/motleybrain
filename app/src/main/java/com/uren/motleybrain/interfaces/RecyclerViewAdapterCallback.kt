package com.uren.motleybrain.interfaces


interface RecyclerViewAdapterCallback {
    fun OnRemoved()
    fun OnInserted()
    fun OnChanged(`object`: Any)
}
