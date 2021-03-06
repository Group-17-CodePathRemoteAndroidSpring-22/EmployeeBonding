package com.parth8199.employeebonding

import android.app.Application
import com.parse.Parse
import com.parse.ParseObject
import com.parth8199.employeebonding.models.*

class EmployeeBondingApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        ParseObject.registerSubclass(Organization::class.java)
        ParseObject.registerSubclass(Team::class.java)
        ParseObject.registerSubclass(Employee::class.java)
        ParseObject.registerSubclass(Discussion::class.java)
        ParseObject.registerSubclass(Comment::class.java)
        Parse.initialize(
            Parse.Configuration.Builder(this)
                .applicationId(getString(R.string.back4app_app_id))
                .clientKey(getString(R.string.back4app_client_key))
                .server(getString(R.string.back4app_server_url))
                .build()
        );
    }
}