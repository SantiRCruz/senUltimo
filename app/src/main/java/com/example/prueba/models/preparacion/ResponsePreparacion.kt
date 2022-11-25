package com.example.prueba.models.preparacion

data class ResponsePreparacion(
    val action: String,
    val code: String,
    val delay: Any,
    val message: String,
    val results: ResultsPreparacion,
    val show: Boolean,
    val status: String
)