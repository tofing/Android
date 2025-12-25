package com.tofing.android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class calculatoractivity : AppCompatActivity() {

    private lateinit var tvdisplay: TextView
    private var currentnumber = ""
    private var firstnumber = ""
    private var operation = ""
    private var isnewoperation = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calculator)

        tvdisplay = findViewById(R.id.tvdisplay)

        findViewById<Button>(R.id.btn0).setOnClickListener { appendnumber("0") }
        findViewById<Button>(R.id.btn1).setOnClickListener { appendnumber("1") }
        findViewById<Button>(R.id.btn2).setOnClickListener { appendnumber("2") }
        findViewById<Button>(R.id.btn3).setOnClickListener { appendnumber("3") }
        findViewById<Button>(R.id.btn4).setOnClickListener { appendnumber("4") }
        findViewById<Button>(R.id.btn5).setOnClickListener { appendnumber("5") }
        findViewById<Button>(R.id.btn6).setOnClickListener { appendnumber("6") }
        findViewById<Button>(R.id.btn7).setOnClickListener { appendnumber("7") }
        findViewById<Button>(R.id.btn8).setOnClickListener { appendnumber("8") }
        findViewById<Button>(R.id.btn9).setOnClickListener { appendnumber("9") }

        findViewById<Button>(R.id.btnplus).setOnClickListener { setoperation("+") }
        findViewById<Button>(R.id.btnminus).setOnClickListener { setoperation("-") }
        findViewById<Button>(R.id.btnmultiply).setOnClickListener { setoperation("*") }
        findViewById<Button>(R.id.btndivide).setOnClickListener { setoperation("/") }

        findViewById<Button>(R.id.btndot).setOnClickListener { adddot() }
        findViewById<Button>(R.id.btnclear).setOnClickListener { clearall() }
        findViewById<Button>(R.id.btnequals).setOnClickListener { calculate() }

        findViewById<Button>(R.id.btnback).setOnClickListener {
            finish()
        }
    }

    private fun appendnumber(number: String) {
        if (isnewoperation) {
            currentnumber = number
            isnewoperation = false
        } else {
            currentnumber += number
        }
        tvdisplay.text = currentnumber
    }

    private fun setoperation(op: String) {
        if (currentnumber.isNotEmpty()) {
            if (firstnumber.isEmpty()) {
                firstnumber = currentnumber
                operation = op
                currentnumber = ""
                tvdisplay.text = "0"
            } else {
                calculate()
                operation = op
            }
        }
    }

    private fun adddot() {
        if (isnewoperation) {
            currentnumber = "0."
            isnewoperation = false
        } else if (!currentnumber.contains(".")) {
            currentnumber += "."
        }
        tvdisplay.text = currentnumber
    }

    private fun calculate() {
        if (firstnumber.isNotEmpty() && currentnumber.isNotEmpty() && operation.isNotEmpty()) {
            val num1 = firstnumber.toDouble()
            val num2 = currentnumber.toDouble()
            var result = 0.0

            when (operation) {
                "+" -> result = num1 + num2
                "-" -> result = num1 - num2
                "*" -> result = num1 * num2
                "/" -> {
                    if (num2 != 0.0) {
                        result = num1 / num2
                    } else {
                        tvdisplay.text = "error"
                        clearall()
                        return
                    }
                }
            }

            currentnumber = if (result % 1 == 0.0) {
                result.toInt().toString()
            } else {
                String.format("%.2f", result)
            }

            tvdisplay.text = currentnumber
            firstnumber = ""
            operation = ""
            isnewoperation = true
        }
    }

    private fun clearall() {
        currentnumber = ""
        firstnumber = ""
        operation = ""
        isnewoperation = true
        tvdisplay.text = "0"
    }
}