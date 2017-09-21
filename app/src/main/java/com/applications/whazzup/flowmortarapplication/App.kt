package com.applications.whazzup.flowmortarapplication

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.applications.whazzup.flowmortarapplication.di.DaggerService
import com.applications.whazzup.flowmortarapplication.di.components.AppComponent
import com.applications.whazzup.flowmortarapplication.di.components.DaggerAppComponent
import com.applications.whazzup.flowmortarapplication.di.modules.AppModule
import com.applications.whazzup.flowmortarapplication.di.modules.PicassoCacheModule
import com.applications.whazzup.flowmortarapplication.di.modules.RootModule

import com.applications.whazzup.flowmortarapplication.mortar.ScreenScoper
import com.applications.whazzup.flowmortarapplication.ui.activities.DaggerRootActivity_RootComponent
import com.applications.whazzup.flowmortarapplication.ui.activities.RootActivity

import com.facebook.stetho.Stetho
import com.uphyca.stetho_realm.RealmInspectorModulesProvider
import io.realm.Realm
import mortar.MortarScope
import mortar.bundler.BundleServiceRunner


class App :Application() {
    private var mRootScope: MortarScope? = null
    private var mRootActivityScope: MortarScope? = null

    override fun getSystemService(name: String): Any {
        return if (mRootScope != null && mRootScope!!.hasService(name)) mRootScope!!.getService<Any>(name) else super.getSystemService(name)
    }

    init {
        instance = this
    }

    override fun onCreate() {
        super.onCreate()
        Realm.init(this)

        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(RealmInspectorModulesProvider.builder(this).build())
                        .build())

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        createDaggerAppComponent()
        createDaggerRootComponent()

        mRootScope = MortarScope.buildRootScope()
                .withService(DaggerService.SERVICE_NAME, appComponent!!)
                .build("Root")
        mRootActivityScope = mRootScope!!.buildChild()
                .withService(DaggerService.SERVICE_NAME, rootComponent!!)
                .withService(BundleServiceRunner.SERVICE_NAME, BundleServiceRunner())
                .build(RootActivity::class.java.name)

        ScreenScoper.registerScope(mRootScope)
        ScreenScoper.registerScope(mRootActivityScope)
    }

    private fun createDaggerRootComponent() {
       rootComponent = DaggerRootActivity_RootComponent.builder()
                .appComponent(appComponent)
                .rootModule(RootModule())
                .picassoCacheModule(PicassoCacheModule())
                .build()
    }

    private fun createDaggerAppComponent() {
        appComponent = DaggerAppComponent.builder().appModule(AppModule(applicationContext)).build()
    }

    companion object {

        private var instance : App? = null

        fun applicationContext() : Context {
            return instance!!.applicationContext
        }

        var appComponent: AppComponent? = null
            private set
        var rootComponent: RootActivity.RootComponent? = null
            private set
        var sharedPreferences: SharedPreferences? = null


    }
}