package com.parth8199.employeebonding

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.parse.FindCallback
import com.parse.ParseException
import com.parse.ParseQuery
import com.parse.ParseUser
import com.parth8199.employeebonding.models.Discussion
import com.parth8199.employeebonding.models.Employee

class ComposeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_compose)

        findViewById<MaterialButton>(R.id.buttonPost).setOnClickListener {
            val titleText = findViewById<TextInputEditText>(R.id.editTitle).text.toString()
            val descriptionText =
                findViewById<TextInputEditText>(R.id.editDescription).text.toString()
            postDiscussion(titleText, descriptionText)
        }
    }

    private fun postDiscussion(titleText: String, descriptionText: String) {
        val discus = Discussion()
        discus.setTitle(titleText)
        discus.setDescription(descriptionText)
        val query: ParseQuery<Employee> = ParseQuery.getQuery(Employee::class.java)
        query.whereEqualTo(Employee.KEY_USERLINK, ParseUser.getCurrentUser())
        query.include(Employee.KEY_WORKSIN)
        query.findInBackground(object : FindCallback<Employee> {
            override fun done(emps: MutableList<Employee>?, e: ParseException?) {
                if (e != null) {
                    Log.e(TAG, "err fetching current emp")
                } else {
                    if (emps != null) {
                        discus.setCreatedByEmp(emps[0])
                        discus.setCreatedIn(emps[0].getWorksAt()!!)
                        discus.saveInBackground { exception ->
                            if (exception != null) {
                                Log.e(TAG, "Error creating discussion: ")
                                exception.printStackTrace()
                            } else {
                                Log.i(TAG, "Successfully created discussion")
                                Toast.makeText(
                                    this@ComposeActivity,
                                    "Discussion created Successfully",
                                    Toast.LENGTH_SHORT
                                ).show()
                                //Go to main activity when successfully registered
                                finish()
                            }
                        }
                    }
                }
            }

        })
    }
    companion object {
        const val TAG = "ComposeActivity"
    }
}