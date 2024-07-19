package com.example.myapplication.screens

import android.content.ContentValues.TAG
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Surface
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberImagePainter
import com.example.myapplication.R
import com.example.myapplication.componens.ButtonComponent
import com.example.myapplication.componens.HeadingTextComponentWithLogOut
import com.example.myapplication.data.StudentModule
import com.example.myapplication.data.StudentUIState
import com.example.myapplication.navidation.Screen
import com.example.myapplication.navidation.studentSupportRouter
import com.example.myapplication.viewModel.StudentSupportViewModel
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerInfoWindow
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState

@Composable
fun AskingStudySupportScreen(studentSupportViewModel: StudentSupportViewModel = viewModel()) {
    val TAG = "AskingStudySupportScreen"
    Surface(
        Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(12.dp)
    )
    {
        Column(
            modifier = Modifier.fillMaxSize()
        ){
            
            HeadingTextComponentWithLogOut(value = "Seeing Other Students")
            Spacer(modifier = Modifier.height(10.dp))

            val isMapLoaded = remember { mutableStateOf(false) }

            val cameraPositionState = rememberCameraPositionState {
                position = studentSupportViewModel.defaultCameraPosition
            }
            Box(Modifier.fillMaxSize()){
                GoogleMapView(
                    studentSupportViewModel = studentSupportViewModel,
                    modifier = Modifier.matchParentSize(),
                    cameraPositionState = cameraPositionState,
                    onMapLoaded = {
                        isMapLoaded.value = true
                    }
                )
                {}
                if (!isMapLoaded.value){
                    androidx.compose.animation.AnimatedVisibility(
                        modifier = Modifier.matchParentSize(),
                        visible = !isMapLoaded.value,
                        enter = EnterTransition.None,
                        exit = fadeOut()
                    ){
                        CircularProgressIndicator(
                            modifier = Modifier
                                .background(MaterialTheme.colorScheme.background)
                                .wrapContentSize()
                        )

                    }
                }
            }
            Spacer(modifier = Modifier.height(15.dp))
        }
    }
}


@Preview
@Composable

fun AskingStudySupportScreenPreview(){
    AskingStudySupportScreen()
}

@Composable
fun GoogleMapView(
    studentSupportViewModel: StudentSupportViewModel = viewModel(),
    modifier: Modifier = Modifier,
    cameraPositionState: CameraPositionState = rememberCameraPositionState(),
    onMapLoaded: () -> Unit = {},
    content: @Composable () -> Unit = {}
) {

    var uiSettings by remember { mutableStateOf(MapUiSettings(compassEnabled = false)) }
    var shouldAnimateZoom by remember { mutableStateOf(true) }
    var ticker by remember { mutableStateOf(0) }
    var mapProperties by remember {
        mutableStateOf(MapProperties(mapType = MapType.NORMAL))
    }
    var mapVisible by remember { mutableStateOf(true) }

    if (mapVisible) {
        GoogleMap(
            modifier = modifier,
            cameraPositionState = cameraPositionState,
            properties = mapProperties,
            uiSettings = uiSettings,
            onMapLoaded = onMapLoaded,
            onPOIClick = {
                Log.d(TAG, "POI clicked: ${it.name}")
            }
        ) {
            // Drawing on the map is accomplished with a child-based API
            val markerClick: (Marker) -> Boolean = {
                Log.d(TAG, "${it.title} was clicked")
                cameraPositionState.projection?.let { projection ->
                    Log.d(TAG, "The current projection is: $projection")
                }
                false
            }

            //TODO Create Function that will populate  as Markers
           //TODO Check First then after remove the comment
            populateStudentsAsMarkers(studentSupportViewModel)

            Marker(
                state = rememberMarkerState(position = studentSupportViewModel.homeLocation),
                draggable = true,
                title = "Current Location",
                snippet = "Marker in Limerick",
                icon =bitmapDescriptorFromVector(LocalContext.current, R.drawable.pin2, 80, 80),
            )

            content()
        }

    }
}


@Composable
fun populateStudentsAsMarkers(studentSupportViewModel: StudentSupportViewModel = viewModel()){
    var TAG = "populateStudentsAsMarkers"
    studentSupportViewModel.studentList.value.forEach {student ->

        val position = LatLng(student.meetLocationLongitude.toDouble(),
            student.meetLocationLatitude.toDouble())
        Log.d(TAG, "Student: ${student.studentName} ${student.studentSurname} has position: $position")
        val markerState = rememberMarkerState(position = position)
        val icon = bitmapDescriptorFromVector(LocalContext.current, R.drawable.pin, 80, 80)


        MarkerInfoWindow(
            state = markerState,
            icon = icon,
            onInfoWindowClick = {
            }
        )
        { marker ->
            Box(
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colorScheme.onPrimary,
                        shape = RoundedCornerShape(10.dp, 10.dp, 10.dp, 10.dp)
                    )
                    .scale(0.7f),
            ) {
                LazyColumn(
                    modifier = Modifier.padding(2.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    item {
                        ClickedMarkerInfoWindowContent(student)
                        //put button that will lead to popped up window of artisan`s products
                        Spacer(modifier = Modifier.height(5.dp))
                    }

                }

            }
        }
        Log.d(TAG, "Marker: ${student.studentName} ${student.studentSurname} has been created")
    }
}


fun bitmapDescriptorFromVector(context: Context, vectorResId: Int, width: Int, height: Int): BitmapDescriptor? {
    val bitmap = BitmapFactory.decodeResource(context.resources, vectorResId)
    val scaledBitmap = Bitmap.createScaledBitmap(bitmap, width, height, false)
    return BitmapDescriptorFactory.fromBitmap(scaledBitmap)
}



@Composable
fun ClickedMarkerInfoWindowContent(student: StudentUIState){
    //.........................Spacer
    Spacer(modifier = Modifier.height(5.dp))
    //.........................Text: title
    // for each student module in studentModules take name and status and put it in the text on next line

    var studentModulesToSHow = ""

        for(module in student.modules){
            studentModulesToSHow += "\n Module: ${module.moduleName} \n Status: ${module.moduleStatus}"
        }


    Text(
        text = "Student name: ${student.studentName} ${student.studentSurname}" +

                "\n Student Phone Number: ${student.studentPhoneNum}" +

                "\n Student Email: ${student.studentEmail}" +
                "\n ${studentModulesToSHow}",


        textAlign = TextAlign.Left,
        modifier = Modifier
            .padding(top = 5.dp)
            .fillMaxWidth(),
        style = MaterialTheme.typography.headlineSmall,
        color = MaterialTheme.colorScheme.primary,
    )
}


