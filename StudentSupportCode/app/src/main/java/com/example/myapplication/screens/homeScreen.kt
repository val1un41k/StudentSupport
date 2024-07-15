package com.example.myapplication.screens

import android.app.Activity
import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.componens.ButtonComponent
import com.example.myapplication.componens.HeadingTextComponentWithLogOut
import com.example.myapplication.componens.HeadingTextComponentWithoutLogout
import com.example.myapplication.navidation.Screen
import com.example.myapplication.navidation.studentSupportRouter
import com.example.myapplication.viewModel.StudentSupportViewModel

@Composable
fun HomeScreen(studentSupportViewModel: StudentSupportViewModel = viewModel()) {

    //TODO: Take information about USer from Firebase
    //TODO: Take information about all users from Firebase

Surface(
    Modifier
        .fillMaxSize()
        .background(Color.White)
        .padding(12.dp)
){

    Column(
        modifier = Modifier.fillMaxSize()
    ){
        Spacer(modifier = Modifier.height(40.dp))

        HeadingTextComponentWithLogOut(value = "Home Screen")
        Spacer(modifier = Modifier.height(40.dp))

        ButtonComponent(value = "Update Profile", onButtonClicked = {
            studentSupportRouter.navigateTo(Screen.HomeScreen)
        })
        Spacer(modifier = Modifier.height(40.dp))

        ButtonComponent(value = "See Other Students", onButtonClicked = {
            studentSupportRouter.navigateTo(Screen.AskingStudySupportScreen) })
        Spacer(modifier = Modifier.height(80.dp))

        var closeApp = remember{ mutableStateOf(false)}
        ButtonComponent(value = "Close Application", onButtonClicked = {
            closeApp.value = true })

        if (closeApp.value){
            closeApplication()
        }
    }
}
}

@Composable
fun closeApplication(){
    Log.d(TAG, "Close Application")
    //terminate the application
    val context = LocalContext.current
    if (context is Activity){
        context.finish()
    }
}


@Composable
@Preview
fun HomeScreenPreview() {
    HomeScreen()
}