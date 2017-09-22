package com.applications.whazzup.flowmortarapplication.ui.screens.user_list_screen

import com.applications.whazzup.flowmortarapplication.R
import com.applications.whazzup.flowmortarapplication.data.storage.dto.UserDto
import com.applications.whazzup.flowmortarapplication.di.DaggerScope
import com.applications.whazzup.flowmortarapplication.di.DaggerService
import com.applications.whazzup.flowmortarapplication.flow.AbstractScreen
import com.applications.whazzup.flowmortarapplication.flow.Screen
import com.applications.whazzup.flowmortarapplication.mvp.models.UserListModel
import com.applications.whazzup.flowmortarapplication.mvp.presenters.AbstractPresenter
import com.applications.whazzup.flowmortarapplication.mvp.presenters.RootPresenter
import com.applications.whazzup.flowmortarapplication.ui.activities.RootActivity
import dagger.Provides
import io.reactivex.rxkotlin.subscribeBy
import mortar.MortarScope

@Screen(R.layout.screen_user_list)
class UserListScreen : AbstractScreen<RootActivity.RootComponent>() {
    override fun createScreenComponent(parentComponent: RootActivity.RootComponent): Any {
        return DaggerUserListScreen_UserListComponent.builder()
                .rootComponent(parentComponent)
                .userListModule(UserListModule())
                .build()
    }

    //region===============================Presenter==========================

    inner class UserListPresenter : AbstractPresenter<UserListView, UserListModel>(){

        override fun initDagger(scope: MortarScope?) {
            scope?.getService<UserListComponent>(DaggerService.SERVICE_NAME)?.inject(this)
        }

        override fun initToolbar() {
            mRootPresenter.newActionBarBuilder().setVisible(true).setTitle("User List").build()
        }

        override fun onEnterScope(scope: MortarScope?) {
            super.onEnterScope(scope)
            mModel.getUserFromRealm().subscribeBy(onNext = {
                view.userAdapter.addUser(UserDto(it))
            }, onComplete = {
                view.initView()
            })
        }

    }

    //endregion

    //region===============================DI==========================

    @dagger.Module
    inner class UserListModule{
        @Provides
        @DaggerScope(UserListPresenter :: class)
        internal fun providePresenter() : UserListPresenter{
            return UserListPresenter()
        }

        @Provides
        @DaggerScope(UserListPresenter :: class)
        internal fun provideModel() : UserListModel{
            return UserListModel()
        }
    }

    @dagger.Component(dependencies = arrayOf(RootActivity.RootComponent :: class), modules = arrayOf(UserListModule :: class))
    @DaggerScope(UserListPresenter :: class)
    interface UserListComponent{
        fun inject(presenter : UserListPresenter)
        fun inject(view : UserListView)
        fun inject(adapter: UserListAdapter)
        val rootPresenter: RootPresenter
    }

    //endregion


}