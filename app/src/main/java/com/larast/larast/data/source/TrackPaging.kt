package com.larast.larast.data.source

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.larast.larast.data.network.MyApi
import com.larast.larast.data.responses.track.Track
import javax.inject.Inject

class TrackPaging @Inject constructor(
    private val api: MyApi,
    private val token: String
): PagingSource<Int, Track>() {

    override fun getRefreshKey(state: PagingState<Int, Track>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.plus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Track> {
        return try {
            val nextPageNumber = params.key ?: 1
            val response = api.track(token, nextPageNumber)
            val track = response.data
            LoadResult.Page(
                data = response.data,
                prevKey = if (nextPageNumber == 1) null else nextPageNumber - 1,
                nextKey = if (track.isEmpty()) null else nextPageNumber + 1,
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}