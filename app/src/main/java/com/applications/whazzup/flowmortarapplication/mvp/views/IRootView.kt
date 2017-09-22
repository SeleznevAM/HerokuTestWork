package com.applications.whazzup.flowmortarapplication.mvp.views

import android.content.Intent


interface IRootView {
    fun showMessage(message: String)
    fun showError(e: Throwable)
    fun showLoad()
    fun hideLoad()
    fun hideBottomNavigation(isVisible: Boolean)
    fun showToolbar()
    fun hideToolbar()
    val currentScreen: IView?
    fun startAct(imageIntent: Intent)
}