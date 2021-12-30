package com.larast.larast.ui.home.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.larast.larast.data.network.MyApi
import com.larast.larast.data.network.Resource
import com.larast.larast.data.repository.NotificationRepository
import com.larast.larast.data.responses.home.Event
import com.larast.larast.data.responses.home.News
import com.larast.larast.data.responses.notification.NotificationResponse
import com.larast.larast.data.source.EventPaging
import com.larast.larast.data.source.NewsPaging
import com.larast.larast.data.store.UserStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val api: MyApi,
    private val repository: NotificationRepository,
    private val userStore: UserStore
) : ViewModel() {

    private val _notificationResponse: MutableLiveData<Resource<NotificationResponse>> = MutableLiveData()

    val notificationResponse: LiveData<Resource<NotificationResponse>>
        get() = _notificationResponse

    fun news(token: String): Flow<PagingData<News>> {
        return Pager(PagingConfig(50)) {
            NewsPaging(api, token)
        }.flow.cachedIn(viewModelScope)
    }

    fun event(token: String): Flow<PagingData<Event>> {
        return Pager(PagingConfig(50)) {
            EventPaging(api, token)
        }.flow.cachedIn(viewModelScope)
    }

    fun notification(token: String, page: Int) = viewModelScope.launch {
        _notificationResponse.value = Resource.Loading
        _notificationResponse.value = repository.notification(token, page)
        _notificationResponse.value = Resource.Dismiss
    }

    fun user() = userStore.authToken

}
