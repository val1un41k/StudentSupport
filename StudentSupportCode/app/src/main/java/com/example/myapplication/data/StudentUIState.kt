package com.example.myapplication.data

data class StudentUIState (
    var studentId: String = "",
    var studentName: String = "",
    var studentSurname: String = "",
    var studentEmail: String = "",
    var studentPhoneNum: String = "",
    val studentPassword: String = "",
    var modules: ArrayList<StudentModule> = ArrayList()
)

data class StudentModule(
    val moduleName: String = "",
    var moduleStatus: String = "",

    )