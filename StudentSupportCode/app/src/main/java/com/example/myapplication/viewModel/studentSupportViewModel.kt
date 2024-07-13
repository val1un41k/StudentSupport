package com.example.myapplication.viewModel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.data.StudentUIState
import com.google.firebase.auth.FirebaseAuth

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
                }
            }
            .addOnFailureListener{
                Log.d(TAG, "Inside_login_failure")
                Log.d(TAG, "${it.localizedMessage}")
                LoginErrorMessage.value = "Invalied Email or Password"
                loginInProgress.value = false
            }

    }


}


data class LoginUIState(
    val loginEmail: String = "",
    val loginPassword: String = "",
    val loginConfirmPassword: String = "",
    val uid: String = "",

    val userExists: Boolean = true,
    val emailError: Boolean = true,
    val passwordError: Boolean = true,
)