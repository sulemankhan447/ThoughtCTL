package com.thoughtctl.model

data class SearchRequestModel(
    var sort: String? = "top", //time | viral | top - defaults to time
    var window: String? = "week",  // week | month | year | all, defaults to all.
    var q:String?=""
)
