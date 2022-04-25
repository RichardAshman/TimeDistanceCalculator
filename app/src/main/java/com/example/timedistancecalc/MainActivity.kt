package com.example.timedistancecalc

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import android.widget.TextView
import java.time.Duration

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.calcButton).setOnClickListener { calculate() }
        findViewById<Button>(R.id.clearButton).setOnClickListener { clear() }
        findViewById<Switch>(R.id.timeUnitSwitch).setOnCheckedChangeListener { _, checked ->
            findViewById<TextView>(R.id.timeTextView).text = "Time (${if (checked) "m" else "h"})"
        }
    }

    private fun calculate() {
        val distanceField: EditText = findViewById(R.id.editDistanceTextNumberDecimal)
        val speedField: EditText = findViewById(R.id.editSpeedTextNumberDecimal)
        val timeField: EditText = findViewById(R.id.editTimeTextNumberDecimal)

        val timeInMinutes = findViewById<Switch>(R.id.timeUnitSwitch).isChecked
        val distanceInput = distanceField.text.toString().toDoubleOrNull()
        val speedInput = speedField.text.toString().toDoubleOrNull()
        val timeInput = timeField.text.toString().toDoubleOrNull()

        when {
            distanceInput != null && speedInput != null && timeInput == null -> {
                val hours = distanceInput / speedInput
                val time = Duration.ofSeconds((hours * 60 * 60).toLong())
                timeField.setText(humanReadableDuration(time))
            }
            speedInput != null && timeInput != null && distanceInput == null -> {
                val distance = speedInput * (timeInput / if (timeInMinutes) 60 else 1)
                distanceField.setText(distance.toString())
            }
            distanceInput != null && timeInput != null && speedInput == null -> {
                val speed = distanceInput / (timeInput / if (timeInMinutes) 60 else 1)
                speedField.setText(speed.toString())
            }
            else -> {
                clear()
            }
        }
    }

    private fun clear() {
        val distanceField: EditText = findViewById(R.id.editDistanceTextNumberDecimal)
        val speedField: EditText = findViewById(R.id.editSpeedTextNumberDecimal)
        val timeField: EditText = findViewById(R.id.editTimeTextNumberDecimal)
        distanceField.setText("")
        speedField.setText("")
        timeField.setText("")
    }

    private fun humanReadableDuration(duration: Duration): String = duration.toString().substring(2)
        .lowercase().replace(Regex("[hms](?!\$)")) { "${it.value} " }
}