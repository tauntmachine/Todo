package com.tauntmachine.todo

import android.app.AlertDialog
import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.tauntmachine.todo.adapter.GroceryAdapter
import com.tauntmachine.todo.model.GroceryEntity
@Suppress("DEPRECATION")
class GroceryActivity : AppCompatActivity() {

    private var rv_grocery: RecyclerView? = null
    private var btn_add_grocery: FloatingActionButton? = null
    private var db_selected: MutableList<GroceryEntity>? = null
    private var adapter_grocery: GroceryAdapter? =null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_grocery)

        var title = intent.getStringExtra("title")
        if ( !title.isNullOrEmpty() ) supportActionBar!!.title = title
        else supportActionBar!!.title = "List"
        rv_grocery = findViewById(R.id.rvGrocery)
        btn_add_grocery = findViewById(R.id.btnAddGrocery)
        db_selected = ArrayList()

        getAllSelected()
        btn_add_grocery!!.setOnClickListener {openDialogue()}

    }

    private fun setRv() {
        rv_grocery!!.setHasFixedSize(true)
        adapter_grocery = GroceryAdapter(this, db_selected!!)
        rv_grocery!!.layoutManager = LinearLayoutManager(this)
        rv_grocery!!.adapter = adapter_grocery
    }

    private fun openDialogue() {
        val factory = LayoutInflater.from(this)
        val add_grocery: View = factory.inflate(R.layout.add_grocery_item, null)
        val groceryDialogue = AlertDialog.Builder(this).create()
        groceryDialogue.setView(add_grocery)
        groceryDialogue.show()
        var edt_addGrocery: EditText = add_grocery.findViewById(R.id.addGroceryTitle)
        var edt_groceryCount: EditText = add_grocery.findViewById(R.id.addGroceryCount)
        add_grocery.findViewById<TextView>(R.id.btn_yes).setOnClickListener {
            if (edt_addGrocery.text.isNullOrEmpty() || edt_groceryCount.text.isNullOrEmpty()) {
                Toast.makeText(this, "Enter Required Fields", Toast.LENGTH_SHORT).show()
            } else {
                addNewGrocery(edt_addGrocery.text.toString(), edt_groceryCount.text.toString())
                groceryDialogue.dismiss()
            }
        }
        add_grocery.findViewById<TextView>(R.id.btn_cancel).setOnClickListener {
            groceryDialogue.dismiss()
        }
    }

    private fun addNewGrocery(title: String, count: String) {
        val db = Room.databaseBuilder(
            this,
            ListDatabase::class.java, "list_database.db"
        ).build()

        class DbAsyncTask : AsyncTask<Void, Void, String>() {

            override fun doInBackground(vararg p0: Void?): String? {
                var grocery_entity = GroceryEntity()
                grocery_entity.name = title
                grocery_entity.count = count
                grocery_entity.status = "Pending"
                grocery_entity.f_id = intent.getIntExtra("list_id", 1)

                db.GroceryDao()!!.insert(grocery_entity)
                db_selected = db.GroceryDao()!!.getAllSelectedGrocery(intent.getIntExtra("list_id", 1))
                return "Added"
            }

            override fun onPostExecute(result: String?) {
                super.onPostExecute(result)
                getAllSelected()
            }
        }

        DbAsyncTask().execute()
    }

    fun getAllSelected() {
        val db = Room.databaseBuilder(
            this,
            ListDatabase::class.java, "list_database.db"
        ).build()

        class DbAsyncTask : AsyncTask<Void, Void, String>() {

            override fun doInBackground(vararg p0: Void?): String? {
                db_selected = db.GroceryDao()!!.getAllSelectedGrocery(intent.getIntExtra("list_id", 1))

                return "Added"
            }

            override fun onPostExecute(result: String?) {
                super.onPostExecute(result)
                if (!db_selected.isNullOrEmpty()) setRv()
            }
        }

        DbAsyncTask().execute()
    }
}