package com.example.myapplication.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.R
import com.example.myapplication.componens.ButtonComponent
import com.example.myapplication.componens.HeadingTextComponentWithoutLogout
import com.example.myapplication.componens.ModulesDropDown
import com.example.myapplication.componens.MyTextFieldComponent
import com.example.myapplication.data.StudentModule
import com.example.myapplication.viewModel.StudentSupportViewModel
import dagger.Module

@Composable
fun StudentRegistrationScreen(studentSupportViewModel: StudentSupportViewModel = viewModel()) {

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(28.dp)
    ){
        Column(modifier = Modifier.fillMaxSize()){
                HeadingTextComponentWithoutLogout(value ="Student Registration Screen")
                Spacer(modifier = Modifier.height(20.dp))


                MyTextFieldComponent(labelValue = "First Name",
                    painterResource(R.drawable.message),
                    onTextChanged = {/*TODO*/})
                Spacer(modifier = Modifier.height(20.dp))


                MyTextFieldComponent(labelValue = "Last Name",
                    painterResource(R.drawable.message),
                    onTextChanged = {/*TODO*/})
                Spacer(modifier = Modifier.height(20.dp))


                MyTextFieldComponent(labelValue = "Email",
                    painterResource(R.drawable.message),
                    onTextChanged = {/*TODO*/})
                Spacer(modifier = Modifier.height(20.dp))


                MyTextFieldComponent(labelValue = "Password",
                    painterResource(R.drawable.lock),
                    onTextChanged = {/*TODO*/})
                Spacer(modifier = Modifier.height(20.dp))

                MyTextFieldComponent(labelValue = "Confirm Password",
                    painterResource(R.drawable.lock),
                    onTextChanged = {/*TODO*/})
                Spacer(modifier = Modifier.height(20.dp))

                //put dropdown menu of the courses and option to add

            Row(Modifier.fillMaxWidth()) {
                Column() {
                    val availableModules = ArrayList<com.example.myapplication.data.Module>()
                    val studentModules = ArrayList<StudentModule>()
                    val selectedModule = ModulesDropDown(elements = availableModules)

                    val selectedModuleStatus = moduleStatusDropDown(
                        elements = arrayListOf(
                            "Not Started",
                            "In Progress",
                            "Completed"
                        )
                    )
                    
                    ButtonComponent(value = "add Module", onButtonClicked = { /*TODO*/ })
                }
                Spacer(Modifier.height(20.dp))
            }

                ButtonComponent(value = "Register", onButtonClicked = { /*TODO*/
                //navigate to Create Profile Screen
                    // all taken data passed to the next screen
                })
                Spacer(modifier = Modifier.height(20.dp))

        }

    }


}




@Composable
fun moduleStatusDropDown(elements: ArrayList<String>): String {
    var expanded by remember { mutableStateOf(false) }
    var selectedModuleStatus by remember { mutableStateOf(elements[0]) }

    Box (modifier = Modifier.wrapContentSize()){
        Text(
            text = selectedModuleStatus,
            modifier = Modifier.clickable { expanded = true}
        )
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            elements.forEach {
                    status -> DropdownMenuItem(onClick = {
                selectedModuleStatus = status
                expanded = false
            }) {
                Text(text = status)
            }
            }
        }
    }
    return selectedModuleStatus
}

@Composable
@Preview
fun StudentRegistrationScreenPreview() {
    StudentRegistrationScreen()
}