package com.applications.whazzup.flowmortarapplication.ui.screens.create_update_user

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.content.FileProvider
import android.text.Editable
import android.util.Log
import com.applications.whazzup.flowmortarapplication.R
import com.applications.whazzup.flowmortarapplication.data.network.req.UserReq
import com.applications.whazzup.flowmortarapplication.data.storage.dto.ActivityResultDto
import com.applications.whazzup.flowmortarapplication.data.storage.dto.UserDto
import com.applications.whazzup.flowmortarapplication.di.DaggerScope
import com.applications.whazzup.flowmortarapplication.di.DaggerService
import com.applications.whazzup.flowmortarapplication.flow.AbstractScreen
import com.applications.whazzup.flowmortarapplication.flow.Screen
import com.applications.whazzup.flowmortarapplication.mvp.models.CreateUpdateUserModel
import com.applications.whazzup.flowmortarapplication.mvp.presenters.AbstractPresenter
import com.applications.whazzup.flowmortarapplication.ui.activities.RootActivity
import com.applications.whazzup.flowmortarapplication.ui.screens.user_list_screen.UserListScreen
import com.applications.whazzup.flowmortarapplication.util.ConstantManager
import com.applications.whazzup.flowmortarapplication.util.UriGetter
import dagger.Provides
import flow.Flow
import flow.TreeKey
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import mortar.MortarScope
import java.io.File
import java.io.IOException
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

@Screen(R.layout.screen_creat_update_user)
class CreateUpdateUserScreen() : AbstractScreen<UserListScreen.UserListComponent>(),TreeKey {
    val CREATE_MODE = 0
    val EDIT_MODE = 1

    var editMode = -1
    lateinit var userInfo : UserDto

    lateinit var mPhotoFile: File

    var userAvatarUrl = ""

    constructor(mode : Int) : this() {
        this.editMode = mode

    }

    constructor(mode: Int, user : UserDto) : this() {
        this.editMode = mode
        userInfo = user
        if(user.avatarUrl != null && !user.avatarUrl.isEmpty()){
        userAvatarUrl = user.avatarUrl
        }else{
            userAvatarUrl = "http://img-fotki.yandex.ru/get/6834/16969765.237/0_90e7b_ab190757_orig.png"
        }
    }

    override fun createScreenComponent(parentComponent: UserListScreen.UserListComponent): Any {
        return DaggerCreateUpdateUserScreen_Component.builder().userListComponent(parentComponent).module(Module()).build()
    }

    override fun getParentKey(): Any {
        return UserListScreen()
    }

    //region===============================Presenter==========================

