package com.parth8199.employeebonding

import com.parse.ParseClassName
import com.parse.ParseObject

@ParseClassName("Employee")
class Employee: ParseObject() {
    fun getEmpName() : String? {
        return getString(KEY_EMPNAME)
    }

    fun getWorksAt() : ParseObject? {
        return getParseObject(KEY_WORKSAT)
    }

    fun setEmpName(name:String) {
        put(KEY_EMPNAME, name)
    }

    fun setWorksAt(worksAt : ParseObject){
        put(KEY_WORKSAT,worksAt)
    }

    companion object{
        const val KEY_EMPNAME = "name"
        const val KEY_WORKSAT = "works_at"
    }
}