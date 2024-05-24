package com.example.oublie_pas

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), RecyclerViewEvent {

    private lateinit var navigationHandler: NavigationHandler
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navigationHandler = NavigationHandler(this)
        setupButtons()
        setupRecyclerView() // Appeler la configuration de RecyclerView sans suspend
        lifecycleScope.launch {
            loadData()
        }
    }

    override fun onItemClick(position: Int) {
        Log.d("MainActivity", "Item clicked at position: $position")
        navigationHandler.goToModification(position)
    }

    private fun setupRecyclerView() {
        recyclerView = findViewById(R.id.RecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = RecycleurAdapter(this, emptyList(), applicationContext)
    }

    private suspend fun loadData() {
        val data = getData()
        (recyclerView.adapter as RecycleurAdapter).updateData(data)
    }

    private fun setupButtons() {
        val buttonParametres: Button = findViewById(R.id.Button_Parametres)
        val buttonNouvelObjectif: Button = findViewById(R.id.Button_Rappel)
        val buttonTrier: Button = findViewById(R.id.Button_Trier)
        navigationHandler.setupButtonNavigation(buttonNouvelObjectif, NouvelObjectif::class.java)
        navigationHandler.setupButtonNavigation(buttonParametres, Parametres::class.java)

        buttonTrier.setOnClickListener {
            showSortDialog()
        }
    }

    private fun showSortDialog() {
        val options = arrayOf("Date du rappel", "Date de création", "Statut")
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Trier par")
        builder.setItems(options) { _, which ->
            when (which) {
                0 -> sortByReminderDate()
                1 -> sortByCreationDate()
                2 -> sortByStatus()
            }
        }
        builder.show()
    }

    private fun sortByReminderDate() {
        lifecycleScope.launch {
            val data = getData().sortedBy { it.dateInMillis }
            (recyclerView.adapter as RecycleurAdapter).updateData(data)
        }
    }

    private fun sortByCreationDate() {
        lifecycleScope.launch {
            // Remplacez `creationDateInMillis` par le champ réel de la date de création si nécessaire
            val data = getData().sortedBy { it.creationDateInMillis }
            (recyclerView.adapter as RecycleurAdapter).updateData(data)
        }
    }

    private fun sortByStatus() {
        lifecycleScope.launch {
            val data = getData().sortedBy { it.status }
            (recyclerView.adapter as RecycleurAdapter).updateData(data)
        }
    }

    private suspend fun getData(): List<RoomEntity> {
        val database = AppDatabase.getDatabase(this)
        val data = database.roomDao().getAllRooms()
        Log.d("MainActivity", "Data retrieved: $data")
        return data
    }
}
