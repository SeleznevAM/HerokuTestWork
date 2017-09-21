package com.applications.whazzup.flowmortarapplication.ui.activities

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import butterknife.BindView
import butterknife.ButterKnife

import com.applications.whazzup.flowmortarapplication.R
import com.applications.whazzup.flowmortarapplication.di.DaggerScope
import com.applications.whazzup.flowmortarapplication.di.components.AppComponent
import com.applications.whazzup.flowmortarapplication.di.modules.PicassoCacheModule
import com.applications.whazzup.flowmortarapplication.di.modules.RootModule
import com.applications.whazzup.flowmortarapplication.flow.TreeKeyDispetcher
import com.applications.whazzup.flowmortarapplication.mvp.presenters.RootPresenter
import com.applications.whazzup.flowmortarapplication.mvp.views.IRootView
import com.applications.whazzup.flowmortarapplication.mvp.views.IView
import com.applications.whazzup.flowmortarapplication.ui.screens.splash_screen.SplashScreen
import com.squareup.picasso.Picasso
import flow.Flow
import mortar.MortarScope
import mortar.bundler.BundleServiceRunner

class RootActivity : AppCompatActivity(), IRootView {

    @BindView(R.id.root_frame)
    lateinit var mRootFrame : FrameLayout

    override fun attachBaseContext(newBase: Context) {
        var newBase = Flow.configure(newBase, this)
                .defaultKey(SplashScreen())
                .dispatcher(TreeKeyDispetcher(this))
                .install()
        super.attachBaseContext(newBase)
    }

    override fun getSystemService(name: String): Any {
        val rootActivityScope = MortarScope.findChild(applicationContext, RootActivity::class.java.name)
        return if (rootActivityScope.hasService(name)) rootActivityScope.getService<Any>(name) else super.getSystemService(name)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        BundleServiceRunner.getBundleServiceRunner(this).onSaveInstanceState(outState)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_root)
        ButterKnife.bind(this)
    }

    // region================DI==============
    @dagger.Component(dependencies = arrayOf(AppComponent::class), modules = arrayOf(RootModule::class, PicassoCacheModule::class))
    @DaggerScope(RootActivity::class)
    interface RootComponent {
        fun inject(rootActivity: RootActivity)
        fun inject(rootPresenter: RootPresenter)
        val rootPresenter: RootPresenter
        val picasso: Picasso
    }
    // endregion

    // region================IRootView==============
    override fun showMessage(message: String) {
    }

    override fun showError(e: Throwable) {
    }

    override fun showLoad() {
    }

    override fun hideLoad() {
     }

    override fun hideBottomNavigation(isVisible: Boolean) {
    }

    override fun showtoolBar() {
    }

    override fun hideToolBar() {
    }

    override val currentScreen: IView?
        get() = mRootFrame.getChildAt(0) as IView

    override fun startAct(imageIntent: Intent) {
    }
    // endregion
}
