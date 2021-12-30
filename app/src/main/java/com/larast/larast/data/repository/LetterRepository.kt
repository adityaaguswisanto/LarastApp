package com.larast.larast.data.repository

import com.larast.larast.data.network.BaseRepository
import com.larast.larast.data.network.MyApi
import com.larast.larast.data.store.UserStore
import retrofit2.http.Field
import retrofit2.http.Header
import javax.inject.Inject

class LetterRepository @Inject constructor(
    private val api: MyApi,
    private val userStore: UserStore
) : BaseRepository() {

    suspend fun citizens(
        token: String
    ) = safeApiCall {
        api.citizens(
            token
        )
    }

    suspend fun services(
        token: String
    ) = safeApiCall {
        api.services(
            token
        )
    }

    suspend fun letter(
        token: String,
        pemohon: Int,
        pelayanan: Int,
        hp: String,
        keterangan: String,
        berkas: String
    ) = safeApiCall {
        api.letter(
            token, pemohon, pelayanan, hp, keterangan, berkas
        )
    }

    suspend fun state(
        token: String,
        id: Long
    ) = safeApiCall {
        api.state(
            token, id
        )
    }

    fun user() = userStore.authToken

}