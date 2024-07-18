package com.example.myapplication.navidation

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

sealed class Screen (val route: String) {

    object HomeScreen: Screen ( "HomeScreen")
    object AskingStudySupportScreen: Screen ("AskingStudySupportScreen")
    object CreatingOfferSupportScreen: Screen ("CreatingOfferToSupportScreen")

    object LoginScreen: Screen ("LoginScreen")
    object StudentRegistrationScreen: Screen ("StudentRegistrationScreen")
}

object studentSupportRouter{
    var currentScreen: MutableState<Screen> = mutableStateOf(Screen.LoginScreen)

    fun navigateTo(destination:Screen){
        currentScreen.value = destination
    }
}