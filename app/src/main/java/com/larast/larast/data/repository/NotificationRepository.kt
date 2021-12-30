package com.larast.larast.data.repository

import com.larast.larast.data.network.BaseRepository
import com.larast.larast.data.network.MyApi
import javax.inject.Inject

class NotificationRepository @Inject constructor(
    private val api: MyApi
) : BaseRepository() {

    suspend fun notification(token: String, page: Int) = safeApiCall {
        api.notification(token, page)
    }

    suspend fun read(token: String) = safeApiCall {
        api.read(token)
    }

}