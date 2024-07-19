package com.example.myapplication

import android.Manifest
import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.app.ActivityCompat
import com.example.myapplication.app.studentSupportApplicaiton
import com.google.firebase.FirebaseApp

var userLongitude = 0.0
var userLatitude = 0.0

class MainActivity : ComponentActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.FOREGROUND_SERVICE,
                Manifest.permission.FOREGROUND_SERVICE_LOCATION,
            ),
            0
        )
        enableEdgeToEdge()

        setContent {
            Intent(applicationContext, LocationService::class.java).apply {
                action = LocationService.ACTION_START
                startService(this)
            }
            Log.d("MainActivity", "Location: $userLatitude, $userLongitude")
            studentSupportApplicaiton()
        }
    }
}


    class studentSupportApp : Application() {
        override fun onCreate() {
            super.onCreate()
            // Initialize Firebase
            FirebaseApp.initializeApp(this)
        }
    }
