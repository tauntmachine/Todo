package com.tauntmachine.todo.adapter

import android.app.AlertDialog
import android.content.Context
import android.os.AsyncTask
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.tauntmachine.todo.ListDatabase
import com.tauntmachine.todo.R
import com.tauntmachine.todo.model.GroceryEntity

@Suppress("DEPRECATION")
class GroceryAdapter(
    private var context: Context,
    private var rv_list: MutableList<GroceryEntity>
    ): RecyclerView.Adapter<GroceryAdapter.GroceryVh>() {
    private var check_list: MutableList<GroceryEntity>? =null
    class GroceryVh(itemview: View): RecyclerView.ViewHolder(itemview) {
        var tv_name: TextView = itemview.findViewById( R.id.tvItemName )
        var tv_status: TextView = itemview.findViewById( R.id.tvStatus )
        var iv_del: ImageView = itemview.findViewById( R.id.delItem )
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroceryVh {
        val view = LayoutInflater.from(context).inflate(R.layout.items, parent, false)
        check_list = ArrayList()
        return GroceryVh(view)
    }

    override fun getItemCount(): Int {
        return rv_list.size
    }

    override fun onBindViewHolder(holder: GroceryVh, position: Int) {
        var cur: GroceryEntity = rv_list[position]
        holder.tv_name.text = "${cur.name} ( ${cur.count} )"
        if ( cur.status == "Pending") {
            holder.tv_status.text = "Pending"
            holder.tv_status.setTextColor( context.resources.getColor( R.color.red ) )
        } else {
            holder.tv_status.text = "Done"
            holder.tv_status.setTextColor( context.resources.getColor( R.color.purple_200 ) )
        }

        holder.tv_status.setOnClickListener {
            showStatusDialogue( position )
        }

        holder.iv_del.setOnClickListener {
            showAlertDialog( position )
        }
    }

    private fun showStatusDialogue( pos: Int) {
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
                        db.GroceryDao()!!.update(rv_list[pos].id, "Done")
                    else
                        db.GroceryDao()!!.update(rv_list[pos].id, "Pending" )
                    check_list = ArrayList()
                    check_list = db.GroceryDao()!!.getAllSelectedGrocery(rv_list[pos].f_id)
                    return "deleted"
                }

                override fun onPostExecute(result: String?) {
                    super.onPostExecute(result)
                    if (rv_list[pos].status == "Pending")
                        rv_list[pos].status = "Done"
                    else
                        rv_list[pos].status = "Pending"
                    notifyDataSetChanged()
                    checkParent(pos)
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

    private fun checkParent( pos: Int) {
        var flag = false
        if ( !check_list.isNullOrEmpty() ) {
            for ( check in check_list!! ){
                if ( check.status.equals( "Pending" ) )
                    flag = true
            }
            if ( flag ) {
                updateParentStatus(pos, "Pending")
            } else {
                updateParentStatus(pos, "Done")
            }
        }
    }

    private fun updateParentStatus(pos: Int, status: String) {
        val db = Room.databaseBuilder(
            context,
            ListDatabase::class.java, "list_database.db"
        ).build()

        class DbAsyncTask : AsyncTask<Void, Void, String>() {

            override fun doInBackground(vararg p0: Void?): String? {
                db.ListDao()!!.updateStatus(rv_list[pos].f_id, status)
                return "deleted"
            }
        }

        DbAsyncTask().execute()
    }

    private fun showAlertDialog( pos: Int) {
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
                    db.GroceryDao()!!.delete( rv_list[pos] )
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