    inner class CreateUpdateUserPresenter : AbstractPresenter<CreateUpdateUserView, CreateUpdateUserModel>(){

        lateinit var mActivityResultSub: Disposable



        override fun initDagger(scope: MortarScope?) {
            scope?.getService<Component>(DaggerService.SERVICE_NAME)?.inject(this)
        }

        override fun initToolbar() {
            if(editMode==0) mRootPresenter.newActionBarBuilder().setTitle(view.context.getString(R.string.creat_user_toolbar_hint)).setBackArrow(true).build()
            if(editMode==1) mRootPresenter.newActionBarBuilder().setTitle(view.context.getString(R.string.update_user_toolbar_hint)).setBackArrow(true).build()
        }

        override fun onEnterScope(scope: MortarScope?) {
            super.onEnterScope(scope)
            subscribeOnActivityResult()

        }

        override fun onLoad(savedInstanceState: Bundle?) {
            super.onLoad(savedInstanceState)
            if(editMode ==0){
            view.initView(editMode)
            }else{
                view.initView(editMode, userInfo)
            }
        }

        fun chooseGallery() {
            if (rootView != null) {
                val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                if (mRootPresenter.checkPermissionAndRequestIfNotGranted(permissions, ConstantManager.REQUEST_PERMISSION_READ_EXTERNAL_STORAGE)) {
                    takePhotoFromGallery()
                }
            }

        }

        fun takePhotoFromGallery(){
            var intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_OPEN_DOCUMENT
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            (mRootPresenter.rootView as RootActivity).startActivityForResult(intent, ConstantManager.REQUEST_PROFILE_PHOTO)
        }

        fun subscribeOnActivityResult() {
            val activityResultObs = mRootPresenter.mActivityResultObs.filter({ activityResultDto -> activityResultDto.resultCode === Activity.RESULT_OK })
            mActivityResultSub = activityResultObs.subscribeBy(onNext = {
                handleActivityResult(it)
            }, onError = {
                mRootPresenter.rootView?.showError(it)
            })
        }
        fun handleActivityResult(activityResultDto: ActivityResultDto) {
            when (activityResultDto.requestCode) {
                ConstantManager.REQUEST_PROFILE_PHOTO -> {
                    if (activityResultDto.data != null) {
                        var photoUrl = UriGetter.getPath(view.context, activityResultDto.data.data)
                        Log.e("AVATAR", photoUrl.toString())

                        mModel.uploadPhoto(Uri.parse(photoUrl)).subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribeBy(onSuccess = {
                                    userAvatarUrl = it.image
                                    view.updateUserAvatar(Uri.parse(it.image))
                                }, onError = {
                                    Log.e("UPLOAD_PHOTO_FROM_CAM", it.stackTrace.toString())
                                    mRootPresenter.rootView?.showMessage(view.context.getString(R.string.photo_cant_download_hint))
                                }
                                )
                    }
                }
                ConstantManager.REQUEST_PROFILE_PHOTO_CAMERA -> {
                    Log.e("AVATAR", Uri.fromFile(mPhotoFile).toString())
                    mModel.uploadPhoto(Uri.fromFile(mPhotoFile)).subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeBy(onSuccess = {
                                userAvatarUrl = it.image
                                view.updateUserAvatar(Uri.parse(it.image))
                            }, onError = {
                                Log.e("UPLOAD_PHOTO_FROM_CAM", it.stackTrace.toString())
                                mRootPresenter.rootView?.showMessage(view.context.getString(R.string.photo_cant_download_hint))
                            })
                }
            }
        }

        fun chooseCamera() {
            if (rootView != null) {
                val permissions = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                if (mRootPresenter.checkPermissionAndRequestIfNotGranted(permissions, ConstantManager.REQUEST_PERMISSON_CAMERA)) {
                    mPhotoFile = createImageFile()
                    if (mPhotoFile == null) {
                        rootView?.showMessage(view.context.getString(R.string.file_cant_create_hint))
                        return
                    }
                    takePhotoFromCamera()
                }
            }

        }

        fun takePhotoFromCamera(){
            var uriForFile = FileProvider.getUriForFile((rootView as RootActivity), ConstantManager.FILE_PROVIDER_AUTHORITY, mPhotoFile)
            var intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uriForFile)
            (mRootPresenter.rootView as RootActivity).startActivityForResult(intent, ConstantManager.REQUEST_PROFILE_PHOTO_CAMERA)
        }

        @Throws(IOException::class)
        private fun createImageFile(): File {
            val dateTimeInstance = SimpleDateFormat.getTimeInstance(DateFormat.MEDIUM)
            val timeStamp = dateTimeInstance.format(Date())
            val imageFileName = "IMG_" + timeStamp
            val storageDir = view.context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            val imageFile = File.createTempFile(imageFileName, ".jpg", storageDir)
            return imageFile
        }

        fun createNewUser(firstName: Editable, lastName: Editable, mail: Editable) {
            mModel.createUser(UserReq(firstName.toString(), lastName.toString(), mail.toString(), userAvatarUrl))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeBy (
                            onSuccess = {
                        mModel.realmManager.saveUserToRealm(it)
                        Flow.get(view).goBack()
                    }, onError = {
                        mRootPresenter.rootView?.showError(it)
                    })
        }

        fun updateUser(firstName: Editable?, lastName: Editable?, mail: Editable?) {
            mModel.updateUser(userInfo.id, UserReq(firstName.toString(), lastName.toString(), mail.toString(), userAvatarUrl))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeBy(onSuccess = {
                        mModel.realmManager.saveUserToRealm(it)
                        Flow.get(view).goBack()
                    }, onError = {
                        mRootPresenter.rootView?.showError(it)
                    })

        }

    }

    //endregion

    //region===============================DI==========================

    @dagger.Module
    inner class Module{
        @Provides
        @DaggerScope(CreateUpdateUserPresenter :: class)
        internal fun providePresenter() : CreateUpdateUserPresenter{
            return CreateUpdateUserPresenter()
        }

        @Provides
        @DaggerScope(CreateUpdateUserPresenter :: class)
        internal fun provideModel() : CreateUpdateUserModel{
            return CreateUpdateUserModel()
        }
    }

    @dagger.Component(dependencies = arrayOf(UserListScreen.UserListComponent :: class), modules = arrayOf(Module :: class))
    @DaggerScope(CreateUpdateUserPresenter :: class)
    interface Component{
        fun inject(presenter : CreateUpdateUserPresenter)
        fun inject(view : CreateUpdateUserView)
    }

    //endregion


}