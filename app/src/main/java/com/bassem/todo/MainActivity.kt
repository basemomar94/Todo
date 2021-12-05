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
            show_dialog()

        }


    }

    override fun onResume() {
        super.onResume()
        Getting_data()
    }

    fun Getting_data() {
        var db = Todo_Database.getInstance(this)
        Todo_Database.databaseWriteExecutor.execute {


             adapter = Adpater(

                db.itemsDao().getitmes() as ArrayList<Todo_item>
            )

            runOnUiThread {
                updateUI()

                binding.recycleView.adapter = adapter
                binding.recycleView.layoutManager = LinearLayoutManager(this)
                binding.recycleView.setHasFixedSize(true)
                adapter.setOnitemclick( object:Adpater.onItemclick{
                    override fun onclick(position: Int) {
                        var db = Todo_Database.getInstance(this@MainActivity)
                        Todo_Database.databaseWriteExecutor.execute {
                            var todoItem:Todo_item= Todo_item()
                            db.itemsDao().delete(todoItem)

                        }
                        adapter.notifyItemRemoved(position)

                    }

                })
            }



        }


    }

    fun Delete_all(view: android.view.View) {

        var db = Todo_Database.getInstance(this@MainActivity)
        Todo_Database.databaseWriteExecutor.execute {
            db.itemsDao().delete_all()
        }
        runOnUiThread {
            Getting_data()
            adapter.notifyDataSetChanged()

        }

    }
    fun updateUI(){

        var  boolean:Boolean=false
        var db = Todo_Database.getInstance(this)

            Todo_Database.databaseWriteExecutor.execute {
                boolean=db.itemsDao().getitmes().isNotEmpty()
            }
        Toast.makeText(this,boolean.toString(),Toast.LENGTH_LONG).show()
        if (boolean==true) {


                binding.itemsCard.visibility=View.VISIBLE
                binding.doneTV.visibility=View.GONE
        }

        else {

            binding.itemsCard.visibility=View.GONE
            binding.doneTV.visibility=View.VISIBLE
        }


}
    fun show_dialog(){
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
            if (title.isNotEmpty()){
                val model = Todo_item()
                model.title = title
                model.noteDate = note_date
                todoList.add(model)
                var db = Todo_Database.getInstance(this)
                Todo_Database.databaseWriteExecutor.execute {
                    db.itemsDao().insert_update(model)
                    runOnUiThread {
                        Getting_data()
                        adapter.notifyDataSetChanged()
                        dialog.hide()

                    }

                }

            } else{
                Toast.makeText(this,"please enter a text",Toast.LENGTH_LONG).show()
            }





        }
    }

}