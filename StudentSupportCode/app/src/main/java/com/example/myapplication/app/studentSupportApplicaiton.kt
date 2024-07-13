package com.example.myapplication.app

import androidx.compose.animation.Crossfade
import androidx.compose.material3.Surface
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.navidation.Screen
import com.example.myapplication.navidation.studentSupportRouter
import com.example.myapplication.screens.AskingStudySupportScreen
import com.example.myapplication.screens.HomeScreen
import com.example.myapplication.screens.LoginScreen
import com.example.myapplication.screens.StudentRegistrationScreen
import com.example.myapplication.screens.UpdateProfileScreen
import com.example.myapplication.viewModel.StudentSupportViewModel

@Composable
fun studentSupportApplicaiton(studentSupportViewModel: StudentSupportViewModel = viewModel()) {
    studentSupportViewModel.checkForActiveSession()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {


        Crossfade(targetState = studentSupportRouter.currentScreen){
            when (it.value){
                is Screen.HomeScreen->{
                    HomeScreen()
                }
                is Screen.AskingStudySupportScreen ->{
                    AskingStudySupportScreen()
                }
                is Screen.LoginScreen->{
                    LoginScreen()
                }
                is Screen.StudentRegistrationScreen ->{
                    StudentRegistrationScreen()
                }
                is Screen.UpdateProfileScreen ->{
                    UpdateProfileScreen()
                }
                Screen.CreatingOfferSupportScreen -> TODO()
            }
        }

    }

}