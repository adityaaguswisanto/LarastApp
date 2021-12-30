package com.larast.larast.data.repository

import com.larast.larast.data.network.BaseRepository
import com.larast.larast.data.network.MyApi
import com.larast.larast.data.store.UserStore
import okhttp3.MultipartBody
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val api: MyApi,
    private val userStore: UserStore
) : BaseRepository() {

    suspend fun login(
        email: String,
        password: String,
        fcm: String,
    ) = safeApiCall {
        api.login(email, password, fcm)
    }

    suspend fun user(
        token: String,
    ) = safeApiCall {
        api.user(token)
    }

    suspend fun profile(
        token: String,
        foto: MultipartBody.Part
    ) = safeApiCall {
        api.profile(token, foto)
    }

    suspend fun logout(
        token: String,
    ) = safeApiCall {
        api.logout(token)
    }

    suspend fun forgot(email: String) = safeApiCall {
        api.forgot(email)
    }

    suspend fun update(
        token: String,
        nama: String,
        email: String,
        hp: String,
    ) = safeApiCall {
        api.update(token, nama, email, hp)
    }

    suspend fun saveToken(
        token: String
    ) = userStore.saveToken(
        token
    )

    fun user() = userStore.authToken

    suspend fun clear() = userStore.clear()

}