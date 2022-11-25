package com.example.prueba

import com.example.consumokotlinsimple.models.LogIn
import com.example.prueba.models.DefaultResponse
import com.example.prueba.models.DefaultResponseObject
import com.example.prueba.models.LogIn.RestLogin
import com.example.prueba.models.alimento.PostAlimentoByRegionAndNutriente
import com.example.prueba.models.alimento.ResponseAliementoByRegionAndNutriente
import com.example.prueba.models.especie.EspecieResult
import com.example.prueba.models.fase.RequerimientoAnimal
import com.example.prueba.models.preparacion.PostPreparacion
import com.example.prueba.models.preparacion.PreparacionesUser
import com.example.prueba.models.preparacion.ResponsePreparacion
import com.example.prueba.models.preparacionAnimales.PostPreparacionAnimal
import com.example.prueba.models.preparacionAnimales.ResponsePreparacionAnimal
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @POST("login")
    fun postLogin(@Body logIn: LogIn): Call<RestLogin>

    @GET("viewUser/{id}")
    fun getUser(@Path("id") id: String): Call<DefaultResponseObject>

    @GET("allespecies")
    fun getEspecies(): Call<DefaultResponse<EspecieResult>>

    @GET("alimentestatus/{id}")
    fun getFaseByIdEspecie(@Path("id") id: String): Call<DefaultResponse<RequerimientoAnimal>>

    @POST("createpreparacion")
    fun postPreparacion(@Body postPreparacion: PostPreparacion): Call<ResponsePreparacion>

    @POST("createpreparacionalimento")
    fun postPreparacionAlimento(@Body postPreparacionAnimal: PostPreparacionAnimal): Call<ResponsePreparacionAnimal>

    @GET("preparacionbyuser/{id}")
    fun getPreparacionById(@Path("id") id: String): Call<DefaultResponse<PreparacionesUser>>

    @POST("alimentobyregionandnutriente")
    fun fetchalimentoByRegionAndTipoNutriente(@Body postAlimentoByRegionAndNutriente: PostAlimentoByRegionAndNutriente): Call<DefaultResponse<ResponseAliementoByRegionAndNutriente>>
}