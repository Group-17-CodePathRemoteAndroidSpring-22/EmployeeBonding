package com.parth8199.employeebonding

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.parse.FindCallback
import com.parse.ParseException
import com.parse.ParseQuery
import com.parse.ParseUser
import com.parth8199.employeebonding.models.Comment
import com.parth8199.employeebonding.models.Discussion
import com.parth8199.employeebonding.models.Employee

class DiscussionActivity : AppCompatActivity() {

    lateinit var tvTitle: TextView
    lateinit var tvUsername: TextView
    lateinit var tvDescription: TextView
    lateinit var swipeContainer: SwipeRefreshLayout

    lateinit var rvComment: RecyclerView
    lateinit var commentAdapter: CommentAdapter
    lateinit var discussionObjectId: String
    var currentComments: MutableList<Comment> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_discussion)

        discussionObjectId = intent.getStringExtra("discussion_id").toString()

        tvTitle = findViewById(R.id.tvTitleInDiscussion)
        tvDescription = findViewById(R.id.tvDescriptionInDiscussion)
        tvUsername = findViewById(R.id.tvEmpInDiscussion)

        rvComment = findViewById(R.id.rvComments)
        swipeContainer = findViewById(R.id.swipeContainerComment)

        val query: ParseQuery<Discussion> = ParseQuery.getQuery(Discussion::class.java)
        query.whereEqualTo("objectId", discussionObjectId)
        query.include(Discussion.KEY_CREATEDBY)
        query.findInBackground(object : FindCallback<Discussion> {
            override fun done(discussions: MutableList<Discussion>?, e: ParseException?) {
                if (e != null) {
                    Log.e(TAG, "err fetching current discussion")
                } else {
                    if (discussions != null) {
                        tvUsername.text = discussions[0].getCreatedByEmp()!!.getEmpName()
                        tvTitle.text = discussions[0].getTitle()
                        tvDescription.text = discussions[0].getDescription()
                    }
                }
            }
        })
        swipeContainer.setOnRefreshListener {
            // Your code to refresh the list here.
            // Make sure you call swipeContainer.setRefreshing(false)
            // once the network request has completed successfully.
            queryCurrentComments()
        }

        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(
            android.R.color.holo_blue_bright,
            android.R.color.holo_green_light,
            android.R.color.holo_orange_light,
            android.R.color.holo_red_light
        )
        commentAdapter = CommentAdapter(this, currentComments)
        rvComment.adapter = commentAdapter
        rvComment.layoutManager = LinearLayoutManager(this)
        queryCurrentComments()

        findViewById<MaterialButton>(R.id.btnPostComment).setOnClickListener {
            val tvCommentEditText =
                findViewById<TextInputEditText>(R.id.textEditComment).text.toString()
            postCommentState(tvCommentEditText)
        }
    }


    private fun queryCurrentComments() {

        val query: ParseQuery<Discussion> = ParseQuery.getQuery(Discussion::class.java)
        query.whereEqualTo("objectId", discussionObjectId)
        query.include(Discussion.KEY_CREATEDBY)
        query.findInBackground(object : FindCallback<Discussion> {
            override fun done(discussions: MutableList<Discussion>?, e: ParseException?) {
                if (e != null) {
                    Log.e(TAG, "err fetching current discussion")
                } else {
                    if (discussions != null) {
                        val innerquery: ParseQuery<Comment> =
                            ParseQuery.getQuery(Comment::class.java)
                        innerquery.whereEqualTo(Comment.KEY_COMMENTEDIN, discussions[0])
                        innerquery.include(Comment.KEY_COMMENTEDBY)
                        innerquery.addDescendingOrder("createdAt")
                        innerquery.findInBackground(object : FindCallback<Comment> {
                            override fun done(comments: MutableList<Comment>?, e: ParseException?) {
                                if (e != null) {
                                    Log.e(ProfileActivity.TAG, "err fetching comments")
                                } else {
                                    if (comments != null) {
                                        for (comm in comments) {
                                            Log.i(TAG, "comment: " + comm.getCommentText())
                                            Log.i(
                                                TAG,
                                                "comment by: " + comm.getCommentedByEmp()!!
                                                    .getEmpName()
                                            )
                                        }
                                        commentAdapter.clear()
                                        currentComments.addAll(comments)
                                        swipeContainer.isRefreshing = false
                                    } else {
                                        Toast.makeText(
                                            this@DiscussionActivity,
                                            "no comments till now",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }

                                }
                            }

                        })
                    }
                }
            }
        })


    }

    private fun postCommentState(tvCommentEditText: String) {

        val query: ParseQuery<Employee> = ParseQuery.getQuery(Employee::class.java)
        query.whereEqualTo(Employee.KEY_USERLINK, ParseUser.getCurrentUser())
        query.include(Employee.KEY_WORKSIN)
        query.findInBackground(object : FindCallback<Employee> {
            override fun done(emps: MutableList<Employee>?, e: ParseException?) {
                if (e != null) {
                    Log.e(TAG, "err fetching current emp")
                } else {
                    if (emps != null) {
                        finalPostComment(tvCommentEditText, emps[0])
                    }
                }
            }

        })


    }

    private fun finalPostComment(tvCommentEditText: String, employee: Employee) {
        val comm = Comment()
        comm.setCommentText(tvCommentEditText)

        val query: ParseQuery<Discussion> = ParseQuery.getQuery(Discussion::class.java)
        query.whereEqualTo("objectId", discussionObjectId)
        query.include(Discussion.KEY_CREATEDBY)
        query.findInBackground(object : FindCallback<Discussion> {
            override fun done(discussions: MutableList<Discussion>?, e: ParseException?) {
                if (e != null) {
                    Log.e(TAG, "err fetching current discussion")
                } else {
                    if (discussions != null) {

                        comm.setCommentedInDiscus(discussions[0])
                        comm.setCommentedByEmp(employee)
                        comm.saveInBackground { exception ->
                            if (exception != null) {
                                Log.e(TAG, "Error posting comment: ")
                                exception.printStackTrace()
                            } else {
                                Log.i(TAG, "Successfully posted comment")
                                Toast.makeText(
                                    this@DiscussionActivity,
                                    "Comment Posted Successfully",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                }
            }
        })
    }

    companion object {
        const val TAG = "DiscussionActivity"
    }
}