package com.larast.larast.data.source

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.larast.larast.data.network.MyApi
import com.larast.larast.data.responses.home.News
import javax.inject.Inject

class NewsPaging @Inject constructor(
    private val api: MyApi,
    private val token: String
): PagingSource<Int, News>() {

    override fun getRefreshKey(state: PagingState<Int, News>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.plus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, News> {
        return try {
            val nextPageNumber = params.key ?: 1
            val response = api.news(token, nextPageNumber)
            val news = response.data
            LoadResult.Page(
                data = response.data,
                prevKey = if (nextPageNumber == 1) null else nextPageNumber - 1,
                nextKey = if (news.isEmpty()) null else nextPageNumber + 1,
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}