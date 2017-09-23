package com.applications.whazzup.flowmortarapplication.mvp.models

import android.net.Uri
import com.applications.whazzup.flowmortarapplication.data.network.req.UserReq
import com.applications.whazzup.flowmortarapplication.data.network.res.UserAvatarRes
import com.applications.whazzup.flowmortarapplication.data.network.res.UserRes
import io.reactivex.Completable
import io.reactivex.Single
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class CreateUpdateUserModel : AbstractModel() {

    fun uploadPhoto(avatar: Uri?): Single<UserAvatarRes> {
        var file = File(avatar?.path)
        var sendFile : RequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file)
        var body : MultipartBody.Part = MultipartBody.Part.createFormData("image", file.name, sendFile)
        return dataManager.uploadPhoto(body)
    }

    fun createUser(userReq: UserReq): Single<UserRes> {
        return dataManager.createUSer(userReq)
    }

    fun updateUser(id: Int, userReq: UserReq): Single<UserRes> {
        return dataManager.updateUser(id, userReq)
    }
}