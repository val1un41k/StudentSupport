package com.example.myapplication.events

sealed class studentUpdateProfileEvent {

        data class studentUpdateProfileNameChanged(val studentName: String) : studentUpdateProfileEvent()

        data class studentUpdateProfileSurnameChanged(val studentSurname: String) : studentUpdateProfileEvent()

        data class studentUpdateProfileEmailChanged(val studentEmail: String) : studentUpdateProfileEvent()

        data class studentUpdateProfilePhoneNumChanged(val studentPhoneNum: String) : studentUpdateProfileEvent()

        data class studentUpdateProfileMeetLocationLatitudeChanged(val meetLocationLatitude: String) : studentUpdateProfileEvent()

        data class studentUpdateProfileMeetLocationLongitudeChanged(val meetLocationLongitude: String) : studentUpdateProfileEvent()

        class studentUpdateProfileButtonClicked : studentUpdateProfileEvent()
}