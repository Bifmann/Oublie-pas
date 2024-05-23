package com.example.oublie_pas

import android.content.Context
import android.content.SharedPreferences

class PreferencesManager(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

    var stringList: List<String>
        get() {
            val serializedList = sharedPreferences.getString("string_list", null)
            return serializedList?.split(",")?.map { it.trim() } ?: emptyList()
        }
        set(value) {
            val editor = sharedPreferences.edit()
            val serializedList = value.joinToString(",")
            editor.putString("string_list", serializedList)
            editor.apply()
        }

    fun addStringToList(item: String) {
        val currentList = stringList.toMutableList()
        currentList.add(item)
        stringList = currentList
    }

    fun removeStringFromList(item: String) {
        val currentList = stringList.toMutableList()
        currentList.remove(item)
        stringList = currentList
    }
}
