package com.parth8199.employeebonding

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.parth8199.employeebonding.models.Discussion

class HomefeedAdapter(val discussions: MutableList<Discussion>): RecyclerView.Adapter<HomefeedAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomefeedAdapter.ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)

        val view = inflater.inflate(R.layout.item_discussion, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: HomefeedAdapter.ViewHolder, position: Int) {
        val discussion: Discussion = discussions.get(position)

        holder.tvUsername.text = discussion.getCreatedByEmp().toString()
        holder.tvDiscussionTitle.text = discussion.getTitle()
    }

    override fun getItemCount(): Int {
        return discussions.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvUsername = itemView.findViewById<TextView>(R.id.tvUsername)
        val tvDiscussionTitle = itemView.findViewById<TextView>(R.id.tvDiscussionTitle)

    }
}