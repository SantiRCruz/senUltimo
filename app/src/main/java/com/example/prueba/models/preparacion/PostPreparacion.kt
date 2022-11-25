package com.example.prueba.models.preparacion

data class PostPreparacion(
    val cantidad_produccion: Double,
    val precio_produccion: Double,
    val requerimiento_animal_id: Int,
    val usuario_id: Int
)