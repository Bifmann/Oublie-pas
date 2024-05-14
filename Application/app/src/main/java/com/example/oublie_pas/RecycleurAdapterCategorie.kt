package com.example.oublie_pas

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class RecycleurAdapterCategorie(private val listener: OnButtonClickListener) :
    RecyclerView.Adapter<RecycleurAdapterCategorie.MyViewHolder>() {

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val button: Button = itemView.findViewById(R.id.button_RecycleurCategorie)
        val titleTextView: TextView = itemView.findViewById(R.id.textview_Titre)

        init {
            button.setOnClickListener {
                listener.onButtonClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_recycleurcategorie, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

    }

    override fun getItemCount(): Int {
        // TODO: Retourner la taille de la liste des données
        return 0 // Temporairement mis à 0 jusqu'à ce que TODO soit complété
    }
}


interface OnButtonClickListener {
    fun onButtonClick(position: Int)
}
