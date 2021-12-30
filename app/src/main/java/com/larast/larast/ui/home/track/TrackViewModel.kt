package com.larast.larast.ui.home.track

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
import com.larast.larast.data.repository.LetterRepository
import com.larast.larast.data.responses.letter.LetterResponse
import com.larast.larast.data.responses.track.Track
import com.larast.larast.data.source.TrackPaging
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TrackViewModel @Inject constructor(
    private val api: MyApi,
    private val repository: LetterRepository,
): ViewModel(){

    private val _stateResponse: MutableLiveData<Resource<LetterResponse>> = MutableLiveData()

    val stateResponse: LiveData<Resource<LetterResponse>>
        get() = _stateResponse

    fun state(token: String, id: Long) = viewModelScope.launch {
        _stateResponse.value = Resource.Loading
        _stateResponse.value = repository.state(token,id)
        _stateResponse.value = Resource.Dismiss
    }

    fun track(token: String): Flow<PagingData<Track>> {
        return Pager(PagingConfig(50)) {
            TrackPaging(api, token)
        }.flow.cachedIn(viewModelScope)
    }

    fun user() = repository.user()

}