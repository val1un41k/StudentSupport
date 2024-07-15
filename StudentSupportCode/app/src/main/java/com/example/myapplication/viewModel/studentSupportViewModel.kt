package com.example.myapplication.viewModel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.data.Module
import com.example.myapplication.data.StudentModule
import com.example.myapplication.data.StudentUIState
import com.example.myapplication.events.studentRegistrationEvent
import com.example.myapplication.navidation.Screen
import com.example.myapplication.navidation.studentSupportRouter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class StudentSupportViewModel: ViewModel(){
    private val TAG = StudentSupportViewModel::class.simpleName

    var studentUIState = mutableStateOf(StudentUIState())

    val isUserLoggedIn: MutableLiveData<Boolean> = MutableLiveData()

    fun checkForActiveSession(){
        if (FirebaseAuth.getInstance().currentUser != null){
            Log.d(TAG, "Valid Session")
            isUserLoggedIn.value = true
        }else{
            Log.d(TAG, "Invalid session")
            isUserLoggedIn.value = false
        }
    }

    ///
    ///////TODO: LOGIN RELATED CODE
    ///

    val loginUIState = mutableStateOf(LoginUIState())

    var LoginErrorMessage = mutableStateOf("")

    var loginInProgress = mutableStateOf(false)

    fun login(){
        loginInProgress.value = true

        val email = loginUIState.value.loginEmail
        val password = loginUIState.value.loginPassword
        FirebaseAuth
            .getInstance()
            .signInWithEmailAndPassword(email, password)
            .addOnCompleteListener{
                Log.d(TAG, "Login Success")
                Log.d(TAG, "${it.isSuccessful}")
                if (it.isSuccessful){
                    loginInProgress.value = false
                    studentSupportRouter.navigateTo(Screen.HomeScreen)
                    //take information from Firebase about user the user
                }
            }
            .addOnFailureListener{
                Log.d(TAG, "Inside_login_failure")
                Log.d(TAG, "${it.localizedMessage}")
                LoginErrorMessage.value = "Invalied Email or Password"
                loginInProgress.value = false
            }

    }

    //get List Of Modules from the Firebase

    val moduleList = mutableStateOf(ArrayList<Module>())

    suspend fun getModules(){
        val db = FirebaseFirestore.getInstance()
        try{
            val snapshot = db.collection("module").get().await()
            for (document in snapshot.documents){
                document.toObject(Module::class.java)?.let { moduleList.value.add(it)}
            }
            Log.d(TAG, moduleList.value.toString())
        } catch (e: Exception){
            println("Error fetching modules: ${e.message}")
        }
    }

    ///
    /////////TODO: REGISTRATION RELATED CODE
    ///
    val registrationUIState = mutableStateOf(StudentUIState())

    val selectedModuleToAdd = mutableStateOf(Module())

    val selectedModuleStatus = mutableStateOf("")

    val studentSelectedModule = mutableStateOf(StudentModule())

    val studentSelectedModules = mutableStateOf(ArrayList<StudentModule>())

    val studentPassword = mutableStateOf("")
    val studentConfirmPassword = mutableStateOf("")

    val db = FirebaseFirestore.getInstance()

    fun onEvent(event: studentRegistrationEvent){
        when(event){

            is studentRegistrationEvent.StudentNameChanged -> {
                registrationUIState.value = registrationUIState.value.copy(studentName = event.studentName)
            }
            is studentRegistrationEvent.StudentSurnameChanged -> {
                registrationUIState.value = registrationUIState.value.copy (studentSurname = event.studentSurname)
            }
            is studentRegistrationEvent.StudentEmailChanged -> {
                registrationUIState.value = registrationUIState.value.copy (studentEmail = event.studentEmail)
            }
            is studentRegistrationEvent.StudentPhoneNumChanged -> {
                registrationUIState.value = registrationUIState.value.copy (studentPhoneNum = event.studentPhoneNum)
            }
            is studentRegistrationEvent.MeetLocationLatitudeChanged -> {
                registrationUIState.value = registrationUIState.value.copy (meetLocationLatitude = event.meetLocationLatitude)
            }
            is studentRegistrationEvent.MeetLocationLongitudeChanged -> {
                registrationUIState.value = registrationUIState.value.copy (meetLocationLongitude = event.meetLocationLongitude)
            }
            is studentRegistrationEvent.AddStudyModuleButtonClicked -> {
                studentSelectedModule.value = studentSelectedModule.value.copy(
                    moduleID = event.selectedModule.moduleID,
                    moduleName = event.selectedModule.moduleName,
                    moduleStatus = selectedModuleStatus.value
                )
                registrationUIState.value.modules.add(studentSelectedModule.value)
            }
            is studentRegistrationEvent.StudentPasswordChanged -> {
                studentPassword.value = event.studentPassword
            }
            is studentRegistrationEvent.StudentConfirmPasswordChanger -> {
                studentConfirmPassword.value = event.studentConfirmPassword
            }

            is studentRegistrationEvent.RegistrationButtonClicked -> {
                registerStudentInFirebaseAuth()

                studentPassword.value = ""
                studentConfirmPassword.value = ""
            }
            is studentRegistrationEvent.AddStudyModuleButtonClicked -> {
                showDialogAddStudyModule.value = true
            }


        }
    }



   fun registerStudentInFirebaseAuth(){
        val email = registrationUIState.value.studentEmail
        val password = studentPassword.value
        FirebaseAuth
            .getInstance()
            .createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener{
                Log.d(TAG, "Registration Success")
                Log.d(TAG, "${it.isSuccessful}")
                if (it.isSuccessful){
                    registrationUIState.value.studentId = FirebaseAuth.getInstance().currentUser?.uid.toString()
                    createStudentProfile()
                }
            }
            .addOnFailureListener{
                Log.d(TAG, "Inside_registration_failure")
                Log.d(TAG, "${it.localizedMessage}")
            }
    }

    fun createStudentProfile(){

        val studentName = registrationUIState.value.studentName
        val studentSurname = registrationUIState.value.studentSurname
        val studentEmail = registrationUIState.value.studentEmail
        val studentPhoneNumber = registrationUIState.value.studentPhoneNum
        val meetLocationLatitude = registrationUIState.value.meetLocationLatitude
        val meetLocationLongitude = registrationUIState.value.meetLocationLongitude

        val studentPassword = studentPassword.value

        val student = hashMapOf(
            "studentName" to studentName,
            "studentSurname" to studentSurname,
            "studentEmail" to studentEmail,
            "studentPassword" to studentPassword,

            "studentPhoneNum" to studentPhoneNumber,
            "meetLocationLatitude" to meetLocationLatitude,
            "meetLocationLongitude" to meetLocationLongitude
        )

        db.collection("student")
            .add(student)
            .addOnSuccessListener {
                Log.d(TAG, "DocumentSnapshot added with ID: ${it.id}")
                registrationUIState.value.studentId = it.id
                addModulesToTheStudent()
            }
            .addOnFailureListener {
                Log.d(TAG, "Error adding document", it)
            }
    }

    fun addModulesToTheStudent(){

        val studentID = registrationUIState.value.studentId
        for (module in studentSelectedModules.value){
            val studentModule = hashMapOf(
                "moduleID" to module.moduleID,
                "moduleName" to module.moduleName,
                "moduleStatus" to module.moduleStatus
            )
            db.collection("student").document(studentID)
                .collection("studentModules")
                .add(studentModule)
                .addOnSuccessListener {
                    Log.d(TAG, "DocumentSnapshot added with ID: ${it.id}")
                }
                .addOnFailureListener {
                    Log.d(TAG, "Error adding document", it)
                }
        }
        FirebaseAuth.getInstance().signOut().also {
            studentSupportRouter.navigateTo(Screen.LoginScreen)
        }
    }



    ////
    ///////////TODO: UPDATE PROFILE CODE
    ////

    var showDialogAddStudyModule = mutableStateOf(false)


}


data class LoginUIState(
    var loginEmail: String = "",
    var loginPassword: String = "",
    val loginConfirmPassword: String = "",
    val uid: String = "",

    val userExists: Boolean = true,
    val emailError: Boolean = true,
    val passwordError: Boolean = true,
)