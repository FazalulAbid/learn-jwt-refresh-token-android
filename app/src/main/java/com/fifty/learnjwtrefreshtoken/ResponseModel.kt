package com.fifty.learnjwtrefreshtoken

data class ResponseModel(
    val message: String? = null,
    val token: String? = null,
    val data: String? = null,
    val isLoading: Boolean = false
)