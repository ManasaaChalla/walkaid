package com.example.walkaid.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.walkaid.R
import com.example.walkaid.result.RunningResultActivity

class ReportAdapter(private val context: Context?, var samplearrlist: ArrayList<String>) : RecyclerView.Adapter<ReportAdapter.ViewHolder>() {

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.single_report_item, parent, false)

        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
            context!!.startActivity(Intent (context!! ,RunningResultActivity ::class.java) )
        }

        holder.imageView.setOnClickListener {
            samplearrlist.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
//        return 6
        return samplearrlist.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val imageView: ImageView = itemView.findViewById(R.id.delete_report)
//        val textView: TextView = itemView.findViewById(R.id.report_text)
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemId(position: Int): Long {
        return super.getItemId(position)
    }
}
