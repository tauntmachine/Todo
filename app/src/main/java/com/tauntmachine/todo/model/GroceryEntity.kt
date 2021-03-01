package com.tauntmachine.todo.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "grocery")
class GroceryEntity {
    @PrimaryKey( autoGenerate = true )
    var id: Int = 0

    @ColumnInfo( name = "name" )
    var name: String = ""

    @ColumnInfo( name = "count" )
    var count: String = ""

    @ColumnInfo( name = "status" )
    var status: String = ""

    @ColumnInfo( name = "f_id")
    var f_id: Int = 0
}