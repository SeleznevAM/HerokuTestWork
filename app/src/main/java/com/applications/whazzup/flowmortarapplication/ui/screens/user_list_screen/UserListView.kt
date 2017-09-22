package com.applications.whazzup.flowmortarapplication.ui.screens.user_list_screen

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import butterknife.BindView
import butterknife.OnClick
import com.applications.whazzup.flowmortarapplication.R
import com.applications.whazzup.flowmortarapplication.di.DaggerService
import com.applications.whazzup.flowmortarapplication.mvp.views.AbstractView
import com.applications.whazzup.flowmortarapplication.ui.screens.create_update_user.CreateUpdateUserScreen
import flow.Flow

class UserListView(context : Context, attrs : AttributeSet) : AbstractView<UserListScreen.UserListPresenter>(context, attrs) {

    @BindView(R.id.user_list_recycler) lateinit var userListRecycler : RecyclerView

    var userAdapter = UserListAdapter()


    override fun viewOnBackPressed(): Boolean {
        return  false
    }

    override fun initDagger(context: Context?) {
        DaggerService.getDaggerComponent<UserListScreen.UserListComponent>(context!!).inject(this)
    }

    fun initView() {
        with(userListRecycler){
            layoutManager = LinearLayoutManager(context)
            adapter = userAdapter
        }
    }

    //region===============================event==========================

    @OnClick(R.id.create_fab)
    fun fabClick(){
        Flow.get(context).set(CreateUpdateUserScreen())
    }

    //endregion

}