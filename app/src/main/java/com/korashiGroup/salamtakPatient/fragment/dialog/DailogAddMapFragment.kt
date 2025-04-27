package com.korashiGroup.salamtakPatient.fragment.dialog

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.navArgs
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.korashiGroup.salamtakPatient.R
import com.korashiGroup.salamtakPatient.databinding.DailogAddMapfragmentBinding
import com.korashiGroup.salamtakPatient.utils.Alert
import java.io.IOException
import java.util.Locale


class DailogAddMapFragment : BottomSheetDialogFragment(), OnMapReadyCallback,
    GoogleMap.OnMarkerClickListener {

    private lateinit var binding: DailogAddMapfragmentBinding
    private lateinit var mMap: GoogleMap
    private var currentlocation: Location? = null
    private var latLng: LatLng? = null
    private var fusedLocationProviderClient: FusedLocationProviderClient? = null
    private val args: DailogAddMapFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DailogAddMapfragmentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.mapview.onCreate(savedInstanceState)
        latLng = args.longitude?.let {
            args.latitude?.let { it1 ->
                LatLng(
                    it1.toDouble(),
                    it.toDouble()
                )
            }
        }
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireContext())
        if (isLocationPermissionAllowed()) {

            // call your function
            getUserLocation()

        } else     // request permission
        {

            requestLocationPermission()

        }
        binding.mapview.getMapAsync(this)
    }

    private fun requestLocationPermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(
                requireActivity(),
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ).toString(),
            )
        ) {

            // فى حالة تم رفضه قيل ذلك مره يعمل برمشن
            // explain why you need the permission
            // عمل بوب لتوضيح سبب البرمش
            //  ثم طلب البرمشن

            Alert.showMessage(
                requireContext(),
                R.string.weneedpermissiontoaccesslocation,
                R.string.worning,
                true,
                R.string.ok,
            ) { p0, _ ->
                p0?.dismiss()
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ),
                    1000
                )

            }


        } else {

            // request permission

            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                1000
            )

        }
    }


    var marker: Marker? = null
    private fun drawMarker() {


        val lastlocaton = latLng?.let {
            args.latitude?.let { it1 ->
                args.longitude?.let { it2 ->
                    LatLng(
                        it1.toDouble(),
                        it2.toDouble()
                    )
                }
            }
        }
        Log.e("location", "${lastlocaton?.latitude},${lastlocaton?.longitude}")
        if (lastlocaton == null || mMap == null) return
        lastlocaton?.let {
            if (marker == null)
                marker = lastlocaton?.let {
                    MarkerOptions().position(it).title(
                        "${
                            latLng?.latitude?.let { it1 ->
                                latLng?.longitude?.let { it2 ->
                                    getAddressFromLatLng(
                                        it1, it2
                                    )
                                }
                            }
                        }"
                    )
                }
                    ?.let { mMap.addMarker(it) }
            mMap.setOnMarkerClickListener(this)
            mMap.moveCamera(CameraUpdateFactory.newLatLng(it))
            if (marker != null)
                marker?.position =
                    lastlocaton?.let {
                        LatLng(
                            lastlocaton.latitude,
                            lastlocaton.longitude
                        )
                    }!!            //
            if (lastlocaton != null) {
                mMap.animateCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        LatLng(
                            lastlocaton.latitude,
                            lastlocaton.longitude
                        ), 12f
                    )
                )
            }
        }


    }

    @SuppressLint("MissingPermission")
    fun getUserLocation() {

        val task = fusedLocationProviderClient?.lastLocation
        task?.addOnSuccessListener { location ->

            if (location != null) {
                currentlocation = location
                drawMarker()
                Log.e("location", "${currentlocation?.latitude},${currentlocation?.longitude}")
            }
        }

    }

    fun isLocationPermissionAllowed(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            1000 -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() &&
                            grantResults[0] == PackageManager.PERMISSION_GRANTED) && grantResults[1] == PackageManager.PERMISSION_GRANTED
                ) {
                    // Permission is granted. Continue the action or workflow
                    // in your app.

                    getUserLocation()
                } else {
                    // Explain to the user that the feature is unavailable because
                    // the features requires a permission that the user has denied.
                    // At the same time, respect the user's decision. Don't link to
                    // system settings in an effort to convince the user to change
                    // their decision.

                    Toast.makeText(
                        requireContext(),
                        " can not access Location ",
                        Toast.LENGTH_SHORT
                    ).show()

                }
                return
            }


            // Add other 'when' lines to check for other
            // permissions this app might request.
            else -> {
                // Ignore all other requests.
            }
        }
    }

    override fun onMapReady(p0: GoogleMap) {
        mMap = p0
        drawMarker()
    }


    override fun onStart() {
        super.onStart()
        binding.mapview.onStart()
    }

    override fun onResume() {
        super.onResume()
        binding.mapview.onResume()
    }

    override fun onMarkerClick(p0: Marker): Boolean {
        Toast.makeText(requireContext(), " " + p0.title, Toast.LENGTH_LONG).show()
        return false
    }

    fun getAddressFromLatLng(lat: Double, lng: Double): String {
        val geocoder = Geocoder(requireContext(), Locale.getDefault())
        return try {
            val addresses = geocoder.getFromLocation(lat, lng, 1)
            if (!addresses.isNullOrEmpty()) {
                addresses[0].getAddressLine(0) // يعيد اسم الموقع الكامل
            } else {
                "الموقع غير معروف"
            }
        } catch (e: IOException) {
            "خطأ في جلب العنوان"
        }
    }

}