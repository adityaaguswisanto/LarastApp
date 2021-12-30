package com.larast.larast.ui.home.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.larast.larast.data.network.Resource
import com.larast.larast.data.repository.AuthRepository
import com.larast.larast.data.responses.auth.LogoutResponse
import com.larast.larast.data.responses.auth.ProfileResponse
import com.larast.larast.data.responses.auth.UserResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    private val _userResponse: MutableLiveData<Resource<UserResponse>> = MutableLiveData()

    val userResponse: LiveData<Resource<UserResponse>>
        get() = _userResponse

    private val _profileResponse: MutableLiveData<Resource<ProfileResponse>> = MutableLiveData()

    val profileResponse: LiveData<Resource<ProfileResponse>>
        get() = _profileResponse

    private val _logoutResponse: MutableLiveData<Resource<LogoutResponse>> = MutableLiveData()

    val logoutResponse: LiveData<Resource<LogoutResponse>>
        get() = _logoutResponse

    fun user(token: String) = viewModelScope.launch {
        _userResponse.value = Resource.Loading
        _userResponse.value = repository.user(token)
        _userResponse.value = Resource.Dismiss
    }

    fun profile(
        token: String,
        foto: MultipartBody.Part
    ) = viewModelScope.launch {
        _profileResponse.value = Resource.Loading
        _profileResponse.value = repository.profile(token, foto)
        _profileResponse.value = Resource.Dismiss
    }

    fun logout(
        token: String
    ) = viewModelScope.launch {
        _logoutResponse.value = Resource.Loading
        _logoutResponse.value = repository.logout(token)
        _logoutResponse.value = Resource.Dismiss
    }

    fun user() = repository.user()

    suspend fun clear() = repository.clear()

}