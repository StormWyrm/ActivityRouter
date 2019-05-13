package com.github.stormwyrm.activityrouter

import java.io.Serializable

data class Teacher(val age : Int,val name : String) : Serializable{
    override fun toString(): String {
        return "Teacher(age=$age, name='$name')"
    }
}
