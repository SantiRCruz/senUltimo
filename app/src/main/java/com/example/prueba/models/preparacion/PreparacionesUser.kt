package com.example.prueba.models.preparacion

data class PreparacionesUser(
    val RequerimientosAnimale: RequerimientosAnimale,
    val User: User,
    val cantidad_produccion: String,
    val createdAt: String,
    val id_preparacion: Int,
    val precio_produccion: String,
    val requerimiento_animal_id: String,
    val updatedAt: String,
    val usuario_id: String
)