package com.thoughtctl

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thoughtctl.api.NetworkInterface
import com.thoughtctl.api.UiState
import com.thoughtctl.model.ImgurResponseModel
import com.thoughtctl.model.SearchRequestModel
import kotlinx.coroutines.launch
import javax.inject.Inject

class SearchViewModel @Inject constructor(private val networkInterface: NetworkInterface) :
    ViewModel() {


    val uiState = MutableLiveData<UiState<ImgurResponseModel>>()


    fun searchImages(request: SearchRequestModel) {
        uiState.value = UiState.Loading()
        val mapOf: HashMap<String, String> = HashMap()
        mapOf["q"] = request.q ?: ""
        viewModelScope.launch {
            kotlin.runCatching {
                networkInterface.callSearchImages(
                    "Client-ID ${Constants.IMGUR_CLIENT_ID}",
                    request.sort,
                    request.window,
                    request.q
                )
            }.onSuccess { result ->
                if (result.isSuccessful) {
                    val response = result.body()
                    uiState.value = UiState.Success(response)
                } else {
                    uiState.value = UiState.Error(Throwable("Failed to load images"))
                }

            }.onFailure {
                uiState.value = UiState.Error(it)
            }

        }
    }
}