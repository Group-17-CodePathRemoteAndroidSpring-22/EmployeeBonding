package com.parth8199.employeebonding

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.parth8199.employeebonding.models.Comment

class CommentAdapter(val context: Context, val comments: MutableList<Comment>) :
    RecyclerView.Adapter<CommentAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_comments, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val comment = comments.get(position)
        holder.bind(comment)
    }

    override fun getItemCount(): Int {
        return comments.size
    }

    public fun clear() {
        comments.clear()
        notifyDataSetChanged()
    }


    public fun addAll(commList: List<Comment>) {
        comments.addAll(commList)
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val tvCommentedBy: TextView
        val tvCommentText: TextView

        init {
            tvCommentedBy = itemView.findViewById(R.id.tvByUser)
            tvCommentText = itemView.findViewById(R.id.tvComment)
        }

        fun bind(comment: Comment) {
            tvCommentedBy.text = comment.getCommentedByEmp()!!.getEmpName()
            tvCommentText.text = comment.getCommentText()
        }
    }
}