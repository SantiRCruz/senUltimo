package com.example.prueba.models.alimento

data class ResponseAliementoByRegionAndNutriente(
    val Regione: Regione,
    val arginina: String,
    val calcio: String,
    val createdAt: Any,
    val e_m_aves: String,
    val e_m_cerdos: String,
    val ext_etereo: String,
    val fibra_cruda: String,
    val fosf_disp: String,
    val id_alimentos: Int,
    val lisina: String,
    val materia_seca: String,
    val met_cis: String,
    val metionina: String,
    val nombre_alimento: String,
    val proteina_cruda: String,
    val region_id: String,
    val sodio: String,
    val tipo_nutriente_id: String,
    val tiponutriente: Tiponutriente,
    val treonina: String,
    val triptofano: String,
    val updatedAt: Any
)