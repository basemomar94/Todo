package com.bassem.todo

import android.widget.Button
import android.widget.ImageView
import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import androidx.room.PrimaryKey


@Entity(tableName = "todo_database")
class Todo_item

    {@NonNull
var title :String=""
    @NonNull
    var priority : String="1"
    @NonNull
    @PrimaryKey(autoGenerate = true)
    var id : Int=0

    @Ignore
    var check : ImageView?=null



}
