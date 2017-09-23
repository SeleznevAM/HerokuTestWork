package com.applications.whazzup.flowmortarapplication.ui.screens.create_update_user

import android.content.Context
import android.net.Uri
import android.support.design.widget.TextInputLayout
import android.text.Editable
import android.util.AttributeSet
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import butterknife.BindView
import butterknife.OnClick
import butterknife.OnFocusChange
import butterknife.OnTextChanged
import com.applications.whazzup.flowmortarapplication.R
import com.applications.whazzup.flowmortarapplication.data.storage.dto.UserDto
import com.applications.whazzup.flowmortarapplication.di.DaggerService
import com.applications.whazzup.flowmortarapplication.mvp.views.AbstractView
import com.applications.whazzup.flowmortarapplication.util.Validator
import com.squareup.picasso.Picasso
import java.io.IOException

class CreateUpdateUserView(context: Context, attributeSet: AttributeSet) : AbstractView<CreateUpdateUserScreen.CreateUpdateUserPresenter>(context, attributeSet) {

    @BindView(R.id.user_avatar_iv) lateinit var userAvatar : ImageView
    @BindView(R.id.create_update_button) lateinit var createUpdateButton : Button
    @BindView(R.id.user_first_name_et) lateinit var userFirstName : EditText
    @BindView(R.id.first_name_til) lateinit var firstNameLayout : TextInputLayout
    @BindView(R.id.user_last_name_et) lateinit var userLastName : EditText
    @BindView(R.id.last_name_til) lateinit var  lastNameLayout : TextInputLayout
    @BindView(R.id.user_mail_et) lateinit var userMainl : EditText
    @BindView(R.id.mail_til) lateinit var mailLAyout : TextInputLayout

    var mode = -1

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        if(firstNameLayout.isFocusable && userFirstName.text.length<3){

        }
    }

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

    fun updateUserAvatar(parse: Uri?) {
        Picasso.with(context).load(parse).into(userAvatar)
    }

    fun initView(editMode: Int) {
        mode = editMode
        createUpdateButton.text = "Создать пользователя"
    }

    fun initView(editMode: Int, userInfo: UserDto) {
        mode = editMode
       createUpdateButton.text = "Редактировать пользователя"
        if(userInfo.avatarUrl !=null && !userInfo.avatarUrl.isEmpty()) Picasso.with(context).load(userInfo.avatarUrl).into(userAvatar)
        userFirstName.setText(userInfo.firstName)
        userLastName.setText(userInfo.lastName)
        userMainl.setText(userInfo.email)

    }


    //region===============================Events==========================

    @OnClick(R.id.user_avatar_iv)
    fun clickOnUserAvatar(){
        showSourceDialog()
    }

    @OnClick(R.id.create_update_button)
    fun buttonClick(){
            if (mode == 0) {
                mPresenter.createNewUser(userFirstName.text, userLastName.text, userMainl.text)
            }else{
                mPresenter.updateUser(userFirstName.text, userLastName.text, userMainl.text)
            }


    }

    @OnFocusChange(R.id.user_first_name_et)
    fun firstNameFocus(isFocus : Boolean){
        if(isFocus && userFirstName.text.length < 3) firstNameLayout.error = "Имя пользователя должно быть не меннее 3 символов без пробелов"
    }


    @OnTextChanged(R.id.user_first_name_et, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    fun firstNameTextChanged(edit : Editable){
        if(!Validator.isValidateLogin(edit)) firstNameLayout.error = "Имя пользователя должно быть не меннее 3 символов"
        else firstNameLayout.error = ""

    }
    @OnFocusChange(R.id.user_last_name_et)
    fun lastNameFocus(isFocus : Boolean){
        if(isFocus && userLastName.text.length < 3) lastNameLayout.error = "Фамилия пользователя должна быть не меннее 3 символов"
    }


    @OnTextChanged(R.id.user_last_name_et, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    fun lastNameTextChanged(edit : Editable){
        if(!Validator.isValidateLogin(edit)) lastNameLayout.error = "Фамилия пользователя должна быть не меннее 3 символов"
        else lastNameLayout.error = ""
    }

    @OnFocusChange(R.id.user_mail_et)
    fun mailFocus(isFocus : Boolean){
        if(isFocus && userMainl.text.isEmpty()) mailLAyout.error = "Введите адреса (anyMail@host.ru)"
    }

    @OnTextChanged(R.id.user_mail_et, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    fun mailTextChanged(s : Editable){
        if(!Validator.isValidateEmail(s)) mailLAyout.error = "Введите адреса (anyMail@host.ru)"
        else mailLAyout.error = ""
    }



    //endregion

}