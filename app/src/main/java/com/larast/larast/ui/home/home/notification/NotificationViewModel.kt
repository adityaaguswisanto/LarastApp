package com.larast.larast.ui.home.home.notification

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
import com.larast.larast.data.responses.notification.Notification
import com.larast.larast.data.responses.notification.ReadResponse
import com.larast.larast.data.source.NotificationPaging
import com.larast.larast.data.store.UserStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val api: MyApi,
    private val repository: NotificationRepository,
    private val userStore: UserStore
) : ViewModel() {

    private val _notificationResponse: MutableLiveData<Resource<ReadResponse>> = MutableLiveData()

    val notificationResponse: LiveData<Resource<ReadResponse>>
        get() = _notificationResponse

    fun notification(token: String): Flow<PagingData<Notification>> {
        return Pager(PagingConfig(50)) {
            NotificationPaging(api, token)
        }.flow.cachedIn(viewModelScope)
    }

    fun read(token: String) = viewModelScope.launch {
        _notificationResponse.value = Resource.Loading
        _notificationResponse.value = repository.read(token)
        _notificationResponse.value = Resource.Dismiss
    }

    fun user() = userStore.authToken

}