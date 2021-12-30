package com.larast.larast.ui.home.settings.account

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.larast.larast.data.network.Resource
import com.larast.larast.data.repository.AuthRepository
import com.larast.larast.data.responses.auth.ProfileResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    private val _userResponse: MutableLiveData<Resource<ProfileResponse>> = MutableLiveData()

    val userResponse: LiveData<Resource<ProfileResponse>>
        get() = _userResponse

    fun update(
        token: String,
        nama: String,
        email: String,
        hp: String,
    ) = viewModelScope.launch {
        _userResponse.value = Resource.Loading
        _userResponse.value = repository.update(token, nama, email, hp)
        _userResponse.value = Resource.Dismiss
    }

    fun user() = repository.user()

}