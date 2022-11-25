package com.example.prueba

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import androidx.lifecycle.Lifecycle
import com.example.prueba.core.Constants
import com.example.prueba.databinding.ActivityMainBinding
import com.example.prueba.models.DefaultResponse
import com.example.prueba.models.Especie
import com.example.prueba.models.Fase
import com.example.prueba.models.especie.EspecieResult
import com.example.prueba.models.fase.RequerimientoAnimal
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private var especiesList = mutableListOf<EspecieResult>()

    private var fasesList = mutableListOf<RequerimientoAnimal>()

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        this.supportActionBar?.hide()

        setDisable(true)

        obtainEspecies()
        clicks()
    }

    private fun obtainEspecies() {
        val retro = Retrofit.Builder()
            .baseUrl(Constants.URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service: ApiService = retro.create(ApiService::class.java)
        service.getEspecies().enqueue(object :
            Callback<DefaultResponse<EspecieResult>> {
            override fun onResponse(
                call: Call<DefaultResponse<EspecieResult>>,
                response: Response<DefaultResponse<EspecieResult>>
            ) {
                if (response.isSuccessful) {
                    Log.e("onResponse: ", response.body().toString())
                    especiesList = response.body()!!.results.toMutableList()
                    val especies = mutableListOf<String>()
                    response.body()?.results?.forEach {
                        if (it.nombre_especie == null) " " else especies.add(it.nombre_especie)
                    }
                    Log.e("onResponse: ", especies.toString())
                    setUpEspecies(especies)
                } else {
                    Snackbar.make(binding.root, "Error al traer los datos", Snackbar.LENGTH_SHORT)
                        .show()
                }
            }

            override fun onFailure(call: Call<DefaultResponse<EspecieResult>>, t: Throwable) {
                Log.e("onFailure: ", t.toString())
                Snackbar.make(binding.root, "Error de servidor", Snackbar.LENGTH_SHORT).show()
            }
        })
    }

    private fun clicks() {
        binding.actvEspecie.setOnItemClickListener { parent, view, position, id ->
            obtainFases(position)
        }
        binding.actvFase.setOnItemClickListener { parent, view, position, id ->
            val data = fasesList[position]
            Constants.faseActual = data
            setTable(data)
            setDisable(false)
        }
        binding.btnContinuar.setOnClickListener {
            if (!validateCantidad()) {
                return@setOnClickListener
            }
            if (binding.tIEDTcantidad.text.toString().isNotEmpty()) {
                Constants.cantidadMezcla = binding.tIEDTcantidad.text.toString().toInt()
            }
            val i = Intent(applicationContext, Tabla2Activity::class.java)
            startActivity(i)
        }
        binding.lLProfile.setOnClickListener {
            val i = Intent(applicationContext, ProfileActivity::class.java)
            startActivity(i)
        }
    }

    private fun validateCantidad(): Boolean {
        return if (binding.tIEDTcantidad.text.toString().isEmpty()
        ) {
            binding.tIEDTcantidad.error = null
            true
        } else if (binding.tIEDTcantidad.text.toString().toInt() > 100) {
            binding.tIEDTcantidad.error = "Este campo no puede ser mayor a 100"
            false
        } else {
            binding.tIEDTcantidad.error = null
            true
        }
    }

    private fun setTable(data: RequerimientoAnimal) {
        Log.e("setTable: ", data.toString())
        binding.tvProteina.text = data.proteina
        binding.tvEnergia.text = if (data.e_m_ave != 0.0000) {
            Constants.tipoEnergia = "ave"
            data.e_m_ave.toString()
        } else {
            Constants.tipoEnergia = "cerdo"
            data.e_d_cerdo.toString()
        }
        binding.tvFosforo.text = data.fosf_disp
        binding.tvCalcio.text = data.calcio
        binding.tvLisina.text = data.lisina
        binding.tvMetionina.text = data.metionina

    }

    private fun obtainFases(position: Int) {
        val especie = especiesList[position]
        val retro = Retrofit.Builder()
            .baseUrl(Constants.URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service: ApiService = retro.create(ApiService::class.java)
        service.getFaseByIdEspecie(especie.id_especie.toString())
            .enqueue(object : Callback<DefaultResponse<RequerimientoAnimal>> {
                override fun onResponse(
                    call: Call<DefaultResponse<RequerimientoAnimal>>,
                    response: Response<DefaultResponse<RequerimientoAnimal>>
                ) {
                    Log.e("onResponse: ", response.body().toString())
                    if (response.isSuccessful) {
                        if (!response.body()!!.results.isNullOrEmpty()) {
                            fasesList = response.body()!!.results.toMutableList()
                            val fases = mutableListOf<String>()
                            response.body()!!.results.forEach {
                                fases.add(it.nombre_fase)
                            }
                            setUpFase(fases)
                        } else {
                            fasesList = mutableListOf()
                            setUpFase(mutableListOf())
                            Snackbar.make(
                                binding.root,
                                "No existen fases de esta especie",
                                Snackbar.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        Snackbar.make(
                            binding.root,
                            "Error al traer los datos",
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(
                    call: Call<DefaultResponse<RequerimientoAnimal>>,
                    t: Throwable
                ) {
                    Log.e("onFailure: ", t.toString())
                    Snackbar.make(binding.root, "Error de servidor", Snackbar.LENGTH_SHORT).show()
                }
            })
    }

    private fun setUpFase(fase: MutableList<String>) {
        val adapter = ArrayAdapter(applicationContext, R.layout.list_item, fase)
        binding.actvFase.setAdapter(adapter)
    }

    private fun setDisable(b: Boolean) {
        if (b) {
            binding.btnContinuar.isEnabled = false
            binding.lLTable.visibility = View.GONE
        } else {
            binding.btnContinuar.isEnabled = true
            binding.lLTable.visibility = View.VISIBLE
        }
    }

    private fun setUpEspecies(especies: MutableList<String>) {
        val adapter = ArrayAdapter(applicationContext, R.layout.list_item, especies)
        binding.actvEspecie.setAdapter(adapter)
    }

}