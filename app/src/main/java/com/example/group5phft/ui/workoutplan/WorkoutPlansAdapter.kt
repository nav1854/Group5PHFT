package com.example.group5phft.ui.workoutplan

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.group5phft.databinding.ListItemWorkoutPlanBinding
import com.example.group5phft.DatabaseHelper

class WorkoutPlansAdapter(private val onDeleteClick: (Map<String, Any>) -> Unit) :
    ListAdapter<Map<String, Any>, WorkoutPlansAdapter.WorkoutPlanViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkoutPlanViewHolder {
        val binding = ListItemWorkoutPlanBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WorkoutPlanViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WorkoutPlanViewHolder, position: Int) {
        val plan = getItem(position)
        holder.bind(plan)
    }

    inner class WorkoutPlanViewHolder(private val binding: ListItemWorkoutPlanBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(plan: Map<String, Any>) {
            // Bind title
            binding.tvPlanTitle.text = plan[DatabaseHelper.COLUMN_PLAN_TITLE] as? String ?: "Untitled Plan"

            // Bind duration
            binding.tvPlanDuration.text = plan[DatabaseHelper.COLUMN_PLAN_DURATION] as? String ?: "No Duration"

            // Bind description (updated to handle null or missing values)
            binding.tvPlanDescription.text = plan[DatabaseHelper.COLUMN_PLAN_DESCRIPTION] as? String ?: "No Description"

            // Set the delete button click listener
            binding.btnDeletePlan.setOnClickListener {
                onDeleteClick(plan)  // Trigger the delete function passed as a lambda
            }
        }
    }

    // DiffCallback for ListAdapter
    class DiffCallback : DiffUtil.ItemCallback<Map<String, Any>>() {
        override fun areItemsTheSame(oldItem: Map<String, Any>, newItem: Map<String, Any>): Boolean {
            // Check by unique ID
            return oldItem[DatabaseHelper.COLUMN_PLAN_ID] == newItem[DatabaseHelper.COLUMN_PLAN_ID]
        }

        override fun areContentsTheSame(oldItem: Map<String, Any>, newItem: Map<String, Any>): Boolean {
            // Check if the entire content is identical
            return oldItem == newItem
        }
    }
}