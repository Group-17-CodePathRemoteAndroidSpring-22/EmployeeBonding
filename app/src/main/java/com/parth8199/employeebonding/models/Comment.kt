package com.parth8199.employeebonding.models

import com.parse.ParseClassName
import com.parse.ParseObject

@ParseClassName("Comment")
class Comment : ParseObject() {

    fun getCommentText(): String? {
        return getString(KEY_COMMENTTEXT)
    }

    fun getCommentedByEmp(): Employee? {
        return getParseObject(KEY_COMMENTEDBY) as Employee
    }

    fun getCommentedInDiscus(): Discussion? {
        return getParseObject(KEY_COMMENTEDIN) as Discussion
    }

    fun setCommentText(cmtText: String) {
        put(KEY_COMMENTTEXT, cmtText)
    }

    fun setCommentedByEmp(emp: Employee) {
        put(KEY_COMMENTEDBY, emp)
    }

    fun setCommentedInDiscus(discus: Discussion) {
        put(KEY_COMMENTEDIN, discus)
    }


    companion object {
        const val KEY_COMMENTTEXT = "comment_text"
        const val KEY_COMMENTEDBY = "commented_by"
        const val KEY_COMMENTEDIN = "commented_in"
    }
}