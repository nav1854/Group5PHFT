package com.example.group5phft.ui.workoutplan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.group5phft.databinding.FragmentWorkoutPlansBinding
import android.content.Intent
import com.example.group5phft.NewPlanActivity
import com.example.group5phft.DatabaseHelper
import androidx.lifecycle.ViewModelProvider
import android.widget.Toast

class WorkoutPlansFragment : Fragment() {

    private lateinit var binding: FragmentWorkoutPlansBinding
    private lateinit var workoutPlansViewModel: WorkoutPlansViewModel
    private lateinit var adapter: WorkoutPlansAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWorkoutPlansBinding.inflate(inflater, container, false)

        // Initialize the ViewModel
        workoutPlansViewModel = ViewModelProvider(this).get(WorkoutPlansViewModel::class.java)

        // Initialize the adapter with an onClick lambda function
        adapter = WorkoutPlansAdapter { plan ->
            // Handle delete click event
            deletePlan(plan)
        }

        // Set up RecyclerView
        binding.recyclerViewWorkoutPlans.layoutManager = LinearLayoutManager(context)
        binding.recyclerViewWorkoutPlans.adapter = adapter

        // Fetch workout plans from the database and submit them to the adapter
        workoutPlansViewModel.fetchWorkoutPlans().observe(viewLifecycleOwner, Observer { plans ->
            adapter.submitList(plans)
        })

        // Button to create a new plan
        binding.btnCreateNewPlan.setOnClickListener {
            val intent = Intent(activity, NewPlanActivity::class.java)
            startActivity(intent)
        }

        return binding.root
    }

    // Delete workout plan function
    private fun deletePlan(plan: Map<String, Any>) {
        val planId = plan[DatabaseHelper.COLUMN_PLAN_ID] as Int
        val dbHelper = DatabaseHelper(requireContext())
        val success = dbHelper.deleteWorkoutPlan(planId)

        if (success) {
            // Show success message
            Toast.makeText(context, "Plan deleted successfully", Toast.LENGTH_SHORT).show()

            // Fetch the updated list of plans from the database
            workoutPlansViewModel.fetchWorkoutPlans().observe(viewLifecycleOwner, Observer { plans ->
                // Update the RecyclerView with the new list of plans
                adapter.submitList(plans)  // This will trigger RecyclerView to refresh
            })
        } else {
            Toast.makeText(context, "Failed to delete plan", Toast.LENGTH_SHORT).show()
        }
    }
}