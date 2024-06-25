package com.application.rehabease.user

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.application.common.ActivityUtils
import com.application.customization.CustomSpinner
import com.application.customization.DialogActivity
import com.application.models.InjuryModel
import com.application.rehabease.R
import com.application.service.InjuriesService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TreatmentDetailsActivityUser : AppCompatActivity(), CustomSpinner.OnSpinnerEventsListener {
    private var injuriesService: InjuriesService = InjuriesService()
    private var injuryModels: ArrayList<InjuryModel> = ArrayList()
    private lateinit var spinner: CustomSpinner
    private lateinit var adapter: ArrayAdapter<CharSequence>
    private lateinit var displayDescriptionImageView: ImageView
    private lateinit var ageEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_treatment_details_user)
        ActivityUtils.actionBarSetup(this)

        spinner = findViewById(R.id.injuries)
        spinner.setSpinnerEventsListener(this)
        setupInjuryModels()

        displayDescriptionImageView = findViewById(R.id.more_info_image)
        displayDescriptionImageView.setOnClickListener { displayInjury() }

        ageEditText = findViewById(R.id.editTextNumber)
        val calculateRecoveryTimeButton: Button = findViewById(R.id.calculate_recovery_time_button)
        calculateRecoveryTimeButton.setOnClickListener {
            val selectedPosition = spinner.selectedItemPosition
            val selectedInjury = injuryModels[selectedPosition].getName()
            val age = ageEditText.text.toString().toInt()
            val intent = Intent(this, TreatmentDetailsResultActivity::class.java)
            intent.putExtra("selected_injury", selectedInjury)
            intent.putExtra("age", age)
            startActivity(intent)
        }
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