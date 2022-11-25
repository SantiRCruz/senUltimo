package com.example.prueba.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.recyclerview.widget.RecyclerView
import com.example.prueba.R
import com.example.prueba.databinding.ItemProfileBinding
import com.example.prueba.databinding.ItemTableBinding
import com.example.prueba.models.Alimento
import com.example.prueba.models.Table
import com.example.prueba.models.preparacion.PreparacionesUser

class PreparacionesAdapter (
    private val list: List<PreparacionesUser>,
) :
    RecyclerView.Adapter<PreparacionesAdapter.PreparacionesViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PreparacionesViewHolder {
        val itemBinding =
            ItemProfileBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PreparacionesViewHolder(itemBinding, parent.context)
    }

    override fun onBindViewHolder(holder: PreparacionesViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size

    inner class PreparacionesViewHolder(
        private val binding: ItemProfileBinding,
        private val context: Context
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: PreparacionesUser) {
            binding.tvName.text = "Preparacion ${item.RequerimientosAnimale.nombre_fase}"
        }
    }
}
