package com.example.prueba.models.preparacionAnimales

data class PostPreparacionAnimal(
    val alimento_id: Int,
    val cantidad_alimento: Double,
    val precio_alimento: Double,
    val preparacion_id: Int
)