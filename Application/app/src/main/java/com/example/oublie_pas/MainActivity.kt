package com.example.oublie_pas

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.oublie_pas.RecycleurAdapter
import com.example.oublie_pas.RecyclerViewEvent


class MainActivity : AppCompatActivity(), RecyclerViewEvent {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupButtons()
    }

    override fun onItemClick(position: Int) {
        setupRecycleur()
    }

    private fun setupButtons() {
        val navigationHandler = NavigationHandler(this)
        val buttonParametres: Button = findViewById(R.id.Button_Parametres)

        navigationHandler.setupButtonNavigation(buttonParametres, Parametres::class.java)
    }

    private fun setupRecycleur() {
        val navigationHandler = NavigationHandler(this)

        navigationHandler.goTo(ModifierObjectif::class.java)
    }
}