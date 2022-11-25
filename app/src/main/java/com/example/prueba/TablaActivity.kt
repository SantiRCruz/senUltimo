package com.example.prueba

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.prueba.adapters.TableAdapter
import com.example.prueba.core.Constants
import com.example.prueba.databinding.ActivityTablaBinding
import com.example.prueba.models.Alimento
import com.example.prueba.models.Table

class TablaActivity : AppCompatActivity() {
    private val ingredientesList = mutableListOf(
        Alimento(
            1,
            0.0,
            "Maiz amarillo",
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
        ),
        Alimento(
            2,
            0.0,
            "Sorgo",
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
        ),
        Alimento(
            3,
            0.0,
            "Trigo",
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
        ),
        Alimento(
            4,
            0.0,
            "Avena",
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
        ),
        Alimento(
            5,
            0.0,
            "Cebada",
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
        )
    )

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
            "Metn \n%",
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
            "Lisina",
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
            "Metionina",
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
            "100",
            Constants.faseActual.proteina,
            Constants.faseActual.fibra_cruda,
            Constants.faseActual.e_m_ave.toString(),
            Constants.faseActual.calcio,
            Constants.faseActual.fosf_disp,
            Constants.faseActual.lisina,
            Constants.faseActual.metionina,
            Constants.faseActual.met_cis,
        )
    )

    private lateinit var binding: ActivityTablaBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTablaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        this.supportActionBar?.hide()

        createTable()

    }

    private fun createTable() {
        binding.rv.adapter = TableAdapter(list)
        binding.rv.layoutManager = LinearLayoutManager(applicationContext)
    }
}