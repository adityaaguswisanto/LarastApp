package com.larast.larast.data.source

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.larast.larast.data.network.MyApi
import com.larast.larast.data.responses.family.Family
import javax.inject.Inject

class FamilyPaging @Inject constructor(
    private val api: MyApi,
    private val token: String
): PagingSource<Int, Family>() {

    override fun getRefreshKey(state: PagingState<Int, Family>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.plus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Family> {
        return try {
            val nextPageNumber = params.key ?: 1
            val response = api.family(token, nextPageNumber)
            val family = response.data
            LoadResult.Page(
                data = response.data,
                prevKey = if (nextPageNumber == 1) null else nextPageNumber - 1,
                nextKey = if (family.isEmpty()) null else nextPageNumber + 1,
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}