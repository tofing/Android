package com.example.myapplication

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class CalculatorActivity : AppCompatActivity() {

    private var currentInput = ""
    private var operator = ""
    private var firstValue = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calculator)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom)
            insets
        }

        val tvResult: TextView = findViewById(R.id.tvResult)
        val tvInput: TextView = findViewById(R.id.tvInput)

        findViewById<Button>(R.id.btnBackToMenu).setOnClickListener { finish() }

        fun bindDigit(id: Int, digit: String) {
            findViewById<Button>(id).setOnClickListener {
                currentInput += digit
                tvResult.text = currentInput
            }
        }
        bindDigit(R.id.btn0, "0")
        bindDigit(R.id.btn1, "1")
        bindDigit(R.id.btn2, "2")
        bindDigit(R.id.btn3, "3")
        bindDigit(R.id.btn4, "4")
        bindDigit(R.id.btn5, "5")
        bindDigit(R.id.btn6, "6")
        bindDigit(R.id.btn7, "7")
        bindDigit(R.id.btn8, "8")
        bindDigit(R.id.btn9, "9")

        findViewById<Button>(R.id.btnDot).setOnClickListener {
            if (!currentInput.contains(".")) {
                currentInput = if (currentInput.isEmpty()) "0." else "$currentInput."
                tvResult.text = currentInput
            }
        }

        fun setOperator(op: String, label: String) {
            if (currentInput.isEmpty() && operator.isEmpty()) return
            if (operator.isNotEmpty() && currentInput.isNotEmpty()) {
                val second = currentInput.toDoubleOrNull() ?: 0.0
                firstValue = applyOp(firstValue, second, operator)
            } else {
                firstValue = currentInput.toDoubleOrNull() ?: firstValue
            }
            operator = op
            tvInput.text = "${format(firstValue)} $label"
            currentInput = ""
            tvResult.text = format(firstValue)
        }

        findViewById<Button>(R.id.btnPlus).setOnClickListener { setOperator("+", "+") }
        findViewById<Button>(R.id.btnMinus).setOnClickListener { setOperator("-", "-") }
        findViewById<Button>(R.id.btnMultiply).setOnClickListener { setOperator("*", "*") }
        findViewById<Button>(R.id.btnDivide).setOnClickListener { setOperator("/", "/") }

        findViewById<Button>(R.id.btnPercent).setOnClickListener {
            val v = currentInput.toDoubleOrNull() ?: 0.0
            currentInput = format(v / 100.0)
            tvResult.text = currentInput
        }

        findViewById<Button>(R.id.btnPlusMinus).setOnClickListener {
            if (currentInput.isEmpty()) return@setOnClickListener
            currentInput = if (currentInput.startsWith("-")) {
                currentInput.substring(1)
            } else {
                "-$currentInput"
            }
            tvResult.text = currentInput
        }

        findViewById<Button>(R.id.btnEquals).setOnClickListener {
            val second = currentInput.toDoubleOrNull() ?: 0.0
            val result = applyOp(firstValue, second, operator)
            tvResult.text = format(result)
            tvInput.text = ""
            currentInput = format(result)
            operator = ""
        }

        findViewById<Button>(R.id.btnClear).setOnClickListener {
            currentInput = ""
            firstValue = 0.0
            operator = ""
            tvResult.text = "0"
            tvInput.text = ""
        }
    }

    private fun applyOp(a: Double, b: Double, op: String): Double = when (op) {
        "+" -> a + b
        "-" -> a - b
        "*" -> a * b
        "/" -> if (b != 0.0) a / b else 0.0
        else -> b
    }

    private fun format(v: Double): String =
        if (v == v.toLong().toDouble()) v.toLong().toString() else v.toString()
}