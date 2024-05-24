package com.example.oublie_pas

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class RecycleurAdapter(
    private val listener: RecyclerViewEvent,
    private var liste: List<RoomEntity>,
    private val context: Context
) : RecyclerView.Adapter<RecycleurAdapter.ItemViewHolder>() {

    // ViewHolder qui contient la référence à la vue de chaque élément de la liste
    inner class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        val objectifName: TextView = view.findViewById(R.id.textview_Titre) // Référence au TextView
        val dateText: TextView = view.findViewById(R.id.textview_Date) // Référence au TextView pour la date
        val button: Button = view.findViewById(R.id.button) // Référence au Button

        init {
            view.setOnClickListener(this) // Définir le gestionnaire de clic pour la vue du ViewHolder
            button.setOnClickListener {
                // Définir un gestionnaire de clic séparé pour le bouton
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(position)
                }
            }
        }

        override fun onClick(v: View?) {
            // Gestion des clics sur l'élément entier
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClick(position) // Appel de l'interface lorsque l'élément est cliqué
            }
        }
    }

    // Méthode pour mettre à jour les données de l'adaptateur
    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newListe: List<RoomEntity>) {
        liste = newListe
        notifyDataSetChanged()
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

        CoroutineScope(Dispatchers.Main).launch {
            editStatus(item)
            val status = item.status
            when (status) {
                "urgent" -> holder.dateText.setTextColor(Color.RED)
                "actif" -> holder.dateText.setTextColor(Color.GREEN)
                "done" -> holder.dateText.setTextColor(Color.BLUE)
                "no notif" -> holder.dateText.setTextColor(Color.GRAY)
            }
        }
    }

    // Obtenir la taille de la liste des données
    override fun getItemCount(): Int {
        return liste.size
    }

    private suspend fun editStatus(item: RoomEntity) {
        val currentDateInMillis = System.currentTimeMillis()
        val oneDayInMillis = 86400000
        val status: String

        if (item.dateInMillis >= currentDateInMillis) {
            // L'élément est dans le futur ou maintenant
            status = "actif"
        } else {
            // L'élément est dans le passé
            status = if (item.status == "actif" || item.status == "urgent" || item.status == "done") {
                if (item.dateInMillis >= (currentDateInMillis - oneDayInMillis)) {
                    // Moins d'un jour de retard
                    "urgent"
                } else {
                    // Plus d'un jour de retard
                    "done"
                }
            } else {
                // Statut n'est pas "actif" ou "urgent"
                "no notif"
                // TODO: Implémenter la logique pour retirer l'élément du système de notification
            }
        }

        withContext(Dispatchers.IO) {
            val database = AppDatabase.getDatabase(context)
            item.status = status
            database.roomDao().updateRoom(item)
        }
    }
}

// Interface pour gérer les événements de clic sur les éléments du RecyclerView
interface RecyclerViewEvent {
    fun onItemClick(position: Int)
}
