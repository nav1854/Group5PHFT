package com.example.group5phft.ui.subscription

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.group5phft.DatabaseHelper
import com.example.group5phft.R
import android.content.Intent
import com.example.group5phft.PaymentActivity
import androidx.lifecycle.ViewModelProvider

class SubscriptionFragment : Fragment() {

    private lateinit var currentPlanTextView: TextView
    private lateinit var subscriptionInfoTextView: TextView
    private lateinit var btnMonthly: Button
    private lateinit var btnThreeMonths: Button
    private lateinit var btnYearly: Button
    private lateinit var btnContinuePayment: Button
    private lateinit var chooseSubscriptionText: TextView
    private lateinit var thankyouText: TextView

    private var userEmail: String? = "nv1854@gmail.com" // Replace with logic to fetch logged-in user's email

    private lateinit var viewModel: SubscriptionViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_subscriptions, container, false)

        // Initialize views
        currentPlanTextView = rootView.findViewById(R.id.text_current_plan)
        subscriptionInfoTextView = rootView.findViewById(R.id.subscription_info)
        btnMonthly = rootView.findViewById(R.id.btn_monthly)
        btnThreeMonths = rootView.findViewById(R.id.btn_three_months)
        btnYearly = rootView.findViewById(R.id.btn_yearly)
        btnContinuePayment = rootView.findViewById(R.id.btn_continue_payment)
        chooseSubscriptionText = rootView.findViewById(R.id.subscription_title)
        thankyouText = rootView.findViewById(R.id.thank_you)

        viewModel = ViewModelProvider(this).get(SubscriptionViewModel::class.java)

        // Check subscription status
        checkSubscriptionStatus()

        // Observe the selected plan in ViewModel
        //look into
        viewModel.selectedPlan.observe(viewLifecycleOwner) { selectedPlan ->
            currentPlanTextView.text = "Current Plan: $selectedPlan"
            btnContinuePayment.isEnabled = selectedPlan != "None" // Only enable the button if a plan is selected
        }

        // Set click listeners for subscription buttons
        btnMonthly.setOnClickListener {
            viewModel.selectPlan("Monthly - $10")
        }

        btnThreeMonths.setOnClickListener {
            viewModel.selectPlan("3 Months - $25")
        }

        btnYearly.setOnClickListener {
            viewModel.selectPlan("Yearly - $60")
        }

        btnContinuePayment.setOnClickListener {
            val selectedPlan = viewModel.selectedPlan.value
            if (selectedPlan != "None") {
                navigateToPaymentPage(selectedPlan ?: "None")
            }
        }

        return rootView
    }

    //once page is reloaded
    override fun onResume() {
        super.onResume()
        // Re-check subscription status when the fragment is resumed
        checkSubscriptionStatus()
    }

    private fun checkSubscriptionStatus() {
        val dbHelper = DatabaseHelper(requireContext())
        val subscription = dbHelper.getUserSubscription(userEmail)

        if (!subscription.isNullOrEmpty()) {
            // User is already subscribed
            subscriptionInfoTextView.text = "You are already subscribed to: $subscription"
            showSubscribedUI()
        } else {
            // User is not subscribed
            subscriptionInfoTextView.text = "Choose a subscription plan."
            showSubscriptionOptions()
        }
    }

    private fun showSubscribedUI() {
        // Hide subscription buttons and show a message
        thankyouText.visibility = View.VISIBLE
        chooseSubscriptionText.visibility = View.GONE
        btnMonthly.visibility = View.GONE
        btnThreeMonths.visibility = View.GONE
        btnYearly.visibility = View.GONE
        btnContinuePayment.visibility = View.GONE
        currentPlanTextView.visibility = View.GONE
        subscriptionInfoTextView.visibility = View.VISIBLE
    }

    private fun showSubscriptionOptions() {
        // Show subscription buttons and options
        thankyouText.visibility = View.GONE
        subscriptionInfoTextView.visibility = View.GONE
        chooseSubscriptionText.visibility = View.VISIBLE
        btnMonthly.visibility = View.VISIBLE
        btnThreeMonths.visibility = View.VISIBLE
        btnYearly.visibility = View.VISIBLE
        btnContinuePayment.visibility = View.VISIBLE
        currentPlanTextView.visibility = View.VISIBLE
    }

    private fun navigateToPaymentPage(selectedPlan: String) {
        // Navigate to payment page with the selected plan
        val intent = Intent(requireContext(), PaymentActivity::class.java).apply {
            putExtra("SELECTED_PLAN", selectedPlan)
            putExtra("EMAIL", userEmail)
        }
        startActivity(intent)
    }
}