package com.parth8199.employeebonding

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.parth8199.employeebonding.models.Discussion

open class HomefeedAdapter(val discussions: MutableList<Discussion>): RecyclerView.Adapter<HomefeedAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomefeedAdapter.ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)

        val view = inflater.inflate(R.layout.item_discussion, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: HomefeedAdapter.ViewHolder, position: Int) {
        val discussion: Discussion = discussions.get(position)

        holder.tvUsername.text = discussion.getCreatedByEmp()!!.getEmpName()
        holder.tvDiscussionTitle.text = discussion.getTitle()
    }

    override fun getItemCount(): Int {
        return discussions.size
    }

    public fun clear() {
        discussions.clear()
        notifyDataSetChanged()
    }

    // Add a list of items -- change to type used
    public fun addAll(dislist: List<Discussion>) {
        discussions.addAll(dislist)
        notifyDataSetChanged()
    }
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val tvUsername = itemView.findViewById<TextView>(R.id.tvUsername)
        val tvDiscussionTitle = itemView.findViewById<TextView>(R.id.tvDiscussionTitle)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            val discussion = discussions[adapterPosition]
            val intent = Intent(itemView.context,DiscussionActivity::class.java)
            intent.putExtra("discussion_id", discussion.objectId)
            itemView.context.startActivity(intent)
        }

    }
}