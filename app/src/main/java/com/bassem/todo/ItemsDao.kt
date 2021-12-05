package com.bassem.todo

import androidx.room.*


@Dao
interface ItemsDao {

@Insert(onConflict = OnConflictStrategy.REPLACE)
fun insert_update (todoItem: Todo_item)

@Delete
fun delete (todoItem: Todo_item)
@Query("Delete From todo_database")
fun delete_all ()

@Query("select * from todo_database")
fun getitmes () : List<Todo_item>

}