package com.larast.larast.data.helper

import android.view.View
import android.widget.AdapterView

abstract class Spinner : AdapterView.OnItemSelectedListener{

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        getData(p2)
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {

    }

    protected abstract fun getData(p2: Int)

}