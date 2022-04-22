package com.parth8199.employeebonding.models

import com.parse.ParseClassName
import com.parse.ParseObject
import com.parse.ParseUser

@ParseClassName("Employee")
class Employee : ParseObject() {
    fun getEmpName(): String? {
        return getString(KEY_EMPNAME)
    }

    fun getWorksAt(): ParseObject? {
        return getParseObject(KEY_WORKSIN)
    }

    fun getUser(): ParseUser? {
        return getParseUser(KEY_USERLINK)
    }

    fun setUser(user: ParseUser) {
        put(KEY_USERLINK, user)
    }

    fun setEmpName(name: String) {
        put(KEY_EMPNAME, name)
    }

    fun setWorksIn(worksAt: ParseObject) {
        put(KEY_WORKSIN, worksAt)
    }


    companion object {
        const val KEY_EMPNAME = "name"
        const val KEY_WORKSIN = "works_in"
        const val KEY_USERLINK = "user_link"
    }
}