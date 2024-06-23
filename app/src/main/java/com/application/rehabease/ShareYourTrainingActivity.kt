package com.application.rehabease

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.NumberPicker
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.Calendar

class ShareYourTrainingActivity : AppCompatActivity() {
    private lateinit var openMenu: ImageView
    private lateinit var dateText: TextView
    private lateinit var durationText: TextView
    @SuppressLint("SetTextI18n", "MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_share_your_training)
        openMenu = findViewById(R.id.image_bars)
        openMenu.setOnClickListener {
            val intent = Intent(this@ShareYourTrainingActivity, MenuActivity::class.java)
            startActivity(intent)
        }
        dateText = findViewById(R.id.date_of_training_input)
        dateText.setOnClickListener {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)
            val datePickerDialog = DatePickerDialog(
                this, { _, year, monthOfYear, dayOfMonth ->
                    dateText.text = (dayOfMonth.toString() + "/" + (monthOfYear + 1) + "/" + year)
                }, year, month, day
            )
            datePickerDialog.show()
        }
        durationText = findViewById(R.id.duration_of_training_input)
        durationText.setOnClickListener {
            val inflater = layoutInflater
            val dialogLayout = inflater.inflate(R.layout.dialog_time_picker, null)
            val minutePicker = dialogLayout.findViewById<NumberPicker>(R.id.minute_picker)
            val secondPicker = dialogLayout.findViewById<NumberPicker>(R.id.second_picker)

            minutePicker.minValue = 0
            minutePicker.maxValue = 59
            secondPicker.minValue = 0
            secondPicker.maxValue = 59

            val currentMinute = 0
            val currentSecond = 0
            minutePicker.value = currentMinute
            secondPicker.value = currentSecond

            val dialog = AlertDialog.Builder(this)
                .setView(dialogLayout)
                .setPositiveButton("OK") { _, _ ->
                    val selectedMinute = minutePicker.value
                    val selectedSecond = secondPicker.value
                    durationText.text = String.format("%02d:%02d", selectedMinute, selectedSecond)
                }
                .setNegativeButton("Cancel", null)
                .create()
            dialog.show()
        }

    }
}