package com.tofing.android

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class mainactivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hub)

        findViewById<Button>(R.id.btncalculator).setOnClickListener {
            val intent = Intent(this, calculatoractivity::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.btnplayer).setOnClickListener {
            val intent = Intent(this, playeractivity::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.btnlocation).setOnClickListener {
            val intent = Intent(this, locationactivity::class.java)
            startActivity(intent)
        }
    }
}