package ca.georgebrown.babaruser37.lab3

import android.content.Context

data class Course(val code: String,
                 val name: String)

fun loadCourses(context: Context): List<Course>{

    val codes = context.resources.getStringArray(R.array.Full_Name)
    val names = context.resources.getStringArray(R.array.message)

    return codes.zip(names){
        code, name -> Course(code, name)
    }
}