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
            binding.tvPlanTitle.text = plan[DatabaseHelper.COLUMN_PLAN_TITLE] as String
            binding.tvPlanDuration.text = plan[DatabaseHelper.COLUMN_PLAN_DURATION] as String
            // Bind other views here

            // Set the delete button click listener
            binding.btnDeletePlan.setOnClickListener {
                onDeleteClick(plan)  // Trigger the delete function passed as a lambda
            }
        }
    }

    // DiffCallback for ListAdapter
    class DiffCallback : DiffUtil.ItemCallback<Map<String, Any>>() {
        override fun areItemsTheSame(oldItem: Map<String, Any>, newItem: Map<String, Any>): Boolean {
            return oldItem[DatabaseHelper.COLUMN_PLAN_ID] == newItem[DatabaseHelper.COLUMN_PLAN_ID]
        }

        override fun areContentsTheSame(oldItem: Map<String, Any>, newItem: Map<String, Any>): Boolean {
            return oldItem == newItem
        }
    }
}