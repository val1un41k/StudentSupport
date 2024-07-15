package com.example.myapplication.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.R
import com.example.myapplication.componens.ButtonComponent
import com.example.myapplication.componens.HeadingTextComponentWithLogOut
import com.example.myapplication.componens.HeadingTextComponentWithoutLogout
import com.example.myapplication.componens.MyTextFieldComponent
import com.example.myapplication.navidation.Screen
import com.example.myapplication.navidation.studentSupportRouter
import com.example.myapplication.ui.theme.GrayColor
import com.example.myapplication.viewModel.StudentSupportViewModel

@Composable
fun LoginScreen(studentSupportViewModel: StudentSupportViewModel = viewModel()){

    //take from Firebase list of modules
    LaunchedEffect(key1=true){
        studentSupportViewModel.getModules()
    }

    Surface(
        Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(20.dp)
    ){
        Column( modifier = Modifier.fillMaxSize()){

            HeadingTextComponentWithoutLogout(value = "Login Screen")
            Spacer(modifier = Modifier.height(20.dp))

            MyTextFieldComponent(labelValue = "Email",
                painterResource(R.drawable.message),
                onTextChanged = {studentSupportViewModel.loginUIState.value.loginEmail = it})
            Spacer(modifier = Modifier.height(20.dp))

            MyTextFieldComponent(labelValue = "Password",
                painterResource(R.drawable.lock) ,
                onTextChanged = {studentSupportViewModel.loginUIState.value.loginPassword = it})
            Spacer(modifier = Modifier.height(20.dp))

            ButtonComponent(value = "Login",
                onButtonClicked = {studentSupportViewModel.login()})
            Spacer(modifier = Modifier.height(20.dp))

            HorizontalDivider()

            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "If you don`t have an account, please register",
            )
            Spacer(modifier = Modifier.height(20.dp))
            ButtonComponent(value = "Register",
                onButtonClicked = {
                    //navigate to Register Screen
                    studentSupportRouter.navigateTo(Screen.StudentRegistrationScreen)
                })
        }
    }
}

@Composable
@Preview
fun LoginScreenPreview(){
    LoginScreen()
}