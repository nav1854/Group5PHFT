package com.example.group5phft.ui.workoutplan

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.group5phft.DatabaseHelper

class WorkoutPlansViewModel(application: Application) : AndroidViewModel(application) {

    private val _workoutPlans = MutableLiveData<List<Map<String, Any>>>()
    val workoutPlans: LiveData<List<Map<String, Any>>> get() = _workoutPlans

    private val dbHelper = DatabaseHelper(application)

    // Fetch workout plans and update LiveData
    fun fetchWorkoutPlans(): LiveData<List<Map<String, Any>>> {
        _workoutPlans.value = dbHelper.getAllWorkoutPlans() // Get data from DB
        return workoutPlans
    }
}
