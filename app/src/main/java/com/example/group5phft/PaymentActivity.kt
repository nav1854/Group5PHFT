package com.example.group5phft

import android.app.Activity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

class PaymentActivity : Activity() {

    private lateinit var cardNumberInput: EditText
    private lateinit var expirationDateInput: EditText
    private lateinit var cvvInput: EditText
    private lateinit var currentPlanTextView: TextView // TextView to show the current subscription plan

    private var selectedPlan: String = "None" // Default to "None"
    private var userEmail: String? = null // Email to identify the user

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

        // Initialize UI components
        cardNumberInput = findViewById(R.id.input_card_number)
        expirationDateInput = findViewById(R.id.input_expiration_date)
        cvvInput = findViewById(R.id.input_cvv)
        currentPlanTextView = findViewById(R.id.text_current_plan)
        val saveSubscriptionButton: Button = findViewById(R.id.btn_save_subscription)

        // Retrieve the selected plan and user email from the previous activity
        selectedPlan = intent.getStringExtra("SELECTED_PLAN") ?: "None"
        userEmail = intent.getStringExtra("EMAIL")

        // Display the selected plan
        updateCurrentPlanText()

        saveSubscriptionButton.setOnClickListener {
            val cardNumber = cardNumberInput.text.toString()
            val expirationDate = expirationDateInput.text.toString()
            val cvv = cvvInput.text.toString()

            when {
                !isValidCardNumber(cardNumber) -> {
                    Toast.makeText(this, "Card number must be 16 digits", Toast.LENGTH_SHORT).show()
                }
                !isValidExpirationDate(expirationDate) -> {
                    Toast.makeText(this, "Expiration date must be MMYY (e.g., 1225)", Toast.LENGTH_SHORT).show()
                }
                !isValidCvv(cvv) -> {
                    Toast.makeText(this, "CVV must be 3 digits", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    val dbHelper = DatabaseHelper(this)
                    val success = dbHelper.updateSubscription(userEmail ?: "", selectedPlan)
                    if (success) {
                        Toast.makeText(this, "Subscription Saved!", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Toast.makeText(this, "Failed to save subscription.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    /**
     * Updates the current plan TextView dynamically.
     */
    private fun updateCurrentPlanText() {
        currentPlanTextView.text = "Current Plan: $selectedPlan"
    }

    private fun isValidCardNumber(cardNumber: String): Boolean {
        return cardNumber.length == 16 && cardNumber.all { it.isDigit() }
    }

    private fun isValidExpirationDate(expirationDate: String): Boolean {
        return expirationDate.length == 4 && expirationDate.all { it.isDigit() }
    }

    private fun isValidCvv(cvv: String): Boolean {
        return cvv.length == 3 && cvv.all { it.isDigit() }
    }
}