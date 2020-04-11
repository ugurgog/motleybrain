package com.uren.motleybrain.statistics

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

//import butterknife.ButterKnife
import com.uren.motleybrain.R
import com.uren.motleybrain.management.BaseFragment

class StatisticsFragment : BaseFragment() {

    private lateinit var mView: View

    override fun onStart() {
        super.onStart()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        //EventBus.getDefault().register(this);
    }

    override fun onDetach() {
        super.onDetach()
        //EventBus.getDefault().unregister(this);
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.fragment_statistics, container, false)
        //ButterKnife.bind(this, mView)
        return mView
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}