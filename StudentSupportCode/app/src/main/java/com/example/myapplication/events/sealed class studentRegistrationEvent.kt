package com.example.myapplication.events

import com.example.myapplication.data.StudentModule

sealed class studentRegistrationEvent {
    data class StudentNameChanged(val studentName: String) : studentRegistrationEvent()

    data class StudentSurnameChanged(val studentSurname: String) : studentRegistrationEvent()

    data class StudentEmailChanged(val studentEmail: String) : studentRegistrationEvent()

    data class StudentPhoneNumChanged(val studentPhoneNum: String) : studentRegistrationEvent()

    data class MeetLocationLatitudeChanged(val meetLocationLatitude: String) : studentRegistrationEvent()

    data class MeetLocationLongitudeChanged(val meetLocationLongitude: String) : studentRegistrationEvent()

    data class StudentPasswordChanged(val studentPassword: String) : studentRegistrationEvent()

    data class StudentConfirmPasswordChanger(val studentConfirmPassword: String) : studentRegistrationEvent()

    data class AddStudyModuleButtonClicked(val selectedModule: StudentModule) : studentRegistrationEvent()

    sealed class RegistrationButtonClicked : studentRegistrationEvent()

}