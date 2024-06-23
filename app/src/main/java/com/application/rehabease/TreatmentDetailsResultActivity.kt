package com.application.rehabease

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.application.common.ActivityUtils
import com.application.customization.CustomSpinner
import com.application.rehabease.openAIIntegration.TreatmentTimeViewModel
import com.google.firebase.firestore.FirebaseFirestore

class TreatmentDetailsResultActivity : AppCompatActivity(), CustomSpinner.OnSpinnerEventsListener {
    private lateinit var spinner: CustomSpinner
    private lateinit var adapter: ArrayAdapter<CharSequence>
    private lateinit var calculateButton: Button
    private val treatmentTimeViewModel: TreatmentTimeViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_treatment_details_result)
        ActivityUtils.actionBarSetup(this)
        val TreatmentTimeTextView: TextView = findViewById(R.id.treatment_time_text)
        treatmentTimeViewModel.TreatmentLiveData.observe(this, Observer { tip ->
            TreatmentTimeTextView.text = tip
        })
        treatmentTimeViewModel.fetchTreatmentTime()
        calculateButton = findViewById(R.id.calculate_recovery_time_button)
        spinner = findViewById(R.id.injuries)
        spinner.setSpinnerEventsListener(this)
        fetchInjuries()
    }

    private fun fetchInjuries() {
        val db = FirebaseFirestore.getInstance()
        db.collection("injuries")
            .get()
            .addOnSuccessListener { result ->
                val injuryNames = mutableListOf<String>()
                for (document in result) {
                    val name = document.getString("name")
                    if (name != null) {
                        injuryNames.add(name)
                    }
                }
                setupSpinner(injuryNames)
            }
            .addOnFailureListener { exception ->
                Log.w("TreatmentDetailsActivity", "Error getting documents: ", exception)
            }
    }

    private fun setupSpinner(injuryNames: List<String>) {
        adapter = ArrayAdapter(this, R.layout.my_selected_item, injuryNames)
        adapter.setDropDownViewResource(R.layout.simple_spinner_item)
        spinner.adapter = adapter
    }

    override fun onPopupWindowOpened(spinner: CustomSpinner) {
        spinner.background = getDrawable(R.drawable.bg_spinner_arrow_up)
    }

    override fun onPopupWindowClosed(spinner: CustomSpinner) {
        spinner.background = getDrawable(R.drawable.bg_spinner_arrow_down)
    }
}