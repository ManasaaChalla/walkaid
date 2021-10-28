package com.example.walkaid.view.activity

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.walkaid.R
import com.example.walkaid.data.Sessions

class RecyclerAdapter(sessionList: ArrayList<Sessions>) :
    RecyclerView.Adapter<RecyclerAdapter.ReportViewHolder>() {

    private var sessions: ArrayList<Sessions> = sessionList

    class ReportViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private var text: TextView = view.findViewById(R.id.textView5)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerAdapter.ReportViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: RecyclerAdapter.ReportViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

}