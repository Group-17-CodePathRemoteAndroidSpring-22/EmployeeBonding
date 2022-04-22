package com.parth8199.employeebonding

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.parse.*


class LoginActivity : AppCompatActivity() {
    //This is a HashMap used to retrieve the Organization object of a particular name
    // (Key: Organization Name, Value : Organization Object)
    var orgList: MutableMap<String, Organization> = mutableMapOf()

    //Just a List of Ogranization Names for populating the Exposed AutoComplete TextField for Organization
    var orgNameList: MutableList<String> = mutableListOf()

    //Hardcoded for now (Can be fetched from db like Organization Name)
    val teamNames = listOf("Engineering", "Human Resource", "IT")

    //Haven't Used
    var teamList: MutableList<Team> = mutableListOf()
    val aux: MutableList<Organization> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //ParseUser.logOutInBackground()
        getSupportActionBar()!!.hide();

        //Go to MainActivity if already logged in
        if (ParseUser.getCurrentUser() != null) {
            goToMainActivity()
        }

        //Code to populate Exposed Autocomplete TextField for Team
        val adapterTeamName = ArrayAdapter(this, R.layout.list_item, teamNames)
        val autoCtvTeamName = findViewById<AutoCompleteTextView>(R.id.autoCtvTeamName)
        autoCtvTeamName.setAdapter(adapterTeamName)

        //Similar to above but fetches Organization Objects from db
        queryOrganizationNameList()

        //Handle Log in
        findViewById<MaterialButton>(R.id.btnLogin).setOnClickListener {
            val username = findViewById<TextInputEditText>(R.id.username_edit_text).text.toString()
            val password = findViewById<TextInputEditText>(R.id.password_edit_text).text.toString()
            loginUser(username, password)
        }

        //Handle Sign in
        findViewById<MaterialButton>(R.id.btnSignin).setOnClickListener {
            val username = findViewById<TextInputEditText>(R.id.username_edit_text).text.toString()
            val password = findViewById<TextInputEditText>(R.id.password_edit_text).text.toString()
            val organizationName = findViewById<AutoCompleteTextView>(R.id.autoCTv).text.toString()
            val teamName = findViewById<AutoCompleteTextView>(R.id.autoCtvTeamName).text.toString()

            Log.i(TAG, "Name is : $organizationName")
            // Sign in
            signUp(username, password, organizationName, teamName)
        }


    }

    private fun loginUser(username: String, password: String) {
        ParseUser.logInInBackground(
            username, password, ({ user, e ->
                if (user != null) {
                    //Login Successful
                    Log.i(TAG, "Logged in Successfully")
                    goToMainActivity()
                } else {
                    // Signup failed.  Look at the ParseException to see what happened.
                    e.printStackTrace()
                    Toast.makeText(this, "Error Logging in", Toast.LENGTH_SHORT).show()
                }
            })
        )
    }

    private fun signUp(
        username: String,
        password: String,
        organizationName: String,
        teamName: String
    ) {
        //First Sign up in User Table since Parse session revolves around User.
        //We have linked a User with an Employee by adding
        // a pointer to User in the Employee table
        //Thus Employee is modified accordingly. (Check Parse Dashboard)
        val user = ParseUser()
        user.setUsername(username)
        user.setPassword(password)
        user.signUpInBackground { e ->
            if (e == null) {
                //User is registered but we need to create an Employee as well and link it to the User
                registerEmployee(username, organizationName, teamName)
            } else {
                // Sign up didn't succeed. Look at the ParseException
                // to figure out what went wrong
                e.printStackTrace()

            }
        }
    }

    private fun registerEmployee(username: String, organizationName: String, teamName: String) {
        //Create an Employee
        val emp = Employee()

        //We also need to link an Employee under a team
        //This query fetches those teams whose Organization is selected by the user.
        //For e.g., if Google is selected, all teams pointing to Google as an Organization are returned
        val query: ParseQuery<Team> = ParseQuery.getQuery(Team::class.java)
        query.whereEqualTo("parent_company", orgList.getValue(organizationName))
        query.findInBackground(object : FindCallback<Team> {
            override fun done(teams: MutableList<Team>?, e: ParseException?) {
                if (e != null) {
                    Log.e(TAG, "Error Fetching Teams")
                    e.printStackTrace()
                } else {
                    if (teams != null) {
                        for (team in teams) {
                            //Among the teams returned under the specified organization we find the
                            // team which the user specified while signing in
                            if (team.getTeamName() == teamName) {
                                //On finding the team, we create the Employee with necessary links
                                emp.setEmpName(username)
                                emp.setUser(ParseUser.getCurrentUser())
                                emp.setWorksIn(team)
                                emp.saveInBackground { exception ->
                                    if (exception != null) {
                                        Log.e(TAG, "Error submitting emp: ")
                                        exception.printStackTrace()
                                    } else {
                                        Log.i(TAG, "Successfully saved emp")
                                        Toast.makeText(
                                            this@LoginActivity,
                                            "Emp Successfully",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        //Go to main activity when successfully registered
                                        goToMainActivity()
                                    }
                                }
                            }
                        }
                    }
                }
            }

        })


    }

    //Intent to MainActivity
    private fun goToMainActivity() {
        val intent = Intent(this@LoginActivity, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    //Fetches list of all Organizations
    fun queryOrganizationNameList() {

        //Populate Exposed AutoComplete TextField
        val adapterOrgName = ArrayAdapter(this, R.layout.list_item, orgNameList)
        val autoCtvOrgName = findViewById<AutoCompleteTextView>(R.id.autoCTv)

        //fetch all the Organization
        val query: ParseQuery<Organization> = ParseQuery.getQuery(Organization::class.java)
        query.addDescendingOrder("createdAt")
        query.findInBackground(object : FindCallback<Organization> {
            override fun done(orgs: MutableList<Organization>?, e: ParseException?) {
                if (e != null) {
                    Log.e(TAG, "Error Fetching Organization Name")
                    e.printStackTrace()
                } else {
                    if (orgs != null) {
                        for (org in orgs) {
                            Log.i(
                                TAG,
                                "Organization: " + org.getCompanyName()
                            )
                            //Mapping Organization Name -> Organization Object for later use
                            orgList.put(org.getCompanyName()!!, org)

                            //Save their names as String in a List
                            orgNameList.add(org.getCompanyName()!!)
                        }
                        //Populate Exposed AutoComplete TextField
                        autoCtvOrgName.setAdapter(adapterOrgName)

                    }
                }
            }

        })

    }

    companion object {
        const val TAG = "LoginActivity"
    }
}