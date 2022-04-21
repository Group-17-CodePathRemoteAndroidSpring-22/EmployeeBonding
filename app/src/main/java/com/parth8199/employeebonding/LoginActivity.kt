package com.parth8199.employeebonding

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.core.content.ContentProviderCompat.requireContext
import com.parse.FindCallback
import com.parse.ParseException
import com.parse.ParseQuery

class LoginActivity : AppCompatActivity() {
    var orgList: MutableList<String> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        queryOrganizationNameList()


    }

    private fun goToMainActivity() {
        val intent = Intent(this@LoginActivity, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun queryOrganizationNameList() {

        val adapter = ArrayAdapter(this, R.layout.list_item, orgList)
        val autoCompleteTextView = findViewById<AutoCompleteTextView>(R.id.autoCTv)
        val query: ParseQuery<Organization> = ParseQuery.getQuery(Organization::class.java)
        query.addDescendingOrder("createdAt")
        query.findInBackground(object : FindCallback<Organization> {
            override fun done(orgs: MutableList<Organization>?, e: ParseException?) {
                if (e != null) {
                    Log.e(TAG, "Error Fetching Organization Name")
                } else {
                    if (orgs != null) {
                        for (org in orgs) {
                            Log.i(
                                TAG,
                                "Organization: " + org.getCompanyName()
                            )
                            orgList.add(org.getCompanyName()!!)
                        }
                        autoCompleteTextView.setAdapter(adapter)
                    }
                }
            }

        })

    }

    companion object {
        const val TAG = "LoginActivity"
    }
}