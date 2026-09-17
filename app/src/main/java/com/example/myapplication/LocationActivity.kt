package com.example.myapplication

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.text.SimpleDateFormat
import java.util.*

class LocationActivity : AppCompatActivity(), LocationListener {

    companion object {
        private const val PERMISSION_REQUEST_LOCATION = 100
    }

    private lateinit var locationManager: LocationManager

    private lateinit var tvLat: TextView
    private lateinit var tvLon: TextView
    private lateinit var tvAlt: TextView
    private lateinit var tvTime: TextView
    private lateinit var btnBack: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_location)

        tvLat = findViewById(R.id.tvLat)
        tvLon = findViewById(R.id.tvLon)
        tvAlt = findViewById(R.id.tvAlt)
        tvTime = findViewById(R.id.tvTime)
        btnBack = findViewById(R.id.btnBackToMenu)

        btnBack.setOnClickListener { finish() }

        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        if (!checkPermissions()) {
            requestPermissions()
        } else {
            startLocation()
        }
    }

    private fun startLocation() {
        if (!isLocationEnabled()) {
            Toast.makeText(this, "Включите геолокацию", Toast.LENGTH_LONG).show()
            return
        }

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) return

        val lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        lastLocation?.let { updateUI(it) }

        locationManager.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            1000L,
            1f,
            this
        )
    }

    override fun onLocationChanged(location: Location) {
        updateUI(location)
    }

    private fun updateUI(location: Location) {
        tvLat.text = "Latitude: ${location.latitude}"
        tvLon.text = "Longitude: ${location.longitude}"
        tvAlt.text = "Altitude: ${location.altitude} m"

        val time = SimpleDateFormat("HH:mm:ss dd.MM.yyyy", Locale.getDefault())
            .format(Date(location.time))
        tvTime.text = "Time: $time"
    }

    private fun isLocationEnabled(): Boolean {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    private fun checkPermissions(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ),
            PERMISSION_REQUEST_LOCATION
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == PERMISSION_REQUEST_LOCATION &&
            grantResults.all { it == PackageManager.PERMISSION_GRANTED }
        ) {
            startLocation()
        }
    }
}