package com.example.group5phft

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.os.SystemClock
import android.widget.Button
import android.widget.Chronometer
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import android.hardware.SensorManager
import android.os.Handler
import android.os.Looper
import android.widget.TextView
import java.util.Locale
import kotlin.math.absoluteValue

class TrackingActivity : AppCompatActivity(), SensorEventListener {
    // Every given metric is recorded here, with distance being affected by steps

    private lateinit var dbHelper : DatabaseHelper
    private var sensorManager : SensorManager? = null

    private var calories = 0
    private var distanceKm = 0f
    private var distanceMi = 0f
    private var steps = 0
    private var heartRate = 0
    private var started = false
    private var seconds = 0

    // True = Miles, False = Kilometers
    private var mode = true

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tracker)
        dbHelper = DatabaseHelper(this)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

        val chrono = findViewById<Chronometer>(R.id.time)
        val startBtn = findViewById<Button>(R.id.startButton)
        val metricBtn = findViewById<Button>(R.id.Switch)
        val endBtn = findViewById<Button>(R.id.endButton)
        val distanceVal = findViewById<TextView>(R.id.distanceVal)
        val metric = findViewById<TextView>(R.id.metric)

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
                distanceVal.text = String.format(Locale.getDefault(), "%.2f", distanceKm)
            } else {
                mode = true
                metric.text = "Mi"
                distanceVal.text = String.format(Locale.getDefault(), "%.2f", distanceMi)
            }
        }

        endBtn.setOnClickListener {
            totalTime = chrono.base - SystemClock.elapsedRealtime()
            chrono.stop()
            val finalStr = getFinalTime(totalTime.absoluteValue)
            val fCalories = calories.toString()
            val fDistance = if (mode) String.format(Locale.getDefault(), "%.2f Mi", distanceMi)
            else String.format(Locale.getDefault(), "%.2f Km", distanceKm)
            val fSteps = steps.toString()

            val success = dbHelper.insertActivity(finalStr, fDistance, fSteps, fCalories, heartRate.toString())
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

    override fun onResume() {
        super.onResume()
        val stepSensor : Sensor? = sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
        if (stepSensor == null) {
            Toast.makeText(this, "No step sensor found.", Toast.LENGTH_SHORT).show()
        } else {
            sensorManager?.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_UI)
        }
    }

    override fun onSensorChanged(p0: SensorEvent?) {
        if (started) {
            val total = p0!!.values[0]
            steps = total.toInt()
        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
        TODO("Not yet implemented")
    }

    fun getDistanceKm(steps: Long) {
        // Get the distance in Kilometers
        distanceKm = ( steps * 78 ) / 100000f
    }

    fun getDistanceMi(steps: Long) {
        // Get the distance in Miles
        distanceMi = steps / 2068f
    }

    fun updateCalories() : String {
        calories = seconds / 9
        return calories.toString()
    }

    private fun runTimer() {
        val calorieVal = findViewById<TextView>(R.id.calorieVal)
        val step = findViewById<TextView>(R.id.stepVal)
        val distanceVal = findViewById<TextView>(R.id.distanceVal)

        val handler = Handler(Looper.getMainLooper())
        handler.post(object : Runnable {
            override fun run() {
                if (started) {
                    seconds++
                    calorieVal.text = updateCalories()
                    getDistanceKm(steps.toLong())
                    getDistanceMi(steps.toLong())
                    step.text = "$steps"

                    if (mode) distanceVal.text = String.format(Locale.getDefault(), "%.2f", distanceMi)
                    else distanceVal.text = String.format(Locale.getDefault(),"%.2f", distanceKm)
                }
                handler.postDelayed(this, 1000)
            }
        })
    }

}