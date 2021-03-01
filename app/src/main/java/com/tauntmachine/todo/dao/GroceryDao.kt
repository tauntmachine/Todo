package com.tauntmachine.todo.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.tauntmachine.todo.model.GroceryEntity

@Dao
interface GroceryDao {
    @Insert
    fun insert( groceryEntity: GroceryEntity)

    @Query(value = "Select * from grocery")
    fun getAllGrocery() : MutableList<GroceryEntity>

    @Query(value = "Select * from grocery where f_id = :id")
    fun getAllSelectedGrocery(id: Int) : MutableList<GroceryEntity>

    @Query(value = "Select * from grocery where status = :status")
    fun getGroceryByStatus( status: String) : MutableList<GroceryEntity>

    @Query("UPDATE grocery SET status=:status WHERE id = :id")
    fun update( id: Int, status: String )

    @Delete
    fun delete( groceryEntity: GroceryEntity )

    @Query("DELETE FROM grocery WHERE f_id = :id")
    fun deleteItemById(id: Int)
}