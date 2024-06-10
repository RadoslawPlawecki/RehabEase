package com.application.rehabease

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.application.customization.CustomSpinner
import com.google.firebase.firestore.FirebaseFirestore

class TreatmentDetailsResultActivity : AppCompatActivity(), CustomSpinner.OnSpinnerEventsListener {
    private lateinit var openMenu: ImageView
    private lateinit var spinner: CustomSpinner
    private lateinit var adapter: ArrayAdapter<CharSequence>
    private lateinit var calculateButton: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_treatment_details_result)
        openMenu = findViewById(R.id.image_bars)
        calculateButton = findViewById(R.id.calculate_recovery_time_button)
        openMenu.setOnClickListener {
            val intent = Intent(this@TreatmentDetailsResultActivity, MenuActivity::class.java)
            startActivity(intent)
        }
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
        spinner.background = getDrawable(R.drawable.spinner_bg_arrow_up)
    }

    override fun onPopupWindowClosed(spinner: CustomSpinner) {
        spinner.background = getDrawable(R.drawable.spinner_bg_arrow_down)
    }
}