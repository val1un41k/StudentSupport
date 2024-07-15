package com.example.myapplication.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
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
import com.example.myapplication.events.studentRegistrationEvent
import com.example.myapplication.navidation.Screen
import com.example.myapplication.navidation.studentSupportRouter
import com.example.myapplication.viewModel.StudentSupportViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.Module

@Composable
fun StudentRegistrationScreen(studentSupportViewModel: StudentSupportViewModel = viewModel()) {


    var studentPassword = rememberSaveable { mutableStateOf("") }
    val confirmPassword = rememberSaveable { mutableStateOf("") }
    val stidentSelectedModules = rememberSaveable { mutableStateOf(ArrayList<StudentModule>()) }


    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(28.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            LazyColumn() {
                item {
                    HeadingTextComponentWithoutLogout(value = "Student Registration Screen")
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(text = "First Name")
                    MyTextFieldComponent(labelValue = studentSupportViewModel.registrationUIState.value.studentName,
                        painterResource(R.drawable.message),
                        onTextChanged = {
                            studentSupportViewModel.onEvent(
                                studentRegistrationEvent.StudentNameChanged(
                                    it
                                )
                            )
                        })
                    Spacer(modifier = Modifier.height(20.dp))


                    Text(text = "Second Name")
                    MyTextFieldComponent(labelValue = studentSupportViewModel.registrationUIState.value.studentSurname,
                        painterResource(R.drawable.message),
                        onTextChanged = {
                            studentSupportViewModel.onEvent(
                                studentRegistrationEvent.StudentSurnameChanged(
                                    it
                                )
                            )
                        })
                    Spacer(modifier = Modifier.height(20.dp))


                    Text(text = "Phone Number")
                    MyTextFieldComponent(labelValue = studentSupportViewModel.registrationUIState.value.studentPhoneNum,
                        painterResource(R.drawable.message),
                        onTextChanged = {
                            studentSupportViewModel.onEvent(
                                studentRegistrationEvent.StudentPhoneNumChanged(
                                    it
                                )
                            )
                        })
                    Spacer(modifier = Modifier.height(20.dp))


                    Text(text = "Email")
                    MyTextFieldComponent(labelValue = studentSupportViewModel.registrationUIState.value.studentEmail,
                        painterResource(R.drawable.message),
                        onTextChanged = {
                            studentSupportViewModel.onEvent(
                                studentRegistrationEvent.StudentEmailChanged(
                                    it
                                )
                            )
                        })
                    Spacer(modifier = Modifier.height(20.dp))



                    Text(text = "Place To Meet")
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(text = "location Longitude")
                    MyTextFieldComponent(labelValue = studentSupportViewModel.registrationUIState.value.meetLocationLongitude,
                        painterResource(R.drawable.message),
                        onTextChanged = {
                            studentSupportViewModel.onEvent(
                                studentRegistrationEvent.MeetLocationLongitudeChanged(
                                    it
                                )
                            )
                        })
                    Spacer(modifier = Modifier.height(20.dp))


                    Text(text = "Location Latitude")
                    MyTextFieldComponent(labelValue = studentSupportViewModel.registrationUIState.value.meetLocationLatitude,
                        painterResource(R.drawable.message),
                        onTextChanged = {
                            studentSupportViewModel.onEvent(
                                studentRegistrationEvent.MeetLocationLatitudeChanged(
                                    it
                                )
                            )
                        })
                    Spacer(modifier = Modifier.height(20.dp))


                    Text(text = "Password")
                    MyTextFieldComponent(labelValue = studentSupportViewModel.studentPassword.value,
                        painterResource(R.drawable.lock),
                        onTextChanged = {
                            studentSupportViewModel.onEvent(
                                studentRegistrationEvent.StudentPasswordChanged(
                                    it
                                )
                            )
                        })
                    Spacer(modifier = Modifier.height(20.dp))


                    val revealConfirmPasswordText = remember { mutableStateOf(false) }
                    MyTextFieldComponent(labelValue = studentSupportViewModel.studentConfirmPassword.value,
                        painterResource(R.drawable.lock),
                        onTextChanged = {
                            studentSupportViewModel.onEvent(
                                studentRegistrationEvent.StudentConfirmPasswordChanger(
                                    it
                                )
                            )
                            revealConfirmPasswordText.value = true
                        })
                    Spacer(modifier = Modifier.height(20.dp))


                    //put dropdown menu of the courses and option to add

                    Spacer(modifier = Modifier.height(10.dp))
                    Row(Modifier.fillMaxWidth()) {
                        Column {
                            val studentModules = studentSupportViewModel.moduleList.value
                            studentSupportViewModel.selectedModuleToAdd.value =
                                ModulesDropDown(elements = studentModules)
                            Spacer(modifier = Modifier.height(10.dp))
                            studentSupportViewModel.selectedModuleStatus.value =
                                moduleStatusDropDown(
                                    elements = arrayListOf(
                                        "Not Started",
                                        "In Progress",
                                        "Completed"
                                    )
                                )
                            Spacer(modifier = Modifier.height(10.dp))
                            ButtonComponent(value = "add Module", onButtonClicked = {

                                studentSupportViewModel.onEvent(
                                    studentRegistrationEvent.AddStudyModuleButtonClicked(
                                        StudentModule(
                                            studentSupportViewModel.selectedModuleToAdd.value.moduleID,
                                            studentSupportViewModel.selectedModuleToAdd.value.moduleName,
                                            studentSupportViewModel.selectedModuleStatus.value
                                        )
                                    )
                                )
                            })
                        }
                        Spacer(Modifier.height(20.dp))
                    }

                    var showText = remember { mutableStateOf(false) }
                    ButtonComponent(value = "Register", onButtonClicked = {

//                        if (
//                            studentSupportViewModel.registrationUIState.value.studentName.isNotEmpty() &&
//                            studentSupportViewModel.registrationUIState.value.studentSurname.isNotEmpty() &&
//                            studentSupportViewModel.registrationUIState.value.studentEmail.isNotEmpty() &&
//                            studentSupportViewModel.registrationUIState.value.meetLocationLatitude.isNotEmpty() &&
//                            studentSupportViewModel.registrationUIState.value.meetLocationLongitude.isNotEmpty() &&
//                            studentSupportViewModel.registrationUIState.value.studentPhoneNum.isNotEmpty() &&
//                            studentSupportViewModel.studentSelectedModules.value.isNotEmpty() &&
//                            studentSupportViewModel.studentPassword.value.isNotEmpty() &&
//                            studentSupportViewModel.studentConfirmPassword.value.isNotEmpty() &&
//                            studentSupportViewModel.studentPassword.value.equals(studentSupportViewModel.studentConfirmPassword.value)
//                        ) {
                            studentSupportViewModel.studentPassword.value = studentPassword.value

                            studentSupportViewModel.registerStudentInFirebaseAuth()

//                        } else {
//                            //show error message please fulfill all fields for registration
//                            showText.value = true
//                        }
                    })

                    if (showText.value) Text(text = "Please Fulfill All Fields for Registration or Password Not Match")
                    Spacer(modifier = Modifier.height(20.dp))

                }
            }
        }
    }
}


@Composable
fun moduleStatusDropDown(elements: ArrayList<String>): String {
    var expanded by remember { mutableStateOf(false) }
    var selectedModuleStatus by remember { mutableStateOf(elements[0]) }

    Box (modifier = Modifier
        .wrapContentSize()
        .padding(8.dp)
        .background(Color.LightGray, shape = RoundedCornerShape(4.dp))
        .border(1.dp, Color.Gray, RoundedCornerShape(4.dp))
        .widthIn(min = 200.dp)){
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