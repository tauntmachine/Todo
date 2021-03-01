package com.tauntmachine.todo.adapter

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.tauntmachine.todo.GroceryActivity
import com.tauntmachine.todo.ListDatabase
import com.tauntmachine.todo.R
import com.tauntmachine.todo.model.GroceryEntity
import com.tauntmachine.todo.model.ListEntity


@Suppress("DEPRECATION")
class ListAdapter(
    private var context: Context,
    private var rv_list: MutableList<ListEntity>,
    private var flag: Int
): RecyclerView.Adapter<ListAdapter.ListVh>() {
    private var temp_list: MutableList<ListEntity>? =null
    class ListVh( itemview: View): RecyclerView.ViewHolder(itemview) {
        var tv_name: TextView = itemview.findViewById( R.id.tvItemName )
        var tv_status: TextView = itemview.findViewById( R.id.tvStatus )
        var iv_del: ImageView = itemview.findViewById( R.id.delItem )
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListVh {
        val view = LayoutInflater.from(context).inflate(R.layout.items, parent, false)
        return ListVh(view)
    }

    override fun getItemCount(): Int {
        return rv_list.size
    }

    @SuppressLint("ResourceType")
    override fun onBindViewHolder(holder: ListVh, position: Int) {
        var cur: ListEntity = rv_list[position]
        holder.tv_name.text = cur.title
        if ( cur.status == "Pending") {
            holder.tv_status.text = "Pending"
            holder.tv_status.setTextColor( context.resources.getColor( R.color.red ) )
        } else {
            holder.tv_status.text = "Done"
            holder.tv_status.setTextColor( context.resources.getColor( R.color.purple_200 ) )
        }

        holder.iv_del.setOnClickListener{
            openDelDialogue( position )
        }

        holder.tv_status.setOnClickListener {
            changeStatus( position )
        }

        holder.itemView.setOnClickListener {
            var intent = Intent( context, GroceryActivity::class.java )
            intent.putExtra( "list_id", cur.id)
            intent.putExtra("title", cur.title)
            context.startActivity( intent )
        }

        holder.itemView.setOnLongClickListener {
            if (flag == 0) copyList(position)
            return@setOnLongClickListener true
        }

    }

    private fun copyList( pos: Int ) {
        val alertDialog: AlertDialog.Builder = AlertDialog.Builder(context)
        alertDialog.setTitle("Copy")
        alertDialog.setMessage("Copy Enter List?")
        alertDialog.setPositiveButton(
            "Confirm"
        ) { _, _ ->
            val db = Room.databaseBuilder(
                context,
                ListDatabase::class.java, "list_database.db"
            ).build()

            class DbAsyncTask : AsyncTask<Void, Void, String>() {

                override fun doInBackground(vararg p0: Void?): String? {
                    var list_entity = ListEntity()
                    list_entity.title = rv_list[pos].title
                    list_entity.status = "Pending"
                    db.ListDao()!!.insert(list_entity)
                    temp_list = ArrayList()
                    temp_list = db.ListDao()!!.getAllList()
                    var list_grocery: MutableList<GroceryEntity>
                    list_grocery = db.GroceryDao()!!.getAllSelectedGrocery( rv_list[pos].id )
                    if ( !list_grocery.isNullOrEmpty() ) {
                        for ( obj in  list_grocery) {
                            var groceryEntity = GroceryEntity()
                            groceryEntity.name = obj.name
                            groceryEntity.status = obj.status
                            groceryEntity.count = obj.count
                            groceryEntity.f_id = temp_list!![temp_list!!.size-1].id
                            db.GroceryDao()!!.insert( groceryEntity )
                        }
                        rv_list = db.ListDao()!!.getAllList()
                    }
                    return "Done"
                }

                override fun onPostExecute(result: String?) {
                    super.onPostExecute(result)
                    notifyDataSetChanged()
                }
            }

            DbAsyncTask().execute()
        }
        alertDialog.setNegativeButton(
            "Cancel"
        ) { _, _ -> }
        val alert: AlertDialog = alertDialog.create()
        alert.setCanceledOnTouchOutside( true )
        alert.show()
    }

    private fun changeStatus( pos: Int ){
        val alertDialog: AlertDialog.Builder = AlertDialog.Builder(context)
        alertDialog.setTitle("Status")
        alertDialog.setMessage("Do you wanna update item status?")
        alertDialog.setPositiveButton(
            "yes"
        ) { _, _ ->
            val db = Room.databaseBuilder(
                context,
                ListDatabase::class.java, "list_database.db"
            ).build()

            class DbAsyncTask : AsyncTask<Void, Void, String>() {

                override fun doInBackground(vararg p0: Void?): String? {
                    if (rv_list[pos].status == "Pending")
                        db.ListDao()!!.updateStatus(rv_list[pos].id, "Done")
                    else
                        db.ListDao()!!.updateStatus(rv_list[pos].id, "Pending")
                    return "deleted"
                }

                override fun onPostExecute(result: String?) {
                    super.onPostExecute(result)
                    rv_list.removeAt( pos )
                    notifyDataSetChanged()
                }
            }

            DbAsyncTask().execute()
        }
        alertDialog.setNegativeButton(
            "No"
        ) { _, _ -> }
        val alert: AlertDialog = alertDialog.create()
        alert.setCanceledOnTouchOutside( true )
        alert.show()
    }

    private fun openDelDialogue( pos: Int ) {
        val alertDialog: AlertDialog.Builder = AlertDialog.Builder(context)
        alertDialog.setTitle("Delete Item")
        alertDialog.setMessage("Do you wanna delete this item?")
        alertDialog.setPositiveButton(
            "yes"
        ) { _, _ ->
            val db = Room.databaseBuilder(
                context,
                ListDatabase::class.java, "list_database.db"
            ).build()

            class DbAsyncTask : AsyncTask<Void, Void, String>() {

                override fun doInBackground(vararg p0: Void?): String? {
                    db.GroceryDao()!!.deleteItemById(rv_list[pos].id)
                    db.ListDao()!!.delete( rv_list[pos] )
                    return "deleted"
                }

                override fun onPostExecute(result: String?) {
                    super.onPostExecute(result)
                    rv_list.removeAt( pos )
                    notifyDataSetChanged()
                }
            }

            DbAsyncTask().execute()
        }
        alertDialog.setNegativeButton(
            "No"
        ) { _, _ -> }
        val alert: AlertDialog = alertDialog.create()
        alert.setCanceledOnTouchOutside( true )
        alert.show()
    }
}