package com.larast.larast.ui.home.family

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.larast.larast.data.network.MyApi
import com.larast.larast.data.responses.family.Family
import com.larast.larast.data.source.FamilyPaging
import com.larast.larast.data.store.UserStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class FamilyViewModel @Inject constructor(
    private val api: MyApi,
    private val userStore: UserStore
): ViewModel(){

    fun family(token: String): Flow<PagingData<Family>> {
        return Pager(PagingConfig(50)) {
            FamilyPaging(api, token)
        }.flow.cachedIn(viewModelScope)
    }

    fun user() = userStore.authToken

}