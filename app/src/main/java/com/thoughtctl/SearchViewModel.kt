package com.thoughtctl

import android.util.MutableBoolean
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thoughtctl.api.ApiResponseGeneric
import com.thoughtctl.api.NetworkInterface
import com.thoughtctl.model.SearchRequestModel
import kotlinx.coroutines.launch
import javax.inject.Inject

class SearchViewModel @Inject constructor(private val networkInterface: NetworkInterface) :
    ViewModel() {

    val imagesLiveData = MutableLiveData<ApiResponseGeneric<Any>>()

    fun searchImages(request: SearchRequestModel) {
        imagesLiveData.value = ApiResponseGeneric.loading()
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
                    imagesLiveData.value = ApiResponseGeneric.success(response)
                } else {
                    imagesLiveData.value =
                        ApiResponseGeneric.error(Throwable("Failed to load images"))
                }

            }.onFailure {
                imagesLiveData.value = ApiResponseGeneric.error(it)
            }

        }
    }
}