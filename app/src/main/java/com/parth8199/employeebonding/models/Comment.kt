package com.parth8199.employeebonding.models

import com.parse.ParseClassName
import com.parse.ParseObject

@ParseClassName("Comment")
class Comment : ParseObject() {

    fun getCommentText(): String? {
        return getString(KEY_COMMENTTEXT)
    }

    //TODO : To try typecasting directly in class methods. If possible, this will make things easier
    /*fun getCommentedByEmp() : Employee?{
        return getParseObject(KEY_COMMENTEDBY) as Employee
    }*/
    fun getCommentedByEmp(): ParseObject? {
        return getParseObject(KEY_COMMENTEDBY)
    }

    fun getCommentedInDiscus(): ParseObject? {
        return getParseObject(KEY_COMMENTEDIN)
    }

    fun setCommentText(cmtText: String) {
        put(KEY_COMMENTTEXT, cmtText)
    }

    fun setCommentedByEmp(emp: ParseObject) {
        put(KEY_COMMENTEDBY, emp)
    }

    fun setCommentedInDiscus(discus: ParseObject) {
        put(KEY_COMMENTEDIN, discus)
    }


    companion object {
        const val KEY_COMMENTTEXT = "comment_text"
        const val KEY_COMMENTEDBY = "commented_by"
        const val KEY_COMMENTEDIN = "commented_in"
    }
}