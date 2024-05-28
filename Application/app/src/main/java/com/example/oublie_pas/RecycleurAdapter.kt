package com.example.oublie_pas

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.util.Log
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
        val notificationHelper = NotificationHelper(context)

        init {
            view.setOnClickListener(this) // Définir le gestionnaire de clic pour la vue du ViewHolder
            button.setOnClickListener { buttonView ->
                // Log message for button click
                Log.d("RecycleurAdapter", "Button clicked at position: $adapterPosition")

                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val item = liste[position]
                    // Log message for item status
                    Log.d("RecycleurAdapter", "Current status: ${item.status}")
                    // Logique pour changer le statut de l'élément
                    if (item.status == "actif" || item.status == "urgent") {
                        item.status = "no notif"
                        Log.d("RecycleurAdapter", "Status changed to no notif")
                        notificationHelper.unscheduleNotification(item.id)
                        Log.d("RecycleurAdapter", "Notification unscheduled for item id: ${item.id}")
                    } else if (item.status == "no notif") {
                        item.status = "actif"
                        Log.d("RecycleurAdapter", "Status changed to actif")
                        notificationHelper.scheduleNotification(
                            id = item.id,
                            title = item.titre,
                            content = item.description,
                            timeInMillis = item.dateInMillis,
                            context
                        )
                        Log.d("RecycleurAdapter", "Notification scheduled for item id: ${item.id}")
                    }

                    // Mettre à jour l'élément dans la base de données et notifier l'adaptateur
                    CoroutineScope(Dispatchers.IO).launch {
                        val database = AppDatabase.getDatabase(context)
                        database.roomDao().updateRoom(item)
                        withContext(Dispatchers.Main) {
                            notifyItemChanged(position)
                            // Log message for notifying item change
                            Log.d("RecycleurAdapter", "Item changed at position: $position")
                        }
                    }
                } else {
                    // Log message if position is not valid
                    Log.d("RecycleurAdapter", "Invalid position: $position")
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

    private suspend fun editStatus(item: RoomEntity): String {
        val currentDateInMillis = System.currentTimeMillis()
        val oneDayInMillis = 86400000
        var status = item.status // Utiliser la variable `var` pour permettre la modification

        if (item.status != "no notif") {
            if (item.dateInMillis >= currentDateInMillis) {
                // L'élément est dans le futur ou maintenant
                status = "actif"
                if (item.dateInMillis <= (currentDateInMillis + oneDayInMillis)) {
                    // Moins d'un jour avant l'alarme
                    status = "urgent"
                }
            } else {
                // L'élément est dans le passé
                status = "done"
            }
        }

        withContext(Dispatchers.IO) {
            val database = AppDatabase.getDatabase(context)
            item.status = status
            database.roomDao().updateRoom(item)
        }

        return status
    }

}

// Interface pour gérer les événements de clic sur les éléments du RecyclerView
interface RecyclerViewEvent {
    fun onItemClick(position: Int)
}
