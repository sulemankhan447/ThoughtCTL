package com.thoughtctl.model

data class ImgurResponseModel(
    val data: List<ImgurModel>
)

data class ImgurModel(
    val title: String? = "",
    val images: List<Image>,
    val images_count: Int,
)


data class Image(
    val title: String? = "",
    val datetime: Long? = 0L,
    val link: String? = ""
)