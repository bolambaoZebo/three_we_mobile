package com.example.three_we_mobile.network.response

data class AppResponse(
    val success: Boolean = false,
    val appVersion: Int? = null,
    val isActive: Boolean = false,
    val downloadLink: String? = null
)
