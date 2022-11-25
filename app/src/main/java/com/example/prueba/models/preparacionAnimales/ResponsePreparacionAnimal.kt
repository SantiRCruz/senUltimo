package com.example.prueba.models.preparacionAnimales

data class ResponsePreparacionAnimal(
    val action: String,
    val code: String,
    val delay: Any,
    val message: String,
    val results: ResultsAnimal,
    val show: Boolean,
    val status: String
)