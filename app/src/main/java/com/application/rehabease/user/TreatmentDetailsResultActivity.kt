package com.application.rehabease.user

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.application.common.ActivityUtils
import com.application.customization.CustomSpinner
import com.application.customization.DialogActivity
import com.application.models.InjuryModel
import com.application.rehabease.R
import com.application.rehabease.ServiceOpenAIAPI.TreatmentTimeViewModel
import com.application.service.InjuriesService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TreatmentDetailsResultActivity : AppCompatActivity(), CustomSpinner.OnSpinnerEventsListener {
    private var injuriesService: InjuriesService = InjuriesService()
    private var injuryModels: ArrayList<InjuryModel> = ArrayList()
    private lateinit var displayDescriptionImageView: ImageView
    private lateinit var spinner: CustomSpinner
    private lateinit var adapter: ArrayAdapter<CharSequence>
    private lateinit var calculateButton: Button
    private val treatmentTimeViewModel: TreatmentTimeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_treatment_details_user_result)
        ActivityUtils.actionBarSetup(this)

        val treatmentTimeTextView: TextView = findViewById(R.id.treatment_time_text)
        treatmentTimeViewModel.TreatmentLiveData.observe(this, Observer { tip ->
            treatmentTimeTextView.text = tip
        })

        val selectedInjury = intent.getStringExtra("selected_injury") ?: ""
        val age = intent.getIntExtra("age", 0)
        treatmentTimeViewModel.fetchTreatmentTime(selectedInjury, age)

        calculateButton = findViewById(R.id.calculate_recovery_time_button)
        spinner = findViewById(R.id.injuries)
        spinner.setSpinnerEventsListener(this)
        setupInjuryModels()

        displayDescriptionImageView = findViewById(R.id.more_info_image)
        displayDescriptionImageView.setOnClickListener { displayInjury() }
    }

    private fun setupInjuryModels() {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val names = injuriesService.fetchInjuryDetails("name")
                val descriptions = injuriesService.fetchInjuryDetails("description")
                val newInjuryModels = ArrayList<InjuryModel>()
                for (i in names.indices) {
                    newInjuryModels.add(InjuryModel(names[i], descriptions[i]))
                }
                setupSpinner(names)
                injuryModels.clear()
                injuryModels.addAll(newInjuryModels)
            } catch (e: Exception) {
                e.printStackTrace()
            }
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

    private fun displayInjury() {
        val dialog = DialogActivity(this, R.layout.dialog_injury_description)
        val view = dialog.getDialog()
        val injuryNameTextView = view.findViewById<TextView>(R.id.injury_name_text)
        val injuryDescriptionTextView = view.findViewById<TextView>(R.id.injury_description_text)
        val selectedPosition = spinner.selectedItemPosition
        injuryNameTextView.text = injuryModels[selectedPosition].getName()
        injuryDescriptionTextView.text = injuryModels[selectedPosition].getDescription()
        val returnBackButton = view.findViewById<Button>(R.id.legend_button)
        returnBackButton.setOnClickListener { dialog.closeDialog() }
    }
}