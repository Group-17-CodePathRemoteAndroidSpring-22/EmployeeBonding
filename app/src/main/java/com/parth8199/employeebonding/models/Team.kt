package com.parth8199.employeebonding.models

import com.parse.ParseClassName
import com.parse.ParseObject

@ParseClassName("Team")
class Team : ParseObject() {

    fun getTeamName(): String? {
        return getString(KEY_TEAMNAME)
    }

    fun setTeamName(teamName: String) {
        put(KEY_TEAMNAME, teamName)
    }

    fun getParent(): ParseObject? {
        return getParseObject(KEY_PARENT)
    }

    fun setParent(parent: ParseObject) {
        put(KEY_PARENT, parent)
    }


    companion object {
        const val KEY_TEAMNAME = "team_name"
        const val KEY_PARENT = "parent_company"
    }
}