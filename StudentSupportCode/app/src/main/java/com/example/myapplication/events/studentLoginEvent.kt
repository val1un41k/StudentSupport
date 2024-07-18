package com.example.myapplication.events

sealed class studentLoginEvent {

    data class studentLoginEmailChanged (val email: String) : studentLoginEvent()

    data class studentLoginPasswordChanged (val password: String) :studentLoginEvent()

    class studentLoginButtonClicked () : studentLoginEvent()


}