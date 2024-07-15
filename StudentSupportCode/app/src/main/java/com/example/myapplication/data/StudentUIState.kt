package com.example.myapplication.data

data class StudentUIState (
    var studentId: String = "",
    var studentName: String = "",
    var studentSurname: String = "",
    var studentEmail: String = "",
    var meetLocationLatitude: String = "",
    var meetLocationLongitude: String = "",
    var studentPhoneNum: String = "",
    var modules: ArrayList<StudentModule> = ArrayList()
)

data class StudentModule(
    val moduleID: String = "",
    val moduleName: String = "",
    var moduleStatus: String = "",

    )