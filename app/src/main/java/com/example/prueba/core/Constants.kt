package com.example.prueba.core

import com.example.prueba.models.Fase
import com.example.prueba.models.LogIn.UserInfoLogin
import com.example.prueba.models.fase.RequerimientoAnimal

object Constants {
    var actualUser : UserInfoLogin ?= null
    const val URL = " http://152.200.173.74:8057/api/"
    var faseActual = RequerimientoAnimal(
        0,
        "",
        "",
        0.0,
        0.0,
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        0,
        "",
    )
    var tipoEnergia = ""
    var cantidadMezcla = 100
}