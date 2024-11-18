package com.example.group5phft.ui.subscription

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SubscriptionViewModel : ViewModel() {

    // Mutable LiveData to store the selected plan
    private val _selectedPlan = MutableLiveData<String>("None")
    val selectedPlan: LiveData<String> get() = _selectedPlan

    // Method to update the selected plan
    fun selectPlan(plan: String) {
        _selectedPlan.value = plan
    }
}