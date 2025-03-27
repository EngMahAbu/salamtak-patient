package com.korashiGroup.salamtakPatient.viewmodel

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class LocationViewModel @Inject constructor(
    private val fusedLocationProviderClient: FusedLocationProviderClient,
    @ApplicationContext private val context: Context
):ViewModel() {

    private val _locationLiveData = MutableLiveData<LatLng?>()
    val locationLiveData: LiveData<LatLng?> get() = _locationLiveData

    private val _permissionStatus = MutableLiveData<Boolean>()
    val permissionStatus: LiveData<Boolean> get() = _permissionStatus

    fun checkLocationPermission(): Boolean {
        val hasPermission = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
        _permissionStatus.postValue(hasPermission)
        return hasPermission
    }

    fun getCurrentLocation() {
        if (!checkLocationPermission()) return

        fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
            location?.let {
                val latLng = LatLng(it.latitude, it.longitude)
                _locationLiveData.postValue(latLng)
            }
        }
    }
}