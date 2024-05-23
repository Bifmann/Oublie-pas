package com.example.oublie_pas

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RecycleurAdapterCategorie(
    private val listener: OnButtonClickListener,
    context: Context
) : RecyclerView.Adapter<RecycleurAdapterCategorie.MyViewHolder>() {

    private val preferencesManager = PreferencesManager(context)
    private val stringList = preferencesManager.stringList

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val button: Button = itemView.findViewById(R.id.button_RecycleurCategorie)
        val texte: TextView = itemView.findViewById(R.id.textview_Titre)

        init {
            button.setOnClickListener {
                listener.onButtonClick(adapterPosition)
            }
        }
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = stringList[position]
        holder.texte.text = item
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_recycleurcategorie, parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return stringList.size
    }
}

interface OnButtonClickListener {
    fun onButtonClick(position: Int)
}
