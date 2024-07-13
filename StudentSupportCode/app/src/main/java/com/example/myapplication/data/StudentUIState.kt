package com.example.myapplication.data

data class StudentUIState (
    val studentId: String = "",
    val studentEmail: String = "",
    val studentPassword: String = "",
    val modules: ArrayList<Module> = ArrayList()
)