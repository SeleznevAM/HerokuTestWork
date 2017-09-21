package com.applications.whazzup.flowmortarapplication.mvp.presenters

import com.applications.whazzup.flowmortarapplication.App
import com.applications.whazzup.flowmortarapplication.mvp.models.RootModel
import com.applications.whazzup.flowmortarapplication.mvp.views.IRootView
import com.applications.whazzup.flowmortarapplication.ui.activities.RootActivity
import mortar.Presenter
import mortar.bundler.BundleService
import javax.inject.Inject


class RootPresenter : Presenter<IRootView>() {

    @Inject
    lateinit var mRootModel : RootModel

    val rootView: IRootView?
        get() = view

    init {
        App.rootComponent!!.inject(this)
    }


    companion object {
        val INSTANCE = RootPresenter()
    }

    override fun extractBundleService(view: IRootView): BundleService {
        return BundleService.getBundleService(view as RootActivity)
    }
}