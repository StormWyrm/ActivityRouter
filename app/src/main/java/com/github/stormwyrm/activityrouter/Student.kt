package com.github.stormwyrm.activityrouter

import android.os.Parcel
import android.os.Parcelable

data class Student(val age: Int, val name: String) : Parcelable {
    override fun toString(): String {
        return "Student(age=$age, name='$name')"
    }

    constructor(source: Parcel) : this(
        source.readInt(),
        source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeInt(age)
        writeString(name)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Student> = object : Parcelable.Creator<Student> {
            override fun createFromParcel(source: Parcel): Student = Student(source)
            override fun newArray(size: Int): Array<Student?> = arrayOfNulls(size)
        }
    }
}
