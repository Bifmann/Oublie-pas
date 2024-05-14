package com.example.oublie_pas

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class NouvelObjectif : AppCompatActivity() , OnButtonClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nouvelobjectif)
        setupButtons()
        setupRecyclerView()
    }

    override fun onButtonClick(position: Int) {
        TODO("Toggle on/off changement a faire quand database terminée")
    }

    private fun setupRecyclerView() {
        val recyclerView: RecyclerView = findViewById(R.id.RecyclerViewCategorie)
        recyclerView.adapter = RecycleurAdapterCategorie(this)
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun setupButtons() {
        val navigationHandler = NavigationHandler(this)
        val buttonParametres: Button = findViewById(R.id.Button_Sauvegarder)

        buttonParametres.setOnClickListener {
            navigationHandler.goTo(MainActivity::class.java)
        }
    }

}