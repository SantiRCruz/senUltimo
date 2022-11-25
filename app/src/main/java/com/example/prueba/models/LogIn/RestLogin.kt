package com.example.prueba.models.LogIn

data class RestLogin(
    val action: String,
    val code: String,
    val delay: Any,
    val message: String,
    val results: UserInfoLogin,
    val show: Boolean,
    val status: String
)