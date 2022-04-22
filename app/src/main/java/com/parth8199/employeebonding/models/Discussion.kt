package com.parth8199.employeebonding.models

import com.parse.Parse
import com.parse.ParseClassName
import com.parse.ParseObject

@ParseClassName("Discussion")
class Discussion : ParseObject() {

    fun getTitle(): String? {
        return getString(KEY_TITLE)
    }

    fun getDescription(): String? {
        return getString(KEY_DESCRIPTION)
    }

    fun getCreatedByEmp() : ParseObject?{
        return getParseObject(KEY_CREATEDBY)
    }

    fun getCreatedInTeam() : ParseObject? {
        return getParseObject(KEY_CREATEDIN)
    }

    fun setTitle(title : String){
        put(KEY_TITLE, title)
    }

    fun setDescription(desc: String) {
        put(KEY_DESCRIPTION, desc)
    }

    fun setCreatedByEmp(emp : ParseObject){
        put(KEY_CREATEDBY, emp)
    }

    fun setCreatedIn(team: ParseObject){
        put(KEY_CREATEDIN,team)
    }

    companion object {
        const val KEY_TITLE = "title"
        const val KEY_DESCRIPTION = "description"
        const val KEY_CREATEDBY = "created_by"
        const val KEY_CREATEDIN = "created_in"
    }
}