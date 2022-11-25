package com.example.prueba

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.consumokotlinsimple.models.LogIn
import com.example.prueba.adapters.Table2Adapter
import com.example.prueba.adapters.TableAdapter
import com.example.prueba.core.Constants
import com.example.prueba.databinding.ActivityTabla2Binding
import com.example.prueba.databinding.ActivityTablaBinding
import com.example.prueba.databinding.AddPriceBinding
import com.example.prueba.models.Alimento
import com.example.prueba.models.DefaultResponse
import com.example.prueba.models.Especie
import com.example.prueba.models.LogIn.RestLogin
import com.example.prueba.models.Table
import com.example.prueba.models.alimento.PostAlimentoByRegionAndNutriente
import com.example.prueba.models.alimento.ResponseAliementoByRegionAndNutriente
import com.example.prueba.models.especie.EspecieResult
import com.example.prueba.models.preparacion.PostPreparacion
import com.example.prueba.models.preparacion.ResponsePreparacion
import com.example.prueba.models.preparacionAnimales.PostPreparacionAnimal
import com.example.prueba.models.preparacionAnimales.ResponsePreparacionAnimal
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Tabla2Activity : AppCompatActivity() {
    private var cantidadKg = Constants.cantidadMezcla
    private var baseCien = 100
    private lateinit var binding: ActivityTabla2Binding
    private var energiaSeleccionada: Alimento? = null
    private var proteinaSeleccionada: Alimento? = null
    private var fosforoSeleccionada: Alimento? = null
    private var calcioSeleccionada: Alimento? = null
    private var cantPostPreparacionAnimal = 0
    private var preparacionAnimalList = mutableListOf<PostPreparacionAnimal>()
    private val ingredientesEnergiaList = mutableListOf<Alimento>()
    private var ingredientesProteinaList = mutableListOf<Alimento>()
    private val ingredientesFosforo = mutableListOf<Alimento>()

    private val ingredientesCalcio = mutableListOf<Alimento>()

    private val list = mutableListOf(
        Table(
            "MP",
            "Cant\nkg",
            "Prot\n%",
            "Fib\n%",
            "Ener\nMcal/kg",
            "Calc\n%",
            "Fosf\n%",
            "Lisn\n%",
            "Metn\n%",
            "Met+Cis \n%",
        ), Table(
            "Proteina",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
        ), Table(
            "Energia",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
        ), Table(
            "Fosforo",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
        ), Table(
            "Calcio",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
        ), Table(
            "Total",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
        ), Table(
            "Balance",
            cantidadKg.toString(),
            Constants.faseActual.proteina,
            Constants.faseActual.fibra_cruda,
            if (Constants.faseActual.e_m_ave != 0.0000) Constants.faseActual.e_m_ave.toString() else Constants.faseActual.e_d_cerdo.toString(),
            Constants.faseActual.calcio,
            Constants.faseActual.fosf_disp,
            Constants.faseActual.lisina,
            Constants.faseActual.metionina,
            Constants.faseActual.met_cis,
        )
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTabla2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        this.supportActionBar?.hide()

        obtainProteina()
        obtainEnergia()
        obtainFosforo()
        obtainCalcio()
        clicks()

    }

    private fun obtainCalcio() {
        val retro = Retrofit.Builder()
            .baseUrl(Constants.URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service: ApiService = retro.create(ApiService::class.java)
        service.fetchalimentoByRegionAndTipoNutriente(
            PostAlimentoByRegionAndNutriente(
                4,
                Constants.actualUser!!.userForToken.region_id.toInt()
            )
        ).enqueue(object :
            Callback<DefaultResponse<ResponseAliementoByRegionAndNutriente>> {
            override fun onResponse(
                call: Call<DefaultResponse<ResponseAliementoByRegionAndNutriente>>,
                response: Response<DefaultResponse<ResponseAliementoByRegionAndNutriente>>
            ) {
                Log.e("onResponse: ", response.body().toString())
                if (response.body()!!.status == "success") {
                    Log.e("onResponse: ", response.body().toString())
                    response.body()!!.results.forEach { alimento ->
                        ingredientesCalcio.add(
                            Alimento(
                                alimento.id_alimentos,
                                0.0,
                                alimento.nombre_alimento,
                                alimento.materia_seca,
                                alimento.e_m_aves,
                                alimento.e_m_cerdos,
                                alimento.proteina_cruda,
                                alimento.fibra_cruda,
                                alimento.ext_etereo,
                                alimento.calcio,
                                alimento.fosf_disp,
                                alimento.sodio,
                                alimento.arginina,
                                alimento.lisina,
                                alimento.metionina,
                                alimento.met_cis,
                                alimento.treonina,
                                alimento.triptofano,
                            )
                        )
                    }
                    val calcio = mutableListOf<String>()
                    ingredientesCalcio.forEach {
                        calcio.add(it.ingrediente)
                    }
                    setUpCalcio(calcio)
                } else if (response.body()!!.status == "empty") {
                    Snackbar.make(binding.root, "Lo sentimos en su ubicacion no existen alimento de este nutriente", Snackbar.LENGTH_SHORT)
                        .show()
                } else {
                    Snackbar.make(binding.root, "Error al traer los datos", Snackbar.LENGTH_SHORT)
                        .show()
                }
            }

            override fun onFailure(
                call: Call<DefaultResponse<ResponseAliementoByRegionAndNutriente>>,
                t: Throwable
            ) {
                Log.e("onFailure: ", t.toString())
                Snackbar.make(binding.root, "Error de servidor", Snackbar.LENGTH_SHORT).show()
            }
        })
    }

    private fun obtainFosforo() {
        val retro = Retrofit.Builder()
            .baseUrl(Constants.URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service: ApiService = retro.create(ApiService::class.java)
        service.fetchalimentoByRegionAndTipoNutriente(
            PostAlimentoByRegionAndNutriente(
                3,
                Constants.actualUser!!.userForToken.region_id.toInt()
            )
        ).enqueue(object :
            Callback<DefaultResponse<ResponseAliementoByRegionAndNutriente>> {
            override fun onResponse(
                call: Call<DefaultResponse<ResponseAliementoByRegionAndNutriente>>,
                response: Response<DefaultResponse<ResponseAliementoByRegionAndNutriente>>
            ) {
                Log.e("onResponse: ", response.body().toString())
                if (response.body()!!.status == "success") {
                    Log.e("onResponse: ", response.body().toString())
                    response.body()!!.results.forEach { alimento ->
                        ingredientesFosforo.add(
                            Alimento(
                                alimento.id_alimentos,
                                0.0,
                                alimento.nombre_alimento,
                                alimento.materia_seca,
                                alimento.e_m_aves,
                                alimento.e_m_cerdos,
                                alimento.proteina_cruda,
                                alimento.fibra_cruda,
                                alimento.ext_etereo,
                                alimento.calcio,
                                alimento.fosf_disp,
                                alimento.sodio,
                                alimento.arginina,
                                alimento.lisina,
                                alimento.metionina,
                                alimento.met_cis,
                                alimento.treonina,
                                alimento.triptofano,
                            )
                        )
                    }
                    val fosforo = mutableListOf<String>()
                    ingredientesFosforo.forEach {
                        fosforo.add(it.ingrediente)
                    }
                    setUpFosforo(fosforo)
                } else if (response.body()!!.status == "empty") {
                    Snackbar.make(binding.root, "Lo sentimos en su ubicacion no existen alimento de este nutriente", Snackbar.LENGTH_SHORT)
                        .show()
                } else {
                    Snackbar.make(binding.root, "Error al traer los datos", Snackbar.LENGTH_SHORT)
                        .show()
                }
            }

            override fun onFailure(
                call: Call<DefaultResponse<ResponseAliementoByRegionAndNutriente>>,
                t: Throwable
            ) {
                Log.e("onFailure: ", t.toString())
                Snackbar.make(binding.root, "Error de servidor", Snackbar.LENGTH_SHORT).show()
            }
        })
    }

    private fun obtainProteina() {
        val retro = Retrofit.Builder()
            .baseUrl(Constants.URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service: ApiService = retro.create(ApiService::class.java)
        service.fetchalimentoByRegionAndTipoNutriente(
            PostAlimentoByRegionAndNutriente(
                2,
                Constants.actualUser!!.userForToken.region_id.toInt()
            )
        ).enqueue(object :
            Callback<DefaultResponse<ResponseAliementoByRegionAndNutriente>> {
            override fun onResponse(
                call: Call<DefaultResponse<ResponseAliementoByRegionAndNutriente>>,
                response: Response<DefaultResponse<ResponseAliementoByRegionAndNutriente>>
            ) {
                Log.e("onResponse: ", response.body().toString())
                if (response.body()!!.status == "success") {
                    Log.e("onResponse: ", response.body().toString())
                    response.body()!!.results.forEach { alimento ->
                        ingredientesProteinaList.add(
                            Alimento(
                                alimento.id_alimentos,
                                0.0,
                                alimento.nombre_alimento,
                                alimento.materia_seca,
                                alimento.e_m_aves,
                                alimento.e_m_cerdos,
                                alimento.proteina_cruda,
                                alimento.fibra_cruda,
                                alimento.ext_etereo,
                                alimento.calcio,
                                alimento.fosf_disp,
                                alimento.sodio,
                                alimento.arginina,
                                alimento.lisina,
                                alimento.metionina,
                                alimento.met_cis,
                                alimento.treonina,
                                alimento.triptofano,
                            )
                        )
                    }
                    val proteina = mutableListOf<String>()
                    ingredientesProteinaList.forEach {
                        proteina.add(it.ingrediente)
                    }
                    setUpProteina(proteina)
                } else if (response.body()!!.status == "empty") {
                    Snackbar.make(binding.root, "Lo sentimos en su ubicacion no existen alimento de este nutriente", Snackbar.LENGTH_SHORT)
                        .show()
                } else {
                    Snackbar.make(binding.root, "Error al traer los datos", Snackbar.LENGTH_SHORT)
                        .show()
                }
            }

            override fun onFailure(
                call: Call<DefaultResponse<ResponseAliementoByRegionAndNutriente>>,
                t: Throwable
            ) {
                Log.e("onFailure: ", t.toString())
                Snackbar.make(binding.root, "Error de servidor", Snackbar.LENGTH_SHORT).show()
            }
        })
    }

    private fun obtainEnergia(){
        val retro = Retrofit.Builder()
            .baseUrl(Constants.URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service: ApiService = retro.create(ApiService::class.java)
        service.fetchalimentoByRegionAndTipoNutriente(
            PostAlimentoByRegionAndNutriente(
                1,
                Constants.actualUser!!.userForToken.region_id.toInt()
            )
        ).enqueue(object :
            Callback<DefaultResponse<ResponseAliementoByRegionAndNutriente>> {
            override fun onResponse(
                call: Call<DefaultResponse<ResponseAliementoByRegionAndNutriente>>,
                response: Response<DefaultResponse<ResponseAliementoByRegionAndNutriente>>
            ) {
                Log.e("onResponse: ", response.body().toString())
                if (response.body()!!.status == "success") {
                    Log.e("onResponse: ", response.body().toString())
                    response.body()!!.results.forEach { alimento ->
                        ingredientesEnergiaList.add(
                            Alimento(
                                alimento.id_alimentos,
                                0.0,
                                alimento.nombre_alimento,
                                alimento.materia_seca,
                                alimento.e_m_aves,
                                alimento.e_m_cerdos,
                                alimento.proteina_cruda,
                                alimento.fibra_cruda,
                                alimento.ext_etereo,
                                alimento.calcio,
                                alimento.fosf_disp,
                                alimento.sodio,
                                alimento.arginina,
                                alimento.lisina,
                                alimento.metionina,
                                alimento.met_cis,
                                alimento.treonina,
                                alimento.triptofano,
                            )
                        )
                    }
                    val energia = mutableListOf<String>()
                    ingredientesEnergiaList.forEach {
                        energia.add(it.ingrediente)
                    }
                    setUpEnergia(energia)
                } else if (response.body()!!.status == "empty") {
                    Snackbar.make(binding.root, "Lo sentimos en su ubicacion no existen alimento de este nutriente", Snackbar.LENGTH_SHORT)
                        .show()
                } else {
                    Snackbar.make(binding.root, "Error al traer los datos", Snackbar.LENGTH_SHORT)
                        .show()
                }
            }

            override fun onFailure(
                call: Call<DefaultResponse<ResponseAliementoByRegionAndNutriente>>,
                t: Throwable
            ) {
                Log.e("onFailure: ", t.toString())
                Snackbar.make(binding.root, "Error de servidor", Snackbar.LENGTH_SHORT).show()
            }
        })
    }

    private fun clicks() {
        binding.actvProteina.setOnItemClickListener { parent, view, position, id ->
            energiaSeleccionada = ingredientesEnergiaList[position]
            añadirPrecio()
        }
        binding.actvEnergia.setOnItemClickListener { parent, view, position, id ->
            proteinaSeleccionada = ingredientesProteinaList[position]
        }
        binding.actvFosforo.setOnItemClickListener { parent, view, position, id ->
            fosforoSeleccionada = ingredientesFosforo[position]
        }
        binding.actvCalcio.setOnItemClickListener { parent, view, position, id ->
            calcioSeleccionada = ingredientesCalcio[position]
        }
        binding.btnCalcular.setOnClickListener {
            if (energiaSeleccionada == null || proteinaSeleccionada == null) {
                Snackbar.make(binding.root, "Debe Seleccionar los datos", Snackbar.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }
            encontrarCantidadesPE()
        }
        binding.btnFosforo.setOnClickListener {
            if (fosforoSeleccionada == null) {
                Snackbar.make(binding.root, "Debe Seleccionar los datos", Snackbar.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }
            encontrarCantidadesFos()
            binding.lLFosforo.visibility = View.GONE
            binding.llCalcio.visibility = View.VISIBLE
        }
        binding.btnCalcio.setOnClickListener {
            if (calcioSeleccionada == null) {
                Snackbar.make(binding.root, "Debe Seleccionar los datos", Snackbar.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }
            encontrarCantidadescal()
            binding.llCalcio.visibility = View.GONE
            binding.btnGuardar.visibility = View.VISIBLE
        }
        binding.btnGuardar.setOnClickListener {
            postPreparacion()
        }
    }

    private fun postPreparacion() {
        Log.e("postPreparacion: ", Constants.faseActual.id_requerimiento_animal.toString())
        val retro = Retrofit.Builder()
            .baseUrl(Constants.URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service: ApiService = retro.create(ApiService::class.java)
        service.postPreparacion(
            PostPreparacion(
                list[list.size - 2].cantidad.toDouble(),
                0.0,
                Constants.faseActual.id_requerimiento_animal,
                Constants.actualUser!!.userForToken.id,
            )
        ).enqueue(object :
            Callback<ResponsePreparacion> {
            override fun onResponse(
                call: Call<ResponsePreparacion>,
                response: Response<ResponsePreparacion>
            ) {
                if (response.body()?.status == "success") {
                    Log.e("onResponse: ", response.body().toString())
                    preparacionAnimalList = mutableListOf(
                        PostPreparacionAnimal(
                            proteinaSeleccionada!!.id,
                            list[1].cantidad.toDouble(),
                            0.0,
                            response.body()!!.results.id_preparacion,
                        ),
                        PostPreparacionAnimal(
                            proteinaSeleccionada!!.id,
                            list[2].cantidad.toDouble(),
                            0.0,
                            response.body()!!.results.id_preparacion,
                        ),
                        PostPreparacionAnimal(
                            proteinaSeleccionada!!.id,
                            list[3].cantidad.toDouble(),
                            0.0,
                            response.body()!!.results.id_preparacion,
                        ),
                        PostPreparacionAnimal(
                            proteinaSeleccionada!!.id,
                            list[4].cantidad.toDouble(),
                            0.0,
                            response.body()!!.results.id_preparacion,
                        )
                    )

                    postPreparacionAnimal()
                } else {
                    Log.e("onResponse: ",response.body().toString() )
                    Log.e("onResponse: ",response.toString() )
                    Snackbar.make(
                        binding.root,
                        "No se puedo guardar la preparacion",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<ResponsePreparacion>, t: Throwable) {
                Log.e("onFailure: ", t.toString())
                Snackbar.make(binding.root, "Error de servidor", Snackbar.LENGTH_SHORT).show()
            }
        })
    }

    private fun postPreparacionAnimal() {
        val retro = Retrofit.Builder()
            .baseUrl(Constants.URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service: ApiService = retro.create(ApiService::class.java)

        preparacionAnimalList.forEach {
            Log.e("postPreparacionAnimalForEach: ", it.toString())
            service.postPreparacionAlimento(it).enqueue(object :
                Callback<ResponsePreparacionAnimal> {
                override fun onResponse(
                    call: Call<ResponsePreparacionAnimal>,
                    response: Response<ResponsePreparacionAnimal>
                ) {
                    if (response.body()?.status == "success") {
                        Log.e("onResponse: ", response.body().toString())
                        Snackbar.make(
                            binding.root,
                            "Se guardo correctamente",
                            Snackbar.LENGTH_SHORT
                        ).show()
                        cantPostPreparacionAnimal++
                        if (cantPostPreparacionAnimal == 4) {
                            Snackbar.make(
                                binding.root,
                                "Se ha guardado satisfactoriamente su preparacion",
                                Snackbar.LENGTH_SHORT
                            ).show()
                            val i = Intent(applicationContext, MainActivity::class.java)
                            startActivity(i)
                            finish()
                        } else {
                            Snackbar.make(
                                binding.root,
                                "Error al guardar la preparacion",
                                Snackbar.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        Log.e("onResponse: ",response.body().toString() )
                        Log.e("onResponse: ",response.toString() )
                        Snackbar.make(
                            binding.root,
                            "No se puedo guardar la preparacion",
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<ResponsePreparacionAnimal>, t: Throwable) {
                    Log.e("onFailure: ", t.toString())
                    Snackbar.make(binding.root, "Error de servidor", Snackbar.LENGTH_SHORT).show()
                }
            })
        }
    }

    private fun encontrarCantidadescal() {
        Log.e("encontrarCantidadesFos: ", list[list.size - 1].fosforo.toDouble().toString())
        Log.e("encontrarCantidadesFos: ", list[list.size - 2].fosforo.toDouble().toString())
        val resta = list[list.size - 1].calcio.toDouble() - list[list.size - 2].calcio.toDouble()
        val cantidad = resta / (calcioSeleccionada!!.calcio.toDouble() / 100)
        Log.e("encontrarCantidadesFos: ", resta.toString())
        Log.e("encontrarCantidadesFos: ", fosforoSeleccionada!!.calcio)
        Log.e("encontrarCantidadesFos: ", cantidad.toString())

        list[4].materia = calcioSeleccionada!!.ingrediente
        list[4].cantidad = String.format("%.2f", cantidad)
        //////////////////
        list[4].proteina = (cantidad * (calcioSeleccionada!!.proteina.toDouble() / 100)).toString()
        list[4].fibra = (cantidad * (calcioSeleccionada!!.fibra_cruda.toDouble() / 100)).toString()
        list[4].energia = (cantidad * (calcioSeleccionada!!.e_m_ave.toDouble() / 100)).toString()
        list[4].calcio = (cantidad * (calcioSeleccionada!!.calcio.toDouble() / 100)).toString()
        list[4].fosforo = (cantidad * (calcioSeleccionada!!.fosf_disp.toDouble() / 100)).toString()
        list[4].lisina = (cantidad * (calcioSeleccionada!!.lisina.toDouble() / 100)).toString()
        list[4].metionina =
            (cantidad * (calcioSeleccionada!!.metionina.toDouble() / 100)).toString()
        list[4].metCis = (cantidad * (calcioSeleccionada!!.met_cis.toDouble() / 100)).toString()
        //////////////////
        list[list.size - 2].cantidad =
            (list[list.size - 2].cantidad.toDouble() + list[4].cantidad.toDouble()).toString()
        list[list.size - 2].proteina =
            (list[list.size - 2].proteina.toDouble() + list[4].proteina.toDouble()).toString()
        list[list.size - 2].fibra =
            (list[list.size - 2].fibra.toDouble() + list[4].fibra.toDouble()).toString()
        list[list.size - 2].energia =
            (list[list.size - 2].energia.toDouble() + list[4].energia.toDouble()).toString()
        list[list.size - 2].calcio =
            (list[list.size - 2].calcio.toDouble() + list[4].calcio.toDouble()).toString()
        list[list.size - 2].fosforo =
            (list[list.size - 2].fosforo.toDouble() + list[4].fosforo.toDouble()).toString()
        list[list.size - 2].lisina =
            (list[list.size - 2].lisina.toDouble() + list[4].lisina.toDouble()).toString()
        list[list.size - 2].metionina =
            (list[list.size - 2].metionina.toDouble() + list[4].metionina.toDouble()).toString()
        list[list.size - 2].metCis =
            (list[list.size - 2].metCis.toDouble() + list[4].metCis.toDouble()).toString()

        setUpRecycler(list)
    }

    private fun setUpCalcio(calcio: MutableList<String>) {
        val adapter = ArrayAdapter(applicationContext, R.layout.list_item, calcio)
        binding.actvCalcio.setAdapter(adapter)
    }

    private fun encontrarCantidadesFos() {
        Log.e("encontrarCantidadesFos: ", list[list.size - 1].fosforo.toDouble().toString())
        Log.e("encontrarCantidadesFos: ", list[list.size - 2].fosforo.toDouble().toString())
        val resta = list[list.size - 1].fosforo.toDouble() - list[list.size - 2].fosforo.toDouble()
        val cantidad = resta / (fosforoSeleccionada!!.fosf_disp.toDouble() / 100)
        Log.e("encontrarCantidadesFos: ", resta.toString())
        Log.e("encontrarCantidadesFos: ", fosforoSeleccionada!!.fosf_disp)
        Log.e("encontrarCantidadesFos: ", cantidad.toString())

        list[3].materia = fosforoSeleccionada!!.ingrediente
        list[3].cantidad = String.format("%.2f", cantidad)
        //////////////////
        list[3].proteina = (cantidad * (fosforoSeleccionada!!.proteina.toDouble() / 100)).toString()
        list[3].fibra = (cantidad * (fosforoSeleccionada!!.fibra_cruda.toDouble() / 100)).toString()
        list[3].energia = (cantidad * (fosforoSeleccionada!!.e_m_ave.toDouble() / 100)).toString()
        list[3].calcio = (cantidad * (fosforoSeleccionada!!.calcio.toDouble() / 100)).toString()
        list[3].fosforo = (cantidad * (fosforoSeleccionada!!.fosf_disp.toDouble() / 100)).toString()
        list[3].lisina = (cantidad * (fosforoSeleccionada!!.lisina.toDouble() / 100)).toString()
        list[3].metionina =
            (cantidad * (fosforoSeleccionada!!.metionina.toDouble() / 100)).toString()
        list[3].metCis = (cantidad * (fosforoSeleccionada!!.met_cis.toDouble() / 100)).toString()
        //////////////////
        list[list.size - 2].cantidad =
            (list[list.size - 2].cantidad.toDouble() + list[3].cantidad.toDouble()).toString()
        list[list.size - 2].proteina =
            (list[list.size - 2].proteina.toDouble() + list[3].proteina.toDouble()).toString()
        list[list.size - 2].fibra =
            (list[list.size - 2].fibra.toDouble() + list[3].fibra.toDouble()).toString()
        list[list.size - 2].energia =
            (list[list.size - 2].energia.toDouble() + list[3].energia.toDouble()).toString()
        list[list.size - 2].calcio =
            (list[list.size - 2].calcio.toDouble() + list[3].calcio.toDouble()).toString()
        list[list.size - 2].fosforo =
            (list[list.size - 2].fosforo.toDouble() + list[3].fosforo.toDouble()).toString()
        list[list.size - 2].lisina =
            (list[list.size - 2].lisina.toDouble() + list[3].lisina.toDouble()).toString()
        list[list.size - 2].metionina =
            (list[list.size - 2].metionina.toDouble() + list[3].metionina.toDouble()).toString()
        list[list.size - 2].metCis =
            (list[list.size - 2].metCis.toDouble() + list[3].metCis.toDouble()).toString()

        setUpRecycler(list)

    }

    private fun encontrarCantidadesCal() {
        val resta = list[list.size - 1].calcio.toDouble() - list[list.size - 2].calcio.toDouble()
        val cantidad = resta / (calcioSeleccionada!!.calcio.toDouble() / 100)
        Log.e("encontrarCantidadesFos: ", resta.toString())
        Log.e("encontrarCantidadesFos: ", calcioSeleccionada!!.calcio)
        Log.e("encontrarCantidadesFos: ", cantidad.toString())
    }

    private fun añadirPrecio() {


//        val dialogBinding = AddPriceBinding.inflate(layoutInflater)
//
//        val alert = AlertDialog.Builder(applicationContext).apply {
//            setView(dialogBinding.root)
//        }.create()
//
//        dialogBinding.txtTitleParameter.text =
//            "Ingrese el precio de ${energiaSeleccionada!!.ingrediente} en su region :"
//
//        dialogBinding.btnPrice.setOnClickListener {
//            if (dialogBinding.edtSample.text.toString().isNullOrEmpty()){
//                return@setOnClickListener
//                Snackbar.make(binding.root, "Debe escribir un precio", Snackbar.LENGTH_SHORT)
//            }
//            alert.dismiss()
//        }
//        alert.setCancelable(false)
//        alert.show()


    }

    private fun encontrarCantidadesPE() {
        Log.e("encontrarCantidadesPE: ","${energiaSeleccionada!!.proteina.toDouble()} > ${Constants.faseActual.proteina.toDouble()}" )
        Log.e("encontrarCantidadesPE: ","${proteinaSeleccionada!!.proteina.toDouble()} > ${Constants.faseActual.proteina.toDouble()}" )
        val difEnergia: Double =
            if (energiaSeleccionada!!.proteina.toDouble() > Constants.faseActual.proteina.toDouble()) {
                (energiaSeleccionada!!.proteina.toDouble() - Constants.faseActual.proteina.toDouble())
            } else {
                (Constants.faseActual.proteina.toDouble() - energiaSeleccionada!!.proteina.toDouble())
            }
        val difProteina: Double =
            if (proteinaSeleccionada!!.proteina.toDouble() > Constants.faseActual.proteina.toDouble()) {
                (proteinaSeleccionada!!.proteina.toDouble() - Constants.faseActual.proteina.toDouble())
            } else {
                (Constants.faseActual.proteina.toDouble() - proteinaSeleccionada!!.proteina.toDouble())
            }

        val sum = difEnergia + difProteina

        energiaSeleccionada!!.cantidad = (difProteina * cantidadKg) / sum
        proteinaSeleccionada!!.cantidad = (difEnergia * cantidadKg) / sum
        list[1].materia = proteinaSeleccionada!!.ingrediente
        list[1].cantidad = String.format("%.2f", proteinaSeleccionada!!.cantidad)
        list[2].materia = energiaSeleccionada!!.ingrediente
        list[2].cantidad = String.format("%.2f", energiaSeleccionada!!.cantidad)

        list[list.size - 2].cantidad =
            (list[1].cantidad.toDouble() + list[2].cantidad.toDouble()).toString()
        balanceProteina()
        balanceFibra()
        balanceEm()
        balanceCalcio()
        balanceFosforo()
        balanceLisina()
        balanceMetionina()
        balanceMetCis()

        binding.linearLayout5.visibility = View.GONE
        binding.lLFosforo.visibility = View.VISIBLE
        setUpRecycler(list)
    }

    private fun setUpFosforo(fosforo: MutableList<String>) {
        val adapter = ArrayAdapter(applicationContext, R.layout.list_item, fosforo)
        binding.actvFosforo.setAdapter(adapter)
    }

    private fun balanceFibra() {
        val aporteEnerigia =
            (energiaSeleccionada!!.cantidad * energiaSeleccionada!!.fibra_cruda.toDouble()) / baseCien
        val aporteProteina =
            (proteinaSeleccionada!!.cantidad * proteinaSeleccionada!!.fibra_cruda.toDouble()) / baseCien
        list[1].fibra = String.format("%.2f", aporteProteina)
        list[2].fibra = String.format("%.2f", aporteEnerigia)
        list[list.size - 2].fibra = (list[1].fibra.toDouble() + list[2].fibra.toDouble()).toString()
    }

    private fun balanceProteina() {
        Log.e("balanceProteina: ","${energiaSeleccionada!!.cantidad} * ${energiaSeleccionada!!.proteina.toDouble()} / $baseCien")
        val aporteProteinaEnerigia =
            (energiaSeleccionada!!.cantidad * energiaSeleccionada!!.proteina.toDouble()) / baseCien
        val aporteProteinaProteina =
            (proteinaSeleccionada!!.cantidad * proteinaSeleccionada!!.proteina.toDouble()) / baseCien
        Log.e("balanceProteina: ", "")
        list[1].proteina = String.format("%.2f", aporteProteinaProteina)
        list[2].proteina = String.format("%.2f", aporteProteinaEnerigia)
        list[list.size - 2].proteina =
            (list[1].proteina.toDouble() + list[2].proteina.toDouble()).toString()
    }

    private fun balanceEm() {
        val aporteEnerigia =
            (energiaSeleccionada!!.cantidad * energiaSeleccionada!!.e_m_ave.toDouble()) / baseCien
        val aporteProteina =
            (proteinaSeleccionada!!.cantidad * proteinaSeleccionada!!.e_m_ave.toDouble()) / baseCien
        list[1].energia = String.format("%.2f", aporteProteina)
        list[2].energia = String.format("%.2f", aporteEnerigia)
        list[list.size - 2].energia =
            (list[1].energia.toDouble() + list[2].energia.toDouble()).toString()
    }

    private fun balanceCalcio() {
        val aporteEnerigia =
            (energiaSeleccionada!!.cantidad * energiaSeleccionada!!.calcio.toDouble()) / baseCien
        val aporteProteina =
            (proteinaSeleccionada!!.cantidad * proteinaSeleccionada!!.calcio.toDouble()) / baseCien
        list[1].calcio = String.format("%.2f", aporteProteina)
        list[2].calcio = String.format("%.2f", aporteEnerigia)
        list[list.size - 2].calcio =
            (list[1].calcio.toDouble() + list[2].calcio.toDouble()).toString()
    }

    private fun balanceFosforo() {
        val aporteEnerigia =
            (energiaSeleccionada!!.cantidad * energiaSeleccionada!!.fosf_disp.toDouble()) / baseCien
        val aporteProteina =
            (proteinaSeleccionada!!.cantidad * proteinaSeleccionada!!.fosf_disp.toDouble()) / baseCien
        list[1].fosforo = String.format("%.2f", aporteProteina)
        list[2].fosforo = String.format("%.2f", aporteEnerigia)
        list[list.size - 2].fosforo =
            (list[1].fosforo.toDouble() + list[2].fosforo.toDouble()).toString()
    }

    private fun balanceLisina() {
        val aporteEnerigia =
            (energiaSeleccionada!!.cantidad * energiaSeleccionada!!.lisina.toDouble()) / baseCien
        val aporteProteina =
            (proteinaSeleccionada!!.cantidad * proteinaSeleccionada!!.lisina.toDouble()) / baseCien
        list[1].lisina = String.format("%.2f", aporteProteina)
        list[2].lisina = String.format("%.2f", aporteEnerigia)
        list[list.size - 2].lisina =
            (list[1].lisina.toDouble() + list[2].lisina.toDouble()).toString()
    }

    private fun balanceMetionina() {
        val aporteEnerigia =
            (energiaSeleccionada!!.cantidad * energiaSeleccionada!!.metionina.toDouble()) / baseCien
        val aporteProteina =
            (proteinaSeleccionada!!.cantidad * proteinaSeleccionada!!.metionina.toDouble()) / baseCien
        list[1].metionina = String.format("%.2f", aporteProteina)
        list[2].metionina = String.format("%.2f", aporteEnerigia)
        list[list.size - 2].metionina =
            String.format("%.2f", (list[1].metionina.toDouble() + list[2].metionina.toDouble()))
    }

    private fun balanceMetCis() {
        val aporteEnerigia =
            (energiaSeleccionada!!.cantidad * energiaSeleccionada!!.met_cis.toDouble()) / baseCien
        val aporteProteina =
            (proteinaSeleccionada!!.cantidad * proteinaSeleccionada!!.met_cis.toDouble()) / baseCien
        list[1].metCis = String.format("%.2f", aporteProteina)
        list[2].metCis = String.format("%.2f", aporteEnerigia)
        list[list.size - 2].metCis =
            (list[1].metCis.toDouble() + list[2].metCis.toDouble()).toString()
    }

    private fun setUpRecycler(list: MutableList<Table>) {
        binding.rv.adapter = Table2Adapter(list)
        binding.rv.layoutManager = LinearLayoutManager(applicationContext)
    }

    private fun setUpEnergia(energia: MutableList<String>) {
        val adapter = ArrayAdapter(applicationContext, R.layout.list_item, energia)
        binding.actvEnergia.setAdapter(adapter)
    }

    private fun setUpProteina(proteina: MutableList<String>) {
        val adapter = ArrayAdapter(applicationContext, R.layout.list_item, proteina)
        binding.actvProteina.setAdapter(adapter)
    }
}