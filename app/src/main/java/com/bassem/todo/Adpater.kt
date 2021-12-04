package com.bassem.todo

import android.content.Context
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView


class Adpater (private val itemsList : ArrayList<Todo_item>) : RecyclerView.Adapter<Adpater.AdapterViewHolder>(){

    lateinit var mlistener : onItemclick
    interface onItemclick {
        fun onclick (position: Int)
    }
    fun setOnitemclick (listner : onItemclick){
     mlistener=listner

    }





    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterViewHolder {

        val v = LayoutInflater.from(parent.context).inflate(R.layout.item,parent,false)
        return AdapterViewHolder(v,mlistener)
    }

    override fun onBindViewHolder(holder: AdapterViewHolder, position: Int) {

        val currentitem = itemsList[position]
        holder.title.text=currentitem.title
        holder.note_date.text=currentitem.noteDate
        holder.check.setImageResource(R.drawable.uncheck)


    }


    override fun getItemCount()=itemsList.size

    class AdapterViewHolder ( itemView : View, listener : onItemclick) : RecyclerView.ViewHolder (itemView){
        var title : TextView = itemView.findViewById(R.id.title)
        var note_date : TextView =itemView.findViewById(R.id.date_added)
        var check : ImageView = itemView.findViewById(R.id.checkbutton)
        init {
            check.setOnClickListener {
                listener.onclick(adapterPosition)
                check.setImageResource(R.drawable.check)
                title.paintFlags= Paint.STRIKE_THRU_TEXT_FLAG
            }
        }


}}
