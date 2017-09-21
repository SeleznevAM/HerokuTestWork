package com.applications.whazzup.flowmortarapplication.ui.screens.splash_screen

import android.content.Context
import android.util.AttributeSet
import com.applications.whazzup.flowmortarapplication.di.DaggerService
import com.applications.whazzup.flowmortarapplication.flow.AbstractScreen
import com.applications.whazzup.flowmortarapplication.mvp.views.AbstractView


class SplashView(context : Context, attrs : AttributeSet) : AbstractView<SplashScreen.SplashPresenter>(context, attrs) {

    override fun viewOnBackPressed(): Boolean {
        return false
    }

    override fun initDagger(context: Context?) {
        DaggerService.getDaggerComponent<SplashScreen.SplashComponent>(context!!).inject(this)
    }

}