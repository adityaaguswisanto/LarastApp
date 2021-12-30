package com.larast.larast.data.source

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.larast.larast.data.network.MyApi
import com.larast.larast.data.responses.notification.Notification
import javax.inject.Inject

class NotificationPaging @Inject constructor(
    private val api: MyApi,
    private val token: String
): PagingSource<Int, Notification>() {

    override fun getRefreshKey(state: PagingState<Int, Notification>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.plus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Notification> {
        return try {
            val nextPageNumber = params.key ?: 1
            val response = api.notification(token, nextPageNumber)
            val notification = response.data
            LoadResult.Page(
                data = response.data,
                prevKey = if (nextPageNumber == 1) null else nextPageNumber - 1,
                nextKey = if (notification.isEmpty()) null else nextPageNumber + 1,
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}