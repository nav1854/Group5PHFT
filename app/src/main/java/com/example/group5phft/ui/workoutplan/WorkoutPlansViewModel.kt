package com.example.group5phft.ui.workoutplan

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.group5phft.DatabaseHelper

class WorkoutPlansViewModel(application: Application) : AndroidViewModel(application) {

    private val databaseHelper = DatabaseHelper(application)
    private val workoutPlans = MutableLiveData<List<Map<String, Any>>>()

    fun fetchWorkoutPlans(): LiveData<List<Map<String, Any>>> {
        workoutPlans.value = databaseHelper.getAllWorkoutPlans()
        return workoutPlans
    }

    fun deleteWorkoutPlan(plan: Map<String, Any>) {
        val id = plan[DatabaseHelper.COLUMN_PLAN_ID] as Int
        databaseHelper.deleteWorkoutPlan(id)
        fetchWorkoutPlans()  // Refresh list after deletion
    }
}
