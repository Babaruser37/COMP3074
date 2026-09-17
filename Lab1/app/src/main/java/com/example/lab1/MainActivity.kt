package com.example.lab1

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val message = findViewById<TextView>(R.id.textViewMessage)
        val button = findViewById<Button>(R.id.buttonChangeText)

        button.setOnClickListener {
            message.text = "Button Clicked!"
        }
    }
}
