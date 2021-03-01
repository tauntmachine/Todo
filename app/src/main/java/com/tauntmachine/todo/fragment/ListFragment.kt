package com.tauntmachine.todo.fragment

import android.app.AlertDialog
import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.tauntmachine.todo.ListDatabase
import com.tauntmachine.todo.R
import com.tauntmachine.todo.adapter.ListAdapter
import com.tauntmachine.todo.model.ListEntity

@Suppress("DEPRECATION")
class ListFragment : Fragment() {

    private var rv_allList: RecyclerView? = null
    private var db_list: MutableList<ListEntity>? =null
    private var btn_addList: FloatingActionButton? =null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v: View = inflater.inflate(R.layout.fragment_list, container, false)
        rv_allList = v.findViewById( R.id.rvAllList )
        btn_addList = v.findViewById( R.id.btnAddList )
        db_list = ArrayList()

        btn_addList!!.setOnClickListener { dialogueBox() }
        getAll()
        return v
    }

    override fun onResume() {
        super.onResume()
        getAll()
    }

    private fun dialogueBox() {
        val factory = LayoutInflater.from( context )
        val add_grocery: View = factory.inflate(R.layout.add_grocery_list, null)
        val groceryDialogue = AlertDialog.Builder( context ).create()
        groceryDialogue.setView(add_grocery)
        groceryDialogue.show()
        var edt_addList: EditText = add_grocery.findViewById( R.id.addGroceryTitle )
        add_grocery.findViewById<TextView>( R.id.btn_yes ).setOnClickListener {
            if ( edt_addList.text.isNullOrEmpty() ) {
                Toast.makeText( context, "Enter Required Field", Toast.LENGTH_SHORT).show()
            } else {
                createList( edt_addList.text.toString() )
                groceryDialogue.dismiss()
            }
        }
        add_grocery.findViewById<TextView>( R.id.btn_cancel ).setOnClickListener {
            groceryDialogue.dismiss()
        }

    }

    private fun createList( title: String ) {
        val db = Room.databaseBuilder(
            context!!,
            ListDatabase::class.java, "list_database.db"
        ).build()


        class DbAsyncTask : AsyncTask<Void, Void, String>() {
            override fun doInBackground(vararg p0: Void?): String? {
                var list_entity = ListEntity()
                list_entity.title = title
                list_entity.status = "Pending"

                db.ListDao()!!.insert(list_entity)
                db_list = db.ListDao()!!.getAllList()
                return "Done"
            }

            override fun onPostExecute(result: String?) {
                super.onPostExecute(result)
                setRv()
            }
        }
        DbAsyncTask().execute()
    }

    private fun getAll() {
        val db = Room.databaseBuilder(
            context!!,
            ListDatabase::class.java, "list_database.db"
        ).build()


        class DbAsyncTask : AsyncTask<Void, Void, String>() {
            override fun doInBackground(vararg p0: Void?): String? {
                db_list = db.ListDao()!!.getAllList()
                return "Done"
            }

            override fun onPostExecute(result: String?) {
                super.onPostExecute(result)
                setRv()
            }
        }

        DbAsyncTask().execute()

    }

    private fun setRv() {
        rv_allList!!.setHasFixedSize( true )
        var adapter_list = ListAdapter( requireContext(), db_list!!, 0 )
        rv_allList!!.layoutManager = LinearLayoutManager ( requireContext() )
        rv_allList!!.adapter = adapter_list

    }
}