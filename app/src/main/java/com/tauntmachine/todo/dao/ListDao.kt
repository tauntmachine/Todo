package com.tauntmachine.todo.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.tauntmachine.todo.model.ListEntity

@Dao
interface ListDao {

    @Insert
    fun insert( listEntity: ListEntity )

    @Query("INSERT INTO todo_item (title, list_status) VALUES(:title, :status) ")
    fun insert( title: String,  status: String)

    @Query(value = "Select * from todo_item order by created_at DESC")
    fun getAllList() : MutableList<ListEntity>

    @Query(value = "Select * from todo_item where list_status = :status order by created_at DESC")
    fun getPending( status: String) : MutableList<ListEntity>

    @Query("UPDATE todo_item SET list_status = :status WHERE id = :id")
    fun updateStatus( id: Int, status: String )

    @Delete
    fun delete( listEntity: ListEntity )
}