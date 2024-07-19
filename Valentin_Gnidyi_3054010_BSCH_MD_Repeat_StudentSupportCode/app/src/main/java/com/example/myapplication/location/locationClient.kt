package com.example.myapplication.location

import android.location.Location
import kotlinx.coroutines.flow.Flow

interface LocationClient {

    fun getLocationUpdates(internal: Long): Flow<Location>

    class LocationException(message: String): Exception()

}