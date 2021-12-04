package com.bassem.todo

import android.app.Dialog
import android.icu.lang.UCharacter.GraphemeClusterBreak.L
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.bassem.todo.databinding.ActivityMainBinding
import com.bassem.todo.databinding.DialogueBinding


class MainActivity : AppCompatActivity() {

    val FILE_NAME: String = "notes"

    lateinit var  adapter : Adpater
    var check : ImageView?=null


    val todoList = ArrayList<Todo_item>()
    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Getting_data()
       check =findViewById(R.id.checkbutton)

        binding.floatingActionButton.setOnClickListener {
            val dialog = Dialog(this)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setTitle("write your todo item")



            dialog.setCancelable(true)
            val bind = DialogueBinding.inflate(layoutInflater)
            dialog.setContentView(bind.root)
            dialog.show()
            bind.addBu.setOnClickListener {
                var title: String = bind.titleEd.text.toString()
                var note_date: String = bind.dateEd.text.toString()
                val model = Todo_item()
                model.title = title
                model.noteDate = note_date
                todoList.add(model)


                if (todoList.isNotEmpty()) {
                    binding.itemsCard.visibility = View.VISIBLE
                    binding.doneTV.visibility = View.GONE
                    println(todoList.isNotEmpty())

                }

                var db = Todo_Database.getInstance(this)
                Todo_Database.databaseWriteExecutor.execute {
                    db.itemsDao().insert_update(model)

                }
                Getting_data()

                dialog.hide()


            }
        }


    }

    override fun onResume() {
        super.onResume()
        Getting_data()
    }

    fun Getting_data() {
        var db = Todo_Database.getInstance(this)
        Todo_Database.databaseWriteExecutor.execute {
            if (db.itemsDao().getitmes().isNotEmpty()) {
                binding.itemsCard.visibility=View.VISIBLE
                binding.doneTV.visibility=View.GONE}

             adapter = Adpater(

                db.itemsDao().getitmes() as ArrayList<Todo_item>
            )

            runOnUiThread {   binding.recycleView.adapter = adapter
                binding.recycleView.layoutManager = LinearLayoutManager(this)
                binding.recycleView.setHasFixedSize(true)
                adapter.setOnitemclick( object:Adpater.onItemclick{
                    override fun onclick(position: Int) {
                        check?.setImageResource(R.drawable.check)
                        Toast.makeText(this@MainActivity,"gi",Toast.LENGTH_LONG).show()

                    }

                })
            }



        }


    }
}