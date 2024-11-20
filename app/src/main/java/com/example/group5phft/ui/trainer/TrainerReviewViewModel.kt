package com.example.group5phft.ui.trainer

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.group5phft.DatabaseHelper

class TrainerReviewViewModel(application: Application) : AndroidViewModel(application) {

    private val dbHelper = DatabaseHelper(application)

    private val _reviewSubmissionStatus = MutableLiveData<Boolean>()
    val reviewSubmissionStatus: LiveData<Boolean> get() = _reviewSubmissionStatus

    fun submitReview(trainerName: String, rating: Int, review: String) {
        val success = dbHelper.insertWorkoutPlan(
            title = trainerName,
            duration = "Rating: $rating",
            sets = null,
            description = review
        )
        _reviewSubmissionStatus.value = success
    }
}