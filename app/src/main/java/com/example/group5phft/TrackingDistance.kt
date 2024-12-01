package com.example.group5phft

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.widget.Button
import android.widget.Chronometer
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlin.math.absoluteValue

class TrackingDistance(context: Context) : AppCompatActivity() {
    // Distance is recorded by location, without taking steps into account
    // If anyone saw this before any kind of update, this isn't done.

    private lateinit var dbHelper : DatabaseHelper

    private val locationManager: LocationManager =
        context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    private val locationListener: LocationListener? = null
    private var previousLocation: Location? = null

    private var calories = 0
    private var distanceKm = 0f
    private var distanceMi = 0f
    private var heartRate = 0
    private var started = false
    private var seconds = 0

    // True = Miles, False = Kilometers
    private var mode = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tracker)
        dbHelper = DatabaseHelper(this)

        val chrono = findViewById<Chronometer>(R.id.time)
        val startBtn = findViewById<Button>(R.id.startButton)
        val metricBtn = findViewById<Button>(R.id.Switch)
        val endBtn = findViewById<Button>(R.id.endButton)
        val distanceVal = findViewById<TextView>(R.id.distanceVal)
        val metric = findViewById<TextView>(R.id.metric)
        var steps = findViewById<TextView>(R.id.stepVal)

        steps.text = "-"
        var totalTime = 0L


        startBtn.setOnClickListener {
            if (!started) {
                chrono.base = SystemClock.elapsedRealtime() + totalTime
                chrono.start()
                started = true
                startBtn.text = "Pause"
            } else {
                totalTime = chrono.base - SystemClock.elapsedRealtime()
                chrono.stop()
                started = false
                startBtn.text = "Start"
            }
        }

        metricBtn.setOnClickListener {
            if (mode) {
                mode = false
                metric.text = "Km"
                distanceVal.text = "$distanceKm"
            } else {
                mode = true
                metric.text = "Mi"
                distanceVal.text = "$distanceMi"
            }
        }

        endBtn.setOnClickListener {
            totalTime = chrono.base - SystemClock.elapsedRealtime()
            chrono.stop()
            val finalStr = getFinalTime(totalTime.absoluteValue)

            val success = dbHelper.insertActivity(finalStr, distanceMi.toString(), steps.toString(), calories.toString(), heartRate.toString())
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
        calories = seconds / 9
        return calories.toString()
    }

    private fun runTimer() {
        val calorieVal = findViewById<TextView>(R.id.calorieVal)
        val distanceVal = findViewById<TextView>(R.id.distanceVal)

        val handler = Handler(Looper.getMainLooper())
        handler.post(object : Runnable {
            override fun run() {
                if (started) {
                    seconds++
                    calorieVal.text = updateCalories()

                    if (mode) distanceVal.text = "$distanceMi"
                    else distanceVal.text = "$distanceKm"
                }
                handler.postDelayed(this, 1000)
            }
        })
    }
}