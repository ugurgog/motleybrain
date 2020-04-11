package com.uren.motleybrain.management

import android.content.Context
import androidx.fragment.app.Fragment


open class BaseFragment : Fragment() {

    lateinit var mFragmentNavigation: FragmentNavigation

    fun getmFragmentNavigation(): FragmentNavigation {
        return mFragmentNavigation
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is FragmentNavigation) {
            mFragmentNavigation = context
        }
    }

    interface FragmentNavigation {
        fun pushFragment(fragment: Fragment)
        fun pushFragment(fragment: Fragment, animationTag: String)
    }
}
