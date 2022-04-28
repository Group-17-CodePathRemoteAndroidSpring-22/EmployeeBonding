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

    fun getCreatedByEmp() : Employee?{
        return getParseObject(KEY_CREATEDBY) as Employee
    }

    fun getCreatedInTeam() : Team? {
        return getParseObject(KEY_CREATEDIN) as Team
    }

    fun setTitle(title : String){
        put(KEY_TITLE, title)
    }

    fun setDescription(desc: String) {
        put(KEY_DESCRIPTION, desc)
    }

    fun setCreatedByEmp(emp : Employee){
        put(KEY_CREATEDBY, emp)
    }

    fun setCreatedIn(team: Team){
        put(KEY_CREATEDIN,team)
    }

    companion object {
        const val KEY_TITLE = "title"
        const val KEY_DESCRIPTION = "description"
        const val KEY_CREATEDBY = "created_by"
        const val KEY_CREATEDIN = "created_in"
    }
}