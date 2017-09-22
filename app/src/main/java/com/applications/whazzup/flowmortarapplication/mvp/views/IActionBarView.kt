package com.applications.whazzup.flowmortarapplication.mvp.views

/**
 * Created by ЗавТер on 22.09.2017.
 */
interface IActionBarView {
    fun setActionBarTitle(title: CharSequence?)
    fun setActionBarVisible(visible: Boolean)
    fun setBackArrow(enable: Boolean)
}