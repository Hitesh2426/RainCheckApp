package com.example.raincheckapp.ui.view

import android.Manifest
import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.location.LocationManager
import android.widget.DatePicker
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.raincheckapp.ui.components.WeatherCard
import com.example.raincheckapp.ui.viewmodel.MainViewModel
import com.example.raincheckapp.ui.viewmodel.UiState
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("MissingPermission")
@Composable
fun MainScreen(viewModel: MainViewModel = viewModel()) {
    val context = LocalContext.current
    val fusedLocationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
    val uiState by viewModel.uiState.collectAsState()

    var showDatePicker by remember { mutableStateOf(false) }
    var selectedDateText by remember { mutableStateOf("") }
    var permissionGranted by remember { mutableStateOf(false) }

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { permissions ->
            val fine = permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false
            val coarse = permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false
            permissionGranted = fine || coarse
            if (permissionGranted) {
                getLastLocation(fusedLocationClient, viewModel)
            }
        }
    )

    // Handle system UI
    val systemUiController = rememberSystemUiController()
    val colorScheme = MaterialTheme.colorScheme
    SideEffect {
        systemUiController.setStatusBarColor(colorScheme.primary)
    }

    // Request permissions on launch
    LaunchedEffect(Unit) {
        locationPermissionLauncher.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text("Rain Check Weather")
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = colorScheme.primary,
                    titleContentColor = colorScheme.onPrimary,
                    navigationIconContentColor = colorScheme.onPrimary
                )
            )
        },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                when (uiState) {
                    is UiState.Idle -> Text("Waiting for location and date selection...")
                    is UiState.Loading -> CircularProgressIndicator()
                    is UiState.Success -> {
                        val data = uiState as UiState.Success
                        Text("Location: ${data.cityName}", style = MaterialTheme.typography.titleMedium)
                        Spacer(modifier = Modifier.height(8.dp))
                        WeatherCard(weatherInfo = data.weatherInfo)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Date: ${data.selectedDate}")
                        Text("Last updated: ${data.lastUpdated}")
                    }
                    is UiState.Error -> {
                        val error = uiState as UiState.Error
                        Text("Error: ${error.message}", color = MaterialTheme.colorScheme.error)
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = { showDatePicker = true },
                    colors = ButtonDefaults.buttonColors(containerColor = colorScheme.primary)
                ) {
                    Text(
                        text = if (selectedDateText.isBlank()) "Select Date" else selectedDateText,
                        color = colorScheme.onPrimary
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        if (permissionGranted) {
                            getLastLocation(fusedLocationClient, viewModel)
                        } else {
                            locationPermissionLauncher.launch(
                                arrayOf(
                                    Manifest.permission.ACCESS_FINE_LOCATION,
                                    Manifest.permission.ACCESS_COARSE_LOCATION
                                )
                            )
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = colorScheme.secondary)
                ) {
                    Text("Refresh Weather", color = colorScheme.onSecondary)
                }
            }

            if (showDatePicker) {
                DatePickerDialog(
                    context,
                    { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
                        val calendar = Calendar.getInstance().apply {
                            set(year, month, dayOfMonth)
                        }
                        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                        val dateStr = sdf.format(calendar.time)
                        selectedDateText = dateStr
                        viewModel.selectDate(dateStr)

                        if (permissionGranted) {
                            getLastLocation(fusedLocationClient, viewModel)
                        }

                        showDatePicker = false
                    },
                    Calendar.getInstance().get(Calendar.YEAR),
                    Calendar.getInstance().get(Calendar.MONTH),
                    Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
                ).apply {
                    datePicker.minDate = System.currentTimeMillis() - 1000
                    datePicker.maxDate = System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000L
                }.show()
            }
        }
    )
}

@SuppressLint("MissingPermission")
private fun getLastLocation(
    fusedLocationClient: FusedLocationProviderClient,
    viewModel: MainViewModel
) {
    fusedLocationClient.lastLocation
        .addOnSuccessListener { location ->
            if (location != null) {
                viewModel.setLocation(location.latitude, location.longitude)
            } else {
                viewModel.setLocation(0.0, 0.0)
            }
        }
        .addOnFailureListener {
            // Optional: log error
        }
}