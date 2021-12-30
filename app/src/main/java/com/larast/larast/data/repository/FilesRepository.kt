package com.larast.larast.data.repository

import com.larast.larast.data.network.BaseRepository
import com.larast.larast.data.network.MyApi
import com.larast.larast.data.store.UserStore
import okhttp3.MultipartBody
import javax.inject.Inject

class FilesRepository @Inject constructor(
    private val api: MyApi,
    private val userStore: UserStore
) : BaseRepository() {

    suspend fun files(
        token: String,
        id: Int
    ) = safeApiCall {
        api.files(token, id)
    }

    suspend fun kk(token: String, id: Int, kk: MultipartBody.Part) = safeApiCall {
        api.kk(token, id, kk)
    }

    suspend fun akte(token: String, id: Int, akte: MultipartBody.Part) = safeApiCall {
        api.akte(token, id, akte)
    }

    suspend fun ktps(token: String, id: Int, ktps: MultipartBody.Part) = safeApiCall {
        api.ktps(token, id, ktps)
    }

    suspend fun ktpi(token: String, id: Int, ktpi: MultipartBody.Part) = safeApiCall {
        api.ktpi(token, id, ktpi)
    }

    suspend fun nikah(token: String, id: Int, nikah: MultipartBody.Part) = safeApiCall {
        api.nikah(token, id, nikah)
    }

    suspend fun ijazah(token: String, id: Int, ijazah: MultipartBody.Part) = safeApiCall {
        api.ijazah(token, id, ijazah)
    }

    suspend fun skl(token: String, id: Int, skl: MultipartBody.Part) = safeApiCall {
        api.skl(token, id, skl)
    }

    suspend fun agama(token: String, id: Int, agama: MultipartBody.Part) = safeApiCall {
        api.agama(token, id, agama)
    }

    fun user() = userStore.authToken

}