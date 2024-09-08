package com.example.calculatorapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.TypedValue
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import java.text.DecimalFormat

class MainActivity : AppCompatActivity() {
    private lateinit var resultText: EditText
    private lateinit var operationText: EditText
    private var operation: String = ""
    private var firstNumber: Double = 0.0
    private var secondNumber: Double = 0.0
    private var isSecondNumber: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        resultText = findViewById(R.id.result_text)
        operationText = findViewById(R.id.operation_text)

        val numberButtons = listOf<Button>(
            findViewById(R.id.button_zero),
            findViewById(R.id.button_one),
            findViewById(R.id.button_two),
            findViewById(R.id.button_three),
            findViewById(R.id.button_four),
            findViewById(R.id.button_five),
            findViewById(R.id.button_six),
            findViewById(R.id.button_seven),
            findViewById(R.id.button_eight),
            findViewById(R.id.button_nine)
        )

        val clearButton = findViewById<Button>(R.id.button_clear)
        val equalButton = findViewById<Button>(R.id.button_equal)
        val addButton = findViewById<Button>(R.id.button_add)
        val subtractButton = findViewById<Button>(R.id.button_subtract)
        val multiplyButton = findViewById<Button>(R.id.button_multiply)
        val divideButton = findViewById<Button>(R.id.button_divide)
        val percentButton = findViewById<Button>(R.id.button_percent)
        val dotButton = findViewById<Button>(R.id.button_dot)

        numberButtons.forEach { button ->
            button.setOnClickListener {
                appendNumber(button.text.toString())
            }
        }

        clearButton.setOnClickListener { clear() }
        equalButton.setOnClickListener { calculate() }
        addButton.setOnClickListener { setOperation("+") }
        subtractButton.setOnClickListener { setOperation("-") }
        multiplyButton.setOnClickListener { setOperation("*") }
        divideButton.setOnClickListener { setOperation("/") }
        percentButton.setOnClickListener { applyPercent() }
        dotButton.setOnClickListener { appendDot() }

        resultText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                adjustTextSize()
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun appendNumber(number: String) {
        if (resultText.text.toString() == "0") {
            resultText.setText(number)
        } else {
            resultText.append(number)
        }
    }

    private fun appendDot() {
        if (!resultText.text.contains(".")) {
            resultText.append(".")
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setOperation(op: String) {
        operation = op
        firstNumber = resultText.text.toString().toDouble()
        operationText.setText("${resultText.text} $op")
        resultText.setText("")
        isSecondNumber = true
    }

    @SuppressLint("SetTextI18n")
    private fun calculate() {
        if (isSecondNumber) {
            secondNumber = resultText.text.toString().toDouble()
            val result = when (operation) {
                "+" -> firstNumber + secondNumber
                "-" -> firstNumber - secondNumber
                "*" -> firstNumber * secondNumber
                "/" -> firstNumber / secondNumber
                else -> 0.0
            }
            resultText.setText(formatResult(result))
            operationText.setText("${formatResult(firstNumber)} $operation ${formatResult(secondNumber)} =")
            isSecondNumber = false
        }
    }

    private fun formatResult(result: Double): String {
        val formatter = DecimalFormat("#,###.##") // Format numbers with commas
        return if (result % 1 == 0.0) {
            formatter.format(result.toInt()) // Format integer numbers
        } else {
            formatter.format(result) // Format decimal numbers
        }
    }

    private fun applyPercent() {
        val currentNumber = resultText.text.toString().toDouble()
        resultText.setText(formatResult(currentNumber / 100))
    }

    private fun clear() {
        resultText.setText("0")
        operationText.setText("")
        firstNumber = 0.0
        secondNumber = 0.0
        operation = ""
        isSecondNumber = false
    }

    private fun adjustTextSize() {
        val textSize = when (resultText.text.length) {
            in 1..5 -> 48f
            in 6..10 -> 36f
            else -> 24f
        }
        resultText.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize)
    }
}
