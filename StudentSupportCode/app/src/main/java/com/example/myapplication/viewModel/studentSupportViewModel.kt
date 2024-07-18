package com.example.myapplication.viewModel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.data.Module
import com.example.myapplication.data.StudentModule
import com.example.myapplication.data.StudentUIState
import com.example.myapplication.events.studentLoginEvent
import com.example.myapplication.events.studentRegistrationEvent
import com.example.myapplication.events.studentUpdateProfileEvent
import com.example.myapplication.navidation.Screen
import com.example.myapplication.navidation.studentSupportRouter
import com.example.myapplication.userLatitude
import com.example.myapplication.userLongitude

import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class StudentSupportViewModel: ViewModel() {
    private val TAG = StudentSupportViewModel::class.simpleName


    val db = FirebaseFirestore.getInstance()

    var studentUIState = mutableStateOf(StudentUIState())

    val isUserLoggedIn: MutableLiveData<Boolean> = MutableLiveData()

    fun checkForActiveSession() {
        if (FirebaseAuth.getInstance().currentUser != null) {
            Log.d(TAG, "Valid Session")
            isUserLoggedIn.value = true
        } else {
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

    var studentList = mutableStateOf(ArrayList<StudentUIState>())

    fun login() {
        loginInProgress.value = true

        val email = loginUIState.value.loginEmail
        val password = loginUIState.value.loginPassword
        FirebaseAuth
            .getInstance()
            .signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                Log.d(TAG, "Login Success")
                Log.d(TAG, "${it.isSuccessful}")
                if (it.isSuccessful) {
                    loginInProgress.value = false
                    //take information from Firebase about user the user where usedID = authID
                    CoroutineScope(Dispatchers.IO).launch {
                        takeStudentDataFromFirebase()
                        }
                    studentSupportRouter.navigateTo(Screen.AskingStudySupportScreen)

                }
            }
            .addOnFailureListener {
                Log.d(TAG, "Inside_login_failure")
                Log.d(TAG, "${it.localizedMessage}")
                LoginErrorMessage.value = "Invalied Email or Password"
                loginInProgress.value = false
            }

    }

    //get List Of Modules from the Firebase

    val moduleList = mutableStateOf(ArrayList<Module>())

    suspend fun getModules() {
        val db = FirebaseFirestore.getInstance()
        try {
            val snapshot = db.collection("module").get().await()
            for (document in snapshot.documents) {
                document.toObject(Module::class.java)?.let { moduleList.value.add(
                    Module(moduleID = document.id,
                        moduleName = it.moduleName,)) }
            }
            Log.d(TAG, moduleList.value.toString())
        } catch (e: Exception) {
            println("Error fetching modules: ${e.message}")
        }
    }

    fun loginEvent(event: studentLoginEvent) {

        when (event) {

            is studentLoginEvent.studentLoginButtonClicked -> {

                homeLocation = LatLng(userLatitude, userLongitude)

                login()

            }

            is studentLoginEvent.studentLoginEmailChanged -> {
                loginUIState.value.loginEmail = event.email
            }

            is studentLoginEvent.studentLoginPasswordChanged -> {
                loginUIState.value.loginPassword = event.password
            }
        }
    }

    //take all data from user related to the loginId as User ID

    suspend fun takeStudentDataFromFirebase() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            try {
                val studentDoc = db.collection(("student")).document(userId).get().await()
                studentUIState.value =
                    studentDoc.toObject(StudentUIState::class.java) as StudentUIState
                val modulesResult =
                    db.collection("student").document(userId).collection("studentModules").get()
                        .await()
                for (document in modulesResult) {
                    val module = document.toObject(StudentModule::class.java)
                    studentUIState.value.modules.add(module)
                }
            } catch (e: Exception) {
                Log.d(TAG, "Error getting student data", e)
            }

        } else {
            Log.d(TAG, "User is not in system")
        }
    }



   suspend fun takeDataForAllStudentsFromFirebase(){
        try{
            val snapshot = db.collection("student").get().await()
            for (document in snapshot.documents){
                val student = document.toObject(StudentUIState::class.java)
                val modulesSnapshot = db.collection("student").document(document.id).collection("studentModules").get().await()
                val studentModules = mutableListOf<StudentModule>()
                for(moduleDocument in modulesSnapshot.documents) {
                    val module = moduleDocument.toObject(StudentModule::class.java) as StudentModule
                    studentModules.add(module)
                }
                if (student != null) {
                    student.modules = ArrayList(studentModules)
                }
                studentList.value.add(student as StudentUIState)
                Log.d(TAG, "Student: $student")
            }
        }catch (e: Exception){
            Log.d(TAG, "Error getting student data", e)
        }
    }

    ///
    /////////TODO: REGISTRATION RELATED CODE
    ///

    val registrationUIState = mutableStateOf(StudentUIState())

    val studentSelectedModule = mutableStateOf(StudentModule())

    val studentPassword = mutableStateOf("")
    val studentConfirmPassword = mutableStateOf("")



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
                registrationUIState.value.modules.add(studentSelectedModule.value)
            }
            is studentRegistrationEvent.StudentPasswordChanged -> {
                studentPassword.value = event.studentPassword
            }
            is studentRegistrationEvent.StudentConfirmPasswordChanger -> {
                studentConfirmPassword.value = event.studentConfirmPassword
            }

            is studentRegistrationEvent.StudentSelectedModuleToAddChanged ->{
                studentSelectedModule.value = studentSelectedModule.value.copy(
                    moduleName = event.selectedModuleToAdd.moduleName,
                    moduleID = event.selectedModuleToAdd.moduleID
                )
                Log.d(TAG, "Module Name: ${studentSelectedModule.value.moduleName}")
                Log.d(TAG, "Module ID: ${studentSelectedModule.value.moduleID}")
            }

            is studentRegistrationEvent.StudentSelectedModuleStatusChanged ->{
                studentSelectedModule.value = studentSelectedModule.value.copy(
                    moduleStatus = event.Status
                )
                Log.d(TAG, "Module Status: ${studentSelectedModule.value.moduleStatus}")
            }

            is studentRegistrationEvent.RegistrationButtonClicked -> {
                val studentModules = registrationUIState.value.modules
                //remove from studentModules the modules that are empty and modules that have same moduleId
                registrationUIState.value.modules = studentModules.distinctBy { it.moduleID } as ArrayList<StudentModule>
                registerStudentInFirebaseAuth()
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

        db.collection("student").document(registrationUIState.value.studentId)
            .set(student)
            .addOnSuccessListener {
                Log.d(TAG, "DocumentSnapshot added with ID: ${registrationUIState.value.studentId}")
                registrationUIState.value.studentId = registrationUIState.value.studentId
                addModulesToTheStudent()
            }
            .addOnFailureListener {
                Log.d(TAG, "Error adding document", it)
            }
    }

    fun addModulesToTheStudent(){
        val studentId = FirebaseAuth.getInstance().currentUser?.uid.toString()
        Log.d(TAG, "Student ID to create Modules in Collection: $studentId")
        if (studentId.isNotEmpty()) {
        for (module in registrationUIState.value.modules) {
                val studentModule = hashMapOf(
                    "moduleID" to module.moduleID,
                    "moduleName" to module.moduleName,
                    "moduleStatus" to module.moduleStatus
                )
                db.collection("student")
                    .document(studentId)
                    .collection("studentModules")
                    .document(module.moduleID).set(studentModule)
                    .addOnSuccessListener {
                        Log.d(TAG, "DocumentSnapshot added with ID: ${module.moduleID}")
                    }
                    .addOnFailureListener {
                        Log.d(TAG, "Error adding document", it)
                    }
            }
        }

        //logout and go back to Login Screen

        FirebaseAuth.getInstance().signOut()
        studentSupportRouter.navigateTo(Screen.LoginScreen)


    }


    ////
    ///////////TODO: UPDATE PROFILE CODE
    ////

    var showDialogAddStudyModule = mutableStateOf(false)

    fun updateProfileEvent(event: studentUpdateProfileEvent){

        when(event){
            is studentUpdateProfileEvent.studentUpdateProfileNameChanged -> {
                studentUIState.value = studentUIState.value.copy(studentName = event.studentName)
            }
            is studentUpdateProfileEvent.studentUpdateProfileSurnameChanged -> {
                studentUIState.value = studentUIState.value.copy(studentSurname = event.studentSurname)
            }
            is studentUpdateProfileEvent.studentUpdateProfileEmailChanged -> {
                studentUIState.value = studentUIState.value.copy(studentEmail = event.studentEmail)
            }
            is studentUpdateProfileEvent.studentUpdateProfilePhoneNumChanged -> {
                studentUIState.value = studentUIState.value.copy(studentPhoneNum = event.studentPhoneNum)
            }
            is studentUpdateProfileEvent.studentUpdateProfileMeetLocationLatitudeChanged -> {
                studentUIState.value = studentUIState.value.copy(meetLocationLatitude = event.meetLocationLatitude)
            }
            is studentUpdateProfileEvent.studentUpdateProfileMeetLocationLongitudeChanged -> {
                studentUIState.value = studentUIState.value.copy(meetLocationLongitude = event.meetLocationLongitude)
            }
            is studentUpdateProfileEvent.studentUpdateProfileButtonClicked -> {
                updateStudentProfile()
            }
        }
    }

    fun updateStudentProfile(){
        val studentName = studentUIState.value.studentName
        val studentSurname = studentUIState.value.studentSurname
        val studentEmail = studentUIState.value.studentEmail
        val studentPhoneNum = studentUIState.value.studentPhoneNum
        val meetLocationLatitude = studentUIState.value.meetLocationLatitude
        val meetLocationLongitude = studentUIState.value.meetLocationLongitude

        val student = hashMapOf(
            "studentName" to studentName,
            "studentSurname" to studentSurname,
            "studentEmail" to studentEmail,
            "studentPhoneNum" to studentPhoneNum,
            "meetLocationLatitude" to meetLocationLatitude,
            "meetLocationLongitude" to meetLocationLongitude
        )

        db.collection("student").document(studentUIState.value.studentId)
            .set(student)
            .addOnSuccessListener {
                Log.d(TAG, "DocumentSnapshot added with ID: ${studentUIState.value.studentId}")
            }
            .addOnFailureListener {
                Log.d(TAG, "Error adding document", it)
            }
    }


    ////
    ///////////TODO: Show Students Map Screen Code
    ////

    val limerick = LatLng(52.66, -8.63)
    var homeLocation = LatLng(userLatitude, userLongitude)
    val defaultCameraPosition = CameraPosition.fromLatLngZoom(limerick, 12f)

    val showSelectedStudentInfoWindow = mutableStateOf(false)

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