package com.example.oublie_pas

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity(), RecyclerViewEvent {

    private lateinit var navigationHandler: NavigationHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navigationHandler = NavigationHandler(this)
        setupButtons()
        setupRecyclerView()
    }

    override fun onItemClick(position: Int) {
        navigationHandler.goTo(ModifierObjectif::class.java)
    }

    private fun setupRecyclerView() {
        val recyclerView: RecyclerView = findViewById(R.id.RecyclerView)
        recyclerView.adapter = RecycleurAdapter(this)
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun setupButtons() {
        val buttonParametres: Button = findViewById(R.id.Button_Parametres)
        val buttonNouvelObjectif: Button = findViewById(R.id.Button_Rappel)
        navigationHandler.setupButtonNavigation(buttonNouvelObjectif, NouvelObjectif::class.java)
        navigationHandler.setupButtonNavigation(buttonParametres, Parametres::class.java)
    }
}
