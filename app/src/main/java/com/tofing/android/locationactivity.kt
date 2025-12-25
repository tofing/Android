package com.tofing.android

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class locationactivity : AppCompatActivity() {

    private lateinit var tvlatitude: TextView
    private lateinit var tvlongitude: TextView
    private lateinit var tvaltitude: TextView
    private lateinit var tvtime: TextView
    private lateinit var btnback: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location)

        tvlatitude = findViewById(R.id.tvlatitude)
        tvlongitude = findViewById(R.id.tvlongitude)
        tvaltitude = findViewById(R.id.tvaltitude)
        tvtime = findViewById(R.id.tvtime)
        btnback = findViewById(R.id.btnback)

        btnback.setOnClickListener { finish() }

        tvlatitude.text = "Широта: 55.7558"
        tvlongitude.text = "Долгота: 37.6173"
        tvaltitude.text = "Высота: 156 м"

        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val currentTime = dateFormat.format(Date())
        tvtime.text = "Время: $currentTime"

        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "GPS доступен", Toast.LENGTH_SHORT).show()
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                100
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 100 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Разрешение получено", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Разрешение на GPS не получено", Toast.LENGTH_SHORT).show()
        }
    }
}