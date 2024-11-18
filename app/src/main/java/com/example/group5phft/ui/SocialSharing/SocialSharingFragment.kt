package com.example.group5phft.ui.SocialSharing

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.group5phft.R

class SocialSharingFragment : Fragment() {

    // Using activityViewModels() to share the ViewModel between Fragment and Activity
    private val socialSharingViewModel: SocialSharingViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = inflater.inflate(R.layout.activity_social_sharing, container, false)

        // Button to share the content
        val btnShare: Button = binding.findViewById(R.id.btnShare)
        btnShare.setOnClickListener {
            // Get the message from the ViewModel
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_TEXT, socialSharingViewModel.shareMessage)

            // Start the sharing intent
            startActivity(Intent.createChooser(shareIntent, "Share via"))
        }

        return binding
    }
}