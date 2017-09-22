package com.applications.whazzup.flowmortarapplication.ui.screens.create_update_user

import android.content.Context
import android.util.AttributeSet
import butterknife.OnClick
import com.applications.whazzup.flowmortarapplication.R
import com.applications.whazzup.flowmortarapplication.di.DaggerService
import com.applications.whazzup.flowmortarapplication.mvp.views.AbstractView
import java.io.IOException

class CreateUpdateUserView(context: Context, attributeSet: AttributeSet) : AbstractView<CreateUpdateUserScreen.CreateUpdateUserPresenter>(context, attributeSet) {


    override fun viewOnBackPressed(): Boolean {
        return false
    }

    override fun initDagger(context: Context?) {
        DaggerService.getDaggerComponent<CreateUpdateUserScreen.Component>(context!!).inject(this)

    }

    private fun showSourceDialog() {
        val source = arrayOf("Загрузить из галереи", "Сделать снимок", "Отмена")
        val alertDialog = android.support.v7.app.AlertDialog.Builder(context)
        alertDialog.setTitle("Установить фото")
        alertDialog.setItems(source) { dialogInterface, i ->
            when (i) {
                0 -> mPresenter.chooseGallery()
                1 -> try {
                    mPresenter.chooseCamera()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

                2 -> dialogInterface.cancel()
            }
        }
        alertDialog.show()
    }

    //region===============================Events==========================

    @OnClick(R.id.user_avatar_iv)
    fun clickOnUserAvatar(){
        showSourceDialog()
    }



    //endregion

}