package com.example.assisgnment1

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toolbar
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.assisgnment1.ui.theme.Assisgnment1Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContentView(R.layout.activity_main)

        val calcButton = findViewById<Button>(R.id.calcButton)

        calcButton.setOnClickListener {
            val taxRateInput = findViewById<EditText>(R.id.taxRateInput)
            val hourlyRateInput = findViewById<EditText>(R.id.hourlyRateInput)
            val hoursWorkedInput = findViewById<EditText>(R.id.hoursWorkedInput)

            val taxRate = taxRateInput.text.toString().toDouble()
            val hourlyRate = hourlyRateInput.text.toString().toDouble()
            val hoursWorked = hoursWorkedInput.text.toString().toDouble()

            val resultTV = findViewById<TextView>(R.id.resultTV)

            val paymentStats = calcTax(
                hoursWorked = hoursWorked,
                hourlyRate = hourlyRate,
                taxRate = taxRate
            )

            val pay = paymentStats[0]
            val overtimePay = paymentStats[1]
            val tax = paymentStats[2]
            val totalPay = paymentStats[3]

            "Pay: $pay\nOvertime Pay: $overtimePay\nTax: $tax\nTotal Pay: $totalPay".also { resultTV.text = it }

        }
    }
}


fun calcTax(hoursWorked: Double, hourlyRate: Double, taxRate: Double): List<Any> {
    val pay = hoursWorked * hourlyRate;
    val overtimePay: Double = if (hoursWorked <= 40) {
        0.0;
    } else {
        (hoursWorked - 40) * (hourlyRate * 1.5)
    }
    val tax = pay * taxRate
    val totalPay = pay + overtimePay - tax
    val paymentStats = listOf(pay, overtimePay, tax, totalPay)
    return paymentStats

}