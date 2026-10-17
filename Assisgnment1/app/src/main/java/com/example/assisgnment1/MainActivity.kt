package com.example.assisgnment1

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Set up the Toolbar as ActionBar
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        // Setup calculation logic when the calculate button is clicked
        val calcButton = findViewById<Button>(R.id.calcButton)
        calcButton.setOnClickListener {
            val taxRateInput = findViewById<EditText>(R.id.taxRateInput)
            val hourlyRateInput = findViewById<EditText>(R.id.hourlyRateInput)
            val hoursWorkedInput = findViewById<EditText>(R.id.hoursWorkedInput)

            // Safely parse input values or default to 0.0 if empty/invalid
            val taxRate = taxRateInput.text.toString().toDoubleOrNull() ?: 0.0
            val hourlyRate = hourlyRateInput.text.toString().toDoubleOrNull() ?: 0.0
            val hoursWorked = hoursWorkedInput.text.toString().toDoubleOrNull() ?: 0.0

            val resultTV = findViewById<TextView>(R.id.resultTV)

            val paymentStats = calcTax(
                hoursWorked = hoursWorked,
                hourlyRate = hourlyRate,
                taxRate = taxRate
            )

            val pay = paymentStats[0]
            val overtimePay = paymentStats[1]
            val totalPay = paymentStats[2]
            val tax = paymentStats[3]
            "Pay: $pay\nOvertime Pay: $overtimePay\nTotal Pay: $totalPay\nTax: $tax".also { resultTV.text = it }
        }

        // Setup navigation to start AboutActivity
        val aboutButton = findViewById<Button>(R.id.aboutButton)
        aboutButton.setOnClickListener {
            val intent = Intent(this, AboutActivity::class.java)
            startActivity(intent)
        }
    }
}

fun calcTax(hoursWorked: Double, hourlyRate: Double, taxRate: Double): List<Double> {
    val pay: Double
    val overtimePay: Double
    if (hoursWorked <= 40.0) {
        pay = hoursWorked * hourlyRate
        overtimePay = 0.0
    } else {
        pay = 40.0 * hourlyRate
        overtimePay = (hoursWorked - 40.0) * hourlyRate * 1.5
    }
    val totalPay = pay + overtimePay
    val tax = pay * taxRate
    return listOf(pay, overtimePay, totalPay, tax)
}
