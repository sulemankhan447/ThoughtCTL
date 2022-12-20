package com.thoughtctl

import com.thoughtctl.api.NetworkInterface
import com.thoughtctl.model.SearchRequestModel
import javax.inject.Inject

class SearchRepository @Inject constructor(private val networkEndPoint: NetworkInterface) {

    fun searchImages(request: SearchRequestModel) {
    }
}