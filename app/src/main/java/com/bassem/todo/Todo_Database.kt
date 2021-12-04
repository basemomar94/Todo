package com.bassem.todo

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import java.util.concurrent.Executors

private const val  DATABASE_NAME= "todo_database"


@Database (entities = [Todo_item::class], version = 1, exportSchema = false)
abstract class Todo_Database : RoomDatabase() {

  abstract fun itemsDao () : ItemsDao

  companion object {
      @Volatile
      private var INSTANCE : Todo_Database?=null
      val databaseWriteExecutor = Executors.newFixedThreadPool(4)

      fun getInstance (context: Context) : Todo_Database {

          val tempinstance = INSTANCE
          if (tempinstance!=null){
              return tempinstance
          }

          synchronized(this){
              val instance = Room.databaseBuilder(context.applicationContext,Todo_Database::class.java,
                  DATABASE_NAME).build()
              INSTANCE=instance
              return instance
          }


      }



  }
}