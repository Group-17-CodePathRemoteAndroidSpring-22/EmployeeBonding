package com.parth8199.employeebonding

import com.parse.ParseClassName
import com.parse.ParseObject

@ParseClassName("Organization")
class Organization : ParseObject() {
    fun getCompanyName(): String? {
        return getString(KEY_COMPANYNAME)
    }

    fun setCompanyName(cName: String) {
        put(KEY_COMPANYNAME, cName)
    }

    companion object {
        const val KEY_COMPANYNAME = "company_name"
    }
}