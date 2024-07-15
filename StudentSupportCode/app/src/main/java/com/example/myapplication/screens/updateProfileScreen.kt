package com.example.myapplication.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.R
import com.example.myapplication.componens.ButtonComponent
import com.example.myapplication.componens.DisplayOnlyTextField
import com.example.myapplication.componens.HeadingTextComponentWithLogOut
import com.example.myapplication.componens.ModulesDropDown
import com.example.myapplication.componens.MyTextFieldComponent
import com.example.myapplication.data.StudentModule
import com.example.myapplication.data.StudentUIState
import com.example.myapplication.viewModel.StudentSupportViewModel

@Composable
fun UpdateProfileScreen(studentSupportViewModel: StudentSupportViewModel = viewModel()) {

    var showDialog = remember{ mutableStateOf(false)}

    Surface(
       modifier = Modifier
           .fillMaxSize()
           .background(Color.White)
           .padding(28.dp)){
        
        Column {
            HeadingTextComponentWithLogOut(value ="Update Profile Screen")
            Spacer(modifier = Modifier.height(10.dp))

            //TODO: CHANGEME:

            val student = remember{ mutableStateOf(StudentUIState())}
            student.value.studentId = "123"
            student.value.studentName = "John"
            student.value.studentSurname = "White"
            student.value.studentEmail = "student@123.ie"
            student.value.studentPhoneNum = "1234567890"
            student.value.modules = ArrayList<StudentModule>()

            //TODO: Change Data to Collected from Firebase

            MyTextFieldComponent(labelValue =  student.value.studentName,
                painterResource(R.drawable.message),
                onTextChanged = {/*TODO*/})
            Spacer(modifier = Modifier.height(20.dp))

            MyTextFieldComponent(labelValue = student.value.studentSurname,
                painterResource(R.drawable.message),
                onTextChanged = {/*TODO*/})
            Spacer(modifier = Modifier.height(20.dp))

            MyTextFieldComponent(labelValue = student.value.studentPhoneNum,
                painterResource(R.drawable.message),
                onTextChanged = {/*TODO*/})
            Spacer(modifier = Modifier.height(20.dp))

            ButtonComponent(value = "Add Study Module",
                onButtonClicked = { /*TODO*/
                //open popped up window - add module
                    showDialog.value = true
                })

            if (showDialog.value) {
                addStudyModuleDialogWindow() }

            Spacer(modifier = Modifier.height(20.dp))


            ButtonComponent(value = "Back to Home Screen",
                onButtonClicked = { //
                //navigate back to HomeScreen

                })
        }
    }
}

@Composable
fun addStudyModuleDialogWindow(studentSupportViewModel: StudentSupportViewModel = viewModel()){

Dialog(onDismissRequest = {studentSupportViewModel.showDialogAddStudyModule.value = false}){

    Surface(
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier.background(Color.White),
        elevation = 8.dp
    ){
        Column(modifier = Modifier.padding(16.dp)){

            //availableModules = availableModulesFromFirebase - studentModules

         //   val selectedModule = ModulesDropDown(elements = availableModules)

         var moduleStatus = moduleStatusDropDown(elements = arrayListOf("Completed", "In Progress", "Not Started"))

            val selectedModule = remember{ mutableStateOf(StudentModule())}

            selectedModule.value.moduleStatus = moduleStatus

            ButtonComponent(value = "Add Module",
                onButtonClicked = {
                    //add module to studentModules
                    //update studentModules in Firebase
                })
            Spacer(modifier = Modifier.height(20.dp))

            ButtonComponent(value = "Close", onButtonClicked = { studentSupportViewModel.showDialogAddStudyModule.value = false })


        }
    }

}

}

