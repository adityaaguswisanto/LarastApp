package com.larast.larast.ui.home.family.files

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.larast.larast.data.network.Resource
import com.larast.larast.data.repository.FilesRepository
import com.larast.larast.data.responses.files.FileResponse
import com.larast.larast.data.responses.files.FilesResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import javax.inject.Inject

@HiltViewModel
class FilesViewModel @Inject constructor(
    private val repository: FilesRepository
) : ViewModel() {

    private val _filesResponse: MutableLiveData<Resource<FilesResponse>> = MutableLiveData()

    val filesResponse: LiveData<Resource<FilesResponse>>
        get() = _filesResponse

    private val _fileResponse: MutableLiveData<Resource<FileResponse>> = MutableLiveData()

    val fileResponse: LiveData<Resource<FileResponse>>
        get() = _fileResponse

    fun files(token: String, id: Int) = viewModelScope.launch {
        _filesResponse.value = Resource.Loading
        _filesResponse.value = repository.files(token, id)
        _filesResponse.value = Resource.Dismiss
    }

    fun kk(token: String, id: Int, kk: MultipartBody.Part) = viewModelScope.launch {
        _fileResponse.value = Resource.Loading
        _fileResponse.value = repository.kk(token, id, kk)
        _fileResponse.value = Resource.Dismiss
    }

    fun akte(token: String, id: Int, akte: MultipartBody.Part) = viewModelScope.launch {
        _fileResponse.value = Resource.Loading
        _fileResponse.value = repository.akte(token, id, akte)
        _fileResponse.value = Resource.Dismiss
    }

    fun ktps(token: String, id: Int, ktps: MultipartBody.Part) = viewModelScope.launch {
        _fileResponse.value = Resource.Loading
        _fileResponse.value = repository.ktps(token, id, ktps)
        _fileResponse.value = Resource.Dismiss
    }

    fun ktpi(token: String, id: Int, ktpi: MultipartBody.Part) = viewModelScope.launch {
        _fileResponse.value = Resource.Loading
        _fileResponse.value = repository.ktpi(token, id, ktpi)
        _fileResponse.value = Resource.Dismiss
    }

    fun nikah(token: String, id: Int, nikah: MultipartBody.Part) = viewModelScope.launch {
        _fileResponse.value = Resource.Loading
        _fileResponse.value = repository.nikah(token, id, nikah)
        _fileResponse.value = Resource.Dismiss
    }

    fun ijazah(token: String, id: Int, ijazah: MultipartBody.Part) = viewModelScope.launch {
        _fileResponse.value = Resource.Loading
        _fileResponse.value = repository.ijazah(token, id, ijazah)
        _fileResponse.value = Resource.Dismiss
    }

    fun skl(token: String, id: Int, skl: MultipartBody.Part) = viewModelScope.launch {
        _fileResponse.value = Resource.Loading
        _fileResponse.value = repository.skl(token, id, skl)
        _fileResponse.value = Resource.Dismiss
    }

    fun agama(token: String, id: Int, agama: MultipartBody.Part) = viewModelScope.launch {
        _fileResponse.value = Resource.Loading
        _fileResponse.value = repository.agama(token, id, agama)
        _fileResponse.value = Resource.Dismiss
    }

    fun user() = repository.user()

}