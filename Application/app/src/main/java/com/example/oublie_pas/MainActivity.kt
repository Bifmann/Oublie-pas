package com.example.oublie_pas

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), RecyclerViewEvent {

    private lateinit var navigationHandler: NavigationHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navigationHandler = NavigationHandler(this)
        setupButtons()
        lifecycleScope.launch {
            setupRecyclerView()
        }
    }

    override fun onItemClick(position: Int) {
        navigationHandler.goTo(ModifierObjectif::class.java)
    }

    private suspend fun setupRecyclerView() {
        val recyclerView: RecyclerView = findViewById(R.id.RecyclerView)
        val data = getData()
        recyclerView.adapter = RecycleurAdapter(this, data)
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun setupButtons() {
        val buttonParametres: Button = findViewById(R.id.Button_Parametres)
        val buttonNouvelObjectif: Button = findViewById(R.id.Button_Rappel)
        navigationHandler.setupButtonNavigation(buttonNouvelObjectif, NouvelObjectif::class.java)
        navigationHandler.setupButtonNavigation(buttonParametres, Parametres::class.java)
    }

    private suspend fun getData(): List<RoomEntity> {
        val database = AppDatabase.getDatabase(this)
        return database.roomDao().getAllRooms()
    }
}
