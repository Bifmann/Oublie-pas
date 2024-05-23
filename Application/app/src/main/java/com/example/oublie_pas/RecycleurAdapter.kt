package com.example.oublie_pas

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class RecycleurAdapter(
    private val listener: RecyclerViewEvent,
    private val liste: List<RoomEntity>
) : RecyclerView.Adapter<RecycleurAdapter.ItemViewHolder>() {

    // ViewHolder qui contient la référence à la vue de chaque élément de la liste
    inner class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        val objectifName: TextView = view.findViewById(R.id.textview_Titre) // Référence au TextView
        val dateText: TextView = view.findViewById(R.id.textview_Date) // Référence au TextView pour la date
        val button: Button = view.findViewById(R.id.button) // Référence au Button

        init {
            view.setOnClickListener(this) // Définir le gestionnaire de clic pour la vue du ViewHolder
            button.setOnClickListener(this) // Définir le gestionnaire de clic pour le bouton
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
        val item = liste[position]
        holder.objectifName.text = item.titre
        holder.dateText.text = item.dateInMillis.toString()

        val date = Date(item.dateInMillis)
        val format = SimpleDateFormat("d/M/yyyy", Locale.getDefault())
        holder.dateText.text = format.format(date)
    }

    // Obtenir la taille de la liste des données
    override fun getItemCount(): Int {
        return liste.size
    }
}

// Interface pour gérer les événements de clic sur les éléments du RecyclerView
interface RecyclerViewEvent {
    fun onItemClick(position: Int)
}
