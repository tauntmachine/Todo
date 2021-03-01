package com.tauntmachine.todo

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.tauntmachine.todo.dao.GroceryDao
import com.tauntmachine.todo.dao.ListDao
import com.tauntmachine.todo.model.GroceryEntity
import com.tauntmachine.todo.model.ListEntity


@Database(entities = [ListEntity::class, GroceryEntity::class], version = 1)
abstract class ListDatabase : RoomDatabase() {
    abstract fun ListDao(): ListDao?
    abstract fun GroceryDao(): GroceryDao?

    companion object {
        private var instance: ListDatabase? = null

        @Synchronized
        fun getInstance(context: Context): ListDatabase? {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    ListDatabase::class.java, "list_database.db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
            }
            return instance
        }
    }
}