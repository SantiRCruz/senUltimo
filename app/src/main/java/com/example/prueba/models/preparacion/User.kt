package com.example.prueba.models.preparacion

data class User(
    val celular: String,
    val contrasena: String,
    val correo: String,
    val createdAt: String,
    val foto: String,
    val id_usuario: Int,
    val identificacion: String,
    val nombres: String,
    val region_id: Any,
    val tipo_usuario: String,
    val updatedAt: String
)