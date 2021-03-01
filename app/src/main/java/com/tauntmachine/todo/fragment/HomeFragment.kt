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
class HomeFragment : Fragment() {

    private var btn_add: FloatingActionButton? =null
    private lateinit var rv_home: RecyclerView
    private var adapter_list: ListAdapter? =null
    private var db_list: MutableList<ListEntity>? =null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v: View = inflater.inflate(R.layout.fragment_home, container, false)

        rv_home = v.findViewById( R.id.rvHome )
        btn_add = v.findViewById( R.id.floatingActionButton )
        btn_add!!.setOnClickListener{
            dialogueBox()
        }
        db_list = ArrayList()
        getAllPending()
        return v
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

    override fun onStart() {
        super.onStart()
        db_list = ArrayList()
        getAllPending()
    }

    private fun createList( title: String ) {
        val db = Room.databaseBuilder(
            context!!,
            ListDatabase::class.java, "list_database.db"
        ).build()


        class DbAsyncTask : AsyncTask<Void, Void, String>() {
            override fun doInBackground(vararg p0: Void?): String? {
                db.ListDao()!!.insert(title, "Pending")
                db_list = db.ListDao()!!.getPending("Pending")
                return "Done"
            }

            override fun onPostExecute(result: String?) {
                super.onPostExecute(result)
                setRv()
            }
        }

        DbAsyncTask().execute()
    }

    private fun getAllPending(){
        val db = Room.databaseBuilder(
            context!!,
            ListDatabase::class.java, "list_database.db"
        ).build()


        class DbAsyncTask : AsyncTask<Void, Void, String>() {

            override fun doInBackground(vararg p0: Void?): String? {
                db_list = db.ListDao()!!.getPending("Pending")
                return "Added"
            }

            override fun onPostExecute(result: String?) {
                super.onPostExecute(result)
                if (!db_list.isNullOrEmpty()) setRv()
            }
        }

        DbAsyncTask().execute()
    }

    private fun setRv(){
        rv_home.setHasFixedSize( true )
        adapter_list = ListAdapter( requireContext(), db_list!!, 1)
        rv_home.layoutManager = LinearLayoutManager( requireContext() )
        rv_home.adapter = adapter_list
    }

    override fun onResume() {
        super.onResume()
        db_list = ArrayList()
        getAllPending()
    }
}