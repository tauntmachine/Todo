package com.tauntmachine.todo.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "todo_item")
class ListEntity {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    @ColumnInfo(name = "created_at", defaultValue = "CURRENT_TIMESTAMP")
    var time_stamp: String? = ""

    @ColumnInfo(name = "title")
    var title: String = ""

    @ColumnInfo(name = "list_status")
    var status: String = ""
}