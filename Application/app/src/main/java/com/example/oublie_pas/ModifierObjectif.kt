package com.example.oublie_pas

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ModifierObjectif : AppCompatActivity(), OnButtonClickListener {

    private val id by lazy { intent.getIntExtra("ID_OBJECTIF", -1 ) + 1}
    private val database by lazy { AppDatabase.getDatabase(this) }
    private lateinit var editTextTitre: EditText
    private lateinit var editTextDescription: EditText
    private lateinit var editTextDateHeure: EditText
    private lateinit var buttonSauvegarder: Button
    private lateinit var buttonDelete: Button
    private var navigationHandler = NavigationHandler(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_modifierobjectif)

        // Initialisation des vues
        editTextTitre = findViewById(R.id.editText_Titre)
        editTextDescription = findViewById(R.id.editText_Description)
        editTextDateHeure = findViewById(R.id.editText_DateHeure)
        buttonSauvegarder = findViewById(R.id.Button_Sauvegarder)
        buttonDelete = findViewById(R.id.Button_Delete)


        setupRecyclerView()
        setupButtons()

        // Lancer la fonction de remplissage des champs
        lifecycleScope.launch {
            remplissageChamp()
        }
    }

    override fun onButtonClick(position: Int) {
        // TODO: Toggle on/off changement à faire quand database terminée
    }

    private fun setupRecyclerView() {
        val recyclerView: RecyclerView = findViewById(R.id.RecyclerViewCategorie)
        recyclerView.adapter = RecycleurAdapterCategorie(this, applicationContext)
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun setupButtons() {
        buttonSauvegarder.setOnClickListener {
            lifecycleScope.launch {
                updateRoom()
                navigationHandler.goTo(MainActivity::class.java)
            }
        }
        buttonDelete.setOnClickListener {
            lifecycleScope.launch {
                database.roomDao().deleteRoomById(id)
                navigationHandler.goTo(MainActivity::class.java)
            }
        }
    }

    fun dateMillisToString(millis: Long): String {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        val date = Date(millis)
        return dateFormat.format(date)
    }

    private suspend fun remplissageChamp() {
        val data = getData()
        data?.let {
            withContext(Dispatchers.Main) {
                editTextTitre.setText(it.titre)
                editTextDescription.setText(it.description)
                editTextDateHeure.setText(dateMillisToString(it.dateInMillis))
            }
        }
    }

    private suspend fun getData(): RoomEntity? {
        return withContext(Dispatchers.IO) {
            database.roomDao().getRoomById(id)
        }
    }

    private suspend fun updateRoom() {
        val data = getData()
        val titre = editTextTitre.text.toString()
        val description = editTextDescription.text.toString()
        val dateInMillis = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).parse(editTextDateHeure.text.toString()).time
        val creationDateInMillis = data!!.creationDateInMillis

        val room = RoomEntity(
            id = id,
            titre = titre,
            description = description,
            true,
            dateInMillis = dateInMillis,
            status = "No",
            tags = listOf("Joshua", "deuxième joshua"),
            creationDateInMillis = creationDateInMillis
        )

        withContext(Dispatchers.IO) {
            database.roomDao().updateRoom(room)
        }
    }

    private fun updateNotif() {
        TODO()
    }
}
