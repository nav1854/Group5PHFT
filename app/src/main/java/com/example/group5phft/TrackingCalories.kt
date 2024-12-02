package com.example.group5phft

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.widget.Button
import android.widget.Chronometer
import android.widget.TextView
import android.widget.Toast
import kotlin.math.absoluteValue


class TrackingCalories : AppCompatActivity() {
    // Only tracks calories, not distance or steps
    private var started = false
    private lateinit var dbHelper : DatabaseHelper

    private var seconds = 0
    private var calories = 0

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tracker)
        dbHelper = DatabaseHelper(this)

        val chrono = findViewById<Chronometer>(R.id.time)
        val startBtn = findViewById<Button>(R.id.startButton)
        val endBtn = findViewById<Button>(R.id.endButton)

        val distances = findViewById<TextView>(R.id.distanceVal)
        val metrics = findViewById<TextView>(R.id.metric)
        val steps = findViewById<TextView>(R.id.stepVal)

        distances.text = "-"
        metrics.text = " "
        steps.text = "-"

        var totalTime = 0L

        startBtn.setOnClickListener {
            if (!started) {
                chrono.base = SystemClock.elapsedRealtime() + totalTime
                chrono.start()
                started = true
                startBtn.text = "Pause"
            }
            else {
                totalTime = chrono.base - SystemClock.elapsedRealtime()
                chrono.stop()
                started = false
                startBtn.text = "Start"
            }
        }

        endBtn.setOnClickListener {
            totalTime = chrono.base - SystemClock.elapsedRealtime()
            chrono.stop()
            val finalStr = getFinalTime(totalTime.absoluteValue)
            val fCalories = calories.toString()

            val success = dbHelper.insertActivity(finalStr, " ", " ", fCalories, " ")
            if (success) {
                Toast.makeText(this, "Activity complete!", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Failed to save.", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        }

        runTimer()
    }

    private fun getFinalTime(totalTime : Long) : String {
        var seconds = totalTime.toInt() / 1000
        var minutes = seconds / 60
        val hours = minutes / 60

        seconds -= minutes * 60
        minutes -= (hours * 60)

        var strSec =  seconds.toString()
        var strMin = minutes.toString()
        var strHr = hours.toString()

        if (strSec.toInt() < 10) strSec = "0$strSec"
        if (strMin.toInt() < 10) strMin = "0$strMin"
        if (strHr.toInt() < 10) strHr = "0$strHr"
        return "$strHr:$strMin:$strSec"
    }

    fun updateCalories() : String {
        calories = seconds / 7
        return calories.toString()
    }


    private fun runTimer() {
        val calorieVal = findViewById<TextView>(R.id.calorieVal)
        val handler = Handler(Looper.getMainLooper())
        handler.post(object : Runnable {
            override fun run() {

                if (started) {
                    seconds++
                    calorieVal.text = updateCalories()
                }
                handler.postDelayed(this, 1000)
            }
        })
    }
}