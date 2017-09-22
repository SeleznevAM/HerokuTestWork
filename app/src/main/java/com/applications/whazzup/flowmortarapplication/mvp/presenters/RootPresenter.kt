package com.applications.whazzup.flowmortarapplication.mvp.presenters

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.support.v4.content.ContextCompat
import com.applications.whazzup.flowmortarapplication.App
import com.applications.whazzup.flowmortarapplication.data.storage.dto.ActivityResultDto
import com.applications.whazzup.flowmortarapplication.mvp.models.RootModel
import com.applications.whazzup.flowmortarapplication.mvp.views.IRootView
import com.applications.whazzup.flowmortarapplication.ui.activities.RootActivity
import io.reactivex.subjects.PublishSubject
import mortar.Presenter
import mortar.bundler.BundleService
import javax.inject.Inject


class RootPresenter : Presenter<IRootView>() {

    @Inject
    lateinit var mRootModel : RootModel

    val mActivityResultObs: PublishSubject<ActivityResultDto> = PublishSubject.create()

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

    fun checkPermissionAndRequestIfNotGranted(permissions: Array<String>, requestCode: Int): Boolean {
        var allGranted = true
        for (permission in permissions) {
            val selfPermission = ContextCompat.checkSelfPermission(view as RootActivity, permission)
            if (selfPermission != PackageManager.PERMISSION_GRANTED) {
                allGranted = false
                break
            }
        }
        if (!allGranted) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                (view as RootActivity).requestPermissions(permissions, requestCode)
            }
            return false
        }
        return allGranted
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        mActivityResultObs.onNext(ActivityResultDto(requestCode, resultCode, data))
    }

    //region===============================ActionBar Builder==========================

    fun newActionBarBuilder(): ActionBarBuilder {
        return ActionBarBuilder()
    }

    inner class ActionBarBuilder {
        var isGoBack: Boolean = false
        var isVisible: Boolean = true
        var title: CharSequence? = null

        fun setBackArrow(enabled: Boolean): ActionBarBuilder {
            this.isGoBack = enabled
            return this
        }

        fun setVisible(visible: Boolean): ActionBarBuilder {
            this.isVisible = visible
            return this
        }

        fun setTitle(title: CharSequence?): ActionBarBuilder {
            this.title = title
            return this
        }

        fun build() {
            if (view != null) {
                var activity = view as RootActivity
                activity.setBackArrow(isGoBack)
                activity.setActionBarTitle(title)
                activity.setActionBarVisible(isVisible)
            }
        }
    }



    //endregion

}