package com.example.oublie_pas

import android.app.AlertDialog
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.coroutines.resume

class NouvelObjectif : AppCompatActivity(), OnButtonClickListener {

    private val database by lazy { AppDatabase.getDatabase(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nouvelobjectif)
        setupButtons()
        setupRecyclerView()
    }

    override fun onButtonClick(position: Int) {
        TODO("Toggle on/off changement à faire quand la base de données est terminée")
    }

    private fun setupRecyclerView() {
        val recyclerView: RecyclerView = findViewById(R.id.RecyclerViewCategorie)
        recyclerView.adapter = RecycleurAdapterCategorie(this, applicationContext)
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun setupButtons() {
        val navigationHandler = NavigationHandler(this)
        val buttonSauvegarder: Button = findViewById(R.id.Button_Sauvegarder)
        buttonSauvegarder.setOnClickListener {
            lifecycleScope.launch {
                addObjectif()
                addNotification()
                navigationHandler.goTo(MainActivity::class.java)
            }
        }
    }

    private suspend fun addObjectif() {
        val titre: String = findViewById<EditText>(R.id.editText_Titre).text.toString()
        val desc: String = findViewById<EditText>(R.id.editText_Description).text.toString()
        val time: String = findViewById<EditText>(R.id.editText_DateHeure).text.toString()
        val timeInMillis: Long = getTimeInMillisFromString(time)


        //TODO "si format pas juste mettre un warning"

        //TODO "if bouton rouge ne pas mettre de rappel"
        //TODO "si RecyclerView cliqué, ajouter à la liste des tags"

        val newRoom = RoomEntity(
            titre = titre,
            description = desc,
            toggleRappel = true,
            dateInMillis = timeInMillis,
            status = "active",
            tags = listOf("Joshua", "deuxième joshua"),
            creationDateInMillis = System.currentTimeMillis()
        )

        withContext(Dispatchers.IO) {
            database.roomDao().insert(newRoom)
        }
    }

    private suspend fun addNotification() {
        val notificationHelper = NotificationHelper(applicationContext)
        val data = getData()
        if (data != null) {
            notificationHelper.scheduleNotification(
                id = data.id,
                title = data.titre,
                content = data.description,
                timeInMillis = data.dateInMillis,
                applicationContext
            )
        }
        if (data != null) {
            showAlert(data.dateInMillis, data.titre, data.description)
        }
    }

    private suspend fun getData(): RoomEntity? {
        return withContext(Dispatchers.IO) {
            database.roomDao().getLastRoom()
        }
    }

    fun getTimeInMillisFromString(dateTimeString: String): Long {
        val format = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        val date = format.parse(dateTimeString)
        return date?.time ?: 0L
    }

    private suspend fun showAlert(time: Long, title: String, message: String) {
        suspendCancellableCoroutine<Unit> { continuation ->
            val date = Date(time)
            val dateFormat = android.text.format.DateFormat.getLongDateFormat(applicationContext)
            val timeFormat = android.text.format.DateFormat.getTimeFormat(applicationContext)

            AlertDialog.Builder(this)
                .setTitle("Notification Scheduled")
                .setMessage(
                    "Title: $title\nMessage: $message\nAt: ${dateFormat.format(date)} ${timeFormat.format(date)}"
                )
                .setPositiveButton("Okay") { _, _ ->
                    continuation.resume(Unit)
                }
                .setOnCancelListener {
                    continuation.resume(Unit)
                }
                .show()
        }
    }
}
