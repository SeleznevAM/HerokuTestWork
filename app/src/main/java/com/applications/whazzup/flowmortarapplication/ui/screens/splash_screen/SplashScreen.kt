package com.applications.whazzup.flowmortarapplication.ui.screens.splash_screen

import com.applications.whazzup.flowmortarapplication.R
import com.applications.whazzup.flowmortarapplication.di.DaggerScope
import com.applications.whazzup.flowmortarapplication.di.DaggerService
import com.applications.whazzup.flowmortarapplication.flow.AbstractScreen
import com.applications.whazzup.flowmortarapplication.flow.Screen
import com.applications.whazzup.flowmortarapplication.mvp.models.SplashModel
import com.applications.whazzup.flowmortarapplication.mvp.presenters.AbstractPresenter
import com.applications.whazzup.flowmortarapplication.ui.activities.RootActivity
import com.applications.whazzup.flowmortarapplication.ui.screens.user_list_screen.UserListScreen
import dagger.Provides
import flow.Flow
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import mortar.MortarScope
import javax.inject.Inject

@Screen(R.layout.screen_splash)
class SplashScreen : AbstractScreen<RootActivity.RootComponent>() {

    @Inject
    lateinit var mModel : SplashModel

    override fun createScreenComponent(parentComponent: RootActivity.RootComponent): Any {
        return DaggerSplashScreen_SplashComponent.builder()
                .rootComponent(parentComponent)
                .splashModule(SplashModule())
                .build()
    }

    // region================Presenter==============

    inner class SplashPresenter : AbstractPresenter<SplashView, SplashModel>(){
        override fun initDagger(scope: MortarScope?) {
            scope?.getService<SplashComponent>(DaggerService.SERVICE_NAME)?.inject(this)
        }

        override fun initToolbar() {
            mRootPresenter.newActionBarBuilder().setVisible(false).build()
        }

        override fun onEnterScope(scope: MortarScope?) {
            super.onEnterScope(scope)
            mModel.getUserFromNetworkAndSaveInBd()?.subscribeOn(Schedulers.io())?.observeOn(AndroidSchedulers.mainThread())?.subscribeBy(onComplete = {Flow.get(view).set(UserListScreen())})
        }
    }

    // endregion

    // region================DI==============

    @dagger.Module
    inner class SplashModule{
        @Provides
        @DaggerScope(SplashPresenter :: class)
        internal fun providePresenter() : SplashPresenter{
            return SplashPresenter()
        }

        @Provides
        @DaggerScope(SplashPresenter :: class)
        internal fun provideModel() : SplashModel{
            return SplashModel()
        }
    }

    @dagger.Component(dependencies = arrayOf(RootActivity.RootComponent :: class), modules = arrayOf(SplashModule :: class))
    @DaggerScope(SplashPresenter :: class)
    interface SplashComponent{
        fun inject (presenter : SplashPresenter)
        fun inject (view : SplashView)
    }

    // endregion

}