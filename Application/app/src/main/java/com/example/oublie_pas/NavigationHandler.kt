package com.example.oublie_pas

import android.content.Context
import android.content.Intent
import android.widget.Button


// Définition de la classe NavigationHandler pour gérer la navigation entre les activités de l'application.
class NavigationHandler(private val context: Context) {

    // Méthode pour configurer la navigation à partir d'un bouton vers une activité spécifiée.
    fun setupButtonNavigation(button: Button, destinationClass: Class<*>) {
        // Définit un écouteur d'événements de clic sur le bouton.
        button.setOnClickListener {
            // Crée une intention pour démarrer l'activité de destination.
            val intent = Intent(context, destinationClass)
            // Démarre l'activité spécifiée.
            context.startActivity(intent)
        }
    }

    fun goTo(destinationClass: Class<*>){
        // Crée une intention pour démarrer l'activité de destination.
        val intent = Intent(context, destinationClass)
        // Démarre l'activité spécifiée.
        context.startActivity(intent)
    }
}
