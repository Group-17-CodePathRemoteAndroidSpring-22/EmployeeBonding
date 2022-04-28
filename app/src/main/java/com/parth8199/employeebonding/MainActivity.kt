package com.parth8199.employeebonding

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.leinardi.android.speeddial.SpeedDialActionItem
import com.leinardi.android.speeddial.SpeedDialView
import com.parse.FindCallback
import com.parse.ParseException
import com.parse.ParseQuery
import com.parse.ParseUser
import com.parth8199.employeebonding.models.Discussion
import com.parth8199.employeebonding.models.Employee
import com.parth8199.employeebonding.models.Team

class MainActivity : AppCompatActivity() {

    lateinit var homeFeedRecyclerView: RecyclerView

    lateinit var adapter: HomefeedAdapter

    var discussionsList : MutableList<Discussion> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        homeFeedRecyclerView = findViewById(R.id.homeFeedRecyclerView)
        adapter = HomefeedAdapter(discussionsList)

      //  homeFeedRecyclerView.layoutManager = LinearLayoutManager(this)
        homeFeedRecyclerView.adapter = adapter
        homeFeedRecyclerView.layoutManager = LinearLayoutManager(this)

        queryCurrentState()

        val speedDialView = findViewById<SpeedDialView>(R.id.speedDial)
        speedDialView.addActionItem(
            SpeedDialActionItem.Builder(
                R.id.fab_log_out,
                R.drawable.ic_baseline_exit_to_app_24_white
            )
                .setFabBackgroundColor(
                    ResourcesCompat.getColor(
                        getResources(),
                        R.color.purple_200,
                        getTheme()
                    )
                )
                .setFabImageTintColor(
                    ResourcesCompat.getColor(
                        getResources(),
                        R.color.white,
                        getTheme()
                    )
                )
                .setLabelColor(Color.DKGRAY)
                .setLabelBackgroundColor(
                    ResourcesCompat.getColor(
                        getResources(),
                        R.color.white,
                        getTheme()
                    )
                )
                .setLabelClickable(false)
                .setLabel("Log Out")
                .create()
        )
        speedDialView.addActionItem(
            SpeedDialActionItem.Builder(R.id.fab_compose_screen, R.drawable.ic_baseline_create_24)
                .setFabBackgroundColor(
                    ResourcesCompat.getColor(
                        getResources(),
                        R.color.purple_200,
                        getTheme()
                    )
                )
                .setFabImageTintColor(
                    ResourcesCompat.getColor(
                        getResources(),
                        R.color.white,
                        getTheme()
                    )
                )
                .setLabelColor(Color.DKGRAY)
                .setLabelBackgroundColor(
                    ResourcesCompat.getColor(
                        getResources(),
                        R.color.white,
                        getTheme()
                    )
                )
                .setLabelClickable(false)
                .setLabel("Create Discussion")
                .create()
        )
        speedDialView.setOnActionSelectedListener(SpeedDialView.OnActionSelectedListener { actionItem ->
            when (actionItem.id) {
                R.id.fab_log_out -> {
                    ParseUser.logOut()
                    val currentUser = ParseUser.getCurrentUser()
                    if (currentUser == null) {
                        val intent = Intent(this@MainActivity, LoginActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    Toast.makeText(this, "Logged Out", Toast.LENGTH_SHORT).show()
                    speedDialView.close() // To close the Speed Dial with animation
                    return@OnActionSelectedListener true // false will close it without animation
                }
                R.id.fab_compose_screen -> {
                    val intent = Intent(this@MainActivity, ComposeActivity::class.java)
                    startActivity(intent)
                    //finish()
                    Toast.makeText(this, "Going to Compose Screen ", Toast.LENGTH_SHORT).show()
                    speedDialView.close() // To close the Speed Dial with animation
                    return@OnActionSelectedListener true // false will close it without animation
                }
            }
            false
        })

    }

    private fun queryCurrentState() {
        val query: ParseQuery<Employee> = ParseQuery.getQuery(Employee::class.java)
        query.whereEqualTo(Employee.KEY_USERLINK,ParseUser.getCurrentUser())
        query.include(Employee.KEY_WORKSIN)
        query.findInBackground(object :FindCallback<Employee>{
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
        query.whereEqualTo(Discussion.KEY_CREATEDIN,currentTeam)
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
                        discussionsList.addAll(discussions)
                        adapter.notifyDataSetChanged()
                    }
                }
            }

        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.item_logout, menu);
        return true;
    }

    companion object {
        const val TAG = "MainActivity"
    }

    fun onLogoutAction(mi: MenuItem) {
        // handle click here
        ParseUser.logOut()
        val currentUser = ParseUser.getCurrentUser()
        if (currentUser == null) {
            val intent = Intent(this@MainActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}