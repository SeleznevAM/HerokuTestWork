package com.applications.whazzup.flowmortarapplication.ui.screens.user_list_screen

import android.support.v7.view.menu.MenuView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.applications.whazzup.flowmortarapplication.R
import com.applications.whazzup.flowmortarapplication.data.storage.dto.UserDto
import com.applications.whazzup.flowmortarapplication.di.DaggerService
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import javax.inject.Inject

class UserListAdapter : RecyclerView.Adapter<UserListAdapter.UserListViewHolder>() {

    @Inject lateinit var picasso : Picasso

    var userList = mutableListOf<UserDto>()

    var listener: ((UserDto) -> Unit)? = null

    fun addListener(onItemClick: (UserDto) -> Unit) {
        this.listener = onItemClick
    }

    fun addUser(user : UserDto){
        userList.add(user)
        notifyDataSetChanged()
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView?) {
        super.onAttachedToRecyclerView(recyclerView)
        DaggerService.getDaggerComponent<UserListScreen.UserListComponent>(recyclerView?.context!!).inject(this)
    }

    override fun onBindViewHolder(holder: UserListViewHolder?, position: Int) {
        val user  = userList[position]
        var path = user.avatarUrl
        if(path !=null && !path.isEmpty()) picasso.load(path).into(holder?.userAvatar)
        else picasso.load("http://img-fotki.yandex.ru/get/6834/16969765.237/0_90e7b_ab190757_orig.png").into(holder?.userAvatar)
        holder?.userName?.text = user.firstName + " " + user.lastName
        holder?.userMail?.text = user.email


    }

    override fun getItemCount(): Int {
        return userList.size

    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): UserListViewHolder {
        val inflater = LayoutInflater.from(parent?.context)
        return UserListViewHolder(inflater.inflate(R.layout.item_user_list, parent, false), listener)
    }


    inner class UserListViewHolder(itemView : View, onItemClick: ((UserDto) -> Unit)?) : RecyclerView.ViewHolder(itemView), View.OnClickListener{

        override fun onClick(v: View?) {
            listener?.invoke(userList[adapterPosition])
        }

        @BindView(R.id.user_avatar) lateinit var userAvatar : CircleImageView
        @BindView(R.id.user_name) lateinit var userName : TextView
        @BindView(R.id.user_mail) lateinit var userMail : TextView
        @BindView(R.id.item_container) lateinit var  conteiner : LinearLayout
        var listener: ((UserDto) -> Unit)? = onItemClick

        init {
            ButterKnife.bind(this, itemView)
            conteiner.setOnClickListener(this)
        }

    }
}