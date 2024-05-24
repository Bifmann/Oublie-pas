package com.example.oublie_pas

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class NavigationHandler(private val context: Context) {

    fun setupButtonNavigation(button: Button, destinationClass: Class<*>) {
        button.setOnClickListener {
            val intent = Intent(context, destinationClass)
            if (context !is AppCompatActivity) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }
    }

    fun goTo(destinationClass: Class<*>) {
        val intent = Intent(context, destinationClass)
        if (context !is AppCompatActivity) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    }

    fun goToModification(position: Int) {
        Log.d("NavigationHandler", "Navigating to ModifierObjectif with ID: $position")
        val intent = Intent(context, ModifierObjectif::class.java).apply {
            putExtra("ID_OBJECTIF", position)
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            if (context !is AppCompatActivity) {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
        }
        context.startActivity(intent)
    }
}
