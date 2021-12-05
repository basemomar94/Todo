package com.bassem.todo

import android.app.Dialog
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

    lateinit var adapter: Adpater
    var check: ImageView? = null
    var priority: Int? = null


    val todoList = ArrayList<Todo_item>()
    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        updateUI()
        Getting_data()

        check = findViewById(R.id.checkbutton)

        binding.floatingActionButton.setOnClickListener {
            show_dialog()


        }
        binding.addRcycle.setOnClickListener {
            show_dialog()
        }


    }

    override fun onResume() {
        super.onResume()
        Getting_data()
      // updateUI()
    }

    fun Getting_data() {
        var db = Todo_Database.getInstance(this)
        Todo_Database.databaseWriteExecutor.execute {


            adapter = Adpater(

                db.itemsDao().getitmes() as ArrayList<Todo_item>
            )

            runOnUiThread {
                binding.recycleView.adapter = adapter
                binding.recycleView.layoutManager = LinearLayoutManager(this)
                binding.recycleView.setHasFixedSize(true)
                adapter.setOnitemclick(object : Adpater.onItemclick {
                    override fun onclick(position: Int) {
                      Delete_item(position)
                    }

                })
            }


        }


    }

    fun Delete_all(view: android.view.View) {

        var db = Todo_Database.getInstance(this@MainActivity)
        Todo_Database.databaseWriteExecutor.execute {
            db.itemsDao().delete_all()

            runOnUiThread {
                Getting_data()
                updateUI()
                adapter.notifyDataSetChanged()

            }
        }

    }
    fun Delete_item (position : Int){
        println("============================$position")
        var db = Todo_Database.getInstance(this@MainActivity)
        Todo_Database.databaseWriteExecutor.execute {
            var todoItem: Todo_item = Todo_item()
            db.itemsDao().delete(todoItem)

        }
        adapter.notifyItemRemoved(position)
    }

    fun updateUI() {

        var boolean: Boolean = false
        var test : Int?=null
        var db = Todo_Database.getInstance(this)

        Todo_Database.databaseWriteExecutor.execute {
            boolean = db.itemsDao().getitmes().isNotEmpty()
            test=db.itemsDao().getitmes().size
            println("=========================$test")
            println("=========================$boolean")

            runOnUiThread {
                if (boolean) {




                    binding.itemsCard.visibility = View.VISIBLE
                    binding.doneTV.visibility = View.GONE
                } else {

                    binding.itemsCard.visibility = View.GONE
                    binding.doneTV.visibility = View.VISIBLE
                }
            }

        }




    }

    fun show_dialog() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setTitle("write your todo item")
        dialog.setCancelable(true)
        val bind = DialogueBinding.inflate(layoutInflater)
        dialog.setContentView(bind.root)
        dialog.show()
        bind.addBu.setOnClickListener {

            var title: String = bind.titleEd.text.toString()
            var priority: String = bind.dateEd.text.toString()
            if (title.isNotEmpty()) {
                val model = Todo_item()
                model.title = title
                model.priority = priority
                todoList.add(model)
                var db = Todo_Database.getInstance(this)
                Todo_Database.databaseWriteExecutor.execute {
                    db.itemsDao().insert_update(model)
                    runOnUiThread {
                        Getting_data()
                        updateUI()

                        adapter.notifyDataSetChanged()
                        dialog.hide()

                    }

                }

            } else {
                Toast.makeText(this, "please enter a text", Toast.LENGTH_LONG).show()
            }


        }
    }

}