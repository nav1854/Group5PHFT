package com.example.group5phft.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.group5phft.ActivityCategoriesActivity
import com.example.group5phft.GoalSettingActivity
import com.example.group5phft.R

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        // Button for Activity Categories
        val btnActivityCategories = view.findViewById<Button>(R.id.btnActivityCategories)
        btnActivityCategories.setOnClickListener {
            val intent = Intent(activity, ActivityCategoriesActivity::class.java)
            startActivity(intent)
        }

        // Button for Goal Setting
        val btnGoalSetting = view.findViewById<Button>(R.id.btnGoalSetting)
        btnGoalSetting.setOnClickListener {
            val intent = Intent(activity, GoalSettingActivity::class.java)
            startActivity(intent)
        }

        return view
    }
}