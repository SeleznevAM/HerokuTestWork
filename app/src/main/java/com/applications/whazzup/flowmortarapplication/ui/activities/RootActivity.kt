package com.applications.whazzup.flowmortarapplication.ui.activities

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.widget.FrameLayout
import butterknife.BindView
import butterknife.ButterKnife

import com.applications.whazzup.flowmortarapplication.R
import com.applications.whazzup.flowmortarapplication.di.DaggerScope
import com.applications.whazzup.flowmortarapplication.di.DaggerService
import com.applications.whazzup.flowmortarapplication.di.components.AppComponent
import com.applications.whazzup.flowmortarapplication.di.modules.PicassoCacheModule
import com.applications.whazzup.flowmortarapplication.di.modules.RootModule
import com.applications.whazzup.flowmortarapplication.flow.TreeKeyDispetcher
import com.applications.whazzup.flowmortarapplication.mvp.presenters.RootPresenter
import com.applications.whazzup.flowmortarapplication.mvp.views.IActionBarView
import com.applications.whazzup.flowmortarapplication.mvp.views.IRootView
import com.applications.whazzup.flowmortarapplication.mvp.views.IView
import com.applications.whazzup.flowmortarapplication.ui.screens.splash_screen.SplashScreen
import com.squareup.picasso.Picasso
import flow.Flow
import mortar.MortarScope
import mortar.bundler.BundleServiceRunner
import javax.inject.Inject
import android.support.v4.view.ViewCompat.setFitsSystemWindows
import android.support.design.widget.CoordinatorLayout
import android.R.attr.data
import android.util.TypedValue



class RootActivity : AppCompatActivity(), IRootView, IActionBarView {


    @BindView(R.id.root_frame) lateinit var mRootFrame : FrameLayout

    @BindView(R.id.toolbar) lateinit var mToolbar: Toolbar

    @Inject
    lateinit var mRootPresenter : RootPresenter

    lateinit var mActionBar: android.support.v7.app.ActionBar


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
        initActionBar()
        BundleServiceRunner.getBundleServiceRunner(this).onCreate(savedInstanceState)
        (DaggerService.getDaggerComponent<Any>(this) as RootComponent).inject(this)
        mRootPresenter.takeView(this)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        mRootPresenter.onActivityResult(requestCode, resultCode, data)
    }


    private fun initActionBar() {
        setSupportActionBar(mToolbar)
        mActionBar = supportActionBar!!
    }

    override fun startActivityForResult(intent: Intent?, requestCode: Int) {
        super.startActivityForResult(intent, requestCode)
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

override fun showToolbar() {
        if (supportActionBar != null) {
            supportActionBar!!.show()
        }
        var actionBarHeight = 0
        val tv = TypedValue()
        if (theme.resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, resources.displayMetrics)
        }
        val layoutParams = mRootFrame.layoutParams as CoordinatorLayout.LayoutParams
        layoutParams.setMargins(0, actionBarHeight, 0, 0)
        mRootFrame.layoutParams = layoutParams
    }

    override fun hideToolbar() {
        if (supportActionBar != null) {
            supportActionBar!!.hide()
        }
        val layoutParams = mRootFrame.layoutParams as CoordinatorLayout.LayoutParams
        layoutParams.setMargins(0, 0, 0, 0)
        mRootFrame.layoutParams = layoutParams
        mRootFrame.fitsSystemWindows = true
    }

    override val currentScreen: IView?
        get() = mRootFrame.getChildAt(0) as IView

    override fun startAct(imageIntent: Intent) {
    }
    // endregion

    //region===============================IActionBarView==========================

    override fun setActionBarTitle(title: CharSequence?) {
        mActionBar.title = title
    }

    override fun setActionBarVisible(visible: Boolean) {
        if(visible) showToolbar()
        else hideToolbar()
    }

    override fun setBackArrow(enable: Boolean) {
        if (enable) {
            mActionBar.setDisplayHomeAsUpEnabled(true)
        } else {
            mActionBar.setDisplayHomeAsUpEnabled(false)
        }
     }

    //endregion

}
