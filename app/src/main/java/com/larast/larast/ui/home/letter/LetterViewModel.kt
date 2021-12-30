package com.larast.larast.ui.home.letter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.larast.larast.data.network.Resource
import com.larast.larast.data.repository.LetterRepository
import com.larast.larast.data.responses.letter.CitizensResponse
import com.larast.larast.data.responses.letter.LetterResponse
import com.larast.larast.data.responses.letter.ServiceResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LetterViewModel @Inject constructor(
    private val repository: LetterRepository
) : ViewModel() {

    private val _citizensResponse: MutableLiveData<Resource<CitizensResponse>> = MutableLiveData()

    val citizensResponse: LiveData<Resource<CitizensResponse>>
        get() = _citizensResponse

    private val _servicesResponse: MutableLiveData<Resource<ServiceResponse>> = MutableLiveData()

    val servicesResponse: LiveData<Resource<ServiceResponse>>
        get() = _servicesResponse

    private val _letterResponse: MutableLiveData<Resource<LetterResponse>> = MutableLiveData()

    val letterResponse: LiveData<Resource<LetterResponse>>
        get() = _letterResponse

    fun citizens(token: String) = viewModelScope.launch {
        _citizensResponse.value = Resource.Loading
        _citizensResponse.value = repository.citizens(token)
        _citizensResponse.value = Resource.Dismiss
    }

    fun services(token: String) = viewModelScope.launch {
        _servicesResponse.value = Resource.Loading
        _servicesResponse.value = repository.services(token)
        _servicesResponse.value = Resource.Dismiss
    }

    fun letter(
        token: String,
        pemohon: Int,
        pelayanan: Int,
        hp: String,
        keterangan: String,
        berkas: String
    ) = viewModelScope.launch {
        _letterResponse.value = Resource.Loading
        _letterResponse.value = repository.letter(token, pemohon, pelayanan, hp, keterangan, berkas)
        _letterResponse.value = Resource.Dismiss
    }

    fun user() = repository.user()

}