package com.example.group5phft.ui.trainer


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.group5phft.databinding.FragmentTrainerReviewBinding

class TrainerReviewFragment : Fragment() {

    private var _binding: FragmentTrainerReviewBinding? = null
    private val binding get() = _binding!!
    private val viewModel: TrainerReviewViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTrainerReviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSubmitReview.setOnClickListener {
            val trainerName = binding.editTrainerName.text.toString()
            val rating = binding.ratingBar.rating.toInt()
            val review = binding.editReview.text.toString()

            if (trainerName.isNotEmpty() && review.isNotEmpty()) {
                viewModel.submitReview(trainerName, rating, review)
            } else {
                Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }

        // Observe ViewModel for submission status
        viewModel.reviewSubmissionStatus.observe(viewLifecycleOwner) { success ->
            if (success) {
                Toast.makeText(requireContext(), "Review Submitted Successfully", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Error submitting review", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}