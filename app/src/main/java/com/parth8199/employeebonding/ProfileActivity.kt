package com.parth8199.employeebonding

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.parse.FindCallback
import com.parse.ParseException
import com.parse.ParseQuery
import com.parse.ParseUser
import com.parth8199.employeebonding.models.Discussion
import com.parth8199.employeebonding.models.Employee
import com.parth8199.employeebonding.models.Team

class ProfileActivity : AppCompatActivity() {

    lateinit var profileFeedRv: RecyclerView
    lateinit var swipeContainer: SwipeRefreshLayout
    lateinit var profileFeedAdapter: ProfileFeedAdapter
    var discussionsList : MutableList<Discussion> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        profileFeedRv = findViewById(R.id.rvProfileFeed)
        profileFeedAdapter = ProfileFeedAdapter(discussionsList)


        profileFeedRv.adapter = profileFeedAdapter
        profileFeedRv.layoutManager = LinearLayoutManager(this)

        queryCurrentState()

        swipeContainer = findViewById(R.id.swipeContainerPF)
        // Setup refresh listener which triggers new data loading

        swipeContainer.setOnRefreshListener {
            // Your code to refresh the list here.
            // Make sure you call swipeContainer.setRefreshing(false)
            // once the network request has completed successfully.
            queryCurrentState()
        }

        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
            android.R.color.holo_green_light,
            android.R.color.holo_orange_light,
            android.R.color.holo_red_light);
    }

    private fun queryCurrentState() {
        val query: ParseQuery<Employee> = ParseQuery.getQuery(Employee::class.java)
        query.whereEqualTo(Employee.KEY_USERLINK, ParseUser.getCurrentUser())
        query.include(Employee.KEY_WORKSIN)
        query.findInBackground(object : FindCallback<Employee> {
            override fun done(emps: MutableList<Employee>?, e: ParseException?) {
                if( e!= null){
                    Log.e(TAG, "err fetching current emp")
                }else{
                    if (emps != null){
                        queryDiscussions(emps[0], emps[0].getWorksAt()!!)
                    }
                }
            }

        })
    }

    fun queryDiscussions(currentEmp : Employee, currentTeam : Team) {

        val query: ParseQuery<Discussion> = ParseQuery.getQuery(Discussion::class.java)
        query.whereEqualTo(Discussion.KEY_CREATEDBY,currentEmp)
        query.include(Discussion.KEY_CREATEDBY)
        query.include(Discussion.KEY_CREATEDIN)
        query.addDescendingOrder("createdAt")
        query.findInBackground(object : FindCallback<Discussion> {
            override fun done(discussions: MutableList<Discussion>?, e: ParseException?) {
                if(e != null) {
                    Log.e(TAG, "err fetching discussions")
                } else {
                    if (discussions != null) {
                        for (discussion in discussions) {
                            Log.i(TAG, "discussion: " + discussion.getTitle())
                            Log.i(TAG, "discussion by: " + discussion.getCreatedByEmp()!!.getEmpName())
                            Log.i(TAG, "discussion in: " + discussion.getCreatedInTeam()!!.getTeamName())
                        }
                        profileFeedAdapter.clear()
                        discussionsList.addAll(discussions)
                        swipeContainer.isRefreshing = false
                    }
                }
            }

        })
    }
    companion object {
        const val TAG ="ProfileActivity"
    }

}