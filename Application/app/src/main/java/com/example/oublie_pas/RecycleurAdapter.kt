package com.example.oublie_pas

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class RecycleurAdapter(
    private val listener: RecyclerViewEvent // L'interface pour gérer les clics
) : RecyclerView.Adapter<RecycleurAdapter.ItemViewHolder>() {

    // ViewHolder qui contient la référence à la vue de chaque élément de la liste
    inner class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        val objectifName: TextView = view.findViewById(R.id.textview_Titre) // Référence au TextView

        init {
            view.setOnClickListener(this) // Définir le gestionnaire de clic pour la vue du ViewHolder
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClick(position) // Appel de l'interface lorsque l'élément est cliqué
            }
        }
    }

    // Création de ViewHolder pour chaque élément
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_recycleur, parent, false)
        return ItemViewHolder(view)
    }

    // Associer les données avec le ViewHolder à une position spécifique
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        // TODO: Ajouter les données à l'affichage
    }

    // Obtenir la taille de la liste des données
    override fun getItemCount(): Int {
        // TODO: Retourner la taille de la liste des données
        return 0 // Temporairement mis à 0 jusqu'à ce que TODO soit complété
    }
}

// Interface pour gérer les événements de clic sur les éléments du RecyclerView
interface RecyclerViewEvent {
    fun onItemClick(position: Int)
}
